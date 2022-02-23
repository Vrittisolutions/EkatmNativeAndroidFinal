package com.vritti.vwb.Adapter;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.chat.activity.AddChatRoomActivity;
import com.vritti.chat.activity.OpenChatroomActivity;
import com.vritti.chat.activity.PrivateChatActvity;
import com.vritti.chat.bean.ChatGroup;
import com.vritti.chat.bean.ChatUser;
import com.vritti.chat.bean.DefaultUser;
import com.vritti.chat.bean.PrivateUser;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.BirthdayBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vritti.chat.adapter.ChatRoomMultipleAdapterNewRecycleView.formateDateFromstring;


/**
 * Created by 300151 on 10/13/16.
 */
public class BirthdayMainAdapter extends RecyclerView.Adapter<BirthdayMainAdapter.BirthDayHoldaer> {
    private static ArrayList<BirthdayBean> lsBirthdayList;
    private final Utility ut;
    private LayoutInflater mInflater;
    Context context;
    String type = "";
    String  username = "", ChatRoomId, UserName1, UserMasterId1, ChatMessage, ChatRoomName, Call_Callid="";

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    DatabaseHandlers db;
    CommonFunction cf;
    SQLiteDatabase sql;
    private ArrayList<DefaultUser> defaultUserArrayList;
    String[] user;
    String ChatType = "P", Starttime;
    private ProgressDialog progressDialog;
    ArrayList<ChatUser> chatUserArrayList;

    public BirthdayMainAdapter(Context context1, ArrayList<BirthdayBean> lsBirthdayList1) {
        lsBirthdayList = lsBirthdayList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        defaultUserArrayList = new ArrayList<>();
        chatUserArrayList = new ArrayList<>();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        Starttime = dateFormat.format(new Date()).toString();
        System.out.println(Starttime);
        progressDialog=new ProgressDialog(context);

    }

    /*   @Override
       public int getCount() {
           return lsBirthdayList.size();
       }

       @Override
       public Object getItem(int position) {
           return lsBirthdayList.get(position);
       }
   */
    @NonNull
    @Override
    public BirthDayHoldaer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vwb_custom_birthday_list_v1, parent, false);

        return new BirthDayHoldaer(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BirthDayHoldaer holder, int position) {
        holder.tv_userName.setText(lsBirthdayList.get(position).getUserName());
        holder.tv_bod.setText(lsBirthdayList.get(position).getDtDay());

        String imagepath = lsBirthdayList.get(position).getImagePath();
        if (imagepath == null) {
            holder.profileImag.setBackground(context.getResources().getDrawable(R.drawable.travel_name));
        } else {
            holder.profileImag.setBackground(null);
            holder.profileImag.setImageURI(imagepath);
        }






       /* if (position % 2 == 0) {
            holder.len.setBackgroundColor(context.getResources().getColor(R.color.card_one_color));
        } else {
            holder.len.setBackgroundColor(context.getResources().getColor(R.color.card_two_color));
        }*/



       /* if(type.equals("")){
            holder.tv_bod.setVisibility(View.VISIBLE);
            type = lsBirthdayList.get(position).getType();
            holder.tv_bod.setText(type);
        }else {
            if(type.equals(lsBirthdayList.get(position).getType())){
                holder.tv_bod.setVisibility(View.GONE);
            }else {
                holder.tv_bod.setVisibility(View.VISIBLE);
                type = lsBirthdayList.get(position).getType();
                holder.tv_bod.setText(type);
            }
        }*/
        //type = lsBirthdayList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return lsBirthdayList.size();
    }



  /*  @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_birthday_list, null);
            holder = new ViewHolder();
            holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
            holder.tv_bod = (TextView) convertView.findViewById(R.id.tv_bod);
            holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
            holder.img_message = (ImageView) convertView.findViewById(R.id.img_message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_userName.setText(lsBirthdayList.get(position).getUserName());
        holder.tv_bod.setText(lsBirthdayList.get(position).getDtDay());


        holder.img_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email=lsBirthdayList.get(position).getEmail();
                String  name=lsBirthdayList.get(position).getUserName();
                String  msg="                 Dear " + name + "\n" +
                        "Many Many Happy Returns of the Day.."+"\n"
                        +"                 Happy Birth Day" +"\n\n"+
                                         "Warm Wishes"+"\n"
                        +                "All at Vritti";
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ Email});
                email.putExtra(Intent.EXTRA_SUBJECT, "Birthday Wishes!!");
                email.putExtra(Intent.EXTRA_TEXT,msg );

                //need this to prompts email client only
                email.setType("message/rfc822");

                context.startActivity(Intent.createChooser(email, "Choose an email client :"));
            }
        });
        return convertView;
    }
*/
   /* static class ViewHolder {
        TextView tv_userName, tv_bod;
        ImageView img_user,img_message;
    }*/

    public class BirthDayHoldaer extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_userName)
        TextView tv_userName;
        @BindView(R.id.tv_bod)
        TextView tv_bod;
        @BindView(R.id.img_chat)
        ImageView img_chat;
        @BindView(R.id.img_message)
        ImageView img_message;
        @BindView(R.id.profile_image)
        SimpleDraweeView profileImag;

        /*  @BindView(R.id.type)
          TextView dayType;*/
       /* @BindView(R.id.img_user)
        ImageView img_user;
        @BindView(R.id.img_message)
        ImageView img_message;*/
       /* @BindView(R.id.len)
        RelativeLayout len;

*/
        public BirthDayHoldaer(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.img_message)
        void sendmessage() {
            String Email = lsBirthdayList.get(getAdapterPosition()).getEmail();
            String name = lsBirthdayList.get(getAdapterPosition()).getUserName();
            String msg ="Dear " + name + ","+"\n\n" +
                    "Wish you happy birthday." + "\n\n"
                    +"Warm regards," + "\n\n\n\n" +
                     UserName;
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{Email});
            email.putExtra(Intent.EXTRA_SUBJECT, "Birthday Wishes!!");
            email.putExtra(Intent.EXTRA_TEXT, msg);

            //need this to prompts email client only
            email.setType("message/rfc822");

            context.startActivity(Intent.createChooser(email, "Choose an email client :"));
        }

        @OnClick(R.id.img_chat)
        void Chatmessage() {
            String OtherUsername = lsBirthdayList.get(getAdapterPosition()).getUserName();
            String OtherUserId = lsBirthdayList.get(getAdapterPosition()).getUserMasterID();
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
                    jsonobj.put("ChatSourceId", "");
                    jsonobj.put("AppCode", SetAppName.AppNameFCM);
                    jsonobj.put("ChatType", ChatType);


                    final String FinalJson = jsonobj.toString();
                    Log.d("Private", FinalJson);
                    String query = "SELECT * FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST + " WHERE  ChatRoomName = '" + OtherUsername + "' LIMIT 1";
                    Cursor cur = sql.rawQuery(query, null);
                    String count = String.valueOf(cur.getCount());
                    if (cur.getCount() > 0) {
                        Toast.makeText(context, "Already chatroom available can't create same chatroom", Toast.LENGTH_SHORT).show();
                        cur.moveToFirst();
                        do {

                            Intent intent = new Intent(context, AddChatRoomActivity.class);
                            intent.putExtra("callid", Call_Callid);
                            intent.putExtra("call_type", "");
                            intent.putExtra("ChatRoomid", cur.getString(cur.getColumnIndex("ChatRoomId")));
                            intent.putExtra("Chatroomname", cur.getString(cur.getColumnIndex("ChatRoomName")));
                            intent.putExtra("value_chat", "1");
                            intent.putExtra("status", cur.getString(cur.getColumnIndex("ChatType")));
                            intent.putExtra("share_image_path", "");
                            intent.putExtra("share_imagename", "");
                            intent.putExtra("share_description", "");
                            context.startActivity(intent);
                        } while (cur.moveToNext());

                    } else {

                        if (isnet()) {
                            if (progressDialog == null) {
                                progressDialog.setMessage("please wait creating chatroom..");
                                progressDialog.setIndeterminate(false);
                                progressDialog.setCancelable(true);

                            }
                            progressDialog.show();
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {

                                    new PostCreateChatRoomJSON().execute(FinalJson);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        } else {
                            Toast.makeText(context, "No Internet Connetion", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                } catch(Exception e){
                    e.printStackTrace();
                }


        }


        private boolean isnet() {
            // TODO Auto-generated method stub
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

    }

    class PostCreateChatRoomJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("please wait creating chatroom..");
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
                res = ut.OpenPostConnection(url, params[0], context);
                System.out.println("BusinessAPI-2 :" + res);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

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
                JSONArray jResults = null;
                try {
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
                            String StartTime=Jsonchatmember1.getString("StartTime");
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
                            chatGroup.setChatroom(ChatRoomName);
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
                        Toast.makeText(context, "Private conversation  created successfully", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(context, AddChatRoomActivity.class);
                        intent.putExtra("ChatRoomid", ChatRoomId);
                        intent.putExtra("Chatroomname", ChatRoomName);
                        intent.putExtra("status", "");
                        intent.putExtra("projmasterId","" );
                        context.startActivity(intent);




                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
            }


        }

    }

}
