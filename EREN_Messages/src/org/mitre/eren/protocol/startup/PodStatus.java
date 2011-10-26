/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.protocol.startup;

/**
 *
 * @author JWINSTON
 */

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class PodStatus extends BaseWrapper implements StartupConstants {

    public PodStatus(Element internal) {
        super(internal);
    }

    public PodStatus(Factory factory, QName qname) {
        super(factory, qname);
    }

    public void setMedicalStaff(int amt){
        setElementText(EREN_MEDICALSTAFF, Integer.toString(amt));
    }

    public int getMedicalStaff() {
        String qty = getSimpleExtension(EREN_MEDICALSTAFF);
        return Integer.parseInt(qty);
    }

    public void setSupportStaff(int amt){
        setElementText(EREN_SUPPORTSTAFF, Integer.toString(amt));
    }

    public Integer getSupportStaff() {
        String qty = getSimpleExtension(EREN_SUPPORTSTAFF);
        return Integer.parseInt(qty);
    }

    public void setSecurityStaff(int amt){
        setElementText(EREN_SECURITYSTAFF, Integer.toString(amt));
    }

    public int getSecurityStaff() {
        String qty =  getSimpleExtension(EREN_SECURITYSTAFF);
        return Integer.parseInt(qty);
    }

    public void setStandardOfCare(int level) {
        setElementText(EREN_STANDARDOFCARE, Integer.toString(level));
    }

    public int getStandardOfCare() {
        String level = getSimpleExtension(EREN_STANDARDOFCARE);
        return Integer.parseInt(level);
    }

    public void setPodQueueSize(int qlength) {
        setElementText(EREN_QUEUESIZE, Integer.toString(qlength));
    }

    public int getPodQueueSize() {
        String qlength = getSimpleExtension(EREN_QUEUESIZE);
        return Integer.parseInt(qlength);
    }

    public void setThroughput(int throughput) {
        setElementText(EREN_THROUGHPUT, Integer.toString(throughput));
    }

    public int getThroughput() {
        String throughput = getSimpleExtension(EREN_THROUGHPUT);
        return Integer.parseInt(throughput);
    }

    public void setHasMeds(boolean hasMeds) {
        setElementText(EREN_HASMEDS, Boolean.toString(hasMeds));
    }

    public boolean getHasMeds() {
        String smeds = getSimpleExtension(EREN_HASMEDS);
        return Boolean.parseBoolean(smeds);
    }

    public void setHasEquipment(boolean hasEquipment) {
        setElementText(EREN_HASEQUIPMENT, Boolean.toString(hasEquipment));
    }

    public boolean getHasEquipment() {
        String equiped = getSimpleExtension(EREN_HASEQUIPMENT);
        return Boolean.parseBoolean(equiped);
    }

}
