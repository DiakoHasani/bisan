package com.example.bisan.UI.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bisan.DataTypes.SliderItem;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.Tools.ApplicationClass;

import java.util.ArrayList;

//TODO Image Adapter for ViewPager
public class ImageAdapter extends PagerAdapter {

    Context context;
    private ArrayList<SliderItem> Galimage;
    public boolean isDetailsActivity=false; //Used this value to know adapter called from details activity or not. in details activity we send full path image

    public ImageAdapter(Context context,ArrayList<SliderItem> arrayList){
        this.context=context;
        this.Galimage=arrayList;
    }


    @Override
    public int getCount() {
        return Galimage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((ImageView)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        try {
            ImageView myView=new ImageView(this.context);

            ViewGroup.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            myView.setLayoutParams(layoutParams);
            myView.setScaleType(ImageView.ScaleType.FIT_XY);

            if(this.isDetailsActivity){
                ApplicationClass.LoadImage(this.context, Galimage.get(position).getImage(), myView);
            }else {
                ApplicationClass.LoadImage(this.context, ServerRequest.BaseURLImage + Galimage.get(position).getImage(), myView);
            }
            /*Glide.with(this.context)
                    .load(ServerRequest.BaseURLImage + Galimage.get(position).getImage())
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .centerCrop()
//                .placeholder(R.drawable.)
                    .into(myView);*/
            container.addView(myView);
            return myView;
        }catch (Exception e){
            //Log.d("e","err"+e.toString());
            return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            //((ViewPager) container).removeViewAt(position);
            container.removeView((ImageView) object);
        }catch (Exception e){
            //Log.d("del","err"+e.toString());
        }
    }



}
