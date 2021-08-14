package com.vritti.vwblib.receiver;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.vritti.databaselib.other.WebUrlClass;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    SharedPreferences userpreferences;
    private static final String TAG = "MyFirebaseIIDService";
    static String Token;

    @Override
    public  void onTokenRefresh() {
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        Token = userpreferences.getString("Token", null);
        if (Token == null || Token.equalsIgnoreCase("") || Token.equalsIgnoreCase(" ")) {
            //Getting registration token
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            //Displaying token on logcat
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString("Token", refreshedToken);
            editor.commit();
           // Token = userpreferences.getString("Token", null);
            Token =refreshedToken;
            //sendRegistrationToServer(Token);
           // Token();
        } else {
         /*  // sendRegistrationToServer(Token);
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            //Displaying token on logcat
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString("Token", refreshedToken);
            editor.commit();
            Token =refreshedToken;
            Token();*/
        }


    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project


    }
    public static String Token(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        String tokan=refreshedToken;
        return tokan;
    }
}