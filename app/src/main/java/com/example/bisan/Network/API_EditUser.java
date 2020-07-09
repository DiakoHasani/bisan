package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.Tools.ApplicationClass;

import org.json.JSONObject;

public class API_EditUser extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="User";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_id=new BaseParam("Id","","","integer","",true);
    public BaseParam in_fullname=new BaseParam("FullName","","","string","",true);
    public BaseParam in_email=new BaseParam("Email","","","string","",true);
    public BaseParam in_address=new BaseParam("Address","","","string","",true);
    public BaseParam in_status=new BaseParam("Status","","","integer","false",true);
    public BaseParam in_tel=new BaseParam("Tel","","","string","",true);

    public API_EditUser(Context context) {
        super(context,BaseModel.PUT_METHOD);
        super.setApi_Path(Api_Path);

    }

    public void sendRequest(ApplicationClass.API_Listener listener,String Tag, JSONObject INparams){
        try{
            this.api_listener=listener;

            super.setResponseListener(this);

            super.AddToQueue(Tag,INparams,true);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String Tag, String answer, String Error, boolean hasError) {
        //--> If Answer has No Records then just send to interface
        api_listener.onSuccess(Tag,answer,null,hasError);

    }
}
