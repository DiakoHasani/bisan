package com.example.bisan.UI.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bisan.DataTypes.ProductItem;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.Tools.FarsiUtil;
import com.example.bisan.R;

import java.util.ArrayList;


/**
 * Created by Ashkan_pc on 30/04/2017.
 */

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {
    public ArrayList<ProductItem> arrayList;
    private Context mContext;
    private ItemClick itemClick;
    private Basketinterface onDeleteClicked;

    public BasketAdapter(Context context, ArrayList<ProductItem> arr) {
        this.arrayList = arr;
        this.mContext = context;
    }

    public void setItemClick(ItemClick itemClick){
        this.itemClick=itemClick;
    }

    public void setOnDeleteClicked(Basketinterface onDeleteClicked) {
        this.onDeleteClicked = onDeleteClicked;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amount,title,number;
        public ImageButton btnAdd,btnMinus,btnDelete;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            amount = (TextView) v.findViewById(R.id.product_item_price);
            title = (TextView) v.findViewById(R.id.product_item_title);
            number = (TextView) v.findViewById(R.id.layout_adapter_basket_number);
            btnAdd=(ImageButton)v.findViewById(R.id.layout_adapter_basket_btnadd);
            btnMinus=(ImageButton)v.findViewById(R.id.layout_adapter_basket_btnminus);
            btnDelete=(ImageButton)v.findViewById(R.id.layout_adapter_basket_btnDelete);
            imageView=(ImageView)v.findViewById(R.id.product_item_image);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_basket, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText(arrayList.get(position).getProduct_name());
        holder.amount.setText(FarsiUtil.ConvertDoubleToToman(arrayList.get(position).getPrice())+" تومان ");
        holder.number.setText(String.valueOf(arrayList.get(position).getCount()));

        ApplicationClass.LoadImage(this.mContext,arrayList.get(position).getProduct_img(),holder.imageView);
                /*Glide.with(mContext)
                        .load(arrayList.get(position).getProduct_img())
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView);*/

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDeleteClicked!=null)onDeleteClicked.onMinusClick(arrayList.get(position));
            }
        });

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDeleteClicked!=null)onDeleteClicked.onPlusClick(arrayList.get(position));
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDeleteClicked!=null)onDeleteClicked.onDeleteItem(arrayList.get(position));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick!=null)itemClick.onItemClick(arrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface ItemClick{
        public void onItemClick(ProductItem item);
    }

    public void SetArray(ArrayList<ProductItem> arr){
        this.arrayList=arr;
    }

    public interface Basketinterface{
        public void onDeleteItem(ProductItem item);
        public void onMinusClick(ProductItem item);
        public void onPlusClick(ProductItem item);
    }
}