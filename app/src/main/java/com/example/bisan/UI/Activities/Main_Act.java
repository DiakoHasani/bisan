package com.example.bisan.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bisan.DataTypes.CategoryItem;
import com.example.bisan.R;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.Tools.database;
import com.example.bisan.UI.Fragments.Basket_Fragment;
import com.example.bisan.UI.Fragments.Category_Fragment;
import com.example.bisan.UI.Fragments.DrawerNavigation_Fragment;
import com.example.bisan.UI.Fragments.Home_Fragment;
import com.example.bisan.UI.Fragments.Order_Fragment;
import com.example.bisan.UI.ModalActivities.Modal_filter;
import com.flurry.android.FlurryAgent;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
//test
public class Main_Act extends AppCompatActivity  {

    private Toolbar mToolbar;
    private DrawerNavigation_Fragment drawerFragment;
    private ImageButton Toolbar_Toggle,Toolbar_Search,Toolbar_Back,Toolbar_Search_Del,Toolbar_Filter;
    private TextView Toolbar_Title;
    private EditText Toolbar_Search_Edittext;
    private RelativeLayout Toolbar_Search_Box;
    private Home_Fragment home_fragment;
    private Category_Fragment category_fragment;
    private Basket_Fragment basket_fragment;
    private Order_Fragment order_fragment;
    private FrameLayout bodyFrame;
    private DrawerLayout mdrawer;
    private LinearLayout menu_home,menu_category,menu_basket,menu_order;
    private ImageView home_img,category_img,basket_img,order_img;
    private View home_view,category_view,basket_view,order_view,line_home,line_category,line_basket,line_order;
    private String newest,expensive,bestsell,order_name,order_category,order_price,maxPrcie;
    private int current_fragment;
    private boolean hasFragmentsAdded,canExit;
    public static FragmentManager fragmentManager;

    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.act_main);
        fragmentManager = getSupportFragmentManager();

        Initials_Objects();

        UI_Initial();


    }



    //--> Do All Activity UI Works like Create Objects and etc here!
    private void UI_Initial(){
        try{
            setSupportActionBar(mToolbar);
            getSupportActionBar().setElevation(1.5f);

            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
            drawerFragment.setDrawerListener(drawerListener);
            Toolbar_Toggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        //--> open drawer Menu
                        mdrawer.openDrawer(Gravity.RIGHT);
                    }catch (Exception e){}
                }
            });



            menu_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ShowFragment(0);
                    SHowFragment(0);
                }
            });
            menu_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ShowFragment(1);
                    SHowFragment(1);
                }
            });
            menu_basket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ShowFragment(2);
                    SHowFragment(2);
                }
            });
            menu_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ShowFragment(3);
                    SHowFragment(3);
                }
            });
            Toolbar_Search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ShowFragment(1);
                    SHowFragment(1);
                }
            });
            Toolbar_Filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Main_Act.this, Modal_filter.class);
                    intent.putExtra("maxPrcie",maxPrcie);
                    intent.putExtra("order_price",order_price);
                    intent.putExtra("order_category",order_category);
                    intent.putExtra("order_name",order_name);
                    intent.putExtra("bestsell",bestsell);
                    intent.putExtra("expensive",expensive);
                    intent.putExtra("newest",newest);
                    startActivityForResult(intent,1010);
                }
            });
            Toolbar_Search_Del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Toolbar_Search_Edittext.setText("");
                    }catch (Exception e){}
                }
            });
            Toolbar_Search_Edittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try{
                        if(Toolbar_Search_Edittext.getText().toString().length()>0){
                            Toolbar_Search_Del.setVisibility(View.VISIBLE);
                        }else{
                            Toolbar_Search_Del.setVisibility(View.GONE);
                        }
                        category_fragment.OnSearchNameChanged(Toolbar_Search_Edittext.getText().toString());
                    }catch (Exception e){}
                }
            });

            //--> First Fragment is Main Fragment. so Show on Activity Starts
//            ShowFragment(0);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SHowFragment(final int newFrag){
        Runnable runShowFragment=new Runnable() {
            @Override
            public void run() {
                ShowFragment(newFrag);
            }
        };
        Handler handler=new Handler();
        handler.postDelayed(runShowFragment,100);

    }



    //--> Initials Objects for the first time with new Values
    private void Initials_Objects(){
        try{
            mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
            drawerFragment = (DrawerNavigation_Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            mdrawer=(DrawerLayout)findViewById(R.id.drawer_layout);
            Toolbar_Toggle=(ImageButton)findViewById(R.id.toolbar_menu);
            bodyFrame=(FrameLayout)findViewById(R.id.frameLayout);

            home_fragment=Home_Fragment.newInstance(new Home_Fragment.OnCategoryClick() {
                @Override
                public void onClick(CategoryItem item) {
                    try{
                        ShowFragment(1);
                        category_fragment.CategoryIntent(item);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            category_fragment=new Category_Fragment();
            basket_fragment=new Basket_Fragment();
            order_fragment=new Order_Fragment();

            menu_home=(LinearLayout)findViewById(R.id.bottomMenu_home);
            menu_category=(LinearLayout)findViewById(R.id.bottomMenu_category);
            menu_basket=(LinearLayout)findViewById(R.id.bottomMenu_basket);
            menu_order=(LinearLayout)findViewById(R.id.bottomMenu_order);

            home_img=(ImageView)findViewById(R.id.bottommenu_home_img);
            category_img=(ImageView)findViewById(R.id.bottommenu_category_img);
            basket_img=(ImageView)findViewById(R.id.bottommenu_basket_img);
            order_img=(ImageView)findViewById(R.id.bottommenu_order_img);

            home_view=(View)findViewById(R.id.orange_background_home);
            category_view=(View)findViewById(R.id.orange_background_category);
            basket_view=(View)findViewById(R.id.orange_background_basket);
            order_view=(View)findViewById(R.id.orange_background_order);

            line_home=(View)findViewById(R.id.Line4);
            line_category=(View)findViewById(R.id.Line3);
            line_basket=(View)findViewById(R.id.Line2);
            line_order=(View)findViewById(R.id.Line1);

            Toolbar_Back=(ImageButton)findViewById(R.id.toolbar_back);
            Toolbar_Filter=(ImageButton)findViewById(R.id.toolbar_filter);
            Toolbar_Search=(ImageButton)findViewById(R.id.toolbar_search);
            Toolbar_Search_Box=(RelativeLayout)findViewById(R.id.toolbar_searchbox);
            Toolbar_Search_Del=(ImageButton)findViewById(R.id.toolbar_delete);
            Toolbar_Title=(TextView)findViewById(R.id.toolbar_title);
            Toolbar_Search_Box=(RelativeLayout)findViewById(R.id.toolbar_searchbox);
            Toolbar_Search_Edittext=(EditText)findViewById(R.id.toolbar_search_edittext);

            maxPrcie="0";
            order_price=expensive=newest=order_category="false";
            order_name=bestsell="true";

            current_fragment=-1;
            hasFragmentsAdded=false;
        }catch (Exception e){}
    }



    private DrawerNavigation_Fragment.FragmentDrawerListener drawerListener=new DrawerNavigation_Fragment.FragmentDrawerListener() {
        @Override
        public void onLoginClick() {
            startActivity(new Intent(Main_Act.this,Login_Act.class));
            mdrawer.closeDrawers();
        }

        @Override
        public void onProfileClick() {
            startActivity(new Intent(Main_Act.this,Profile_Act.class));
            mdrawer.closeDrawers();
        }

        @Override
        public void onWalletClick() {
            try {
                DBExcute db=DBExcute.getInstance(Main_Act.this);
                db.Open();
                if(db.Read(database.QRY_GET_USER_INFO,null).HasRecord()) {
                    startActivity(new Intent(Main_Act.this, Wallet_Act.class));
                    mdrawer.closeDrawers();
                }else{
                    Intent intent=new Intent(Main_Act.this, Login_Act.class);
                    startActivity(intent);
                    mdrawer.closeDrawers();
                }
                db.Close();
            }catch (Exception e){}
        }

        @Override
        public void onSettingClick() {

        }

        @Override
        public void onWishClick() {
            startActivity(new Intent(Main_Act.this,Favorite_Act.class));
            mdrawer.closeDrawers();
        }

        @Override
        public void onSupportClick() {

        }

        @Override
        public void onRulesClick() {

        }

        @Override
        public void onAboutClick() {

        }

        @Override
        public void onFriendClick() {

        }

        @Override
        public void onDrawerColsed() {

        }
    };

    private void ShowFragment(int new_fragment){
        try{
            if(current_fragment==new_fragment)return;
            InitialToolbar(new_fragment);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if(new_fragment<current_fragment){
                transaction.setCustomAnimations(R.anim.fadein,R.anim.fadeout);
            }else{
                transaction.setCustomAnimations(R.anim.fadein,R.anim.fadeout);
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //--> Remove Previous fragment
            if(hasFragmentsAdded && current_fragment!=new_fragment) {
                //--> Add New Fragment
                switch (new_fragment){
                    case 0:
//                        transaction.remove(home_fragment);
                        transaction.replace(bodyFrame.getId(), home_fragment);
                        break;
                    case 1:
//                        transaction.remove(category_fragment);
                        transaction.replace(bodyFrame.getId(), category_fragment);
                        break;
                    case 2:
//                        transaction.remove(basket_fragment);
                        transaction.replace(bodyFrame.getId(), basket_fragment);
                        break;
                    case 3:
//                        transaction.remove(order_fragment);
                        transaction.replace(bodyFrame.getId(), order_fragment);
                        break;
                }
            }


            else{
                //--> Add New Fragment
                switch (new_fragment){
                    case 0:
                        home_fragment=new Home_Fragment();
                        transaction.add(bodyFrame.getId(), home_fragment);
                        break;
                    case 1:
                        category_fragment=new Category_Fragment();
                        transaction.add(bodyFrame.getId(), category_fragment);
                        category_fragment.Page_Offset=-1;
                        break;
                    case 2:
                        basket_fragment=new Basket_Fragment();
                        transaction.add(bodyFrame.getId(), basket_fragment);
                        break;
                    case 3:
                        order_fragment=new Order_Fragment();
                        transaction.add(bodyFrame.getId(), order_fragment);
                        break;
                }
            }


            transaction.commit();

            if(!hasFragmentsAdded)hasFragmentsAdded=true;

        }catch (Exception e){
            e.printStackTrace();
        }

        //--> Update Bottom Menu UI
        SetBottomMenu(new_fragment);
    }

    private void SetBottomMenu(int new_fragment){
        try {

            //--> Part 1: Disable Current Item
            switch (current_fragment) {
                case 0:
                    home_img.setImageResource(R.drawable.icon_black_home);
                    home_view.setVisibility(View.INVISIBLE);
                    line_home.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    category_img.setImageResource(R.drawable.icon_black_category);
                    category_view.setVisibility(View.INVISIBLE);
                    line_category.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    basket_img.setImageResource(R.drawable.icon_black_basket);
                    basket_view.setVisibility(View.INVISIBLE);
                    line_basket.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    order_img.setImageResource(R.drawable.icon_black_order);
                    order_view.setVisibility(View.INVISIBLE);
                    line_order.setVisibility(View.INVISIBLE);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try{

            //--> Part 2: Active New Item
            switch (new_fragment){
                case 0:
                    home_img.setImageResource(R.drawable.icon_red_home);
                    home_view.setVisibility(View.VISIBLE);
                    line_home.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    category_img.setImageResource(R.drawable.icon_red_category);
                    category_view.setVisibility(View.VISIBLE);
                    line_category.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    basket_img.setImageResource(R.drawable.icon_red_basket);
                    basket_view.setVisibility(View.VISIBLE);
                    line_basket.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    order_img.setImageResource(R.drawable.icon_red_order);
                    order_view.setVisibility(View.VISIBLE);
                    line_order.setVisibility(View.VISIBLE);
                    break;
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        //--> Change Current Fragment number to Selected new One
        current_fragment=new_fragment;
    }

    private void InitialToolbar(int new_fragment){
        try{
            HideAllToolbarObjects();
            switch (new_fragment){
                case 0:
                    Toolbar_Toggle.setVisibility(View.VISIBLE);
                    Toolbar_Search.setVisibility(View.VISIBLE);
                    Toolbar_Title.setVisibility(View.VISIBLE);
                    Toolbar_Title.setText(R.string.toolbar_title_1);
                    break;
                case 1:
                    Toolbar_Toggle.setVisibility(View.VISIBLE);
                    Toolbar_Filter.setVisibility(View.VISIBLE);
                    Toolbar_Search_Box.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    Toolbar_Toggle.setVisibility(View.VISIBLE);
                    Toolbar_Search.setVisibility(View.VISIBLE);
                    Toolbar_Title.setVisibility(View.VISIBLE);
                    Toolbar_Title.setText(R.string.toolbar_title_2);
                    break;
                case 3:
                    Toolbar_Toggle.setVisibility(View.VISIBLE);
                    Toolbar_Search.setVisibility(View.VISIBLE);
                    Toolbar_Title.setVisibility(View.VISIBLE);
                    Toolbar_Title.setText(R.string.toolbar_title_3);
                    break;
            }
        }catch (Exception e){}
    }

    private void HideAllToolbarObjects(){
        try{
            Toolbar_Search.setVisibility(View.GONE);
            Toolbar_Search_Box.setVisibility(View.GONE);
            Toolbar_Toggle.setVisibility(View.GONE);
            Toolbar_Filter.setVisibility(View.GONE);
            Toolbar_Back.setVisibility(View.GONE);
            Toolbar_Title.setVisibility(View.GONE);
        }catch (Exception e){}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(resultCode==1010 && data.getExtras()!=null){
                //Filter Form
                Bundle bundle=data.getExtras();
                order_name=bundle.getString("order_name");
                order_category=bundle.getString("order_category");
                order_price=bundle.getString("order_price");
                maxPrcie=bundle.getString("maxPrcie");
                expensive=bundle.getString("expensive");
                newest=bundle.getString("newest");
                bestsell=bundle.getString("bestsell");
                ChangeFilterImage(Boolean.parseBoolean(bundle.getString("isClear")));
                category_fragment.OnSearchNameChanged(Toolbar_Search_Edittext.getText().toString(),Boolean.parseBoolean(order_name),Boolean.parseBoolean(order_price),Boolean.parseBoolean(order_category),maxPrcie,Boolean.parseBoolean(expensive),Boolean.parseBoolean(newest),Boolean.parseBoolean(bestsell));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void ChangeFilterImage(boolean isClear){
        try{
            if(isClear){Toolbar_Filter.setImageResource(R.drawable.icon_gray_filter);}else{Toolbar_Filter.setImageResource(R.drawable.icon_red_filter);}
        }catch (Exception e){}
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            drawerFragment.UpdateMenu();
            if(current_fragment==-1){SHowFragment(0);}else{SHowFragment(current_fragment);}


        }catch (Exception e){
            Log.i("e",e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        if(canExit==true) {
            super.onBackPressed();
        }else{
            canExit=true;
            Toast.makeText(Main_Act.this,"دوباره دکمه بازگشت را بزنید",Toast.LENGTH_SHORT).show();
        }
        CountDownTimer countDownTimer=new CountDownTimer(1000,10) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                canExit=false;
            }
        }.start();
    }
}
