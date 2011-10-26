package org.mitre.eren.action;

/*****************************************************************************
 *  License Agreement
 *
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 *
 *****************************************************************************/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Document;
import org.apache.abdera.parser.Parser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.mitre.eren.dialogue.ResourceMessageType;

import org.mitre.eren.http.OutboundHttpEndpoint;
import org.mitre.eren.message.MessageProcessor;
import org.mitre.eren.protocol.action.ActionExtensionFactory;
import org.mitre.eren.protocol.action.ActionConstants.ActionsEnum;
import org.mitre.eren.protocol.action.ActionRequest;
import org.mitre.eren.protocol.action.ActionsFile;
import org.mitre.eren.protocol.edxl_rm.AssignmentInformation;
import org.mitre.eren.protocol.edxl_rm.ContactInformationType;
import org.mitre.eren.protocol.edxl_rm.ContactRoleType;
import org.mitre.eren.protocol.edxl_rm.EDXLRMExtensionFactory;
import org.mitre.eren.protocol.edxl_rm.LocationType;
import org.mitre.eren.protocol.edxl_rm.MeasuredQuantity;
import org.mitre.eren.protocol.edxl_rm.QuantityType;
import org.mitre.eren.protocol.edxl_rm.Resource;
import org.mitre.eren.protocol.edxl_rm.ResourceInformation;
import org.mitre.eren.protocol.edxl_rm.ResourceMessage;
import org.mitre.eren.protocol.edxl_rm.ResourceTypes;
import org.mitre.eren.protocol.edxl_rm.ScheduleInformation;
import org.mitre.eren.protocol.edxl_rm.ValueListType;
import org.mitre.eren.protocol.startup.StartupConstants;
import org.mitre.eren.protocol.startup.SetStandardOfCare;
import org.mitre.eren.protocol.startup.StartupExtensionFactory;
import org.mitre.eren.wrapper.WrappedERENModule;
import org.mitre.javautil.logging.LoggingUtils;
import org.opencare.lib.model.edxl.EDXLDistribution;



/**
 *
 * @author JWINSTON
 */
public class ActionManager implements WrappedERENModule {
    private static final Logger log = LoggingUtils.getLogger(ActionManager.class);
    private OutboundHttpEndpoint client;
    private String sender = "action@erenbus.mitre.org";
    private ActionsFile actionFile;
    private Factory factory;
    private static final String DEFAULT_RESOURCE_TYPE_URN = "urn:x-hazard:vocab:resourceTypes";
    private MessageProcessor messageProcessor;

    public ActionManager(String[] args) {

        // parse the arguments
        int serverPort = 3737;
        String endpoint = "http://erenbus.mitre.org:3732/";
        Abdera abdera = Abdera.getInstance();
        factory = abdera.getFactory();
        factory.registerExtension(new ActionExtensionFactory());
        log.info("finished factory configuration");

        try {
            Options options = new Options();

            options.addOption("p", "port", true, "Server port to listen on for incoming HTTP messages from the bus");
            options.addOption("u", "url", true, "Outbound URL to post HTTP messages to");
            options.addOption("l", "log", true, "Path to logfiles");
            options.addOption("f", "file", true, "location of action file to parse");
            options.addOption("c", "class", true, "this is ignored");
            options.addOption("h", "help", false, "Prints this help message");

            CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse(options, args, true);

            if (cmd.hasOption("h")) {
                HelpFormatter hf = new HelpFormatter();
                hf.printHelp("ERENAction.jar", options);
                System.exit(1);
            }
            if (cmd.hasOption("p")) {
                serverPort = Integer.parseInt(cmd.getOptionValue("p"));
            }
            if (cmd.hasOption("u")) {
                endpoint = cmd.getOptionValue("u");
            }
            if (cmd.hasOption("l")) {
                String logfile = cmd.getOptionValue("l");
                FileHandler fh;
                try {
                    fh = new FileHandler(logfile + "action%g.log", 10000000, 5);
                    log.addHandler(fh);
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (cmd.hasOption("f")) {
                String fileName = cmd.getOptionValue("f");
                actionFile = readActionFile(fileName);
            }
            
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.log(Level.WARNING, "Caught Exception", e);
        } catch (FileNotFoundException fnf) {
            log.log(Level.SEVERE, "Couldn't read ActionFile", fnf);
            fnf.printStackTrace();
        }


        // Start the server
        messageProcessor = new InboundActionHttpController(this);
//        InboundHttpEndpoint server = new InboundHttpEndpoint(serverPort, new InboundMessageHandler(messageProcessor));
//        Thread st = new Thread(server, "EREN HTTP Inbound Endpoint");
//        //st.setDaemon(true);
//        st.start();


//        client = new OutboundHttpEndpoint(endpoint);
//        client.registerExtension(new EDXLRMExtensionFactory());
//        client.registerExtension(new StartupExtensionFactory());

        if (actionFile != null) {
            log.log(Level.INFO, actionFile.toString());
        }
    }

    public void act(ActionRequest act) {
        log.info("Received action message: " + act);
        if (ActionsEnum.CLOSE.toString().equals(act.getAction())) {
            closePod(act.getFacility());
        } else if (ActionsEnum.OPEN.toString().equals(act.getAction())) {
            openPod(act.getFacility());
        } else if (ActionsEnum.COMMIT.toString().equals(act.getAction())) {
            commitResource(act.getFacility(), act.getResource(), act.getQuantity());
        } else if (ActionsEnum.RELEASE.toString().equals(act.getAction())) {
            releaseResource(act.getFacility(), act.getResource(), act.getQuantity());
        }  else if (ActionsEnum.SETSTANDARDOFCARE.toString().equals(act.getAction())) {
            setSOC(act.getFacility(), act.getValueParam());
        } else {
            log.warning("Unknown action" + act);
        }
    }

    public final ActionsFile readActionFile(String actionFileName) throws FileNotFoundException {
        log.info("Processing Action File: " + actionFileName);
        File f = new File(actionFileName);

        Parser p = factory.newParser();
        Document<ActionsFile> doc = p.parse(new FileInputStream(f));
        ActionsFile aFile = doc.getRoot();
        return aFile;
    }


    public static WrappedERENModule initalizeWrapedObject(String[] args) {
        return new ActionManager(args);
    }


    public ResourceMessage makeResourceMessage(EDXLDistribution edxl,
            ResourceMessageType messageType) {
        ResourceMessage msg = client.attachElement(edxl, messageType.getQname());
        return msg;
    }


    private void closePod(String resource) {
        EDXLDistribution edxl =  client.makeEdxl(sender);
        ResourceMessage message = makeResourceMessage(edxl, ResourceMessageType.RELEASE_RESOURCE);
        ResourceInformation ri = message.addResourceInformation();
        Resource r = ri.addResource();
        r.setResourceID(resource);
        log.log(Level.INFO, edxl.toString());
        client.send(edxl);
    }

    private void openPod(String resource) {
        EDXLDistribution edxl =  client.makeEdxl(sender);
        ResourceMessage message = makeResourceMessage(edxl, ResourceMessageType.REQUISITION_RESOURCE);
        ContactInformationType contact =  message.addContactInformation();
        contact.setContactRole(ContactRoleType.REQUESTER);
        contact.setContactDescription("OpsChief");
        ResourceInformation ri = message.addResourceInformation();
        Resource r = ri.addResource();
        r.setResourceID(resource);
        ValueListType valueList = r.addTypeStructure();
        valueList.addValue(ResourceTypes.POD.toString());
        valueList.setValueListURN(DEFAULT_RESOURCE_TYPE_URN);
        log.log(Level.INFO, edxl.toString());
        client.send(edxl);
    }

    private void releaseResource(String facility, String resource, int qty) {
        EDXLDistribution edxl =  client.makeEdxl(sender);
        ResourceMessage message = makeResourceMessage(edxl, ResourceMessageType.RELEASE_RESOURCE);
        ContactInformationType contact =  message.addContactInformation();
        contact.setContactRole(ContactRoleType.REQUESTER);
        contact.setContactDescription("OpsChief");
        ResourceInformation ri = message.addResourceInformation();
        Resource r = ri.addResource();
        r.setName(resource);
        r.setResourceID(resource);
        ValueListType vlt = r.addTypeStructure();
        vlt.setValueListURN(DEFAULT_RESOURCE_TYPE_URN);
        vlt.addValue(resource);
        ScheduleInformation si = ri.addScheduleInformation();
        LocationType loc = si.addLocation();
        loc.setLocationDescription(facility);
        AssignmentInformation ai = ri.addAssignmentInformation();
        QuantityType qt = ai.addQuantity();
        MeasuredQuantity mq = qt.addMeasuredQuantity();
        mq.setAmount(new Double (qty));
        log.log(Level.INFO, edxl.toString());
        client.send(edxl);
    }

    private void commitResource(String facility, String resource, int qty) {
        EDXLDistribution edxl =  client.makeEdxl(sender);
        ResourceMessage message = makeResourceMessage(edxl, ResourceMessageType.COMMIT_RESOURCE);
        ContactInformationType contact =  message.addContactInformation();
        contact.setContactRole(ContactRoleType.REQUESTER);
        contact.setContactDescription("OpsChief");
        ResourceInformation ri = message.addResourceInformation();
        Resource r = ri.addResource();
        r.setName(resource);
        ValueListType vlt = r.addTypeStructure();
        vlt.setValueListURN(DEFAULT_RESOURCE_TYPE_URN);
        vlt.addValue(resource);
        ScheduleInformation si = ri.addScheduleInformation();
        LocationType loc = si.addLocation();
        loc.setLocationDescription(facility);
        AssignmentInformation ai = ri.addAssignmentInformation();
        QuantityType qt = ai.addQuantity();
        MeasuredQuantity mq = qt.addMeasuredQuantity();
        mq.setAmount(new Double (qty));
        log.log(Level.INFO, edxl.toString());
        client.send(edxl);

    }

    private void setSOC(String facility, int socLevel) {
        if (socLevel < 1 || socLevel > 6) {
            log.warning("Invalid Standard of Care: " + socLevel + "Must be between 1 and 6 inclusive");
            return;
        }
        EDXLDistribution edxl = client.makeEdxl(sender);
        SetStandardOfCare message = client.attachElement(edxl, StartupConstants.EREN_SETSTANDARDOFCARE);
        message.setPodId(facility);
        message.setStandardOfCare(socLevel);
        log.log(Level.INFO, edxl.toString());
        client.send(edxl);
    }

    public void setActionsFile(ActionsFile af) {
        actionFile = af;
    }


    @Override
    public MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }

    @Override
    public void setOutboundEndpoint(OutboundHttpEndpoint outbound) {
        client = outbound;
        client.registerExtension(new EDXLRMExtensionFactory());
        client.registerExtension(new StartupExtensionFactory());
    }

}
