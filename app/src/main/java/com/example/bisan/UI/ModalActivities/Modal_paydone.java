package com.example.bisan.UI.ModalActivities;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bisan.DataTypes.MessageItem;
import com.example.bisan.Tools.ApplicationClass;
import com.example.bisan.Tools.DBExcute;
import com.example.bisan.Tools.FarsiUtil;
import com.example.bisan.Network.API_BasketConfirmation;
import com.example.bisan.Network.ServerRequest;
import com.example.bisan.R;
import com.example.bisan.Tools.CToast;
import com.example.bisan.UI.Dialogs.CustomDialog;
import com.example.bisan.UI.UIHandlers.ConnectionFailHandler;
import com.example.bisan.UI.UIHandlers.LoadingHandler;
import com.example.bisan.Tools.database;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Modal_paydone extends AppCompatActivity implements ApplicationClass.API_Listener {

    private LoadingHandler loadingHandler;
    private ConnectionFailHandler connectionFailHandler;
    private TextView code,status;
    private Button btn;
    private boolean isPay=false,resendPayment=false,canClose=false,answerDelivered=true;
    private DBExcute db;
    private String TAG_CONFIRM="basketConfirm";

    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_modal_paydone);

        Initials_Objects();

        UI_Initial();

    }

    private  void Initials_Objects(){
        try{
            code=(TextView)findViewById(R.id.modal_paydone_code);
            status=(TextView)findViewById(R.id.modal_paydone_status);
            btn=(Button)findViewById(R.id.modal_paydone_btn);

            loadingHandler = new LoadingHandler(Modal_paydone.this);
            connectionFailHandler = new ConnectionFailHandler(Modal_paydone.this);
            connectionFailHandler.setOnRetryClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connectionFailHandler.Hide();
                }
            });
        }catch (Exception e){}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.Close();
    }

    private void UI_Initial(){
        try{
            db=DBExcute.getInstance(this);
            db.Open();

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(isPay){
                            if(canClose){
                                finish();
                            }else{
                                PayConfirmation();
                                new CToast(Modal_paydone.this,"لطفا صبر کنید",1,CToast.LONG_DURATION).Show();
                            }
                        }else{
                            if(resendPayment){
                                final CustomDialog dialog=new CustomDialog(Modal_paydone.this,"خروج","آیا مایل هستید بدون تکمیل پرداخت خارج شوید؟");
                                dialog.setOnclick(new CustomDialog.Onclick() {
                                    @Override
                                    public void onAcceptClick() {
                                        Modal_paydone.this.finish();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelClick() {
                                        ResendPayment();
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }else{
                                finish();
                            }
                        }
                    }catch (Exception e){}
                }
            });

            Bundle bundle=getIntent().getExtras();
            if(bundle!=null){
                if(bundle.getString("pay")!=null){
                    status.setText("پرداخت شد");
                    btn.setText("بستن");
                    isPay=true;
                    code.setText("کیف پول");
                }
            }

            final Uri data = getIntent().getData();
            //--> ZarinPal Payment Listener
            ZarinPal.getPurchase(this).verificationPayment(data, new OnCallbackVerificationPaymentListener() {
                @Override
                public void onCallbackResultVerificationPayment(boolean isPaymentSuccess, String refID, PaymentRequest paymentRequest) {
                    try {
                        String price=String.valueOf(paymentRequest.getAmount());
                        String desc=String.valueOf(paymentRequest.getDescription());

                        //--> If Payment was Succeed
                        if (isPaymentSuccess) {

                            //--> Show Success Alert to User and Send Informations to Server
                            try{
                                //--> Check price and userid is equal code
                                String UserID=db.Read(database.QRY_GET_USER_INFO,null).GetField(6);
                                String shCode=db.Read(database.QRY_GET_SETTING_INFO,null).GetField(2);
                                String temp= FarsiUtil.EncodeBase64((price));
                                temp=temp.replaceAll("\n", "");;
                                if(shCode.compareTo(temp)==0){
                                    btn.setText("صبر کنید");
                                    status.setText("پرداخت شد");
                                    code.setText(refID);
                                    isPay=true;
                                    resendPayment=false;
                                    canClose=false;
                                    PayConfirmation();
                                }else{
                                    canClose=true;
                                    isPay=false;
                                    btn.setText("تلاش مجدد");
                                    status.setText("خطای امنیتی");
                                    code.setText(refID);
                                    resendPayment=false;
                                }
                            }catch (Exception e){}

                        } else {

                            //--> Show Failure Alert to User
                            new CToast(Modal_paydone.this,"خطا در پرداخت",1,CToast.LONG_DURATION).Show();
                            isPay=false;
                            resendPayment=true;
                            canClose=true;
                            btn.setText("تلاش مجدد");
                            status.setText("خطا");
                            code.setText("---");

                        }

                    }catch (Exception e){}
                }
            });
        }catch (Exception e){}
    }

    private void PayConfirmation(){
        try{
            if(!answerDelivered)return;
            if(loadingHandler!=null)loadingHandler.ShowLoading();
            if(connectionFailHandler!=null)
                if(connectionFailHandler.isVisible())connectionFailHandler.Hide();

            answerDelivered=true;

            API_BasketConfirmation basketConfirmation=new API_BasketConfirmation(Modal_paydone.this);
            basketConfirmation.in_id.setVal(db.Read(database.QRY_GET_USER_INFO,null).GetField(6));
            basketConfirmation.in_msg.setVal( FarsiUtil.SHA1((db.Read(database.QRY_GET_SETTING_INFO,null).GetField(3)+db.Read(database.QRY_GET_USER_INFO,null).GetField(6))));

            basketConfirmation.sendRequest(Modal_paydone.this,TAG_CONFIRM,basketConfirmation.in_id,basketConfirmation.in_msg);

        }catch (Exception e){}
    }

    private void ResendPayment(){
        try{

            String price="0";
            price=db.Read(database.QRY_GET_SETTING_INFO,null).GetField(3);
            ApplicationClass.pay(Modal_paydone.this,Integer.valueOf(price));
            this.finish();
        }catch (Exception e){}
    }


    @Override
    public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items, boolean hasError) {
        if (loadingHandler != null) loadingHandler.HideLoading();
        if (hasError) {

            if(Tag.compareToIgnoreCase(TAG_CONFIRM)==0){
                new CToast(Modal_paydone.this,"لطفا دوباره تلاش کنید",1,CToast.LONG_DURATION).Show();
                btn.setText("تلاش مجدد");
                answerDelivered=true;
            }

            //If No Internet Connection Found
            if (Answer.equals(ServerRequest.NO_CONNECTION))
                if (connectionFailHandler != null)
                    if (!connectionFailHandler.isVisible()) connectionFailHandler.Show();

        }else{
            if(Tag.compareToIgnoreCase(TAG_CONFIRM)==0){
                answerDelivered=true;
                ArrayList<MessageItem> item=new ArrayList<>();
                item.addAll(Items);

                boolean ans=item.get(0).status;
                if(ans){
                    try{
                        db.Execute(database.QRY_DELETE_BASKET_INFO,null);
                    }catch (Exception e){}
                    btn.setText("بستن");
                    canClose=true;
                }else{
                    canClose=false;
                    btn.setText("تایید خرید");
//                    PayConfirmation();
                }
            }
        }
    }
}
