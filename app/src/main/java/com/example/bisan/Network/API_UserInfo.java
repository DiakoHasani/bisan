package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.UserItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class API_UserInfo extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="User/Info";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_id=new BaseParam("id","","","integer","",true);

    public API_UserInfo(Context context) {
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

        ArrayList<UserItem> arrayList = new ArrayList<>();
        try {

                UserItem item = new UserItem();
                JSONObject object = new JSONObject(answer);

                item.setId(object.getString("Id"));
                item.setName(object.getString("FullName"));
                item.setPhone(object.getString("CellPhone"));
//                item.setAddress(object.getString("FkCategory"));
                item.setEmail(object.getString("Email"));
                item.setTel(object.getString("Tel"));
                arrayList.add(item);
        }catch (Exception e){}

        api_listener.onSuccess(Tag, answer, arrayList, false);
    }
}
