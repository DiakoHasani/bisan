package com.example.bisan.UI.UIHandlers;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.bisan.R;

public class LoadingHandler  {

    private boolean isShown;
    private CardView cardView;

    public LoadingHandler(Activity activity){
        try{
            isShown=false;
            cardView=(CardView)activity.findViewById(R.id.loading_cardview);
        }catch (Exception e){}
    }

    public LoadingHandler(View view){
        try{
            isShown=false;
            cardView=(CardView)view.findViewById(R.id.loading_cardview);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public boolean isVisible(){
        return this.isShown;
    }

    public void ShowLoading(){
        try{
            cardView.setVisibility(View.VISIBLE);
            this.isShown=true;
        }catch (Exception e){}
    }

    public void HideLoading(){
        try{
            cardView.setVisibility(View.GONE);
            this.isShown=false;
        }catch (Exception e){}
    }

}
