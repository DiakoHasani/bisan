package com.example.bisan.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.OrderItem;
import com.example.bisan.Network.API_Orders;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.UI.Adapters.OrderAdapter;
import com.example.bisan.UI.ModalActivities.Modal_details;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.EmptyHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;
import com.example.bisan.Tools.database;

import java.util.ArrayList;

public class Order_Fragment extends Fragment implements ApplicationClass.API_Listener {

    private RecyclerView order_recycler;
    private ArrayList<OrderItem> arrayList;
    private OrderAdapter adapter;

    private String TAG_GETORDER="Orders";
    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;
    private EmptyHandler emptyHandler;
    private DBExcute db;

    public Order_Fragment() {
        // Required empty public constructor
    }

    public static Order_Fragment newInstance() {
        Order_Fragment fragment = new Order_Fragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                GetOrders();
            }
        };
        Handler handler=new Handler();
        handler.postDelayed(runnable,1000);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_order, container, false);

        order_recycler=(RecyclerView)view.findViewById(R.id.order_fragment_list);

        arrayList=new ArrayList<>();

        adapter=new OrderAdapter(Order_Fragment.this.getContext(),arrayList);
        loadingHandler=new LoadingHandler(view);
        emptyHandler=new EmptyHandler(view);
        connectionFailHandler=new ConnectionFailHandler(view);
        connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetOrders();
            }
        });

        adapter.setItemsClicked(new OrderAdapter.OnMenuItemsClicked() {
            @Override
            public void onViewClick(OrderItem item) {
                //On View Details Clicked
                Intent intent=new Intent(Order_Fragment.this.getContext(), Modal_details.class);
                intent.putExtra("order_id",item.getOrder_id());
                startActivity(intent);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        order_recycler.setLayoutManager(linearLayoutManager);

        db=DBExcute.getInstance(Order_Fragment.this.getContext());
        order_recycler.setAdapter(adapter);

       ;

        return view;
    }

    private void GetOrders(){
        try{
            if(loadingHandler!=null)loadingHandler.ShowLoading();
            if(connectionFailHandler!=null)
                if(connectionFailHandler.isVisible())connectionFailHandler.Hide();
            if(emptyHandler!=null)
                if(emptyHandler.isVisible())emptyHandler.Hide();

            arrayList.clear();
            if(db.Read(database.QRY_GET_USER_INFO,null).HasRecord()) {
                API_Orders orders = new API_Orders(Order_Fragment.this.getContext());
                orders.in_id.setVal((db.Read(database.QRY_GET_USER_INFO, null).GetField(6)));
                orders.sendRequest(Order_Fragment.this, TAG_GETORDER, orders.in_id);
            }else{
                emptyHandler.Show();
                loadingHandler.HideLoading();
            }
        }catch (Exception e){}
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
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        if(loadingHandler!=null)loadingHandler.HideLoading();
        if(hasError){

            //If No Internet Connection Found
            if(Answer.equals(ServerRequest.NO_CONNECTION))
                if(connectionFailHandler!=null)
                    if(!connectionFailHandler.isVisible())connectionFailHandler.Show();
        }else {


            if(Tag.compareToIgnoreCase(TAG_GETORDER)==0){

                arrayList.addAll(Items);
                adapter.notifyDataSetChanged();

                if(arrayList==null){
                    emptyHandler.Show();
                }else if(arrayList.size()<=0)emptyHandler.Show();

            }

        }
    }
    public void OnRfresh(){

    }
}
