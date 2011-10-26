package org.mitre.eren.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.nio.DefaultServerIOEventDispatch;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.protocol.BufferingHttpServiceHandler;
import org.apache.http.nio.protocol.EventListener;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.ListeningIOReactor;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import org.mitre.javautil.logging.LoggingUtils;

public class InboundHttpEndpoint implements Runnable {
	
	private static Logger log = LoggingUtils.getLogger(InboundHttpEndpoint.class);
	
	private int port;
	private HttpRequestHandler handler;
	private static DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public InboundHttpEndpoint(int port, HttpRequestHandler handler) {
		this.port = port;		
		this.handler = handler;
	}
	
	@Override
	public void run() {
        HttpParams params = new BasicHttpParams();
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 10000)
        	.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
            .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
            .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
            .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");

        BasicHttpProcessor httpproc = new BasicHttpProcessor();
        httpproc.addInterceptor(new ResponseDate());
        httpproc.addInterceptor(new ResponseServer());
        httpproc.addInterceptor(new ResponseContent());
        httpproc.addInterceptor(new ResponseConnControl());

        BufferingHttpServiceHandler handler = new BufferingHttpServiceHandler(
                httpproc,
                new DefaultHttpResponseFactory(),
                //new DefaultConnectionReuseStrategy(),
                new ConnectionReuseStrategy() {
					@Override
					public boolean keepAlive(HttpResponse arg0, HttpContext arg1) {
						// TODO Auto-generated method stub
						return false;
					}
				},
                params);

        // Set up request handlers
        HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
        reqistry.register("*", this.handler);

        handler.setHandlerResolver(reqistry);

        // Provide an event logger
        handler.setEventListener(new EventLogger());

        log.info("Starting server on port " + port);
        
        try {
        	IOEventDispatch ioEventDispatch = new DefaultServerIOEventDispatch(handler, params);
        	ListeningIOReactor ioReactor = new DefaultListeningIOReactor(2, params);
            ioReactor.listen(new InetSocketAddress(port));
            ioReactor.execute(ioEventDispatch);
        } catch (InterruptedIOException ex) {
            log.warning("Interrupted");
        } catch (IOException e) {
            log.warning("I/O error: " + e.getMessage());
            e.printStackTrace();
            //Throwable t = e.getCause();
            //t.printStackTrace();
        }
        log.info("Shutdown");

	}
	
    static class EventLogger implements EventListener {

        @Override
		public void connectionOpen(final NHttpConnection conn) {
            log.fine("Connection open: " + conn);
        }

        @Override
		public void connectionTimeout(final NHttpConnection conn) {
            log.fine("Connection timed out: " + conn);
        }

        @Override
		public void connectionClosed(final NHttpConnection conn) {
            log.fine("Connection closed: " + conn);
        }

        @Override
		public void fatalIOException(final IOException ex, final NHttpConnection conn) {
            log.warning("I/O error: " + ex.getMessage());
        }

        @Override
		public void fatalProtocolException(final HttpException ex, final NHttpConnection conn) {
            log.log(Level.SEVERE, "HTTP error: " + ex.getMessage(), ex);
        }

    }

}
