package com.example.bisan.Tools;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bisan.R;

public class CToast extends Toast {

    /*
    *   Types :
    *           0- Success
    *           1- Error
    *           2- Inforamtion
     */

    public static int SUCCESS=0,
                        ERROR=1,
                        INFORMATION=2;
    public static int LONG_DURATION=Toast.LENGTH_LONG,
                        SHORT_DURATION=Toast.LENGTH_SHORT;

    private String title;
    private int type,duration;
    private Context context;

    public void setType(int type) {
        this.type = type;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CToast(Context context) {
        super(context);
        type=SUCCESS;
        title="";
        duration=LONG_DURATION;
        this.context=context;
    }

    public CToast(Context context,String title,int type,int duration){
        super(context);
        this.context=context;
        this.title=title;
        this.duration=duration;
        this.type=type;
    }

    public void Show(){
        try{
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.layout_ui_ctoast,null);

            Toast toast = new Toast(this.context);
            toast.setDuration(this.duration);

            TextView txt=(TextView)layout.findViewById(R.id.ctoast_title);
            txt.setText(this.title);

            ImageView image=(ImageView)layout.findViewById(R.id.ctoast_img);

            toast.setView(layout);//setting the view of custom toast layout
            toast.show();
            AnimatorSet animator = new AnimatorSet();
            ObjectAnimator a1 = ObjectAnimator.ofFloat(layout, "scaleX", 1.0F, 0.8F, 1.0F, 1.1F, 1.3F, 1.0F);
            ObjectAnimator b1 = ObjectAnimator.ofFloat(layout, "scaleY", 1.0F, 0.8F, 1.0F, 1.1F, 1.3F, 1.0F);
            a1.setDuration(500);
            b1.setDuration(500);
            animator.playTogether(a1, b1);
            animator.start();
        }catch (Exception e){}
    }
}
