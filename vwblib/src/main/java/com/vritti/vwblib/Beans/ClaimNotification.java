package com.vritti.vwblib.Beans;

import java.io.Serializable;

/**
 * Created by sharvari on 31-May-18.
 */

public class ClaimNotification implements Serializable {


    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
