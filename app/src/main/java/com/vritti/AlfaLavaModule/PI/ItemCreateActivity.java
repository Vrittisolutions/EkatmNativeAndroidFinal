package com.vritti.AlfaLavaModule.PI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.AlfaLavaModule.activity.picking.ItemWisePickListDetailActivity;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.activity.ItemMasterSyncActivity;
import com.vritti.inventory.physicalInventory.adapter.PartCodeNameAdapter;
import com.vritti.inventory.physicalInventory.bean.PartCodeName;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

public class ItemCreateActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private  Context pContext;


    Button btn_save;
    AutoCompleteTextView spinner_item;
    private String warehousename;
    private String  warehouseID;
    String PICode="",PIHeaderID="";
    EditText edt_lot;
    String KEYSYNCALL = "SyncAll", KEYRESUME = "StartsFrom";
    String KEY = "";
    private String flag;
    private int ROWS;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    String filePathUrl = "", readFileDB = "";
    ArrayList<String > ItemCodelist;
    PartCodeNameAdapter padapter;
    private String partName="";
    private String itemPlantID="",LocationMasterId="",PacketNo="",StockDetailsId="";
    private String partCode="";
    public static final int REQ_PARTCODE = 7;
    private String LotNo="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pi_item_create_lay);
        ButterKnife.bind(this);
        pContext = ItemCreateActivity.this;
        getSupportActionBar().setTitle("Create Item");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        pDialog=new ProgressDialog(ItemCreateActivity.this);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(pContext);
        String settingKey = ut.getSharedPreference_SettingKey(pContext);
        String dabasename = ut.getValue(pContext, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(pContext, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(pContext, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(pContext, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(pContext, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(pContext, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(pContext, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(pContext, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(pContext, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(pContext, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        sql.delete(db.TABLE_GRN_PACKET, null, null);

        btn_save=findViewById(R.id.btn_save);
        spinner_item=findViewById(R.id.spinner_item);
        edt_lot=findViewById(R.id.edt_lot);

        ItemCodelist = new ArrayList<>();

        Intent intent=getIntent();
        if (intent.hasExtra("PIHeaderID")){
            PIHeaderID=intent.getStringExtra("PIHeaderID");
            PICode=intent.getStringExtra("PICode");
            LocationMasterId=intent.getStringExtra("locMasterId");
            PacketNo=getIntent().getStringExtra("Packet");
            StockDetailsId=getIntent().getStringExtra("StockDetailsId");

        }




        if (cf.getPIGetItemcount()>0){
            displayProduct();
        }else {


            if (isnet()) {
                new StartSession(ItemCreateActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        //String url = CompanyURL+"/Attachments/View%20Attachment/ItemMaster.json";
                        KEY = KEYSYNCALL;
                        new DownloadROWONLY().execute(KEY);

                        String url = CompanyURL + "/Attachments/" + EnvMasterId + "/ItemMaster.json";
                        new DownloadFileFromURL().execute(url);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                    }
                });
            }
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LotNo=edt_lot.getText().toString();
                if (LotNo.matches("")) {
                    Toast.makeText(ItemCreateActivity.this, "Please enter LOT No", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());

                    Intent intent = new Intent(ItemCreateActivity.this, AddPacketActivity.class);
                    intent.putExtra("ItemPlantId", itemPlantID);
                    intent.putExtra("fifo", date);
                    intent.putExtra("lot", LotNo);
                    intent.putExtra("PIHeaderID", PIHeaderID);
                    intent.putExtra("locMasterId", LocationMasterId);
                    intent.putExtra("StockDetailsId", StockDetailsId);
                    intent.putExtra("Packet", PacketNo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }


            }
        });



        spinner_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                partCode = spinner_item.getText().toString();
                GetItemCode(partCode);

            }
        });



    }




    public boolean isnet() {
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    class DownloadROWONLY extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        String rowsCnt = "0";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(ItemCreateActivity.this);
                    progressDialog.setMessage("Checking for row count Please wait...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "";
            flag = "Y";
            KEY = params[0];

            // String url = CompanyURL + WebUrlClass.api_ItemList;
            url = CompanyURL + WebUrlClass.api_ItemListAndroid+"?RQry=Y";  //RQry,from,to

            try {
                res = ut.OpenConnection(url,ItemCreateActivity.this);
                if (res!=null) {
                    response = res.toString().replaceAll("\\\\r\\\\n","");
                    response = response.toString().replaceAll("\\\\","");
                    response = response.substring(1, response.length() - 1);

                    JSONObject jsonObject = new JSONObject(response);
                    rowsCnt = jsonObject.getString("Rows");
                    ROWS = Integer.parseInt(rowsCnt);
                    Log.e("Rows ", String.valueOf(ROWS));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            // dismissProgressDialog();
            if(ROWS > 0){

                Toast.makeText(ItemCreateActivity.this,String.valueOf(ROWS),Toast.LENGTH_LONG).show();


                try{

                }catch (Exception e){
                    e.printStackTrace();
                }

            }else {

            }
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                //  InputStream input = new BufferedInputStream(url.openStream(), 8192);
                InputStream input = new BufferedInputStream(url.openStream(), lenghtOfFile);

                // Output stream
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString()
                        + "/ItemMaster.json");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();

                try{
                    filePathUrl = Environment.getExternalStorageDirectory().toString() + "/ItemMaster.json";
                    Log.e("file_url",filePathUrl);
                }catch (Exception e){
                    e.printStackTrace();
                }

                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // Log.e("file_url",String.valueOf(file_url));
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);


            new DownloadFileTask(ItemCreateActivity.this,"Downloading items...").execute();

        }

    }

    private void readFile_json(String readFileDB) {

        sql.delete(db.TABLE_GetItemListPI,null,null);

        parseFileData(readFileDB);

    }

    private void parseFileData(String readFileDB) {

        String ItemCode = "", ItemDesc = "", ItemMasterId = "", ItemPlantId = "",
                PurUnit = "", SalesUnit = "", ConvFactor = "",StockUnit = "", WareHouseMasterId = "", LocationMasterId = "",
                WarehouseCode = "",LocationCode = "";

        JSONArray jsonArray = null;
        View view = null;

        try {
            jsonArray = new JSONArray(readFileDB);

            for(int i=0; i<jsonArray.length();i++){

                try{
                    JSONObject jobj = jsonArray.getJSONObject(i);
                    ItemCode = jobj.getString("ItemCode");
                    ItemDesc = jobj.getString("ItemDesc");
                    ItemMasterId = jobj.getString("ItemMasterId");
                    ItemPlantId = jobj.getString("ItemPlantId");
                    PurUnit = jobj.getString("PurUnit");
                    SalesUnit = jobj.getString("SalesUnit");
                    ConvFactor = jobj.getString("ConvFactor");

                    try{
                        StockUnit = jobj.getString("StockUnit");
                        WareHouseMasterId = jobj.getString("WareHouseMasterId");
                        LocationMasterId = jobj.getString("LocationMasterId");
                        WarehouseCode = jobj.getString("WarehouseCode");
                        LocationCode = jobj.getString("LocationCode");
                    }catch (Exception e){

                    }


                    final String finalItemCode = ItemCode;
                    final String finalItemCode1 = ItemCode;

                    cf.insertItemMasterData(this,ItemCode, ItemDesc, ItemMasterId, ItemPlantId, PurUnit, SalesUnit, ConvFactor,
                            StockUnit,WareHouseMasterId,LocationMasterId,WarehouseCode,LocationCode);

                    displayProduct();

                }catch (Exception e){
                    e.printStackTrace();

                }

                //txtdwnlitems.setText(String.valueOf(cf.getGetItemcount()));

              /*  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ItemMasterSyncActivity.this,
                                "Storing items..."+String.valueOf(cf.getGetItemcount()),Toast.LENGTH_SHORT).show();
                        Log.e("Count2 : ",String.valueOf(cf.getGetItemcount()));
                        ItemMasterSyncActivity.this.txtdwnlitems.setText(String.valueOf(cf.getGetItemcount()));
                    }
                });*/
            }

         /*   runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(View.GONE);
                }
            });*/

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("jsnparse exc - ", String.valueOf(cf.getGetItemcount())+","+ e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ItemCreateActivity.this,"unable to parse json response",Toast.LENGTH_SHORT).show();
                }
            });
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
                pDialog.setCancelable(false);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    public void readFileFromAssets(Context context, String fileName) {
        String data = "";

       /* runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progress.setVisibility(View.VISIBLE);
            }
        });*/

        File file = new File(fileName);

        //Read text from file
        final StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

            data = line;
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            //You'll need to add proper error handling here
        }

        readFile_json(text.toString().trim());

        //   return line;
    }
    public class DownloadFileTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog mPDialog;
        private Context mContext;
        private PowerManager.WakeLock mWakeLock;
        //Constructor parameters :
        // @context (current Activity)
        // @targetFile (File object to write,it will be overwritten if exist)
        // @dialogMessage (message of the ProgresDialog)

        public DownloadFileTask(Context context,String dialogMessage) {
            this.mContext = context;
            mPDialog = new ProgressDialog(context);

            mPDialog.setMessage(dialogMessage);
            mPDialog.setIndeterminate(true);
            mPDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mPDialog.setCancelable(false);
            mPDialog.setCanceledOnTouchOutside(false);
            // reference to instance to use inside listener
            final DownloadFileTask me = this;
            mPDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    me.cancel(false);
                }
            });
            Log.i("DownloadTask","Constructor done");
        }

        @Override
        protected String doInBackground(String... sUrl) {

            // readFileFromAssets(HomeScreenActvity.this,file_url_1);

            try {
                readFileFromAssets(ItemCreateActivity.this,filePathUrl);
            }
            catch (Exception e) {
                e.printStackTrace();
                //You'll need to add proper error handling here
            }

            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();

            mPDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mPDialog.setIndeterminate(false);
            //mPDialog.setMax(100);
            mPDialog.setMax(ROWS);
            // mPDialog.setProgress(progress[0]);
            mPDialog.setProgress(cf.getGetItemcount());

        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("DownloadTask", "Work Done! PostExecute");
            mWakeLock.release();
            mPDialog.dismiss();
            if (result != null)
                Toast.makeText(mContext,"Download error: "+result, Toast.LENGTH_LONG).show();
            else
            Toast.makeText(mContext,"Items Downloaded successfully!"+String.valueOf(cf.getGetItemcount()), Toast.LENGTH_SHORT).show();
        }
    }

    private void displayProduct() {

        ItemCodelist.clear();
        // ItemDesclist.clear();

        String query = "SELECT distinct ItemCode,ItemMasterId,ItemDesc,ItemPlantId" +
                " FROM " + db.TABLE_GetItemListPI;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                //ItemCodelist.add(cur.getString(cur.getColumnIndex("ItemCode")));
                //ItemDesclist.add(cur.getString(cur.getColumnIndex("ItemDesc")));

                ItemCodelist.add(cur.getString(cur.getColumnIndex("ItemCode")));


            } while (cur.moveToNext());

        }


        AutoCompleteAdapter stringArrayAdapter = new AutoCompleteAdapter(this,
                android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1,ItemCodelist);

        spinner_item.setAdapter(stringArrayAdapter);//SF0006*/
        spinner_item.setSelection(0);


        // Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
    }
    private void GetItemCode(String itemcode) {
        //String query = "SELECT * FROM " + db.TABLE_GetItemList + " WHERE  ItemCode like '%" + itemcode + "%'";
        String query = "SELECT * FROM " + db.TABLE_GetItemListPI + " WHERE  ItemCode='"+itemcode+"'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                partName = cur.getString(cur.getColumnIndex("ItemDesc"));
                itemPlantID = cur.getString(cur.getColumnIndex("ItemPlantId"));

               /* Intent intent = new Intent();
                intent.putExtra("PartCode",partCode);
                intent.putExtra("PartName", partName);
                intent.putExtra("ItemPlantId",itemPlantID);
                intent.putExtra("ConvFactor", String.valueOf(CONV_factor));
                intent.putExtra("uomval", uomval);
                intent.putExtra("WareHouseMasterId", WareHouseMasterId);
                intent.putExtra("WarehouseCode", WarehouseCode);
                intent.putExtra("LocationMasterID", LocationMasterId);
                intent.putExtra("LocationCode", LocationCode);
                setResult(REQ_PARTCODE, intent);
                finish();*/




            } while (cur.moveToNext());
        }else{
            Toast.makeText(this, "Scanned code not found", Toast.LENGTH_SHORT).show();
        }
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
                    new StartSession(ItemCreateActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            //String url = CompanyURL+"/Attachments/View%20Attachment/ItemMaster.json";
                            KEY = KEYSYNCALL;
                            new DownloadROWONLY().execute(KEY);

                            String url = CompanyURL + "/Attachments/" + EnvMasterId + "/ItemMaster.json";
                            new DownloadFileFromURL().execute(url);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                        }
                    });
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public class AutoCompleteAdapter extends ArrayAdapter<String> implements
            Filterable {

        private ArrayList<String> fullList;
        private ArrayList<String> mOriginalValues;
        private ArrayFilter mFilter;
        LayoutInflater inflater;
        String text = "";

        public AutoCompleteAdapter(Context context, int resource,
                                   int textViewResourceId, List<String> objects) {

            super(context, resource, textViewResourceId, objects);
            fullList = (ArrayList<String>) objects;
            mOriginalValues = new ArrayList<String>(fullList);
            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return fullList.size();
        }

        @Override
        public String getItem(int position) {
            return fullList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            // tvViewResourceId = (TextView) view.findViewById(android.R.id.text1);
            String item = getItem(position);
            Log.d("item", "" + item);
            if (convertView == null) {
                convertView = view = inflater.inflate(
                        android.R.layout.simple_dropdown_item_1line, null);
            }
            // Lookup view for data population
            TextView myTv = (TextView) convertView.findViewById(android.R.id.text1);
            myTv.setText(highlight(text, item));
            return view;
        }

        @Override
        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new ArrayFilter();
            }
            return mFilter;
        }

        private class ArrayFilter extends Filter {
            private Object lock;

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();
                if (prefix != null) {
                    text = prefix.toString();
                }
                if (mOriginalValues == null) {
                    synchronized (lock) {
                        mOriginalValues = new ArrayList<String>(fullList);
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    synchronized (lock) {
                        ArrayList<String> list = new ArrayList<String>(
                                mOriginalValues);
                        results.values = list;
                        results.count = list.size();
                    }
                } else {
                    final String prefixString = prefix.toString().toLowerCase();
                    ArrayList<String> values = mOriginalValues;
                    int count = values.size();

                    ArrayList<String> newValues = new ArrayList<String>(count);

                    for (int i = 0; i < count; i++) {
                        String item = values.get(i);
                        if (item.toLowerCase().contains(prefixString)) {
                            newValues.add(item);
                        }

                    }

                    results.values = newValues;
                    results.count = newValues.size();
                }

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                if (results.values != null) {
                    fullList = (ArrayList<String>) results.values;
                } else {
                    fullList = new ArrayList<String>();
                }
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        }

        public  CharSequence highlight(String search, String originalText) {
            // ignore case and accents
            // the same thing should have been done for the search text
            String normalizedText = Normalizer
                    .normalize(originalText, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase(Locale.ENGLISH);

            int start = normalizedText.indexOf(search.toLowerCase(Locale.ENGLISH));
            if (start < 0) {
                // not found, nothing to to
                return originalText;
            } else {
                // highlight each appearance in the original text
                // while searching in normalized text
                Spannable highlighted = new SpannableString(originalText);
                while (start >= 0) {
                    int spanStart = Math.min(start, originalText.length());
                    int spanEnd = Math.min(start + search.length(),
                            originalText.length());

                    highlighted.setSpan(new ForegroundColorSpan(Color.BLACK),
                            spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    start = normalizedText.indexOf(search, spanEnd);
                }

                return highlighted;
            }
        }
    }
}
