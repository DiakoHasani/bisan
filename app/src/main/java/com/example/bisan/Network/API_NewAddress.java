package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.MessageItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class API_NewAddress extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="User/NewAddress";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_Address=new BaseParam("Address","","","string","",true);
    public BaseParam in_NameReceiver=new BaseParam("NameReceiver","","","string","",true);
    public BaseParam in_TelReceiver=new BaseParam("TelReceiver","","","string","",true);
    public BaseParam in_id=new BaseParam("Id","","","integer","false",true);
    public BaseParam in_FkUserId=new BaseParam("FkUserId","","","integer","",true);
    public BaseParam in_Lat=new BaseParam("Lat","","","string","",true);
    public BaseParam in_Lang=new BaseParam("Long","","","string","",true);
    public BaseParam in_OrderID=new BaseParam("FkOrder","","","integer","",true);

    public API_NewAddress(Context context) {
        super(context,BaseModel.POST_METHOD);
        super.setApi_Path(Api_Path);

    }

    public void sendRequest(ApplicationClass.API_Listener listener,String Tag,JSONObject INparams){
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
//        api_listener.onSuccess(Tag,answer,null,hasError);

        if(hasError){
            api_listener.onSuccess(Tag,answer,null,hasError);
        }else{
            try {
                ArrayList<MessageItem> item=ApplicationClass.MessageItem(new JSONObject(answer));
                api_listener.onSuccess(Tag,answer,item,hasError);
            }catch (Exception e){}
        }

    }
}
