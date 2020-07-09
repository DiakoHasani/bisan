package com.example.bisan.UI.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.bisan.DataTypes.AddressItem;
import com.example.bisan.DataTypes.OrderItem;
import com.example.bisan.DataTypes.ProductItem;
import com.example.bisan.R;
import com.example.bisan.Tools.FarsiUtil;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;


/**
 * Created by Ashkan_pc on 30/04/2017.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    public  ArrayList<AddressItem> arrayList;
    private ArrayList<AddressItem> emptyarr;
    private Context mContext;
    private ItemClick itemClick;
    private OnMenuItemsClicked itemsClicked;
    private boolean showEmptyItems=true;

    public void setItemsClicked(OnMenuItemsClicked itemsClicked) {
        this.itemsClicked = itemsClicked;
    }

    public AddressAdapter(Context context) {
        this.arrayList = new ArrayList<>();
        this.emptyarr=new ArrayList<>();
        this.mContext = context;
    }

    public void setShowEmptyItems(boolean showEmptyItems) {
        this.showEmptyItems = showEmptyItems;
        this.notifyDataSetChanged();
    }

    public void setItemClick(ItemClick itemClick){
        this.itemClick=itemClick;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,address,phone;
        public ImageButton btnAccept;
        public ShimmerFrameLayout shimmer;
        public LinearLayout primaryHolder;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            name = (TextView) v.findViewById(R.id.layout_adapter_address_name);
            address = (TextView) v.findViewById(R.id.layout_adapter_address_address);
            phone = (TextView) v.findViewById(R.id.layout_adapter_address_tell);
            btnAccept=(ImageButton) v.findViewById(R.id.layout_adapter_address_btnaccept);
            shimmer=(ShimmerFrameLayout) v.findViewById(R.id.address_item_shimmer);
            primaryHolder=(LinearLayout) v.findViewById(R.id.adapter_address_primary);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_address, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(!this.showEmptyItems) {
            holder.shimmer.setVisibility(View.INVISIBLE);
            holder.primaryHolder.setVisibility(View.VISIBLE);
            holder.shimmer.startShimmerAnimation();

            holder.name.setText(arrayList.get(position).name);
            holder.address.setText(arrayList.get(position).address);
            holder.phone.setText(arrayList.get(position).phone);

            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClick != null) itemClick.onItemClick(arrayList.get(position));
                }
            });
        }else{
            holder.shimmer.setVisibility(View.VISIBLE);
            holder.primaryHolder.setVisibility(View.GONE);
            holder.shimmer.startShimmerAnimation();
        }
    }

    public interface OnMenuItemsClicked{
        public void onViewClick(OrderItem item);
    }

    @Override
    public int getItemCount() {
        if(this.showEmptyItems){
            return emptyarr.size();
        }else {
            return arrayList.size();
        }
    }

    public  void setArray(ArrayList<AddressItem> arr){
        this.arrayList=arr;
    }

    public interface ItemClick{
        public void onItemClick(AddressItem item);
    }
}