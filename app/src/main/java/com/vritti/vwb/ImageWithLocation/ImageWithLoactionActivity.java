package com.vritti.vwb.ImageWithLocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.ekatm.services.GPSTracker;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.vworkbench.LoggingTimeActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ImageWithLoactionActivity extends Activity {
    private static File mediaFile;

    @BindView(R.id.clickImage)
    Button clickImage;

    @BindView(R.id.image)
    SimpleDraweeView image;

    @BindView(R.id.eAddress)
    TextView eAddress;

    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Bitmap bitmap;
    String currentAddres;
    GPSTracker locationGps = null;

    String gpsLat = "";
    String gpsLong = "";
    String gpsLatRef = "";
    String gpsLongRef = "";
    float latHasil;
    float longHasil;
    @BindView(R.id.fullImage)
    RelativeLayout fullImage;
    @BindView(R.id.locationImage)
    SimpleDraweeView locationImage;

    @BindView(R.id.saveImage)
    Button saveImage;

    @BindView(R.id.locationLayout)
    LinearLayout locationLayout;


    SamplePojoClass samplePojoClass = null;
    boolean isCreateNew = false;
    int pos = -1;
    String ActivityId,Locationname="",LocationType="";
    private String objStr="";

    static ArrayList<SamplePojoClass>samplePojoClasses=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_with_location);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            samplePojoClass = new Gson().fromJson(getIntent().getStringExtra("objectClass"), SamplePojoClass.class);

            Locationname=samplePojoClass.locationName;
            LocationType=samplePojoClass.getLocationType();

            isCreateNew = getIntent().getBooleanExtra("type", true);

                ActivityId = getIntent().getStringExtra("activityId");

            if (!isCreateNew) {
                fullImage.setVisibility(View.VISIBLE);
                saveImage.setVisibility(View.VISIBLE);
                image.setImageURI(Uri.parse(samplePojoClass.getImageUri()));
                pos = getIntent().getIntExtra("pos" , -1);
                locationLayout.setVisibility(View.GONE);
            } else {
                fullImage.setVisibility(View.GONE);
                saveImage.setVisibility(View.GONE);
            }

        }
    }

    @OnClick(R.id.clickImage)
    void setClickImage() {
        GPSTracker gpsTracker = new GPSTracker(this);
        getAddress(gpsTracker);
        try {
            if (ContextCompat.checkSelfPermission(ImageWithLoactionActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                }
            } else {

                captureImage();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAddress(GPSTracker location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        locationGps = location;
        // latString = String.valueOf(lat);
        // lngString = String.valueOf(lng);


        double curentLat = location.getLatitude();
        double curentLng = location.getLongitude();
        gpsLat = String.valueOf(curentLat);
        gpsLong = String.valueOf(curentLng);
                /*SharedHelper.putKey(context, "current_lat", current_lat);
            SharedHelper.putKey(context, "current_lng", current_lng);*/
        try {
            List<Address> addressList = geocoder.getFromLocation(curentLat, curentLng, 5);
            if (addressList.size() != 0) {
                String area = addressList.get(0).getAdminArea();
                String cName = addressList.get(0).getCountryName();
                String cityName = addressList.get(0).getLocality();
                currentAddres = cityName + "\n" + area + "\n" + cName;
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd(EEE) HH:mm",
                        Locale.getDefault()).format(new Date());
                eAddress.setText(addressList.get(0).getAddressLine(0) + "\n" + timeStamp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            eAddress.setText("No address found! please check your internet..");
        }
    }

    private void captureImage() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }


    public static File getOutputMediaFile(int type) {
        File mediaStorageDir;
        // External sdcard location
        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), SetAppName.IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(SetAppName.IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + SetAppName.IMAGE_DIRECTORY_NAME + " directory");
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


        }
        return mediaFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    try {
                        captureImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Permission", "Denied");
                }
                break;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                bitmap = null;
                try {
                    // bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    FileOutputStream out = new FileOutputStream(mediaFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "attachment", null);
                    fileUri = Uri.parse(url);
                    image.setImageURI(fileUri);
                    locationLayout.setVisibility(View.VISIBLE);

                    String baseUrl = "https://maps.googleapis.com/maps/api/staticmap?center=";
                    String strLocation = String.valueOf(locationGps.getLatitude()) + "," + String.valueOf(locationGps.getLongitude());
                    String baseUrl2 = "&zoom=16&size=600x300&maptype=roadmap&markers=color:red%7C";
                    String srtKey = "&key=";
                    String key = getResources().getString(R.string.google_map_key);
                    String path = baseUrl + strLocation + baseUrl2 + strLocation + srtKey + key;
                    locationImage.setImageURI(path);
                    fullImage.setVisibility(View.VISIBLE);
                    saveImage.setVisibility(View.VISIBLE);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {

            } else {

            }
        }
    }

    private Bitmap takescreenshotOfRootView(View v) {
        return takescreenshot(v);
    }

    private Bitmap takescreenshot(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }


    @OnClick(R.id.saveImage)
    void setScreeenShot() {

        Bitmap b = takescreenshotOfRootView(fullImage);
        if (b != null) {
            //  showScreenShotImage(b);//show bitmap over imageview

            File saveFile = getMainDirectoryName(this);//get the path to save screenshot
            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            File file = storeFile(b, "screenshot" + timeStamp + ".jpg", saveFile);//save the screenshot to selected path
            Date now = new Date(); // java.util.Date, NOT java.sql.Date
            // or
            // java.sql.Timestamp!



            String format2 = new SimpleDateFormat("dd-MMM-yy ")
                    .format(now);
            samplePojoClass=new SamplePojoClass();
            samplePojoClass.setImageUri(Uri.fromFile(file).toString());
            samplePojoClass.setAddress(eAddress.getText().toString().trim());
            samplePojoClass.setLat(gpsLat);
            samplePojoClass.setLng(gpsLong);
            samplePojoClass.setDate(format2);
            samplePojoClass.setActivityId(ActivityId);
            samplePojoClass.setLocationName(Locationname);
            samplePojoClass.setLocationType(LocationType);

            samplePojoClasses.add(samplePojoClass);
            objStr= new Gson().toJson(new Sample_List_Object(samplePojoClasses));
            AppCommon.getInstance(this).setEnoSampelList(objStr);



            try {
                captureImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

               /*setResult(998, new Intent()
                        .putExtra("objClass", objStr)
                        .putExtra("pos",pos));

            finish();
*/

        } else
            //If bitmap is null show toast message
            Toast.makeText(this, "Somthing went wroung", Toast.LENGTH_SHORT).show();
    }

    private File storeFile(Bitmap bm, String fileName, File saveFilePath) {
        File dir = new File(saveFilePath.getAbsolutePath());
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(saveFilePath.getAbsolutePath(), fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private File getMainDirectoryName(Context context) {
        File mainDir = new File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Demo");

        //If File is not present create directory
        if (!mainDir.exists()) {
            if (mainDir.mkdir())
                Log.e("Create Directory", "Main Directory Created : " + mainDir);
        }
        return mainDir;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* setResult(998, new Intent()
                .putExtra("objClass", objStr)
                .putExtra("pos",pos));

        finish();*/
    }
}