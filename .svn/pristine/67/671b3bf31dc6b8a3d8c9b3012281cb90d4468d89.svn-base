package com.vritti.crmlib.vcrm7;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crmlib.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {
    TextView btnSend,btndate;
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    public static OkHttpClient mClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_main2);
        btnSend = (TextView) findViewById(R.id.tvfcm);

        btndate = (TextView) findViewById(R.id.tvdate);

        mClient = new OkHttpClient();

        String refreshedToken = "eUfWuWfBq6M:APA91bE-G6H0V-nEetgYpZui1FCtRr5VPEHo4WWt4AWZa3BVXlewhOHaM0zy_uPlqyaXTio_JtDvgVrLLdYkQWM9YHch6TWyCKxmlDPLnilLFr8Wxyit8aFSaTV-KfLkCxynCUFBIzz1a6ahIMb6XRa9WjiMdOoOmw";//add your user refresh tokens who are logged in with firebase.

        final JSONArray jsonArray = new JSONArray();
        jsonArray.put(refreshedToken);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(jsonArray, "Hello", "How r u", "Http:\\google.com", "My Name is Pradnya");
            }
        });

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void sendMessage(final JSONArray recipients, final String title, final String body, final String icon, final String message) {

        new AsyncTask<String, String, String>() {
            @Override

            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);

                    JSONObject data = new JSONObject();
                    data.put("message", message);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);

                    String result = postToFCM(root.toString());
                    Log.d("Main Activity", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                    Toast.makeText(Main2Activity.this, "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Main2Activity.this, "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {


        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + "AIzaSyDAAC12_MZ625nxslf8odI-9LEuuoinCJU")
                .addHeader("Sender","id="+"215836600894")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
}
