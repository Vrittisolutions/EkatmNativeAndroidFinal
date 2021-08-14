package com.vritti.expensemanagement;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import com.vritti.ekatm.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by sharvari on 20-Sep-19.
 */

public class LocationActivity extends AppCompatActivity
{

    AutoCompleteTextView ed_fromPlace, ed_toPlace;
    LinearLayout len_distance;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk";
    private String Tolocation, Fromlocation;
    private double disatnce;
    String  Location_disatnce;
    public static DefaultHttpClient httpClient = new DefaultHttpClient();
    private Location location,location1;
    double source_lat,source_lng;
    double dest_lat,dest_lat_lng;
    String  vehicle;


    private static final int VOICE_RECOGNITION_FROM_CODE = 1234;
    private static final int VOICE_RECOGNITION_DEST_CODE = 1235;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_lay);

        ed_fromPlace = (AutoCompleteTextView) findViewById(R.id.ed_fromPlace);
        ed_fromPlace.setSelection(0);
        ed_toPlace = (AutoCompleteTextView) findViewById(R.id.ed_toPlace);
        ed_toPlace.setSelection(0);

        Intent intent=getIntent();
        vehicle=intent.getStringExtra("vehicle");


        ed_fromPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FromstartVoiceRecognitionActivity();
            }
        });

        ed_toPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeststartVoiceRecognitionActivity();
            }
        });


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
        ed_fromPlace.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.vwb_list_item));
        ed_toPlace.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.vwb_list_item));

    }





    private void CalculateDistancelocation() {

        location.setLatitude(23232.23);
        location.setLongitude(2342342.2342);
        location1.setLatitude(23232.23);
        location1.setLongitude(2342342.2342);
        location1.distanceTo(location);


    }


    private void CalculateDistance(final String fromlocation, final String tolocation) {

        final Thread t = new Thread() {

            public void run() {
                try {
                    String url = "https://maps.googleapis.com/maps/api/directions/json?key=AIzaSyA-HkyklaKY4n4BH33QJikLXntbpLhsJk0&origin=" + URLEncoder.encode(fromlocation, "UTF-8") + "&destination=" + URLEncoder.encode(tolocation, "UTF-8") + "&sensor=false";
                    Object res = OpenConnection(url, getApplicationContext());
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
                                //final String[] separated = text.split("km");

                                    LocationActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Location_disatnce = text;

                                        }
                                    });
                                    Intent intent = new Intent();
                                    intent.putExtra("source", Fromlocation);
                                    intent.putExtra("dest", Tolocation);
                                    intent.putExtra("km", text);
                                    intent.putExtra("vehicle",vehicle);
                                    setResult(10,intent);
                                    finish();


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
    public  String OpenConnection(String url, Context mContext) {
        String res = "";
        InputStream inputStream = null;
        try {

            HttpGet httppost = new HttpGet(url.toString());
            httppost.setHeader("Accept", "application/json");
            //  httppost.setHeader("Content-type", "application/json");
            httppost.setHeader("Content-type", "application/json");

            HttpResponse response = null;


            response = httpClient.execute(httppost);
           /* inputStream = response.getEntity().getContent();
               String result;
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";*/

            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity);


        } catch (Exception e) {

        }

        return res;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
    private void FromstartVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // identifying your application to the Google service
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        // hint in the dialog
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        // hint to the recognizer about what the user is going to say
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // number of results
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        // recognition language
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
        startActivityForResult(intent, VOICE_RECOGNITION_FROM_CODE);
    }
    private void DeststartVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // identifying your application to the Google service
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        // hint in the dialog
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        // hint to the recognizer about what the user is going to say
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // number of results
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        // recognition language
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
        startActivityForResult(intent, VOICE_RECOGNITION_DEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_FROM_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            ed_fromPlace.setText(matches.get(0));



        }
        if (requestCode == VOICE_RECOGNITION_DEST_CODE && resultCode == RESULT_OK) {

            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            ed_toPlace.setText(matches.get(0));



        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
