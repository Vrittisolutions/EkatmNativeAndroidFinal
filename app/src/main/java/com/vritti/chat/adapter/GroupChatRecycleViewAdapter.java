package com.vritti.chat.adapter;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.vritti.chat.activity.AddChatRoomActivity;

import com.vritti.chat.activity.ImageFullScreenActivity;

import com.vritti.chat.bean.ChatMessage;
import com.vritti.chat.bean.ChatModelObject;
import com.vritti.chat.bean.DateObject;
import com.vritti.chat.bean.ListObject;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Optional;
import jp.wasabeef.fresco.processors.BlurPostprocessor;

import static android.content.Context.MODE_PRIVATE;

public class GroupChatRecycleViewAdapter extends RecyclerView.Adapter<GroupChatRecycleViewAdapter.ChatHolder> {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    public static DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    public ArrayList<ListObject> chatMessages;
    public LayoutInflater mInflater;
    String ChatRoomId, ReceiverImage, res, Message_id, IsDownloaded, localMessage_id;
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
    public ProgressDialog mProgressDialog;
    private Date date;
    private String dotformat = "No";
    public static MediaPlayer mp = new MediaPlayer();
    long timestamp;
    private int pos;
    String userType;
    int isUnread = -1;
    int selectedPos = -1;

    public GroupChatRecycleViewAdapter(ArrayList<ListObject> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO,
                MODE_PRIVATE);
        this.context = context;
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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mProgressDialog = new ProgressDialog(context);
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = null;
        if (viewType == ListObject.TYPE_GENERAL_RIGHT) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vwb_chat_user1_item, parent, false);  // sender
        } else if (viewType == ListObject.TYPE_GENERAL_LEFT) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vwb_chat_user2_item, parent, false); //reciver
        } else if (viewType == ListObject.TYPE_DATE) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.date_formate, parent, false);
        }

        return new ChatHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, final int position) {
        if (getItemViewType(position) != 0) {
            chatMessage = ((ChatModelObject) chatMessages.get(position)).getChatMessage();
            String Msg = chatMessage.getMessage();
            userType = chatMessage.getStatus();


            if (userType.equals("")) { //RECIVER
                if (selectedPos != -1 && selectedPos == position) {
                    holder.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.green_transprent));
                    selectedPos = -1;
                } else {
                    holder.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                }// reciver
                if (Msg == null || Msg.equals("")) {
                    //   holder.video_player_view.setActivity((Activity) context);
                    holder.outgoing_layout_bubble.setVisibility(View.GONE);

                } else {
                    holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                }
                Log.i("userType", userType);
                String MessageDecode = StringEscapeUtils.unescapeJava(chatMessage.getMessage());
                holder.messageTextView.setText(MessageDecode);
                if (AppCommon.getInstance(context).getTextSize() == 0)
                    holder.messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.small));
                else if (AppCommon.getInstance(context).getTextSize() == 1)
                    holder.messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.mediam));
                else if (AppCommon.getInstance(context).getTextSize() == 2)
                    holder.messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.large));
                holder.textview_username.setText(chatMessage.getUsername());
                Message_id = chatMessage.getMessageId();
                IsDownloaded = chatMessage.getIsDownloaded();

                timestamp = chatMessage.getMessageDate();


                SimpleDateFormat targetFormat;
                if (isUnread < position && isUnread != -1) {
                    boolean isToday = isTodayDay(timestamp);
                    // if(isToday)
                    targetFormat = new SimpleDateFormat("dd MMM  hh:mm aa");
                } else
                    targetFormat = new SimpleDateFormat("hh:mm aa");

                Date date = new Date(timestamp);
                chat_time = targetFormat.format(date);
                holder.timeTextView.setText(chat_time);
                holder.txt_download.setTag(position);


                ReceiverImage = chatMessage.getAttachment();


                holder.outgoing_img_chat.setTag(chatMessage.getAttachment());

                //holder.video_player_view.setTag(chatMessage.getAttachment());

                if (ReceiverImage == null || ReceiverImage.equals("")) {
                    holder.outgoing_rel_image.setVisibility(View.GONE);
                    holder.textview_time1.setText(chat_time);
                    if (!(chatMessage.getMessageType().equals("map:") || chatMessage.getMessageType().equals("video:"))) {
                        // holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        if (chatMessage.getMessageType().equals("text:")) {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                            holder.replyLayout.setVisibility(View.GONE);
                            holder.contactLayout.setVisibility(View.GONE);
                            if (chatMessage.getAttachment() != null) {
                                holder.outgoing_layout_bubble.setVisibility(View.GONE);
                            } else {
                                holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                            }
                        } else if (chatMessage.getMessageType().equals("contact:")) {
                            String contectInfo[] = chatMessage.getMessage().split(" ");
                            if (contectInfo[0].contains("#contact")) {
                                String contectInfoNew[] = chatMessage.getMessage().replace("#contact", "").split(" ");
                                String userName = "";
                                for (int i = 0; i < contectInfoNew.length - 1; i++) {
                                    userName += contectInfoNew[i] + " ";
                                }
                                holder.contectUserName.setText(userName);
                            } else {
                                String userName = "";
                                for (int i = 0; i < contectInfo.length - 1; i++) {
                                    userName += contectInfo[i] + " ";
                                }
                                holder.contectUserName.setText(userName);
                                // holder.contectUserName.setText(contectInfo[0]);
                            }
                            holder.contactLayout.setVisibility(View.VISIBLE);
                            holder.replyLayout.setVisibility(View.GONE);
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                          /*  String contectInfo[] = chatMessage.getMessage().split(" ");
                            holder.contectUserName.setText(contectInfo[0]);*/
                            holder.textview_time_contact.setText(chat_time);

                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                            holder.replyLayout.setVisibility(View.VISIBLE);
                            holder.closeImg.setVisibility(View.INVISIBLE);
                            holder.contactLayout.setVisibility(View.GONE);
                            String[] value = chatMessage.getMessageType().split("reply:");
                            int rpl = getUserName(value[1]);
                            if (rpl != -1) {
                                ChatMessage localObj = ((ChatModelObject) chatMessages.get(rpl)).getChatMessage();
                                if (localObj.getStatus().equals("Sender")) {
                                    holder.userNameText.setText("You");
                                } else
                                    holder.userNameText.setText(localObj.getUsername());
                                if (!localObj.getMessage().equals("")) {
                                    String replyDecode = StringEscapeUtils.unescapeJava(localObj.getMessage());
                                    holder.replyText.setText(replyDecode);
                                    holder.replyImage1.setVisibility(View.GONE);
                                } else {
                                    if (localObj.getAttachment() != null) {
                                        File imageFile = setReplyImage(localObj.getAttachment());
                                        if (imageFile != null) {
                                            holder.replyImage1.setVisibility(View.VISIBLE);
                                            holder.replyImage1.setController(getImageDrableWithoutBlure(holder.replyImage1, Uri.fromFile(imageFile), 100));
                                        } else {

                                        }
                                        holder.replyText.setText("Image");
                                        holder.replyLayout.setVisibility(View.VISIBLE);
                                        holder.contactLayout.setVisibility(View.GONE);
                                    } else
                                        holder.replyImage1.setVisibility(View.GONE);
                                }

                                //holder.closeImg.setVisibility(View.GONE);
                                //String[] value1 = chatMessage.getMessageType().split("@@");
                            } else {
                                holder.replyLayout.setVisibility(View.GONE);
                                holder.closeImg.setVisibility(View.INVISIBLE);
                            }
                        }
                    } else if (chatMessage.getMessageType().equals("map:")) {
                        holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        holder.outgoing_rel_image.setVisibility(View.VISIBLE);
                        holder.outgoing_img_chat.setImageURI(chatMessage.getMessage());
                        holder.outgoing_img_chat.setEnabled(true);
                        holder.txt_download.setVisibility(View.GONE);
                        holder.textview_time1.setText(chat_time);
                        holder.contactLayout.setVisibility(View.GONE);
                        holder.textview_username1.setText(chatMessage.getUsername());
                        holder.txt_image_delivered.setVisibility(View.VISIBLE);
                    } else {
                        holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        holder.outgoing_rel_image.setVisibility(View.VISIBLE);
                        holder.contactLayout.setVisibility(View.GONE);
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(String.valueOf(chatMessage.getMessage()));
                        Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(2000);

                        holder.outgoing_img_chat.setImageBitmap(bmFrame);
                        holder.outgoing_img_chat.setEnabled(true);
                        holder.txt_download.setVisibility(View.GONE);
                        holder.textview_time1.setText(chat_time);
                        holder.playIcon.setVisibility(View.VISIBLE);
                        holder.textview_username1.setText(chatMessage.getUsername());
                        holder.txt_image_delivered.setVisibility(View.VISIBLE);
                        if (chatMessage.getAttachment() != null) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        }

                    }


                } else {
                    holder.len_audio_play.setVisibility(View.GONE);
                    holder.outgoing_rel_image.setVisibility(View.GONE);
                    if (ReceiverImage.contains(".pdf")) {
                        imgFileReceiver = new File(ReceiverImage);
                        filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                        holder.outgoing_img_chat.setImageResource(R.drawable.pd);
                        holder.outgoing_rel_image.setVisibility(View.VISIBLE);
                        holder.textview_time1.setText(chat_time);

                        //holder.textview_time1.setText(filename + "  " + chat_time);
                        holder.textview_username1.setText(chatMessage.getUsername());
                        if (chatMessage.getAttachment() != null) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        }

                        if (IsDownloaded.equals("Yes")) {
                            filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                            holder.outgoing_img_chat.setEnabled(true);
                            holder.txt_download.setVisibility(View.GONE);
                            holder.outgoing_img_chat.setImageResource(R.drawable.pd);
                            // holder.textview_time1.setText(filename + "  " + chat_time);
                            holder.textview_time1.setText(chat_time);
                            holder.txt_image_delivered.setVisibility(View.VISIBLE);
                            holder.img_progress1.setVisibility(View.GONE);
                            holder.textview_username1.setText(chatMessage.getUsername());
                        } else {
                            if (chatMessage.isDownload()) {
                                holder.img_progress1.setVisibility(View.VISIBLE);
                                holder.txt_download.setVisibility(View.GONE);
                            } else {
                                holder.img_progress1.setVisibility(View.GONE);
                                holder.txt_download.setVisibility(View.VISIBLE);
                            }
                            holder.txt_image_delivered.setVisibility(View.GONE);
                        }

                    } else if (ReceiverImage.contains(".doc")) {
                        imgFileReceiver = new File(ReceiverImage);
                        filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                        holder.outgoing_img_chat.setImageResource(R.drawable.word);
                        holder.outgoing_rel_image.setVisibility(View.VISIBLE);
                        holder.textview_time1.setText(chat_time);
                        if (chatMessage.getAttachment() != null) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        }
                        //holder.textview_time1.setText(filename + "  " + chat_time);
                        holder.textview_username1.setText(chatMessage.getUsername());

                        if (IsDownloaded.equals("Yes")) {
                            filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                            holder.outgoing_img_chat.setEnabled(true);
                            holder.txt_download.setVisibility(View.GONE);
                            holder.outgoing_img_chat.setImageResource(R.drawable.word);
                            // holder.textview_time1.setText(filename + "  " + chat_time);
                            holder.textview_time1.setText(chat_time);
                            holder.img_progress1.setVisibility(View.GONE);
                            holder.textview_username1.setText(chatMessage.getUsername());
                            holder.txt_image_delivered.setVisibility(View.VISIBLE);
                        } else {
                            holder.txt_image_delivered.setVisibility(View.GONE);
                            if (chatMessage.isDownload()) {
                                holder.img_progress1.setVisibility(View.VISIBLE);
                                holder.txt_download.setVisibility(View.GONE);
                            } else {
                                holder.img_progress1.setVisibility(View.GONE);
                                holder.txt_download.setVisibility(View.VISIBLE);
                            }
                        }


                    } else if (ReceiverImage.contains(".docx")) {
                        imgFileReceiver = new File(ReceiverImage);
                        filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                        holder.outgoing_img_chat.setImageResource(R.drawable.word);
                        holder.outgoing_rel_image.setVisibility(View.VISIBLE);
                        holder.textview_time1.setText(chat_time);

                        //holder.textview_time1.setText(filename + "  " + chat_time);
                        holder.textview_username1.setText(chatMessage.getUsername());
                        if (chatMessage.getAttachment() != null) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        }

                        if (IsDownloaded.equals("Yes")) {
                            filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                            holder.outgoing_img_chat.setEnabled(true);
                            holder.txt_download.setVisibility(View.GONE);
                            holder.outgoing_img_chat.setImageResource(R.drawable.word);
                            // holder.textview_time1.setText(filename + "  " + chat_time);
                            holder.textview_time1.setText(chat_time);
                            holder.img_progress1.setVisibility(View.GONE);
                            holder.txt_image_delivered.setVisibility(View.VISIBLE);
                            holder.textview_username1.setText(chatMessage.getUsername());
                        } else {
                            if (chatMessage.isDownload()) {
                                holder.img_progress1.setVisibility(View.VISIBLE);
                                holder.txt_download.setVisibility(View.GONE);
                            } else {
                                holder.img_progress1.setVisibility(View.GONE);
                                holder.txt_download.setVisibility(View.VISIBLE);
                            }
                            holder.txt_image_delivered.setVisibility(View.GONE);
                        }


                    } else if (ReceiverImage.contains(".ppt")) {
                        imgFileReceiver = new File(ReceiverImage);
                        filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                        holder.outgoing_img_chat.setImageResource(R.drawable.powerpoint);
                        holder.outgoing_rel_image.setVisibility(View.VISIBLE);
                        holder.textview_time1.setText(chat_time);

                        //holder.textview_time1.setText(filename + "  " + chat_time);
                        holder.textview_username1.setText(chatMessage.getUsername());
                        if (chatMessage.getAttachment() != null) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        }

                        if (IsDownloaded.equals("Yes")) {
                            filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                            holder.outgoing_img_chat.setEnabled(true);
                            holder.txt_download.setVisibility(View.GONE);
                            holder.outgoing_img_chat.setImageResource(R.drawable.powerpoint);
                            // holder.textview_time1.setText(filename + "  " + chat_time);
                            holder.textview_time1.setText(chat_time);
                            holder.img_progress1.setVisibility(View.GONE);
                            holder.txt_image_delivered.setVisibility(View.VISIBLE);
                            holder.textview_username1.setText(chatMessage.getUsername());
                        } else {
                            if (chatMessage.isDownload()) {
                                holder.img_progress1.setVisibility(View.VISIBLE);
                                holder.txt_download.setVisibility(View.GONE);
                            } else {
                                holder.img_progress1.setVisibility(View.GONE);
                                holder.txt_download.setVisibility(View.VISIBLE);
                            }
                            holder.txt_image_delivered.setVisibility(View.GONE);
                        }


                    } else if (ReceiverImage.contains(".pptx")) {
                        imgFileReceiver = new File(ReceiverImage);
                        filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                        holder.outgoing_img_chat.setImageResource(R.drawable.powerpoint);
                        holder.outgoing_rel_image.setVisibility(View.VISIBLE);
                        holder.textview_time1.setText(chat_time);

                        //holder.textview_time1.setText(filename + "  " + chat_time);
                        holder.textview_username1.setText(chatMessage.getUsername());
                        if (chatMessage.getAttachment() != null) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        }

                        if (IsDownloaded.equals("Yes")) {
                            filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                            holder.outgoing_img_chat.setEnabled(true);
                            holder.txt_download.setVisibility(View.GONE);
                            holder.outgoing_img_chat.setImageResource(R.drawable.powerpoint);
                            // holder.textview_time1.setText(filename + "  " + chat_time);
                            holder.textview_time1.setText(chat_time);
                            holder.img_progress1.setVisibility(View.GONE);
                            holder.txt_image_delivered.setVisibility(View.VISIBLE);
                            holder.textview_username1.setText(chatMessage.getUsername());
                        } else {
                            if (chatMessage.isDownload()) {
                                holder.img_progress1.setVisibility(View.VISIBLE);
                                holder.txt_download.setVisibility(View.GONE);
                            } else {
                                holder.img_progress1.setVisibility(View.GONE);
                                holder.txt_download.setVisibility(View.VISIBLE);
                            }
                            holder.txt_image_delivered.setVisibility(View.GONE);
                        }


                    } else if (ReceiverImage.contains(".xls")) {
                        imgFileReceiver = new File(ReceiverImage);
                        filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                        holder.outgoing_img_chat.setImageResource(R.drawable.excel);
                        holder.outgoing_rel_image.setVisibility(View.VISIBLE);
                        holder.textview_time1.setText(chat_time);

                        //holder.textview_time1.setText(filename + "  " + chat_time);
                        holder.textview_username1.setText(chatMessage.getUsername());
                        if (chatMessage.getAttachment() != null) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        }

                        if (IsDownloaded.equals("Yes")) {
                            filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                            holder.outgoing_img_chat.setEnabled(true);
                            holder.txt_download.setVisibility(View.GONE);
                            holder.outgoing_img_chat.setImageResource(R.drawable.excel);
                            holder.img_progress1.setVisibility(View.GONE);
                            // holder.textview_time1.setText(filename + "  " + chat_time);
                            holder.textview_time1.setText(chat_time);
                            holder.txt_image_delivered.setVisibility(View.VISIBLE);
                            holder.textview_username1.setText(chatMessage.getUsername());
                        } else {
                            if (chatMessage.isDownload()) {
                                holder.img_progress1.setVisibility(View.VISIBLE);
                                holder.txt_download.setVisibility(View.GONE);
                            } else {
                                holder.img_progress1.setVisibility(View.GONE);
                                holder.txt_download.setVisibility(View.VISIBLE);
                            }
                            holder.txt_image_delivered.setVisibility(View.GONE);
                        }


                    } else if (ReceiverImage.contains(".xlsx")) {
                        imgFileReceiver = new File(ReceiverImage);
                        filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                        holder.outgoing_img_chat.setImageResource(R.drawable.excel);
                        holder.outgoing_rel_image.setVisibility(View.VISIBLE);
                        holder.textview_time1.setText(chat_time);

                        //holder.textview_time1.setText(filename + "  " + chat_time);
                        holder.textview_username1.setText(chatMessage.getUsername());
                        if (chatMessage.getAttachment() != null) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        }

                        if (IsDownloaded.equals("Yes")) {
                            filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                            holder.outgoing_img_chat.setEnabled(true);
                            holder.txt_download.setVisibility(View.GONE);
                            holder.outgoing_img_chat.setImageResource(R.drawable.excel);
                            // holder.textview_time1.setText(filename + "  " + chat_time);
                            holder.textview_time1.setText(chat_time);
                            holder.img_progress1.setVisibility(View.GONE);
                            holder.txt_image_delivered.setVisibility(View.VISIBLE);
                            holder.textview_username1.setText(chatMessage.getUsername());
                        } else {
                            if (chatMessage.isDownload()) {
                                holder.img_progress1.setVisibility(View.VISIBLE);
                                holder.txt_download.setVisibility(View.GONE);
                            } else {
                                holder.img_progress1.setVisibility(View.GONE);
                                holder.txt_download.setVisibility(View.VISIBLE);
                            }
                            holder.txt_image_delivered.setVisibility(View.GONE);
                        }


                    } else if (ReceiverImage.contains("mp3")) {
                        holder.len_audio_play.setVisibility(View.VISIBLE);
                        imgFile = new File(ReceiverImage);
                        String filename1 = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
                        //  holder.textview_mp3_time.setText(filename1 + "  " + chat_time);
                        holder.textview_mp3_time.setText(chat_time);
                        holder.textview_mp3_username.setText(chatMessage.getUsername());
                        holder.outgoing_rel_image.setVisibility(View.GONE);
                        if (chatMessage.getAttachment() != null) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        }
                        if (IsDownloaded.equals("Yes")) {
                            imgFile = new File(ReceiverImage);
                            String filename = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
                            // holder.textview_mp3_time.setText(filename + "  " + chat_time);
                            holder.textview_mp3_time.setText(chat_time);
                            holder.textview_mp3_username.setText(chatMessage.getUsername());
                            holder.outgoing_rel_image.setVisibility(View.GONE);
                            holder.txt_mp3_download.setVisibility(View.GONE);
                            holder.img_progress1.setVisibility(View.GONE);
                            holder.play_pause_layout.setVisibility(View.VISIBLE);
                            holder.txt_image_delivered.setVisibility(View.VISIBLE);
                        } else {
                            if (chatMessage.isDownload()) {
                                holder.img_progress1.setVisibility(View.VISIBLE);
                                holder.txt_download.setVisibility(View.GONE);
                            } else {
                                holder.img_progress1.setVisibility(View.GONE);
                                holder.txt_download.setVisibility(View.VISIBLE);
                            }
                            holder.txt_image_delivered.setVisibility(View.GONE);
                        }
                    } else if (ReceiverImage.contains("mp4")) {
                        String path1 = Environment.getExternalStorageDirectory()
                                .toString();
                        File file = new File(path1 + "/" + "Vwb" + "/" + "Video" + "/" + "ReceiveVideos");
                        if (!file.exists())
                            file.mkdirs();
                        imgFileReceiver = new File(ReceiverImage);
                        if (chatMessage.getAttachment() != null) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        }
                        if (IsDownloaded.equals("Yes")) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                            holder.outgoing_rel_image.setVisibility(View.VISIBLE);
                            filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                            File checkFile = new File(file + "/" + filename);
                            holder.outgoing_img_chat.setEnabled(true);
                            holder.img_progress1.setVisibility(View.GONE);
                            // holder.textview_time1.setText(filename + "  " + chat_time);
                            holder.textview_time1.setText(chat_time);
                            holder.textview_username1.setText(chatMessage.getUsername());
                            holder.txt_image_delivered.setVisibility(View.VISIBLE);
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            //mediaMetadataRetriever.setDataSource(String.valueOf(chatMessage.getAttachment()));
                            //Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(2000);
                            mediaMetadataRetriever.setDataSource(ReceiverImage, new HashMap<String, String>());
                            Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1000);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmFrame.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                            byte[] byteArray = stream.toByteArray();
                            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            if (checkFile.exists()) {


                                holder.outgoing_img_chat.setImageBitmap(compressedBitmap);
                                // holder.outgoing_img_chat.setController(getImageDrableWithoutBlure(holder.outgoing_img_chat, Uri.fromFile(checkFile), 100));
                            } else {
                                holder.txt_download.setVisibility(View.VISIBLE);
                                holder.outgoing_img_chat.setEnabled(true);
                                ReceiverImage = String.valueOf(chatMessage.getAttachment().replaceAll(" ", "%20"));
                                // holder.outgoing_img_chat.setController(getImageDrable(holder.outgoing_img_chat, ReceiverImage, 100));
                                holder.outgoing_img_chat.setImageBitmap(bmFrame);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("IsDownloaded", "No");
                                contentValues.put("ChatRoomId", ChatRoomId);
                                sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId='" + chatMessage.getMessageId() + "'", null);
                            }
                           /* MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            //mediaMetadataRetriever.setDataSource(String.valueOf(chatMessage.getAttachment()));
                            //Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(2000);
                            mediaMetadataRetriever.setDataSource(ReceiverImage, new HashMap<String, String>());
                            Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1000);

                            holder.outgoing_img_chat.setImageBitmap(bmFrame);*/
                            holder.outgoing_img_chat.setEnabled(true);
                            holder.txt_download.setVisibility(View.GONE);
                            holder.textview_time1.setText(chat_time);
                            holder.playIcon.setVisibility(View.VISIBLE);
                            holder.textview_username1.setText(chatMessage.getUsername());
                            holder.txt_image_delivered.setVisibility(View.VISIBLE);

                        } else {
                            if (chatMessage.isDownload()) {
                                holder.img_progress1.setVisibility(View.VISIBLE);
                                holder.txt_download.setVisibility(View.GONE);
                            } else {
                                holder.img_progress1.setVisibility(View.GONE);
                                holder.txt_download.setVisibility(View.VISIBLE);
                                holder.playIcon.setVisibility(View.GONE);
                            }
                            holder.txt_image_delivered.setVisibility(View.GONE);
                            filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                            holder.textview_time1.setText(chat_time);

                            ReceiverImage = String.valueOf(ReceiverImage.replaceAll(" ", "%20"));
                            Log.d("ImageUri :", ReceiverImage);
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            //mediaMetadataRetriever.setDataSource(String.valueOf(chatMessage.getAttachment()));
                            //Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(2000);
                            mediaMetadataRetriever.setDataSource(ReceiverImage, new HashMap<String, String>());
                            Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1000);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmFrame.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                            byte[] byteArray = stream.toByteArray();
                            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            holder.outgoing_img_chat.setImageBitmap(compressedBitmap);
                            //holder.outgoing_img_chat.setController(getImageDrable(holder.outgoing_img_chat, ReceiverImage, 100));
                            holder.textview_username1.setText(chatMessage.getUsername());
                        }


                    } else {
                        holder.outgoing_rel_image.setVisibility(View.VISIBLE);
                        String path1 = Environment.getExternalStorageDirectory()
                                .toString();
                        File file = new File(path1 + "/" + "Vwb" + "/" + "Receive");
                        if (!file.exists())
                            file.mkdirs();
                        imgFileReceiver = new File(ReceiverImage);
                        if (new File(file + "/" + ReceiverImage.substring(ReceiverImage.lastIndexOf("/")).replace("/", "")).exists()) {
                            IsDownloaded = "Yes";
                        }
                        if (chatMessage.getAttachment() != null) {
                            holder.outgoing_layout_bubble.setVisibility(View.GONE);
                        } else {
                            holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                        }

                        if (IsDownloaded.equals("Yes")) {
                            filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                            File checkFile = new File(file + "/" + filename);
                            holder.txt_download.setVisibility(View.GONE);
                            holder.outgoing_img_chat.setEnabled(true);
                            holder.img_progress1.setVisibility(View.GONE);
                            // holder.textview_time1.setText(filename + "  " + chat_time);
                            holder.textview_time1.setText(chat_time);
                            holder.textview_username1.setText(chatMessage.getUsername());
                            holder.txt_image_delivered.setVisibility(View.VISIBLE);
                            if (checkFile.exists()) {

                                holder.outgoing_img_chat.setController(getImageDrableWithoutBlure(holder.outgoing_img_chat, Uri.fromFile(checkFile), 100));
                            } else {
                                holder.txt_download.setVisibility(View.VISIBLE);
                                holder.outgoing_img_chat.setEnabled(true);
                                ReceiverImage = String.valueOf(chatMessage.getAttachment().replaceAll(" ", "%20"));
                                holder.outgoing_img_chat.setController(getImageDrable(holder.outgoing_img_chat, ReceiverImage, 100));
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("IsDownloaded", "No");
                                contentValues.put("ChatRoomId", ChatRoomId);
                                sql.update(db.TABLE_CHAT_GROUP_MESSAGE, contentValues, "MessageId='" + chatMessage.getMessageId() + "'", null);
                            }
                            if (chatMessage.getAttachment() != null) {
                                holder.outgoing_layout_bubble.setVisibility(View.GONE);
                            } else {
                                holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if (chatMessage.isDownload()) {
                                holder.img_progress1.setVisibility(View.VISIBLE);
                                holder.txt_download.setVisibility(View.GONE);
                            } else {
                                holder.img_progress1.setVisibility(View.GONE);
                                holder.txt_download.setVisibility(View.VISIBLE);
                            }
                            holder.txt_image_delivered.setVisibility(View.GONE);
                            filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                            holder.textview_time1.setText(chat_time);

                            ReceiverImage = String.valueOf(ReceiverImage.replaceAll(" ", "%20"));
                            Log.d("ImageUri :", ReceiverImage);
                            holder.outgoing_img_chat.setController(getImageDrable(holder.outgoing_img_chat, ReceiverImage, 100));
                            holder.textview_username1.setText(chatMessage.getUsername());
                            holder.contactLayout.setVisibility(View.GONE);

                            if (chatMessage.getAttachment() != null && !chatMessage.getAttachment().equals("")) {
                                holder.outgoing_layout_bubble.setVisibility(View.GONE);
                                if (!chatMessage.getMessage().equals("") && !chatMessage.getMessage().equals("File") && !chatMessage.getMessage().equals("#File")) {
                                    holder.imageMessage.setVisibility(View.VISIBLE);
                                    holder.imageMessage.setText(chatMessage.getMessage().replace("#File" , " "));
                                } else
                                    holder.imageMessage.setVisibility(View.GONE);
                            } else {
                                holder.outgoing_layout_bubble.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                }
            } else {                                            // Sender
                Log.i("userType", userType);
                Log.i("listSize", String.valueOf(chatMessages.size()));

                //String Msg = chatMessage.getMessage();
                if (selectedPos != -1 && selectedPos == position) {
                    holder.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.green_transprent));
                    selectedPos = -1;
                } else {
                    holder.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                }
                timestamp = chatMessage.getMessageDate();
                SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm aa");
                Date date = new Date(timestamp);
                chat_time = targetFormat.format(date);
                if (Msg == null || Msg.equals("")) {
                    holder.incoming_layout_bubble.setVisibility(View.GONE);
                } else {
                    if (!(chatMessage.getMessageType().equals("map:") || chatMessage.getMessageType().equals("video:"))) {
                        if (chatMessage.getMessageType().equals("text:")) {
                            holder.incoming_layout_bubble.setVisibility(View.VISIBLE);
                            holder.replyLayout.setVisibility(View.GONE);
                            holder.contactLayout.setVisibility(View.GONE);
                        } else if (chatMessage.getMessageType().equals("contact:")) {
                            String contectInfo[] = chatMessage.getMessage().split(" ");
                            if (contectInfo[0].contains("#contact")) {
                                holder.contectUserName.setText(contectInfo[1]);
                            } else {
                                holder.contectUserName.setText(contectInfo[0]);
                            }
                            holder.contactLayout.setVisibility(View.VISIBLE);
                            holder.replyLayout.setVisibility(View.GONE);
                            holder.incoming_layout_bubble.setVisibility(View.GONE);
                            // String contectInfo[] = chatMessage.getMessage().split(" ");
                            holder.textview_time_contact.setText(chat_time);

                            // holder.contectUserName.setText(contectInfo[0]);
                            IsDownloaded = chatMessage.getIsDownloaded();
                            if (IsDownloaded.equals("Yes")) {

                                holder.txt_message_delivered2.setImageDrawable(context.getDrawable(R.drawable.deliver_msg));
                            } else {

                                holder.txt_message_delivered2.setImageDrawable(context.getDrawable(R.drawable.sent_msg));
                            }


                        } else {
                            holder.incoming_layout_bubble.setVisibility(View.VISIBLE);
                            holder.replyLayout.setVisibility(View.VISIBLE);
                            holder.closeImg.setVisibility(View.INVISIBLE);
                            holder.contactLayout.setVisibility(View.GONE);
                            String[] value = chatMessage.getMessageType().split("reply:");
                            int rpl = getUserName(value[1]);
                            if (rpl != -1) {
                                ChatMessage localObj = ((ChatModelObject) chatMessages.get(rpl)).getChatMessage();
                                if (localObj.getStatus().equals("Sender")) {
                                    holder.userNameText.setText("You");
                                } else
                                    holder.userNameText.setText(localObj.getUsername());
                                if (!localObj.getMessage().equals("")) {
                                    String replyDecode = StringEscapeUtils.unescapeJava(localObj.getMessage());
                                    holder.replyText.setText(replyDecode);
                                    holder.replyImage1.setVisibility(View.GONE);
                                } else {
                                    if (localObj.getAttachment() != null) {
                                        File imageFile = setReplyImage(localObj.getAttachment());
                                        if (imageFile != null) {
                                            holder.replyImage1.setVisibility(View.VISIBLE);
                                            holder.replyImage1.setController(getImageDrableWithoutBlure(holder.replyImage1, Uri.fromFile(imageFile), 100));
                                        }
                                        holder.replyText.setText("Image");
                                        holder.replyLayout.setVisibility(View.VISIBLE);
                                        holder.contactLayout.setVisibility(View.GONE);
                                    } else
                                        holder.replyImage1.setVisibility(View.GONE);
                                }
                                //holder.closeImg.setVisibility(View.GONE);
                                //String[] value1 = chatMessage.getMessageType().split("@@");
                            } else {
                                holder.replyLayout.setVisibility(View.GONE);
                                holder.closeImg.setVisibility(View.INVISIBLE);
                            }


                        }
                    } else if (chatMessage.getMessageType().equals("map:")) {
                        holder.incoming_layout_bubble.setVisibility(View.GONE);
                        holder.rel_image.setVisibility(View.VISIBLE);
                        holder.textview_time1.setText(chat_time);
                        Glide.with(context)
                                .load(StringEscapeUtils.unescapeJava(chatMessage.getMessage()))
                                .into(holder.img_chat);
                        holder.contactLayout.setVisibility(View.GONE);
                    } else {
                        holder.incoming_layout_bubble.setVisibility(View.GONE);
                        holder.rel_image.setVisibility(View.VISIBLE);
                        holder.textview_time1.setText(chat_time);
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        File tempFile = new File(chatMessage.getMessage());
                        if (tempFile.exists()) {
                            try {
                                mediaMetadataRetriever.setDataSource(String.valueOf(chatMessage.getMessage()));
                                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(2000);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                // bmFrame.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                                // byte[] byteArray = stream.toByteArray();
                                // Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                holder.img_chat.setImageBitmap(bmFrame);
                                holder.imageMessage.setText(chatMessage.getMessage().replace("#File" , " "));
                                holder.messageTextView.setVisibility(View.GONE);
                            } catch (Exception e) {
                                try {
                                    mediaMetadataRetriever.setDataSource("file://" + String.valueOf(chatMessage.getMessage()));
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    e.printStackTrace();
                                }
                            }
                        }
                        holder.playIcon.setVisibility(View.VISIBLE);
                        holder.contactLayout.setVisibility(View.GONE);
                    }


                }


                String MessageDecode = StringEscapeUtils.unescapeJava(chatMessage.getMessage());

                holder.messageTextView.setText(MessageDecode);
                if (AppCommon.getInstance(context).getTextSize() == 0)
                    holder.messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.small));
                else if (AppCommon.getInstance(context).getTextSize() == 1)
                    holder.messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.mediam));
                else if (AppCommon.getInstance(context).getTextSize() == 2)
                    holder.messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.large));

                   /* try {
                        date = originalFormat.parse(chat_time);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }*/
                //  chat_time = targetFormat.format(date);

                holder.timeTextView.setText(chat_time);
                String Attachment = chatMessage.getAttachment();


                IsDownloaded = chatMessage.getIsDownloaded();

                if (IsDownloaded.equals("Yes")) {
                    //holder.txt_image_delivered.setText(context.getResources().getString(R.string.tick));
                    /*holder.txt_message_delivered.setVisibility(View.VISIBLE);
                    holder.txt_message_delivered1.setVisibility(View.GONE);*/
                    holder.txt_image_delivered.setImageDrawable(context.getDrawable(R.drawable.deliver_msg));
                    holder.txt_message_delivered.setImageDrawable(context.getDrawable(R.drawable.deliver_msg));

                    if (chatMessage.getMessageType().equals("map:")) {
                   /*     holder.txt_image_delivered.setVisibility(View.VISIBLE);
                        holder.txt_image_delivered1.setVisibility(View.GONE);*/
                        holder.txt_image_delivered.setImageDrawable(context.getDrawable(R.drawable.deliver_msg));
                    }

                    if (Attachment == null || Attachment.equals("")) {

                    } else {


                        holder.txt_image_delivered.setImageDrawable(context.getDrawable(R.drawable.deliver_msg));
                    }

                } else if (IsDownloaded.equals("wait")) {

                    holder.txt_message_delivered.setVisibility(View.VISIBLE);
                    holder.txt_message_delivered.setImageDrawable(context.getDrawable(R.drawable.sent_msg));
                    if (chatMessage.getMessageType().equals("map:")) {

                        holder.txt_image_delivered.setImageDrawable(context.getDrawable(R.drawable.sent_msg));
                    }

                    if (Attachment == null || Attachment.equals("")) {

                    } else {

                        holder.txt_image_delivered.setImageDrawable(context.getDrawable(R.drawable.sent_msg));
                    }
                } else {

                    holder.txt_message_delivered.setVisibility(View.GONE);
                    if (Attachment == null || Attachment.equals("") || Attachment.contains("mp4")) {

                    } else {

                        holder.rel_image.setVisibility(View.VISIBLE);
                       /* holder.txt_image_delivered.setVisibility(View.GONE);
                        holder.txt_image_delivered1.setVisibility(View.VISIBLE);*/
                        holder.txt_image_delivered.setImageDrawable(context.getDrawable(R.drawable.sent_msg));
                    }
                }


                holder.textview_username.setVisibility(View.GONE);


                if (Attachment == null || Attachment.equals("") || Attachment.contains("mp4")) {
                    if (!(chatMessage.getMessageType().equals("map:") || chatMessage.getMessageType().equals("video:")))
                        holder.rel_image.setVisibility(View.GONE);
                    else {
                        holder.rel_image.setVisibility(View.VISIBLE);
                    }


                } else {
                    holder.rel_image.setVisibility(View.VISIBLE);
                    imgFile = new File(Attachment);
                    String filename = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
                    holder.textview_time1.setText(chat_time);
                    holder.contactLayout.setVisibility(View.GONE);


                    if (Attachment.contains(".pdf")) {
                        holder.img_chat.setImageResource(R.drawable.pd);
                    } else if (Attachment.contains(".doc")) {
                        holder.img_chat.setImageResource(R.drawable.word);

                    } else if (Attachment.contains(".docx")) {
                        holder.img_chat.setImageResource(R.drawable.word);
                    } else if (Attachment.contains(".ppt")) {
                        holder.img_chat.setImageResource(R.drawable.powerpoint);

                    } else if (Attachment.contains(".xls")) {
                        holder.img_chat.setImageResource(R.drawable.excel);

                    } else if (Attachment.contains(".xlsx")) {

                        holder.img_chat.setImageResource(R.drawable.excel);

                    } else if (Attachment.contains(".pptx")) {
                        holder.img_chat.setImageResource(R.drawable.powerpoint);


                    } else if (Attachment.contains("mp3")) {
                        holder.len_audio_play.setVisibility(View.VISIBLE);
                        holder.rel_image.setVisibility(View.GONE);
                        imgFile = new File(Attachment);
                        String filename1 = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
                        // holder.textview_mp3_time.setText(filename1 + "  " + chat_time);
                        holder.textview_mp3_time.setText(chat_time);


                    } else if (Attachment.contains("mp4")) {
                    /*holder.video_player_view.reset();
                    holder.rel_video_view.setVisibility(View.VISIBLE);
                    holder.rel_image.setVisibility(View.GONE);
                    imgFile = new File(Attachment);
                    String filename1 = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1);
                    videoUri = Uri.parse(Attachment);
                    try {
                        holder.video_player_view.setVideoURI(videoUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // holder.textview_video_time.setText(filename1 + "  " + chat_time);
                    holder.textview_video_time.setText(chat_time);*/

                    } else {

                        Glide.with(context)
                                .load(Attachment)
                                .into(holder.img_chat);
                        holder.contactLayout.setVisibility(View.GONE);
                        holder.incoming_layout_bubble.setVisibility(View.GONE);
                        if (!chatMessage.getMessage().equals("") && !chatMessage.getMessage().equals("File") && !chatMessage.getMessage().equals("#File")) {
                            holder.imageMessage.setVisibility(View.VISIBLE);
                            holder.imageMessage.setText(chatMessage.getMessage().replace("#File" , " "));
                        } else
                            holder.imageMessage.setVisibility(View.GONE);


                    /*if (imgFile.exists()) {
                       bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 100, 100, true);//  Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                         bitmap = ImageUtils.getInstant().getCompressedBitmap(imgFile.getAbsolutePath());
                        holder.img_chat.setImageBitmap(bitmap);
                   }*/
                    }

                }
                if (chatMessage.getAttachment() != null && !chatMessage.getAttachment().equals("")) {
                    holder.incoming_layout_bubble.setVisibility(View.GONE);
                    if (!chatMessage.getMessage().equals("") && !chatMessage.getMessage().equals("File") && !chatMessage.getMessage().equals("#File")) {
                        holder.imageMessage.setVisibility(View.VISIBLE);
                        holder.imageMessage.setText(chatMessage.getMessage().replace("#File" , " "));
                    } else
                        holder.imageMessage.setVisibility(View.GONE);
                } else
                    holder.incoming_layout_bubble.setVisibility(View.VISIBLE);


            }
        } else {
            holder.date.setText(((DateObject) chatMessages.get(position)).getDate());
            if (((DateObject) chatMessages.get(position)).getDate().contains("Unread message")) {
                isUnread = position;
            } else {
                isUnread = -1;
            }
            if (position == 0) {
                try {
                    ((AddChatRoomActivity) context).GetGrupMessageList(((DateObject) chatMessages.get(position)).getDate(),
                            chatMessage.getMessageId());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }
    }

    private boolean isTodayDay(long timestamp) {
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = new Date(timestamp);
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        if (targetFormat.format(currentDate).equals(targetFormat.format(date))) {
            return true;
        } else
            return false;
    }

    private File setReplyImage(String ReceiverImage) {
        //String ReceiverImage = ((ChatModelObject) consolidatedList.get(position)).getChatMessage().getAttachment();
        String path1 = Environment.getExternalStorageDirectory()
                .toString();
        File recFile = new File(path1 + "/" + "Vwb" + "/" + "Receive");
        if (!recFile.exists())
            recFile.mkdirs();
        File sendFile = new File(path1 + "/" + "Vwb" + "/" + "Sender");
        if (!sendFile.exists())
            sendFile.mkdirs();
        imgFileReceiver = new File(ReceiverImage);
        if (new File(recFile + "/" + ReceiverImage.substring(ReceiverImage.lastIndexOf("/")).replace("/", "")).exists()) {
            IsDownloaded = "Yes";
            if (IsDownloaded.equals("Yes")) {
                filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                File checkFile = new File(recFile + "/" + filename);

                if (checkFile.exists()) {
                    return checkFile;

                }
                // String replyDecode = StringEscapeUtils.unescapeJava(message);

            }
        } else if (new File(sendFile + "/" + ReceiverImage.substring(ReceiverImage.lastIndexOf("/")).replace("/", "")).exists()) {
            IsDownloaded = "Yes";
            if (IsDownloaded.equals("Yes")) {
                filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                File checkFile = new File(sendFile + "/" + filename);

                if (checkFile.exists()) {
                    return checkFile;
                }
                // String replyDecode = StringEscapeUtils.unescapeJava(message);
            }
        } else
            IsDownloaded = "No";

        return null;
    }


    private int getUserName(String s) {
        /*for( ListObject chatMessageObject : chatMessages){
            chatMessageObject.getType();
            if(chatMessageObject.getType()).getChatMessage())
            if(((ChatModelObject) chatMessages.get(position)).getChatMessage())
        }*/
        for (int i = 0; i < chatMessages.size(); i++) {
            if (getItemViewType(i) != 0) {
                ChatMessage chatMessageobj = ((ChatModelObject) chatMessages.get(i)).getChatMessage();
                if (s.equals(chatMessageobj.getMessageId())) {
                    // String replyStr = chatMessageobj.getMessage()+"@@"+chatMessageobj.getUsername();
                    return i;
                }
            }
        }
        return -1;
    }

    private DraweeController getImageDrable(DraweeView imageView, String imageUrl, int size) {
        Uri uri = Uri.parse(imageUrl);

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(size, size))
                .setProgressiveRenderingEnabled(false)
                .setPostprocessor(new BlurPostprocessor(context, 50))
                .build();

        return Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.getController())
                .setImageRequest(request)
                .build();
    }

    private DraweeController getImageDrableWithoutBlure(DraweeView imageView, Uri imageUrl, int size) {
        // Uri uri = Uri.parse(imageUrl);

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUrl)
                .setResizeOptions(new ResizeOptions(size, size))
                .setProgressiveRenderingEnabled(false)
                .build();

        return Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.getController())
                .setImageRequest(request)
                .build();
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        /*String typeView = chatMessages.get(position).getType();
        if (typeView.equals("Sender")) {
            return 1;
        } else {
            return 0;
        }*/
        return chatMessages.get(position).getType();
    }

    public void updateList(ArrayList<ListObject> consolidatedList) {
        chatMessages = consolidatedList;
        notifyDataSetChanged();
    }

    public void setSelectedPostion(int rpl) {
        selectedPos = rpl;
        notifyItemChanged(selectedPos);


    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.textview_message)
        TextView messageTextView;
        @Nullable
        @BindView(R.id.textview_time)
        TextView timeTextView;
        @Nullable
        @BindView(R.id.textview_username)
        TextView textview_username;
        @Nullable
        @BindView(R.id.textview_time1)
        TextView textview_time1;
        @Nullable
        @BindView(R.id.outgoing_img_chat)
        SimpleDraweeView outgoing_img_chat;
        @Nullable
        @BindView(R.id.outgoing_rel_image)
        RelativeLayout outgoing_rel_image;
        @Nullable
        @BindView(R.id.outgoing_layout_bubble)
        LinearLayout outgoing_layout_bubble;
        @Nullable
        @BindView(R.id.txt_download)
        ImageView txt_download;
        @Nullable
        @BindView(R.id.img_progress1)
        ProgressBar img_progress1;
        @Nullable
        @BindView(R.id.textview_username1)
        TextView textview_username1;
        @Nullable
        @BindView(R.id.play1)
        ImageView play1;
        @Nullable
        @BindView(R.id.pause1)
        ImageView pause1;
        @Nullable
        @BindView(R.id.media_seekbar)
        SeekBar media_seekbar;
        @Nullable
        @BindView(R.id.run_time)
        TextView run_time;
        @Nullable
        @BindView(R.id.total_time)
        TextView total_time;
        @Nullable
        @BindView(R.id.len_audio_play)
        LinearLayout len_audio_play;
        @Nullable
        @BindView(R.id.textview_mp3_username)
        TextView textview_mp3_username;
        @Nullable
        @BindView(R.id.textview_mp3_time)
        TextView textview_mp3_time;
        @Nullable
        @BindView(R.id.txt_mp3_download)
        TextView txt_mp3_download;
        @Nullable
        @BindView(R.id.txt_video_download)
        TextView txt_video_download;
        @Nullable
        @BindView(R.id.play_pause_layout)
        LinearLayout play_pause_layout;
        @Nullable
        @BindView(R.id.textview_video_username)
        TextView textview_video_username;
        /* @BindView(R.id.video_player_view)
         FullscreenVideoLayout video_player_view;*/
        @Nullable
        @BindView(R.id.rel_video_view)
        RelativeLayout rel_video_view;
        @Nullable
        @BindView(R.id.textview_video_time)
        TextView textview_video_time;
        @Nullable
        @BindView(R.id.txt_image_delivered)
        ImageView txt_image_delivered;

/*
        @Nullable
        @BindView(R.id.txt_image_delivered1)
        ImageView txt_image_delivered1;
*/

        @Nullable
        @BindView(R.id.txt_message_delivered)
        ImageView txt_message_delivered;
        @Nullable
        @BindView(R.id.txt_message_delivered2)
        ImageView txt_message_delivered2;


        @Nullable
        @BindView(R.id.contactLayout)
        RelativeLayout contactLayout;

        @Nullable
        @BindView(R.id.callBtn)
        Button callBtn;

        @Nullable
        @BindView(R.id.addContactBtn)
        Button addContactBtn;

        @Nullable
        @BindView(R.id.contectUserName)
        TextView contectUserName;


        @Nullable
        @BindView(R.id.date)
        TextView date;

        @Nullable
        @BindView(R.id.replyLayout)
        RelativeLayout replyLayout;

        @Nullable
        @BindView(R.id.replyImage)
        SimpleDraweeView replyImage;

        @Nullable
        @BindView(R.id.replyImage1)
        SimpleDraweeView replyImage1;



        /*sander*/
        /*@BindView((R.id.textview_message))
        TextView messageTextView ;

        @BindView((R.id.textview_time))
        TextView textview_time ;

        @BindView((R.id.textview_username))
        TextView textview_username ;

        @BindView((R.id.textview_time))
        TextView textview_time ;

        @BindView((R.id.textview_time1))
        TextView textview_time1 ;*/

        @Nullable
        @BindView(R.id.img_chat)
        ImageView img_chat;
        @Nullable
        @BindView(R.id.rel_image)
        RelativeLayout rel_image;
        @Nullable
        @BindView(R.id.incoming_layout_bubble)
        LinearLayout incoming_layout_bubble;
        @Nullable
        @BindView(R.id.play)
        ImageView play;
        @Nullable
        @BindView(R.id.pause)
        ImageView pause;

        @Nullable
        @BindView(R.id.playIcon)
        ImageView playIcon;

      /*  @BindView((R.id.media_seekbar))
        SeekBar media_seekbar ;
        @BindView((R.id.run_time))
        TextView run_time ;
        @BindView((R.id.total_time))
        TextView total_time ;*/

        @Nullable
        @BindView((R.id.video_player_view))
        FullscreenVideoLayout video_player_view;


       /* @BindView((R.id.txt_message_delivered))
        TextView txt_message_delivered ;
         @BindView((R.id.txt_image_delivered))
        TextView txt_image_delivered ;*/


        @Nullable
        @BindView(R.id.replyText)
        TextView replyText;

        @Nullable
        @BindView(R.id.userNameText)
        TextView userNameText;

        @Nullable
        @BindView(R.id.closeImg)
        ImageView closeImg;

        @Nullable
        @BindView(R.id.textview_time_contact)
        TextView textview_time_contact;

        @Nullable
        @BindView(R.id.rootLayout)
        LinearLayout rootLayout;

        @Nullable
        @BindView(R.id.imageMessage)
        TextView imageMessage;


        public ChatHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        @Optional
        @OnClick(R.id.txt_download)
        void setTxt_download() {

            int position = getAdapterPosition();
            pos = getAdapterPosition();
            ChatRoomId = ((ChatModelObject) chatMessages.get(pos)).getChatMessage().getChatRoomId();
            final String Picture = ((ChatModelObject) chatMessages.get(pos)).getChatMessage().getAttachment();
            Message_id = ((ChatModelObject) chatMessages.get(pos)).getChatMessage().getMessageId();
            localMessage_id = Message_id;
            if (isnet())
                if (!((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getMessageType().equals("video:"))
                    ((AddChatRoomActivity) context).setDownload(position, "No", ChatRoomId, context);
                else
                    ((AddChatRoomActivity) context).setVideoDownload(position, "No", ChatRoomId, context);

        }


        @Optional
        @OnClick(R.id.outgoing_img_chat)
        void setOutgoing_imgChat() {
            String Attachment = ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getAttachment();
            if (!(((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getMessageType().equals("map:") || ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getMessageType().equals("video:"))) {
                if (((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getIsDownloaded().equals("Yes")) {
                    Attachment = ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getAttachment();
                    if (Attachment.contains(".pdf") || Attachment.contains(".doc") || Attachment.contains(".docx")
                            || Attachment.contains(".ppt") || Attachment.contains(".xls") || Attachment.contains(".xlsx") || Attachment.contains(".pptx")) {

                        openDocument(Attachment);


                    } else {
                        Intent intent = new Intent(context, ImageFullScreenActivity.class);
                        intent.putExtra("share_image_path", ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getAttachment());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);


                    }
                } else {
                    setTxt_download();
                }
            } else if (((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getMessageType().equals("map:")) {
                Uri uri = Uri.parse(((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getMessage());
                Set<String> args = uri.getQueryParameterNames();
                String[] latLong = uri.getQueryParameter("center").split(",");
                double lat = Double.parseDouble(latLong[0]);
                double lang = Double.parseDouble(latLong[1]);
                //String uriMap = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lang);
                String uriMap = "http://maps.google.com/maps?q=loc:" + lat + "," + lang + " (" + ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getUsername() + ")";
                //String uriMap = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lang);  // use for faocus to location by google map
                //String nameAdd = "?q=" + Uri.encode(String.valueOf(lat) + " ," + String.valueOf(lang) + ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getUsername());  // use for add marker on location
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMap + nameAdd));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMap));
                context.startActivity(intent);
            } else {
                String path1 = Environment.getExternalStorageDirectory()
                        .toString();
                File file = new File(path1 + "/" + "Vwb" + "/" + "Video" + "/" + "ReceiveVideos");
                if (!file.exists())
                    file.mkdirs();
                imgFileReceiver = new File(Attachment);
                filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                File checkFile = new File(file + "/" + filename);
                if (checkFile.exists())
                    ((AddChatRoomActivity) context).playVideo(Attachment);
                else
                    ((AddChatRoomActivity) context).setVideoDownload(getAdapterPosition(), "No", ChatRoomId, context);
            }


        }

        private void openDocument(String attachment) {

            /*String path1 = Environment.getExternalStorageDirectory()
                        .toString();
                File file = new File(path1 + "/" + "Vwb" + "/" + "Video" + "/" + "ReceiveVideos");
                if (!file.exists())
                    file.mkdirs();
                imgFileReceiver = new File(Attachment);
                filename = imgFileReceiver.getAbsolutePath().substring(imgFileReceiver.getAbsolutePath().lastIndexOf("/") + 1);
                File checkFile = new File(file + "/" + filename);
                if (checkFile.exists())
                    ((AddChatRoomActivity) context).playVideo(Attachment);
                else
                    ((AddChatRoomActivity) context).setVideoDownload(getAdapterPosition(), "No", ChatRoomId, context);*/
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File file = new File(attachment);
            String fileName  = file.getAbsoluteFile().getName();

            String path1 = Environment.getExternalStorageDirectory()
                    .toString();
            File file1 = new File(path1 + "/" + "Vwb" + "/" + "Receive" );
            if (!file1.exists())
                file1.mkdirs();
            File checkFile = new File(file1 + "/" + fileName);

            String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(checkFile).toString());
            String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (extension.equalsIgnoreCase("") || mimetype == null) {
                // if there is no extension or there is no definite mimetype, still try to open the file
                intent.setDataAndType(Uri.fromFile(checkFile), "text/*");
            } else {
                intent.setDataAndType(Uri.fromFile(checkFile), mimetype);
            }
            // custom message for the intent
            context.startActivity(Intent.createChooser(intent, "Choose an Application:"));
        }


        @Optional
        @OnClick(R.id.img_chat)
        void setImg_chat() {
            int position = getAdapterPosition();
            String Attachment = ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getAttachment();

            if (((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getMessageType().equals("map:")) {
                Log.e("error", "map");
                Uri uri = Uri.parse(((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getMessage());
                Set<String> args = uri.getQueryParameterNames();
                String[] latLong = uri.getQueryParameter("center").split(",");
                double lat = Double.parseDouble(latLong[0]);
                double lang = Double.parseDouble(latLong[1]);
                // String uriMap = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lang);  // use for faocus to location by google map
                String nameAdd = "?q=" + Uri.encode(String.valueOf(lat) + " ," + String.valueOf(lang) + ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getUsername());  // use for add marker on location
                String uriMap = "http://maps.google.com/maps?q=loc:" + lat + "," + lang + " (" + ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getUsername() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMap + nameAdd));
                context.startActivity(intent);
            } else if (((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getMessageType().equals("video:")) {
                ((AddChatRoomActivity) context).playVideo(Attachment);

            } else {
                File file = new File(Attachment);


                if (Attachment.contains(".pdf") || Attachment.contains(".doc") || Attachment.contains(".docx")
                        || Attachment.contains(".ppt") || Attachment.contains(".xls") || Attachment.contains(".xlsx") || Attachment.contains(".pptx")) {

                    try {
                        if (Attachment.contains("http:") || Attachment.contains("https:")) {
                            Intent intent = new Intent(context, ImageFullScreenActivity.class);
                            intent.putExtra("share_image_path", ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getAttachment());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            openFile(context, file);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                                  /*  Intent target = new Intent(Intent.ACTION_VIEW);
                                    target.setDataAndType(Uri.fromFile(imgFile), "application/pdf");
                                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    Intent intent = Intent.createChooser(target, "Open File");

                                    try {
                                        context.startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context,
                                                "No Application Available to View PDF",
                                                Toast.LENGTH_SHORT).show();
                                    }*/
                } else {


                    try {
                        if (Attachment.contains("http:") || Attachment.contains("https:")) {
                            Intent intent = new Intent(context, ImageFullScreenActivity.class);
                            intent.putExtra("share_image_path", ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getAttachment());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File(((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getAttachment())), "image/*");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }
            }
        }


        @Optional
        @OnLongClick({R.id.img_chat, R.id.outgoing_img_chat})
        boolean imageLongPress() {
            ChatRoomId = chatMessage.getChatRoomId();
            if (((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getIsDownloaded().equals("Yes"))
                ((AddChatRoomActivity) context).appBarLayout(getAdapterPosition(), ChatRoomId, context, true);
            else
                setOutgoing_imgChat();
            return true;
        }

        @Optional
        @OnClick(R.id.play)
        void setPlay1() {
            int position = getAdapterPosition();
            mp = new MediaPlayer();

            Uri uri = Uri.parse(((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getAttachment());
            try {
                mp.setDataSource(uri.toString());
                mp.prepare();
                mp.start();
                Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Optional
        @OnClick(R.id.pause)
        void setPauseMusic() {
            int position = getAdapterPosition();
            if (mp.isPlaying()) {
                mp.pause();
            } else {
                mp.start();
            }
            Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show();
        }


        @Optional
        @OnLongClick({R.id.outgoing_layout_bubble, R.id.incoming_layout_bubble})
        boolean setClickView(View v) {
            final int position = getAdapterPosition();
            ChatRoomId = chatMessage.getChatRoomId();
            ((AddChatRoomActivity) context).appBarLayout(position, ChatRoomId, context, false);

            return true;
        }

        @Optional
        @OnClick({R.id.outgoing_layout_bubble, R.id.incoming_layout_bubble})
        void clickForreply() {
            ((AddChatRoomActivity) context).checkIsReply(getAdapterPosition());
        }

        @Optional
        @OnClick(R.id.callBtn)
        void setCallBtn() {
            int position = getAdapterPosition();
            ((AddChatRoomActivity) context).callNewUser(position, ChatRoomId, context, false);
        }

        @Optional
        @OnClick(R.id.addContactBtn)
        void setAddContactBtn() {
            int position = getAdapterPosition();
            ((AddChatRoomActivity) context).callAddNewContact(position, ChatRoomId, context, false);
        }


        private void openFile(Context context, File url) throws Exception {
            File file = url;
            Uri uri = Uri.fromFile(file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Check what kind of file you are trying to open, by comparing the url with extensions.
            // When the if condition is matched, plugin sets the correct intent (mime) type,
            // so Android knew what application to use to open the file
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } /*else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video*//*");
        }*/ else {
                //if you want you can also define the intent type for any other file

                //additionally use else clause below, to manage other unknown extensions
                //in this case, Android will show all applications installed on the device
                //so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
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


}
