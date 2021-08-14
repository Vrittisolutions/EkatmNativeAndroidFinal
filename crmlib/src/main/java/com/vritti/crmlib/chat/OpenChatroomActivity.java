package com.vritti.crmlib.chat;

import android.app.ProgressDialog;
import android.content.ContentUris;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

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
 * Created by prad on 02-Nov-17.
 */

public class OpenChatroomActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    ListView listview_multiple_group;
    ChatRoomMultipleAdapter chatRoomMultipleAdapter;
    ArrayList<ChatGroup> chatRoomDisplayArrayList;
    EditText edt_search_name;
    SharedPreferences userpreferences;
    String  Starttime,ChatRoomId,Call_Callid,Call_CallType,ChatRoomName;
    SQLiteDatabase sql;
    TextView txt_chatroom_add;
    private String path,Imagefilename;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.crm_open_chat__listview_lay);
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
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);

        sql = db.getWritableDatabase();
        init();
    }

    private void init() {



        chatRoomDisplayArrayList = new ArrayList<>();
        listview_multiple_group = (ListView) findViewById(R.id.listview_multiple_group);
        txt_chatroom_add = (TextView) findViewById(R.id.txt_chatroom_add);
        Intent intent = getIntent();
        Call_Callid = intent.getStringExtra("callid");
        Call_CallType = intent.getStringExtra("call_type");
        ChatRoomId = intent.getStringExtra("ChatRoomid");

        intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();



        // if this is from the share menu
        if (Intent.ACTION_SEND.equals(action)) {
            if (extras.containsKey(Intent.EXTRA_STREAM)) {
                try {
                    // Get resource path from intent callee
                    Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
                    File f = new File(getPath(OpenChatroomActivity.this,uri));

                    path = f.toString();
                    Imagefilename = f.getName();

                    /*if (path!=null&Imagefilename!=null){
                        startActivity(new Intent(OpenChatroomActivity.this,ImageFullScreenActivity.class).putExtra("share_image_path", path).putExtra("share_imagename", Imagefilename));
                    }*/

                } catch (Exception e) {
                    Log.e(this.getClass().getName(), e.toString());
                }

            } else if (extras.containsKey(Intent.EXTRA_TEXT)) {
                return;
            }
        }



        if (cf.getChatRoomNamecount()>0){
            ChatRoomNameUpdatList(UserMasterId);
        }else {


            if (isnet()) {
                new StartSession(OpenChatroomActivity.this, new CallbackInterface() {
                    @Override

                    public void callMethod() {
                        new DownloadChatRoomDisplayDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
        listview_multiple_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChatRoomId=chatRoomDisplayArrayList.get(i).getChatRoomId();
                ChatRoomName=chatRoomDisplayArrayList.get(i).getChatroom();
                String Chat_status=chatRoomDisplayArrayList.get(i).getStatus();

                Intent intent=new Intent(OpenChatroomActivity.this, AddChatRoomActivity.class);
                intent.putExtra("callid",Call_Callid);
                intent.putExtra("call_type",Call_CallType);
                intent.putExtra("ChatRoomid",ChatRoomId);;
                intent.putExtra("Chatroomname",ChatRoomName);
                intent.putExtra("value_chat","1");
                intent.putExtra("status",Chat_status);
                intent.putExtra("share_image_path", path);
                intent.putExtra("share_imagename", Imagefilename);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                ContentValues values = new ContentValues();
                values.put("Count", "0");
                sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});

                // finish();


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

            progressDialog = new ProgressDialog(OpenChatroomActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.show();
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebUrlClass.api_GetRefreshChatRoom + "?ApplicationCode="+WebUrlClass.AppNameFCM;

            String url = CompanyURL + WebUrlClass.api_GetRefreshChatRoom + "?ApplicationCode="+WebUrlClass.AppNameFCM +"&UserMasterId="+UserMasterId;

            try {
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(response);
             //  chatRoomDisplayArrayList.clear();

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
                    chatUser.setStatus(Jsonchatmember.getString("ChatRoomStatus"));
                        String StartTime =Jsonchatmember.getString("StartTime");
                    chatUser.setStartTime(StartTime);
                    chatUser.setAddedBy(Jsonchatmember.getString("AddedBy"));
                        chatUser.setCreater(Jsonchatmember.getString("Creator"));
                    chatUser.setUserMasterId(UserMasterId);
                        chatUser.setCount("0");
                    cf.AddGroupList(chatUser);
                    chatRoomDisplayArrayList.add(chatUser);


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
                txt_chatroom_add.setVisibility(View.VISIBLE);

            }else {
                if (response != null) {
                    txt_chatroom_add.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    ChatRoomNameUpdatList(UserMasterId);
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
    private void ChatRoomNameUpdatList(String userMasterId) {
        chatRoomDisplayArrayList.clear();




        ////////////////////////////////////////////////
        //String query = "SELECT  distinct ChatRoomName FROM " + db.TABLE_CHAT_CHATROOM_MEMBER_LIST +" WHERE  UserMasterId ='" + userMasterId + "'";;
       String query = "SELECT Distinct ChatRoomId,AddedBy,StartTime,ChatRoomName,ChatRoomStatus,Creator,Count FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST +" WHERE  UserMasterId ='" + userMasterId + "'order by StartTime desc ";;
        Cursor cur = sql.rawQuery(query, null);
        String count= String.valueOf(cur.getCount());
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ChatGroup  chatGroup = new ChatGroup();
                chatGroup.setChatRoomId(cur.getString(cur.getColumnIndex("ChatRoomId")));
                String AddedBy=cur.getString(cur.getColumnIndex("AddedBy"));
                chatGroup.setAddedBy(AddedBy);
                chatGroup.setStartTime(cur.getString(cur.getColumnIndex("StartTime")));
                String ChatRoomName=cur.getString(cur.getColumnIndex("ChatRoomName"));
                chatGroup.setChatroom(ChatRoomName);
                chatGroup.setStatus(cur.getString(cur.getColumnIndex("ChatRoomStatus")));
                chatGroup.setCreater(cur.getString(cur.getColumnIndex("Creator")));
                chatGroup.setCount(cur.getString(cur.getColumnIndex("Count")));
                chatRoomDisplayArrayList.add(chatGroup);
            } while (cur.moveToNext());

            chatRoomMultipleAdapter = new ChatRoomMultipleAdapter(OpenChatroomActivity.this,chatRoomDisplayArrayList);
            listview_multiple_group.setAdapter(chatRoomMultipleAdapter);
            chatRoomMultipleAdapter.notifyDataSetChanged();

        }else {
            txt_chatroom_add.setVisibility(View.VISIBLE);
        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OpenChatroomActivity.this.finish();
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        return cursor.getString(idx);
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                     String docId = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        docId = DocumentsContract.getDocumentId(uri);
                    }


                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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

            if(item.getItemId()== R.id.refresh1) {
                if (isnet()) {
                    new StartSession(OpenChatroomActivity.this, new CallbackInterface() {
                        @Override

                        public void callMethod() {
                            new DownloadChatRoomDisplayDataJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
                return true;
            }else  if(item.getItemId()== R.id.private_chat) {


                return true;
            }else {
                return super.onOptionsItemSelected(item);
            }


        }


    @Override
    protected void onResume() {
        super.onResume();
        UserMasterId = userpreferences.getString("UserMasterId", "");
        if (cf.getChatRoomNamecount()>0){
            ChatRoomNameUpdatList(UserMasterId);
        }
    }
}
