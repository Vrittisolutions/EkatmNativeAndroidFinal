package com.vritti.vwblib.vworkbench;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwblib.Adapter.ReportingPersonsGPSLocationAdapter;
import com.vritti.vwblib.Beans.GPSLocationTimeBean;
import com.vritti.vwblib.R;
import com.vritti.vwblib.classes.CommonFunction;

public class ReportingPersonsGPSLocationActivity extends Activity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    String mobno, link;

    String sop = "no";

    ImageView iv;

    String reporteeMobno;
    ListView gpsdetail;

    private Calendar cal_L, cal_H;
    Date currentLocalTime;
    SQLiteDatabase sql;
    private Context parent;
    private ProgressDialog progressDialog;

    private int mPpageno = 0;
    private DateFormat dateFormat;
    private Date date;
    String Date_L;
    String Date_H;
    ArrayList<GPSLocationTimeBean> userdatagps;
    private String mIfDate;
    private String Date_CheckString = null;
    private Date Date_CheckDate;
    private String mStartDate, mEndDate, mindATE;
    Date d_e;
    Date d_s;
    SharedPreferences userpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vwb_reporting_person_gps_location);

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
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        sql = db.getWritableDatabase();

        userdatagps = new ArrayList<GPSLocationTimeBean>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();
        Bundle extras = getIntent().getExtras();
        DateSetting();
        initialize();

    }

    private void DateSetting() {
        // TODO Auto-generated method stub

        cal_L = Calendar.getInstance();
        Date today = cal_L.getTime();
        cal_H = Calendar.getInstance();
        Date toadayH = cal_H.getTime();
        cal_H.add(Calendar.DATE, 1);
        Date nextday = cal_H.getTime();

        Date_L = dateFormat.format(today);
        Date_H = dateFormat.format(nextday);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_menu_refresh:
                Toast.makeText(parent, "Refreshing...", Toast.LENGTH_SHORT).show();
                // refreshList();
                return true;
        }

        return false;
    }
*/
    public static boolean isInternetAvailable(Context parent) {
        ConnectivityManager cm = (ConnectivityManager) parent
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void initialize() {

        // iv = (ImageView) findViewById(R.id.refreshtt);
        gpsdetail = (ListView) findViewById(R.id.gpsdetail);


        Bundle extras = getIntent().getExtras();
        reporteeMobno = extras.getString("reporteeMobno");

        mobno = userpreferences.getString("MobileNo", null);
        ;
        link = "http://vritti.ekatm.com";

        if (cf.check_gpsrecorts(reporteeMobno) > 0) {
            updaterefreshlist();
        } else {
            if (isInternetAvailable(ReportingPersonsGPSLocationActivity.this))
                new Downloadxml_FirstWithRefresh().execute();
            // else
               /* Utilities
                        .showCustomMessageDialog(
                                "Internet connection is not available on this device..Please turn on mobile data..",
                                "No internet connection found", parent);*/
        }
    }

    public void reLoadMore(View v) {

        new DownloadxmlsURL_Load().execute(mPpageno + "");

    }

    public void Refreshonclick(View v) {

        DateSetting();
        new Downloadxml_FirstWithRefresh().execute();
    }

    private void updatelist_Load() {
        userdatagps.clear();

        ArrayList<String> ls = new ArrayList<String>();
        Cursor c = sql.rawQuery("SELECT * FROM GPSrecords WHERE MobileNo='"
                + reporteeMobno + "'  ORDER BY AddedDt DESC", null);

        Cursor c1 = sql.rawQuery(
                "SELECT MIN(AddedDt) FROM GPSrecords WHERE MobileNo='"
                        + reporteeMobno + "'", null);

        if (c1.getCount() == 0) {


        } else {
            c1.moveToFirst();
            do {
                // ls.add(networkodelist.setNetworkCode(cursor.getString(0)));
                ls.add(c1.getString(0));

            } while (c1.moveToNext());

            mindATE = ls.get(0);
        }

        if (c.getCount() == 0) {

        } else {
            c.moveToFirst();
            do {

                String schTime = (c.getString(c.getColumnIndex("AddedDt")));

                userdatagps.add(new GPSLocationTimeBean(c.getString(0), c
                        .getString(1), c.getString(2), c.getString(3), c
                        .getString(4), c.getString(5), c.getString(6)));

            } while (c.moveToNext());
        }


        int currentPosition = gpsdetail.getFirstVisiblePosition();
        gpsdetail.setAdapter(new ReportingPersonsGPSLocationAdapter(this,
                userdatagps));

        gpsdetail.setSelectionFromTop(currentPosition + 1, 0);

    }

    private void updaterefreshlist() {
        // TODO Auto-generated method stub

        ArrayList<String> ls = new ArrayList<String>();
        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_GPSRECORDS + " WHERE MobileNo='"
                + reporteeMobno + "' ORDER BY AddedDt DESC ", null);

        Cursor c1 = sql.rawQuery(
                "SELECT MIN(AddedDt) FROM " + db.TABLE_GPSRECORDS + " WHERE MobileNo='"
                        + reporteeMobno + "'", null);


        if (c1.getCount() == 0) {


        } else {
            c1.moveToFirst();
            do {

                ls.add(c1.getString(0));

            } while (c1.moveToNext());

            mindATE = ls.get(0);
        }

        if (c.moveToFirst()) {
            do {

                String schTime = (c.getString(c.getColumnIndex("AddedDt")));

                userdatagps.add(new GPSLocationTimeBean(c.getString(0), c
                        .getString(1), c.getString(2), c.getString(3), c
                        .getString(4), c.getString(5), c.getString(6)));

            } while (c.moveToNext());
        }

        gpsdetail.setAdapter(new ReportingPersonsGPSLocationAdapter(this,
                userdatagps));
    }












    public class DownloadxmlsURL_Load extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ReportingPersonsGPSLocationActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
            String s = mindATE;// 05-17-2016 10:22:04
            if (s == null || s.equalsIgnoreCase(" ")) {
                sop = "over";
            } else {
                SimpleDateFormat nextdt = new SimpleDateFormat(
                        "MM-dd-yyyy hh:mm:ss");
                Date min;
                try {
                    min = nextdt.parse(s);
                    SimpleDateFormat fom = new SimpleDateFormat("yyyy-MM-dd");
                    Date_H = fom.format(min);

                    Date_L = fom.format(min.getTime() - MILLIS_IN_DAY);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    String url;
                    // LDate=2016-05-20&Hdate=2016-05-21
                    url = link
                            + "/VWB/webservice/ActivityWebservice.asmx/getGpscordinates?Mobileno="
                            + reporteeMobno + "&LDate=" + Date_L + "&Hdate="
                            + Date_H;

                    String res = httpGet(url);

                    if (checkRecord(res)) {
                        mPpageno = mPpageno + 1;
                        sop = "valid";
                        NodeList nl = getnode(res, "Table");
                        Log.e("nodelist", "nl.getLength() : " + nl.getLength());
                        for (int i = 0; i < nl.getLength(); i++) {
                            Element e = (Element) nl.item(i);

                            String location = getValue(e,
                                    "locationName");
                            String LocationName = null;
                            if (location.equalsIgnoreCase("Location Not Found")
                                    || location.equalsIgnoreCase("null")) {
                                String lat = getValue(e, "latitude");
                                Double Latitud = Double.valueOf(lat)
                                        .doubleValue();
                                String longt = getValue(e,
                                        "longitude");
                                Double longitude = Double.valueOf(longt)
                                        .doubleValue();
                                String LocationName2;
                                try {
                                    Geocoder geocoder = new Geocoder(
                                            ReportingPersonsGPSLocationActivity.this,
                                            Locale.getDefault());
                                    List<Address> addressList = geocoder
                                            .getFromLocation(Latitud,
                                                    longitude, 1);
                                    if (addressList != null
                                            && addressList.size() > 0) {
                                        Address address = addressList.get(0);
                                        StringBuilder sb = new StringBuilder();
                                        for (int v = 0; v < address
                                                .getMaxAddressLineIndex(); v++) {
                                            sb.append(address.getAddressLine(v));

                                        }

                                        LocationName2 = sb.toString();
                                        if (cf.check_GPSrecords(getValue(e, "AddedDt")) > 0) {

                                        } else {
                                            cf.adduserdatagps(new GPSLocationTimeBean(
                                                    getValue(e,
                                                            "GPSID"),
                                                    getValue(e,
                                                            "MobileNo"),
                                                    getValue(e,
                                                            "latitude"),
                                                    getValue(e,
                                                            "longitude"),
                                                    LocationName2,
                                                    getValue(e,
                                                            "AddedDt"),

                                                    getValue(e, "num")));
                                        }

                                    }

                                } catch (Exception err) {
                                    err.printStackTrace();
                                    LocationName2 = " ";
                                }

                            } else if (location
                                    .equalsIgnoreCase("Network and GPS not enabled")
                                    || location.contains("Network")) {

                            } else {

                                Log.e("update master........",
                                        "users : name: "
                                                + getValue(e,
                                                "username")
                                                + "  psd : "
                                                + getValue(e,
                                                "userpass"));
                                if (cf.check_GPSrecords(getValue(e,
                                        "AddedDt")) > 0) {

                                } else {
                                    cf.adduserdatagps(new GPSLocationTimeBean(
                                            getValue(e, "GPSID"),
                                            getValue(e, "MobileNo"),
                                            getValue(e, "latitude"),
                                            getValue(e, "longitude"),
                                            getValue(e,
                                                    "locationName"),
                                            getValue(e, "AddedDt"),
                                            getValue(e, "num")));
                                }
                            }
                        }
                    } else {
                        sop = "over";
                    }
                } catch (Exception e) {
                    sop = "invalid";

                    Log.e("error ", " msg : " + e.getMessage());
                }

            }

            // Date_L = dateFormat.format(today);

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (sop.equals("valid")) {


                updatelist_Load();

            } else if (sop.equals("over")) {

            } else {
                Toast.makeText(
                        ReportingPersonsGPSLocationActivity.this,
                        "Internet speed is slow,data will get displayed within sometime",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public static String httpGet(String urlString) throws IOException {
        URL url = new URL(urlString.replaceAll(" ", "%20"));

        //	URL url = new URL("http://vritti.vworkbench.com/webservice/ActivityWebservice.asmx/GetreportingGps?MobileNo=9890156056");
        Log.d("test", "url" + url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        Log.d("test", "conn" + conn);
        //conn.connect();
        int resCode = conn.getResponseCode();
        // Check for successful response code or throw error
        if (conn.getResponseCode() != 200) {
            throw new IOException(conn.getResponseMessage());
            //return "0";
        }

        // Buffer the result into a string
        BufferedReader buffrd = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = buffrd.readLine()) != null) {
            sb.append(line);
        }

        buffrd.close();

        conn.disconnect();
        return sb.toString();
    }

    public class Downloadxml_FirstWithRefresh extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ReportingPersonsGPSLocationActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String url;
                // &Date=2015-10-07
                url = link
                        + "/VWB/webservice/ActivityWebservice.asmx/getGpscordinates?Mobileno="
                        + reporteeMobno + "&LDate=" + Date_L + "&Hdate="
                        + Date_H;

				/*
                 * url = link +
				 * "/VWB/webservice/ActivityWebservice.asmx/getGpscordinates?Mobileno=8390199115&LDate=2016-06-11&Hdate=2016-06-12"
				 * ;
				 */

                String res = httpGet(url);

                if (checkRecord(res)) {

                    sop = "valid";
                    NodeList nl = getnode(res, "Table");
                    Log.e("nodelist", "nl.getLength() : " + nl.getLength());
                    for (int i = 0; i < nl.getLength(); i++) {
                        Element e = (Element) nl.item(i);
                        String location = getValue(e, "locationName");

                        if (location.equalsIgnoreCase("Location Not Found")
                                || location.equalsIgnoreCase("null")) {
                            String lat = getValue(e, "latitude");
                            Double Latitud = Double.valueOf(lat).doubleValue();
                            String longt = getValue(e, "longitude");
                            Double longitude = Double.valueOf(longt)
                                    .doubleValue();
                            String LocationName2;
                            try {
                                Geocoder geocoder = new Geocoder(
                                        ReportingPersonsGPSLocationActivity.this,
                                        Locale.getDefault());
                                List<Address> addressList = geocoder
                                        .getFromLocation(Latitud, longitude, 1);
                                if (addressList != null
                                        && addressList.size() > 0) {
                                    Address address = addressList.get(0);
                                    StringBuilder sb = new StringBuilder();
                                    for (int v = 0; v < address
                                            .getMaxAddressLineIndex(); v++) {
                                        sb.append(address.getAddressLine(v));

                                    }
                                    LocationName2 = sb.toString();
                                    if (cf.check_GPSrecords(getValue(e, "AddedDt")) > 0) {

                                    } else {
                                        cf.adduserdatagps(new GPSLocationTimeBean(
                                                getValue(e, "GPSID"),
                                                getValue(e,
                                                        "MobileNo"),
                                                getValue(e,
                                                        "latitude"),
                                                getValue(e,
                                                        "longitude"),
                                                LocationName2,
                                                getValue(e, "AddedDt"),
                                                getValue(e, "num")));
                                    }
                                }

                            } catch (Exception err) {
                                err.printStackTrace();
                                LocationName2 = " ";
                            }

                        } else if (location
                                .equalsIgnoreCase("Network and GPS not enabled")
                                || location.contains("Network")) {

                        } else {
                            String columnName = getValue(e, "AddedDt");
                            Log.e("update master........",
                                    "users : name: "
                                            + getValue(e, "username")
                                            + "  psd : "
                                            + getValue(e, "userpass"));
                            // if(columnName.equalsIgnoreCase("AddedDt")){
                            // //yyyy-MM-dd'T'HH:mm:ss.SSSZ
                            // }
                            if (cf.check_GPSrecords(getValue(e,
                                    "AddedDt")) > 0) {

                            } else {
                                cf.adduserdatagps(new GPSLocationTimeBean(
                                        getValue(e, "GPSID"),
                                        getValue(e, "MobileNo"),
                                        getValue(e, "latitude"),
                                        getValue(e, "longitude"),
                                        getValue(e, "locationName"),
                                        getValue(e, "AddedDt"),
                                        getValue(e, "num")));
                            }
                        }
                    }
                } else {
                    sop = "over";
                }

            } catch (Exception e) {
                sop = "invalid";
                Log.e("error ", " msg : " + e.getMessage());
            }
            return sop;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (sop.equals("valid")) {
                userdatagps.clear();
                updaterefreshlist();
            } else if (sop.equals("over")) {
				/*
				 * Utilities.showCustomMessageDialog("No Records to display.",
				 * "Done", parent);
				 */
                Download_Date ada = new Download_Date();
                ada.execute();
            } else {
                Toast.makeText(
                        ReportingPersonsGPSLocationActivity.this,
                        "Internet speed is slow,data will get displayed within sometime",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public class Download_Date extends AsyncTask<String, Void, String> {

        String Soup;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // progressDialog = new ProgressDialog(parent);
            // progressDialog.setMessage("Loading...");
            // progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url;
            // &Date=2015-10-07
            Soup = "INVALID";
            url = link
                    + "/VWB/webservice/ActivityWebservice.asmx/getGpsCordinate_date?mobileno="
                    + reporteeMobno;
            String res = null;
            try {
                res = httpGet(url);

                if (res.contains("</string>")) {

                    Soup = "VALID";
                    res = res.substring(res.indexOf(">") + 1);
                    res = res.substring(res.indexOf(">") + 1);
                    res = res.substring(0, res.indexOf("<")); // 7/6/2016
                    // 2:50:31 PM
                    String Data = res;
                    res = Data;
                } else {
                    Soup = "INVALID";

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Soup = "INVALID";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (Soup.equalsIgnoreCase("VALID")) {

                SimpleDateFormat date = new SimpleDateFormat(
                        "MM/dd/yyyy hh:mm:ss aa");// 7/6/2016 2:50:31 PM
                Date ss = null;
                try {
                    ss = date.parse(result);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Date_L = dateFormat.format(ss);// 2016-07-06

                Calendar c;
                c = Calendar.getInstance();
                c.setTime(ss);
                c.add(Calendar.DATE, 1);
                Date nextday = c.getTime();
                Date_H = dateFormat.format(nextday);
                new Downloadxml_FirstWithRefresh().execute();

            } else if (Soup.equalsIgnoreCase("INVALID")) {

               /* Utilities.showCustomMessageDialog("No Records to Display",
                        "NO Data", parent);*/

            }
        }

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }

    public static NodeList getnode(String xml, String Tag) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        NodeList nl = doc.getElementsByTagName(Tag);
        return nl;
    }

    public static String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return getElementValue(n.item(0));
    }

	/*public static Double getValueD(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
		return getElementValueD(n.item(0));
	}*/

    public static final String getElementValueD(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public static final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public boolean checkRecord(String xml) {
        String columnName, response = null;
        if (xml.contains("<response>")) {
            NodeList nl = getnode(xml, "Table");
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                columnName = "response";
                response = getValue(e, columnName);
            }
            if (response.equalsIgnoreCase("No record found")
                    || response.equalsIgnoreCase(null)) {
                return false;
            }
        } else if (xml.contains("<UserMasterId>")) {
            return true;
        }
        return true;
    }
}