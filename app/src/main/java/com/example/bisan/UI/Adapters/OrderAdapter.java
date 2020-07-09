package com.example.bisan.UI.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.bisan.DataTypes.OrderItem;
import com.example.bisan.Tools.FarsiUtil;
import com.example.bisan.R;

import java.util.ArrayList;


/**
 * Created by Ashkan_pc on 30/04/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private ArrayList<OrderItem> arrayList;
    private Context mContext;
    private ItemClick itemClick;
    private OnMenuItemsClicked itemsClicked;

    public void setItemsClicked(OnMenuItemsClicked itemsClicked) {
        this.itemsClicked = itemsClicked;
    }

    public OrderAdapter(Context context, ArrayList<OrderItem> arr) {
        this.arrayList = arr;
        this.mContext = context;
    }

    public void setItemClick(ItemClick itemClick){
        this.itemClick=itemClick;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,address,date,price,status;
        public ImageButton btnMenu;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            name = (TextView) v.findViewById(R.id.layout_adapter_order_name);
            address = (TextView) v.findViewById(R.id.layout_adapter_order_address);
            date = (TextView) v.findViewById(R.id.layout_adapter_order_date);
            price=(TextView) v.findViewById(R.id.layout_adapter_order_price);
            status=(TextView) v.findViewById(R.id.layout_adapter_order_status);
            btnMenu=(ImageButton)v.findViewById(R.id.layout_adapter_order_btnMenu);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_order, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(arrayList.get(position).getName().compareToIgnoreCase("null")==0){ holder.name.setText("___");}else{ holder.name.setText(arrayList.get(position).getName());}
        holder.price.setText(FarsiUtil.ConvertDoubleToToman(arrayList.get(position).getPrice())+" تومان ");
        if(arrayList.get(position).getAddress().compareToIgnoreCase("null")==0){holder.address.setText("___");}else{holder.address.setText(arrayList.get(position).getAddress());}
        if(arrayList.get(position).getDate().compareToIgnoreCase("null")==0){holder.date.setText("___");}else{holder.date.setText(arrayList.get(position).getDate());}
        if(arrayList.get(position).getStatus().compareToIgnoreCase("false")==0){
            holder.status.setText("تایید نشده");
        }else{
            holder.status.setText("تایید شد");
        }
        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    PopupMenu popupMenu=new PopupMenu(mContext,holder.btnMenu);
                    popupMenu.inflate(R.menu.menu_order_item);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            try{
                                switch (item.getItemId()){
                                    case R.id.menu_order_item_view:
                                        if(itemsClicked!=null){itemsClicked.onViewClick(arrayList.get(position));}
                                        break;
                                }
                            }catch (Exception e){}
                            return false;
                        }
                    });
                    popupMenu.show();
                }catch (Exception e){}
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick!=null)itemClick.onItemClick(arrayList.get(position));
            }
        });
    }

    public interface OnMenuItemsClicked{
        public void onViewClick(OrderItem item);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface ItemClick{
        public void onItemClick(OrderItem item);
    }
}