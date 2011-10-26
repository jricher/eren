/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2010. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Logger;
import org.mitre.eren.protocol.edxl_rm.ResourceTypes;
import org.mitre.javautil.logging.LoggingUtils;

/**
 *
 * @author JWINSTON
 */
public class Usps {

    private ArrayList<LetterCarrier> letterCarriers;
    private int unassignedLawEnforcement = 0;
    private ModelManager modelManager;
    private final PriorityBlockingQueue<DelayedEvent> patientQueue = new PriorityBlockingQueue<DelayedEvent>();
    private final LinkedBlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>();
    private Thread uspsThread = null;
    private boolean finished = false;
    private boolean started = false;
    private long tick;
    private static final Logger log = LoggingUtils.getLogger(Usps.class);
    private long medDelay;

    public Usps(ModelManager mm) {
        modelManager = mm;

        // Delay the start of delivery until the medicine arrives.
        Random random = new Random();
        double delay = random.nextGaussian() + Pod.ACTIVATION_TIME; // hours
        medDelay = Math.round(delay * 60 * 60); // seconds

        init();
    }


    private void init() {
        if (uspsThread != null)
            return;

        uspsThread = new Thread("EREN USPS Event Queue") {
            @Override
            public void run() {
                while (!finished) {
                    try {
                        Event e = eventQueue.take();
                        // USPS delivery has to be equivalent to lowest Pod standard of care
                        e.getPatient().treatMe(ThroughputHelper.getCarePenalty(6));
                    } catch (InterruptedException e) {
                    }
                }
                // cleanup
                for (LetterCarrier lc : letterCarriers) {
                    lc.releaseLE();
                    modelManager.releaseResource(ResourceTypes.LETTER_CARRIER, 1);
                }
                letterCarriers.clear();
            }
        };
    }


    public void addCarriers(int numCarriers) {
        if (letterCarriers == null) {
            letterCarriers = new ArrayList<LetterCarrier>(numCarriers);
        }
        for (int i = 0; i < numCarriers; i++) {
            LetterCarrier lc = new LetterCarrier(i, this, modelManager);
            letterCarriers.add(lc);
            if (unassignedLawEnforcement >= LetterCarrier.MINIMUM_SECURITY) {
                lc.assignLawEnforcement(LetterCarrier.MINIMUM_SECURITY);
                unassignedLawEnforcement = unassignedLawEnforcement - LetterCarrier.MINIMUM_SECURITY;
                assignPeople(lc);
                // At least one letter carrier has people on his route
                started = true;
            }
        }
        if (started) {
            uspsThread.start();
        }
    }

    public void assignLawEnforcement(int numLE) {
        unassignedLawEnforcement += numLE;

        if (letterCarriers == null)
            return;

        for (LetterCarrier lc : letterCarriers) {
            if (unassignedLawEnforcement < LetterCarrier.MINIMUM_SECURITY)
                break;

            if (!lc.hasSecurity()) {
                lc.assignLawEnforcement(LetterCarrier.MINIMUM_SECURITY);
                unassignedLawEnforcement = unassignedLawEnforcement - LetterCarrier.MINIMUM_SECURITY;
                assignPeople(lc);
                // At least one letter carrier has people on his route
                started = true;
            }
        }
        if (started) {
            uspsThread.start();
        }
    }

    private void assignPeople(LetterCarrier lc) {
        // Get people from somewhere and let the distribution begin
        List<Person> people = modelManager.requestPeople(lc.getPopulationServed());
        if (!people.isEmpty()) {
            lc.enqueuePeople(people, medDelay);
        }
    }

    public void setTick(long time) {
        tick  = time;
        fireDelayedEvents();
    }

    public void queueDelayedEvent(String event, Person personStateMachine, long time) {
        log.info("queueDelayedEvent: event " + event + " will fire after game time=" + (tick + time));
        DelayedEvent e = new DelayedEvent(event, personStateMachine, tick + time);
        patientQueue.offer(e);
    }


    /**
     * Take all events that are due based on the current time tick and
     * queue them in the event queue so that they will be processed on the
     * event processing thread.
     */
    private void fireDelayedEvents() {
        while (patientQueue.peek() != null && patientQueue.peek().getTime() <= tick) {
            DelayedEvent p = patientQueue.poll();
            queueEvent(p.getEventName(), p.getPatient());
        }

        if (patientQueue.isEmpty() && started) {
            // Looks like we're done. (I hope this isn't a race condition.)
            finished = true;
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
