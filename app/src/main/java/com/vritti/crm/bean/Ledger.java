package com.vritti.crm.bean;

import java.io.Serializable;

public class Ledger implements Serializable {
    String TransNo,EffectiveDate,Debit,Credit,TransNarrative;

    public String getTransNo() {
        return TransNo;
    }

    public void setTransNo(String transNo) {
        TransNo = transNo;
    }

    public String getEffectiveDate() {
        return EffectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        EffectiveDate = effectiveDate;
    }

    public String getDebit() {
        return Debit;
    }

    public void setDebit(String debit) {
        Debit = debit;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getTransNarrative() {
        return TransNarrative;
    }

    public void setTransNarrative(String transNarrative) {
        TransNarrative = transNarrative;
    }
}
