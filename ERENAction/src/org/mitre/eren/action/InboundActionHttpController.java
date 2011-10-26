package org.mitre.eren.action;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.abdera.factory.ExtensionFactory;
import org.apache.abdera.model.Element;
import org.mitre.eren.message.MessageProcessor;
import org.mitre.eren.protocol.action.ActionConstants;
import org.mitre.eren.protocol.action.ActionExtensionFactory;
import org.mitre.eren.protocol.action.ActionRequest;
import org.mitre.eren.protocol.action.ActionsFile;
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.mitre.eren.protocol.edxl_rm.EDXLRMExtensionFactory;
import org.mitre.eren.protocol.scenario.ScenarioConstants;
import org.mitre.eren.protocol.startup.StartupConstants;
import org.mitre.javautil.logging.LoggingUtils;

/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
*****************************************************************************/

/**
 *
 * @author JWINSTON
 */
class InboundActionHttpController implements MessageProcessor, EDXLRMConstants ,ClockConstants, ScenarioConstants, DialogueConstants, StartupConstants, ActionConstants {

    private double eps = 0.01;
    private double tot = 0.0;
    private static final Logger log = LoggingUtils.getLogger(InboundActionHttpController.class);
    private LinkedBlockingQueue<Element> myQueue = new LinkedBlockingQueue<Element>();

    private ActionManager am;

    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    public InboundActionHttpController(final ActionManager am) {
        this.am = am;
        Thread qt = new Thread("inboundActionQueue") {
            @Override
            public void run() {
                while (true) {
                    Element e;
                    try {
                        e = myQueue.take();
                    } catch (InterruptedException ex) {
                        log.log(Level.SEVERE, null, ex);
                        continue;
                    }
                    //log.info(e.toString());
                    if (e.getQName().equals(EREN_CLOCKTICK)) {
                    } else if (e.getQName().equals(EREN_CLOCKSPEED)) {
                    } else if (e.getQName().equals(ACT_COMMITACTION)){
                        ActionRequest act = (ActionRequest)e;
                        log.info(act.toString());
                        am.act(act);
                    } else if (e.getQName().equals(EREN_ACTIONSFILE)) {
                        ActionsFile af = (ActionsFile)e;
                        log.info(af.toString());
                        am.setActionsFile(af);
                    } else if (e.getQName().equals(RM_COMMITRESOURCE)) {
                    } else if (e.getQName().equals(RM_REQUESTRESOURCEDEPLOYMENTSTATUS)) {
                    } else if (e.getQName().equals(RM_REPORTRESOURCEDEPLOYMENTSTATUS)) {
                    } else if (e.getQName().equals(EREN_SCENARIO)) {
                    } else if (e.getQName().equals(RM_RELEASERESOURCE)) {
                    } else if (e.getQName().equals(DLG_OPENPOD)) {
                    } else if (e.getQName().equals(EREN_REQUESTSCORE)) {
                    } else if (e.getQName().equals(EREN_SETSTANDARDOFCARE)) {
                    } else if (e.getQName().equals(EREN_REQUESTPODSTATUS)) {
                    }
                }
            }
        };
        qt.start();

    }


    @Override
    public void processMessage(Element e, String gameID, String sender, List<String> roles, List<String> userID) {
        //log.info("Adding to Queue ");
        myQueue.offer(e);
    }// processMessage (Element e)


    @Override
    @SuppressWarnings("unchecked")
    public <T extends ExtensionFactory> List<T> getExtensions() {
        List<T> l = new ArrayList<T>();
        l.add((T) new EDXLRMExtensionFactory());
        l.add((T) new ActionExtensionFactory());
         return l;
    }

}
