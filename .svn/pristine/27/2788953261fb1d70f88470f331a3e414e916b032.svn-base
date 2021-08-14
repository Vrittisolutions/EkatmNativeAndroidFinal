package com.vritti.crmlib.chat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.codetail.animation.SupportAnimator;

import me.leolin.shortcutbadger.ShortcutBadger;


import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.GroupChatListAdapter;
import com.vritti.crmlib.bean.ChatGroupJson;
import com.vritti.crmlib.bean.ChatMessage;
import com.vritti.crmlib.bean.ChatUser;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.services.ChattingDataSendBackground;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.databaselib.videocompression.MediaController;



import static com.vritti.crmlib.chat.FilePath.getDataColumn;
import static com.vritti.crmlib.chat.FilePath.isDownloadsDocument;
import static com.vritti.crmlib.chat.FilePath.isExternalStorageDocument;
import static com.vritti.crmlib.chat.FilePath.isMediaDocument;


/**
 * Created by pradnya on 30-Oct-17.
 */

public class AddChatRoomActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    public static Context context;

    String Call_Callid, Call_CallType, MessageId;
    RelativeLayout rel_chat;
    public static SQLiteDatabase sql;
    // public static ArrayList<ChatMessage> chatMessageArrayList1;
    public static ListView listview_chatting;
    EmojiconEditText chat_edit_text;
    ImageButton enter_chat;
    SharedPreferences userpreferences;
    public static GroupChatListAdapter groupChatListAdapter;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;

    String ChatRoomId, Starttime = "", FinalJson, ReceiverName, Message = "", MessageDate = "", Sender, SenderName, Status = "", MessageType = "";
    public static String ChatRoomName;
    public static boolean status;
    boolean shouldExecuteOnResume;
    //static int Count;
    static String Active = "1";
    SimpleDateFormat df;
    Calendar c;
    String message;
    ChatGroupJson chatGroupJson;
    ArrayList<ChatGroupJson> chatGroupJsonArrayList;
    String current_time, Create_check;
    String Chat_message = "", CStatus = "", Chat_User_check;
    private static View header;
    TextView text;
    Dialog dialog;
    LinearLayout bottomlayout;
    ArrayList<ChatUser> chatUserArrayList;
    ImageView attachment, attachment_video;
    static Rect location = new Rect();


    //Chat Attachment

    LinearLayout revealLayout2;
    boolean hidden = true;
    LinearLayout len_document;
    LinearLayout len_camera;
    LinearLayout len_gallery, len_audio, len_video, len_record_video;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int MEDIA_TYPE_VIDEO_CAPTURE = 22;
    public static final int REQUEST_VIDEO_TRIMMER = 8;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    private static final int INTENT_PICK_AUDIO = 3;
    String encodedImage, res, image_encode, Imagefilename, path, path1;
    static Context parent;
    Object responsemsg3;
    static File mediaFile;
    private static final String IMAGE_DIRECTORY_NAME = "VWB_Image";
    public static final int SELECT_FILE_IMAGE = 1;
    private int PICK_PDF_REQUEST = 2;

    AsyncTask async;
    public String dir, Attachment = "";

    ChatMessage chatMessage;
    // RevealFrameLayout lay_reveal;
    ProgressBar progress;
    String ProjectMasterID;
    TextView txt_chat_title;
    private Uri fileUri;


    CoordinatorLayout cordinate_view;
    static final String FTP_HOST = "219.91.158.74";//"182.50.132.40";
    static final String FTP_USER = "love";//"STASupport";
    static final String FTP_PASS = "love123";//"of4E9q&0";
    private String serverResponseMessage;
    public static String isRefresh = "false";
    private Date date;
    public static String dotformat = "No";
    private String uuidInString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_chat_activity_main);
        AddChatRoomActivity.this.registerReceiver(mMyBroadcastReceiver, new IntentFilter("chatscrren"));
        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {
            }
        } else {
        }

    }


    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        Call_Callid = intent.getStringExtra("callid");
        Call_CallType = intent.getStringExtra("call_type");
        ChatRoomId = intent.getStringExtra("ChatRoomid");
        ChatRoomName = intent.getStringExtra("Chatroomname");
        Status = intent.getStringExtra("group_status");
        Create_check = intent.getStringExtra("value_chat");
        ProjectMasterID = intent.getStringExtra("projmasterId");
        CStatus = intent.getStringExtra("status");

        if (CStatus == null) {
            CStatus = "Created";
        }

        path = intent.getStringExtra("share_image_path");
        Imagefilename = intent.getStringExtra("share_imagename");


        bottomlayout = (LinearLayout) findViewById(R.id.bottomlayout);
        cordinate_view = (CoordinatorLayout) findViewById(R.id.cocordinate_view);
        txt_chat_title = (TextView) findViewById(R.id.txt_group_title);

        if (CStatus != null && CStatus.equalsIgnoreCase("Closed")) {
            bottomlayout.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar.make(cordinate_view, "You can't send message because this chatroom has been closed", Snackbar.LENGTH_LONG).setDuration(Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setMaxLines(3);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        ShortcutBadger.with(getApplicationContext()).remove();
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.remove("count");
        editor.commit();

        chatGroupJsonArrayList = new ArrayList<>();
        chatUserArrayList = new ArrayList<>();
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
        UserName = "";
        sql = db.getWritableDatabase();

        context = AddChatRoomActivity.this;

        c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.AM_PM, Calendar.PM);
        System.out.println("Current time => " + c.getTime());

        /*long d=System.currentTimeMillis();
        c.setTimeInMillis(d);
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH)+1;
        int day=c.get(Calendar.DAY_OF_MONTH);
        int time=c.get(Calendar.HOUR_OF_DAY);
        int min=c.get(Calendar.MINUTE);
        int ampm=c.get(Calendar.AM_PM);
       // df = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");

        Starttime= String.valueOf(year)+"-"+month+"-"+day+" "+time+":"+min+" "+ampm;
*/

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        Starttime = dateFormat.format(new Date()).toString();
        System.out.println(Starttime);

        //  Starttime = df.format(c.getTime());
        /*SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy K:mm aa");
        current_time = df1.format(c.getTime());
   */

        listview_chatting = (ListView) findViewById(R.id.listview_chatting);
        rel_chat = (RelativeLayout) findViewById(R.id.rel_chat);
        chat_edit_text = (EmojiconEditText) findViewById(R.id.chat_edit_text);
        enter_chat = (ImageButton) findViewById(R.id.enter_chat);
        attachment = (ImageView) findViewById(R.id.attachment);
        attachment_video = (ImageView) findViewById(R.id.attachment_video);
        progress = (ProgressBar) findViewById(R.id.progress);
        chatUserArrayList = new ArrayList<>();
        //Chat Attachment
        revealLayout2 = (LinearLayout) findViewById(R.id.reveal_items2);
        revealLayout2.setVisibility(View.INVISIBLE);

        len_document = (LinearLayout) findViewById(R.id.len_document);
        len_audio = (LinearLayout) findViewById(R.id.len_audio);
        len_video = (LinearLayout) findViewById(R.id.len_video);
        len_record_video = (LinearLayout) findViewById(R.id.len_record_video);
        if (CStatus != null && CStatus.equalsIgnoreCase("Closed")) {

        } else {
            if (path != null & Imagefilename != null) {
                if (isnet()) {
                    async = new PostUploadImageMethod().execute();
                }
            }
        }


        len_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealLayout2.setVisibility(View.INVISIBLE);
                hidden = true;
                isRefresh = "false";
                DocumentSelect();

            }
        });

        len_camera = (LinearLayout) findViewById(R.id.len_camera);
        len_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRefresh = "false";
                try {
                    if (ContextCompat.checkSelfPermission(AddChatRoomActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                        }
                    } else {
                        revealLayout2.setVisibility(View.INVISIBLE);
                        hidden = true;
                        captureImage();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        len_gallery = (LinearLayout) findViewById(R.id.len_gallery);

        len_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealLayout2.setVisibility(View.INVISIBLE);
                hidden = true;
                isRefresh = "false";
                GallerycaptureImage();
            }
        });


        len_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealLayout2.setVisibility(View.INVISIBLE);
                hidden = true;
                isRefresh = "false";
                pickaudio();
            }
        });

        len_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealLayout2.setVisibility(View.INVISIBLE);
                hidden = true;
                isRefresh = "false";
                pickFromGalleryvideo();
            }
        });


        len_record_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRefresh = "false";
                try {

                    if (ContextCompat.checkSelfPermission(AddChatRoomActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                        }
                    } else {
                        revealLayout2.setVisibility(View.INVISIBLE);
                        hidden = true;
                        recordVideo();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (cf.getChatmessagecount() > 0) {
            groupMessage(ChatRoomId, AddChatRoomActivity.this);

        }
        if (Create_check != null) {
            ChatUserUpdatList(ChatRoomId);
        } else {

        }
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            txt_chat_title.setText(ChatRoomName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        chat_edit_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
//                    chat_edit_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    Chat_message = chat_edit_text.getText().toString();

                    enter_chat.setBackgroundDrawable(getResources().getDrawable(R.drawable.send));
                } else {
                    enter_chat.setBackgroundDrawable(getResources().getDrawable(R.drawable.keyboard));

                }
            }
        });


        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int cx = revealLayout2.getRight();
                int cy = revealLayout2.getBottom();
                makeEffect(revealLayout2, cx, cy);


            }
        });


        enter_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
*/
                isRefresh = "false";
                if (validate()) {

                    Chat_message = chat_edit_text.getText().toString();

                    JSONObject jsonObject = new JSONObject();
                    onResume();
                    try {

                        jsonObject.put("ChatRoomId", ChatRoomId);
                        jsonObject.put("senderId", UserMasterId);
                        jsonObject.put("senderName", UserName);
                        String MessageEncode = StringEscapeUtils.escapeJava(Chat_message);
                        jsonObject.put("senderMSG", MessageEncode);
                        jsonObject.put("senderMSGType", "text:");
                        jsonObject.put("senderMSGtime", Starttime);
                        jsonObject.put("Attachment", Attachment);
                        UUID uuid = UUID.randomUUID();
                        uuidInString = uuid.toString();
                        jsonObject.put("UUID", uuidInString);

                        FinalJson = jsonObject.toString();
                        chatGroupJson = new ChatGroupJson();
                        chatGroupJson.setFinalJsonGroup(FinalJson);
                        cf.AddGrouMessageJson(chatGroupJson);
                        chatGroupJsonArrayList.add(chatGroupJson);
                        Intent intent = new Intent(AddChatRoomActivity.this, ChattingDataSendBackground.class);
                        startService(intent);
                        chatMessage = new ChatMessage();
                        chatMessage.setMessage(Chat_message);
                        chatMessage.setStatus("Sender");
                        chatMessage.setMessageDate(Starttime);
                        chatMessage.setUsername(UserName);
                        uuidInString = uuid.toString();
                        chatMessage.setMessageId(uuidInString);
                        chatMessage.setChatRoomId(ChatRoomId);
                        chatMessage.setUserMasterId(UserMasterId);
                        chatMessage.setMessageType("text:");
                        chatMessage.setAttachment("");
                        chatMessage.setIsDownloaded("No");
                        cf.AddGroupMessage(chatMessage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    chat_edit_text.setText("");
                    groupMessage(ChatRoomId, AddChatRoomActivity.this);
                }
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ChatRoomId.equals("null")) {
                    if (CStatus.equalsIgnoreCase("Closed")) {
                        Snackbar snackbar = Snackbar.make(cordinate_view, "You can't add participants because this chatroom has been closed", Snackbar.LENGTH_LONG).setDuration(Snackbar.LENGTH_LONG);
                        View snackbarView = snackbar.getView();
                        TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setMaxLines(3);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        } else {
                            tv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();
                    } else {
                        Intent intent = new Intent(AddChatRoomActivity.this, GroupmemberShowActivity.class);
                        intent.putExtra("callid", Call_Callid);
                        intent.putExtra("call_type", Call_CallType);
                        intent.putExtra("ChatRoomid", ChatRoomId);
                        intent.putExtra("Chatroomname", ChatRoomName);
                        intent.putExtra("projmasterId", ProjectMasterID);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(AddChatRoomActivity.this, "Please Create Chat Room", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


   /* private void getChateDatalist() {

        db = new DatabaseHandler(context);
        sql = db.getWritableDatabase();
        chatMessageArrayList1.clear();
        String Status;
        String query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGES + " WHERE  ChatRoomId ='" + ChatRoomId + "' order by MessageDate asc ";

        // String query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGES + " WHERE  ChatRoomId='" + ChatRoomId + "'";///TABLE_CHAT_GROUP_MESSAGES
        Cursor cur = sql.rawQuery(query, null);
        int Count = cur.getCount();
        if (Count > 0) {
            cur.moveToFirst();
            do {
                ChatMessage chatMessage1 = new ChatMessage();
                chatMessage1.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                chatMessage1.setChatRoomId(cur.getString(cur.getColumnIndex("ChatRoomId")));
                chatMessage1.setMessage(cur.getString(cur.getColumnIndex("Message")));
                chatMessage1.setMessageDate(cur.getString(cur.getColumnIndex("MessageDate")));
                chatMessage1.setUsername(cur.getString(cur.getColumnIndex("UserName")));
                Status = cur.getString(cur.getColumnIndex("Status"));
                chatMessage1.setStatus(Status);
                chatMessage1.setMessageType(cur.getString(cur.getColumnIndex("MessageType")));
                chatMessage1.setMessageId(cur.getString(cur.getColumnIndex("MessageId")));
                chatMessage1.setAttachment(cur.getString(cur.getColumnIndex("Attachment")));
                String IsDownloaded = cur.getString(cur.getColumnIndex("IsDownloaded"));
                if (IsDownloaded == null) {
                    chatMessage1.setIsDownloaded("No");
                } else {
                    chatMessage1.setIsDownloaded(IsDownloaded);
                }
                chatMessageArrayList1.add(chatMessage1);
            } while (cur.moveToNext());
        }
        groupChatListAdapter = new GroupChatListAdapter(chatMessageArrayList1, context);
        listview_chatting.setAdapter(groupChatListAdapter);
        listview_chatting.setAddStatesFromChildren(true);
        groupChatListAdapter.notifyDataSetChanged();
        listview_chatting.setSelection(groupChatListAdapter.getCount() - 1);
        chat_edit_text.setText("");
    }*/

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
        //super.onBackPressed();
        hidden = true;
        if (revealLayout2.getVisibility() == View.VISIBLE) {
            revealLayout2.setVisibility(View.INVISIBLE);
            hidden = true;
        } else {
            // startActivity(new Intent(AddChatRoomActivity.this, OpenChatroomActivity.class));
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();


        }
        if (id == R.id.chat_menu) {

            dialog = new Dialog(AddChatRoomActivity.this);
            Window window = dialog.getWindow();
            dialog.requestWindowFeature(window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.crm_chat_data_menu);

            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;

            wlp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            TextView txt_chatroom = (TextView) dialog.findViewById(R.id.txt_chatroom);
            TextView txt_refresh = (TextView) dialog.findViewById(R.id.txt_refresh);

            if (Create_check != null) {
                txt_chatroom.setVisibility(View.GONE);
            } else {
                txt_chatroom.setVisibility(View.VISIBLE);

            }


            txt_chatroom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddChatRoomActivity.this, AddGroupActivity.class);
                    intent.putExtra("callid", Call_Callid);
                    intent.putExtra("call_type", Call_CallType);
                    intent.putExtra("status", CStatus);
                    intent.putExtra("projmasterId", ProjectMasterID);


                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            });

            txt_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //sql = db.getWritableDatabase();
                    sql.delete(db.TABLE_CHAT_GROUP_MESSAGE, "ChatRoomId=?",
                            new String[]{ChatRoomId});
                    ChatRoomMessageRetrive chatRoomMessageRetrive = new ChatRoomMessageRetrive();
                    chatRoomMessageRetrive.execute();
                    dialog.dismiss();
                    //sql.close();
                }
            });

            dialog.show();
            return true;
        }

        return (super.onOptionsItemSelected(menuItem));
    }

    public void groupMessage(String chatRoomId, Context context) {
        sql = db.getWritableDatabase();
        ArrayList<ChatMessage> chatMessageArrayList1 = new ArrayList<ChatMessage>();
        String Status;
        //  String query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGES + " WHERE  ChatRoomId ='" + chatRoomId + "' order by MessageDate asc ";
        String query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE + " WHERE  ChatRoomId ='" + chatRoomId + "'";
        Cursor cur = sql.rawQuery(query, null);
        int Count = cur.getCount();
        if (Count > 0) {
            cur.moveToFirst();
            do {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                chatMessage.setChatRoomId(cur.getString(cur.getColumnIndex("ChatRoomId")));

                String Msg = cur.getString(cur.getColumnIndex("Message"));
                if (Msg != null) {
                    Msg = Msg.replaceAll("u0026", "&");
                }
                chatMessage.setMessage(Msg);
                chatMessage.setMessageDate(cur.getString(cur.getColumnIndex("MessageDate")));
                chatMessage.setUsername(cur.getString(cur.getColumnIndex("UserName")));
                Status = cur.getString(cur.getColumnIndex("Status"));
                chatMessage.setStatus(Status);
                chatMessage.setMessageType(cur.getString(cur.getColumnIndex("MessageType")));
                chatMessage.setMessageId(cur.getString(cur.getColumnIndex("MessageId")));
                chatMessage.setAttachment(cur.getString(cur.getColumnIndex("Attachment")));
                chatMessage.setIsDownloaded(cur.getString(cur.getColumnIndex("IsDownloaded")));
                chatMessageArrayList1.add(chatMessage);
            } while (cur.moveToNext());
            groupChatListAdapter = new GroupChatListAdapter(chatMessageArrayList1, context);
            listview_chatting.setAdapter(groupChatListAdapter);
            listview_chatting.addStatesFromChildren();
            //   groupChatListAdapter.notifyDataSetChanged();
            listview_chatting.setSelection(groupChatListAdapter.getCount() - 1);


        } else {
            Toast.makeText(getApplicationContext(), "No chat to dispaly", Toast.LENGTH_LONG).show();
        }
    }


    private static void scrollMyListViewToBottom() {
        listview_chatting.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
            }
        });
    }


    BroadcastReceiver mMyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra("message");
            ChatRoomId = intent.getStringExtra("ChatRoomid");
            ChatRoomName = intent.getStringExtra("Chatroomname");
            //onResume();
            if (message.equals("1")) {
                groupMessage(ChatRoomId, AddChatRoomActivity.this);
                getSupportActionBar().setTitle(ChatRoomName);


            }
        }
    };


    public boolean validate() {
        if ((chat_edit_text.getText().toString().equalsIgnoreCase("") ||
                chat_edit_text.getText().toString().equalsIgnoreCase(" ") ||
                chat_edit_text.getText().toString().equalsIgnoreCase(null))) {
            // Toast.makeText(context, "Enter " + setup("Firmname"), Toast.LENGTH_LONG).show();

            Toast.makeText(context, "Enter message", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AddChatRoomActivity.this.unregisterReceiver(mMyBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dotformat = "No";

        c = Calendar.getInstance();
       /* c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.AM_PM, Calendar.PM);
       */
        // df = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        Starttime = dateFormat.format(new Date()).toString();
        System.out.println(Starttime);
        //Starttime = df.format(c.getTime());
        /*if (Starttime.contains("a.m.")){
            dotformat="yes";
            String a = Starttime.replace("a.m.","AM");
            Starttime =a;
        }else if (Starttime.contains("p.m.")){
            dotformat="yes";
            String a = Starttime.replace("p.m.","PM");
            Starttime =a;
        }*/

      /*  SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy K:mm aa");
        current_time = df1.format(c.getTime());
*/


    }

    class ChatRoomMessageRetrive extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(AddChatRoomActivity.this);
            if (!isFinishing()) {

                progressDialog.setCancelable(true);
                progressDialog.show();
                progressDialog.setContentView(R.layout.crm_progress_lay);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            }
            //  showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                sql = db.getWritableDatabase();
                String url = CompanyURL + WebUrlClass.api_GetRefreshMessages + "?ChatRoomId=" + ChatRoomId + "&UserMasterId=" + UserMasterId;
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);

                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);
                    ContentValues values = new ContentValues();
                    //sql = db.getWritableDatabase();


                 /*   sql.delete(db.TABLE_CHAT_GROUP_MESSAGES, null,
                            null);*/

                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE, null);
                    int count = c.getCount();

                    if (jResults.length() > 0) {
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject Jsongroupmessage = jResults.getJSONObject(i);
                            ChatMessage chatMessage = new ChatMessage();
                            //  UserMasterId = Jsongroupmessage.getString("UserMasterId");
                            //chatMessage.setUserMasterId(UserMasterId);
                            ChatRoomId = Jsongroupmessage.getString("ChatRoomId");
                            chatMessage.setChatRoomId(ChatRoomId);
                            Message = Jsongroupmessage.getString("Message");
                            chatMessage.setMessage(Message);

                            Status = Jsongroupmessage.getString("Status");
                            chatMessage.setStatus(Status);


                            MessageDate = Jsongroupmessage.getString("MessageDate");

                            if (Status.equals("Sender")) {
                                DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mmaa");
                                DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
                                if (dotformat.equals("yes")) {
                                    if (MessageDate.contains("a.m")) {
                                        String a = MessageDate.replace("a.m.", "AM");
                                        MessageDate = a;
                                    } else if (MessageDate.contains("PM")) {
                                        String a = MessageDate.replace("p.m.", "PM");
                                        MessageDate = a;

                                    }
                                }


                                try {
                                    date = originalFormat.parse(MessageDate);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                String chat_time = targetFormat.format(date);
                                chatMessage.setMessageDate(chat_time);
                            } else {
                                if (Status.equals("")) {
                                    DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mmaa");
                                    DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy HH:mmaa");
                                    try {
                                        date = originalFormat.parse(MessageDate);
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    String chat_time = targetFormat.format(date);
                                    chatMessage.setMessageDate(chat_time);
                                }
                            }


                            MessageId = Jsongroupmessage.getString("MessageId");
                            chatMessage.setMessageId(MessageId);
                            UserName = Jsongroupmessage.getString("UserName");
                            chatMessage.setUsername(UserName);

                            MessageType = Jsongroupmessage.getString("MessageType");
                            chatMessage.setMessageType(MessageType);
                            Attachment = Jsongroupmessage.getString("Attachment");
                            chatMessage.setIsDownloaded("No");
                            if (Attachment.equals("")) {
                                chatMessage.setAttachment(null);
                            } else {
                                chatMessage.setAttachment(CompanyURL + Attachment);
                            }
                            cf.AddGroupMessage(chatMessage);
                            //   chatMessageArrayList1.add(chatMessage);
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("Chat Message Refresh :" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (response != null) {
                groupMessage(ChatRoomId, context);
            } else {
                Toast.makeText(AddChatRoomActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
            }


        }

    }

    private void ChatUserUpdatList(String ChatRoomid) {
        chatUserArrayList.clear();
        chatUserArrayList = new ArrayList<>();
        String query = "SELECT * FROM " + db.TABLE_CHAT_CHATROOM_MEMBER_LIST + " WHERE  ChatRoomId ='" + ChatRoomid + "'";
        ;
        ChatUser chatUser;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                chatUser = new ChatUser();
                Chat_User_check = cur.getString(cur.getColumnIndex("ParticipantId"));
                chatUser.setUserMasterId(Chat_User_check);
                chatUser.setUsername(cur.getString(cur.getColumnIndex("ParticipantName")));
                chatUser.setChatroom(cur.getString(cur.getColumnIndex("ChatRoomName")));
                String Creater = cur.getString(cur.getColumnIndex("Creator"));
                chatUser.setCreater(Creater);
                chatUserArrayList.add(chatUser);


            } while (cur.moveToNext());


        }
    }

    private void makeEffect(final LinearLayout layout, int cx, int cy) {

        int radius = Math.max(layout.getWidth(), layout.getHeight());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            SupportAnimator animator =
                    io.codetail.animation.ViewAnimationUtils.createCircularReveal(layout, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(800);

            SupportAnimator animator_reverse = animator.reverse();

            if (hidden) {
                layout.setVisibility(View.VISIBLE);
                animator.start();
                hidden = false;
            } else {
                animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        layout.setVisibility(View.INVISIBLE);
                        hidden = true;

                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });
                animator_reverse.start();

            }
        } else {
            if (hidden) {
                Animator anim = ViewAnimationUtils.createCircularReveal(layout, cx, cy, 0, radius);
                layout.setVisibility(View.VISIBLE);
                anim.start();
                hidden = false;

            } else {
                Animator anim = ViewAnimationUtils.createCircularReveal(layout, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout.setVisibility(View.INVISIBLE);
                        hidden = true;
                    }
                });
                anim.start();

            }
        }
    }

    private void captureImage() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUriCamera = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
       // intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriCamera);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/

    }


    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO_CAPTURE);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    private void GallerycaptureImage() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_FILE_IMAGE);
    }

    private void pickaudio() {
       /* Intent intent_upload = new Intent();
        fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_VIDEO));
        intent_upload.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent_upload.setType("audio*//*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,INTENT_PICK_AUDIO);*/
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Audio "), INTENT_PICK_AUDIO);

    }

    private void DocumentSelect() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
    }


    public class PostUploadImageMethod extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... urls) {

            try {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(path);

                String upLoadServerUri = CompanyURL + WebUrlClass.api_FileUpload;
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", path);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + path + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseMessage.equals("OK")) {
//

                    //
                    AddChatRoomActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            progress.setVisibility(View.GONE);
                            revealLayout2.setVisibility(View.INVISIBLE);
                            hidden = true;
                            Toast.makeText(AddChatRoomActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ChatRoomId", ChatRoomId);
                        jsonObject.put("senderId", UserMasterId);
                        jsonObject.put("senderName", UserName);
                        jsonObject.put("senderMSG", null);
                        jsonObject.put("senderMSGType", "text:");
                        jsonObject.put("senderMSGtime", Starttime);
                        jsonObject.put("Attachment", Imagefilename);
                        FinalJson = jsonObject.toString();
                        chatGroupJson = new ChatGroupJson();
                        chatGroupJson.setFinalJsonGroup(FinalJson);
                        cf.AddGrouMessageJson(chatGroupJson);
                        chatGroupJsonArrayList.add(chatGroupJson);
                        Intent intent = new Intent(AddChatRoomActivity.this, ChattingDataSendBackground.class);
                        startService(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    if (serverResponseMessage.contains("Error")) {
                        AddChatRoomActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                progress.setVisibility(View.GONE);
                                Toast.makeText(AddChatRoomActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;

        }

        protected void onPostExecute(String feed) {


        }
    }

  /*  public class PostUploadImageMethod extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... urls) {


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("image_encode", image_encode);
                jsonObject.put("Imagefilename", Imagefilename);
                jsonObject.put("TicketId", ChatRoomId);
                params = jsonObject.toString();


                try {
                    String url = CompanyURL + WebUrlClass.api_PostUploadImageAndroid;
                    responsemsg3 = ut.OpenPostConnection(url, params, getApplicationContext());

                } catch (NullPointerException e) {
                    responsemsg3 = "error";
                    e.printStackTrace();
                } catch (Exception e) {
                    responsemsg3 = "error";
                    e.printStackTrace();
                }
            } catch (Exception e) {
                responsemsg3 = "error";
                e.printStackTrace();
            }


            return null;

        }

        protected void onPostExecute(String feed) {
            progress.setVisibility(View.GONE);
            res = String.valueOf(responsemsg3);
            revealLayout2.setVisibility(View.INVISIBLE);
            hidden = true;
            if (res.contains("Success")) {
                Toast.makeText(AddChatRoomActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ChatRoomId", ChatRoomId);
                    jsonObject.put("senderId", UserMasterId);
                    jsonObject.put("senderName", UserName);
                    jsonObject.put("senderMSG", null);
                    jsonObject.put("senderMSGType", "text:");
                    jsonObject.put("senderMSGtime", Starttime);
                    jsonObject.put("Attachment", Imagefilename);
                    FinalJson = jsonObject.toString();
                    chatGroupJson = new ChatGroupJson();
                    chatGroupJson.setFinalJsonGroup(FinalJson);
                    db.AddGrouMessageJson(chatGroupJson);
                    chatGroupJsonArrayList.add(chatGroupJson);
                    Intent intent = new Intent(AddChatRoomActivity.this, ChattingDataSendBackground.class);
                    startService(intent);
                    chatMessage = new ChatMessage();
                    chatMessage.setMessage(Chat_message);
                    chatMessage.setStatus("Sender");
                    chatMessage.setMessageDate(current_time);
                    chatMessage.setUsername(UserName);
                    UUID uuid = UUID.randomUUID();
                    String uuidInString = uuid.toString();
                    chatMessage.setMessageId(uuidInString);
                    chatMessage.setChatRoomId(ChatRoomId);
                    chatMessage.setUserMasterId(UserMasterId);
                    chatMessage.setMessageType("text:");
                    chatMessage.setIsDownloaded("No");
                    if (path != null) {
                        chatMessage.setAttachment(path);
                    }
                    db.AddGroupMessage(chatMessage);
                    getChateDatalist();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (res.contains("Error")) {
                Toast.makeText(AddChatRoomActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                    File f = new File(fileUri.getPath().toString());
                    path = f.toString();
                    Imagefilename = f.getName();

                    chatMessage = new ChatMessage();
                    chatMessage.setMessage(Chat_message);
                    chatMessage.setStatus("Sender");
                    chatMessage.setMessageDate(Starttime);
                    chatMessage.setUsername(UserName);
                    UUID uuid = UUID.randomUUID();
                    String uuidInString = uuid.toString();
                    chatMessage.setMessageId(uuidInString);
                    chatMessage.setChatRoomId(ChatRoomId);
                    chatMessage.setUserMasterId(UserMasterId);
                    chatMessage.setMessageType("text:");
                    chatMessage.setIsDownloaded("No");
                    if (path != null) {
                        chatMessage.setAttachment(path);
                    }
                    cf.AddGroupMessage(chatMessage);
                    AddChatRoomActivity.this.runOnUiThread(new Thread(new Runnable() {
                        public void run() {


                            groupMessage(ChatRoomId, AddChatRoomActivity.this);


                        }

                    }));


                    if (isnet()) {
                        async = new PostUploadImageMethod().execute();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {

            } else {

            }
        } else if (requestCode == SELECT_FILE_IMAGE) {
            if (resultCode == RESULT_OK) {

                fileUri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                    //image_encode = getStringImage(bitmap);
                    File f = new File(getRealPathFromURI(fileUri));

                    path = f.toString();
                    Imagefilename = f.getName();

                    chatMessage = new ChatMessage();
                    chatMessage.setMessage(Chat_message);
                    chatMessage.setStatus("Sender");
                    chatMessage.setMessageDate(Starttime);
                    chatMessage.setUsername(UserName);
                    UUID uuid = UUID.randomUUID();
                    String uuidInString = uuid.toString();
                    chatMessage.setMessageId(uuidInString);
                    chatMessage.setChatRoomId(ChatRoomId);
                    chatMessage.setUserMasterId(UserMasterId);
                    chatMessage.setMessageType("text:");
                    chatMessage.setIsDownloaded("No");
                    if (path != null) {
                        chatMessage.setAttachment(path);
                    }
                    cf.AddGroupMessage(chatMessage);
                    AddChatRoomActivity.this.runOnUiThread(new Thread(new Runnable() {
                        public void run() {


                            groupMessage(ChatRoomId, AddChatRoomActivity.this);


                        }

                    }));


                    if (isnet()) {
                        async = new PostUploadImageMethod().execute();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // previewCapturedImage();

            } else if (resultCode == RESULT_CANCELED) {

            } else {

            }

        } else if (requestCode == PICK_PDF_REQUEST) {
            if (resultCode == RESULT_OK) {
                fileUri = data.getData();
                path = getPath(fileUri);
                File file = new File(path);
                // uploadFile(file);
                path = file.getPath().toString();
                Imagefilename = file.getName();

                File originalFile = new File(path);
                try {
                    FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
                    byte[] bytes = new byte[(int) originalFile.length()];
                    fileInputStreamReader.read(bytes);
                    //     image_encode = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
                    System.out.println("PDFENCODEtt :" + image_encode);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // image_encode=getStringFile(f);

                StringTokenizer tokens = new StringTokenizer(Imagefilename, ":");
                String file_1 = tokens.nextToken().trim();


                chatMessage = new ChatMessage();
                chatMessage.setMessage(Chat_message);
                chatMessage.setStatus("Sender");
                chatMessage.setMessageDate(Starttime);
                chatMessage.setUsername(UserName);
                UUID uuid = UUID.randomUUID();
                String uuidInString = uuid.toString();
                chatMessage.setMessageId(uuidInString);
                chatMessage.setChatRoomId(ChatRoomId);
                chatMessage.setUserMasterId(UserMasterId);
                chatMessage.setMessageType("text:");
                chatMessage.setIsDownloaded("No");
                if (path != null) {
                    chatMessage.setAttachment(path);
                }
                cf.AddGroupMessage(chatMessage);
                AddChatRoomActivity.this.runOnUiThread(new Thread(new Runnable() {
                    public void run() {


                        groupMessage(ChatRoomId, AddChatRoomActivity.this);


                    }

                }));


                if (isnet()) {
                    async = new PostUploadImageMethod().execute();
                }
            } else if (resultCode == RESULT_CANCELED) {

            } else {

            }

        } else if (requestCode == INTENT_PICK_AUDIO) {
            if (resultCode == RESULT_OK) {

                fileUri = data.getData();
                path = getPath(fileUri);
                File file = new File(path);
                // uploadFile(file);
                path = file.getPath().toString();
                Imagefilename = file.getName();

                chatMessage = new ChatMessage();
                chatMessage.setMessage(Chat_message);
                chatMessage.setStatus("Sender");
                chatMessage.setMessageDate(Starttime);
                chatMessage.setUsername(UserName);
                UUID uuid = UUID.randomUUID();
                String uuidInString = uuid.toString();
                chatMessage.setMessageId(uuidInString);
                chatMessage.setChatRoomId(ChatRoomId);
                chatMessage.setUserMasterId(UserMasterId);
                chatMessage.setMessageType("text:");
                chatMessage.setIsDownloaded("No");
                if (path != null) {
                    chatMessage.setAttachment(path);
                }
                cf.AddGroupMessage(chatMessage);
                AddChatRoomActivity.this.runOnUiThread(new Thread(new Runnable() {
                    public void run() {


                        groupMessage(ChatRoomId, AddChatRoomActivity.this);


                    }

                }));

                if (isnet()) {
                    async = new PostUploadImageMethod().execute();
                }

            } else if (resultCode == RESULT_CANCELED) {
            } else {

            }


        } else if (requestCode == REQUEST_VIDEO_TRIMMER) {
            if (resultCode == RESULT_OK) {

                File f = new File(fileUri.getPath().toString());
                path = getPath(fileUri);
                Imagefilename = f.getName();
                videoToBase64(f);

                Toast.makeText(AddChatRoomActivity.this, Imagefilename, Toast.LENGTH_SHORT).show();

                chatMessage = new ChatMessage();
                chatMessage.setMessage(Chat_message);
                chatMessage.setStatus("Sender");
                chatMessage.setMessageDate(Starttime);
                chatMessage.setUsername(UserName);
                UUID uuid = UUID.randomUUID();
                String uuidInString = uuid.toString();
                chatMessage.setMessageId(uuidInString);
                chatMessage.setChatRoomId(ChatRoomId);
                chatMessage.setUserMasterId(UserMasterId);
                chatMessage.setMessageType("text:");
                chatMessage.setIsDownloaded("No");
                if (path != null) {
                    chatMessage.setAttachment(path);
                }
                cf.AddGroupMessage(chatMessage);
                AddChatRoomActivity.this.runOnUiThread(new Thread(new Runnable() {
                    public void run() {


                        groupMessage(ChatRoomId, AddChatRoomActivity.this);


                    }

                }));


                if (isnet()) {

                    async = new PostUploadImageMethod().execute();
                }

            } else if (resultCode == RESULT_CANCELED) {
            } else {

            }


        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                File f = new File(fileUri.getPath().toString());
                path1 = f.toString();
                // Imagefilename = f.getName();


                new VideoCompressor().execute();


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    public Uri getOutputMediaFileUri(int type) {
        requestRuntimePermission();
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes

        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public void requestRuntimePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(AddChatRoomActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddChatRoomActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    public static File getOutputMediaFile(int type) {
        File mediaStorageDir;
        // External sdcard location
        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + timeStamp + ".jpg");

            Log.d("test", "mediaFile" + mediaFile);


        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "Audio_" + timeStamp + ".mp3");
        } else if (type == MEDIA_TYPE_VIDEO_CAPTURE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VIDEO_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "          " + timeStamp + "";
        File storageDir = null;

        storageDir = new File("/sdcard/", IMAGE_DIRECTORY_NAME);

      /*  File image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );
*/

        mediaFile = new File(storageDir + File.separator
                + timeStamp + ".jpg");

        String path = "file:" + mediaFile.getAbsolutePath();
        return mediaFile;

    }

    private String videoToBase64(File file) {


        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            // TODO: handle exception
        }
        byte[] bytes;
        byte[] buffer = new byte[(int) file.length()];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        image_encode = Base64.encodeToString(bytes, Base64.DEFAULT);
        Log.i("Strng", image_encode);

        return image_encode;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void pickFromGalleryvideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video "), INTENT_PICK_AUDIO);

    }

    public String getPathvideo(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/"
                                + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));

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
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection,
                            selectionArgs);
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


    private class VideoCompressor extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return MediaController.getInstance().convertVideo(path1);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            if (compressed) {
                Log.e("Compression", "Compression successfully!");
                path = MediaController.cachedFile.getPath();
                File f = new File(path);
                Imagefilename = f.getName();

                chatMessage = new ChatMessage();
                chatMessage.setMessage(Chat_message);
                chatMessage.setStatus("Sender");
                chatMessage.setMessageDate(Starttime);
                chatMessage.setUsername(UserName);
                UUID uuid = UUID.randomUUID();
                String uuidInString = uuid.toString();
                chatMessage.setMessageId(uuidInString);
                chatMessage.setChatRoomId(ChatRoomId);
                chatMessage.setUserMasterId(UserMasterId);
                chatMessage.setMessageType("text:");
                chatMessage.setIsDownloaded("No");
                if (path != null) {
                    chatMessage.setAttachment(path);
                }
                cf.AddGroupMessage(chatMessage);
                AddChatRoomActivity.this.runOnUiThread(new Thread(new Runnable() {
                    public void run() {


                        groupMessage(ChatRoomId, AddChatRoomActivity.this);


                    }

                }));


                if (isnet()) {

                    async = new PostUploadImageMethod().execute();
                }


            }


        }
    }

  /*  public void uploadFile(File fileName){


        FTPClient client = new FTPClient();

        try {

            client.connect(FTP_HOST,21);
            client.login(FTP_USER, FTP_PASS);
            client.setType(FTPClient.TYPE_BINARY);
           // client.changeDirectory("httpdocs/Advclips/");
            client.changeDirectory("/VrittiWebApplications/Media/");

            client.upload(fileName, new MyTransferListener());

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    *//*******  Used to file upload and show progress  **********//*

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {

            //btn.setVisibility(View.GONE);
            // Transfer started
            Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {

            // Yet other length bytes has been transferred since the last time this
            // method was called
            Toast.makeText(getBaseContext(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
            //System.out.println(" transferred ..." + length);
        }

        public void completed() {

           // btn.setVisibility(View.VISIBLE);
            // Transfer completed

            Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" completed ..." );
        }

        public void aborted() {

            *//*btn.setVisibility(View.VISIBLE);
            // Transfer aborted
            Toast.makeText(getBaseContext()," transfer aborted ,
                    please try again...", Toast.LENGTH_SHORT).show();
            //System.out.println(" aborted ..." );*//*
        }

        public void failed() {


            // Transfer failed
            System.out.println(" failed ..." );
        }

    }
*/
}



