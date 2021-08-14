package com.vritti.chat.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.vritti.chat.adapter.GroupChatRecycleViewAdapter;
import com.vritti.chat.bean.ChatGroup;
import com.vritti.chat.bean.ChatGroupJson;
import com.vritti.chat.bean.ChatMessage;
import com.vritti.chat.bean.ChatModelObject;
import com.vritti.chat.bean.ChatUser;
import com.vritti.chat.bean.DateObject;
import com.vritti.chat.bean.ListObject;
import com.vritti.chat.services.ChattingDataSendBackground;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.databaselib.videocompression.MediaController;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.FileUtilities;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.ekatm.receiver.ConnectivityReceiver;
import com.vritti.ekatm.services.GPSTracker;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Attachment;
import com.vritti.vwb.Beans.TeamChatListObject;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.AssignActivity;
import com.vritti.vwb.vworkbench.Myapplication;

import org.apache.commons.lang3.StringEscapeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import butterknife.ButterKnife;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.codetail.animation.SupportAnimator;
import me.leolin.shortcutbadger.ShortcutBadger;

import static com.vritti.chat.activity.SharefunctionActivity.getRealPathFromUri;
import static org.apache.commons.io.FilenameUtils.getName;


/**
 * Created by sharvari on 30-Oct-17.
 */

public class AddChatRoomActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 2366;
    private static final int SELECT_FILE_IMAGE_theme = 11;
    private static final int REQUEST_PHONE_CALL = 987;
    private static final int REQUEST_READ_CONTACTS = 865;
    private static final int REQUEST_WRITE_CONTACT = 654;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    public static DatabaseHandlers db;

    CommonFunction cf;
    public static Context context;

    int adapterStatus = 0;
    String adapterMsgId = "";
    String Call_Callid, Call_CallType, MessageId;
    RelativeLayout rel_chat;

    public static SQLiteDatabase sql;
    // public static ArrayList<ChatMessage> chatMessageArrayList1;
    public static RecyclerView listview_chatting;
    EmojiconEditText chat_edit_text;
    ImageView enter_chat;
    int fromEnterChat = -1;
    SharedPreferences userpreferences;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;

    String ChatMessageDate = "", Starttime = "", FinalJson, ReceiverName, Message = "", MessageDate = "", Sender, SenderName, Status = "", MessageType = "";

    public static String ChatRoomName, ChatRoomId;
    public static boolean status;
    boolean shouldExecuteOnResume;
    //static int Count;
    static String Active = "1";
    SimpleDateFormat df;
    Calendar c;
    String message;
    ChatGroupJson chatGroupJson;
    ArrayList<ChatGroupJson> chatGroupJsonArrayList;
    public static ArrayList<String> finalChatList;
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
    LinearLayout len_gallery, len_audio, len_video, len_record_video, len_live_location;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int MEDIA_TYPE_VIDEO_CAPTURE = 22;
    public static final int REQUEST_VIDEO_TRIMMER = 8;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int INTENT_PICK_AUDIO = 3;
    String encodedImage, res, image_encode, Imagefilename = "", path = "", path1, videoFileName = "";
    static Context parent;
    Object responsemsg3;
    static File mediaFile;
    public static final int SELECT_FILE_IMAGE = 1;
    private int PICK_PDF_REQUEST = 2;
    int refeshRespSize = -1;
    int frombroadcastreceiver = -1;


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
    private String serverResponseMessage, ActivityDescription = "";
    public static String isRefresh = "false";
    private Date date;
    public static String dotformat = "No";
    String uuidInString;
    long timestamp;
    ProgressBar mprogress;
    public static ArrayList<ChatMessage> chatMessageArrayList1;
    private String MessageID = "", dabasename;
    RecyclerView.LayoutManager layoutManager;
    public static GroupChatRecycleViewAdapter groupChatRecycleViewAdapter;
    boolean isFocuse = false;
    RelativeLayout copyOptionLayout;
    /* download variable*/
    String downloadChatRoomId, downloadImageUrl, downloadMessageId, filename, file1;
    Object responsemsg;
    File imgFile, pdfFile;
    ChattingDataSendBackground chattingDataSendBackground;
    Bitmap bitmap;
    private Toolbar toolbar;
    TeamChatListObject teamChatListObject;
    public static ArrayList<ListObject> consolidatedList = new ArrayList<>();
    private int unreadPos = -1;

    /*reply variable*/
    RelativeLayout replyLayout;
    TextView userNameText, replyText;
    ImageView closeImg;
    SimpleDraweeView replyImage;
    boolean isReply = false;
    String replyMessageId = null;


    ///////////////////
    AppBarLayout appbar_chat;
    ImageView img_reply, img_assign, img_copy, img_forward, img_delete;
    private int msgPosition;
    private String ChatMessageId = "", ChatChatRoomId = "";
    private AlertDialog alertDialog;

    SimpleDraweeView themeSDV;
    private String User = "";
    boolean replyTypeIsImage = false;

    int listCount = 0;
    ArrayList<ChatMessage> chatMessageArrayListTotal;
    int firstSlotRefreshData = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_chat_activity_main);
        ButterKnife.bind(this);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        teamChatListObject = new TeamChatListObject();
        finalChatList = new ArrayList<>();
        Call_Callid = intent.getStringExtra("callid");
        Call_CallType = intent.getStringExtra("call_type");
        ChatRoomId = intent.getStringExtra("ChatRoomid");
        ChatRoomName = intent.getStringExtra("Chatroomname");
        Status = intent.getStringExtra("group_status");
        Create_check = intent.getStringExtra("value_chat");
        ProjectMasterID = intent.getStringExtra("projmasterId");
        CStatus = intent.getStringExtra("status");
        copyOptionLayout = findViewById(R.id.copyOptionLayout);
        replyLayout = findViewById(R.id.replyLayout);
        replyImage = findViewById(R.id.replyImage);
        userNameText = findViewById(R.id.userNameText);
        replyText = findViewById(R.id.replyText);
        closeImg = findViewById(R.id.closeImg);
        themeSDV = findViewById(R.id.themeSDV);
        /*if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("Message_Back", "Key: " + key + " Value: " + value);
            }
        }*/


        if (CStatus == null) {
            CStatus = "Created";
        }

        path = intent.getStringExtra("share_image_path");
        Imagefilename = intent.getStringExtra("share_imagename");


        bottomlayout = (LinearLayout) findViewById(R.id.bottomlayout);
        cordinate_view = findViewById(R.id.cocordinate_view);
        txt_chat_title = findViewById(R.id.txt_group_title);
        mprogress = findViewById(R.id.toolbar_progress_App_bar);
        chatMessageArrayList1 = new ArrayList<ChatMessage>();
        replyLayout.setVisibility(View.GONE);

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
        chatMessageArrayList1 = new ArrayList<>();
        context = AddChatRoomActivity.this;
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        sql = db.getWritableDatabase();

        context = AddChatRoomActivity.this;

        c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.AM_PM, Calendar.PM);
        System.out.println("Current time => " + c.getTime());


        Long tsLong = System.currentTimeMillis();
        timestamp = tsLong;
        Date date = new Date(timestamp);


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        Starttime = dateFormat.format(new Date()).toString();
        System.out.println(Starttime);


        listview_chatting = findViewById(R.id.listview_chatting);
        rel_chat = (RelativeLayout) findViewById(R.id.rel_chat);
        chat_edit_text = (EmojiconEditText) findViewById(R.id.chat_edit_text);
        enter_chat = findViewById(R.id.enter_chat);
        attachment = (ImageView) findViewById(R.id.attachment);
        attachment_video = findViewById(R.id.attachment_video);
        progress = (ProgressBar) findViewById(R.id.progress);
        chatUserArrayList = new ArrayList<>();
        //Chat Attachment
        revealLayout2 = (LinearLayout) findViewById(R.id.reveal_items2);
        revealLayout2.setVisibility(View.INVISIBLE);

        len_document = (LinearLayout) findViewById(R.id.len_document);
        len_audio = (LinearLayout) findViewById(R.id.len_audio);
        len_video = (LinearLayout) findViewById(R.id.len_video);
        len_record_video = (LinearLayout) findViewById(R.id.len_record_video);
        len_live_location = (LinearLayout) findViewById(R.id.len_live_location);
        layoutManager = new LinearLayoutManager(this);
        if (getIntent().hasExtra("share_description")) {
            intent = getIntent();
            ActivityDescription = intent.getStringExtra("share_description");
            enter_chat.setImageDrawable(getResources().getDrawable(R.drawable.send));
            chat_edit_text.setText(ActivityDescription);
            if (!chat_edit_text.getText().toString().trim().equals("")) {
                enter_chat.setImageDrawable(getResources().getDrawable(R.drawable.send_icon));
            } else {
                enter_chat.setImageDrawable(getResources().getDrawable(R.drawable.keyboard_icon));
            }
        }
        appbar_chat = findViewById(R.id.appbar_chat);
        img_assign = findViewById(R.id.img_assign);
        if (Constants.type == Constants.Type.Sahara) {
            img_assign.setVisibility(View.GONE);
        } else {
            img_assign.setVisibility(View.VISIBLE);
        }
        img_copy = findViewById(R.id.img_copy);
        img_forward = findViewById(R.id.img_forward);
        img_reply = findViewById(R.id.img_reply);
        img_delete = findViewById(R.id.img_delete);
        //groupChatRecycleViewAdapter = new GroupChatRecycleViewAdapter(consolidatedList, context);
        groupChatRecycleViewAdapter = new GroupChatRecycleViewAdapter(consolidatedList, context);
        listview_chatting.setLayoutManager(layoutManager);
        listview_chatting.setAdapter(groupChatRecycleViewAdapter);

        if (CStatus != null && CStatus.equalsIgnoreCase("Closed")) {

        } else {
            if (path == null || path.equalsIgnoreCase("")) {

            } else {
                if (path != null & Imagefilename != null) {
                    if (isnet()) {
                        chatMessage = new ChatMessage();
                        chatMessage.setMessage(Chat_message);
                        chatMessage.setStatus("Sender");
                        chatMessage.setMessageDate(timestamp);
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
                        async = new PostUploadImageMethod().execute(uuidInString);
                    }
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
                // Toast.makeText(AddChatRoomActivity.this , "Under Development" , Toast.LENGTH_SHORT).show();
            }
        });

        len_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealLayout2.setVisibility(View.INVISIBLE);
                hidden = true;
                isRefresh = "false";
                // pickFromGalleryvideo();
                getLocationFromService();

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
                        addMoreImages();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyLayout.setVisibility(View.GONE);
                replyText.setText("");
                userNameText.setText("");
                isReply = false;
                replyMessageId = null;
            }
        });


        if (cf.getChatmessagecount() > 0) {
            groupMessage(ChatRoomId, AddChatRoomActivity.this);
        } else {
            if (isnet()) {
                showProgress();
                new StartSession(AddChatRoomActivity.this, new CallbackInterface() {
                    @Override

                    public void callMethod() {
                        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = new Date();
                        String CurrentDate = dfDate.format(d);
                        ChatRoomMessageRetrive chatRoomMessageRetrive = new ChatRoomMessageRetrive();
                        chatRoomMessageRetrive.execute(CurrentDate);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        dismissProgress();
                    }
                });

            }

        }


        if (Create_check != null) {
            ChatUserUpdatList(ChatRoomId);
        } else {

        }
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            txt_chat_title.setText(ChatRoomName);
            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (chat_edit_text.getText().toString().trim().equals(""))
            enter_chat.setImageDrawable(getResources().getDrawable(R.drawable.keyboard_icon));
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

                    enter_chat.setImageDrawable(getResources().getDrawable(R.drawable.send_icon));
                } else {
                    enter_chat.setImageDrawable(getResources().getDrawable(R.drawable.keyboard_icon));

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
        img_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setCopyOptionLayout(msgPosition, ChatRoomId, AddChatRoomActivity.this);
                Toast.makeText(AddChatRoomActivity.this, "Message Copied", Toast.LENGTH_SHORT).show();
                appbar_chat.setVisibility(View.GONE);


            }
        });
        img_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!replyTypeIsImage)
                    setassginWorkText(msgPosition, ChatRoomId, AddChatRoomActivity.this);
                appbar_chat.setVisibility(View.GONE);


            }
        });
        img_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!replyTypeIsImage)
                    setForwardMessage(msgPosition, ChatRoomId, AddChatRoomActivity.this);
                appbar_chat.setVisibility(View.GONE);
            }
        });

        img_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!replyTypeIsImage)
                    setReply(msgPosition, ChatRoomId, AddChatRoomActivity.this);
                else
                    setReplyForImage(msgPosition, ChatRoomId, AddChatRoomActivity.this);
                appbar_chat.setVisibility(View.GONE);
            }
        });

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteMessage(msgPosition, ChatRoomId, AddChatRoomActivity.this);
                appbar_chat.setVisibility(View.GONE);


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
                fromEnterChat = 1;
                isFocuse = false;
                if (validate()) {

                    Chat_message = chat_edit_text.getText().toString();
                    Chat_message = Chat_message.replaceAll("'", "");


                    JSONObject jsonObject = new JSONObject();
                    onResume();
                    try {

                        jsonObject.put("ChatRoomId", ChatRoomId);
                        jsonObject.put("senderId", UserMasterId);
                        jsonObject.put("senderName", UserName);
                        String MessageEncode = StringEscapeUtils.escapeJava(Chat_message);
                        jsonObject.put("senderMSG", MessageEncode);
                        if (!isReply)
                            jsonObject.put("senderMSGType", "text:");
                        else
                            jsonObject.put("senderMSGType", "reply:" + replyMessageId);
                        jsonObject.put("senderMSGtime", Starttime);
                        jsonObject.put("Attachment", Attachment);
                        jsonObject.put("pos", consolidatedList.size());
                        UUID uuid = UUID.randomUUID();
                        uuidInString = uuid.toString();
                        jsonObject.put("UUID", uuidInString);

                        FinalJson = jsonObject.toString();
                        chatGroupJson = new ChatGroupJson();
                        chatGroupJson.setFinalJsonGroup(FinalJson);
                        cf.AddGrouMessageJson(chatGroupJson);
                        if (!ConnectivityReceiver.isConnected()) {
                            if (new Gson().fromJson(AppCommon.getInstance(context).getChatJson(), TeamChatListObject.class) != null) {
                                teamChatListObject = new Gson().fromJson(AppCommon.getInstance(context).getChatJson(), TeamChatListObject.class);
                                finalChatList = teamChatListObject.getFinalJsonList();
                                finalChatList.add(FinalJson);
                                teamChatListObject.setFinalJsonList(finalChatList);
                                AppCommon.getInstance(context).setChatJson(new Gson().toJson(teamChatListObject));
                            } else {
                                finalChatList.add(FinalJson);
                                teamChatListObject.setFinalJsonList(finalChatList);
                                AppCommon.getInstance(context).setChatJson(new Gson().toJson(teamChatListObject));
                            }

                        } else {

                            chatGroupJson.setMessage_id(uuidInString);
                            chatGroupJsonArrayList.add(chatGroupJson);
                        }

                        chatMessage = new ChatMessage();
                        chatMessage.setMessage(Chat_message);
                        chatMessage.setStatus("Sender");
                        chatMessage.setMessageDate(timestamp);
                        chatMessage.setUsername(UserName);
                        uuidInString = uuid.toString();
                        chatMessage.setMessageId(uuidInString);
                        chatMessage.setChatRoomId(ChatRoomId);
                        chatMessage.setUserMasterId(UserMasterId);
                        if (!isReply)
                            chatMessage.setMessageType("text:");
                        else
                            chatMessage.setMessageType("reply:" + replyMessageId);

                        chatMessage.setAttachment("");
                        // chatMessage.setIsDownloaded("No");
                        chatMessage.setIsDownloaded("wait");
                        chatMessageArrayList1.add(chatMessage);
                        ChatModelObject generalItem = new ChatModelObject();
                        generalItem.setChatMessage(chatMessage);
                        consolidatedList.add(generalItem);

                        Intent intent = new Intent(AddChatRoomActivity.this, ChattingDataSendBackground.class);
                        intent.putExtra("pos", consolidatedList.size() - 1);
                        startService(intent);
                        cf.AddGroupMessage(chatMessage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    chat_edit_text.setText("");
                    groupDataIntoHashMap(chatMessageArrayList1, ChatRoomId);
                    // groupMessage(ChatRoomId, AddChatRoomActivity.this);
                }
                if (replyLayout.getVisibility() == View.VISIBLE) {
                    replyLayout.setVisibility(View.GONE);
                    replyText.setText("");
                    userNameText.setText("");
                    isReply = false;
                    replyMessageId = null;
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

        len_live_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealLayout2.setVisibility(View.INVISIBLE);
                hidden = true;
                isRefresh = "false";
                startActivity(new Intent(AddChatRoomActivity.this, ChatRoomLiveLocationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("ChatRoomId", ChatRoomId));

            }
        });

        if (AppCommon.getInstance(this).getThemeURI() != null) {
            //Drawable drawable=Drawable.createFromPath(AppCommon.getInstance(this).getThemeURI());
            //listview_chatting.setBackground(drawable);
            //themeSDV.setImageURI(AppCommon.getInstance(this).getThemeURI());
            themeSDV.setController(AppCommon.getDraweeController(themeSDV, AppCommon.getInstance(this).getThemeURI(), 500));
            /*Uri imageUri = Uri.parse(AppCommon.getInstance(this).getThemeURI());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Drawable dr = new BitmapDrawable(bitmap);
                listview_chatting.setBackgroundDrawable(dr);
            } catch (IOException e) {
                e.printStackTrace();
            }*/


        }


    }

    private void setForwardMessage(int msgposition, String chatRoomId, AddChatRoomActivity addChatRoomActivity) {
        if (msgposition != -1) {
            String message = ((ChatModelObject) consolidatedList.get(msgposition)).getChatMessage().getMessage();
            Intent intent = new Intent();
            intent.putExtra("Description", message);
            setResult(267, intent);
            //startActivity(intent);
            finish();
        }

    }

    private void addMoreImages() {
// recordVideo();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_option_dialog);
        dialog.setTitle(getResources().getString(R.string.app_name));
        TextView camera = (TextView) dialog.findViewById(R.id.camera);
        TextView gallery = (TextView) dialog.findViewById(R.id.gallery);
        TextView textViewCancel = (TextView) dialog.findViewById(R.id.cancel);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
                dialog.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGalleryPermission();
                dialog.dismiss();
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                    200);
        } else {
            recordVideo();
        }
    }

    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    201);
        } else {
            startGalleryIntent();
        }
    }


    private void startGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);

    }

    private void getLocationFromService() {
        if (isnet()) {
            GPSTracker gpsTracker = new GPSTracker(this);
            gpsTracker.getLocation();

            double lat = gpsTracker.getLatitude();
            double lang = gpsTracker.getLongitude();
            String userName = UserName;
            String locationAddress = "";
            if (lat != 0.0 && lang != 0.0)
                locationAddress = gpsTracker.GetCurrentLocation(lat, lang, this);
            if (!locationAddress.equals("")) {
                //chat_edit_text.setText(locationAddress);

                String baseUrl = "https://maps.googleapis.com/maps/api/staticmap?center=";
                String strLocation = String.valueOf(lat) + "," + String.valueOf(lang);
                String baseUrl2 = "&zoom=16&size=600x300&maptype=roadmap&markers=color:red%7C";
                String srtKey = "&key=";
                String key = getResources().getString(R.string.google_map_key);
                path = baseUrl + strLocation + baseUrl2 + strLocation + srtKey + key;
                isRefresh = "false";
                isFocuse = false;
                JSONObject jsonObject = new JSONObject();
                onResume();
                try {

                    jsonObject.put("ChatRoomId", ChatRoomId);
                    jsonObject.put("senderId", UserMasterId);
                    jsonObject.put("senderName", UserName);
                    String MessageEncode = StringEscapeUtils.escapeJava(path);
                    jsonObject.put("senderMSG", MessageEncode);
                    jsonObject.put("senderMSGType", "map:");
                    jsonObject.put("senderMSGtime", Starttime);
                    jsonObject.put("Attachment", Attachment);
                    jsonObject.put("pos", consolidatedList.size());
                    UUID uuid = UUID.randomUUID();
                    uuidInString = uuid.toString();
                    jsonObject.put("UUID", uuidInString);

                    FinalJson = jsonObject.toString();
                    chatGroupJson = new ChatGroupJson();
                    chatGroupJson.setFinalJsonGroup(FinalJson);

                    cf.AddGrouMessageJson(chatGroupJson);
                    chatGroupJson.setMessage_id(uuidInString);
                    chatGroupJsonArrayList.add(chatGroupJson);


                    chatMessage = new ChatMessage();
                    chatMessage.setMessage(path);
                    chatMessage.setStatus("Sender");
                    chatMessage.setMessageDate(timestamp);
                    chatMessage.setUsername(UserName);
                    uuidInString = uuid.toString();
                    chatMessage.setMessageId(uuidInString);
                    chatMessage.setChatRoomId(ChatRoomId);
                    chatMessage.setUserMasterId(UserMasterId);
                    chatMessage.setMessageType("map:");
                    chatMessage.setAttachment("");
                    chatMessage.setIsDownloaded("No");
                    chatMessageArrayList1.add(chatMessage);
                    ChatModelObject generalItem = new ChatModelObject();
                    generalItem.setChatMessage(chatMessage);
                    consolidatedList.add(generalItem);
                    Intent intent = new Intent(AddChatRoomActivity.this, ChattingDataSendBackground.class);
                    intent.putExtra("pos", consolidatedList.size() - 1);
                    startService(intent);
                    cf.AddGroupMessage(chatMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                chat_edit_text.setText("");
                //groupMessage(ChatRoomId, AddChatRoomActivity.this);

                groupDataIntoHashMap(chatMessageArrayList1, ChatRoomId);


            } else {
                Toast.makeText(this, "Sorry location not found", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "please check your internet connection!", Toast.LENGTH_SHORT).show();
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

        hidden = true;
        ContentValues values = new ContentValues();
        values.put("Count", "0");
        sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});


        if (revealLayout2.getVisibility() == View.VISIBLE) {
            revealLayout2.setVisibility(View.INVISIBLE);
            hidden = true;
        } else if (appbar_chat.isShown()) {
            appbar_chat.setVisibility(View.GONE);
        } else {
            ChatRoomName = "";
            ChatRoomId = "";

            getIntent().removeExtra("share_image_path");
            getIntent().removeExtra("share_imagename");
            getIntent().removeExtra("share_description");
            getIntent().removeExtra("ChatRoomid");

            // startActivity(new Intent(AddChatRoomActivity.this, OpenChatroomActivity.class));
            /*Intent intent = new Intent();
            setResult(Activity.RESULT_OK,intent);*/
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
            dialog.setContentView(R.layout.vwb_chat_data_menu);

            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;

            wlp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            TextView txt_chatroom = (TextView) dialog.findViewById(R.id.txt_chatroom);
            TextView txt_refresh = (TextView) dialog.findViewById(R.id.txt_refresh);
            TextView txt_theme = (TextView) dialog.findViewById(R.id.txt_theme);
            if (Create_check == null) {
                txt_chatroom.setVisibility(View.GONE);
            } else {
                if (Create_check.equals("1")) {
                    //  txt_chatroom.setVisibility(View.VISIBLE);
                }
            }
            txt_theme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_FILE_IMAGE_theme);
                    dialog.dismiss();
                }
            });

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
                    chatMessageArrayList1.clear();
                    refeshRespSize = -1;
                    firstSlotRefreshData = -1;


                    sql.delete(db.TABLE_CHAT_GROUP_MESSAGE, "UserMasterId=?",
                            new String[]{UserMasterId});


                    if (isnet()) {
                        showProgress();

                        new StartSession(AddChatRoomActivity.this, new CallbackInterface() {
                            @Override

                            public void callMethod() {
                                SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
                                Date d = new Date();
                                String CurrentDate = dfDate.format(d);

                                ChatRoomMessageRetrive chatRoomMessageRetrive = new ChatRoomMessageRetrive();
                                chatRoomMessageRetrive.execute(CurrentDate);
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                dismissProgress();
                            }
                        });

                    }


                    dialog.dismiss();
                    //sql.close();
                }
            });

            dialog.show();
            return true;
        }

        return (super.onOptionsItemSelected(menuItem));
    }


    public void groupMessage(final String chatRoomId, Context context) {
        try {

            if (consolidatedList != null) {
                consolidatedList.clear();
            }

            sql = db.getWritableDatabase();

            String Status;

         /*   if (listCount == 0) {
                chatMessageArrayList1.clear();
            }*/
            //  String query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGES + " WHERE  ChatRoomId ='" + chatRoomId + "'
            //  order by MessageDate asc ";
            //  String query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE + " WHERE  ChatRoomId ='" + chatRoomId + "'";
            String query = "";
            query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE + " WHERE ChatRoomId = '" + chatRoomId +
                    "' order by MessageDate asc ";
         /*   if (refeshRespSize == -1) {
                query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE + " WHERE ChatRoomId = '" + chatRoomId +
                        "' order by MessageDate asc ";
                refeshRespSize = 1;
            } else {
                query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE + " WHERE ChatRoomId = '" + chatRoomId +
                        "' order by MessageDate desc ";
            }*/
       /*     String query = "SELECT Distinct ChatRoomId,AddedBy,StartTime,ChatRoomName,ChatRoomStatus,Creator,Count,ChatSourceId,ChatType,ChatMessage FROM " +
                    db.TABLE_CHAT_CHATROOM_GROUP_LIST + " WHERE  UserMasterId ='" + userMasterId + "'order by StartTime desc ";*/
            Cursor cur = sql.rawQuery(query, null);
            int Count = cur.getCount();


            if (Count > 0) {
                cur.moveToFirst(); //cur.moveToFirst
                do {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                    chatMessage.setChatRoomId(cur.getString(cur.getColumnIndex("ChatRoomId")));

                    String Msg = cur.getString(cur.getColumnIndex("Message"));
                    if (Msg != null) {
                        Msg = Msg.replaceAll("u0026", "&");
                    }
                    String Username1 = cur.getString(cur.getColumnIndex("UserName"));
                    chatMessage.setUsername(Username1);
                    chatMessage.setMessage(Msg);
                    long MessageDate = cur.getLong(cur.getColumnIndex("MessageDate"));
                    chatMessage.setMessageDate(MessageDate);
                    //chatMessage.setUsername(cur.getString(cur.getColumnIndex("UserName")));
                    Status = cur.getString(cur.getColumnIndex("Status"));
                    chatMessage.setStatus(Status);
                    chatMessage.setMessageType(cur.getString(cur.getColumnIndex("MessageType")));
                    chatMessage.setMessageId(cur.getString(cur.getColumnIndex("MessageId")));
                    String msgId = cur.getString(cur.getColumnIndex("MessageId"));
                    chatMessage.setAttachment(cur.getString(cur.getColumnIndex("Attachment")));
                    chatMessage.setIsDownloaded(cur.getString(cur.getColumnIndex("IsDownloaded")));
                    //  chatMessageArrayList1.add(chatMessage);

                    if (firstSlotRefreshData == -1) {
                        chatMessageArrayList1.add(chatMessage);
                    } else {
                        if (chatMessageArrayList1.size() != 0) {
                            int p = -1;
                            for (int i = 0; i < chatMessageArrayList1.size(); i++) {
                                if (msgId.equals(chatMessageArrayList1.get(i).getMessageId())) {
                                    p = 1;
                                    break;
                                } else {
                                    p = -1;
                                }
                            }

                            if (p == -1) {
                                if (frombroadcastreceiver == 1) {
                                    chatMessageArrayList1.add(chatMessage);
                                } else {
                                    chatMessageArrayList1.add(0,chatMessage);
                                }

                            }
                        }


                    }


                    Message = Msg;
                    ChatMessageDate = String.valueOf(MessageDate);
                    User = Username1;

                    if (Message.equals("")) {
                        Message = "#File";
                    } else {
                        Message = Msg;
                    }

                    ContentValues values = new ContentValues();

                    if (UserName.equals(User)) {
                        values.put("ChatMessage", StringEscapeUtils.unescapeJava(Message));
                        values.put("StartTime", "/Date(" + ChatMessageDate + ")/");
                        sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                    } else {
                        values.put("ChatMessage", User + " :- " + StringEscapeUtils.unescapeJava(Message));
                        values.put("StartTime", "/Date(" + ChatMessageDate + ")/");
                        sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                    }


                } while (cur.moveToNext()); //cur.moveToNext.
                firstSlotRefreshData = 1;
                refeshRespSize = 1;
                groupDataIntoHashMap(chatMessageArrayList1, chatRoomId);
                //groupChatRecycleViewAdapter.updateList(chatMessageArrayList1);


            } else {

                if (isnet()) {

                    new StartSession(AddChatRoomActivity.this, new CallbackInterface() {
                        @Override

                        public void callMethod() {
                            SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
                            Date d = new Date();
                            String CurrentDate = dfDate.format(d);
                            ChatRoomMessageRetrive chatRoomMessageRetrive = new ChatRoomMessageRetrive();
                            chatRoomMessageRetrive.execute(CurrentDate);

                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }

                //Toast.makeText(getApplicationContext(), "No chat to dispaly", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // sql.close();
        }
    }

    private void groupDataIntoHashMap(ArrayList<ChatMessage> chatMessageArrayList1, String chatRoomId) {
        //sql.close();
        sql = db.getWritableDatabase();
        LinkedHashMap<String, Set<ChatMessage>> groupedHashMap = new LinkedHashMap<>();
        Set<ChatMessage> list = null;

        for (ChatMessage chatModel : chatMessageArrayList1) {
            //Log.d(TAG, travelActivityDTO.toString());
            String hashMapKey = "";

            if (chatModel.getIsDownloaded().contains("isRead")) {
                if (AppCommon.getInstance(this).getUnReadCount().equals("1"))
                    hashMapKey = "Unread message";
                else {
                    if (AppCommon.getInstance(this).getUnReadCount().equals("0")) {
                        String unreadCountValue = getUnreadCount(chatRoomId);
                        AppCommon.getInstance(this).setUnReadCount(unreadCountValue);
                        hashMapKey = AppCommon.getInstance(this).getUnReadCount() + " Unread messages";
                    } else
                        hashMapKey = AppCommon.getInstance(this).getUnReadCount() + " Unread messages";
                }
                ContentValues values = new ContentValues();
                values.put("IsDownloaded", "No");
                sql.update(db.TABLE_CHAT_GROUP_MESSAGE, values, "MessageId=?", new String[]{chatModel.getMessageId()});
            } else
                hashMapKey = convertDateToString(chatModel.getMessageDate());
            //Log.d(TAG, "start date: " + DateParser.convertDateToString(travelActivityDTO.getStartDate()));
            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(chatModel);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                list = new LinkedHashSet<>();
                list.add(chatModel);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        AppCommon.getInstance(this).setUnReadCount("0");
        ContentValues values = new ContentValues();
        values.put("Count", "0");
        sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
        //Generate list from map
        Log.d("Done", "Work");
        int tempCount = 0;
        AppCommon.getInstance(this).setNotificationCount(tempCount);
        generateListFromMap(groupedHashMap);
        //groupChatRecycleViewAdapter.updateListTotal(chatMessageArrayListTotal);
        groupChatRecycleViewAdapter.notifyDataSetChanged();
        dismissProgress();
        if (!isFocuse) {
            if (consolidatedList.size() != 0) {
                if (unreadPos == -1) {
                    if (adapterStatus == 0) {
                        listview_chatting.scrollToPosition(consolidatedList.size() - 1);
                    } else {
                        listview_chatting.scrollToPosition(chatMessageArrayList1.size());
                    }
                } else
                    listview_chatting.scrollToPosition(unreadPos);

                unreadPos = -1;
            }
        }
    }


    public void groupDataIntoHashMap1(ChatMessage chatMessage, String chatRoomId, ArrayList<ChatMessage> chatMessageArrayListTotal) {
        try {

            if (consolidatedList != null) {
                consolidatedList.clear();
            }

            chatMessageArrayListTotal = new ArrayList<>();
            sql = db.getWritableDatabase();

            String Status;

            if (listCount == 0) {
                chatMessageArrayList1.clear();
            }
            //  String query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGES + " WHERE  ChatRoomId ='" + chatRoomId + "' order by MessageDate asc ";
            String query1 = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE + " WHERE  ChatRoomId ='" + chatRoomId + "'";
            String query = "SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE + " WHERE ChatRoomId = '" + chatRoomId +
                    "' order by MessageDate asc ";
       /*     String query = "SELECT Distinct ChatRoomId,AddedBy,StartTime,ChatRoomName,ChatRoomStatus,Creator,Count,ChatSourceId,ChatType,ChatMessage FROM " +
                    db.TABLE_CHAT_CHATROOM_GROUP_LIST + " WHERE  UserMasterId ='" + userMasterId + "'order by StartTime desc ";*/
            Cursor cur1 = sql.rawQuery(query1, null);
            Cursor cur = sql.rawQuery(query, null);
            int Count = cur.getCount();
            listCount = listCount + 9;
            if (Count > 0) {
                cur.moveToFirst(); //cur.moveToFirst
                do {
                    chatMessage = new ChatMessage();
                    chatMessage.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                    chatMessage.setChatRoomId(cur.getString(cur.getColumnIndex("ChatRoomId")));

                    String Msg = cur.getString(cur.getColumnIndex("Message"));
                    if (Msg != null) {
                        Msg = Msg.replaceAll("u0026", "&");
                    }
                    String Username1 = cur.getString(cur.getColumnIndex("UserName"));
                    chatMessage.setUsername(Username1);
                    chatMessage.setMessage(Msg);
                    long MessageDate = cur.getLong(cur.getColumnIndex("MessageDate"));
                    chatMessage.setMessageDate(MessageDate);
                    //chatMessage.setUsername(cur.getString(cur.getColumnIndex("UserName")));
                    Status = cur.getString(cur.getColumnIndex("Status"));
                    chatMessage.setStatus(Status);
                    chatMessage.setMessageType(cur.getString(cur.getColumnIndex("MessageType")));
                    chatMessage.setMessageId(cur.getString(cur.getColumnIndex("MessageId")));
                    chatMessage.setAttachment(cur.getString(cur.getColumnIndex("Attachment")));
                    chatMessage.setIsDownloaded(cur.getString(cur.getColumnIndex("IsDownloaded")));

                    chatMessageArrayListTotal.add(chatMessage);

                    if (chatMessageArrayList1.size() < listCount) {

                        chatMessageArrayList1.add(chatMessage);
                    } else {
                        //break;
                    }

                    Message = Msg;
                    ChatMessageDate = String.valueOf(MessageDate);
                    User = Username1;

                    if (Message.equals("")) {
                        Message = "#File";
                    } else {
                        Message = Msg;
                    }

                    ContentValues values = new ContentValues();

                    if (UserName.equals(User)) {
                        values.put("ChatMessage", StringEscapeUtils.unescapeJava(Message));
                        values.put("StartTime", "/Date(" + ChatMessageDate + ")/");
                        sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                    } else {
                        values.put("ChatMessage", User + " :- " + StringEscapeUtils.unescapeJava(Message));
                        values.put("StartTime", "/Date(" + ChatMessageDate + ")/");
                        sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                    }


                } while (cur.moveToNext()); //cur.moveToNext.

                groupDataIntoHashMap(chatMessageArrayList1, chatRoomId);
                //groupChatRecycleViewAdapter.updateList(chatMessageArrayList1);


            } else {

                if (isnet()) {

                    new StartSession(AddChatRoomActivity.this, new CallbackInterface() {
                        @Override

                        public void callMethod() {
                            SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
                            Date d = new Date();
                            String CurrentDate = dfDate.format(d);
                            ChatRoomMessageRetrive chatRoomMessageRetrive = new ChatRoomMessageRetrive();
                            chatRoomMessageRetrive.execute(CurrentDate);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }

                //Toast.makeText(getApplicationContext(), "No chat to dispaly", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // sql.close();
        }

    }

    private String getUnreadCount(String chatRoomId) {
        String count = "0";
        String query = "SELECT Count FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST + " WHERE  ChatRoomId ='" + chatRoomId + "'";
        Cursor cur = sql.rawQuery(query, null);
        int Count = cur.getCount();
        if (Count > 0) {
            cur.moveToFirst(); //cur.moveToFirst
            do {
                count = (cur.getString(cur.getColumnIndex("Count")));

            } while (cur.moveToNext()); //cur.moveToNext.
        }
        return count;
    }

    private ArrayList<ListObject> generateListFromMap(LinkedHashMap<String, Set<ChatMessage>> groupedHashMap) {
        // We linearly add every item into the consolidatedList.

        consolidatedList.clear();
        ArrayList<Integer> tempList = new ArrayList<>();
        int tempPos = 0;
        for (String date : groupedHashMap.keySet()) {
            DateObject dateItem = new DateObject();
            dateItem.setDate(date);
            consolidatedList.add(dateItem);
            if (dateItem.getDate().contains("Unread message"))
                unreadPos = consolidatedList.size() - 1;
            for (ChatMessage chatModel : groupedHashMap.get(date)) {
                ChatModelObject generalItem = new ChatModelObject();

                if (chatModel.getIsDownloaded().contains("isRead"))
                    chatModel.setIsDownloaded("NO");
                generalItem.setChatMessage(chatModel);
                consolidatedList.add(generalItem);

                // tempList.add(tempPos);
            }
        }


        groupChatRecycleViewAdapter.updateList(consolidatedList);
        // chatAdapter.setDataChanroupe(consolidatedList);

        return consolidatedList;
    }

    private String convertDateToString(long messageDate) {
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = new Date(messageDate);
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        if (targetFormat.format(currentDate).equals(targetFormat.format(date))) {
            return "Today";
        } else
            return targetFormat.format(date);
    }


    BroadcastReceiver mMyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra("message");
            ChatRoomId = intent.getStringExtra("ChatRoomid");
            ChatRoomName = intent.getStringExtra("Chatroomname");

            fromEnterChat = 1;
            refeshRespSize = 1;
            frombroadcastreceiver = 1;
            //    firstSlotRefreshData = -1;
            //onResume();
            if (message.equals("1")) {
                groupMessage(ChatRoomId, AddChatRoomActivity.this);
                ContentValues values = new ContentValues();
                values.put("Count", "0");
                sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                getSupportActionBar().setTitle(ChatRoomName);
            } else {
                if (message.equals("0")) {
                    ContentValues values = new ContentValues();
                    values.put("Count", "0");
                    sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, values, "ChatRoomId=?", new String[]{ChatRoomId});
                    groupMessage(ChatRoomId, AddChatRoomActivity.this);
                }
            }
        }
    };


    public boolean validate() {
        String chatStr = chat_edit_text.getText().toString().trim();
        if (chatStr.isEmpty()) {
            // Toast.makeText(context, "Enter " + setup("Firmname"), Toast.LENGTH_LONG).show();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm.isAcceptingText()) {
                AppCommon.getInstance(this).showHideKeyBoard(this);
                //AppCommon.getInstance(this).onHideKeyBoard(this);
            } else {
                AppCommon.getInstance(this).onHideKeyBoard(this);
                // AppCommon.getInstance(this).showHideKeyBoard(this);
            }

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
        Myapplication.getInstance().setConnectivityListener(this);

        c = Calendar.getInstance();
       /* c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.AM_PM, Calendar.PM);
       */
        // df = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");


        Long tsLong = System.currentTimeMillis();
        timestamp = tsLong;
        Date date = new Date(timestamp);


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        Starttime = dateFormat.format(new Date()).toString();
        System.out.println(Starttime);
        // Starttime = df.format(c.getTime());

        if (Starttime.contains("a.m.")) {
            String a = Starttime.replace("a.m.", "AM");
            Starttime = a;
        } else if (Starttime.contains("p.m.")) {
            String a = Starttime.replace("p.m.", "PM");
            Starttime = a;
        } else {
            Starttime = dateFormat.format(c.getTime());

        }


      /*  SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy K:mm aa");
        current_time = df1.format(c.getTime());
*/

        int tempCount = 0;

        AppCommon.getInstance(this).setNotificationCount(tempCount);


    }


    public void setDownload(int position, String isDownloaded, String ChatRoomId, Context context) {
        if (isDownloaded.equals("No")) {
            ((ChatModelObject) consolidatedList.get(position)).getChatMessage().setDownload(true);
            ChatMessage chatMessage = ((ChatModelObject) consolidatedList.get(position)).getChatMessage();
            String chatRoomId = chatMessage.getChatRoomId();
            String downloadImageUrl = chatMessage.getAttachment();
            String messageId = chatMessage.getMessageId();
            groupChatRecycleViewAdapter.notifyDataSetChanged();
            DownloadActivity downloadActivity = new DownloadActivity(context, position, chatRoomId, downloadImageUrl, messageId, "image");
        } else {
            ((ChatModelObject) consolidatedList.get(position)).getChatMessage().setDownload(false);
            ((ChatModelObject) consolidatedList.get(position)).getChatMessage().setIsDownloaded("Yes");
            //chatMessageArrayList1.get(position).setIsDownloaded("Yes");
            isFocuse = true;
            //  groupMessage(ChatRoomId, context);
            groupDataIntoHashMap(chatMessageArrayList1, ChatRoomId);

        }

    }

    public void DeleteMessage(int adapterPosition, String chatRoomId, Context context) {
        msgPosition = adapterPosition;
        if (adapterPosition != -1) {
            ChatMessageId = ((ChatModelObject) consolidatedList.get(adapterPosition)).getChatMessage().getMessageId();
            ChatChatRoomId = ((ChatModelObject) consolidatedList.get(adapterPosition)).getChatMessage().getChatRoomId();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddChatRoomActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("Confirm message delete...");

            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want delete this message?");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.mipmap.ic_vwb);

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    if (isnet()) {
                        new StartSession(AddChatRoomActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new ChatMessageDelete().execute();
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
    }

    public static void updateUI(String uuid, String chatId, int pos) {
        if (consolidatedList != null) {
            try {
                ((ChatModelObject) consolidatedList.get(pos)).getChatMessage().setMessageId(uuid);
                ((ChatModelObject) consolidatedList.get(pos)).getChatMessage().setIsDownloaded("Yes");
                groupChatRecycleViewAdapter.notifyItemChanged(pos);
                // listview_chatting.scrollToPosition(pos);
                listview_chatting.smoothScrollToPosition(consolidatedList.size() - 1);
                finalChatList.clear();
            } catch (ClassCastException e) {
                e.printStackTrace();
                updateUI(uuid, chatId, pos + 1);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                updateUI(uuid, chatId, pos - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void updateUIDelete(String uuid, String chatId, int pos) {
        if (consolidatedList != null) {
            try {
                consolidatedList.remove(pos);
                groupChatRecycleViewAdapter.notifyDataSetChanged();
                listview_chatting.smoothScrollToPosition(consolidatedList.size() - 1);
                finalChatList.clear();
            } catch (ClassCastException e) {
                e.printStackTrace();
                updateUI(uuid, chatId, pos + 1);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                updateUI(uuid, chatId, pos - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void setCopyOptionLayout(int adapterPosition, String chatRoomId, Context context) {
        if (adapterPosition != -1) {
            ClipboardManager cm = (ClipboardManager) AddChatRoomActivity.context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(((ChatModelObject) consolidatedList.get(adapterPosition)).getChatMessage().getMessage());
            //copyOptionLayout.setVisibility(View.GONE);
        }
    }

    public void setassginWorkText(int adapterPosition, String chatRoomId, Context context) {
        if (adapterPosition != -1) {
            String message = ((ChatModelObject) consolidatedList.get(adapterPosition)).getChatMessage().getMessage();
            Intent intent = new Intent(AddChatRoomActivity.this, AssignActivity.class);
            intent.putExtra("Description", message);
            startActivity(intent);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        //Toast.makeText(context, "internet is connected", Toast.LENGTH_SHORT).show();
    }

    public void playVideo(String attachment) {
        startActivity(new Intent(AddChatRoomActivity.this, VideoPlayerClass.class).putExtra("fileUrl", attachment));
        /*File fileNew = new File(attachment);
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(fileNew.getAbsolutePath());
        newIntent.setDataAndType(Uri.fromFile(fileNew), mimeType);
        //newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }*/
    }

    public void setVideoDownload(int position, String isDownloaded, String ChatRoomId, Context context) {
        if (isDownloaded.equals("No")) {
            ((ChatModelObject) consolidatedList.get(position)).getChatMessage().setDownload(true);
            ChatMessage chatMessage = ((ChatModelObject) consolidatedList.get(position)).getChatMessage();
            String chatRoomId = chatMessage.getChatRoomId();
            String downloadImageUrl = chatMessage.getAttachment();
            String messageId = chatMessage.getMessageId();
            groupChatRecycleViewAdapter.notifyDataSetChanged();
            new DownloadActivity(context, position, chatRoomId, downloadImageUrl, messageId, "video");
        } else {
            ((ChatModelObject) consolidatedList.get(position)).getChatMessage().setDownload(false);
            ((ChatModelObject) consolidatedList.get(position)).getChatMessage().setIsDownloaded("Yes");
            //chatMessageArrayList1.get(position).setIsDownloaded("Yes");
            isFocuse = true;
            //  groupMessage(ChatRoomId, context);
            groupDataIntoHashMap(chatMessageArrayList1, ChatRoomId);

        }
    }

    public void setReply(int position, String chatRoomId, Context context) {
        if (position != -1) {
            String message = ((ChatModelObject) consolidatedList.get(position)).getChatMessage().getMessage();
            String replyDecode = StringEscapeUtils.unescapeJava(message);
            replyText.setText(replyDecode);
            replyLayout.setVisibility(View.VISIBLE);
            replyImage.setVisibility(View.GONE);
            //Sender
            if (((ChatModelObject) consolidatedList.get(position)).getChatMessage().getStatus().equals("Sender"))
                userNameText.setText("You");
            else
                userNameText.setText(((ChatModelObject) consolidatedList.get(position)).getChatMessage().getUsername());

            replyMessageId = ((ChatModelObject) consolidatedList.get(position)).getChatMessage().getMessageId();

        }
        isReply = true;
    }

    public void setReplyForImage(int position, String chatRoomId, Context context) {
        File imgFileReceiver;
        String IsDownloaded = "No";
        if (position != -1) {
            String ReceiverImage = ((ChatModelObject) consolidatedList.get(position)).getChatMessage().getAttachment();
            String path1 = Environment.getExternalStorageDirectory()
                    .toString();
            File recFile = new File(path1 + "/" + "Vwb" + "/" + "Receive");
            if (!recFile.exists())
                recFile.mkdirs();
            File sendFile = new File(path1 + "/" + "Vwb" + "/" + "Sender");
            if (!sendFile.exists())
                sendFile.mkdirs();
            imgFileReceiver = new File(ReceiverImage);
            File checkFile = null;
            if (new File(recFile + "/" + ReceiverImage.substring(ReceiverImage.lastIndexOf("/")).replace("/", "")).exists()) {
                IsDownloaded = "Yes";
                filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                checkFile = new File(recFile + "/" + filename);
            } else if (new File(sendFile + "/" + ReceiverImage.substring(ReceiverImage.lastIndexOf("/")).replace("/", "")).exists()) {
                IsDownloaded = "Yes";
                filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                checkFile = new File(sendFile + "/" + filename);
            } else
                IsDownloaded = "No";
            if (IsDownloaded.equals("Yes")) {


                if (checkFile != null && checkFile.exists()) {
                    replyImage.setVisibility(View.VISIBLE);
                    replyImage.setController(getImageDrableWithoutBlure(replyImage, Uri.fromFile(checkFile), 100));
                }
                // String replyDecode = StringEscapeUtils.unescapeJava(message);
                replyText.setText("Image");
                replyLayout.setVisibility(View.VISIBLE);
                //Sender
                if (((ChatModelObject) consolidatedList.get(position)).getChatMessage().getStatus().equals("Sender"))
                    userNameText.setText("You");
                else
                    userNameText.setText(((ChatModelObject) consolidatedList.get(position)).getChatMessage().getUsername());

                replyMessageId = ((ChatModelObject) consolidatedList.get(position)).getChatMessage().getMessageId();

            }
            isReply = true;
        }
    }

    public void appBarLayout(int position, String chatRoomId, Context context, boolean isImage) {
        hidden = true;
        msgPosition = position;
        ChatRoomId = chatRoomId;
        appbar_chat.setVisibility(View.VISIBLE);
        replyTypeIsImage = isImage;

    }

    public void checkIsReply(int adapterPosition) {
        String messageType = ((ChatModelObject) consolidatedList.get(adapterPosition)).getChatMessage().getMessageType();
        if (messageType.contains("reply:")) {
            ChatMessage chatMessageObj = ((ChatModelObject) consolidatedList.get(adapterPosition)).getChatMessage();
            String[] value = chatMessageObj.getMessageType().split("reply:");
            int rpl = getUserName(value[1]);
            if (rpl != -1) {
                listview_chatting.scrollToPosition(rpl);
                groupChatRecycleViewAdapter.setSelectedPostion(rpl);
                /*Thread t = new Thread() {
                    public void run() {
                        try {
                            sleep(5000);
                            groupChatRecycleViewAdapter.setSelectedPostion(-1);
//
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();*/


            }

        }

    }

    private int getUserName(String s) {
        for (int i = 0; i < consolidatedList.size(); i++) {

            int type = consolidatedList.get(i).getType();

            if (type != 0) {
                ChatMessage chatMessageobj = ((ChatModelObject) consolidatedList.get(i)).getChatMessage();
                if (s.equals(chatMessageobj.getMessageId())) {
                    // String replyStr = chatMessageobj.getMessage()+"@@"+chatMessageobj.getUsername();
                    return i;
                }
            }

        }
        return -1;
    }

    public void GetGrupMessageList(final String date, String messageId) {
        String messageDt = "";
        adapterStatus = 1;
        adapterMsgId = messageId;
        String timeStampDt = "";
        if (date.equals("Today")) {
            String[] spilttime = Starttime.split(" ");
            messageDt = spilttime[0];
        } else {
            timeStampDt = date;

            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
            Date date1 = null;
            String dateString = format.format(new Date());
            try {
                date1 = format.parse(timeStampDt);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (date1 != null) {
                messageDt = sdf.format(date1);
            }

        }

    /*    String endDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long Etime = Long.parseLong(timeStampDt);
        Date EndDate = new Date(Etime);
        endDate = sdf.format(EndDate);
*/

        if (fromEnterChat == -1) {
            if (!messageDt.equals("")) {
                if (isnet()) {
                    showProgress();
                    final String finalMessageDt = messageDt;
                    new StartSession(AddChatRoomActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            ChatRoomMessageRetrive chatRoomMessageRetrive = new ChatRoomMessageRetrive();
                            chatRoomMessageRetrive.execute(finalMessageDt);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            dismissProgress();
                        }
                    });
                } else {
                    Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            fromEnterChat = -1;
        }


    }


    class ChatRoomMessageRetrive extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        //  ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress();
           /* progressDialog = new ProgressDialog(AddChatRoomActivity.this);
            if (!isFinishing()) {

                progressDialog.setCancelable(true);
                progressDialog.show();
                progressDialog.setContentView(R.layout.vwb_progress_lay);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            }*/
            //  showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            String dt = "";
            dt = params[0];
            try {
                sql = db.getWritableDatabase();

                //String url = CompanyURL + WebUrlClass.api_GetRefreshMessages + "?ChatRoomId=" + ChatRoomId + "&UserMasterId=" + UserMasterId;

                String url = CompanyURL + WebUrlClass.api_GetRefreshMessages + "?ChatRoomId=" + ChatRoomId + "&UserMasterId=" + UserMasterId
                        + "&MessageDate=" + dt;


                /*http://c207.ekatm.com/api/ChatRoomApi/getRefreshMessages?
                ChatRoomId=c60e6dbd-b242-41ac-b5b1-680a6478f7d6&UserMasterId=70377995-cbfd-426e-b7c1-10e41cc31854&MessageDate=2020-08-18*/

                res = ut.OpenConnection(url, AddChatRoomActivity.this);
                response = res.toString();


                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);


                    ContentValues values = new ContentValues();
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_GROUP_MESSAGE, null);
                    int count = c.getCount();

                    if (jResults.length() != 100) {
                        if (refeshRespSize == -1) {
                            // refeshRespSize = 1;
                            if (jResults.length() > 0) {
                                for (int i = 0; i < jResults.length(); i++) {
                                    JSONObject Jsongroupmessage = jResults.getJSONObject(i);
                                    ChatMessage chatMessage = new ChatMessage();
                                    //  UserMasterId = Jsongroupmessage.getString("UserMasterId");
                                    //chatMessage.setUserMasterId(UserMasterId);
                                    ChatRoomId = Jsongroupmessage.getString("ChatRoomId");
                                    chatMessage.setChatRoomId(ChatRoomId);
                                    Message = Jsongroupmessage.getString("Message");
                                    //String MessageEncode = StringEscapeUtils.unescapeJava(Message);
                                    chatMessage.setMessage(Message);
                                    Status = Jsongroupmessage.getString("Status");
                                    chatMessage.setStatus(Status);

                                    if (Status.equals("Sender")) {
                                        chatMessage.setIsDownloaded("Yes");

                                    } else {
                                        chatMessage.setIsDownloaded("No");

                                    }


                                    String jsondate = Jsongroupmessage.getString("MessageDate");
                                    jsondate = jsondate.replace("T", " "); // escape .
                                    try {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                        Date parsedDate = dateFormat.parse(jsondate);
                                        timestamp = (parsedDate.getTime());
                                        chatMessage.setMessageDate(timestamp);
                                    } catch (Exception e) { //this generic but you can control another types of exception
                                        // look the origin of excption
                                        e.printStackTrace();
                                    }


                                    MessageId = Jsongroupmessage.getString("MessageId");
                                    chatMessage.setMessageId(MessageId);
                                    UserName = Jsongroupmessage.getString("UserName");
                                    chatMessage.setUsername(UserName);

                                    MessageType = Jsongroupmessage.getString("MessageType");
                                    chatMessage.setMessageType(MessageType);
                                    Attachment = Jsongroupmessage.getString("Attachment");
                                    if (Attachment.equals("")) {
                                        chatMessage.setAttachment(null);
                                    } else {
                                        if (Status.equals("")) {
                                            String path1 = Environment.getExternalStorageDirectory()
                                                    .toString();
                                            File file = new File(path1 + "/" + "Vwb" + "/" + "Receive");
                                            if (!file.exists())
                                                file.mkdirs();
                                            File checkFile = new File(file + "/" + Jsongroupmessage.getString("ChatAttachment"));
                                            if (checkFile.exists()) {
                                                chatMessage.setIsDownloaded("Yes");
                                            } else {

                                                chatMessage.setIsDownloaded("No");
                                            }
                                        }
                                        chatMessage.setAttachment(CompanyURL + Attachment);
                                    }

                                    Boolean isReadyToInsert = cf.CheckChatroomRecordPresent(db.TABLE_CHAT_GROUP_MESSAGE, "MessageId", chatMessage.getMessageId());
                                    Log.e("data", "" + isReadyToInsert);
                                    if (!isReadyToInsert) {
                                        sql.delete(db.TABLE_CHAT_GROUP_MESSAGE, "MessageId=?",
                                                new String[]{chatMessage.getMessageId()});
                                    }

                                    if (cf.CheckifRecordPresentForChat(db.TABLE_CHAT_GROUP_MESSAGE, "MessageId", chatMessage.getMessageId())) {
                                        cf.AddGroupMessage(chatMessage);
                                    }


                                }


                            }
                        } else {
                            response = "[]";
                        }
                    } else {
                        if (jResults.length() > 0) {
                            for (int i = 0; i < jResults.length(); i++) {
                                JSONObject Jsongroupmessage = jResults.getJSONObject(i);
                                ChatMessage chatMessage = new ChatMessage();
                                //  UserMasterId = Jsongroupmessage.getString("UserMasterId");
                                //chatMessage.setUserMasterId(UserMasterId);
                                ChatRoomId = Jsongroupmessage.getString("ChatRoomId");
                                chatMessage.setChatRoomId(ChatRoomId);
                                Message = Jsongroupmessage.getString("Message");
                                //String MessageEncode = StringEscapeUtils.unescapeJava(Message);
                                chatMessage.setMessage(Message);
                                Status = Jsongroupmessage.getString("Status");
                                chatMessage.setStatus(Status);

                                if (Status.equals("Sender")) {
                                    chatMessage.setIsDownloaded("Yes");

                                } else {
                                    chatMessage.setIsDownloaded("No");

                                }


                                String jsondate = Jsongroupmessage.getString("MessageDate");
                                jsondate = jsondate.replace("T", " "); // escape .
                                try {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Date parsedDate = dateFormat.parse(jsondate);
                                    timestamp = (parsedDate.getTime());
                                    chatMessage.setMessageDate(timestamp);
                                } catch (Exception e) { //this generic but you can control another types of exception
                                    // look the origin of excption
                                }


                                MessageId = Jsongroupmessage.getString("MessageId");
                                chatMessage.setMessageId(MessageId);
                                UserName = Jsongroupmessage.getString("UserName");
                                chatMessage.setUsername(UserName);

                                MessageType = Jsongroupmessage.getString("MessageType");
                                chatMessage.setMessageType(MessageType);
                                Attachment = Jsongroupmessage.getString("Attachment");
                                if (Attachment.equals("")) {
                                    chatMessage.setAttachment(null);
                                } else {
                                    if (Status.equals("")) {
                                        String path1 = Environment.getExternalStorageDirectory()
                                                .toString();
                                        File file = new File(path1 + "/" + "Vwb" + "/" + "Receive");
                                        if (!file.exists())
                                            file.mkdirs();
                                        File checkFile = new File(file + "/" + Jsongroupmessage.getString("ChatAttachment"));
                                        if (checkFile.exists()) {
                                            chatMessage.setIsDownloaded("Yes");
                                        } else {

                                            chatMessage.setIsDownloaded("No");
                                        }
                                    }
                                    chatMessage.setAttachment(CompanyURL + Attachment);
                                }

                                Boolean isReadyToInsert = cf.CheckChatroomRecordPresent(db.TABLE_CHAT_GROUP_MESSAGE, "MessageId", chatMessage.getMessageId());
                                Log.e("data", "" + isReadyToInsert);
                                if (!isReadyToInsert) {
                                    sql.delete(db.TABLE_CHAT_GROUP_MESSAGE, "MessageId=?",
                                            new String[]{chatMessage.getMessageId()});
                                }

                                if (cf.CheckifRecordPresentForChat(db.TABLE_CHAT_GROUP_MESSAGE, "MessageId", chatMessage.getMessageId())) {
                                    cf.AddGroupMessage(chatMessage);
                                }


                            }


                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    response = "[]";
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
            // progressDialog.dismiss();
            dismissProgress();

            if (response == null /*|| response.contains("[]"*/) {
                Toast.makeText(AddChatRoomActivity.this, "No message found", Toast.LENGTH_SHORT).show();
            } else if (response.equals("[]")) {
                //   Toast.makeText(AddChatRoomActivity.this, "", Toast.LENGTH_SHORT).show();
            } else {

                if (response != null) {
                    groupMessage(ChatRoomId, context);
                }

            }

        }

    }


    private void ChatUserUpdatList(String ChatRoomid) {
        chatUserArrayList.clear();
        chatUserArrayList = new ArrayList<>();
        String query = "SELECT * FROM " + db.TABLE_CHAT_CHATROOM_MEMBER_LIST + " WHERE  ChatRoomId ='" + ChatRoomid + "'";
        ;
        ChatUser chatUser;
        //sql.close();
        sql = db.getWritableDatabase();
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

    }


    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO_CAPTURE);
        path = String.valueOf(fileUri);
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
           /* Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Audio "), INTENT_PICK_AUDIO);*/

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(contactPickerIntent, 13);
        } else {
            requestLocationPermission();
        }


    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                pickaudio();
                //startActivityForResult(contactPickerIntent, 13);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                        REQUEST_READ_CONTACTS);
            }

        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }

    private void DocumentSelect() {
        browseDocuments();
        /*Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*//*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, PICK_PDF_REQUEST);*/

        /* Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Choose directory"), PICK_PDF_REQUEST);

*/


        /*Intent intent = new Intent();
        intent.setType("application");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);*/
    }


    private void saveImage(String imagefilename, Bitmap bitmap) {

        if (bitmap != null) {
            String path1 = Environment.getExternalStorageDirectory()
                    .toString();
            File file = new File(path1 + "/" + "Vwb" + "/" + "Sender");
            if (!file.exists())
                file.mkdirs();
            File file1 = new File(file, imagefilename);
            if (file1.exists())
                file1.delete();
            try {
                FileOutputStream out = new FileOutputStream(file1);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


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
                bitmap = null;
                try {
                    // bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    FileOutputStream out = new FileOutputStream(mediaFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "attachment", null);
                    fileUri = Uri.parse(url);
                    //   bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                    File f = new File(getRealPathFromURI(fileUri));
                    path = f.toString();
                    Imagefilename = f.getName();
                    chatMessage = new ChatMessage();
                    chatMessage.setMessage(Chat_message);
                    chatMessage.setStatus("Sender");
                    chatMessage.setMessageDate(timestamp);
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
                        async = new PostUploadImageMethod().execute(uuidInString);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {

            } else {

            }
        } else if (requestCode == SELECT_FILE_IMAGE) {
            if (resultCode == RESULT_OK) {

                fileUri = data.getData();
                Intent intent = new Intent(AddChatRoomActivity.this, AddImageWithTextForChat.class).putExtra("imageUri", String.valueOf(fileUri));
                startActivityForResult(intent, 987);
             /*   bitmap = null;
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    File f = new File(getRealPathFromURI(fileUri));
                    FileOutputStream out = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "attachment", null);
                    fileUri = Uri.parse(url);
                    f = new File(getRealPathFromURI(fileUri));
                    path = f.toString();
                    Imagefilename = f.getName();

                    chatMessage = new ChatMessage();
                    chatMessage.setMessage(Chat_message);
                    chatMessage.setStatus("Sender");
                    chatMessage.setMessageDate(timestamp);
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
                        async = new PostUploadImageMethod().execute(uuidInString);


                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                // previewCapturedImage();

            } else if (resultCode == RESULT_CANCELED) {

            } else {

            }

        } else if (requestCode == PICK_PDF_REQUEST) {
            if (resultCode == RESULT_OK) {
                fileUri = data.getData();
              /*  File f = new File(FileUtilities.getPath(AddChatRoomActivity.this, fileUri));

                path = f.toString();

                Imagefilename = f.getName();


*/
                try {
                    fileUri = data.getData();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        File file = new File(getRealPathFromUri(AddChatRoomActivity.this, fileUri));//create path from uri
                        Log.d("", "File : " + file.getName());
                        path = file.toString();
                        Imagefilename = file.getName();
                    }

                    Toast.makeText(AddChatRoomActivity.this, "Document attached successfully", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();

                    String id = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        id = DocumentsContract.getDocumentId(fileUri);
                    }
                    InputStream inputStream = null;
                    try {
                        inputStream = getContentResolver().openInputStream(fileUri);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    File file = new File(getCacheDir().getAbsolutePath() + "/" + id);
                    writeFile(inputStream, file);
                    path = file.getAbsolutePath();
                    Imagefilename = getFileName(AddChatRoomActivity.this, fileUri);
                    Toast.makeText(AddChatRoomActivity.this, "Document attached successfully", Toast.LENGTH_SHORT).show();

                }


                chatMessage = new ChatMessage();
                chatMessage.setMessage(Chat_message);
                chatMessage.setStatus("Sender");
                chatMessage.setMessageDate(timestamp);
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
                    async = new PostUploadImageMethod().execute(uuidInString);
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
                chatMessage.setMessageDate(timestamp);
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
                    async = new PostUploadImageMethod().execute(uuidInString);
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
                chatMessage.setMessageDate(timestamp);
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

                    async = new PostUploadImageMethod().execute(uuidInString);
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
                videoFileName = f.getName();

                // Imagefilename = f.getName();
                showProgress();
                Log.d("vido fream", String.valueOf(fileUri));
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
        } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
            Uri selectedImageUri = data.getData();

            // OI FILE Manager
            path1 = selectedImageUri.getPath();
            File f = new File(path1.toString());
            path1 = f.toString();
            path = path1;
            // MEDIA GALLERY
            // path = getPath(path);
            Imagefilename = f.getName();
            File tempFile = getVideoDirecory(MEDIA_TYPE_VIDEO_CAPTURE);
            if (!tempFile.exists()) {
                try {
                    tempFile.createNewFile();
                    copyFileOrDirectory(path1, tempFile.getParentFile().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //copyFileOrDirectory(path1 ,String.valueOf(tempFile.));
            // new VideoCompressing().execute(path);
            // new VideoCompressor().execute();
        } else if (requestCode == SELECT_FILE_IMAGE_theme) {
            if (resultCode == RESULT_OK) {
                Uri themeUri = null;
                themeUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), themeUri);
                    if (bitmap != null) {
                        String path1 = Environment.getExternalStorageDirectory()
                                .toString();
                        File file = new File(path1 + "/" + "Vwb" + "/" + "Theme");
                        if (!file.exists())
                            file.mkdirs();
                        java.util.Date date = new java.util.Date();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                .format(date.getTime());
                        File themeFile;

                        // For unique video file name appending current timeStamp with file name
                        themeFile = new File(file.getPath() + File.separator +
                                "Theme_" + timeStamp + ".jpg");
                        File file1 = new File(file, themeFile.getName());
                        if (file1.exists())
                            file1.delete();
                        try {
                            FileOutputStream out = new FileOutputStream(file1);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 1, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "theme", null);
                    // fileUri = Uri.parse(url);
                    AppCommon.getInstance(this).setThemeURI(String.valueOf(url));
                    if (AppCommon.getInstance(this).getThemeURI() != null) {
                        //AppCommon.getDraweeController(themeSDV ,AppCommon.getInstance(this).getThemeURI() , 500 );
                        // themeSDV.setImageURI(AppCommon.getInstance(this).getThemeURI());
                        themeSDV.setController(AppCommon.getDraweeController(themeSDV, AppCommon.getInstance(this).getThemeURI(), 500));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 13) {
            if (resultCode == Activity.RESULT_OK) {
                String userName = "";
                String userNumber = "";
                Uri contactData = data.getData();
                ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(contactData, null, null, null, null);
                if (cur.getCount() > 0) {// thats mean some resutl has been found
                    if (cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        userName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Log.e("Names", userName);
                        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            // Query phone here. Covered next
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                            while (phones.moveToNext()) {
                                userNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Log.e("Number", userNumber);
                            }
                            phones.close();
                        }
                    }
                }
                cur.close();
                chatMessage = new ChatMessage();
                chatMessage.setMessage("#contact " + userName + " " + userNumber.trim());
                chatMessage.setStatus("Sender");
                chatMessage.setMessageDate(timestamp);
                chatMessage.setUsername(UserName);
                UUID uuid = UUID.randomUUID();
                String uuidInString = uuid.toString();
                chatMessage.setMessageId(uuidInString);
                chatMessage.setChatRoomId(ChatRoomId);
                chatMessage.setUserMasterId(UserMasterId);
                chatMessage.setMessageType("contact:");
                chatMessage.setIsDownloaded("wait");
                AddChatRoomActivity.this.runOnUiThread(new Thread(new Runnable() {
                    public void run() {
                        groupMessage(ChatRoomId, AddChatRoomActivity.this);
                    }

                }));
                JSONObject jsonObject = new JSONObject();
                onResume();
                try {
                    jsonObject.put("ChatRoomId", ChatRoomId);
                    jsonObject.put("senderId", UserMasterId);
                    jsonObject.put("senderName", UserName);
                    String MessageEncode = StringEscapeUtils.escapeJava(userName + " " + userNumber);
                    jsonObject.put("senderMSG", MessageEncode);
                    jsonObject.put("senderMSGType", "contact:");
                    jsonObject.put("senderMSGtime", Starttime);
                    jsonObject.put("Attachment", Attachment);
                    jsonObject.put("pos", consolidatedList.size());
                    uuidInString = uuid.toString();
                    jsonObject.put("UUID", uuidInString);
                    FinalJson = jsonObject.toString();
                    chatGroupJson = new ChatGroupJson();
                    chatGroupJson.setFinalJsonGroup(FinalJson);
                    cf.AddGrouMessageJson(chatGroupJson);
                    if (!ConnectivityReceiver.isConnected()) {
                        if (new Gson().fromJson(AppCommon.getInstance(context).getChatJson(), TeamChatListObject.class) != null) {
                            teamChatListObject = new Gson().fromJson(AppCommon.getInstance(context).getChatJson(), TeamChatListObject.class);
                            finalChatList = teamChatListObject.getFinalJsonList();
                            finalChatList.add(FinalJson);
                            teamChatListObject.setFinalJsonList(finalChatList);
                            AppCommon.getInstance(context).setChatJson(new Gson().toJson(teamChatListObject));
                        } else {
                            finalChatList.add(FinalJson);
                            teamChatListObject.setFinalJsonList(finalChatList);
                            AppCommon.getInstance(context).setChatJson(new Gson().toJson(teamChatListObject));
                        }

                    } else {

                        chatGroupJson.setMessage_id(uuidInString);
                        chatGroupJsonArrayList.add(chatGroupJson);
                    }


                    chatMessage.setAttachment("");
                    // chatMessage.setIsDownloaded("No");
                    chatMessage.setIsDownloaded("wait");
                    chatMessageArrayList1.add(chatMessage);
                    ChatModelObject generalItem = new ChatModelObject();
                    generalItem.setChatMessage(chatMessage);
                    consolidatedList.add(generalItem);

                    Intent intent = new Intent(AddChatRoomActivity.this, ChattingDataSendBackground.class);
                    intent.putExtra("pos", consolidatedList.size() - 1);
                    startService(intent);
                    cf.AddGroupMessage(chatMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                chat_edit_text.setText("");
                groupDataIntoHashMap(chatMessageArrayList1, ChatRoomId);

            }
        }
        if (requestCode == 987) {
            if (resultCode == 986) {
                message = data.getStringExtra("messages");
                Log.i("resposnseImage:", fileUri.toString() + " :: " + message);
                if (message != null) {
                    if (!message.equals("")) {
                        message = "#File" + message;
                    }
                }
                bitmap = null;
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    File f = new File(getRealPathFromURI(fileUri));
                    FileOutputStream out = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "attachment", null);
                    fileUri = Uri.parse(url);
                    f = new File(getRealPathFromURI(fileUri));
                    path = f.toString();
                    Imagefilename = f.getName();

                    chatMessage = new ChatMessage();
                    chatMessage.setMessage(message);
                    chatMessage.setStatus("Sender");
                    chatMessage.setMessageDate(timestamp);
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
                        async = new PostUploadImageMethod().execute(uuidInString);


                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }

    }

    private void copyFileOrDirectory(String srcDir, String dstDir) {
        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        requestRuntimePermission();
        // return Uri.fromFile(getOutputMediaFile(type));
        return Uri.fromFile(getVideoDirecory(type));
    }

    private File getVideoDirecory(int type) {
        String path1 = Environment.getExternalStorageDirectory()
                .toString();
        File file = new File(path1 + "/" + "Vwb" + "/" + "Video" + "/" + "SendVideos");
        if (!file.exists())
            file.mkdirs();

        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO_CAPTURE) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(file.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");

        } else {
            return null;
        }
        return mediaFile;
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
        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), SetAppName.IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(SetAppName.IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + SetAppName.IMAGE_DIRECTORY_NAME + " directory");
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                } else {
                    Log.e("Permission", "Denied");
                }
                break;
            }
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    recordVideo();
                }
                break;
            case 201:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startGalleryIntent();
                }
                break;
            case REQUEST_READ_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickaudio();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cur = getContentResolver().query(uri, null, null, null, null);
        cur.moveToFirst();
        int idx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cur.getString(idx);
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "          " + timeStamp + "";
        File storageDir = null;

        storageDir = new File("/sdcard/", SetAppName.IMAGE_DIRECTORY_NAME);

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
        Cursor cur = managedQuery(uri, projection, null, null, null);
        int column_index = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cur.moveToFirst();
        return cur.getString(column_index);
    }

    private void pickFromGalleryvideo() {

        Intent intent = new Intent();
        intent.setType("video/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video "), INTENT_PICK_AUDIO);

    }

    public void callNewUser(int position, String chatRoomId, Context context, boolean b) {
        ChatMessage chatMessage = ((ChatModelObject) consolidatedList.get(position)).getChatMessage();
        String contactInfo[] = chatMessage.getMessage().split(" ");
        Intent callIntent = new Intent(Intent.ACTION_CALL);

        callIntent.setData(Uri.parse("tel:" + contactInfo[contactInfo.length - 1].trim()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddChatRoomActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else
            startActivity(callIntent);
    }

    public void callAddNewContact(int position, String chatRoomId, Context context, boolean b) {
        ChatMessage chatMessage = ((ChatModelObject) consolidatedList.get(position)).getChatMessage();
        String contactInfo[] = chatMessage.getMessage().split(" ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddChatRoomActivity.this, new String[]{Manifest.permission.WRITE_CONTACTS}, REQUEST_WRITE_CONTACT);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            int rawContactInsertIndex = ops.size();

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactInfo[0]) // Name of the person
                    .build());
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactInfo[1]) // Number of the person
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Type of mobile number
            try {
                ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                Toast.makeText(AddChatRoomActivity.this, "Contact Added", Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                // error
            } catch (OperationApplicationException e) {
                // error
            }
        }
    }

    public String getPathvideo(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cur = getContentResolver().query(uri, projection, null, null, null);
        if (cur != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cur
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cur.moveToFirst();
            return cur.getString(column_index);
        } else
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
                isRefresh = "false";
                isFocuse = false;
                Log.e("Compression", "Compression successfully!");
                path = MediaController.cachedFile.getPath();
                if (!path.equals("")) {
                    File fdelete = new File(path1);
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            System.out.println("file Deleted :" + path1);
                        } else {
                            System.out.println("file not Deleted :" + path1);
                        }
                    }
                }
                File f = new File(path);
                Imagefilename = f.getName();

                chatMessage = new ChatMessage();
                chatMessage.setMessage(path);
                chatMessage.setStatus("Sender");
                chatMessage.setMessageDate(timestamp);
                chatMessage.setUsername(UserName);
                UUID uuid = UUID.randomUUID();
                String uuidInString = uuid.toString();
                chatMessage.setMessageId(uuidInString);
                chatMessage.setChatRoomId(ChatRoomId);
                chatMessage.setUserMasterId(UserMasterId);
                //chatMessage.setMessageType("text:");
                chatMessage.setMessageType("video:");
                chatMessage.setIsDownloaded("No");
                if (path != null) {
                    chatMessage.setAttachment(path);
                }
                chatMessageArrayList1.add(chatMessage);
                ChatModelObject generalItem = new ChatModelObject();
                generalItem.setChatMessage(chatMessage);
                consolidatedList.add(generalItem);
                cf.AddGroupMessage(chatMessage);
                groupDataIntoHashMap(chatMessageArrayList1, ChatRoomId);
               /* AddChatRoomActivity.this.runOnUiThread(new Thread(new Runnable() {
                    public void run() {


                        groupMessage(ChatRoomId, AddChatRoomActivity.this);


                    }

                }));*/


                if (isnet()) {

                    //async = new PostUploadImageMethod().execute(uuidInString);
                    async = new PostUploadVideoMethod().execute(uuidInString);
                }


            }


        }
    }

    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);

    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }


    private void browseDocuments() {

//        String[] mimeTypes =
//                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
//                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
//                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
//                        "text/plain",
//                        "application/pdf",
//                        "application/zip"};
//
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
//            if (mimeTypes.length > 0) {
//                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//            }
//        } else {
//            String mimeTypesStr = "";
//            for (String mimeType : mimeTypes) {
//                mimeTypesStr += mimeType + "|";
//            }
//            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
//        }
//        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), PICK_PDF_REQUEST);


        Intent intent;
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", "*/*");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    PICK_PDF_REQUEST);
        } else {

            String[] mimeTypes =
                    {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                            "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                            "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                            "text/plain",
                            "application/pdf",
                            "application/zip", "application/vnd.android.package-archive"};

            intent = new Intent(Intent.ACTION_GET_CONTENT); // or ACTION_OPEN_DOCUMENT
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    PICK_PDF_REQUEST);
        }
    }






    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // getNewIntent(intent);
        Call_Callid = intent.getStringExtra("callid");
        Call_CallType = intent.getStringExtra("call_type");
        ChatRoomId = intent.getStringExtra("ChatRoomid");
        ChatRoomName = intent.getStringExtra("Chatroomname");
        Status = intent.getStringExtra("group_status");
        Create_check = intent.getStringExtra("value_chat");
        ProjectMasterID = intent.getStringExtra("projmasterId");
        CStatus = intent.getStringExtra("status");

        if (cf.getChatmessagecount() > 0) {
            groupMessage(ChatRoomId, AddChatRoomActivity.this);
        }
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            txt_chat_title.setText(ChatRoomName);
            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        int tempCount = 0;
        AppCommon.getInstance(this).setNotificationCount(tempCount);
    }

    public class PostUploadImageMethod extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params = "";
        //   ProgressDialog SPdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... urls) {
            params = urls[0];

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
                            //  Toast.makeText(AddChatRoomActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ChatRoomId", ChatRoomId);
                        jsonObject.put("senderId", UserMasterId);
                        jsonObject.put("senderName", UserName);
                        jsonObject.put("senderMSG", message);
                        jsonObject.put("senderMSGType", "text:");
                        jsonObject.put("senderMSGtime", Starttime);
                        jsonObject.put("Attachment", Imagefilename);
                        jsonObject.put("UUID", params);
                        jsonObject.put("pos", consolidatedList.size() - 1);
                        FinalJson = jsonObject.toString();
                        chatGroupJson = new ChatGroupJson();
                        chatGroupJson.setFinalJsonGroup(FinalJson);
                        cf.AddGrouMessageJson(chatGroupJson);
                        chatGroupJsonArrayList.add(chatGroupJson);
                        Intent intent = new Intent(AddChatRoomActivity.this, ChattingDataSendBackground.class);
                        startService(intent);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("IsDownloaded", "Yes");
                        contentValues.put("ChatRoomId", ChatRoomId);
                        sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId=?", new String[]{params});


                        AddChatRoomActivity.this.runOnUiThread(new Thread(new Runnable() {
                            public void run() {


//                                groupMessage(ChatRoomId, AddChatRoomActivity.this);
                                saveImage(Imagefilename, bitmap);


                            }

                        }));

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

    private class PostUploadVideoMethod extends AsyncTask<String, Void, String> {
        private Exception exception;
        String params = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... urls) {
            params = urls[0];

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
                            //  Toast.makeText(AddChatRoomActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ChatRoomId", ChatRoomId);
                        jsonObject.put("senderId", UserMasterId);
                        jsonObject.put("senderName", UserName);
                        jsonObject.put("senderMSG", null);
                        jsonObject.put("senderMSGType", "video:");
                        jsonObject.put("senderMSGtime", Starttime);
                        jsonObject.put("Attachment", Imagefilename);
                        jsonObject.put("UUID", params);
                        jsonObject.put("pos", consolidatedList.size() - 1);
                        FinalJson = jsonObject.toString();
                        chatGroupJson = new ChatGroupJson();
                        chatGroupJson.setFinalJsonGroup(FinalJson);
                        cf.AddGrouMessageJson(chatGroupJson);
                        chatGroupJsonArrayList.add(chatGroupJson);
                        Intent intent = new Intent(AddChatRoomActivity.this, ChattingDataSendBackground.class);
                        startService(intent);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("IsDownloaded", "Yes");
                        contentValues.put("ChatRoomId", ChatRoomId);
                        sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId=?", new String[]{params});


                        AddChatRoomActivity.this.runOnUiThread(new Thread(new Runnable() {
                            public void run() {


//                                groupMessage(ChatRoomId, AddChatRoomActivity.this);
                                // saveImage(Imagefilename, bitmap);


                            }

                        }));

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
    }

    // new upload image code


    private DraweeController getImageDrableWithoutBlure(SimpleDraweeView imageView, Uri imageUrl, int size) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUrl)
                .setResizeOptions(new ResizeOptions(size, size))
                .setProgressiveRenderingEnabled(false)
                .build();

        return Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.getController())
                .setImageRequest(request)
                .build();
    }


    class ChatMessageDelete extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress();
            //  showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_DeleteChatMsges + "?RoomId=" + ChatChatRoomId + "&MessageId=" + ChatMessageId;
                System.out.println("Delete" + url);
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

            dismissProgress();
            if (integer.contains("ActivityId")) {

                try {

                    JSONObject jsonObject = new JSONObject(integer);

                    String messageid = jsonObject.getString("ActivityId");
                    String Msg = jsonObject.getString("Msg");
                    String detete = "This message was deleted";

                    Toast.makeText(AddChatRoomActivity.this, "Message deleted successfully", Toast.LENGTH_LONG).show();

                    sql = db.getWritableDatabase();
                   /* ContentValues contentValues = new ContentValues();
                    contentValues.put("ChatRoomId", messageid);
                    contentValues.put("Message", detete);*/

                    sql.delete(db.TABLE_CHAT_GROUP_MESSAGE, "MessageId=?", new String[]{Msg});

                    // sql.delete(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId=?", new String[]{Msg});

                    unreadPos = msgPosition;
                    groupChatRecycleViewAdapter.notifyItemRemoved(unreadPos);
                    groupMessage(messageid, AddChatRoomActivity.this);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {


            }

        }

    }
    public static void writeFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( out != null ) {
                    out.close();
                }
                in.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    public String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null) {
            String path = getPath(uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null,
                    null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

}