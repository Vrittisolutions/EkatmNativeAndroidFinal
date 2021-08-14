package com.vritti.vwb.vworkbench;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vritti.chat.activity.ChatRoomLiveLocationActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.MyTeamBean;
import com.vritti.vwb.classes.CommonFunction;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class GoogleMapTeamActivity extends AppCompatActivity implements OnMapReadyCallback {
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
    ArrayList<MyTeamBean> lsMyteam;
    private MyTeamBean venue;
    ArrayList<MyTeamBean> myTeamBeen = new ArrayList<>();


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    LatLngBounds.Builder builder;
    List<LatLng> locations = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_team_map);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_toolbar_logo_vwb);
        getSupportActionBar().setTitle(R.string.app_name_toolbar_Vwb);

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

        lsMyteam = new ArrayList<>();
        lsMyteam = (ArrayList<MyTeamBean>) getIntent().getSerializableExtra("bundle");


        mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        mMarkerImageView =  mCustomMarkerView.findViewById(R.id.profile_image);


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

       /* ArrayList<HashMap<String, String>> loc = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;


        map = new HashMap<String, String>();
        map.put("Latitude", "18.502594");
        map.put("Longitude", "73.815427");
        map.put("LocationName", "Nilesh");
        map.put("image", "http://www.pngmart.com/files/3/Bill-Gates-PNG-File.png");
        loc.add(map);

        // Location 2
        map = new HashMap<String, String>();
        map.put("Latitude", "19.070828");
        map.put("Longitude", "72.877131");
        map.put("LocationName", "Chetana");
        map.put("image", "http://www.pngmart.com/files/3/Bill-Gates-PNG-File.png");
        loc.add(map);

        // Location 3
        map = new HashMap<String, String>();
        map.put("Latitude", "18.652032");
        map.put("Longitude", "73.770272");
        map.put("LocationName", "Pradnya");
        map.put("image", "http://www.pngmart.com/files/3/Bill-Gates-PNG-File.png");
        loc.add(map);*/



           /* Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1);
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                LocationName = address + " , " + city + " , " + state;

            } catch (IOException e) {
                e.printStackTrace();
            }*/
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // mMap.addMarker(new MarkerOptions().position(sydney).title(strAddress));

/*

            if (marker != null) {
                marker.remove();
            }
*/
        builder = new LatLngBounds.Builder();

        for (int i = 0; i < lsMyteam.size(); i++) {

            lat = Double.parseDouble(lsMyteam.get(i).getLatitude().toString());
            lng = Double.parseDouble(lsMyteam.get(i).getLongitude().toString());
            final String Profileimage = lsMyteam.get(i).getImagePath();
            final String name = lsMyteam.get(i).getUserName().toString();
            final String Usermasterid = lsMyteam.get(i).getUserMasterId().toString();
            final String diff = lsMyteam.get(i).getDayDiff();
            String t = name;
            Pattern p = Pattern.compile("((^| )[A-Za-z])");
            Matcher m = p.matcher(t);
            String initials = "";
            while (m.find()) {
                initials += m.group().trim();
            }

            final String Title = initials.toUpperCase();
            LatLng locationplace = null;


            //   MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng)).title(name);
            if (lat.equals(0.0) || lng.equals(0.0)) {

            } else {
                locationplace = new LatLng(lat, lng);

            }


            //   String ImageUrl = "http://www.pngmart.com/files/3/Bill-Gates-PNG-File.png";



          /*  try {
                URL url = new URL(image);
                 bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                System.out.println(e);
            }*/
            if (lat.equals(0.0) || lng.equals(0.0)) {

            } else {
                final LatLng finalLocationplace = locationplace;
                if (locationplace != null)
                    locations.add(locationplace);
                final int finalI = i;
                Glide.with(getApplicationContext())
                        .asBitmap().
                        load(lsMyteam.get(i).getImagePath())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                MarkerOptions markerOptions = new MarkerOptions();

                                markerOptions.position(finalLocationplace);


                                if (diff.equals("0")) {
                                    if (Profileimage.contains("female") || Profileimage.contains("male")) {
                                        markerOptions.title(Title);


                                        marker = mMap.addMarker(markerOptions);
                                        // marker.showInfoWindow();
                                    } else {
                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(mCustomMarkerView, Profileimage)));
                                        // markerOptions.title(Title);

                                        marker = mMap.addMarker(markerOptions);
                                        //  marker.showInfoWindow();
                                    }


                                } else {
                                    //  markerOptions.position(finalLocationplace);


                                  /*  Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                            R.drawable.dots);
                       */         //    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getCroppedBitmap(mCustomMarkerView, bitmap)));
                                    markerOptions.title(Title);


                                    marker = mMap.addMarker(markerOptions);
                                    // marker.showInfoWindow();
                                }
                                         /*   mMap.addMarker(markerOptions);
                                            CameraPosition cameraPosition = new CameraPosition.Builder().
                                                    target(finalLocationplace).
                                                    zoom(9).
                                                    build();
                                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                            mMap.getUiSettings().setCompassEnabled(false);*/

                                           /* Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                                    R.drawable.dots);
                                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getCroppedBitmap(mCustomMarkerView, bitmap)));
                                            markerOptions.title(Title);*/

                                String id = marker.getId();
                                Log.d("Marker Id", id);
                                builder.include(marker.getPosition());

                                MyTeamBean myTeamBean = new MyTeamBean();

                                myTeamBean.setId(id);
                                myTeamBean.setUserMasterId(Usermasterid);
                                myTeamBean.setUserName(name);
                                myTeamBeen.add(myTeamBean);

                               /* CameraPosition cameraPosition = new CameraPosition.Builder().
                                        target(finalLocationplace).
                                        zoom(13).
                                        build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                mMap.getUiSettings().setCompassEnabled(false);*/
                                if (ActivityCompat.checkSelfPermission(GoogleMapTeamActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleMapTeamActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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



               /* mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        String name= String.valueOf(marker.getPosition().latitude);
                        Toast.makeText(GoogleMapTeamActivity.this,name,Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });*/


                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        String id = marker.getId();

                        for (int i = 0; i < myTeamBeen.size(); i++) {
                            if (myTeamBeen.get(i).getId().equals(id)) {
                                Intent intent = new Intent(getApplicationContext(), MyTeamMemberActivity.class);
                                intent.putExtra("UsermasterID", myTeamBeen.get(i).getUserMasterId());
                                intent.putExtra("Username", myTeamBeen.get(i).getUserName());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }
                        }

                          return false;
                    }
                });



            }

        }
        //   marker =mMap.addMarker(new MarkerOptions().position(sydney).title(LocationName));
        if(locations.size() != 0) {
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
            }catch (Exception e){
                e.printStackTrace();
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

    /*private Bitmap getMarkerBitmapFromView(View view, String image) {

        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(image).getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }

        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }


    public Bitmap getCroppedBitmap(View view, Bitmap bitmap) {
        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }*/
    public Bitmap getCroppedBitmap(View view, Bitmap bitmap) {
        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
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
        GoogleMapTeamActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
