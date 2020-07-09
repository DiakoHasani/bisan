package com.example.bisan.UI.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bisan.R;


/**
 * Created by shahab-pc on 11/7/2018.
 */

public class CustomDialog extends AlertDialog {

    /*************************************
     *
     * CLASS :: Display Specialized alert Dialog. Display this Delete Alert Where need to delete Something.Used to have User Confirmition
     *
     ************************************/

    public CustomDialog(@NonNull Context context, String text, @NonNull String title) {
        super(context);

        /********************************************************
         * Text is Our Message in body of Alert Dialog Window.
         *
         * Title is Our Window Title at Top of the window.
         *
         * btnDelTitle is a label for our Delete Button.
         ***************************************************/
        this.deleteText=text;
        this.deleteTitle=title;
        this.btnCancel="خیر";
    }

    /************************** Constractor Method ******************/
    public CustomDialog(@NonNull Context context, String text, @NonNull String title, @NonNull String btnDelTitle) {
        super(context);

        /********************************************************
        * Text is Our Message in body of Alert Dialog Window.
         *
        * Title is Our Window Title at Top of the window.
         *
        * btnDelTitle is a label for our Delete Button.
         ***************************************************/
        this.deleteText=text;
        this.btnDelTitle=btnDelTitle;
        this.deleteTitle=title;
        this.btnCancel="انصراف";
    }

    /************************** Constractor Method ******************/
    public CustomDialog(@NonNull Context context, String text, @NonNull String title, @NonNull String btnDelTitle, @NonNull String btnCancel) {
        super(context);

        /********************************************************
         * Text is Our Message in body of Alert Dialog Window.
         *
         * Title is Our Window Title at Top of the window.
         *
         * btnDelTitle is a label for our Delete Button.
         ***************************************************/
        this.deleteText=text;
        this.btnDelTitle=btnDelTitle;
        this.deleteTitle=title;
        this.btnCancel=btnCancel;
    }

    /************************** Constractor Method ******************/
    public CustomDialog(@NonNull Context context, int text, @NonNull int title, @NonNull int btnDelTitle, @NonNull int btnCancel) {
        super(context);

        /********************************************************
         * Text is Our Message in body of Alert Dialog Window.
         *
         * Title is Our Window Title at Top of the window.
         *
         * btnDelTitle is a label for our Delete Button.
         ***************************************************/
        this.deleteText=context.getResources().getString(text);
        this.btnDelTitle=context.getResources().getString(btnDelTitle);
        this.deleteTitle=context.getResources().getString(title);
        this.btnCancel=context.getResources().getString(btnCancel);
    }

    protected Button btnYes,btnNo;
    private Onclick onclick;
    private String deleteText,deleteTitle,btnDelTitle,btnCancel;

    public interface Onclick{
        public void onAcceptClick();
        public void onCancelClick();
    }

    public void setOnclick(Onclick onclick) {
        this.onclick = onclick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);

        final TextView txt=(TextView)findViewById(R.id.dialog_custom_descripption);
        if(deleteText!=null){txt.setText(deleteText);}

        final TextView title=(TextView)findViewById(R.id.dialog_custom_title);
        if(deleteTitle!=null){title.setText(deleteTitle);}

        btnYes=(Button)findViewById(R.id.dialog_custom_btnaccept);
        btnNo=(Button)findViewById(R.id.dialog_custom_btncancel);

        if(btnDelTitle!=null){
            btnYes.setText(btnDelTitle);
        }

        try{btnNo.setText(btnCancel);}catch (Exception e){}

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(onclick!=null){onclick.onAcceptClick();}
                }catch (Exception e){}
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(onclick!=null){onclick.onCancelClick();}
                }catch (Exception e){}
            }
        });

    }

}
