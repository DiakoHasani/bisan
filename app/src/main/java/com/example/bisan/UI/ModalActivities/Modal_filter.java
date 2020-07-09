package com.example.bisan.UI.ModalActivities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bisan.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Modal_filter extends AppCompatActivity {

    private TextView txtPrice;
    private SeekBar seekPrice;
    private CheckBox checkBestSell,checkNewest,checkExpensive;
    private RadioButton radioName,radioPrice,radioCategory;
    private Button btnAccept,btnClear;

    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.act_modal_filter);

        Initials_Objects();

        UI_Initial();

    }

    private void Initials_Objects(){
        try{
            txtPrice=(TextView)findViewById(R.id.modal_filter_txtprice);
            btnAccept=(Button)findViewById(R.id.modal_filter_btnAccept);
            btnClear=(Button)findViewById(R.id.modal_filter_btnDelete);
            seekPrice=(SeekBar)findViewById(R.id.modal_filter_seek);

            checkBestSell=(CheckBox)findViewById(R.id.modal_filter_check3);
            checkExpensive=(CheckBox)findViewById(R.id.modal_filter_check1);
            checkNewest=(CheckBox)findViewById(R.id.modal_filter_check2);

            radioName=(RadioButton)findViewById(R.id.modal_filter_radioName);
            radioCategory=(RadioButton)findViewById(R.id.modal_filter_radioCategory);
            radioPrice=(RadioButton)findViewById(R.id.modal_filter_radioPrice);

        }catch (Exception e){}
    }

    private void UI_Initial(){
        try{
            seekPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    try{
                        txtPrice.setText(String.valueOf(progress));
                    }catch (Exception e){}
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendResult(false);
                }
            });
            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendResult(true);
                }
            });

            Bundle bundle=getIntent().getExtras();
            if(bundle==null){
                finish();
            }else{
                seekPrice.setProgress(Integer.valueOf(bundle.getString("maxPrcie")));
                radioPrice.setChecked(Boolean.parseBoolean(bundle.getString("order_price")));
                radioCategory.setChecked(Boolean.parseBoolean(bundle.getString("order_category")));
                radioName.setChecked(Boolean.parseBoolean(bundle.getString("order_name")));

                checkBestSell.setChecked(Boolean.parseBoolean(bundle.getString("bestsell")));
                checkExpensive.setChecked(Boolean.parseBoolean(bundle.getString("expensive")));
                checkNewest.setChecked(Boolean.parseBoolean(bundle.getString("newest")));
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SendResult(boolean isClear){
        try{
            Intent intent=new Intent();
            if(isClear){
                intent.putExtra("maxPrcie", "0");
                intent.putExtra("order_price", "false");
                intent.putExtra("order_category", "false");
                intent.putExtra("order_name", "true");
                intent.putExtra("bestsell", "true");
                intent.putExtra("expensive", "false");
                intent.putExtra("newest", "false");
            }else {
                intent.putExtra("maxPrcie", String.valueOf(seekPrice.getProgress()));
                intent.putExtra("order_price", String.valueOf(radioPrice.isChecked()));
                intent.putExtra("order_category", String.valueOf(radioCategory.isChecked()));
                intent.putExtra("order_name", String.valueOf(radioName.isChecked()));
                intent.putExtra("bestsell", String.valueOf(checkBestSell.isChecked()));
                intent.putExtra("expensive", String.valueOf(checkExpensive.isChecked()));
                intent.putExtra("newest", String.valueOf(checkNewest.isChecked()));
            }
            intent.putExtra("isClear",String.valueOf(isClear));
            setResult(1010,intent);
            finish();
        }catch (Exception e){}
    }
}
