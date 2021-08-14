package com.vritti.crmlib.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;


import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.ChatMessage;
import com.vritti.crmlib.chat.AddChatRoomActivity;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

//yyyy-MM-dd HH.mmaa
//dd MMM yyyy hh:mmaa
public class GroupChatListAdapter extends BaseAdapter{


    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    public static DatabaseHandlers db;
    CommonFunctionCrm cf;
    public Context context;
    public ArrayList<ChatMessage> chatMessages;

    public LayoutInflater mInflater;
    String  ChatRoomId, ReceiverImage, res, Message_id, IsDownloaded;
    Object responsemsg;
    File imgFile;
    SharedPreferences userpreferences;
    public static ChatMessage chatMessage;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Conversation";
    static File mediaFile;
    String filename;
    private File imgFileReceiver;
    Bitmap bitmap;
    public static MediaPlayer mp=new MediaPlayer();
    public static SQLiteDatabase sql;
    private String chat_time;
    ContentValues contentValues = new ContentValues();
    private String filepath = "CRM Document";
    File myInternalFile;
    File myExternalFile;
    private final int MEGABYTE = 1024 * 1024;
    private String file1;
    private File pdfFile;
    Handler handler = new Handler();
    public static Uri videoUri;

    public static Runnable runOnUiThread;
    public static ProgressDialog mProgressDialog;
    private Date date;
    private String dotformat="No";


    public GroupChatListAdapter(ArrayList<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
        mInflater = LayoutInflater.from(context);

        this.context = context;
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
        CompanyURL = userpreferences.getString("CompanyURL", null);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mProgressDialog = new ProgressDialog(context);


    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String Message = chatMessages.get(position).getStatus();
        ViewHolder holder1 = new ViewHolder();
        if (convertView == null) {
            if (Message.equals("Sender")) {
                convertView = mInflater.inflate(R.layout.crm_chat_user1_item, null,false);
            } else if (Message.equals("")) {
                convertView = mInflater.inflate(R.layout.crm_chat_user2_item, null,false);
            }

            if (Message.equalsIgnoreCase("Sender")) {
                holder1.messageTextView = (TextView) convertView.findViewById(R.id.textview_message);
                holder1.timeTextView = (TextView) convertView.findViewById(R.id.textview_time);
                holder1.textview_username = (TextView) convertView.findViewById(R.id.textview_username);
                holder1.textview_time1 = (TextView) convertView.findViewById(R.id.textview_time1);
                holder1.img_chat = (ImageView) convertView.findViewById(R.id.img_chat);
                holder1.rel_image = (RelativeLayout) convertView.findViewById(R.id.rel_image);
                holder1.incoming_layout_bubble = (LinearLayout) convertView.findViewById(R.id.incoming_layout_bubble);
                holder1.mPlayMedia = (ImageView) convertView.findViewById(R.id.play);
                holder1.mPauseMedia = (ImageView) convertView.findViewById(R.id.pause);
                holder1.mMediaSeekBar = (SeekBar) convertView.findViewById(R.id.media_seekbar);
                holder1.mRunTime = (TextView) convertView.findViewById(R.id.run_time);
                holder1.mTotalTime = (TextView) convertView.findViewById(R.id.total_time);
                holder1.len_audio_play = (LinearLayout) convertView.findViewById(R.id.len_audio_play);
                holder1.textview_mp3_time = (TextView) convertView.findViewById(R.id.textview_mp3_time);
                holder1.video_player_view = (FullscreenVideoLayout) convertView.findViewById(R.id.video_player_view);
                holder1.rel_video_view = (RelativeLayout) convertView.findViewById(R.id.rel_video_view);
                holder1.textview_video_time = (TextView) convertView.findViewById(R.id.textview_video_time);
                holder1.txt_message_delivered=convertView.findViewById(R.id.txt_message_delivered);
                holder1.video_player_view.setActivity((Activity) context);


                holder1.img_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.getTag(position);
                        ImageView imageView = (ImageView) view;
                        imageView.setTag(position);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String Attachment = chatMessages.get(position).getAttachment();
                                if (Attachment.contains(".pdf")) {
                                    Intent target = new Intent(Intent.ACTION_VIEW);
                                    target.setDataAndType(Uri.fromFile(imgFile), "application/pdf");
                                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    Intent intent = Intent.createChooser(target, "Open File");

                                    try {
                                        context.startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context,
                                                "No Application Available to View PDF",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(new File(chatMessages.get(position).getAttachment())), "image/*");
                                    context.startActivity(intent);
                                }
                            }
                        });


                    }
                });

                holder1.mPlayMedia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mp=new MediaPlayer();

                        Uri uri= Uri.parse(chatMessages.get(position).getAttachment());
                        try {
                            mp.setDataSource(uri.toString());
                            mp.prepare();
                            mp.start();
                            Toast.makeText(context,"Play",Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder1.mPauseMedia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mp.isPlaying()){
                            mp.pause();
                        } else {
                            mp.start();
                        }
                        Toast.makeText(context,"Pause",Toast.LENGTH_SHORT).show();

                    }
                });



            } else if (Message.equalsIgnoreCase(" ") || Message.equalsIgnoreCase("")) {
                holder1.messageTextView = (TextView) convertView.findViewById(R.id.textview_message);
                holder1.timeTextView = (TextView) convertView.findViewById(R.id.textview_time);
                holder1.textview_username = (TextView) convertView.findViewById(R.id.textview_username);
                holder1.textview_time1 = (TextView) convertView.findViewById(R.id.textview_time1);
                holder1.outgoing_img_chat = (ImageView) convertView.findViewById(R.id.outgoing_img_chat);
                holder1.outgoing_rel_image = (RelativeLayout) convertView.findViewById(R.id.outgoing_rel_image);
                holder1.outgoing_layout_bubble = (LinearLayout) convertView.findViewById(R.id.outgoing_layout_bubble);
                holder1.txt_download = (TextView) convertView.findViewById(R.id.txt_download);
                //  holder1.img_progress = (ProgressBar) convertView.findViewById(R.id.img_progress1);
                holder1.textview_username1 = (TextView) convertView.findViewById(R.id.textview_username1);
                holder1.mPlayMedia1 = (ImageView) convertView.findViewById(R.id.play1);
                holder1.mPauseMedia1 = (ImageView) convertView.findViewById(R.id.pause1);
                holder1.mMediaSeekBar = (SeekBar) convertView.findViewById(R.id.media_seekbar);
                holder1.mRunTime = (TextView) convertView.findViewById(R.id.run_time);
                holder1.mTotalTime = (TextView) convertView.findViewById(R.id.total_time);
                holder1.len_audio_play = (LinearLayout) convertView.findViewById(R.id.len_audio_play);
                holder1.textview_mp3_username = (TextView) convertView.findViewById(R.id.textview_mp3_username);
                holder1.textview_mp3_time = (TextView) convertView.findViewById(R.id.textview_mp3_time);
                holder1.txt_mp3_download = (TextView) convertView.findViewById(R.id.txt_mp3_download);
                holder1.txt_video_download = (TextView) convertView.findViewById(R.id.txt_video_download);
                holder1.play_pause_layout = (LinearLayout) convertView.findViewById(R.id.play_pause_layout);
                holder1.textview_video_username = (TextView) convertView.findViewById(R.id.textview_video_username);
                holder1.video_player_view = (FullscreenVideoLayout) convertView.findViewById(R.id.video_player_view);
                holder1.rel_video_view = (RelativeLayout) convertView.findViewById(R.id.rel_video_view);
                holder1.textview_video_time = (TextView) convertView.findViewById(R.id.textview_video_time);
                //  holder1.video_frame= (RelativeLayout) convertView.findViewById(R.id.video_frame);
                // holder1.img_video_play= (ImageView) convertView.findViewById(R.id.img_video_play);
                holder1.video_player_view.setActivity((Activity) context);


                holder1.outgoing_img_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.getTag(position);
                        String Attachment = chatMessages.get(position).getAttachment();
                        if (Attachment.contains(".pdf")) {
                            imgFileReceiver = new File(Attachment);
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            target.setDataAndType(Uri.fromFile(imgFileReceiver), "application/pdf");
                            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            Intent intent = Intent.createChooser(target, "Open File");

                            try {
                                context.startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(context,
                                        "No Application Available to View PDF",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            Uri uri = Uri.parse("file://" + chatMessages.get(position).getAttachment());
                            intent.setDataAndType(uri, "image/*");
                            context.startActivity(intent);
                        }
                    }
                });


                // holder1.img_progress.getTag(position);

                holder1.txt_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        view.getTag(position);

                        final TextView textView = (TextView) view;
                        //  holder1.img_progress.getTag(position);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ChatRoomId = chatMessages.get(position).getChatRoomId();
                                ReceiverImage = chatMessages.get(position).getAttachment();
                                Message_id = chatMessages.get(position).getMessageId();
                                if (isnet()) {
                                    new StartSession(context, new CallbackInterface() {
                                        @Override
                                        public void callMethod() {


                                            textView.setVisibility(View.GONE);
                                        /*holder1.img_progress.getTag(position);
                                        holder1.img_progress.setVisibility(View.VISIBLE);*/
                                            new DownloadImageMethod().execute(ChatRoomId, ReceiverImage, Message_id);

                                        }

                                        @Override
                                        public void callfailMethod(String msg) {

                                        }
                                    });

                                }
                            }
                        });


                    }


                });


                holder1.txt_mp3_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        final TextView textView = (TextView) view;
                        // holder1.img_progress.getTag(position);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                view.getTag(position);

                                ChatRoomId = chatMessages.get(position).getChatRoomId();
                                ReceiverImage = chatMessages.get(position).getAttachment();
                                Message_id = chatMessages.get(position).getMessageId();
                                if (isnet()) {
                                    new StartSession(context, new CallbackInterface() {
                                        @Override
                                        public void callMethod() {


                                            textView.setVisibility(View.GONE);
                                            //  holder1.img_progress.getTag(position);
                                            //   holder1.img_progress.setVisibility(View.VISIBLE);

                                            new DownloadMP3Method().execute(ChatRoomId, ReceiverImage, Message_id);

                                        }

                                        @Override
                                        public void callfailMethod(String msg) {

                                        }
                                    });

                                }
                            }
                        });


                    }


                });

                holder1.txt_video_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final TextView textView = (TextView) view;
                        // holder1.img_progress.getTag(position);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                view.getTag(position);

                                ChatRoomId = chatMessages.get(position).getChatRoomId();
                                ReceiverImage = chatMessages.get(position).getAttachment();
                                Message_id = chatMessages.get(position).getMessageId();
                                if (isnet()) {
                                    new StartSession(context, new CallbackInterface() {
                                        @Override
                                        public void callMethod() {


                                            textView.setVisibility(View.GONE);
                                            new DownloadVideoMethod().execute(ChatRoomId, ReceiverImage, Message_id);

                                        }

                                        @Override
                                        public void callfailMethod(String msg) {

                                        }
                                    });

                                }
                            }
                        });


                    }


                });

                holder1.mPlayMedia1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mp = new MediaPlayer();

                        Uri uri = Uri.parse(chatMessages.get(position).getAttachment());
                        try {
                            mp.setDataSource(uri.toString());
                            mp.prepare();
                            mp.start();
                            Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder1.mPauseMedia1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mp.isPlaying()) {
                            mp.pause();
                        } else {
                            mp.start();
                        }
                        Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show();


                    }
                });
            }
                convertView.setTag(holder1);

        } else {

            holder1 = (ViewHolder) convertView.getTag();
        }



        chatMessage = (ChatMessage) getItem(position);
        if (Message.equals("Sender")) {
            String Msg = chatMessage.getMessage();
            if (Msg == null || Msg.equals("")) {
                holder1.incoming_layout_bubble.setVisibility(View.GONE);
            } else {
                holder1.incoming_layout_bubble.setVisibility(View.VISIBLE);
            }


            String MessageDecode = StringEscapeUtils.unescapeJava(chatMessage.getMessage());
            holder1.messageTextView.setText(MessageDecode);
            chat_time = chatMessage.getMessageDate();


                /*DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mmaa");
                DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                try {
                    date = originalFormat.parse(chat_time);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                chat_time = targetFormat.format(date);

*/
              //  holder1.timeTextView.setText(chat_time);


            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");


               /* if (chat_time.contains("AM")) {
                    String a = chat_time.replace("AM", "a.m.");
                    chat_time = a;
                } else if (chat_time.contains("PM")) {
                    String a = chat_time.replace("PM", "p.m.");
                    chat_time = a;

                }*/

            DateFormat targetFormat = new SimpleDateFormat("dd MMM hh:mm aa");
                    try {
                        date = originalFormat.parse(chat_time);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    chat_time = targetFormat.format(date);


                    holder1.timeTextView.setText(chat_time);
            IsDownloaded = chatMessage.getIsDownloaded();
            if (IsDownloaded.equals("Yes")) {
                holder1.txt_message_delivered.setVisibility(View.VISIBLE);
            }else {
                holder1.txt_message_delivered.setVisibility(View.GONE);
            }


            holder1.textview_username.setVisibility(View.GONE);
            String Attachment = chatMessage.getAttachment();

            if (Attachment == null || Attachment.equals("")) {
                holder1.rel_image.setVisibility(View.GONE);

            } else {
                holder1.rel_image.setVisibility(View.VISIBLE);
                imgFile = new File(Attachment);
                String filename = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);

               // holder1.textview_time1.setText(filename + "  " + chat_time);
                holder1.textview_time1.setText(chat_time);

                if (Attachment.contains(".pdf")) {
                    holder1.img_chat.setImageResource(R.drawable.pd);
                } else if (Attachment.contains("mp3")) {
                    holder1.len_audio_play.setVisibility(View.VISIBLE);
                    holder1.rel_image.setVisibility(View.GONE);
                    imgFile = new File(Attachment);
                    String filename1 = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
                   // holder1.textview_mp3_time.setText(filename1 + "  " + chat_time);
                    holder1.textview_mp3_time.setText(chat_time);


                } else if (Attachment.contains("mp4")) {
                    holder1.video_player_view.reset();
                    holder1.rel_video_view.setVisibility(View.VISIBLE);
                    holder1.rel_image.setVisibility(View.GONE);
                    imgFile = new File(Attachment);
                    String filename1 = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
                    videoUri = Uri.parse(Attachment);
                    try {
                        holder1.video_player_view.setVideoURI(videoUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   // holder1.textview_video_time.setText(filename1 + "  " + chat_time);
                    holder1.textview_video_time.setText(chat_time);

                } else {
                    if (imgFile.exists()) {
                        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 100, 100, true);

                        //  Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        // bitmap = ImageUtils.getInstant().getCompressedBitmap(imgFile.getAbsolutePath());
                        holder1.img_chat.setImageBitmap(bitmap);
                    }
                }

            }





        } else if (Message.equals(" ") || Message.equalsIgnoreCase("")) {
            String Msg = chatMessage.getMessage();

            if (Msg == null || Msg.equals("")) {
                holder1.video_player_view.setActivity((Activity) context);
                holder1.outgoing_layout_bubble.setVisibility(View.GONE);

            } else {
                holder1.outgoing_layout_bubble.setVisibility(View.VISIBLE);

            }


            String MessageDecode = StringEscapeUtils.unescapeJava(chatMessage.getMessage());
            holder1.messageTextView.setText(MessageDecode);
            holder1.textview_username.setText(chatMessage.getUsername());
            Message_id = chatMessage.getMessageId();
            IsDownloaded = chatMessage.getIsDownloaded();
            chat_time = chatMessage.getMessageDate();

                    DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy HH:mmaa", Locale.ENGLISH);

                    DateFormat targetFormat = new SimpleDateFormat("dd MMM hh:mm aa");
                    try {
                        date = originalFormat.parse(chat_time);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    chat_time = targetFormat.format(date);
                    holder1.timeTextView.setText(chat_time);


            holder1.txt_download.setTag(position);


            ReceiverImage = chatMessages.get(position).getAttachment();

            holder1.outgoing_img_chat.setTag(chatMessage.getAttachment());
            holder1.video_player_view.setTag(chatMessage.getAttachment());

            if (ReceiverImage == null || ReceiverImage.equals("")) {
                holder1.outgoing_rel_image.setVisibility(View.GONE);
                holder1.textview_time1.setText(chat_time);


            } else {
                if (ReceiverImage.contains(".pdf")) {
                    imgFileReceiver = new File(ReceiverImage);
                    filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                    holder1.outgoing_img_chat.setImageResource(R.drawable.pd);
                    holder1.outgoing_rel_image.setVisibility(View.VISIBLE);
                    holder1.textview_mp3_time.setText(chat_time);

                    //holder1.textview_time1.setText(filename + "  " + chat_time);
                    holder1.textview_username1.setText(chatMessage.getUsername());

                    if (IsDownloaded.equals("Yes")) {
                        filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                        holder1.outgoing_img_chat.setEnabled(true);
                        holder1.txt_download.setVisibility(View.GONE);
                        holder1.outgoing_img_chat.setImageResource(R.drawable.pd);
                       // holder1.textview_time1.setText(filename + "  " + chat_time);
                        holder1.textview_time1.setText(chat_time);

                        holder1.textview_username1.setText(chatMessage.getUsername());
                    }

                } else if (ReceiverImage.contains("mp3")) {
                    holder1.len_audio_play.setVisibility(View.VISIBLE);
                    imgFile = new File(ReceiverImage);
                    String filename1 = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
                  //  holder1.textview_mp3_time.setText(filename1 + "  " + chat_time);
                    holder1.textview_mp3_time.setText(chat_time);
                    holder1.textview_mp3_username.setText(chatMessage.getUsername());
                    holder1.outgoing_rel_image.setVisibility(View.GONE);
                    if (IsDownloaded.equals("Yes")) {
                        imgFile = new File(ReceiverImage);
                        String filename = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
                       // holder1.textview_mp3_time.setText(filename + "  " + chat_time);
                        holder1.textview_mp3_time.setText(chat_time);
                        holder1.textview_mp3_username.setText(chatMessage.getUsername());
                        holder1.outgoing_rel_image.setVisibility(View.GONE);
                        holder1.txt_mp3_download.setVisibility(View.GONE);
                        holder1.play_pause_layout.setVisibility(View.VISIBLE);
                    }
                } else if (ReceiverImage.contains("mp4")) {
                    holder1.video_player_view.reset();
                    holder1.rel_video_view.setVisibility(View.VISIBLE);
                    holder1.outgoing_rel_image.setVisibility(View.GONE);
                    holder1.txt_video_download.setVisibility(View.VISIBLE);
                    imgFile = new File(ReceiverImage);
                    String filename1 = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
                    videoUri = Uri.parse(ReceiverImage);
                    //holder1.textview_mp3_time.setText(filename1 + "  " + chat_time);
                    holder1.textview_mp3_time.setText(chat_time);

                    try {
                        holder1.video_player_view.setVideoURI(videoUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   // holder1.textview_video_time.setText(filename1 + "  " + chat_time);
                    holder1.textview_video_time.setText(chat_time);
                    holder1.textview_video_username.setText(chatMessage.getUsername());
                    if (IsDownloaded.equals("Yes")) {
                        holder1.video_player_view.reset();
                        holder1.video_player_view.setVisibility(View.VISIBLE);
                        imgFile = new File(ReceiverImage);
                        String filename = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
                        videoUri = Uri.parse(ReceiverImage);
                        try {
                            holder1.video_player_view.setVideoURI(videoUri);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                      //  holder1.textview_video_time.setText(filename + "  " + chat_time);
                        holder1.textview_video_time.setText(chat_time);
                        holder1.textview_video_username.setText(chatMessage.getUsername());
                        holder1.outgoing_rel_image.setVisibility(View.GONE);
                        holder1.txt_video_download.setVisibility(View.GONE);


                    }
                } else {
                    holder1.outgoing_rel_image.setVisibility(View.VISIBLE);
                    imgFileReceiver = new File(ReceiverImage);
                    if (IsDownloaded.equals("Yes")) {
                        filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                        holder1.txt_download.setVisibility(View.GONE);
                        holder1.outgoing_img_chat.setEnabled(true);
                       // holder1.textview_time1.setText(filename + "  " + chat_time);
                        holder1.textview_time1.setText(chat_time);
                        holder1.textview_username1.setText(chatMessage.getUsername());
                        if (imgFileReceiver.exists()) {
                           // Bitmap myBitmap = BitmapFactory.decodeFile(imgFileReceiver.getAbsolutePath());
                            bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFileReceiver.getAbsolutePath()), 100, 100, true);

                            // bitmap = ImageUtils.getInstant().getCompressedBitmap(imgFileReceiver.getAbsolutePath());
                            holder1.outgoing_img_chat.setImageBitmap(bitmap);

                        }
                    } else {
                        filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                        holder1.textview_time1.setText(chat_time);
                        //holder1.textview_time1.setText(filename + "  " + chat_time);
                        Glide.with(context)
                                .load(ReceiverImage)
                                .apply(RequestOptions.bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(60)))
                                .into(holder1.outgoing_img_chat);
                        holder1.textview_username1.setText(chatMessage.getUsername());


                    }
                }
            }

            holder1.txt_download.setTag(chatMessage);
            holder1.mPlayMedia1.setTag(chatMessage);
            holder1.outgoing_img_chat.setTag(chatMessage.getAttachment());
            holder1.video_player_view.setTag(chatMessage.getAttachment());


        }
        return convertView;

    }


    /*@Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header_list_lay, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String date = chatMessages.get(position).getMessageDate();


        String output = date.substring(0, 10);  // Output : 2012/01/20

        holder.text.setText(output);

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return 0;
    }*/


    public static class ViewHolder {
        public static TextView textview_username, textview_username1;
        public static TextView messageTextView;
        public static TextView timeTextView, textview_time1, txt_download;
        public static ImageView img_chat, outgoing_img_chat;
        public static RelativeLayout rel_image, outgoing_rel_image;
        public static LinearLayout incoming_layout_bubble, outgoing_layout_bubble;
        // public static ProgressBar img_progress,img_video_progress1;
        public static LinearLayout len_audio_play, play_pause_layout;
        public static ImageView mPlayMedia, mPlayMedia1, pickAudio;
        public static ImageView mPauseMedia, mPauseMedia1, img_video_play;
        public static SeekBar mMediaSeekBar;
        public static TextView mRunTime,txt_message_delivered;
        public static TextView mTotalTime, textview_mp3_username, textview_mp3_time, txt_mp3_download, textview_video_time, textview_video_username, txt_video_download;
        public static RelativeLayout video_frame;
        public static FullscreenVideoLayout video_player_view;
        public static RelativeLayout rel_video_view;
    }

    class HeaderViewHolder {
        TextView text;
    }


    private boolean isnet() {
        // TODO Auto-generated method stub
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

    class DownloadImageMethod extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        int position;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.crm_progress_lay);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //  holder1.txt_download.setVisibility(View.GONE);


        }

        protected String doInBackground(String... urls) {


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("chatRoomId", ChatRoomId);
                jsonObject.put("filepath", ReceiverImage);
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
            // holder1.img_progress.setVisibility(View.GONE);
            mProgressDialog.dismiss();
            //holder1.txt_download.setVisibility(View.GONE);


            res = String.valueOf(responsemsg);
            imgFile = new File(ReceiverImage);
            String filename1 = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);

            if (filename1.contains(".pdf")) {


                {
                    new DownloadFile().execute(ReceiverImage, filename1);
                }
                    /*String path1 = android.os.Environment.getExternalStorageDirectory()
                            .toString();
                    File file = new File(path1 + "/" + "CRM");
                    if (!file.exists())
                        file.mkdirs();
                    File filename = new File(file.getAbsolutePath() + "/" + filename1);
                    String f= String.valueOf(filename);
                    FileOutputStream out = new FileOutputStream(filename);
                    out.flush();
                    out.close();
                    contentValues.put("IsDownloaded", "Yes");
                    contentValues.put("Attachment", f);
                    contentValues.put("ChatRoomId",ChatRoomId);
                    sql.update(db.TABLE_CHAT_GROUP_MESSAGES, contentValues, "MessageId='"+Message_id+"'",null);
                    sql.close();*/


            } else {


                {
                    new DownloadFile().execute(ReceiverImage, filename1);
                }
                //saveMyImage("CRM", ReceiverImage, filename1);
            }
        }
    }


    class DownloadMP3Method extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        int position;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.crm_progress_lay);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //  holder1.txt_download.setVisibility(View.GONE);


        }

        protected String doInBackground(String... urls) {


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("chatRoomId", ChatRoomId);
                jsonObject.put("filepath", ReceiverImage);
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
            // holder1.img_progress.setVisibility(View.GONE);
            mProgressDialog.dismiss();
            //holder1.txt_mp3_download.setVisibility(View.GONE);


            res = String.valueOf(responsemsg);
            imgFile = new File(ReceiverImage);
            String filename1 = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
            new DownloadFile().execute(ReceiverImage, filename1);

            //saveMyImage("CRM", ReceiverImage, filename1);

        }
    }

    class DownloadVideoMethod extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        int position;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context);

                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
                mProgressDialog.setContentView(R.layout.crm_progress_lay);
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //holder1.txt_download.setVisibility(View.GONE);
            }

        }

        protected String doInBackground(String... urls) {


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("chatRoomId", ChatRoomId);
                jsonObject.put("filepath", ReceiverImage);
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
            // holder1.img_progress.setVisibility(View.GONE);
            //holder1.txt_video_download.setVisibility(View.GONE);

            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            res = String.valueOf(responsemsg);
            imgFile = new File(ReceiverImage);
            String filename1 = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
            new DownloadFile().execute(ReceiverImage, filename1);


        }
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
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
            File file = new File(path1 + "/" + "Vwb");
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

    public void downloadFile(String fileUrl, File directory) {
        try {
            fileUrl = fileUrl.replaceAll(" ", "%20");

            URL url = new URL(fileUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int  serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();
            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();

            contentValues.put("IsDownloaded", "Yes");
            contentValues.put("Attachment", file1);
            contentValues.put("ChatRoomId", ChatRoomId);
            sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId='" + Message_id + "'", null);
            sql.close();

            handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    if (file1.contains(".pdf")) {
                        Toast.makeText(context, "Pdf downloaded successfully", Toast.LENGTH_SHORT).show();
                        AddChatRoomActivity addChatRoomActivity=new AddChatRoomActivity();
                        addChatRoomActivity.groupMessage(ChatRoomId,context);

                    } else if (file1.contains(".jpg")||(file1.contains(".png"))){
                        AddChatRoomActivity  addChatRoomActivity=new AddChatRoomActivity();
                        addChatRoomActivity.groupMessage(ChatRoomId,context);
                        Toast.makeText(context, "Image downloaded successfully", Toast.LENGTH_SHORT).show();


                    } else if (file1.contains("mp3")) {
                        Toast.makeText(context, "Audio downloaded successfully", Toast.LENGTH_SHORT).show();
                        AddChatRoomActivity  addChatRoomActivity=new AddChatRoomActivity();
                        addChatRoomActivity.groupMessage(ChatRoomId,context);

                    } else {
                        Toast.makeText(context, "Video downloaded successfully", Toast.LENGTH_SHORT).show();
                        AddChatRoomActivity  addChatRoomActivity=new AddChatRoomActivity();
                        addChatRoomActivity.groupMessage(ChatRoomId,context);

                    }
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

    private String makedate(String olddate) {
        // TODO Auto-generated method stub
        DateFormat originalFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        try {
            date = originalFormat.parse(olddate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        chat_time = targetFormat.format(date);
        return chat_time;
    }

}

