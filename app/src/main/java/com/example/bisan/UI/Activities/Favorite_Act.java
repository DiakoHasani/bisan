package com.example.bisan.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bisan.DataTypes.FieldItem;
import com.example.bisan.DataTypes.RecordHolder;
import com.example.bisan.Tools.ApplicationClass.API_Listener;
import com.example.bisan.DataTypes.ProductItem;
import com.example.bisan.Network.API_BasketProduct;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.Tools.DBCursor;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.UI.Adapters.PrimaryProductAdapter;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.EmptyHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;
import com.example.bisan.Tools.database;

import org.json.JSONArray;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Favorite_Act extends AppCompatActivity implements API_Listener {

    private Toolbar mToolbar;
    private RelativeLayout Toolbar_Search_Box;
    private ImageButton Toolbar_Toggle, Toolbar_Search, Toolbar_Back, Toolbar_Menu;
    private RecyclerView list;
    private PrimaryProductAdapter adapter;
    private TextView Toolbar_Title;
    private String TAG_GET_INFO = "GETINFO";
    private EmptyHandler emptyHandler;
    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;
    private DBExcute db;
    private ArrayList<String> ItemsId;

    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.act_favorite);

        Initials_Objects();

        UI_Initial();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.Close();
    }

    private void Initials_Objects() {
        try {
            mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
            Toolbar_Title = (TextView) findViewById(R.id.toolbar_title);
            Toolbar_Toggle = (ImageButton) findViewById(R.id.toolbar_menu);
            Toolbar_Back = (ImageButton) findViewById(R.id.toolbar_back);
            Toolbar_Menu = (ImageButton) findViewById(R.id.toolbar_dotmenu);
            Toolbar_Search = (ImageButton) findViewById(R.id.toolbar_search);
            Toolbar_Search_Box=(RelativeLayout)findViewById(R.id.toolbar_searchbox);

            list=(RecyclerView)findViewById(R.id.favorite_act_productList);

            emptyHandler=new EmptyHandler(Favorite_Act.this);
            connectionFailHandler=new ConnectionFailHandler(Favorite_Act.this);
            connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetInfo();
                }
            });
            loadingHandler=new LoadingHandler(Favorite_Act.this);

            db=DBExcute.getInstance(Favorite_Act.this);
            db.Open();

            adapter=new PrimaryProductAdapter(Favorite_Act.this);
            adapter.setShowEmptyItems(true);
            adapter.setShowDeletebtn(true);

            adapter.setItemClick(new PrimaryProductAdapter.ItemClick() {
                @Override
                public void onItemClick(ProductItem item) {
                    try{
                        Intent intent=new Intent(Favorite_Act.this,Details_Act.class);
                        intent.putExtra("product_id",item.getProduct_id());
                        startActivity(intent);
                    }catch (Exception e){}
                }
            });

            adapter.setDeleteClick(new PrimaryProductAdapter.DeleteClick() {
                @Override
                public void onDeleteClick(ProductItem item) {
                    try{
                        db.Execute(database.QRY_DELETE_FAVORITE_INFO_BY_ID,new RecordHolder(new FieldItem("'@i1'","'"+item.getProduct_id()+"'")));
                        GetData();
                    }catch (Exception e){}
                }
            });
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            list.setLayoutManager(gridLayoutManager);
            list.setAdapter(adapter);

            GetData();
        } catch (Exception e) {
        }
    }

    private void UI_Initial() {
        try {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setElevation(1.5f);

            Toolbar_Search.setVisibility(View.GONE);
            Toolbar_Toggle.setVisibility(View.GONE);
            Toolbar_Back.setVisibility(View.VISIBLE);
            Toolbar_Menu.setVisibility(View.GONE);
            Toolbar_Title.setText("علاقه مندی ها");
            Toolbar_Search_Box.setVisibility(View.GONE);
            Toolbar_Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        } catch (Exception e) {
        }
    }

    public void GetData(){
        try{
            adapter.arrayList.clear();
            db.Read(database.QRY_GET_FAVORITE_INFO,null).RecordFound(new DBCursor.ListCounter() {
                @Override
                public void onEachrecord(ArrayList<FieldItem> arr) {
                    ProductItem item = new ProductItem();
                    item.setProduct_id(arr.get(1).value);
                    adapter.arrayList.add(item);
                }
            }, new DBCursor.NoRecordListener() {
                @Override
                public void NoRecord() {
                    adapter.arrayList.clear();
                    adapter.setShowEmptyItems(false);
                    adapter.notifyDataSetChanged();
                    if (emptyHandler != null) emptyHandler.Show();
                }
            }, new DBCursor.FetchFinishListener() {
                @Override
                public void onFinish() {
                    if(adapter.arrayList.size()>0)GetInfo();
                }
            }, 2);
        }catch (Exception e){}
    }

    public void GetInfo(){
        try{
            if(adapter.arrayList.size()<=0)return;

            if (loadingHandler != null) loadingHandler.ShowLoading();
            if (connectionFailHandler != null)
                if (connectionFailHandler.isVisible()) connectionFailHandler.Hide();
            if (emptyHandler != null)
                if (emptyHandler.isVisible()) emptyHandler.Hide();

            API_BasketProduct basketProduct=new API_BasketProduct(Favorite_Act.this);
            JSONArray array=new JSONArray();
            for(int i=0;i<adapter.arrayList.size();i++){
                array.put(Integer.valueOf(adapter.arrayList.get(i).getProduct_id()));
            }
            basketProduct.sendRequest(Favorite_Act.this,TAG_GET_INFO,array);
        }catch (Exception e){}
    }


    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        if(loadingHandler!=null)loadingHandler.HideLoading();
        if(hasError){
            //If No Internet Connection Found
            if(Answer.equals(ServerRequest.NO_CONNECTION))
                if(connectionFailHandler!=null)
                    if(!connectionFailHandler.isVisible())connectionFailHandler.Show();
        }else{
            if (Tag.compareToIgnoreCase(TAG_GET_INFO) == 0) {
                ArrayList<ProductItem> temp=new ArrayList<>();
                temp.addAll(adapter.arrayList);

                ReplaceProductItems(adapter.arrayList,Items);

                for(int i=0;i<temp.size();i++){
                    for(int j=0;j<adapter.arrayList.size();j++) {
                        if (adapter.arrayList.get(j).getProduct_id().compareToIgnoreCase(temp.get(i).getProduct_id()) == 0) {
                            ProductItem item=adapter.arrayList.get(j);
                            item.setCount(temp.get(i).getCount());
                            adapter.arrayList.set(j, item);
                        }
                    }
                }

                adapter.setShowEmptyItems(false);
                adapter.notifyDataSetChanged();
            }
        }
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
}
