/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2010. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.model;

import java.util.List;
import org.mitre.eren.protocol.edxl_rm.ResourceTypes;

/**
 *
 * @author JWINSTON
 */


public class LetterCarrier {

    public static final int MINIMUM_SECURITY = 3;
    private int lawEnforcement = 0;
    private String carrierId;
    private ModelManager modelManager;
    private Usps usps;
    private final int coverageRate = 146; // People per hour
    private final int coveredPopulation = 876; //6 hour route, 146/hour


    public LetterCarrier(int id, Usps usps, ModelManager mm) {
        carrierId = Integer.toString(id);
        modelManager = mm;
        this.usps = usps;
    }

    public void assignLawEnforcement(int numOfficers) {
        // Assume that the resources has already been requested/allocated
        lawEnforcement = numOfficers;
    }

    public int getPopulationServed() {
        return coveredPopulation/modelManager.getScaleFactor();
    }


    public void releaseLE() {
        modelManager.releaseResource(ResourceTypes.LE_STAFF, lawEnforcement);
    }

    
    public boolean hasSecurity() {
        if (lawEnforcement < MINIMUM_SECURITY)
            return false;
        else
            return true;
    }

   /**
     * Queue up the people to have drugs delivered to their homes by this carrier.
     * A delay is added because we assume that the carrier doesn't get the medication immediately.
     *
     * This won't work for adding people to an existing queue. They won't be
     * added to the end of the existing list. 
     *
     * @param people
     * @param medDelay seconds to delay the start of the route due to the delay in the arrival of medication
     * @return false if the carrier has insufficient security to deliver
     */
    public boolean enqueuePeople(List<Person> people, long medDelay) {

        if (lawEnforcement < MINIMUM_SECURITY) {
            return false;
        }

        for (int i = 0; i < people.size(); i++) {
            Person person = people.get(i);
            // The carrier dispenses meds a rate of coverageRate
            // Queue up the people to receive meds
            float scaledCoverageRate = (float)coverageRate/(float)modelManager.getScaleFactor();
            long waitTime = Math.round((1.0 * i / scaledCoverageRate * 60 * 60) + medDelay);
            usps.queueDelayedEvent("treatMe", person, waitTime);
        }
        return true;
    }

}
