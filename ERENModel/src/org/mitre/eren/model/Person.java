/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2010. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
 *****************************************************************************/
package org.mitre.eren.model;

import java.net.URL;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

import java.util.logging.Logger;
import org.apache.commons.scxml.env.AbstractStateMachine;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;
import org.mitre.javautil.logging.LoggingUtils;

/**
 *
 * @author JWINSTON
 */
public class Person extends AbstractStateMachine {

    public final int PRODROMAL_RECOVERY_RATE = 60; //Out of 100
    public final double INCUBATION_PERIOD_MEAN = 5.4; //days
    public final double INCUBATION_PERIOD_SIGMA = 0.8;
    public final double PRODROMAL_PERIOD_MEAN = 3.9; //days
    public final double PRODROMAL_PERIOD_SIGMA = 0.35;
    public final double FULMINANT_PERIOD_MEAN = 1.41;
    public final double FULMINANT_PERIOD_SIGMA = 1.0;

    private Logger log = LoggingUtils.getLogger(Person.class);
    private Random random = new Random();
    private boolean wentSomewhere = false;
    private boolean hasBeenTreated = false;
    /** The Timer to keep time. */
    private int sec = 0;
    private ModelManager modelManager;
    private Pod pod;

    private static final URL scxmlUrl = Person.class.getClassLoader().getResource("org/mitre/eren/model/resource/person.xml");


    public Person(ModelManager mm) {
        super(scxmlUrl);
        this.modelManager = mm;
    }

    public Person(SCXML personSCXML, ModelManager mm) {
        super(personSCXML);
        this.modelManager = mm;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
        // Send this person to the pod at a random time over the next 24 hours
        long podTime = random.nextInt(24 * 60 *60);
        modelManager.queueDelayedEvent("location.toPOD", this, podTime);
    }

    public Pod getPod(){
        return this.pod;
    }

    public void treatMe(int standardOfCarePenalty) {
        setTreated(true);
        if (isInState("well")) {
            fireEvent("location.toHome");
        } else if (isInState("incubational")) {
            // 100% antibiotic efficacy during incubation, but let's assess a penalty for lower standard of care
            if (random.nextInt(100) <= 100 - standardOfCarePenalty) {
                fireEvent("turn.recovered");
            }
        } else if (isInState("prodromal")) {
            if (random.nextInt(100) <= PRODROMAL_RECOVERY_RATE - standardOfCarePenalty) {
                fireEvent("turn.recovered");
            } else {
                log.log(Level.INFO, "So sorry, {0}, you''re not going to get better", this);
            }
        } else {
            log.log(Level.INFO, "So sorry, {0}, you''re too sick to help", this);
        }
        modelManager.incrementTreated();
    }

    public void sickness() {
    }

    public void person() {
    }

    public void well() {
    }

    public void incubational() {
        //The game starts three days after the first patients become symptomatic.
        long timeToProdromal = Math.round((((INCUBATION_PERIOD_SIGMA * random.nextGaussian()) + INCUBATION_PERIOD_MEAN) - 3 ) * 24 * 60 *60); //seconds
        modelManager.queueDelayedEvent("turn.prodromal", this, timeToProdromal);
    }

    public void prodromal() {
        long timeToFulminant = Math.round(((PRODROMAL_PERIOD_SIGMA * random.nextGaussian()) + PRODROMAL_PERIOD_MEAN) * 24 *60 *60); //seconds
        modelManager.incrementSick();
        modelManager.queueDelayedEvent("turn.fulminant", this, timeToFulminant);
    }

    public void fulminant() {
        long timeToDead = Math.round(((FULMINANT_PERIOD_SIGMA * random.nextGaussian()) + FULMINANT_PERIOD_MEAN) * 24 * 60 *60); //seconds
        modelManager.queueDelayedEvent("turn.dead", this, timeToDead);
    }

    public void dead() {
        modelManager.incrementDead();
    }

    public void recovered() {
        log.info("I'm recovered");
    }

    public void locationContainer() {
    }

    public void home() {
        if (wentSomewhere) {
            //log.info("I'm home, again.");
        }
    }

    public void pod() {
        wentSomewhere = true;
        if (pod == null) {
            pod = modelManager.assignToPod(this);
        }
        pod.enqueuePerson(this);
    }

    public void location() {
    }

    public void hospital() {
//        wentSomewhere = true;
//        if (random.nextInt(10) < PRODROMAL_RECOVERY_RATE) {
//            fireEvent("turn.recovered");
//        }
    }

    public void admitted() {
    }

    public void morgue() {
    }

    public void timing() {
    }


    // used by the demonstration (see PatientDisplay usecase)
    public String getCurrentState() {
        Set states = getEngine().getCurrentStatus().getStates();
        return ((org.apache.commons.scxml.model.State) states.iterator().
                next()).getId();
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

    public void setTreated(boolean b) {
        hasBeenTreated = true;
    }

    public boolean isTreated() {
        return hasBeenTreated;
    }
}
