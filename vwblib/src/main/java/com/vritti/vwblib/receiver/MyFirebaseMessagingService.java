package com.vritti.vwblib.receiver;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwblib.Beans.ActivityBean;
import com.vritti.vwblib.Beans.ClaimNotification;
import com.vritti.vwblib.chat.AddChatRoomActivity;
import com.vritti.vwblib.chat.ChatGroup;
import com.vritti.vwblib.chat.ChatMessage;
import com.vritti.vwblib.chat.ChatUser;
import com.vritti.vwblib.chat.OpenChatroomActivity;
import com.vritti.vwblib.classes.CommonFunction;
import com.vritti.vwblib.vworkbench.ActivityDetailsActivity;
import com.vritti.vwblib.vworkbench.ActivityMain;
import com.vritti.vwblib.vworkbench.ClaimNotificationActivity;



import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;
import com.vritti.vwblib.R;


/**
 * Created by pradnya on 11-Apr-17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    String CompanyURL, EnvMasterId = "", LoginId ="", Password = "", PlantMasterId = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;

    private static final String TAG = "MyFirebaseMsgService";
    String FinalObj, UserMasterId;
    public static SharedPreferences userpreferences;
    String notification4;
    String Titile, NotificationMessage;
    SQLiteDatabase sql;
    String MessageDecode,Group_Message, CallId, ChatRoomId, ChatMessage, Add_ChatMessage, Chatroomname, ChatGroupMessage, MessageDate, MessageType, UserName, Status, MessageId, ChatRoomName,Attachment;
    ArrayList<ChatUser> chatUserArrayList;
    ArrayList<com.vritti.vwblib.chat.ChatMessage> chatMessageArrayList;
    ArrayList<ChatGroup> chatGroupArrayList;
    ArrayList <ClaimNotification>claimNotificationArrayList;
    Handler mHandler;
    Context context;
    String Outcome, Firmname, AssignedTo, date, AppointmentDate, Remarks, Address, Mobile, Data;

    NotificationChannel channel;
    String channel_id="one";
    String channel_id1="two";
    String channel_id2="three";
    String channel_id3="four";
    String channel_id4="five";
    ActivityBean bean=new ActivityBean();
    ArrayList<ActivityBean> activityBeen=new ArrayList<>();
    int Notificationcount,ChatMessageCount=0;

    static ArrayList<String> notifications = new ArrayList<>();
    private String ChatRoomStatus,Groupstatus;
    private ChatUser chatUser;
    private FirebaseJobDispatcher mDispatcher;


   /* @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        context=this;

        Toast.makeText(context,"Hi notification",Toast.LENGTH_SHORT).show();
    }*/




   static void start(Context context) {

       Intent notificationServiceIntent = new Intent(context, MyFirebaseMessagingService.class);
       context.startService(notificationServiceIntent);
   }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        context=this;
        ut = new Utility();
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        sql = db.getWritableDatabase();
        chatUserArrayList=new ArrayList<ChatUser>();
        claimNotificationArrayList=new ArrayList<>();
        chatGroupArrayList=new ArrayList<>();
        activityBeen = new ArrayList<ActivityBean>();
        chatMessageArrayList=new ArrayList<ChatMessage>();
       // Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());

        System.out.println("Message Get :" + remoteMessage.getMessageType());
        String notification = remoteMessage.getData().toString();
        // String notification1 = remoteMessage.getSentTime();
        String notification2 = remoteMessage.getMessageId().toString();
        notification4 = remoteMessage.getData().get("message");

        System.out.println("Message Back notification :" + remoteMessage.getNotification());


        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        Notificationcount=userpreferences.getInt("count",0);



        Notificationcount=Notificationcount+1;

        SharedPreferences.Editor editor = userpreferences.edit();
        editor.putInt("count", Notificationcount);
        editor.commit();

        ShortcutBadger.with(getApplicationContext()).count(Notificationcount);
       /* notification4 = notification4.replaceAll("\\\\\\\\\\\"", "");
        notification4 = notification4.replaceAll("\\\\", "");
        notification4 = notification4.replaceAll("u0026", "&");*/
        JSONObject obj = null;
        String MsgType = "";
        String Msgcontent = "";
        try {
            obj = new JSONObject(notification4);
            MsgType = obj.getString("MsgType");
            Msgcontent = obj.getString("Msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgUserAdded)) {
            JSONObject mainObj = null;
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(Msgcontent);

               // sql.delete(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null, null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null);
                int count = c.getCount();
                if (jArray.length() > 0) {
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject Jsonchatmember = jArray.getJSONObject(i);
                        chatUser = new ChatUser();
                        Chatroomname = Jsonchatmember.getString("ChatRoomName");
                        chatUser.setChatroom(Chatroomname);
                        ChatRoomId = Jsonchatmember.getString("ChatRoomId");
                        chatUser.setChatRoomId(ChatRoomId);
                        ChatRoomStatus=Jsonchatmember.getString("ChatRoomStatus");
                        chatUser.setStatus(ChatRoomStatus);
                        chatUser.setStartTime(Jsonchatmember.getString("StartTime"));
                        chatUser.setCreater(Jsonchatmember.getString("Creator"));
                        CallId = Jsonchatmember.getString("ChatSourceId");
                        chatUser.setChatSourceId(Jsonchatmember.getString("ChatSourceId"));
                        String AddedBy=Jsonchatmember.getString("AddedBy");
                        AddedBy=AddedBy.trim();
                        chatUser.setAddedBy(AddedBy);
                        chatUser.setParticipantName(Jsonchatmember.getString("ParticipantName"));
                        chatUser.setParticipantId(Jsonchatmember.getString("ParticipantId"));
                        Add_ChatMessage = Jsonchatmember.getString("Message");
                        chatUser.setMessage(ChatMessage);
                        chatUser.setUserMasterId(UserMasterId);
                        chatUser.setCount("0");
                        if (cf.CheckifRecordPresent(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, "ParticipantId","ParticipantName", chatUser.getParticipantId(),chatUser.getParticipantName())) {

                            cf.AddGroupMember(chatUser);
                        }
                        chatUserArrayList.add(chatUser);

                      //  chatGroupArrayList.clear();
                        ChatGroup chatGroup=new ChatGroup();
                        Chatroomname = Jsonchatmember.getString("ChatRoomName");
                        chatGroup.setChatroom(Chatroomname);
                        ChatRoomId = Jsonchatmember.getString("ChatRoomId");
                        chatGroup.setChatRoomId(ChatRoomId);
                        ChatRoomStatus=Jsonchatmember.getString("ChatRoomStatus");
                        chatGroup.setStatus(ChatRoomStatus);
                        chatGroup.setStartTime(Jsonchatmember.getString("StartTime"));
                        chatGroup.setCreater(Jsonchatmember.getString("Creator"));
                        AddedBy=Jsonchatmember.getString("AddedBy");
                        AddedBy=AddedBy.trim();
                        chatGroup.setAddedBy(AddedBy);

                        chatGroup.setUserMasterId(UserMasterId);
                        chatGroup.setCount("0");
                        if (cf.CheckChatroomRecordPresent(db.TABLE_CHAT_CHATROOM_GROUP_LIST,"ChatRoomId",ChatRoomId)) {
                            cf.AddGroupList(chatGroup);
                        }

                    }

                    SendChattingNotification();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgChat)) {

            try {

                JSONArray jArray = new JSONArray(Msgcontent);
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
                            MessageDecode = StringEscapeUtils.unescapeJava(Group_Message);
                            chatMessage.setMessage(MessageDecode);
                            MessageDate = Jsongroupmessage.getString("MessageDate");
                            MessageDate = MessageDate.substring(MessageDate.indexOf("(") + 1, MessageDate.lastIndexOf(")"));
                            long timestamp = Long.parseLong(MessageDate);
                            chatMessage.setMessageDate(timestamp);
                            MessageId = Jsongroupmessage.getString("MessageId");
                            chatMessage.setMessageId(MessageId);
                            UserName = Jsongroupmessage.getString("UserName");
                            chatMessage.setUsername(UserName);
                            Groupstatus=Jsongroupmessage.getString("ChatRoomStatus");
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
                            ContentValues values=new ContentValues();
                            DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy hh:mmaa");
                            Date date = new Date(timestamp);
                            MessageDate = targetFormat.format(date);
                            values.put("StartTime", MessageDate);
                            sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});


                        }

                        // Intent intent = new Intent("chat.AddChatRoomActivity");
                        Intent intent = new Intent();
                        intent.setAction("chatscrren");
                        intent.putExtra("message", "1");
                        intent.putExtra("ChatRoomid", ChatRoomId);
                        intent.putExtra("Chatroomname", ChatRoomName);
                        intent.putExtra("status",Groupstatus);
                        context.sendBroadcast(intent);


                                if (ChatRoomName.equals(AddChatRoomActivity.ChatRoomName)) {
                                    //SendGroupChattingNotification();
                                } else {

                                    String query = "SELECT * FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST +" WHERE  ChatRoomId ='" + ChatRoomId + "'";;
                                    Cursor cur = sql.rawQuery(query, null);
                                    if (cur.getCount() > 0) {
                                        cur.moveToFirst();
                                        do {
                                            ChatGroup chatGroup = new ChatGroup();
                                            ChatRoomId=cur.getString(cur.getColumnIndex("ChatRoomId"));
                                            chatGroup.setChatRoomId(ChatRoomId);
                                            String AddedBy=cur.getString(cur.getColumnIndex("AddedBy"));
                                            chatGroup.setAddedBy(AddedBy);
                                            String StartTime =cur.getString(cur.getColumnIndex("StartTime"));
                                            //StartTime=StartTime.substring(0, 10);
                                            chatGroup.setStartTime(StartTime);
                                            String ChatRoomName=cur.getString(cur.getColumnIndex("ChatRoomName"));
                                            chatGroup.setChatroom(ChatRoomName);
                                            chatGroup.setStatus(cur.getString(cur.getColumnIndex("ChatRoomStatus")));
                                            chatGroup.setCreater(cur.getString(cur.getColumnIndex("Creator")));
                                            String UserMasterId=cur.getString(cur.getColumnIndex("UserMasterId"));
                                            chatGroup.setUserMasterId(UserMasterId);
                                            ChatMessageCount=cur.getInt(cur.getColumnIndex("Count"));
                                            ChatMessageCount=ChatMessageCount+1;
                                            chatGroup.setCount(String.valueOf(ChatMessageCount));
                                            cf.UpdateGroupList(chatGroup);
                                            //chatGroupArrayList.add(chatGroup)  ;

                                        } while (cur.moveToNext());

                               /* editor = userpreferences.edit();
                                editor.putInt("count1", ChatMessageCount);
                                editor.commit();
                                ContentValues values = new ContentValues();
                                values.put("Count", ChatMessageCount);
                                sql.update(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});

*/
                                    }else {

                                    }

                                    SendGroupChattingNotification();
                                }








                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();

            }
        } else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgActivityAssign)) {
            String MSG = "An Activity has assigned to you";
            try {
                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(Msgcontent);
                String msg = "";
                  Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    bean=new ActivityBean();
                    bean.setIsChargable(jorder.getString("IsChargable"));
                    String ActivityName = jorder.getString("ActivityName");
                    bean.setActivityName(ActivityName);
                    String AssignedById=jorder.getString("AssignedById");
                    bean.setAssignedById(AssignedById);
                    String nameby=jorder.getString("Assigned_By");
                    bean.setAssigned_By(nameby);
                    String IssuedToName=jorder.getString("IssuedToName");
                    bean.setIssuedToName(IssuedToName);
                    String ActivityId=jorder.getString("ActivityId");
                    bean.setActivityId(ActivityId);
                    bean.setStartDate(jorder.getString("StartDate"));
                    String EndDate = jorder.getString("EndDate");
                    bean.setEndDate(EndDate);
                    bean.setExpectedCompleteDate(jorder.getString("ExpectedCompleteDate"));
                    bean.setExpectedComplete_Date(jorder.getString("ExpectedComplete_Date"));
                    bean.setModifiedBy(jorder.getString("ModifiedBy"));
                    bean.setModified_By(jorder.getString("Modified_By"));
                    bean.setFormatStDt(jorder.getString("FormatStDt"));
                  //  bean.setStartDt(jorder.getString("StartDt"));
                    //bean.setEndDt(jorder.getString("EndDt"));
                    bean.setFormatEndDt(jorder.getString("FormatEndDt"));
                    String Status=jorder.getString("Status");
                    bean.setStatus(Status);
                    String ProjectId=jorder.getString("ProjectId");
                    bean.setProjectID(ProjectId);
                    String PAllowUsrTimeSlotHrs=jorder.getString("PAllowUsrTimeSlotHrs");
                    bean.setPAllowUsrTimeSlotHrs(PAllowUsrTimeSlotHrs);
                    bean.setIsActivityMandatory(jorder.getString("IsActivityMandatory"));
                    bean.setIsDelayedActivityAllowed(jorder.getString("IsDelayedActivityAllowed"));
                    bean.setCd(jorder.getString("Cd"));
                    bean.setUnitId(jorder.getString("UnitId"));
                    bean.setPKModuleMastId(jorder.getString("PKModuleMastId"));
                    bean.setPriorityName(jorder.getString("PriorityName"));
                    bean.setColour(jorder.getString("Colour"));
                    bean.setPriorityIndex(jorder.getString("PriorityIndex"));
                    bean.setTotalHoursBooked(jorder.getString("TotalHoursBooked"));
                    String AddedDt = jorder.getString("AddedDt");
                    bean.setAddedDt(AddedDt);
                    bean.setUserMasterId(jorder.getString("UserMasterId"));
                    bean.setModifiedDt(jorder.getString("ModifiedDt"));
                    bean.setAssignedById1(jorder.getString("AssignedById1"));
                    bean.setIsDeleted(jorder.getString("IsDeleted"));
                    bean.setIsApproved(jorder.getString("IsApproved"));
                    bean.setIsChargable1(jorder.getString("IsChargable1"));
                    bean.setActivityTypeId(jorder.getString("ActivityTypeId"));
                    bean.setIsApproval(jorder.getString("IsApproval"));
                    bean.setHoursRequired(jorder.getString("HoursRequired"));
                    bean.setAttachmentName(jorder.getString("AttachmentName"));
                    bean.setAttachmentContent(jorder.getString("AttachmentContent"));
                    bean.setModifiedDt1(jorder.getString("ModifiedDt1"));
                    String SourceType=jorder.getString("SourceType");
                    bean.setSourceType(SourceType);
                    String SourceId=jorder.getString("SourceId");
                    bean.setSourceId(SourceId);
                    bean.setUnitName(jorder.getString("UnitName"));
                    bean.setUnitDesc(jorder.getString("UnitDesc"));
                    bean.setModuleName(jorder.getString("ModuleName"));
                    bean.setActivityName1(jorder.getString("ActivityName1"));
                    bean.setRemarks(jorder.getString("Remarks"));
                    bean.setProjectCode(jorder.getString("ProjectCode"));
                    String ProjectName=jorder.getString("ProjectName");
                    bean.setProjectName(ProjectName);
                    bean.setUserName(jorder.getString("UserName"));
                    bean.setExpectedComplete_Date1(jorder.getString("ExpectedComplete_Date1"));
                    bean.setDeptDesc(jorder.getString("DeptDesc"));
                    bean.setDeptMasterId(jorder.getString("DeptMasterId"));
                    bean.setCompletionIntimate(jorder.getString("CompletionIntimate"));
                    bean.setActivityCode(jorder.getString("ActivityCode"));
                    bean.setModifiedBy1(jorder.getString("ModifiedBy1"));
                    bean.setReassignedBy(jorder.getString("ReassignedBy"));
                    bean.setReassignedDt(jorder.getString("ReassignedDt"));
                    bean.setActualCompletionDate(jorder.getString("ActualCompletionDate"));
                    bean.setWarrantyCode(jorder.getString("WarrantyCode"));
                    bean.setTicketCategory(jorder.getString("TicketCategory"));
                    bean.setIsEndTime(jorder.getString("IsEndTime"));
                    bean.setIsCompActPresent(jorder.getString("IsCompActPresent"));
                    bean.setCompletionActId(jorder.getString("CompletionActId"));
                    bean.setTktCustReportedBy(jorder.getString("TktCustReportedBy"));
                    bean.setTktCustApprovedBy(jorder.getString("TktCustApprovedBy"));
                    bean.setIsSubActivity(jorder.getString("IsSubActivity"));
                    bean.setParentActId(jorder.getString("ParentActId"));
                    bean.setConsigneeName(jorder.getString("ConsigneeName"));
                    bean.setContMob(jorder.getString("ContMob"));
                    bean.setActivityTypeName(jorder.getString("ActivityTypeName"));
                    bean.setCompActName(jorder.getString("CompActName"));
                    AddedDt = getDateAdded(AddedDt);
                    EndDate = getDateEnd(EndDate);

                    activityBeen.add(bean);

                        MSG = "Activity " + ActivityName + " has been assign by " + nameby + " on " + AddedDt + ", having end date " + EndDate + ".";
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            if (columnName.equalsIgnoreCase("ActivityName")) {
                                columnValue = jorder.getString(columnName);
                                columnValue = URLDecoder.decode(columnValue, "UTF-8");
                            }
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_ACTIVITYMASTER_PAGING, null, values);
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (ActivityMain.isInFront==true){
                Intent intent=new Intent();
                intent.setAction("assignscreen");
                context.sendBroadcast(intent);
                sendActivityNotification(MSG);
            }
            else {
                sendActivityNotification(MSG);
            }


        }else if(MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgActivityCompleted)
                ||MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgActivityCancel)){

            try {
                String Activityid=obj.getString("ActivityId");
                SQLiteDatabase sql1 = db.getWritableDatabase();
                sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{Activityid});
                sql1.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{Activityid});
                sql1.close();

            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (ActivityMain.isInFront==true){
                Intent intent=new Intent();
                intent.setAction("assignscreen");
                context.sendBroadcast(intent);
                sendNotification(Msgcontent);
            }
            else {
                sendNotification(Msgcontent);
            }



        } else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_Payment_Transfer)) {
            try {
                JSONArray jArray = new JSONArray(Msgcontent);
                if (jArray != null) {
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE, null);
                    int count = c.getCount();
                    if (jArray.length() > 0) {
                        for (int j = 0; j < jArray.length(); j++) {
                            ClaimNotification claimNotification=new ClaimNotification();
                            JSONObject Jsongroupmessage = jArray.getJSONObject(j);
                            Data = Jsongroupmessage.getString("Activity");
                            claimNotification.setMessage(Data);
                            cf.AddClaimPayment(claimNotification);
                            claimNotificationArrayList.add(claimNotification);

                        }
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();

            }
            sendClaimNotification(Data);
        }
    }



    private String GetDateAdded(String inDate) {
        inDate = inDate.replace("T", " ");
        inDate = inDate.substring(0, inDate.indexOf("."));
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM,yyyy hh:mm:ss aa");
        Date date = null;
        String retDate = "";
        try {
            date = input.parse(inDate);
        } catch (ParseException e) {
            e.printStackTrace();
            retDate = "";
        }
        retDate = output.format(date);
        //2017-09-20T12:24:23.2215225 05:30

        return retDate;
    }

    private String GetDateEndDate(String inDate) {
        inDate = inDate.replace("T", " ");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM,yyyy");
        Date date = null;
        String retDate = "";
        try {
            date = input.parse(inDate);
        } catch (ParseException e) {
            e.printStackTrace();
            retDate = "";
        }
        retDate = output.format(date);
        //2017-09-20T00:00:00
        return retDate;
    }


    @SuppressLint("NewApi")
    private void sendActivityNotification(String messageBody) {

        Intent intent = new Intent(context, ActivityDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("actbean", bean);
        intent.putExtra("checkassign","");
        intent.putExtra("unapprove","");


        intent.putExtras(bundle);



        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel= new NotificationChannel(channel_id,
                    "Activity",
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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channel_id)
                .setSmallIcon(R.drawable.vlogonew)
                .setContentTitle("vWb Notification")
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vlogonew))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open Activity List");

       /* NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        notifications.add(messageBody);
            for (int i = 0; i < notifications.size(); i++) {
                inboxStyle.addLine(notifications.get(i));
            }
        if (notifications.size()>1) {

            // Moves the expanded layout object into the notification object.
            notificationBuilder.setStyle(inboxStyle);
        }
*/

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(0, notificationBuilder.build());
    }


    @SuppressLint("NewApi")
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, ActivityMain.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel= new NotificationChannel(channel_id1,
                    "ActivityData",
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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channel_id1)
                .setSmallIcon(R.drawable.vlogonew)
                .setContentTitle("vWb Notification")
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vlogonew))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open Activity List");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private String getDateAdded(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy");
        String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
        long Etime = Long.parseLong(EndDresults);
        Date EndDate = new Date(Etime);
        String endDate = sdf.format(EndDate);
        return endDate;
    }

    private String getDateEnd(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy");
        String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
        long Etime = Long.parseLong(EndDresults);
        Date EndDate = new Date(Etime);
        String endDate = sdf.format(EndDate);
        return endDate;
    }


    @SuppressLint("NewApi")
    private void SendChattingNotification() {
        Intent intent = new Intent(this, OpenChatroomActivity.class);
        intent.putExtra("callid", CallId);
        intent.putExtra("ChatRoomid", ChatRoomId);
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.putString("UserMasterId", UserMasterId);
        editor.commit();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent piResult = PendingIntent.getActivity(this, 0, intent, 0);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                channel= new NotificationChannel(channel_id2,
                        "Chat Rooom",
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
                this,channel_id2);
        Notification notification = mBuilder
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.vwb_logo) //ok
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vwb_logo))
                .setContentTitle(Chatroomname)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Add_ChatMessage))
                .setContentIntent(piResult)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(Add_ChatMessage)
                .setGroupSummary(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentText(Add_ChatMessage).build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notification);

    }

    @SuppressLint("NewApi")
    private void SendGroupChattingNotification() {
        Intent intent = new Intent(this, AddChatRoomActivity.class);
        intent.putExtra("ChatRoomid", ChatRoomId);
        intent.putExtra("Chatroomname", ChatRoomName);
        intent.putExtra("status",Groupstatus);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent piResult = PendingIntent.getActivity(this, 0, intent, 0);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel= new NotificationChannel(channel_id3,
                    "Group Message",
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
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,channel_id3);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.vwb_logo) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vwb_logo))
                    .setContentTitle(UserName)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(MessageDecode))
                    .setContentIntent(piResult)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setGroup(MessageDecode)
                    .setGroupSummary(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentText(MessageDecode).build();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(channel);
            }
           notificationManager.notify(0, notification);

        }else if(Attachment.contains(".pdf")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,channel_id3);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.vwb_logo) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vwb_logo))
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

        }else if(Attachment.contains(".mp3")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,channel_id3);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.vwb_logo) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vwb_logo))
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

        }else if(Attachment.contains(".mp4")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,channel_id3);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.vwb_logo) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vwb_logo))
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

        }
        else if(Attachment.contains(".jpg")||Attachment.contains(".png")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,channel_id3);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.vwb_logo) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vwb_logo))
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

        }

    }

    @SuppressLint("NewApi")
    private void sendClaimNotification(String messageBody) {
        Intent intent = new Intent(this, ClaimNotificationActivity.class);
       // ShortcutBadger.with(getApplicationContext()).remove();

        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel= new NotificationChannel(channel_id4,
                    "Claim",
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
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channel_id4)
                .setSmallIcon(R.drawable.vlogonew)
                .setContentTitle("vWb Payment Notification")
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vlogonew))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open Activity List");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }


    private String getDate(String data) {
        Date date = null;
        String result = "";

        SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy HH:mm");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            date = format.parse(data);
            result = format1.format(date);

        } catch (Exception ex) {
        }


        return result;
    }

}