package com.example.bisan.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bisan.DataTypes.FieldItem;
import com.example.bisan.DataTypes.RecordHolder;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.CategoryItem;
import com.example.bisan.DataTypes.ProductItem;
import com.example.bisan.DataTypes.SliderItem;
import com.example.bisan.Network.API_Category;
import com.example.bisan.Network.API_MostSell;
import com.example.bisan.Network.API_Offer;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.Network.API_Slider;
import com.example.bisan.R;
import com.example.bisan.Tools.DBCursor;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.Tools.DateTool;
import com.example.bisan.Tools.database;
import com.example.bisan.UI.Activities.Details_Act;
import com.example.bisan.UI.Adapters.CategoryAdapter;
import com.example.bisan.UI.Adapters.ImageAdapter;
import com.example.bisan.UI.Adapters.PrimaryProductAdapter;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

public class Home_Fragment extends Fragment implements ApplicationClass.API_Listener {

    private  RecyclerView Category_recycler, MostSell_Recycler, Offer_Recycler;
    private static PrimaryProductAdapter MostSell_Adapter, Offer_Adapter;
    private CategoryAdapter Category_Adapter;
    private String TAG_CATEGORY = "home_category",
            TAG_MOSTSELL = "home_mostsell",
            TAG_COMMERCIAL = "home_commercial",
            TAG_OFFER = "home_offer";

    private boolean HasFilledCategory=false,HasFilledProduct=false;
    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;
    private ArrayList<SliderItem> sliderItems,sliderItemsDown;
    private SwipeRefreshLayout refreshLayout;
    private ShimmerFrameLayout shimmerFrameLayout,shimmerFrameLayoutDown;
    private DBExcute db;
    private View MainView;

    //    Slider slider;
    private static int NUM_PAGES = 1,NUM_PAGESDown=1;
    private ViewPager mPager,mPagerDown;
    private PageIndicatorView pageIndicatorView,pageIndicatorViewDown;
    private static OnCategoryClick onCategoryClick;


    public void setOnCategoryClick(OnCategoryClick onCategoryClick) {
        this.onCategoryClick = onCategoryClick;
        Log.i("e","oo");
    }

    public OnCategoryClick getOnCategoryClick() {
        return onCategoryClick;
    }

    public Home_Fragment() {
        // Required empty public constructor
    }

    public interface OnCategoryClick {
        public void onClick(CategoryItem item);
    }

    // TODO: Rename and change types and number of parameters
    public static Home_Fragment newInstance(OnCategoryClick click) {
        Home_Fragment fragment = new Home_Fragment();
        fragment.onCategoryClick=click;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void InitialObject(View view){
        HasFilledProduct=HasFilledCategory=false;

        //--> Initial UI Objects
        Category_recycler = (RecyclerView) view.findViewById(R.id.home_fragment_categoryList);
        MostSell_Recycler = (RecyclerView) view.findViewById(R.id.home_fragment_mostSellList);
        Offer_Recycler = (RecyclerView) view.findViewById(R.id.home_fragment_referList);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_fragment_refresh);
        shimmerFrameLayout=(ShimmerFrameLayout)view.findViewById(R.id.home_shimmer);
        shimmerFrameLayoutDown=(ShimmerFrameLayout)view.findViewById(R.id.home_shimmer2);
        shimmerFrameLayout.startShimmerAnimation();
        shimmerFrameLayoutDown.startShimmerAnimation();

        loadingHandler = new LoadingHandler(view);
        connectionFailHandler = new ConnectionFailHandler(view);
        connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_Request_To_Server();
            }
        });
        sliderItems = new ArrayList<>();
        sliderItemsDown=new ArrayList<>();

        //--> Initial Adapters
        Category_Adapter = new CategoryAdapter(this.getContext(), false);
        MostSell_Adapter = new PrimaryProductAdapter(this.getContext(),false,true);
        Offer_Adapter = new PrimaryProductAdapter(this.getContext(),false,true);

        //--> Set Adapters to RecyclerViews
        GridLayoutManager category_Mlinear = new GridLayoutManager(this.getContext(),1,GridLayoutManager.HORIZONTAL,false);
//        category_Mlinear.setOrientation(LinearLayoutManager.HORIZONTAL);
        Category_recycler.setLayoutManager(category_Mlinear);

        Category_Adapter.setItemClick(new CategoryAdapter.ItemClick() {
            @Override
            public void onItemClick(CategoryItem item) {
                try {
                    if (Home_Fragment.this.onCategoryClick != null) onCategoryClick.onClick(item);
                } catch (Exception e) {
                }
            }
        });

        LinearLayoutManager mostsell_Mlinear = new LinearLayoutManager(this.getContext());
        mostsell_Mlinear.setOrientation(LinearLayoutManager.HORIZONTAL);
        MostSell_Recycler.setLayoutManager(mostsell_Mlinear);

        MostSell_Adapter.setItemClick(new PrimaryProductAdapter.ItemClick() {
            @Override
            public void onItemClick(ProductItem item) {
                try{
                    Intent intent=new Intent(Home_Fragment.this.getContext(),Details_Act.class);
                    intent.putExtra("product_id",item.getProduct_id());
                    startActivity(intent);
                }catch (Exception e){}
            }
        });

        LinearLayoutManager offer_Mlinear = new LinearLayoutManager(this.getContext());
        offer_Mlinear.setOrientation(LinearLayoutManager.HORIZONTAL);
        Offer_Recycler.setLayoutManager(offer_Mlinear);

        Offer_Adapter.setItemClick(new PrimaryProductAdapter.ItemClick() {
            @Override
            public void onItemClick(ProductItem item) {
                try{
                    Intent intent=new Intent(Home_Fragment.this.getContext(), Details_Act.class);
                    intent.putExtra("product_id",item.getProduct_id());
                    startActivity(intent);
                }catch (Exception e){}
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    refreshLayout.setRefreshing(false);
                    Offer_Adapter.arrayList.clear();
                    Category_Adapter.arrayList.clear();
                    MostSell_Adapter.arrayList.clear();
                    HasFilledCategory=false;
                    HasFilledProduct=false;
                    sliderItems.clear();
                    sliderItemsDown.clear();
                    Send_Request_To_Server();
                } catch (Exception e) {
                }
            }
        });

        pageIndicatorView = view.findViewById(R.id.pageIndicatorView);
        pageIndicatorViewDown = view.findViewById(R.id.pageIndicatorView2);

        mPager = (ViewPager) view.findViewById(R.id.view_pager_commercial);
        mPagerDown = (ViewPager) view.findViewById(R.id.view_pager_commercial2);

    }

    private void UI_Initial(){
        //--> Initial ArrayLists
        db=DBExcute.getInstance(this.getContext());
        db.Open();

        Category_recycler.setAdapter(Category_Adapter);
        MostSell_Recycler.setAdapter(MostSell_Adapter);
        Offer_Recycler.setAdapter(Offer_Adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(MainView==null)MainView = inflater.inflate(R.layout.fragment_home, container, false);
        if(Category_recycler==null)InitialObject(MainView);
        UI_Initial();
        return MainView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.Close();
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
        if(!HasFilledCategory || !HasFilledProduct) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Send_Request_To_Server();
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable, 1000);
//        }
         }else{
                    MostSell_Adapter.ReloadFavorites();
                    MostSell_Adapter.notifyDataSetChanged();

                    Offer_Adapter.ReloadFavorites();
                    Offer_Adapter.notifyDataSetChanged();
                }

    }


    private void Send_Request_To_Server() {
        if (loadingHandler != null && (!HasFilledCategory || !HasFilledProduct )) loadingHandler.ShowLoading();
        if (connectionFailHandler != null)
            if (connectionFailHandler.isVisible()) connectionFailHandler.Hide();


        if(!HasFilledCategory) {
            Category_Adapter.arrayList=new ArrayList<>();
            Category_Adapter.notifyDataSetChanged();
            //Get Category From DB
            db.Read(database.QRY_GET_CATEGORY_INFO, null).RecordFound(new DBCursor.ListCounter() {
                @Override
                public void onEachrecord(ArrayList<FieldItem> record) {
                    CategoryItem categoryItem = new CategoryItem();
                    categoryItem.setCategory_id(record.get(2).value);
                    categoryItem.setCategory_name(record.get(1).value);
                    categoryItem.setCategory_img(record.get(3).value);
                    Category_Adapter.arrayList.add(categoryItem);
                }
            }, new DBCursor.NoRecordListener() {
                @Override
                public void NoRecord() {
                    GetCategories();
                }
            }, new DBCursor.FetchFinishListener() {
                @Override
                public void onFinish() {
                    if (loadingHandler != null) loadingHandler.HideLoading();
                    Category_Adapter.setShowEmptyItems(false);
                    Category_Adapter.notifyDataSetChanged();

                    HasFilledCategory=true;

                    // if category Update time is over (Update every day)
                    if (DateTool.GetDurationFromNow(DateTool.ConvertStringToDate(db.Read(database.QRY_GET_SETTING_INFO, null).GetField(1))) > 24)
                        GetCategories();
                }
            }, 4);
        }else{
            Category_Adapter.notifyDataSetChanged();
        }

        if(!HasFilledProduct) {
            MostSell_Adapter.arrayList=new ArrayList<>();
            MostSell_Adapter.notifyDataSetChanged();

            Offer_Adapter.arrayList=new ArrayList<>();
            Offer_Adapter.notifyDataSetChanged();
            //Get Products From DB
            db.Read(database.QRY_GET_PRODUCT_INFO, null).RecordFound(new DBCursor.ListCounter() {
                @Override
                public void onEachrecord(ArrayList<FieldItem> record) {
                    ProductItem Item = new ProductItem();
                    Item.setProduct_id(record.get(1).value);
                    Item.setProduct_name(record.get(2).value);
                    Item.setPrice(record.get(5).value);
                    Item.setProduct_img(record.get(3).value);
                    if (Integer.valueOf(record.get(4).value) == 1) {

                        MostSell_Adapter.setShowEmptyItems(false);
                        MostSell_Adapter.arrayList.add(Item);
                    } else {

                        Offer_Adapter.setShowEmptyItems(false);
                        Offer_Adapter.arrayList.add(Item);
                    }
                }
            }, new DBCursor.NoRecordListener() {
                @Override
                public void NoRecord() {
                    GetProducts();
                }
            }, new DBCursor.FetchFinishListener() {
                @Override
                public void onFinish() {
                    if (loadingHandler != null) loadingHandler.HideLoading();
                    MostSell_Adapter.setShowEmptyItems(false);

                    MostSell_Adapter.ReloadFavorites();
                    MostSell_Adapter.notifyDataSetChanged();

                    Offer_Adapter.setShowEmptyItems(false);
                    Offer_Adapter.ReloadFavorites();
                    Offer_Adapter.notifyDataSetChanged();

                    HasFilledProduct=true;

                    // if Product Update time is over (Update every 1 Hour)
                    if (DateTool.GetDurationFromNow(DateTool.ConvertStringToDate(db.Read(database.QRY_GET_SETTING_INFO, null).GetField(0))) > 1)
                        GetProducts();
                }
            }, 6);
        }

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();

        shimmerFrameLayoutDown.setVisibility(View.VISIBLE);
        shimmerFrameLayoutDown.startShimmerAnimation();

        API_Slider api_slider = new API_Slider(Home_Fragment.super.getContext());
        api_slider.sendRequest(Home_Fragment.this, TAG_COMMERCIAL);
    }

    private void GetCategories(){
        loadingHandler.ShowLoading();
        Category_Adapter.setShowEmptyItems(true);
        Category_Adapter.notifyDataSetChanged();

        API_Category api_category = new API_Category(Home_Fragment.super.getContext());
        api_category.sendRequest(Home_Fragment.this, TAG_CATEGORY);
    }

    private void GetProducts(){
        loadingHandler.ShowLoading();

        MostSell_Adapter.setShowEmptyItems(true);
        MostSell_Adapter.notifyDataSetChanged();

        Offer_Adapter.setShowEmptyItems(true);
        Offer_Adapter.notifyDataSetChanged();

        API_MostSell api_mostSell = new API_MostSell(Home_Fragment.super.getContext());
        api_mostSell.sendRequest(Home_Fragment.this, TAG_MOSTSELL);

        API_Offer api_offer = new API_Offer(Home_Fragment.super.getContext());
        api_offer.sendRequest(Home_Fragment.this, TAG_OFFER);
    }

    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        if (loadingHandler != null) loadingHandler.HideLoading();
        //<editor-fold desc="Hidden Slider Shimmer layout">
        if(Tag.compareToIgnoreCase(TAG_COMMERCIAL)==0){
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
        }
        //</editor-fold>

        if (hasError) {

            //region No Internet Connection
            if (Answer.equals(ServerRequest.NO_CONNECTION))
                if (connectionFailHandler != null)
                    if (!connectionFailHandler.isVisible()) connectionFailHandler.Show();
            //endregion
        } else {

            //region Category Delivered
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
                Category_Adapter.setShowEmptyItems(false);
                Category_Adapter.arrayList.clear();
                Category_Adapter.arrayList.addAll(temp);
                Category_Adapter.notifyDataSetChanged();
            }
            //endregion

            //region Product Delivered Part MostSells
            if (Tag.compareToIgnoreCase(TAG_MOSTSELL) == 0) {
                //Save Products In DB
                ArrayList<ProductItem> temp=new ArrayList<>();
                temp.addAll(Items);
                OnProductResult(temp,1);
                MostSell_Adapter.setShowEmptyItems(false);
                MostSell_Adapter.arrayList.clear();
                MostSell_Adapter.arrayList.addAll(temp);
                MostSell_Adapter.notifyDataSetChanged();
            }
            //endregion

            //region Product Delivered Part Best Offers
            if (Tag.compareToIgnoreCase(TAG_OFFER) == 0) {
                //Save Products In DB
                ArrayList<ProductItem> temp=new ArrayList<>();
                temp.addAll(Items);
                OnProductResult(temp,0);

                Offer_Adapter.setShowEmptyItems(false);
                Offer_Adapter.arrayList.clear();
                Offer_Adapter.arrayList.addAll(temp);
                Offer_Adapter.notifyDataSetChanged();
            }
            //endregion

            // <editor-fold defaultstate="collapsed" desc="Slider Answer Delivered">
            if (Tag.compareToIgnoreCase(TAG_COMMERCIAL) == 0) {
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayoutDown.setVisibility(View.GONE);

                ArrayList<SliderItem> arrayTemp=new ArrayList<>();
                arrayTemp.addAll(Items);
                for(int i=0;i<arrayTemp.size();i++){
                    if(arrayTemp.get(i).isMain()){
                        sliderItems.add(arrayTemp.get(i));
                    }else{
                        sliderItemsDown.add(arrayTemp.get(i));
                    }
                }

                NUM_PAGES = sliderItems.size();
                NUM_PAGESDown=sliderItemsDown.size();

                pageIndicatorView.setCount(NUM_PAGES);
                pageIndicatorViewDown.setCount(NUM_PAGESDown);

                ImageAdapter imageAdapter=new ImageAdapter(Home_Fragment.this.getContext(),sliderItems);
                ImageAdapter imageAdapterDown=new ImageAdapter(Home_Fragment.this.getContext(),sliderItemsDown);

                mPager.setAdapter(imageAdapter);
                pageIndicatorView.setViewPager(mPager);

                mPagerDown.setAdapter(imageAdapterDown);
                pageIndicatorViewDown.setViewPager(mPagerDown);
            }
            // </editor-fold>



        }
    }

    private void OnProductResult(ArrayList<ProductItem> arr,int type){
        try{
            /*************
            / Method Work Desc:  Save Products In DB
            *************/

            // First Delete All Products from previous cache
            db.Execute(database.QRY_DELETE_PRODUCT_INFO_BY_TYPE,new RecordHolder(new FieldItem("'@i1'",""+type)));

            // second add all Items one by one
            for(int i=0;i<arr.size();i++){
                ProductItem item=arr.get(i);
                db.Execute(database.QRY_INSERT_PRODUCT_INFO,new RecordHolder(new FieldItem("'@i1'","'"+item.getProduct_id()+"'"),new FieldItem("'@i2'","'"+item.getProduct_name()+"'"),new FieldItem("'@i3'","'"+item.getProduct_img()+"'"),new FieldItem("'@i4'",""+type+""),new FieldItem("'@i5'","'"+item.getPrice()+"'")));
            }

            // third save the current date of update. used for next update
            db.Execute(database.QRY_UPDATE_SETTING_INFO_FIELD,new RecordHolder(new FieldItem("@f1","product_timestamp"),new FieldItem("'@i1'","'"+DateTool.GetCurrentDateTime()+"'")));

        }catch (Exception e){}
    }

    public void OnRfresh(){
        //Nothing
    }
}
