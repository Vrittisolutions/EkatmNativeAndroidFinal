package com.vritti.crm.vcrm7;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.adapter.AutocompleteCustomArrayAdapter;
import com.vritti.crm.adapter.FollowupAdapter;
import com.vritti.crm.adapter.VisitPlanAdapter;
import com.vritti.crm.bean.CityBean;
import com.vritti.crm.bean.Schedule;
import com.vritti.crm.bean.TravelPlan;
import com.vritti.crm.bean.VisitPlan;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.CustomAutoCompleteTextChangedListener;
import com.vritti.crm.classes.CustomAutoCompleteView;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.vritti.crm.vcrm7.BusinessProspectusActivity.COUNTRY;
import static com.vritti.crm.vcrm7.OpportunityActivity_V1.formateDateFromstring;

public class TravelPlanAddActivity extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;


    private TextView editTextDate;
    EditText editTextNotes;

    private Button buttonSave_travelplan, buttonClose_travelplan;
    public static CustomAutoCompleteView myAutoComplete;
    private static Context context;
    public static String  date = null, CurrentDate;
    String CityId, CityName, finaljson;
    static int year, month, day;
    SimpleDateFormat dfDate;
    ProgressHUD progressHUD;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    List city;
    public static ArrayAdapter<CityBean> myAdapter;
    TextView textweekdays;
    Animation scaleUp, scaleDown;

    ProgressBar progressbar;
    ImageView img_add,img_refresh,img_back;
    TextView txt_title;
    private Intent intent;
    String fromdt="", todt="";
    private LinearLayout lay_travelplan;
    ArrayList<TravelPlan> travelPlanArrayList;
    RecyclerView callhistory_listview;
    ArrayList<VisitPlan> visitPlanArrayList;
    VisitPlanAdapter visitPlanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_travel_plan);
        context = TravelPlanAddActivity.this;
        init();
        travelPlanArrayList = new ArrayList<TravelPlan>();
        visitPlanArrayList = new ArrayList<VisitPlan>();

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
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        //UserType = userpreferences.getString("UserType", null);
        dfDate = new SimpleDateFormat("dd/MM/yyy");
        CurrentDate = dfDate.format(new Date());
        editTextDate.setText(CurrentDate);

        if (cf.getCitycount() > 0) {
            getCity();
        } else {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCityJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }
        fromdt =formateDateFromstring("dd/MM/yyyy", "yyyy-MM-dd", CurrentDate);
        todt =formateDateFromstring("dd/MM/yyyy", "yyyy-MM-dd", CurrentDate);

        if (ut.isNet(TravelPlanAddActivity.this)) {
            new StartSession(TravelPlanAddActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadVisitData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

        setListener();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
       // toolbar.setTitleMarginStart(-8);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete);
        editTextDate = (TextView) findViewById(R.id.editTextDate);
        editTextNotes = (EditText) findViewById(R.id.editTextNotes);
        buttonSave_travelplan = (Button) findViewById(R.id.buttonSave_travelplan);
        buttonClose_travelplan = (Button) findViewById(R.id.buttonClose_travelplan);
        progressbar = (ProgressBar) findViewById(R.id.progressbar_1);
        textweekdays = (TextView)findViewById(R.id.txtweekdays);
        callhistory_listview = findViewById(R.id.callhistory_listview);
        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);
        lay_travelplan=findViewById(R.id.lay_travelplan);

        img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));

        txt_title.setText("Travel Plan - Add");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_u);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        /*buttonClose_travelplan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    buttonClose_travelplan.startAnimation(scaleUp);

                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    buttonClose_travelplan.startAnimation(scaleDown);
                }
                return false;

            }
        });
*/
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_u);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        /*buttonSave_travelplan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    buttonClose_travelplan.startAnimation(scaleUp);

                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    buttonClose_travelplan.startAnimation(scaleDown);
                }
                return false;

            }
        });*/
    }


    public boolean validate() {
        // TODO Auto-generated method stub
        if ((editTextDate.getText().toString().equalsIgnoreCase("") ||
                editTextDate.getText().toString().equalsIgnoreCase(" ") ||
                editTextDate.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Select date", Toast.LENGTH_LONG).show();
            return false;
        } else if ((myAutoComplete.getText().toString().equalsIgnoreCase("") ||
                myAutoComplete.getText().toString().equalsIgnoreCase(" ") ||
                myAutoComplete.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter city", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextNotes.getText().toString().equalsIgnoreCase("") ||
                editTextNotes.getText().toString().equalsIgnoreCase(" ") ||
                editTextNotes.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter notes", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void setListener() {
        buttonClose_travelplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validate()) {
                        final JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Mode", "A");
                        jsonObject.put("CityId ", CityId);
                        jsonObject.put("CityName", CityName);
                        String notes = editTextNotes.getText().toString();
                        jsonObject.put("Notes", URLEncoder.encode(notes,"UTF-8"));//
                        jsonObject.put("TravelDate", editTextDate.getText().toString());
                        jsonObject.put("PKTravelPlanId", "");
                        finaljson = jsonObject.toString();
                        finaljson = finaljson.replaceAll("\\\\", "");
                        finaljson = finaljson.replaceAll(" ", "");
                        overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);


                        if (isnet()) {
                            showProgressDialog();
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostTravelPlanJSON().execute(finaljson);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        }
                    }


                } catch (JSONException | UnsupportedEncodingException e) {

                }
            }
        });
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                editTextDate.setText(date);
                                fromdt =formateDateFromstring("dd/MM/yyyy", "yyyy-MM-dd", date);
//                                todt =formateDateFromstring("dd/MM/yyyy", "yyyy-MM-dd", date);
                                if (isnet()) {
                                    new StartSession(context, new CallbackInterface() {
                                        @Override
                                        public void callMethod() {
                                            new DownloadVisitData().execute();
                                        }

                                        @Override
                                        public void callfailMethod(String msg) {

                                        }
                                    });
                                }

                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });


        myAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = CompanyURL + WebUrlClass.api_get_city
                        + "?PKCityID=";
                Intent intent = new Intent(TravelPlanAddActivity.this, CountryListActivity.class);
                intent.putExtra("Table_Name", db.TABLE_CITY_ENTITY);
                intent.putExtra("Id", "PKCityID");
                intent.putExtra("DispName", "CityName");
                intent.putExtra("WHClauseParameter","");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<City> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });


       /* myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {


                RelativeLayout rl = (RelativeLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                myAutoComplete.setText(tv.getText().toString());
                String que = "SELECT *  FROM " + db.TABLE_CITY +
                        " WHERE CityName='" + myAutoComplete.getText().toString() + "'";
                Cursor cur = sql.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    CityId = cur.getString(cur.getColumnIndex("PKCityID"));
                    CityName = cur.getString(cur.getColumnIndex("CityName"));

                }


            }

        });*/
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

    private void showProgressDialog() {


        // progressHUD = ProgressHUD.show(context, "", false, false, null);

        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }
    }

    private void getCity() {

        myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));
        CityBean myObject = null;
        CityBean[] ObjectItemData = new CityBean[1];
        myObject = new CityBean();
        ObjectItemData[0] = myObject;
        myAdapter = new AutocompleteCustomArrayAdapter(this, R.layout.crm_list_view_row_item, ObjectItemData);
        myAutoComplete.setAdapter(myAdapter);
        int textLength1 = myAutoComplete.getText().length();
        myAutoComplete.setSelection(textLength1, textLength1);
        myAutoComplete.setFocusable(true);


    }

    class DownloadCityJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_city
                        + "?PKCityID=" + URLEncoder.encode("", "UTF-8");

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_CITY, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CITY, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        String jsonAddeddt = jorder.getString("AddedDt");
                        String jsonModifiedDt = jorder.getString("ModifiedDt");
                        if (columnName.equalsIgnoreCase("AddedDt")) {
                            jsonAddeddt = jsonAddeddt.substring(jsonAddeddt.indexOf("(") + 1, jsonAddeddt.lastIndexOf(")"));
                            long DOB_date = Long.parseLong(jsonAddeddt);
                            DOBDate = new Date(DOB_date);
                            jsonAddeddt = sdf.format(DOBDate);
                            values.put(columnName, jsonAddeddt);

                        } else if (columnName.equalsIgnoreCase("ModifiedDt")) {
                            jsonModifiedDt = jsonModifiedDt.substring(jsonModifiedDt.indexOf("(")
                                    + 1, jsonModifiedDt.lastIndexOf(")"));
                            long DOB_date = Long.parseLong(jsonModifiedDt);
                            DOBDate = new Date(DOB_date);
                            jsonModifiedDt = sdf.format(DOBDate);
                            values.put(columnName, jsonModifiedDt);

                        } else {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }


                    }

                    long a = sql.insert(db.TABLE_CITY, null, values);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.contains("")) {

                //   getInitiatedby();
                getCity();
            }

        }

    }

    class PostTravelPlanJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_post_travel_plan;
            try {
                res = ut.OpenPostConnection(url, params[0],TravelPlanAddActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();

            if (response != null) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String data=jsonObject.getString("Data");
                    String Resp=jsonObject.getString("Resp");
                    if (Resp.equalsIgnoreCase("False")){
                        Toast.makeText(context, data, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(context, "Travel plan added successfully on " + data, Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(context, CallListActivity.class);
        startActivity(intent);*/
        // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);


        TravelPlanAddActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                Animation rotation = AnimationUtils.loadAnimation(this, R.anim.clockwise);
                rotation.setRepeatCount(Animation.INFINITE);



        }
        return (super.onOptionsItemSelected(menuItem));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COUNTRY && resultCode == COUNTRY) {
               String  CountryName = data.getStringExtra("Name");
                myAutoComplete.setText(CountryName);

            String que = "SELECT *  FROM " + db.TABLE_CITY +
                    " WHERE CityName='" + CountryName + "'";
            Cursor cur = sql.rawQuery(que, null);
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                CityId = cur.getString(cur.getColumnIndex("PKCityID"));
                CityName = cur.getString(cur.getColumnIndex("CityName"));

            }


        }
        }

    class DownloadTravelPlan extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //  showProgressDialog();

        }


        @Override
        protected String doInBackground(String... params) {
            //FromDate: '01/01/2015', ToDate: '31/05/2015'


            try {
                String url = CompanyURL + WebUrlClass.api_GetVisitandTravelPlan
                        + "?UserMasterId=" + UserMasterId +
                        "&Date=" + fromdt;

                res = ut.OpenConnection(url);
                response = res.toString();
              /*  response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
            */
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("error")) {
                Toast.makeText(TravelPlanAddActivity.this, "Record not found", Toast.LENGTH_SHORT).show();

            } else {
                displayTravelplan();
            }
        }

    }

    private void displayTravelplan() {
        try {
            travelPlanArrayList.clear();
            lay_travelplan.removeAllViews();
            Date d1;
            String query = "SELECT  PKTravelPlanId,Date,Notes,CityName" +
                    " FROM " + db.TABLE_Travelplan + " ORDER BY Date desc";

            Cursor cur = sql.rawQuery(query, null);

            if (cur.getCount() > 0) {

                cur.moveToFirst();
                do {
                    TravelPlan travelPlan = new TravelPlan();
                    travelPlan.setPKTravelPlanId(cur.getString(cur.getColumnIndex("PKTravelPlanId")));
                    travelPlan.setDate(cur.getString(cur.getColumnIndex("Date")));
                    travelPlan.setNotes(cur.getString(cur.getColumnIndex("Notes")));
                    travelPlan.setCityName(cur.getString(cur.getColumnIndex("CityName")));
                    travelPlanArrayList.add(travelPlan);
                } while (cur.moveToNext());
            }

            if (travelPlanArrayList.size() > 0) {
                for (int i = 0; i < travelPlanArrayList.size(); i++) {
                    addView_Travellist(i);
                }
            } else {
                TravelPlan travelPlan = new TravelPlan();
                travelPlan.setPKTravelPlanId("");
                travelPlan.setDate("");
                travelPlan.setNotes("No data available");
                travelPlan.setCityName("");
                travelPlanArrayList.add(travelPlan);
                for (int i = 0; i < travelPlanArrayList.size(); i++) {
                    addView_Travellist(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addView_Travellist(int i) {

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int pos = i;
        final View convertView = layoutInflater.inflate(R.layout.crm_custom_travel_plan_show,
                null);
       TextView textViewcity = (TextView) convertView.findViewById(R.id.textViewcity);
        TextView textViewdate = (TextView) convertView.findViewById(R.id.textViewdate);
        TextView textViewnotes = (TextView) convertView.findViewById(R.id.textViewnotes);
       ImageView spinner_action = (ImageView) convertView.findViewById(R.id.spinner_action);
       ImageView spinneraction = (ImageView) convertView.findViewById(R.id.spinneraction);
        LinearLayout lay1 = (LinearLayout) convertView.findViewById(R.id.lay1);
        textViewdate.setVisibility(View.GONE);
        //  spinneraction.setSelection(0);No data available
        spinneraction.setVisibility(View.GONE);
        spinner_action.setVisibility(View.GONE);
        spinner_action.setVisibility(View.GONE);
        if (travelPlanArrayList.get(i).getNotes().equalsIgnoreCase("No data available")) {
            textViewnotes.setText(travelPlanArrayList.get(i).getNotes());
            spinneraction.setVisibility(View.GONE);
            lay1.setVisibility(View.GONE);
        } else {
            textViewcity.setText(travelPlanArrayList.get(i).getCityName());

            String dt = travelPlanArrayList.get(i).getDate();
            System.out.println("DateBefore  :" + dt);
            String[] namesList = dt.split("T");
            String name1 = namesList [0];


            String date_after = formateDateFromstring("yyyy-MM-dd", "dd MMM", name1);
            textViewdate.setText(date_after);
            textViewnotes.setText(travelPlanArrayList.get(i).getNotes().replace("+"," "));
            spinneraction.setTag(lay_travelplan.getChildCount());
        }

        lay_travelplan.addView(convertView);
    }

    class DownloadVisitData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url =CompanyURL + WebUrlClass.api_GetVisitandTravelPlan+"?UserMasterId="+UserMasterId+"&Date="+fromdt;

                System.out.println("URLCALLHISTORY :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString();

                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (response.equals("[]")) {
                Toast.makeText(TravelPlanAddActivity.this, "Record not found", Toast.LENGTH_SHORT).show();


            } else {
                if (response != null) {
                    try {
                        travelPlanArrayList.clear();
                        lay_travelplan.removeAllViews();
                        visitPlanArrayList.clear();
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jResults =jsonObject.getJSONArray("VisitPlan") ;
                        if (jResults.length()>0) {
                            for (int i = 0; i < jResults.length(); i++) {
                                JSONObject jcallhistory = jResults.getJSONObject(i);

                                VisitPlan visitPlan = new VisitPlan();
                                visitPlan.setFirmName(jcallhistory.getString("FirmName"));
                                visitPlan.setCityName(jcallhistory.getString("CityName"));
                                visitPlan.setFollowupComment(jcallhistory.getString("FollowupComment"));
                                visitPlanArrayList.add(visitPlan);
                            }
                            visitPlanAdapter = new VisitPlanAdapter(TravelPlanAddActivity.this, visitPlanArrayList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            callhistory_listview.setLayoutManager(mLayoutManager);
                            callhistory_listview.setItemAnimator(new DefaultItemAnimator());
                            callhistory_listview.setAdapter(visitPlanAdapter);
                        }

                        JSONArray TjResult =jsonObject.getJSONArray("TravelPlan") ;
                        if (TjResult.length()>0) {

                            for (int j = 0; j < TjResult.length(); j++) {
                                JSONObject jtravel = TjResult.getJSONObject(j);
                                TravelPlan travelPlan = new TravelPlan();
                                travelPlan.setPKTravelPlanId(jtravel.getString("PKTravelPlanId"));
                                travelPlan.setDate(jtravel.getString("Date"));
                                travelPlan.setNotes(jtravel.getString("Notes"));
                                travelPlan.setCityName(jtravel.getString("CityName"));
                                travelPlanArrayList.add(travelPlan);
                            }

                            if (travelPlanArrayList.size() > 0) {
                                for (int i = 0; i < travelPlanArrayList.size(); i++) {
                                    addView_Travellist(i);
                                }
                            }
                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }


}
