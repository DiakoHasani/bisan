package com.example.bisan.UI.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bisan.DataTypes.CategoryItem;
import com.example.bisan.DataTypes.ProductItem;
import com.example.bisan.R;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.UI.Fragments.Category_Fragment;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Ashkan_pc on 30/04/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    public ArrayList<CategoryItem> arrayList;
    private ArrayList<CategoryItem> emptyarr;
    private Context mContext;
    private ItemClick itemClick;
    private boolean ShowSelectedItems = false;
    private boolean showEmptyItems = true;

    public CategoryAdapter(Context context, boolean showSelected) {
        this.arrayList = new ArrayList<>();
        this.emptyarr=new ArrayList<>();
        FillEmptyItem();
        this.mContext = context;
        this.ShowSelectedItems = showSelected;
    }

    private void FillEmptyItem(){
        for(int i=0;i<4;i++){
            CategoryItem item=new CategoryItem();
            this.emptyarr.add(item);
        }
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CircleImageView imageView;
        public ShimmerFrameLayout shimmer;
        public LinearLayout primaryHolder;

        public ViewHolder(View v) {
            super(v);
            imageView = (CircleImageView) v.findViewById(R.id.layout_adapter_category_img);
            title = (TextView) v.findViewById(R.id.layout_adapter_category_title);
            shimmer = (ShimmerFrameLayout) v.findViewById(R.id.product_item_shimmer);
            primaryHolder = (LinearLayout) v.findViewById(R.id.linear_holder);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_category, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!this.showEmptyItems) {
            holder.title.setText(arrayList.get(position).getCategory_name());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClick != null) itemClick.onItemClick(arrayList.get(position));
                }
            });

            ApplicationClass.LoadImage(this.mContext,arrayList.get(position).getCategory_img(),holder.imageView);
            /*Glide.with(this.mContext)
                    .load(arrayList.get(position).getCategory_img())
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);*/

            if (this.ShowSelectedItems) {
                if (!arrayList.get(position).isSelected()) {
                    holder.itemView.setAlpha(0.5f);
                } else {
                    holder.itemView.setAlpha(1.0f);
                }
            }

            holder.shimmer.stopShimmerAnimation();
            holder.shimmer.setVisibility(View.INVISIBLE);
            holder.primaryHolder.setVisibility(View.VISIBLE);
        } else {
            holder.shimmer.setVisibility(View.VISIBLE);
            holder.primaryHolder.setVisibility(View.GONE);
            holder.shimmer.startShimmerAnimation();
        }
    }

    public void setArrayList(ArrayList<CategoryItem> arr){
        this.arrayList=arr;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(this.showEmptyItems){return emptyarr.size();}else{return this.arrayList.size();}
    }

    public interface ItemClick {
        public void onItemClick(CategoryItem item);
    }

    public void setShowEmptyItems(boolean showEmptyItems) {
        this.showEmptyItems = showEmptyItems;
        this.notifyDataSetChanged();
    }
}