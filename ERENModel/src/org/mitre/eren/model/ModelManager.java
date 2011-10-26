/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2010. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 

 *****************************************************************************/
package org.mitre.eren.model;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.Node;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.Parser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.scxml.env.AbstractStateMachine;
import org.apache.commons.scxml.model.SCXML;
import org.mitre.eren.http.OutboundHttpEndpoint;
import org.mitre.eren.message.MessageProcessor;
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.clock.ClockExtensionFactory;
import org.mitre.eren.protocol.clock.SetClockspeedWrapper;
import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.mitre.eren.protocol.edxl_rm.AssignmentInformation;
import org.mitre.eren.protocol.edxl_rm.ContactInformationType;
import org.mitre.eren.protocol.edxl_rm.ContactRoleType;
import org.mitre.eren.protocol.edxl_rm.DeploymentStatusType;
import org.mitre.eren.protocol.edxl_rm.DirectPositionType;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.mitre.eren.protocol.edxl_rm.EDXLRMExtensionFactory;
import org.mitre.eren.protocol.edxl_rm.ERENDatatypeFactory;
import org.mitre.eren.protocol.edxl_rm.IncidentInformationType;
import org.mitre.eren.protocol.edxl_rm.LocationType;
import org.mitre.eren.protocol.edxl_rm.MeasuredQuantity;
import org.mitre.eren.protocol.edxl_rm.NameElement;
import org.mitre.eren.protocol.edxl_rm.Occupation;
import org.mitre.eren.protocol.edxl_rm.OccupationElement;
import org.mitre.eren.protocol.edxl_rm.OccupationElementList;
import org.mitre.eren.protocol.edxl_rm.Occupations;
import org.mitre.eren.protocol.edxl_rm.OrganisationName;
import org.mitre.eren.protocol.edxl_rm.PartyNameType;
import org.mitre.eren.protocol.edxl_rm.PartyType;
import org.mitre.eren.protocol.edxl_rm.PersonName;
import org.mitre.eren.protocol.edxl_rm.PersonNameElementList;
import org.mitre.eren.protocol.edxl_rm.PointType;
import org.mitre.eren.protocol.edxl_rm.QuantityType;
import org.mitre.eren.protocol.edxl_rm.ReportResourceDeploymentStatus;
import org.mitre.eren.protocol.edxl_rm.ReportResourceDeploymentStatusWrapper;
import org.mitre.eren.protocol.edxl_rm.Resource;
import org.mitre.eren.protocol.edxl_rm.ResourceTypes;
import org.mitre.eren.protocol.edxl_rm.ResourceInformation;
import org.mitre.eren.protocol.edxl_rm.ResourceMessageWrapper;
import org.mitre.eren.protocol.edxl_rm.ResourceStatus;
import org.mitre.eren.protocol.edxl_rm.ScheduleInformation;
import org.mitre.eren.protocol.edxl_rm.ScheduleTypeType;
import org.mitre.eren.protocol.edxl_rm.ValueListType;
import org.mitre.eren.protocol.edxl_rm.WhereType;
import org.mitre.eren.protocol.scenario.Airport;
import org.mitre.eren.protocol.scenario.EOC;
import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.Equipment;
import org.mitre.eren.protocol.scenario.Facilities;
import org.mitre.eren.protocol.scenario.Hospital;
import org.mitre.eren.protocol.scenario.KMLLocation;
import org.mitre.eren.protocol.scenario.Location;
import org.mitre.eren.protocol.scenario.Medication;
import org.mitre.eren.protocol.scenario.POD;
import org.mitre.eren.protocol.scenario.PODEquipment;
import org.mitre.eren.protocol.scenario.People;
import org.mitre.eren.protocol.scenario.RSS;
import org.mitre.eren.protocol.scenario.ScenarioConstants;
import org.mitre.eren.protocol.scenario.ScenarioLocation;
import org.mitre.eren.protocol.scenario.Staff;
import org.mitre.eren.protocol.startup.PodStatus;
import org.mitre.eren.protocol.startup.Score;
import org.mitre.eren.protocol.startup.StartupConstants;
import org.mitre.eren.protocol.startup.StartupExtensionFactory;
import org.mitre.eren.wrapper.WrappedERENModule;
import org.mitre.javautil.logging.LoggingUtils;
import org.opencare.lib.model.edxl.ContentObject;
import org.opencare.lib.model.edxl.EDXLDistribution;
import org.opencare.lib.model.edxl.EmbeddedXMLContent;
import org.opencare.lib.model.edxl.XmlContent;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

/**
 *
 * @author JWINSTON
 */
public class ModelManager implements EDXLRMConstants, ClockConstants, 
        ScenarioConstants, StartupConstants, DialogueConstants,
        WrappedERENModule {

    public final int DEFAULT_STANDARD_OF_CARE = 1;
    private static final Logger log = LoggingUtils.getLogger(ModelManager.class);
    public LinkedBlockingQueue<Element> myQueue;
    private ERENscenario scenario;
    private SCXML podStateMachine;

    private long tick;
    private long lastStatusSent = 0;
    private final PriorityBlockingQueue<DelayedEvent> delayedEventQueue;
    private final LinkedBlockingQueue<Event> eventQueue;
    private HashMap<String, Pod> pods = new HashMap<String, Pod>();
    private ArrayList<Pod> openPods = new ArrayList<Pod>();
    //private HashMap<ResourceTypes, ModelResource> resources = new HashMap<ResourceTypes, ModelResource>();
    private ResourceHolder resourceHolder;
    private HashMap<String, ModelRss> rssList = new HashMap<String, ModelRss>();
    private Usps usps;
    private int toPODPercent = 60; // !!! Look in Rand document
    private ArrayList<Person> unassignedPatients = new ArrayList<Person>();
    private int numTreated = 0;
    private int numDead = 0;
    private int numSick = 0; // Sick is Prodromal or Fulminant, not Incubational
    private int numExposed = 0; //431970;
    private Random random = new Random();
    private OutboundHttpEndpoint client;
    private String sender = "model@erenbus.mitre.org";
    private MessageProcessor messageProcessor;

    /** Population numbers will be divided by this factor before being sent to
     * the Pods. The numbers returned by the Pods (sick, treated, dead) will be
     * multiplied by this factor before being sent on the bus in status messages. **/
    private int scaleFactor = 10;

    //Have we already set the clock speed faster?
    private boolean speededUpAlready = false;

    public void addToQueue(Element e) {
        //log.info("ADDed to Queue ");
        myQueue.offer(e);
    }

    public Element getFromQueue() {
        try {
            //log.info("Requested from Queue ");
            return myQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }



    public ModelManager(String[] args) {
        myQueue = new LinkedBlockingQueue<Element>();

            // parse the arguments
        int serverPort = 3737;
        String endpoint = "http://erenbus-gertner.mitre.org:3732/";

        try {
            Options options = new Options();

            options.addOption("p", "port", true, "Server port to listen on for incoming HTTP messages from the bus");
            options.addOption("u", "url", true, "Outbound URL to post HTTP messages to");
            options.addOption("l", "log", true, "Path to logfiles");
            options.addOption("s", "scale", true, "Population scale factor.  The populations from the scenario file are divided by this number");
            options.addOption("c", "class", true, "this is ignored");
            options.addOption("h", "help", false, "Prints this help message");

            CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse(options, args, true);

            if (cmd.hasOption("h")) {
                HelpFormatter hf = new HelpFormatter();
                hf.printHelp("ERENModel.jar", options);
                System.exit(1);
            }
            if (cmd.hasOption("p")) {
                serverPort = Integer.parseInt(cmd.getOptionValue("p"));
            }
            if (cmd.hasOption("u")) {
                endpoint = cmd.getOptionValue("u");
            }
            if (cmd.hasOption("s")) {
               scaleFactor =  Integer.parseInt(cmd.getOptionValue("s"));
            }
            if (cmd.hasOption("l")) {
                String logfile = cmd.getOptionValue("l");
                FileHandler fh;
                try {
                    fh = new FileHandler(logfile + "model%g.log", 10000000, 5);
                    log.addHandler(fh);
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.log(Level.WARNING, "Caught Exception", e);
        }

        // Start the server
        messageProcessor = new InboundModelHttpController(this);
//        InboundHttpEndpoint server = new InboundHttpEndpoint(serverPort, new InboundMessageHandler(messageProcessor));
//        Thread st = new Thread(server, "EREN HTTP Inbound Endpoint");
//        //st.setDaemon(true);
//        st.start();

        // build the client
        this.delayedEventQueue = new PriorityBlockingQueue<DelayedEvent>();
        this.eventQueue = new LinkedBlockingQueue<Event>();
        Thread qt = new Thread("EREN Model Event Queue") {
            @Override
            public void run() {
                while (true) {
                    try {
                        Event e = eventQueue.take();
                        e.getStateMachine().fireEvent(e.getEventName());
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        qt.start();

//        client = new OutboundHttpEndpoint(endpoint);
//        client.registerExtension(new EDXLRMExtensionFactory());
//        client.registerExtension(new StartupExtensionFactory());
        resourceHolder = new ResourceHolder(client, sender);
    }

    public int getScaleFactor() {
        return scaleFactor;
    }
    
    public void incrementSick() {
        numSick += scaleFactor;
    }
    
    public void incrementDead() {
        numSick -= scaleFactor;
        numDead += scaleFactor;
    }

    public void incrementTreated() {
        numTreated += scaleFactor;
    }

    public Pod assignToPod(Person person){
        int podNum = random.nextInt(openPods.size());
        Pod pod = openPods.get(podNum);
        return pod;
    }

    // Randomly reassign people to an open pod
    public void reassignPatients(List<Person> people) {
        int numOpen = openPods.size();
        if (numOpen > 0) {
            for (Person p : people) {
                int podNum = random.nextInt(numOpen);
                Pod pod = openPods.get(podNum);
                p.setPod(pod);
            }
        } else {
            unassignedPatients.addAll(people);
        }
    }

    public void sendMessage(Node outgoing) {
        EDXLDistribution edxl = client.makeEdxl(sender);
        attachMessage(edxl, outgoing);
        client.send(edxl);
    }

    /**
     * Convert the message into a string and then parse it using the abdera parser.
     * Is there a simpler way to do this?
     * @param edxl
     * @param outgoing
     */
    private void attachMessage(EDXLDistribution edxl, Node outgoing) {

        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

            DOMImplementationLS impl =
                    (DOMImplementationLS) registry.getDOMImplementation("LS");


            LSSerializer writer = impl.createLSSerializer();
            String str = writer.writeToString(outgoing);

            Abdera abdera = new Abdera();
            Parser parser = abdera.getParser();
            Document<Feed> feed_doc = parser.parse(new StringReader(str));
            ExtensibleElement content = feed_doc.getRoot();

            ContentObject co = edxl.addContentObject();
            XmlContent xc = co.setXmlContent();
            EmbeddedXMLContent exc = xc.addEmbeddedXMLContent();

            exc.addExtension(content);
        } catch (ClassCastException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public double getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
        fireDelayedEvents();

        // Send status every 15 minutes of game time
        boolean sendStatus = false;
        if (scenario != null && tick - lastStatusSent >= 15 * 60) {
            sendStatus = true;
            reportScore();
        }
        for (Pod pod : pods.values()) {
            pod.setTick(tick);
            if (sendStatus)
                reportPODStatus(pod.getId());
        }
        if (usps != null) {
            usps.setTick(tick);
        }
    }

    public void setPodOpen(Pod pod, boolean markAsOpen) {
        for (Pod currentPod:openPods) {
            if (pod == currentPod) {
                if (markAsOpen) {
                    // It's already open, nothing to do, but we shouldn't be here
                } else {
                    openPods.remove(pod);
                }
                return;
            }
        }
        if (markAsOpen) {
            openPods.add(pod);
        }
    }
    
    public void reportResourceStatus(String resourceID, String resourceName, String status, double quantity) {
        log.info("reportResourceStatus " + resourceID + " resourceName " + resourceName + " status " + status + " quantity " + quantity);
        EDXLDistribution edxl = client.makeEdxl(sender);

        ReportResourceDeploymentStatus rrds = client.attachElement(edxl, RM_REPORTRESOURCEDEPLOYMENTSTATUS);
        rrds.setMessageID("urn:au-qld-eoc:997967");
        // date
        java.util.GregorianCalendar today =
                new java.util.GregorianCalendar();// temp
        XMLGregorianCalendar dateTime = ERENDatatypeFactory.factory.newXMLGregorianCalendar(today.get(java.util.GregorianCalendar.YEAR),
                today.get(java.util.GregorianCalendar.MONTH) + 1,
                today.get(java.util.GregorianCalendar.DAY_OF_MONTH), 0, 0, 0, 0, 0);
        //
        rrds.setSentDateTime(dateTime);
        rrds.setOriginatingMessageID("urn:au-qld-eoc:12332");// FIXME:  need to get from request
        rrds.setPrecedingMessageID("urn:au-qld-eoc:997958");// not sure where this comes from
        IncidentInformationType ii = rrds.addIncidentInformation();
        ii.setIncidentDescription("Anthrax Release");// FIXME:  need to get from request
        ContactInformationType ci = rrds.addContactInformation();// may get this etc. from model do we need this?
        ci.setContactRole(ContactRoleType.SENDER);
        PartyType pt = ci.addAdditionalContactInformation();
        PartyNameType ptname = pt.addPartyName();
        PersonName pname = ptname.addPersonName();
        NameElement ne = pname.addNameElement();
        ne.setElementType(PersonNameElementList.FIRST_NAME);
        ne.setText("Charlotte");
        ne = pname.addNameElement();
        ne.setElementType(PersonNameElementList.LAST_NAME);
        ne.setText("Ryan");
        OrganisationName org = ptname.addOrganisationName();
        ne = org.addNameElement();
        ne.setElementType(PersonNameElementList.LAST_NAME);
        ne.setText("Norfolk EOC");
        Occupations occs = pt.addOccupations();
        Occupation occ = occs.addOccupation();
        OccupationElement occe = occ.addOccupationElement();
        occe.setType(OccupationElementList.ROLE);
        occe.setText("POD Manager");
        ci = rrds.addContactInformation();// requester FIXME: Get From Request
        ci.setContactRole(ContactRoleType.REQUESTER);
        pt = ci.addAdditionalContactInformation();
        ptname = pt.addPartyName();
        pname = ptname.addPersonName();
        ne = pname.addNameElement();
        ne.setElementType(PersonNameElementList.FIRST_NAME);
        ne.setText("John"); // FIXME: Get From Request
        ne = pname.addNameElement();
        ne.setElementType(PersonNameElementList.LAST_NAME);
        ne.setText("Smith"); // FIXME: Get From Request
        org = ptname.addOrganisationName();
        ne = org.addNameElement();
        ne.setElementType(PersonNameElementList.LAST_NAME);
        ne.setText("Norfolk EOC"); // FIXME: Get From Request
        occs = pt.addOccupations();
        occ = occs.addOccupation();
        occe = occ.addOccupationElement();
        occe.setType(OccupationElementList.ROLE);
        occe.setText("Unified Commander");  // FIXME: Get From Request
        // Resource Information
        ResourceInformation ri = rrds.addResourceInformation();
        ri.setResourceInfoElementID("001");
        Resource r = ri.addResource();
        r.setResourceID(resourceID);
        r.setName("resourceName");
        ValueListType vlt = r.addTypeStructure();
        vlt.setValueListURN(RM_RESOURCETYPES);
        vlt.addValue("Point of Distribution");// is this correct?
        r.setDescription("A facility to be used to distribute medications to the population");

        ResourceStatus rs = r.addResourceStatus();
        vlt = rs.addDeploymentStatus();
        vlt.setValueListURN(RM_DEPLOYMENTSTATUSTYPES);
        vlt.addValue(status);
        // is this correct?
        AssignmentInformation assgn = ri.addAssignmentInformation();
        QuantityType qt = assgn.addQuantity();
        MeasuredQuantity mq = qt.addMeasuredQuantity();
        mq.setAmount(quantity);
        ScheduleInformation si = ri.addScheduleInformation();
        si.setScheduleType(ScheduleTypeType.BEGIN_AVAILABLE);
        // now date FIXME: from model time
        dateTime = ERENDatatypeFactory.factory.newXMLGregorianCalendar(today.get(java.util.GregorianCalendar.YEAR),
                today.get(java.util.GregorianCalendar.MONTH) + 1,
                today.get(java.util.GregorianCalendar.DAY_OF_MONTH), 0, 0, 0, 0, 0);
        si.setDateTime(dateTime);
        LocationType lt = si.addLocation();//LocationType.RM_TARGETAREA
        WhereType wt = lt.addTargetArea();
        PointType point = wt.addPoint();
        DirectPositionType dpt = point.addPos();
        ArrayList<Double> myArr = new ArrayList<Double>();
        myArr.add(36.815278);
        myArr.add(-76.297222);
        dpt.setValue(myArr);
        client.send(edxl);
    }// reportResourceStatus(String resourceInfoElementID, String resourceID, String resourceName, String status, double quantity)

    public void receiveResourceStatusRequest(ReportResourceDeploymentStatusWrapper rrds) {
        for (ResourceInformation ri : rrds.getResourceInformation()) {
            Resource r = ri.getResource();
            String id = r.getResourceID();
            log.info("receiveResourceStatusRequest " + id);
            ValueListType vl = r.getTypeStructure();
            for (String type : vl.getValue()) {
                String vlUrn = vl.getValueListURN();
                log.info("Open" + vlUrn);
                for (String s : r.getResourceStatus().getDeploymentStatus().getValue()) {
                    if (s.equals(DeploymentStatusType.IN_USE)) {
                        log.info("openResource_to " + id);
//                        openResource(id);// use type??
                    } else if (s.equals(DeploymentStatusType.RELEASED))// is this corect?
                    {
                        log.info("ShutDownRRDS " + id);
//                        closeResource(id);// type?
                    } /*else if s.equals(RM_AVAILABLE)// National Guard
                    {

                    }*/ else if (vlUrn.equals("urn:eren:pod:soc")) {
                        AssignmentInformation assgn = ri.getAssignmentInformation();
                        QuantityType qt = assgn.getQuantity();
                        MeasuredQuantity mq = qt.getMeasuredQuantity();
                        double amount = mq.getAmount();
                        log.info("SOC " + id + " amount " + amount);
                    // setQualOfService(id, amount);
                    } else if (vlUrn.equals("Point of Distribution")) {
                        log.info("Open " + id);
                    }
                }
            }
        }
    }// String receiveResourceStatusRequest()

    public void reportScore() {
        /*<eren:score xmlns:eren="urn:mitre:eren:1.0">
        <eren:exposure>{#exposed}</eren:exposure>
        <eren:morbidity>{#sick}</eren:morbidity>
        <eren:mortality>{#dead}</eren:mortality>
        <eren:treated>{#treated}</eren:treated>
        </eren:score>*/

        EDXLDistribution edxl = client.makeEdxl(sender);
        Score score = client.attachElement(edxl, EREN_SCORE);
        score.setExposure(Integer.toString(numExposed));
        score.setMorbidity(Integer.toString(numSick));
        score.setMortality(Integer.toString(numDead));
        score.setTreated(Integer.toString(numTreated));
        client.send(edxl);
    }// reportScore()

    public void reportPODStatus(String podId) {
        log.info("Report POD Status " + podId);
        EDXLDistribution edxl = client.makeEdxl(sender);
        PodStatus status = client.attachElement(edxl, EREN_PODSTATUS);
        Pod pod = pods.get(podId);
        if (pod == null) {
            log.info("reportPODStatus unknown Pod " + podId);
            return;
        }
        status.setAttributeValue(DLG_PODID, podId);
        status.setHasEquipment(pod.getEquipment());
        status.setHasMeds(pod.getMeds());
        status.setStandardOfCare(pod.getStandardOfCare());
        status.setMedicalStaff(pod.getMedicalStaff());
        status.setSupportStaff(pod.getSupportStaff());
        status.setSecurityStaff(pod.getSecurityStaff());
        status.setPodQueueSize(pod.getQueueSize() * scaleFactor);
        if (pod.isInState("riot")) {
            status.setThroughput(0);
        } else {
            status.setThroughput(pod.getThroughput());
        }
        client.send(edxl);
    }

    public void setStandardOfCare(String podId, int standardOfCare) {
        Pod pod = pods.get(podId);
        if (pod == null) {
            log.info("setStandardOfCare unknown Pod " + podId);
            return;
        }
        pod.setStandardOfCare(standardOfCare);
    }

    public void commitResource(List<ResourceInformation> el) {
        for (ResourceInformation ri : el) {
            Resource r = ri.getResource();
            String resourceID = r.getResourceID();
            String name = r.getName();
            //log.info("name " + name);
            //log.info("resourceID[" + resourceID + "]");
            String resourceTypeString = r.getTypeStructure().getValue().get(0).trim();
            log.info(" resourceType= " + resourceTypeString);
            ResourceTypes resourceType = ResourceTypes.valueOf(resourceTypeString);
            AssignmentInformation assgn = ri.getAssignmentInformation();
            //log.info("AssignmentInformation " + assgn);
            QuantityType qt = assgn.getQuantity();
            //log.info("QuantityType " + qt);
            MeasuredQuantity mq = qt.getMeasuredQuantity();
            int amount = 1;
            if (mq != null) {
                amount = (int)mq.getAmount();
            }
            //log.info("MeasuredQuantity " + mq);

            //log.info("amount " + amount);
            List<ScheduleInformation> sil = ri.getScheduleInformation();
            //log.info("ScheduleInformation " + sil);
            for (ScheduleInformation si : sil) {

                LocationType locTyp = si.getLocation();
                //log.info("LocationType " + locTyp);
                String locDes = locTyp.getLocationDescription();
                //log.info("locDes " + locDes);
                if (resourceType.equals(ResourceTypes.USPS)) {
                    log.info("commitResource " +  resourceType);
                    if (usps == null) {
                        usps = new Usps(this);
                    }
                } else if (resourceID == null) {
                    log.info("commitResource " +  resourceType + " to " + locDes +  " amount=" + amount);
                    //log.info("name " + name);
                    if (locDes == null) {
                            activateResource(" ", resourceType, amount);
                        } else {
                            activateResource(locDes, resourceType, amount);
                        }
                } else if ((resourceID != null) && resourceID.length() > 0) {
                    Pod p = pods.get(resourceID);
                    if (p != null) {
                        p.fireEvent("activate");
                    }
                }

            }
        }
    }//commitResource()

    private void activateResource(String locDescription, ResourceTypes resourceType, int quantity) {
        
        if (locDescription.equals("USPS")) {
            if (usps == null) {
                // This should have been created when USPS was commited.
                usps = new Usps(this);
            }
            if (resourceType.equals(ResourceTypes.LETTER_CARRIER)) {
                int qtyAvailable = requestResource(ResourceTypes.LETTER_CARRIER, quantity) ;
                if (qtyAvailable > 0)
                    usps.addCarriers(qtyAvailable);
            } else if (resourceType.equals(ResourceTypes.LE_STAFF)) {
                int qtyAvailable = requestResource(ResourceTypes.LE_STAFF, quantity);
                if (qtyAvailable > 0)
                    usps.assignLawEnforcement(qtyAvailable);
            } else {
                log.log(Level.INFO," Don't know what to do with Resource {0} at USPS", new Object[]{resourceType});
                return;
            }

        }
        ModelRss rss = rssList.get(locDescription);
        // Is it a POD?
        Pod pod = pods.get(locDescription);
        if (pod != null) {
            if (resourceType.equals(ResourceTypes.EQUIPMENT_SET)) {
                pod.requestEquipment();
            } else {
                pod.requestStaff(resourceType, quantity);
            }
            log.info(" Resource name " + resourceType + " Activated at " + "Location: " + locDescription);
            // HACK!! Speed up the game if we are sitting and waiting
            if (!speededUpAlready && isWaiting()) {
                setClockSpeed(200);
            }
            return;
        } if (rss != null) {
            rss.requestStaff(resourceType, quantity);
            log.info(" Resource name " + resourceType + " Activated at " + "Location: " + locDescription);
            return;
        } else
            log.info("activateResource: resource not found " + locDescription);
        return;
    }

    void openPod(String podId) {

        log.info("Received Open POD message for " + podId);
        Pod pod = pods.get(podId);
        if (pod == null) {
            log.warning("Don't know about pod " + podId + "!");
            return;
        }

        // Send all the people who were kicked out of the last pod to this one
        if (unassignedPatients.size() > 0) {
            pod.addPeople(unassignedPatients);
        }
        unassignedPatients.clear();
        pod.fireEvent("open");
    }


    private void makePod(POD p) {
        String id = p.getID();
        String name = p.getName();

        int locPop = Integer.parseInt(p.getLocalPopulation()) / scaleFactor;
        int exposed = Integer.parseInt(p.getExposedPopulation());
        numExposed += exposed;
        int scaledExposed =  exposed / scaleFactor;

        // !!! standard of care needs to be added to scenario
        //standardOfCare = Integer.parseInt(p.getStandardOfCare());
        int standardOfCare = DEFAULT_STANDARD_OF_CARE;
        // Call create POD
        log.info("POD " + id + " name[ " + name + "] localPopulation= " + locPop + "exposedPopulation=" + exposed + "SOC= " + standardOfCare);
        Pod pod = null;
        if (podStateMachine == null) {
            pod = new Pod(this, id, name, locPop,
                    scaledExposed,standardOfCare, scaleFactor);
            podStateMachine = pod.getEngine().getStateMachine();
        } else {
            pod = new Pod( podStateMachine, this,id, name, locPop,
                    scaledExposed, standardOfCare, scaleFactor);
        }

        // Fix this!!!
        pod.startMedTimer();
        List<Staff> stf = p.getStaff();
        for (Staff per : stf) {
            String function = per.getFunction();
            log.info("Function " + function);
            String minp = per.getMin();
            String cur = per.getCurrent();
            pod.initPodStaff(function, Integer.parseInt(minp), Integer.parseInt(cur));
        }
        pods.put(id, pod);
    }

    public void releaseResource(ResourceMessageWrapper resourceMessage) {
        List<ResourceInformation> el = resourceMessage.getResourceInformation();
        for (ResourceInformation ri : el) {
            Resource r = ri.getResource();
            String resourceID = r.getResourceID();
            log.info("Release Resource " + resourceID);
            // Is it a pod?
            Pod pod = pods.get(resourceID);
            if (pod != null) {
                pod.fireEvent("close");
            } else {
                // No? Must be a resource that's assigned to a pod or RSS
                String resourceTypeString = r.getTypeStructure().getValue().get(0).trim();
                log.info(" resourceType= " + resourceTypeString);
                ResourceTypes resourceType = ResourceTypes.valueOf(resourceTypeString);
                AssignmentInformation assgn = ri.getAssignmentInformation();
                QuantityType qt = assgn.getQuantity();
                //log.info("QuantityType " + qt);
                MeasuredQuantity mq = qt.getMeasuredQuantity();
                int amount = 0;
                if (mq != null) {
                    amount = (int) mq.getAmount();
                } else {
                    // No point in continuing...
                    return;
                }
                List<ScheduleInformation> sil = ri.getScheduleInformation();
                for (ScheduleInformation si : sil) {
                    LocationType locTyp = si.getLocation();
                    //log.info("LocationType " + locTyp);
                    String locDes = locTyp.getLocationDescription();
                    // Pod or RSS?
                    pod = pods.get(locDes);
                    if (pod != null) {
                        pod.releaseResource(resourceType, amount);
                    } else {
                        ModelRss rss = rssList.get(locDes);
                        if (rss != null) {
                            rss.releaseResource(resourceType, amount);
                        } else {
                            log.info("Unknown facility: " + locDes);
                        }
                    }

                }
            }
        }
    }// releaseResource(Element e)

    public void setScenario(ERENscenario scen) {

        if (scenario != null) {
            return;
        }

        this.scenario = scen;
        ScenarioLocation cty = scenario.getScenarioLocation();
        log.info("Scenario Location " + cty);
        if (cty != null) {
            String name = cty.getName();
            String state = cty.getState();
            Location loc = cty.getLocation();
            log.info("Location " + loc);
            KMLLocation kmlLoc = loc.getKMLLocation();
            log.info("KMLLocation " + kmlLoc);
            String lat = kmlLoc.getLatitude();
            String lon = kmlLoc.getLongitude();
            if (lat == null) {
                lat = "36.91";
            }
            if (lon == null) {
                lon = "-76.201867";
            }
            int pop = Integer.parseInt(cty.getPopulation());
            //people = new ArrayList<Person>(pop);

            log.info("Location coordinates lat= " + lat + " lon= " + lon);
        }
  //      initCounty(name, state, Double.parseDouble(lon), Double.parseDouble(lat), Integer.parseInt(pop));
        // Facility
        Facilities fs = scenario.getFacilities();
        log.info("Facilities ");
        // POD
        List<POD> pds = fs.getPods();
        for (POD p : pds) {
            makePod(p);
        }
        List<Hospital> hs = fs.getHospitals();
        for (Hospital h : hs) {
            String name = h.getName();
            String status = h.getStatus();
            String id = h.getID();
            Location loc = h.getLocation();
            KMLLocation kmlLoc2 = loc.getKMLLocation();
            String lat2 = kmlLoc2.getLatitude();
            String lon2 = kmlLoc2.getLongitude();
            if (lat2 == null) {
                lat2 = "36.861527";
            }
            if (lon2 == null) {
                lon2 = "-76.302990";
            }
            String cap = h.getCapacity();
            String filled = h.getFilled();
            log.info("Hospital " + id + " name[ " + name + "] status[ " + status + "] lat= " + lat2 + "long= " + lon2 + "cap= " + cap + "filled=" + filled);
            // create hospital
//            Hospitals hos = initHospital(id, name, status, Integer.parseInt(avail),
//                    Double.parseDouble(lon2), Double.parseDouble(lat2), Integer.parseInt(cap), Integer.parseInt(filled));
            List<Staff> stf = h.getStaff();
            for (Staff per : stf) {
                String namehp = per.getFunction();
                String minp = per.getMin();
                String cur = per.getCurrent();
                // add staff type
//                hos.initPodStaff(namehp, Integer.parseInt(minp), Integer.parseInt(maxp), Integer.parseInt(cur));
            }
        }
        // EOC
        EOC eoc = fs.getEoc();
        String id = eoc.getID();
        String name = eoc.getName();
        String status = eoc.getStatus();
        Location loc3 = eoc.getLocation();
        KMLLocation kmlLoc3 = loc3.getKMLLocation();
        String lat3 = kmlLoc3.getLatitude();
        String lon3 = kmlLoc3.getLongitude();
        if (lat3 == null) {
            lat3 = "37.036984";
        }
        if (lon3 == null) {
            lon3 = "-76.385351";
        }
        log.info("EOC" + id + " name [" + name + "] status[ lat= " + lat3 + "long= " + lon3);
  //      initEOC(id, name, status, Integer.parseInt(avail), Double.parseDouble(lon), Double.parseDouble(lat));
        // Airport
        List<Airport> aps = fs.getAirports();
        log.info("Airport ");
        for (Airport a : aps) {
            String ida = a.getID();
            String s = a.getStatus();
            Location loc = a.getLocation();
            KMLLocation kmlLoc4 = loc.getKMLLocation();
            String lat4 = kmlLoc4.getLatitude();
            String lon4 = kmlLoc4.getLongitude();
            if (lat4 == null) {
                lat4 = "36.898670";
            }
            if (lon4 == null) {
                lon4 = "-76.205764";
            }
            log.info("Airport= " + ida + " status " + status + " lat= " + lat4 + " long= " + lon4);
//            initAirport(ida, status, Integer.parseInt(avail), Double.parseDouble(lat4), Double.parseDouble(lon4));
        }
        // RSS
        List<RSS> rss = fs.getRSSes();
        for (RSS r : rss) {
            String idrss = r.getID();
            status = r.getStatus();
            Location locrss = r.getLocation();
            KMLLocation kmlLoc4 = locrss.getKMLLocation();
            String lat4 = kmlLoc4.getLatitude();
            String lon4 = kmlLoc4.getLongitude();
            if (lat4 == null) {
                lat4 = "36.898670";
            }
            if (lon4 == null) {
                lon4 = "-76.205764";
            }
            
            rssList.put(idrss, new ModelRss(this, idrss, status, Double.parseDouble(lat4), Double.parseDouble(lon4)));
        }
        // Vehicles
        // Handle people
        People people = scenario.getPeople();
        List<Staff> staff = people.getStaff();
        for (Staff per : staff) {
            String idsf = per.getID();
            status = per.getStatus();
            String quantity = per.getQuantity();
            if (quantity == null) {
                quantity = "0";
                log.info("Quantity for Staff input incorrectly set to 0");
            }
            String function = per.getFunction();
            log.info("initPeople id= " + idsf + " status= " + status + " function " + function + " quantity " + quantity);
            if (function.equals(ResourceTypes.CLINICAL_STAFF.toString())
                    || function.equals(ResourceTypes.LE_STAFF.toString())
                    || function.equals(ResourceTypes.OPS_STAFF.toString())
                    || function.equals(ResourceTypes.LETTER_CARRIER.toString())) {
                ResourceTypes resourceType = ResourceTypes.valueOf(function);
                resourceHolder.add(resourceType, Integer.parseInt(quantity));
            } else {
                log.info("Not dealing with " + function);
            }

        }
        // handle equipment
        Equipment eqp = scenario.getEquipment();
        PODEquipment pde = eqp.getPODEquipment();
        String ideq = pde.getID();
        Location loc = pde.getLocation();
        String fac = loc.getFacility();
        log.info("Facility= " + fac);
        String quantity = pde.getQuantity();
        log.info("initEquipment id= " + ideq + "facility= " + fac + " quantity " + quantity);
        //ModelResource equipmentResource = new ModelResource(ResourceTypes.EQUIPMENT_SET,Integer.parseInt(quantity));
        resourceHolder.add(ResourceTypes.EQUIPMENT_SET, Integer.parseInt(quantity));
//        initEquipment(ideq, Integer.parseInt(avail), fac, Double.parseDouble(quantity));
        // handle Medication
        Medication meds = eqp.getMedication();
        String medid = meds.getID();
        status = meds.getStatus();
        loc = meds.getLocation();
        String fac1 = loc.getFacility();
        String quanmeds = meds.getQuantity();
        log.info("initMeds id= " + medid + " status= " + status + " fac1 " + fac1);
        //ModelResource medicationResource = new ModelResource(ResourceTypes.MEDICATION,Integer.parseInt(quanmeds));
        //resources.put(ResourceTypes.MEDICATION, medicationResource);
//        initMeds(medid, Integer.parseInt(avail), fac1, Double.parseDouble(quanmeds));
    }


    /**
     * Steal people a random pod to give to a letter carrier
     * @param population
     * @return
     */
    public List<Person> requestPeople(int population) {
        // Get people from a random pod
        Pod[] tmpPods = pods.values().toArray(new Pod[0]);
        int randomPodIndex = random.nextInt(pods.size());
        Pod luckyPod = tmpPods[randomPodIndex];
        return luckyPod.stealPeople(population);
    }



    /**
     *
     * @param resource
     * @param amountRequested
     * @return the amount of the resource granted
     */
    public int requestResource(ResourceTypes resource, int amountRequested) {

        return resourceHolder.request(resource, amountRequested);
    }
    

    /**
     * @param resource
     * @param amountRequested
     */
    public void releaseResource(ResourceTypes resource, int amountRequested) {

        resourceHolder.release(resource, amountRequested);
    }

    /**
     * Are we waiting for timers to expire?
     * If we have pods that are being activated (they are waiting for people,
     * equipment, medications), and each of these has some staff, then we can
     * speed up the clock
     */
    private boolean isWaiting() {
        int beingActivatedPods = 0;
        for (Pod p : pods.values()) {
            if (p.isInState("beingActivated")) {
                beingActivatedPods++;
                if (!p.hasSomeStaff()) {
                    return false;
                }
            }
        }
        if (beingActivatedPods > 0) {
            //there are pods in this state and they each have some staff
            return true;
        }
        return false;
    }

    public void setClockSpeed(int ratio) {
        log.info("Setting clock ratio to " + ratio + ":1");
        speededUpAlready = true;
        EDXLDistribution edxl = client.makeEdxl(sender);
        SetClockspeedWrapper message = client.attachElement(edxl, ClockConstants.EREN_SETCLOCKSPEED);
        message.setRatio(ratio);
        client.send(edxl);
    }

    public void queueDelayedEvent(String event, AbstractStateMachine stateMachine, long time) {
        log.info("queueDelayedEvent: event " + event + " will fire after game time=" + (tick+time) + " (now is " + tick +")");
        DelayedEvent e = new DelayedEvent(event, stateMachine, tick + time);
        delayedEventQueue.offer(e);
    }


    /**
     * Take all events that are due based on the current time tick and
     * queue them in the event queue so that they will be processed on the
     * event processing thread.
     */
    private void fireDelayedEvents() {
        while (delayedEventQueue.peek() != null && delayedEventQueue.peek().getTime() <= tick) {
            DelayedEvent p = delayedEventQueue.poll();
            queueEvent(p.getEventName(), p.getStateMachine());
        }
    }

    /**
     * Add an event to the queue to be fired on the model
     * event processing thread
     * @param event
     */
    public void queueEvent(String event) {
        queueEvent(event, null);
    }
    /**
     * Add an event with a stateMachine to the queue to be fired on the model
     * event processing thread
     * @param event
     * @param stateMachine
     */
    public void queueEvent(String event, AbstractStateMachine stateMachine) {
        eventQueue.offer(new Event(event, stateMachine));
    }


    public static WrappedERENModule initalizeWrapedObject(String[] args) {
        return new ModelManager(args);
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
        client.registerExtension(new ClockExtensionFactory());
        resourceHolder.setOutboundHttpEndpoint(client);
    }


   // Stolen from DialogueManager and modified
   private class Event {
       String eventName;
       AbstractStateMachine stateMachine;


       public Event(String eventName, AbstractStateMachine stateMachine) {
           super();
           this.eventName = eventName;
           this.stateMachine = stateMachine;
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
        * @return the stateMachine
        */
       public AbstractStateMachine getStateMachine() {
           return stateMachine;
       }
       /**
        * @param stateMachine the stateMachine to set
        */
       public void setPayload(AbstractStateMachine stateMachine) {
           this.stateMachine = stateMachine;
       }


  }

   private class DelayedEvent extends Event implements Comparable {
        long time;

        public DelayedEvent(String eventName, AbstractStateMachine stateMachine, long time) {
            super(eventName, stateMachine);
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

}// class ModelManager implements EDXLRMConstants,ClockConstants, ScenarioConstants, StartupConstants

