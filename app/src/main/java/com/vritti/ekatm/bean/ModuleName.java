package com.vritti.ekatm.bean;

import java.io.Serializable;

public class ModuleName implements Serializable {

    String ModuleName="";
    public int image;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }
}
