package com.example.bisan.Network;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class VolleyStringRequest extends StringRequest{

    private String tag="";
    private onRequestResponseListener responseListener;

    public void setResponseListener(onRequestResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public VolleyStringRequest(int method, String url, final onRequestResponseListener responseListener, final String Tag) {
        super(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(responseListener!=null)responseListener.onResponse(response,Tag,false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(responseListener!=null)responseListener.onResponse(error,Tag,true);
            }
        });
        this.responseListener=responseListener;
    }

    public VolleyStringRequest(String url, final onRequestResponseListener responseListener, final String Tag) {
        super(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(responseListener!=null)responseListener.onResponse(response,Tag,false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(responseListener!=null)responseListener.onResponse(error,Tag,true);
            }
        });
        this.responseListener=responseListener;
    }

    public VolleyStringRequest(int post, String s1, Response.Listener<String> listener, Response.ErrorListener s, String url) {
        super(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }



}
