package com.example.bisan.UI.Activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bisan.DataTypes.FieldItem;
import com.example.bisan.DataTypes.RecordHolder;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.UserItem;
import com.example.bisan.Network.API_EditUser;
import com.example.bisan.Network.API_UserInfo;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.Tools.CToast;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;
import com.example.bisan.Tools.database;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Profile_Act extends AppCompatActivity implements ApplicationClass.API_Listener {

    private RelativeLayout Toolbar_Search_Box;
    private CircleImageView profile_image;
    private EditText name, phone, address, email;
    private Toolbar mToolbar;
    private Button btnSave;
    private ImageButton Toolbar_Toggle, Toolbar_Search, Toolbar_Back, Toolbar_Menu;
    private TextView Toolbar_Title;
    private String TAG_SAVE = "SaveEditUser", TAG_INFO = "UserInfo";
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
        setContentView(R.layout.act_profile);

        Initials_Objects();

        UI_Initial();

        GetInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.Close();
    }

    //--> Do All Activity UI Works like Create Objects and etc here!
    private void UI_Initial() {
        try {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setElevation(1.5f);

            db=DBExcute.getInstance(Profile_Act.this);
            db.Open();

            Toolbar_Search.setVisibility(View.GONE);
            Toolbar_Toggle.setVisibility(View.GONE);
            Toolbar_Back.setVisibility(View.VISIBLE);
            Toolbar_Menu.setVisibility(View.GONE);
            Toolbar_Title.setText("مشخصات کاربر");
            Toolbar_Title.setVisibility(View.VISIBLE);
            Toolbar_Search_Box.setVisibility(View.GONE);
            Toolbar_Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            Toolbar_Menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOptionsMenu();
                }
            });
            loadingHandler=new LoadingHandler(Profile_Act.this);
            connectionFailHandler=new ConnectionFailHandler(Profile_Act.this);
            connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connectionFailHandler.Hide();
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SaveEdit();
                }
            });

        } catch (Exception e) {
        }
    }

    //--> Initials Objects for the first time with new Values
    private void Initials_Objects() {
        try {
            profile_image = (CircleImageView) findViewById(R.id.profile_image);
            name = (EditText) findViewById(R.id.edt_name_profile);
            phone = (EditText) findViewById(R.id.edt_phone_profile);
            address = (EditText) findViewById(R.id.edt_address_profile);
            email = (EditText) findViewById(R.id.edt_email_profile);
            btnSave=(Button)findViewById(R.id.btn_save_profile);

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

    private void SaveEdit() {
        try {
            if(loadingHandler!=null)loadingHandler.ShowLoading();

            API_EditUser api_editUser = new API_EditUser(Profile_Act.this);

            JSONObject object=new JSONObject();
            object.put(api_editUser.in_id.getParamName(),db.Read(database.QRY_GET_USER_INFO,null).GetField(0));
            object.put(api_editUser.in_tel.getParamName(),phone.getText().toString());
            object.put(api_editUser.in_fullname.getParamName(),name.getText().toString());
            object.put(api_editUser.in_email.getParamName(),email.getText().toString());
            object.put(api_editUser.in_address.getParamName(),address.getText().toString());
            object.put(api_editUser.in_status.getParamName(),true);
            api_editUser.sendRequest(Profile_Act.this, TAG_SAVE,object);
        } catch (Exception e) {
        }
    }

    private void GetInfo() {
        try {

            if(loadingHandler!=null)loadingHandler.ShowLoading();

            API_UserInfo api_userInfo = new API_UserInfo(Profile_Act.this);
            api_userInfo.in_id.setVal(db.Read(database.QRY_GET_USER_INFO,null).GetField(0));
            api_userInfo.sendRequest(Profile_Act.this, TAG_INFO, api_userInfo.in_id);
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

                if (Tag.compareToIgnoreCase(TAG_SAVE) == 0) {
                    boolean answer=Boolean.parseBoolean(Answer);
                    if(answer){
                        Toast.makeText(Profile_Act.this,"ویرایش انجام شد",Toast.LENGTH_LONG).show();

                        db.Open();
                        db.Execute(database.QRY_UPDATE_SETTING_INFO_FIELD,new RecordHolder(new FieldItem("'@i1'","'"+name.getText().toString()+"'"),new FieldItem("@f1","user_name")));

                    }else{
                        Toast.makeText(Profile_Act.this, "لطفا دوباره تلاش کنید", Toast.LENGTH_LONG).show();
                    }
                }

                if (Tag.compareToIgnoreCase(TAG_INFO) == 0) {
                    try {
                        userItem.addAll(Items);
                        updateUserInfo();
                    } catch (Exception e) {
                    }
                }

            }
        } catch (Exception e) {
        }
    }

    public void updateUserInfo() {
        if (userItem.size() > 0) {
            UserItem userInfo = userItem.get(0);
            name.setText(userInfo.getName());
            phone.setText(userInfo.getPhone());
            address.setText(userInfo.getAddress());
            email.setText(userInfo.getEmail());
//            Glide.with(this)
//                    .load(ServerRequest.BaseURLImage + userInfo.getImage())
//                    .dontAnimate()
//                    .into(profile_image);
        }
    }
}
