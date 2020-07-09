package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.Tools.ApplicationClass;

public class API_UserActivation extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="User/CheckSms";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_id=new BaseParam("id","","","integer","",true);
    public BaseParam in_sms=new BaseParam("sms","","","integer","",true);

    public API_UserActivation(Context context) {
        super(context,BaseModel.GET_METHOD);
        super.setApi_Path(Api_Path);

    }

    public void sendRequest(ApplicationClass.API_Listener listener,String Tag, BaseParam... INparams){
        try{
            this.api_listener=listener;

            super.setResponseListener(this);

            super.AddToQueue(Tag,INparams);

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
