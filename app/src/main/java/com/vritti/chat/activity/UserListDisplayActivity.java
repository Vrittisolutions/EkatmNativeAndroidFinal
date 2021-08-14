package com.vritti.chat.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.chat.adapter.AddUserAdapter;
import com.vritti.chat.bean.ChatUser;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.UserList;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by pradnya on 30-Oct-17.
 */

public class UserListDisplayActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    RecyclerView user_list_display;
    // ChatUserlistAdapter chatUserlistAdapter;
    AddUserAdapter chatUserlistAdapter;
    SQLiteDatabase sql;
    ArrayList<ChatUser> chatUserArrayList;
    ArrayList<ChatUser> tempChatUserArrayList;
    EditText edt_search_name;
    SharedPreferences userpreferences;
    String Starttime, username, ChatRoomId, UserName1, UserMasterId1, ChatMessage, ChatRoomName, Call_Callid;
    ImageView img_adduser;
    ChatUser chatUser;
    String[] user;
    ArrayList<ChatUser> chatUserArrayList1;
    Intent intent;
    String ProjectMasterID;
    ProgressDialog progressDialog;
    private String dotformat = "No";
    ProgressBar mprogress;
    ProgressBar progress_bar;
    ArrayList<ChatUser> lastUserList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_user_list_lay);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(" Add Participant");
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        setSupportActionBar(toolbar);

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
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        UserName = "";
        sql = db.getWritableDatabase();
        chatUserArrayList = new ArrayList<>();
        tempChatUserArrayList = new ArrayList<>();
        user_list_display = findViewById(R.id.user_list_display);
        mprogress = findViewById(R.id.toolbar_progress_App_bar);
        progress_bar = findViewById(R.id.progress_bar);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("chatroom_user");
        lastUserList = (ArrayList<ChatUser>) args.getSerializable("userlist");
       /* Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
        Starttime = df.format(c.getTime());*/

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        Starttime = dateFormat.format(new Date()).toString();
        System.out.println(Starttime);
        if (Starttime.contains("a.m.")) {
            dotformat = "yes";
            String a = Starttime.replace("a.m.", "AM");
            Starttime = a;
        } else if (Starttime.contains("p.m.")) {
            dotformat = "yes";
            String a = Starttime.replace("p.m.", "PM");
            Starttime = a;
        }

        intent = getIntent();
        ChatRoomId = intent.getStringExtra("ChatRoomid");
        ChatRoomName = intent.getStringExtra("Chatroomname");
        ProjectMasterID = intent.getStringExtra("projmasterId");
        edt_search_name = (EditText) findViewById(R.id.edt_search_name);

        chatUserlistAdapter = new AddUserAdapter(UserListDisplayActivity.this, chatUserArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        user_list_display.setLayoutManager(mLayoutManager);
        user_list_display.setAdapter(chatUserlistAdapter);

        if (CheckLocalChatUser()) {
            ChatUserUpdate();
        } else {
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
        }
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
                ArrayList<ChatUser> list = new ArrayList<ChatUser>();
                username = edt_search_name.getText().toString();
                if (!(chatUserlistAdapter == null)) {
                    list = filter(arg0.toString().trim());

                }
                if (!(list.size() > 0)) {
                    if (username.length() > 1) {
                        if (isnet()) {
                            progress_bar.setVisibility(View.VISIBLE);
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

                    } else {
                        // Toast.makeText(getApplicationContext(), "Please enter atleast three letters to customize your search", Toast.LENGTH_LONG).show();
                    }

                }


                // TODO Auto-generated method stub
            }
        });

        img_adduser = (ImageView) findViewById(R.id.img_adduser);

        img_adduser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Toast.makeText(UserListDisplayActivity.this, "" + chatUserArrayList.size(), Toast.LENGTH_LONG).show();
                String username = null;

                JSONObject obj = new JSONObject();
                JSONObject jsonobj = new JSONObject();

                try {
                    if (getChatUserArrayList().size() > 0) {
                        chatUserArrayList = getChatUserArrayList();
                        tempChatUserArrayList.clear();
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
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select at least one participants", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ArrayList<ChatUser> filter(String trim) {


        String charText = trim.toLowerCase(Locale.getDefault());
        ArrayList<ChatUser> chatUsers = new ArrayList<>();
        // chatUserArrayList.clear();
        if (charText.length() == 0) {
            chatUsers.addAll(chatUserArrayList);
        } else {

            for (ChatUser wp : chatUserArrayList) {
                if (wp.getUsername().toLowerCase(Locale.getDefault()).contains(charText)) {
                    chatUsers.add(wp);
                }
            }
        }
        chatUserArrayList = chatUsers;
        //chatUserlistAdapter.notifyDataSetChanged();
        user_list_display.setAdapter(new AddUserAdapter(this, chatUserArrayList));
        // chatUserlistAdapter.fil
        // notifyDataSetChanged();
        return chatUsers;


    }

    public ArrayList<ChatUser> getChatUserArrayList() {
        ArrayList<ChatUser> list = new ArrayList<>();
        list.clear();
        for (int i = 0; i < tempChatUserArrayList.size(); i++) {
            if (tempChatUserArrayList.get(i).isSelected())
                list.add(tempChatUserArrayList.get(i));
        }
        return list;
    }


    private void ChatUserUpdate() {
        chatUserArrayList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_CHAT_USER_LIST + " ORDER BY UserName ASC";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ChatUser chatUser = new ChatUser();
                chatUser.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                chatUser.setUsername(cur.getString(cur.getColumnIndex("UserName")));
                chatUserArrayList.add(chatUser);
            } while (cur.moveToNext());

            for (int i = 0; i < chatUserArrayList.size(); i++) {
                if (tempChatUserArrayList.size() != 0) {
                    for (ChatUser tempList : tempChatUserArrayList) {
                        if (chatUserArrayList.get(i).getUserMasterId().equals(tempList.getUserMasterId())) {
                            if (tempList.isSelected()) {
                                chatUserArrayList.get(i).setSelected(true);
                            } else {
                                chatUserArrayList.get(i).setSelected(false);
                            }
                        } else {
                            chatUserArrayList.get(i).setSelected(false);
                        }
                    }
                }
            }

            chatUserlistAdapter = new AddUserAdapter(UserListDisplayActivity.this, chatUserArrayList);
            user_list_display.setAdapter(chatUserlistAdapter);
            //chatUserlistAdapter.notifyDataSetChanged();

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

            chatUserlistAdapter = new AddUserAdapter(UserListDisplayActivity.this, chatUserArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            user_list_display.setLayoutManager(mLayoutManager);
            user_list_display.setAdapter(chatUserlistAdapter);
            // chatUserlistAdapter.notifyDataSetChanged();

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserListDisplayActivity.this.finish();
    }

    public void setUserSelectiom(boolean checked, int adapterPosition) {
        boolean isAlreadyInGroupChat = false;
            for (ChatUser userList : lastUserList) {
                isAlreadyInGroupChat = userList.getParticipantId().equals(chatUserArrayList.get(adapterPosition).getUserMasterId());
                if (isAlreadyInGroupChat) {
                    break;
                }
            }

        if (!isAlreadyInGroupChat) {
            if (tempChatUserArrayList.size() != 0) {
                for (int i = 0; i < tempChatUserArrayList.size(); i++) {
                    if (tempChatUserArrayList.get(i).getUserMasterId().equals(chatUserArrayList.get(adapterPosition).getUserMasterId())) {
                        if (checked) {
                            chatUserArrayList.get(adapterPosition).setSelected(checked);
                            tempChatUserArrayList.add(chatUserArrayList.get(adapterPosition));
                        } else {
                            tempChatUserArrayList.remove(tempChatUserArrayList.get(i));
                        }
                        break;
                    } else {
                        if (checked) {
                            chatUserArrayList.get(adapterPosition).setSelected(checked);
                            tempChatUserArrayList.add(chatUserArrayList.get(adapterPosition));
                        } else {
                            tempChatUserArrayList.remove(chatUserArrayList.get(adapterPosition));
                        }
                    }
                }
            } else {
                if (checked) {
                    chatUserArrayList.get(adapterPosition).setSelected(checked);
                    tempChatUserArrayList.add(chatUserArrayList.get(adapterPosition));
                }

            }
            chatUserArrayList.get(adapterPosition).setSelected(checked);
            chatUserlistAdapter.notifyItemChanged(adapterPosition);
        } else {
            Toast.makeText(context, "Participant already in chatroom", Toast.LENGTH_SHORT).show();
            chatUserArrayList.get(adapterPosition).setSelected(false);
            chatUserlistAdapter = new AddUserAdapter(UserListDisplayActivity.this, chatUserArrayList);
            user_list_display.setAdapter(chatUserlistAdapter);
        }
    }


    public class ChatUserlistAdapter extends BaseAdapter {
        ArrayList<ChatUser> chatUserArrayList;
        LayoutInflater mInflater;
        Context context;
        ArrayList<ChatUser> arraylist;

        public ChatUserlistAdapter(Context context1, ArrayList<ChatUser> chatUserArrayList) {
            this.chatUserArrayList = chatUserArrayList;
            mInflater = LayoutInflater.from(context1);
            context = context1;
            this.arraylist = new ArrayList<ChatUser>();
            this.arraylist.addAll(chatUserArrayList);

        }

        @Override
        public int getCount() {
            return chatUserArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return chatUserArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.vwb_user_item_list, null);
                holder = new ViewHolder();
                holder.txt_user_name_data = (TextView) convertView.findViewById(R.id.user_name_data);
                holder.checkbox_user = (AppCompatCheckBox) convertView.findViewById(R.id.checkbox_user);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.checkbox_user.setText(chatUserArrayList.get(position).getUsername());
            holder.checkbox_user.setChecked(chatUserArrayList.get(position).isSelected());


            holder.checkbox_user.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    boolean isSelected = ((AppCompatCheckBox) v).isChecked();
                    chatUserArrayList.get(position).setSelected(isSelected);
                    tempChatUserArrayList.add(chatUserArrayList.get(position));

                }
            });
            return convertView;
        }


        public class ViewHolder {
            TextView txt_user_name_data;
            AppCompatCheckBox checkbox_user;


        }
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
            ChatUserUpdate();
           /* if (isnet()) {
                new StartSession(UserListDisplayActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadChatUSerDataJSON().execute(ChatRoomId);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }*/

        }
        return (super.onOptionsItemSelected(menuItem));

    }

    private Boolean CheckifRecordPresent(String Table, String Column, String Value) {
        // SQLiteDatabase sql = db.getWritableDatabase();
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

    class PostSaveAddUserJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;
        private JSONObject obj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(UserListDisplayActivity.this);
                progressDialog.setMessage("Loading please wait...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(true);

            }
            progressDialog.show();

           /* progressDialog = new ProgressDialog(UserListDisplayActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.show();
            progressDialog.setContentView(R.layout.vwb_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_PostAddUser;
            System.out.println("AdvanceProvisionalRecieptjson-1 :" + url);

            try {
                /*res = ut.OpenPostConnection(url, params[0], getApplicationContext());
                System.out.println("BusinessAPI-2 :" + res);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);*/
                res = ut.OpenPostConnection(url,params[0], UserListDisplayActivity.this);

                response =  String.valueOf(new JSONTokener(res.toString()).nextValue());
/*
                try {
                    obj = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

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
                JSONArray jResults = new JSONArray();
                try {
                   jResults = new JSONArray(response);

                   //jResults = obj.getJSONArray("PostAddUser");

                    ContentValues values = new ContentValues();
                    //  chatUserArrayList.clear();
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
                            chatUser.setStartTime(Jsonchatmember1.getString("StartTime"));
                            chatUser.setCreater(Jsonchatmember1.getString("Creator"));
                            Call_Callid = Jsonchatmember1.getString("ChatSourceId");
                            chatUser.setChatSourceId(Jsonchatmember1.getString("ChatSourceId"));
                            String AddedBy = Jsonchatmember1.getString("AddedBy");
                            AddedBy = AddedBy.trim();
                            chatUser.setAddedBy(AddedBy);
                            chatUser.setParticipantName(Jsonchatmember1.getString("ParticipantName"));
                            chatUser.setParticipantId(Jsonchatmember1.getString("ParticipantId"));
                            ChatMessage = Jsonchatmember1.getString("Message");
                            chatUser.setMessage(ChatMessage);
                            chatUser.setUserMasterId(UserMasterId);
                            chatUser.setCount("0");
                            chatUser.setChatType(Jsonchatmember1.getString("ChatType"));

                            if (cf.CheckifRecordPresent(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, "ParticipantId", "ChatRoomId", chatUser.getParticipantId(), chatUser.getChatRoomId())) {

                                cf.AddGroupMember(chatUser);
                                chatUserArrayList.add(chatUser);

                            }

                        }
                    }

                    Toast.makeText(UserListDisplayActivity.this, "Participant added successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UserListDisplayActivity.this, GroupmemberShowActivity.class);
                    intent.putExtra("ChatRoomid", ChatRoomId);
                    intent.putExtra("projmasterId", ProjectMasterID);
                    intent.putExtra("chatroom_user", username);
                    intent.putExtra("Chatroomname", ChatRoomName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }


    class DownloadGetChkUserlistDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;
        private JSONObject obj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(UserListDisplayActivity.this);
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

                    // SQLiteDatabase sql = db.getWritableDatabase();

                    ContentValues values = new ContentValues();
                   // jResults = obj.getJSONArray("UserListExcludingExisting");
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

    class DownloadChatUSerDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        private JSONObject obj;


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

           /* if (progressDialog == null) {
                progressDialog = new ProgressDialog(UserListDisplayActivity.this);
                progressDialog.setMessage("Loading. Please wait...");
                progressDialog.setIndeterminate(false);
                //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //  progressDialog.setContentView(R.layout.vwb_progress_lay);
                progressDialog.setCancelable(true);

            }
            progressDialog.show();*/
            progress_bar.setVisibility(View.VISIBLE);


        }


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetUserListExcludingExisting + "?ChatRoomId=" + ChatRoomId + "&UserName=" + URLEncoder.encode(username, "UTF-8");

                res = ut.OpenConnection(url, UserListDisplayActivity.this);

                try {
                    if (res != null) {

                        response =  String.valueOf(new JSONTokener(res.toString()).nextValue());

                        try {
                            obj = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray jResults=new JSONArray();
                        jResults = obj.getJSONArray("UserListExcludingExisting");
                        chatUserArrayList.clear();
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject jorder = jResults.getJSONObject(i);
                            String Id = jorder.getString("UserMasterId");

                            ChatUser chatUser = new ChatUser();
                            chatUser.setUserMasterId(Id);
                            chatUser.setUsername(jorder.getString("UserName"));
                            chatUserArrayList.add(chatUser);

                        }
                        for (int i = 0; i < chatUserArrayList.size(); i++) {
                            if (tempChatUserArrayList.size() != 0) {
                                for (ChatUser tempList : tempChatUserArrayList) {
                                    if (chatUserArrayList.get(i).getUserMasterId().equals(tempList.getUserMasterId())) {
                                        if (tempList.isSelected()) {
                                            chatUserArrayList.get(i).setSelected(true);
                                        } else {
                                            chatUserArrayList.get(i).setSelected(false);
                                        }
                                        break;
                                    } else {
                                        chatUserArrayList.get(i).setSelected(false);
                                    }

                                }
                            }
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "error";
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progress_bar.setVisibility(View.GONE);

            //progressDialog.dismiss();
            if (response != "error") {
                //progressDialog.dismiss();
                progress_bar.setVisibility(View.GONE);
                // ChatUserUpdate();
                chatUserlistAdapter = new AddUserAdapter(UserListDisplayActivity.this, chatUserArrayList);
                user_list_display.setAdapter(chatUserlistAdapter);
            }


        }

    }

    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);

    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }
}