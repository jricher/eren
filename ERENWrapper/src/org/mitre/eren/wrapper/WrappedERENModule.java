package org.mitre.eren.wrapper;

import org.mitre.eren.http.OutboundHttpEndpoint;
import org.mitre.eren.message.MessageProcessor;

public interface WrappedERENModule {
	
	
	public MessageProcessor getMessageProcessor();
	public void setOutboundEndpoint(OutboundHttpEndpoint outbound);


}
