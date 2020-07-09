package com.example.bisan.UI.Activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bisan.DataTypes.UserItem;
import com.example.bisan.Network.API_UserInfo;
import com.example.bisan.Network.API_UserWallet;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.Tools.FarsiUtil;
import com.example.bisan.Tools.database;
import com.example.bisan.UI.Fragments.Basket_Fragment;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Wallet_Act extends AppCompatActivity implements ApplicationClass.API_Listener{

    private RelativeLayout Toolbar_Search_Box;
    private TextView price;
    private Toolbar mToolbar;
    private ImageButton Toolbar_Toggle, Toolbar_Search, Toolbar_Back, Toolbar_Menu;
    private TextView Toolbar_Title;
    private String TAG_INFO = "UserInfo";
    private ArrayList<UserItem> userItem;
    private DBExcute db;
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
        setContentView(R.layout.act_wallet);

        Initials_Objects();

        UI_Initial();

        GetInfo();

    }

    //--> Do All Activity UI Works like Create Objects and etc here!
    private void UI_Initial() {
        try {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setElevation(1.5f);

            db=DBExcute.getInstance(Wallet_Act.this);
            db.Open();

            Toolbar_Search.setVisibility(View.GONE);
            Toolbar_Toggle.setVisibility(View.GONE);
            Toolbar_Back.setVisibility(View.VISIBLE);
            Toolbar_Menu.setVisibility(View.GONE);
            Toolbar_Title.setText("کیف پــول");
            Toolbar_Title.setVisibility(View.VISIBLE);
            Toolbar_Search_Box.setVisibility(View.GONE);
            Toolbar_Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            loadingHandler=new LoadingHandler(Wallet_Act.this);
            connectionFailHandler=new ConnectionFailHandler(Wallet_Act.this);
            connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connectionFailHandler.Hide();
                }
            });

        } catch (Exception e) {
        }
    }

    //--> Initials Objects for the first time with new Values
    private void Initials_Objects() {
        try {
            price = (TextView) findViewById(R.id.edt_price_wallet);

            mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
            Toolbar_Title = (TextView) findViewById(R.id.toolbar_title);
            Toolbar_Toggle = (ImageButton) findViewById(R.id.toolbar_menu);
            Toolbar_Back = (ImageButton) findViewById(R.id.toolbar_back);
            Toolbar_Menu = (ImageButton) findViewById(R.id.toolbar_dotmenu);
            Toolbar_Search = (ImageButton) findViewById(R.id.toolbar_search);
            Toolbar_Search_Box=(RelativeLayout)findViewById(R.id.toolbar_searchbox);
            userItem = new ArrayList<>();
        } catch (Exception e) {
        }
    }

    private void GetInfo() {
        try {

            if(loadingHandler!=null)loadingHandler.ShowLoading();
            API_UserWallet userWallet=new API_UserWallet(Wallet_Act.this);
            userWallet.in_id.setVal(db.Read(database.QRY_GET_USER_INFO,null).GetField(6));
            userWallet.sendRequest(Wallet_Act.this,TAG_INFO,userWallet.in_id);
        } catch (Exception e) {
        }
    }

    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        try {
            if(loadingHandler!=null)loadingHandler.HideLoading();
            if (hasError) {

                //If No Internet Connection Found
                if(Answer.equals(ServerRequest.NO_CONNECTION))
                    if(connectionFailHandler!=null)
                        if(!connectionFailHandler.isVisible())connectionFailHandler.Show();

            } else {


                if (Tag.compareToIgnoreCase(TAG_INFO) == 0) {

                    price.setText(FarsiUtil.ConvertDoubleToToman(FarsiUtil.ReplaceQoute(Answer))+" تومان ");
                    price.setEnabled(false);

                }

            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{db.Close();}catch (Exception e){}
    }
}
