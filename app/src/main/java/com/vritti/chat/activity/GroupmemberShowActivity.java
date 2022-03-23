package com.vritti.chat.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.chat.adapter.ChatGroupMemberlistAdapter;
import com.vritti.chat.bean.ChatUser;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class GroupmemberShowActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    SharedPreferences userpreferences;
    String Groupname, Starttime, Endtime, Status, Roomid, ChatSourceType = "1", ChatSourceId = "hfhsf2132322";
    String Call_Callid, Call_CallType, ChatRoomId, Creater, ChatType;
    RecyclerView list_group_member;
    //ChatGroupMemberlistAdapter chatUserlistAdapter;
    ChatGroupMemberlistAdapter chatUserlistAdapter;

    SQLiteDatabase sql;
    ArrayList<ChatUser> chatUserArrayList;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView txt_participants, txt_exit_chatroom, txt_title;
    RelativeLayout rlExit;
    private Menu menu;
    ImageView fab;
    static private boolean isShow = false;
    static private boolean flagIsinitialise;
    CoordinatorLayout mView;
    String ProjectMasterID;
    private String Participant, ParticipantId;

    ProgressDialog progressDialog;
    int pos;
    private JSONArray jResults;

    String Name="";
    private AlertDialog b;
    ProgressDialog pd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.vwb_scrolling);
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
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
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
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
        Starttime = df.format(c.getTime());


        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);


        list_group_member =  findViewById(R.id.list_group_member);
        txt_participants = (TextView) findViewById(R.id.txt_participants);
        txt_exit_chatroom = (TextView) findViewById(R.id.txt_exit_chatroom);
        rlExit =  findViewById(R.id.rlExit);
        mView = (CoordinatorLayout) findViewById(R.id.cocordinate_view);
        // collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText(Groupname.trim());
        //collapsingToolbarLayout.setTitle(Groupname);
        progressDialog = new ProgressDialog(GroupmemberShowActivity.this);

        //<<<--------------collapsing.....................

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Bundle args = new Bundle();
                args.putSerializable("userlist", (Serializable) chatUserArrayList);
                args.putString("projmasterId", ProjectMasterID);
                startActivity(new Intent(GroupmemberShowActivity.this, UserListDisplayActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("ChatRoomid", ChatRoomId).putExtra("chatroom_user", args).putExtra("Chatroomname", Groupname).putExtra("projmasterId", ProjectMasterID));
                finish();
            }
        });

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
      /*  mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
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
        });*/

        rlExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(GroupmemberShowActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm exit chat room...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want exit this conversation  room ?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.mipmap.ic_vwb);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (isnet()) {
                            new StartSession(GroupmemberShowActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    ChatRoomExitAsync chatRoomExitAsync = new ChatRoomExitAsync();
                                    chatRoomExitAsync.execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }


                            });
                        }


                        // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();



            }
        });

        if (cf.getChatRoomUsercount() > 0) {
            ChatUserUpdatList(ChatRoomId);
        } else {
            Refresh();
        }


    }

    public void chatuserdelete(int adapterPosition) {

        ParticipantId=chatUserArrayList.get(adapterPosition).getParticipantId();;
        ChatRoomId=chatUserArrayList.get(adapterPosition).getChatRoomId();


        if (isnet()) {
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.vwb_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            new StartSession(GroupmemberShowActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DeleteChatUSerDataJSON().execute();
                }


                @Override
                public void callfailMethod(String msg) {
                    progressDialog.dismiss();
                }
            });
        } else {
            Snackbar.make(mView, "No Internet Connetion", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    class DownloadChatUSerDataJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        private JSONObject obj;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.vwb_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getUsersinChatRoom + "?ChatRoomId=" + ChatRoomId;
            try {
                res = ut.OpenConnection(url, GroupmemberShowActivity.this);

                response =  String.valueOf(new JSONTokener(res.toString()).nextValue());

                try {
                    obj = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray();

                jResults = obj.getJSONArray("UsersinChatRoom");

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
                    String ChatRoomStatus = jorder.getString("ChatRoomStatus");
                    chatUser.setStatus(ChatRoomStatus);
                    chatUser.setStartTime(jorder.getString("StartTime"));
                    chatUser.setCreater(jorder.getString("Creator"));
                    chatUser.setChatSourceId(jorder.getString("ChatSourceId"));
                    String AddedBy = jorder.getString("AddedBy");
                    chatUser.setAddedBy(AddedBy);
                    String Id = jorder.getString("ParticipantId");
                    String ParticipantName = jorder.getString("ParticipantName");
                    chatUser.setParticipantName(ParticipantName);
                    chatUser.setParticipantId(Id);
                    chatUser.setMessage("");
                    chatUser.setUserMasterId(UserMasterId);
                    chatUser.setCount("0");
                    ChatRoomId = jorder.getString("ChatRoomId");
                    chatUser.setChatRoomId(ChatRoomId);
                    chatUser.setChatType(jorder.getString("ChatType"));
                    String ImagePath=jorder.getString("ImagePath");
                    chatUser.setImagePath(CompanyURL+ImagePath);
                    if (cf.CheckifRecordPresent(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, "ParticipantId", "ChatRoomId", chatUser.getParticipantId(), chatUser.getChatRoomId())) {
                        cf.AddGroupMember(chatUser);
                        chatUserArrayList.add(chatUser);
                    }


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
            progressDialog.dismiss();
            if (response != null) {
                progressDialog.dismiss();
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
                ParticipantId = cur.getString(cur.getColumnIndex("ParticipantId"));
                chatUser.setParticipantId(ParticipantId);
                Participant = cur.getString(cur.getColumnIndex("ParticipantName"));
                chatUser.setParticipantName(Participant);
                chatUser.setChatroom(cur.getString(cur.getColumnIndex("ChatRoomName")));
                chatUser.setCount(cur.getString(cur.getColumnIndex("Count")));
                Creater = cur.getString(cur.getColumnIndex("Creator"));
                chatUser.setCreater(Creater);
                ChatType = cur.getString(cur.getColumnIndex("ChatType"));
                chatUser.setChatType(ChatType);
                String ImagePath=cur.getString(cur.getColumnIndex("ImagePath"));
                chatUser.setImagePath(ImagePath);
                ChatRoomId=cur.getString(cur.getColumnIndex("ChatRoomId"));
                chatUser.setChatRoomId(ChatRoomId);
                String UserMasterId=cur.getString(cur.getColumnIndex("UserMasterId"));
                chatUser.setUserMasterId(UserMasterId);


                if (ChatType.equals("P")) {
                    fab.setVisibility(View.GONE);
                    rlExit.setVisibility(View.VISIBLE);

                } else {

                    if (Creater.equalsIgnoreCase(UserMasterId)) {
                        fab.setVisibility(View.VISIBLE);
                   /* showOption(R.id.action_info);
                    isShow = true;*/
                        rlExit.setVisibility(View.VISIBLE);
                    } else {
                        fab.setVisibility(View.INVISIBLE);
                   /* hideOption(R.id.action_info);
                    isShow = false;*/
                        rlExit.setVisibility(View.GONE);
                    }
                }

                if (Participant != null || ParticipantId != null) {

                    chatUserArrayList.add(chatUser);
                } else {

                }
            } while (cur.moveToNext());



            /*if (Participant==null){

            }*/


            String count = String.valueOf(chatUserArrayList.size());

            txt_participants.setText(count + " participants");
            //txt_title.setText(chatUserArrayList.get(0).getChatroom());
            // toolbar.setTitle(chatUserArrayList.get(0).getChatroom());
            chatUserlistAdapter = new ChatGroupMemberlistAdapter(GroupmemberShowActivity.this, chatUserArrayList);
            //  = new ChatGroupMemberlistAdapter(GroupmemberShowActivity.this, chatUserArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            list_group_member.setLayoutManager(mLayoutManager);
            list_group_member.setAdapter(chatUserlistAdapter);


            // Utility.setListViewHeightBasedOnItems(list_group_member);
            //chatUserlistAdapter.notifyDataSetChanged();


        } else {
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


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            if (integer.contains("ActivityId")) {


                Toast.makeText(GroupmemberShowActivity.this, "Chat Room exit successfully", Toast.LENGTH_LONG).show();
                SQLiteDatabase sql1 = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("ChatRoomStatus", "Closed");
                sql1.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatRoomId=?", new String[]{ChatRoomId});
                sql1.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                //Intent intent = new Intent(GroupmemberShowActivity.this, OpenChatroomActivity.class);
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
        // hideOption(R.id.action_info);

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
        }
        if (id == R.id.action_settings) {

            CreateAttendancedialog();


            return true;
        }

        /* else if (id == R.id.action_info) {
            Bundle args = new Bundle();
            args.putSerializable("userlist", (Serializable) chatUserArrayList);
            args.putString("projmasterId", ProjectMasterID);

            startActivity(new Intent(GroupmemberShowActivity.this, UserListDisplayActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("ChatRoomid", ChatRoomId).putExtra("chatroom_user", args).putExtra("Chatroomname", Groupname).putExtra("projmasterId", ProjectMasterID));
            finish();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
/*
    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }*/

    public void Refresh() {
        if (isnet()) {
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.vwb_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            new StartSession(GroupmemberShowActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadChatUSerDataJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    progressDialog.dismiss();
                }
            });
        } else {
            Snackbar.make(mView, "No Internet Connetion", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }


    class DeleteChatUSerDataJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.vwb_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_ChatRoomUserDelete + "?RoomId=" + ChatRoomId +"&UsermasterId="+ParticipantId;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                jResults = new JSONArray(response);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();

            if (integer!=null){
                try {
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);

                        ChatRoomId=jorder.getString("ChatRoomId");
                        ParticipantId=jorder.getString("UserMasterId");


                    }
                    SQLiteDatabase sql = db.getWritableDatabase();
                    sql.delete(db.TABLE_CHAT_CHATROOM_MEMBER_LIST,"ChatRoomId=? and ParticipantId=?",new String[]{ChatRoomId,ParticipantId});

                    //cf.deletechatuser(ChatRoomId, ParticipantId);
                    if (cf.getChatRoomUsercount() > 0) {
                        ChatUserUpdatList(ChatRoomId);
                    }
                }catch (Exception e){
                    e.printStackTrace();

                }
            }else if(integer.equals("[]")){

            }


        }

    }


    public void CreateAttendancedialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GroupmemberShowActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.vwb_dialog_chat_edit, null);
        dialogBuilder.setView(dialogView);
        /*final Dialog dialog = new Dialog(ActivityDetailsActivity.this);
        dialog.setContentView(R.layout.vwb_dialog_reschedule);
        dialog.setTitle("Select New End Date");*/
        Button btn_ok = (Button) dialogView.findViewById(R.id.btn_ok);
        final EditText edt_remark = (EditText) dialogView.findViewById(R.id.edt_remark);

        Name=txt_title.getText().toString();
        edt_remark.setText(Name);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name=edt_remark.getText().toString();
                if (Name.equals("")){
                    Toast.makeText(GroupmemberShowActivity.this,"Please enter chatroom name",Toast.LENGTH_LONG).show();
                }else {

                    JSONObject jsonObject = new JSONObject();


                    try {
                        jsonObject.put("ChatRoomName", Name);
                        jsonObject.put("ChatRoomId", ChatRoomId);
                        jsonObject.put("OldChatRoomName", Groupname);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    final String FinalJson = jsonObject.toString();

                    if (isnet()) {
                        progressDialog.setCancelable(true);
                        if (!isFinishing()) {
                            progressDialog.show();
                        }
                        progressDialog.setContentView(R.layout.vwb_progress_lay);
                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        new StartSession(GroupmemberShowActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                new PostEditChatRoomJSON().execute(FinalJson);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
                    }


                }

            }
        });


        b = dialogBuilder.create();
        b.show();

    }

    class PostEditChatRoomJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                progressDialog.setCancelable(true);
                if (!isFinishing()) {
                    progressDialog.show();
                }
                progressDialog.setContentView(R.layout.vwb_progress_lay);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_EditChatRoomName;
            System.out.println("AdvanceProvisionalRecieptjson-1 :" + url);

            try {
                res = ut.OpenPostConnection(url, params[0], getApplicationContext());
                System.out.println("BusinessAPI-2 :" + res);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

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
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    ChatRoomId=jsonObject.getString("ChatRoomId");
                    Groupname=jsonObject.getString("ChatRoomName");
                    txt_title.setText(Groupname);
                    ContentValues values = new ContentValues();
                    values.put("ChatRoomName", Groupname);
                    values.put("ChatRoomId", ChatRoomId);
                    sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});

                    b.dismiss();


                    Toast.makeText(GroupmemberShowActivity.this, "Chat room name change successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GroupmemberShowActivity.this,AddChatRoomActivity.class)
                            .putExtra("Chatroomname",Groupname).putExtra("ChatRoomid",ChatRoomId).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(GroupmemberShowActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
            }


        }

    }


}
