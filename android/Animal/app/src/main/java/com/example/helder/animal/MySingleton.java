package com.example.helder.animal;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by helder on 27/03/2017.
 */

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue mRequestQeue;
    private static Context mCtx;

    private MySingleton(Context context){
        mCtx = context;
        mRequestQeue = getRequestQeue();
    }

    public static synchronized MySingleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQeue(){
        if(mRequestQeue == null){
            mRequestQeue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQeue;
    }

    public <T> void addToRequestQeueu(Request<T> req){
        getRequestQeue().add(req);
    }
}
