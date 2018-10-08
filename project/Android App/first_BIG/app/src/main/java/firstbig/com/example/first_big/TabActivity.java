package firstbig.com.example.first_big;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by 짹짹 on 2014-08-01.
 */
public class TabActivity extends FragmentActivity {

    private Display disp;

    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabs;
    private MyPagerAdapter myPagerAdapter;

    private View vAction;
    private TextView txtvActionbarTitle;

    private ImageButton btnBackToLogin;
    private ImageButton btnExit;

    private String Logined_pk;
    private String device_pk;
    private String Goal;
    private String Import;

    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab);

        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

        Init();
    }

    public void SetActionBar(){
        //Drawable drawable = new ColorDrawable(Color.parseColor("#76CCFF"));
        //getActionBar().setBackgroundDrawable(drawable);

        vAction = getLayoutInflater().inflate(R.layout.actionbar_tab, null);

        btnBackToLogin = (ImageButton) vAction.findViewById(R.id.btnBackToLogin);
        btnExit = (ImageButton) vAction.findViewById(R.id.btnExit);

        ActionBar mActionBar = getActionBar();

        mActionBar.setCustomView(vAction, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT
        ));

        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        txtvActionbarTitle = (TextView) vAction.findViewById(R.id.txtvActionbarTitle);
        txtvActionbarTitle.setTypeface(mTypeface);

        txtvActionbarTitle.setText("일별");
    }

    public void Init(){

        disp = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        //Toast.makeText(getBaseContext(), "" + disp.getHeight(), Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        Logined_pk = intent.getExtras().getString("pk");
        Goal = intent.getExtras().getString("Goal");
        Import = intent.getExtras().getString("Import");

        SetActionBar();

        DayActivity day = new DayActivity();

        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(myPagerAdapter);
        mTabs.setViewPager(mViewPager);
        mTabs.setDividerColor(Color.parseColor("#00ffffff"));

        LinearLayout tabscon = (LinearLayout) mTabs.getChildAt(0);
        for(int i=1;i<4;i++){
            ((ImageButton) tabscon.getChildAt(i)).getDrawable().setAlpha(102);
        }

        mTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                String[] title = new String[] {"일 별", "월 별", "년 별", "설정"};

                txtvActionbarTitle.setText(title[position]);

                LinearLayout tabscon = (LinearLayout) mTabs.getChildAt(0);

                if(position == 0){

                    //와 이거 진짜 미치네 ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ
                    //개 고생해서 알아냇음!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //LinearLayout tabscon = (LinearLayout) mTabs.getChildAt(0);
                    //((ImageButton) tabscon.getChildAt(0)).setImageResource(R.drawable.plug_tab_pressed);

                    ((ImageButton) tabscon.getChildAt(0)).getDrawable().setAlpha(255);
                    ((ImageButton) tabscon.getChildAt(1)).getDrawable().setAlpha(102);
                    ((ImageButton) tabscon.getChildAt(2)).getDrawable().setAlpha(102);
                    ((ImageButton) tabscon.getChildAt(3)).getDrawable().setAlpha(102);
                }
                else if(position == 1){
                    ((ImageButton) tabscon.getChildAt(0)).getDrawable().setAlpha(102);
                    ((ImageButton) tabscon.getChildAt(1)).getDrawable().setAlpha(255);
                    ((ImageButton) tabscon.getChildAt(2)).getDrawable().setAlpha(102);
                    ((ImageButton) tabscon.getChildAt(3)).getDrawable().setAlpha(102);
                }
                else if(position == 2){
                    ((ImageButton) tabscon.getChildAt(0)).getDrawable().setAlpha(102);
                    ((ImageButton) tabscon.getChildAt(1)).getDrawable().setAlpha(102);
                    ((ImageButton) tabscon.getChildAt(2)).getDrawable().setAlpha(255);
                    ((ImageButton) tabscon.getChildAt(3)).getDrawable().setAlpha(102);
                }
                else if(position == 3){
                    ((ImageButton) tabscon.getChildAt(0)).getDrawable().setAlpha(102);
                    ((ImageButton) tabscon.getChildAt(1)).getDrawable().setAlpha(102);
                    ((ImageButton) tabscon.getChildAt(2)).getDrawable().setAlpha(102);
                    ((ImageButton) tabscon.getChildAt(3)).getDrawable().setAlpha(255);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void setGoal(String Goal){
        this.Goal = Goal;
    }

    public void mOnClick(View v){

        if(v.getId() == btnBackToLogin.getId()){
            final View dialogView = (View) View.inflate(TabActivity.this, R.layout.dialog_logout, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(TabActivity.this);
            builder.setCancelable(true);
            builder.setView(dialogView);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();

            // positive 버튼 오버라이딩
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TabActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_rectangle_whitetobrown));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_rectangle_whitetobrown));
        }
        else if(v.getId() == btnExit.getId()){
            final View dialogView = (View) View.inflate(TabActivity.this, R.layout.dialog_exit, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(TabActivity.this);
            builder.setCancelable(true);
            builder.setView(dialogView);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();

            // positive 버튼 오버라이딩
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_rectangle_whitetobrown));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_rectangle_whitetobrown));
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{

        final int[] ICONS = { R.drawable.disconnected, R.drawable.month, R.drawable.year, R.drawable.settings_pressed };
        final String[] Titles = { "일별", "월별", "년별", "설정" };

        public MyPagerAdapter(android.support.v4.app.FragmentManager fm){
            super(fm);
        }

        @Override
        public int getPageIconResId(int i) {
            return ICONS[i];
        }

        @Override
        public int getCount() {
            return ICONS.length;
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return DayActivity.newInstance(Logined_pk);
            }
            else if(position == 1){
                return MonthActivity.newInstance(Logined_pk);
            }
            else if(position == 2){
                return YearActivity.newInstance();
            }
            else{
                return MyInfoActivity.newInstance(Logined_pk);
            }
        }
    }
}
