package com.example.bisan.UI.UIHandlers;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.bisan.R;

public class ConnectionFailHandler {

    private boolean isShown;
    private RelativeLayout cardView;
    private Button btnRetry;

    public ConnectionFailHandler(Activity activity){
        try{
            isShown=false;
            cardView=(RelativeLayout) activity.findViewById(R.id.connectiofail_holder);
            btnRetry=(Button)activity.findViewById(R.id.connectionfail_btnrefresh);
        }catch (Exception e){}
    }

    public ConnectionFailHandler(View view){
        try{
            isShown=false;
            cardView=(RelativeLayout) view.findViewById(R.id.connectiofail_holder);
            btnRetry=(Button)view.findViewById(R.id.connectionfail_btnrefresh);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOnRetryClicked(View.OnClickListener listener){
        try{
            btnRetry.setOnClickListener(listener);
        }catch (Exception e){}
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
