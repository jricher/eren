package org.mitre.eren.clock;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.mitre.eren.http.InboundHttpEndpoint;
import org.mitre.eren.http.InboundMessageHandler;
import org.mitre.eren.http.OutboundHttpEndpoint;
import org.mitre.eren.message.MessageProcessor;
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.clock.ClockExtensionFactory;
import org.mitre.eren.protocol.clock.Clockspeed;
import org.mitre.eren.protocol.clock.Clocksync;
import org.mitre.eren.protocol.clock.Clocktick;
import org.mitre.eren.wrapper.WrappedERENModule;
import org.mitre.javautil.logging.LoggingUtils;
import org.opencare.lib.model.Constants;
import org.opencare.lib.model.edxl.EDXLDistribution;

public class ERENClock implements Constants, ClockConstants, WrappedERENModule{
	private static Logger log = LoggingUtils.getLogger(ERENClock.class);
	//private HttpClient hc;
    private long currentTick = 0;
    private int currentRatio = 0;
    private int timeBetweenTicks = 5; // seconds between ticks
    private boolean running = false;
	private TimerTask ticker;
	private Timer timer;
	private Date syncDate = new Date(0); // set the date to epoc
	private OutboundHttpEndpoint client;
	private String gameID;
	private String sender = "clock@erenbus.mitre.org";
	private final String urnGID = "eren:GameID";
	//private InboundHttpEndpoint server;
	private MessageProcessor mp;
	
	
	public static WrappedERENModule initalizeWrapedObject(String[] args) {
		return new ERENClock(args);
	}
	
	private ERENClock(String[] args) {
		
		// parse the arguments
		
		int serverPort = 3736;
		
		
		Options options = new Options();
		options.addOption("f", "frequency", true, "Number of real-world seconds between clock ticks");
		options.addOption("h", "help", false, "Prints this help message");
		
		
		try {
			CommandLineParser parser = new PosixParser();
	        CommandLine cmd = parser.parse(options, args, true);
	        
	        if (cmd.hasOption("h")) {
	        	HelpFormatter hf = new HelpFormatter();
	        	hf.printHelp("ERENClock.jar", options);
	        	System.exit(1);
	        }

	        if (cmd.hasOption("f")) {
	        	timeBetweenTicks = Integer.parseInt(cmd.getOptionValue("f"));
	        }	            
	        
        } catch (ParseException e) {
	        // TODO Auto-generated catch block
	        log.log(Level.WARNING, "Caught Exception", e);
        	HelpFormatter hf = new HelpFormatter();
        	hf.printHelp("ERENClock.jar", options);
        	System.exit(1);
        }
		
		mp = new InboundClockHttpController(this);

		
		// build the client
		
		//client = new OutboundHttpEndpoint(endpoint);
		
		
		// make the clock ticker tick
		
		ticker = new TimerTask() {
			@Override
			public void run() {
				if (isRunning()) {
					long millisOff = System.currentTimeMillis() - scheduledExecutionTime();
					//log.info("Millis:" + millisOff);
					int secondsSinceLast = (int) (millisOff / 1000) + timeBetweenTicks;
					//log.info("Seconds:" + secondsSinceLast);
					currentTick += secondsSinceLast * currentRatio;
					sendTick();
				}
			}
		};
		
		timer = new Timer("Clock Timer");						
		timer.scheduleAtFixedRate(ticker, 0, timeBetweenTicks * 1000);
		
	}

	private void sendTick() {		
		EDXLDistribution edxl = client.makeEdxl(sender);
		
		attachTick(edxl);
		attachSpeed(edxl);
		//edxl.addSenderRole(urnGID, gameID);
		// if we've ticked enough ticks, send out a sync, too
		if (client.getId() % 100 == 0) {
			attachSync(edxl);
		}
		
		client.send(edxl);
	}

	/**
     * @param edxl
     */
    private void attachTick(EDXLDistribution edxl) {
		Clocktick tick = client.attachElement(edxl, EREN_CLOCKTICK);
		tick.setTime(currentTick);
    }
	
	private void sendRatio() {
		EDXLDistribution edxl = client.makeEdxl(sender);
		attachSpeed(edxl);
		
		//edxl.addSenderRole(urnGID, gameID);
		client.send(edxl);
	}

	/**
     * @param edxl
     */
    private void attachSpeed(EDXLDistribution edxl) {
		Clockspeed speed = client.attachElement(edxl, EREN_CLOCKSPEED);
		speed.setRatio(currentRatio);
    }
	
	private void sendSync() {
		EDXLDistribution edxl = client.makeEdxl(sender);
		attachSync(edxl);
		
		//edxl.addSenderRole(urnGID, gameID);
		client.send(edxl);		
		
	}
	
	private void attachSync(EDXLDistribution edxl) {
		
		Clocksync sync = client.attachElement(edxl, EREN_CLOCKSYNC);
		sync.setDate(syncDate);
		
	}
	
	public int getRatio() {
    	return currentRatio;
    }

	public void setRatio(int currentRatio) {
    	this.currentRatio = currentRatio;
    	sendRatio();
    }
	
	public void reset(Long currentTick) {
		if (currentTick == null) {
			this.currentTick = 0;
		} else {
			this.currentTick = currentTick.longValue();
		}
	}

	public boolean isRunning() {
    	return running;
    }
	
	public void start() {
		if (!isRunning()) { // ignore if we're already running
			running = true;
		}
	}
	
	public void pause() {
		if (isRunning()) {
			running = false;
		}
	}

	public Date getSyncDate() {
    	return syncDate;
    }

	public void setSyncDate(Date syncDate) {
    	this.syncDate = syncDate;
    	sendSync();
    }

	@Override
	public MessageProcessor getMessageProcessor() {
		
		return mp;
	}

	@Override
	public void setOutboundEndpoint(OutboundHttpEndpoint outbound) {
		// TODO Auto-generated method stub
		client = outbound;
		client.registerExtension(new ClockExtensionFactory());
	}


}
