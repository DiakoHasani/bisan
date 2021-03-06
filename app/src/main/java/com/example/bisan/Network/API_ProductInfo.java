package com.example.bisan.Network;

import android.content.Context;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.ProductItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class API_ProductInfo extends BaseModel implements BaseModel.ResponseListener {
    private String Api_Path="Product/detialProduct";

    private ApplicationClass.API_Listener api_listener;

    public BaseParam in_Id=new BaseParam("Id","","","integer","0",true);

    public API_ProductInfo(Context context) {
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

        ArrayList<ProductItem> arrayList=new ArrayList<>();
        try{

            JSONObject object=new JSONObject(answer);
                ProductItem item=new ProductItem();

                item.setProduct_id(object.getString("Id"));
                item.setProduct_name(object.getString("ProductName"));
                item.setProduct_img("http://besan.tdaapp.ir/"+object.getString("Pic"));
                item.setCategory_id(object.getString("FkCategory"));
                item.setPrice(object.getString("UnitPrice"));
                item.setUnit(object.getString("Discount"));
                item.setDescription(object.getString("Description"));
                item.setCategory_title(object.getString("TblCategoryTitel"));

                JSONArray imgArr=object.getJSONArray("TblMoreImages");
                for(int i=0;i<imgArr.length();i++){

                    JSONObject obj=imgArr.getJSONObject(i);
                    item.MoreImg.add(obj.getString("ImageUrl"));

                }

                arrayList.add(item);
        }catch (Exception e){}

        api_listener.onSuccess(Tag,"",arrayList,false);
    }
}
