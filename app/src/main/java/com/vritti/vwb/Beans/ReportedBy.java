package com.vritti.vwb.Beans;

import java.io.Serializable;

/**
 * Created by sharvari on 16-Apr-18.
 */

public class ReportedBy implements Serializable {


    String customer_name;

    public ReportedBy() {

    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }
}
