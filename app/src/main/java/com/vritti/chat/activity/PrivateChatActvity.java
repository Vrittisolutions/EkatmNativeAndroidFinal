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
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vritti.chat.adapter.PrivateChatAdapter;
import com.vritti.chat.bean.ChatGroup;
import com.vritti.chat.bean.ChatUser;
import com.vritti.chat.bean.DefaultUser;
import com.vritti.chat.bean.PrivateUser;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sharvari on 20-Dec-18.
 */

public class PrivateChatActvity extends AppCompatActivity {


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", cStatus = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    RecyclerView user_list_display;
    PrivateChatAdapter privateChatAdapter;
    SQLiteDatabase sql;
    ArrayList<PrivateUser> privateArrayList;
    ArrayList<PrivateUser> tempList = new ArrayList<>();
    EditText edt_search_name;
    SharedPreferences userpreferences;
    String Starttime, username = "", ChatRoomId, UserName1, UserMasterId1, ChatMessage, ChatRoomName, Call_Callid = "";
    ImageView img_adduser;
    ChatUser chatUser;
    String[] user;
    ArrayList<ChatUser> privateArrayList1;
    Intent intent;
    String ProjectMasterID = "";
    ProgressDialog progressDialog;
    private String dotformat = "No";
    String ChatType = "P";
    private ArrayList<DefaultUser> defaultUserArrayList;
    private String OtherUsername, OtherUserId;
    DefaultUser defaultUser;
    ArrayList<ChatUser> chatUserArrayList;
    ProgressBar mprogress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_privatechat_list_lay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(" Add Participant");
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        setSupportActionBar(toolbar);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
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
        defaultUserArrayList = new ArrayList<>();
        chatUserArrayList = new ArrayList<>();


        Intent intent = getIntent();

        if (intent.hasExtra("status")) {
            cStatus = intent.getStringExtra("status");
        }


        user_list_display = findViewById(R.id.user_list_display);
        mprogress = findViewById(R.id.toolbar_progress_App_bar);

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

        edt_search_name = (EditText) findViewById(R.id.edt_search_name);
        img_adduser = (ImageView) findViewById(R.id.img_adduser);


        privateArrayList = new ArrayList<>();
        privateChatAdapter = new PrivateChatAdapter(PrivateChatActvity.this, privateArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        user_list_display.setLayoutManager(mLayoutManager);
        user_list_display.setAdapter(privateChatAdapter);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PrivateChatActvity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("PrivateUser", "");
        Type type = new TypeToken<List<PrivateUser>>() {
        }.getType();
        if (gson.fromJson(json, type) != null) {
            privateArrayList = gson.fromJson(json, type);
            tempList.addAll(privateArrayList);
        }

        if (privateArrayList.size() == 0) {
            if (isnet()) {
                new StartSession(PrivateChatActvity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadChatUSerDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (privateArrayList.size() > 0) {
                privateChatAdapter = new PrivateChatAdapter(PrivateChatActvity.this, privateArrayList);
                user_list_display.setAdapter(privateChatAdapter);
            }

        }


        edt_search_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = edt_search_name.getText().toString().toLowerCase(Locale.getDefault());
               filter(text);
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
            }
        });



       /* user_list_display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {*/


        img_adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public void filter(String charText) {
        // public ArrayList<PrivateUser> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        privateArrayList.clear();
        if (charText.length() == 0) {
            privateArrayList.addAll(tempList);
        } else {
            for (PrivateUser wp : tempList) {
                if (wp.getUsername().toLowerCase(Locale.getDefault()).contains(charText)) {
                    privateArrayList.add(wp);
                }
            }
        }
        privateChatAdapter = new PrivateChatAdapter(PrivateChatActvity.this, privateArrayList);
        user_list_display.setAdapter(privateChatAdapter);
        //    return privateArrayList;


    }



    public void rowMessage(int position) {
        defaultUserArrayList.clear();

        OtherUsername = privateArrayList.get(position).getUsername();
        OtherUserId = privateArrayList.get(position).getUserMasterId();

        defaultUserArrayList.add(new DefaultUser(OtherUsername, OtherUserId));
        defaultUserArrayList.add(new DefaultUser(UserName, UserMasterId));

        JSONObject obj = new JSONObject();
        JSONObject jsonobj = new JSONObject();

        try {
            if (defaultUserArrayList.size() > 0) {

                user = new String[defaultUserArrayList.size()];
                for (int i = 0; i < defaultUserArrayList.size(); i++) {
                    String UserMasterId = defaultUserArrayList.get(i).getUserMasterId();
                    String UserName = defaultUserArrayList.get(i).getUsername();
                    obj.put("UserId", UserMasterId);
                    obj.put("Username", UserName);


                    user[i] = obj.toString();
                }


                JSONArray obj1 = new JSONArray();


                for (int i = 0; i < user.length; i++) {
                    JSONObject a1 = new JSONObject(user[i]);
                    obj1.put(a1);

                }
                jsonobj.put("AddUser", obj1);
                jsonobj.put("Start_time", Starttime);
                jsonobj.put("Added_by", UserName);
                jsonobj.put("Modified_by", UserName);
                jsonobj.put("ChatRoomName", OtherUsername);
                jsonobj.put("InitiatorId", UserMasterId);
                jsonobj.put("EndTime", "2020/10/30");
                jsonobj.put("ChatSourceType", "Private");
                jsonobj.put("ChatSourceId", Call_Callid);
                jsonobj.put("AppCode", SetAppName.AppNameFCM);
                jsonobj.put("ChatType", ChatType);


                final String FinalJson = jsonobj.toString();
                Log.d("Private", FinalJson);


              /*  String query = "SELECT ChatRoomName" +
                        " FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST + " WHERE  ChatRoomName like '%" + OtherUsername + "%'";*/

                    // update by nilesh
                String query = "SELECT ChatRoomName FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST + " WHERE  ChatRoomName = '" + OtherUsername +"'" ;

                Cursor cur = sql.rawQuery(query, null);
                String count = String.valueOf(cur.getCount());
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {

                        Toast.makeText(PrivateChatActvity.this, "Already chatroom available can't create same chatroom", Toast.LENGTH_SHORT).show();

                    } while (cur.moveToNext());
                } else {

                    // Toast.makeText(OpenChatroomActivity.this,"Chatroom name not found",Toast.LENGTH_SHORT).show();


                    if (isnet()) {
                        new StartSession(PrivateChatActvity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                new PostCreateChatRoomJSON().execute(FinalJson);
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
        } catch (Exception e) {
            e.printStackTrace();
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
        PrivateChatActvity.this.finish();
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
            SharedPreferences.Editor editor = userpreferences.edit();
            editor.remove("PrivateUser");
            editor.commit();
            if (isnet()) {
                new StartSession(PrivateChatActvity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadChatUSerDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }

        return (super.onOptionsItemSelected(menuItem));

    }


    class DownloadChatUSerDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showProgress();
           /* try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(PrivateChatActvity.this);
                    progressDialog.setMessage("Loading. Please wait...");
                    progressDialog.setIndeterminate(false);
                    //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //  progressDialog.setContentView(R.layout.vwb_progress_lay);
                    progressDialog.setCancelable(true);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }*/

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

                       // response = response.substring(1, response.length() - 1);
                        //ContentValues values = new ContentValues();

                        privateArrayList.clear();
                        JSONArray jResults = new JSONArray(response);

                        for (int i = 0; i < jResults.length(); i++) {
                            PrivateUser userList = new PrivateUser();
                            JSONObject jorder = jResults.getJSONObject(i);

                            userList.setUserMasterId(jorder.getString("UserMasterId"));
                            userList.setUsername(jorder.getString("UserName"));
                            privateArrayList.add(userList);


                        }


                   /* sql.delete(db.TABLE_CHAT_USER_LIST, null,
                            null);*/

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
            dismissProgress();
            /*if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
            if (response.contains("[]")) {
                // ChatUserUpdatList(username);

            } else {
                dismissProgress();
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PrivateChatActvity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(privateArrayList);
                editor.putString("PrivateUser", json);
                editor.commit();
                privateChatAdapter = new PrivateChatAdapter(PrivateChatActvity.this, privateArrayList);
                user_list_display.setAdapter(privateChatAdapter);
                tempList.addAll(privateArrayList);
            }


        }

    }

    class PostCreateChatRoomJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        private JSONObject obj;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(PrivateChatActvity.this);
                    progressDialog.setMessage("Loading please wait...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(true);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetCreateChatRoom;
            System.out.println("AdvanceProvisionalRecieptjson-1 :" + url);

            try {
               /* res = ut.OpenPostConnection(url, params[0], getApplicationContext());
                System.out.println("BusinessAPI-2 :" + res);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
*/
                res = ut.OpenPostConnection(url, params[0],PrivateChatActvity.this);

                response =  String.valueOf(new JSONTokener(res.toString()).nextValue());

               /* try {
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
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (response != null) {
                JSONArray jResults = new JSONArray();
                try {
                   // jResults = obj.getJSONArray("CreateChatRoom");
                    jResults = new JSONArray(response);

                    ContentValues values = new ContentValues();
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
                            String StartTime = Jsonchatmember1.getString("StartTime");
                            chatUser.setStartTime(StartTime);
                            chatUser.setCreater(Jsonchatmember1.getString("Creator"));
                            chatUser.setChatSourceId(Jsonchatmember1.getString("ChatSourceId"));
                            chatUser.setAddedBy(Jsonchatmember1.getString("AddedBy"));
                            chatUser.setParticipantName(Jsonchatmember1.getString("ParticipantName"));
                            chatUser.setParticipantId(Jsonchatmember1.getString("ParticipantId"));
                            ChatMessage = Jsonchatmember1.getString("Message");
                            chatUser.setMessage(ChatMessage);
                            chatUser.setUserMasterId(UserMasterId);
                            chatUser.setCount("0");
                            chatUser.setChatType("P");
                            cf.AddGroupMember(chatUser);
                            chatUserArrayList.add(chatUser);

                            ChatGroup chatGroup = new ChatGroup();
                            chatGroup.setChatroom(OtherUsername);
                            chatGroup.setChatRoomId(ChatRoomId);
                            chatGroup.setStatus(Jsonchatmember1.getString("ChatRoomStatus"));
                            chatGroup.setStartTime(StartTime);
                            chatGroup.setCreater(Jsonchatmember1.getString("Creator"));
                            chatGroup.setAddedBy(Jsonchatmember1.getString("AddedBy"));
                            chatGroup.setUserMasterId(UserMasterId);
                            chatGroup.setCount("0");
                            chatGroup.setChatSourceId(Jsonchatmember1.getString("ChatSourceId"));
                            chatGroup.setChatType("P");
                            chatGroup.setChatMessage("");
                            cf.AddGroupList(chatGroup);


                        }
                        Toast.makeText(PrivateChatActvity.this, "Private chat created successfully", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(PrivateChatActvity.this, AddChatRoomActivity.class);
                        intent.putExtra("ChatRoomid", ChatRoomId);
                        intent.putExtra("Chatroomname", OtherUsername);
                        intent.putExtra("status", cStatus);
                        intent.putExtra("projmasterId", ProjectMasterID);
                        startActivity(intent);
                        finish();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(PrivateChatActvity.this, "Please try again", Toast.LENGTH_SHORT).show();
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