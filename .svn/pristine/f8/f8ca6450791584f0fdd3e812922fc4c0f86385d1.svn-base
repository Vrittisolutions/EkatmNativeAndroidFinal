package com.vritti.vwb.vworkbench;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.widget.Filter;

import com.google.gson.Gson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.ClaimDetailsBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.ClaimRecordObjectForDate;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;

/**
 * Created by 300151 on 11/18/2016.
 */
public class ClaimDetailActivity extends AppCompatActivity {

    public static Spinner sp_vehicle_type, sp_mode_of_journy;
    ArrayAdapter<String> ModeOfTravelAdapter, VehicleTypeAdapter;

    String[] ModeOfJourny;
    String[] VehicleType = {"2 Wheeler", "4 Wheeler"};
    public static Button btn_claim_date;
    public static LinearLayout lay_vehicleType, lay_distance;
    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat Calim_dff = new SimpleDateFormat("dd MMM yyyy");
    static int year, month, day;
    public static JSONObject claimDtlsobj;
    public static JSONObject claimDtlsobjTotalobj;
    public static JSONArray claimDtlArray = new JSONArray();
    public static JSONArray claimDtls = new JSONArray();
    //public static JSONArray claimDtlsobjTotal = new JSONArray();
    public static EditText ed_Maintenance, ed_distance, ed_travel, ed_local, ed_phone, ed_food, ed_stay;
    public static TextView tv_total,txt_distance;
    public static String ClaimDate = "";
    public static Button btn_save, btn_cancel;
    static int totTravel = 0, totLocal = 0, totLodging = 0, totPhone = 0, totFood = 0, total = 0, totRM = 0, totTotal = 0;
    public static List<ClaimDetailsBean> lsCalimDetails = new ArrayList<ClaimDetailsBean>();
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    Toolbar toolbar;
    boolean EditAction = false;
    public static int ClaimEditPosition;
    AutoCompleteTextView ed_fromPlace, ed_toPlace;
    LinearLayout len_distance;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk";
    private String Tolocation, Fromlocation;
    private double disatnce;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="", UserMasterId="",UserName = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    String localMode = null;
    boolean isEditClaim = false;
    ArrayList<String> claimList;
    ClaimRecordObjectForDate claimRecordObjectForDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_claim_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        claimRecordObjectForDate = new ClaimRecordObjectForDate(new ArrayList<String>());
        Context context = this.getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
       UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        sql = db.getWritableDatabase();
        Initview();
        SetListner();
        if (cf.check_ModeOFJourny() > 0) {
            UpdateModeOfJounry();
        } else {
            new StartSession(ClaimDetailActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadModeOfJourny().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);

                }
            });
        }
        if (getIntent().hasExtra("Action")) {
            if (getIntent().getStringExtra("Action").equalsIgnoreCase("Edit")) {
                ClaimDetailsBean bean = new Gson().fromJson(getIntent().getStringExtra("object") , ClaimDetailsBean.class);
                InitEditClaim(bean);
            }
        }



    }

    private void InitEditClaim(ClaimDetailsBean bean) {
        Intent intent = getIntent();
        ClaimEditPosition = intent.getIntExtra("Position", 0);
        /*ed_Maintenance.setText(intent.getStringExtra("Maintenanace"));
        ed_local.setText(intent.getStringExtra("Local"));
        ed_phone.setText(intent.getStringExtra("Ph"));
        ed_travel.setText(intent.getStringExtra("travelling"));
        ed_food.setText(intent.getStringExtra("food"));
        ed_stay.setText(intent.getStringExtra("lodging"));
        ed_fromPlace.setText(intent.getStringExtra("fromPlace"));
        ed_toPlace.setText(intent.getStringExtra("ToPlace"));
        btn_claim_date.setText(intent.getStringExtra("ClaimDate"));
        txt_distance.setText(intent.getStringExtra("distance"));
        len_distance.setVisibility(View.VISIBLE);
        int mode = ModeOfTravelAdapter.getPosition(intent.getStringExtra("mode"));
        sp_mode_of_journy.setSelection(mode);
        sp_mode_of_journy.setSelected(true);*/
        ed_Maintenance.setText(bean.getTv_Maintenanace());
        ed_local.setText(bean.getTv_Local());
        ed_phone.setText(bean.getTv_Ph());
        ed_travel.setText(bean.getTv_travelling());
        ed_food.setText(bean.getTv_food());
        ed_stay.setText(bean.getTv_lodging());
        ed_fromPlace.setText(bean.getFromPlace());
        ed_toPlace.setText(bean.getToPlace());
        btn_claim_date.setText(bean.getClaimDate());
        txt_distance.setText(bean.getDistance());
        len_distance.setVisibility(View.VISIBLE);
       // int mode = ModeOfTravelAdapter.getPosition(bean.getTv_mode());
        localMode = bean.getTv_mode();
        int index = -1;
        if(ModeOfJourny != null && ModeOfJourny.length != 0) {
            for (int i = 0; i < ModeOfJourny.length; i++) {
                if (ModeOfJourny[i].equals(bean.getTv_mode())) {
                    index = i;
                    break;
                }
            }
        }
      if(index != -1) {
          sp_mode_of_journy.setSelection(index);
          sp_mode_of_journy.setSelected(true);
      }


    }

    private void Initview() {
        ed_distance = (EditText) findViewById(R.id.ed_distance);
        ed_distance.setSelection(0);//ed_distance.getText().length()
        ed_Maintenance = (EditText) findViewById(R.id.ed_Maintenance);
        ed_Maintenance.setSelection(0);
        ed_food = (EditText) findViewById(R.id.ed_food);
        ed_food.setSelection(0);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        ed_local = (EditText) findViewById(R.id.ed_local);
        ed_local.setSelection(0);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_phone.setSelection(0);
        ed_stay = (EditText) findViewById(R.id.ed_stay);
        ed_stay.setSelection(0);
        ed_travel = (EditText) findViewById(R.id.ed_travel);
        ed_travel.setSelection(0);
        ed_fromPlace = (AutoCompleteTextView) findViewById(R.id.ed_fromPlace);
        ed_fromPlace.setSelection(0);
        ed_toPlace = (AutoCompleteTextView) findViewById(R.id.ed_toPlace);
        ed_toPlace.setSelection(0);
        sp_vehicle_type = (Spinner) findViewById(R.id.sp_vehicle_type);
        sp_mode_of_journy = (Spinner) findViewById(R.id.sp_mode_of_journy);
        btn_claim_date = (Button) findViewById(R.id.btn_claim_date);
        btn_claim_date.setText(Calim_dff.format(new Date()));
        lay_distance = (LinearLayout) findViewById(R.id.lay_distance);
        lay_vehicleType = (LinearLayout) findViewById(R.id.lay_vehicleType);
        btn_save = (Button) findViewById(R.id.btn_save);
        VehicleTypeAdapter = new ArrayAdapter<String>(ClaimDetailActivity.this, android.R.layout.simple_spinner_item, VehicleType);
        VehicleTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_vehicle_type.setAdapter(VehicleTypeAdapter);
        tv_total = (TextView) findViewById(R.id.tv_total);
        txt_distance=(TextView) findViewById(R.id.txt_distance);
        len_distance= (LinearLayout) findViewById(R.id.len_distance);

        claimList = new ArrayList<>();


        ed_fromPlace.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.vwb_list_item));
        ed_toPlace.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.vwb_list_item));

        UpdateModeOfJounry();


        ed_fromPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fromlocation = (String) parent.getItemAtPosition(position);
            }
        });

        ed_toPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tolocation = (String) parent.getItemAtPosition(position);

                CalculateDistance(Fromlocation, Tolocation);

                //new CalculateDistane().execute(Fromlocation,Tolocation);
            }
        });

        sp_vehicle_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String Vehicletype=parent.getSelectedItem().toString();

                if (Vehicletype.equals("2 Wheeler")){
                    double vehicle=2.5;
                    String distance_vehicle=txt_distance.getText().toString();
                    if (distance_vehicle.contains(",")){
                        distance_vehicle = distance_vehicle.replace(",", "");

                        double calvehicle= Double.parseDouble(distance_vehicle);
                        String vehicledistance= String.valueOf(vehicle*calvehicle);
                        ed_travel.setText(vehicledistance);
                        ed_travel.setEnabled(false);
                      //  ed_travel.setBackground(getResources().getDrawable(R.drawable.edit_text));
                    }else {

                        double calvehicle = Double.parseDouble(txt_distance.getText().toString());
                        String vehicledistance = String.valueOf(vehicle * calvehicle);
                        ed_travel.setText(vehicledistance);
                        ed_travel.setEnabled(false);
                       // ed_travel.setBackground(getResources().getDrawable(R.drawable.edit_text));

                    }
                }else {
                    double vehicle=7;
                    String distance_vehicle=txt_distance.getText().toString();
                    if (distance_vehicle.contains(",")){
                        distance_vehicle = distance_vehicle.replace(",", "");
                        double calvehicle= Double.parseDouble(distance_vehicle);
                        String vehicledistance= String.valueOf(vehicle*calvehicle);
                        ed_travel.setText(vehicledistance);
                        ed_travel.setEnabled(false);
                      //  ed_travel.setBackground(getResources().getDrawable(R.drawable.edit_text));

                    }else {
                        double calvehicle = Double.parseDouble(txt_distance.getText().toString());
                        String vehicledistance = String.valueOf(vehicle * calvehicle);
                        ed_travel.setText(vehicledistance);
                        ed_travel.setEnabled(false);
                        //ed_travel.setBackground(getResources().getDrawable(R.drawable.edit_text));

                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void SetListner() {
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    btn_claim_date.setOnClickListener(new View.OnClickListener() {
                        int year, month, day;

                        @Override
                        public void onClick(View v) {
                            final Calendar c = Calendar.getInstance();
                            year = c.get(Calendar.YEAR);
                            month = c.get(Calendar.MONTH);
                            day = c.get(Calendar.DAY_OF_MONTH);
                            int a = 15;


                            DatePickerDialog datePickerDialog = new DatePickerDialog(ClaimDetailActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {
                                        Date d;
                                        String dd;

                                        @Override
                                        public void onDateSet(DatePicker datePicker, int year,
                                                              int monthOfYear, int dayOfMonth) {
                                            datePicker.setMinDate(c.getTimeInMillis());
                                            Calendar cal = Calendar.getInstance();
                                            datePicker.setMaxDate(cal.getTimeInMillis());
                                            String date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
                                            try {
                                                d = dff.parse(date);
                                                dd = Calim_dff.format(d);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            btn_claim_date.setText(dd);
                                        }
                                    }, year, month, day);
                            c.add(Calendar.DATE, -a);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                // only for gingerbread and newer versions
                                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                                Calendar cal = Calendar.getInstance();
                                datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                            }
                            datePickerDialog.setTitle("Select Date");
                            datePickerDialog.show();
                        }
                    });
                    sp_mode_of_journy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String ModeOfJourny = parent.getItemAtPosition(position).toString();
                            if (ModeOfJourny.equalsIgnoreCase("Vehicle")) {
                               // lay_distance.setVisibility(View.VISIBLE);
                                lay_vehicleType.setVisibility(View.VISIBLE);

                            } else {

                                   // ed_travel.setText("0");
                                    ed_travel.setEnabled(true);
                                    lay_distance.setVisibility(View.GONE);
                                    lay_vehicleType.setVisibility(View.GONE);


                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    btn_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (ed_fromPlace.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(ClaimDetailActivity.this, "Enter from place", Toast.LENGTH_LONG).show();
                            } else if (ed_toPlace.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(ClaimDetailActivity.this, "Enter to place", Toast.LENGTH_LONG).show();
                            } else if (ed_travel.getText().toString().equalsIgnoreCase("") || ed_local.getText().toString().equalsIgnoreCase("") || ed_phone.getText().toString().equalsIgnoreCase("") || ed_food.getText().toString().equalsIgnoreCase("") || ed_stay.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(ClaimDetailActivity.this, "Enter claim expenses", Toast.LENGTH_LONG).show();
                            } else {
                               if(ut.isNet(ClaimDetailActivity.this)){
                                new ValidateDate().execute(btn_claim_date.getText().toString());
                               }
                                /*if(checkDate())
                                getclaimDtlsobjTotalobj();
                                else
                                    Toast.makeText(ClaimDetailActivity.this, "Already Applied Claim for this date!", Toast.LENGTH_SHORT).show();*/
                            }


                        }
                    });
       /* ed_travel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                total = total + Integer.parseInt(ed_travel.getText().toString());
                tv_total.setText(total + " Rs");
            }
        });
        ed_stay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                total = total + Integer.parseInt(ed_stay.getText().toString());
                tv_total.setText(total + " Rs");
            }
        });
        ed_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                total = total + Integer.parseInt(ed_phone.getText().toString());
                tv_total.setText(total + " Rs");
            }
        });
        ed_food.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                total = total + Integer.parseInt(ed_food.getText().toString());
                tv_total.setText(total + " Rs");
            }
        });
        ed_local.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                total = total + Integer.parseInt(ed_local.getText().toString());
                tv_total.setText(total + " Rs");
            }
        });*/
                }


    private boolean checkDate() {

        boolean dateFalg = true;
        String claimdate= btn_claim_date.getText().toString();
        if(AppCommon.getInstance(this).getClaimDateList() != null) {
            claimRecordObjectForDate = new Gson().fromJson(AppCommon.getInstance(this).getClaimDateList()
                    , ClaimRecordObjectForDate.class);
        }
        claimList.add(claimdate);
        if(claimRecordObjectForDate !=  null){
            if(claimRecordObjectForDate.getDateList().size() != 0) {
                for (String string : claimRecordObjectForDate.getDateList()) {
                    if (btn_claim_date.getText().toString().equals(string)) {
                        dateFalg = false;
                    }
                }
            }

        }else {
            dateFalg = true;
                claimRecordObjectForDate.setDateList(claimList);
        }
        return dateFalg;
    }

    class ValidateDate extends AsyncTask<String,Void,String>{
        String res="";
        String url=null;
        String fromdate="",todate="";


        //dateFormat.format(date1);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

          /*  fromdate = params[0];
            todate = params[1];*/
            // SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            url = CompanyURL + WebUrlClass.api_ValidateDate_Claim +"?date="+ URLEncoder.encode(params[0]);

            try {
                res = ut.OpenConnection(url, ClaimDetailActivity.this);
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;

            // String url = CompanyURL + WebUrlClass.api_Leave_ReportingTo +"?DocMthdId=" + docHdrId;;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(res.equalsIgnoreCase("false")){
                getclaimDtlsobjTotalobj();
            }else{
                Toast.makeText(ClaimDetailActivity.this, "Already Applied Claim for this date!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    class DownloadModeOfJourny extends AsyncTask<Integer, Void, Integer> {
                    String res;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
           /* progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*/
                    }

                    @Override
                    protected Integer doInBackground(Integer... params) {
                        String url = CompanyURL + WebUrlClass.api_get_Mode_of_journy;

                        try {
                            res = ut.OpenConnection(url, getApplicationContext());
                            res = res.replaceAll("\\\\", "");
                            res = res.substring(1, res.length() - 1);
                            ContentValues values = new ContentValues();

                            JSONArray jResults = new JSONArray(res);
                            String msg = "";
                            sql.delete(db.TABLE_MODE_OF_JOURNY, null,
                                    null);
                            Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MODE_OF_JOURNY, null);
                            int count = c.getCount();
                            String columnName, columnValue;
                            for (int i = 0; i < jResults.length(); i++) {
                                JSONObject jorder = jResults.getJSONObject(i);
                                for (int j = 0; j < c.getColumnCount(); j++) {

                                    columnName = c.getColumnName(j);
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);

                                }

                                long a = sql.insert(db.TABLE_MODE_OF_JOURNY, null, values);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Integer integer) {
                        super.onPostExecute(integer);
                        UpdateModeOfJounry();
                    }

                }

                private void getclaimDtlsobjTotalobj() {

                    ClaimDetailsBean bean = new ClaimDetailsBean();

                    // bean.setAmount(total + "");
                    bean.setClaimDate(btn_claim_date.getText().toString());
                    bean.setFromPlace(ed_fromPlace.getText().toString());
                    bean.setToPlace(ed_toPlace.getText().toString());
                    bean.setTv_Local(ed_local.getText().toString());
                    bean.setTv_food(ed_food.getText().toString());
                    bean.setTv_lodging(ed_stay.getText().toString());
                    bean.setTv_Ph(ed_phone.getText().toString());
                    bean.setTv_Maintenanace(ed_Maintenance.getText().toString());
                    bean.setTv_travelling(ed_travel.getText().toString());
                    bean.setVehicleType(sp_vehicle_type.getSelectedItem().toString());
                    bean.setTv_mode(sp_mode_of_journy.getSelectedItem().toString());
                    if(!txt_distance.getText().toString().equals(""))
                    bean.setDistance(txt_distance.getText().toString());
                    else
                    bean.setDistance("0.0");

                    float total = (float) (Float.parseFloat(ed_local.getText().toString()) + Float.parseFloat(ed_food.getText().toString())
                                                + Float.parseFloat(ed_stay.getText().toString()) + Float.parseFloat(ed_phone.getText().toString())
                                                + Float.parseFloat(ed_Maintenance.getText().toString()) + Float.parseFloat(ed_travel.getText().toString()));

                    bean.setAmount(String.valueOf(total) + "");
                    int pos = -1;
                    if (getIntent().hasExtra("Action")) {
                        if (getIntent().getStringExtra("Action").equalsIgnoreCase("Edit")) {
                           // lsCalimDetails.remove(getIntent().getIntExtra("Position", -1));
                            //lsCalimDetails.add(getIntent().getIntExtra("Position", -1), bean);
                            pos = getIntent().getIntExtra("Position", -1);
                        }
                    } else {
                       // lsCalimDetails.add(bean);
                    }
     /*if(getIntent().getStringExtra("Action").equalsIgnoreCase("Edit")){

      }*/
                    Intent intent = new Intent();
                    intent.putExtra("Position" , pos);
                    intent.putExtra("object" , new Gson().toJson(bean));
                    setResult(12, intent);
                    finish();
                }


                private void getclaimDtlsobj() {
                    total = 0;
                    claimDtlsobj = new JSONObject();
                    try {
                        claimDtlsobj.put("Date", btn_claim_date.getText().toString());
                        ClaimDate = btn_claim_date.getText().toString();
                        claimDtlsobj.put("FrmPlace", ed_fromPlace.getText().toString());
                        claimDtlsobj.put("ToPlace", ed_toPlace.getText().toString());

                        String que = "SELECT Cd FROM " + db.TABLE_MODE_OF_JOURNY + " WHERE Desc_r='" + sp_mode_of_journy.getSelectedItem().toString() + "'";
                        Cursor cur = sql.rawQuery(que, null);
                        if (cur.getCount() > 0) {
                            cur.moveToFirst();
                            claimDtlsobj.put("JMode", cur.getString(cur.getColumnIndex("Cd")));
                        }
                        claimDtlsobj.put("Exp6", " ");
                        claimDtlsobj.put("Exp7", " ");
                        claimDtlsobj.put("Exp8", " ");
                        claimDtlsobj.put("Exp9", " ");
                        claimDtlsobj.put("lblhdn", " ");
                        claimDtlsobj.put("lblDtlId", " ");
                        claimDtlsobj.put("ModeName", " ");
                        claimDtlsobj.put("Lodging", " ");
                        if (!ed_travel.getText().toString().equalsIgnoreCase("")) {
                            claimDtlsobj.put("Travel", ed_travel.getText().toString());
                            totTravel = totTravel + Integer.parseInt(ed_travel.getText().toString());
                            total = total + Integer.parseInt(ed_travel.getText().toString());
                        } else {
                            claimDtlsobj.put("Travel", 0);
                            totTravel = totTravel + 0;
                            total = total + 0;
                        }

                        if (!ed_local.getText().toString().equalsIgnoreCase("")) {
                            claimDtlsobj.put("Local", ed_local.getText().toString());
                            totLocal = totLocal + Integer.parseInt(ed_local.getText().toString());
                            total = total + Integer.parseInt(ed_local.getText().toString());
                        } else {
                            claimDtlsobj.put("Local", 0);
                            totTravel = totTravel + 0;
                            total = total + 0;
                        }

                        if (!ed_phone.getText().toString().equalsIgnoreCase("")) {
                            claimDtlsobj.put("Phone", ed_phone.getText().toString());
                            totPhone = totPhone + Integer.parseInt(ed_phone.getText().toString());
                            total = total + Integer.parseInt(ed_phone.getText().toString());
                        } else {
                            claimDtlsobj.put("Phone", 0);
                            totTravel = totTravel + 0;
                            total = total + 0;
                        }

                        if (!ed_food.getText().toString().equalsIgnoreCase("")) {
                            claimDtlsobj.put("Food", ed_food.getText().toString());
                            totFood = totFood + Integer.parseInt(ed_food.getText().toString());
                            total = total + Integer.parseInt(ed_food.getText().toString());
                        } else {
                            claimDtlsobj.put("Food", 0);
                            totTravel = totTravel + 0;
                            total = total + 0;
                        }
                        if (!ed_stay.getText().toString().equalsIgnoreCase("")) {
                            claimDtlsobj.put("RM", ed_stay.getText().toString());
                            totRM = totRM + Integer.parseInt(ed_stay.getText().toString());
                            total = total + Integer.parseInt(ed_stay.getText().toString());
                        } else {
                            claimDtlsobj.put("RM", 0);
                            totTravel = totTravel + 0;
                            total = total + 0;
                        }

                        claimDtlsobj.put("Total", total);
                        totTotal = totTotal + total;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (getIntent().hasExtra("Action")) {
                            if (getIntent().getStringExtra("Action").equalsIgnoreCase("Edit")) {
                                //  claimDtls.remove(getIntent().getIntExtra("Position", 0));
                                claimDtls.put(getIntent().getIntExtra("Position", 0), claimDtlsobj);
                            }
                        } else {
                            claimDtls.put(claimDtlsobj);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    getclaimDtlsobjTotalobj();
                }

                public void UpdateModeOfJounry() {
                    String que = "SELECT Desc_r FROM " + db.TABLE_MODE_OF_JOURNY;
                    Cursor cur = sql.rawQuery(que, null);

                    if (cur.getCount() > 0) {
                        ModeOfJourny = new String[cur.getCount()];
                        cur.moveToFirst();
                        for (int i = 0; i < cur.getCount(); i++) {
                            ModeOfJourny[i] = cur.getString(cur.getColumnIndex("Desc_r"));
                            cur.moveToNext();
                        }
                        ModeOfTravelAdapter = new ArrayAdapter<String>(ClaimDetailActivity.this, android.R.layout.simple_spinner_item, ModeOfJourny);
                        ModeOfTravelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_mode_of_journy.setAdapter(ModeOfTravelAdapter);
                        if(localMode != null){
                            int index = -1;
                            for (int i=0;i<ModeOfJourny.length;i++) {
                                if (ModeOfJourny[i].equals(localMode)) {
                                    index = i;
                                    break;
                                }
                            }
                            if(index != -1) {
                                sp_mode_of_journy.setSelection(index);
                                sp_mode_of_journy.setSelected(true);
                            }
                        }
                    }


                }

                public static ArrayList autocomplete(String input) {
                    ArrayList resultList = null;

                    HttpURLConnection conn = null;
                    StringBuilder jsonResults = new StringBuilder();
                    try {
                        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                        sb.append("?key=" + API_KEY);
                        sb.append("&components=country:ind");
                        sb.append("&sensor=false");
                        sb.append("&types=geocode");
                        sb.append("&input=" + URLEncoder.encode(input, "utf8"));

                        URL url = new URL(sb.toString());
                        conn = (HttpURLConnection) url.openConnection();
                        InputStreamReader in = new InputStreamReader(conn.getInputStream());

                        // Load the results into a StringBuilder
                        int read;
                        char[] buff = new char[1024];
                        while ((read = in.read(buff)) != -1) {
                            jsonResults.append(buff, 0, read);
                        }
                    } catch (MalformedURLException e) {
                        // Log.e(LOG_TAG, "Error processing Places API URL", e);
                        return resultList;
                    } catch (IOException e) {
                        // Log.e(LOG_TAG, "Error connecting to Places API", e);
                        return resultList;
                    } finally {
                        if (conn != null) {
                            conn.disconnect();
                        }
                    }

                    try {
                        // Create a JSON object hierarchy from the results
                        JSONObject jsonObj = new JSONObject(jsonResults.toString());
                        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                        // Extract the Place descriptions from the results
                        resultList = new ArrayList(predsJsonArray.length());
                        for (int i = 0; i < predsJsonArray.length(); i++) {
                            System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                            System.out.println("============================================================");

                            String LocationSourceDescription = predsJsonArray.getJSONObject(i).getString("description");

                            resultList.add(LocationSourceDescription);
                        }
                    } catch (JSONException e) {
                        //  Log.e(LOG_TAG, "Cannot process JSON results", e);
                    }

                    return resultList;
                }

                class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
                    private ArrayList resultList;

                    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
                        super(context, textViewResourceId);
                    }

                    @Override
                    public int getCount() {
                        return resultList.size();
                    }

                    @Override
                    public String getItem(int index) {
                        return String.valueOf(resultList.get(index));
                    }

                    @Override
                    public Filter getFilter() {
                        Filter filter = new Filter() {
                            @Override
                            protected FilterResults performFiltering(CharSequence constraint) {
                                FilterResults filterResults = new FilterResults();
                                if (constraint != null) {
                                    // Retrieve the autocomplete results.
                                    resultList = autocomplete(constraint.toString());

                                    // Assign the data to the FilterResults
                                    filterResults.values = resultList;
                                    filterResults.count = resultList.size();
                                }
                                return filterResults;
                            }

                            @Override
                            protected void publishResults(CharSequence constraint, FilterResults results) {
                                if (results != null && results.count > 0) {
                                    notifyDataSetChanged();
                                } else {
                                    notifyDataSetInvalidated();
                                }
                            }
                        };
                        return filter;
                    }
                }



    private void CalculateDistance(final String fromlocation, final String tolocation) {

        final Thread t = new Thread() {

            public void run() {
                try {
                    String url = "https://maps.googleapis.com/maps/api/directions/json?key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk&origin=" + URLEncoder.encode(fromlocation, "UTF-8") + "&destination=" + URLEncoder.encode(tolocation, "UTF-8") + "&sensor=false";
                    Object res = ut.OpenConnection(url, getApplicationContext());
                    if (res == null) {

                    } else {
                        String response = res.toString();
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonroutes = jsonObject.getJSONArray("routes");


                        for (int i = 0; i < jsonroutes.length(); i++) {
                            JSONObject jorder = jsonroutes.getJSONObject(i);
                            JSONArray legs = jorder.getJSONArray("legs");
                            for (int j = 0; j < legs.length(); j++) {
                                JSONObject jlegs = legs.getJSONObject(i);
                                JSONObject jdistance = jlegs.getJSONObject("distance");
                                final String  text = jdistance.getString("text");
                                final String[] separated = text.split("km");
                                if (text.contains(".")) {
                                    disatnce = Double.parseDouble(separated[0]);
                                    final String D = String.valueOf(Math.ceil(disatnce));
                                    String[] separated1 = D.split("\\.");
                                    final String Distance_claim= separated1[0];
                                    ClaimDetailActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            txt_distance.setText(Distance_claim);
                                            len_distance.setVisibility(View.VISIBLE);
                                        }
                                    });

                                }else {
                                    ClaimDetailActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            String disatnce = separated[0];
                                            txt_distance.setText(disatnce);
                                            len_distance.setVisibility(View.VISIBLE);
                                        }
                                    });

                                }

                            }

                        }
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }


        };
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}


    /*class CalculateDistane extends AsyncTask<String , Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           *//* progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*//*
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = "https://maps.googleapis.com/maps/api/directions/json??key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk&origin=" + URLEncoder.encode(Fromlocation, "UTF-8") +"&destination=" + URLEncoder.encode(Tolocation, "UTF-8") +"&sensor=false";


                try {
                    res = ut.OpenConnection(url, getApplicationContext());
                    res = res.toString();

                    JSONObject jsonObject=new JSONObject(res);

                    JSONArray jsondistance =jsonObject.getJSONArray("legs");

                    for (int i = 0; i < jsondistance.length(); i++) {
                        JSONObject jorder = jsondistance.getJSONObject(i);
                        double disatnce= Double.parseDouble(jorder.getString("text"));

                        String D= String.valueOf(Math.ceil(disatnce));



                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
        }

    }
*/

