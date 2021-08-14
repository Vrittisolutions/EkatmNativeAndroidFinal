package com.vritti.crmlib.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sharvari on 27-Oct-17.
 */

public class Room {
    public ArrayList<String> member;
    public Map<String, String> groupInfo;

    public Room(){
        member = new ArrayList<>();
        groupInfo = new HashMap<String, String>();
    }
}
