package com.vritti.vwblib.chat;

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
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.vritti.vwblib.R;


public class GroupmemberShowActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    SharedPreferences userpreferences;
    String  Groupname, Starttime, Endtime, Status, Roomid,  ChatSourceType = "1", ChatSourceId = "hfhsf2132322";
    String Call_Callid, Call_CallType, ChatRoomId, Creater;
    ListView list_group_member;
    ChatGroupMemberlistAdapter chatUserlistAdapter;

    SQLiteDatabase sql;
    ArrayList<ChatUser> chatUserArrayList;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView txt_participants, txt_exit_chatroom,txt_title;
    private Menu menu;
    FloatingActionButton fab;
    static private boolean isShow = false;
    static private boolean flagIsinitialise;
    CoordinatorLayout mView;
    String ProjectMasterID;
    private String Participant,ParticipantId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_scrolling);
        flagIsinitialise = false;
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

    }

    private void init() {
        Intent intent = getIntent();
        Call_Callid = intent.getStringExtra("callid");
        Call_CallType = intent.getStringExtra("call_type");
        ChatRoomId = intent.getStringExtra("ChatRoomid");
        Groupname = intent.getStringExtra("Chatroomname");
        ProjectMasterID = intent.getStringExtra("projmasterId");


        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
        Starttime = df.format(c.getTime());


        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        UserMasterId = userpreferences.getString("UserMasterId", "");
        UserName = userpreferences.getString("UserName", "");
        CompanyURL = userpreferences.getString("CompanyURL", null);


        list_group_member = (ListView) findViewById(R.id.list_group_member);
        txt_participants = (TextView) findViewById(R.id.txt_participants);
        txt_exit_chatroom = (TextView) findViewById(R.id.txt_exit_chatroom);
        mView = (CoordinatorLayout) findViewById(R.id.cocordinate_view);
        collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        txt_title= (TextView) findViewById(R.id.txt_title);
        txt_title.setText(Groupname);
        collapsingToolbarLayout.setTitle(Groupname);


        //<<<--------------collapsing.....................

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Bundle args = new Bundle();
                args.putSerializable("userlist", (Serializable) chatUserArrayList);
                args.putString("projmasterId",ProjectMasterID);
                startActivity(new Intent(GroupmemberShowActivity.this, UserListDisplayActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("ChatRoomid", ChatRoomId).putExtra("chatroom_user", args).putExtra("Chatroomname", Groupname).putExtra("projmasterId",ProjectMasterID));
                finish();
            }
        });

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (fab.getVisibility() == View.VISIBLE) {
                        isShow = true;
                        showOption(R.id.action_info);
                    } else {
                        isShow = false;
                        hideOption(R.id.action_info);
                    }

                } else if (isShow) {
                    isShow = false;
                    if (flagIsinitialise) {
                        hideOption(R.id.action_info);
                    }
                }
            }
        });

        txt_exit_chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatRoomExitAsync chatRoomExitAsync = new ChatRoomExitAsync();
                chatRoomExitAsync.execute();

            }
        });

        if (cf.getChatRoomUsercount()>0) {
            ChatUserUpdatList(ChatRoomId);
        }else {
            Refresh();
        }


    }

    class DownloadChatUSerDataJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
         /*   progressDialog = new ProgressDialog(GroupmemberShowActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.vwb_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getUsersinChatRoom + "?ChatRoomId=" + ChatRoomId;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, "ChatRoomId=?",
                        new String[]{ChatRoomId});
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                       ChatUser chatUser = new ChatUser();
                        String Chatroomname = jorder.getString("ChatRoomName");
                        chatUser.setChatroom(Chatroomname);
                        String ChatRoomStatus=jorder.getString("ChatRoomStatus");
                        chatUser.setStatus(ChatRoomStatus);
                        chatUser.setStartTime(jorder.getString("StartTime"));
                        chatUser.setCreater(jorder.getString("Creator"));
                        chatUser.setChatSourceId(jorder.getString("ChatSourceId"));
                        String AddedBy=jorder.getString("AddedBy");
                        chatUser.setAddedBy(AddedBy);
                        String Id = jorder.getString("ParticipantId");
                        String ParticipantName = jorder.getString("ParticipantName");
                        chatUser.setParticipantName(ParticipantName);
                        chatUser.setParticipantId(Id);
                        chatUser.setMessage("");
                        chatUser.setUserMasterId(UserMasterId);
                        chatUser.setCount("0");
                        ChatRoomId=jorder.getString("ChatRoomId");
                        chatUser.setChatRoomId(ChatRoomId);
                        cf.AddGroupMember(chatUser);
                        chatUserArrayList.add(chatUser);




                   // long a = sql.insert(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null, values);
                    //Log.e("",""+a);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
           // progressDialog.dismiss();
            if (response != null) {
             //   progressDialog.dismiss();
                ChatUserUpdatList(ChatRoomId);
            }


        }

    }

    private void ChatUserUpdatList(String ChatRoomid) {
        chatUserArrayList.clear();
        String query = "SELECT * FROM " + db.TABLE_CHAT_CHATROOM_MEMBER_LIST + " WHERE  ChatRoomId ='" + ChatRoomid + "' order by ParticipantName asc";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ChatUser chatUser = new ChatUser();
                ParticipantId=cur.getString(cur.getColumnIndex("ParticipantId"));
                chatUser.setParticipantId(ParticipantId);
                Participant=cur.getString(cur.getColumnIndex("ParticipantName"));
                chatUser.setParticipantName(Participant);
                chatUser.setChatroom(cur.getString(cur.getColumnIndex("ChatRoomName")));
                chatUser.setCount(cur.getString(cur.getColumnIndex("Count")));
                Creater = cur.getString(cur.getColumnIndex("Creator"));
                chatUser.setCreater(Creater);
                    if (Creater.equalsIgnoreCase(UserMasterId)) {
                        fab.setVisibility(View.VISIBLE);
                   /* showOption(R.id.action_info);
                    isShow = true;*/
                        txt_exit_chatroom.setVisibility(View.VISIBLE);
                    } else {
                        fab.setVisibility(View.INVISIBLE);
                   /* hideOption(R.id.action_info);
                    isShow = false;*/
                        txt_exit_chatroom.setVisibility(View.GONE);
                    }


                 if (Participant!=null||ParticipantId!=null) {

                     chatUserArrayList.add(chatUser);
                 }else {

                 }
            } while (cur.moveToNext());



            /*if (Participant==null){

            }*/


            String count = String.valueOf(chatUserArrayList.size());

            txt_participants.setText(count + " participants");
            //txt_title.setText(chatUserArrayList.get(0).getChatroom());
            // toolbar.setTitle(chatUserArrayList.get(0).getChatroom());
            chatUserlistAdapter = new ChatGroupMemberlistAdapter(GroupmemberShowActivity.this, chatUserArrayList);
            list_group_member.setAdapter(chatUserlistAdapter);
            Utility.setListViewHeightBasedOnItems(list_group_member);
            chatUserlistAdapter.notifyDataSetChanged();


        }else {
            Refresh();
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
        GroupmemberShowActivity.this.finish();
    }


    class ChatRoomExitAsync extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(GroupmemberShowActivity.this);
            if (!isFinishing()) {
                progressDialog.setCancelable(true);
                progressDialog.show();
                progressDialog.setContentView(R.layout.vwb_progress_lay);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
            //  showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetExitChatRoom + "?ChatRoomId=" + ChatRoomId;
                System.out.println("AddGroup" + url);
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
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

            if (integer.equalsIgnoreCase("Success")) {

                Toast.makeText(GroupmemberShowActivity.this, "Chat Room exit successfully", Toast.LENGTH_LONG).show();
                SQLiteDatabase sql1 = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("ChatRoomStatus", "Closed");
                sql1.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                Intent intent = new Intent(GroupmemberShowActivity.this, OpenChatroomActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            } else {


            }
            progressDialog.dismiss();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        flagIsinitialise = true;
        hideOption(R.id.action_info);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ref) {
            sql.delete(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, "ChatRoomId=?",
                    new String[]{ChatRoomId});
            Refresh();
            return true;
        } else if (id == R.id.action_info) {
            Bundle args = new Bundle();
            args.putSerializable("userlist", (Serializable) chatUserArrayList);
            args.putString("projmasterId",ProjectMasterID);

            startActivity(new Intent(GroupmemberShowActivity.this, UserListDisplayActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("ChatRoomid", ChatRoomId).putExtra("chatroom_user", args).putExtra("Chatroomname", Groupname).putExtra("projmasterId",ProjectMasterID));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
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
    public void Refresh(){
        if (isnet()) {
            new StartSession(GroupmemberShowActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadChatUSerDataJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }else {
            Snackbar.make(mView, "No Internet Connetion", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();        }
    }
}

