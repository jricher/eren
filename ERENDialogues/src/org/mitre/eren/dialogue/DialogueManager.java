package org.mitre.eren.dialogue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.digester.Digester;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.Evaluator;
import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.env.SimpleDispatcher;
import org.apache.commons.scxml.env.SimpleErrorHandler;
import org.apache.commons.scxml.env.SimpleErrorReporter;
import org.apache.commons.scxml.env.jexl.JexlContext;
import org.apache.commons.scxml.env.jexl.JexlEvaluator;
import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.model.CustomAction;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;

import org.opencare.lib.model.edxl.EDXLDistribution;

import org.mitre.eren.dialogue.custom.DuplicateSC;
import org.mitre.eren.dialogue.custom.EvalExpression;
import org.mitre.eren.dialogue.custom.SendOpenPod;
import org.mitre.eren.dialogue.custom.SendResourceMessage;
import org.mitre.eren.dialogue.custom.SendUserMessage;
import org.mitre.eren.dialogue.custom.WaitTimer;
import org.mitre.eren.http.InboundHttpEndpoint;
import org.mitre.eren.http.InboundMessageHandler;
import org.mitre.eren.http.OutboundHttpEndpoint;
import org.mitre.eren.message.MessageProcessor;
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.clock.ClockExtensionFactory;
import org.mitre.eren.protocol.clock.SetClockspeed;
import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.mitre.eren.protocol.dialogue.DialogueExtensionFactory;
import org.mitre.eren.protocol.dialogue.UserMessage;
import org.mitre.eren.protocol.edxl_rm.CommitResource;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.mitre.eren.protocol.edxl_rm.EDXLRMExtensionFactory;
import org.mitre.eren.protocol.edxl_rm.ReleaseResource;
import org.mitre.eren.protocol.edxl_rm.ReportResourceDeploymentStatus;
import org.mitre.eren.protocol.edxl_rm.RequestResource;
import org.mitre.eren.protocol.edxl_rm.RequisitionResource;
import org.mitre.eren.protocol.edxl_rm.ResourceInformation;
import org.mitre.eren.protocol.edxl_rm.ResourceMessage;
import org.mitre.eren.protocol.edxl_rm.ResourceTypes;
import org.mitre.eren.protocol.kml.KMLExtensionFactory;
import org.mitre.eren.protocol.scenario.Airport;
import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.Hospital;
import org.mitre.eren.protocol.scenario.NPC;
import org.mitre.eren.protocol.scenario.POD;
import org.mitre.eren.protocol.scenario.People;
import org.mitre.eren.protocol.scenario.RSS;
import org.mitre.eren.protocol.scenario.ScenarioExtensionFactory;
import org.mitre.eren.protocol.scenario.Staff;
import org.mitre.eren.wrapper.WrappedERENModule;
import org.mitre.javautil.logging.LoggingUtils;

import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public class DialogueManager implements DialogueConstants, EDXLRMConstants,
DMConstants,WrappedERENModule,ClockConstants {

  // Game state variables
  /**
   * Scenario object
   */
  ERENscenario scenario;

  /** 
   * Game started flag
   */
  private boolean gameStarted = false;
  
  /**
   * The date associated with clock tick 0
   */
  private Date startDate;

  /**
   * The last time tick received
   */
  private long tick;

  /**
   * Queue to keep track of events that should fire at a certain time
   */
  private final PriorityBlockingQueue<DelayedEvent> delayedEventQueue;

  /**
   * Queue to process events that need to be fired immediately
   */
  private final LinkedBlockingQueue<Event> eventQueue;

  /**
   * Map of Queues of stored events that need to be processed by a specific NPC
   */
  private final Map<String, Queue<Event>> npcQueueMap;


  private String dialoguePath = "/opt/eren/dialog/dialogues/npcDialogues";

  /**
   * The state machine that will drive the instances of this class.
   */
  private SCXML stateMachine;

  /**
   * The SCXML engines corresponding to each NPC.
   */
  private Map<String,SCXMLExecutor> engines;
  
  private List<String> liveDialogues;

  private static Logger log = LoggingUtils.getLogger(DialogueManager.class);

  /**
   * The method signature for the activities corresponding to each state in the
   * SCXML document.
   */
  private static final Class[] SIGNATURE = new Class[0];

  /**
   * The method parameters for the activities corresponding to each state in the
   * SCXML document.
   */
  private static final Object[] PARAMETERS = new Object[0];

  private boolean shutdown = false;

  private OutboundHttpEndpoint client;
  private String sender = "dialogue@erenbus.mitre.org";
  private MessageProcessor mp;

  /**
   * Primary constructor, object instantiation incurs parsing cost.
   * 
   * @param scxmlDocument
   *          The URL pointing to the SCXML document that describes the
   *          &quot;lifecycle&quot; of the instances of this class.
   * @param rootCtx
   *          The root context for this instance.
   * @param evaluator
   *          The expression evaluator for this instance.
   * 
   * @see Context
   * @see Evaluator
   */
  public DialogueManager(String[] args) {

    FileHandler fh = null;
    try { 
      fh = new FileHandler("DialogueLog.txt"); 
      log.addHandler(fh);
    } catch (IOException e) { 
      log.warning("Couldn't add log file DialogueLog.txt");
    }


    // parse the arguments
    int serverPort = 3737;
    String endpoint = "http://erenbus.mitre.org:3732/";

    try {
      Options options = new Options();

      options.addOption("p", "port", true,
      "Server port to listen on for incoming HTTP messages from the bus");
      options.addOption("u", "url", true,
      "Outbound URL to post HTTP messages to");
      options.addOption("s", "sender", true,
      "Identifier for messages sent to the bus");
      options.addOption("d", "dialogue", true,
      "path to directory that contains the dialogue definition files");
      options.addOption("c", "class", true, "this is ignored");
      options.addOption("h", "help", false, "Prints this help message");


      CommandLineParser parser = new PosixParser();
      CommandLine cmd = parser.parse(options, args, true);

      if (cmd.hasOption("h")) {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("ERENDialogue.jar", options);
        System.exit(1);
      }
      if (cmd.hasOption("p")) {
        serverPort = Integer.parseInt(cmd.getOptionValue("p"));
      }
      if (cmd.hasOption("u")) {
        endpoint = cmd.getOptionValue("u");
      }
      if (cmd.hasOption("s")) { 
        sender = cmd.getOptionValue("s");
      }
      if (cmd.hasOption("d")) { 
        dialoguePath = cmd.getOptionValue("d");
      }

    } catch (ParseException e) {
      // TODO Auto-generated catch block
      log.log(Level.WARNING, "Caught Exception", e);
    }
    // start the server

    mp = new InboundDialogueHttpController(this,fh);
    //InboundHttpEndpoint server = new InboundHttpEndpoint(serverPort,
    //    new InboundMessageHandler(new InboundDialogueHttpController(this,fh)));

    //log.info("DM starting inbound endpoint");
    //Thread st = new Thread(server, "EREN HTTP Inbound Endpoint");
    // st.setDaemon(true);
    //st.start();

    // build the client
    log.info("DM sending to " + endpoint);

    this.npcQueueMap = new HashMap<String, Queue<Event>>();

    this.eventQueue = new LinkedBlockingQueue<Event>();

    this.engines = new HashMap<String,SCXMLExecutor>();
    
    this.liveDialogues = new ArrayList<String>();

    this.delayedEventQueue = new PriorityBlockingQueue<DelayedEvent>();

  }

  public static WrappedERENModule initalizeWrapedObject(String[] args) {
    return new DialogueManager(args);
  }
  
  public MessageProcessor getMessageProcessor () { 
    return mp;
  }

  public void setOutboundEndpoint (OutboundHttpEndpoint outbound) { 
    client = outbound;
    client.registerExtension(new EDXLRMExtensionFactory());
    client.registerExtension(new DialogueExtensionFactory());
    client.registerExtension(new EDXLRMExtensionFactory());
    client.registerExtension(new ClockExtensionFactory());
    client.registerExtension(new ScenarioExtensionFactory());
    client.registerExtension(new KMLExtensionFactory());
  }
  
  /** 
   * Creates a new state machine for the NPC with the given id. This will 
   * be triggered whenever an existing live state machine leaves its initial state
   * so that new messages for the same NPC can be processed. 
   * @param id
   */
  public void createStateMachine (String id) { 
    NPC npc = getNPC(id);
    initializeNpcStateMachine(npc);
  }
  
  public void dialogueStarted (String scID) { 
    sendSetClockSpeed(CLOCKSPEED_SLOW);
    liveDialogues.add(scID);
  }
  
 private void setConstants (Map<String,Object> contextMap) {
   contextMap.put("PRIORITY_NORMAL", PRIORITY_URGENT);
   contextMap.put("PRIORITY_ENHANCED", PRIORITY_URGENT);
   contextMap.put("PRIORITY_URGENT", PRIORITY_URGENT);
   contextMap.put("APOS", "'");
 }
  
  private void createNpcStateMachine (String statechart, Map<String,String> ctx, String id) { 
    // Create State Chart

    Map<String, Object> contextMap = new HashMap<String, Object>(ctx);
    contextMap.put("dm", this);
    contextMap.put("scID", id);
    setConstants(contextMap);

    final Context rootCtx = new JexlContext(contextMap);
    final Evaluator evaluator = new JexlEvaluator();
    ErrorHandler errHandler = new SimpleErrorHandler();
    try {

      List<CustomAction> customActions = new ArrayList<CustomAction>();
      customActions.add(new CustomAction("urn:mitre:eren:dlg:custom:1.0",
          "sendUserMessage", SendUserMessage.class));
      customActions.add(new CustomAction("urn:mitre:eren:dlg:custom:1.0",
          "sendResourceMessage", SendResourceMessage.class));
      customActions.add(new CustomAction("urn:mitre:eren:dlg:custom:1.0",
          "sendOpenPod", SendOpenPod.class));
      customActions.add(new CustomAction("urn:mitre:eren:dlg:custom:1.0",
          "startTimer", WaitTimer.class));
      customActions.add(new CustomAction("urn:mitre:eren:dlg:custom:1.0",
          "eval", EvalExpression.class));
      customActions.add(new CustomAction("urn:mitre:eren:dlg:custom:1.0",
          "duplicateSC", DuplicateSC.class));


      File scxmlFile = new File(dialoguePath + File.separator + statechart + ".xml"); 
      //URL scxmlDocument = getClass().getClassLoader().getResource(
      //);
      Digester dig = SCXMLParser.newInstance(null, null, customActions);
      dig.setNamespaceAware(true);
      dig.setXIncludeAware(true);
      dig.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris",
          false);
      dig.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
      // dig.setFeature("http://xml.org/sax/features/namespaces", true); //
      // default is true
      stateMachine = (SCXML) dig.parse(scxmlFile);

      SCXMLParser.updateSCXML(stateMachine);

    } catch (ParserConfigurationException pce) {
      logError(pce);
    } catch (IOException ioe) {
      logError(ioe);
    } catch (SAXException sae) {
      logError(sae);
    } catch (ModelException me) {
      logError(me);
    }
    // This starts up the state machine
    initialize(stateMachine, rootCtx, evaluator, id);
    
  }

  /**
   * Instantiate and initialize the underlying executor instance.
   * 
   * @param stateMachine
   *          The state machine
   * @param rootCtx
   *          The root context
   * @param evaluator
   *          The expression evaluator
   */
  private void initialize(final SCXML stateMachine, final Context rootCtx,
      final Evaluator evaluator, String id) {
    SCXMLExecutor engine = new SCXMLExecutor(evaluator, new DialogueDispatcher(this),
        new SimpleErrorReporter());

    engine.setStateMachine(stateMachine);
    engine.setSuperStep(true);
    engine.setRootContext(rootCtx);

    engine.addListener(stateMachine, new StateChartListener(this));

    // Add this engine to the global engine list
    engines.put(id,engine);

    try {
      engine.go();
    } catch (ModelException me) {
      logError(me);
    }
  }

  public void startGame () {
    if (!gameStarted) { 
      Thread qt = new Thread("EREN Dialogue Event Queue") {
        public void run() {
          while (true) {
            try {
              Event e = eventQueue.take();
              fireEvent(e.getEventName(), e.getPayload(), e.getTarget());
            } catch (InterruptedException e) {
            }
          }
        }
      };
      qt.start();

      queueEvent("patients.arrive", "hospital_01");

      queueDelayedEvent("initial.questions", null, 15 * 60);
      queueDelayedEvent("pushpack.arrival", null, 60 * 60 * 12);
      queueDelayedEvent("hour.one.questions", null, 60 * 60);
      queueDelayedEvent("mayor.wants.pod", "POD_03", 60 * 60);
      queueDelayedEvent("fema.support.offered",null,70*60);
      queueDelayedEvent("bioagent.cocktail", null, 60 * 60 * 2);
      queueDelayedEvent("fake.cipro", null, 3 * 60 * 60);

      gameStarted = true;
    }
  }

  /**
   * Add an event to the queue to be fired on the dialogue event processing
   * thread
   * 
   * @param event
   */
  public void queueEvent(String event) {
    queueEvent(event, null);
  }

  /**
   * Add an event with a payload to the queue to be fired on the dialogue event
   * processing thread
   * 
   * @param event
   * @param payload
   */
  public void queueEvent(String event, Object payload) {
    eventQueue.offer(new Event(event, payload));
  }

  /**
   * Add an event with a payload and target to the queue to be fired 
   * on the dialogue event processing thread
   * 
   * @param event
   * @param payload
   * @param target
   */
  public void queueEvent(String event, Object payload, String target) {
    eventQueue.offer(new Event(event, payload, target));
  }



  /**
   * Fire an event on the SCXML engine.
   * 
   * @param event
   *          The event name.
   * @return Whether the state machine has reached a &quot;final&quot;
   *         configuration.
   */
  public void fireEvent(final String event) {
    fireEvent(event, null, (String) null);
  }

  /**
   * Fire an event on the SCXML engine.
   * 
   * @param event
   *          The event name.
   * @param payload
   *          The payload to be sent with the event
   * @param target
   *          The SCXML engine to receive the event
   * @return Whether the state machine has reached a &quot;final&quot;
   *         configuration.
   */
  public void fireEvent(final String event, final Object payload, final String target) {
    log.info("Firing event: " + event);
    if (payload != null) {
      log.info("payload = " + payload);
    }
    if (target != null) { 
      log.info("target = " + target);
    }

    for (Entry<String,SCXMLExecutor> engine : new HashSet<Entry<String,SCXMLExecutor>>(engines.entrySet())) {
      // Since there may be multiple engines for the same NPC we will send 
      // the message to all the state charts with a prefix that matches the target id
      String enginePrefix = engine.getKey().substring(0,engine.getKey().lastIndexOf(':'));
      if (target == null || Arrays.asList(target.split(",\\s*")).contains(enginePrefix)) {
        // fireEvent returns true if firing the event results in the state chart
        // transitioning to a final state. In this case, the engine should be removed
        // so that no future events will be fired on it. 
        if (fireEvent(event,payload,engine.getValue())) { 
          engines.remove(engine.getKey());
          // If this was the last live dialogue engine, set the clock speed
          // to fast so the time will go by faster
          liveDialogues.remove(engine.getKey());
          if (liveDialogues.isEmpty()) { 
            sendSetClockSpeed(CLOCKSPEED_FAST);
          }
        }
      }
    }
  }

  private boolean fireEvent(final String event, final Object payload, final SCXMLExecutor engine) {
    TriggerEvent[] evts = { new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT,
        payload) };
    try {
      engine.triggerEvents(evts);
    } catch (ModelException me) {
      logError(me);
    }
    return engine.getCurrentStatus().isFinal();
  }

  public void queueDelayedEvent(String event, Object payload, long time) {
    queueDelayedEvent(event,payload,null,time);
  }
    
  public void queueDelayedEvent(String event, Object payload, String target, long time) {
    DelayedEvent e = new DelayedEvent(event, payload, target, tick + time);
    delayedEventQueue.offer(e);
  }

  /**
   * Take all events that are due based on the current time tick and queue them
   * in the event queue so that they will be processed on the event processing
   * thread.
   */
  private void fireDelayedEvents() {
    while (delayedEventQueue.peek() != null
        && delayedEventQueue.peek().getTime() <= tick) {
      DelayedEvent p = delayedEventQueue.poll();
      queueEvent(p.getEventName(), p.getPayload());
    }
  }


  /**
   * Get the SCXML engine driving the &quot;lifecycle&quot; of the instances of
   * this class.
   * 
   * @return Returns the engine.
   */
  public Map<String,SCXMLExecutor> getEngines() {
    return engines;
  }

  /**
   * Get the log for this class.
   * 
   * @return Returns the log.
   */
  public Logger getLog() {
    return log;
  }

  /**
   * Set the log for this class.
   * 
   * @param log
   *          The log to set.
   */
  public void setLog(final Logger log) {
    this.log = log;
  }


  public void resetMachines () { 
    for (SCXMLExecutor engine : engines.values()) { 
      boolean success = resetMachine(engine);
      if (!success) { 
        log.warning("Failed to reset state machine " + engine);
      }
    }
  }

  /**
   * Reset the state machine.
   * 
   * @return Whether the reset was successful.
   */
  public boolean resetMachine(SCXMLExecutor engine) {
    try {
      engine.reset();
    } catch (ModelException me) {
      logError(me);
      return false;
    }
    return true;
  }

  private void sendSetClockSpeed(Integer speed) {
    EDXLDistribution edxl = client.makeEdxl(sender);
    attachSpeed(edxl,speed);
    
    //edxl.addSenderRole(urnGID, gameID);
    client.send(edxl);
  }

  /**
     * @param edxl
     */
    private void attachSpeed(EDXLDistribution edxl, Integer s) {
    SetClockspeed speed = client.attachElement(edxl, EREN_SETCLOCKSPEED);
    speed.setRatio(s);
    }
  

  
  /**
   * Utility method for logging error.
   * 
   * @param exception
   *          The exception leading to this error condition.
   */
  protected void logError(final Exception exception) {
    log.log(Level.WARNING, exception.getMessage(), exception);
  }

  public void sendMessage(Node outgoing, String addressee) {
    EDXLDistribution edxl = client.makeEdxl(sender);
    if (addressee != null) {
      edxl.addExplicitAddress("eren:role", addressee);
    }

    client.attachXML(edxl, outgoing);

    client.send(edxl);
  }

  public void sendMessage(EDXLDistribution edxl) {
    log.info("DM sending message: " + edxl);
    client.send(edxl);
  }

  public OutboundHttpEndpoint getClient () { 
    return client;
  }

  private State getCurrentState(SCXMLExecutor engine) {
    Set<State> states = engine.getCurrentStatus().getStates();
    return states.iterator().next();
  }

  public boolean isActiveState(String stateID, String npcID) {
    return isActiveState(stateID, engines.get(npcID));
  }

  @SuppressWarnings("unchecked")
  public boolean isActiveState(String stateID, SCXMLExecutor engine) {
    //log.info("Checking if state is active: " + stateID);
    Set<State> states = engine.getCurrentStatus().getStates();
    for (State s : states) {
      //log.info("Active state: " + s.getId());
      if (s.getId().equals(stateID)) {
        return true;
      }
    }
    return false;
  }

  public String nextMessageId(String sourceid) {
    String id = IdFactory.getNextMessageId(sourceid);
    return id;
  }

  public void setStartDate(Date date) {
    this.startDate = date;
  }

  public void setTick(long tick) {
    this.tick = tick;
    fireDelayedEvents();
  }

  public long getTick() {
    return tick;
  }

  /**
   * Gets the formatted time string associated with the current time tick
   * 
   * @return
   */
  public String getCurrentDate() {
    return getDateString(tick);
  }

  /**
   * Gets the formatted time string associated with the given number of seconds
   * since game start
   * 
   * @param time
   * @return
   */
  private String getDateString(long time) {
    long startTime = startDate.getTime();
    long newTime = startTime + (time * 1000);
    Date newDate = new Date(newTime);
    return formatDate(newDate);
  }

  private String formatDate(Date d) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",
        new Locale("en", "US"));

    StringBuffer dateBuff = new StringBuffer();
    dateBuff.append(format.format(d));

    dateBuff.insert(dateBuff.length() - 2, ':');
    return dateBuff.toString();
  }


  public void addNpcEvent(String owner, String name, Object payload) {
    System.out.println("Adding event " + name + " for " + owner + " with payload " + payload); 
    Event event = new Event(name, payload);
    Queue<Event> queue = npcQueueMap.get(owner);
    if (queue == null) {
      queue = new LinkedList<Event>();
      npcQueueMap.put(owner, queue);
    }
    queue.offer(event);
  }

  public void addNpcEvents(String owner, Map<String,Object> eventmap) {
    for (Entry<String,Object> eventdata : eventmap.entrySet()) {
      addNpcEvent(owner,eventdata.getKey(),eventdata.getValue());
    }
  }

  public Event getNpcEvent(String owner) {
    Event e = npcQueueMap.get(owner).poll();
    if (e != null) return e; 
    else return new Event("",null);
  }

  /**
   * Null safe way of getting name for Event
   * 
   * @return
   */
  public String getEventName(Event event) {
    if (event == null)
      return "";
    else
      return event.getEventName();
  }

  public Object getEventPayload(Event event) {
    if (event == null)
      return null;
    else
      return event.getPayload();
  }

  public void loadScenario(ERENscenario scenario) {
    if (this.scenario == null) { 
      this.scenario = scenario;
    }

    for (NPC npc : scenario.getPeople().getNPC()) { 
      initializeNpcStateMachine(npc);
    }
  }
  
  private NPC getNPC (String id) { 
    for (NPC npc : scenario.getPeople().getNPC()) { 
      if (npc.getID().equals(id)) { 
        return npc;
      }
    }
    return null;
  }
  
  private void initializeNpcStateMachine (NPC npc) { 
    String statechart = npc.getStatechart();
    Map<String,String> ctx = new HashMap<String,String>();
    ctx.put("msgSrc", npc.getID());
    ctx.put("incident", scenario.getName());
    ctx.put("EOCname", scenario.getScenarioLocation().getName() + " EOC");

    if (statechart.equals("POD_OpsMgr")) { 
      ctx.put("podID", npc.getFacility());
    }

    if (statechart.equals("HospitalMgr")) { 
      ctx.put("hospitalID", npc.getFacility());
    }

    if (statechart.equals("RSS_OpsMgr")) { 
      ctx.put("rssID", npc.getFacility());
    }

    if (statechart.equals("POD_Owner")) { 
      ctx.put("podID", npc.getFacility());
    }

    createNpcStateMachine(statechart,ctx,IdFactory.getNextStateChartId(npc.getID()));
  }

  public ERENscenario getScenario () { 
    return this.scenario;
  }

  public String getPodName(String id) {
    if (scenario == null)
      return "";
    for (POD pod : scenario.getFacilities().getPods()) {
      if (pod.getID().equals(id)) {
        return pod.getName();
      }
    }
    return "";
  }

  public String getAirportName(String id) {
    if (scenario == null)
      return "";
    for (Airport air : scenario.getFacilities().getAirports()) {
      if (air.getID().equals(id)) {
        return air.getName();
      }
    }
    return "";
  }

  public String getPodLat(String id) {
    if (scenario == null)
      return "";
    for (POD pod : scenario.getFacilities().getPods()) {
      if (pod.getID().equals(id)) {
        return pod.getLocation().getKMLLocation().getLatitude();
      }
    }
    return null;
  }

  public String getPodLon(String id) {
    if (scenario == null)
      return "";
    for (POD pod : scenario.getFacilities().getPods()) {
      if (pod.getID().equals(id)) {
        return pod.getLocation().getKMLLocation().getLongitude();
      }
    }
    return null;
  }

  public String getPodLocation(String id) {
    if (scenario == null)
      return "";
    for (POD pod : scenario.getFacilities().getPods()) {
      if (pod.getID().equals(id)) {
        String lat = pod.getLocation().getKMLLocation().getLatitude();
        String lon = pod.getLocation().getKMLLocation().getLongitude();
        return lat + " " + lon;
      }
    }
    return "";
  }

  public List<POD> getUnallocatedPods() {
    return getPodsByStatus("Requisitioned");
  }

  public List<POD> getSelectedPods() {
    if (scenario == null)
      return null;
    List<POD> result = new ArrayList<POD>();
    for (POD pod : scenario.getFacilities().getPods()) {
      if (!pod.getStatus().toLowerCase().equals("not requested")) {
        result.add(pod);
      }
    }
    return result;
  }

  public List<POD> getPodsByStatus(String status) {
    if (scenario == null)
      return null;
    List<POD> result = new ArrayList<POD>();
    for (POD pod : scenario.getFacilities().getPods()) {
      if (pod.getStatus().toLowerCase().equals(status.toLowerCase())) {
        result.add(pod);
      }
    }
    return result;
  }

  public POD getPodByStatus(String status) {
    if (scenario == null)
      return null;
    for (POD pod : scenario.getFacilities().getPods()) {
      if (pod.getStatus().toLowerCase().equals(status.toLowerCase())) {
        return pod;
      }
    }
    return null;
  }

  public String getRSSLocation() {
    if (scenario == null)
      return "";
    RSS rss = scenario.getFacilities().getRSSes().get(0);
    String lat = rss.getLocation().getKMLLocation().getLatitude();
    String lon = rss.getLocation().getKMLLocation().getLongitude();
    return lat + " " + lon;
  }

  public NPC getNpc (String id) { 
    for (NPC npc : scenario.getPeople().getNPC()) { 
      if (npc.getID().equals(id)) { 
        return npc;
      }
    }
    return null;
  }

  public void updateResourceStatus(ResourceMessage rm) {
    for (ResourceInformation ri : rm.getResourceInformation()) {
      String rid = ri.getResource().getResourceID();
      String type = ri.getResource().getTypeStructure().getValue().get(0);
      if (rm instanceof RequisitionResource) {
        updateResourceStatus(rid, type, "Requisitioned");
      } else if (rm instanceof CommitResource) {
        updateResourceStatus(rid, type, "Committed");
      } else if (rm instanceof RequestResource) { 
        updateResourceStatus(rid,type, "Requested");
      } else if (rm instanceof ReleaseResource) { 
        updateResourceStatus(rid,type,"Released");
      } else if (rm instanceof ReportResourceDeploymentStatus) {
        String status = ri.getResource().getResourceStatus()
        .getDeploymentStatus().getValue().get(0);
        updateResourceStatus(rid, type, status);
      }
    }
  }

  public void updateResourceStatus(String rid, String type, String status) {
    if (scenario == null)
      return;
    if (type.equals(ResourceTypes.POD.toString())) {
      for (POD pod : scenario.getFacilities().getPods()) {
        if (rid.equals(pod.getID())) {
          pod.setStatus(status);
        }
      }
    } else if (type.equals(ResourceTypes.HOSPITAL.toString())) {
      for (Hospital hospital : scenario.getFacilities().getHospitals()) {
        if (rid.equals(hospital.getID())) {
          hospital.setStatus(status);
        }
      }
    } else if (type.equalsIgnoreCase(ResourceTypes.RSS.toString())) {
      for (RSS rss : scenario.getFacilities().getRSSes()) {
        if (rid.equals(rss.getID())) {
          rss.setStatus(status);
        }
      }
    }
  }

  public boolean hasResourceStatus(String rid, String type, String status) {
    if (scenario == null)
      return false;
    if (type.equals(ResourceTypes.POD.toString())) {
      for (POD pod : scenario.getFacilities().getPods()) {
        if (rid.equals(pod.getID())) {
          return pod.getStatus().equalsIgnoreCase(status);
        }
      }
      return false;
    } else if (type.equals(ResourceTypes.HOSPITAL.toString())) {
      for (Hospital hospital : scenario.getFacilities().getHospitals()) {
        if (rid.equals(hospital.getID())) {
          return hospital.getStatus().equalsIgnoreCase(status);
        }
      }
      return false;
    } else if (type.equals(ResourceTypes.RSS.toString())) {
      for (RSS rss : scenario.getFacilities().getRSSes()) {
        if (rid.equals(rss.getID())) {
          return rss.getStatus().equalsIgnoreCase(status);
        }
      }
      return false;
    } else
      return false;
  }

  public List<String> getResources(String typeString) {
    ResourceTypes type = ResourceTypes.valueOf(typeString);
    List<String> ids = new ArrayList<String>();
    if (type.equals(ResourceTypes.POD)) {
      List<POD> pods = scenario.getFacilities().getPods();
      for (POD pod : pods) {
        ids.add(pod.getID());
      }
    } else if (type.equals(ResourceTypes.HOSPITAL)) {
      List<Hospital> hospitals = scenario.getFacilities().getHospitals();
      for (Hospital hospital : hospitals) {
        ids.add(hospital.getID());
      }
    } else if (type.equals(ResourceTypes.RSS)) {
      List<RSS> rsses = scenario.getFacilities().getRSSes();
      for (RSS rss : rsses) {
        ids.add(rss.getID());
      }
    }
    return ids;
  }
  
  
  public String getResourceMin (String rid, String rtype) { 
    if (scenario == null)
      return null;
    for (POD pod : scenario.getFacilities().getPods()) {
      if (rid.equals(pod.getID())) {
        for (Staff s : pod.getStaff()) { 
          if (s.getFunction().equals(rtype)) { 
            return s.getMin();
          }
        }
      }
    }
    for (Hospital hospital : scenario.getFacilities().getHospitals()) {
      if (rid.equals(hospital.getID())) {
        for (Staff s : hospital.getStaff()) { 
          if (s.getFunction().equals(rtype)) { 
            return s.getMin();
          }
        }
      }
    }
    for (RSS rss : scenario.getFacilities().getRSSes()) {
      if (rid.equals(rss.getID())) {
        for (Staff s : rss.getStaff()) { 
          if (s.getFunction().equals(rtype)) { 
            return s.getMin();
          }
        }
      }
    }
    return null;
  }

  public String getResourceMax (String rid, String rtype) { 
    if (scenario == null)
      return null;
    for (POD pod : scenario.getFacilities().getPods()) {
      if (rid.equals(pod.getID())) {
        for (Staff s : pod.getStaff()) { 
          if (s.getFunction().equals(rtype)) { 
            return s.getTarget();
          }
        }
      }
    }
    for (Hospital hospital : scenario.getFacilities().getHospitals()) {
      if (rid.equals(hospital.getID())) {
        for (Staff s : hospital.getStaff()) { 
          if (s.getFunction().equals(rtype)) { 
            return s.getTarget();
          }
        }
      }
    }
    for (RSS rss : scenario.getFacilities().getRSSes()) {
      if (rid.equals(rss.getID())) {
        for (Staff s : rss.getStaff()) { 
          if (s.getFunction().equals(rtype)) { 
            return s.getTarget();
          }
        }
      }
    }
    return null;
  }
    
  public String getFacilityName (String fid) { 
    if (scenario == null)
      return null;
    for (POD pod : scenario.getFacilities().getPods()) {
      if (fid.equals(pod.getID())) {
        return pod.getName();
      }
    }
    for (Hospital hospital : scenario.getFacilities().getHospitals()) {
      if (fid.equals(hospital.getID())) {
        return hospital.getName();
      }
    }
    for (RSS rss : scenario.getFacilities().getRSSes()) {
      if (fid.equals(rss.getID())) {
        return rss.getName();
      }
    }
    return null;
  }
  
  public String getAvailableStaff (String type) { 
    if (scenario == null) return null;
    for (Staff staff : scenario.getPeople().getStaff()) { 
      if (staff.getFunction().equals(type)) { 
        return staff.getQuantity();
      }
    }
    return null;
  }
  
  // Assume the resource is a type of staff for now
  public String getAvailability (String resourceType) { 
    if (resourceType.equals(ResourceTypes.LE_STAFF.toString())) { 
      return getAvailableStaff("Security");
    } else if (resourceType.equals(ResourceTypes.CLINICAL_STAFF.toString())) { 
      return getAvailableStaff("Medical");
    } else if (resourceType.equals(ResourceTypes.OPS_STAFF.toString())) { 
      return getAvailableStaff("Support");
    } else return null;

  }
  
  /**
   * This is a placeholder for now until we have a service for keeping track of the 
   * resource allocations
   */
  public String getAllocation (String resourceType, String facilityID) { 
    return "200";
  }
    
  public Map<Object, Object> getEmptyMap() {
    return new HashMap<Object, Object>();
  }

  public List<Object> getEmptyList() {
    return new ArrayList<Object>();
  }

  public EDXLDistribution makeEDXL() {
    return client.makeEdxl(sender);
  }

  public UserMessage makeUserMessage(EDXLDistribution edxl) {
    UserMessage msg = client.attachElement(edxl, DLG_USERMESSAGE);
    return msg;
  }

  public ResourceMessage makeResourceMessage(EDXLDistribution edxl,
      ResourceMessageType messageType) {
    ResourceMessage msg = client.attachElement(edxl, messageType.getQname());
    return msg;
  }

  /**
   * Start up the Dialogue Manager
   * 
   * @param args
   */
  public static void main(String[] args) {
    new DialogueManager(args);
  }

  public class Event {
    String eventName;
    Object payload;
    String target;

    public Event(String eventName, Object payload) {
      super();
      this.eventName = eventName;
      this.payload = payload;
    }



    public Event(String eventName, Object payload, String target) {
      super();
      this.eventName = eventName;
      this.payload = payload;
      this.target = target;
    }



    /**
     * @return the target
     */
    public String getTarget() {
      return target;
    }



    /**
     * @param target the target to set
     */
    public void setTarget(String target) {
      this.target = target;
    }



    /**
     * @return the eventName
     */
    public String getEventName() {
      return eventName;
    }

    /**
     * @param eventName
     *          the eventName to set
     */
    public void setEventName(String eventName) {
      this.eventName = eventName;
    }

    /**
     * @return the payload
     */
    public Object getPayload() {
      return this.payload;
    }

    /**
     * @param payload
     *          the payload to set
     */
    public void setPayload(Object payload) {
      this.payload = payload;
    }

    public String toString() {
      return "Event[" + eventName + "," + payload + "]";
    }

  }

  private class DelayedEvent extends Event implements Comparable {
    long time;

    public DelayedEvent (String eventName, Object payload, String target, long time)  { 
      super(eventName,payload,target);
      this.time = time;
    }

    public DelayedEvent(String eventName, Object payload, long time) {
      this(eventName,payload,null,time);
    }

    /**
     * @return the time
     */
    public long getTime() {
      return time;
    }

    /**
     * @param time
     *          the time to set
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
