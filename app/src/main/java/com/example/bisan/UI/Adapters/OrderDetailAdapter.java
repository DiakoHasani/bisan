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

import com.example.bisan.DataTypes.OrderDetail;
import com.example.bisan.DataTypes.OrderItem;
import com.example.bisan.R;

import java.util.ArrayList;


/**
 * Created by Ashkan_pc on 30/04/2017.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private ArrayList<OrderDetail> arrayList;
    private Context mContext;
    private ItemClick itemClick;
    private OnMenuItemsClicked itemsClicked;

    public void setItemsClicked(OnMenuItemsClicked itemsClicked) {
        this.itemsClicked = itemsClicked;
    }

    public OrderDetailAdapter(Context context, ArrayList<OrderDetail> arr) {
        this.arrayList = arr;
        this.mContext = context;
    }

    public void setItemClick(ItemClick itemClick){
        this.itemClick=itemClick;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,weight;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.layout_adapter_order_detail_name);
            weight = (TextView) v.findViewById(R.id.layout_adapter_order_detail_weight);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_order_detail, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(arrayList.get(position).getProduct_name());
        holder.weight.setText(arrayList.get(position).getCount());
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