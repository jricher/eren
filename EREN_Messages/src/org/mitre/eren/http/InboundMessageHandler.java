package org.mitre.eren.http;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.ExtensionFactory;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.parser.Parser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.opencare.lib.model.edxl.ContentObject;
import org.opencare.lib.model.edxl.EDXLDistribution;
import org.opencare.lib.model.edxl.EmbeddedXMLContent;
import org.opencare.lib.model.edxl.ValueList;
import org.opencare.lib.model.edxl.ValueScheme;

import org.mitre.eren.message.MessageProcessor;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.mitre.eren.protocol.scenario.Role;
import org.mitre.javautil.logging.LoggingUtils;

public class InboundMessageHandler implements HttpRequestHandler, EDXLRMConstants, Runnable {
	private static Logger log = LoggingUtils.getLogger(InboundMessageHandler.class);

	private MessageProcessor mp;

	private Factory factory;
	
	private Thread processThread;

	private BlockingQueue<Document<EDXLDistribution>> queue;
	
	public InboundMessageHandler(MessageProcessor mp) {
		// TODO Auto-generated constructor stub
		this.mp = mp;

		Abdera abdera = new Abdera();
		factory = abdera.getFactory();

		// you get this one for free, it has DE in it
		factory.registerExtension(new org.opencare.lib.model.ExtensionFactory());
		
		List<ExtensionFactory> extensions = mp.getExtensions();
		for (ExtensionFactory ef : extensions) {
	        factory.registerExtension(ef);
        }
		
		queue = new LinkedBlockingDeque<Document<EDXLDistribution>>();
		
		processThread = new Thread(this, "Message Processor thread");
		processThread.setDaemon(true);
		processThread.start();
		
		

	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

		String method = request.getRequestLine().getMethod().toUpperCase();
		if (!method.equals("POST")) {
			throw new MethodNotSupportedException("Only POST is allowed");
		}

		if (request instanceof HttpEntityEnclosingRequest) {
			HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();

			//System.out.println("Incoming entity content (bytes): " + entity.getContentLength());

			Parser parser = factory.newParser();
			try {
				Document<EDXLDistribution> doc = parser.parse(entity.getContent());

				// TODO: add this to a proper log
				/*
				System.out.println("Got document:");
				doc.writeTo(System.out);
				System.out.println();
				 */

				// fire off a new thread to process things
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						for (ContentObject content : doc.getRoot().getContentObject()) {
//							// get everything under the xml content
//						  for (EmbeddedXMLContent exc : content.getXmlContent().getEmbeddedXMLContent()) {
//						    for (Element e : exc.getExtensions()) {
//						    	mp.processMessage(e);
//						    }
//						  }
//
//						}
//
//						
//					}
//				}).start();
				
				if (queue.offer(doc)) {				
					response.setStatusCode(HttpStatus.SC_OK);
				} else {
					response.setStatusCode(HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE);
				}

				entity.consumeContent(); // finish
				return;
			} catch (ParseException pe) {
				System.err.println("Error parsing data: " + pe.getMessage());
				throw new HttpException("Content not valid EDXL");
			}
		} else {
			throw new HttpException("Content not valid EDXL");
		}
	}

	/**
	 * Daemon runnable for processing documents coming in off the wire.
	 * @see java.lang.Runnable#run()
	 */
	@Override
    public void run() {
	    // TODO Auto-generated method stub
	    while (true) {
	    	try {
	    		// grab the next document off the queue
	    		Document<EDXLDistribution> doc = queue.take();
	    		EDXLDistribution edxl = doc.getRoot();
	    		String gameId = edxl.getSenderRole("eren:GameID");
	    		String sender = edxl.getSenderID();
	    		List<String> roles = new java.util.ArrayList<String>(); 
	    		List<ValueScheme> addresses = edxl.getExplicitAddresses();
	    		List<String> usernames = new java.util.ArrayList<String>();
	    		for (ValueScheme a : addresses) {
	    			if (a.getExplicitAddressScheme().equals("eren:role")) {
						String[] vals = a.getExplicitAddressValue();
						for (int i = 0; i < vals.length; i++) {
							roles.add(vals[i]);
						}
	    			}else if (a.getExplicitAddressScheme().equals("eren:username")) {
						String[] vals = a.getExplicitAddressValue();
						for (int i = 0; i < vals.length; i++) {
							usernames.add(vals[i]);
						}
	    			}
	    		}		
	    			
	    		// process envelope
	    		
	    		
				for (ContentObject content : doc.getRoot().getContentObject()) {
					// get everything under the xml content
					
					for (EmbeddedXMLContent exc : content.getXmlContent().getEmbeddedXMLContent()) {
						for (Element e : exc.getExtensions()) {
							
							
							mp.processMessage(e, gameId, sender, roles, usernames);
						}
					}

				}
            } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            log.log(Level.WARNING, "Caught Exception", e);
            }
	    }
    }



}
