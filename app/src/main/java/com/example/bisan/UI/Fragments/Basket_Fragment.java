package com.example.bisan.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bisan.DataTypes.FieldItem;
import com.example.bisan.DataTypes.RecordHolder;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.MessageItem;
import com.example.bisan.DataTypes.ProductItem;
import com.example.bisan.Tools.DBCursor;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.Tools.FarsiUtil;
import com.example.bisan.Network.API_BasketProduct;
import com.example.bisan.Network.API_CheckBon;
import com.example.bisan.Network.API_NewBasket;
import com.example.bisan.Network.API_UserWallet;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.UI.Activities.Login_Act;
import com.example.bisan.UI.Adapters.BasketAdapter;
import com.example.bisan.Tools.CToast;
import com.example.bisan.UI.Dialogs.CustomDialog;
import com.example.bisan.UI.ModalActivities.Modal_basket;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.EmptyHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;
import com.example.bisan.Tools.database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Basket_Fragment extends Fragment implements ApplicationClass.API_Listener {


    private int Page_Offset = 0,TotalPrice=0;
    private RecyclerView product_list;
    private BasketAdapter productAdapter;
    private String TAG_PRODUCT_INFO="categoryProduct",TAG_WALLET="UserWallet",TAG_BON="CheckBon",TAG_NEW_BASKET="NEwBasket",order_id="";
    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;
    private EditText bon;
    private EmptyHandler emptyHandler;
    private ArrayList<ProductItem> items;
    private TextView totalPrice,userWallet;
    private Button btnMap,btnClear,btnComplete;
    private EditText offCode;
    private DBExcute db;
    private boolean isBonOkay=false,canGoToPayment=false;

    public Basket_Fragment() {
        // Required empty public constructor
    }


    public static Basket_Fragment newInstance() {
        Basket_Fragment fragment = new Basket_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.Close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_basket_, container, false);

        product_list = (RecyclerView) view.findViewById(R.id.basket_fragment_list);

        db=DBExcute.getInstance(Basket_Fragment.this.getContext());
        db.Open();
        items=new ArrayList<>();

        productAdapter = new BasketAdapter(Basket_Fragment.this.getContext(), items);
        offCode=(EditText)view.findViewById(R.id.basket_fragment_offEditText);
        totalPrice=(TextView)view.findViewById(R.id.basket_fragment_basketPrice);
        userWallet=(TextView)view.findViewById(R.id.basket_fragment_walletPrice);
        btnClear=(Button)view.findViewById(R.id.basket_fragment_btnClear);
        btnComplete=(Button)view.findViewById(R.id.basket_fragment_btnComplete);
        btnMap=(Button)view.findViewById(R.id.basket_fragment_btnMap);
        bon=(EditText)view.findViewById(R.id.basket_fragment_offEditText);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        loadingHandler=new LoadingHandler(view);
        connectionFailHandler=new ConnectionFailHandler(view);
        connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowItems();
            }
        });
        emptyHandler=new EmptyHandler(view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);
        product_list.setLayoutManager(gridLayoutManager);
        product_list.setAdapter(productAdapter);

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(!canGoToPayment){
                        // When Product Info not Updated From server don't send User to Payment Gateway
                        new CToast(Basket_Fragment.this.getContext(),"لطفا صبر کنید",1,CToast.LONG_DURATION).Show();
                        return;
                    }
                    if(items.size()>0){

                        if(db.Read(database.QRY_GET_USER_INFO,null).HasRecord()) {
                            // Send User to Payment Gateway

                            API_NewBasket newBasket=new API_NewBasket(Basket_Fragment.this.getContext());

                            JSONObject params=new JSONObject();
                            params.put(newBasket.in_userID.getParamName(),Integer.valueOf(db.Read(database.QRY_GET_USER_INFO,null).GetField(6)));

                            params.put(newBasket.in_FkBon.getParamName(),"");
                            if(isBonOkay)params.put(newBasket.in_FkBonSerial.getParamName(),offCode.getText().toString());

                            params.put(newBasket.in_Discount.getParamName(),"");
                            params.put(newBasket.in_Total.getParamName(),TotalPrice);

                            JSONArray ProductDetails=new JSONArray();
                            for(int i=0;i<items.size();i++){
                                JSONObject object=new JSONObject();

                                object.put("FkProductId",Integer.valueOf(items.get(i).getProduct_id()));
                                object.put("Weight",Integer.valueOf(items.get(i).getCount()));
                                object.put("Paid",true);
                                object.put("UnitPrice",Integer.valueOf(items.get(i).getPrice()));

                                ProductDetails.put(object);
                            }
                            params.put(newBasket.in_ListProductOrderDetails.getParamName(),ProductDetails);

                            newBasket.sendRequest(Basket_Fragment.this,TAG_NEW_BASKET,params);

//                            Intent intent = new Intent(Basket_Fragment.this.getContext(), Modal_basket.class);
//                            startActivity(intent);
                        }else{
                            Intent intent=new Intent(Basket_Fragment.this.getContext(), Login_Act.class);
                            startActivity(intent);
                        }
                    }else{
                        new CToast(Basket_Fragment.this.getContext(),"سبد شما خالی است",1,CToast.LONG_DURATION).Show();
                    }
                }catch (Exception e){}
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
//                    Intent intent = new Intent(Basket_Fragment.this.getContext(), Modal_basket.class);
//                    startActivity(intent);
                    if(bon.getText().toString().length()>0){
                        isBonOkay=false;
                        GetBon(bon.getText().toString());
                    }
                }catch (Exception e){}
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final CustomDialog dialog=new CustomDialog(Basket_Fragment.this.getContext(),"آیا می خواهید سبد را خالی کنید؟","حذف سبد");
                    dialog.setOnclick(new CustomDialog.Onclick() {
                        @Override
                        public void onAcceptClick() {
                            try{
                                ApplicationClass.Vibration(Basket_Fragment.this.getContext());
                                db.Execute(database.QRY_DELETE_BASKET_INFO,null);
                                items.clear();
                                productAdapter.notifyDataSetChanged();
                                ShowItems();
                            }catch (Exception e){}
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }catch (Exception e){}
            }
        });

        productAdapter.setOnDeleteClicked(new BasketAdapter.Basketinterface() {
            @Override
            public void onDeleteItem(ProductItem item) {
                try{
                    ApplicationClass.Vibration(Basket_Fragment.this.getContext());
                    db.Execute(database.QRY_DELETE_BASKET_INFO_BY_ID,new RecordHolder(new FieldItem("'@i1'","'"+item.getProduct_id()+"'")));
                    int index=index_of_Item(item);
                    if(index>=0)items.remove(index);
                    productAdapter.notifyDataSetChanged();
                    CalcFactor();
                    CheckItemsSize();
                }catch (Exception e){}
            }

            @Override
            public void onMinusClick(ProductItem item) {
                try{
                    if(item.getCount()>1){
                        db.Execute(database.QRY_UPDATE_BASKET_INFO_DECREASE,new RecordHolder(new FieldItem("'@i1'","'"+item.getProduct_id()+"'")));
                        item.setCount(item.getCount()-1);
                        int index=index_of_Item(item);
                        if(index>=0)items.set(index,item);
                    }else{
                        //Nothing
                    }
                    productAdapter.notifyDataSetChanged();
                    CalcFactor();
                    CheckItemsSize();
                }catch (Exception e){}
            }

            @Override
            public void onPlusClick(ProductItem item) {
                try{
                    db.Execute(database.QRY_UPDATE_BASKET_INFO_INCREASE,new RecordHolder(new FieldItem("'@i1'","'"+item.getProduct_id()+"'")));
                    item.setCount(item.getCount()+1);
                    int index=index_of_Item(item);
                    if(index>=0)items.set(index,item);
                    productAdapter.notifyDataSetChanged();
                    CalcFactor();
                }catch (Exception e){}
            }
        });


        return view;
    }

    private void GetBon(String bon){
        try{
            if(loadingHandler!=null)loadingHandler.ShowLoading();

            API_CheckBon checkBon=new API_CheckBon(Basket_Fragment.this.getContext());
            checkBon.in_id.setVal(bon);
            checkBon.sendRequest(Basket_Fragment.this,TAG_BON,checkBon.in_id);

        }catch (Exception e){}
    }

    private void ShowItems(){
        try {
            items = new ArrayList<>();
            productAdapter.arrayList=new ArrayList<>();
            productAdapter.notifyDataSetChanged();
            db.Read(database.QRY_GET_BASKET_INFO,null).RecordFound(new DBCursor.ListCounter() {
                @Override
                public void onEachrecord(ArrayList<FieldItem> arr) {
                    ProductItem item = new ProductItem();
                    item.setProduct_id(arr.get(1).value);
                    item.setCount(Integer.valueOf(arr.get(2).value));
                    items.add(item);
                }
            }, new DBCursor.NoRecordListener() {
                @Override
                public void NoRecord() {
                    if (items.size() > 0) {
                        GetInfo();
                    } else {
                        emptyHandler.Show();
                    }

                    productAdapter.notifyDataSetChanged();
                }
            }, new DBCursor.FetchFinishListener() {
                @Override
                public void onFinish() {
                    if (items.size() > 0) {
                        GetInfo();
                    } else {
                        emptyHandler.Show();
                    }

                    productAdapter.notifyDataSetChanged();
                }
            }, 3);

        }catch (Exception e){}
    }

    public void GetInfo(){
        try{
            if (loadingHandler != null) loadingHandler.ShowLoading();
            if (connectionFailHandler != null)
                if (connectionFailHandler.isVisible()) connectionFailHandler.Hide();
            if (emptyHandler != null)
                if (emptyHandler.isVisible()) emptyHandler.Hide();

            if(items.size()>0){

                JSONArray array=new JSONArray();
                for(int i=0;i<items.size();i++){
                    array.put(Integer.valueOf(items.get(i).getProduct_id()));
                }
                API_BasketProduct api_basketProduct=new API_BasketProduct(Basket_Fragment.this.getContext());
                api_basketProduct.sendRequest(Basket_Fragment.this,TAG_PRODUCT_INFO,array);

                loadingHandler.ShowLoading();

            }else{
                emptyHandler.Show();
                loadingHandler.HideLoading();
            }
        }catch (Exception e){}
    }

    private void GetWallet(){
        try{
            API_UserWallet userWallet=new API_UserWallet(Basket_Fragment.this.getContext());
            userWallet.in_id.setVal(db.Read(database.QRY_GET_USER_INFO,null).GetField(6));
            userWallet.sendRequest(Basket_Fragment.this,TAG_WALLET,userWallet.in_id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                ShowItems();
                GetWallet();
            }
        };
        Handler handler=new Handler();
        handler.postDelayed(runnable,1000);
    }

    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        if(loadingHandler!=null)loadingHandler.HideLoading();
        if(hasError){

            if (Tag.compareToIgnoreCase(TAG_PRODUCT_INFO) == 0) {
                Toast.makeText(Basket_Fragment.this.getContext(),"خطا در بازیابی اطلاعات",Toast.LENGTH_LONG).show();
            }

            //If No Internet Connection Found
            if(Answer.equals(ServerRequest.NO_CONNECTION))
                if(connectionFailHandler!=null)
                    if(!connectionFailHandler.isVisible())connectionFailHandler.Show();
        }else {

            if (Tag.compareToIgnoreCase(TAG_NEW_BASKET)==0){
                ArrayList<MessageItem> item=new ArrayList<>();
                item.addAll(Items);

                //--> Check Server Price. to Check just add
                int serverPrice=0;
                try{serverPrice=Integer.valueOf(item.get(0).Code);}catch (Exception e){}
                serverPrice=serverPrice-987654;
                if(serverPrice!=TotalPrice){
                    //Do Something when Prices Are not equal
                }

                //--> Save Payment Details
                try {
                    db.Execute(database.QRY_UPDATE_SETTING_INFO_FIELD,new RecordHolder(new FieldItem("@f1","basket_hs"),new FieldItem("'@i1'","'"+item.get(0).Titel+"'")));
                    db.Execute(database.QRY_UPDATE_SETTING_INFO_FIELD,new RecordHolder(new FieldItem("@f1","totalPrice"),new FieldItem("'@i1'","'"+String.valueOf(serverPrice)+"'")));

                }catch (Exception e){
                    e.printStackTrace();
                }

                //--> If Payment done by wallet go to Paydone else Send to Payment Gateway
                Intent intent = new Intent(Basket_Fragment.this.getContext(), Modal_basket.class);
                if(item.get(0).Redirect.indexOf("ok")>=0){
                    try {
                        order_id=item.get(0).Redirect.substring(0,item.get(0).Redirect.indexOf("-"));
                        intent.putExtra("order_id",order_id);
                        String sh1Price=FarsiUtil.SHA1(String.valueOf(TotalPrice));
                        if (sh1Price.compareToIgnoreCase(item.get(0).Titel)!=0){
                            Toast.makeText(Basket_Fragment.this.getContext(), "مبلغ همخوانی ندارد", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    intent.putExtra("Code","ok");
                }else {

                    order_id=item.get(0).Redirect;
                    intent.putExtra("order_id",order_id);

                    //--> Check Price Base64 With Titel
                    try{
                        String B64=FarsiUtil.EncodeBase64(String.valueOf(serverPrice).trim());
                        B64=B64.replaceAll("\n", "");;
                        if(B64.compareTo(item.get(0).Titel)==0){
                            //--> Success
                        }else{
                            //--> Fail
                            Toast.makeText(Basket_Fragment.this.getContext(), "مبلغ همخوانی ندارد", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }catch (Exception e){}
                    intent.putExtra("Code","okNot");
                }
                startActivity(intent);
            }

            if(Tag.compareToIgnoreCase(TAG_WALLET)==0){
                userWallet.setText(FarsiUtil.ConvertDoubleToToman(FarsiUtil.ReplaceQoute(Answer)));
            }

            if(Tag.compareToIgnoreCase(TAG_BON)==0){
                if(Integer.valueOf(FarsiUtil.ReplaceQoute(Answer))>0) {
                    isBonOkay = true;
                    new CToast(Basket_Fragment.this.getContext(), "مبلغ " + FarsiUtil.ConvertDoubleToToman(FarsiUtil.ReplaceQoute(Answer))+" لحاظ گردید ", 1, CToast.LONG_DURATION).Show();
                }
            }


            if (Tag.compareToIgnoreCase(TAG_PRODUCT_INFO) == 0) {
                ArrayList<ProductItem> temp=new ArrayList<>();
                temp.addAll(Items);

//                ReplaceProductItems(items,Items);

                for(int i=0;i<temp.size();i++){
                    for(int j=0;j<items.size();j++) {
                        if (items.get(j).getProduct_id().compareToIgnoreCase(temp.get(i).getProduct_id()) == 0) {
                            ProductItem item=items.get(j);
                            item.setProduct_img(temp.get(i).getProduct_img());
                            item.setPrice(temp.get(i).getPrice());
                            item.setProduct_name(temp.get(i).getProduct_name());
                            items.set(j, item);
                        }
                    }
                }

                productAdapter.SetArray(items);
                productAdapter.notifyDataSetChanged();
                CalcFactor();
                canGoToPayment=true;
            }
        }
    }

    public void CheckItemsSize(){
        try{
            if(items.size()<=0){
                if(emptyHandler!=null)emptyHandler.Show();
            }
        }catch (Exception e){}
    }

    private void CalcFactor(){
        try{
            int sum_of_products=0;
            for(int i=0;i<items.size();i++){
                sum_of_products+=(items.get(i).getCount()*Integer.valueOf(items.get(i).getPrice()));
            }

            totalPrice.setText(FarsiUtil.ConvetPriceToToman(String.valueOf(sum_of_products)));
            TotalPrice=sum_of_products;
        }catch (Exception e){}
    }

    private int index_of_Item(ProductItem item){
        int index=-1;
        try{
            for(int i=0;i<items.size();i++){
                if(items.get(i).getProduct_id().compareToIgnoreCase(item.getProduct_id())==0){
                    index=i;
                    break;
                }
            }
        }catch (Exception e){}
        return index;
    }

    public void ReplaceProductItems(ArrayList<ProductItem> source,ArrayList<ProductItem> arr){
        try{
            for(int i=0;i<arr.size();i++){
                for(int j=0;j<source.size();j++){
                    if(source.get(j).getProduct_id().compareToIgnoreCase(arr.get(i).getProduct_id())==0){
                        source.set(j,arr.get(i));
                        break;
                    }
                }
            }
        }catch (Exception e){}
    }

    public void OnRfresh(){

    }

}
