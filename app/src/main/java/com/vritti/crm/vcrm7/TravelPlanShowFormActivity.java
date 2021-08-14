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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.vritti.crm.vcrm7.BusinessProspectusActivity.COUNTRY;

public class TravelPlanShowFormActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Animation scaleUp, scaleDown;

    AutoCompleteTextView AutocompExecutive, AutocompTravelplan;
    TextView editTextfrom, editTextto;
    CheckBox chkdate, chkExecutive,checkBox;
    ImageView button_add;
    TextView btnshow;
    private static Context context;
    ProgressHUD progressHUD;
    public static String date = null, CurrentDate;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
    CardView cardView1;
    Date DOBDate = null;
    static int year, month, day;
    Date result;
    boolean chkdt = false, chkexe = true;
    List category;
    String AssignToUserId = "",AssignToUsername="";
    ProgressBar progressbar;

    ImageView img_add,img_refresh,img_back;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_travel_plan_show_form);
        init();

        context = TravelPlanShowFormActivity.this;

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
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        AutocompExecutive.setText(UserName);



        if (cf.getExecutivecount() > 0) {
            getCategory();
        } else {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCategoryJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }

        setListener();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txt_title=findViewById(R.id.txt_title);
        // img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        txt_title.setText("Travel Plan - View");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        AutocompExecutive = (AutoCompleteTextView) findViewById(R.id.AutocompExecutive);
        AutocompTravelplan = (AutoCompleteTextView)findViewById(R.id.Autocompelete);
        editTextfrom = (TextView) findViewById(R.id.editTextfrom);
        editTextto = (TextView) findViewById(R.id.editTextto);
        btnshow = (TextView) findViewById(R.id.btnshow);
        button_add = (ImageView) findViewById(R.id.button_add);
        chkdate = (CheckBox) findViewById(R.id.chkdate);
        chkExecutive = (CheckBox) findViewById(R.id.chkdate);
        progressbar = (ProgressBar) findViewById(R.id.progressbar_1);
        cardView1 = (CardView)findViewById(R.id.card_view);
        checkBox = (CheckBox)findViewById(R.id.checkbox);


        Calendar aCalendar = Calendar.getInstance();
        Date firstDateOfCurrentMonth = aCalendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dayFirst = sdf.format(firstDateOfCurrentMonth);
        System.out.println(dayFirst);

        editTextfrom.setText(dayFirst);

        //txt_fromdate.setText("01-02-2021");

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +7);
        String date=dateFormat.format(cal.getTime());
        editTextto.setText(date);


        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_u);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

       /* btnshow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    btnshow.startAnimation(scaleUp);

                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){

                    btnshow.startAnimation(scaleDown);
                }
                return false;

            }
        });

*/

       /* checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    AutocompTravelplan.setVisibility(View.VISIBLE);
                }
                else{
                    AutocompTravelplan.setVisibility(View.GONE);
                }
            }
        });*/


        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TravelPlanShowFormActivity.this, TravelPlanAddActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });



    }

    private void closeKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public boolean validate() {
        // TODO Auto-generated method stub

            if ((editTextfrom.getText().toString().equalsIgnoreCase("") ||
                    editTextfrom.getText().toString().equalsIgnoreCase(" ") ||
                    editTextfrom.getText().toString().equalsIgnoreCase(null))) {

                //  Toast.makeText(context, "Select from date", Toast.LENGTH_LONG).show();
                return false;
            } else if ((editTextto.getText().toString().equalsIgnoreCase("") ||
                    editTextto.getText().toString().equalsIgnoreCase(" ") ||
                    editTextto.getText().toString().equalsIgnoreCase(null))) {

                //   Toast.makeText(context, "Select to date", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }


    }

    private void setListener() {
        /*chkExecutive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    chkexe = true;

                } else {
                    chkexe = false;
                }
            }
        });
        chkdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    chkdt = true;
                } else {
                    chkdt = false;

                }
            }
        });*/


        AutocompExecutive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TravelPlanShowFormActivity.this,
                        CountryListActivity.class);
            //    String url = CompanyURL + WebUrlClass.api_Category;
                String url = CompanyURL + WebUrlClass.api_get_TeamMembers +
                        "?UserMasterId="+UserMasterId;
               // intent.putExtra("Table_Name", db.TABLE_Executive);
                intent.putExtra("Table_Name", db.TABLE_Team_Member);
                intent.putExtra("Id", "UserMasterId");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("WHClauseParameter", "");
                intent.putExtra("APIName", url);
                startActivityForResult(intent, COUNTRY);
            }
        });


        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate() == true) {



                    Intent intent = new Intent(context, TravelPlanShowActivity.class);
                    String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                            " FROM " + db.TABLE_Executive + " WHERE " +
                            " UserName='" + AutocompExecutive.getText().toString() + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {

                        cur.moveToFirst();


                        AssignToUserId = cur.getString(cur.getColumnIndex("UserMasterId"));
                        AssignToUsername=AutocompExecutive.getText().toString();
                        intent.putExtra("userid", AssignToUserId);
                        intent.putExtra("username",AssignToUsername);
                    } else {
                        intent.putExtra("userid", UserMasterId);
                        intent.putExtra("username", UserName);
                    }
                    intent.putExtra("from", editTextfrom.getText().toString());
                    intent.putExtra("to", editTextto.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                }


            }
        });


        editTextfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                result = c.getTime();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis() - 10000);
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                editTextfrom.setText(date);


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

                editTextfrom.setText(date);


            }
        });




        editTextto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                date = "";
                result = c.getTime();

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(result.getTime());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                //  editTextto.setText(date);
                                if (editTextfrom.getText().toString().trim().length() > 0) {
                                    if (compare_date(editTextfrom.getText().toString(), date) == true) {
                                        editTextto.setText(date);
                                        btnshow.setVisibility(View.VISIBLE);
                                    } else {
                                        btnshow.setVisibility(View.GONE);
                                        //  editTextto.setText(date);
                                        Toast.makeText(TravelPlanShowFormActivity.this,
                                                "You cannot select a day earlier than from date!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(TravelPlanShowFormActivity.this,
                                            "Select from date ",
                                            Toast.LENGTH_LONG).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                if(!isFinishing())
                    datePickerDialog.show();
            }
        });

        AutocompExecutive.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (validate() == true) {

                        Intent intent = new Intent(context, TravelPlanShowActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                                " FROM " + db.TABLE_Executive + " WHERE " +
                                " UserName='" + AutocompExecutive.getText().toString() + "'";
                        Cursor cur = sql.rawQuery(query, null);

                        if (cur.getCount() > 0) {

                            cur.moveToFirst();

                            AssignToUserId = cur.getString(cur.getColumnIndex("UserMasterId"));
                            intent.putExtra("userid", AssignToUserId);
                            intent.putExtra("username", AutocompExecutive.getText().toString());
                        } else {
                            intent.putExtra("userid", UserMasterId);
                            intent.putExtra("username", UserName);
                        }
                        intent.putExtra("from", editTextfrom.getText().toString());
                        intent.putExtra("to", editTextto.getText().toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        // TravelPlanShowFormActivity.this.finish();
                    }

                }
                return false;
            }
        });


    }                                                   //todate

    public static boolean compare_date(String fromdate, String dt) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
        Date d1, d2;
        String s1;

        // str = outputFormat.format(date);
        //   String today;
        //  today = dfDate.format(new Date());
        try {
            d1 = dfDate.parse(fromdate);
            d2 = dfDate.parse(dt);
            if ((dfDate.parse(dt).after(dfDate.parse(fromdate)) ||
                    dfDate.parse(dt).equals(dfDate.parse(fromdate)))) {
                b = true;
            } else {
                // date = today;
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
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

    private void showProgressDialog(String txt) {


        // progressHUD = ProgressHUD.show(context, "" + txt, false, false, null);

        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        /*if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }*/
        if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }


    }

    class DownloadCategoryJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            // progressHUD7 = ProgressHUD.show(ReassignCallsActivity.this, " ", false, false, null);
            showProgressDialog("");
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Category;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_Executive, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Executive, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_Executive, null, values);

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

            if (response != null) {

            }

            getCategory();
        }


    }

    private void getCategory() {

        category = new ArrayList();
        String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                " FROM " + db.TABLE_Executive + " WHERE CRMCategory='1' OR CRMCategory='2'";
        Cursor cur = sql.rawQuery(query, null);
        category.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                category.add(cur.getString(cur.getColumnIndex("UserName")));

            } while (cur.moveToNext());

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_spinner_item, category);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AutocompExecutive.setAdapter(dataAdapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COUNTRY && resultCode == COUNTRY) {
                AssignToUsername = data.getStringExtra("Name");
                AssignToUserId = data.getStringExtra("ID");

               AutocompExecutive .setText(AssignToUsername);
            }
        }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(context, CallListActivity.class);
        startActivity(intent);*/
        // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        TravelPlanShowFormActivity.this.finish();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(context, TravelPlanAddActivity.class);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        }
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }*/



}
