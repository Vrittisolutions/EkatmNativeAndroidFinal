package com.vritti.chat.activity;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.vision.barcode.Barcode;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.vcrm7.CreateOpportunityActivity;
import com.vritti.crm.vcrm7.IndividualProspectusActivity;
import com.vritti.crm.vcrm7.ProspectFilterActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.vworkbench.ActivityMain;
import com.vritti.vwb.vworkbench.AssignActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;


/**
 * Created by sharvari on 20-Sep-18.
 */

public class SharefunctionActivity extends AppCompatActivity {


    Button txt_chat, txt_activity, txt_existing_activity;
    private String Imagefilename, path, Textname;
    private ProgressDialog progressDialog;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    SharedPreferences Prospectpreference;
    private static String ProspectTypeID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_sharefunction_lay);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.vworkbench);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("  Share Data");
        txt_existing_activity = (Button) findViewById(R.id.txt_existing_activity);
        txt_chat = (Button) findViewById(R.id.txt_chat);
        txt_activity = (Button) findViewById(R.id.txt_activity);

        context = getApplicationContext();
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
        Prospectpreference = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
        ProspectTypeID = Prospectpreference.getString(WebUrlClass.Key_indivisual, "");

        //   Toast.makeText(SharefunctionActivity.this, "Data send", Toast.LENGTH_SHORT).show();


        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();
        String type = intent.getType();


        // if this is from the share menu
        if (Intent.ACTION_SEND.equals(action)) {
            if (extras.containsKey(Intent.EXTRA_STREAM)) {
                try {

                    // Get resource path from intent callee

                    Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);

                    if (uri.toString().contains("content") && type.startsWith("image")) {
                        handleSendImage(intent);
                    } else {
                        File file = new File(getRealPathFromUri(SharefunctionActivity.this, uri));//create path from uri
                        path = file.toString();
                        Imagefilename = file.getName();
                    }

                    /*File file;
                    if (type.startsWith("image")){
                         file = new File(getRealPathFromUri(SharefunctionActivity.this,uri));

                    }else {

                        file = new File(getRealPathFromUri(SharefunctionActivity.this,uri));//create path from uri
                    }
*/


                    // path = file.toString();
                    //Imagefilename = file.getName();

                    txt_existing_activity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SharefunctionActivity.this, ActivityMain.class);
                            //intent.putExtra("Description",Textname);
                            intent.putExtra("Imagename", Imagefilename);
                            intent.putExtra("ImagePath", path);
                            startActivity(intent);
                            finish();
                        }
                    });
                    txt_chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SharefunctionActivity.this, OpenChatroomActivity.class);
                            intent.putExtra("Description", Textname);
                            intent.putExtra("Imagename", Imagefilename);
                            intent.putExtra("ImagePath", path);
                            startActivity(intent);
                            finish();
                            AppCommon.getInstance(SharefunctionActivity.this).setChatPostion(0);
                        }
                    });
                    txt_activity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SharefunctionActivity.this, AssignActivity.class);
                            intent.putExtra("Description", Textname);
                            intent.putExtra("Imagename", Imagefilename);
                            intent.putExtra("ImagePath", path);
                            startActivity(intent);
                            finish();


                        }
                    });

                    /*if (path!=null&Imagefilename!=null){
                        startActivity(new Intent(OpenChatroomActivity.this,ImageFullScreenActivity.class).putExtra("share_image_path", path).putExtra("share_imagename", Imagefilename));
                    }*/

                } catch (Exception e) {
                    Log.e(this.getClass().getName(), e.toString());
                }

            } else if (extras.containsKey(Intent.EXTRA_TEXT)) {
                /***************************************/


                Textname = extras.getString(Intent.EXTRA_TEXT);

                if (Textname.contains("https://maps.app.goo.gl")||Textname.contains("https://maps.google.com")||Textname.contains("maps")) {
                    // See my real-time location on Maps: https://maps.app.goo.gl/Szi5dsdTAr8jPrHa9
                /*Woods Royale B-Wing
Survey No 71, Swami Vivekanand Nagar, Jijai Nagar, Kothrud, Pune, Maharashtra 411038
https://maps.app.goo.gl/9WZR7jfmP95HCq497*/

                    String[] URL = Textname.split("https");
                    String add = URL[0];
                    String[] firmnameSplit = add.split("\n");
                    String firmname = firmnameSplit[0];
                    add = add.replace("\n", " ");
                    String locationURL = URL[1];

                    // https://maps.app.goo.gl/QvYBrckFiWDziyYb7

                    Geocoder coder = new Geocoder(this);
                    List<Address> address = null;
                    Barcode.GeoPoint p1 = null;

                    try {
                        address = coder.getFromLocationName(Textname, 5);

                        Address location = address.get(0);
                        //String add = String.valueOf(address.get(0));
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();
                        String loc = location.getLocality();
                        String pin = location.getPostalCode();
                        String pin1 = location.getFeatureName();
                        String mobile = location.getPhone();

                        p1 = new Barcode.GeoPoint((double) (location.getLatitude()),
                                (double) (location.getLongitude()));

                        if (address.size() != 0) {
                      /*  Intent intent1 = new Intent(SharefunctionActivity.this, IndividualProspectusActivity.class);
                        intent1.putExtra("keymode", "AddNewfromshare");
                        intent1.putExtra("Address",add);
                        intent1.putExtra("pinCode", pin);
                        intent1.putExtra("Lat", String.valueOf(lat));
                        intent1.putExtra("Lng", String.valueOf(lng));
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);*/
                            CreateIndiProspectNew(add, String.valueOf(lat), String.valueOf(lng), pin, firmname);


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {

                    txt_existing_activity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SharefunctionActivity.this, ActivityMain.class);
                            // intent.putExtra("Description",Textname);
                            //intent.putExtra("Imagename",Imagefilename);
                            //intent.putExtra("ImagePath",path);
                            startActivity(intent);
                            finish();
                        }
                    });
                    txt_chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SharefunctionActivity.this, OpenChatroomActivity.class);
                            intent.putExtra("Description", Textname);
                            // intent.putExtra("Imagename",Imagefilename);
                            //intent.putExtra("ImagePath",path);
                            startActivity(intent);
                            finish();
                            AppCommon.getInstance(SharefunctionActivity.this).setChatPostion(0);
                        }
                    });
                    txt_activity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SharefunctionActivity.this, AssignActivity.class);
                            intent.putExtra("Description", Textname);
                            //intent.putExtra("Imagename",Imagefilename);
                            //intent.putExtra("ImagePath",path);
                            startActivity(intent);
                            finish();


                        }
                    });
                }
                return;
            }
        }
    }

    private void CreateIndiProspectNew(String add, String lat, String lng, String pin, String firmname) {

        JSONObject jsoncontact = new JSONObject();
        String contact = "", product = "";
        try {
            jsoncontact.put("ContactName", "");
            jsoncontact.put("Designation", "");
            jsoncontact.put("EmailId", "");
            jsoncontact.put("Mobile", "");
            jsoncontact.put("Telephone", "");
            jsoncontact.put("DateofBirth", "");
            jsoncontact.put("ContactPersonDept", "");
            jsoncontact.put("Fax", "");
            jsoncontact.put("AnniversaryDate", "");
            jsoncontact.put("Gender", "");
            jsoncontact.put("MaritalStatus", "");
            jsoncontact.put("SpouseName", "");
            jsoncontact.put("WhatsAppNo", "");

            contact = jsoncontact.toString();

            System.out.println("Contact list : " + jsoncontact.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }


        JSONObject jsonProduct = new JSONObject();
        try {
            jsonProduct.put("FKProductId", "");
            product = jsonProduct.toString();
        } catch (Exception e) {
        }

        //susmaster = new String[5];
        JSONObject jsonBusinessprospect = new JSONObject();

        try {

            jsonBusinessprospect.put("PKSuspectId", null);
            jsonBusinessprospect.put("FirmName", firmname);
            jsonBusinessprospect.put("Address", add);
            jsonBusinessprospect.put("FirmAlias", "");
            jsonBusinessprospect.put("FKCityId", "");
            jsonBusinessprospect.put("FKTerritoryId", "");
            jsonBusinessprospect.put("FKBusiSegmentId", "");
            jsonBusinessprospect.put("CompanyURL", "");
            jsonBusinessprospect.put("FKEnqSourceId", "");
            jsonBusinessprospect.put("Fax", "");
            jsonBusinessprospect.put("Notes", "");
            jsonBusinessprospect.put("Remark", "");
            jsonBusinessprospect.put("Department", "");
            jsonBusinessprospect.put("BusinessDetails", "");
            jsonBusinessprospect.put("CurrencyMasterId", "");
            jsonBusinessprospect.put("CurrencyDesc", "");
            jsonBusinessprospect.put("Turnover", "");
            jsonBusinessprospect.put("NoOfEmployees", "");
            jsonBusinessprospect.put("NoOfOffices", "");
            jsonBusinessprospect.put("LeadGivenBYId", "");
            jsonBusinessprospect.put("FKConsigneeId", "");
            jsonBusinessprospect.put("FKCustomerId", "");
            jsonBusinessprospect.put("EntityType", "");
            jsonBusinessprospect.put("PBT", "");
            jsonBusinessprospect.put("Rating", "");
            jsonBusinessprospect.put("Network", "");
            jsonBusinessprospect.put("Borrowings", "");
            jsonBusinessprospect.put("FKStateId", "");
            jsonBusinessprospect.put("GSTState", "");
            jsonBusinessprospect.put("GSTCode", "");
            jsonBusinessprospect.put("TANNo", "");
            jsonBusinessprospect.put("TANNoName", "");
            jsonBusinessprospect.put("FKCountryId", "");
            jsonBusinessprospect.put("ProspectType", "2");
            jsonBusinessprospect.put("Qualification: ", "");
            jsonBusinessprospect.put("Experience: ", "");


            jsonBusinessprospect.put("val1", lat);
            jsonBusinessprospect.put("val2", lng);
            jsonBusinessprospect.put("val3",pin);
            jsonBusinessprospect.put("val4", "");
            jsonBusinessprospect.put("val5", "");
            jsonBusinessprospect.put("val6", "");
            jsonBusinessprospect.put("val7", "");
            jsonBusinessprospect.put("val8", "");
            jsonBusinessprospect.put("val9", "");
            jsonBusinessprospect.put("val10", "");
            jsonBusinessprospect.put("sex", "");
            jsonBusinessprospect.put("District", "");
            jsonBusinessprospect.put("Village", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonData = new JSONObject();

        try {

            JSONArray ob = new JSONArray();
            JSONObject j = new JSONObject(jsonBusinessprospect.toString());
            System.out.println("ArrayBusiness : " + jsonBusinessprospect.toString());
            ob.put(j);

            jsonData.put("SuspMaster", ob);

            JSONArray obj1 = new JSONArray();
            JSONObject a = null;

            a = new JSONObject(contact);
            obj1.put(a);

            jsonData.put("SuspContactDetails", obj1);

            JSONArray obj = new JSONArray();
            JSONObject a2 = null;

            a2 = new JSONObject(product);
            obj.put(a2);

            jsonData.put("SuspProdDetails", obj);
            jsonData.put("EnquiryRegistryId", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // FinalArray[0]
        String finaljson = jsonData.toString();
        finaljson = finaljson.replaceAll("\\\\", "");

        if (isnet()) {

            progressDialog = new ProgressDialog(SharefunctionActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
            final String finalJson = finaljson;

            new StartSession(SharefunctionActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new PostProspectUpdate_savenstartJSON().execute(finalJson);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
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
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    public String getPath1(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String getRealPathFromUri(Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }

        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public void handleSendImage(Intent intent) throws IOException {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            File file = new File(getCacheDir(), "image");
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            try {

                OutputStream output = new FileOutputStream(file);
                try {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;

                    while ((read = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }

                    output.flush();
                } finally {
                    output.close();
                }
            } finally {
                inputStream.close();
                byte[] bytes = getFileFromPath(file);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmapToUriConverter(bitmap);
                //Upload Bytes.
            }
        }
    }

    public static byte[] getFileFromPath(File file) {
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }


    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;


        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);
            int w = mBitmap.getWidth();
            int h = mBitmap.getHeight();
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, w, h,
                    true);
            String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString();
            File file = new File(path1 + "/" + "Vwb" + "/" + "Sender");
            if (!file.exists())
                file.mkdirs();
            File file1 = new File(file, "Image-" + new Random().nextInt() + ".jpg");
            if (file1.exists())
                file1.delete();
           /* File file = new File(SharefunctionActivity.this.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");*/
            FileOutputStream out = new FileOutputStream(file1);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
            out.flush();
            out.close();
            //get absolute path
            path = file1.getAbsolutePath();
            File f = new File(path);
            Imagefilename = f.getName();
            uri = Uri.fromFile(f);
//file:///data/data/vworkbench7.vritti.com.vworkbench7/files/Image1825476171.jpeg


        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    class PostProspectUpdate_savenstartJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SharefunctionActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Post_Prospect;
            try {
                res = ut.OpenPostConnection(url, params[0], SharefunctionActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    System.out.println("SaveBusiness :" + response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            Toast.makeText(SharefunctionActivity.this, "Prospect added succcessfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SharefunctionActivity.this,
                    CreateOpportunityActivity.class);
            intent.putExtra("SuspectID", integer);//"cab7944e-d227-479e-91e4-c7b84d9e26b7"
            startActivity(intent);
            SharefunctionActivity.this.finish();
            //onBackPressed();
        }

    }
}
