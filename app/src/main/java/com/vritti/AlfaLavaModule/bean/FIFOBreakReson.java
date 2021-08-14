package com.vritti.AlfaLavaModule.bean;

import java.io.Serializable;

public class FIFOBreakReson implements Serializable {

    String ConfigurationDetailId,Configuration;

    public String getConfigurationDetailId() {
        return ConfigurationDetailId;
    }

    public void setConfigurationDetailId(String configurationDetailId) {
        ConfigurationDetailId = configurationDetailId;
    }

    public String getConfiguration() {
        return Configuration;
    }

    public void setConfiguration(String configuration) {
        Configuration = configuration;
    }
    @Override
    public String toString() {
        return Configuration;
    }
}
