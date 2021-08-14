package com.vritti.expensemanagement;

import java.io.Serializable;

/**
 * Created by sharvari on 18-Sep-19.
 */

public class Expenses implements Serializable {



    public String text_for_assistant ="";

    public String user_type ="";
    public String Exp_type ="expense";

    public String getExp_type() {
        return Exp_type;
    }

    public void setExp_type(String exp_type) {
        Exp_type = exp_type;
    }

    public Expenses(String text_for_assistant, String user_type) {
        this.text_for_assistant = text_for_assistant;
        this.user_type = user_type;
    }

    public String getText_for_assistant() {
        return text_for_assistant;
    }

    public void setText_for_assistant(String text_for_assistant) {
        this.text_for_assistant = text_for_assistant;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
