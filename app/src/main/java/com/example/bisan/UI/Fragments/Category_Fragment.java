package com.example.bisan.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bisan.DataTypes.FieldItem;
import com.example.bisan.DataTypes.RecordHolder;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.CategoryItem;
import com.example.bisan.DataTypes.ProductItem;
import com.example.bisan.Network.API_Category;
import com.example.bisan.Network.API_ProductCustom;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.Tools.DBCursor;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.Tools.DateTool;
import com.example.bisan.Tools.database;
import com.example.bisan.UI.Activities.Details_Act;
import com.example.bisan.UI.Adapters.CategoryAdapter;
import com.example.bisan.UI.Adapters.PrimaryProductAdapter;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.EmptyHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Category_Fragment extends Fragment implements ApplicationClass.API_Listener {


    public Category_Fragment() {
        // Required empty public constructor
    }

    public int Page_Offset = -1;
    private RecyclerView category_list, product_list;
    private CategoryAdapter categoryAdapter;
    private PrimaryProductAdapter productAdapter;
    private ArrayList<CategoryItem> arrayChoose;

    private String TAG_CATEGORY = "CATEGORY", TAG_PRODUCT = "categoryProduct", MaxPrice, SearchText;
    private boolean order_name, order_price, order_category, Expensive, BestSell, Newest,HasFillCategory=false;

    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;
    private EmptyHandler emptyHandler;
    private CategoryItem IntentCategoryitem;
    private DBExcute db;
    private View MainView;

    public static Category_Fragment newInstance() {
        Category_Fragment fragment = new Category_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void InitialObjects(){
        HasFillCategory=false;

        Page_Offset=-1;
        MaxPrice = "0";
        SearchText = "";
        order_name = BestSell = true;
        Newest = Expensive = order_price = order_category = false;

        category_list = (RecyclerView) MainView.findViewById(R.id.category_fragment_categoryList);
        product_list = (RecyclerView) MainView.findViewById(R.id.category_fragment_productList);

        categoryAdapter = new CategoryAdapter(this.getContext(), true);
        productAdapter = new PrimaryProductAdapter(this.getContext(),false,false);

        categoryAdapter.setItemClick(new CategoryAdapter.ItemClick() {
            @Override
            public void onItemClick(CategoryItem item) {
                try {
                    int index = GetIndex(categoryAdapter.arrayList,item);
                    boolean isSelect=item.isSelected();
                    item.setSelected(!isSelect);
                    if(!isSelect){
                        int chindex=GetIndex(arrayChoose,item);
                        if(chindex<0)arrayChoose.add(item);
                    }else{
                        int chindex=GetIndex(arrayChoose,item);
                        if(chindex>=0){
                            arrayChoose.remove(chindex);
                        }
                    }
                    categoryAdapter.arrayList.set(index,item);
                    categoryAdapter.setArrayList(categoryAdapter.arrayList);
                    Page_Offset = -1;
                    GetProducts();
                } catch (Exception e) {
                }
            }
        });

        GridLayoutManager category_Mlinear = new GridLayoutManager(this.getContext(),1,GridLayoutManager.HORIZONTAL,false);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        category_list.setLayoutManager(category_Mlinear);
        category_list.setAdapter(categoryAdapter);
        loadingHandler = new LoadingHandler(MainView);
        connectionFailHandler = new ConnectionFailHandler(MainView);
        connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCategories();
                GetProducts();
            }
        });
        emptyHandler = new EmptyHandler(MainView);

        categoryAdapter.setShowEmptyItems(true);
        productAdapter.setShowEmptyItems(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2, LinearLayoutManager.VERTICAL, false);
        product_list.setLayoutManager(gridLayoutManager);
        product_list.setAdapter(productAdapter);


        productAdapter.setItemClick(new PrimaryProductAdapter.ItemClick() {
            @Override
            public void onItemClick(ProductItem item) {
                try{
                    Intent intent=new Intent(Category_Fragment.this.getContext(), Details_Act.class);
                    intent.putExtra("product_id",item.getProduct_id());
                    startActivity(intent);
                }catch (Exception e){}
            }
        });
        product_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                try {
                    //--> Hide FAB Button if List Scroll Down. Show in Scroll Up
                    if (!product_list.canScrollVertically(1) && dy!=0) {
                        if (Page_Offset != -1 && Page_Offset!=-2)
                            GetProducts();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(MainView==null){
            arrayChoose=new ArrayList<>();
            MainView = inflater.inflate(R.layout.fragment_category, container, false);
            InitialObjects();
        }
        return MainView;
    }

    public void CategoryIntent(CategoryItem item) {
        try {
            IntentCategoryitem = item;
            Page_Offset = -1;
            GetCategories();
        } catch (Exception e) {
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

    public void OnSearchNameChanged(String text, boolean order_name, boolean order_price, boolean order_category, String maxPrice, boolean expansive, boolean newest, boolean bestsell) {
        Page_Offset = -1;
        this.Newest = newest;
        this.order_name = order_name;
        this.order_category = order_category;
        this.order_price = order_price;
        this.BestSell = bestsell;
        this.MaxPrice = maxPrice;
        this.SearchText = text;
        this.Expensive = expansive;
        GetProducts();
    }

    public void OnSearchNameChanged(String name) {
        Page_Offset = -1;
        this.SearchText = name;
        GetProducts();
    }

    private void GetCategories() {
        try {
            if(!HasFillCategory) {
                categoryAdapter.arrayList.clear();
                //Get Category From DB
                db.Read(database.QRY_GET_CATEGORY_INFO, null).RecordFound(new DBCursor.ListCounter() {
                    @Override
                    public void onEachrecord(ArrayList<FieldItem> record) {
                        CategoryItem categoryItem = new CategoryItem();
                        categoryItem.setCategory_id(record.get(2).value);
                        categoryItem.setCategory_name(record.get(1).value);
                        categoryItem.setCategory_img(record.get(3).value);
                        categoryAdapter.arrayList.add(categoryItem);
                    }
                }, new DBCursor.NoRecordListener() {
                    @Override
                    public void NoRecord() {
                        GetCategoryFromServer();
                    }
                }, new DBCursor.FetchFinishListener() {
                    @Override
                    public void onFinish() {
                        HasFillCategory=true;
                        if (loadingHandler != null) loadingHandler.HideLoading();
                        categoryAdapter.setShowEmptyItems(false);
                        categoryAdapter.notifyDataSetChanged();

                    }
                }, 4);
            }
        } catch (Exception e) {
        }
    }

    private void GetProducts() {
        JSONArray categoryArray = new JSONArray();
        try {
            //If Offset is finished search result is out
            if (Page_Offset == -2 && Page_Offset==0) return;
            if (Page_Offset == -1) {
                Page_Offset=0;
                productAdapter.arrayList.clear();
                productAdapter.notifyDataSetChanged();
            }

            if (loadingHandler != null) loadingHandler.ShowLoading();
            if (connectionFailHandler != null)
                if (connectionFailHandler.isVisible()) connectionFailHandler.Hide();
            if (emptyHandler != null)
                if (emptyHandler.isVisible()) emptyHandler.Hide();

            if (IntentCategoryitem != null) {
                CategoryItem item=new CategoryItem();
                if (categoryAdapter.arrayList.size() > 0) {
                    int index = GetIndex(categoryAdapter.arrayList,IntentCategoryitem);
                    if (index >= 0) {
                        CategoryItem temp=categoryAdapter.arrayList.get(index);
                        temp.setSelected(true);
                        arrayChoose.add(temp);
                    }
                }
                IntentCategoryitem = null;
            }
            if (arrayChoose.size() > 0) {
                for (int i = 0; i < arrayChoose.size(); i++) {
                    categoryArray.put(Integer.valueOf(arrayChoose.get(i).getCategory_id()));
                }
            }
        } catch (Exception e) {
        }
        API_ProductCustom api_productCustom = new API_ProductCustom(this.getContext());
        JSONArray params = new JSONArray();
        JSONObject paramObj = new JSONObject();
        try {
            api_productCustom.in_page.setVal(String.valueOf(Page_Offset));
            api_productCustom.in_bestselling.setVal(String.valueOf(BestSell));
            api_productCustom.in_category.setVal(String.valueOf(order_category));
            api_productCustom.in_name.setVal(String.valueOf(order_name));
            api_productCustom.in_newest.setVal(String.valueOf(Newest));
            api_productCustom.in_listcategory.setVal(categoryArray.toString());
            api_productCustom.in_pageorder.setVal(String.valueOf(order_price));
            api_productCustom.in_productname.setVal(SearchText);
            api_productCustom.in_unitprice.setVal(String.valueOf(MaxPrice));
        }catch (Exception e){}
        try{
            if (api_productCustom.in_page.getVal().length() > 0)
                paramObj.put(api_productCustom.in_page.getParamName(), Integer.valueOf(api_productCustom.in_page.getVal()));
            if (api_productCustom.in_unitprice.getVal().length() > 0)
                paramObj.put(api_productCustom.in_unitprice.getParamName(), Integer.valueOf(api_productCustom.in_unitprice.getVal()));
            if (api_productCustom.in_productname.getVal().length() > 0)
                paramObj.put(api_productCustom.in_productname.getParamName(), api_productCustom.in_productname.getVal());
            if (api_productCustom.in_pageorder.getVal().length() > 0)
                paramObj.put(api_productCustom.in_pageorder.getParamName(), Boolean.parseBoolean(api_productCustom.in_pageorder.getVal()));
            paramObj.put(api_productCustom.in_listcategory.getParamName(), categoryArray);
//            Log.i("Catg:","CatList"+categoryArray.toString());
            if (api_productCustom.in_newest.getVal().length() > 0)
                paramObj.put(api_productCustom.in_newest.getParamName(), Boolean.parseBoolean(api_productCustom.in_newest.getVal()));
            if (api_productCustom.in_name.getVal().length() > 0)
                paramObj.put(api_productCustom.in_name.getParamName(),  Boolean.parseBoolean(api_productCustom.in_name.getVal()));
            if (api_productCustom.in_category.getVal().length() > 0)
                paramObj.put(api_productCustom.in_category.getParamName(),  Boolean.parseBoolean(api_productCustom.in_category.getVal()));
            if (api_productCustom.in_bestselling.getVal().length() > 0)
                paramObj.put(api_productCustom.in_bestselling.getParamName(),  Boolean.parseBoolean(api_productCustom.in_bestselling.getVal()));

            params.put(paramObj);
        }catch (Exception e){}

        api_productCustom.sendRequest(Category_Fragment.this, TAG_PRODUCT, params);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (loadingHandler != null) loadingHandler.ShowLoading();

        db=DBExcute.getInstance(Category_Fragment.this.getContext());
        db.Open();

        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                GetCategories();
                if(Page_Offset<0)GetProducts();
            }
        };
        handler.postDelayed(runnable,1000);
        if(productAdapter.arrayList.size()>0){
            productAdapter.ReloadFavorites();
            productAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.Close();
    }

    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        if (loadingHandler != null) loadingHandler.HideLoading();
        if (hasError) {
            //If No Internet Connection Found
            if (Answer.equals(ServerRequest.NO_CONNECTION))
                if (connectionFailHandler != null)
                    if (!connectionFailHandler.isVisible()) connectionFailHandler.Show();
        } else {

            if (Tag.compareToIgnoreCase(TAG_CATEGORY) == 0) {

                //Save Categories In DB
                ArrayList<CategoryItem> temp=new ArrayList<>();
                temp.addAll(Items);
                db.Execute(database.QRY_DELETE_CATEGORY_INFO,null);
                for(int i=0;i<temp.size();i++){
                    CategoryItem item=temp.get(i);
                    db.Execute(database.QRY_INSERT_CATEGORY_INFO,new RecordHolder(new FieldItem("'@i1'","'"+item.getCategory_name()+"'"),new FieldItem("'@i2'","'"+item.getCategory_id()+"'"),new FieldItem("'@i3'","'"+item.getCategory_img()+"'")));
                }
                db.Execute(database.QRY_UPDATE_SETTING_INFO_FIELD,new RecordHolder(new FieldItem("@f1","category_timestamp"),new FieldItem("'@i1'","'"+DateTool.GetCurrentDateTime()+"'")));
                categoryAdapter.setShowEmptyItems(false);
                categoryAdapter.arrayList.clear();
                categoryAdapter.arrayList.addAll(temp);
                categoryAdapter.notifyDataSetChanged();
                HasFillCategory=true;
            }

            if (Tag.compareToIgnoreCase(TAG_PRODUCT) == 0) {

                productAdapter.setShowEmptyItems(false);
                if(Page_Offset==0){
                    productAdapter.arrayList.clear();
                }

                if (Page_Offset == 0 && Items.size() <= 0) {
                    if (emptyHandler != null) emptyHandler.Show();
                }

                if (Items == null) {
                    Page_Offset = -2;
                } else {
                    if(Page_Offset==0){productAdapter.arrayList.clear();}

                    if (Items.size() < 10) {
                        Page_Offset = -2;
                    }else{
                        Page_Offset++;
                    }


                    //region Avoid Duplicate add item
                    for(int i=0;i<Items.size();i++){
                        boolean canAdd=true;
                        for(int j=0;j<productAdapter.arrayList.size();j++){
                            ProductItem item=(ProductItem) Items.get(i);
                            if(productAdapter.arrayList.get(j).getProduct_id().compareToIgnoreCase(item.getProduct_id())==0){
                                canAdd=false;
                                break;
                            }
                        }
                        if(canAdd){
                            ProductItem Item=(ProductItem)Items.get(i);
                            productAdapter.arrayList.add(Item);
                        }
                    }
                    //endregion
                }
                productAdapter.ReloadFavorites();
                productAdapter.notifyDataSetChanged();

            }

        }
    }


    private int GetIndex(ArrayList<CategoryItem> arr,CategoryItem item){
        int index=-1;
        try{
            for(int i=0;i<arr.size();i++){
                if(arr.get(i).getCategory_id().compareToIgnoreCase(item.getCategory_id())==0){
                    index=i;
                    break;
                }
            }
        }catch (Exception e){}
        return index;
    }

    public void GetCategoryFromServer(){
        try{
            API_Category api_category = new API_Category(Category_Fragment.super.getContext());
            api_category.sendRequest(Category_Fragment.this, TAG_CATEGORY);
        }catch (Exception e){}
    }

    public void OnRfresh(){
        try{
            productAdapter.ReloadFavorites();
            productAdapter.notifyDataSetChanged();
        }catch (Exception e){}
    }

}
