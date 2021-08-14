package com.vritti.ekatm.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;


import com.vritti.chat.activity.AddChatRoomActivity;
import com.vritti.chat.bean.ChatMessage;
import com.vritti.chat.bean.ChatModelObject;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.services.MyFirebaseMessagingService;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class NotificationReceiver extends BroadcastReceiver {
    Utility ut;
    Context context;
    String userName, uuidInString, UserMasterId, CompanyURL, dabasename;
    CommonFunction cf;
    public SQLiteDatabase sql;
    DatabaseHandlers db;
    int notificationId;
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        this.context = context;
        cf = new CommonFunction(context);
        ut = new Utility();
        String settingKey = ut.getSharedPreference_SettingKey(context);
        dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        sql = db.getWritableDatabase();
        userName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        uuidInString = String.valueOf(UUID.randomUUID());
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);

        //if there is some input
        if (remoteInput != null) {

            //getting the input value
            CharSequence name = remoteInput.getCharSequence(MyFirebaseMessagingService.NOTIFICATION_REPLY);

            //updating the notification with the input value
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, MyFirebaseMessagingService.CHANNNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_menu_info_details)
                    .setContentTitle("Hey Thanks, " + name);
            NotificationManager notificationManager = (NotificationManager) context.
                    getSystemService(Context.NOTIFICATION_SERVICE);
            //notificationManager.notify(MyFirebaseMessagingService.NOTIFICATION_ID, mBuilder.build());
            Log.d("data , ", intent.getStringExtra("ChatRoomid"));
            sendData(name, intent.getStringExtra("ChatRoomid"));
            notificationId = intent.getIntExtra("notificationID" , 0);
            String messageId = intent.getStringExtra("messageId");
           this.notificationManager = notificationManager;
            ContentValues contentValues = new ContentValues();
            contentValues.put("IsDownloaded", "Yes");

            //  contentValues.put("ChatRoomId", ChatRoomId);
            int a = sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId=?", new String[]{messageId});
        }

        //if help button is clicked
        if (intent.getIntExtra(MyFirebaseMessagingService.KEY_INTENT_HELP, -1) == MyFirebaseMessagingService.REQUEST_CODE_HELP) {
            Toast.makeText(context, "Message will be send!", Toast.LENGTH_LONG).show();
        }

        //if more button is clicked
        if (intent.getIntExtra(MyFirebaseMessagingService.KEY_INTENT_MORE, -1) == MyFirebaseMessagingService.REQUEST_CODE_MORE) {
            Toast.makeText(context, "Message will be send!", Toast.LENGTH_LONG).show();
        }
    }

    private void sendData(CharSequence Chat_message, CharSequence chatRoomId) {

        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("ChatRoomId", String.valueOf(chatRoomId));
            jsonObject.put("senderId", UserMasterId);
            jsonObject.put("senderName", userName);
            String MessageEncode = StringEscapeUtils.escapeJava(String.valueOf(Chat_message));
            jsonObject.put("senderMSG", MessageEncode);
            jsonObject.put("senderMSGType", "text:");
            jsonObject.put("senderMSGtime", startTime());
            jsonObject.put("Attachment", "");
            jsonObject.put("pos", "");
            UUID uuid = UUID.randomUUID();
            uuidInString = uuid.toString();
            jsonObject.put("UUID", uuidInString);

            final ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessage(String.valueOf(Chat_message));
            chatMessage.setStatus("Sender");
            chatMessage.setMessageDate(System.currentTimeMillis());
            chatMessage.setUsername(userName);
            chatMessage.setMessageId(uuidInString);
            chatMessage.setChatRoomId(String.valueOf(chatRoomId));
            chatMessage.setUserMasterId(UserMasterId);
            chatMessage.setMessageType("text:");
            chatMessage.setAttachment("");
            chatMessage.setIsDownloaded("Yes");
            //chatMessageArrayList1.add(chatMessage);
            ChatModelObject generalItem = new ChatModelObject();
            generalItem.setChatMessage(chatMessage);
            //consolidatedList.add(generalItem);

            /*Intent intent = new Intent(context, ChattingDataSendBackground.class);
            intent.putExtra("pos", consolidatedList.size() - 1);
            context.startService(intent);*/
            cf.AddGroupMessage(chatMessage);

            String messageId = String.valueOf(chatRoomId);
            if (ConnectivityReceiver.isConnected()) {

                final String finalUUID = messageId;
                new StartSession(context, new CallbackInterface() {
                    @Override

                    public void callMethod() {

                        PostGroupMessageJSON postGroupMessageJSON = new PostGroupMessageJSON();
                        postGroupMessageJSON.execute(jsonObject.toString(), finalUUID);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String startTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        return dateFormat.format(new Date()).toString();
       // return null;
    }

    private class PostGroupMessageJSON extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

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
            String res = integer.get(0);
            String msgid = integer.get(1);
            if (res.contains("MessageId")) {
                // delete(Message_id);

                sql.delete(db.TABLE_GROUP_JSON, "ID=?",
                        new String[]{msgid});
                JSONArray jResults = null;
                try {
                    notificationManager.cancel(notificationId);
                    jResults = new JSONArray(response);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject Jsonmessage = jResults.getJSONObject(i);
                        String MessageDate = Jsonmessage.getString("MessageDate");
                        String ChatRoomId = Jsonmessage.getString("ChatRoomId");
                        String MessageId = Jsonmessage.getString("MessageId");
                        String Message = Jsonmessage.getString("Message");
                        Log.d("chatTestRespons : ", Jsonmessage.toString());

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("IsDownloaded", "Yes");
                        contentValues.put("MessageId", MessageId);

                        //  contentValues.put("ChatRoomId", ChatRoomId);
                        int a = sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId=?", new String[]{msgid});

                        int mgsPos = -1;
                      //  AddChatRoomActivity.updateUI(uuidInString, ChatRoomId, pos);

                        ContentValues values = new ContentValues();
                        values.put("StartTime", MessageDate);
                        values.put("ChatMessage", Message);
                        sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }
}
