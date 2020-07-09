package com.example.bisan.Network;

import android.content.Context;
import android.util.Log;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.CategoryItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class API_Category extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="Product/AllCategories";

    private ApplicationClass.API_Listener api_listener;


    public API_Category(Context context) {
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
        //api_listener.onSuccess(Tag,answer,null,hasError);

        //--> If Error Occured
        if(hasError) {
            api_listener.onSuccess(Tag, answer, null, true);
            return;
        }

        //--> If Answer has Records
//        Log.i("Filter",answer);
        ArrayList<CategoryItem> arrayList=new ArrayList<>();
        try{

            JSONArray jsonArray=new JSONArray(answer);
            for(int i=0;i<jsonArray.length();i++){
                CategoryItem item=new CategoryItem();
                JSONObject object=jsonArray.getJSONObject(i);

                item.setCategory_id(object.getString("Id"));
                item.setCategory_name(object.getString("Titel"));
                item.setCategory_img("http://besan.tdaapp.ir/"+object.getString("Image"));
                arrayList.add(item);
            }
        }catch (Exception e){}

        api_listener.onSuccess(Tag,"",arrayList,false);
    }
}
