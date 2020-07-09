package com.example.bisan.Network;

import android.content.Context;
import android.util.Log;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.ProductItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class API_ProductCustom extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="Product/AllFillter";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_page=new BaseParam("Page","","","integer","0",true);
    public BaseParam in_pageorder=new BaseParam("PageOrder","","","integer","false",true);
    public BaseParam in_newest=new BaseParam("Newest","","","integer","false",true);
    public BaseParam in_bestselling=new BaseParam("BestSelling","","","integer","false",true);
    public BaseParam in_name=new BaseParam("Name","","","integer","false",true);
    public BaseParam in_category=new BaseParam("Category","","","integer","false",true);
    public BaseParam in_unitprice=new BaseParam("UnitPrice","","","integer","10000000",true);
    public BaseParam in_productname=new BaseParam("ProductName","","","string","",true);
    public BaseParam in_listcategory=new BaseParam("ListCategory","","","integer","",true);

    public API_ProductCustom(Context context) {
        super(context,BaseModel.POST_METHOD);
        super.setApi_Path(Api_Path);

    }

    public void sendRequest(ApplicationClass.API_Listener listener,String Tag, JSONArray INparams){
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

        ArrayList<ProductItem> arrayList=new ArrayList<>();
        Log.i("Profucts",answer);
        try{
            JSONArray jsonArray=new JSONArray(answer);
            for(int i=0;i<jsonArray.length();i++){
                ProductItem item=new ProductItem();
                JSONObject object=jsonArray.getJSONObject(i);

                item.setProduct_id(object.getString("Id"));
                item.setProduct_name(object.getString("ProductName"));
                item.setProduct_img("http://besan.tdaapp.ir/"+object.getString("Pic"));
                item.setCategory_id(object.getString("FkCategory"));
                item.setPrice(object.getString("UnitPrice"));
//                item.setDescription(object.getString("Description"));
                arrayList.add(item);
            }
        }catch (Exception e){}
        api_listener.onSuccess(Tag,"",arrayList,false);
    }
}
