package com.example.bisan.UI.Activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bisan.DataTypes.FieldItem;
import com.example.bisan.DataTypes.RecordHolder;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.ProductItem;
import com.example.bisan.DataTypes.SliderItem;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.Tools.FarsiUtil;
import com.example.bisan.Network.API_ProductInfo;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.Tools.CToast;
import com.example.bisan.UI.Adapters.ImageAdapter;
import com.example.bisan.UI.Fragments.Home_Fragment;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;
import com.example.bisan.Tools.database;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Details_Act extends AppCompatActivity implements ApplicationClass.API_Listener {

    private ImageButton Toolbar_Back,btnLike,Toolbar_Toggle,Toolbar_Menu,Toolbar_Search;
    private RelativeLayout Toolbar_Search_Box;
    private TextView Toolbar_Title;
    private String TAG_SAVE="SaveEditUser",product_id;
    private Toolbar mToolbar;
    private static int NUM_PAGES = 1;
    private ViewPager mPager;
    private PageIndicatorView pageIndicatorView;
    private TextView name,category,price,rate,description,type;
    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;
    private Button btnBasket;
    private DBExcute db;

    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.act_details);

        Initials_Objects();

        UI_Initial();

    }


    //--> Initials Objects for the first time with new Values
    private void Initials_Objects() {
        try {
            name=(TextView)findViewById(R.id.details_name);
            category=(TextView)findViewById(R.id.details_category);
            price=(TextView)findViewById(R.id.details_price);
            description=(TextView)findViewById(R.id.details_description);
            type=(TextView)findViewById(R.id.details_type);

            mPager=(ViewPager)findViewById(R.id.view_pager_imgs);
            pageIndicatorView=(PageIndicatorView)findViewById(R.id.pageIndicatorView);

            btnBasket=(Button)findViewById(R.id.details_btn_add);
            btnLike=(ImageButton)findViewById(R.id.details_btnLike);

            mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
            Toolbar_Title = (TextView) findViewById(R.id.toolbar_title);
            Toolbar_Toggle = (ImageButton) findViewById(R.id.toolbar_menu);
            Toolbar_Back = (ImageButton) findViewById(R.id.toolbar_back);
            Toolbar_Menu = (ImageButton) findViewById(R.id.toolbar_dotmenu);
            Toolbar_Search = (ImageButton) findViewById(R.id.toolbar_search);
            Toolbar_Search_Box=(RelativeLayout)findViewById(R.id.toolbar_searchbox);

            loadingHandler=new LoadingHandler(Details_Act.this);
            connectionFailHandler=new ConnectionFailHandler(Details_Act.this);
            connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        GetInfo();
                    }catch (Exception e){}
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.Close();
    }

    //--> Do All Activity UI Works like Create Objects and etc here!
    private void UI_Initial() {
        try {

            Bundle bundle=getIntent().getExtras();
            if(bundle==null){
                finish();
            }
            product_id=bundle.getString("product_id");
            db=DBExcute.getInstance(this);
            db.Open();
            if(db.Read(database.QRY_GET_FAVORITE_INFO_BY_ID,new RecordHolder(new FieldItem("'@i1'","'"+product_id+"'"))).HasRecord()){btnLike.setImageResource(R.drawable.icon_red_heart);}else{btnLike.setImageResource(R.drawable.icon_gray_heart);}

            Toolbar_Search.setVisibility(View.GONE);
            Toolbar_Toggle.setVisibility(View.GONE);
            Toolbar_Back.setVisibility(View.VISIBLE);
            Toolbar_Menu.setVisibility(View.GONE);
            Toolbar_Title.setText("جزییـــات");
            Toolbar_Search_Box.setVisibility(View.GONE);
            Toolbar_Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(db.Read(database.QRY_GET_FAVORITE_INFO_BY_ID,new RecordHolder(new FieldItem("'@i1'","'"+product_id+"'"))).HasRecord()){
                            db.Execute(database.QRY_DELETE_FAVORITE_INFO_BY_ID,new RecordHolder(new FieldItem("'@i1'","'"+product_id+"'")));
                            btnLike.setImageResource(R.drawable.icon_gray_heart);
                        }else{
                            db.Execute(database.QRY_INSERT_FAVORITE_INFO,new RecordHolder(new FieldItem("'@i1'","'"+product_id+"'")));
                            btnLike.setImageResource(R.drawable.icon_red_heart);
                        }
                    }catch (Exception e){}
                }
            });

            btnBasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(db.Read(database.QRY_GET_BASKET_INFO_BY_ID,new RecordHolder(new FieldItem("'@i1'","'"+product_id+"'"))).HasRecord()){

                            Toast.makeText(Details_Act.this, R.string.label72, CToast.SHORT_DURATION).show();
                        }else {
                            db.Execute(database.QRY_INSERT_BASKET_INFO, new RecordHolder(new FieldItem("'@i1'", "'" + product_id + "'")));

                            Toast.makeText(Details_Act.this, R.string.label51, CToast.SHORT_DURATION).show();
                        }
                    }catch (Exception e){}
                }
            });

            GetInfo();
        } catch (Exception e) {
        }
    }

    private void GetInfo(){
        try{
            if(loadingHandler!=null)loadingHandler.ShowLoading();
            if(connectionFailHandler!=null)
                if(connectionFailHandler.isVisible())connectionFailHandler.Hide();

            API_ProductInfo productInfo=new API_ProductInfo(Details_Act.this);
            productInfo.in_Id.setVal(product_id);
            productInfo.sendRequest(Details_Act.this,TAG_SAVE,productInfo.in_Id);
        }catch (Exception e){}
    }

    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        if(loadingHandler!=null)loadingHandler.HideLoading();
        if(hasError){
            //If No Internet Connection Found
            if(Answer.equals(ServerRequest.NO_CONNECTION))
                if(connectionFailHandler!=null)
                    if(!connectionFailHandler.isVisible())connectionFailHandler.Show();
        }else{

            if(Tag.compareToIgnoreCase(TAG_SAVE)==0){
                if(Items==null)finish();
                ProductItem item=(ProductItem) Items.get(0);
                name.setText(item.getProduct_name());
                category.setText(item.getCategory_title());
                type.setText(FarsiUtil.convertToFarsi(String.valueOf(item.getCount()))+" کیلو ");
                description.setText(item.getDescription());

                price.setText(FarsiUtil.ConvertDoubleToToman((item.getPrice())) + " تومان");

                SliderItem item1=new SliderItem();
                item1.setDescription("");
                item1.setImage(item.getProduct_img());

                /*Glide.with(Details_Act.this)
                        .load(item.getProduct_img())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(image);*/

                ArrayList<SliderItem> sliderItems=new ArrayList<>();

                //--> Add Primary image to array
                SliderItem sliderItem=new SliderItem();
                sliderItem.setImage(item.getProduct_img());
                sliderItem.setOrder("1");
                sliderItems.add(sliderItem);

                for(int i=0;i<item.MoreImg.size();i++){
                    sliderItem=new SliderItem();
                    sliderItem.setImage(item.MoreImg.get(i));
                    sliderItem.setOrder("1");
                    sliderItems.add(sliderItem);
                }

                NUM_PAGES = item.MoreImg.size()+1;
                pageIndicatorView.setCount(NUM_PAGES);
                ImageAdapter imageAdapter=new ImageAdapter(Details_Act.this,sliderItems);
                imageAdapter.isDetailsActivity=true;
                mPager.setAdapter(imageAdapter);
                pageIndicatorView.setViewPager(mPager);
            }

        }
    }
}
