package com.vritti.ekatm.other;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;

import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;

public class SetAppName {


    public final static String AppNameFCM = getFcmAppName();
    public static final String IMAGE_DIRECTORY_NAME =getImageDirectoryName();

    private static String getFcmAppName() {
        String retn = "";
        if (Constants.type == Constants.Type.CRM) {
            retn = WebUrlClass.AppNameFCM_EKATM;

        } else if (Constants.type == Constants.Type.Vwb) {
            retn = WebUrlClass.AppNameFCM_EKATM;

        } else if (Constants.type == Constants.Type.PM) {
            retn = WebUrlClass.AppNameFCM_PM;
        }else if (Constants.type == Constants.Type.Delivery) {
            retn = WebUrlClass.AppNameFCM_Delivery;
        }else if (Constants.type == Constants.Type.MilkRun) {
            retn = WebUrlClass.AppNameFCM_MilkRun;
        }else if (Constants.type == Constants.Type.Sahara) {
            retn = WebUrlClass.AppNameFCM_Sahara;
        }else if (Constants.type == Constants.Type.Alfa) {
            retn = WebUrlClass.AppNameFCM_Alfa;
        }
        return retn;
    }

    private static String getImageDirectoryName() {
        String retn = "";
        if (Constants.type == Constants.Type.CRM) {
            retn = WebUrlClass.IMAGE_DIRECTORY_EKATM;

        } else if (Constants.type == Constants.Type.Vwb) {
            retn = WebUrlClass.IMAGE_DIRECTORY_EKATM;

        } else if (Constants.type == Constants.Type.PM) {
            retn = WebUrlClass.IMAGE_DIRECTORY_PM;
        }
        return retn;
    }


  /*  public static void setToolbar(Toolbar toolbar, String AppNameToolbar) {

        if (AppNameToolbar.equalsIgnoreCase(WebUrlClass.app_name_toolbar_PM)) {
            toolbar.setLogo(R.mipmap.ic_simplify);
            toolbar.setTitle(WebUrlClass.app_name_toolbar_PM);
        } else if (AppNameToolbar.equalsIgnoreCase(WebUrlClass.app_name_toolbar_Vwb)) {
            toolbar.setLogo(R.mipmap.ic_vwb);
            toolbar.setTitle(WebUrlClass.app_name_toolbar_Vwb);
        } else if (AppNameToolbar.equalsIgnoreCase(WebUrlClass.app_name_toolbar_CRM)) {
            toolbar.setLogo(R.mipmap.ic_crm);
            toolbar.setTitle(WebUrlClass.app_name_toolbar_CRM);
        }

    }*/


}
