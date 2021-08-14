package com.vritti.vwb.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DatasheetListObject {
    ArrayList<Datasheet> datasheets;
    ArrayList<EditDatasheet> editDatasheets;
    ArrayList<EditDatasheetNew> editDatasheetNewArrayList;




    public DatasheetListObject(ArrayList<Datasheet> datasheetlists) {
        this.datasheets = datasheetlists;
    }

    public DatasheetListObject(ArrayList<EditDatasheet> editDatasheetslist,int e) {
        this.editDatasheets = editDatasheetslist;
    }

    public DatasheetListObject(ArrayList<EditDatasheetNew> editDatasheetAdapterNewArrayList, String common) {
        this.editDatasheetNewArrayList = editDatasheetAdapterNewArrayList;

    }

    public ArrayList<Datasheet> getDatasheets() {
        return datasheets;
    }

    public ArrayList<EditDatasheet> geteditDatasheets() {
        return editDatasheets;
    }

    public ArrayList<EditDatasheetNew> geteditDatasheetsNew() {
        return editDatasheetNewArrayList;
    }

    public void setDatasheets(ArrayList<Datasheet> datasheets) {
        this.datasheets = datasheets;
    }
}