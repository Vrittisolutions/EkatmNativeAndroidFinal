package com.vritti.chat.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatRoomLiveLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Double lng = 0.00, lat = 0.00;
    String Profileimage;
    SharedPreferences sharedPreferences;
    ProgressBar progressbar;
    private Marker marker;
    private View mCustomMarkerView;
    private CircleImageView mMarkerImageView;
    TextView txt_name;
    Bitmap image;
    ArrayList<String> stringArrayList = new ArrayList<>();
    private Bitmap bitmap;

    String ChatRoomId="", PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    LatLngBounds.Builder builder;
    List<LatLng> locations = new ArrayList<>();
    private JSONArray jResults;
    private LatLng locationplace;
    private String name,Usermasterid,Title;
    private int cornerRadius=40;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_team_map);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_toolbar_logo_vwb);
        getSupportActionBar().setTitle("ChatRoom Live Location");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);

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

        Intent intent=getIntent();
        ChatRoomId=intent.getStringExtra("ChatRoomId");
        if (isnet()) {
            new StartSession(ChatRoomLiveLocationActivity.this, new CallbackInterface() {
                @Override

                public void callMethod() {
                    new DownloadChatRoomLivelocationDataJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }




        mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.live_custom_marker, null);
        mMarkerImageView =  mCustomMarkerView.findViewById(R.id.user_dp);

        progressbar = findViewById(R.id.progressbar);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String LocationName = "";

        Location location = null;

        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!(isGPSEnabled || isNetworkEnabled)) {

        } else {
            if (isNetworkEnabled) {

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
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {

                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //lat = location.getLatitude();
                //lng = location.getLongitude();

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
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private Bitmap getMarkerBitmapFromView(View view, String image) {

        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(image).getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }

       // mMarkerImageView.setImageBitmap(bitmap);

        //Bitmap bitmap1= getRoundedBitmap(bitmap,80);

        mMarkerImageView.setImageBitmap(bitmap);
        return bitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int     width           = bm.getWidth();
        int     height          = bm.getHeight();
        float   scaleWidth      = ((float) newWidth) / width;
        float   scaleHeight     = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    class DownloadChatRoomLivelocationDataJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ChatRoomLiveLocationActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.vwb_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebUrlClass.api_GetRefreshChatRoom + "?ApplicationCode="+WebUrlClass.AppNameFCM;

            String url = CompanyURL + WebUrlClass.api_getChatRoomLiveLocation + "?ChatRoomId="+ChatRoomId;

            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();

                jResults = new JSONArray(response);

                //  chatRoomDisplayArrayList.clear();




            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
           // progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")) {
                //progressbar.setVisibility(View.GONE);

            } else {
                if (response != null) {
                    try {
                        for (int i = 0; i < jResults.length(); i++) {

                            JSONObject Jsonchatmember = jResults.getJSONObject(i);

                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            builder = new LatLngBounds.Builder();

                            if (!Jsonchatmember.getString("Latitude").equals("")){
                                lat = Double.parseDouble(Jsonchatmember.getString("Latitude"));
                            }
                            // lng = Double.parseDouble(Jsonchatmember.getString("Longitude"));
                            if (!Jsonchatmember.getString("Longitude").equals("")){
                                lng = Double.parseDouble(Jsonchatmember.getString("Longitude"));
                            }
                           /* lat = Double.parseDouble(Jsonchatmember.getString("Latitude"));
                            lng = Double.parseDouble(Jsonchatmember.getString("Longitude"));*/
                            Profileimage = Jsonchatmember.getString("ImagePath");
                            final String profile=CompanyURL+Profileimage;
                            String name = Jsonchatmember.getString("ParticipantName");
                            String Usermasterid = Jsonchatmember.getString("ParticipantId");

                            String t = name;
                            Pattern p = Pattern.compile("((^| )[A-Za-z])");
                            Matcher m = p.matcher(t);
                            String initials = "";
                            while (m.find()) {
                                initials += m.group().trim();
                            }

                            final String Title = initials.toUpperCase();
                            LatLng locationplace = null;


                            if (lat.equals(0.0) || lng.equals(0.0)) {

                            } else {
                                locationplace = new LatLng(lat, lng);

                            }


                            if (lat.equals(0.0) || lng.equals(0.0)) {

                            } else {
                                final LatLng finalLocationplace = locationplace;
                                if (locationplace != null)
                                    locations.add(locationplace);

                                Glide.with(getApplicationContext())
                                        .asBitmap().
                                        load(profile)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                                MarkerOptions markerOptions = new MarkerOptions();

                                                markerOptions.position(finalLocationplace);


                                                if (profile.contains("female") || profile.contains("male")) {
                                                    // markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, profile)));
                                                    markerOptions.title(Title);
                                                    marker = mMap.addMarker(markerOptions);
                                                    // marker.showInfoWindow();
                                                } else {
                                                    //  markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, profile)));
                                                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(mCustomMarkerView, profile)));
                                                    markerOptions.title(Title);

                                                    // markerOptions.title(Title);

                                                    marker = mMap.addMarker(markerOptions);
                                                    //  marker.showInfoWindow();
                                                }


                                                // marker.showInfoWindow();

                                                String id = marker.getId();
                                                Log.d("Marker Id", id);
                                                builder.include(marker.getPosition());


                                                if (ActivityCompat.checkSelfPermission(ChatRoomLiveLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ChatRoomLiveLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                    // TODO: Consider calling
                                                    //    ActivityCompat#requestPermissions
                                                    // here to request the missing permissions, and then overriding
                                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                    //                                          int[] grantResults)
                                                    // to handle the case where the user grants the permission. See the documentation
                                                    // for ActivityCompat#requestPermissions for more details.
                                                    return;
                                                }
                                                mMap.setMyLocationEnabled(true);
                                            }

                                        });


                                mMap.getUiSettings().setMyLocationButtonEnabled(true);


                            }
                            if (locations.size() != 0) {
                                try {


                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    for (LatLng latLng : locations) {
                                        builder.include(latLng);
                                        // builder.include(locations.get(0)); //Taking Point A (First LatLng)
                                        // builder.include(locations.get(locations.size() - 1));
                                    }//Taking Point B (Second LatLng)
                                    LatLngBounds bounds = builder.build();
                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                                    mMap.moveCamera(cu);
                                    mMap.animateCamera(cu);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            // chatRoomDisplayArrayList.add(chatUser);
                        }
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }

        private Bitmap createCustomMarker(View mCustomMarkerView, String profile) {
            View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.live_custom_marker, null);

            CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
            profile = profile.replace(" ","%20");
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(profile).getContent());

            } catch (IOException e) {
                e.printStackTrace();
            }
            markerImage.setImageBitmap(bitmap);
            //TextView txt_name = (TextView)marker.findViewById(R.id.name);
            //txt_name.setText(_name);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ChatRoomLiveLocationActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
            marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
            marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
            marker.buildDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            marker.draw(canvas);

            return bitmap;
        }
    }
}
