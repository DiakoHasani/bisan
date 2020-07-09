package com.example.bisan.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.bisan.R;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.Tools.database;


/**
 * Created by shahab-pc on 12/14/2016.
 */

public class DrawerNavigation_Fragment extends Fragment {


    /*************************************
     *
     * FRAGMENT :: Display This Fragment As Primary Drawer Menu Of App in Main Menu.
     *
     ************************************/

    private static String TAG = DrawerNavigation_Fragment.class.getSimpleName();

    private View containerView;
    private DrawerLayout mDrawerLayout;
    private FragmentDrawerListener drawerListener;
    private LinearLayout login,profile,wallet,setting,wish,rules,support,about,friend;
    public TextView name;
    public ImageView business_logo;
    public DrawerNavigation_Fragment() {}


    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // preparing navigation drawer items
        View layout;

        layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        business_logo=(ImageView)layout.findViewById(R.id.drawer_img);
        name=(TextView)layout.findViewById(R.id.drawer_title);

        login=(LinearLayout)layout.findViewById(R.id.drawer_item_login);
        profile=(LinearLayout)layout.findViewById(R.id.drawer_item_profile);
        wallet=(LinearLayout)layout.findViewById(R.id.drawer_item_wallet);
        setting=(LinearLayout)layout.findViewById(R.id.drawer_item_update);
        wish=(LinearLayout)layout.findViewById(R.id.drawer_item_wish);
        support=(LinearLayout)layout.findViewById(R.id.drawer_item_support);
        rules=(LinearLayout)layout.findViewById(R.id.drawer_item_rules);
        about=(LinearLayout)layout.findViewById(R.id.drawer_item_about);
        friend=(LinearLayout)layout.findViewById(R.id.drawer_item_friend);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerListener!=null){drawerListener.onLoginClick();}
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerListener!=null){drawerListener.onProfileClick();}
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerListener!=null){drawerListener.onWalletClick();}
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerListener!=null){drawerListener.onSettingClick();}
            }
        });

        wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerListener!=null){drawerListener.onWishClick();}
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerListener!=null){drawerListener.onSupportClick();}
            }
        });

        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerListener!=null){drawerListener.onRulesClick();}
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerListener!=null){drawerListener.onAboutClick();}
            }
        });

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerListener!=null){drawerListener.onFriendClick();}
            }
        });

        UpdateMenu();

        return layout;
    }

    public void UpdateMenu(){
        try{
            //Check if User Has Logged in so Show Profile MEnu Item, othercase show Login Menu Item
            boolean hasLogin=false;
            DBExcute db=DBExcute.getInstance(getContext());
            db.Open();
            hasLogin=db.Read(database.QRY_GET_USER_INFO,null).HasRecord();

            if(hasLogin){

//                db.Open();
                String user_name=db.Read(database.QRY_GET_SETTING_INFO,null).GetField(4);

                if(user_name==null){
                    name.setText("کاربر بیسان");
                }else {
                    if(user_name.length()>0){name.setText(user_name);}else{name.setText("کاربر بیسان");}
                }
                login.setVisibility(View.GONE);
                profile.setVisibility(View.VISIBLE);
            }else{

                name.setText("کاربر بیسان");
                login.setVisibility(View.VISIBLE);
                profile.setVisibility(View.GONE);
            }

        }catch (Exception e){
            Log.i("e",e.toString());
        }
    }



    public interface FragmentDrawerListener {
        public void onLoginClick();
        public void onProfileClick();
        public void onWalletClick();
        public void onSettingClick();
        public void onWishClick();
        public void onSupportClick();
        public void onRulesClick();
        public void onAboutClick();
        public void onFriendClick();
        public void onDrawerColsed();
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

