package com.vritti.vwblib.chat;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.classes.CommonFunction;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vritti.vwblib.R;


/**
 * Created by pradnya on 26-Oct-17.
 */

public class AddGroupActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    EditText editGroupName, edt_search_name;
    TextView txtActionName, txtGroupIcon;
    ImageView txt_create_room;
    private Set<String> listIDChoose;
    SharedPreferences userpreferences;
    String  Groupname, Starttime, Endtime, Status, Roomid,  ChatSourceType = "1", ChatSourceId = "hfhsf2132322";
    String Call_Callid="", Call_CallType="", ChatRoomId = "", ChatMessage = "", ChatRoomName = "";
    ListView listview_user;
    ChatUserlistAdapter chatUserlistAdapter;
    SQLiteDatabase sql;
    ArrayList<ChatUser> chatUserArrayList;
    ImageView img_adduser;
    String[] user;
    String UserMasterId1, UserName1, Firmname;
    RelativeLayout len_add_user;
    RelativeLayout len_group;
    String username;
    ProgressDialog progressDialog;
     String cStatus="", ProjectMasterID;
    private JSONObject jsonObject;
    private Date date;
    String dotformat="No",Individual;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_activity_add_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
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
        init();


        /* if (db.getChatusercount()>0){
            ChatUserUpdatList();
        }*/


    }

    private void init() {

        Intent intent = getIntent();
        Call_Callid = intent.getStringExtra("callid");
        if (Call_Callid==null){
            Call_Callid="";
        }
        Call_CallType = intent.getStringExtra("call_type");
        Groupname = intent.getStringExtra("firm");
        if(intent.hasExtra("status")){
            cStatus = intent.getStringExtra("status");
        }

        ProjectMasterID = intent.getStringExtra("projmasterId");
        Individual=intent.getStringExtra("Individual");




        Date d=new Date();


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
        Starttime = df.format(c.getTime());

        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        Starttime = dateFormat.format(new Date()).toString();*/
        System.out.println(Starttime);

        if (Starttime.contains("a.m.")){
            dotformat="yes";
            String a = Starttime.replace("a.m.","AM");
            Starttime =a;
        }else if (Starttime.contains("p.m.")){
            dotformat="yes";
            String a = Starttime.replace("p.m.","PM");
            Starttime =a;
        }







        if (Individual==null){

        }else {
            if (Individual.equals("1")) {
                Groupname = UserName;

            }
        }
        editGroupName = (EditText) findViewById(R.id.editGroupName);

        editGroupName.setText(Groupname);
        editGroupName.setSelection(editGroupName.getText().length());
        listIDChoose = new HashSet<>();
        txtGroupIcon = (TextView) findViewById(R.id.icon_group);
        txt_create_room = (ImageView) findViewById(R.id.txt_create_room);
        listview_user = (ListView) findViewById(R.id.listview_user);
        edt_search_name = (EditText) findViewById(R.id.edt_search_name);
        len_add_user = (RelativeLayout) findViewById(R.id.len_add_user);
        len_group = (RelativeLayout) findViewById(R.id.len_group);
        img_adduser = (ImageView) findViewById(R.id.img_adduser);


        edt_search_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable text) {
                // TODO Auto-generated method stub


                //chatUserlistAdapter.filter(username);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

                ArrayList<ChatUser> list = new ArrayList<ChatUser>();
                list.clear();
                username = edt_search_name.getText().toString();
                if (!(chatUserlistAdapter == null)) {
                    list = chatUserlistAdapter.filter(arg0.toString().trim());
                }
                if (!(list.size() > 0)) {
                    if (username.length() > 3) {
                        if (isnet()) {
                            new StartSession(AddGroupActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadChatUSerDataJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter atleast three letters to customize your search", Toast.LENGTH_LONG).show();
                    }

                }


            }
        });


        img_adduser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                chatUserArrayList = chatUserlistAdapter.getChatUserArrayList();

                // Toast.makeText(AddGroupActivity.this,""+chatUserArrayList.size(),Toast.LENGTH_LONG).show();
                String username = null;

                JSONObject obj = new JSONObject();
                JSONObject jsonobj = new JSONObject();
                int a = chatUserArrayList.size();
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
                        jsonobj.put("ChatRoomName", Groupname);
                        final String FinalJson = jsonobj.toString();

                        if (isnet()) {
                            new StartSession(AddGroupActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostSaveAddUserJSON().execute(FinalJson);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select atleast one participant", Toast.LENGTH_LONG).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


        editGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    txtGroupIcon.setText((charSequence.charAt(0) + "").toUpperCase());
                } else {
                    txtGroupIcon.setText("V");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txt_create_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Groupname = editGroupName.getText().toString();


                /*if (listIDChoose.size() < 3) {
                //    Toast.makeText(AddGroupActivity.this, "Add at lease two people to create group", Toast.LENGTH_SHORT).show();
                } else {
                */
                if (editGroupName.getText().length() == 0) {
                    Toast.makeText(AddGroupActivity.this, "Enter chat room name", Toast.LENGTH_SHORT).show();
                } else {
                    if (isnet()) {
                        createGroup();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
                    }

                        /*if (isEditGroup) {
                            editGroup();
                        } else {
                            ();
                        }*/
                }

            }
        });

    }

    private void createGroup() {

        AddGroupJSON addGroupJSON = new AddGroupJSON();
        addGroupJSON.execute();

    }

    class AddGroupJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(AddGroupActivity.this);
                    progressDialog.setMessage("Loading. Please wait...");
                    progressDialog.setIndeterminate(false);
                    //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //  progressDialog.setContentView(R.layout.vwb_progress_lay);
                    progressDialog.setCancelable(true);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetCreateChatRoom + "?initiatorId=" + UserMasterId + "&ChatRoomName=" + URLEncoder.encode(Groupname, "UTF-8") + "&StartTime=" + URLEncoder.encode(Starttime, "UTF-8") + "&EndTime=2020/10/30&ChatSourceType=" + URLEncoder.encode(Call_CallType, "UTF-8") + "&ChatSourceId=" + Call_Callid + "&AddedBy=" + URLEncoder.encode(UserName, "UTF-8") + "&ModifiedBy=" + URLEncoder.encode(UserName, "UTF-8") + "&AppCode=" + WebUrlClass.AppNameFCM;
                String url1= CompanyURL + WebUrlClass.api_GetCreateChatRoom + "?initiatorId=" + UserMasterId + "&ChatRoomName=" + URLEncoder.encode(Groupname, "UTF-8") + "&StartTime=" + URLEncoder.encode(Starttime, "UTF-8") + "&EndTime=2020/10/30&ChatSourceType=" + URLEncoder.encode(Call_CallType, "UTF-8") + "&ChatSourceId=" + Call_Callid + "&AddedBy=" + URLEncoder.encode(UserName, "UTF-8") + "&ModifiedBy=" + URLEncoder.encode(UserName, "UTF-8") + "&AppCode=" + WebUrlClass.AppNameFCM;

                System.out.println("AddGroup" + url);
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {

                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            String Status = "";
            try {
                jsonObject = new JSONObject(integer);
                Status = jsonObject.getString("Status");
                ChatRoomId = jsonObject.getString("ChatRoomId");
                Groupname = jsonObject.getString("ChatRoomName");




            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (Status.equalsIgnoreCase("Success")) {
                Toast.makeText(AddGroupActivity.this, "Chat Room created successfully", Toast.LENGTH_LONG).show();
 try{
                ChatGroup chatGroup = new ChatGroup();
                chatGroup.setChatroom(Groupname);
                chatGroup.setChatRoomId(ChatRoomId);
                chatGroup.setStatus(jsonObject.getString("ChatRoomStatus"));
                String StartTime=jsonObject.getString("StartTime");
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
                 if (dotformat.equals("yes")){
                     if (StartTime.contains("AM")){
                         String a = StartTime.replace("AM","a.m.");
                         StartTime =a;
                     }else if (StartTime.contains("PM")){
                         String a = StartTime.replace("PM","p.m.");
                         StartTime =a;
                     }
                 }

               DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                 try {
                     date = originalFormat.parse(StartTime);
                 } catch (ParseException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                 }
                String  chat_time = targetFormat.format(date);
     chatGroup.setStartTime(chat_time);
     chatGroup.setCreater(jsonObject.getString("Creator"));
     chatGroup.setAddedBy(jsonObject.getString("AddedBy"));
     chatGroup.setUserMasterId(UserMasterId);
     chatGroup.setCount("0");
                cf.AddGroupList(chatGroup);
              //  chatUserArrayList.add(chatUser);

                } catch (JSONException e) {
                     e.printStackTrace();
                }

                cleardata();
                len_group.setVisibility(View.GONE);
                len_add_user.setVisibility(View.VISIBLE);
               /* if (CheckLocalChatUser()) {
                    ChatUserUpdate();
                } else {*/
                if (isnet()) {
                    new StartSession(getApplicationContext(), new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadGetChkUserlistDataJSON().execute(ProjectMasterID);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
                }
                //  }

            } else {
                Toast.makeText(AddGroupActivity.this, "Chat room not created.Please try after sometime", Toast.LENGTH_LONG).show();
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

    }

    private void cleardata() {
        editGroupName.setText("");
    }


    class DownloadChatUSerDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(AddGroupActivity.this);
                    progressDialog.setMessage("Loading. Please wait...");
                    progressDialog.setIndeterminate(false);
                    //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //  progressDialog.setContentView(R.layout.vwb_progress_lay);
                    progressDialog.setCancelable(true);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetUserList + "?UserName=" + URLEncoder.encode(username, "UTF-8");

                try {
                    res = ut.OpenConnection(url, getApplicationContext());
                    if (res != null) {
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
                                Log.e("cnt", "" + a);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "error";
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (response != null) {
                // ChatUserUpdatList(username);
                ChatUserUpdate();

            }


        }

    }

    private void ChatUserUpdatList(String member_username) {
        chatUserArrayList.clear();
        String query = "SELECT UserName,UserMasterId" +
                " FROM " + db.TABLE_CHAT_USER_LIST + " WHERE  UserName like '%" + member_username + "%'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ChatUser chatUser = new ChatUser();
                chatUser.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                chatUser.setUsername(cur.getString(cur.getColumnIndex("UserName")));
                chatUserArrayList.add(chatUser);
            } while (cur.moveToNext());

            chatUserlistAdapter = new ChatUserlistAdapter(AddGroupActivity.this, chatUserArrayList);
            listview_user.setAdapter(chatUserlistAdapter);

        }


    }

    private void ChatUserUpdate() {
        chatUserArrayList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_CHAT_USER_LIST+" ORDER BY UserName ASC" ;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ChatUser chatUser = new ChatUser();
                chatUser.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                chatUser.setUsername(cur.getString(cur.getColumnIndex("UserName")));
                chatUserArrayList.add(chatUser);
            } while (cur.moveToNext());

            chatUserlistAdapter = new ChatUserlistAdapter(AddGroupActivity.this, chatUserArrayList);
            listview_user.setAdapter(chatUserlistAdapter);
        }
    }

    private Boolean CheckLocalChatUser() {
        String query = "SELECT *" +
                " FROM " + db.TABLE_CHAT_USER_LIST;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

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


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(AddGroupActivity.this);
                    progressDialog.setMessage("Loading. Please wait...");
                    progressDialog.setIndeterminate(false);
                    //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //  progressDialog.setContentView(R.layout.vwb_progress_lay);
                    progressDialog.setCancelable(true);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_PostAddUser;
            System.out.println("AdvanceProvisionalRecieptjson-1 :" + url);

            try {
                res = ut.OpenPostConnection(url, params[0], getApplicationContext());
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
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (response != null) {
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);
                    ContentValues values = new ContentValues();
                   // chatUserArrayList.clear();
                   /* sql.delete(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null,
                            null);*/
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
                            String StartTime=Jsonchatmember1.getString("StartTime");
                            chatUser.setStartTime(StartTime);
                            chatUser.setCreater(Jsonchatmember1.getString("Creator"));
                            Call_Callid = Jsonchatmember1.getString("ChatSourceId");
                            chatUser.setChatSourceId(Jsonchatmember1.getString("ChatSourceId"));
                            chatUser.setAddedBy(Jsonchatmember1.getString("AddedBy"));
                            chatUser.setParticipantName(Jsonchatmember1.getString("ParticipantName"));
                            chatUser.setParticipantId(Jsonchatmember1.getString("ParticipantId"));
                            ChatMessage = Jsonchatmember1.getString("Message");
                            chatUser.setMessage(ChatMessage);
                            chatUser.setUserMasterId(UserMasterId);
                            chatUser.setCount("0");
                            cf.AddGroupMember(chatUser);
                            chatUserArrayList.add(chatUser);
                        }
                        Toast.makeText(AddGroupActivity.this, "Participants added successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddGroupActivity.this, AddChatRoomActivity.class);
                        intent.putExtra("ChatRoomid", ChatRoomId);
                        intent.putExtra("Chatroomname", ChatRoomName);
                        intent.putExtra("status", cStatus);
                        intent.putExtra("projmasterId",ProjectMasterID );
                        startActivity(intent);
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(AddGroupActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
            }


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AddGroupActivity.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        //stopForegroundDispatch(this, nfcAdapter);
        super.onPause();

        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;

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

    class DownloadGetChkUserlistDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(AddGroupActivity.this);
                    progressDialog.setMessage("Loading. Please wait...");
                    progressDialog.setIndeterminate(false);
                    //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //  progressDialog.setContentView(R.layout.vwb_progress_lay);
                    progressDialog.setCancelable(true);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetChkUser_list + "?prjMstId=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (!(response.equalsIgnoreCase("error"))) {
                try {

                    SQLiteDatabase sql = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
               /* sql.delete(db.TABLE_PROJECT_MEMBERS, null,
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
                            String data = a + "";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ChatUserUpdate();
        }


    }
}

