package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.MessageItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class API_NewBasket extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="Order";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_userID=new BaseParam("FkUserId","","","integer","",true);
    public BaseParam in_FkBonSerial=new BaseParam("FkBonSerial","","","string","",true);
    public BaseParam in_FkBon=new BaseParam("FkBon","","","integer","",true);
    public BaseParam in_Discount=new BaseParam("Discount","","","integer","false",true);
    public BaseParam in_Total=new BaseParam("Total","","","integer","",true);
    public BaseParam in_ListProductOrderDetails=new BaseParam("ListProductOrderDetails","","","integer","",true);

    public API_NewBasket(Context context) {
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
