package com.example.bisan.UI.UIHandlers;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.bisan.R;

public class EmptyHandler {

    private boolean isShown;
    private RelativeLayout cardView;

    public EmptyHandler(Activity activity){
        try{
            isShown=false;
            cardView=(RelativeLayout) activity.findViewById(R.id.empty);
        }catch (Exception e){}
    }

    public EmptyHandler(View view){
        try{
            isShown=false;
            cardView=(RelativeLayout) view.findViewById(R.id.empty);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public boolean isVisible(){
        return this.isShown;
    }

    public void Show(){
        try{
            cardView.setVisibility(View.VISIBLE);
            this.isShown=true;
        }catch (Exception e){}
    }

    public void Hide(){
        try{
            cardView.setVisibility(View.GONE);
            this.isShown=false;
        }catch (Exception e){}
    }



}
