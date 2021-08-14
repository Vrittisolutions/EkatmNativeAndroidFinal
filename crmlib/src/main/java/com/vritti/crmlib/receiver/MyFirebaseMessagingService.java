package com.vritti.crmlib.receiver;



import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import me.leolin.shortcutbadger.ShortcutBadger;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.ChatMessage;
import com.vritti.crmlib.bean.ChatUser;
import com.vritti.crmlib.chat.AddChatRoomActivity;
import com.vritti.crmlib.chat.MultipleGroupActivity;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.vcrm7.CallListActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

/**
 * Created by sharvari on 11-Apr-17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    private static final String TAG = "MyFirebaseMsgService";
    String Title,NotificationMessage;

    SQLiteDatabase sql;
    String Group_Message,CallId,ChatRoomId,ChatMessage,Add_ChatMessage,Chatroomname,ChatGroupMessage,MessageDate,MessageType,Status,MessageId,ChatRoomName,Attachment;
    ArrayList<ChatUser> chatUserArrayList;
    ArrayList<com.vritti.crmlib.bean.ChatMessage>chatMessageArrayList;
    Handler mHandler;
    String Outcome,Firmname,AssignedTo,date,AppointmentDate,Remarks,Address,Mobile,Data;
    SharedPreferences userpreferences;
    int count;
    NotificationChannel channel;
    String channel_id="Crm";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
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

        sql = db.getWritableDatabase();
        chatUserArrayList=new ArrayList<>();
        chatMessageArrayList=new ArrayList<>();


        //Displaying data in log
        //It is optional
       /* Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());*/
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());

        Log.d(TAG, "Notification Message ID: " + remoteMessage.getMessageId());
        Log.d(TAG, "Notification Message send time: " + remoteMessage.getSentTime());

        System.out.println("Message Get :" + remoteMessage.getMessageType());
        String notification = remoteMessage.getData().toString();
        String notification2 = remoteMessage.getMessageId().toString();



        ChatMessage=remoteMessage.getData().get("message");

        count=userpreferences.getInt("count",0);

        count=count+1;
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.putInt("count", count);
        editor.commit();

        ShortcutBadger.with(getApplicationContext()).count(count);


        if (ChatMessage.contains("User_Added")) {
        JSONObject mainObj = null;
        try {
            mainObj = new JSONObject(ChatMessage);
            if (mainObj != null) {
             //   Data= mainObj.getString("MsgType");

                String User=mainObj.getString("Msg");
                JSONArray jArray = new JSONArray(User);
                sql.delete(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null, null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null);
                int count = c.getCount();
                if (jArray.length() > 0) {
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject Jsonchatmember = jArray.getJSONObject(i);
                        ChatUser chatUser = new ChatUser();
                                        Chatroomname = Jsonchatmember.getString("ChatRoomName");
                                        chatUser.setChatroom(Chatroomname);
                                        ChatRoomId = Jsonchatmember.getString("ChatRoomId");
                                        chatUser.setChatRoomId(ChatRoomId);
                                        chatUser.setStatus(Jsonchatmember.getString("ChatRoomStatus"));
                                        chatUser.setStartTime(Jsonchatmember.getString("StartTime"));
                                        chatUser.setCreater(Jsonchatmember.getString("Creator"));
                                        CallId = Jsonchatmember.getString("ChatSourceId");
                                        chatUser.setChatSourceId(Jsonchatmember.getString("ChatSourceId"));
                                        chatUser.setAddedBy(Jsonchatmember.getString("AddedBy"));
                                        chatUser.setParticipantName(Jsonchatmember.getString("ParticipantName"));
                                        chatUser.setParticipantId(Jsonchatmember.getString("ParticipantId"));
                                        Add_ChatMessage = Jsonchatmember.getString("Message");
                                        chatUser.setMessage(ChatMessage);
                                        cf.AddGroupMember(chatUser);
                                        chatUserArrayList.add(chatUser);
                                    }
                    SendChattingNotification();

                                }

            }




        }catch (JSONException e) {
                    e.printStackTrace();
                }

        }else if (ChatMessage.contains("CHATMSG")) {
            JSONObject mainObj = null;
            try {
                mainObj = new JSONObject(ChatMessage);
                String User=mainObj.getString("Msg");
                JSONArray jArray = new JSONArray(User);
                               if (jArray != null) {
                                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE, null);
                                    int count = c.getCount();
                                    if (jArray.length() > 0) {
                                        for (int j = 0; j < jArray.length(); j++) {
                                            JSONObject Jsongroupmessage = jArray.getJSONObject(j);
                                            ChatMessage chatMessage = new ChatMessage();
                                            UserMasterId = Jsongroupmessage.getString("UserMasterId");
                                            chatMessage.setUserMasterId(UserMasterId);
                                            ChatRoomId = Jsongroupmessage.getString("ChatRoomId");
                                            chatMessage.setChatRoomId(ChatRoomId);
                                            Group_Message = Jsongroupmessage.getString("Message");
                                            chatMessage.setMessage(Group_Message);
                                            MessageDate = Jsongroupmessage.getString("MessageDate");
                                            chatMessage.setMessageDate(MessageDate);
                                            MessageId = Jsongroupmessage.getString("MessageId");
                                            chatMessage.setMessageId(MessageId);
                                            UserName = Jsongroupmessage.getString("UserName");
                                            chatMessage.setUsername(UserName);
                                            Status = Jsongroupmessage.getString("Status");
                                            chatMessage.setStatus(Status);
                                            MessageType = Jsongroupmessage.getString("MessageType");
                                            chatMessage.setMessageType(MessageType);
                                            ChatRoomName = Jsongroupmessage.getString("ChatRoomName");
                                            Attachment=Jsongroupmessage.getString("Attachment");
                                            chatMessage.setIsDownloaded("No");
                                            if (Attachment.equals("")||Attachment.equals("null")){
                                                chatMessage.setAttachment(null);
                                            }else {
                                                chatMessage.setAttachment(CompanyURL + Attachment);
                                            }
                                            cf.AddGroupMessage(chatMessage);
                                            chatMessageArrayList.add(chatMessage);
                                        }

                                        Intent intent = new Intent();
                                        intent.setAction("chatscrren");
                                        intent.putExtra("message", "1");
                                        intent.putExtra("ChatRoomid", ChatRoomId);
                                        intent.putExtra("Chatroomname", ChatRoomName);
                                        context.sendBroadcast(intent);
                                        if (ChatRoomName.equals(AddChatRoomActivity.ChatRoomName)) {
                                        }
                                        else {
                                            SendGroupChattingNotification();
                                        }


                                    }
                                }






            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        else {


            try {
                JSONObject mainObj = null;
                try {

                    mainObj = new JSONObject(ChatMessage);
                    String User=mainObj.getString("Msg");
                    if (User!=null) {
                        JSONObject obj = new JSONObject(User);
                        Firmname = obj.getString("FirmName");
                        System.out.println("Message Firm name :" + Firmname);
                        AppointmentDate = obj.getString("AppointmentDate");
                        date = getDate(AppointmentDate);
                        System.out.println("Message AppointmentDate :" + date);
                        AssignedTo = obj.getString("AssignedTo");
                        System.out.println("Message AssignedTo :" + AssignedTo);
                        Remarks = obj.getString("Remarks");
                        System.out.println("Message Remarks :" + Remarks);
                        Address = obj.getString("Address");
                        System.out.println("Message Address  :" + Address);
                        Mobile = obj.getString("Mobile");
                        System.out.println("Message Mobile   :" + Mobile);
                        Outcome = obj.getString("OutCome");
                        System.out.println("Message Mobile   :" + Outcome);
                    }
                    if (Outcome.equalsIgnoreCase("Appointment")) {
                        Title = "Appointment Notification";
                        NotificationMessage = AssignedTo + " has set an appoinment for you with " + Firmname + " , " + Address + " on " + AppointmentDate + " , " + Mobile + " , " + Remarks;
                    } else if (Outcome.equalsIgnoreCase("Call again")) {
                        Title = "Call Again Notification";
                        NotificationMessage = "Call has been  scheduled again on " + AppointmentDate;
                    } else if (Outcome.equalsIgnoreCase("Visit")) {
                        Title = "Visit Notification";
                        NotificationMessage = "Visit has been scheduled on " + AppointmentDate;
                    } else if (Outcome.equalsIgnoreCase("Order Received")) {
                        Title = "Order Received Notification";
                        NotificationMessage = " Order for " + Firmname + " for product has been received on " + AppointmentDate;
                    } else if (Outcome.equalsIgnoreCase("Transfer To SE")) {
                        Title = "Transfer To SE Notification";
                        NotificationMessage = AssignedTo + " has assigned a call to you on " + AppointmentDate;
                    } else if (Outcome.equalsIgnoreCase("Call Close without order")) {
                        Title = "Call Close without order Notification";
                        NotificationMessage = "Call has been closed without order of " + Firmname;
                    } else if (Outcome.equalsIgnoreCase("Reschedule")) {
                        Title = "Call Reschedule Notification";
                        NotificationMessage = "Call of " + Firmname + " has been scheduled on " + AppointmentDate;
                    } else if (Outcome.equalsIgnoreCase("Transfer To BOE")) {
                        Title = "Transfer To BOE Notification";
                        NotificationMessage = AssignedTo + " has assigned a call to you on " + AppointmentDate;

                    }else if (Outcome.equalsIgnoreCase("Demo Request")) {
                        Title = "Demo Request";
                        NotificationMessage = AssignedTo + " has been requested you to give demo on " + Firmname+ " on " +AppointmentDate;
                    }
                    sendNotification(date, AssignedTo, Mobile, Firmname, Address, Remarks);


                }catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + ChatMessage + "\"");
                }


                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + ChatMessage + "\"");
                }

            }

        }


    //This method is only generating push notification
    //It is same as we did in earlier posts

    private void sendNotification(String appointmentdate,String assigned,String mobile,String firmname,String address,String remark ){
        Intent intent = new Intent(this, CallListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent piResult = PendingIntent.getActivity(this, 0, intent, 0);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel= new NotificationChannel(channel_id,
                    "data",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            channel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            channel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setShowBadge(true);


        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this);
        Notification notification = mBuilder
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.crm_logo_2) //ok
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.crm_logo_2))
                .setContentTitle(Title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(NotificationMessage))
                .setContentIntent(piResult)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText(NotificationMessage).build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notification);

        notificationManager.notify(0, notification);


     /* NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());*/


      /* Notification notification = new Notification.InboxStyle(builder)
                .setBigContentTitle(assigned+"has set an appoinment for you with"+firmname+","+address+"on"+appointmentdate+""+mobile+""+remark)
                .build();*/
       /* NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(121,notification);*/
    }

    // Chatting Used Notification

    private void SendChattingNotification( ){
        Intent intent = new Intent(this, MultipleGroupActivity.class);
        intent.putExtra("callid",CallId);
        intent.putExtra("ChatRoomid",ChatRoomId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent piResult = PendingIntent.getActivity(this, 0, intent, 0);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int notifyID = 1;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel= new NotificationChannel(channel_id,
                    "data",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            channel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            channel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setShowBadge(true);


        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this);
        Notification notification = mBuilder
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.crm_logo_2) //ok
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.crm_logo_2))
                .setContentTitle(Chatroomname)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Add_ChatMessage))
                .setContentIntent(piResult)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText(Add_ChatMessage).build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notification);

        notificationManager.notify(notifyID, notification);


    }
    private void SendGroupChattingNotification( ){
        Intent intent = new Intent(this, AddChatRoomActivity.class);
        intent.putExtra("ChatRoomid",ChatRoomId);
        intent.putExtra("Chatroomname",ChatRoomName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent piResult = PendingIntent.getActivity(this, 0, intent, 0);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel= new NotificationChannel(channel_id,
                    "data",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            channel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            channel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setShowBadge(true);


        }
        if (Attachment.equals("")) {

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.crm_logo_2) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.crm_logo_2))
                    .setContentTitle(UserName)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(Group_Message))
                    .setContentIntent(piResult)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setGroup(Group_Message)
                    .setGroupSummary(true)
                    .setContentText(Group_Message).build();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notification);


            notificationManager.notify(0, notification);
        }else if(Attachment.contains(".pdf")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.crm_logo_2) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.crm_logo_2))
                    .setContentTitle(UserName)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Pdf Received"))
                    .setContentIntent(piResult)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentText("Pdf Received").build();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notification);

            notificationManager.notify(0, notification);

        }else if(Attachment.contains(".mp3")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.crm_logo_2) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.crm_logo_2))
                    .setContentTitle(UserName)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Audio Received"))
                    .setContentIntent(piResult)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentText("Audio Received").build();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notification);

            notificationManager.notify(0, notification);

        }else if(Attachment.contains(".mp4")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.crm_logo_2) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.crm_logo_2))
                    .setContentTitle(UserName)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Video Received"))
                    .setContentIntent(piResult)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentText("Video Received").build();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notification);

            notificationManager.notify(0, notification);

        }
        else if(Attachment.contains(".jpg")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.crm_logo_2) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.crm_logo_2))
                    .setContentTitle(UserName)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Image Received"))
                    .setContentIntent(piResult)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentText("Image Received").build();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notification);


            notificationManager.notify(0, notification);

        }

    }

private  String getDate(String data){
    Date date = null;
    String result = "";

    SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy HH:mm");
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    try {
        date = format.parse(data);
        result = format1.format(date);

    }catch (Exception ex){}


    return result;
}


}