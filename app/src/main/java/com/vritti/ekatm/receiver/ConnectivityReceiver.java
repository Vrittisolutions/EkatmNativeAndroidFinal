package com.vritti.ekatm.receiver;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.google.gson.Gson;
import com.vritti.chat.activity.AddChatRoomActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.activity.ActivityModuleSelection;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.TeamChatListObject;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.Myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConnectivityReceiver extends BroadcastReceiver {
    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver() {
        super();
    }

    Context context;
    public  FirebaseJobDispatcher dispatcher ;
    public  Job myJob = null;

    // post chat
    DatabaseHandlers db;
    CommonFunction cf;
    private String settingKey, dabasename , CompanyURL = "",   UUID , FinalJson , Message_id;
    int pos = -1;
    public  SQLiteDatabase sql;
    Utility ut;
    ArrayList<String> chatUserArrayList = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            ut = new Utility();
            String settingKey = ut.getSharedPreference_SettingKey(context);
            dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
            db = new DatabaseHandlers(context, dabasename);
            CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();
            this.context = context;
            myJob = ActivityModuleSelection.myJob;
            dispatcher = ActivityModuleSelection.dispatcher;
            sql = db.getWritableDatabase();
       /* if(isConnected){
            setJobShedulder();
        }else {
            if(dispatcher != null)
                dispatcher.cancelAll();
        }*/
            if (connectivityReceiverListener != null) {

                //connectivityReceiverListener.onNetworkConnectionChanged(isConnected);

            }
            if (isConnected() != AppCommon.getInstance(context).getConnectionStatus()) {
                if (isConnected) {
                    updateChatLocalData(context);
                    AppCommon.getInstance(context).IsConnected(true);
                } else
                    AppCommon.getInstance(context).IsConnected(false);
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                Intent intent1 = new Intent(context, SendOfflineData.class);
                intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                        WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
                context.startService(intent1);
            }

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void updateChatLocalData(Context context) {
        getJson();
    }

    private void getJson() {
        TeamChatListObject teamChatListObject = new Gson().fromJson(AppCommon.getInstance(context).getChatJson() , TeamChatListObject.class);
        if (teamChatListObject != null){
            chatUserArrayList = teamChatListObject.getFinalJsonList();
            if(chatUserArrayList!= null && chatUserArrayList.size()!= 0){
                for (final String chatJson : chatUserArrayList){
                    FinalJson = chatJson;

                    if (FinalJson != null) {
                        String UUID1 = null;
                        if (FinalJson.contains("UUID")) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(FinalJson);
                                UUID1 = jsonObject.getString("UUID");
                               UUID = UUID1;
                                pos = jsonObject.getInt("pos");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (ConnectivityReceiver.isConnected()) {

                            final String finalUUID = UUID1;
                            new StartSession(context, new CallbackInterface() {
                                @Override

                                public void callMethod() {

                                    PostGroupMessageJSON postGroupMessageJSON = new PostGroupMessageJSON();
                                    postGroupMessageJSON.execute(chatJson, finalUUID);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                    }
                }
                AppCommon.getInstance(context).setChatJson(null);

            }
        }


       // Message_id = cur.getString(cur.getColumnIndex("ID"));

    }





    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) Myapplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
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
           // link = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
            String url = CompanyURL + WebUrlClass.api_SendMessage;
            // String url = "aaa" + WebUrlClass.api_SendMessage;
            System.out.println("Back Message :" + url);
            Log.d("chatTestSend : ", params[0]);
            try {
                res = ut.OpenPostConnection(url, params[0], context);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject Jsonmessage = jResults.getJSONObject(i);
                       /* MessageDate = Jsonmessage.getString("MessageDate");
                        ChatRoomId = Jsonmessage.getString("ChatRoomId");
                        MessageId = Jsonmessage.getString("MessageId");
                        Message=Jsonmessage.getString("Message");*/

                    }

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
            if (res.contains("MessageId")) {
                // delete(Message_id);

                sql.delete(db.TABLE_GROUP_JSON, "ID=?",
                        new String[]{msgid});
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject Jsonmessage = jResults.getJSONObject(i);
                        String  MessageDate = Jsonmessage.getString("MessageDate");
                        String ChatRoomId = Jsonmessage.getString("ChatRoomId");
                        String MessageId = Jsonmessage.getString("MessageId");
                        String  Message=Jsonmessage.getString("Message");
                        Log.d("chatTestRespons : ", Jsonmessage.toString());

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("IsDownloaded", "Yes");
                      //  contentValues.put("ChatRoomId", ChatRoomId);
                      int a =  sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId=?", new String[]{msgid});

                        int mgsPos = -1;
                        if(chatUserArrayList!= null && chatUserArrayList.size()!= 0){
                        for(String object : chatUserArrayList){
                            JSONObject jsonObject = new JSONObject(object);
                            if(jsonObject.getString("senderMSG").equals(Message)){
                                mgsPos = jsonObject.getInt("pos");
                            }
                        }}
                        ContentValues values = new ContentValues();
                        if(!isApplicationSentToBackground()){
                            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                            if(cn.getClassName().equals("com.vritti.chat.activity.AddChatRoomActivity")){
                                if(ChatRoomId.equals(AddChatRoomActivity.ChatRoomId)) {
                                    if (mgsPos != -1)
                                        AddChatRoomActivity.updateUI(msgid, ChatRoomId, mgsPos);
                                    else
                                        AddChatRoomActivity.updateUI(msgid, ChatRoomId, pos);
                                }
                            }
                        }
                        values.put("StartTime", MessageDate);
                        values.put("ChatMessage",Message);
                        sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                        Cursor cur = sql.rawQuery("select * from " + db.TABLE_GROUP_JSON, null);
                        int a1 = cur.getCount();



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
              /*  ContentValues contentValues = new ContentValues();
                contentValues.put("IsDownloaded", "Yes");
                contentValues.put("ChatRoomId", ChatRoomId);
                sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId=?", new String[]{UUID});

                AddChatRoomActivity.updateUI(UUID ,ChatRoomId, pos);

                ContentValues values = new ContentValues();

                values.put("StartTime", MessageDate);
                values.put("ChatMessage",Message);
                sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                Cursor cur = sql.rawQuery("select * from " + db.TABLE_GROUP_JSON, null);
                int a = cur.getCount();
                sql.close();
                Getchatgroupmessagedata();*/

            }


        }


    }
    private boolean isApplicationSentToBackground() {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
