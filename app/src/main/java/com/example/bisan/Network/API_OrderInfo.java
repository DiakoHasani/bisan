package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.OrderDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class API_OrderInfo extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="Order/FullDetail";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_id=new BaseParam("id","","","integer","",true);

    public API_OrderInfo(Context context) {
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

        ArrayList<OrderDetail> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(answer);
            for (int i = 0; i < jsonArray.length(); i++) {
                OrderDetail item = new OrderDetail();
                JSONObject object = jsonArray.getJSONObject(i);

                item.setProduct_name(object.getString("TblProductProductName"));
                item.setCount(object.getString("Weight"));
                item.setPrice(object.getString("UnitPrice"));
                arrayList.add(item);
            }
        }catch (Exception e){}
        api_listener.onSuccess(Tag, "", arrayList, false);
    }
}
