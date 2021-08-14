package com.vritti.chat.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.vritti.chat.bean.ChatGroupJson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DownloadActivity {

    String downloadChatRoomId , downloadImageUrl , downloadMessageId , filename ,file1;
    Object responsemsg;
    File imgFile , pdfFile;
    Context context;
    // ArrayList<ChatMessage> chatMessageArrayList1;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "",res , FinalJson ;
    Utility ut;
    String link, ChatRoomId, Message, MessageDate, MessageId , Message_id , UUID;
    private final int MEGABYTE = 1024 * 1024;
    public static DatabaseHandlers db;
    CommonFunction cf;
    int pos = -1;
    public static SQLiteDatabase sql;
    boolean isTextType = false;


    public DownloadActivity(Context context, int position, String chatRoomId ,String downloadImageUrl1 ,String messageId , String Type ) {
        downloadChatRoomId = chatRoomId;
        this.downloadImageUrl = downloadImageUrl1;
        downloadMessageId = messageId;
        this.context = context;
        // this.chatMessageArrayList1 = chatMessageArrayList1;
        pos =position;
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        sql = db.getWritableDatabase();
        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {



                                        /*holder.img_progress.getTag(position);
                                        holder.img_progress.setVisibility(View.VISIBLE);*/

                    new DownloadImageMethod().execute(downloadChatRoomId, downloadImageUrl, downloadMessageId);

                }

                @Override
                public void callfailMethod(String msg) {

                }
            });

        }
    }


    public void sendMessage() {
        String query = "SELECT * FROM " + db.TABLE_GROUP_JSON;
       // SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(query, null);
        int Count = cur.getCount();
        if (Count > 0) {
            cur.moveToFirst();
            ChatGroupJson chatGroupJson = new ChatGroupJson();
            FinalJson = cur.getString(cur.getColumnIndex("FinalJson"));
            Message_id = cur.getString(cur.getColumnIndex("ID"));
              /*  chatGroupJson.setFinalJsonGroup(FinalJson);
                chatGroupJson.setMessage_id(Message_id);
                chatGroupJsonArrayList.add(chatGroupJson);*/
            if (FinalJson.contains("UUID")) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(FinalJson);
                    UUID = jsonObject.getString("UUID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            if (isnet()) {

                new StartSession(context, new CallbackInterface() {
                    @Override

                    public void callMethod() {
                        PostGroupMessageJSON postGroupMessageJSON = new PostGroupMessageJSON();
                        postGroupMessageJSON.execute(FinalJson, Message_id);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }

        } else {

        }
    }

/*    private void downloadImageInBackgrownd(int position) {
        downloadChatRoomId = chatMessageArrayList1.get(position).getChatRoomId();
        downloadImageUrl = chatMessageArrayList1.get(position).getAttachment();
        downloadMessageId = chatMessageArrayList1.get(position).getMessageId();

        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {



                                        *//*holder.img_progress.getTag(position);
                                        holder.img_progress.setVisibility(View.VISIBLE);*//*

                    new AddChatRoomActivity.DownloadImageMethod().execute(downloadChatRoomId, downloadImageUrl, downloadMessageId);

                }

                @Override
                public void callfailMethod(String msg) {

                }
            });

        }
    }*/


    class DownloadImageMethod extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        int position;
        String pic;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {


            try {
                pic = urls[1];

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("chatRoomId", downloadChatRoomId);
                jsonObject.put("filepath", downloadImageUrl);

                params = jsonObject.toString();
                Log.d("ImageDownload:", params);
                params = params.toString().replaceAll("\\\\", "");


                try {
                    String url = CompanyURL + WebUrlClass.api_DownloadImageImageAndroid;
                    System.out.println("Params :" + params);
                    responsemsg = ut.OpenPostConnection(url, params, context);
                    Log.d("resp", "resp" + responsemsg);
                } catch (NullPointerException e) {
                    responsemsg = "error";
                    e.printStackTrace();
                } catch (Exception e) {
                    responsemsg = "error";
                    e.printStackTrace();
                }
            } catch (Exception e) {
                responsemsg = "error";
                e.printStackTrace();
            }
            Log.d("resp", "resp" + responsemsg);


            return null;

        }

        protected void onPostExecute(String feed) {
            super.onPostExecute(feed);
            // holder.img_progress.setVisibility(View.GONE);
            //  mProgressDialog.dismiss();
            //holder.txt_download.setVisibility(View.GONE);


            res = String.valueOf(responsemsg);
            imgFile = new File(pic);
            String filename1 = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);

            if (filename1.contains(".pdf")) {


                {
                    new DownloadFile().execute(pic, filename1);
                }



            } else {

                new DownloadFile().execute(downloadImageUrl, filename1);

                //saveMyImage("CRM", ReceiverImage, filename1);
            }
        }
    }


    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            filename = strings[1];  // -> maven.pdf
            String[] parts = filename.split("\\.");
            String prefix = parts[1];
            /*String path1 = Environment.getExternalStorageDirectory()
                    .toString();*/
            String path1 = Environment.getExternalStorageDirectory()
                    .toString();
            File file = null;
            if(fileUrl.contains("mp4"))
                file = new File(path1 + "/" + "Vwb" + "/" + "Video"+"/"+"ReceiveVideos");
            else
                file = new File(path1 + "/" + "Vwb"+"/"+"Receive");

            if (!file.exists())
                file.mkdirs();
            pdfFile = new File(file + "/" + filename);
            file1 = String.valueOf(pdfFile);


            try {
                //pdfFile = File.createTempFile(filename /* prefix */,prefix, pdfFile /* directory */);

                pdfFile.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadFile(fileUrl, pdfFile);
            return null;
        }
    }


    private void downloadFile(String fileUrl, File directory) {
        try {
            fileUrl = fileUrl.replaceAll(" ", "%20");

            URL url = new URL(fileUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();
            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put("IsDownloaded", "Yes");
            //  contentValues.put("Attachment", file1);
            contentValues.put("ChatRoomId", downloadChatRoomId);
            sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId='" + downloadMessageId + "'", null);
            //  sql.close();

            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    if (file1.contains(".pdf")) {
                        //AddChatRoomActivity addChatRoomActivity=new AddChatRoomActivity();
                        //  addChatRoomActivity.groupMessage(ChatRoomId,context);
                        //  mProgressDialog.dismiss();


                    } else if (file1.contains(".jpg") || (file1.contains(".png"))) {
                        // AddChatRoomActivity addChatRoomActivity=new AddChatRoomActivity();
                        //addChatRoomActivity.groupMessage(ChatRoomId,context);
                        // mProgressDialog.dismiss();
                        //holder.img_progress.setVisibility(View.GONE);


                    } else if (file1.contains("mp3")) {
                        //   Toast.makeText(context, "File downloaded successfully", Toast.LENGTH_SHORT).show();
                        // AddChatRoomActivity addChatRoomActivity=new AddChatRoomActivity();
                        // addChatRoomActivity.groupMessage(ChatRoomId,context);
                        // mProgressDialog.dismiss();

                    } else {
                        Log.i("status :", "File downloaded successfully");
                        //  Toast.makeText(context, "File downloaded successfully", Toast.LENGTH_SHORT).show();

                        //AddChatRoomActivity addChatRoomActivity=new AddChatRoomActivity();
                        //addChatRoomActivity.groupMessage(ChatRoomId,context);
                        //  mProgressDialog.dismiss();

                    }

                    ((AddChatRoomActivity) context).setDownload(pos, "Yes", downloadChatRoomId, context);


                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isnet() {
        Context context1 = context.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context1
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    class PostGroupMessageJSON extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String url = link + WebUrlClass.api_SendMessage;
            //String url = "aaa" + WebUrlClass.api_SendMessage;
            System.out.println("Back Message :" + url);

            try {
                res = ut.OpenPostConnection(url, params[0], context);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject Jsonmessage = jResults.getJSONObject(i);
                        MessageDate = Jsonmessage.getString("MessageDate");
                        ChatRoomId = Jsonmessage.getString("ChatRoomId");
                        MessageId = Jsonmessage.getString("MessageId");
                        Message=Jsonmessage.getString("Message");

                    }
                    ContentValues values = new ContentValues();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("Back Msg Response :" + response);
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);
            data.add(params[1]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> integer) {
            super.onPostExecute(integer);
            // progressDialog.dismiss();
            String res = integer.get(0);
            String msgid = integer.get(1);
            if (res.contains("MessageId")) {
                // delete(Message_id);
               // SQLiteDatabase sql = db.getWritableDatabase();

                sql.delete(db.TABLE_GROUP_JSON, "ID=?",
                        new String[]{msgid});

                ContentValues contentValues = new ContentValues();
                contentValues.put("IsDownloaded", "Yes");
                contentValues.put("ChatRoomId", ChatRoomId);
                sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId=?", new String[]{UUID});

                AddChatRoomActivity addChatRoomActivity = new AddChatRoomActivity();
                // addChatRoomActivity.groupMessage(ChatRoomId, ChattingDataSendBackground.this);
                Log.i("serverVerify :" , res);
                ((AddChatRoomActivity)context).updateUI(UUID ,ChatRoomId , pos);

                ContentValues values = new ContentValues();
              /*  MessageDate = MessageDate.substring(MessageDate.indexOf("(") + 1, MessageDate.lastIndexOf(")"));
                long timestamp = Long.parseLong(MessageDate);
                DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy hh:mma");
                Date date = new Date(timestamp);
                MessageDate = targetFormat.format(date);
              */
                values.put("StartTime", MessageDate);
                values.put("ChatMessage",Message);
                sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                Cursor cur = sql.rawQuery("select * from " + db.TABLE_GROUP_JSON, null);
                int a = cur.getCount();
              //  sql.close();
                sendMessage();

            }

        }


    }


}
