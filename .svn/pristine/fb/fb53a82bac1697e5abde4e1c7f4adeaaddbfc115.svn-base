package com.vritti.inventory.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.adapter.CustomAdapter;
import com.vritti.inventory.bean.CommonData;
import com.vritti.inventory.bean.DataItem;
import com.vritti.inventory.bean.FilePath;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by sharvari on 16-Jul-18.
 */

public class VendorRegistrationForm extends AppCompatActivity {


    SearchableSpinner spinner_country,spinner_state,spinner_city,
            spinner_currency,spinner_entitytype,spinner_district,spinner_taluka,spinner_accounttype;
    SharedPreferences sharedPreferences;
    public static final String MYPREFERENCE = "Mypreference";
    SharedPreferences.Editor editor;
    String UserType;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    SQLiteDatabase sql;
    private ArrayList<String > lstcountrylist=new ArrayList<>();
    private ArrayList<String > lstCitylist=new ArrayList<>();
    private ArrayList<String > lstStatelist=new ArrayList<>();
    private ArrayList<String > lstCurrencylist=new ArrayList<>();
    private ArrayList<String > lstEntitypelist=new ArrayList<>();
    ArrayList<String> StateArrayList=new ArrayList<>();
    ArrayList<DataItem> StateArrayList1=new ArrayList<>();
    public static ProgressBar mprogress;
    EditText edt_ifsc,edt_branchname,edt_bankname,edt_bankaddress,
    edt_companyname,edt_shortname,edt_address,edt_pincode,edt_landline,
    edt_contactname,edt_designation,edt_mobileno,edt_email,
    edt_skypeid,edt_influe_level,edt_gstn,edt_panno,edt_payeename,edt_accountno,edt_remiinst,edt_expertise;
    TextView txt_verify,attachment_name,txt_pan,txt_gstn;
    String IFSCCode;
    private String Bankaddress,Bankname,Branch;
    private String StateId,DistrictId,EntityTypeMasterId,CityId,TalukaId;
    CustomAdapter commonSpinnerAdapter;
    ArrayList<CommonData> commonDistrictDataArrayList=new ArrayList<>();
    ArrayList<String> commonDistrictDataArrayList1=new ArrayList<>();

    ArrayList<CommonData> commonTalukaDataArrayList=new ArrayList<>();
    ArrayList<String> commonTalukaDataArrayList1=new ArrayList<>();
    Button btnsave;
    private static final int PICK_FILE_REQUEST = 1;

    private Uri fileUri;
    String path, Imagefilename;
    private String serverResponseMessage;
    private String Promise_date,parsedDate;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int MEDIA_TYPE_VIDEO_CAPTURE = 22;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    static File mediaFile;
    private static final String IMAGE_DIRECTORY_NAME ="Vendor";

    private SimpleDateFormat dateFormatdate;
    String Current_date;
    AsyncTask async;
    private String Id,Vendordata,Epertise="";
    DatabaseHandlers db;
    CommonFunction cf;

    JSONArray jsonArray=new JSONArray();
    JSONObject jsonimage=new JSONObject();
    ImageView txt_add_attachment,img_refresh;
    Utility ut;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_venor_reg_form_lay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Vendor Registration");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        context=VendorRegistrationForm.this;
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
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);


        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);


        Calendar cal = Calendar.getInstance();
        DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
        String formattedTime = outputFormat.format(cal.getTime());


        spinner_country= (SearchableSpinner) findViewById(R.id.sp_country);
        spinner_state= (SearchableSpinner) findViewById(R.id.spinner_state);
        spinner_city= (SearchableSpinner) findViewById(R.id.spinner_city);
        spinner_currency= (SearchableSpinner) findViewById(R.id.spinner_currency);
        spinner_entitytype= (SearchableSpinner) findViewById(R.id.spinner_entitytype);
        spinner_district= (SearchableSpinner) findViewById(R.id.spinner_district);
        spinner_taluka= (SearchableSpinner) findViewById(R.id.spinner_taluka);
        spinner_accounttype= (SearchableSpinner) findViewById(R.id.spinner_accounttype);
        edt_ifsc= (EditText) findViewById(R.id.edt_ifsc);
        txt_verify= (TextView) findViewById(R.id.txt_verify);
        edt_bankname= (EditText) findViewById(R.id.edt_bankname);
        edt_bankaddress= (EditText) findViewById(R.id.edt_bankaddress);
        edt_branchname= (EditText) findViewById(R.id.edt_branchname);
        edt_companyname= (EditText) findViewById(R.id.edt_companyname);
        edt_shortname= (EditText) findViewById(R.id.edt_shortname);
        edt_address= (EditText) findViewById(R.id.edt_address);
        edt_pincode= (EditText) findViewById(R.id.edt_pincode);
        edt_landline= (EditText) findViewById(R.id.edt_landline);
        edt_contactname= (EditText) findViewById(R.id.edt_contactname);
        edt_designation= (EditText) findViewById(R.id.edt_designation);
        edt_mobileno= (EditText) findViewById(R.id.edt_mobileno);
        edt_email= (EditText) findViewById(R.id.edt_email);
        edt_skypeid= (EditText) findViewById(R.id.edt_skypeid);
        edt_influe_level= (EditText) findViewById(R.id.edt_influe_level);
        edt_gstn= (EditText) findViewById(R.id.edt_gstn);
        edt_panno= (EditText) findViewById(R.id.edt_panno);
        edt_payeename= (EditText) findViewById(R.id.edt_payeename);
        edt_accountno= (EditText) findViewById(R.id.edt_accountno);
        edt_remiinst= (EditText) findViewById(R.id.edt_remiinst);
        btnsave= (Button) findViewById(R.id.btnsave);
        txt_add_attachment= (ImageView) findViewById(R.id.txt_add_attachment);
        attachment_name= (TextView) findViewById(R.id.attachment_name);
        img_refresh= (ImageView) findViewById(R.id.img_refresh);
        edt_expertise= (EditText) findViewById(R.id.edt_expertise);


        txt_gstn= (TextView) findViewById(R.id.txt_gstn);
        txt_pan= (TextView) findViewById(R.id.txt_pan);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {
            }
        } else {
        }

        requestRuntimePermission();

        txt_gstn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String GSTNO = edt_gstn.getText().toString(); // get your editext value here
               // Pattern pattern = Pattern.compile("/^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$/");
                String reg = "[0-9]{2}[A-Za-z]{5}[0-9]{4}[a-zA-Z][0-9]{1}[a-zA-Z]{1}[0-9]{1}";
               // Matcher matcher = pattern.matcher(GSTNO);
// Check if pattern matches
                if (GSTNO.matches(reg)) {
                    Log.i("Matching","Yes");
                    Toast.makeText(VendorRegistrationForm.this,"Valid GST Code",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(VendorRegistrationForm.this,"Please enter valid GSTIN no",Toast.LENGTH_SHORT).show();
                }

            }
        });
        txt_pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pan = edt_panno.getText().toString(); // get your editext value here
               // Pattern pattern = Pattern.compile("/^([a-zA-Z]{5})(\\d{4})([a-zA-Z]{1})$/");

               // Matcher matcher = pattern.matcher(pan);
                String reg = "[A-Za-z]{5}[0-9]{4}[a-zA-Z]";


// Check if pattern matches
                if (pan.matches(reg)) {
                    Log.i("Matching","Yes");
                    Toast.makeText(VendorRegistrationForm.this,"Valid PAN",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(VendorRegistrationForm.this,"Please enter valid PAN",Toast.LENGTH_SHORT).show();
                }

            }
        });

        txt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IFSCCode=edt_ifsc.getText().toString();
                if (isnet()) {

                            new DownloadIFSCcodeData().execute(IFSCCode);

                }
            }
        });


        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String query = "SELECT distinct PKStateId,StateDesc" +
                        " FROM " + db.TABLE_STATE +
                        " WHERE StateDesc='" + spinner_state.getSelectedItem().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        StateId = cur.getString(cur.getColumnIndex("PKStateId"));

                        if (isnet()) {
                            new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadDistrictData().execute(StateId);

                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }

                    } while (cur.moveToNext());

                } else {
                    StateId = "";
                }



            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_entitytype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String query = "SELECT distinct EntityTypeMasterId,EntityType" +
                        " FROM " + db.TABLE_EntityType +
                        " WHERE EntityType='" + spinner_entitytype.getSelectedItem().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        EntityTypeMasterId = cur.getString(cur.getColumnIndex("EntityTypeMasterId"));

                    } while (cur.moveToNext());

                } else {
                    StateId = "";
                }



            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        spinner_taluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TalukaId = commonTalukaDataArrayList.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                DistrictId=commonDistrictDataArrayList.get(i).getId();


                if (isnet()) {
                    new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadTalukaData().execute(DistrictId);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }




            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String query = "SELECT distinct PKCityID,CityName" +
                        " FROM " + db.TABLE_City_INV +
                        " WHERE CityName='" + spinner_city.getSelectedItem().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        CityId = cur.getString(cur.getColumnIndex("PKCityID"));



                    } while (cur.moveToNext());

                } else {
                    CityId = "";
                }



            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        txt_add_attachment.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent();
                                                        //sets the select file to all types of files
                                                        intent.setType("*/*");
                                                        //allows to select data and return it
                                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                                        //starts new activity to select file and return data
                                                        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);          }
                                                }
        );

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate1()) {
                    JSONObject jobj = new JSONObject();
                    try {
                        jobj.put("VendorMasterId", "");
                        jobj.put("EntityType", EntityTypeMasterId);
                        jobj.put("VendorCode", "");
                        jobj.put("ShortName", edt_shortname.getText().toString());
                        jobj.put("VendorName", edt_companyname.getText().toString());
                        jobj.put("CompAddress", edt_address.getText().toString());
                        jobj.put("Pincode", edt_pincode.getText().toString());
                        jobj.put("Country", "1");
                        jobj.put("InState", StateId);
                        jobj.put("City", CityId);
                        jobj.put("Distict", DistrictId);
                        jobj.put("Taluka", TalukaId);
                        jobj.put("LanlineNo", edt_landline.getText().toString());
                        jobj.put("Currency", spinner_currency.getSelectedItem().toString());
                        jobj.put("ContactName", edt_contactname.getText().toString());
                        jobj.put("MobileNo", edt_mobileno.getText().toString());
                        jobj.put("EmailId", edt_email.getText().toString());
                        jobj.put("SkypeId", edt_skypeid.getText().toString());
                        jobj.put("Designation", edt_designation.getText().toString());
                        jobj.put("Influntial", edt_influe_level.getText().toString());
                        jobj.put("Gstno", edt_gstn.getText().toString());
                        jobj.put("Pano", edt_panno.getText().toString());
                        jobj.put("PayeeName", edt_payeename.getText().toString());
                        jobj.put("IFSCode", edt_ifsc.getText().toString());
                        jobj.put("BankName", edt_bankname.getText().toString());
                        jobj.put("BranchName", edt_branchname.getText().toString());
                        jobj.put("BankAddress", edt_bankaddress.getText().toString());
                        jobj.put("AccountType", spinner_accounttype.getSelectedItem().toString());
                        jobj.put("AccountNo", edt_accountno.getText().toString());
                        jobj.put("Remittance", edt_remiinst.getText().toString());
                        jobj.put("Remittance", Imagefilename);
                        jobj.put("ExpertName",edt_expertise.getText().toString() );

                        JSONObject jobjdata = new JSONObject();
                        jobjdata.put("Data1", jobj);
                        jobjdata.put("Mode", "A");
                        final String finaljson = jobjdata.toString();
                        // finaljson = finaljson.replaceAll("\\\\", "");

                        if (isnet()) {
                            new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new Postvendordata().execute(finaljson);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }


                            });
                        }

                    } catch (JSONException e) {

                    }
                }
            }
        });

        img_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isnet()) {
                    new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCountryJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
                if (isnet()) {
                    new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCityJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
                if (isnet()) {
                    new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadStateData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
                if (isnet()) {
                    new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCurrencyData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
                if (isnet()) {
                    new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadEntityTypeData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }
        });



        if (cf.getcountryecount() > 0) {
            getCountrylist();
        } else {
            if (isnet()) {
                new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCountryJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        if (cf.getCitycount() > 0) {
            getCitylist();
        } else {
            if (isnet()) {
                new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
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
        if (cf.getStatecount() > 0) {
            getStatelist();
        } else {
            if (isnet()) {
                new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadStateData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        if (cf.getCurrencycount() > 0) {
            getCurrencylist();
        } else {
            if (isnet()) {
                new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCurrencyData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
        if (cf.getEntityTyptcount() > 0) {
            getEntityTypelist();
        } else {
            if (isnet()) {
                new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadEntityTypeData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
    }





    class DownloadCountryJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            showprogress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getCountry;

            try {
                res = ut.OpenConnection(url,VendorRegistrationForm.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_Country_INVEN, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Country_INVEN, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_Country_INVEN, null, values);

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
             hidprogress();
            // progressDialog.dismiss();
            // dismissProgressDialog();
            if (response.contains("")) {

            }

            getCountrylist();


        }

    }

    class DownloadCityJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
             showprogress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getCityMaster;

            try {
                res = ut.OpenConnection(url,VendorRegistrationForm.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_City_INV, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_City_INV, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_City_INV, null, values);

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
             hidprogress();
            // progressDialog.dismiss();
            // dismissProgressDialog();
            if (response.contains("")) {

            }

            getCitylist();


        }

    }

    public void getCountrylist() {
        lstcountrylist.clear();
        String query = "SELECT distinct PKCountryId,CountryName" +
                " FROM " + db.TABLE_Country_INVEN;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstcountrylist.add(cur.getString(cur.getColumnIndex("CountryName")));

            } while (cur.moveToNext());

        }

         MySpinnerAdapter customDept = new MySpinnerAdapter(VendorRegistrationForm.this,
                R.layout.vwb_custom_spinner_txt, lstcountrylist);
         spinner_country.setAdapter(customDept);
         spinner_country.setSelection(0);
    }

    public void getCitylist() {
        lstCitylist.clear();
        String query = "SELECT distinct PKCityID,CityName" +
                " FROM " + db.TABLE_City_INV;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");

        lstCitylist.add("Select City");

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstCitylist.add(cur.getString(cur.getColumnIndex("CityName")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(VendorRegistrationForm.this,
                R.layout.vwb_custom_spinner_txt, lstCitylist);
        spinner_city.setAdapter(customDept);
        spinner_city.setSelection(0);
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

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:


        private MySpinnerAdapter(Context context, int resource,
                                 List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView,
                    parent);
            //view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);
            //  view.setTypeface(font);
            return view;
        }

    }



    class DownloadStateData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            showprogress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getState + "?id=1";

            try {
                res = ut.OpenConnection(url,VendorRegistrationForm.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_STATE, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_STATE, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_STATE, null, values);

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
            hidprogress();
            // progressDialog.dismiss();
            // dismissProgressDialog();
            if (response.contains("")) {

            }

            getStatelist();


        }

    }


    class DownloadCurrencyData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
             showprogress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getCurrency;

            try {
                res = ut.OpenConnection(url,VendorRegistrationForm.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_Currency, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Currency, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_Currency, null, values);

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
             hidprogress();
            // progressDialog.dismiss();
            // dismissProgressDialog();
            if (response.contains("")) {

            }

            getCurrencylist();


        }

    }


    class DownloadEntityTypeData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
             showprogress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getPartyType;

            try {
                res = ut.OpenConnection(url,VendorRegistrationForm.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_EntityType, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_EntityType, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_EntityType, null, values);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
             hidprogress();
            // progressDialog.dismiss();
            // dismissProgressDialog();
            if (response.contains("")) {

            }

            getEntityTypelist();


        }

    }



    public void getStatelist() {
        lstStatelist.clear();
        String query = "SELECT distinct PKStateId,StateDesc" +
                " FROM " + db.TABLE_STATE;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        lstStatelist.add("Select State");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstStatelist.add(cur.getString(cur.getColumnIndex("StateDesc")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(VendorRegistrationForm.this,
                R.layout.vwb_custom_spinner_txt, lstStatelist);
        spinner_state.setAdapter(customDept);
        spinner_state.setSelection(0);
    }
    public void getCurrencylist() {
        lstCurrencylist.clear();
        String query = "SELECT distinct CurrencyMasterId,CurrCode" +
                " FROM " + db.TABLE_Currency;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstCurrencylist.add(cur.getString(cur.getColumnIndex("CurrCode")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(VendorRegistrationForm.this,
                R.layout.vwb_custom_spinner_txt, lstCurrencylist);
        spinner_currency.setAdapter(customDept);
        spinner_currency.setSelection(0);
    }
    public void getEntityTypelist() {
        lstEntitypelist.clear();
        String query = "SELECT distinct EntityTypeMasterId,EntityType" +
                " FROM " + db.TABLE_EntityType;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstEntitypelist.add(cur.getString(cur.getColumnIndex("EntityType")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(VendorRegistrationForm.this,
                R.layout.vwb_custom_spinner_txt, lstEntitypelist);
        spinner_entitytype.setAdapter(customDept);
        spinner_entitytype.setSelection(0);
    }
    void showprogress() {
        mprogress.setVisibility(View.VISIBLE);

    }

    void hidprogress() {
        mprogress.setVisibility(View.GONE);

    }

    class DownloadIFSCcodeData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            showprogress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = "https://ifsc.razorpay.com/"+IFSCCode;

            try {
                res = ut.OpenConnection(url,VendorRegistrationForm.this);
                if (res != null) {
                    response = res.toString();
                   /* response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);*/
                    JSONObject jorder = new JSONObject(response);

                     Bankaddress=jorder.getString("ADDRESS");
                     Bankname=jorder.getString("BANK");
                     Branch=jorder.getString("BRANCH");



                    }
                } catch (JSONException e1) {
                e1.printStackTrace();
                VendorRegistrationForm.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(VendorRegistrationForm.this,"Invalid IFSC code",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();


                edt_bankaddress.setText(Bankaddress);
                edt_bankname.setText(Bankname);
                edt_branchname.setText(Branch);


        }

    }

    class DownloadDistrictData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            showprogress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getDistrict + "?id="+StateId;

            try {
                res = ut.OpenConnection(url,VendorRegistrationForm.this);
                if (res != null) {
                    response = res.toString();

                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    commonDistrictDataArrayList.clear();
                    commonDistrictDataArrayList1.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        CommonData commonData=new CommonData();
                        JSONObject jorder = jResults.getJSONObject(i);

                        commonData.setFilename(jorder.getString("DistrictDesc"));
                        commonData.setId(jorder.getString("PKDistrictId"));
                        commonDistrictDataArrayList.add(commonData);
                        String point = jorder.getString("DistrictDesc");
                        commonDistrictDataArrayList1.add(point);





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
            hidprogress();
            // progressDialog.dismiss();
            // dismissProgressDialog();
            if (response.contains("[]")) {
                hidprogress();
                Toast.makeText(VendorRegistrationForm.this,"District not found", Toast.LENGTH_SHORT).show();
            }else {
                /*commonSpinnerAdapter= new CustomAdapter(VendorRegistrationForm.this,commonDistrictDataArrayList);
                spinner_district.setAdapter(commonSpinnerAdapter);
*/
                spinner_district.setAdapter(new ArrayAdapter<String>(VendorRegistrationForm.this,
                        R.layout.vwb_custom_spinner_txt,
                        commonDistrictDataArrayList1));


                spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        DistrictId=commonDistrictDataArrayList.get(i).getId();


                        if (isnet()) {
                            new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadTalukaData().execute(DistrictId);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }




                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }



        }

    }
    class DownloadTalukaData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            showprogress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getTaluka + "?id="+DistrictId;

            try {
                res = ut.OpenConnection(url,VendorRegistrationForm.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    commonTalukaDataArrayList.clear();
                    commonTalukaDataArrayList1.clear();

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        CommonData commonData=new CommonData();
                        JSONObject jorder = jResults.getJSONObject(i);

                        commonData.setFilename(jorder.getString("TalukaDesc"));
                        commonData.setId(jorder.getString("PKTalukaId"));
                        commonTalukaDataArrayList.add(commonData);
                        String point = jorder.getString("TalukaDesc");
                        commonTalukaDataArrayList1.add(point);



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
            hidprogress();
            // progressDialog.dismiss();
            // dismissProgressDialog();
            if (response.equals("[]")) {
                hidprogress();
                Toast.makeText(VendorRegistrationForm.this,"No data found", Toast.LENGTH_SHORT).show();
            }else {
                spinner_taluka.setAdapter(new ArrayAdapter<String>(VendorRegistrationForm.this,
                        R.layout.vwb_custom_spinner_txt,
                        commonTalukaDataArrayList1));
            }



        }

    }
    public boolean validate1() {
        // TODO Auto-generated method stub

        if ((edt_companyname.getText().toString().equalsIgnoreCase("") ||
                edt_companyname.getText().toString().equalsIgnoreCase(" ") ||
                edt_companyname.getText().toString().equalsIgnoreCase(null))) {
            Toast.makeText(VendorRegistrationForm.this, "Please enter company name", Toast.LENGTH_LONG).show();
            return false;
        }
        else if ((edt_pincode.getText().toString().equalsIgnoreCase("") ||
                edt_pincode.getText().toString().length()!= 6 ||
                edt_pincode.getText().toString().equalsIgnoreCase(null))) {
            Toast.makeText(VendorRegistrationForm.this, "Pincode should be 6 characters", Toast.LENGTH_LONG).show();
            return false;

        }
        else if ((edt_panno.getText().toString().equalsIgnoreCase("") ||
                edt_panno.getText().toString().length()!= 10 ||
                edt_panno.getText().toString().equalsIgnoreCase(null))) {
            Toast.makeText(VendorRegistrationForm.this, "Please enter pan number", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }

    class Postvendordata extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showprogress();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_PostData;
            try {
                res = ut.OpenPostConnection(url, params[0],VendorRegistrationForm.this);
                if (res!=null) {
                    response = res.toString();
                    response = response.substring(1, response.length() - 1);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();


            //  progressDialog.dismiss();

            if (integer==null){

            }else {
                if (integer.contains("true")) {

                    Toast.makeText(VendorRegistrationForm.this, "Vendor register successfully", Toast.LENGTH_LONG).show();

                    // onBackPressed();
                    String IndentId = integer;
                    String[] data = IndentId.split(",");
                    String res = data[0];
                    //DocMthCode = "IndAppr";
                    Id = data[1];

                    if (Imagefilename == null) {
                        onBackPressed();
                    }


                    if (Imagefilename != null) {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray Idjsonarray = new JSONArray();
                        try {
                            jsonObject.put("fileName", jsonArray);
                            jsonObject.put("ActivityId", Id);


                            Vendordata = jsonObject.toString();
                            Vendordata = Vendordata.replaceAll("\\\\", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (isnet()) {

                            new StartSession(VendorRegistrationForm.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new SaveAttachment().execute(Vendordata);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }


                            });

                        }
                    }
                    //onBackPressed();

                } else if (integer.equals("Duplicate Emaid Id")) {
                    Toast.makeText(VendorRegistrationForm.this, "Duplicate email id", Toast.LENGTH_LONG).show();

                } else if (integer.equals("Duplicate Mobile No")) {
                    Toast.makeText(VendorRegistrationForm.this, "Duplicate mobile number", Toast.LENGTH_LONG).show();


                }
            }
        }

    }
    public void requestRuntimePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(VendorRegistrationForm.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(VendorRegistrationForm.this,
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
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
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
    public Uri getOutputMediaFileUri(int type) {
        requestRuntimePermission();
        return Uri.fromFile(getOutputMediaFile(type));
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

    public class PostUploadImageMethod extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();

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
                // String upLoadServerUri="http://192.168.1.53/api/ChatRoomAPI/UploadFiles";

                String upLoadServerUri =CompanyURL + WebUrlClass.api_FileUpload;

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
                int  serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();



                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseMessage.equals("OK")) {
//

                    //
                    VendorRegistrationForm.this.runOnUiThread(new Runnable() {
                        public void run() {
                            hidprogress();
                            Toast.makeText(VendorRegistrationForm.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            attachment_name.setText(Imagefilename);

                           // jsonArray.put(Imagefilename);
                            try {
                                jsonimage.put("File",Imagefilename);
                                jsonArray.put(jsonimage);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });




                } else  {

                    if (serverResponseMessage.contains("Error")) {
                        VendorRegistrationForm.this.runOnUiThread(new Runnable() {
                            public void run() {
                                hidprogress();
                                Toast.makeText(VendorRegistrationForm.this, "Server Error", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedFileUri = data.getData();
                path = FilePath.getPath(this,selectedFileUri);
                File f = new File(FilePath.getPath(this,selectedFileUri));
                Imagefilename=f.getName();

                //File originalFile = new File(path);
                try {
                    FileInputStream fileInputStreamReader = new FileInputStream(f);
                    byte[] bytes = new byte[(int) f.length()];
                    fileInputStreamReader.read(bytes);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // image_encode=getStringFile(f);



                if (isnet()) {
                    PostUploadImageMethod postUploadImageMethod = new PostUploadImageMethod();
                    postUploadImageMethod.execute();
                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        }else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                File f = new File(fileUri.getPath().toString());
                path = f.toString();
                Imagefilename = f.getName();
               // new VideoCompressor().execute();


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            }
        }else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                    File f = new File(fileUri.getPath().toString());
                    path = f.toString();
                    Imagefilename = f.getName();

                    if (isnet()) {
                        async = new PostUploadImageMethod().execute();

                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {

            } else {

            }
        }
    }

    class SaveAttachment extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();

            if (response!=null) {
                if (response.equalsIgnoreCase("true")) {
                    Toast.makeText(VendorRegistrationForm.this, "Attachment save successfully", Toast.LENGTH_SHORT).show();
               onBackPressed();
                }else {
                    Toast.makeText(VendorRegistrationForm.this, "Please try again", Toast.LENGTH_SHORT).show();

                }
            }else {
                Toast.makeText(VendorRegistrationForm.this, "Please try again", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_PostSaveAttachment;

            //url="http://192.168.1.53/api/TicketRegisterAPI/PostSaveAttachment";
            try {
                res = ut.OpenPostConnection(url, params[0],VendorRegistrationForm.this);
                response = res.toString();
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
