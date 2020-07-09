package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.DataTypes.QueryItem;
import com.example.bisan.Tools.ApplicationClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class API_AppVersion extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="User/UserLastOn";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_id=new BaseParam("id","","","string","",true);

    public API_AppVersion(Context context,String tel) {
        super(context,BaseModel.PUT_METHOD);
        super.setApi_Path(Api_Path+"?id="+tel);

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
        if(hasError) {
            api_listener.onSuccess(Tag, answer, null, hasError);
        }


        ArrayList<QueryItem> arr=new ArrayList<>();
        try{

            JSONArray jsonArray=new JSONArray(answer);

            for(int i=0;i<jsonArray.length();i++){
                QueryItem item=new QueryItem();

                JSONObject jsonObject=jsonArray.getJSONObject(i);

                item.Query=jsonObject.getString("Query");
                item.Vesrsion=jsonObject.getDouble("Vesrsion");
                item.AppVesrsion=jsonObject.getDouble("AppVesrsion");
                item.Status=jsonObject.getBoolean("Status");
                item.Cleardate=jsonObject.getBoolean("Cleardate");
                item.UpdateBazzar=jsonObject.getString("UpdateBazzar");
                item.UpdateGoogle=jsonObject.getString("UpdateGoogle");

                arr.add(item);

            }

        }catch (Exception e){}
        api_listener.onSuccess(Tag, answer, arr, hasError);
    }
}
