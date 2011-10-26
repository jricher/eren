/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.model;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mitre.eren.http.OutboundHttpEndpoint;
import org.mitre.eren.protocol.edxl_rm.ResourceTypes;
import org.mitre.eren.protocol.startup.ResourceUsage;
import org.mitre.eren.protocol.startup.ResourceUse;
import org.mitre.eren.protocol.startup.StartupConstants;
import org.mitre.javautil.logging.LoggingUtils;
import org.opencare.lib.model.edxl.EDXLDistribution;

/**
 *
 * @author JWINSTON
 */
public class ResourceHolder implements StartupConstants {

    private static final Logger log = LoggingUtils.getLogger(ResourceHolder.class);
    private HashMap<ResourceTypes, ModelResource> resourceMap = new HashMap<ResourceTypes, ModelResource>();
    private OutboundHttpEndpoint client;
    private String sender;


    public ResourceHolder(OutboundHttpEndpoint client, String sender) {
        this.client = client;
        this.sender = sender;
    }

 /*   public void put(ResourceTypes rt, ModelResource mr) {
        resourceMap.put(rt, mr);
        sendResourceUsage();
    }
*/
    public void setOutboundHttpEndpoint(OutboundHttpEndpoint client) {
        this.client = client;
    }
    public int request(ResourceTypes rt, int qtyRequested) {
        ModelResource resource = resourceMap.get(rt);
        if (resource == null) {
            return 0;
        }
        int granted = resource.request(qtyRequested);
        sendResourceUsage();
        return granted;
    }

    public int release(ResourceTypes rt, int qtyReleased) {
        ModelResource resource = resourceMap.get(rt);
        if (resource == null) {
            return 0;
        }
        int nowAvailable = resource.release(qtyReleased);
        sendResourceUsage();
        return nowAvailable;
    }

    public int add(ResourceTypes rt, int increaseQty) {
        ModelResource resource = resourceMap.get(rt);
        if (resource == null) {
            resource = new ModelResource(rt, increaseQty);
            resourceMap.put(rt, resource);
        } else {
            resource.add(increaseQty);
        }
        sendResourceUsage();
        return resource.getTotal();
    }

    public int reduce(ResourceTypes rt, int decreaseQty) {
        ModelResource resource = resourceMap.get(rt);
        if (resource == null) {
            return 0;
        } else {
            resource.reduce(decreaseQty);
        }
        sendResourceUsage();
        return resource.getTotal();

    }
    public void sendResourceUsage() {

        if (client == null) {
            log.log(Level.WARNING, "sendResourceUsage: can't send, client is null");
            return;
        }
        EDXLDistribution edxl = client.makeEdxl(sender);
        ResourceUsage status = client.attachElement(edxl, EREN_RESOURCEUSAGE);
        for (ModelResource resource : resourceMap.values()) {
            ResourceUse ru = status.addResourceUse();
            ru.setResourceType(resource.getFunction());
            ru.setAvailable(resource.getAvailable());
            ru.setTotal(resource.getTotal());
        }
        client.send(edxl);
    }
}
