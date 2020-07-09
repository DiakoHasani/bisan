package com.example.bisan.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.MessageItem;
import com.example.bisan.Network.API_NewUser;
import com.example.bisan.Network.API_UserResendSms;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login_Act extends AppCompatActivity implements ApplicationClass.API_Listener {

    private EditText phone;
    private ImageButton Toolbar_Back;
    private TextView Toolbar_Title;
    private String TAG_SAVE = "SaveNewUser",TAG_RESEND="Resend";
    private Button btnSend;
    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.act_login);

        Initials_Objects();

        UI_Initial();

    }


    //--> Initials Objects for the first time with new Values
    private void Initials_Objects() {
        try {
            Toolbar_Back = (ImageButton) findViewById(R.id.toolbar_back);
            Toolbar_Title = (TextView) findViewById(R.id.toolbar_title);

            btnSend=(Button)findViewById(R.id.btn_login_sign_up);
            phone=(EditText)findViewById(R.id.edt_phone_sign_up);
            loadingHandler=new LoadingHandler(this);
            connectionFailHandler=new ConnectionFailHandler(this);
            connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendNumber();
                }
            });


        } catch (Exception e) {
        }
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
                        phone.setError(null);
                        if (phone.getText().toString().length() > 9) {
                            SendNumber();
                        } else {
                            phone.setError("شماره تلفن صحیح نیست");
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


        } catch (Exception e) {
        }
    }

    private void SendNumber(){
        try{
            if(loadingHandler!=null)loadingHandler.ShowLoading();
            if(connectionFailHandler!=null)
                if(connectionFailHandler.isVisible())connectionFailHandler.Hide();

            API_NewUser api_newUser=new API_NewUser(Login_Act.this);
            api_newUser.in_cellphone.setVal(phone.getText().toString());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(api_newUser.in_cellphone.getParamName(), api_newUser.in_cellphone.getVal());
            jsonObject.put(api_newUser.in_fullname.getParamName(),"");
            jsonObject.put(api_newUser.in_email.getParamName(),"");
            jsonObject.put(api_newUser.in_tel.getParamName(),"");
            api_newUser.sendRequest(Login_Act.this, TAG_SAVE, jsonObject);
        } catch (Exception e) {
        }
    }

    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        if(loadingHandler!=null)loadingHandler.HideLoading();
        if(!hasError){

            ArrayList<MessageItem> item=new ArrayList<>();
            if(Tag.compareToIgnoreCase(TAG_SAVE)==0){
                item.addAll(Items);
                if(item.get(0).Code.compareToIgnoreCase("202")==0 || item.get(0).status){
//                    ResendSms();
                    Intent intent = new Intent(Login_Act.this, Activation_Act.class);
                    intent.putExtra("phone", phone.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }

            //If No Internet Connection Found
            if(Answer.equals(ServerRequest.NO_CONNECTION))
                if(connectionFailHandler!=null)
                    if(!connectionFailHandler.isVisible())connectionFailHandler.Show();


        }else{

            if (Tag.compareToIgnoreCase(TAG_SAVE) == 0 || Tag.compareToIgnoreCase(TAG_RESEND)==0) {
                    Intent intent = new Intent(Login_Act.this, Activation_Act.class);
                    intent.putExtra("phone", phone.getText().toString());
                    startActivity(intent);
                    finish();
            }

        }
    }

    private void ResendSms(){
        try{
            if(loadingHandler!=null)loadingHandler.ShowLoading();
            if(connectionFailHandler!=null)
                if(connectionFailHandler.isVisible())connectionFailHandler.Hide();

            API_UserResendSms api_userResendSms=new API_UserResendSms(Login_Act.this);
            api_userResendSms.in_id.setVal(phone.getText().toString());
            api_userResendSms.sendRequest(Login_Act.this, TAG_RESEND, api_userResendSms.in_id);
        } catch (Exception e) {
        }
    }
}
