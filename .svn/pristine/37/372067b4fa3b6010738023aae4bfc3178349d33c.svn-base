package com.vritti.crmlib.classes;

import android.content.Context;

/**
 * Created by sharvari on 04-Oct-17.
 */

public class GlobalValues {
    public  static String loadmore="null";

    public static void logOutUser(Context context) {

        context.getSharedPreferences("PREFERENCE", context.MODE_PRIVATE)
                .edit()
                .putString("loadmore", "null")
                .apply();
        GlobalValues.loadmore="null";


    }

}






