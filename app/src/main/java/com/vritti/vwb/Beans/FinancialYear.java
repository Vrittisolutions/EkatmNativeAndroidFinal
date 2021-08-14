package com.vritti.vwb.Beans;

import java.io.Serializable;

/**
 * Created by pradnya on 09-Oct-18.
 */

public class FinancialYear implements Serializable {

    String FYMasterId,FYCode;

    public String getFYMasterId() {
        return FYMasterId;
    }

    public void setFYMasterId(String FYMasterId) {
        this.FYMasterId = FYMasterId;
    }

    public String getFYCode() {
        return FYCode;
    }

    public void setFYCode(String FYCode) {
        this.FYCode = FYCode;
    }
}
