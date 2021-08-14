package com.vritti.vwb.ImageWithLocation;

import java.util.ArrayList;

public class Sample_List_Object {
    ArrayList<SamplePojoClass> samplePojoClassArrayList;

    public ArrayList<SamplePojoClass> getSamplePojoClassArrayList() {
        return samplePojoClassArrayList;
    }

    public void setSamplePojoClassArrayList(ArrayList<SamplePojoClass> samplePojoClassArrayList) {
        this.samplePojoClassArrayList = samplePojoClassArrayList;
    }

    public Sample_List_Object(ArrayList<SamplePojoClass> samplePojoClassArrayList) {
        this.samplePojoClassArrayList = samplePojoClassArrayList;
    }
}
