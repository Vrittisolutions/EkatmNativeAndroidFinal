package com.vritti.crmlib.bean;

import java.io.Serializable;

/**
 * Created by sharvari on 17-Jul-17.
 */

public class Contactlist implements Serializable {

    String PKSuspectId;

    public String getPKSuspectId() {
        return PKSuspectId;
    }

    public void setPKSuspectId(String PKSuspectId) {
        this.PKSuspectId = PKSuspectId;
    }
}
