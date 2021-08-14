package com.vritti.ekatm.other;

import android.content.Context;
import android.os.AsyncTask;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Interface.CallBack;


/**
 * Created by Admin-1 on 11/23/2016.
 */
public class ValidateUser {

    private CallBack mcallBack;
    private static Context pContext;
    private DatabaseHandlers db;
    private static Utility ut;

    public ValidateUser(String EnvID, String UserID, String psw, String plantId, Context context, CallBack callBack) {
        this.pContext = context;
        this.mcallBack = callBack;
        this.pContext = context;
        db = new DatabaseHandlers(context);
        ut = new Utility();
        new CheckValid_USer().execute(EnvID, UserID, psw,plantId);

    }

    public class CheckValid_USer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Object res;
            String response;

            String url = ut.getSharedPreference_URL(pContext) + WebUrlClass.api_GetIsValidUser + "?AppEnvMasterId="
                    + params[0] + "&PlantId="+params[3] +"&UserLoginId=" + params[1] +
                    "&UserPwd=" + params[2];
            try {
                url = url.replaceAll(" ","%20");
                res = Utility.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("false")) {//Invalid Password And Plant,Invalid Password,You are not valid user for selected plant
                mcallBack.failCall("User Not Valid");
            } else if (s.contains("true")) {
                mcallBack.onCall();
            }else if (s.contains("Invalid Password And Plant")) {
                mcallBack.failCall("Invalid Password And Plant");
            }else if (s.contains("Invalid Password")) {
                mcallBack.failCall("Invalid Password");
            }else if (s.contains("You are not valid user for selected plant")) {
                mcallBack.failCall("You are not valid user for selected plant");
            }else{
                mcallBack.failCall("Server error...");
            }
        }
    }
}
