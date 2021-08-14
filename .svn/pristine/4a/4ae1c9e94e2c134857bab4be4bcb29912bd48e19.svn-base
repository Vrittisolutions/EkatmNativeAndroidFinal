package com.vritti.crmlib.services;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;


import com.vritti.crmlib.bean.ChatGroupJson;
import com.vritti.crmlib.bean.ChatMessage;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class ChattingDataSendBackground extends Service {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    SharedPreferences userpreferences;
    //private CommonClass cc;
    String link, ChatRoomId, Message, MessageDate, MessageId, MessageType, FinalJson;
    ArrayList<ChatGroupJson> chatGroupJsonArrayList;
    ArrayList<ChatMessage> chatMessageArrayList1;
    int i;
    String Message_id,UUID;



    public ChattingDataSendBackground() {


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");


        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        //  cc = new CommonClass();
        link = userpreferences.getString("CompanyURL", null);
        chatMessageArrayList1 = new ArrayList<>();
        chatGroupJsonArrayList = new ArrayList<>();



        Getchatgroupmessagedata();


        return super.onStartCommand(intent, flags, startId);

    }

    private void Getchatgroupmessagedata() {
        String query = "SELECT * FROM " + db.TABLE_GROUP_JSON;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(query, null);
        int Count = cur.getCount();
        if (Count > 0) {
                cur.moveToFirst();
                ChatGroupJson chatGroupJson = new ChatGroupJson();
                FinalJson = cur.getString(cur.getColumnIndex("FinalJson"));
                Message_id=cur.getString(cur.getColumnIndex("ID"));
              /*  chatGroupJson.setFinalJsonGroup(FinalJson);
                chatGroupJson.setMessage_id(Message_id);
                chatGroupJsonArrayList.add(chatGroupJson);*/
              if (FinalJson.contains("UUID")){
                  JSONObject jsonObject;
                  try {
                      jsonObject = new JSONObject(FinalJson);
                      UUID=jsonObject.getString("UUID");
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }

            if (isInternetAvailable(ChattingDataSendBackground.this)){
                PostGroupMessageJSON postGroupMessageJSON=new PostGroupMessageJSON();
                postGroupMessageJSON.execute(FinalJson,Message_id);
            }

        }else {

        }

    }


    public static boolean isInternetAvailable(Context parent) {
        ConnectivityManager cm = (ConnectivityManager) parent
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    class PostGroupMessageJSON extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String url = link + WebUrlClass.api_SendMessage;
            System.out.println("Back Message :" + url);

            try {
                res = ut.OpenPostConnection(url, params[0],getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject Jsonmessage = jResults.getJSONObject(i);
                        MessageDate=Jsonmessage.getString("MessageDate");
                        ChatRoomId=Jsonmessage.getString("ChatRoomId");
                        MessageId=Jsonmessage.getString("MessageId");

                    }
                    ContentValues values = new ContentValues();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("Back Msg Response :" + response);
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);
            data.add(params[1]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> integer) {
            super.onPostExecute(integer);
            // progressDialog.dismiss();
            String res = integer.get(0);
            String msgid = integer.get(1);
            if (res.contains("MessageId")){
                // delete(Message_id);
                SQLiteDatabase sql =db.getWritableDatabase();

                sql.delete(db.TABLE_GROUP_JSON, "ID=?",
                        new String[]{msgid});

                ContentValues contentValues=new ContentValues();
                contentValues.put("IsDownloaded", "Yes");
                contentValues.put("ChatRoomId", ChatRoomId);
                sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId=?",new String[]{UUID} );



                ContentValues values = new ContentValues();
                values.put("StartTime", MessageDate);
                sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                Cursor cur = sql.rawQuery("select * from "+db.TABLE_GROUP_JSON,null);
                int a = cur.getCount();
                sql.close();
                Getchatgroupmessagedata();

            }

        }


    }
   /* public void delete(String id) {
        sql.execSQL("delete from GroupJson where ID='"+id+"'");
    }*/


}
