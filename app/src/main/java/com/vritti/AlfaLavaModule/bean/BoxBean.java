package com.vritti.AlfaLavaModule.bean;

import java.io.Serializable;

public class BoxBean implements Serializable {

    String BoxTypeMasterId,BoxCode,BoxName;

    public String getBoxTypeMasterId() {
        return BoxTypeMasterId;
    }

    public void setBoxTypeMasterId(String boxTypeMasterId) {
        BoxTypeMasterId = boxTypeMasterId;
    }

    public String getBoxCode() {
        return BoxCode;
    }

    public void setBoxCode(String boxCode) {
        BoxCode = boxCode;
    }

    public String getBoxName() {
        return BoxName;
    }

    public void setBoxName(String boxName) {
        BoxName = boxName;
    }
}
