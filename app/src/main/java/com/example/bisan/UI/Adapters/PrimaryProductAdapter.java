package com.example.bisan.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bisan.DataTypes.FieldItem;
import com.example.bisan.DataTypes.ProductItem;
import com.example.bisan.DataTypes.RecordHolder;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.Tools.DBCursor;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.Tools.FarsiUtil;
import com.example.bisan.R;
import com.example.bisan.UI.Activities.Details_Act;
import com.example.bisan.Tools.CToast;
import com.example.bisan.Tools.database;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;


/**
 * Created by Ashkan_pc on 30/04/2017.
 */

public class PrimaryProductAdapter extends RecyclerView.Adapter<PrimaryProductAdapter.ViewHolder> {
    public  ArrayList<ProductItem> arrayList;
    private ArrayList<ProductItem> emptyarr;
    private Context mContext;
    private ItemClick itemClick;
    private DeleteClick deleteClick;
    private boolean showDeletebtn = false;
    private ArrayList<String> FavoriteList;
    private DBExcute db;
    private boolean showEmptyItems = true;
    private  int Layout_id=R.layout.layout_adapter_product_new;

    public PrimaryProductAdapter(Context context) {
        this.arrayList = new ArrayList<>();
        this.emptyarr=new ArrayList<>();
        FillEmptyITems();
        this.mContext = context;
        this.db = DBExcute.getInstance(mContext);
        this.db.Open();
    }

    public void setShowDeletebtn(boolean showDeletebtn) {
        this.showDeletebtn = showDeletebtn;
    }

    public PrimaryProductAdapter(Context context, boolean showDeleteButton,boolean isHome) {
        this.arrayList = new ArrayList<>();
        this.emptyarr=new ArrayList<>();
        FillEmptyITems();
        this.mContext = context;
        this.showDeletebtn = showDeleteButton;
        this.db = DBExcute.getInstance(mContext);
        this.db.Open();
        if(isHome)this.Layout_id=R.layout.layout_adapter_product;
    }

    private void FillEmptyITems(){
        for(int i=0;i<4;i++){
            ProductItem item=new ProductItem();
            this.emptyarr.add(item);
        }
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public void setDeleteClick(DeleteClick deleteClick) {
        this.deleteClick = deleteClick;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amount, title;
        public ImageView image;
        public ImageButton btnLike, btnDelete;
        public RelativeLayout btnBasket;
        public ShimmerFrameLayout shimmer;
        public LinearLayout primaryHolder;
        public CardView cardView;


        public ViewHolder(View v) {
            super(v);
            amount = (TextView) v.findViewById(R.id.product_item_price);
            title = (TextView) v.findViewById(R.id.product_item_title);
            image = (ImageView) v.findViewById(R.id.product_item_image);
            btnLike = (ImageButton) v.findViewById(R.id.product_item_btnLike);
            btnDelete = (ImageButton) v.findViewById(R.id.product_item_btnDelete);
            btnBasket = (RelativeLayout) v.findViewById(R.id.btnbasket3);
            shimmer = (ShimmerFrameLayout) v.findViewById(R.id.product_item_shimmer);
            primaryHolder = (LinearLayout) v.findViewById(R.id.product_item_linear);
            cardView=(CardView)v.findViewById(R.id.product_item_cardview);
        }
    }

    public void setShowEmptyItems(boolean showEmptyItems) {
        this.showEmptyItems = showEmptyItems;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(this.Layout_id, parent, false);
        ReloadFavorites();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!this.showEmptyItems) {
            holder.amount.setBackgroundColor(Color.TRANSPARENT);
            holder.image.setBackgroundColor(Color.TRANSPARENT);
            holder.title.setText(arrayList.get(position).getProduct_name());
            holder.amount.setText(FarsiUtil.ConvertDoubleToToman(arrayList.get(position).getPrice()) + " تومان");


            if (this.showDeletebtn) {
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnLike.setVisibility(View.GONE);
            } else {
                holder.btnLike.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.GONE);
            }

            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(!db.Read(database.QRY_GET_FAVORITE_INFO_BY_ID,new RecordHolder(new FieldItem("'@i1'","'"+arrayList.get(position).getProduct_id()+"'"))).HasRecord()){
                            db.Execute(database.QRY_INSERT_FAVORITE_INFO, new RecordHolder(new FieldItem("'@i1'", "'" + arrayList.get(position).getProduct_id() + "'")));
                        }else{
                            db.Execute(database.QRY_DELETE_FAVORITE_INFO_BY_ID, new RecordHolder(new FieldItem("'@i1'", "'" + arrayList.get(position).getProduct_id() + "'")));
                        }
                    } catch (Exception e) {
                        Log.i("e",e.toString());
                        e.printStackTrace();
                    }

                    ReloadFavorites();
                    PrimaryProductAdapter.super.notifyDataSetChanged();
                }
            });

            try {
                if (FavoriteList.indexOf(arrayList.get(position).getProduct_id()) > -1) {
                    holder.btnLike.setImageResource(R.drawable.icon_red_heart);
                    holder.btnLike.setAlpha(1.0F);
                } else {
                    holder.btnLike.setImageResource(R.drawable.icon_gray_heart);
                    holder.btnLike.setAlpha(0.2F);
                }
            } catch (Exception e) {
            }

            holder.btnBasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        db.Open();
                        if(db.Read(database.QRY_GET_BASKET_INFO_BY_ID,new RecordHolder(new FieldItem("'@i1'","'"+arrayList.get(position).getProduct_id()+"'"))).HasRecord()){

                            Toast.makeText(mContext, R.string.label72, CToast.SHORT_DURATION).show();
                        }else {
                            db.Execute(database.QRY_INSERT_BASKET_INFO, new RecordHolder(new FieldItem("'@i1'", "'" + arrayList.get(position).getProduct_id() + "'")));

                            Toast.makeText(mContext, R.string.label51, CToast.SHORT_DURATION).show();
                        }
                    } catch (Exception e) {
                        Log.i("e",e.toString());
                    }
                }
            });

            ApplicationClass.LoadImage(this.mContext,arrayList.get(position).getProduct_img(),holder.image);
            /*Glide.with(this.mContext)
                    .load(arrayList.get(position).getProduct_img())
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image);*/

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(mContext, Details_Act.class);
                        intent.putExtra("product_id", arrayList.get(position).getProduct_id());
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                    }

                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteClick != null) deleteClick.onDeleteClick(arrayList.get(position));
                }
            });
            holder.shimmer.stopShimmerAnimation();
            holder.shimmer.setVisibility(View.GONE);
            holder.primaryHolder.setVisibility(View.VISIBLE);

            holder.primaryHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("ashkan", "onClick: linear");

                    Intent intent = new Intent(mContext, Details_Act.class);
                    intent.putExtra("product_id", arrayList.get(position).getProduct_id());
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder.shimmer.setVisibility(View.VISIBLE);
            holder.primaryHolder.setVisibility(View.GONE);
            holder.shimmer.startShimmerAnimation();
        }
    }

    @Override
    public int getItemCount() {
        if(this.showEmptyItems){
            return emptyarr.size();
        }else {
            return arrayList.size();
        }
    }

    public interface ItemClick {
        public void onItemClick(ProductItem item);
    }

    public interface DeleteClick {
        public void onDeleteClick(ProductItem item);
    }

    public void ReloadFavorites() {
        try {
            FavoriteList=new ArrayList<>();
            db.Open();
            db.Read(database.QRY_GET_FAVORITE_INFO,null).RecordFound(new DBCursor.ListCounter() {
                @Override
                public void onEachrecord(ArrayList<FieldItem> record) {
                    FavoriteList.add(record.get(1).value);
                }
            }, new DBCursor.NoRecordListener() {
                @Override
                public void NoRecord() {
                    PrimaryProductAdapter.this.notifyDataSetChanged();
                }
            }, new DBCursor.FetchFinishListener() {
                @Override
                public void onFinish() {
                    PrimaryProductAdapter.this.notifyDataSetChanged();
                }
            }, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void setArray(ArrayList<ProductItem> arr){
        this.arrayList=arr;
    }
}