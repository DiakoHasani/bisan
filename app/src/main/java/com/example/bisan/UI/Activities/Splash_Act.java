package com.example.bisan.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bisan.DataTypes.FieldItem;
import com.example.bisan.DataTypes.QueryItem;
import com.example.bisan.DataTypes.RecordHolder;
import com.example.bisan.Network.API_AppVersion;
import com.example.bisan.Network.BaseModel;
import com.example.bisan.Network.VolleyStringRequest;
import com.example.bisan.Network.onRequestResponseListener;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.Network.API_UserLastSeen;
import com.example.bisan.R;
import com.example.bisan.Tools.CToast;
import com.example.bisan.Tools.DBCursor;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.UI.Dialogs.CustomDialog;
import com.example.bisan.UI.UIHandlers.LoadingHandler;
import com.example.bisan.Tools.database;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Splash_Act extends AppCompatActivity implements ApplicationClass.API_Listener {

    ImageView Logo_Besan,Logo_Fruit;
    private LoadingHandler loadingHandler;
    private DBExcute db;
    private String TAG_VERSION="VersionControl";
    private boolean can_go_to_home=false,sent_to_home=false;
    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.act_splash);

        Initials_Objects();

        UI_Initial();


    }

    private void Initials_Objects(){
        database database= com.example.bisan.Tools.database.getInstance(this);
        database.databse();

        Logo_Besan=findViewById(R.id.Logo_Besan);
        Logo_Fruit=findViewById(R.id.Logo_Besan_1);

        db=DBExcute.getInstance(this);
        db.CopyDB();
        loadingHandler=new LoadingHandler(this);
    }


    private void UI_Initial(){
        CheckQuery();
        SendStatus();
        DataTime();
    }

    public void SendStatus(){
        try{
            API_UserLastSeen lastSeen=new API_UserLastSeen(Splash_Act.this,db.Read(database.QRY_GET_USER_INFO,null).GetField(0));
            lastSeen.in_id.setVal(db.Read(database.QRY_GET_USER_INFO,null).GetField(0));
            lastSeen.sendRequest(Splash_Act.this,"Lastseen",lastSeen.in_id);
        }catch (Exception e){}
    }

    public void CheckQuery(){
        try{
            API_AppVersion api_appVersion=new API_AppVersion(Splash_Act.this,db.Read(database.QRY_GET_USER_INFO,null).GetField(0));
            api_appVersion.in_id.setVal(db.Read(database.QRY_GET_USER_INFO,null).GetField(0));
            api_appVersion.sendRequest(Splash_Act.this,TAG_VERSION,api_appVersion.in_id);
        }catch (Exception e){}
    }


    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {

        if(Tag.compareToIgnoreCase(TAG_VERSION)==0){
            if(!hasError){

                // If No Items found.
                if(Items.size()<=0){
                    can_go_to_home=true;
                    GoToHome();
                    return;
                }

                final ArrayList<QueryItem> QryArr=new ArrayList<>();
                QryArr.addAll(Items);

                double myAppVer=Double.valueOf(db.Read(database.QRY_GET_VERSION_INFO,null).GetField(0));
                double myQueryVer=Double.valueOf(db.Read(database.QRY_GET_VERSION_INFO,null).GetField(1));

                // Difference between app versions
                double difVer=QryArr.get(QryArr.size()-1).AppVesrsion-myAppVer;

                //Check App Version
                if(difVer<1 && difVer>=0){

                    can_go_to_home=true;

                    double lastQueryNum=myQueryVer;

                    //Execute Updated Queries
                    for(int i=0;i<QryArr.size();i++){
                        if(QryArr.get(i).Vesrsion>myQueryVer){
                            try{db.Execute(QryArr.get(i).Query,null);}catch (Exception e){}
                            if(lastQueryNum<QryArr.get(i).Vesrsion)lastQueryNum=QryArr.get(i).Vesrsion;
                        }
                    }

                    if(lastQueryNum>myQueryVer){

                        Toast.makeText(Splash_Act.this,getResources().getString(R.string.label87),Toast.LENGTH_LONG).show();

                    }

                    db.Execute(database.QRY_UPDATE_VERSION_INFO,new RecordHolder(new FieldItem("@f1","queryVersion"),new FieldItem("@v1","'"+lastQueryNum+"'")));

                    GoToHome();

                }else if(difVer>=1){

                    // App Version is old. send user to market
                    final CustomDialog dialog=new CustomDialog(Splash_Act.this,R.string.label84,R.string.label83,R.string.label85,R.string.label86);
                    dialog.setOnclick(new CustomDialog.Onclick() {
                        @Override
                        public void onAcceptClick() {
                            dialog.dismiss();
                            Intent intent=new Intent(QryArr.get(QryArr.size()-1).UpdateBazzar);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelClick() {
                            dialog.dismiss();
                            Intent intent=new Intent(QryArr.get(QryArr.size()-1).UpdateGoogle);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                }

            }else{
                can_go_to_home=true;
            }
        }

    }

    void DataTime(){
        final Handler handler_Besan_Logo = new Handler();
        handler_Besan_Logo.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation aniFade = AnimationUtils.loadAnimation(Splash_Act.this, R.anim.fadein);
                Logo_Besan.startAnimation(aniFade);
                Logo_Besan.setVisibility(View.VISIBLE);
            }
        }, 800);
        final Handler handler_Ibl_Logo_Fruit=new Handler();
        handler_Ibl_Logo_Fruit.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation aniSlide=AnimationUtils.loadAnimation(Splash_Act.this,R.anim.slide_in_right);
                Logo_Fruit.setAnimation(aniSlide);
                Logo_Fruit.setVisibility(View.VISIBLE);
            }
        },1500);

        Handler nextPage=new Handler();

        nextPage.postDelayed(new Runnable() {
            @Override
            public void run() {
                GoToHome();
//                Splash_Act.this.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left).replace(R.id.frame_layout,new About_Fragment_Java()).commit();
            }
        },5000);
    }


    public void GoToHome(){
        if(can_go_to_home && !sent_to_home){
            sent_to_home=true;
            startActivity(new Intent(Splash_Act.this, Main_Act.class));
            Splash_Act.this.finish();
        }
    }

}


