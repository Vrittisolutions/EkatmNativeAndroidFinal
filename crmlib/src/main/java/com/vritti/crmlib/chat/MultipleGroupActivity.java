package com.vritti.crmlib.chat;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;


import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.ChatRoomMultipleAdapter;
import com.vritti.crmlib.bean.ChatGroup;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;


/**
 * Created by pradnya on 02-Nov-17.
 */

public class MultipleGroupActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    ListView listview_multiple_group;
    ChatRoomMultipleAdapter chatRoomMultipleAdapter;
    ArrayList<ChatGroup> chatRoomDisplayArrayList;
    EditText edt_search_name;
    SharedPreferences userpreferences;
    String ChatRoomId, Call_Callid, Call_CallType, ChatRoomName, Firm_name;
    SQLiteDatabase sql;
    TextView txt_chatroom_add;
    Boolean IsChatApplicable;
    ProgressDialog progressDialog;
    String ProjectmasterID;
    LinearLayout len_add_chatroom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_multiplae_listview_lay);
        getSupportActionBar().setTitle("Chat Room");
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
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);

        sql = db.getWritableDatabase();
        init();
    }

    private void init() {


        ShortcutBadger.with(getApplicationContext()).remove();
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.remove("count");
        editor.commit();

        chatRoomDisplayArrayList = new ArrayList<>();
        listview_multiple_group = (ListView) findViewById(R.id.listview_multiple_group);
        txt_chatroom_add = (TextView) findViewById(R.id.txt_chatroom_add);
        len_add_chatroom = (LinearLayout) findViewById(R.id.len_add_chatroom);
        Intent intent = getIntent();
        Call_Callid = intent.getStringExtra("callid");
        Call_CallType = intent.getStringExtra("call_type");
        ChatRoomId = intent.getStringExtra("ChatRoomid");
        Firm_name = intent.getStringExtra("firm");
        ProjectmasterID = intent.getStringExtra("projmasterId");

        if (isnet()) {
            new StartSession(MultipleGroupActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadChatRoomDisplayDataJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        //Refresh();
        listview_multiple_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChatRoomId = chatRoomDisplayArrayList.get(i).getChatRoomId();
                ChatRoomName = chatRoomDisplayArrayList.get(i).getChatroom();
                String Chat_status = chatRoomDisplayArrayList.get(i).getStatus();
                Intent intent = new Intent(MultipleGroupActivity.this, AddChatRoomActivity.class);
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_type", Call_CallType);
                intent.putExtra("ChatRoomid", ChatRoomId);
                intent.putExtra("Chatroomname", ChatRoomName);
                intent.putExtra("firm", Firm_name);
                intent.putExtra("status", Chat_status);
                intent.putExtra("projmasterId", ProjectmasterID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                /* finish();*/


            }
        });

        txt_chatroom_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MultipleGroupActivity.this, AddGroupActivity.class);
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_type", Call_CallType);
                intent.putExtra("firm", Firm_name);

                intent.putExtra("projmasterId", ProjectmasterID);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                /* finish();*/
            }
        });


    }

    class DownloadChatRoomDisplayDataJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(MultipleGroupActivity.this);
                    progressDialog.setMessage("Loading. Please wait...");
                    progressDialog.setIndeterminate(false);
                    //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //  progressDialog.setContentView(R.layout.crm_progress_lay);
                    progressDialog.setCancelable(true);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getChatRoomsForCall + "?CallId=" + Call_Callid;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
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
            if (response.equalsIgnoreCase("[]")) {
                len_add_chatroom.setVisibility(View.VISIBLE);
            } else {
                if (response != null) {
                    try {
                        listview_multiple_group.setVisibility(View.VISIBLE);
                        ContentValues values = new ContentValues();
                        JSONArray jResults = new JSONArray(response);
                        /*sql.delete(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null,
                                null);*/
                        // Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null);
                        // int count = c.getCount();
                        String columnName, columnValue;
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject Jsonchatmember = jResults.getJSONObject(i);
                            ChatGroup chatUser = new ChatGroup();
                            String Chatroomname = Jsonchatmember.getString("ChatRoomName");
                            chatUser.setChatroom(Chatroomname);
                            ChatRoomId = Jsonchatmember.getString("ChatRoomId");
                            chatUser.setChatRoomId(ChatRoomId);
                            chatUser.setStatus(Jsonchatmember.getString("ChatRoomStatus"));
                            chatUser.setStartTime(Jsonchatmember.getString("StartTime"));
                            chatUser.setAddedBy(Jsonchatmember.getString("AddedBy"));
                            chatUser.setUserMasterId(UserMasterId);
                            chatUser.setChatSourceId(Call_Callid);
                            //db.AddGroupMember(chatUser);
                            chatRoomDisplayArrayList.add(chatUser);
                        }
                        chatRoomMultipleAdapter = new ChatRoomMultipleAdapter(MultipleGroupActivity.this, chatRoomDisplayArrayList);
                        listview_multiple_group.setAdapter(chatRoomMultipleAdapter);

                        //    ChatRoomNameUpdatList(Call_Callid);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

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

  /*  private void ChatRoomNameUpdatList( String call_id) {
        chatRoomDisplayArrayList.clear();
        String query = "SELECT * FROM " + db.TABLE_CHAT_CHATROOM_MEMBER_LIST +" WHERE  ChatSourceId ='" + call_id + "'";;;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ChatUser chatRoomDisplay = new ChatUser();
                chatRoomDisplay.setChatRoomId(cur.getString(cur.getColumnIndex("ChatRoomId")));
                chatRoomDisplay.setAddedBy(cur.getString(cur.getColumnIndex("AddedBy")));
                chatRoomDisplay.setStartTime(cur.getString(cur.getColumnIndex("StartTime")));
                chatRoomDisplay.setChatroom(cur.getString(cur.getColumnIndex("ChatRoomName")));
                chatRoomDisplay.setStatus(cur.getString(cur.getColumnIndex("ChatRoomStatus")));
                chatRoomDisplayArrayList.add(chatRoomDisplay);
            } while (cur.moveToNext());

            chatRoomMultipleAdapter = new ChatRoomMultipleAdapter(MultipleGroupActivity.this, chatRoomDisplayArrayList);
            listview_multiple_group.setAdapter(chatRoomMultipleAdapter);

        }else {
            if (isnet()) {
                new StartSession(MultipleGroupActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadChatRoomDisplayDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }


    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MultipleGroupActivity.this.finish();
    }


    class DownloadGetEnvJSON extends AsyncTask<Integer, Void, String> {
        String res;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(MultipleGroupActivity.this);
                    progressDialog.setMessage("Loading. Please wait...");
                    progressDialog.setIndeterminate(false);
                    // progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    // progressDialog.setContentView(R.layout.crm_progress_lay);
                    progressDialog.setCancelable(true);

                }
                progressDialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getEnv;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\\\\\\\\"", "");
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (res.contains("AppEnvMasterId")) {
                try {
                    JSONArray jResults = new JSONArray(res);
                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jorder = jResults.getJSONObject(index);
                        String data = jorder.getString("AppEnvMasterId");
                        if (data.equalsIgnoreCase(EnvMasterId)) {
                            IsChatApplicable = jorder.getBoolean("IsChatApplicable");
                        } else {
                            IsChatApplicable = false;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (IsChatApplicable) {
                    new DownloadChatRoomDisplayDataJSON().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Chat module is not installed", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Server not responding", Toast.LENGTH_SHORT).show();
            }

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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        if (item.getItemId() == R.id.refresh) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }


    }
}

    /*private void Refresh(){
        if (isnet()) {
            new StartSession(MultipleGroupActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadGetEnvJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }*/
