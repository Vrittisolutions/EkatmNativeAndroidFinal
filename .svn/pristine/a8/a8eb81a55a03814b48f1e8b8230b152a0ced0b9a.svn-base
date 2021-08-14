package com.vritti.chat.activity;

/**
 * Created by sharvari on 18-Jan-18.
 */

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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.chat.adapter.CloseChatRoomAdapter;
import com.vritti.chat.bean.ChatRoomDisplay;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class CloseConversationFragment extends Fragment {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    ListView listview_multiple_group;
    CloseChatRoomAdapter closeChatRoomAdapter;
    ArrayList<ChatRoomDisplay> chatRoomDisplayArrayList;
    EditText edt_search_name;
    SharedPreferences userpreferences;
    String  Starttime,ChatRoomId,Call_Callid,Call_CallType,ChatRoomName,Firm_name;
    SQLiteDatabase sql;
    View view;
    TextView text_not_found;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.crm_close_history_conversation_lay, container, false);

        context = getContext();
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
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        init();
        return  view;


    }
    private void init() {



        chatRoomDisplayArrayList=new ArrayList<>();
        listview_multiple_group= (ListView) view.findViewById(R.id.listview_multiple_group);
        text_not_found= (TextView) view.findViewById(R.id.text_not_found);
        Intent intent=getActivity().getIntent();
        Call_Callid=intent.getStringExtra("callid");
      /*  Call_CallType=intent.getStringExtra("call_type");
        ChatRoomId=intent.getStringExtra("ChatRoomid");
        Firm_name=intent.getStringExtra("firm");
*/

        if (isnet()) {
            new StartSession(getActivity(), new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadChatRoomDisplayDataJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }



        listview_multiple_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChatRoomId=chatRoomDisplayArrayList.get(i).getChatRoomId();
                ChatRoomName=chatRoomDisplayArrayList.get(i).getChatRoom_name();
                String Chat_status=chatRoomDisplayArrayList.get(i).getStatus();
                Intent intent=new Intent(getActivity(), AddChatRoomActivity.class);
                intent.putExtra("callid",Call_Callid);
                intent.putExtra("ChatRoomid",ChatRoomId);;
                intent.putExtra("Chatroomname",ChatRoomName);
                intent.putExtra("value_chat","1");
                intent.putExtra("status",Chat_status);
               /* if (Firm_name==null){
                    intent.putExtra("firm","");
                }else {
                    intent.putExtra("firm",Firm_name);
                }
                intent.putExtra("status",Chat_status);
                if (Call_CallType!=null) {
                    if (Call_CallType.equalsIgnoreCase("Crm_Collection")) {
                        intent.putExtra("call_type", "Crm_Collection");
                    }else if (Call_CallType.equalsIgnoreCase("Crm_feedback")){
                        intent.putExtra("call_type", "Crm_feedback");
                    }
                    else {
                        intent.putExtra("call_type", "Crm_Opportunity");
                    }
                }else {
                    intent.putExtra("call_type","");

                }*/
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();


            }
        });




    }

    class DownloadChatRoomDisplayDataJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(true);
            if (!getActivity().isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getChatRoomsForCall + "?CallId=" +Call_Callid;
            try {
                res = ut.OpenConnection(url,context);
                if (res!=null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_CHAT_ROOMNAME_DISPLAY_LIST, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_ROOMNAME_DISPLAY_LIST, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }


                        long a = sql.insert(db.TABLE_CHAT_ROOMNAME_DISPLAY_LIST, null, values);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")){
                text_not_found.setVisibility(View.VISIBLE);
            }else {
                if (response != null) {
                    progressDialog.dismiss();
                    ChatRoomNameUpdatList();
                }
            }

        }

    }
    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = getActivity().getApplicationContext();
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
    private void ChatRoomNameUpdatList() {
        chatRoomDisplayArrayList.clear();
        String query = "SELECT * FROM " + db.TABLE_CHAT_ROOMNAME_DISPLAY_LIST;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ChatRoomDisplay chatRoomDisplay = new ChatRoomDisplay();
                chatRoomDisplay.setChatRoomId(cur.getString(cur.getColumnIndex("ChatRoomId")));
                chatRoomDisplay.setUsername(cur.getString(cur.getColumnIndex("AddedBy")));
                chatRoomDisplay.setDate(cur.getString(cur.getColumnIndex("StartTime")));
                chatRoomDisplay.setChatRoom_name(cur.getString(cur.getColumnIndex("ChatRoomName")));
                chatRoomDisplay.setStatus(cur.getString(cur.getColumnIndex("ChatRoomStatus")));
                chatRoomDisplayArrayList.add(chatRoomDisplay);
            } while (cur.moveToNext());

            closeChatRoomAdapter = new CloseChatRoomAdapter(getActivity(),chatRoomDisplayArrayList);
            listview_multiple_group.setAdapter(closeChatRoomAdapter);

        }


    }



}
