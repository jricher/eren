/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2010. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.model;

import java.util.Random;
import org.apache.commons.scxml.env.AbstractStateMachine;

/**
 *
 * @author JWINSTON
 */
public class RssResource extends AbstractStateMachine {
    private ModelManager modelManager;
    private String resourceType;
    private Random random = new Random();


    public RssResource(ModelManager modelManager, String resourceType){
        super(Pod.class.getClassLoader().getResource("org/mitre/eren/model/resource/rssResource.xml"));
        this.resourceType = resourceType;
        this.modelManager = modelManager;
    }

    public void unSent() {
        
    }

    public void enRoute() {
        double travelTime = random.nextGaussian() + 3.5/*AnyLogic uniform(2,5)*/; // hours
        travelTime = travelTime * 60 * 60; // seconds
        modelManager.queueDelayedEvent("resourceArrived", this, (int)travelTime);
    }

    public void arrived(){
        // Tell the RSS that this resource has arrived
    }
}
