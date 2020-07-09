package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.SliderItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class API_Slider extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="Slider/All";

    private ApplicationClass.API_Listener api_listener;

//    public BaseParam in_id=new BaseParam("id","","","string","",true);

    public API_Slider(Context context) {
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

        ArrayList<SliderItem> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(answer);
            for (int i = 0; i < jsonArray.length(); i++) {
                SliderItem item = new SliderItem();
                JSONObject object = jsonArray.getJSONObject(i);

                item.setId(object.getString("Id"));
                item.setDescription(object.getString("Description"));
                item.setImage(object.getString("Image"));
                item.setOrder(object.getString("order"));
                item.setMain(object.getBoolean("MainSlider"));
                arrayList.add(item);
            }
        }catch (Exception e){}
        api_listener.onSuccess(Tag, "", arrayList, false);
    }
}
