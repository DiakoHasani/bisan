package com.example.bisan.Network;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BaseModel {

    private String Api_Path;
    private String TAG;
    private ServerRequest serverRequest;
    private Context context;
    private ResponseListener responseListener;
    private int method_type;

    public static int POST_METHOD= Request.Method.POST;
    public static int GET_METHOD= Request.Method.GET;
    public static int PUT_METHOD=Request.Method.PUT;
    public static int DELETE_METHOD=Request.Method.DELETE;

    public void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void setApi_Path(String api_Path) {
        Api_Path = api_Path;
    }

    public BaseModel(Context context,int method){
        this.context=context;
        this.method_type=method;
        this.serverRequest=ServerRequest.getServerInstance(this.context);
    }

    public BaseModel(Context context,int method,String api_path){
        this.context=context;
        this.method_type=method;
        this.serverRequest=ServerRequest.getServerInstance(this.context);
        this.Api_Path=api_path;
    }

    public void AddToQueue(String tag, final BaseParam... inParams){
        try{
            this.TAG=tag;
//
//            Runnable runnable=new Runnable() {
//                @Override
//                public void run() {
                    Map<String,String> InParams=ConvertParamsToMap(inParams);
                    serverRequest.NewRequest(Api_Path, method_type, InParams, TAG, new ServerRequest.OnRequestResponse() {
                        @Override
                        public void onResponse(String response, String Tag, boolean hasError) {
                            if(Tag.compareToIgnoreCase(TAG)==0){
                                if(responseListener!=null){
                                    ConvertResponse(response,Tag,hasError);
                                }
                            }
                        }
                    });
//                }
//            };
//            Handler handler=new Handler();
//            handler.postDelayed(runnable,100);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void AddToQueue(String tag, final JSONArray inParams){
        try{
            this.TAG=tag;

//            Runnable runnable=new Runnable() {
//                @Override
//                public void run() {
                    serverRequest.NewRequest(Api_Path, method_type, inParams, TAG, new ServerRequest.OnRequestResponse() {
                        @Override
                        public void onResponse(String response, String Tag, boolean hasError) {
                            if(Tag.compareToIgnoreCase(TAG)==0){
                                if(responseListener!=null){
                                    ConvertResponse(response,Tag,hasError);
                                }
                            }
                        }
                    });
//                }
//            };
//            Handler handler=new Handler();
//            handler.postDelayed(runnable,100);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void AddToQueue(String tag, final JSONObject inParams){
        try{
            this.TAG=tag;

//            Runnable runnable=new Runnable() {
//                @Override
//                public void run() {
                    serverRequest.NewRequest(Api_Path, method_type, inParams, TAG, new ServerRequest.OnRequestResponse() {
                        @Override
                        public void onResponse(String response, String Tag, boolean hasError) {
                            if(Tag.compareToIgnoreCase(TAG)==0){
                                if(responseListener!=null){
                                    ConvertResponse(response,Tag,hasError);
                                }
                            }
                        }
                    });
//                }
//            };
//            Handler handler=new Handler();
//            handler.postDelayed(runnable,100);



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void AddToQueue(String tag, final JSONObject inParams, final boolean isStringAnswer){
        try{
            this.TAG=tag;

//            Runnable runnable=new Runnable() {
//                @Override
//                public void run() {
                    serverRequest.NewRequest(Api_Path, method_type, inParams, TAG, new ServerRequest.OnRequestResponse() {
                        @Override
                        public void onResponse(String response, String Tag, boolean hasError) {
                            if(Tag.compareToIgnoreCase(TAG)==0){
                                if(responseListener!=null){
                                    ConvertResponse(response,Tag,hasError);
                                }
                            }
                        }
                    },isStringAnswer);
//                }
//            };
//            Handler handler=new Handler();
//            handler.postDelayed(runnable,100);


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private Map<String,String> ConvertParamsToMap(BaseParam... params){
        Map<String,String> returnVal=new HashMap<>();
        try{
            if(params!=null) {
                for (int i = 0; i < params.length; i++) {

                    //--> Send Integer without single cotation
                    if (params[i].getType().compareToIgnoreCase("string") == 0) {

                        //--> If field val set
                        if (params[i].getVal().length() > 0) {
                            returnVal.put(params[i].getParamName(), "'" + params[i].getVal() + "'");
                        }
                    }

                    //--> Send Integer without single cotation
                    if (params[i].getType().compareToIgnoreCase("integer") == 0) {

                        //--> If field val set
                        if (params[i].getVal().length() > 0) {
                            returnVal.put(params[i].getParamName(), "" + params[i].getVal() + "");
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.i("Errorr","Collect Maps:"+e.toString());e.printStackTrace();}
        return returnVal;
    }

    public interface ResponseListener{
        public void onResponse(String Tag, String answer,String Error,boolean hasError);
    }

    private void ConvertResponse(String Answer,String Tag,boolean hasError){
        try{
            if(hasError) {
                responseListener.onResponse(Tag,"",Answer,hasError);
            }else {
                if (Answer == null) {
                    responseListener.onResponse(Tag, "", Answer, true);
                } else {
                    if (Answer.length() <= 0) {
                        responseListener.onResponse(Tag, "", Answer, true);
                    } else {
                        responseListener.onResponse(Tag, Answer, "", false);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
