package com.vritti.crmlib.chat;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.ChatUserlistAdapter;
import com.vritti.crmlib.bean.ChatUser;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

/**
 * Created by sharvari on 30-Oct-17.
 */

public class UserListDisplayActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    ListView user_list_display;
    ChatUserlistAdapter chatUserlistAdapter;
    SQLiteDatabase sql;
    ArrayList<ChatUser> chatUserArrayList;
    EditText edt_search_name;
    SharedPreferences userpreferences;
    String Starttime, username,ChatRoomId,UserName1, UserMasterId1,ChatMessage,ChatRoomName,Call_Callid;
    ImageView img_adduser;
    ChatUser chatUser;
    String[] user;
    ArrayList<ChatUser> chatUserArrayList1;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_user_list_lay);
        getSupportActionBar().setTitle("Add Participant");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        chatUserArrayList = new ArrayList<>();
        chatUserArrayList1 = new ArrayList<>();
        user_list_display = (ListView) findViewById(R.id.user_list_display);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
        Starttime = df.format(c.getTime());



        intent = getIntent();
        ChatRoomId = intent.getStringExtra("ChatRoomid");
        ChatRoomName=intent.getStringExtra("Chatroomname");


        edt_search_name = (EditText) findViewById(R.id.edt_search_name);
        edt_search_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

                username=edt_search_name.getText().toString();
                if (username.length()>3){
                        if (isnet()) {
                            new StartSession(UserListDisplayActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadChatUSerDataJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        }
                    }


                // TODO Auto-generated method stub
            }
        });

        img_adduser = (ImageView) findViewById(R.id.img_adduser);

        img_adduser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if ((edt_search_name.getText().toString().equalsIgnoreCase("") ||
                        edt_search_name.getText().toString().equalsIgnoreCase(" ") ||
                        edt_search_name.getText().toString().equalsIgnoreCase(null))) {
                    Toast.makeText(UserListDisplayActivity.this, "Please enter name", Toast.LENGTH_LONG).show();

                }else {
                    chatUserArrayList = ((ChatUserlistAdapter) user_list_display.getAdapter()).getChatUserArrayList();
                    if (chatUserArrayList.size() > 0) {
                        // Toast.makeText(UserListDisplayActivity.this, "" + chatUserArrayList.size(), Toast.LENGTH_LONG).show();
                        String username = null;

                        JSONObject obj = new JSONObject();
                        JSONObject jsonobj = new JSONObject();

                        try {
                            if (chatUserArrayList.size() > 0) {
                                user = new String[chatUserArrayList.size()];
                                for (int i = 0; i < chatUserArrayList.size(); i++) {
                                    UserMasterId1 = chatUserArrayList.get(i).getUserMasterId();
                                    UserName1 = chatUserArrayList.get(i).getUsername();
                                    obj.put("UserId", UserMasterId1);
                                    obj.put("Username", UserName1);


                                    user[i] = obj.toString();
                                }
                            }
                            JSONArray obj1 = new JSONArray();

                            for (int i = 0; i < user.length; i++) {
                                JSONObject a1 = new JSONObject(user[i]);
                                obj1.put(a1);

                            }
                            jsonobj.put("AddUser", obj1);
                            jsonobj.put("Start_time", Starttime);
                            jsonobj.put("chatroom_Id", ChatRoomId);
                            jsonobj.put("Added_by", UserName);
                            jsonobj.put("Modified_by", "");
                            jsonobj.put("ChatRoomName", ChatRoomName);
                            final String FinalJson = jsonobj.toString();

                            if (isnet()) {
                                new StartSession(UserListDisplayActivity.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new PostSaveAddUserJSON().execute(FinalJson);
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {

                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(UserListDisplayActivity.this,"Please select name",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


       /* if (db.getChatusercount() > 0) {
            ChatUserUpdatList();
        }else {*/


    }

    class DownloadChatUSerDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response="error";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(UserListDisplayActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetUserListExcludingExisting + "?ChatRoomId=" + ChatRoomId + "&UserName=" + URLEncoder.encode(username, "UTF-8");

                try {
                    res = ut.OpenConnection(url,context);
                    if (res!=null) {
                        response = res.toString().replaceAll("\\\\", "");
                        response = response.replaceAll("\\\\\\\\/", "");
                        response = response.substring(1, response.length() - 1);
                        ContentValues values = new ContentValues();
                        JSONArray jResults = new JSONArray(response);
                        /* sql.delete(db.TABLE_CHAT_USER_LIST, null,
                            null);*/
                        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_USER_LIST, null);
                        int count = c.getCount();
                        String columnName, columnValue;
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject jorder = jResults.getJSONObject(i);
                            String Id = jorder.getString("UserMasterId");

                            if (CheckifRecordPresent(db.TABLE_CHAT_USER_LIST, "UserMasterId", Id)) {
                                for (int j = 0; j < c.getColumnCount(); j++) {

                                    columnName = c.getColumnName(j);
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);

                                }

                                long a = sql.insert(db.TABLE_CHAT_USER_LIST, null, values);
                            }
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response="error";
                }
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (response != null) {
                progressDialog.dismiss();
                ChatUserUpdatList(username);
                    }


        }

    }

    private void ChatUserUpdatList(String member_user) {
        chatUserArrayList.clear();
        String query = "SELECT UserName,UserMasterId" +
                " FROM " + db.TABLE_CHAT_USER_LIST + " WHERE  UserName like '%" + member_user + "%'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ChatUser chatUser = new ChatUser();
                chatUser.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                chatUser.setUsername(cur.getString(cur.getColumnIndex("UserName")));
                chatUserArrayList.add(chatUser);
            } while (cur.moveToNext());

            chatUserlistAdapter = new ChatUserlistAdapter(UserListDisplayActivity.this, chatUserArrayList);
            user_list_display.setAdapter(chatUserlistAdapter);
            chatUserlistAdapter.notifyDataSetChanged();

        }
    }



    /*private void ChatUserUpdatList() {
        chatUserArrayList.clear();
        String query = "SELECT * FROM " + db.TABLE_CHAT_USER_LIST;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

            } while (cur.moveToNext());



        }


    }
*/
    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    class PostSaveAddUserJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(UserListDisplayActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_PostAddUser;
            System.out.println("AdvanceProvisionalRecieptjson-1 :" + url);

            try {
                res = ut.OpenPostConnection(url, params[0]);
                System.out.println("BusinessAPI-2 :" + res);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                System.out.println("AdvanceProvisionalRecieptjson-1 :" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (!response.equalsIgnoreCase("[]")) {
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);
                    ContentValues values = new ContentValues();

                    sql.delete(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null);
                    int count = c.getCount();
                    if (jResults.length() > 0) {
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject Jsonchatmember1 = jResults.getJSONObject(i);
                            ChatUser chatUser = new ChatUser();
                            ChatRoomName = Jsonchatmember1.getString("ChatRoomName");
                            chatUser.setChatroom(ChatRoomName);
                            ChatRoomId = Jsonchatmember1.getString("ChatRoomId");
                            chatUser.setChatRoomId(ChatRoomId);
                            chatUser.setStatus(Jsonchatmember1.getString("ChatRoomStatus"));
                            chatUser.setStartTime(Jsonchatmember1.getString("StartTime"));
                            chatUser.setCreater(Jsonchatmember1.getString("Creator"));
                            Call_Callid = Jsonchatmember1.getString("ChatSourceId");
                            chatUser.setChatSourceId(Jsonchatmember1.getString("ChatSourceId"));
                            String  AddedBy=Jsonchatmember1.getString("AddedBy");
                            AddedBy=AddedBy.trim();
                            chatUser.setAddedBy(AddedBy);
                            chatUser.setParticipantName(Jsonchatmember1.getString("ParticipantName"));
                            chatUser.setParticipantId(Jsonchatmember1.getString("ParticipantId"));
                            ChatMessage = Jsonchatmember1.getString("Message");
                            chatUser.setMessage(ChatMessage);
                            chatUser.setUserMasterId(UserMasterId);
                            chatUser.setCount("0");
                            cf.AddGroupMember(chatUser);
                            chatUserArrayList.add(chatUser);
                        }
                    }

                    Toast.makeText(UserListDisplayActivity.this, "Participant added successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UserListDisplayActivity.this, GroupmemberShowActivity.class);
                    intent.putExtra("ChatRoomid", ChatRoomId);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserListDisplayActivity.this.finish();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_member_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        if (id == R.id.new_group_member) {
            if (isnet()) {
                new StartSession(UserListDisplayActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadChatUSerDataJSON().execute(ChatRoomId);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
        return (super.onOptionsItemSelected(menuItem));

    }
    private Boolean CheckifRecordPresent(String Table, String Column, String Value) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c1 = sql.rawQuery("SELECT * FROM " + Table, null);
        Cursor c = sql.rawQuery("SELECT * FROM " + Table + " WHERE " + Column + "='" + Value + "'", null);
        int a1 = c1.getCount();
        int a = c.getCount();
        if (a == 0) {
            return true;
        } else {
            return false;
        }
    }
}