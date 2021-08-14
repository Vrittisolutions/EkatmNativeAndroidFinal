package com.vritti.ekatm.services;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.support.v4.app.RemoteInput;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vritti.chat.activity.AddChatRoomActivity;
import com.vritti.chat.activity.OpenChatroomActivity;
import com.vritti.chat.bean.ChatGroup;
import com.vritti.chat.bean.ChatMessage;
import com.vritti.chat.bean.ChatUser;
import com.vritti.crm.vcrm7.CRMHomeActivity;
import com.vritti.crm.vcrm7.CallListActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.ekatm.receiver.NotificationReceiver;
import com.vritti.vwb.Beans.ActivityBean;
import com.vritti.vwb.Beans.ClaimNotification;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.ActivityDetailsActivity;
import com.vritti.vwb.vworkbench.ActivityMain;
import com.vritti.vwb.vworkbench.ActvityNotificationTypeActivity;
import com.vritti.vwb.vworkbench.ClaimNotificationActivity;
import com.vritti.vwb.vworkbench.MyTeamActivity;


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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.os.Handler;
import android.widget.Toast;

import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * Created by pradnya on 11-Apr-17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String FinalObj;
    public static SharedPreferences userpreferences;
    String notification4;
    String Titile, NotificationMessage;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SQLiteDatabase sql;
    String MessageDecode = "", Group_Message, CallId, ChatRoomId, Chatmessage, Add_ChatMessage, Chatroomname, ChatGroupMessage, MessageDate, MessageType, UserName, Status, MessageId, ChatRoomName, Attachment;
    ArrayList<ChatUser> chatUserArrayList;
    ArrayList<ChatMessage> chatMessageArrayList;
    ArrayList<ChatGroup> chatGroupArrayList;
    ArrayList<ClaimNotification> claimNotificationArrayList;
    Handler mHandler;
    Context context;
    String Outcome, Firmname, AssignedTo, date, AppointmentDate, Remarks, Address, Mobile, Data;

    NotificationChannel channel;
    String channel_id = "one";
    String channel_id1 = "two";
    String channel_id2 = "three";
    String channel_id3 = "four";
    String channel_id4 = "five";
    String channel_id5 = "six";
    ActivityBean bean = new ActivityBean();
    ArrayList<ActivityBean> activityBeen = new ArrayList<>();
    int Notificationcount, ChatMessageCount = 0;

    static ArrayList<String> notifications = new ArrayList<>();
    private String ChatRoomStatus, Groupstatus, ChatType;
    private ChatUser chatUser;
    private RemoteMessage remoteMessage;
    private String message;

    public static final int REQUEST_CODE_MORE = 100;
    public static final int REQUEST_CODE_HELP = 101;
    public static final String NOTIFICATION_REPLY = "NotificationReply";
    public static final String CHANNNEL_ID = "SimplifiedCodingChannel";
    public static final String KEY_INTENT_MORE = "keyintentmore";
    public static final String KEY_INTENT_HELP = "keyintenthelp";
    private JSONObject obj;
    boolean flag;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        ut = new Utility();
        context = this;
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        chatUserArrayList = new ArrayList<ChatUser>();
        claimNotificationArrayList = new ArrayList<>();
        chatGroupArrayList = new ArrayList<>();

        activityBeen = new ArrayList<ActivityBean>();
        chatMessageArrayList = new ArrayList<ChatMessage>();
        // Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());
        flag = false;
        ChatRoomId = "";
        ChatRoomName = "";
        CallId = "";


        String notification = remoteMessage.getData().toString();
        // String notification1 = remoteMessage.getSentTime();
        String notification2 = remoteMessage.getMessageId().toString();
        notification4 = remoteMessage.getData().get("message");

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn == false) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");

            wl_cpu.acquire(10000);
        } else {

        }

        getnotification(notification4);

    }

    private void getnotification(String notification4) {

        if (!flag) {

            userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                    Context.MODE_PRIVATE);
            Notificationcount = userpreferences.getInt(WebUrlClass.USERINFO_SHORTCUTADGER_COUNT, 0);
            Notificationcount = Notificationcount + 1;

            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putInt(WebUrlClass.USERINFO_SHORTCUTADGER_COUNT, Notificationcount);
            editor.commit();

            ShortcutBadger.with(getApplicationContext()).count(Notificationcount);

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
                            ChatRoomStatus = Jsonchatmember.getString("ChatRoomStatus");
                            chatUser.setStatus(ChatRoomStatus);
                            String Starttime = Jsonchatmember.getString("StartTime");
                            chatUser.setStartTime(Starttime);
                            chatUser.setCreater(Jsonchatmember.getString("Creator"));
                            CallId = Jsonchatmember.getString("ChatSourceId");
                            chatUser.setChatSourceId(Jsonchatmember.getString("ChatSourceId"));
                            String AddedBy = Jsonchatmember.getString("AddedBy");
                            AddedBy = AddedBy.trim();
                            chatUser.setAddedBy(AddedBy);
                            chatUser.setParticipantName(Jsonchatmember.getString("ParticipantName"));
                            chatUser.setParticipantId(Jsonchatmember.getString("ParticipantId"));
                            Add_ChatMessage = Jsonchatmember.getString("Message");
                            chatUser.setMessage(Chatmessage);
                            chatUser.setUserMasterId(UserMasterId);
                            chatUser.setCount("0");
                            ChatType = Jsonchatmember.getString("ChatType");
                            chatUser.setChatType(ChatType);
                            chatUser.setImagePath(CompanyURL + Jsonchatmember.getString("ImagePath"));

                            if (cf.CheckifRecordPresent(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, "ParticipantId", "ChatRoomId", chatUser.getParticipantId(), chatUser.getChatRoomId())) {

                                cf.AddGroupMember(chatUser);
                                chatUserArrayList.add(chatUser);

                            }

                            //  chatGroupArrayList.clear();
                            ChatGroup chatGroup = new ChatGroup();
                            Chatroomname = Jsonchatmember.getString("ChatRoomName");
                            chatGroup.setChatroom(Chatroomname);
                            ChatRoomId = Jsonchatmember.getString("ChatRoomId");
                            chatGroup.setChatRoomId(ChatRoomId);
                            ChatRoomStatus = Jsonchatmember.getString("ChatRoomStatus");
                            chatGroup.setStatus(ChatRoomStatus);
                            Starttime = Jsonchatmember.getString("StartTime");
                            chatGroup.setStartTime(Starttime);
                            chatGroup.setCreater(Jsonchatmember.getString("Creator"));
                            AddedBy = Jsonchatmember.getString("AddedBy");
                            AddedBy = AddedBy.trim();
                            chatGroup.setAddedBy(AddedBy);
                            chatGroup.setChatSourceId(CallId);
                            chatGroup.setUserMasterId(UserMasterId);
                            chatGroup.setCount("0");
                            chatGroup.setChatType(ChatType);
                            chatGroup.setChatMessage("");

                            if (cf.CheckChatroomRecordPresent(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatRoomId", ChatRoomId)) {
                                cf.AddGroupList(chatGroup);
                            }

                        }
                        if (!flag) {
                            SendChattingNotification();
                            flag = true;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgChat)) {
                int notiCount = AppCommon.getInstance(context).getNotificationCount();
                AppCommon.getInstance(context).setNotificationCount(notiCount + 1);
                try {

                    JSONArray jArray = new JSONArray(Msgcontent);

                    String MessageChatRoomId = "";
                    for (int j = 0; j < jArray.length(); j++) {
                        JSONObject Jsongroupmessage = jArray.getJSONObject(j);
                        MessageChatRoomId = Jsongroupmessage.getString("ChatRoomId");
                    }

                    if (jArray != null) {
                        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE, null);
                        // String query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE + " WHERE  ChatRoomId ='" + chatRoomId + "'";
                        int count = c.getCount();
                        if (jArray.length() > 0) {
                            for (int j = 0; j < jArray.length(); j++) {
                                JSONObject Jsongroupmessage = jArray.getJSONObject(j);
                                ChatMessage chatMessage = new ChatMessage();
                                UserMasterId = Jsongroupmessage.getString("UserMasterId");
                                chatMessage.setUserMasterId(UserMasterId);
                                MessageChatRoomId = Jsongroupmessage.getString("ChatRoomId");
                                chatMessage.setChatRoomId(MessageChatRoomId);
                                Group_Message = Jsongroupmessage.getString("Message");
                              /*  if(Group_Message.equals("")){
                                    Group_Message = "#File";
                                }*/

                                MessageDate = Jsongroupmessage.getString("MessageDate");
                                String MessageTime = Jsongroupmessage.getString("MessageDate");
                                MessageDate = MessageDate.substring(MessageDate.indexOf("(") + 1, MessageDate.lastIndexOf(")"));
                                long timestamp = Long.parseLong(MessageDate);
                                chatMessage.setMessageDate(timestamp);
                                MessageId = Jsongroupmessage.getString("MessageId");
                                chatMessage.setMessageId(MessageId);
                                UserName = Jsongroupmessage.getString("UserName");
                                chatMessage.setUsername(UserName);
                                Groupstatus = Jsongroupmessage.getString("ChatRoomStatus");
                                Status = Jsongroupmessage.getString("Status");
                                chatMessage.setStatus(Status);
                                MessageType = Jsongroupmessage.getString("MessageType");
                                chatMessage.setMessageType(MessageType);
                                ChatRoomName = Jsongroupmessage.getString("ChatRoomName");
                                Attachment = Jsongroupmessage.getString("Attachment");
                                if (!MessageChatRoomId.equals(AddChatRoomActivity.ChatRoomId))
                                    chatMessage.setIsDownloaded("No // isRead");
                                else
                                    chatMessage.setIsDownloaded("No");

                                if (Attachment.equals("") || Attachment.equals("null")) {
                                    chatMessage.setAttachment(null);
                                } else {
                                    chatMessage.setAttachment(CompanyURL + Attachment);
                                    if (!Group_Message.equals("")) {
                                        Group_Message = "#File" + Group_Message;
                                    } else {
                                        Group_Message = "#File";
                                    }
                                }
                                MessageDecode = StringEscapeUtils.unescapeJava(Group_Message);
                                chatMessage.setMessage(MessageDecode);
                                cf.AddGroupMessage(chatMessage);
                                chatMessageArrayList.add(chatMessage);
                                ContentValues values = new ContentValues();
                                values.put("StartTime", MessageTime);
                                values.put("ChatMessage", MessageDecode);
                                sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{MessageChatRoomId});


                            }
                            if (MessageChatRoomId.equals(AddChatRoomActivity.ChatRoomId)) {

                                Intent intent = new Intent();
                                intent.setAction("chatscrren");
                                intent.putExtra("message", "1");
                                intent.putExtra("ChatRoomid", MessageChatRoomId);
                                intent.putExtra("Chatroomname", ChatRoomName);
                                intent.putExtra("status", Groupstatus);
                                context.sendBroadcast(intent);

                                String query = "SELECT * FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST + " WHERE  ChatRoomId ='" + MessageChatRoomId + "'";

                                Cursor cur = sql.rawQuery(query, null);
                                if (cur.getCount() > 0) {
                                    cur.moveToFirst();
                                    do {
                                        ChatGroup chatGroup = new ChatGroup();
                                        ChatRoomId = cur.getString(cur.getColumnIndex("ChatRoomId"));
                                        chatGroup.setChatRoomId(ChatRoomId);
                                        String AddedBy = cur.getString(cur.getColumnIndex("AddedBy"));
                                        chatGroup.setAddedBy(AddedBy);
                                        String StartTime = cur.getString(cur.getColumnIndex("StartTime"));
                                        //StartTime=StartTime.substring(0, 10);
                                        chatGroup.setStartTime(StartTime);
                                        ChatRoomName = cur.getString(cur.getColumnIndex("ChatRoomName"));
                                        chatGroup.setChatroom(ChatRoomName);
                                        chatGroup.setStatus(cur.getString(cur.getColumnIndex("ChatRoomStatus")));
                                        chatGroup.setCreater(cur.getString(cur.getColumnIndex("Creator")));
                                        String UserMasterId = cur.getString(cur.getColumnIndex("UserMasterId"));
                                        chatGroup.setUserMasterId(UserMasterId);
                                        ChatMessageCount = cur.getInt(cur.getColumnIndex("Count"));
                                        ChatMessageCount = ChatMessageCount + 1;
                                        chatGroup.setCount(String.valueOf(ChatMessageCount));
                                        String ChatSourceId = cur.getString(cur.getColumnIndex("ChatSourceId"));
                                        chatGroup.setChatSourceId(ChatSourceId);
                                        chatGroup.setChatType(cur.getString(cur.getColumnIndex("ChatType")));
                                        chatGroup.setChatMessage(MessageDecode);
                                        cf.UpdateGroupList(chatGroup);
                                        //chatGroupArrayList.add(chatGroup)  ;

                                    } while (cur.moveToNext());

                                    editor.putInt("count1", ChatMessageCount);
                                    editor.commit();
                                    ContentValues values = new ContentValues();
                                    values.put("Count", ChatMessageCount);
                                    sql.update(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});


                                } else {

                                }
                                if (!flag) {
                                    SendGroupChattingNotification(MessageChatRoomId, ChatRoomName);
                                    flag = true;
                                }
                            } else {
                                String query = "SELECT * FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST + " WHERE  ChatRoomId ='" + MessageChatRoomId + "'";
                                ;
                                Cursor cur = sql.rawQuery(query, null);
                                if (cur.getCount() > 0) {
                                    cur.moveToFirst();
                                    do {
                                        ChatGroup chatGroup = new ChatGroup();
                                        ChatRoomId = cur.getString(cur.getColumnIndex("ChatRoomId"));
                                        chatGroup.setChatRoomId(ChatRoomId);
                                        String AddedBy = cur.getString(cur.getColumnIndex("AddedBy"));
                                        chatGroup.setAddedBy(AddedBy);
                                        String StartTime = cur.getString(cur.getColumnIndex("StartTime"));
                                        //StartTime=StartTime.substring(0, 10);
                                        chatGroup.setStartTime(StartTime);
                                        ChatRoomName = cur.getString(cur.getColumnIndex("ChatRoomName"));
                                        chatGroup.setChatroom(ChatRoomName);
                                        chatGroup.setStatus(cur.getString(cur.getColumnIndex("ChatRoomStatus")));
                                        chatGroup.setCreater(cur.getString(cur.getColumnIndex("Creator")));
                                        String UserMasterId = cur.getString(cur.getColumnIndex("UserMasterId"));
                                        chatGroup.setUserMasterId(UserMasterId);
                                        ChatMessageCount = cur.getInt(cur.getColumnIndex("Count"));
                                        ChatMessageCount = ChatMessageCount + 1;
                                        chatGroup.setCount(String.valueOf(ChatMessageCount));
                                        String ChatSourceId = cur.getString(cur.getColumnIndex("ChatSourceId"));
                                        chatGroup.setChatSourceId(ChatSourceId);
                                        String ChatType = cur.getString(cur.getColumnIndex("ChatType"));
                                        chatGroup.setChatType(ChatType);
                                        if (ChatType.equals("P")) {
                                            ChatRoomName = UserName;
                                        }
                                        chatGroup.setChatMessage(MessageDecode);
                                        cf.UpdateGroupList(chatGroup);
                                        //chatGroupArrayList.add(chatGroup)  ;

                                    } while (cur.moveToNext());

                                    editor.putInt("count1", ChatMessageCount);
                                    editor.commit();
                                    ContentValues values = new ContentValues();
                                    values.put("Count", ChatMessageCount);
                                    sql.update(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});


                                } else {

                                }
                                if (!flag) {
                                    SendGroupChattingNotification(MessageChatRoomId, ChatRoomName);
                                    flag = true;
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgActivityAssign)) {
                String MSG = "An Activity has assigned to you";
                try {
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(Msgcontent);
                    String msg = "";
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        bean = new ActivityBean();
                        bean.setIsChargable(jorder.getString("IsChargable"));
                        String ActivityName = jorder.getString("ActivityName");
                        bean.setActivityName(ActivityName);
                        String AssignedById = jorder.getString("AssignedById");
                        bean.setAssignedById(AssignedById);
                        String nameby = jorder.getString("Assigned_By");
                        bean.setAssigned_By(nameby);
                        String ActivityId = jorder.getString("ActivityId");
                        bean.setActivityId(ActivityId);
                        bean.setStartDate(jorder.getString("StartDate"));
                        String EndDate = jorder.getString("EndDate");
                        bean.setEndDate(EndDate);
                        bean.setExpectedCompleteDate(jorder.getString("ExpectedCompleteDate"));
                        bean.setExpectedComplete_Date(jorder.getString("ExpectedComplete_Date"));
                        bean.setModifiedBy(jorder.getString("ModifiedBy"));
                        bean.setModified_By(jorder.getString("Modified_By"));
                        bean.setFormatStDt(jorder.getString("FormatStDt"));
                        //bean.setStartDt(jorder.getString("StartDt"));
                        //bean.setEndDt(jorder.getString("EndDt"));
                        bean.setFormatEndDt(jorder.getString("FormatEndDt"));
                        String Status = jorder.getString("Status");
                        bean.setStatus(Status);
                        String ProjectId = jorder.getString("ProjectId");
                        bean.setProjectID(ProjectId);
                        String PAllowUsrTimeSlotHrs = jorder.getString("PAllowUsrTimeSlotHrs");
                        bean.setPAllowUsrTimeSlotHrs(PAllowUsrTimeSlotHrs);
                        //bean.setIsActivityMandatory(jorder.getString("IsActivityMandatory"));
                        //bean.setIsDelayedActivityAllowed(jorder.getString("IsDelayedActivityAllowed"));
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
                        bean.setIsDeleted(jorder.getString("IsDeleted"));
                        bean.setIsApproved(jorder.getString("IsApproved"));
                      //  bean.setActivityTypeId(jorder.getString("ActivityTypeId"));
                        bean.setIsApproval(jorder.getString("IsApproval"));
                        bean.setHoursRequired(jorder.getString("HoursRequired"));
                        bean.setAttachmentName(jorder.getString("AttachmentName"));
                        String SourceType = jorder.getString("SourceType");
                        bean.setSourceType(SourceType);
                        String SourceId = jorder.getString("SourceId");
                        bean.setSourceId(SourceId);
                        bean.setUnitName(jorder.getString("UnitName"));
                        bean.setUnitDesc(jorder.getString("UnitDesc"));
                        bean.setModuleName(jorder.getString("ModuleName"));
                        bean.setRemarks(jorder.getString("Remarks"));
                        bean.setProjectCode(jorder.getString("ProjectCode"));
                        String ProjectName = jorder.getString("ProjectName");
                        bean.setProjectName(ProjectName);
                        bean.setUserName(jorder.getString("UserName"));
                        bean.setDeptDesc(jorder.getString("DeptDesc"));
                        bean.setDeptMasterId(jorder.getString("DeptMasterId"));
                        bean.setCompletionIntimate(jorder.getString("CompletionIntimate"));
                        bean.setActivityCode(jorder.getString("ActivityCode"));
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
                        String ConsigneeName = jorder.getString("ConsigneeName");
                        bean.setConsigneeName(ConsigneeName);
                        bean.setContMob(jorder.getString("ContMob"));
                        bean.setActivityTypeName(jorder.getString("ActivityTypeName"));
                     //   bean.setCompActName(jorder.getString("CompActName"));
                        AddedDt = getDateAdded(AddedDt);
                        EndDate = getDateEnd(EndDate);

                        activityBeen.add(bean);
                        cf.ActivityPaging(bean);

                        if (SourceType.equalsIgnoreCase("support")) {
                            MSG = "Activity " + ActivityName + " has been assign by " + nameby + " on " + AddedDt + " having end date " + EndDate + " by " + ConsigneeName;

                        } else {
                            MSG = "Activity " + ActivityName + " has been assign by " + nameby + " on " + AddedDt + " having end date " + EndDate + ".";
                        }


             /*           for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            if (columnName.equalsIgnoreCase("ActivityName")) {
                                columnValue = jorder.getString(columnName);
                                columnValue = URLDecoder.decode(columnValue, "UTF-8");
                            }
                            values.put(columnName, columnValue);

                        }
                        long a = sql.insert(db.TABLE_ACTIVITYMASTER, null, values);
            */        }

                    //  Log.e("", "" + a);


                    //int badgeCount=activityBeen.size();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                String Msgtype="Activity Assign Notification";

                if (ActivityMain.isInFront == true) {
                    Intent intent = new Intent();
                    intent.setAction("assignscreen");
                    context.sendBroadcast(intent);
                    sendActivityNotification(MSG,Msgtype);
                } else {


                    if (!flag) {
                        sendActivityNotification(MSG,Msgtype);
                        flag = true;
                    }
                }
            } else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgACTIVITYReassign)) {
                String MSG = "An Activity has reassigned to you";
                try {

                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(Msgcontent);
                    String msg = "";
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        bean = new ActivityBean();
                        bean.setIsChargable(jorder.getString("IsChargable"));
                        String ActivityName = jorder.getString("ActivityName");
                        bean.setActivityName(ActivityName);
                        String AssignedById = jorder.getString("AssignedById");
                        bean.setAssignedById(AssignedById);
                        String nameby = jorder.getString("Assigned_By");
                        bean.setAssigned_By(nameby);
                        String ActivityId = jorder.getString("ActivityId");
                        bean.setActivityId(ActivityId);
                        bean.setStartDate(jorder.getString("StartDate"));
                        String EndDate = jorder.getString("EndDate");
                        bean.setEndDate(EndDate);
                        bean.setExpectedCompleteDate(jorder.getString("ExpectedCompleteDate"));
                        bean.setExpectedComplete_Date(jorder.getString("ExpectedComplete_Date"));
                        bean.setModifiedBy(jorder.getString("ModifiedBy"));
                        bean.setModified_By(jorder.getString("Modified_By"));
                        bean.setFormatStDt(jorder.getString("FormatStDt"));
                       // bean.setStartDt(jorder.getString("StartDt"));
                        //bean.setEndDt(jorder.getString("EndDt"));
                        bean.setFormatEndDt(jorder.getString("FormatEndDt"));
                        String Status = jorder.getString("Status");
                        bean.setStatus(Status);
                        String ProjectId = jorder.getString("ProjectId");
                        bean.setProjectID(ProjectId);
                        String PAllowUsrTimeSlotHrs = jorder.getString("PAllowUsrTimeSlotHrs");
                        bean.setPAllowUsrTimeSlotHrs(PAllowUsrTimeSlotHrs);
                        //bean.setIsActivityMandatory(jorder.getString("IsActivityMandatory"));
                        //bean.setIsDelayedActivityAllowed(jorder.getString("IsDelayedActivityAllowed"));
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
                        bean.setIsDeleted(jorder.getString("IsDeleted"));
                        bean.setIsApproved(jorder.getString("IsApproved"));
                      //  bean.setActivityTypeId(jorder.getString("ActivityTypeId"));
                        bean.setIsApproval(jorder.getString("IsApproval"));
                        bean.setHoursRequired(jorder.getString("HoursRequired"));
                        bean.setAttachmentName(jorder.getString("AttachmentName"));
                        String SourceType = jorder.getString("SourceType");
                        bean.setSourceType(SourceType);
                        String SourceId = jorder.getString("SourceId");
                        bean.setSourceId(SourceId);
                        bean.setUnitName(jorder.getString("UnitName"));
                        bean.setUnitDesc(jorder.getString("UnitDesc"));
                        bean.setModuleName(jorder.getString("ModuleName"));
                        bean.setRemarks(jorder.getString("Remarks"));
                        bean.setProjectCode(jorder.getString("ProjectCode"));
                        String ProjectName = jorder.getString("ProjectName");
                        bean.setProjectName(ProjectName);
                        bean.setUserName(jorder.getString("UserName"));
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
                        bean.setCompActName(jorder.getString("CompActName"));
                        String ConsigneeName = jorder.getString("ConsigneeName");
                        bean.setConsigneeName(ConsigneeName);
                        bean.setContMob(jorder.getString("ContMob"));
                        bean.setActivityTypeName(jorder.getString("ActivityTypeName"));
                      //  bean.setCompActName(jorder.getString("CompActName"));
                        AddedDt = getDateAdded(AddedDt);
                        EndDate = getDateEnd(EndDate);
                        activityBeen.add(bean);
                        cf.ActivityPaging(bean);
                        if (SourceType.equalsIgnoreCase("support")) {
                            MSG = "Activity " + ActivityName + " has been reassign by " + nameby + " on " + AddedDt + " having end date " + EndDate + " by "+ ConsigneeName;

                        }else {
                            MSG = "Activity " + ActivityName + " has been reassign by " + nameby + " on " + AddedDt + " having end date " + EndDate + ".";
                        }
                       /* for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            if (columnName.equalsIgnoreCase("ActivityName")) {
                                columnValue = jorder.getString(columnName);
                                columnValue = URLDecoder.decode(columnValue, "UTF-8");
                            }
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_ACTIVITYMASTER, null, values);*/
                    }

                    //  Log.e("", "" + a);


                    //int badgeCount=activityBeen.size();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                String Msgtype="Activity Reassigned Notification";

                if (ActivityMain.isInFront == true) {
                    Intent intent = new Intent();
                    intent.setAction("assignscreen");
                    context.sendBroadcast(intent);
                    sendActivityNotification(MSG,Msgtype);
                } else {
                    if (!flag) {
                        sendActivityNotification(MSG,Msgtype);
                        flag = true;
                    }
                }


            } else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgActivityCompleted) || MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgActivityCancel)) {

                try {
                    String Activityid = obj.getString("ActivityId");
                    sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{Activityid});
                    sql.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatSourceId=?", new String[]{Activityid});


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String Msgtype="Activity Completed Notification";


                if (ActivityMain.isInFront == true) {
                    Intent intent = new Intent();
                    intent.setAction("assignscreen");
                    context.sendBroadcast(intent);
                    sendNotification(Msgcontent,Msgtype);
                } else {
                    if (!flag) {
                        sendNotification(Msgcontent,Msgtype);
                        flag = true;
                    }
                }


            } else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_Close_Chat)) {
                String AddedBy = "", ChatRoomName = "";

                try {
                    String Activityid = obj.getString("ActivityId");
                    String query = "SELECT * FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST + " WHERE ChatRoomId ='" + Activityid + "'";
                    ;
                    Cursor cur = sql.rawQuery(query, null);
                    String count = String.valueOf(cur.getCount());
                    if (cur.getCount() > 0) {
                        cur.moveToFirst();
                        do {
                            //String ChatRoomId=cur.getString(cur.getColumnIndex("ChatRoomId"));
                            AddedBy = cur.getString(cur.getColumnIndex("AddedBy"));
                            ChatRoomName = cur.getString(cur.getColumnIndex("ChatRoomName"));

                        } while (cur.moveToNext());
                    }

                    ContentValues values = new ContentValues();
                    values.put("ChatRoomStatus", "Closed");
                    sql.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatRoomId=?", new String[]{Activityid});
                    sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{Activityid});


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!flag) {
                    sendChatroomClosedNotification(AddedBy, ChatRoomName);
                    flag = true;
                }


            }

            else if (MsgType.contains(WebUrlClass.ntf_Business_Information)) {
                String Message = "", Typecode = "";

                String[] namesList = MsgType.split("#");
                Message = namesList [1];

                try {
                    JSONArray jResults = new JSONArray(Msgcontent);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        Typecode = jorder.getString("TypeCode");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!flag) {
                    sendBusinessNotification(Message, Typecode);
                    flag = true;
                }


            } else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_Message_Deleted)) {
                String Activityid = "";
                String detete = " This message was deleted";
                try {
                    Activityid = obj.getString("ActivityId");
                    String Msg = obj.getString("Msg");
                    String query = "SELECT * FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST + " WHERE ChatRoomId ='" + Activityid + "'";
                    ;
                    Cursor cur = sql.rawQuery(query, null);
                    String count = String.valueOf(cur.getCount());
                    if (cur.getCount() > 0) {
                        cur.moveToFirst();
                        do {
                            //String ChatRoomId=cur.getString(cur.getColumnIndex("ChatRoomId"));
                            ChatRoomName = cur.getString(cur.getColumnIndex("ChatRoomName"));

                        } while (cur.moveToNext());
                    }
                   /* ContentValues contentValues = new ContentValues();
                    contentValues.put("ChatRoomId", Activityid);
                    contentValues.put("Message", detete);
                   */
                    sql.delete(db.TABLE_CHAT_GROUP_MESSAGE, "MessageId=?", new String[]{Msg});

                    // sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId=?", new String[]{Msg});


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (Activityid.equals(AddChatRoomActivity.ChatRoomId)) {
                    Intent intent = new Intent();
                    intent.setAction("chatscrren");
                    intent.putExtra("message", "0");
                    intent.putExtra("ChatRoomid", Activityid);
                    intent.putExtra("Chatroomname", ChatRoomName);
                    intent.putExtra("status", "");
                    context.sendBroadcast(intent);

                } else {
                    if (!flag) {
                        sendMessageDeleteNotification(detete, ChatRoomName);
                        flag = true;
                    }
                }


            }else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_GroupNameChange)) {

                String ChatRoomname="";
                try {
                    String Activityid = obj.getString("ActivityId");
                    MessageDecode =obj.getString("Msg");
                    String[] namesList = MessageDecode.split("to");
                    String name2 = namesList [1];

                    ContentValues values = new ContentValues();
                    values.put("ChatRoomName", name2);
                    values.put("UserMasterId", UserMasterId);
                    sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{Activityid});


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (!flag) {
                    sendMessageEditChatrromnameNotification(MessageDecode);
                    flag = true;
                }



            }
            else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgActivityReschedule)) {


                JSONObject mainObj = null;
                JSONArray jArray = null;
                try {
                    String ActivityId = obj.getString("ActivityId");

                    jArray = new JSONArray(Msgcontent);

                    if (jArray.length() > 0) {
                        for (int i = 0; i < jArray.length(); i++) {

                            JSONObject Jsonchatmember = jArray.getJSONObject(i);
                            String EndDate = Jsonchatmember.getString("EndDate");
                            // String ActivityId = Jsonchatmember.getString("ActivityId");
                            String ActivityName = Jsonchatmember.getString("ActivityName");
                            message = "Activity " + ActivityName + " has been rescheduled";
                            ContentValues values = new ContentValues();
                            values.put("EndDate", EndDate);
                            sql.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
                            sql.update(db.TABLE_ACTIVITYMASTER_PAGING, values, "ActivityId=?", new String[]{ActivityId});

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String Msgtype="Activity Rescheduled Notification";

                if (ActivityMain.isInFront == true) {
                    Intent intent = new Intent();
                    intent.setAction("assignscreen");
                    context.sendBroadcast(intent);
                    sendNotification(message,Msgtype);
                } else {
                    if (!flag) {
                        sendNotification(message,Msgtype);
                        flag = true;
                    }
                }

            }else if(MsgType.equalsIgnoreCase(WebUrlClass.anydukaan_cancel_ordbycust) ||
                    MsgType.equalsIgnoreCase(WebUrlClass.anydukaan_received_ordbycust)) {
                if (!flag) {
                    sendNotification_anydukaan(Msgcontent);
                    flag = true;
                }
            } else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_msgDeleteuser)) {
                String Usermessage = "";
                String UserMasterid = "";
                String ChatRoomName = "";
                String UserName = "";
                String ChatRoomid = "";

                try {
                    JSONArray jArray = new JSONArray(Msgcontent);

                    if (jArray.length() > 0) {
                        for (int i = 0; i < jArray.length(); i++) {

                            JSONObject Jsonchatmember = jArray.getJSONObject(i);
                            ChatRoomName = Jsonchatmember.getString("ChatRoomName");
                            UserName = Jsonchatmember.getString("UserName");
                            ChatRoomid = Jsonchatmember.getString("ChatRoomId");
                            UserMasterid = Jsonchatmember.getString("UserMasterId");
                        }

                        Usermessage = UserName + " has been remove from " + ChatRoomName;
                        if (UserMasterid.equals(UserMasterId)) {
                            ContentValues values = new ContentValues();
                            values.put("ChatRoomStatus", "Closed");
                            sql.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatRoomId=?", new String[]{ChatRoomid});
                            sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomid});
                            sql.delete(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, "ChatRoomId=? and ParticipantId=?", new String[]{ChatRoomid, UserMasterid});

                        } else {
                            sql.delete(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, "ChatRoomId=? and ParticipantId=?", new String[]{ChatRoomid, UserMasterid});

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (!flag) {
                    sendUserRemoveChatrooNotification(Usermessage);
                    flag = true;
                }


            } else if (MsgType.equalsIgnoreCase(WebUrlClass.ntf_Payment_Transfer)) {
                try {
                    JSONArray jArray = new JSONArray(Msgcontent);
                    if (jArray != null) {
                        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE, null);
                        int count = c.getCount();
                        if (jArray.length() > 0) {
                            for (int j = 0; j < jArray.length(); j++) {
                                ClaimNotification claimNotification = new ClaimNotification();
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
                if (!flag) {
                    sendClaimNotification(Data);
                    flag = true;
                }
            } else {
                try {
                    JSONObject mainObj = null;
                    try {
                        String Title = "";
                        String User = Msgcontent;
                        if (User != null) {
                            JSONObject obj1 = new JSONObject(User);
                            Firmname = obj1.getString("FirmName");
                            System.out.println("Message Firm name :" + Firmname);
                            AppointmentDate = obj1.getString("AppointmentDate");
                            date = getDate(AppointmentDate);
                            System.out.println("Message AppointmentDate :" + date);
                            AssignedTo = obj1.getString("AssignedTo");
                            System.out.println("Message AssignedTo :" + AssignedTo);
                            Remarks = obj1.getString("Remarks");
                            System.out.println("Message Remarks :" + Remarks);
                            Address = obj1.getString("Address");
                            System.out.println("Message Address  :" + Address);
                            Mobile = obj1.getString("Mobile");
                            System.out.println("Message Mobile   :" + Mobile);
                            Outcome = obj1.getString("OutCome");
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

                        } else if (Outcome.equalsIgnoreCase("Demo Request")) {
                            Title = "Demo Request";
                            NotificationMessage = AssignedTo + " has been requested you to give demo on " + Firmname + " on " + AppointmentDate;
                        }

                        editor = userpreferences.edit();
                        editor.putString("apichange", "true");
                        editor.commit();

                        sendNotification(date, AssignedTo, Mobile, Firmname, Address, Remarks, Title);


                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + Msgcontent + "\"");
                    }


                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + Msgcontent + "\"");
                }

            }
        } else {
            Log.e("notification", "already send notification");
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

    private boolean isnet() {
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts

    @SuppressLint("NewApi")
    private void sendActivityNotification(String messageBody,String Title) {
        Intent intent;
        if (Constants.type == Constants.Type.Delivery)
            intent = new Intent(context, ActivityMain.class);
        else {

            if (bean.getSourceType().equalsIgnoreCase("Datasheet")) {
                intent = new Intent(context, ActivityMain.class);
            } else {
                intent = new Intent(context, ActivityDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("actbean", bean);
                intent.putExtra("checkassign", "");
                intent.putExtra("unapprove", "");
                intent.putExtra("Flag", "1");
                intent.putExtras(bundle);
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel(channel_id,
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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel_id)
                .setContentTitle(Title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setGroup(messageBody)
                .setGroupSummary(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open Activity List");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Constants.type == Constants.Type.Delivery) {
                notificationBuilder.setSmallIcon(R.drawable.notification_delivery);
            } else
                notificationBuilder.setSmallIcon(R.mipmap.dummy);
            notificationBuilder.setColor(getResources().getColor(R.color.blue_color));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notificationBuilder.setSmallIcon(R.mipmap.ic_vwb);
        }
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

        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(code, notificationBuilder.build());
    }


    @SuppressLint("NewApi")
    private void sendNotification(String messageBody,String Title) {
        Intent intent = new Intent(this, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel(channel_id1,
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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel_id1)
                .setContentTitle(Title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(messageBody)
                .setGroupSummary(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open Activity List");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.dummy);
            notificationBuilder.setColor(getResources().getColor(R.color.blue_color));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notificationBuilder.setSmallIcon(R.drawable.vwb_logo);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);

        }
        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(code, notificationBuilder.build());
    }


    @SuppressLint("NewApi")
    private void sendNotification(String appointmentdate, String assigned, String mobile,
                                  String firmname, String address, String remark, String title) {
        Intent intent = new Intent(this, CRMHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent piResult = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel(channel_id,
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
        NotificationCompat.Builder notification = mBuilder
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.crm_logo_2))
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(NotificationMessage))
                .setContentIntent(piResult)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText(NotificationMessage);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.setSmallIcon(R.mipmap.dummy);
            notification.setColor(getResources().getColor(R.color.blue_color));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notification.setSmallIcon(R.drawable.vwb_logo);
        }
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notification.build());



     /* NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());*/


      /* Notification notification = new Notification.InboxStyle(builder)
                .setBigContentTitle(assigned+"has set an appoinment for you with"+firmname+","+address+"on"+appointmentdate+""+mobile+""+remark)
                .build();*/
       /* NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(121,notification);*/
    }

    private String getDateAdded(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
        long Etime = Long.parseLong(EndDresults);
        Date EndDate = new Date(Etime);
        String endDate = sdf.format(EndDate);
        return endDate;
    }

    private String getDateEnd(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
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
        AppCommon.getInstance(context).setChatPostion(0);
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.putString("UserMasterId", UserMasterId);
        editor.commit();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent piResult = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AppCommon.getInstance(context).setNotificationFlag(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel(channel_id2,
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
                this, channel_id2);
        NotificationCompat.Builder notification = mBuilder
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vwb_logo))
                .setContentTitle(Chatroomname)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Add_ChatMessage))
                .setContentIntent(piResult)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(Add_ChatMessage)
                .setGroupSummary(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentText(Add_ChatMessage);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.setSmallIcon(R.mipmap.dummy);
            notification.setColor(getResources().getColor(R.color.blue_color));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notification.setSmallIcon(R.drawable.vwb_logo);
        }

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);
        }
        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(code, notification.build());

    }

    @SuppressLint("NewApi")
    private void SendGroupChattingNotification(String Chatroomid, String ChatRoomName) {
        Intent intent = new Intent(this, AddChatRoomActivity.class);
        intent.putExtra("ChatRoomid", Chatroomid);
        intent.putExtra("Chatroomname", ChatRoomName);
        intent.putExtra("status", Groupstatus);
        intent.putExtra("msg", Group_Message);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AppCommon.getInstance(context).setNotificationFlag(true);

        String code1 = Chatroomid.replaceAll("[^0-9]", "");
        do {
            code1 = method(code1);
        } while (code1.length() != 5);
        int codeInt = Integer.valueOf(code1);

        PendingIntent piResult = PendingIntent.getActivity(this, codeInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel(channel_id3,
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
        String msgText = "";
        if (MessageType.equals("map:")) {
            msgText = "Location Received ....";
        } else {
            msgText = MessageDecode;
        }
        if (Attachment.equals("")) {
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            // NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,channel_id3);

            PendingIntent helpPendingIntent = PendingIntent.getBroadcast(
                    this,
                    REQUEST_CODE_HELP,
                    new Intent(context, NotificationReceiver.class)
                            .putExtra(KEY_INTENT_HELP, REQUEST_CODE_HELP)
                            .putExtra("ChatRoomid", Chatroomid)
                            .putExtra("Chatroomname", ChatRoomName)
                            .putExtra("status", Groupstatus)
                            .putExtra("notificationID", codeInt)
                            .putExtra("msg", Group_Message)
                            .putExtra("instance", String.valueOf(notificationManager))
                            .putExtra("messageId", MessageId),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            RemoteInput remoteInput = new RemoteInput.Builder(NOTIFICATION_REPLY)
                    .setLabel("Reply")
                    .build();
            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.app_logo,
                            "Reply Now...", helpPendingIntent)
                            .addRemoteInput(remoteInput)
                            .build();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel_id3);

            NotificationCompat.Builder notification = mBuilder
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_vwb))
                    .setContentTitle(UserName + " - " + ChatRoomName)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msgText))
                    .setContentIntent(piResult)
                    .setAutoCancel(true)
                    .addAction(action)
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setGroupSummary(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentText(msgText);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notification.setSmallIcon(R.mipmap.dummy);
                notification.setColor(getResources().getColor(R.color.blue_color));
            } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
                notification.setSmallIcon(R.mipmap.ic_vwb);
            }


            //  NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(codeInt, notification.build());

        } else if (Attachment.contains(".pdf") || Attachment.contains(".doc") || Attachment.contains(".docx") || Attachment.contains(".ppt") || Attachment.contains(".pptx")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel_id3);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.vwb_logo) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vwb_logo))
                    .setContentTitle(UserName + " - " + ChatRoomName)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Document Received"))
                    .setContentIntent(piResult)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentText("Document Receive").build();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(codeInt, notification);

        } else if (Attachment.contains(".mp3")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel_id3);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.vwb_logo) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vwb_logo))
                    .setContentTitle(UserName + " - " + ChatRoomName)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Audio Received"))
                    .setContentIntent(piResult)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentText("Audio Receive").build();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(codeInt, notification);

        } else if (Attachment.contains(".mp4")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel_id3);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.vwb_logo) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vwb_logo))
                    .setContentTitle(UserName + " - " + ChatRoomName)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Video Received"))
                    .setContentIntent(piResult)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentText("Video Receive").build();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(codeInt, notification);

        } else if (Attachment.contains(".jpg") || Attachment.contains(".png") || Attachment.contains(".jpeg")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel_id3);
            Notification notification = mBuilder
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.vwb_logo) //ok
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vwb_logo))
                    .setContentTitle(UserName + " - " + ChatRoomName)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Image Received"))
                    .setContentIntent(piResult)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentText("Image Receive").build();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(codeInt, notification);

        }

    }

    @SuppressLint("NewApi")
    private void sendClaimNotification(String messageBody) {
        Intent intent = new Intent(this, ClaimNotificationActivity.class);
        // ShortcutBadger.with(getApplicationContext()).remove();

        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel(channel_id4,
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
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel_id4)
                .setContentTitle(getResources().getString(R.string.app_name_notification))
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open Activity List");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.dummy);
            notificationBuilder.setColor(getResources().getColor(R.color.blue_color));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notificationBuilder.setSmallIcon(R.mipmap.ic_vwb);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);
        }
        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(code, notificationBuilder.build());
    }


    @SuppressLint("NewApi")
    private void sendChatroomClosedNotification(String AddedBy, String ChatroomName) {
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.putString("UserMasterId", UserMasterId);
        editor.commit();
        Intent intent = new Intent(this, OpenChatroomActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel(channel_id1,
                    "ChatClosed",
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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel_id1)
                .setContentTitle(getResources().getString(R.string.app_name_notification))
                .setContentText(ChatroomName + " has been closed by " + AddedBy)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(ChatroomName + " has been closed by " + AddedBy))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(ChatroomName + " has been closed by " + AddedBy)
                .setGroupSummary(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open List");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.dummy);
            notificationBuilder.setColor(getResources().getColor(R.color.blue_color));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notificationBuilder.setSmallIcon(R.drawable.vwb_logo);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);

        }
        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(code, notificationBuilder.build());
    }


    @SuppressLint("NewApi")
    private void sendBusinessNotification(String message, String typecode) {

        Intent intent = new Intent(this, ActvityNotificationTypeActivity.class);
        intent.putExtra("TypeCode", typecode);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel(channel_id1,
                    "Business_notification",
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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel_id1)
                .setContentTitle("Sahara Notification")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(message)
                .setGroupSummary(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open List");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.dummy);
            notificationBuilder.setColor(getResources().getColor(R.color.blue_color));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notificationBuilder.setSmallIcon(R.drawable.vwb_logo);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);

        }
        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(code, notificationBuilder.build());
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

    private String method(String str) {
        str = str.substring(0, str.length() - 1);

        return str;
    }


    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

        final String message = intent.getStringExtra("message");

        if (message != null) {
            // if (isAppRunning(message)) ;
            if (isApplicationSentToBackground(getApplicationContext())) {
                getnotification(message);
            }
        }
    }


    private boolean isAppRunning(final String message) {
        ActivityManager m = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = m.getRunningTasks(10);
        Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator();
        int n = 0;
        while (itr.hasNext()) {
            n++;
            itr.next();
        }
        if (n == 1) { // App is killed

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {


                    getnotification(message);


                }
            });
            return false;
        }

        return true; // App is in background or foreground
    }

    private boolean isApplicationSentToBackground(Context mcontext) {
        ActivityManager am = (ActivityManager) mcontext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mcontext.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("NewApi")
    private void sendMessageDeleteNotification(String Delete, String ChatRoomName) {
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.putString("UserMasterId", UserMasterId);
        editor.commit();
        Intent intent = new Intent(this, OpenChatroomActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AppCommon.getInstance(context).setNotificationFlag(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel(channel_id1,
                    "Messagedelete",
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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel_id1)
                .setContentTitle(ChatRoomName)
                .setContentText(Delete)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Delete))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(Delete)
                .setGroupSummary(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open ");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.dummy);
            notificationBuilder.setColor(getResources().getColor(R.color.blue_color));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notificationBuilder.setSmallIcon(R.drawable.vwb_logo);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);

        }
        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(code, notificationBuilder.build());
    }

    @SuppressLint("NewApi")
    private void sendUserRemoveChatrooNotification(String Message) {
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.putString("UserMasterId", UserMasterId);
        editor.commit();
        Intent intent = new Intent(this, OpenChatroomActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel(channel_id1,
                    "ChatClosed",
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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel_id1)
                .setContentTitle(getResources().getString(R.string.app_name_notification))
                .setContentText(Message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Message))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(Message)
                .setGroupSummary(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open List");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.dummy);
            notificationBuilder.setColor(getResources().getColor(R.color.blue_color));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notificationBuilder.setSmallIcon(R.drawable.vwb_logo);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);

        }
        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(code, notificationBuilder.build());
    }


    @SuppressLint("NewApi")
    private void sendMessageEditChatrromnameNotification(String Message) {
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.putString("UserMasterId", UserMasterId);
        editor.commit();
        Intent intent = new Intent(this, OpenChatroomActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AppCommon.getInstance(context).setNotificationFlag(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel= new NotificationChannel(channel_id1,
                    "NameChange",
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
                .setContentTitle(getResources().getString(R.string.app_name_notification))
                .setContentText(Message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Message))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(Message)
                .setGroupSummary(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open ");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.dummy);
            notificationBuilder.setColor(getResources().getColor(R.color.blue_color));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notificationBuilder.setSmallIcon(R.drawable.vwb_logo);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);

        }
        Random random = new Random();
        int code=random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(code, notificationBuilder.build());
    }
    @SuppressLint("NewApi")
    private void sendNotification_anydukaan(String Message) {
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.putString("UserMasterId", UserMasterId);
        editor.commit();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AppCommon.getInstance(context).setNotificationFlag(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel= new NotificationChannel(channel_id1,
                    "OrderNameChange",
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
                .setContentTitle(getResources().getString(R.string.app_name_notification))
                .setContentText(Message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Message))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(Message)
                .setGroupSummary(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open ");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.dummy);
            notificationBuilder.setColor(getResources().getColor(R.color.blue_color));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notificationBuilder.setSmallIcon(R.drawable.vwb_logo);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);

        }
        Random random = new Random();
        int code=random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(code, notificationBuilder.build());
    }

}