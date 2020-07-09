package com.example.bisan.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bisan.DataTypes.MessageItem;
import com.example.bisan.R;
import com.example.bisan.Tools.CToast;
import com.flurry.android.FlurryAgent;
import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ApplicationClass extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        //--> We Define Application Primary Font Path.
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "YD9SDW3P6WS8M8VSHVQC");

    }

    public static void LoadImage(final Context context, final String url, final ImageView img){
        try{
//
//            Thread thread=new Thread(new Runnable() {
//                @Override
//                public void run() {
                    Glide.with(context)
                            .load(url)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img);
//                }
//            });
//            thread.run();

        }catch (Exception e){}
    }

    public static void Vibration(Context context){
        try{
            Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            vibe.vibrate(100);

        }catch (Exception e){}
    }

    public static void pay(final Activity context, int amount){
        /* Prepare a Payment object with custom data.
            The standard form we recommend is you create a constant value including your merchantID, and for
            anything you want to sell create a unique payment object (instead of editing your current object times and times)
            and pass the variable holding merchantID, this helps you edit your merchantID safe and easy anytime you wanted.
        */
        try {

            PaymentRequest paymentRequest = ZarinPal.getPaymentRequest();
            ZarinPal zarinPal = ZarinPal.getPurchase(context);

            //--> Zarin Pal MerchentID
            paymentRequest.setMerchantID("5385ee56-b230-4b81-9470-40935bef37d4");
            paymentRequest.setAmount(amount);
            paymentRequest.setDescription("PayBisan");

            //--> CallBack URL After ZArin Pal Payment Returns
            paymentRequest.setCallbackURL("zarinpayment://bisaan");

            zarinPal.startPayment(paymentRequest, new OnCallbackRequestPaymentListener() {
                @Override
                public void onCallbackResultPaymentRequest(int status, String authority, Uri paymentGatewayUri, Intent intent) {
                    if (status == 100) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        context.startActivity(intent);
                        context.finish();
                    } else {
                        //TODO Failure Authority !
                        new CToast(context,"خطا در اتصال به درگاه بانکی",1,CToast.LONG_DURATION).show();
                    }
                }
            });
        }catch (Exception e){
        }

    }

    public static ArrayList<MessageItem> MessageItem(JSONObject obj){
        MessageItem item=new MessageItem();
        try{
            item.Code=obj.getString("Code");
            item.Redirect=obj.getString("Redirect");
            item.Titel=obj.getString("Titel");
            item.status=Boolean.parseBoolean(obj.getString("Status"));
        }catch (Exception e){}
        ArrayList<MessageItem> arrayList=new ArrayList<>();
        arrayList.add(item);
        return arrayList;
    }


    public interface API_Listener{
        public void onSuccess(String Tag, String Answer, @Nullable ArrayList Items,boolean hasError);
    }

    public static String GetCurrentDateTime(){
        String ans="";
        try{
            Calendar calendar=Calendar.getInstance();
            ans=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
        }catch (Exception e){}
        return ans;
    }

}
