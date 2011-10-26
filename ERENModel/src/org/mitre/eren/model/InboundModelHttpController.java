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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.abdera.factory.ExtensionFactory;
import org.apache.abdera.model.Element;
import org.mitre.eren.message.MessageProcessor;
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.clock.ClockExtensionFactory;
import org.mitre.eren.protocol.clock.Clocktick;
import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.mitre.eren.protocol.dialogue.DialogueExtensionFactory;
import org.mitre.eren.protocol.dialogue.OpenPod;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.mitre.eren.protocol.edxl_rm.EDXLRMExtensionFactory;
import org.mitre.eren.protocol.edxl_rm.ReportResourceDeploymentStatusWrapper;
import org.mitre.eren.protocol.edxl_rm.Resource;
import org.mitre.eren.protocol.edxl_rm.ResourceInformation;
import org.mitre.eren.protocol.edxl_rm.ResourceMessageWrapper;
import org.mitre.eren.protocol.kml.KMLExtensionFactory;
import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.ScenarioConstants;
import org.mitre.eren.protocol.scenario.ScenarioExtensionFactory;
import org.mitre.eren.protocol.startup.RequestPodStatus;
import org.mitre.eren.protocol.startup.SetStandardOfCare;
import org.mitre.eren.protocol.startup.StartupConstants;
import org.mitre.eren.protocol.startup.StartupExtensionFactory;
import org.mitre.javautil.logging.LoggingUtils;

/**
 *
 * @author JWINSTON
 */
public class InboundModelHttpController implements MessageProcessor, EDXLRMConstants ,ClockConstants, ScenarioConstants, DialogueConstants, StartupConstants  {
	private double eps = 0.01;
	private double tot = 0.0;
	private static final Logger log = LoggingUtils.getLogger(InboundModelHttpController.class);

	private ModelManager dm;

    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
	public InboundModelHttpController(final ModelManager mm) {
		this.dm = mm;
		Thread qt = new Thread("inboundModelQueue"){
                    @Override
                    public void run() {
                        while (true) {
                            Element e = mm.getFromQueue();
                            //System.err.println("Qname: " + e.getQName());
                            if (e.getQName().equals(EREN_CLOCKTICK)) {
                                // tick
                                Clocktick c = (Clocktick) e;
                                //log.log(Level.INFO, c.toString());
                                mm.setTick(c.getTime());
                            } else if (e.getQName().equals(EREN_CLOCKSPEED)) {
                                // ratio sync
                                // don't care
                            } else if (e.getQName().equals(RM_COMMITRESOURCE)) {
                                List<ResourceInformation> el = ((ResourceMessageWrapper) e).getResourceInformation();
                                ResourceInformation ri = el.get(0);
                                Resource r = ri.getResource();
                                String id = r.getResourceID();
                                tot = tot + eps;// This makes them handled sort of in FIFO order
                                log.log(Level.INFO, "Received RM_COMMITRESOURCE at {0}{1} {2}", new Object[]{mm.getTick(), tot, id});
                                mm.commitResource(el);

                            } else if (e.getQName().equals(RM_REQUESTRESOURCEDEPLOYMENTSTATUS)) {
                                log.info("RM_REQUESTRESOURCEDEPLOYMENTSTATUS ");
                                mm.receiveResourceStatusRequest((ReportResourceDeploymentStatusWrapper) e);
                            } else if (e.getQName().equals(RM_REPORTRESOURCEDEPLOYMENTSTATUS)) {
                                log.info("RM_REPORTTRESOURCEDEPLOYMENTSTATUS ");
                                mm.receiveResourceStatusRequest((ReportResourceDeploymentStatusWrapper) e);
                            } else if (e.getQName().equals(EREN_SCENARIO)) {
                                mm.setScenario((ERENscenario) e);
                            } else if (e.getQName().equals(RM_RELEASERESOURCE)) {
                                mm.releaseResource((ResourceMessageWrapper) e);
                            } else if (e.getQName().equals(DLG_OPENPOD)) {
                                OpenPod op = (OpenPod) e;
                                mm.openPod(op.getPodId());
                            } else if (e.getQName().equals(EREN_REQUESTSCORE)) {
                                mm.reportScore();
                            } else if (e.getQName().equals(EREN_SETSTANDARDOFCARE)) {
                                SetStandardOfCare soc = (SetStandardOfCare) e;
                                mm.setStandardOfCare(soc.getPodId(), soc.getStandardOfCare());
                            } else if (e.getQName().equals(EREN_REQUESTPODSTATUS)) {
                                RequestPodStatus rps = (RequestPodStatus) e;
                                mm.reportPODStatus(rps.getPodId());
                            }
                        }
                    }
            };
            qt.start();

    }


    @Override
    public void processMessage(Element e, String gameID, String sender, List<String> roles, List<String> userID) {
        //log.info("Adding to Queue ");
        dm.addToQueue(e);
    }// processMessage (Element e)


    @Override
    @SuppressWarnings("unchecked")
    public <T extends ExtensionFactory> List<T> getExtensions() {
        List<T> l = new ArrayList<T>();
        l.add((T) new ClockExtensionFactory());
        l.add((T) new EDXLRMExtensionFactory());
        l.add((T) new ScenarioExtensionFactory());
        l.add((T) new KMLExtensionFactory());
        l.add((T) new DialogueExtensionFactory());
        l.add((T) new StartupExtensionFactory());
        return l;
    }
}// class InboundModelHttpController implements MessageProcessor, EDXLRMConstants ,ClockConstants, ScenarioConstants

