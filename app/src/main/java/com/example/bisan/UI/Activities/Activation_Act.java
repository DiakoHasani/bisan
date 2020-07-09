package com.example.bisan.UI.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bisan.DataTypes.FieldItem;
import com.example.bisan.DataTypes.MessageItem;
import com.example.bisan.DataTypes.RecordHolder;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.Network.API_UserActivation;
import com.example.bisan.Network.API_UserResendSms;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.Tools.CToast;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;
import com.example.bisan.Tools.database;

import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Activation_Act extends AppCompatActivity implements ApplicationClass.API_Listener {

    private EditText activationCode;
    private ImageButton Toolbar_Back;
    private TextView Toolbar_Title,timer_textview;
    private String TAG_SAVE="ActivationUser",TAG_RESEND="ResendSms",phoneNumber="";
    private Button btnSend,btnResend;
    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;
    private DBExcute db;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.act_activation);

        Initials_Objects();

        UI_Initial();
    }


    //--> Initials Objects for the first time with new Values
    private void Initials_Objects() {
        Toolbar_Back = (ImageButton) findViewById(R.id.toolbar_back);
        Toolbar_Title = (TextView) findViewById(R.id.toolbar_title);

        btnSend = (Button) findViewById(R.id.btn_activate_activation);
        btnResend = (Button) findViewById(R.id.btn_send_again_activation);
        activationCode = (EditText) findViewById(R.id.edt_phone_activation);

        timer_textview = findViewById(R.id.txt_activation_timer);

        sec = 0;
        min = 1;
        isTimerFinish = false;
        timer_textview.setText("01:00");
        myCountDownTimer = new MyCountDownTimer(time, 1000);
        myCountDownTimer.start();
        loadingHandler=new LoadingHandler(this);
        connectionFailHandler=new ConnectionFailHandler(this);
        connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    connectionFailHandler.Hide();
                }catch (Exception e){}
            }
        });

        db=DBExcute.getInstance(this);
        db.Open();

        try{
            Bundle bundle=getIntent().getExtras();
            phoneNumber=bundle.getString("phone");
        }catch (Exception e){}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.Close();
    }

    //--> Do All Activity UI Works like Create Objects and etc here!
    private void UI_Initial() {
        try {
            connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendNumber();
                }
            });
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        activationCode.setError(null);
                        if (activationCode.getText().toString().length() > 3) {
                            SendNumber();
                        } else {
                            activationCode.setError("کد فعالسازی را وارد کنید");
                        }
                    } catch (Exception e) {
                    }
                }
            });
            Toolbar_Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            btnResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTimerFinish)
                        ResendSms();
                    else {
                        AnimatorSet animator = new AnimatorSet();
                        ObjectAnimator a1 = ObjectAnimator.ofFloat(timer_textview, "scaleX", 1.0F, 0.8F, 1.0F, 1.1F, 1.3F, 1.0F);
                        ObjectAnimator b1 = ObjectAnimator.ofFloat(timer_textview, "scaleY", 1.0F, 0.8F, 1.0F, 1.1F, 1.3F, 1.0F);
                        a1.setDuration(500);
                        b1.setDuration(500);
                        animator.playTogether(a1, b1);
                        animator.start();
                    }
                }
            });
        } catch (Exception e) {
        }
    }
    private void SendNumber(){
        try{
            if(loadingHandler!=null)loadingHandler.ShowLoading();
            if(connectionFailHandler!=null)
                if(connectionFailHandler.isVisible())connectionFailHandler.Hide();

            API_UserActivation api_userActivation=new API_UserActivation(Activation_Act.this);
            api_userActivation.in_id.setVal(phoneNumber);
            api_userActivation.in_sms.setVal(activationCode.getText().toString());
            api_userActivation.sendRequest(Activation_Act.this, TAG_SAVE, api_userActivation.in_id, api_userActivation.in_sms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ResendSms(){
        try{
            if(loadingHandler!=null)loadingHandler.ShowLoading();
            if(connectionFailHandler!=null)
                if(connectionFailHandler.isVisible())connectionFailHandler.Hide();

            API_UserResendSms api_userResendSms=new API_UserResendSms(Activation_Act.this);
            sec = 0;
            min = 1;
            isTimerFinish = false;
            timer_textview.setText("01:00");
            myCountDownTimer.cancel();
            myCountDownTimer = new MyCountDownTimer(time, 1000);
            myCountDownTimer.start();
            api_userResendSms.in_id.setVal(phoneNumber);
            api_userResendSms.sendRequest(Activation_Act.this, TAG_RESEND, api_userResendSms.in_id);
        } catch (Exception e) {
        }
    }


    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        if(loadingHandler!=null)loadingHandler.HideLoading();
        if(hasError){

            //If Activation Request timed out, Show user an alert
            if(Tag.compareToIgnoreCase(TAG_SAVE)==0){
                Toast.makeText(Activation_Act.this,R.string.label81,Toast.LENGTH_LONG).show();
            }

            //If No Internet Connection Found
            if(Answer.equals(ServerRequest.NO_CONNECTION))
                if(connectionFailHandler!=null)
                    if(!connectionFailHandler.isVisible())connectionFailHandler.Show();
        }else{

            // Tag : On Activation Answer Recieved
            if(Tag.compareToIgnoreCase(TAG_SAVE)==0){

                //Check if Answer string is null
                if(Answer.length()==0){
                    Toast.makeText(Activation_Act.this,R.string.label82,Toast.LENGTH_LONG).show();
                }

                try{
                    ArrayList<MessageItem> item=ApplicationClass.MessageItem(new JSONObject(Answer));
                    if(!item.get(0).status) {
                        new CToast(Activation_Act.this,"دوباره تلاس کنید",1,CToast.LONG_DURATION).Show();
                    }else{

                        db.Execute(database.QRY_UPDATE_SETTING_INFO_FIELD,new RecordHolder(new FieldItem("@f1","user_name"),new FieldItem("'@i1'","'"+item.get(0).Titel+"'")));
                        db.Execute(database.QRY_INSERT_USER_INFO,new RecordHolder(new FieldItem("'@i1'","'"+phoneNumber+"'"),new FieldItem("'@i2'",""+item.get(0).Code)));
                        finish();
                    }
                }catch (Exception e){
                    Log.i("e",e.toString());
                }
            }
            //endregion

            if (Tag.compareToIgnoreCase(TAG_RESEND) == 0) {

            }

        }
    }

    int time = 60000;
    int sec = 0;
    int min = 1;
    boolean isTimerFinish = false;
    MyCountDownTimer myCountDownTimer;

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (sec == 0) {
                sec = 59;
                min--;
                if (min < 0) {
                    isTimerFinish = true;
                    sec = 0;
                    timer_textview.setText("00 : 00");
                    return;
                }
                timer_textview.setText("0" + min + " : " + sec);
                return;
            }
            if (sec < 10)
                timer_textview.setText("0" + min + " : 0" + sec);
            else
                timer_textview.setText("0" + min + " : " + sec);
            sec--;
        }

        @Override
        public void onFinish() {
            isTimerFinish = true;
        }

    }

}
