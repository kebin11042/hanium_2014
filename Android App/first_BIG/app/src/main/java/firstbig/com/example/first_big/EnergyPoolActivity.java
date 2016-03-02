package firstbig.com.example.first_big;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by 짹짹 on 2014-09-25.
 */
public class EnergyPoolActivity extends Activity {

    private TextView txtvMonthMoney;
    private TextView txtvImport;
    private TextView txtvImport10pro;
    private TextView txtvEnergyTarget;

    private ImageButton btnPoolImage;

    private int MonthMoney;
    private int Import;
    private int Import10pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energypool);

        setActionBar();
        Init();
    }

    public void setActionBar(){
        View vAction = getLayoutInflater().inflate(R.layout.actionbar_payinfo, null);
        ActionBar mActionBar = getActionBar();
        mActionBar.setCustomView(vAction, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT
        ));

        TextView txtvAction = (TextView) vAction.findViewById(R.id.txtvActionbarPayInfoTitle);
        txtvAction.setText("에너지 빈곤층");

        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    public void Init(){
        Intent intent = getIntent();
        MonthMoney = intent.getExtras().getInt("MonthMoney");
        Import = Integer.parseInt(intent.getExtras().getString("Import"));
        Import10pro = (int)((double)Import * 10.0 / 100.0);

        txtvMonthMoney = (TextView) findViewById(R.id.txtvPoolMonthMoney);
        txtvImport = (TextView) findViewById(R.id.txtvPoolImport);
        txtvImport10pro = (TextView) findViewById(R.id.txtvPoolImport10pro);
        txtvEnergyTarget = (TextView) findViewById(R.id.txtvPoolTarget);

        btnPoolImage = (ImageButton) findViewById(R.id.btnPoolImage);

        txtvMonthMoney.setText(String.format("%,d 원", MonthMoney));
        txtvImport.setText(String.format("%,d 원", Import));
        txtvImport10pro.setText(String.format("%,d 원", Import10pro));

        int ICONS[] = new int[] {R.drawable.happy, R.drawable.danger, R.drawable.sad};

        if(MonthMoney >= Import10pro){
            btnPoolImage.setImageResource(ICONS[2]);
            txtvEnergyTarget.setText("에너지 빈곤층 대상자 입니다.");
        }
        else if(MonthMoney >= Import10pro * 0.9){
            btnPoolImage.setImageResource(ICONS[1]);
            txtvEnergyTarget.setText("에너지 빈곤층 위험 대상자 입니다.");
        }
        else{
            btnPoolImage.setImageResource(ICONS[0]);
            txtvEnergyTarget.setText("에너지 빈곤층 대상자가 아닙니다.");
        }
    }

    public void mOnClick(View view){
        finish();
    }
}
