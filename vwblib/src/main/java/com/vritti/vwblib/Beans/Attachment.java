package com.vritti.vwblib.Beans;

import java.io.Serializable;

/**
 * Created by sharvari on 06-Apr-18.
 */

public class Attachment implements Serializable {

    public String Option;
    public String Count;

    public Attachment(String option, String count) {
        Option = option;
        Count = count;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getOption() {
        return Option;
    }

    public void setOption(String option) {
        Option = option;
    }
}
