package com.vritti.sales.beans;

import java.util.ArrayList;

public class BillNoClass {

    String billNo;
    int bill_no = 0;

    ArrayList<CounterbillingBean> cbillList;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public ArrayList<CounterbillingBean> getCbillList() {
        return cbillList;
    }

    public void setCbillList(ArrayList<CounterbillingBean> cbillList) {
        this.cbillList = cbillList;
    }

    public int getBill_no() {
        return bill_no;
    }

    public void setBill_no(int bill_no) {
        this.bill_no = bill_no;
    }
}