package com.vritti.vwb.vworkbench;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.SaharaModule.SaharaBeans.AttachmentBean;
import com.vritti.chat.bean.ChatUser;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.DatasheetQueListAdapter;
import com.vritti.vwb.Adapter.Sahara_AttachmentDetailsAdapter;
import com.vritti.vwb.Beans.Datasheet;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.vwb.Beans.DatasheetListObject;
import com.vritti.vwb.Beans.SaharaAppr;
import com.vritti.vwb.classes.CommonFunction;

import static com.vritti.ekatm.services.DownloadJobService.FASTEST_INTERVAL;
import static com.vritti.ekatm.services.DownloadJobService.MIN_DISTANCE_CHANGE_FOR_UPDATES;
import static com.vritti.ekatm.services.DownloadJobService.MIN_TIME_BW_UPDATES;

/**
 * Created by 300151 on 12/5/2016.
 */
public class AddDatasheetActivityMain extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    String DateToStr = "";
    ArrayList<String> apprv_List;
    ArrayList<ChatUser> chatUserArrayList;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    SQLiteDatabase sql;
    String FormId, FKQuesId, SourceId = "", ActivityId, ActivityName, HeaderId = "", UseForProspect, Result, PKSuspectId, Mode = "A",
            assignNew = "";
    SharedPreferences userpreferences;
    Button btn_save, btn_return;
    LinearLayout layBottom;
    ListView lv_queList;
    JSONArray DatasheetAnsDataFinal;
    JSONObject DatasheetAnsData;
    JSONObject DatasheetFinalobj, ArrDocumentReview;
    JSONObject approvalJsonData;
    String finalApprObj;
    String FinalObj;
    public static ArrayList<Datasheet> datasheetlists = new ArrayList<Datasheet>();
    private String remark;
    Button btn_SaveDraft;
    int mandatoryNotAns=-1;

    TextView mLevel, txt_gpsdetails, txt_gps_date, txt_nofound;
    SearchableSpinner sp_appr;
    Button btn_ok, btn_cancel;
    int nextAppr = 0;
    String UserMasterIDnextAppr = "", reassignToName = "";
    ArrayList<SaharaAppr> approval_list;
    SaharaAppr saharaAppr;
    String issuedTo = "";
    HashMap<Integer, String> staticValue_List = new HashMap<>();
    ArrayList<String> valueList;
    ArrayList<Integer> postnList;
    int selectedPos = -1;
    int adapterPos = -1;
    DatasheetQueListAdapter dataAdapter;
    //   ProgressBar center_progressbar;

    Dialog dialog1;
    RecyclerView ls_attachname;
    Sahara_AttachmentDetailsAdapter sahara_attachmentDetailsAdapter;
    public static ArrayList<AttachmentBean> attachmentList = new ArrayList<>();
    public static final int progress_bar_type = 0;
    private final int MEGABYTE = 1024 * 1024;
    private ProgressDialog pDialog;
    //  public static int progress_bar_type = 0;
    BottomSheetDialog dialog;
    ProgressBar progressBar1;
    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    public static String LocationName = "";
    String result = "";

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    LocationRequest mLocationRequest;
    private TextView txt_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_activity_add_datasheet_main);
        InitView();
        SetListner();

        context = getApplicationContext();
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

        dialog1 = new Dialog(this);

        getCurrentLocationNew();

        chatUserArrayList = new ArrayList<>();
        apprv_List = new ArrayList<>();

        if (getIntent().hasExtra("FormId")) {
            Intent i = getIntent();
            FormId = i.getStringExtra("FormId");

            if (EnvMasterId.equalsIgnoreCase("pragati")
                    || EnvMasterId.equalsIgnoreCase("b207")) {
                ActivityId = i.getStringExtra("ActivityId");
                SourceId = i.getStringExtra("PkCssHeaderID");

            } else {
                ActivityId = i.getStringExtra("ActivityId");
                SourceId = i.getStringExtra("SourceId");
            }
            ActivityName = i.getStringExtra("ActivityName");

            txt_title.setText(ActivityName);
            UseForProspect = i.getStringExtra("UseForProspect");
            Result = i.getStringExtra("Result");
            PKSuspectId = i.getStringExtra("PKSuspectId");
            Mode = i.getStringExtra("Mode");
            assignNew = i.getStringExtra("AssignNew");

        }

       /* if (cf.check_DatasheetQueList(FormId) > 0) {
            UpdateDatasheet();} else {*/

        if (ut.isNet(context)) {

            new StartSession(AddDatasheetActivityMain.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadChatUSerDataJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);

                }
            });
        } else {
            Toast.makeText(AddDatasheetActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        if (ut.isNet(context)) {
            new StartSession(AddDatasheetActivityMain.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadDatasheetGetData().execute();

                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                }
            });
        } else {
            Toast.makeText(AddDatasheetActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void getDatasheetJSONObj() {

getCurrentLocationNew();

        for(int k=0;k<datasheetlists.size();k++){
            String ans="";
            if(datasheetlists.get(k).getIsResponseMandatory().equals("Y")){
                if(datasheetlists.get(k).getAnswer() == null || datasheetlists.get(k).getAnswer().equals("")){
                    String msg = "Please Select Answer For "+(k+1)+" Question";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    mandatoryNotAns = 1;
                    break;
                }else{
                    mandatoryNotAns = -1;
                }
            }

        }
        if(mandatoryNotAns == -1) {
            try {
                for (int i = 0; i < datasheetlists.size(); i++) {
                    DatasheetAnsData = new JSONObject();

                    //json.put("jsonValue",URLEncoder.encode(jsonValue, "utf-8"));
                    String Questiontext = datasheetlists.get(i).getQuesText();
                    String ansText = datasheetlists.get(i).getAnswer();


                    // DatasheetAnsData.put("QuesText", URLEncoder.encode(Questiontext, "utf-8"));
                    DatasheetAnsData.put("QuesText", Questiontext);

                    DatasheetAnsData.put("Remarks", " ");
                    if (ansText != null) {
                        DatasheetAnsData.put("ResponseByCustomer", datasheetlists.get(i).getAnswer());
                    } else {
                        DatasheetAnsData.put("ResponseByCustomer", "");
                    }
                    if (SourceId == null) {
                        DatasheetAnsData.put("FKCssHeaderId", "");
                    } else {
                        DatasheetAnsData.put("FKCssHeaderId", SourceId);
                    }
                    DatasheetAnsData.put("PKCssFormsQuesID", datasheetlists.get(i).getPKCssFormsQuesID());
                    if (datasheetlists.get(i).getResponseType().equalsIgnoreCase("Text")) {
                        DatasheetAnsData.put("Flag", "T");
                    } else if (datasheetlists.get(i).getResponseType().equalsIgnoreCase("Selection")) {
                        DatasheetAnsData.put("Flag", "R");
                    } else if (datasheetlists.get(i).getResponseType().equalsIgnoreCase("Numeric")) {
                        DatasheetAnsData.put("Flag", "T");
                    } else {

                    }
                    DatasheetAnsData.put("FKQuesId", datasheetlists.get(i).getFKQuesId());
                    DatasheetAnsData.put("PKCssDtlsID", datasheetlists.get(i).getDetailid());
                    DatasheetAnsDataFinal.put(DatasheetAnsData);


                }
                DatasheetFinalobj.put("FormId", FormId);
                DatasheetFinalobj.put("Mode", "A");
                DatasheetFinalobj.put("DatasheetAnsDataFinal", DatasheetAnsDataFinal);

                if (EnvMasterId.equalsIgnoreCase("pragati") ||
                        EnvMasterId.equalsIgnoreCase("b207")) {
                    // DatasheetFinalobj.put("ActivityId", ActivityId);

                    DatasheetFinalobj.put("ActivityId", ActivityId);

                } else {
                    DatasheetFinalobj.put("ActivityId", ActivityId);
                }
                // DatasheetFinalobj.put("ActivityId", ActivityId);
                if (EnvMasterId.equalsIgnoreCase("pragati")
                        || EnvMasterId.equalsIgnoreCase("b207")) {

                    DatasheetFinalobj.put("ActivityId", ActivityId);

                } else {
                    DatasheetFinalobj.put("ActivityId", ActivityId);
                }
                // DatasheetFinalobj.put("ActivityId", ActivityId);
                if (EnvMasterId.equalsIgnoreCase("pragati")
                        || EnvMasterId.equalsIgnoreCase("Pragati")) {

                    DatasheetFinalobj.put("Module", "Konnect");
                } else {
                    DatasheetFinalobj.put("Module", "VWB");
                }
                DatasheetFinalobj.put("CallId", " ");
                DatasheetFinalobj.put("FormDesc", ActivityName);
                if (assignNew.equalsIgnoreCase("yes")) {
                    DatasheetFinalobj.put("FlagSaveAction", "0");
                } else {
                    DatasheetFinalobj.put("FlagSaveAction", "2");
                }


                JSONArray jsonArray1 = new JSONArray();

                for (int j = 0; j < datasheetlists.size(); j++) {
                    ArrDocumentReview = new JSONObject();

                    if (datasheetlists.get(j).isAns()) {

                        String ansText = datasheetlists.get(j).getAnswer();
                        if (ansText != null) {
                            ArrDocumentReview.put("Remark", datasheetlists.get(j).getAnswer());
                        } else {
                            ArrDocumentReview.put("Remark", "");

                        }
                        ArrDocumentReview.put("PKCSSDtlsId", datasheetlists.get(j).getDetailid());
                        if (SourceId == null) {
                            ArrDocumentReview.put("FKCssHeaderId", "");
                        } else {
                            ArrDocumentReview.put("FKCssHeaderId", SourceId);
                        }

                        ArrDocumentReview.put("UserMasterId", UserMasterId);
                        ArrDocumentReview.put("ApprStatus", "");
                        ArrDocumentReview.put("Assigned_Count", "");
                        jsonArray1.put(ArrDocumentReview);
                        DatasheetFinalobj.put("ArrDocumentReview", jsonArray1);

                    } else {
                        DatasheetFinalobj.put("ArrDocumentReview", jsonArray1);
                    }
                    DatasheetFinalobj.put("Latitude", latitude);
                    DatasheetFinalobj.put("Longitude", longitude);
                    DatasheetFinalobj.put("LocationName", LocationName);




                }

                FinalObj = DatasheetFinalobj.toString();
                FinalObj = FinalObj.replaceAll("\\\\", "");

                String status = "";
                String url = CompanyURL + WebUrlClass.api_save_datasheet;
                String op = "Success-Data Save Successfully";

                remark = "Datasheet save successfully for- "+ActivityName;
                CreateOfflinedatasheetfill_Appr(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);


          /*  if (ut.isNet(context)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                //finish();
            } else {*/
                if (Constants.type == Constants.Type.Sahara || Constants.type == Constants.Type.ZP) {


                    if (ut.isNet(context)) {
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadHierarchyCount().execute(SourceId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                } else {



                    onBackPressed();
                    // finish();
                  /*  if (ut.isNet(context)) {
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new UploadDatasheet().execute(FinalObj);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }*/
                }
                // }

                //Check for the hierachy count


                //center_progressbar.setVisibility(View.GONE);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getCurrentLocationNew() {
        if(isGooglePlayServicesAvailable()){
            getLocationPlayservice();
        }
    }

    private void getLocationPlayservice() {
        Context context = this;
        locationManager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (mGoogleApiClient == null) {
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(this, this, (GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
            Boolean data = mGoogleApiClient.isConnected();
            if (data) {
                if (mGoogleApiClient.isConnected()) {
                    try {

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                        if (mCurrentLocation != null) {
                            double lat = mCurrentLocation.getLatitude(),
                                    lon = mCurrentLocation.getLongitude();
                            latitude = lat;
                            longitude = lon;
                            canGetLocation = true;

                            Log.e("token ", "Lat :" + lat + " Long" + lon);
                            GetCurrentLocation(lat, lon);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Boolean data = mGoogleApiClient.isConnected();
            if (mGoogleApiClient.isConnected()) {
                try {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    if (mCurrentLocation != null) {
                        double lat = mCurrentLocation.getLatitude(),
                                lon = mCurrentLocation.getLongitude();
                        latitude = lat;
                        longitude = lon;
                        canGetLocation = true;

                        Log.e("token ", "Lat :" + lat + " Long" + lon);
                        GetCurrentLocation(lat, lon);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MIN_TIME_BW_UPDATES);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//PRIORITY_BALANCED_POWER_ACCURACY
        mLocationRequest.setSmallestDisplacement(MIN_DISTANCE_CHANGE_FOR_UPDATES);
    }


    public void attachmentDetailsShow(int pos) {
        final String pkCssDtldID = datasheetlists.get(pos).getDetailid();

        if (datasheetlists.get(pos).getAttachmentCount() == null || datasheetlists.get(pos).getAttachmentCount().equals("0")) {
            Toast.makeText(AddDatasheetActivityMain.this, "Sorry there is not attachment here", Toast.LENGTH_SHORT).show();
        } else {
            attachmentDetailsDialog(pos);

        }


    }


    private void attachmentDetailsDialog(int selectedPos) {

        dialog1.setContentView(R.layout.sahara_dialog_attachments);
        ls_attachname = dialog1.findViewById(R.id.ls_attachname);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ls_attachname.setLayoutManager(layoutManager);
        sahara_attachmentDetailsAdapter = new Sahara_AttachmentDetailsAdapter(AddDatasheetActivityMain.this,
                datasheetlists.get(selectedPos).getFilePathName(), datasheetlists,
                false, selectedPos);
        ls_attachname.setAdapter(sahara_attachmentDetailsAdapter);
        dialog1.show();
        dialog1.setCanceledOnTouchOutside(true);
    }

    public void deletefileName(int pos, int adapterpos) {
        selectedPos = pos;
        adapterPos = adapterpos;

        String que = datasheetlists.get(pos).getQuesText();


        //datasheetArrayList.remove(d)

        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.confim_ui);

        dialog.show();


    }


    public void viewFile(int listPos, boolean isDownload, int adapterPosition) {
        selectedPos = listPos;
        adapterPos = adapterPosition;
        if (isDownload) {
            ArrayList<String> arrayList = new ArrayList<>();

            if (datasheetlists.get(selectedPos).getFilePathName().size() > 0) {
                for (int i = 0; i < datasheetlists.get(selectedPos).getFilePathName().size(); i++) {
                    String attachmentName1 = datasheetlists.get(selectedPos).getFilePathName().get(i);
                    arrayList.add(attachmentName1);
                }
            }

            String viewFile = arrayList.get(adapterPos);


            if (ut.isNet(context)) {
                File file = new File(viewFile);
                String path = file.getPath().replace("[", "").replace("]", "");
                MimeTypeMap map = MimeTypeMap.getSingleton();
                String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName().replace("]", ""));
               /* if(ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png") ||
                        ext.equalsIgnoreCase("jpg")){
                    Intent intent = new Intent(context, ImageFullScreenActivity.class);
                    intent.putExtra("share_image_path",path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else  {*/
                openDocument(path);
                //  }



               /* String path1 = Environment.getExternalStorageDirectory()
                        .toString();
                File file = new File(path1 + "/" + "Sahara" + "/" + "File");
                if (file.exists()) {
                    final File fileNew = new File(path1 + "/" + attachmentName1);
                    if (fileNew.exists()) {
                        Handler handler = new Handler(context.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {

                               *//* urlGetMimeType(fileNew.getAbsolutePath());
                                Toast.makeText(context, "File Already downloaded", Toast.LENGTH_SHORT).show();
                                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                String mimeType = myMime.getMimeTypeFromExtension(fileNew.getAbsolutePath());
                                newIntent.setDataAndType(Uri.fromFile(fileNew), mimeType);
                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                try {
                                    context.startActivity(newIntent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                                }*//*
                                //((NotificationActivity)context).sendResult(directory.getName());


                            }
                        });
                    } else {
                        cllDownloadApi(path, attachmentName1);
                    }
                } else {
                    cllDownloadApi(path, attachmentName1);
                }*/
            }

        } else {

            dialog = new BottomSheetDialog(this);
            dialog.setContentView(R.layout.confim_ui);

            dialog.show();

          /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to delete?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create();*/

            // Toast.makeText(context, "Under development !!!..", Toast.LENGTH_SHORT).show();
        }
    }


    private void openDocument(String attachment) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(attachment);
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (extension.equalsIgnoreCase("") || mimetype == null) {
            // if there is no extension or there is no definite mimetype, still try to open the file
            intent.setDataAndType(Uri.fromFile(file), "text/*");
        } else {
            intent.setDataAndType(Uri.fromFile(file), mimetype);
        }
        // custom message for the intent
        context.startActivity(Intent.createChooser(intent, "Choose an Application:"));
    }

    private void urlGetMimeType(String path) {
        File file = new File(path);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null)
            type = "*/*";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);

        intent.setDataAndType(data, type);

        startActivity(intent);
    }


    private void cllDownloadApi(final String path, final String attachmentName1) {
        new StartSession(context, new CallbackInterface() {
            @Override
            public void callMethod() {
                new DownloadFileApi().execute(path, attachmentName1);
            }

            @Override
            public void callfailMethod(String msg) {
                // ((NotificationActivity)context).showPhhopUp(false);
            }
        });
    }




    private class DownloadFileApi extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... strings) {

            String pathName = strings[0];
            final String fileName = strings[1];
            int count;
            File file = null;
            String urlStr = CompanyURL + "/Downloads/" + EnvMasterId + "/" + fileName;
            try {
                final String path1 = Environment.getExternalStorageDirectory()
                        .toString();

                if (Constants.type == Constants.Type.Sahara) {
                    file = new File(path1 + "/" + "Sahara" + "/" + "File");

                } else if (Constants.type == Constants.Type.ZP) {
                    file = new File(path1 + "/" + "ZP" + "/" + "File");
                } else {
                    file = new File(path1 + "/" + "Ekatm" + "/" + "File");
                }
                if (!file.exists())
                    file.mkdirs();
                //   pdfFile = new File(file + "/" + fileName);
                // file1 = String.valueOf(pdfFile);


                try {
                    //pdfFile = File.createTempFile(filename /* prefix */,prefix, pdfFile /* directory */);

                    final File fileNew = new File(file + "/" + fileName);
                    fileNew.createNewFile();
                    //downloadFileInloacl(url , new File(file + "/" + fileNew));

                    try {
                        urlStr = urlStr.replaceAll(" ", "%20");
                        //final File directory =  new File(file + "/" + fileNew);
                        URL url = new URL(urlStr);

                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.connect();
                        int lenghtOfFile = urlConnection.getContentLength();
                        long total = 0;


                        FileOutputStream fileOutputStream = new FileOutputStream(fileNew);
                        InputStream inputStream = urlConnection.getInputStream();
                        int totalSize = urlConnection.getContentLength();
                        int serverResponseCode = urlConnection.getResponseCode();
                        String serverResponseMessage = urlConnection.getResponseMessage();
                        byte[] buffer = new byte[MEGABYTE];
                        int bufferLength = 0;
                        while ((bufferLength = inputStream.read(buffer)) > 0) {
                            total += bufferLength;
                            fileOutputStream.write(buffer, 0, bufferLength);
                            onProgressUpdate("" + (int) ((total * 100) / lenghtOfFile));
                        }
                        fileOutputStream.close();
                        Handler handler = new Handler(context.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                urlGetMimeType(fileNew.getAbsolutePath());
                              /*  MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                String mimeType = myMime.getMimeTypeFromExtension(fileNew.getAbsolutePath());
                                newIntent.setDataAndType(Uri.fromFile(fileNew), mimeType);
                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                try {
                                    context.startActivity(newIntent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                                }*/

                            }
                        });


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //((NotificationActivity)context).showPopUp(false);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        // ((NotificationActivity)context).showPopUp(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        //((NotificationActivity)context).showPopUp(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //((NotificationActivity)context).showPopUp(false);
                    }
                    //publishProgress(""+(int)((total*100)/lenghtOfFile));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // ((NotificationActivity)context).showPopUp(false);
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissDialog(progress_bar_type);
        }
    }

    public void deleteAttachment(View view) {
        if (selectedPos != -1) {
            String que = datasheetlists.get(selectedPos).getQuesText();
            ArrayList<String> arrayList = new ArrayList<>();

            if (datasheetlists.get(selectedPos).getFilePathName().size() > 0) {
                for (int j = 0; j < datasheetlists.get(selectedPos).getFilePathName().size(); j++) {
                    String fileName = datasheetlists.get(selectedPos).getFilePathName().get(j).toString();
                    // fileName = fileName.replace("[","").replace("]","");
                    arrayList.add(fileName);
                }
                arrayList.remove(adapterPos);
                datasheetlists.get(selectedPos).setFilePathName(arrayList);
                dataAdapter.deleteChange(datasheetlists);
            }

            dialog1.dismiss();
            dialog.dismiss();
        }

    }

    public void cancelAttatchment(View view) {
        dialog.dismiss();

    }

    public class DownloadChangeStatus extends AsyncTask<String, Void, String> {

        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // center_progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String status = params[0];

            try {
                String url = CompanyURL + WebUrlClass.api_change_activity_status_Sahara + "?Status=" + status + "&ActivityId=" + ActivityId;
                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                  /*  res = res.substring(1, res.length() - 1);
                    res = res.replaceAll("\\\\", "");*/

                }
                String s = res;
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }


            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // center_progressbar.setVisibility(View.GONE);

            if (res == "") {

            } else {

            }


        }
    }

 /*   public class DownloadChangeStatus extends AsyncTask<String, Void, String> {

        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // center_progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String headerId = params[0];

            try {
                String url = CompanyURL + WebUrlClass.api_change_activity_status_Sahara + "?Status=" + headerId;
                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                  *//*  res = res.substring(1, res.length() - 1);
                    res = res.replaceAll("\\\\", "");*//*

                }
                String s = res;
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }


            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // center_progressbar.setVisibility(View.GONE);

            if (res.contains("PKCssDtlsID")) {

                //call of BEO
                // /api/DatasheetEntryAPI/GetLastLevel

            } else {


                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new getReportingTo().execute();

                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }


        }
    }*/

    public class DownloadHierarchyCount extends AsyncTask<String, Void, String> {

        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // center_progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String headerId = params[0];

            try {
                String url = CompanyURL + WebUrlClass.api_HierarchyCount_Sahara + "?HeaderId=" + headerId;
                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                  /*  res = res.substring(1, res.length() - 1);
                    res = res.replaceAll("\\\\", "");*/

                }
                String s = res;
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }


            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // center_progressbar.setVisibility(View.GONE);

            if (res.contains("PKCssDtlsID")) {

                //call of BEO
                // /api/DatasheetEntryAPI/GetLastLevel

            } else {


                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new getReportingTo().execute();

                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }

              /*  if(ut.isNet(context)){
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {


                          //  new reassignActivity().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }*/


            }


        }
    }

    public class getReportingTo extends AsyncTask<String, Void, String> {

        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_getReportingTo_sahara;
                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                    res = res.substring(1, res.length() - 1);
                    res = res.replaceAll("\\\\", "");
                    //json_Resp = new JSONArray(res);

                }
                String s = res;
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }


            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    issuedTo = jsonObject.getString("reportingto");

                    if (issuedTo.equalsIgnoreCase("")) {
                        Toast.makeText(AddDatasheetActivityMain.this, "Datasheet save successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {

                        if (ut.isNet(context)) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new reassignActivity().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        }


                    }

                } catch (Exception e) {
                    e.getMessage();

                }


            }
        }
    }

    private void ChangeApproverDialog_complete() {
        final String[] UserName = new String[1];
        final Dialog dialog1 = new Dialog(AddDatasheetActivityMain.this);
        dialog1.setContentView(R.layout.vwb_dialog_next_approver);
        mLevel = dialog1.findViewById(R.id.nextappr);
        sp_appr = (SearchableSpinner) dialog1.findViewById(R.id.sp_nxtAppr);
        btn_ok = (Button) dialog1.findViewById(R.id.btn_ok);
        btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);

        getAppr_complete();
        dialog1.setCancelable(false);
        dialog1.show();
        mLevel.setText(nextAppr + "");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPostReassignDataJson();

                dialog1.dismiss();
            }
        });

        sp_appr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserName[0] = parent.getItemAtPosition(position).toString();
                String query = "SELECT * FROM " + db.TABLE_NEXTAPPR + " WHERE UserName='" + UserName[0] + "'";
                SQLiteDatabase sql = db.getWritableDatabase();
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    UserMasterIDnextAppr = cur.getString(cur.getColumnIndex("UserMasterId"));
                    reassignToName = cur.getString(cur.getColumnIndex("UserName"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void getAppr_complete() {
        SQLiteDatabase sql = db.getWritableDatabase();
        //ArrayList<String> apprv_List = new ArrayList<>();
       /* String query = "SELECT * FROM " + db.TABLE_CHAT_USER_LIST + " WHERE UserMasterId="+ issuedTo;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                apprv_List.add(cur.getString(cur.getColumnIndex("UserName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(AddDatasheetActivityMain.this, android.R.layout.simple_spinner_item, apprv_List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_appr.setAdapter(adapter);

        }
*/
        int pos = -1;

        for (int i = 0; i < chatUserArrayList.size(); i++) {
            if (issuedTo.equalsIgnoreCase(chatUserArrayList.get(i).getUserMasterId())) {
                pos = i;
            }
        }

        if (pos != -1) {
            apprv_List.add(chatUserArrayList.get(pos).getUsername());

        } else {

        }

        ArrayAdapter<String> adapter = new ArrayAdapter(AddDatasheetActivityMain.this, android.R.layout.simple_spinner_item, apprv_List);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_appr.setAdapter(adapter);
    }

    class DownloadChatUSerDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetUserList + "?UserName=" + "";

            try {
                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
               /* sql.delete(db.TABLE_CHAT_USER_LIST, null,
                        null);*/
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CHAT_USER_LIST, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        String Id = jorder.getString("UserMasterId");
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }

                        long a = sql.insert(db.TABLE_CHAT_USER_LIST, null, values);
                        Log.e("cnt", "" + a);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            /*if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
            if (response != null) {
                ChatUserUpdate();
                // getAppr_complete();

            }


        }

    }

    private void ChatUserUpdate() {
        chatUserArrayList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_CHAT_USER_LIST + " ORDER BY UserName ASC";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ChatUser chatUser = new ChatUser();
                chatUser.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                chatUser.setUsername(cur.getString(cur.getColumnIndex("UserName")));
                chatUserArrayList.add(chatUser);
            } while (cur.moveToNext());


            /*chatUserlistAdapter = new AddUserAdapterForGroup(AddGroupActivity.this, chatUserArrayList);
            listview_user.setAdapter(chatUserlistAdapter);*/
        }
    }

    public class reassignActivity extends AsyncTask<String, Void, String> {

        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // center_progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_saveApproval_Reassign + "?ActivityId=" + ActivityId;
                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                    res = res.substring(1, res.length() - 1);
                    res = res.replaceAll("\\\\", "");

                }
                String s = res;
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }


            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // center_progressbar.setVisibility(View.GONE);

            if (s.contains("UserName")) {
                ContentValues values = new ContentValues();
                JSONArray jResults = null;
                SQLiteDatabase sql = db.getWritableDatabase();
                try {
                    try {
                        jResults = new JSONArray(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String msg = "";
                    sql.delete(db.TABLE_NEXTAPPR_SAHARA, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_NEXTAPPR_SAHARA, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_NEXTAPPR_SAHARA, null, values);
                        Log.e("", "" + a);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                ChangeApproverDialog_complete();
            }


        }
    }

    public void getAppr() {
        SQLiteDatabase sql = db.getWritableDatabase();
        ArrayList<String> Workspace_list = new ArrayList<>();
        String query = "SELECT * FROM " + db.TABLE_NEXTAPPR_SAHARA;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Workspace_list.add(cur.getString(cur.getColumnIndex("UserName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(AddDatasheetActivityMain.this, android.R.layout.simple_spinner_item, Workspace_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_appr.setAdapter(adapter);

        }
    }

    private void getPostReassignDataJson() {


        approval_list = getApprPostData();

        getApprovalJSONObj();


    }

    private void getApprovalJSONObj() {
        try {

            for (int i = 0; i < approval_list.size(); i++) {
                approvalJsonData = new JSONObject();

                String Date = approval_list.get(i).getStartDt();
                String startDate = "", EndDate = "", ActualStartDate = "", ActualEndDate = "", DueDate = "", ExpectedComplete_Date = "",
                        ActRassExpctdEndDt = "", expdt = "";


                startDate = Convertdate(approval_list.get(i).getStartDt());
                EndDate = Convertdate(approval_list.get(i).getEndDt());

                ActualStartDate = approval_list.get(i).getActualStartDate();
                ActualStartDate = ActualStartDate.substring(ActualStartDate.indexOf("(") + 1, ActualStartDate.lastIndexOf(")"));
                long ActualStartDate1 = Long.parseLong(ActualStartDate);

                ActualEndDate = approval_list.get(i).getActualEndDate();
                ActualEndDate = ActualEndDate.substring(ActualEndDate.indexOf("(") + 1, ActualEndDate.lastIndexOf(")"));
                long ActualEndDate1 = Long.parseLong(ActualEndDate);

                DueDate = approval_list.get(i).getDueDate();
                DueDate = DueDate.substring(DueDate.indexOf("(") + 1, DueDate.lastIndexOf(")"));
                long DueDate1 = Long.parseLong(DueDate);


                ExpectedComplete_Date = approval_list.get(i).getExpectedComplete_Date();
                ExpectedComplete_Date = ExpectedComplete_Date.substring(ExpectedComplete_Date.indexOf("(") + 1, ExpectedComplete_Date.lastIndexOf(")"));
                long ExpectedComplete_Date1 = Long.parseLong(ExpectedComplete_Date);




               /* ActRassExpctdEndDt = Convertdate(approval_list.get(i).getExpectedComplete_Date());
                expdt = Convertdate(approval_list.get(i).getExpectedComplete_Date());*/

                Convertdate(Date);

                approvalJsonData.put("ActivityId", approval_list.get(i).getActivityId());
                approvalJsonData.put("ActivityName", approval_list.get(i).getActivityName());
                approvalJsonData.put("UnitId", "CSSDetail");
                approvalJsonData.put("ActivityTypeId", "1234321");
                approvalJsonData.put("ProjectId", approval_list.get(i).getProjectId());
                approvalJsonData.put("PriorityId", approval_list.get(i).getPriorityId());
                approvalJsonData.put("StartDate", startDate);
                approvalJsonData.put("EndDate", EndDate);
                approvalJsonData.put("ActualStartDate", getDate(ActualStartDate1));
                approvalJsonData.put("ActualEndDate", getDate(ActualEndDate1));
                approvalJsonData.put("DueDate", getDate(DueDate1));
                approvalJsonData.put("ExpectedComplete_Date", getDate(ExpectedComplete_Date1));
                approvalJsonData.put("HoursRequired", approval_list.get(i).getHoursRequired());
                approvalJsonData.put("IssuedTo", issuedTo);
                approvalJsonData.put("ActivityCode", "CSSDetail");
                approvalJsonData.put("Reason", "Transfer datasheet");
                approvalJsonData.put("ddlDaysHours", "");
                approvalJsonData.put("NewAssignee", "");
                approvalJsonData.put("hdnfldTransferFlag", "");
                approvalJsonData.put("IsCompActPresent", "");
                approvalJsonData.put("CompletionActId", "");
                approvalJsonData.put("hdnfldUnitIdLocal", approval_list.get(i).getUnitId());
                approvalJsonData.put("ActRassExpctdEndDt", getDate(ExpectedComplete_Date1));
                approvalJsonData.put("expdt", getDate(ExpectedComplete_Date1));

                finalApprObj = approvalJsonData.toString();
                finalApprObj = finalApprObj.replaceAll("\\\\", "");


                if (ut.isNet(context)) {
                    new StartSession(AddDatasheetActivityMain.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new UploadReassignDataURLJSON().execute(finalApprObj);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);

                        }
                    });

                }


              /*  String status = "";
                String url = CompanyURL + WebUrlClass.api_reassign_datasheet;
                String op = status;
                remark = "Datasheet save successfully for " + approval_list.get(i).getActivityName();
                CreateOfflinedatasheetfill(url, finalApprObj, WebUrlClass.POSTFLAG, remark, op);*/
                // center_progressbar.setVisibility(View.GONE);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    class UploadReassignDataURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgressDialog();
        }


        @Override
        protected String doInBackground(String... params) {
            String finalApprObj = params[0];
            String url = CompanyURL + WebUrlClass.api_reassignActivity;
            try {
                res = ut.OpenPostConnection(url, finalApprObj, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            String Activityid = "";
            if (resp.contains("success")) {
                try {
                    Activityid = approvalJsonData.getString("ActivityId");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?",
                        new String[]{Activityid});

                sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?",
                        new String[]{Activityid});
                String msg = "Datasheet Saved Sucessfully and Send To  " + reassignToName;
                Toast.makeText(AddDatasheetActivityMain.this, msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddDatasheetActivityMain.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(AddDatasheetActivityMain.this, "Activity Reassign Unsuccessful", Toast.LENGTH_LONG).show();
            }
        }

    }


    private ArrayList<SaharaAppr> getApprPostData() {
        ArrayList<SaharaAppr> results = new ArrayList<SaharaAppr>();


        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_NEXTAPPR_SAHARA, null);
        if (c.getCount() > 0) {
            try {
                c.moveToFirst();
                do {
                    saharaAppr = new SaharaAppr();
                    saharaAppr.setActivityId(c.getString(c.getColumnIndex("ActivityId")));
                    saharaAppr.setActivityName(c.getString(c.getColumnIndex("ActivityName")));
                    saharaAppr.setProjectName(c.getString(c.getColumnIndex("ProjectName")));
                    saharaAppr.setUnitId(c.getString(c.getColumnIndex("UnitId")));
                    saharaAppr.setActivityTypeId(c.getString(c.getColumnIndex("ActivityTypeId")));
                    saharaAppr.setProjectId(c.getString(c.getColumnIndex("ProjectId")));
                    saharaAppr.setPriorityId(c.getString(c.getColumnIndex("PriorityId")));
                    saharaAppr.setStartDt(c.getString(c.getColumnIndex("StartDt")));
                    saharaAppr.setEndDt(c.getString(c.getColumnIndex("EndDt")));
                    saharaAppr.setActualStartDate(c.getString(c.getColumnIndex("ActualStartDate")));
                    saharaAppr.setActualEndDate(c.getString(c.getColumnIndex("ActualEndDate")));
                    saharaAppr.setDueDate(c.getString(c.getColumnIndex("DueDate")));
                    saharaAppr.setExpectedComplete_Date(c.getString(c.getColumnIndex("ExpectedComplete_Date")));
                    saharaAppr.setHoursRequired(c.getString(c.getColumnIndex("HoursRequired")));
                    saharaAppr.setIssuedTo(c.getString(c.getColumnIndex("IssuedTo")));
                    saharaAppr.setActivityCode(c.getString(c.getColumnIndex("ActivityCode")));
                    saharaAppr.setReason(c.getString(c.getColumnIndex("Reason")));
                    saharaAppr.setUserMasterId(c.getString(c.getColumnIndex("UserMasterId")));
                    saharaAppr.setUserMasterName(c.getString(c.getColumnIndex("UserName")));


                    results.add(saharaAppr);

                } while (c.moveToNext());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error", e.getMessage());
            }
        } else {

        }
        return results;

    }


    private void ChangeApproverDialog() {
        final String[] UserName = new String[1];
        final Dialog dialog1 = new Dialog(AddDatasheetActivityMain.this);
        dialog1.setContentView(R.layout.vwb_dialog_next_approver);
        mLevel = dialog1.findViewById(R.id.nextappr);
        sp_appr = (SearchableSpinner) dialog1.findViewById(R.id.sp_nxtAppr);
        btn_ok = (Button) dialog1.findViewById(R.id.btn_ok);
        btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);

        getAppr();
        dialog1.setCancelable(false);
        dialog1.show();
        mLevel.setText(nextAppr + "");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // center_progressbar.setVisibility(View.VISIBLE);

                getPostReassignDataJson();




/*
                Remark = edApproveRemar.getText().toString();
                try {
                    String remarkname = ActivityName + " has send to " + UserName[0] + "  for approval";
                    String url = CompanyURL + WebUrlClass.api_sendNextAppr + "?DocApprHdrId=" + URLEncoder.encode(DocApprHdrId, "UTF-8") +
                            "&ActivityId=" + URLEncoder.encode(ActivityId, "UTF-8") +
                            "&IssuedTo=" + URLEncoder.encode(UserMasterId + "", "UTF-8") +
                            "&SourceId=" + URLEncoder.encode(SourceId + "", "UTF-8") +
                            "&UserMasterId=" + URLEncoder.encode(UserMasterIDnextAppr + "", "UTF-8") +
                            "&Remark=" + URLEncoder.encode(Remark + "", "UTF-8");


                    String op = "";
                    CreateOfflineIntend(url, null, WebUrlClass.GETFlAG, remarkname, op);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/
                dialog1.dismiss();
            }
        });

        sp_appr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserName[0] = parent.getItemAtPosition(position).toString();
                String query = "SELECT * FROM " + db.TABLE_NEXTAPPR + " WHERE UserName='" + UserName[0] + "'";
                SQLiteDatabase sql = db.getWritableDatabase();
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    UserMasterIDnextAppr = cur.getString(cur.getColumnIndex("UserMasterId"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getDatasheetJSONObj_Draft() {
        try {
            for (int i = 0; i < datasheetlists.size(); i++) {
                DatasheetAnsData = new JSONObject();

                //json.put("jsonValue",URLEncoder.encode(jsonValue, "utf-8"));
                String Questiontext = datasheetlists.get(i).getQuesText();
                Questiontext = Questiontext.toString().trim();
                String ansText = datasheetlists.get(i).getAnswer();


                // DatasheetAnsData.put("QuesText", URLEncoder.encode(Questiontext, "utf-8"));
                DatasheetAnsData.put("QuesText", Questiontext);

                DatasheetAnsData.put("Remarks", " ");
                if (ansText != null) {
                    DatasheetAnsData.put("ResponseByCustomer", datasheetlists.get(i).getAnswer());
                } else {
                    DatasheetAnsData.put("ResponseByCustomer", "");
                }
                if (SourceId == null) {
                    DatasheetAnsData.put("FKCssHeaderId", "");
                } else {
                    DatasheetAnsData.put("FKCssHeaderId", SourceId);
                }
                DatasheetAnsData.put("PKCssFormsQuesID", datasheetlists.get(i).getPKCssFormsQuesID());
                if (datasheetlists.get(i).getResponseType().equalsIgnoreCase("Text")) {
                    DatasheetAnsData.put("Flag", "T");
                } else if (datasheetlists.get(i).getResponseType().equalsIgnoreCase("Selection")) {
                    DatasheetAnsData.put("Flag", "R");
                } else if (datasheetlists.get(i).getResponseType().equalsIgnoreCase("Numeric")) {
                    DatasheetAnsData.put("Flag", "T");
                } else {

                }
                DatasheetAnsData.put("FKQuesId", datasheetlists.get(i).getFKQuesId());

                int dataList_Size = datasheetlists.size();
                int staticList_Size = datasheetlists.size();

                   /* if(i <= staticValue_List.size()) {
                        for(int k=0 ; k<staticValue_List.size() ; k++){
                            DatasheetAnsData.put("PKCssDtlsID", valueList.get(k));
                        }
                    }else{*/
                DatasheetAnsData.put("PKCssDtlsID", datasheetlists.get(i).getDetailid());
                //}


                DatasheetAnsDataFinal.put(DatasheetAnsData);


            }
            DatasheetFinalobj.put("FormId", FormId);
            DatasheetFinalobj.put("Mode", "A");
            DatasheetFinalobj.put("DatasheetAnsDataFinal", DatasheetAnsDataFinal);

            if (EnvMasterId.equalsIgnoreCase("pragati") ||
                    EnvMasterId.equalsIgnoreCase("Pragati") ||
                    EnvMasterId.equalsIgnoreCase("b207")) {
                // DatasheetFinalobj.put("ActivityId", ActivityId);

                DatasheetFinalobj.put("ActivityId", ActivityId);

            } else {
                DatasheetFinalobj.put("ActivityId", ActivityId);
            }
            // DatasheetFinalobj.put("ActivityId", ActivityId);
            if (EnvMasterId.equalsIgnoreCase("pragati")
                    || EnvMasterId.equalsIgnoreCase("b207")) {

                DatasheetFinalobj.put("ActivityId", ActivityId);

            } else {
                DatasheetFinalobj.put("ActivityId", ActivityId);
            }
            // DatasheetFinalobj.put("ActivityId", ActivityId);
            if (EnvMasterId.equalsIgnoreCase("pragati")) {

                DatasheetFinalobj.put("Module", "Konnect");
            } else {
                DatasheetFinalobj.put("Module", "VWB");
            }
            DatasheetFinalobj.put("CallId", " ");
            DatasheetFinalobj.put("FormDesc", ActivityName);
            DatasheetFinalobj.put("FlagSaveAction", "3");


            JSONArray jsonArray1 = new JSONArray();

            for (int j = 0; j < datasheetlists.size(); j++) {

                if (datasheetlists.get(j).getAnswer() == null ||
                        datasheetlists.get(j).getAnswer().equalsIgnoreCase("")) {
                    /* Log.i("Unseleted_pos :: ",String.valueOf(j));*/
                } else {
                    /*Log.i("seleted_pos :: ",String.valueOf(j));*/
                    ArrDocumentReview = new JSONObject();

                    String ansText = datasheetlists.get(j).getAnswer();
                    if (ansText != null) {
                        ArrDocumentReview.put("Remark", datasheetlists.get(j).getAnswer());
                    } else {
                        ArrDocumentReview.put("Remark", "");

                    }
                    ArrDocumentReview.put("PKCSSDtlsId", datasheetlists.get(j).getDetailid());
                    if (SourceId == null) {
                        ArrDocumentReview.put("FKCssHeaderId", "");
                    } else {
                        ArrDocumentReview.put("FKCssHeaderId", SourceId);
                    }

                    ArrDocumentReview.put("UserMasterId", UserMasterId);
                    ArrDocumentReview.put("ApprStatus", "");
                    ArrDocumentReview.put("Assigned_Count", "");
                    jsonArray1.put(ArrDocumentReview);
                    DatasheetFinalobj.put("ArrDocumentReview", jsonArray1);

                }

            }

            FinalObj = DatasheetFinalobj.toString();
            FinalObj = FinalObj.replaceAll("\\\\", "");

            String status = "";
            String url = CompanyURL + WebUrlClass.api_save_datasheet;
            String op = ActivityName;
            remark = "Datasheet save successfully for ";
            CreateOfflinedatasheetfill(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetListner() {
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(AddDatasheetActivityMain.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                startActivity(intent);*/
                //finish();
                onBackPressed();
            }
        });
        btn_SaveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDatasheetJSONObj_Draft();

            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // center_progressbar.setVisibility(View.VISIBLE);
                getDatasheetJSONObj();
            }
        });
        lv_queList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String aa = datasheetlists.get(position).getQuesText();
                String detailid_store = datasheetlists.get(position).getDetailid();
                String datasheetObjStr = new Gson().toJson(datasheetlists.get(position));
                String datasheetList = new Gson().toJson(new DatasheetListObject(datasheetlists));

                selectedPos = position;


             /*  if(detailid_store != null){
                   staticValue_List.put(position,detailid_store);
                   valueList = new ArrayList<String>(staticValue_List.values());
                   postnList = new ArrayList<Integer>(staticValue_List.keySet());
               }*/


                String que = "SELECT FormId,FKQuesId FROM " + db.TABLE_DATASHEET_DATA + " WHERE QuesText LIKE '" + aa + "'";
                Cursor cur = sql.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        FormId = cur.getString(cur.getColumnIndex("FormId"));
                        FKQuesId = cur.getString(cur.getColumnIndex("FKQuesId"));
                        Intent intent = new Intent(AddDatasheetActivityMain.this, DatasheetAddDetailActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("obj", datasheetObjStr);
                        intent.putExtra("datasheetList", datasheetList);
                        intent.putExtra("FormId", FormId);
                        intent.putExtra("FKQuesId", FKQuesId);
                        startActivityForResult(intent, 1145);
                    } while (cur.moveToNext());

                    //  finish();
                } else {
                    Toast.makeText(AddDatasheetActivityMain.this, "Please refresh data", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        lv_queList = (ListView) findViewById(R.id.lv_queList);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_return = (Button) findViewById(R.id.btn_return);
        btn_SaveDraft = findViewById(R.id.btn_SaveDraft);
        layBottom = findViewById(R.id.layBottom);
        progressBar1 = findViewById(R.id.progressBar1);
        txt_title = findViewById(R.id.txt_title);
        // center_progressbar = findViewById(R.id.center_progressbar);
        datasheetlists = new ArrayList<Datasheet>();

        layBottom.setBottom(5);

        DatasheetAnsDataFinal = new JSONArray();
        DatasheetFinalobj = new JSONObject();


        if (Constants.type == Constants.Type.Sahara) {
            btn_SaveDraft.setVisibility(View.VISIBLE);
            btn_save.setText("Send Forward");
            btn_SaveDraft.setText("Save Remarks");
            btn_SaveDraft.setBackgroundColor(Color.parseColor("#23b7e5"));
            btn_save.setTextSize(11.00f);
            btn_SaveDraft.setTextSize(12.00f);
            btn_return.setTextSize(12.00f);

        } else if (Constants.type == Constants.Type.ZP) {
            btn_SaveDraft.setVisibility(View.VISIBLE);
            btn_save.setText("Send for approval");
            btn_SaveDraft.setText("Save");
            btn_SaveDraft.setBackgroundColor(Color.parseColor("#23b7e5"));
            btn_save.setTextSize(9.00f);
            btn_SaveDraft.setTextSize(12.00f);
            btn_return.setTextSize(12.00f);
        } else {
            btn_SaveDraft.setVisibility(View.GONE);
            btn_save.setText("Submit");
        }
    }

    private void UpdateDatasheet() {
        datasheetlists.clear();
        datasheetlists = getdata();
        Log.i("completeDatasheet:::", String.valueOf(datasheetlists));
        dataAdapter = new DatasheetQueListAdapter(AddDatasheetActivityMain.this, datasheetlists, EnvMasterId);
        lv_queList.setAdapter(dataAdapter);
    }

    private ArrayList<Datasheet> getdata() {
        ArrayList<Datasheet> datasheetArrayList = new ArrayList<Datasheet>();

        Datasheet datasheet;

        Cursor c = sql
                .rawQuery(
                        "SELECT * FROM " + db.TABLE_DATASHEET_DATA + " WHERE FormId='" + FormId + "'", null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            do {
                datasheet = new Datasheet();

                datasheet.setFKQuesId(c.getString(c.getColumnIndex("FKQuesId")));
                datasheet.setSequenceNo(Integer.parseInt(c.getString(c.getColumnIndex("SequenceNo"))));
                datasheet.setQuesText(c.getString(c.getColumnIndex("QuesText")));
                datasheet.setValueMax(c.getString(c.getColumnIndex("ValueMax")));
                datasheet.setSelectionValue(c.getString(c.getColumnIndex("SelectionValue")));
                datasheet.setWeightage(c.getString(c.getColumnIndex("Weightage")));
                datasheet.setPKCssFormsQuesID(c.getString(c.getColumnIndex("PKCssFormsQuesID")));
                datasheet.setIsResponseMandatory(c.getString(c.getColumnIndex("IsResponseMandatory")));
                datasheet.setSelectionText(c.getString(c.getColumnIndex("SelectionText")));
                datasheet.setValueMin(c.getString(c.getColumnIndex("ValueMin")));
                datasheet.setSelectionType(c.getString(c.getColumnIndex("SelectionType")));
                datasheet.setMaxValueText(c.getString(c.getColumnIndex("MaxValueText")));
                datasheet.setControlWidth(c.getString(c.getColumnIndex("ControlWidth")));
                datasheet.setMaxNoOfResponses(c.getString(c.getColumnIndex("MaxNoOfResponses")));
                datasheet.setResponseType(c.getString(c.getColumnIndex("ResponseType")));
                datasheet.setNotes(c.getString(c.getColumnIndex("Notes")));
                datasheet.setFKPrimaryQuesId(c.getString(c.getColumnIndex("FKPrimaryQuesId")));
                datasheet.setFKSecondaryQuesId(c.getString(c.getColumnIndex("FKSecondaryQuesId")));
                datasheet.setDisableQuesStr(c.getString(c.getColumnIndex("DisableQuesStr")));
                datasheet.setGroupID(c.getString(c.getColumnIndex("GroupID")));
                datasheet.setGroupName(c.getString(c.getColumnIndex("GroupName")));
                datasheet.setQuesCode(c.getString(c.getColumnIndex("QuesCode")));
                datasheet.setMaxExpectedResponse(c.getString(c.getColumnIndex("MaxExpectedResponse")));
                datasheet.setResponseValue(c.getString(c.getColumnIndex("ResponseValue")));
                datasheet.setDetailid(c.getString(c.getColumnIndex("PKCssDtlsID")));
                datasheet.setFormId(c.getString(c.getColumnIndex("FormId")));
                datasheet.setIsSingleAttachment(c.getString(c.getColumnIndex("IsSingleAttachment")));

                Log.i("dataObject::", new Gson().toJson(datasheet));
                datasheetArrayList.add(datasheet);

            } while (c.moveToNext());

        } else {


        }
        return datasheetArrayList;

    }

    class UploadDatasheet extends AsyncTask<String, Void, String> {
        Object respond;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (res.contains("")) {

                sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?",
                        new String[]{ActivityId});
                Toast.makeText(AddDatasheetActivityMain.this, "Datasheet Uploaded successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddDatasheetActivityMain.this, ActivityMain.class);
                startActivity(intent);
                finish();
                dismissProgressDialog();

            }
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_save_datasheet;
            try {
                FinalObj = DatasheetFinalobj.toString();
                FinalObj = FinalObj.replaceAll("\\\\", "");
                respond = ut.OpenPostConnection(url, FinalObj, getApplicationContext());
                res = respond.toString();
                res = res.replaceAll("\"", "");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void showProgressDialog() {
    }

    private void dismissProgressDialog() {
    }

    class DownloadDatasheetGetData extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar1.setVisibility(View.VISIBLE);
            //center_progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url;
                // if (Constants.type == Constants.Type.Sahara || Constants.type == Constants.Type.ZP) {
                url = CompanyURL + WebUrlClass.api_Datasheet_GetData + "?FormId=" + URLEncoder.encode(FormId, "UTF-8")
                        + "&HeaderId=" + URLEncoder.encode(SourceId, "UTF-8") + "&Mode=" + URLEncoder.encode(Mode, "UTF-8");
                //} else {

                //  url = CompanyURL + WebUrlClass.api_Datasheet_GetData + "?FormId=" + URLEncoder.encode(FormId, "UTF-8");
                // }

                res = ut.OpenConnection(url, getApplicationContext());
                res = res.toString().replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                Log.i("responseData::", res);
                JSONArray jResults = new JSONArray(res);
                ContentValues values = new ContentValues();
                sql.delete(db.TABLE_DATASHEET_DATA, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_DATASHEET_DATA, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int index = 0; index < jResults.length(); index++) {
                    JSONObject jorder = jResults.getJSONObject(index);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("FormId")) {
                            columnValue = FormId;
                        } else {
                            columnValue = jorder.getString(columnName);
                        }


                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_DATASHEET_DATA, null, values);
                    Log.e("", "" + a);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error in Add Datasheet ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressBar1.setVisibility(View.GONE);
            if (res.equals("[]")) {
                Toast.makeText(AddDatasheetActivityMain.this, "No Questions Present", Toast.LENGTH_LONG).show();
            } else {
                //center_progressbar.setVisibility(View.GONE);
                UpdateDatasheet();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(AddDatasheetActivityMain.this, ActivityMain.class);
        startActivity(intent);*/
        finish();

    }

    private void CreateOfflinedatasheetfill(final String url, final String parameter,
                                            final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            SQLiteDatabase sql1 = db.getWritableDatabase();
           /* sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?",
                    new String[]{ActivityId});*/
            Toast.makeText(AddDatasheetActivityMain.this, "Datasheet save successfully", Toast.LENGTH_LONG).show();
            //  dismissProgressDialog();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved ", Toast.LENGTH_LONG).show();


        }


    }

    private void CreateOfflinedatasheetfill_Appr(final String url, final String parameter,
                                                 final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            SQLiteDatabase sql1 = db.getWritableDatabase();
            sql1.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?",
                    new String[]{ActivityId});
            Toast.makeText(AddDatasheetActivityMain.this, "Datasheet save successfully", Toast.LENGTH_LONG).show();
            //  dismissProgressDialog();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);

        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved ", Toast.LENGTH_LONG).show();


        }


    }

    public String Convertdate(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");//yyyy-MM-dd'T'HH:mm:ss
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        Date data = null;
        try {
            data = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateToStr = output.format(data);
        return DateToStr;

    }

    private String getDate(long timeStamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1145 && resultCode == 1254) {

            if (data != null) {
                datasheetlists = new Gson().fromJson(data.getStringExtra("objStr"), DatasheetListObject.class).getDatasheets();
                dataAdapter.update(datasheetlists);

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
      //  Context context = this;
        locationManager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        try {
            Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mCurrentLocation != null) {
                double lat = mCurrentLocation.getLatitude(),
                        lon = mCurrentLocation.getLongitude();
                canGetLocation = true;
                latitude = lat;
                longitude = lon;
                Log.e("token ", "Lat :" + lat + " Long" + lon);
                GetCurrentLocation(lat, lon);

            } else {

                canGetLocation = false;
                   /* if (!isGPSEnabled) {
                        noLocation("GPS Location");
                    } else {
                        noLocation("GPS Location and Network Connectivity");
                    }*/

                if (!isGPSEnabled && !isNetworkEnabled) {
                    //  noLocation("GPS Location and Network Connectivity");
                } else {
                    if (!isGPSEnabled) {
                        //  noLocation("GPS Location");
                    } else if (!isNetworkEnabled) {
                        // noLocation("Network Connectivity");
                    } else {
                        //  noLocation("GPS Location or Network Connectivity");
                    }
                }


            }
        } catch (SecurityException e) {
            e.printStackTrace();
            //noLocation("Location Permission");
            canGetLocation = false;

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void GetCurrentLocation(double lat, double lon) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());

            List<Address> addressList = geocoder.getFromLocation(lat,
                    lon, 1);
            //  System.out.println(latitude + " lat " + longitude + " long");
            if (addressList != null && addressList.size() > 0) {
                Address address1 = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address1.getMaxAddressLineIndex(); i++) {
                    if (i == 0) {
                        sb.append(address1.getAddressLine(0));
                    } else {
                        sb.append("," + address1.getAddressLine(i));
                    }

                }
                  /*  String data = address.getAdminArea();
                    YourCityName = address.getLocality();
                    result = sb.toString();
                    txtMyLocation.setText(result);
                    //  btnSE.setVisibility(View.VISIBLE);
                    imggps.setVisibility(View.VISIBLE);*/

                String address = addressList.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addressList.get(0).getLocality();
                String state = addressList.get(0).getAdminArea();
                String country = addressList.get(0).getCountryName();
                String postalCode = addressList.get(0).getPostalCode();
                // String knownName = addressList.get(0).getFeatureName();
                LocationName = address + " , " + city + " , " + state + "," + country + " . " + postalCode;
                //LocationName = result;
               // txtMyLocation.setText(LocationName);
                // btnSE.setVisibility(View.GONE);
                //imggps.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            result = "Location not Found";
            Log.e("test", "Unable connect to Geocoder", e);
            if (ut.isNet(context)) {

                try {
                    final String[] loc = new String[1];
                    final Thread t = new Thread() {

                        public void run() {
                            try {
                                String url = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk&latlng=" + latitude + "," + longitude + "&sensor=true";
                                Object res = ut.OpenConnection(url, getApplicationContext());
                                if (res == null) {

                                } else {
                                    String response = res.toString();
                                    JSONObject jsonObj = null;
                                    jsonObj = new JSONObject(response);
                                    String Status1 = jsonObj.getString("status");
                                    if (Status1.equalsIgnoreCase("OK")) {
                                        JSONArray Results = jsonObj.getJSONArray("results");
                                        int cnt = Results.length();
                                        if (cnt > 1) {
                                            JSONObject zero2 = Results.getJSONObject(1);
                                            result = zero2.getString("formatted_address");

                                        } else {
                                            JSONObject zero2 = Results.getJSONObject(0);
                                            result = zero2.getString("formatted_address");

                                        }

                                    } else {

                                    }
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();

                            }
                        }

                    };
                    t.start();

                    t.join();


                } catch (Exception e1) {
                    e1.printStackTrace();

                }


            } else {
                ut.displayToast(getApplicationContext(), "No Internet Connection");
            }
            LocationName = result;
          //  txtMyLocation.setText(result);
            //   btnSE.setVisibility(View.GONE);
            //imggps.setVisibility(View.VISIBLE);

        }
        // LocationName = result;
        // txtMyLocation.setText(result);
        //    btnSE.setVisibility(View.GONE);
      //  imggps.setVisibility(View.VISIBLE);

    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS) {
            return true;
        } else {
            return false;
        }
    }

}
