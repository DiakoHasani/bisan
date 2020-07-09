package com.example.bisan.Network;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.example.bisan.Tools.ApplicationClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class ServerRequest {

    public static String BaseURLImage="http://besan.tdaapp.ir/";
    private String BaseURL="http://besan.tdaapp.ir/api/";
    private RequestQueue myQueue;
    private static ServerRequest serverInstance;
    private Context context;
    public static String NO_CONNECTION="No Connection";


    public ServerRequest(Context context){
        myQueue=newRequestQueue(context.getApplicationContext());
        this.context=context.getApplicationContext();
    }

    public static synchronized ServerRequest getServerInstance(Context context) {
        try{if(serverInstance==null){
            serverInstance = new ServerRequest(context);
        }}catch (Exception e){e.printStackTrace();}
        return serverInstance;
    }

    private String ConvertParamToGet(Map<String,String> param){
        String answ="";
        try{
            int i=0;
            for(Map.Entry<String, String> entry : param.entrySet()){
                if(i>0)answ=answ+"&";
                answ=answ+entry.getKey() + "=" + entry.getValue();
                i++;
            }
        }catch (Exception e){}
        return answ;
    }

    public void NewRequest(final String Path, @Nullable int method, final Map<String,String> INparams, String tag, final OnRequestResponse requestResponse){
        try{

            if(NetworkUtil.getConnectivityStatus(this.context)!=0){

                String newPath=Path;
                if(method== Request.Method.GET && INparams.size()>0){
                    newPath=Path+"?"+ConvertParamToGet(INparams);
                }
                VolleyStringRequest request = new VolleyStringRequest(method, this.BaseURL + newPath, new onRequestResponseListener() {
                    @Override
                    public void onResponse(Object response, String Tag, boolean hasError) {
                        Log.i("Volley","Answer of Request : "+Path+" at "+ ApplicationClass.GetCurrentDateTime());
                        if (requestResponse != null) {
                            requestResponse.onResponse(response.toString(), Tag, hasError);
                        }
                    }
                }, tag) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        INparams.put("Content-Type", "application/x-www-form-urlencoded");
                        return INparams;
                    }
                };
                request.setTag(tag);
                request.setRetryPolicy(GetPolicy());
                Log.i("Volley", request.getBodyContentType() + " == " + request.toString()+" at "+ ApplicationClass.GetCurrentDateTime());
                myQueue.add(request);

                Log.i("ServerRequest","StringRequest Added to Queue Successfully!");

            }else{
                // No Network Connection Found!
                if(requestResponse!=null)requestResponse.onResponse(NO_CONNECTION,tag,true);
            }

        }catch (Exception e){
            Log.i("NewRequest"," Error in "+e.toString());
        }
    }

    public void NewRequest(final String Path, @Nullable int method, final JSONArray INparams, String tag, final OnRequestResponse requestResponse){
        try{

            if(NetworkUtil.getConnectivityStatus(this.context)!=0){

                //JSONArray Request
                VolleyJSONArrayRequest request = new VolleyJSONArrayRequest(method, this.BaseURL + Path,INparams, new onRequestResponseListener() {
                    @Override
                    public void onResponse(Object response, String Tag, boolean hasError) {
                        Log.i("Volley","Answer of Request : "+Path+" at "+ ApplicationClass.GetCurrentDateTime());
                        if (requestResponse != null) {
                            requestResponse.onResponse(response.toString(), Tag, hasError);
                        }
                    }
                }, tag);
                request.setTag(tag);
//                request.setRetryPolicy(GetPolicy());
                Log.i("Volley", request.getBodyContentType() + " == " + request.toString()+" at "+ ApplicationClass.GetCurrentDateTime());
                myQueue.add(request);

                Log.i("ServerRequest","JSONRequest Added to Queue Successfully!"+INparams.toString());

            }else{
                // No Network Connection Found!
                if(requestResponse!=null)requestResponse.onResponse(NO_CONNECTION,tag,true);
            }

        }catch (Exception e){
            Log.i("NewRequest"," Error in "+e.toString());
        }
    }

    public void NewRequest(final String Path, @Nullable int method, final JSONObject INparams, String tag, final OnRequestResponse requestResponse){
        try{

            if(NetworkUtil.getConnectivityStatus(this.context)!=0){

                //JSONArray Request
                VolleyJSONObjectRequest request = new VolleyJSONObjectRequest(method, this.BaseURL + Path,INparams, new onRequestResponseListener() {
                    @Override
                    public void onResponse(Object response, String Tag, boolean hasError) {
                        Log.i("Volley","Answer of Request : "+Path+" at "+ ApplicationClass.GetCurrentDateTime());
                        if (requestResponse != null) {
                            requestResponse.onResponse(response.toString(), Tag, hasError);
                        }
                    }
                }, tag);
                request.setTag(tag);
                request.setRetryPolicy(GetPolicy());
                Log.i("Volley", request.getBodyContentType() + " == " + request.toString() +" at "+ ApplicationClass.GetCurrentDateTime());
                myQueue.add(request);

                Log.i("ServerRequest","JSONRequest Added to Queue Successfully!"+INparams.toString());

            }else{
                // No Network Connection Found!
                if(requestResponse!=null)requestResponse.onResponse(NO_CONNECTION,tag,true);
            }

        }catch (Exception e){
            Log.i("NewRequest"," Error in "+e.toString());
        }
    }

    public void NewRequest(final String Path,final int method, final JSONObject INparams, String tag, final OnRequestResponse requestResponse,boolean isAnswerString){
        try{

            if(NetworkUtil.getConnectivityStatus(this.context)!=0){


                VolleyStringRequest request = new VolleyStringRequest(method, this.BaseURL + Path, new onRequestResponseListener() {
                    @Override
                    public void onResponse(Object response, String Tag, boolean hasError) {
                        Log.i("Volley","Answer of Request : "+Path+" at "+ ApplicationClass.GetCurrentDateTime());
                        if (requestResponse != null) {
                            requestResponse.onResponse(response.toString(), Tag, hasError);
                        }
                    }
                }, tag) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return INparams.toString().getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }

                };
                request.setTag(tag);
                Log.i("Volley", request.getBodyContentType() + " == " + request.toString()+" at "+ ApplicationClass.GetCurrentDateTime());
                myQueue.add(request);

                Log.i("ServerRequest","StringRequest Added to Queue Successfully!");

            }else{
                // No Network Connection Found!
                if(requestResponse!=null)requestResponse.onResponse(NO_CONNECTION,tag,true);
            }

        }catch (Exception e){
            Log.i("NewRequest"," Error in "+e.toString());
        }
    }


    public interface OnRequestResponse{
        public void onResponse(String response,String Tag,boolean hasError);
    }

    private DefaultRetryPolicy GetPolicy(){
        return new DefaultRetryPolicy(0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }


}
