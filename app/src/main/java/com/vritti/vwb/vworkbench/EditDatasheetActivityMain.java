package com.vritti.vwb.vworkbench;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
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
import android.widget.RelativeLayout;
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
import com.vritti.vwb.Adapter.EditDatasheetAdapter;
import com.vritti.vwb.Adapter.EditDatasheetKendraAdapter;
import com.vritti.vwb.Adapter.SaharaRemarkAdapter;
import com.vritti.vwb.Adapter.Sahara_AttachmentDetailsAdapter;
import com.vritti.vwb.Beans.DatasheetListObject;
import com.vritti.vwb.Beans.EditDatasheet;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.vwb.Beans.SaharaAppr;
import com.vritti.vwb.Beans.SaharaRemarksBean;
import com.vritti.vwb.Beans.SaharaRemarksDetailsListObj;
import com.vritti.vwb.classes.CommonFunction;

import static com.vritti.ekatm.services.DownloadJobService.FASTEST_INTERVAL;
import static com.vritti.ekatm.services.DownloadJobService.MIN_DISTANCE_CHANGE_FOR_UPDATES;
import static com.vritti.ekatm.services.DownloadJobService.MIN_TIME_BW_UPDATES;
import static com.vritti.vwb.vworkbench.NotificationActivity.progress_bar_type;
/*
 * Created by 300151 on 12/12/2016.
 */

public class EditDatasheetActivityMain extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", UserProspect = "", Result = "", Mode = "", Designation = "",
            PKSuspectId = "";
    int flagFromTeam;
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    JSONArray json_Resp;
    ArrayList<ChatUser> chatUserArrayList;
    private long mLastClickTime = 0;
    ArrayList<EditDatasheet> results;
    ArrayList<String> pkCssDetailID;
    ArrayList<SaharaRemarksDetailsListObj> editListObj;


    String FormId, SourceId, ActivityId, FKQuesId, ActivityName, HeaderId = "";
    public static ArrayList<EditDatasheet> editDatasheetslist;
    boolean isRemark;
    public static ArrayList<AttachmentBean> attachmentList;
    ListView lstquestions_editdatasheet;
    SQLiteDatabase sql;
    SharedPreferences userpreferences;
    EditDatasheet editDatasheet;
    AttachmentBean attachmentBean;
    EditDatasheetAdapter editDatasheetAdapter;
    EditDatasheetKendraAdapter editDatasheetKendraAdapter;
    Button btnSave, btnReturn, btn_remarks;
    Button btn_SaveDraft;
    JSONObject DatasheetAnsData, DatasheetFinalobj, ArrDocumentReview;
    JSONArray DatasheetAnsDataFinal;
    String FinalObj;
    private String remark;
    TextView mLevel, txt_gpsdetails, txt_gps_date, txt_nofound;
    SearchableSpinner sp_appr;
    Button btn_ok, btn_cancel;
    int nextAppr = 0;
    String UserMasterIDnextAppr = "", senderName = "";
    ArrayList<SaharaAppr> approval_list;
    String issuedTo = "", reassignUserName = "";
    SaharaAppr saharaAppr;
    JSONObject approvalJsonData;
    String finalApprObj;
    String DateToStr = "";
    String respApprText = "";
    String reportingTo = "";
    RecyclerView ls_attachname;
    RecyclerView list_remarks;
    TextView txt_quest, edit_remarks;
    RelativeLayout rel_pending, rel_appr, rel_disappr;

    Sahara_AttachmentDetailsAdapter sahara_attachmentDetailsAdapter;
    SaharaRemarkAdapter saharaRemarkAdapter;
    private final int MEGABYTE = 1024 * 1024;
    private ProgressDialog pDialog;
    //public static int progress_bar_type = 0;
    BottomSheetDialog dialog;
    int selectedPos = -1;
    int adapterPos = -1;
    String fromEdit = "";
    Dialog dialog1;
    Dialog dialog2, dialog3;
    String clickedImgStatus;
    String Status = "";
    String flagBtn = "";
    boolean btnClicked;

    String reassignToId = "";
    ArrayList<String> codeArrayList;
    int rowClick = -1;
    ArrayList<SaharaRemarksDetailsListObj> listObjs;

    HashMap<String, SaharaRemarksBean> saharaRemarksBeanHashMap;

    boolean fromFlag;
    int remarkPos;
    ProgressBar progressBar1;
    LinearLayout ln_list;
    public static String LocationName = "";
    String result = "";
    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_editdatasheet);
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
        Designation = ut.getValue(context, WebUrlClass.GET_Designation, settingKey);
        editListObj = new ArrayList<>();
        editListObj.clear();

        InitView();
        SetListner();
        getCurrentLocationNew();

        dialog1 = new Dialog(EditDatasheetActivityMain.this);
        dialog2 = new Dialog(EditDatasheetActivityMain.this);
        dialog3 = new Dialog(EditDatasheetActivityMain.this);
        if (getIntent().hasExtra("FormId")) {
            Intent i = getIntent();
            FormId = i.getStringExtra("FormId");
            if (EnvMasterId.equalsIgnoreCase("pragati")
                    || EnvMasterId.equalsIgnoreCase("Pragati")
                    || EnvMasterId.equalsIgnoreCase("b207")) {
                SourceId = i.getStringExtra("SourceId");
            } else {
                SourceId = i.getStringExtra("SourceId");
            }

            ActivityId = i.getStringExtra("ActivityId");
            ActivityName = i.getStringExtra("ActivityName");
            HeaderId = i.getStringExtra("PkCssHeaderID");
            UserProspect = i.getStringExtra("UseForProspect");
            Result = i.getStringExtra("Result");
            Mode = i.getStringExtra("Mode");
            PKSuspectId = i.getStringExtra("PKSuspectId");
            flagFromTeam = i.getIntExtra("flagFromTeam", 0);

            if(Constants.type == Constants.Type.Sahara || Constants.type == Constants.Type.ZP) {


                if (flagFromTeam == 1) {
                    btn_SaveDraft.setVisibility(View.GONE);
                    btnSave.setVisibility(View.GONE);
                    btnReturn.setText("Cancel");
                    btnReturn.setBackgroundColor(Color.parseColor("#23b7e5"));
                } else {
                    btn_SaveDraft.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                }
            }else{

            }


        }

        if (ut.isNet(context)) {

            new StartSession(EditDatasheetActivityMain.this, new CallbackInterface() {
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
            Toast.makeText(EditDatasheetActivityMain.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }


        // Toast.makeText(EditDatasheetActivityMain.this, "In Edit Datasheet", Toast.LENGTH_LONG).show();
        if (ut.isNet(context)) {
            new StartSession(EditDatasheetActivityMain.this, new CallbackInterface() {
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
            Toast.makeText(EditDatasheetActivityMain.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
       /* if(cf.getChatusercount() > 0){

        }else {*/


        // }

    }

    public void deletefileName(int pos, int adapterpos, String edit) {
        selectedPos = pos;
        adapterPos = adapterpos;
        fromEdit = edit;


        String que = editDatasheetslist.get(pos).getQuesText();


        //datasheetArrayList.remove(d)

        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.confim_ui);

        dialog.show();


    }

    public void downloadFile(int adapterPosition, boolean isDownload) {
        selectedPos = adapterPosition;
        if (isDownload) {
            String attachmentName1 = attachmentList.get(adapterPosition).getAttachFilename();
            String path = attachmentList.get(adapterPosition).getPath();

            if (isnet()) {
                String path1 = Environment.getExternalStorageDirectory()
                        .toString();
                File file = new File(path1 + "/" + "Sahara" + "/" + "File");
                if (file.exists()) {
                    final File fileNew = new File(file + "/" + attachmentName1);
                    if (fileNew.exists()) {
                        Handler handler = new Handler(context.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                // urlGetMimeType(fileNew.getAbsolutePath());
                                Toast.makeText(context, "File Already downloaded", Toast.LENGTH_SHORT).show();

                                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(fileNew).toString());
                                String mimeType = myMime.getMimeTypeFromExtension(fileNew.getAbsolutePath());
                                String file = fileNew.getAbsolutePath();

                                if (file.contains("jpg") || file.contains("png") || file.contains("jpeg") || file.contains("JPG") ||
                                        file.contains("PNG") || file.contains("JPEG")) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(fileNew), "image/*");
                                    startActivity(intent);
                                } else {
                                    newIntent.setDataAndType(Uri.fromFile(fileNew), mimeType);
                                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    try {
                                        context.startActivity(newIntent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                                    }
                                }

                               /* MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                String mimeType = myMime.getMimeTypeFromExtension(fileNew.getAbsolutePath());
                                newIntent.setDataAndType(Uri.fromFile(fileNew), mimeType);
                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                try {
                                    context.startActivity(newIntent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                                }*/
                                //((NotificationActivity)context).sendResult(directory.getName());


                            }
                        });
                    } else {
                        cllDownloadApi(path, attachmentName1);
                    }
                } else {
                    cllDownloadApi(path, attachmentName1);
                }
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

    public void deleteAttachment(View view) {

        if (selectedPos != -1) {

            if (fromEdit.equalsIgnoreCase("fromEdit")) {
                String que = editDatasheetslist.get(selectedPos).getQuesText();
                ArrayList<String> arrayList = new ArrayList<>();

                if (editDatasheetslist.get(selectedPos).getFilePathName().size() > 0) {
                    for (int j = 0; j < editDatasheetslist.get(selectedPos).getFilePathName().size(); j++) {
                        String fileName = String.valueOf(editDatasheetslist.get(selectedPos).getFilePathName().get(j));
                        arrayList.add(fileName);
                    }

                    //   fileName = fileName.replace("[","").replace("]","");
                    arrayList.remove(adapterPos);
                    editDatasheetslist.get(selectedPos).setFilePathName(arrayList);
                    editDatasheetAdapter.deleteChange(editDatasheetslist);
                }
                dialog1.dismiss();
                dialog.dismiss();


            } else {
                callDeleteAttachmentApi(selectedPos);
            }

        }


    }

    public void viewFile(int listPos, boolean isDownload, int adapterPosition) {
        selectedPos = listPos;
        adapterPos = adapterPosition;
        if (isDownload) {
            ArrayList<String> arrayList = new ArrayList<>();

            if (editDatasheetslist.get(selectedPos).getFilePathName().size() > 0) {
                for (int i = 0; i < editDatasheetslist.get(selectedPos).getFilePathName().size(); i++) {
                    String attachmentName1 = editDatasheetslist.get(selectedPos).getFilePathName().get(i);
                    arrayList.add(attachmentName1);
                }
            }

            String viewFile = arrayList.get(adapterPos);

            if (ut.isNet(context)) {
                File file = new File(viewFile);
                String path = file.getPath().replace("[", "").replace("]", "");
                MimeTypeMap map = MimeTypeMap.getSingleton();
                String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName().replace("]", ""));
                /*if(ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png") ||
                        ext.equalsIgnoreCase("jpg")){
                    Intent intent = new Intent(context, ImageFullScreenActivity.class);
                    intent.putExtra("share_image_path",path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else  {*/
                openDocument(path);
                // }



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

    private void callDeleteAttachmentApi(final int selectedPos) {
        dialog.dismiss();
        dialog1.dismiss();
        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DeleteAttachmentApi().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    // ((NotificationActivity)context).showPopUp(false);
                }
            });
        } else
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();

    }

    public void cancelAttatchment(View view) {
        dialog.dismiss();

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

    private class DeleteAttachmentApi extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            String url = null;
            String attachId = attachmentList.get(selectedPos).getAttachGuid();
            url = CompanyURL + WebUrlClass.api_deleteAttachment + "?Attachid=" + attachId;

            try {
                res = ut.OpenPostConnection(url, "", getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("deleteResponse", response);
            if (!response.equals("error")) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                ArrayList<String> arrayList = new ArrayList<>();
                if (attachmentList.get(selectedPos).getAttachFilename() != null && !(attachmentList.get(selectedPos).getAttachFilename().equals(""))) {

                    attachmentList.remove(selectedPos);
                    sahara_attachmentDetailsAdapter.update(selectedPos);
                    editDatasheetAdapter.updateCount(rowClick, attachmentList.size(), selectedPos);//deleteChange(editDatasheetslist);
                    //}
                }


                dialog1.dismiss();
                dialog.dismiss();
            }

        }
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
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
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_CHAT_USER_LIST, null,
                            null);
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

    private void SetListner() {

        if (Designation.equalsIgnoreCase("BEO") || Designation.contains("Kendra") || Designation.contains("Extension Officer")) {
            lstquestions_editdatasheet.setClickable(false);
        } else {


            lstquestions_editdatasheet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    if (editDatasheetslist.get(position).getIsApprDisAppr() != null) {
                        if (editDatasheetslist.get(position).getIsApprDisAppr().equalsIgnoreCase("Approved")) {
                            lstquestions_editdatasheet.setClickable(false);
                        } else {

                            if (flagFromTeam == 1 && Designation.equalsIgnoreCase("school")) {

                            } else {
                                String aa = editDatasheetslist.get(position).getQuesText();

                                String datasheetObjStr = new Gson().toJson(editDatasheetslist.get(position));
                                String datasheetList = new Gson().toJson(new DatasheetListObject(editDatasheetslist, 1));
                                String que = "SELECT FormId,FKQuesId FROM " + db.TABLE_EDIT_DATASHEET + " WHERE QuesText LIKE '" + aa + "'";
                                Cursor cur = sql.rawQuery(que, null);
                                if (cur.getCount() > 0) {
                                    cur.moveToFirst();
                                    do {
                                        FormId = cur.getString(cur.getColumnIndex("FormId"));
                                        FKQuesId = cur.getString(cur.getColumnIndex("FKQuesId"));
                                    } while (cur.moveToNext());

                                    Intent intent = new Intent(EditDatasheetActivityMain.this, EditDatasheetDetailActivity.class);
                                    intent.putExtra("position", position);
                                    intent.putExtra("FormId", FormId);
                                    intent.putExtra("SourceId", SourceId);
                                    intent.putExtra("FKQuesId", FKQuesId);
                                    intent.putExtra("editobj", datasheetObjStr);
                                    intent.putExtra("editdatasheetList", datasheetList);
                                    intent.putExtra("fromSahara", "disappr");
                                    startActivityForResult(intent, 1150);
                                    //finish();
                                } else {
                                    Toast.makeText(EditDatasheetActivityMain.this, "Please refresh data", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                    } else {
                        if (flagFromTeam == 1 && Designation.equalsIgnoreCase("school")) {

                        } else {
                            String aa = editDatasheetslist.get(position).getQuesText();

                            String datasheetObjStr = new Gson().toJson(editDatasheetslist.get(position));
                            String datasheetList = new Gson().toJson(new DatasheetListObject(editDatasheetslist, 1));
                            String que = "SELECT FormId,FKQuesId FROM " + db.TABLE_EDIT_DATASHEET + " WHERE QuesText LIKE '" + aa + "'";
                            Cursor cur = sql.rawQuery(que, null);
                            if (cur.getCount() > 0) {
                                cur.moveToFirst();
                                do {
                                    FormId = cur.getString(cur.getColumnIndex("FormId"));
                                    FKQuesId = cur.getString(cur.getColumnIndex("FKQuesId"));
                                } while (cur.moveToNext());

                                Intent intent = new Intent(EditDatasheetActivityMain.this, EditDatasheetDetailActivity.class);
                                intent.putExtra("position", position);
                                intent.putExtra("FormId", FormId);
                                intent.putExtra("SourceId", SourceId);
                                intent.putExtra("FKQuesId", FKQuesId);
                                intent.putExtra("editobj", datasheetObjStr);
                                intent.putExtra("editdatasheetList", datasheetList);
                                startActivityForResult(intent, 1150);
                                //finish();
                            } else {
                                Toast.makeText(EditDatasheetActivityMain.this, "Please refresh data", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            });
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagBtn = "save";


                if (Designation.equalsIgnoreCase("BEO")) {
                    boolean isAllAppr = false;

                    for (int i = 0; i < editDatasheetslist.size(); i++) {
                        if (editDatasheetslist.get(i).getIsApprDisAppr().equalsIgnoreCase("Pending") ||
                                editDatasheetslist.get(i).getIsApprDisAppr().equalsIgnoreCase("Disapproved")) {
                            isAllAppr = false;
                            break;

                        } else {
                            isAllAppr = true;
                        }
                    }

                    if (isAllAppr) {
                        getDatasheetJSONObj_Complete();
                    } else {
                        Toast.makeText(EditDatasheetActivityMain.this, "Please check if all documents are Approved before completing the Datasheet", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    if (Designation.equalsIgnoreCase("School")) {

                        getDatasheetJSONObj(true);
                    }
                    else if (Designation.equalsIgnoreCase("Kendra") ||
                            Designation.equalsIgnoreCase("Extension Officer")) {

                        boolean isAllAppr = false;
                        if(Constants.type == Constants.Type.ZP){
                            isAllAppr = true;
                        }else{
                            for (int i = 0; i < editDatasheetslist.size(); i++) {
                                if (editDatasheetslist.get(i).getIsApprDisAppr() != null) {
                                    if (editDatasheetslist.get(i).getIsApprDisAppr().equalsIgnoreCase("Pending")) {
                                        isAllAppr = false;
                                        break;

                                    } else {
                                        if (editDatasheetslist.get(i).getDescr().equalsIgnoreCase(Designation) && !editDatasheetslist.get(i).getIsApprDisAppr().equalsIgnoreCase("Pending")) {
                                            isAllAppr = true;
                                        } else {
                                            isAllAppr = false;
                                            // break;
                                        }
                                    }
                                } else {
                                    if (editDatasheetslist.get(i).getDescr() == null && editDatasheetslist.get(i).getIsApprDisAppr() == null) {
                                        isAllAppr = false;
                                        break;
                                    } else {
                                        isAllAppr = true;

                                    }

                                }
                            }



                        }



                        if (isAllAppr) {
                            /*if(!btnClicked){
                                btnClicked = true;*/
                            getDatasheetJSONObj(true);
                           /* }else{
                                Toast.makeText(EditDatasheetActivityMain.this, "You Have Clicked button once", Toast.LENGTH_SHORT).show();
                            }
*/

                        } else {
                            Toast.makeText(EditDatasheetActivityMain.this, "Please check if all documents are Approved or Disapproved before Submitting..", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        getDatasheetJSONObj_Complete();
                    }



                }
            }
        });

        btn_SaveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagBtn = "draft";

                if (Designation.equalsIgnoreCase("BEO") || Designation.equalsIgnoreCase("Kendra")) {
                    boolean isAllAppr = false;

                    for (int i = 0; i < editDatasheetslist.size(); i++) {

                        if (editDatasheetslist.get(i).getDescr().equalsIgnoreCase(Designation)) {

                            if (editDatasheetslist.get(i).getIsApprDisAppr().equalsIgnoreCase("Pending")
                                    && editDatasheetslist.get(i).getDescr().equalsIgnoreCase(Designation)) {
                                isAllAppr = false;
                                break;

                            } else {
                                isAllAppr = true;
                            }

                        } else {
                            isAllAppr = false;
                            break;

                        }


                    }
                    if (isAllAppr) {

                        getDatasheetJSONObj(false);//Send Back button instead of Save draft name
                    } else {
                        Toast.makeText(EditDatasheetActivityMain.this, "Please check if all documents are Approved or Disapproved before Submitting..", Toast.LENGTH_SHORT).show();
                    }
                } else if (Designation.contains("Extension Officer") || Designation.equalsIgnoreCase("school")) {

                    getDatasheetJSONObj_Remark();

                } else {
                    getDatasheetJSONObj_Draft();//remark code not present
                }

            }
        });


        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.type == Constants.Type.Sahara || Constants.type == Constants.Type.ZP) {
                    if (flagFromTeam == 1) {
                        onBackPressed();
                    } else {
                        if (Designation.equalsIgnoreCase("Extension Officer") || Designation.equalsIgnoreCase("School")) {
                            onBackPressed();
                        } else {

                            getDatasheetJSONObj_Remark();
                        }
                    }
                }else{
                    finish();
                }

                /*"FlagSaveAction": "3",*/
                /*Intent intent = new Intent(EditDatasheetActivityMain.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                startActivity(intent);
                finish();*/
            }
        });


    }

    private void getDatasheetJSONObj_Complete() {
getCurrentLocationNew();

        try {
            for (int i = 0; i < editDatasheetslist.size(); i++) {
                DatasheetAnsData = new JSONObject();

                String Questiontext = editDatasheetslist.get(i).getQuesText();
                String ansText = editDatasheetslist.get(i).getAnswer();

                DatasheetAnsData.put("QuesText", editDatasheetslist.get(i).getQuesText().toString());
                DatasheetAnsData.put("Remarks", " ");

                if (ansText != null) {
                    DatasheetAnsData.put("ResponseByCustomer", editDatasheetslist.get(i).getAnswer().toString());
                } else {
                    DatasheetAnsData.put("ResponseByCustomer", "");
                }


                DatasheetAnsData.put("FKCssHeaderId", SourceId);
                DatasheetAnsData.put("PKCssFormsQuesID", editDatasheetslist.get(i).getPKCssFormsQuesID());
                if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Text")) {
                    DatasheetAnsData.put("Flag", "T");
                } else if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Selection")) {
                    DatasheetAnsData.put("Flag", "R");
                } else if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Numeric")) {
                    DatasheetAnsData.put("Flag", "T");
                } else {

                }
                DatasheetAnsData.put("FKQuesId", editDatasheetslist.get(i).getFKQuesId().toString());
                String Detailid = editDatasheetslist.get(i).getPkcssdtlsid().toString();
                DatasheetAnsData.put("PKCssDtlsID", Detailid);
                DatasheetAnsDataFinal.put(DatasheetAnsData);


            }


            DatasheetFinalobj.put("FormId", FormId);
            DatasheetFinalobj.put("Mode", "E");
            DatasheetFinalobj.put("DatasheetAnsDataFinal", DatasheetAnsDataFinal);
            if (EnvMasterId.equalsIgnoreCase("pragati")) {
                DatasheetFinalobj.put("FKActivityId", ActivityId);

            } else {
                DatasheetFinalobj.put("ActivityId", ActivityId);
            }

            //DatasheetFinalobj.put("ActivityId", ActivityId);
            if (EnvMasterId.equalsIgnoreCase("pragati")) {
                DatasheetFinalobj.put("Module", "Konnect");
            } else {
                DatasheetFinalobj.put("Module", "VWB");
            }

            DatasheetFinalobj.put("CallId", " ");
            DatasheetFinalobj.put("FormDesc", ActivityName);
            DatasheetFinalobj.put("FlagSaveAction", "0");
            DatasheetFinalobj.put("Latitude", longitude);
            DatasheetFinalobj.put("Longitude", latitude);
            DatasheetFinalobj.put("LocationName", LocationName);

            JSONArray jsonArray1 = new JSONArray();

            for (int j = 0; j < editListObj.size(); j++) {
                ArrDocumentReview = new JSONObject();
                ArrDocumentReview.put("Remark", editListObj.get(j).getRemark());
                ArrDocumentReview.put("PKCSSDtlsId", editListObj.get(j).getFkcssdtlsid());
                ArrDocumentReview.put("FKCssHeaderId", editListObj.get(j).getHeaderId());
                ArrDocumentReview.put("UserMasterId", editListObj.get(j).getUsermasterid());
                ArrDocumentReview.put("ApprStatus", editListObj.get(j).getIsApprDisAppr());
                jsonArray1.put(ArrDocumentReview);


            }
            DatasheetFinalobj.put("ArrDocumentReview", jsonArray1);


            FinalObj = DatasheetFinalobj.toString();
            //FinalObj = FinalObj.replaceAll("\\\\", "");

            String url = CompanyURL + WebUrlClass.api_save_datasheet;
            String op = "Success-Data Save Successfully";
            remark = "Datasheet save successfully for " + ActivityName;
            CreateOfflinedatasheetfill_Appr(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

         /*   if (ut.isNet(context)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(EditDatasheetActivityMain.this, ActivityMain.class);
                        startActivity(i);
                        finish();
                    }
                }, 1000);
            }else{*/
            if (Constants.type == Constants.Type.Sahara || Constants.type == Constants.Type.ZP) {


                if (Designation.equalsIgnoreCase("BEO")) {

                    try {
                        ActivityId = approvalJsonData.getString("ActivityId");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?",
                            new String[]{ActivityId});

                    sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?",
                            new String[]{ActivityId});

//                Datasheet Saved Successfully and Forwarded to Kendra!
                    Toast.makeText(EditDatasheetActivityMain.this, "Datasheet Saved Sucessfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditDatasheetActivityMain.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                    // intent.putExtra("fromEdit",0);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    // finish();

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
                    } else {
                        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                }
            }
            else{
                finish();
            }


            // http://d207.ekatm.com/api/DatasheetEntryAPI/GetReportingTo?UserMasterId=


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getDatasheetJSONObj(boolean isSave) {
        try {
            for (int i = 0; i < editDatasheetslist.size(); i++) {
                DatasheetAnsData = new JSONObject();

                String Questiontext = editDatasheetslist.get(i).getQuesText();
                String ansText = editDatasheetslist.get(i).getAnswer();

                DatasheetAnsData.put("QuesText", editDatasheetslist.get(i).getQuesText().toString());
                DatasheetAnsData.put("Remarks", " ");

                if (ansText != null) {
                    DatasheetAnsData.put("ResponseByCustomer", editDatasheetslist.get(i).getAnswer().toString());
                } else {
                    DatasheetAnsData.put("ResponseByCustomer", "");
                }


                DatasheetAnsData.put("FKCssHeaderId", SourceId);
                DatasheetAnsData.put("PKCssFormsQuesID", editDatasheetslist.get(i).getPKCssFormsQuesID());
                if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Text")) {
                    DatasheetAnsData.put("Flag", "T");
                } else if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Selection")) {
                    DatasheetAnsData.put("Flag", "R");
                } else if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Numeric")) {
                    DatasheetAnsData.put("Flag", "T");
                } else {

                }
                DatasheetAnsData.put("FKQuesId", editDatasheetslist.get(i).getFKQuesId().toString());
                String Detailid = editDatasheetslist.get(i).getPkcssdtlsid().toString();
                DatasheetAnsData.put("PKCssDtlsID", Detailid);
                DatasheetAnsDataFinal.put(DatasheetAnsData);


            }
            DatasheetFinalobj.put("FormId", FormId);
            DatasheetFinalobj.put("Mode", "E");
            DatasheetFinalobj.put("DatasheetAnsDataFinal", DatasheetAnsDataFinal);
            if (EnvMasterId.equalsIgnoreCase("pragati")) {
                DatasheetFinalobj.put("FKActivityId", ActivityId);

            } else {
                DatasheetFinalobj.put("ActivityId", ActivityId);
            }

            //DatasheetFinalobj.put("ActivityId", ActivityId);
            if (EnvMasterId.equalsIgnoreCase("pragati")) {
                DatasheetFinalobj.put("Module", "Konnect");
            } else {
                DatasheetFinalobj.put("Module", "VWB");
            }

            DatasheetFinalobj.put("CallId", " ");
            DatasheetFinalobj.put("FormDesc", ActivityName);
            if (Designation.equalsIgnoreCase("BEO") || Designation.equalsIgnoreCase("Kendra")) {
                if (!isSave) {
                    DatasheetFinalobj.put("FlagSaveAction", "SendBack");

                } else {
                    DatasheetFinalobj.put("FlagSaveAction", "2");

                }

            } else {
                DatasheetFinalobj.put("FlagSaveAction", "2");
            }

            JSONArray jsonArray = new JSONArray();

            if (Designation.equalsIgnoreCase("School")) {

                JSONArray jsonArray1 = new JSONArray();

                for (int j = 0; j < editDatasheetslist.size(); j++) {
                    ArrDocumentReview = new JSONObject();

                    if (editDatasheetslist.get(j).getIsApprDisAppr() == null && editDatasheetslist.get(j).isAns()) {

                        String ansText = editDatasheetslist.get(j).getAnswer();
                        if (ansText != null) {
                            ArrDocumentReview.put("Remark", editDatasheetslist.get(j).getAnswer());
                        } else {
                            ArrDocumentReview.put("Remark", "");

                        }
                        ArrDocumentReview.put("PKCSSDtlsId", editDatasheetslist.get(j).getPkcssdtlsid());
                        if (SourceId == null) {
                            ArrDocumentReview.put("FKCssHeaderId", "");
                        } else {
                            ArrDocumentReview.put("FKCssHeaderId", SourceId);
                        }

                        ArrDocumentReview.put("UserMasterId", UserMasterId);
                        if (editDatasheetslist.get(j).getIsApprDisAppr() != null) {
                            ArrDocumentReview.put("ApprStatus", editDatasheetslist.get(j).getIsApprDisAppr());
                        } else {
                            ArrDocumentReview.put("ApprStatus", "");
                        }
                        ArrDocumentReview.put("Assigned_Count", "");

                        jsonArray1.put(ArrDocumentReview);
                        DatasheetFinalobj.put("ArrDocumentReview", jsonArray1);

                    } else if (editDatasheetslist.get(j).getIsApprDisAppr() != null && editDatasheetslist.get(j).isAns()) {
                        String ansText = editDatasheetslist.get(j).getAnswer();
                        if (ansText != null) {
                            ArrDocumentReview.put("Remark", editDatasheetslist.get(j).getAnswer());
                        } else {
                            ArrDocumentReview.put("Remark", "");

                        }
                        ArrDocumentReview.put("PKCSSDtlsId", editDatasheetslist.get(j).getPkcssdtlsid());
                        if (SourceId == null) {
                            ArrDocumentReview.put("FKCssHeaderId", "");
                        } else {
                            ArrDocumentReview.put("FKCssHeaderId", SourceId);
                        }

                        ArrDocumentReview.put("UserMasterId", UserMasterId);
                      /*  if (editDatasheetslist.get(j).getIsApprDisAppr() != null) {
                            ArrDocumentReview.put("ApprStatus", editDatasheetslist.get(j).getIsApprDisAppr());
                        } else {*/
                        ArrDocumentReview.put("ApprStatus", "");
                        // }
                        ArrDocumentReview.put("Assigned_Count", "");

                        jsonArray1.put(ArrDocumentReview);
                        DatasheetFinalobj.put("ArrDocumentReview", jsonArray1);

                    } else if (editDatasheetslist.get(j).getIsApprDisAppr() == null || editDatasheetslist.get(j).getIsApprDisAppr().equalsIgnoreCase("Approved")) {

                    } else {
                        DatasheetFinalobj.put("ArrDocumentReview", jsonArray1);
                    }
                }

                // DatasheetFinalobj.put("ArrDocumentReview", jsonArray1);
            } else {
                JSONArray jsonArray1 = new JSONArray();

                for (int j = 0; j < editListObj.size(); j++) {
                    ArrDocumentReview = new JSONObject();

                    ArrDocumentReview.put("Remark", editListObj.get(j).getRemark());
                    ArrDocumentReview.put("PKCSSDtlsId", editListObj.get(j).getFkcssdtlsid());
                    ArrDocumentReview.put("FKCssHeaderId", editListObj.get(j).getHeaderId());
                    ArrDocumentReview.put("UserMasterId", editListObj.get(j).getUsermasterid());
                    ArrDocumentReview.put("ApprStatus", editListObj.get(j).getIsApprDisAppr());
                    jsonArray1.put(ArrDocumentReview);

                }
                DatasheetFinalobj.put("ArrDocumentReview", jsonArray1);
            }


            FinalObj = DatasheetFinalobj.toString();
            //FinalObj = FinalObj.replaceAll("\\\\", "");

            String url = CompanyURL + WebUrlClass.api_save_datasheet;
            String op = ActivityName;
            remark = "Datasheet save successfully for ";
            CreateOfflinedatasheetfill_Appr(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);


        } catch (Exception e) {
            e.printStackTrace();
        }


        ///api/DatasheetEntryAPI/CheckAssignCount?ActivityId
        //if gone from extension officer then send to BEO else normal flow

        /*   if(btnClicked){*/
        if (isSave) {

            if (Designation.equalsIgnoreCase("School")) {

                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadAssignCount().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(EditDatasheetActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    Toast.makeText(EditDatasheetActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                ///api/DatasheetEntryAPI/CheckAssignCount' (contains exofficer) ->change status(22) and lastlevel
                //     else{ change status(21)
                //     new DownloadHierarchyCount().execute(SourceId);
                //           getReportingTo

            } else if (Designation.equalsIgnoreCase("Kendra")
                    || Designation.equalsIgnoreCase("Extension Officer")) {
                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            String status = "20";
                            new DownloadChangeStatus().execute(status);
                            new getReportingTo().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(EditDatasheetActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(EditDatasheetActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                //change status(21)
                //getreporting to
            } else if (Designation.equalsIgnoreCase("BEO")) {
            }

        } else {

            if (Designation.equalsIgnoreCase("School")) {
            } else if (Designation.equalsIgnoreCase("Kendra")) {
                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            String status = "11";
                            new DownloadChangeStatus().execute(status);
                            new lastLevel_Reassign().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(EditDatasheetActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(EditDatasheetActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                //changestatus( )->lastlevel

            } else if (Designation.equalsIgnoreCase("BEO")) {

                if (ut.isNet(context)) {

                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            //new DownloadAssignCount().execute();
                            String status;
                            status = "20";

                            final String finalStatus = status;
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {

                                    new DownloadChangeStatus().execute(finalStatus);
                                    new lastLevel_Reassign().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    Toast.makeText(EditDatasheetActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(EditDatasheetActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(EditDatasheetActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                    /*checkassignmentcoucnt
                            containds ext officer then
                            changestatus(11)
                                    lastlevel
                }*/


            }
            // }


        }





            /*if (ut.isNet(context)) {
                new StartSession(this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        if (Designation.equalsIgnoreCase("BEO")){
                            //check assignment if contains ex.officer then send to school lastlevel
                            new lastLevel_Reassign().execute();
                        }else if(Designation.equalsIgnoreCase("Kendra") ||
                                Designation.equalsIgnoreCase("Extension Officer")) {
                            if(btn_SaveDraft.isClickable() || Designation.equalsIgnoreCase("Kendra")){
                                new lastLevel_Reassign().execute();
                            }else{
                                new getReportingTo().execute();
                            }

                        } else if(Designation.equalsIgnoreCase("School")) {

                            // check assignment if BEO present then reassign to it

                            new DownloadHierarchyCount().execute(SourceId);

                            //new reassignActivity().execute();
                        }


                        //http://d207.ekatm.com/api/TimesheetAPI/GetActReassignDtl?ActivityId=87951F1C-675F-4D2B-B3FB-F43320151BCA
                        //http://d207.ekatm.com/api/DatasheetEntryAPI/GetReportingTo (not used)
                        //http://d207.ekatm.com/api/TimesheetAPI/PostReassignActivity

                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }*/

    }

    private void getDatasheetJSONObj_Draft() {
        try {
            for (int i = 0; i < editDatasheetslist.size(); i++) {
                DatasheetAnsData = new JSONObject();

                String Questiontext = editDatasheetslist.get(i).getQuesText();
                String ansText = editDatasheetslist.get(i).getAnswer();

                DatasheetAnsData.put("QuesText", editDatasheetslist.get(i).getQuesText().toString());
                DatasheetAnsData.put("Remarks", " ");

                if (ansText != null) {
                    DatasheetAnsData.put("ResponseByCustomer", editDatasheetslist.get(i).getAnswer().toString());
                } else {
                    DatasheetAnsData.put("ResponseByCustomer", "");
                }
                DatasheetAnsData.put("FKCssHeaderId", SourceId);
                DatasheetAnsData.put("PKCssFormsQuesID", editDatasheetslist.get(i).getPKCssFormsQuesID());
                if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Text")) {
                    DatasheetAnsData.put("Flag", "T");
                } else if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Selection")) {
                    DatasheetAnsData.put("Flag", "R");
                } else if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Numeric")) {
                    DatasheetAnsData.put("Flag", "T");
                } else {

                }
                DatasheetAnsData.put("FKQuesId", editDatasheetslist.get(i).getFKQuesId().toString());
                String Detailid = editDatasheetslist.get(i).getPkcssdtlsid().toString();
                DatasheetAnsData.put("PKCssDtlsID", Detailid);
                DatasheetAnsDataFinal.put(DatasheetAnsData);


            }
            DatasheetFinalobj.put("FormId", FormId);
            DatasheetFinalobj.put("Mode", "E");
            DatasheetFinalobj.put("DatasheetAnsDataFinal", DatasheetAnsDataFinal);
            if (EnvMasterId.equalsIgnoreCase("pragati")) {
                DatasheetFinalobj.put("FKActivityId", ActivityId);

            } else {
                DatasheetFinalobj.put("ActivityId", ActivityId);
            }

            //DatasheetFinalobj.put("ActivityId", ActivityId);
            if (EnvMasterId.equalsIgnoreCase("pragati")) {
                DatasheetFinalobj.put("Module", "Konnect");
            } else {
                DatasheetFinalobj.put("Module", "VWB");
            }

            DatasheetFinalobj.put("CallId", " ");
            DatasheetFinalobj.put("FormDesc", ActivityName);
            if (Designation.equalsIgnoreCase("BEO")) {
                DatasheetFinalobj.put("FlagSaveAction", "2");
            } else {
                DatasheetFinalobj.put("FlagSaveAction", "3");
            }

            FinalObj = DatasheetFinalobj.toString();
            //FinalObj = FinalObj.replaceAll("\\\\", "");

            String url = CompanyURL + WebUrlClass.api_save_datasheet;
            String status = "";
            String op = status;
            remark = "Datasheet save successfully for " + ActivityName;
            CreateOfflinedatasheetfill(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
            /* new StartSession(EditDatasheetActivityMain.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UploadDatasheet().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(),msg);

                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getDatasheetJSONObj_Remark() {
        try {
            for (int i = 0; i < editDatasheetslist.size(); i++) {
                DatasheetAnsData = new JSONObject();

                String Questiontext = editDatasheetslist.get(i).getQuesText();
                String ansText = editDatasheetslist.get(i).getAnswer();

                DatasheetAnsData.put("QuesText", editDatasheetslist.get(i).getQuesText().toString());
                DatasheetAnsData.put("Remarks", " ");

                if (ansText != null) {
                    DatasheetAnsData.put("ResponseByCustomer", editDatasheetslist.get(i).getAnswer().toString());
                } else {
                    DatasheetAnsData.put("ResponseByCustomer", "");
                }
                DatasheetAnsData.put("FKCssHeaderId", SourceId);
                DatasheetAnsData.put("PKCssFormsQuesID", editDatasheetslist.get(i).getPKCssFormsQuesID());
                if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Text")) {
                    DatasheetAnsData.put("Flag", "T");
                } else if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Selection")) {
                    DatasheetAnsData.put("Flag", "R");
                } else if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Numeric")) {
                    DatasheetAnsData.put("Flag", "T");
                } else {

                }
                DatasheetAnsData.put("FKQuesId", editDatasheetslist.get(i).getFKQuesId().toString());
                String Detailid = editDatasheetslist.get(i).getPkcssdtlsid().toString();
                DatasheetAnsData.put("PKCssDtlsID", Detailid);
                DatasheetAnsDataFinal.put(DatasheetAnsData);


            }
            DatasheetFinalobj.put("FormId", FormId);
            DatasheetFinalobj.put("Mode", "E");
            DatasheetFinalobj.put("DatasheetAnsDataFinal", DatasheetAnsDataFinal);
            if (EnvMasterId.equalsIgnoreCase("pragati")) {
                DatasheetFinalobj.put("FKActivityId", ActivityId);

            } else {
                DatasheetFinalobj.put("ActivityId", ActivityId);
            }

            //DatasheetFinalobj.put("ActivityId", ActivityId);
            if (EnvMasterId.equalsIgnoreCase("pragati")) {
                DatasheetFinalobj.put("Module", "Konnect");
            } else {
                DatasheetFinalobj.put("Module", "VWB");
            }

            DatasheetFinalobj.put("CallId", " ");
            DatasheetFinalobj.put("FormDesc", ActivityName);

            DatasheetFinalobj.put("FlagSaveAction", "3");


            if (Designation.equalsIgnoreCase("School")) {

                JSONArray jsonArray1 = new JSONArray();

                for (int j = 0; j < editDatasheetslist.size(); j++) {
                    ArrDocumentReview = new JSONObject();

                    if (editDatasheetslist.get(j).getIsApprDisAppr().equalsIgnoreCase("Approved")) {

                    } else {

                        if (editDatasheetslist.get(j).isAns()) {

                            String ansText = editDatasheetslist.get(j).getAnswer();
                            if (ansText != null) {
                                ArrDocumentReview.put("Remark", editDatasheetslist.get(j).getAnswer());
                            } else {
                                ArrDocumentReview.put("Remark", "");

                            }
                            ArrDocumentReview.put("PKCSSDtlsId", editDatasheetslist.get(j).getPkcssdtlsid());
                            if (SourceId == null) {
                                ArrDocumentReview.put("FKCssHeaderId", "");
                            } else {
                                ArrDocumentReview.put("FKCssHeaderId", SourceId);
                            }

                            ArrDocumentReview.put("UserMasterId", UserMasterId);
                           /* if(editDatasheetslist.get(j).getIsApprDisAppr() != null) {
                                ArrDocumentReview.put("ApprStatus", editDatasheetslist.get(j).getIsApprDisAppr());
                            }else{*/
                            ArrDocumentReview.put("ApprStatus", "");
                            //  }
                            ArrDocumentReview.put("Assigned_Count", "");

                            jsonArray1.put(ArrDocumentReview);
                        }

                        DatasheetFinalobj.put("ArrDocumentReview", jsonArray1);
                    }
                }

            } else {

                JSONArray jsonArray1 = new JSONArray();
                for (int j = 0; j < editListObj.size(); j++) {
                    ArrDocumentReview = new JSONObject();
                    ArrDocumentReview.put("Remark", editListObj.get(j).getRemark());
                    ArrDocumentReview.put("PKCSSDtlsId", editListObj.get(j).getFkcssdtlsid());
                    ArrDocumentReview.put("FKCssHeaderId", editListObj.get(j).getHeaderId());
                    ArrDocumentReview.put("UserMasterId", editListObj.get(j).getUsermasterid());
                    ArrDocumentReview.put("ApprStatus", editListObj.get(j).getIsApprDisAppr());

                    jsonArray1.put(ArrDocumentReview);
                }

                DatasheetFinalobj.put("ArrDocumentReview", jsonArray1);
            }

            FinalObj = DatasheetFinalobj.toString();
            //FinalObj = FinalObj.replaceAll("\\\\", "");

            String url = CompanyURL + WebUrlClass.api_save_datasheet;
            String status = "";
            String op = ActivityName;
            remark = "Datasheet save successfully for ";
            CreateOfflinedatasheetfill(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

            /* new StartSession(EditDatasheetActivityMain.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UploadDatasheet().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(),msg);

                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class DownloadAssignCount extends AsyncTask<String, Void, String> {

        String res = "";
        String response = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String url = CompanyURL + WebUrlClass.api_checkAssignCount + "?ActivityId=" + ActivityId;

            res = ut.OpenConnection(url, getApplicationContext());
            if (res != null) {
            }

            response = res;

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (response == null || response.equals("[]") || response.equals("")) {

                if (ut.isNet(context)) {
                    // final String status = "21";
                    //flow check again on 12 Mar 2020
                    final String status = "20";
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadChangeStatus().execute(status);
                            /* new DownloadHierarchyCount().execute(SourceId);*/
                            new getReportingTo().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }


            } else {

                try {
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jsonObject = jResults.getJSONObject(i);

                        String id = jsonObject.getString("Code");

                        codeArrayList.add(id);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String status = "";
                int postn = -1;

                for (int j = 0; j < codeArrayList.size(); j++) {
                    Log.i("level::", codeArrayList.get(j));
                    if (codeArrayList.get(j).equalsIgnoreCase("Extension Officer")) {
                        postn = j;

                    }
                }

                if (postn != -1) {
                    if (ut.isNet(context)) {
                        if (Designation.equalsIgnoreCase("School")) {
                            status = "21";
                            //for first time 20
                        } else if (Designation.equalsIgnoreCase("BEO")) {
                            status = "11";
                        }

                        final String finalStatus = status;
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                new DownloadChangeStatus().execute(finalStatus);
                                new lastLevel_Reassign().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                } else {
                    status = "21";

                    if (ut.isNet(context)) {
                        final String finalStatus1 = status;
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadChangeStatus().execute(finalStatus1);
                                // new DownloadHierarchyCount().execute(SourceId);
                                new getReportingTo().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                }


            }


        }
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

                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new lastLevel_Reassign().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }


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
    }


    public class lastLevel_Reassign extends AsyncTask<String, Void, String> {

        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = null;
                if (Designation.equalsIgnoreCase("BEO") || Designation.equalsIgnoreCase("Kendra")) {
                    url = CompanyURL + WebUrlClass.api_getlastLevel + "?ActivityId=" + ActivityId + "&Designation=school";
                } else if (Designation.equalsIgnoreCase("School")) {
                    url = CompanyURL + WebUrlClass.api_getlastLevel + "?ActivityId=" + ActivityId + "&Designation=BEO";
                }


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

                    issuedTo = jsonObject.getString("UserMasterId");
                    String Description = jsonObject.getString("Description");
                    reassignUserName = jsonObject.getString("UserName");
                    reassignToId = issuedTo;


                    if (issuedTo.equalsIgnoreCase("")) {
                        Toast.makeText(EditDatasheetActivityMain.this, "Datasheet save successfully", Toast.LENGTH_SHORT).show();
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
                    reassignToId = issuedTo;

                    if (issuedTo.equalsIgnoreCase("")) {
                        Toast.makeText(EditDatasheetActivityMain.this, "Datasheet save successfully", Toast.LENGTH_SHORT).show();
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


    public class GetIssuedToId extends AsyncTask<String, Void, String> {

        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_GetIssuedTo_Reassign + "?ActivityId=" + ActivityId;
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

                    issuedTo = jsonObject.getString("IssuedTo");

                    if (issuedTo.equalsIgnoreCase("")) {
                        getPostReassignDataJson();
                    } else {

                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new reassignActivity().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

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

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    public class reassignActivity extends AsyncTask<String, Void, String> {

        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                if (Designation.equalsIgnoreCase("BEO")) {
                    getPostReassignDataJson();
                } else if (Designation.equalsIgnoreCase("Kendra") ||
                        Designation.equalsIgnoreCase("Extension Officer")) {
                    ChangeApproverDialog_complete();
                } else if (Designation.equalsIgnoreCase("School")) {
                    String a = issuedTo;
                    ChangeApproverDialog_complete();
                    //getPostReassignDataJson();
                } else {

                    ChangeApproverDialog();
                }
            }


        }
    }

    private void ChangeApproverDialog() {
        final String[] UserName = new String[1];
        final Dialog dialog1 = new Dialog(EditDatasheetActivityMain.this);
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

                //   getPostReassignDataJson();




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
                String query = "SELECT * FROM " + db.TABLE_NEXTAPPR_SAHARA + " WHERE UserName='" + UserName[0] + "'";
                SQLiteDatabase sql = db.getWritableDatabase();
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    UserMasterIDnextAppr = cur.getString(cur.getColumnIndex("UserMasterId"));
                    senderName = cur.getString(cur.getColumnIndex("UserName"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void ChangeApproverDialog_complete() {

        final String[] UserName = new String[1];
        final Dialog dialog1 = new Dialog(EditDatasheetActivityMain.this);
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
                // btnClicked = false;
                dialog1.dismiss();
                finish();
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
                reassignUserName = UserName[0];
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

    private void attachmentDetailsDialog() {

        dialog1.setContentView(R.layout.sahara_dialog_attachments);

        ls_attachname = dialog1.findViewById(R.id.ls_attachname);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ls_attachname.setLayoutManager(layoutManager);
        sahara_attachmentDetailsAdapter = new Sahara_AttachmentDetailsAdapter(EditDatasheetActivityMain.this, attachmentList, true, "", Designation);
        ls_attachname.setAdapter(sahara_attachmentDetailsAdapter);

      /*  sahara_attachmentDetailsAdapter = new Sahara_AttachmentDetailsAdapter(EditDatasheetActivityMain.this,attachmentList);
        ls_attachname.setAdapter(sahara_attachmentDetailsAdapter);*/
        //getFilename();
        dialog1.show();
        dialog1.setCanceledOnTouchOutside(true);


    }

    private void remarksDialogSahara(ArrayList<SaharaRemarksDetailsListObj> listObjs) {

        dialog2.setContentView(R.layout.sahara_remarks_dialog);

        list_remarks = dialog2.findViewById(R.id.list_remarks);
        txt_quest = dialog2.findViewById(R.id.txt_quest);
        txt_quest.setText(editDatasheetslist.get(remarkPos).getQuesText());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        list_remarks.setLayoutManager(layoutManager);
        saharaRemarkAdapter = new SaharaRemarkAdapter(EditDatasheetActivityMain.this, listObjs, Designation);
        list_remarks.setAdapter(saharaRemarkAdapter);

      /*  sahara_attachmentDetailsAdapter = new Sahara_AttachmentDetailsAdapter(EditDatasheetActivityMain.this,attachmentList);
        ls_attachname.setAdapter(sahara_attachmentDetailsAdapter);*/
        //getFilename();
        dialog2.show();
        dialog2.setCanceledOnTouchOutside(true);


    }

    public void RemarkDialog(final String pkCssDetails, final int pos) {


        clickedImgStatus = "";
        dialog3.setContentView(R.layout.sahara_edit_remarks);
        edit_remarks = dialog3.findViewById(R.id.edit_remarks);
        rel_pending = dialog3.findViewById(R.id.rel_pending);
        rel_appr = dialog3.findViewById(R.id.rel_appr);
        rel_disappr = dialog3.findViewById(R.id.rel_disappr);
        editDatasheet = editDatasheetslist.get(pos);


        /*if(editDatasheetslist.get(pos).getPkcssdtlsid().equals(pkCssDetails)){
            editDatasheet = editDatasheetslist.get(pos);
        }else{
            for(int i = 0;i<editDatasheetslist.size();i++){
                if(editDatasheetslist.get(i).getPkcssdtlsid().equals(pkCssDetails)){
                    editDatasheet = editDatasheetslist.get(i);
                }
            }
        }*/


        if (editListObj.size() != 0) {
            for (int i = 0; i < editListObj.size(); i++) {

                if (editListObj.get(i).getFkcssdtlsid() == pkCssDetails) {

                    edit_remarks.setText(editListObj.get(i).getRemark());
                    break;
                }
            }
        }

        final String editRemark = "";
        rel_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickedImgStatus = "";
                editDatasheet.setIsApprDisAppr("Pending");
                editDatasheet.setDescr(Designation);
                editDatasheetslist.set(pos, editDatasheet);
                editDatasheetKendraAdapter.update(editDatasheetslist);
                editRemarkList(pkCssDetails, edit_remarks.getText().toString());

                dialog3.dismiss();


            }


        });

        rel_appr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedImgStatus = "Yes";
                editDatasheet.setIsApprDisAppr("Approved");
                editDatasheetslist.set(pos, editDatasheet);
                editDatasheet.setDescr(Designation);
                editDatasheetKendraAdapter.update(editDatasheetslist);
                editRemarkList(pkCssDetails, edit_remarks.getText().toString());
                dialog3.dismiss();

            }
        });

        rel_disappr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedImgStatus = "No";
                editDatasheet.setIsApprDisAppr("Disapproved");
                editDatasheetslist.set(pos, editDatasheet);
                editDatasheet.setDescr(Designation);
                editDatasheetKendraAdapter.update(editDatasheetslist);
                editRemarkList(pkCssDetails, edit_remarks.getText().toString());
                dialog3.dismiss();

            }
        });

        //getFilename();


        dialog3.show();
        dialog3.setCanceledOnTouchOutside(true);


    }

    private void editRemarkList(String pkCssDetails, String editRemark) {
        String detailsId = pkCssDetails;
        boolean isPresent = false;

        int pos = -1;

        if (editListObj.size() != 0) {

            for (int i = 0; i < editListObj.size(); i++) {
                String list_DetailsId = editListObj.get(i).getFkcssdtlsid();

                SaharaRemarksDetailsListObj editListBean = new SaharaRemarksDetailsListObj();
                editListBean.setRemark(editRemark);
                editListBean.setFkcssdtlsid(pkCssDetails);
                editListBean.setHeaderId(SourceId);
                editListBean.setUsermasterid(UserMasterId);
                editListBean.setIsApprDisAppr(clickedImgStatus);
                if (pkCssDetails.equals(editListObj.get(i).getFkcssdtlsid())) {

                    editListObj.set(i, editListBean);
                    isPresent = true;
                    break;

                } else {
                    isPresent = false;

                   /* editListObj.add(editListBean);
                    break;*/
                }

            }

            if (!isPresent) {

                SaharaRemarksDetailsListObj editListBean = new SaharaRemarksDetailsListObj();
                editListBean.setRemark(editRemark);
                editListBean.setFkcssdtlsid(pkCssDetails);
                editListBean.setHeaderId(SourceId);
                editListBean.setUsermasterid(UserMasterId);
                editListBean.setIsApprDisAppr(clickedImgStatus);
                editListObj.add(editListBean);
            }

        } else {
            SaharaRemarksDetailsListObj editListBean = new SaharaRemarksDetailsListObj();
            editListBean.setRemark(editRemark);
            editListBean.setFkcssdtlsid(pkCssDetails);
            editListBean.setHeaderId(SourceId);
            editListBean.setUsermasterid(UserMasterId);
            editListBean.setIsApprDisAppr(clickedImgStatus);
            editListObj.add(editListBean);
        }

    }


    private void attachmentDetailsDialog1(int selectedPos) {

        dialog1.setContentView(R.layout.sahara_dialog_attachments);
        ls_attachname = dialog1.findViewById(R.id.ls_attachname);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ls_attachname.setLayoutManager(layoutManager);
     /*   sahara_attachmentDetailsAdapter = new Sahara_AttachmentDetailsAdapter
                (EditDatasheetActivityMain.this, editDatasheetslist , true);*/


        sahara_attachmentDetailsAdapter = new Sahara_AttachmentDetailsAdapter(EditDatasheetActivityMain.this,
                editDatasheetslist.get(selectedPos).getFilePathName(), editDatasheetslist,
                true, selectedPos, "fromEdit");
        ls_attachname.setAdapter(sahara_attachmentDetailsAdapter);
      /*  sahara_attachmentDetailsAdapter = new Sahara_AttachmentDetailsAdapter(EditDatasheetActivityMain.this,attachmentList);
        ls_attachname.setAdapter(sahara_attachmentDetailseAdapter);*/
        //getFilename();
        dialog1.show();
        dialog1.setCanceledOnTouchOutside(true);
    }


    public void getFilename() {
        SQLiteDatabase sql = db.getWritableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        String query = "SELECT * FROM " + db.TABLE_ATTACHMENT_DETAILS;
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                arrayList.add(cur.getString(cur.getColumnIndex("AttachFilename")));
            } while (cur.moveToNext());


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
            ArrayAdapter<String> adapter = new ArrayAdapter(EditDatasheetActivityMain.this, android.R.layout.simple_spinner_item, Workspace_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_appr.setAdapter(adapter);

        }
    }

    public void getAppr_complete() {
        SQLiteDatabase sql = db.getWritableDatabase();
        ArrayList<String> apprv_List = new ArrayList<>();
       /* String query = "SELECT * FROM " + db.TABLE_CHAT_USER_LIST + " WHERE UserMasterId="+ issuedTo;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                apprv_List.add(cur.getString(cur.getColumnIndex("UserName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(EditDatasheetActivityMain.this, android.R.layout.simple_spinner_item, apprv_List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_appr.setAdapter(adapter);

        }*/

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

        ArrayAdapter<String> adapter = new ArrayAdapter(EditDatasheetActivityMain.this, android.R.layout.simple_spinner_item, apprv_List);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_appr.setAdapter(adapter);
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
                if (Designation.equalsIgnoreCase("BEO") || Designation.equalsIgnoreCase("Kendra")
                        || Designation.equalsIgnoreCase("Extension Officer") ||
                        Designation.equalsIgnoreCase("School")) {
                    approvalJsonData.put("IssuedTo", reassignToId);//30429178-6241-4679-83bb-96e419ac5942 taluka
                } else {
                    approvalJsonData.put("IssuedTo", approval_list.get(i).getUserMasterId());//30429178-6241-4679-83bb-96e419ac5942 taluka
                }

                approvalJsonData.put("ActivityCode", "CSSDetail");//39e7673a-5d0c-4ee2-bcd6-501770fdb833 school id
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
                    new StartSession(EditDatasheetActivityMain.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new UploadReassignDataURLJSON().execute(finalApprObj);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);

                        }
                    });

                } else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


                /*String status = "";
                String url;
                if (issuedTo.equalsIgnoreCase("")) {
                    url = CompanyURL + WebUrlClass.api_save_datasheet;
                } else {
                    url = CompanyURL + WebUrlClass.api_reassign_datasheet;
                }

                String op = "is Reassigned Sucessfully to " + reassignUserName;
                if (Designation.equalsIgnoreCase("BEO") || Designation.equalsIgnoreCase("Kendra")
                        || Designation.equalsIgnoreCase("Extension Officer") ||
                        Designation.equalsIgnoreCase("School")) {

                    remark = ActivityName;
                } else {
                    remark = "Activity Reassign successfully";
                }
                *//*approval_list.get(i).getActivityName();*//*
                if (reassignToId.equals("")) {

                } else {
                    CreateOfflinedatasheetfill(url, finalApprObj, WebUrlClass.POSTFLAG, remark, op);
                    Toast.makeText(EditDatasheetActivityMain.this, "Activity Reassign successfully", Toast.LENGTH_LONG).show();
                    finish();
                }*/


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
            //showProgressDialog();
        }


        @Override
        protected String doInBackground(String... params) {
            String finalApprObj = params[0];
            String url = CompanyURL + WebUrlClass.api_reassign_datasheet;
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
                String msg = "Datasheet Saved Sucessfully and Send To  " + reassignUserName;

//                Datasheet Saved Successfully and Forwarded to Kendra!
                Toast.makeText(EditDatasheetActivityMain.this, msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditDatasheetActivityMain.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                // intent.putExtra("fromEdit",0);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // btnClicked = false;
                // finish();

            } else {
                //btnClicked = false;
                Toast.makeText(EditDatasheetActivityMain.this, "Failed to Forward Activity to " + senderName, Toast.LENGTH_LONG).show();
                finish();
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

    class UploadDatasheet extends AsyncTask<Integer, Void, Integer> {
        Object respond;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (res.contains("")) {
                Toast.makeText(EditDatasheetActivityMain.this, "Datasheet Uploaded successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditDatasheetActivityMain.this, ActivityMain.class);
                startActivity(intent);
                finish();
                dismissProgressDialog();

            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {

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

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        lstquestions_editdatasheet = (ListView) findViewById(R.id.lstquestions_editdatasheet);
        sql = db.getWritableDatabase();
        btnReturn = findViewById(R.id.btnReturn);
        btnSave = findViewById(R.id.btnSave);
        btn_SaveDraft = findViewById(R.id.btn_SaveDraft);
        btn_remarks = findViewById(R.id.btn_remarks);
        progressBar1 = findViewById(R.id.progressBar1);
        ln_list = findViewById(R.id.ln_list);
        DatasheetAnsDataFinal = new JSONArray();
        DatasheetFinalobj = new JSONObject();

        saharaRemarksBeanHashMap = new HashMap<>();
        listObjs = new ArrayList<>();


        chatUserArrayList = new ArrayList<>();
        codeArrayList = new ArrayList<>();


        if (Constants.type == Constants.Type.Sahara || Constants.type == Constants.Type.ZP) {
            if (Designation.equalsIgnoreCase("BEO")) {
                btn_SaveDraft.setVisibility(View.VISIBLE);
                // btn_remarks.setVisibility(View.VISIBLE);

                btn_SaveDraft.setText("Send Back");
                btnSave.setText("Complete");
                // btn_remarks.setText("Save Remarks");
                btnReturn.setText("Save Remarks");

                btnSave.setTextSize(12.00f);
                btn_SaveDraft.setTextSize(12.00f);
                btnReturn.setTextSize(12.00f);
                btnReturn.setBackgroundColor(Color.parseColor("#23b7e5"));

            } else if (Designation.contains("Kendra")) {
                btn_SaveDraft.setVisibility(View.VISIBLE);
                btn_SaveDraft.setText("Send Back");
                btnSave.setText("Send Forward");
                btnSave.setTextSize(12.00f);
                btn_SaveDraft.setTextSize(12.00f);
                btnReturn.setText("Save Remarks");
                btnReturn.setTextSize(12.00f);
                btnReturn.setBackgroundColor(Color.parseColor("#23b7e5"));
            } else if (Designation.equalsIgnoreCase("Extension Officer")) {
                btn_SaveDraft.setVisibility(View.GONE);
                btnSave.setText("Send Forward");
                btn_SaveDraft.setText("Save Remarks");
                btnReturn.setText("Cancel");
                btnReturn.setTextSize(12.00f);
                // btnReturn.setBackgroundColor(Color.parseColor("#23b7e5"));
            } else if (Designation.equalsIgnoreCase("School")) {
                btnSave.setText("Send Forward");
                btn_SaveDraft.setText("Save Remarks");
                btnReturn.setText("Cancel");
                btnReturn.setTextSize(12.00f);
            } else {
                btn_SaveDraft.setVisibility(View.VISIBLE);
                btnSave.setText("Submit for Approval");
                btnSave.setTextSize(11.00f);
                btn_SaveDraft.setTextSize(12.00f);
                btnReturn.setTextSize(12.00f);
            }
        }
        else{
            btn_SaveDraft.setVisibility(View.GONE);
            btnSave.setText("Submit");
            btnReturn.setText("Cancel");
        }


        editDatasheetKendraAdapter = new EditDatasheetKendraAdapter(EditDatasheetActivityMain.this, editDatasheetslist, flagFromTeam, Designation);
        lstquestions_editdatasheet.setAdapter(editDatasheetKendraAdapter);

    }

    private void updatelist() {

        editDatasheetslist = getdata();

        String data = "";

        if(Constants.type == Constants.Type.Sahara || Constants.type == Constants.Type.ZP) {

            if (flagFromTeam == 1 && Designation.equalsIgnoreCase("school")) {
                ln_list.setVisibility(View.VISIBLE);
                editDatasheetAdapter = new EditDatasheetAdapter(EditDatasheetActivityMain.this, editDatasheetslist, flagFromTeam, Designation);
                lstquestions_editdatasheet.setAdapter(editDatasheetAdapter);
            } else {


                for (int i = 0; i < pkCssDetailID.size(); i++) {

                    if (ut.isNet(context)) {
                        data = pkCssDetailID.get(i);
                        final String finalData = data;
                        final int finalI = i;
                        final int finalI1 = i;
                        new StartSession(EditDatasheetActivityMain.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadRemarkDetails().execute(finalData, String.valueOf(finalI1));
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        }else{
            ln_list.setVisibility(View.VISIBLE);
            editDatasheetAdapter = new EditDatasheetAdapter(EditDatasheetActivityMain.this, editDatasheetslist, 1, Designation);
            lstquestions_editdatasheet.setAdapter(editDatasheetAdapter);
        }
        if (fromFlag) {

        } else {

            //   editDatasheetAdapter.update(editDatasheetslist);
        }


    }

    private void updateKendraList() {
        ln_list.setVisibility(View.VISIBLE);
        editDatasheetslist = getdata();
        String data = "";

        for (int i = 0; i < pkCssDetailID.size(); i++) {

            if (ut.isNet(context)) {
                data = pkCssDetailID.get(i);
                final String finalData = data;
                final int finalI = i;
                final int finalI1 = i;
                new StartSession(EditDatasheetActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadRemarkDetails().execute(finalData, String.valueOf(finalI1));
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
        if (fromFlag) {

        } else {

            editDatasheetKendraAdapter.update(editDatasheetslist);
        }


    }


    public class DownloadRemarkDetails extends AsyncTask<String, Void, String> {

        Object res;
        String response = "";
        String cssdetails = "";
        String type = "";
        int lastSize = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            cssdetails = params[0];
            lastSize = Integer.parseInt(params[1]);
            //type = params[1];
            ArrayList<SaharaRemarksDetailsListObj> listObjs;

            String url = CompanyURL + WebUrlClass.api_remarks_sahara + "?PkCssdetailid=" + cssdetails;
            res = ut.OpenConnection(url, EditDatasheetActivityMain.this);
            response = res.toString();
            if (response.equals("[]")) {
                return response;
            } else {
                SaharaRemarksDetailsListObj remarksDetailsListObj;
                if (fromFlag) {
                    return response;

                } else {
                    try {
                        listObjs = new ArrayList<>();
                        SaharaRemarksBean saharaRemarksBean = null;
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            saharaRemarksBean = new SaharaRemarksBean();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            remarksDetailsListObj = new SaharaRemarksDetailsListObj();
                            remarksDetailsListObj.setFkcssdtlsid(jsonObject.getString("fkcssdtlsid"));
                            remarksDetailsListObj.setUsermasterid(jsonObject.getString("usermasterid"));
                            remarksDetailsListObj.setDescr(jsonObject.getString("descr"));
                            remarksDetailsListObj.setUsername(jsonObject.getString("username"));
                            remarksDetailsListObj.setIsApprDisAppr(jsonObject.getString("IsApprDisAppr"));
                            remarksDetailsListObj.setRemark(jsonObject.getString("remark"));
                            remarksDetailsListObj.setAddeddt(jsonObject.getString("Addeddt"));
                            listObjs.add(remarksDetailsListObj);

                        }
                        saharaRemarksBean.setRemarksDetailsListObjs(listObjs);
                        saharaRemarksBeanHashMap.put(cssdetails, saharaRemarksBean);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (pkCssDetailID.size() - 1 == lastSize)
                        return response;

                    else {
                        /* response = null;*/
                        return response;
                    }

                }

            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (response != null) {

                if (response.equals("[]")) {
                    if (fromFlag) {
                        Toast.makeText(EditDatasheetActivityMain.this, "No data Present", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(EditDatasheetActivityMain.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    SaharaRemarksDetailsListObj remarksDetailsListObj;
                    if (fromFlag) {
                        if (response.equals("[]")) {
                            Toast.makeText(EditDatasheetActivityMain.this, "No data Present", Toast.LENGTH_SHORT).show();
                        } else {
                            if (Designation.equalsIgnoreCase("school")) {
                                ArrayList<SaharaRemarksDetailsListObj> listObjs = new ArrayList<>();

                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(response);


                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        remarksDetailsListObj = new SaharaRemarksDetailsListObj();
                                        remarksDetailsListObj.setFkcssdtlsid(jsonObject.getString("fkcssdtlsid"));
                                        remarksDetailsListObj.setUsermasterid(jsonObject.getString("usermasterid"));
                                        remarksDetailsListObj.setDescr(jsonObject.getString("descr"));
                                        remarksDetailsListObj.setUsername(jsonObject.getString("username"));
                                        remarksDetailsListObj.setIsApprDisAppr(jsonObject.getString("IsApprDisAppr"));
                                        remarksDetailsListObj.setRemark(jsonObject.getString("remark"));
                                        remarksDetailsListObj.setAddeddt(jsonObject.getString("Addeddt"));
                                        //listObjs.add(remarksDetailsListObj);
                                        if (remarksDetailsListObj.getDescr().equalsIgnoreCase("school")) {
                                            listObjs.add(remarksDetailsListObj);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (listObjs.size() == 0) {
                                    Toast.makeText(EditDatasheetActivityMain.this, "No Remarks Present", Toast.LENGTH_SHORT).show();
                                } else {

                                    remarksDialogSahara(listObjs);
                                }

                            } else {

                                ArrayList<SaharaRemarksDetailsListObj> listObjs = new ArrayList<>();

                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(response);


                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        remarksDetailsListObj = new SaharaRemarksDetailsListObj();
                                        remarksDetailsListObj.setFkcssdtlsid(jsonObject.getString("fkcssdtlsid"));
                                        remarksDetailsListObj.setUsermasterid(jsonObject.getString("usermasterid"));
                                        remarksDetailsListObj.setDescr(jsonObject.getString("descr"));
                                        remarksDetailsListObj.setUsername(jsonObject.getString("username"));
                                        remarksDetailsListObj.setIsApprDisAppr(jsonObject.getString("IsApprDisAppr"));
                                        remarksDetailsListObj.setRemark(jsonObject.getString("remark"));
                                        remarksDetailsListObj.setAddeddt(jsonObject.getString("Addeddt"));
                                        listObjs.add(remarksDetailsListObj);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                              /*  ArrayList<ArrayList<SaharaRemarksDetailsListObj>> listObjs1 = new ArrayList<>();
                                for(int i=0 ; i<listObjs.size() ; i++ ){
                                    if(listObjs.get(i).getDescr().equalsIgnoreCase("school")){
                                       listObjs1.add(i,listObjs);
                                    }
                                }*/
                                if (listObjs.size() == 0) {
                                    Toast.makeText(EditDatasheetActivityMain.this, "No Remarks Present", Toast.LENGTH_SHORT).show();
                                } else {
                                    remarksDialogSahara(listObjs);
                                }


                            }
                        }


                    } else {
                        Log.i("HashMap::: ", saharaRemarksBeanHashMap.toString());


                        for (int j = 0; j < editDatasheetslist.size(); j++) {

                            ArrayList<SaharaRemarksDetailsListObj> saharaRemarksDetailsListObjs = null;

                            if (saharaRemarksBeanHashMap.get(editDatasheetslist.get(j).getPkcssdtlsid()) != null) {
                                saharaRemarksDetailsListObjs = saharaRemarksBeanHashMap.get(editDatasheetslist.get(j).getPkcssdtlsid()).getRemarksDetailsListObjs();

                                if (saharaRemarksDetailsListObjs != null && saharaRemarksDetailsListObjs.size() != 0) {
                                    SaharaRemarksDetailsListObj detailsListObj = saharaRemarksDetailsListObjs.get(0);

                                    if (detailsListObj.getIsApprDisAppr().equalsIgnoreCase("Pending") &&
                                            !detailsListObj.getDescr().equalsIgnoreCase(Designation)) {
                                        if (saharaRemarksDetailsListObjs.size() > 1) {
                                            SaharaRemarksDetailsListObj detailsListObj1 = saharaRemarksDetailsListObjs.get(1);
                                            Log.i("pkId ::", editDatasheetslist.get(j).getPkcssdtlsid());
                                            Log.i("pkIdObject ::", new Gson().toJson(detailsListObj1));
                                            editDatasheetslist.get(j).setDescr(detailsListObj1.getDescr());
                                            editDatasheetslist.get(j).setUsername(detailsListObj1.getUsername());
                                            editDatasheetslist.get(j).setIsApprDisAppr(detailsListObj1.getIsApprDisAppr());
                                            editDatasheetslist.get(j).setRemark(detailsListObj1.getRemark());

                                            if (editDatasheetslist.get(j).getRemark() == null /*||
                                                    editDatasheetslist.get(j).getRemark().equals("")*/) {
                                                editDatasheetslist.get(j).setRemarks(isRemark);
                                            } else {
                                                isRemark = true;
                                                editDatasheetslist.get(j).setRemarks(isRemark);
                                            }
                                            editDatasheetslist.get(j).setAddeddt(detailsListObj1.getAddeddt());

                                            if (isRemark) {
                                                isRemark = false;
                                            }
                                        } else {
                                            Log.i("pkId ::", editDatasheetslist.get(j).getPkcssdtlsid());
                                            Log.i("pkIdObject ::", new Gson().toJson(detailsListObj));
                                            editDatasheetslist.get(j).setDescr(detailsListObj.getDescr());
                                            editDatasheetslist.get(j).setUsername(detailsListObj.getUsername());
                                            editDatasheetslist.get(j).setIsApprDisAppr(detailsListObj.getIsApprDisAppr());
                                            editDatasheetslist.get(j).setRemark(detailsListObj.getRemark());

                                            if (editDatasheetslist.get(j).getRemark() == null /*||
                                                    editDatasheetslist.get(j).getRemark().equals("")*/) {
                                                editDatasheetslist.get(j).setRemarks(isRemark);
                                            } else {
                                                isRemark = true;
                                                editDatasheetslist.get(j).setRemarks(isRemark);
                                            }
                                            editDatasheetslist.get(j).setAddeddt(detailsListObj.getAddeddt());

                                            if (isRemark) {
                                                isRemark = false;
                                            }
                                        }

                                    } else {
                                        Log.i("pkId ::", editDatasheetslist.get(j).getPkcssdtlsid());
                                        Log.i("pkIdObject ::", new Gson().toJson(detailsListObj));
                                        editDatasheetslist.get(j).setDescr(detailsListObj.getDescr());
                                        editDatasheetslist.get(j).setUsername(detailsListObj.getUsername());
                                        editDatasheetslist.get(j).setIsApprDisAppr(detailsListObj.getIsApprDisAppr());
                                        editDatasheetslist.get(j).setRemark(detailsListObj.getRemark());

                                        if (editDatasheetslist.get(j).getRemark() == null /*||
                                                editDatasheetslist.get(j).getRemark().equals("")*/) {
                                            editDatasheetslist.get(j).setRemarks(isRemark);
                                        } else {
                                            isRemark = true;
                                            editDatasheetslist.get(j).setRemarks(isRemark);
                                        }
                                        editDatasheetslist.get(j).setAddeddt(detailsListObj.getAddeddt());

                                        if (isRemark) {
                                            isRemark = false;
                                        }
                                    }

                                    //  detailsListObj = saharaRemarksDetailsListObjs.get(1);


                                }
                            }


                        }
                    }
                }
                /* if(pkCssDetailID.size() - 1 == lastSize){*/
                if (fromFlag) {

                } else {
                    if (Designation.equalsIgnoreCase("School")) {
                        ln_list.setVisibility(View.VISIBLE);
                        editDatasheetAdapter = new EditDatasheetAdapter(EditDatasheetActivityMain.this, editDatasheetslist, flagFromTeam, Designation);
                        lstquestions_editdatasheet.setAdapter(editDatasheetAdapter);
                    } else {
                        ln_list.setVisibility(View.VISIBLE);
                        //   Parcelable state = lstquestions_editdatasheet.onSaveInstanceState();
                        editDatasheetKendraAdapter.update(editDatasheetslist);
                        // lstquestions_editdatasheet.onRestoreInstanceState(state);
                    }
                }
            }
           /* } else {
                //   Toast.makeText(EditDatasheetActivityMain.this, "Server Error", Toast.LENGTH_SHORT).show();
            }*/


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
//            showProgressDialog();
            progressBar1.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_edit_datasheet + "?FormId=" +
                        URLEncoder.encode(FormId, "UTF-8") + "&HeaderId=" + URLEncoder.encode(SourceId, "UTF-8");


                /*String url = CompanyURL + WebUrlClass.api_edit_datasheet + "?FormId=" +
                        URLEncoder.encode(FormId, "UTF-8")  + "&HeaderId=" + URLEncoder.encode(HeaderId, "UTF-8") ;*/

                res = ut.OpenConnection(url, getApplicationContext());
               /* res = res.toString().replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);*/
                JSONArray jResults = new JSONArray(res.toString());
                ContentValues values = new ContentValues();
                sql.delete(db.TABLE_EDIT_DATASHEET, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_EDIT_DATASHEET, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int index = 0; index < jResults.length(); index++) {
                    JSONObject jorder = jResults.getJSONObject(index);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("SourceId")) {
                            columnValue = SourceId;
                        } else if (columnName.equalsIgnoreCase("FormId")) {
                            columnValue = FormId;
                        } else {
                            columnValue = jorder.getString(columnName);
                        }
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_EDIT_DATASHEET, null, values);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Error in Edit", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
//            dismissProgressDialog();

            if (res.equals("[]")) {
                progressBar1.setVisibility(View.GONE);
                ln_list.setVisibility(View.VISIBLE);
                Toast.makeText(EditDatasheetActivityMain.this, "No Questions Present", Toast.LENGTH_LONG).show();

            } else {
                progressBar1.setVisibility(View.GONE);
                if (Designation.equalsIgnoreCase("Kendra") ||
                        Designation.equalsIgnoreCase("Extension Officer") ||
                        Designation.equalsIgnoreCase("BEO")) {
                    updateKendraList();
                } else {
                    updatelist();
                }

            }
        }

    }


    private ArrayList<EditDatasheet> getdata() {
        results = new ArrayList<EditDatasheet>();
        pkCssDetailID = new ArrayList<String>();


        Cursor c = sql.rawQuery(
                "SELECT distinct SequenceNo, ExpectedResponse, QuesText ,"
                        + "FKQuesId, PKCssFormsQuesID, Weightage, IsResponseMandatory,"
                        + "SelectionText, SelectionValue, ValueMin, ValueMax, MaxValueText,"
                        + "ResponseType, ResponseValue, SelectionType, ControlWidth,"
                        + "MaxNoOfResponses, MaxExpectedResponse, pkcssdtlsid, FKCSSheaderid,"
                        + "	responsebycustomer, selectionvalue1,SourceId,IsApproved,Attachment,AttachmentCount,FormId,IsSingleAttachment from "
                        + db.TABLE_EDIT_DATASHEET + " where SourceId ='"
                        + SourceId + "'  ORDER BY SequenceNo asc",
                null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                editDatasheet = new EditDatasheet();
                editDatasheet.setSequenceNo(Integer.parseInt(c.getString(c.getColumnIndex("SequenceNo"))));
                editDatasheet.setExpectedResponse(c.getString(c.getColumnIndex("ExpectedResponse")));
                editDatasheet.setQuesText(c.getString(c.getColumnIndex("QuesText")));
                editDatasheet.setFKQuesId(c.getString(c.getColumnIndex("FKQuesId")));
                editDatasheet.setPKCssFormsQuesID(c.getString(c.getColumnIndex("PKCssFormsQuesID")));
                editDatasheet.setWeightage(c.getString(c.getColumnIndex("Weightage")));
                editDatasheet.setIsResponseMandatory(c.getString(c.getColumnIndex("IsResponseMandatory")));
                editDatasheet.setSelectionText(c.getString(c.getColumnIndex("SelectionText")));
                editDatasheet.setSelectionValue(c.getString(c.getColumnIndex("SelectionValue")));
                editDatasheet.setValueMin(c.getString(c.getColumnIndex("ValueMin")));
                editDatasheet.setValueMax(c.getString(c.getColumnIndex("ValueMax")));
                editDatasheet.setMaxValueText(c.getString(c.getColumnIndex("MaxValueText")));
                editDatasheet.setResponseType(c.getString(c.getColumnIndex("ResponseType")));
                editDatasheet.setResponseValue(c.getString(c.getColumnIndex("ResponseValue")));
                editDatasheet.setSelectionType(c.getString(c.getColumnIndex("SelectionType")));
                editDatasheet.setControlWidth(c.getString(c.getColumnIndex("ControlWidth")));
                editDatasheet.setMaxNoOfResponses(c.getString(c.getColumnIndex("MaxNoOfResponses")));
                editDatasheet.setMaxExpectedResponse(c.getString(c.getColumnIndex("MaxExpectedResponse")));
                editDatasheet.setPkcssdtlsid(c.getString(c.getColumnIndex("PKCssDtlsID")));
                editDatasheet.setFKCSSheaderid(c.getString(c.getColumnIndex("FKCssHeaderId")));
                editDatasheet.setResponsebycustomer(c.getString(c.getColumnIndex("ResponseByCustomer")));
                editDatasheet.setAnswer(c.getString(c.getColumnIndex("ResponseByCustomer")));
                editDatasheet.setSelectionvalue1(c.getString(c.getColumnIndex("SelectionValue1")));
                editDatasheet.setActivityid(c.getString(c.getColumnIndex("SourceId")));
                editDatasheet.setApprStatus(c.getString(c.getColumnIndex("IsApproved")));
                editDatasheet.setIsAttachment(c.getString(c.getColumnIndex("Attachment")));
                editDatasheet.setAttachmentCount(c.getString(c.getColumnIndex("AttachmentCount")));
                editDatasheet.setFormId(c.getString(c.getColumnIndex("FormId")));
                editDatasheet.setIsSingleAttachment(c.getString(c.getColumnIndex("IsSingleAttachment")));


                results.add(editDatasheet);
                pkCssDetailID.add(editDatasheet.getPkcssdtlsid());


            } while (c.moveToNext());


        } else {

        }
        return results;


    }


    private void CreateOfflinedatasheetfill(final String url, final String parameter,
                                            final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            SQLiteDatabase sql1 = db.getWritableDatabase();
         /*   sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?",
                    new String[]{ActivityId});*/
            Toast.makeText(EditDatasheetActivityMain.this, "Datasheet Save Successfully", Toast.LENGTH_LONG).show();
            //dismissProgressDialog();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);
            finish();
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
            sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?",
                    new String[]{ActivityId});
            Toast.makeText(EditDatasheetActivityMain.this, "Datasheet save successfully", Toast.LENGTH_LONG).show();
            //dismissProgressDialog();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved ", Toast.LENGTH_LONG).show();


        }

    }

    public void updateAppr(int pos) {

        final String pkCssDtldID = editDatasheetslist.get(pos).getPkcssdtlsid();
        final String remarkByAppr = editDatasheetslist.get(pos).getResponsebycustomer();
        int respAppr = editDatasheetslist.get(pos).getIsApproved();


        if (respAppr == 1) {
            respApprText = "Yes";
        } else {
            respApprText = "No";
        }

        if (ut.isNet(context)) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new updateApprStatus().execute(pkCssDtldID, remarkByAppr, respApprText);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

    }

    public void attachmentDetailsShow(int pos) {
        final String pkCssDtldID = editDatasheetslist.get(pos).getPkcssdtlsid();
        final int p = pos;
        rowClick = pos;
//        if(!editDatasheetslist.get(pos).getAttachmentCount().equals("0")) {
        if (!editDatasheetslist.get(pos).getAttachmentCount().equals("0")) {
            if (ut.isNet(context)) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new downloadAttachmentDetails().execute(pkCssDtldID, String.valueOf(p));
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        } else {
            Toast.makeText(context, "Sorry there is not attachment here", Toast.LENGTH_SHORT).show();
        }

    }

    public void remarksDetails(int pos, boolean res) {
        fromFlag = res;
        remarkPos = pos;
        final String pkCssDtldID = editDatasheetslist.get(pos).getPkcssdtlsid();

//        if(!editDatasheetslist.get(pos).getAttachmentCount().equals("0")) {
        if (ut.isNet(context)) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadRemarkDetails().execute(pkCssDtldID, String.valueOf(remarkPos));

                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(context, "Sorry there is not attachment here", Toast.LENGTH_SHORT).show();
        }

    }

    public void editRemark(int pos) {
        String pkCssDetails = editDatasheetslist.get(pos).getPkcssdtlsid();
        String ques = editDatasheetslist.get(pos).getQuesText();
        // editListObj = new ArrayList<>();

        RemarkDialog(pkCssDetails, pos);
    }


    public class updateApprStatus extends AsyncTask<String, Void, String> {

        String res = "";
        String respAppr = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            respAppr = params[2];
            try {
                String url = CompanyURL + WebUrlClass.api_UpdateAppr_sahara + "?PKCSSDtlsId=" + params[0] + "&CustomerResp=" + params[1] + "&flag=" + params[2];
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
        protected void onPostExecute(String res) {
            super.onPostExecute(res);

            if (respAppr.equalsIgnoreCase("Yes")) {
                Toast.makeText(EditDatasheetActivityMain.this, "Details Approve Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditDatasheetActivityMain.this, "Details Disapprove Successfully", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public class downloadAttachmentDetails extends AsyncTask<String, Void, String> {

        String res = "";
        String pkCssDetailsId = "";
        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            pkCssDetailsId = params[0];
            pos = Integer.parseInt(params[1]);
            try {
                String url = CompanyURL + WebUrlClass.api_getUploadedAttachment_Sahara + "?ActivityId=" + params[0] + "&SourceType=CSSDetail";
                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                    //res = res.substring(1, res.length() - 1);
                    // res = res.replaceAll("\\\\", "");

                }
                String s = res;


            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }


            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);

            if (res.contains("[]")) {
                if (editDatasheetslist.get(pos).getFilePathName() != null) {
                    if (editDatasheetslist.get(pos).getFilePathName().size() != 0) {
                        attachmentDetailsDialog1(pos);
                    }
                }


            } else {

                try {

                    JSONArray jResults = new JSONArray(res.toString());

                    ContentValues values = new ContentValues();
                    sql.delete(db.TABLE_ATTACHMENT_DETAILS, null, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ATTACHMENT_DETAILS, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jorder = jResults.getJSONObject(index);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_ATTACHMENT_DETAILS, null, values);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("res", e.getMessage());
                }

                attachmentList = getAttachmentDetails();

                attachmentDetailsDialog();


            }


        }
    }

    private ArrayList<AttachmentBean> getAttachmentDetails() {
        ArrayList<AttachmentBean> attachmentDetails = new ArrayList<AttachmentBean>();

        Cursor c = sql.rawQuery("SELECT * from " + db.TABLE_ATTACHMENT_DETAILS, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {

                attachmentBean = new AttachmentBean();

                attachmentBean.setPkAttachId(c.getString(c.getColumnIndex("PkAttachId")));
                attachmentBean.setAttachGuid(c.getString(c.getColumnIndex("AttachGuid")));
                attachmentBean.setAttachFilename(c.getString(c.getColumnIndex("AttachFilename")));
                attachmentBean.setPath(c.getString(c.getColumnIndex("Path")));
                attachmentBean.setActivityId(c.getString(c.getColumnIndex("ActivityId")));
                attachmentBean.setAddedBy(c.getString(c.getColumnIndex("AddedBy")));
                attachmentBean.setModifiedBy(c.getString(c.getColumnIndex("ModifiedBy")));
                attachmentBean.setModifiedDt(c.getString(c.getColumnIndex("ModifiedDt")));
                attachmentBean.setIsDeleted(c.getString(c.getColumnIndex("IsDeleted")));
                attachmentBean.setSourcetype(c.getString(c.getColumnIndex("Sourcetype")));
                attachmentBean.setGPSId(c.getString(c.getColumnIndex("GPSId")));
                attachmentBean.setAttachmentType(c.getString(c.getColumnIndex("AttachmentType")));
                attachmentBean.setLatitude(c.getString(c.getColumnIndex("Latitude")));
                attachmentBean.setLongitude(c.getString(c.getColumnIndex("Longitude")));
                attachmentBean.setAttachmentCode(c.getString(c.getColumnIndex("AttachmentCode")));
                attachmentBean.setAttachmentDesc(c.getString(c.getColumnIndex("AttachmentDesc")));

                attachmentDetails.add(attachmentBean);
            } while (c.moveToNext());

        } else {

        }


        return attachmentDetails;


    }


    @Override
    public void onBackPressed() {

        if (SystemClock.elapsedRealtime() - mLastClickTime > 5000) {
            Toast.makeText(getApplicationContext(), "Please click again ", Toast.LENGTH_SHORT).show();//18123373

        } else {
            super.onBackPressed();
        }
        mLastClickTime = SystemClock.elapsedRealtime();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1150 && resultCode == 1221) {
            if (data != null) {
                if(Constants.type == Constants.Type.ZP || Constants.type == Constants.Type.Sahara) {
                    if (Designation.equalsIgnoreCase("school")) {
                        editDatasheetslist = new Gson().fromJson(data.getStringExtra("objStr"), DatasheetListObject.class).
                                geteditDatasheets();

                        editDatasheetAdapter.update(editDatasheetslist);
                    } else {
                        editDatasheetslist = new Gson().fromJson(data.getStringExtra("objStr"), DatasheetListObject.class).geteditDatasheets();
                        editDatasheetKendraAdapter.update(editDatasheetslist);
                    }
                }else{

                    editDatasheetslist = new Gson().fromJson(data.getStringExtra("objStr"), DatasheetListObject.class).
                            geteditDatasheets();

                    editDatasheetAdapter.update(editDatasheetslist);
                }

            }

        }
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

            }
            else {

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

}
