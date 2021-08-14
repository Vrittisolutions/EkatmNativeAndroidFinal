package com.vritti.AlfaLavaModule.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.activity.ActivityGRNPutAway;
import com.vritti.AlfaLavaModule.activity.PutAwayPacketDetails;
import com.vritti.AlfaLavaModule.adapter.AdapterPutawayPacketDetail;
import com.vritti.AlfaLavaModule.bean.AlfaLocation;
import com.vritti.AlfaLavaModule.bean.PutawaysPacketBean;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentPutAwaysPacketDetail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPutAwaysPacketDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPutAwaysPacketDetail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static RecyclerView recycler;
    private static AdapterPutawayPacketDetail adapter;
    private static Paint p = new Paint();
    private static ArrayList<PutawaysPacketBean> list;
    static View Creatview;
    private String S_GRN, S_HDR;
    private static Context pContext;//Dowmloaditem
    private Dowmloaditem dowmloaditem;
    MenuItem chk;
    ArrayList<String> barcodecontent;
    static boolean DoNOTShowPopup = false;
    MenuItem menuItem;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private JSONObject J_obj;
    String GrnId = "";
    EditText s_search;
    private EditText editText;
    private AlertDialog dialog;
    private List<AlfaLocation> locationList;
    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback;
    private ItemTouchHelper itemTouchHelper;
    RelativeLayout bgTransParent;
    TextView grnText;
    TextView poNumberText;
    TextView poDate;
    TextView currentDate;
    Button cancelBtn , cfmBtn;


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;

    public FragmentPutAwaysPacketDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPutAwaysPacketDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPutAwaysPacketDetail newInstance(String param1, String param2) {
        FragmentPutAwaysPacketDetail fragment = new FragmentPutAwaysPacketDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        Creatview = inflater.inflate(R.layout.alfa_fragment_put_aways_packet_detail, container, false);
        pContext = getContext();
        userpreferences = getContext().getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(getContext());
        String settingKey = ut.getSharedPreference_SettingKey(getContext());
        String dabasename = ut.getValue(getContext(), WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(getContext(), dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(getContext(), WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(getContext(), WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(getContext(), WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(getContext(), WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(getContext(), WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(getContext(), WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(getContext(), WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(getContext(), WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
        S_GRN = PutAwayPacketDetails.GRN_ID_Packet;
        S_HDR = PutAwayPacketDetails.GRN_Header_Packet;

        initView(Creatview);


        if (Check_Db()) {
            detailPacket();
        } else {


            if (isnet()) {
                ProgressHUD.show(getContext(), "Fetching packet ...", true, false);
                new StartSession(getContext(), new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        dowmloaditem = new Dowmloaditem();
                        dowmloaditem.execute(S_HDR, S_GRN);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        dowmloaditem = new Dowmloaditem();
                        dowmloaditem.execute(S_HDR, S_GRN);
                    }


                });

            } else {
                MySnackbar("No Internet Connection", Creatview);
            }

        }


        s_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER)
                        || keyCode == KeyEvent.KEYCODE_TAB) {
                    // handleInputScan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (s_search != null) {
                                s_search.requestFocus();
                            }
                        }
                    }, 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay
                    String data = s_search.getText().toString();
                    Log.i("data::" , data);
                       // String[] separated = data.split("Info:");
                       // String json= separated[1];


                        try {

                            JSONObject jsonObject=new JSONObject(data);

                            String packet=jsonObject.getString("PacketNo");

                            filter(packet);


                            /*etScanner.setText(jsonObject.getString("Location"));
                            etItemId.setText(jsonObject.getString("ItemCode"));
                            etItemDes.setText(jsonObject.getString("ItemDesc"));
                            etTime.setText(jsonObject.getString("WareHouse"));*/

           /*// String[] stringArray = data.split("**");
            String[] stringArray =data.replace("**" , "::").split("::");
            for(int i = 0 ; i <= stringArray.length-1 ;i ++){
                String value = "";
                if(i == 4){
                  value = stringArray[4].split(":",2)[1];
                }else
                 value  = stringArray[i].split(":")[1];
                if(i == 0)
                    etItemId.setText(value);
                else if(i == 1)
                    etItemDes.setText(value);
                else  if(i == 2)
                    etScanner.setText(value);
                else if(i == 3) //warehouse value
                    etWereHouse.setText(value);
                else if(i == 4)
                    etTime.setText(value);
            }*/
           /* LocationScannerObject locationScannerObject = new Gson().fromJson(data, LocationScannerObject.class);
            etScanner.setText(locationScannerObject.getLocationName());
            etItemId.setText(locationScannerObject.getItemId());
            etItemDes.setText(locationScannerObject.getItemDiscribstion());
            etTime.setText(locationScannerObject.getTime());*/
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                   // convertToGson(data);

                    return true;
                } else if (event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                    String data = s_search.getText().toString();
                    filter(data);
                    return true;
                }
                return false;
            }
        });

        cfmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bgTransParent.setVisibility(View.GONE);
                grnText.setText("");
                poDate.setText("");
                poNumberText.setText("");
                currentDate.setText("");
            }
        });



        return Creatview;
    }

    private void convertToGson(String data) {
        try {
            // String[] stringArray = data.split("**");
            String[] stringArray =data.replace("**" , "::").split("::");
            if(stringArray.length !=0)
                bgTransParent.setVisibility(View.VISIBLE);
            for(int i = 0 ; i <= stringArray.length-1 ;i ++){

                String value = "";
               /* if(i == 4){
                    value = stringArray[4].split(":",2)[1];
                }else*/
                    value  = stringArray[i].split(":")[1];
                if(i == 0)
                    grnText.setText("GRN NO: "+value);
                else if(i == 1)
                    poNumberText.setText("PO No : "+value);
                else  if(i == 2)
                    poDate.setText("PO Date: "+value);
                else if(i == 3) //warehouse value
                    currentDate.setText(value);
               /* else if(i == 4)
                    etTime.setText(value);*/
            }
           /* LocationScannerObject locationScannerObject = new Gson().fromJson(data, LocationScannerObject.class);
            etScanner.setText(locationScannerObject.getLocationName());
            etItemId.setText(locationScannerObject.getItemId());
            etItemDes.setText(locationScannerObject.getItemDiscribstion());
            etTime.setText(locationScannerObject.getTime());*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView(View creatview) {

        recycler = (RecyclerView) creatview.findViewById(R.id.my_recycler_view);
        s_search = creatview.findViewById(R.id.s_search);
        bgTransParent = creatview.findViewById(R.id.bgTransParent);
        cfmBtn = creatview.findViewById(R.id.cfmBtn);
        cancelBtn = creatview.findViewById(R.id.cancelBtn);
        grnText = creatview.findViewById(R.id.grnText);
        poNumberText = creatview.findViewById(R.id.poNumberText);
        poDate = creatview.findViewById(R.id.poDate);
        currentDate = creatview.findViewById(R.id.currentDate);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        list = new ArrayList<PutawaysPacketBean>();
        barcodecontent = new ArrayList<String>();
    }

    private void MySnackbar(String display, View view) {
        Snackbar.make(view.findViewById(R.id.putawayPacket), display, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText( LogInScreen.this, "Snackbar Action", Toast.LENGTH_LONG).show();
            }
        }).show();

    }

    public void detailPacket() {
        list.clear();
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT *   FROM " + db.TABLE_PUTAWAY_PACKET_DETAIL, null);
            //Cursor c = sql.rawQuery("SELECT * FROM " + pFd.TABLE_PUTAWAY_PACKET_DETAIL + " WHERE GRNHeaderId='" + S_HDR + "' AND DoneFlag !='" + WebUrlClass.DoneFlag_Complete + "'", null);//WHERE GRNId =" + GRN_ID + "
            // Cursor c = sql.rawQuery("SELECT * FROM " + pFd.TABLE_PUTAWAY_PACKET_DETAIL + " WHERE GRNHeaderId = ? AND DoneFlag != ?", new String[]{S_HDR ,WebUrlClass.DoneFlag_Complete });//WHERE GRNId =" + GRN_ID + "
            if (c.getCount() == 0) {

            } else {
                c.moveToFirst();
                do {
                    PutawaysPacketBean bean = new PutawaysPacketBean();
                    bean.setItemdesc(c.getString(c.getColumnIndex("ItemDesc")));
                    bean.setItemCode(c.getString(c.getColumnIndex("ItemCode")));
                    bean.setItemPlantId(c.getString(c.getColumnIndex("ItemPlantId")));
                    bean.setPacketMasterId(c.getString(c.getColumnIndex("PacketMasterId")));
                    bean.setGRNDetailId(c.getString(c.getColumnIndex("GRNDetailId")));
                    bean.setGRNHeaderId(c.getString(c.getColumnIndex("GRNHeaderId")));
                    bean.setBalQty(c.getString(c.getColumnIndex("BalQty")));
                    bean.setPacketNo(c.getString(c.getColumnIndex("PacketNo")));
                    bean.setLocationCode(c.getString(c.getColumnIndex("LocationCode")));
                    bean.setLocationMasterId(c.getString(c.getColumnIndex("LocationMasterId")));
                    bean.setLocationDesc(c.getString(c.getColumnIndex("LocationDesc")));
                    bean.setDoneFlag(c.getString(c.getColumnIndex("DoneFlag")));
                    GrnId = c.getString(c.getColumnIndex("GRNDetailId"));
                    if (!c.getString(c.getColumnIndex("DoneFlag")).equals(WebUrlClass.DoneFlag_Complete))
                        list.add(bean);
                } while (c.moveToNext());

            }

            adapter = new AdapterPutawayPacketDetail(list);
            recycler.setAdapter(adapter);
            adapter.update(list);

            inithelper();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private Boolean Check_Db() {
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_PUTAWAY_PACKET_DETAIL + "' WHERE GRNHeaderId='" + S_HDR + "'", null);
            if (c.getCount() > 0) {

                return true;
            } else {

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    private void inithelper() {

        simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {// ItemTouchHelper.LEFT |

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.RIGHT) {
                    // adapter.removeItem(position);
                    String Packetno = adapter.getPacketNo(position);
                    ContentValues values = new ContentValues();
                    String aa = WebUrlClass.DoneFlag_Complete;
                    values.put("DoneFlag", aa);
                    sql.update(db.TABLE_PUTAWAY_PACKET_DETAIL, values, "PacketNo=?", new String[]{String.valueOf(Packetno)});
                    onResume();
                    Log.e("Packetno  :   ", Packetno);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    float x = dX;
                    float y = dY;
                    if (dX > 0) { //swipe right
                        p.setColor((Color.parseColor("#A2D6FD")));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_done_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else { //swipe left
                        p.setColor(Color.parseColor("#faeae9"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_done_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler);
    }

    @Override
    public void onResume() {
        super.onResume();
        S_GRN = PutAwayPacketDetails.GRN_ID_Packet;
        S_HDR = PutAwayPacketDetails.GRN_Header_Packet;
        detailPacket();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_putdetail_packet, menu);
        menuItem = menu.findItem(R.id.action_Popupcheck);
        menuItem.setChecked(isPopUpval());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_scan:
                barcodecontent.clear();
                try {
                    Intent intent = new Intent(WebUrlClass.ACTION_SCAN);
                    intent.putExtra("CODE_39", "PRODUCT_MODE");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException anfe) {
                    Toast.makeText(getActivity(), "No Scanner Found Download a scanner code activity?",Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.menu_send:
              /*  if (isPresentToLocal_Doneflg()) {*/


                    if (isnet()) {
                        ProgressHUD.show(getContext(), "Progress...", true, false);
                        new StartSession(getContext(), new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                JSONObject J_obj = getdata();
                                if (J_obj != null) {
                                    String s = J_obj.toString();
                                    s = s.replace("\\\\", "");
                                    new UpLoadData().execute(s);
                                } else {
                                    ProgressHUD.Destroy();
                                    MySnackbar("Data send", Creatview);
                                }
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                JSONObject J_obj = getdata();
                                if (J_obj != null) {
                                    String s = J_obj.toString();
                                    s = s.replace("\\\\", "");
                                    new UpLoadData().execute(s);
                                } else {
                                    ProgressHUD.Destroy();
                                    MySnackbar("Data send", Creatview);
                                }
                            }


                        });

                    } else {
                        MySnackbar("No Internet Connection", Creatview);
                    }

                /*} else {
                    MySnackbar("First Complete all Items", Creatview);
                }*/
                return true;
            case R.id.action_Popupcheck:
                if (menuItem.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("PrefisPopUp", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isPopUp", false);
                    editor.commit();
                    menuItem.setChecked(false);
                } else {
                    SharedPreferences preferences = getActivity().getSharedPreferences("PrefisPopUp", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isPopUp", true);
                    editor.commit();
                    menuItem.setChecked(true);
                }
                return true;
            default:
                break;
        }

        return false;
    }

    private Boolean isPopUpval() {
        SharedPreferences preferences = getActivity().getSharedPreferences("PrefisPopUp", Context.MODE_PRIVATE);
        Boolean check = preferences.getBoolean("isPopUp", false);
        return preferences.getBoolean("isPopUp", false);
    }

    private boolean isPresentToLocal(String itemCode) {
        Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_PUTAWAY_PACKET_DETAIL + "' WHERE PacketNo=?", new String[]{String.valueOf(itemCode)});
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPresentToLocal_Doneflg() {
        Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_PUTAWAY_PACKET_DETAIL + "' WHERE DoneFlag=?", new String[]{String.valueOf(WebUrlClass.DoneFlag_Default)});
        if (c.getCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean CheckStatus(String itemCode) {
        String Status;

        Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_PUTAWAY_PACKET_DETAIL + "' WHERE PacketNo=?", new String[]{String.valueOf(itemCode)});
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                Status = c.getString(c.getColumnIndex("DoneFlag"));
            } while (c.moveToNext());
            if (Status.equalsIgnoreCase(WebUrlClass.DoneFlag_Default)) {
                return true;
            } else if (Status.equalsIgnoreCase(WebUrlClass.DoneFlag_Complete)) {
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                if (contents.startsWith(WebUrlClass.PACKET_START_WITH)) {
                    if (isPresentToLocal(contents)) {
                        barcodecontent.add(contents);
                        try {
                            Log.e("Flag ", "" + DoNOTShowPopup);
                            Intent intentdata = new Intent(WebUrlClass.ACTION_SCAN);
                            intentdata.putExtra("CODE_39", "PRODUCT_MODE");
                            startActivityForResult(intent, 0);
                        } catch (ActivityNotFoundException anfe) {
                           Toast.makeText(getActivity(), "No Scanner Found Download a scanner code activity?",Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        showAlert(contents);
                    }
                } else {
                    barcodecontent.add(contents);
                    try {
                        Intent intentdata = new Intent(WebUrlClass.ACTION_SCAN);
                        intentdata.putExtra("CODE_39", "PRODUCT_MODE");
                        startActivityForResult(intent, 0);
                    } catch (ActivityNotFoundException anfe) {
                        Toast.makeText(getActivity(), "No Scanner Found Download a scanner code activity?",Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(getContext(), "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
                ArrayList<String> data = barcodecontent;
                String location = null;
                String contents = null;
                for (int i = 0; i < data.size(); i++) {
                    String a = data.get(i);
                    if (a.startsWith(WebUrlClass.PACKET_START_WITH)) {
                        contents = data.get(i);
                    } else if (a.startsWith(WebUrlClass.PACKET_LOCATION_START_WITH)) {
                        location = data.get(i);
                    } /*else {
                        location = null;
                        contents = null;
                    }*/
                }
                if (contents != null) {
                    if (isPresentToLocal(contents)) {
                        if (location != null) {
                            if (CheckStatus(contents)) {
                                Boolean aBoolean = isPopUpval();
                                if (isPopUpval()) {
                                    ContentValues values = new ContentValues();
                                    values.put("DoneFlag", WebUrlClass.DoneFlag_Complete);
                                    values.put("LocationMasterId", " ");
                                    values.put("LocationCode", location);
                                    values.put("LocationDesc", " ");
                                    String ss = contents;
                                    sql.update(db.TABLE_PUTAWAY_PACKET_DETAIL, values, "PacketNo=?", new String[]{String.valueOf(ss)});
                                    onResume();
                                } else {
                                    Complete_putlist(contents, location);
                                }

                            } else {
                                Undo_Complete_putList(contents);
                            }

                        } else {
                            if (CheckStatus(contents)) {
                                Boolean aBoolean = isPopUpval();
                                if (isPopUpval()) {
                                    ContentValues values = new ContentValues();
                                    values.put("DoneFlag", WebUrlClass.DoneFlag_Complete);
                                    String ss = contents;
                                    sql.update(db.TABLE_PUTAWAY_PACKET_DETAIL, values, "PacketNo=?", new String[]{String.valueOf(ss)});
                                    onResume();
                                } else {
                                    Complete_putlist(contents);
                                }
                            } else {
                                Undo_Complete_putList(contents);
                            }
                        }
                    } else {
                        showAlert(contents);
                    }
                } else {
                    Toast.makeText(getContext(), "You Haven't Scan Packet Number", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void Undo_Complete_putList(String data) {
        // TODO Auto-generated method stub
        final String itemcode = data;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(data + " Item is already completed \n Do you wish to remove from complete");
        builder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ContentValues values = new ContentValues();
                        values.put("DoneFlag", WebUrlClass.DoneFlag_Default);
                        String ss = itemcode;
                        sql.update(db.TABLE_PUTAWAY_PACKET_DETAIL, values, "PacketNo=?", new String[]{String.valueOf(ss)});
                        onResume();
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onResume();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showAlert(String data) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Item " + data + " is not present in list");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void Complete_putlist(String data) {
        // TODO Auto-generated method stub
        final String itemcode = data;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to complete " + data + " Item");
        builder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ContentValues values = new ContentValues();
                        values.put("DoneFlag", WebUrlClass.DoneFlag_Complete);
                        String ss = itemcode;
                        sql.update(db.TABLE_PUTAWAY_PACKET_DETAIL, values, "PacketNo=?", new String[]{String.valueOf(ss)});
                        onResume();
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onResume();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void Complete_putlist(String data, final String Location) {
        // TODO Auto-generated method stub
        final String itemcode = data;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to complete Packet" + data + " for Location " + Location);
        builder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ContentValues values = new ContentValues();
                        values.put("DoneFlag", WebUrlClass.DoneFlag_Complete);
                        values.put("LocationMasterId", " ");
                        values.put("LocationCode", Location);
                        values.put("LocationDesc", " ");
                        String ss = itemcode;
                        sql.update(db.TABLE_PUTAWAY_PACKET_DETAIL, values, "PacketNo=?", new String[]{String.valueOf(ss)});
                        onResume();
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onResume();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private JSONObject getdata() {
        //String GRNId = list.get(0).getGRNDetailId();
        String myTable = db.TABLE_PUTAWAY_PACKET_DETAIL;
        String y = "Y";
        String searchQuery = "SELECT  * FROM " + myTable + " where GRNDetailId='" + GrnId + "' AND DoneFlag='" + WebUrlClass.DoneFlag_Complete + "'";
        Cursor cursor = sql.rawQuery(searchQuery, null);
        JSONArray resultSet = new JSONArray();
        JSONObject rowObject1 = null;
        int count = cursor.getCount();
        if (count == 0) {
            rowObject1 = null;
        } else {
            cursor.moveToFirst();
            do {

                JSONObject rowObject = new JSONObject();
                try {
                    // rowObject.put("SuggPutAwayId", cursor.getString(cursor.getColumnIndex("SuggPutAwayId")));
                    rowObject.put("GRNHeaderId", S_HDR);
                    rowObject.put("GRNDetailId", cursor.getString(cursor.getColumnIndex("GRNDetailId")));
                    rowObject.put("LocationMasterId", cursor.getString(cursor.getColumnIndex("LocationMasterId")));
                    rowObject.put("ItemCode", cursor.getString(cursor.getColumnIndex("ItemCode")));
                    rowObject.put("ItemDesc", cursor.getString(cursor.getColumnIndex("ItemDesc")));
                    rowObject.put("LocationCode", cursor.getString(cursor.getColumnIndex("LocationCode")));
                    //    rowObject.put("PutAwayQty", cursor.getString(cursor.getColumnIndex("PutAwayQty")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultSet.put(rowObject);

            } while (cursor.moveToNext());

            rowObject1 = new JSONObject();
            try {
                rowObject1.put("ItemDetails", resultSet);
                rowObject1.put("GRNHeaderId", S_HDR);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rowObject1;
    }

    private class UpLoadData extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String strRes = null;
            String url = CompanyURL + WebUrlClass.api_PostGRN;
            try {
                res = ut.OpenPostConnection(url, params[0],pContext);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgressHUD.Destroy();
            if (s.contains("true")) {
                Toast.makeText(pContext, "Data Send Successfully", Toast.LENGTH_LONG).show();
                String myTable = db.TABLE_PUTAWAY;
                String myTable_user = db.TABLE_PUTAWAY_USER;
                String y = "Y";
                String searchQuery = "SELECT  * FROM " + myTable + " where GRNNumber='" + s + "' AND DoneFlag='" + WebUrlClass.DoneFlag_Complete + "'";
                sql.delete(myTable, "GRNNumber=? And DoneFlag=?", new String[]{String.valueOf(S_GRN), String.valueOf(WebUrlClass.DoneFlag_Complete)});
                Cursor c = sql.rawQuery("SELECT * FROM " + myTable, null);
                int i = c.getCount();
                sql.delete(myTable_user, "GRNNo=?", new String[]{String.valueOf(S_GRN)});
                Cursor c1 = sql.rawQuery("SELECT * FROM " + myTable_user, null);
                int i2 = c.getCount();
            } else {
                Toast.makeText(pContext, "Data Not Send", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class Dowmloaditem extends AsyncTask<String, String, ArrayList<String>> {
        Object res;
        String response;
        String GRNNumber = null;
        String GRNHeader = null;

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String strRes = null;
            GRNNumber = params[1];
            GRNHeader = params[0];
            String url = CompanyURL + WebUrlClass.api_getItemPacket + "?GRNHeaderId=" + params[0];
            try {
                res = ut.OpenConnection(url,pContext);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                response = "Error";
                e.printStackTrace();
            }
            ArrayList<String> Data = new ArrayList<>();
            Data.add(params[0]);
            Data.add(params[1]);
            Data.add(response);
            return Data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);//GRNHeaderId
            String GRN_No = s.get(1);
            String GRN_Hdr = s.get(0);
            String res = s.get(2);
            if (!(res.equalsIgnoreCase("[]"))) {
                if (res.contains("PacketMasterId")) {
                    try {
                        JSONArray jResults = new JSONArray(res);
                        Log.d("test", "jResults :=" + jResults);

                        cf.DeleteAllRecord(db.TABLE_PUTAWAY_PACKET_DETAIL);
                        Cursor cur = sql.rawQuery("SELECT *   FROM " + db.TABLE_PUTAWAY_PACKET_DETAIL, null);
                        Log.e("Table values----", "" + cur.getCount());
                        ContentValues Container = new ContentValues();
                        String columnName, columnValue;
                        for (int index = 0; index < jResults.length(); index++) {
                            JSONObject jstring = jResults.getJSONObject(index);
                            for (int j = 0; j < cur.getColumnCount(); j++) {
                                columnName = cur.getColumnName(j);
                                if (columnName.equalsIgnoreCase("DoneFlag")) {
                                    columnValue = WebUrlClass.DoneFlag_Default;
                                } else {
                                    columnValue = jstring.getString(columnName);
                                }
                                Container.put(columnName, columnValue);
                                Log.e("Count ...",
                                        " count i: " + index + "  j:" + j);
                            }
                            long a = sql.insert(db.TABLE_PUTAWAY_PACKET_DETAIL, null, Container);
                        }


                    } catch (Exception e) {
                    }
                    detailPacket();
                } else {
                    Toast.makeText(pContext, "Invalid Packet GRN", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(pContext, "No Packets Present", Toast.LENGTH_SHORT).show();
            }
            ProgressHUD.Destroy();
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void filter(String data) {
        List<PutawaysPacketBean> dummyList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPacketNo().equals(data)) {
                dummyList.add(list.get(i));
                adapter.update1(dummyList);
            }

        }

        if (dummyList.size() == 0) {
            dummyList.clear();
            MySnackbar("No packet found !!", Creatview);
        } else {
            //  Toast.makeText(getActivity(),"Scan location",Toast.LENGTH_SHORT).show();

            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.alfa_custom, null);

            editText = alertLayout.findViewById(R.id.edt_search_loc);


            Button dialogButton = (Button) alertLayout.findViewById(R.id.btn_submit);
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Scan location");
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);


            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    inithelper();
                    dialog.dismiss();

                }
            });

            editText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER)
                            || keyCode == KeyEvent.KEYCODE_TAB) {
                        // handleInputScan();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (s_search != null) {
                                    s_search.requestFocus();
                                }
                            }
                        }, 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay
                        String data = editText.getText().toString();
                        String[] separated = data.split("Info:");
                        String json= separated[1];

                        try {

                            JSONObject jsonObject=new JSONObject(json);
                            String  Location =jsonObject.getString("Location");
                            filterLocation(Location);

                        return true;
                    } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                        else if (event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                        String data = editText.getText().toString();
                        filterLocation(data);
                        return true;
                    }
                    return false;
                }
            });


            dialog = alert.create();
            dialog.show();
        }
        s_search.setText("");
    }

    private void filterLocation(String data) {
        List<PutawaysPacketBean> dummyList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getLocationCode().equals(data)) {
                dummyList.add(list.get(i));
                //  adapter.updateLocation(dummyList);
            }

        }

        if (dummyList.size() == 0) {
            dummyList.clear();
            MySnackbar("Location not found !!", Creatview);
            dialog.dismiss();
        } else {

            editText.setText(data);
            //inithelper();

            simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {// ItemTouchHelper.LEFT |

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    if (direction == ItemTouchHelper.RIGHT) {
                        // adapter.removeItem(position);
                        String Packetno = adapter.getPacketNo(position);
                        ContentValues values = new ContentValues();
                        String aa = WebUrlClass.DoneFlag_Complete;
                        values.put("DoneFlag", aa);
                        sql.update(db.TABLE_PUTAWAY_PACKET_DETAIL, values, "PacketNo=?", new String[]{String.valueOf(Packetno)});
                        onResume();
                        Log.e("Packetno  :   ", Packetno);
                    }
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                    Bitmap icon;
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;
                        float x = dX;
                        float y = dY;
                        if (dX > 0) { //swipe right
                            p.setColor((Color.parseColor("#A2D6FD")));
                            RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_done_white);
                            RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        } else { //swipe left
                            p.setColor(Color.parseColor("#faeae9"));
                            RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_done_white);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        }
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };

            itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recycler);

            dialog.dismiss();

        }


    }
    public boolean isnet() {
        Context context = pContext.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


}
