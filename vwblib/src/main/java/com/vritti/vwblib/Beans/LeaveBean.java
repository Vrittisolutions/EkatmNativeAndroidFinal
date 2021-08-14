package com.vritti.vwblib.Beans;

/**
 * Created by Admin-1 on 6/7/2017.
 */

public class LeaveBean {
    String  LeaveCode ,
            OpenBal ,
            Credit ,
            Consumed,
            Balance,
            OpenBalPer,
            CreditPer,
            ConsumedPer,
            BalancePer ;

    public String getLeaveCode() {
        return LeaveCode;
    }

    public void setLeaveCode(String leaveCode) {
       LeaveCode = leaveCode;
    }

    public String getOpenBal() {
        return OpenBal;
    }

    public void setOpenBal(String openBal) {
        OpenBal = openBal;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getConsumed() {
        return Consumed;
    }

    public void setConsumed(String consumed) {
        Consumed = consumed;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getOpenBalPer() {
        return OpenBalPer;
    }

    public void setOpenBalPer(String openBalPer) {
        OpenBalPer = openBalPer;
    }

    public String getCreditPer() {
        return CreditPer;
    }

    public void setCreditPer(String creditPer) {
        CreditPer = creditPer;
    }

    public String getConsumedPer() {
        return ConsumedPer;
    }

    public void setConsumedPer(String consumedPer) {
        ConsumedPer = consumedPer;
    }

    public String getBalancePer() {
        return BalancePer;
    }

    public void setBalancePer(String balancePer) {
        BalancePer = balancePer;
    }
}
