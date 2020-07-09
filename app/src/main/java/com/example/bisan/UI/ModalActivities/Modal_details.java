package com.example.bisan.UI.ModalActivities;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.OrderDetail;
import com.example.bisan.Network.API_OrderInfo;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.UI.Adapters.OrderDetailAdapter;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Modal_details extends AppCompatActivity implements ApplicationClass.API_Listener {

    private String order_id,TAG_GET_INFO="GetOrderInfo";
    private RecyclerView list;
    private Button btnClose;
    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;
    private ArrayList<OrderDetail> arrayList;
    private OrderDetailAdapter adapter;

    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_modal_details);

        Initials_Objects();

        UI_Initial();
        GetData();
    }

    private void Initials_Objects() {
        try {

            try{
                Bundle bundle=getIntent().getExtras();
                if(bundle==null)finish();
                order_id=bundle.getString("order_id");
            }catch (Exception e){}

            loadingHandler = new LoadingHandler(Modal_details.this);
            connectionFailHandler = new ConnectionFailHandler(Modal_details.this);
            connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetData();
                }
            });

            list = (RecyclerView) findViewById(R.id.modal_details_list);
            btnClose=(Button) findViewById(R.id.modal_details_btnMap);

        } catch (Exception e) {
        }
    }

    private void UI_Initial() {
        try {
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            arrayList=new ArrayList<>();

            adapter=new OrderDetailAdapter(Modal_details.this,arrayList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Modal_details.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            list.setLayoutManager(linearLayoutManager);

            list.setAdapter(adapter);

        } catch (Exception e) {
        }
    }

    private void GetData(){
        try{
            if(loadingHandler!=null)loadingHandler.ShowLoading();
            if(connectionFailHandler!=null)
                if(connectionFailHandler.isVisible())connectionFailHandler.Hide();



            API_OrderInfo orderInfo=new API_OrderInfo(Modal_details.this);
            orderInfo.in_id.setVal((order_id));
            orderInfo.sendRequest(Modal_details.this,TAG_GET_INFO,orderInfo.in_id);
        }catch (Exception e){}
    }

    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        if(loadingHandler!=null)loadingHandler.HideLoading();
        if(hasError){
            //If No Internet Connection Found
            if (Answer.equals(ServerRequest.NO_CONNECTION))
                if (connectionFailHandler != null)
                    if (!connectionFailHandler.isVisible()) connectionFailHandler.Show();
        }else{

            if(Tag.compareToIgnoreCase(TAG_GET_INFO)==0){
                if(Items==null)finish();
                arrayList.addAll(Items);
                adapter.notifyDataSetChanged();
            }

        }
    }
}
