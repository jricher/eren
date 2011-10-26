/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2010. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.scxml.env.AbstractStateMachine;

import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;
import org.mitre.eren.protocol.edxl_rm.DeploymentStatusType;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.mitre.eren.protocol.edxl_rm.ResourceTypes;
import org.mitre.javautil.logging.LoggingUtils;

/**
 *
 * @author JWINSTON
 */
public class Pod extends AbstractStateMachine implements EDXLRMConstants {

    private final PriorityBlockingQueue<DelayedEvent> patientQueue  = new PriorityBlockingQueue<DelayedEvent>();
    private final Object patientQueueLock = new Object();
    private final Object peopleLock = new Object();
    private final LinkedBlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>();
    private long tick;
    private long staffNextDegradeTime;
    private int staffPerformancePenalty = 0;
    private long activationClockTime;
    private long nextRiotTime;
    private ModelManager mm;
    private String podId;
    private String name;
    private DeploymentStatusType status;
    private int population;
    private int numExposed;
    private int throughput;
    private int scaleFactor = 1;
    private int standardOfCare;
    private int standardOfCarePenalty = 0;
    private int medMin;
    private int medCurrent;
    private int secMin;
    private int secCurrent;
    private int supportMin;
    private int supportCurrent;
    private int lastShiftMedical;
    private int lastShiftSecurity;
    private int lastShiftSupport;
    private boolean hasMeds = false;
    private boolean hasEquipment = false;
    private boolean startupTimeExpired = false;
    private boolean shiftTired = false;
    private Random random = new Random();
    private static SCXML peopleStateMachine;
    private ArrayList<Person> people;


    public static final long ACTIVATION_TIME = 2; //hours
    private static final Logger log = LoggingUtils.getLogger(Pod.class);
    private static final URL podStateMachineUrl = Pod.class.getClassLoader().getResource("org/mitre/eren/model/resource/pod.xml");
    private static int podsEnqueing = 0;

    public Pod(SCXML stateMachine, ModelManager mm, String id, String name,
            int population, int numExposed, int soc, int scale) {
        super(stateMachine);
        init(mm, id, name, population, numExposed, soc, scale);
    }

    public Pod(ModelManager mm, String id, String name, 
            int population, int numExposed, int soc, int scale) {
        super(podStateMachineUrl);
        init(mm, id, name, population, numExposed, soc, scale);
    }

    private void init(ModelManager mm, String id, String name,
            int population, int numExposed, int standardOfCare, int scaleFactor) {
        log.log(Level.INFO, "Initialing Pod {0} {1} population={2} exposed={3} stand of care={4} scale={5}",
                new Object[]{id, name, population, numExposed, standardOfCare, scaleFactor});
        this.mm = mm;
        podId = id;
        this.name = name;
        this.population = population;
        this.standardOfCare = standardOfCare;
        this.numExposed = numExposed;
        this.scaleFactor = scaleFactor;

        Thread qt = new Thread("EREN Pod_" + podId + " Event Queue") {
            @Override
            public void run() {
                while (true) {
                    try {
                        Event e = eventQueue.take();
                        treatPerson(e.getPatient());
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        qt.start();
        initPeople(population - numExposed, numExposed);

     }

    // Create people for the pod
    // Shuffle exposed and unexposed in together so that they have an equal
    // chance of getting treated at any given time
    public void initPeople(int numNotExposed, int numExposed) {

        int count = numExposed < numNotExposed ? numNotExposed : numExposed;

        people = new ArrayList<Person>(numExposed + numNotExposed);

        log.info("Pod " + podId + " intializing " + (numExposed + numNotExposed) + " people. (Hang on, this could take a while)");
        // Make a Person to create the state machine, if necessary. Shouldn't affect anything.
        if (peopleStateMachine == null) {
            Person person = new Person(mm);
            peopleStateMachine = person.getEngine().getStateMachine();
        }

        // create two people for every count
        synchronized(peopleLock) {
            for (int i = 0; i < count; i++) {
                if (i < numNotExposed) {
                    Person unExposedPerson = new Person(peopleStateMachine, mm);
                    people.add(unExposedPerson);
                }

                if (i < numExposed) {
                    Person exposedPerson = new Person(peopleStateMachine, mm);
                    exposedPerson.fireEvent("turn.incubational");
                    people.add(exposedPerson);
                }
            }
        }
        log.info("Pod " + podId + " done intializing people");
    }

    // Do we have any staff?
    public boolean hasSomeStaff() {
        int staffTotal = medCurrent + supportCurrent + secCurrent;
        return staffTotal > 0;
    }

    public int getNumExposed() {
        return numExposed;
    }
    
    public void setTick(long tickTime) {
        tick = tickTime;
        fireDelayedEvents();
        if (shiftTired) {
            if (tick > staffNextDegradeTime) {
                if (!replaceStaff()) {
                    staffNextDegradeTime += 60 * 60; // peformance will degrade each hour.
                    staffPerformancePenalty += 2; // performance will degrade by an additional 2%
                    log.info("New staff performance penalty: " + staffPerformancePenalty + "%");
                    // Requeue everyone based on the new penalized throughput
                    setThroughput();
                }
            }
        }

        // Could there be a riot?
        // Once an hour (after the pod has been open for four hours), check
        // to see if these riot conditions exist:
        //  - the people at the end of the queue have a wait of more than 4 hours
        //  - the ratio of people to security is greater than 100:1
        // If these conditions exits, there's a one in 100 chance of a riot happening and shutting down the pod
        if (status.equals(DeploymentStatusType.IN_USE) && nextRiotTime > tick) {
            nextRiotTime = tick + 60 * 60; // 1 hour
            if (patientQueue.size() > 0
                && patientQueue.size()/throughput > 4
                && patientQueue.size()/secCurrent > 100) {
                // One in 500 chance of a riot if we meet these conditions
                if (random.nextInt(500) == 0) {
                    log.info("Riot this time at tick " + tick);
                    fireEvent("riot");
                } else {
                    log.info("No riot this time at tick " + tick);
                }
            }
        }
     }
    
    /**
     * This is a bogus method that should be removed. 
     */
    public void startMedTimer() {
        // !!! Change this !!! Set a timer for meds arrival
        double waitTime = random.nextGaussian() + ACTIVATION_TIME; // hours
        waitTime = waitTime * 60 * 60; // seconds
        mm.queueDelayedEvent("gotMeds", this, (int)waitTime);
    }
    
    public void initPodStaff(String type, int min, int current) {
        if (type.equals(ResourceTypes.CLINICAL_STAFF.toString())) {
            this.medMin = min;
            this.medCurrent = current;
        } else if (type.equals(ResourceTypes.LE_STAFF.toString())) {
            this.secMin = min;
            this.secCurrent = current;
        }  else if (type.equals(ResourceTypes.OPS_STAFF.toString())) {
            this.supportMin = min;
            this.supportCurrent = current;
        }else{
            log.info("No Pod Staff of type " + type);
        }
    }

    public int requestStaff(ResourceTypes staffType, int qty) {

        boolean isOpen = isInState("open");

        // If the shift is overdue for replacement, call replaceStaff() instead
        if (isOpen && shiftTired) {
            int amount = 0;
            replaceStaff();
            if (staffType.equals(ResourceTypes.CLINICAL_STAFF))
                amount = getMedicalStaff();
            else if (staffType.equals(ResourceTypes.OPS_STAFF))
                amount = getSupportStaff();
            else if (staffType.equals(ResourceTypes.LE_STAFF))
                amount = getSecurityStaff();
            return amount;
        }

        int amtGranted = mm.requestResource(staffType, qty);
        if (staffType.equals(ResourceTypes.CLINICAL_STAFF)) {
            medCurrent += amtGranted;
            if (medCurrent >= medMin) {
                if (isOpen){
                    setThroughput();
                } else {
                    fireEvent("gotMedStaff");
                }
            }
                
        } else if (staffType.equals(ResourceTypes.LE_STAFF)) {
            secCurrent += amtGranted;
            if (secCurrent >= secMin)
                fireEvent("gotSecurityStaff");
            if (isInState("riot") && patientQueue.size()/secCurrent < 100) {
                fireEvent("calm");
            }
        }else if (staffType.equals(ResourceTypes.OPS_STAFF)) {
            supportCurrent += amtGranted;
            if (supportCurrent >= supportMin)
                if (isOpen) {
                    setThroughput();
                } else {
                    fireEvent("gotOperationsStaff");
                }
        }
        if (!isOpen) {
            amIReady();
        }
        return amtGranted;
    }

    
    public void resetStaff(int newMed, int newSupport, int newSecurity) {
        if (newMed == medCurrent && newSupport == supportCurrent && newSecurity == secCurrent) {
            log.log(Level.INFO, "Replaced staff with same levels in {0} Medical={1} Support={2} Security={3}", new Object[]{podId, medCurrent, supportCurrent, secCurrent});
        } else {
            medCurrent = newMed;
            supportCurrent = newSupport;
            secCurrent = newSecurity;
            staffPerformancePenalty = 0;
            log.log(Level.INFO, "New staff levels in {0} Medical={1} Support={2} Security={3}", new Object[]{podId, medCurrent, supportCurrent, secCurrent});
            setThroughput();
        }
    }

    /**
     * At the end of a shift, replace the staff. Don't release the current staff
     * for 8 hours, because they shouldn't be available to work. They're too tired.
     * 
     * If they're aren't enough replacement workers in any one category,
     * don't replace any of them and start assessing a throughput penalty.
     *
     */
    private boolean replaceStaff() {
        int newMed = mm.requestResource(ResourceTypes.CLINICAL_STAFF, medCurrent);
        int newSec = mm.requestResource(ResourceTypes.LE_STAFF, secCurrent);
        int newSupport = mm.requestResource(ResourceTypes.OPS_STAFF, supportCurrent);

        if ((newMed < medMin) ||(newSec < secMin) || (newSupport < supportMin)) {

            log.log(Level.INFO, "{0} insufficent staff to replace current shift. Med={1} Secuirty={2} Support={3}",
                    new Object[]{podId, newMed, newSec, newSupport});
            // Not enough staff to replace our current staff, so skip the whole thing
            mm.releaseResource(ResourceTypes.CLINICAL_STAFF, newMed);
            mm.releaseResource(ResourceTypes.LE_STAFF, newSec);
            mm.releaseResource(ResourceTypes.OPS_STAFF, newSupport);
            shiftTired = true;
            staffNextDegradeTime = tick;
            return false;
        } else  {
            lastShiftMedical = medCurrent;
            lastShiftSecurity = secCurrent;
            lastShiftSupport = supportCurrent;
            shiftTired = false;
            resetStaff(newMed, newSupport, newSec);
            // Reset the timer on the new shift
            mm.queueDelayedEvent("shiftReplaced", this, 0);
            // Keep the replaced workers out of the pool for the next 8 hours
            mm.queueDelayedEvent("shiftSentHome", this, 0);
        }
        return true;
    }

    public void releaseResource(ResourceTypes resourceType, int qty) {
        if (resourceType.equals(ResourceTypes.CLINICAL_STAFF)) {
            if (medCurrent >= qty) {
                medCurrent -= qty;
            } else {
                qty = medCurrent;
                medCurrent = 0;
            }

        } else if (resourceType.equals(ResourceTypes.OPS_STAFF)) {
            if (supportCurrent >= qty) {
                supportCurrent -= qty;
            } else {
                qty = supportCurrent;
                supportCurrent = 0;
            }
        } else if (resourceType.equals(ResourceTypes.LE_STAFF)) {
            if (secCurrent >= qty) {
                secCurrent -= qty;
            } else {
                qty = secCurrent;
                secCurrent = 0;
            }
        }
        mm.releaseResource(resourceType, qty);
        setThroughput();
    }

    public int getMedicalStaff() {
        return medCurrent;
    }

    public int getSecurityStaff() {
        return secCurrent;
    }

    public int getSupportStaff() {
        return supportCurrent;
    }

    public boolean requestEquipment() {
        int amtGranted = mm.requestResource(ResourceTypes.EQUIPMENT_SET, 1);
        if (amtGranted == 1) {
            hasEquipment = true;
            fireEvent("gotEquipment");
            amIReady();
            return true;
        }
        return false;
    }

    public boolean getEquipment() {
        return hasEquipment;
    }

    // not used, yet
    public void setMeds(boolean meds){
        hasMeds = meds;
        if (meds) {
            fireEvent("gotMeds");
        }
    }

    public boolean getMeds() {
        return hasMeds;
    }

    public int getPopulation() {
        return population;
    }

    public String getId() {
        return podId;
    }


   public int getQueueSize() {
       return patientQueue.size();
    }

   public List<Person> getUntreated() {

       synchronized(peopleLock) {
           ArrayList<Person> untreated = new ArrayList<Person>(people.size());
           for (Person p : people) {
               if (!p.isTreated()) {
                   untreated.add(p);
               }
           }
           return untreated;
       }
   }


   /**
    * Adds people to the pool and adds them to the queue if the pod is open.
    * (If the pod hasn't opened yet, they will be added to the queue when the pod opens)
    * @param unassignedPatients
    */
   public void addPeople(ArrayList<Person> unassignedPatients) {
       synchronized(peopleLock) {
           people.addAll(unassignedPatients);
           if (isInState("open")) {
               for (Person p : unassignedPatients) {
                   p.setPod(this);
               }
           }
       }
    }


// The following methods match the states in pod.xml
    public void pod() {
        if (log != null)
            log.info("Pod " + podId + " is in state pod");


    }

    public void overall() {
        if (log != null)
            log.info("Pod " + podId + " is in state overall");
    }

    public void unActivated() {
        status = DeploymentStatusType.AVAILABLE;
        if (log != null)
            log.info("Pod " + podId + " is unActivated");
        if (mm != null)
            mm.reportResourceStatus(podId, name, status.toString(), 1);
    }

    public void beingActivated() {
        status = DeploymentStatusType.COMMITTED;
        double waitTime = random.nextGaussian() + ACTIVATION_TIME; // hours
        waitTime = waitTime * 60 * 60; // seconds
        log.info("Pod " + podId + " is beingActivated. Wait time is " + (int) waitTime + " seconds");
        mm.queueDelayedEvent("doneActivating", this, (int) waitTime);
        mm.reportResourceStatus(podId, name, status.toString(), 1);
        activationClockTime = (int)waitTime + tick;
    }

    public void activated() {
        // set the throughput now that we're sure we have met minimum staff levels
        log.info("Pod " + podId + " is in state activated");
        setThroughput();
        startupTimeExpired = true;
        status = DeploymentStatusType.READY;
        mm.reportResourceStatus(podId, name, status.toString(), 1);
    }



    public void open() {
        log.info("Pod " + podId + " is open");
        status = DeploymentStatusType.IN_USE;

        // First chance to riot in 4 hours after opening
        nextRiotTime = tick + 4 * 60 *60;
        
        // The staff will need to be replaced in 12 hours
        mm.queueDelayedEvent("shiftEnded", this, 12 * 60 *60);

        // slow down the clock
        podsEnqueing++;
        mm.setClockSpeed(12);
        // Send my people to this pod
        for (Person p : people) {
            p.setPod(this);
        }
        // speed the clock up if there aren't other pods enqueing at the moment
        podsEnqueing--;
        if (podsEnqueing == 0)
            mm.setClockSpeed(200);
        else
            log.info("Not speeding up the clock, other pods are enqueing people");
        mm.reportResourceStatus(podId, name, status.toString(), 1);
        mm.setPodOpen(this, true);

    }

    public void riot() {
        log.info("Pod " + podId + " is rioting");
        // !!! Send a message that we're in trouble
        status = DeploymentStatusType.NON_FUNCTIONAL;
        mm.reportResourceStatus(podId, name, status.toString(), 1);
    }

    public void closing() {
        log.info("Pod " + podId + " is closing");
        mm.setPodOpen(this, false);
        synchronized(patientQueueLock) {
            mm.reassignPatients(getUntreated());
            patientQueue.clear();
        }

        mm.releaseResource(ResourceTypes.CLINICAL_STAFF, medCurrent);
        mm.releaseResource(ResourceTypes.LE_STAFF, secCurrent);
        mm.releaseResource(ResourceTypes.OPS_STAFF, supportCurrent);
        medCurrent = 0;
        supportCurrent = 0;
        secCurrent = 0;

        status = DeploymentStatusType.RELEASED;
        mm.reportResourceStatus(podId, name, status.toString(), 1);

    }

    public void meds() {
        if (log != null)
            log.info("Pod " + podId + " is in state meds");
    }

    public void noMeds() {
        if (log != null)
            log.info("Pod " + podId + "  is in state noMeds");
    }

    public void hasMeds() {
        hasMeds = true;
        log.info("Pod " + podId + " is in state hasMeds");
        // We can't call amIReady() here because we're not fully in the hasMeds
        // state, we're still in onentry.
        // So, queue up a doneActivating event to happen as soon as possible
        mm.queueDelayedEvent("doneActivating", this, 0);
        //amIReady();
    }

    public void equipment() {
        if (log != null)
            log.info("Pod " + podId + " is in state equipment");
    }

    public void noEquipment() {
        if (log != null)
            log.info("Pod " + podId + " is in state noEquipment");
    }

    public void hasEquipment() {
        log.info("Pod " + podId + " is in state hasEquipment");
    }

    public void medStaff() {
        if (log != null)
            log.info("Pod " + podId + " is in state medStaff");
    }

    public void noMedStaff() {
        if (log != null)
            log.info("Pod " + podId + " is in state noMedStaff");
    }

    public void hasMedStaff() {
        log.info("Pod " + podId + " is in state hasMedStaff");
    }

    public void securityStaff() {
        if (log != null)
            log.info("Pod " + podId + " is in state securityStaff");
    }

    public void noSecurityStaff() {
        if (log != null)
            log.info("Pod " + podId + " is in state noSecurityStaff");
    }

    public void hasSecurityStaff() {
        log.info("Pod " + podId + " is in state hasSecurityStaff");
    }

    public void operationsStaff() {
        if (log != null)
            log.info("Pod " + podId + " is in state operationsStaff");
    }

    public void noOperationsStaff() {
        if (log != null)
            log.info("Pod " + podId + " is in state noOperationsStaff");
    }

    public void hasOperationsStaff() {
        log.info("Pod " + podId + " is in state hasOperationsStaff");
    }

    public void shiftStatus() {}

    public void shiftStartState() {}

    public void notShiftExpired() {
        mm.queueDelayedEvent("shiftEnded", this, 12 * 60 * 60);
        log.log(Level.INFO, "{0} new shift started", podId);
        shiftTired = false;
    }

    public void shiftExpired() {
        // this is queued when the pod opens
        log.log(Level.INFO, "{0} shift expired", podId);
        replaceStaff();
    }

    public void lastShiftRested() {}
    public void shiftRested() {
        // release the last shift after their 8 hours off
        if (mm != null) {
            log.log(Level.INFO, "{0} Ahhh, that was a good rest. The shift can go back into the work pool", podId);
            mm.releaseResource(ResourceTypes.CLINICAL_STAFF, lastShiftMedical);
            mm.releaseResource(ResourceTypes.OPS_STAFF, lastShiftSupport);
            mm.releaseResource(ResourceTypes.LE_STAFF, lastShiftSecurity);
        }
    }
    public void shiftNotRested(){
        // make sure they don't work again for 8 hours
        mm.queueDelayedEvent("shiftSleptEnough", this, 8 * 60* 60);
    }


    public void enqueuePerson(Person person) {
        // Make this person wait some amount of time, then fire an event.
        // The wait time is based on the number in the queue already and the throughput.
        // The scaleFactor represents the number of people each person object
        // represents. If throughput is 200 people per hour and each person
        // represents 10 people, the throughput is effectively 20 people per hour.

        synchronized(patientQueueLock) {
            long waitTime = Math.round((1.0 * patientQueue.size() / (throughput/scaleFactor)) * 60 * 60);
            queueDelayedEvent("treatMe", person, waitTime);
        }
    }

    public void setStandardOfCare(int soc) {
        log.info("setStandardOfCare:  "  + podId + " " + soc);
        if (soc < 1 || soc > 6) {
            log.warning("Invalid standard of care: " + soc);
            return;
        }
        standardOfCare = soc;
        standardOfCarePenalty = ThroughputHelper.getCarePenalty(standardOfCare);
        setThroughput();
    }

    public int getStandardOfCare() {
        return standardOfCare;
    }
    
    /**
     *  Requeue the patients based on the new throughput time.
     */
    private void setThroughput() {
        this.throughput = ThroughputHelper.getThroughput(standardOfCare, medCurrent, supportCurrent, staffPerformancePenalty);
        log.log(Level.INFO, "Pod {0} Throughput={1}", new Object[]{podId, throughput});
        // Note: toArray() doesn't guarentee order, so the queue might be reshuffled.

        synchronized(patientQueueLock) {
            DelayedEvent[] events = patientQueue.toArray(new DelayedEvent[0]);
            patientQueue.clear();
            for (DelayedEvent e : events) {
                enqueuePerson(e.getPatient());
            }
        }
    }

    public int getThroughput() {
        return throughput;
    }

    /**
     * Take people from this pod's people list
     * @param qty
     * @return
     */
    public List<Person> stealPeople(int qty) {
        // This doesn't quite do the job if the pod is active and there are people in the queue
       synchronized(peopleLock) {
           if (qty > people.size())
               qty = people.size();
           ArrayList<Person> stolenPeople = new ArrayList<Person>(qty);
           for (int i = 0; i < qty; i++) {
               stolenPeople.add(people.remove(0));
           }
           return stolenPeople;
        }
    }


    public void treatPerson(Person person) {
        person.treatMe(standardOfCarePenalty);
    }



    private void amIReady() {
        StringBuilder out = new StringBuilder("amIReady: ");
        out.append(podId);
        out.append(" hasMedStaff=");
        out.append(isInState("hasMedStaff") ? "Y" : "N");
        out.append(" hasSupportStaff=");
        out.append(isInState("hasOperationsStaff") ? "Y" : "N");
        out.append(" hasSecurityStaff=");
        out.append(isInState("hasSecurityStaff") ? "Y" : "N");
        out.append(" hasEquipment=");
        out.append(isInState("hasEquipment") ? "Y" : "N");
        out.append(" hasMeds=");
        out.append(isInState("hasMeds") ? "Y" : "N");
        log.info(out.toString());
        if(medCurrent >= medMin
                && secCurrent >= secMin
                && supportCurrent >= supportMin
                && hasMeds
                && hasEquipment
                && tick >= activationClockTime) {
            fireEvent("doneActivating");
        } else {
            StringBuilder statusMessage = new StringBuilder("Pod ");
            statusMessage.append(podId);
            statusMessage.append(" not activated");
            if (medCurrent < medMin) {
                statusMessage.append(". Need ");
                statusMessage.append(medMin);
                statusMessage.append(" medical staff, have ");
                statusMessage.append(medCurrent);
            }
            if (supportCurrent < supportMin) {
                statusMessage.append(". Need ");
                statusMessage.append(supportMin);
                statusMessage.append(" support staff, have ");
                statusMessage.append(supportCurrent);
            }
            if (secCurrent < secMin){
                statusMessage.append(". Need ");
                statusMessage.append(secMin);
                statusMessage.append(" security staff, have ");
                statusMessage.append(secCurrent);
            }
            if (!hasMeds) {
                statusMessage.append(". Pod has no medication.");
            }
            if (!hasEquipment) {
                statusMessage.append(". Pod has no equipment.");
            }
            if (tick < activationClockTime) {
                statusMessage.append(". Pod still needs " + (activationClockTime - tick) + " seconds to set up");
            }
            log.info(statusMessage.toString());
        }
    }


    public boolean isInState(String queryState) {
        @SuppressWarnings("unchecked")
        Set<State> states = getEngine().getCurrentStatus().getStates();
        for (State state : states) {
            String id = state.getId();
            if (id.equals(queryState)) {
                return true;
            }
        }
        return false;
    }

    public void queueDelayedEvent(String event, Person personStateMachine, long time) {
//        log.info("queueDelayedEvent: event " + event + " will fire after game time=" + (tick + time));
        DelayedEvent e = new DelayedEvent(event, personStateMachine, tick + time);
        synchronized(patientQueueLock) {
            patientQueue.offer(e);
        }
    }


    /**
     * Take all events that are due based on the current time tick and
     * queue them in the event queue so that they will be processed on the
     * event processing thread.
     */
    private void fireDelayedEvents() {
        if (!isInState("riot")) {
            synchronized (patientQueueLock) {
                while (patientQueue.peek() != null && patientQueue.peek().getTime() <= tick) {
                    DelayedEvent p = patientQueue.poll();
                    queueEvent(p.getEventName(), p.getPatient());
                }
            }
        }
    }

     /**
     * Add an event with a patient to the queue to be fired on the dialogue
     * event processing thread
     * @param event
     * @param patient
     */
    public void queueEvent(String event, Person stateMachine) {
        eventQueue.offer(new Event(event, stateMachine));
    }



    // Stolen from DialogueManager and modified
   private class Event {
       String eventName;
       Person patient;


       public Event(String eventName, Person patient) {
           super();
           this.eventName = eventName;
           this.patient = patient;
       }
       /**
        * @return the eventName
        */
       public String getEventName() {
           return eventName;
       }
       /**
        * @param eventName the eventName to set
        */
       public void setEventName(String eventName) {
           this.eventName = eventName;
       }
       /**
        * @return the patient
        */
       public Person getPatient() {
           return patient;
       }
       /**
        * @param patient the patient to set
        */
       public void setPayload(Person stateMachine) {
           this.patient = stateMachine;
       }


  }

    private class DelayedEvent extends Event implements Comparable {
        long time;

        public DelayedEvent(String eventName, Person personStateMachine, long time) {
            super(eventName, personStateMachine);
            this.time = time;
        }

        /**
         * @return the time
         */
        public long getTime() {
            return time;
        }

        /**
         * @param time the time to set
         */
        public void setTime(long time) {
            this.time = time;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof DelayedEvent))
                return -1;
            else {
                DelayedEvent de = (DelayedEvent) o;
                return time < de.getTime() ? -1 : time == de.getTime() ? 0 : 1;
            }
        }
    }

}
