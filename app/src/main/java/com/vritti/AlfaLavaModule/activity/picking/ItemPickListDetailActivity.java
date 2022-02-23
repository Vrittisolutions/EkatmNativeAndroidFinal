package com.vritti.AlfaLavaModule.activity.picking;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.adapter.AdapterItemPicklist;
import com.vritti.AlfaLavaModule.adapter.Adapter_PrinterName;
import com.vritti.AlfaLavaModule.bean.PickListDetail;
import com.vritti.AlfaLavaModule.bean.PrinterName;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class ItemPickListDetailActivity extends AppCompatActivity {


    private static RecyclerView recycler;
    private static AdapterItemPicklist adapter;
    private static Paint p = new Paint();
    private  ArrayList<PickListDetail> list;
    static View Creatview;
    private DownloadPicklistItem dowmloaditem;
    MenuItem chk;
    ArrayList<String> barcodecontent;
    static boolean DoNOTShowPopup = false;
    MenuItem menuItem;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private JSONObject J_obj;
    String GrnId = "";
    Spinner s_search;
    private EditText s_search1;
    private AlertDialog dialog;



    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private String ItemCode,PacketNo;
    private double ReqQty;

    private String Pick_ListHdrId="",PickListNo="",
            Pick_ListDtlId="",SoScheduleId="",ItemMasterId="",
            QtyToPick="",QtyPicked="",Pick_listSuggLotId="",
            Pick_listDtlId="",StockDetailsId="",QtyPickPosted="",ItemDesc="",FinalJson="",LocationCode="",QCStatus="";

    ImageView img_search;
    private String  barcode;
    TextView txt_do,txt_qty;
    private Spinner printername;
    ArrayList<PrinterName>printerNameArrayList;
    private Adapter_PrinterName adapterPrinterName;
    private String printerName;
    private AlertDialog b;

    //4368534

    int currentTotal=0;
    int currentQty=0;
    int Qty=0;
    private String data;
    private TextToSpeech t1;
    private ProgressBar progress;

    int QtyTopick,BalToPick,FiFoQtyBalanceTopick,LotQtyBalanceofFiFo,FiFoQtyToPick,LotQtyToPick;
    Button btn_finish;
    LinearLayout len_search;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picklist_dodropdown_lay);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        userpreferences = this.getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(ItemPickListDetailActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(ItemPickListDetailActivity.this);
        String dabasename = ut.getValue(ItemPickListDetailActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(ItemPickListDetailActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(ItemPickListDetailActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(ItemPickListDetailActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(ItemPickListDetailActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(ItemPickListDetailActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(ItemPickListDetailActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(ItemPickListDetailActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(ItemPickListDetailActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(ItemPickListDetailActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        progress=findViewById(R.id.progress);




        PickListNo=getIntent().getStringExtra("dono");
        Pick_ListHdrId=getIntent().getStringExtra("headerid");
        QCStatus=getIntent().getStringExtra("qcstatus");

        getSupportActionBar().setTitle(PickListNo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        /*if (Check_Db()) {
            detailPacket(PickListNo);
        } else {


            if (isnet()) {
                progress.setVisibility(View.VISIBLE);
                new StartSession(ItemPickListDetailActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        dowmloaditem = new DownloadPicklistItem();
                        dowmloaditem.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }


                });

            } else {
                Toast.makeText(ItemPickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
*/




    }

    private void initView() {

        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        s_search1 = findViewById(R.id.s_search1);
        txt_do = findViewById(R.id.txt_do);
        txt_qty = findViewById(R.id.txt_qty);
        img_search = findViewById(R.id.img_search);
        len_search = findViewById(R.id.len_search);

        txt_do.setText(" "+PickListNo);

        s_search1.setHint("Scan Packet No");




        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ItemPickListDetailActivity.this);
        recycler.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        barcodecontent = new ArrayList<String>();
        btn_finish=findViewById(R.id.btn_finish);

        btn_finish.setText("Picklist Post");

        btn_finish.setVisibility(View.VISIBLE);
        len_search.setVisibility(View.GONE);


        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(ItemPickListDetailActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PostPicklistData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                } else {
                    Toast.makeText(ItemPickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }



              /*  startActivity(new Intent(ItemPickListDetailActivity.this, DOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();*/
            }
        });




    }


    public void detailPacket(String DO) {
        list.clear();
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_ITEM_PICKLIST + "' WHERE PickListNo=?", new String[]{String.valueOf(PickListNo)});


            if (c.getCount()>0) {
                c.moveToFirst();
                do {
                    recycler.setVisibility(View.VISIBLE);

                    PickListDetail pickListDetail = new PickListDetail();
                    pickListDetail.setPick_ListDtlId(c.getString(c.getColumnIndex("Pick_ListHdrId")));
                    pickListDetail.setPickListNo(c.getString(c.getColumnIndex("PickListNo")));
                    pickListDetail.setItemMasterId(c.getString(c.getColumnIndex("ItemMasterId")));
                    pickListDetail.setQtyToPick(c.getString(c.getColumnIndex("QtyToPick")));
                    pickListDetail.setQtyPicked(c.getString(c.getColumnIndex("QtyPicked")));
                    pickListDetail.setItemCode(c.getString(c.getColumnIndex("ItemCode")));
                    pickListDetail.setItemDesc(c.getString(c.getColumnIndex("ItemDesc")));
                    pickListDetail.setFlag(c.getString(c.getColumnIndex("Flag")));
                     list.add(pickListDetail);
                } while (c.moveToNext());



                adapter = new AdapterItemPicklist(list, ItemPickListDetailActivity.this);
                recycler.setAdapter(adapter);
                adapter.update(list);

            }else {

                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(ItemPickListDetailActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            dowmloaditem = new DownloadPicklistItem();
                            dowmloaditem.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            dowmloaditem = new DownloadPicklistItem();
                            dowmloaditem.execute();
                            }


                    });

                } else {
                    Toast.makeText(ItemPickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }



            }



        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if (isnet()) {
            progress.setVisibility(View.VISIBLE);
            new StartSession(ItemPickListDetailActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    dowmloaditem = new DownloadPicklistItem();
                    dowmloaditem.execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        } else {
            Toast.makeText(ItemPickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
    public Boolean Check_Db() {
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT *   FROM " + db.TABLE_ITEM_PICKLIST, null);
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

    public void Qtytotal(double currentTotal) {
        String qty= String.valueOf(currentTotal);
        String TotalPick = String.valueOf(qty).split("\\.")[0];
        txt_qty.setVisibility(View.VISIBLE);
        txt_qty.setText("Total Pick :"+TotalPick);

    }

    public void ItemMRPGroup(String itemMasterId,String  Itemcode) {

        startActivity(new Intent(ItemPickListDetailActivity.this,ItemWisePickListDetailActivity.class).
                putExtra("headerid",Pick_ListHdrId).
                putExtra("itemmaster",itemMasterId).
                putExtra("Itemcode",Itemcode)
                .putExtra("qcstatus", QCStatus)
                .putExtra("dono",PickListNo)
        );
    }

    public class DownloadPicklistItem extends AsyncTask<String, String, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {

          //  PackOrderNo="INV/19-20/018";
            String url = CompanyURL + WebUrlClass.api_GetPickItemGrp + "?Pick_ListHdrId=" +Pick_ListHdrId;

            try {

                 res = ut.OpenConnection(url, ItemPickListDetailActivity.this);
                 response=res.toString();
                 response = res.toString().replaceAll("\\\\", "");
                // response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                response = "Error";
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);


            if (!(s.equalsIgnoreCase("[]"))) {
                if (s.contains("ItemMasterId")) {
                    try {
                        list.clear();
                       cf.DeleteAllRecord(db.TABLE_ITEM_PICKLIST);
                        PickListDetail pickListDetail=new PickListDetail();
                        JSONArray pick_ListDtl=new JSONArray(s);

                        if (pick_ListDtl.length()>0) {


                            for (int i=0;i<pick_ListDtl.length();i++){
                                JSONObject pick_ListDtljsonObject=pick_ListDtl.getJSONObject(i);
                                pickListDetail.setPick_ListHdrId(Pick_ListHdrId);
                                pickListDetail.setPickListNo(PickListNo);
                                ItemMasterId=pick_ListDtljsonObject.getString("ItemMasterId");
                                pickListDetail.setItemMasterId(ItemMasterId);
                                QtyToPick=pick_ListDtljsonObject.getString("QtyToPick");
                                pickListDetail.setQtyToPick(QtyToPick);
                                QtyPicked=pick_ListDtljsonObject.getString("QtyPicked");
                                pickListDetail.setQtyPicked(QtyPicked);
                                ItemCode=pick_ListDtljsonObject.getString("ItemCode");
                                pickListDetail.setItemCode(ItemCode);
                                ItemDesc=pick_ListDtljsonObject.getString("ItemDesc");
                                pickListDetail.setItemDesc(ItemDesc);
                                pickListDetail.setFlag(WebUrlClass.DoneFlag_Default);
                                pickListDetail.setDONumber(PickListNo);

                                cf.InsertItemPickingOrderDetails(pickListDetail);
                               // cf.InsertPickingOrderDetails(pickListDetail);
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    detailPacket(PickListNo);
                    progress.setVisibility(View.GONE);

                } else {
                    cf.DeleteAllRecord(db.TABLE_ITEM_PICKLIST);
                    detailPacket(PickListNo);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                        Toast toast = Toast.makeText(ItemPickListDetailActivity.this, "Record not found", Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastView.setBackgroundColor(Color.WHITE);
                        toast.show();
                        progress.setVisibility(View.GONE);

                        final MediaPlayer mp = MediaPlayer.create(ItemPickListDetailActivity.this, R.raw.alert);
                        mp.start();
                    }else {
                        Toast toast = Toast.makeText(ItemPickListDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Record not found" + "</big></b></font>"), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }


                }

            } else {
                cf.DeleteAllRecord(db.TABLE_ITEM_PICKLIST);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                    Toast toast = Toast.makeText(ItemPickListDetailActivity.this, "Record not found", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                    progress.setVisibility(View.GONE);
                    final MediaPlayer mp = MediaPlayer.create(ItemPickListDetailActivity.this, R.raw.alert);
                    mp.start();
                }else {
                    Toast toast = Toast.makeText(ItemPickListDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Record not found" + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and nameuom
        void onFragmentInteraction(Uri uri);
    }



    public boolean isnet () {
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_refresh:
                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(ItemPickListDetailActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            dowmloaditem = new DownloadPicklistItem();
                            dowmloaditem.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            dowmloaditem = new DownloadPicklistItem();
                            dowmloaditem.execute();
                        }


                    });

                } else {
                    Toast.makeText(ItemPickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private class UpLoadSplitData extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetPacketSplitUpdate+"?PacketNo="+PacketNo+"&RemovedQty="+ReqQty+"&PrinterName="+ URLEncoder.encode(printerName,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                res = ut.OpenConnection(url, ItemPickListDetailActivity.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);
            if (s.equalsIgnoreCase("ok")) {
                b.dismiss();
                ContentValues values = new ContentValues();
                String aa = WebUrlClass.DoneFlag_Complete;
                values.put("Flag", aa);
                sql.update(db.TABLE_CARTAN_PICKLIST, values, "ItemCode=?", new String[]{String.valueOf(ItemCode)});

                onResume();
                Toast.makeText(ItemPickListDetailActivity.this, "Print Successfully", Toast.LENGTH_LONG).show();
                b.dismiss();
            } else {
                Toast.makeText(ItemPickListDetailActivity.this, "Print Successfully", Toast.LENGTH_LONG).show();
                onResume();
                b.dismiss();
            }
        }
    }
    class DownloadPrinterData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetPrinterName;

            try {
                res = ut.OpenConnection(url, ItemPickListDetailActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    printerNameArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        PrinterName userList = new PrinterName();
                        JSONObject jorder = jResults.getJSONObject(i);
                        userList.setPrinterName(jorder.getString("PrinterName"));
                        printerNameArrayList.add(userList);


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


            progress.setVisibility(View.GONE);

            if (response.contains("[]")) {

                Toast.makeText(ItemPickListDetailActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ItemPickListDetailActivity.this);
                LayoutInflater inflater = ItemPickListDetailActivity.this.getLayoutInflater();
                final View myView = inflater.inflate(R.layout.printername_lay, null);
                dialogBuilder.setView(myView);
                printername = (Spinner) myView .findViewById(R.id.spinner_printer);

                adapterPrinterName = new Adapter_PrinterName(ItemPickListDetailActivity.this, printerNameArrayList);
                printername.setAdapter(adapterPrinterName);
                printername.setSelection(0,false);


                printername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        printerName=printerNameArrayList.get(position).getPrinterName();

                        if (isnet()) {
                            progress.setVisibility(View.VISIBLE);
                            new StartSession(ItemPickListDetailActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new UpLoadSplitData().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    new UpLoadSplitData().execute();

                                }


                            });

                        } else {
                            Toast.makeText(ItemPickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                b = dialogBuilder.create();
                b.show();

            }


        }
    }



    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.System.canWrite(ItemPickListDetailActivity.this))
                return true;
            else
                openAndroidPermissionsMenu();
        }
        return false;
    }

    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    private void getdeletedialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ItemPickListDetailActivity.this);
        LayoutInflater inflater = ItemPickListDetailActivity.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.split_lay_dialog, null);
        dialogBuilder.setView(myView);

        Button btn_cancel=myView.findViewById(R.id.btn_cancel);
        Button btn_yes=myView.findViewById(R.id.btn_yes);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(ItemPickListDetailActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadPrinterData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                } else {
                    Toast.makeText(ItemPickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                b.dismiss();




            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                b.dismiss();
            }
        });






        b = dialogBuilder.create();
        b.show();
    }


    private class PostPicklistData extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "";
            url = CompanyURL + WebUrlClass.PutAwayToPackingLoc + "?Pick_ListHdrId=" + Pick_ListHdrId;

            try {
                res = ut.OpenConnection(url, ItemPickListDetailActivity.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);
            if (s.contains("Success")) {

                Toast.makeText(ItemPickListDetailActivity.this, "Picklist post successfully", Toast.LENGTH_LONG).show();

                t1.speak("Picklist post Successfully", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }
                final MediaPlayer mp = MediaPlayer.create(ItemPickListDetailActivity.this, R.raw.ok);
                mp.start();

                startActivity(new Intent(ItemPickListDetailActivity.this, DOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

            }
            else if (s.contains("Failed")) {
                try {
                    s = s.substring(1, s.length() - 1);
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("ERROR");
                    Toast.makeText(ItemPickListDetailActivity.this, status, Toast.LENGTH_LONG).show();
                    final MediaPlayer mp = MediaPlayer.create(ItemPickListDetailActivity.this, R.raw.alert);
                    mp.start();

                    t1.speak(status, TextToSpeech.QUEUE_FLUSH, null);
                    if (t1 != null) {
                        t1.stop();
                        t1.shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (s.contains("False"))
            {
                s = s.substring(1, s.length() - 1);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("ERROR");
                    Toast.makeText(ItemPickListDetailActivity.this, status, Toast.LENGTH_LONG).show();
                    final MediaPlayer mp = MediaPlayer.create(ItemPickListDetailActivity.this, R.raw.alert);
                    mp.start();
                    t1.speak(status, TextToSpeech.QUEUE_FLUSH, null);
                    if (t1 != null) {
                        t1.stop();
                        t1.shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ItemPickListDetailActivity.this, s, Toast.LENGTH_LONG).show();
                final MediaPlayer mp = MediaPlayer.create(ItemPickListDetailActivity.this, R.raw.alert);
                mp.start();
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }


            }
        }
    }
}

