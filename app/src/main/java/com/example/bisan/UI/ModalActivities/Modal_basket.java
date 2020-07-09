package com.example.bisan.UI.ModalActivities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bisan.DataTypes.MessageItem;
import com.example.bisan.Network.API_NewBasket;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.DataTypes.AddressItem;
import com.example.bisan.Network.API_GetAddress;
import com.example.bisan.Network.API_NewAddress;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.UI.Activities.Address_Act;
import com.example.bisan.UI.Activities.Map_Act;
import com.example.bisan.UI.Fragments.Basket_Fragment;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;
import com.example.bisan.Tools.database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Modal_basket extends AppCompatActivity implements ApplicationClass.API_Listener {

    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;
    private Button btnMap, btnAccept, btnCancel,btnPrev;
    private EditText name, phone;
    public static EditText address;
    private TextView lat_txt;
    private DBExcute db;
    private boolean hasEdited=false;
    private String Code,TAG_GET_ADDRESS="getAddress",TAG_SAVE_ADDRESS="saveAddress",lat="0",lang="0",address_id="0",order_id="";


    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.act_modal_basket);

        Initials_Objects();

        UI_Initial();

        GetData();
    }

    private void Initials_Objects() {
        try {

            loadingHandler = new LoadingHandler(Modal_basket.this);
            connectionFailHandler = new ConnectionFailHandler(Modal_basket.this);
            connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetData();
                }
            });

            btnAccept = (Button) findViewById(R.id.modal_basket_btnAccept);
            btnCancel = (Button) findViewById(R.id.modal_basket_btnDelete);
            btnMap = (Button) findViewById(R.id.modal_basket_btnMap);
            name = (EditText) findViewById(R.id.modal_basket_name);
            address = (EditText) findViewById(R.id.modal_basket_address);
            phone = (EditText) findViewById(R.id.modal_basket_phone);
            btnPrev=(Button)findViewById(R.id.modal_basket_btnprev);
            lat_txt=(TextView)findViewById(R.id.modal_basket_lat);

            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    hasEdited=true;
                }
            });
            phone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    hasEdited=true;
                }
            });
            address.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    hasEdited=true;
                }
            });

            db=DBExcute.getInstance(this);
            db.Open();

            Bundle bundle=getIntent().getExtras();
            if (bundle==null)finish();

            Code=bundle.getString("Code");
//            if(Code.compareToIgnoreCase("okNot")!=0){
                order_id=bundle.getString("order_id");
//            }


        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.Close();
    }

    private void UI_Initial() {
        try {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name.setError(null);
                    address.setError(null);
                    phone.setError(null);
                    if (name.getText().toString().length() > 0 && address.getText().toString().length() > 0 && phone.getText().toString().length() > 0) {
                        if(hasEdited) {
                            SaveData();
                        }else{
                            SaveBasket();
                        }
                    } else {
                        if (name.getText().toString().length() <= 0)
                            name.setError("لطفا نام را وارد کنید");
                        if (address.getText().toString().length() <= 0)
                            address.setError("لطفا آدرس را وارد کنید");
                        if (phone.getText().toString().length() <= 0)
                            phone.setError("لطفا تلفن را وارد کنید");
                    }
                }
            });

            btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Modal_basket.this, Map_Act.class);
                    intent.putExtra("lat",lat);
                    intent.putExtra("lang",lang);
                    startActivityForResult(intent,10);
                }
            });

            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent=new Intent(Modal_basket.this, Address_Act.class);
                        startActivityForResult(intent,11);
                    }catch (Exception e){}
                }
            });

        } catch (Exception e) {
        }
    }

    private void GetData() {
        try {
            if(loadingHandler!=null)loadingHandler.ShowLoading();
            if(connectionFailHandler!=null)
                if(connectionFailHandler.isVisible())connectionFailHandler.Hide();

            API_GetAddress getAddress=new API_GetAddress(Modal_basket.this);
            getAddress.in_userID.setVal(db.Read(database.QRY_GET_USER_INFO,null).GetField(6));

            getAddress.sendRequest(Modal_basket.this,TAG_GET_ADDRESS,getAddress.in_userID);

        } catch (Exception e) {
        }
    }

    private void SaveData(){
        try{
            if(loadingHandler!=null)loadingHandler.ShowLoading();
            if(connectionFailHandler!=null)
                if(connectionFailHandler.isVisible())connectionFailHandler.Hide();

            API_NewAddress newAddress=new API_NewAddress(Modal_basket.this);
            JSONObject object=new JSONObject();
            object.put(newAddress.in_FkUserId.getParamName(),Integer.valueOf(db.Read(database.QRY_GET_USER_INFO,null).GetField(6)));

            object.put(newAddress.in_NameReceiver.getParamName(),name.getText().toString());
            object.put(newAddress.in_Address.getParamName(),address.getText().toString());
            object.put(newAddress.in_TelReceiver.getParamName(),phone.getText().toString());
            if(lat!=null && lat.length()>0 && lat.compareToIgnoreCase("null")!=0)object.put(newAddress.in_Lat.getParamName(),lat);
            if(lang!=null && lang.length()>0 && lang.compareToIgnoreCase("null")!=0)object.put(newAddress.in_Lang.getParamName(),lang);
            object.put(newAddress.in_id.getParamName(),address_id);

            object.put(newAddress.in_OrderID.getParamName(),Integer.valueOf(order_id));

            Log.d("Address",object.toString());
            newAddress.sendRequest(Modal_basket.this,TAG_SAVE_ADDRESS,object);
        }catch (Exception e){
            Log.d("err",e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bundle bundle = data.getExtras();
            if (bundle != null && resultCode==10) {
                lat=bundle.getString("lat");
                lang=bundle.getString("lang");
                hasEdited=true;
                if(lat!=null && lat!="null" ){
                    lat_txt.setText(lat+" , "+lang);
                }
                Toast.makeText(Modal_basket.this,"موقعیت شما دریافت گردید",Toast.LENGTH_LONG).show();
            }
            if (bundle != null && resultCode==11) {
                address_id=bundle.getString("id");
                name.setText(bundle.getString("name"));
                phone.setText(bundle.getString("phone"));
                address.setText(bundle.getString("address"));
                lat=bundle.getString("lat");
                lang=bundle.getString("lang");
                hasEdited=true;
                if(lat!=null && lat!="null" ){
                    lat_txt.setText(lat+" , "+lang);
                }
                Toast.makeText(Modal_basket.this,"موقعیت شما دریافت گردید",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        if (loadingHandler != null) loadingHandler.HideLoading();
        if (hasError) {

            //If No Internet Connection Found
            if (Answer.equals(ServerRequest.NO_CONNECTION))
                if (connectionFailHandler != null)
                    if (!connectionFailHandler.isVisible()) connectionFailHandler.Show();
        } else {

            if(Tag.compareToIgnoreCase(TAG_SAVE_ADDRESS)==0){
                try {
                    ArrayList<MessageItem> item = ApplicationClass.MessageItem(new JSONObject(Answer));
                    if (item.get(0).status) {
//                       if(item.get(0).Redirect.indexOf("ok")>=0){
//                           Code="ok";
//                       }else{
//                           Code="okNot";
//                       }

                        SaveBasket();
                    }
                }catch (Exception e){}
            }

            if(Tag.compareToIgnoreCase(TAG_GET_ADDRESS)==0){
                ArrayList<AddressItem> arrayList=new ArrayList<>();
                arrayList.addAll(Items);

                address_id=(arrayList.get(0).id);
                name.setText(arrayList.get(0).name);
                address.setText(arrayList.get(0).address);
                phone.setText(arrayList.get(0).phone);
                lat=arrayList.get(0).lat;
                lang=arrayList.get(0).lang;

                if(lat!=null && lat!="null" ){
                    lat_txt.setText(lat+" , "+lang);
                }
//                hasEdited=false;

            }
        }
    }

    public void SaveBasket(){
        try{
            if(Code.compareToIgnoreCase("okNot")==0){
                //--> Open Payment GateWay
                String price="0";
                try{
                    price=db.Read(database.QRY_GET_SETTING_INFO,null).GetField(3);
                }catch (Exception e){}

                ApplicationClass.pay(Modal_basket.this,Integer.valueOf(price));
                this.finish();

            }else{
                //--> Pay by Wallet. go to Paydone to Complete
                Intent intent=new Intent(Modal_basket.this,Modal_paydone.class);
                intent.putExtra("pay","ok");
                startActivity(intent);
                finish();
            }
        }catch (Exception e){}
    }


}
