/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2010. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.model;

import org.mitre.eren.protocol.edxl_rm.ResourceTypes;

/**
 *
 * @author JWINSTON
 */
public class ModelResource {
    private int total;
    private int available;
    private ResourceTypes function;

    public ModelResource(ResourceTypes function, int qty) {
        this.function = function;
        this.available = qty;
        this.total = qty;
    }

    public int request(int qtyRequested) {
        if (available >= qtyRequested) {
            available -= qtyRequested;
            return qtyRequested;
        }
        int retAmt = available;
        available = 0;
        return retAmt;
    }

    public int release(int qtyReleased) {
        if (qtyReleased > total) {
            available = total;
        } else {
            available += qtyReleased;
        }
        return available;
    }

    public int add(int amountToAdd) {
        available += amountToAdd;
        total += amountToAdd;
        return available;
    }

    public int reduce(int amountToReduce) {
        if (amountToReduce < available) {
            available -= amountToReduce;
        } else {
            available = 0;
        }

        if (amountToReduce < total) {
            total -= amountToReduce;
        } else {
            total = 0;
        }
        
        return available;
    }

    public int getAvailable() {
        return available;
    }

    public int getTotal() {
        return total;
    }

    public int getInUse() {
        return total-available;
    }

    public ResourceTypes getFunction() {
        return function;
    }
}
