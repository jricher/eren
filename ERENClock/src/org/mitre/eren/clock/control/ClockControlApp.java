package org.mitre.eren.clock.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.opencare.lib.model.Constants;
import org.opencare.lib.model.edxl.EDXLDistribution;

import org.mitre.eren.http.InboundHttpEndpoint;
import org.mitre.eren.http.InboundMessageHandler;
import org.mitre.eren.http.OutboundHttpEndpoint;
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.clock.ClockExtensionFactory;
import org.mitre.eren.protocol.clock.SetClockspeed;
import org.mitre.eren.protocol.clock.SetClocksync;
import org.mitre.eren.protocol.startup.GameId;
import org.mitre.eren.protocol.startup.StartupConstants;
import org.mitre.javautil.logging.LoggingUtils;

public class ClockControlApp implements ClockConstants, Constants, StartupConstants {
	private static Logger log = LoggingUtils.getLogger(ClockControlApp.class);

	private OutboundHttpEndpoint client;
	
	private JLabel tick;
	private JLabel sync;
	private JLabel gameTime;
	private JLabel speed;
	private String gameID;
	private static final String urnGID = "eren:GameID";
	private String sender = "clockcontrol@erenbus.mitre.org";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ClockControlApp(args);
	}

	public ClockControlApp(String[] args) {
		
		//parse the commandline args
		
		int serverPort = 3737;
		String endpoint = "http://erenbus-tmlewis.mitre.org:3732/";
		
		
		try {
			Options options = new Options();
			
			options.addOption("p", "port", true, "Server port to listen on for incoming HTTP messages from the bus");
			options.addOption("u", "url", true, "Outbound URL to post HTTP messages to");
			options.addOption("h", "help", false, "Prints this help message");
			
			CommandLineParser parser = new PosixParser();
	        CommandLine cmd = parser.parse(options, args);
	        
	        if (cmd.hasOption("h")) {
	        	HelpFormatter hf = new HelpFormatter();
	        	hf.printHelp("ERENClock.jar", options);
	        	System.exit(1);
	        } 
	        if (cmd.hasOption("p")) {
	        	serverPort = Integer.parseInt(cmd.getOptionValue("p"));
	        } 
	        if (cmd.hasOption("u")) {
	        	endpoint = cmd.getOptionValue("u");
	        }
		} catch (ParseException e) {
	        // TODO Auto-generated catch block
	        log.log(Level.WARNING, "Caught Exception", e);
        }
		
		// start the server
		
		InboundHttpEndpoint server = new InboundHttpEndpoint(serverPort, new InboundMessageHandler(new InboundClockPanelHttp(this)));
		
		Thread st = new Thread(server, "EREN HTTP Inbound Endpoint");
		//st.setDaemon(true);
		st.start();
		
		client = new OutboundHttpEndpoint(endpoint,null);
		client.registerExtension(new ClockExtensionFactory());
		
		JFrame frame = new JFrame("EREN Clock Control");
		
		Box box = Box.createVerticalBox();
		frame.add(box);
		
		JButton start = new JButton("Start");
		start.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendStart();
			}
		});
		box.add(start);
		
		JButton pause = new JButton("Pause");
		pause.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendPause();
			}
		});
		box.add(pause);
		
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendReset();
			}
		});
		box.add(reset);
		
		ratioInput = new JSpinner();
		ratioInput.setValue(60);
		box.add(ratioInput);
				
		JButton setRatio = new JButton("Set Ratio");
		setRatio.addActionListener(new ActionListener() {
		
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendRatio();
			}
		});
		
		box.add(setRatio);

		tbox = new JTextField("1");
		JButton bGameId = new JButton("SetGameID");
		bGameId.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setGameID();
			}
		});
		box.add(tbox);
		box.add(bGameId);
		
		

		sync = new JLabel("Sync");
		box.add(sync);
		speed = new JLabel("Speed");
		box.add(speed);
		box.add(new JSeparator(JSeparator.HORIZONTAL));
		tick = new JLabel("Tick");
		box.add(tick);
		gameTime = new JLabel("GameTime");
		box.add(gameTime);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.show();
		
		
//		sendSync();
//		sendRatio();
		
	}
	private void setGameID(){
		this.gameID =  tbox.getText();
	}
	
	private void sendStart() {
		EDXLDistribution edxl = client.makeEdxl(sender);
		client.attachElement(edxl, EREN_CLOCKSTART);
		setGameID();
		edxl.addSenderRole(urnGID, gameID);
		client.send(edxl);		
	}
	
	private void sendPause() {
		EDXLDistribution edxl = client.makeEdxl(sender);

		client.attachElement(edxl, EREN_CLOCKPAUSE);
		setGameID();
		edxl.addSenderRole(urnGID, gameID);
		client.send(edxl);		
	}

	private void sendReset() {
		EDXLDistribution edxl = client.makeEdxl(sender);
		
		client.attachElement(edxl, EREN_CLOCKRESET);
		setGameID();
		edxl.addSenderRole(urnGID, gameID);
		client.send(edxl);		
	}

	private void sendRatio() {
		EDXLDistribution edxl = client.makeEdxl(sender);
		
		SetClockspeed speed = client.attachElement(edxl, EREN_SETCLOCKSPEED);
		int ratio = Integer.parseInt(ratioInput.getValue().toString());
		speed.setRatio(ratio);
		
		setGameID();
		edxl.addSenderRole(urnGID, gameID);
		client.send(edxl);
	}
	
	private void sendSync() {
		EDXLDistribution edxl = client.makeEdxl(sender);
		
		SetClocksync sync = client.attachElement(edxl, EREN_SETCLOCKSYNC);
		sync.setDate(new Date());
		setGameID();
		edxl.addSenderRole(urnGID, gameID);
		client.send(edxl);		
		
	}
	
	private int id = 0;
    public void setTick(final Long t) {
    	SwingUtilities.invokeLater(new Runnable() {
    		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tick.setText(t.toString());
				
				Calendar c = Calendar.getInstance();
				c.setTime(syncDate);
				c.add(Calendar.SECOND, t.intValue()); // FIXME: this could overflow
				
				gameTime.setText(c.getTime().toString());
			}
		});
    }

    private Date syncDate = new Date();

	private JSpinner ratioInput;

	private JTextField tbox;
	public void setSync(final Date d) {
	    // TODO Auto-generated method stub
    	SwingUtilities.invokeLater(new Runnable() {
    		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sync.setText(d.toString());
				syncDate = d;
			}
		});
	    
    }

	public void setSpeed(final Integer r) {
	    // TODO Auto-generated method stub
    	SwingUtilities.invokeLater(new Runnable() {
    		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				speed.setText(r.toString());
			}
		});
	    
    }
    
}
