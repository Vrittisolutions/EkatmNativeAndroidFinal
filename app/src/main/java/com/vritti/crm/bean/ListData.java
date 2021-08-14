package com.vritti.crm.bean;

import java.io.Serializable;

public class ListData implements Serializable

{

    String name,id;


    public ListData(String name, String id) {
        this.name = name;
        this.id = id;
    }
    public ListData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
