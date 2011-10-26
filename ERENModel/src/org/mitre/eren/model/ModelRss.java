/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2010. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 *
*****************************************************************************/

package org.mitre.eren.model;

import java.util.logging.Logger;
import org.mitre.eren.protocol.edxl_rm.ResourceTypes;
import org.mitre.javautil.logging.LoggingUtils;

/**
 *
 * @author JWINSTON
 */
public class ModelRss {
    private ModelManager modelManager;
    private String id;
    private String status;
    private double latitude;
    private double longitude;
    private int medCurrent;
    private int secCurrent;
    private int supportCurrent;
    private Logger log = LoggingUtils.getLogger(ModelRss.class);

    public ModelRss(ModelManager mm, String id, String status,
            double latitude, double longitude) {

        this.modelManager = mm;
        this.id = id;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        log.info("ModelRss constructor id= " + id + " status= " + status + " lat " + latitude + " long " + longitude);
    }

    public String getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    public int requestStaff(ResourceTypes staffType, int qty) {
        int amtGranted = modelManager.requestResource(staffType, qty);
        if (staffType.equals(ResourceTypes.CLINICAL_STAFF)) {
            medCurrent += amtGranted;
        } else if (staffType.equals(ResourceTypes.LE_STAFF)) {
            secCurrent += amtGranted;
        } else if (staffType.equals(ResourceTypes.OPS_STAFF)) {
            supportCurrent += amtGranted;
        }
        return amtGranted;
    }

    public void releaseResource(ResourceTypes resourceType, int qty) {
        if (resourceType.equals(ResourceTypes.CLINICAL_STAFF)) {
            if (medCurrent >= qty) {
                medCurrent -= qty;
            } else {
                qty = medCurrent;
                medCurrent = 0;
            }

        } else if (resourceType.equals(ResourceTypes.OPS_STAFF)) {
            if (supportCurrent >= qty) {
                supportCurrent -= qty;
            } else {
                qty = supportCurrent;
                supportCurrent = 0;
            }
        } else if (resourceType.equals(ResourceTypes.LE_STAFF)) {
            if (secCurrent >= qty) {
                secCurrent -= qty;
            } else {
                qty = secCurrent;
                secCurrent = 0;
            }
        }
        modelManager.releaseResource(resourceType, qty);
    }

}
