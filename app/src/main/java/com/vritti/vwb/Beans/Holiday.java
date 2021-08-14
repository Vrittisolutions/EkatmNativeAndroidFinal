package com.vritti.vwb.Beans;

import java.io.Serializable;

/**
 * Created by sharvari on 02-Apr-19.
 */

public class Holiday implements Serializable {

    String ShortHolidayDate,DayName,HolidayReason;


    public String getShortHolidayDate() {
        return ShortHolidayDate;
    }

    public void setShortHolidayDate(String shortHolidayDate) {
        ShortHolidayDate = shortHolidayDate;
    }

    public String getDayName() {
        return DayName;
    }

    public void setDayName(String dayName) {
        DayName = dayName;
    }

    public String getHolidayReason() {
        return HolidayReason;
    }

    public void setHolidayReason(String holidayReason) {
        HolidayReason = holidayReason;
    }
}
