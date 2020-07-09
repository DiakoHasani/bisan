package com.example.bisan.Network;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class VolleyJSONArrayRequest extends JsonArrayRequest {

    private String tag="";
    private onRequestResponseListener responseListener;

    public void setResponseListener(onRequestResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public VolleyJSONArrayRequest(int method, String url,JSONArray params, final onRequestResponseListener responseListener, final String Tag) {
        super(method, url,params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(responseListener!=null)responseListener.onResponse(response.toString(),Tag,false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(responseListener!=null)responseListener.onResponse(error,Tag,true);
            }
        });
        this.responseListener=responseListener;
    }




}
