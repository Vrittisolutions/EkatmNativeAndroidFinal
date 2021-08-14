package com.vritti.chat.activity;

import android.app.NotificationManager;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.chat.adapter.ChatRoomMultipleAdapterNewRecycleView;
import com.vritti.chat.bean.ChatGroup;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenChatroomActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    static Context context;

    @BindView(R.id.listview_multiple_group)
    RecyclerView listview_multiple_group;

    //ChatRoomMultipleAdapter chatRoomMultipleAdapter;
    ChatRoomMultipleAdapterNewRecycleView chatRoomMultipleAdapterNewRecycleView;
    ArrayList<ChatGroup> chatRoomDisplayArrayList;
    ArrayList<ChatGroup> searchList;
    @BindView(R.id.edt_search_chatroomname)
    EditText edt_search_chatroomname;

    SharedPreferences userpreferences;
    String Starttime, ChatRoomId, Call_Callid, Call_CallType, ChatRoomName, ActivityDescription = "";
    SQLiteDatabase sql;

    @BindView(R.id.txt_chatroom_add)
    TextView txt_chatroom_add;
    private String path = "", Imagefilename = "", ChatMessage = "";
    ProgressBar mprogress;
    int selectedPos = -1;
    String selectedId = "-1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_open_chat__listview_lay_new);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(" Conversation");
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
        sql = db.getWritableDatabase();
        mprogress = findViewById(R.id.toolbar_progress_App_bar);
        chatRoomDisplayArrayList = new ArrayList<>();
        searchList = new ArrayList<>();
        chatRoomMultipleAdapterNewRecycleView = new ChatRoomMultipleAdapterNewRecycleView(OpenChatroomActivity.this, chatRoomDisplayArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listview_multiple_group.setLayoutManager(mLayoutManager);
        listview_multiple_group.setAdapter(chatRoomMultipleAdapterNewRecycleView);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        Call_Callid = intent.getStringExtra("callid");
        Call_CallType = intent.getStringExtra("call_type");
        ChatRoomId = intent.getStringExtra("ChatRoomid");

        intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();


        // if this is from the share menu
        if (getIntent().hasExtra("Description") || getIntent().hasExtra("Imagename") || getIntent().hasExtra("ImagePath")) {
            intent = getIntent();
            ActivityDescription = intent.getStringExtra("Description");
            Imagefilename = intent.getStringExtra("Imagename");
            path = intent.getStringExtra("ImagePath");
        }

        if (cf.getChatRoomNamecount() > 0) {
            ChatRoomNameUpdatList(UserMasterId);
        } else {
            if (isnet()) {
                showProgress();
                new StartSession(OpenChatroomActivity.this, new CallbackInterface() {
                    @Override

                    public void callMethod() {
                        new DownloadChatRoomDisplayDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        dismissProgress();
                    }
                });
            }
        }

       /* listview_multiple_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



            }
        });*/

        edt_search_chatroomname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable text) {
                // TODO Auto-generated method stub

                String text1 = edt_search_chatroomname.getText().toString().toLowerCase(Locale.getDefault());
               // chatRoomMultipleAdapter.filter(text1);
                //chatUserlistAdapter.filter(username);
                filter(text1);
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

                //String name = edt_search_chatroomname.getText().toString();

               /* if (name.length() > 0) {
                    getchatroomsearch(name);
                }
                else {
                  //  Toast.makeText(getApplicationContext(), "Please enter atleast two charactor to customize your search", Toast.LENGTH_LONG).show();

                }
*/


            }
        });
    }
    private void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        chatRoomDisplayArrayList.clear();
        if (charText.length() == 0) {
            chatRoomDisplayArrayList.addAll(searchList);
        }
        else
        {
            for (ChatGroup wp : searchList)
            {
                if (wp.getChatroom().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    chatRoomDisplayArrayList.add(wp);
                }
            }
        }
        chatRoomMultipleAdapterNewRecycleView.notifyDataSetChanged();
        //return chatRoomDisplayArrayList;
    }

    private void ChatRoomNameUpdatList(String userMasterId) {
        chatRoomDisplayArrayList.clear();

        sql = db.getWritableDatabase();


        ////////////////////////////////////////////////
        //String query = "SELECT  distinct ChatRoomName FROM " + db.TABLE_CHAT_CHATROOM_MEMBER_LIST +" WHERE  UserMasterId ='" + userMasterId + "'";;
        String query = "SELECT Distinct ChatRoomId,AddedBy,StartTime,ChatRoomName,ChatRoomStatus,Creator,Count,ChatSourceId,ChatType,ChatMessage FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST + " WHERE  UserMasterId ='" + userMasterId + "'order by StartTime desc ";
        ;
        Cursor cur = sql.rawQuery(query, null);
        String count = String.valueOf(cur.getCount());
        if (cur.getCount() > 0) {
            cur.moveToFirst();                           // chatUser.setChatMessage("");

            do {
                ChatGroup chatGroup = new ChatGroup();
                chatGroup.setChatRoomId(cur.getString(cur.getColumnIndex("ChatRoomId")));
                String AddedBy = cur.getString(cur.getColumnIndex("AddedBy"));
                chatGroup.setAddedBy(AddedBy);
                chatGroup.setStartTime(cur.getString(cur.getColumnIndex("StartTime")));
                String ChatRoomName = cur.getString(cur.getColumnIndex("ChatRoomName"));
                chatGroup.setChatroom(ChatRoomName);
                chatGroup.setStatus(cur.getString(cur.getColumnIndex("ChatRoomStatus")));
                chatGroup.setCreater(cur.getString(cur.getColumnIndex("Creator")));
                chatGroup.setCount(cur.getString(cur.getColumnIndex("Count")));
                chatGroup.setChatSourceId(cur.getString(cur.getColumnIndex("ChatSourceId")));
                String ChatRoomStatus = cur.getString(cur.getColumnIndex("ChatType"));
                chatGroup.setChatType(ChatRoomStatus);
                ChatMessage = cur.getString(cur.getColumnIndex("ChatMessage"));
                chatGroup.setChatMessage(ChatMessage);

                if (ChatRoomStatus.equals("Closed")) {

                } else {
                    chatRoomDisplayArrayList.add(chatGroup);
                }
            } while (cur.moveToNext());


            chatRoomMultipleAdapterNewRecycleView.notifyDataSetChanged();
            searchList.clear();
            searchList.addAll(chatRoomDisplayArrayList);
            if(selectedPos != -1) {
                if (changePos()) {
                    listview_multiple_group.scrollToPosition(0);
                }
            }

        /*    chatRoomMultipleAdapter = new ChatRoomMultipleAdapter(OpenChatRoomNew.this, chatRoomDisplayArrayList);
            listview_multiple_group.setAdapter(chatRoomMultipleAdapter);
            listview_multiple_group.setSelection(AppCommon.getInstance(OpenChatRoomNew.this).getChatPostion());
            chatRoomMultipleAdapter.notifyDataSetChanged();

           */

            setNotifiCationCount(chatRoomDisplayArrayList);

        } else {
            txt_chatroom_add.setVisibility(View.VISIBLE);
        }


    }

    private boolean changePos() {
        boolean flag = false;
        for(int i = 0 ; i<chatRoomDisplayArrayList.size(); i++){
            if(selectedId.equals(chatRoomDisplayArrayList.get(i).getChatRoomId())){
                if(selectedPos != i){
                    flag = true;
                }else
                    flag = false;
            }
        }
        return flag;
    }

    private void setNotifiCationCount(ArrayList<ChatGroup> chatRoomDisplayArrayList) {
        int tempCount = 0;
        for (ChatGroup chatGroup : chatRoomDisplayArrayList) {
            tempCount = Integer.parseInt(chatGroup.getCount()) + tempCount;
        }
        AppCommon.getInstance(this).setNotificationCount(tempCount);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.open_chat_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

        }

        if (item.getItemId() == R.id.refresh1) {

            if (isnet()) {
                showProgress();
                new StartSession(OpenChatroomActivity.this, new CallbackInterface() {
                    @Override

                    public void callMethod() {
                        new DownloadChatRoomDisplayDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        dismissProgress();
                    }
                });
            }
            return true;
        } else if (item.getItemId() == R.id.private_chat) {


            Intent intent = new Intent(OpenChatroomActivity.this, PrivateChatActvity.class);
            intent.putExtra("callid", "");
            intent.putExtra("call_type", "");
            intent.putExtra("status", "");
            startActivityForResult(intent, 900);

            return true;
        } else if (item.getItemId() == R.id.group_chat) {


            Intent intent = new Intent(OpenChatroomActivity.this, AddGroupActivity.class);
            intent.putExtra("callid", "");
            intent.putExtra("call_type", "");
            intent.putExtra("status", "");
            startActivityForResult(intent, 900);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);

    }
    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }

    public void setOnClickRow(int i) {
        sql = db.getWritableDatabase();
        ChatRoomId = chatRoomDisplayArrayList.get(i).getChatRoomId();
        ChatRoomName = chatRoomDisplayArrayList.get(i).getChatroom();
        String Chat_status = chatRoomDisplayArrayList.get(i).getStatus();
        selectedPos = i;
        selectedId = chatRoomDisplayArrayList.get(i).getChatRoomId();
        AppCommon.getInstance(OpenChatroomActivity.this).setUnReadCount(chatRoomDisplayArrayList.get(i).getCount());
        Intent intent = new Intent(OpenChatroomActivity.this, AddChatRoomActivity.class);
        intent.putExtra("callid", Call_Callid);
        intent.putExtra("call_type", Call_CallType);
        intent.putExtra("ChatRoomid", ChatRoomId);
        intent.putExtra("Chatroomname", ChatRoomName);
        intent.putExtra("value_chat", "1");
        intent.putExtra("status", Chat_status);
        intent.putExtra("share_image_path", path);
        intent.putExtra("share_imagename", Imagefilename);
        intent.putExtra("share_description", ActivityDescription);
        startActivityForResult(intent , 900);
        ContentValues values = new ContentValues();
        values.put("Count", "0");
        sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
        // finish();
        ActivityDescription = "";
        Imagefilename = "";
        path = "";
        AppCommon.getInstance(OpenChatroomActivity.this).setChatPostion(i);
    }

    class DownloadChatRoomDisplayDataJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;
        private JSONObject obj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress();

        }

        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebUrlClass.api_GetRefreshChatRoom + "?ApplicationCode="+WebUrlClass.AppNameFCM;

            String url = CompanyURL + WebUrlClass.api_GetRefreshChatRoom + "?ApplicationCode=" + SetAppName.AppNameFCM + "&UserMasterId=" + UserMasterId;

            try {
                res = ut.OpenConnection(url, OpenChatroomActivity.this);
                //String response1 = response.substring(1, response.length() - 1);

                response =  String.valueOf(new JSONTokener(res.toString()).nextValue());

               /* try {
                    obj = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

               chatRoomDisplayArrayList.clear();


                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(response);

                   // jResults = obj.getJSONArray("RefreshChatRoom");




                sql.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, null,
                        null);


                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {

                    JSONObject Jsonchatmember = jResults.getJSONObject(i);
                    ChatGroup chatUser = new ChatGroup();
                    String Chatroomname = Jsonchatmember.getString("ChatRoomName");
                    chatUser.setChatroom(Chatroomname);
                    ChatRoomId = Jsonchatmember.getString("ChatRoomId");
                    chatUser.setChatRoomId(ChatRoomId);
                    String ChatRoomStatus = Jsonchatmember.getString("ChatRoomStatus");
                    chatUser.setStatus(ChatRoomStatus);
                    String StartTime = Jsonchatmember.getString("StartTime");
                    chatUser.setStartTime(StartTime);
                    chatUser.setAddedBy(Jsonchatmember.getString("AddedBy"));
                    chatUser.setCreater(Jsonchatmember.getString("Creator"));
                    chatUser.setUserMasterId(UserMasterId);
                    chatUser.setCount("0");
                    chatUser.setChatSourceId(Jsonchatmember.getString("ChatSourceId"));
                    chatUser.setChatType(Jsonchatmember.getString("ChatType"));
                    chatUser.setChatMessage("");

                    if (ChatRoomStatus.equals("Closed")) {

                    } else {
                        cf.AddGroupList(chatUser);
                        chatRoomDisplayArrayList.add(chatUser);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgress();
            // progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")) {

                txt_chatroom_add.setVisibility(View.VISIBLE);

            } else {
                if (response != null) {
                    txt_chatroom_add.setVisibility(View.GONE);
                    dismissProgress();
                    ChatRoomNameUpdatList(UserMasterId);
                }
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 900 && resultCode == 267){
            if (data.getStringExtra("Description") != null) {
                // intent = getIntent();
                ActivityDescription = data.getStringExtra("Description");
                //Imagefilename = data.getStringExtra("Imagename");
                //path = data.getStringExtra("ImagePath");
            }
        }else if(requestCode == 900){
            ut = new Utility();
            cf = new CommonFunction(context);
            String settingKey = ut.getSharedPreference_SettingKey(context);
            String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
            db = new DatabaseHandlers(context, dabasename);
            UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
            UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
            if (cf.getChatRoomNamecount() > 0) {
                ChatRoomNameUpdatList(UserMasterId);
            }
            NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancelAll();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* if (AppCommon.getInstance(context).getNotificationFlag())
            startActivity(new Intent(OpenChatroomActivity.this, ActivityMain.class));
        AppCommon.getInstance(context).setNotificationFlag(false);*/
        OpenChatroomActivity.this.finish();

    }
}
