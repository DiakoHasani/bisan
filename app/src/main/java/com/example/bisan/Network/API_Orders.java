package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.OrderItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class API_Orders extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="Order/AllOrder";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_id=new BaseParam("idUser","","","integer","",true);

    public API_Orders(Context context) {
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

        ArrayList<OrderItem> arrayList = new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(answer);
            for (int i = 0; i < jsonArray.length(); i++) {
                OrderItem item = new OrderItem();
                JSONObject object = jsonArray.getJSONObject(i);

                item.setOrder_id(object.getString("id"));
                item.setName(object.getString("NameReceiver"));
                item.setAddress(object.getString("Address"));
                item.setDate(object.getString("DateOfPayment"));
                item.setRecieveTime(object.getString("ReceiveTime"));
                item.setPrice(object.getString("Total"));
                item.setStatus(object.getString("Status"));
                arrayList.add(item);
            }
        }catch (Exception e){}

        api_listener.onSuccess(Tag, "", arrayList, false);
    }
}
