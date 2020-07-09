package com.example.bisan.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bisan.DataTypes.AddressItem;
import com.example.bisan.DataTypes.FieldItem;
import com.example.bisan.DataTypes.ProductItem;
import com.example.bisan.DataTypes.RecordHolder;
import com.example.bisan.Network.API_BasketProduct;
import com.example.bisan.Network.API_GetAddress;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.Tools.database;
import com.example.bisan.UI.Adapters.AddressAdapter;
import com.example.bisan.UI.Adapters.PrimaryProductAdapter;
import com.example.bisan.UI.ModalActivities.Modal_basket;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.EmptyHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Address_Act extends AppCompatActivity implements ApplicationClass.API_Listener {

    private Toolbar mToolbar;
    private RelativeLayout Toolbar_Search_Box;
    private ImageButton Toolbar_Toggle, Toolbar_Search, Toolbar_Back, Toolbar_Menu;
    private RecyclerView list;
    private AddressAdapter adapter;
    private TextView Toolbar_Title;
    private String TAG_GET_INFO = "GETINFO";
    private EmptyHandler emptyHandler;
    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;
    private DBExcute db;

    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.act_address);

        Initials_Objects();

        UI_Initial();

    }

    public void Initials_Objects(){
        mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        Toolbar_Title = (TextView) findViewById(R.id.toolbar_title);
        Toolbar_Toggle = (ImageButton) findViewById(R.id.toolbar_menu);
        Toolbar_Back = (ImageButton) findViewById(R.id.toolbar_back);
        Toolbar_Menu = (ImageButton) findViewById(R.id.toolbar_dotmenu);
        Toolbar_Search = (ImageButton) findViewById(R.id.toolbar_search);
        Toolbar_Search_Box=(RelativeLayout)findViewById(R.id.toolbar_searchbox);

        list=(RecyclerView)findViewById(R.id.address_act_List);

        emptyHandler=new EmptyHandler(Address_Act.this);
        connectionFailHandler=new ConnectionFailHandler(Address_Act.this);
        connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetInfo();
            }
        });
        loadingHandler=new LoadingHandler(Address_Act.this);

        db=DBExcute.getInstance(Address_Act.this);
        db.Open();

        adapter=new AddressAdapter(Address_Act.this);
        adapter.setShowEmptyItems(true);

        adapter.setItemClick(new AddressAdapter.ItemClick() {
            @Override
            public void onItemClick(AddressItem item) {
                try{
                    Intent intent=new Intent();
                    intent.putExtra("id",item.id);
                    intent.putExtra("name",item.name);
                    intent.putExtra("address",item.address);
                    intent.putExtra("phone",item.phone);
                    intent.putExtra("lat",item.lat);
                    intent.putExtra("lang",item.lang);
                    setResult(11,intent);
                    finish();
                }catch (Exception e){}
            }
        });

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(gridLayoutManager);
        list.setAdapter(adapter);

        GetInfo();
    }

    public void UI_Initial(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setElevation(1.5f);

        Toolbar_Search.setVisibility(View.GONE);
        Toolbar_Toggle.setVisibility(View.GONE);
        Toolbar_Back.setVisibility(View.VISIBLE);
        Toolbar_Menu.setVisibility(View.GONE);
        Toolbar_Title.setText("آدرس های قبلی");
        Toolbar_Search_Box.setVisibility(View.GONE);
        Toolbar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.Close();
    }

    public void GetInfo(){
        try{
            if (loadingHandler != null) loadingHandler.ShowLoading();
            if (connectionFailHandler != null)
                if (connectionFailHandler.isVisible()) connectionFailHandler.Hide();
            if (emptyHandler != null)
                if (emptyHandler.isVisible()) emptyHandler.Hide();


            API_GetAddress getAddress=new API_GetAddress(Address_Act.this);
            getAddress.in_userID.setVal(db.Read(database.QRY_GET_USER_INFO,null).GetField(6));

            getAddress.sendRequest(Address_Act.this,TAG_GET_INFO,getAddress.in_userID);

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
                ArrayList<AddressItem> temp=new ArrayList<>();
                temp.addAll(Items);

                adapter.setShowEmptyItems(false);
                adapter.setArray(temp);
                adapter.notifyDataSetChanged();
            }
        }
    }


}
