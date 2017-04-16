package com.example.helder.client;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.helder.client.WebServices.Singleton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by helder on 16/04/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() { //Catch the new token
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("MyFirebaseInstance",refreshedToken);

        if (MainActivity.UserID!=null) {

            String url = Utils.URL_PRINCIPAL + "/api/user/updateFBToken";
            StringRequest putRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // response
                    Log.d("Response", response);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error", error.toString());
                        }
                    }
            ) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userID", MainActivity.UserID);
                    params.put("fbToken", refreshedToken);

                    return params;
                }
            };
            Singleton.getInstance(this).addToRequestQeueu(putRequest);
        }
    }
}
