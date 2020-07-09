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
import com.example.bisan.DataTypes.TransactionItem;
import com.example.bisan.R;

import java.util.ArrayList;


/**
 * Created by Ashkan_pc on 30/04/2017.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private ArrayList<TransactionItem> arrayList;
    private Context mContext;
    private ItemClick itemClick;


    public TransactionAdapter(Context context, ArrayList<TransactionItem> arr) {
        this.arrayList = arr;
        this.mContext = context;
    }

    public void setItemClick(ItemClick itemClick){
        this.itemClick=itemClick;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView type,date,price;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            type = (TextView) v.findViewById(R.id.layout_adapter_order_name);
            date = (TextView) v.findViewById(R.id.layout_adapter_order_date);
            price=(TextView) v.findViewById(R.id.layout_adapter_order_price);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_product, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.type.setText(arrayList.get(position).getType());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick!=null)itemClick.onItemClick(arrayList.get(position));
            }
        });
    }

    public interface OnMenuItemsClicked{
        public void onAcceptClick(OrderItem item);
        public void onViewClick(OrderItem item);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface ItemClick{
        public void onItemClick(TransactionItem item);
    }
}