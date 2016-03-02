package firstbig.com.example.first_big;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 짹짹 on 2014-09-16.
 */
public class PayInfoActivity extends Activity{

    private double Elec_Data;
    private int PayElec;
    private int PayPlus;
    private int PayIndustry;
    private int PayTotal;
    private int Min;

    private LinearLayout layPayLevel;

    private TextView txtvActionbarPayInfoTitle;
    private TextView txtvPayTitle;
    private TextView txtvPayBasic;
    private TextView txtvPayElec;
    private TextView txtvPayLevel1;
    private TextView txtvPayLevel2;
    private TextView txtvPayLevel3;
    private TextView txtvPayLevel4;
    private TextView txtvPayLevel5;
    private TextView txtvPayLevel6;
    private TextView txtvPayBasicElec;
    private TextView txtvPayPlus;
    private TextView txtvPayIndustry;
    private TextView txtvPayTotal;
    private TextView txtvPayTotal2;

    private CALMONEY mCalMoney;

    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payinfo);

        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

        Init();
    }

    public void Init(){
        Intent intent = getIntent();
        Elec_Data = intent.getExtras().getDouble("Elec_Data");

        mCalMoney = new CALMONEY();

        View vAction = getLayoutInflater().inflate(R.layout.actionbar_payinfo, null);
        ActionBar mActionBar = getActionBar();
        mActionBar.setCustomView(vAction, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT
        ));
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        layPayLevel = (LinearLayout) findViewById(R.id.layPayLevel);

        txtvActionbarPayInfoTitle = (TextView) vAction.findViewById(R.id.txtvActionbarPayInfoTitle);
        txtvActionbarPayInfoTitle.setTypeface(mTypeface);
        txtvPayTitle = (TextView) findViewById(R.id.txtvPayTitle);
        txtvPayBasic = (TextView) findViewById(R.id.txtvPayBasic);
        txtvPayElec = (TextView) findViewById(R.id.txtvPayElec);
        txtvPayLevel1 = (TextView) findViewById(R.id.txtvPayLevel1);
        txtvPayLevel2 = (TextView) findViewById(R.id.txtvPayLevel2);
        txtvPayLevel3 = (TextView) findViewById(R.id.txtvPayLevel3);
        txtvPayLevel4 = (TextView) findViewById(R.id.txtvPayLevel4);
        txtvPayLevel5 = (TextView) findViewById(R.id.txtvPayLevel5);
        txtvPayLevel6 = (TextView) findViewById(R.id.txtvPayLevel6);
        txtvPayBasicElec = (TextView) findViewById(R.id.txtvPayBasicElec);
        txtvPayPlus = (TextView) findViewById(R.id.txtvPayPlus);
        txtvPayIndustry = (TextView) findViewById(R.id.txtvPayIndustry);
        txtvPayTotal = (TextView) findViewById(R.id.txtvPayTotal);
        txtvPayTotal2 = (TextView) findViewById(R.id.txtvPayTotal2);

        setPayTitle();
        setPayBasic();
        setPayElec();
        setPayBasicElec();
        setPayPlus();
        setPayIndustry();
        setPayTotal();
        setPayTotal2();
    }

    public void setPayTitle(){
        txtvPayTitle.setText("월간 " + (int)Elec_Data + "kwh 사용시 전기요금 계산");
    }

    public void setPayBasic(){
        txtvPayBasic.setText(String.format("%,d 원", (int)mCalMoney.getBasicMoney(Elec_Data)));
    }

    public void setPayElec(){

        int count = (int) (Elec_Data / 100.0);

        //100kwh 미만으로 쓴거임
        if(count < 1){
            layPayLevel.removeView(txtvPayLevel2);
            layPayLevel.removeView(txtvPayLevel3);
            layPayLevel.removeView(txtvPayLevel4);
            layPayLevel.removeView(txtvPayLevel5);
            layPayLevel.removeView(txtvPayLevel6);

            double Level1 = Elec_Data * mCalMoney.getPay(0);
            txtvPayLevel1.setText(String.format("1단계 : %dkWh * %.1f원 = %,d원", (int)Elec_Data, mCalMoney.getPay(0), (int)Level1));

            PayElec = (int)Math.floor(Level1);
        }
        //200kwh 미만
        else if(count < 2){
            layPayLevel.removeView(txtvPayLevel3);
            layPayLevel.removeView(txtvPayLevel4);
            layPayLevel.removeView(txtvPayLevel5);
            layPayLevel.removeView(txtvPayLevel6);

            double Level1 = 100.0 * mCalMoney.getPay(0);
            txtvPayLevel1.setText(String.format("1단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(0), (int)Level1));

            double Level2 = (Elec_Data - 100.0) * mCalMoney.getPay(1);
            txtvPayLevel2.setText(String.format("2단계 : %dkWh * %.1f원 = %,d원", (int)(Elec_Data - 100.0), mCalMoney.getPay(1), (int)Level2));

            PayElec = (int)Math.floor(Level1) + (int)Math.floor(Level2);
        }
        //300kwh 미만
        else if(count < 3){
            layPayLevel.removeView(txtvPayLevel4);
            layPayLevel.removeView(txtvPayLevel5);
            layPayLevel.removeView(txtvPayLevel6);

            double Level1 = 100.0 * mCalMoney.getPay(0);
            txtvPayLevel1.setText(String.format("1단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(0), (int)Level1));

            double Level2 = 100.0 * mCalMoney.getPay(1);
            txtvPayLevel2.setText(String.format("2단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(1), (int)Level2));

            double Level3 = (Elec_Data - 200.0) * mCalMoney.getPay(2);
            txtvPayLevel3.setText(String.format("3단계 : %dkWh * %.1f원 = %,d원", (int)(Elec_Data - 200.0), mCalMoney.getPay(2), (int)Level3));

            PayElec = (int)Math.floor(Level1) + (int)Math.floor(Level2) + (int)Math.floor(Level3);
        }
        //400kwh 미만
        else if(count < 4){
            layPayLevel.removeView(txtvPayLevel5);
            layPayLevel.removeView(txtvPayLevel6);

            double Level1 = 100.0 * mCalMoney.getPay(0);
            txtvPayLevel1.setText(String.format("1단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(0), (int)Level1));

            double Level2 = 100.0 * mCalMoney.getPay(1);
            txtvPayLevel2.setText(String.format("2단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(1), (int)Level2));

            double Level3 = 100.0 * mCalMoney.getPay(2);
            txtvPayLevel3.setText(String.format("3단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(2), (int)Level3));

            double Level4 = (Elec_Data - 300.0) * mCalMoney.getPay(3);
            txtvPayLevel4.setText(String.format("4단계 : %dkWh * %.1f원 = %,d원", (int)(Elec_Data - 300.0), mCalMoney.getPay(3), (int)Level4));

            PayElec = (int)Math.floor(Level1) + (int)Math.floor(Level2) + (int)Math.floor(Level3) + (int)Math.floor(Level4);
        }
        //500kwh 미만
        else if(count < 5) {
            layPayLevel.removeView(txtvPayLevel6);

            double Level1 = 100.0 * mCalMoney.getPay(0);
            txtvPayLevel1.setText(String.format("1단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(0), (int)Level1));

            double Level2 = 100.0 * mCalMoney.getPay(1);
            txtvPayLevel2.setText(String.format("2단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(1), (int)Level2));

            double Level3 = 100.0 * mCalMoney.getPay(2);
            txtvPayLevel3.setText(String.format("3단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(2), (int)Level3));

            double Level4 = 100.0 * mCalMoney.getPay(3);
            txtvPayLevel4.setText(String.format("4단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(3), (int)Level4));

            double Level5 = (Elec_Data - 400.0) * mCalMoney.getPay(4);
            txtvPayLevel5.setText(String.format("5단계 : %dkWh * %.1f원 = %,d원", (int)(Elec_Data - 400.0), mCalMoney.getPay(4), (int)Level5));

            PayElec = (int)Math.floor(Level1) + (int)Math.floor(Level2) + (int)Math.floor(Level3) + (int)Math.floor(Level4) + (int)Math.floor(Level5);
        }
        else{
            double Level1 = 100.0 * mCalMoney.getPay(0);
            txtvPayLevel1.setText(String.format("1단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(0), (int)Level1));

            double Level2 = 100.0 * mCalMoney.getPay(1);
            txtvPayLevel2.setText(String.format("2단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(1), (int)Level2));

            double Level3 = 100.0 * mCalMoney.getPay(2);
            txtvPayLevel3.setText(String.format("3단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(2), (int)Level3));

            double Level4 = 100.0 * mCalMoney.getPay(3);
            txtvPayLevel4.setText(String.format("4단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(3), (int)Level4));

            double Level5 = 100.0 * mCalMoney.getPay(4);
            txtvPayLevel5.setText(String.format("5단계 : 100kWh * %.1f원 = %,d원", mCalMoney.getPay(4), (int)Level5));

            double Level6 = (Elec_Data - 500.0) * mCalMoney.getPay(5);
            txtvPayLevel6.setText(String.format("6단계 : %dkWh * %.1f원 = %,d원", (int)(Elec_Data - 500.0), mCalMoney.getPay(5), (int)Level6));

            PayElec = (int)Math.floor(Level1) + (int)Math.floor(Level2) + (int)Math.floor(Level3) + (int)Math.floor(Level4) + (int)Math.floor(Level5) + (int)Math.floor(Level6);
        }

        txtvPayElec.setText(String.format("%,d 원", PayElec));
    }

    public void setPayBasicElec(){
        int basic = (int)mCalMoney.getBasicMoney(Elec_Data);
        Min = basic + PayElec;

        if(Min < 1000){
            txtvPayBasicElec.setText(String.format(" 월 최저요금 = %,d원", 1000));
        }
        else{
            txtvPayBasicElec.setText(String.format(" : %,d원 + %,d원 = %,d원", basic, PayElec, Min));
        }

    }

    public void setPayPlus(){

        if(Min < 1000){
            PayPlus = 100;
            txtvPayPlus.setText(String.format(" : %,d원 * 0.1 = %,d원", 1000, PayPlus));
        }
        else {
            PayPlus = (int) Math.round(((int) mCalMoney.getBasicMoney(Elec_Data) + PayElec) * 0.1);
            txtvPayPlus.setText(String.format(" : %,d원 * 0.1 = %,d원", ((int) mCalMoney.getBasicMoney(Elec_Data) + PayElec), PayPlus));
        }
    }

    public void setPayIndustry(){

        if(Min < 1000){
            PayIndustry = 30;

            txtvPayIndustry.setText(String.format(" : %,d원 * 0.037 = %,d원", 1000, PayIndustry));
        }
        else{
            double industry = ((int)mCalMoney.getBasicMoney(Elec_Data) + PayElec) * 0.037;
            industry = Math.floor(industry / 10.0) * 10.0;
            PayIndustry = (int)industry;

            txtvPayIndustry.setText(String.format(" : %,d원 * 0.037 = %,d원", ((int)mCalMoney.getBasicMoney(Elec_Data) + PayElec), PayIndustry));
        }

    }

    public void setPayTotal(){

        if(Min < 1000){
            PayTotal = 1000 + PayPlus + PayIndustry;
            txtvPayTotal.setText(String.format(" : %,d원 + %,d원 + %,d원 = %,d원", 1000, PayPlus, PayIndustry, PayTotal));
        }
        else{
            double total = ((int)mCalMoney.getBasicMoney(Elec_Data) + PayElec) + PayPlus + PayIndustry;
            total = Math.floor(total / 10.0) * 10.0;
            PayTotal = (int)total;
            txtvPayTotal.setText(String.format(" : %,d원 + %,d원 + %,d원 = %,d원", ((int)mCalMoney.getBasicMoney(Elec_Data) + PayElec), PayPlus, PayIndustry, PayTotal));
        }

    }

    public void setPayTotal2(){
        txtvPayTotal2.setText(String.format("%,d", PayTotal));
    }

    public void mOnClick(View v){
        finish();
    }
}
