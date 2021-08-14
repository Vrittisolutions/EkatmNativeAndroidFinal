package com.vritti.chat.services;

import android.app.Activity;
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
import android.os.Handler;
import android.os.IBinder;

import com.vritti.chat.activity.AddChatRoomActivity;
import com.vritti.chat.activity.GroupmemberShowActivity;
import com.vritti.chat.bean.ChatGroupJson;
import com.vritti.chat.bean.ChatMessage;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class ChattingDataSendBackground extends Service {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    SharedPreferences userpreferences;
    //private CommonClass ut;
    String link, ChatRoomId, Message, MessageDate, MessageId, MessageType, FinalJson;
    ArrayList<ChatGroupJson> chatGroupJsonArrayList;
    ArrayList<ChatMessage> chatMessageArrayList1;
    int i;
    String Message_id, UUID;
    AddChatRoomActivity addChatRoomActivity;
    Activity activity;
    int pos = -1;
    SQLiteDatabase sql;
    Handler handle;
    private JSONObject obj;

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
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
         sql = db.getWritableDatabase();
        link = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        chatMessageArrayList1 = new ArrayList<>();
        chatGroupJsonArrayList = new ArrayList<>();

        Getchatgroupmessagedata();


        return super.onStartCommand(intent, flags, startId);

    }

    private void Getchatgroupmessagedata() {
        String query = "SELECT * FROM " + db.TABLE_GROUP_JSON;
       // SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(query, null);
        int Count = cur.getCount();
        if (Count > 0) {
            cur.moveToFirst();
            ChatGroupJson chatGroupJson = new ChatGroupJson();
            FinalJson = cur.getString(cur.getColumnIndex("FinalJson"));
            Message_id = cur.getString(cur.getColumnIndex("ID"));

              /*  chatGroupJson.setFinalJsonGroup(FinalJson);
                chatGroupJson.setMessage_id(Message_id);
                chatGroupJsonArrayList.add(chatGroupJson);*/
            if (FinalJson.contains("UUID")) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(FinalJson);
                    UUID = jsonObject.getString("UUID");
                    pos = jsonObject.getInt("pos");
                    Message=jsonObject.getString("senderMSG");
                    ChatRoomId=jsonObject.getString("ChatRoomId");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            if (isInternetAvailable(ChattingDataSendBackground.this)) {

                new StartSession(ChattingDataSendBackground.this, new CallbackInterface() {
                    @Override

                    public void callMethod() {
                        PostGroupMessageJSON postGroupMessageJSON = new PostGroupMessageJSON();
                        postGroupMessageJSON.execute(FinalJson, Message_id);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }

        } else {

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
            // String url = "aaa" + WebUrlClass.api_SendMessage;
            System.out.println("Back Message :" + url);



            try {
                res = ut.OpenPostConnection(url, params[0], ChattingDataSendBackground.this);
                if (res == null || res.equals("")){
                    handle=  new Handler(context.getMainLooper());
                    handle.post( new Runnable(){
                        public void run(){
                            sql.delete(db.TABLE_GROUP_JSON, "ID=?",
                                    new String[]{Message_id});
                            sql.delete(db.TABLE_CHAT_GROUP_MESSAGE, "MessageId=?",
                                    new String[]{UUID});
                            AddChatRoomActivity.updateUIDelete(UUID ,ChatRoomId, pos);                        }
                    });


                }else {
                  /*  response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    JSONArray jResults = null;*/

                    response =  String.valueOf(new JSONTokener(res.toString()).nextValue());

                  /*  try {
                        obj = new JSONObject(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray();

                  //  jResults = obj.getJSONArray("SendMessage");
                    try {
                        jResults = new JSONArray(response);
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject Jsonmessage = jResults.getJSONObject(i);
                            MessageDate = Jsonmessage.getString("MessageDate");
                            ChatRoomId = Jsonmessage.getString("ChatRoomId");
                            MessageId = Jsonmessage.getString("MessageId");
                            //Message=Jsonmessage.getString("Message");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
                handle=  new Handler(context.getMainLooper());
                handle.post( new Runnable(){
                    public void run(){
                        sql.delete(db.TABLE_GROUP_JSON, "ID=?",
                                new String[]{Message_id});
                        sql.delete(db.TABLE_CHAT_GROUP_MESSAGE, "MessageId=?",
                                new String[]{UUID});
                        AddChatRoomActivity.updateUIDelete(UUID ,ChatRoomId, pos);                        }
                });
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
            if(response != null) {
                if (response.equals("error")) {
                    sql.delete(db.TABLE_GROUP_JSON, "ID=?",
                            new String[]{Message_id});
                    sql.delete(db.TABLE_CHAT_GROUP_MESSAGE, "MessageId=?",
                            new String[]{UUID});
                    AddChatRoomActivity.updateUIDelete(UUID, ChatRoomId, pos);
                } else {
                    String res = integer.get(0);
                    String msgid = integer.get(1);
                    if (res.contains("MessageId")) {
                        // delete(Message_id);


                        sql.delete(db.TABLE_GROUP_JSON, "ID=?",
                                new String[]{msgid});

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("IsDownloaded", "Yes");
                        contentValues.put("ChatRoomId", ChatRoomId);
                        sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId=?", new String[]{UUID});

                        AddChatRoomActivity.updateUI(UUID, ChatRoomId, pos);

                        ContentValues values = new ContentValues();

                        values.put("StartTime", MessageDate);
                        values.put("ChatMessage", Message);
                        sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                        Cursor cur = sql.rawQuery("select * from " + db.TABLE_GROUP_JSON, null);
                        int a = cur.getCount();


                        Getchatgroupmessagedata();

                    }
                }
            }else {
                sql.delete(db.TABLE_GROUP_JSON, "ID=?",
                        new String[]{Message_id});
                AddChatRoomActivity.updateUIDelete(UUID, ChatRoomId, pos);
            }
        }


    }
   /* public void delete(String id) {
        sql.execSQL("delete from GroupJson where ID='"+id+"'");
    }*/





}
