package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.AddressItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class API_GetAddress extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="User/AllAddress";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_userID=new BaseParam("id","","","integer","",true);
//    public BaseParam in_FkBonSerial=new BaseParam("FkBonSerial","","","string","",true);
//    public BaseParam in_FkBon=new BaseParam("FkBon","","","integer","",true);
//    public BaseParam in_Discount=new BaseParam("Discount","","","integer","false",true);
//    public BaseParam in_Total=new BaseParam("Total","","","integer","",true);
//    public BaseParam in_ListProductOrderDetails=new BaseParam("ListProductOrderDetails","","","integer","",true);

    public API_GetAddress(Context context) {
        super(context,BaseModel.GET_METHOD);
        super.setApi_Path(Api_Path);

    }

    public void sendRequest(ApplicationClass.API_Listener listener,String Tag,BaseParam... INparams){
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

        if(hasError){
            api_listener.onSuccess(Tag,answer,null,hasError);
        }else{

            ArrayList<AddressItem> arrayList=new ArrayList<>();
            try {
                JSONObject obj = new JSONObject(answer);
                AddressItem item=new AddressItem();

                JSONArray addresses= obj.getJSONArray("AllAddress");
                JSONObject objAddress=addresses.getJSONObject(addresses.length()-1);

                item.address=objAddress.getString("Address");
                item.name=obj.getString("FullName");
                item.phone=obj.getString("CellPhone");
                item.lat=objAddress.getString("Lat");
                item.lang=objAddress.getString("Long");
                item.id=objAddress.getString("Id");
                arrayList.add(item);
            }catch (Exception e){}

            api_listener.onSuccess(Tag,answer,arrayList,hasError);
        }

    }
}
