package org.mitre.eren.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.ExtensionFactory;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.Parser;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.opencare.lib.model.Constants;
import org.opencare.lib.model.edxl.ContentObject;
import org.opencare.lib.model.edxl.EDXLDistribution;
import org.opencare.lib.model.edxl.EmbeddedXMLContent;
import org.opencare.lib.model.edxl.XmlContent;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import org.mitre.javautil.logging.LoggingUtils;

public class OutboundHttpEndpoint implements Constants {

	private String endpoint;

	private Factory factory;

	private String gameId;

	private static Logger log = LoggingUtils.getLogger(OutboundHttpEndpoint.class);

	public OutboundHttpEndpoint(String endpoint, String gID) {
		this.endpoint = endpoint;
		this.gameId = gID;

		Abdera abdera = new Abdera();
		factory = abdera.getFactory();
	
		// you get one for free
		factory.registerExtension(new org.opencare.lib.model.ExtensionFactory());		

        params = new BasicHttpParams();
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 10000)
            .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
            .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
            .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
            .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");
	
	}

	public Factory registerExtension(ExtensionFactory ef) {
	    return factory.registerExtension(ef);
    }

	public Factory getFactory() {
    	return factory;
    }
	
	public EDXLDistribution makeEdxl() {
		return makeEdxl(null);
	}
	
	public EDXLDistribution makeEdxl(String sender) {
		return makeEdxl(getNextID(), sender);
	}
	
	/**
     * @return
     */
    public EDXLDistribution makeEdxl(String distId, String sender) {
	    Document<EDXLDistribution> doc = factory.newDocument();
		doc.setCharset("UTF-8");
		
		EDXLDistribution edxl = (EDXLDistribution) factory.newElement(EDXL_EDXL_DISTRIBUTION, doc);
		
		edxl.setDistributionID(distId); // TODO: generate distro id's
		edxl.setDistributionStatus(EDXLDistribution.DistributionStatus.System); // TODO: make these make sense
		edxl.setDistributionType(EDXLDistribution.DistributionType.Update);
		if (sender != null) {
			edxl.setSenderID(sender);
		}
		edxl.setCombinedConfidentiality("UNCLASSIFIED AND NOT SENSITIVE");
		edxl.setLanguage("EN");
		if (this.gameId != null) {
			edxl.addSenderRole("eren:GameID",this.gameId);			
		}
		
	    return edxl;
    }
    
    public <T extends ExtensibleElement> T attachElement(EDXLDistribution edxl, QName e) {
		ContentObject co = edxl.addContentObject();
		XmlContent xc = co.setXmlContent();		
		EmbeddedXMLContent exc = xc.addEmbeddedXMLContent();
		
		return exc.addExtension(e);
    }
    
    public <T extends ExtensibleElement> T attachElement(EDXLDistribution edxl, Element e) {
		ContentObject co = edxl.addContentObject();
		XmlContent xc = co.setXmlContent();		
		EmbeddedXMLContent exc = xc.addEmbeddedXMLContent();
		
		return exc.addExtension(e);
    }
    
    public <T extends ExtensibleElement> T attachXMLString(EDXLDistribution edxl, String xml) {
    	Parser parser = factory.getAbdera().getParser();
        Document<Feed> feed_doc = parser.parse(new StringReader(xml));
        ExtensibleElement content = feed_doc.getRoot();
        
        return attachElement(edxl, content);
    }
    
    public <T extends ExtensibleElement> T attachXML(EDXLDistribution edxl, Node n) {
        try {
        	DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        	DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");

        	LSSerializer writer = impl.createLSSerializer();
        	String str = writer.writeToString(n);
        	String wsfixed = str.replaceAll("\\s+", " ");
	          
        	return attachXMLString(edxl, wsfixed);
        } catch (ClassCastException e) {
	        // TODO Auto-generated catch block
	        log.log(Level.WARNING, "Caught Exception", e);
        } catch (ClassNotFoundException e) {
	        // TODO Auto-generated catch block
	        log.log(Level.WARNING, "Caught Exception", e);
        } catch (InstantiationException e) {
	        // TODO Auto-generated catch block
	        log.log(Level.WARNING, "Caught Exception", e);
        } catch (IllegalAccessException e) {
	        // TODO Auto-generated catch block
	        log.log(Level.WARNING, "Caught Exception", e);
        }

        // if we fail, return null
        return null;
    }
	
    private int id = 0;

	private HttpParams params;
    public String getNextID() {
    	id += 1;
    	return Integer.toString(id);
    }
    
	public void send(final EDXLDistribution edxl) {
		try {
			
			System.out.println("Sending:");
			edxl.writeTo(System.out);
			System.out.println();
			
			HttpPost req = new HttpPost(endpoint);
			
			ContentProducer cp = new ContentProducer() {
				@Override
				public void writeTo(OutputStream out) throws IOException {
					edxl.writeTo(out);
				}
			};
			
			req.setEntity(new EntityTemplate(cp));
			
			HttpClient hc = new DefaultHttpClient(params) {
				@Override
                protected ConnectionReuseStrategy createConnectionReuseStrategy() {
					return new ConnectionReuseStrategy() {						
						@Override
						public boolean keepAlive(HttpResponse arg0, HttpContext arg1) {
							// TODO Auto-generated method stub
							return false;
						}
					};
                }				
			};

			hc.execute(req);
		
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.log(Level.WARNING, "Caught Exception", e);
		}
	}

	public int getId() {
    	return id;
    }

	public void setId(int id) {
    	this.id = id;
    }	
	
}
