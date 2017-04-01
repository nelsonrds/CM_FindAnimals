package com.example.helder.client.WebServices;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Nelson on 31/03/2017.
 */

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by helder on 27/03/2017.
 */

public class Singleton {
    private static Singleton mInstance;
    private RequestQueue mRequestQeue;
    private static Context mCtx;

    private Singleton(Context context){
        mCtx = context;
        mRequestQeue = getRequestQeue();
    }

    public static synchronized Singleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new Singleton(context);
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
