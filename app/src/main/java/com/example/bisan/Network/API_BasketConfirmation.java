package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.DataTypes.MessageItem;
import com.example.bisan.Tools.ApplicationClass;

import org.json.JSONObject;

import java.util.ArrayList;

public class API_BasketConfirmation extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="Order/Confirmation";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_msg=new BaseParam("msg","","","integer","",true);
    public BaseParam in_id=new BaseParam("msg2","","","integer","",true);

    public API_BasketConfirmation(Context context) {
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
//        api_listener.onSuccess(Tag,answer,null,hasError);

        //--> If Error Occured
        if(hasError) {
            api_listener.onSuccess(Tag, answer, null, true);
            return;
        }

        //--> If Answer has Records
        try {
            ArrayList<MessageItem> item=ApplicationClass.MessageItem(new JSONObject(answer));
            api_listener.onSuccess(Tag,answer,item,hasError);
        }catch (Exception e){}
    }
}
