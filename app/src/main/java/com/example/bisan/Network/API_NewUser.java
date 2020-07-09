package com.example.bisan.Network;

import android.content.Context;
import android.util.Log;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.MessageItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class API_NewUser extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="User";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_fullname=new BaseParam("FullName","","","string","",true);
    public BaseParam in_email=new BaseParam("Email","","","string","",true);
    public BaseParam in_cellphone=new BaseParam("CellPhone","","","string","",true);
//    public BaseParam in_status=new BaseParam("Status","","","integer","false",true);
    public BaseParam in_tel=new BaseParam("Tel","","","string","",true);

    public API_NewUser(Context context) {
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
        Log.i("Login",answer);
        if(hasError){
            api_listener.onSuccess(Tag,answer,null,hasError);
        }else{

            try {
                ArrayList<MessageItem> item=ApplicationClass.MessageItem(new JSONObject(answer));
                api_listener.onSuccess(Tag,answer,item,item.get(0).status);
            }catch (Exception e){}

        }

    }
}
