package com.example.muhammadobaid.sims;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singleton {
    private static Singleton instance;
    private RequestQueue requestQueue;
    private static Context mctx;
    private Singleton(Context context){
        mctx=context;
        requestQueue=getRequestQueue();

    }

    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized Singleton getIsntance(Context context){
    if (instance==null){
        instance=new Singleton(context);
    }
    return instance;
    }
    public<T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
