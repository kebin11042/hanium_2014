package firstbig.com.example.first_big;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 짹짹 on 2014-08-01.
 */
public class LogInActivity extends Activity{

    private LOGIN task_Login;

    private TextView txtvWattcher;

    private EditText edtxLogInID;
    private EditText edtxLogInPW;

    private Button btnLogIn;
    private Button btnJoin;

    private String URL_Login;

    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

        Init();
    }

    public void Init(){

        txtvWattcher = (TextView) findViewById(R.id.txtvWattcher);
        txtvWattcher.setTypeface(mTypeface);

        edtxLogInID = (EditText) findViewById(R.id.edtxLoginID);
        edtxLogInPW = (EditText) findViewById(R.id.edtxLoginPW);

        btnLogIn = (Button) findViewById(R.id.btnLogin);
        btnLogIn.setTypeface(mTypeface);
        btnJoin = (Button) findViewById(R.id.btnJoin);
        btnJoin.setTypeface(mTypeface);

        btnLogIn.startAnimation(AnimationUtils.loadAnimation(LogInActivity.this, R.anim.chan));

        URL_Login = "http://kebin1104.dothome.co.kr/BIG/Login.php";
    }

    public void mOnClick(View v){

        Intent intent = null;

        if(v.getId() == R.id.btnLogin){

            if(EmptyCheck()){
              // 비어있다
                Toast.makeText(getBaseContext(), "Blank Error", Toast.LENGTH_SHORT).show();

                //Drawable Old = new ColorDrawable(Color.parseColor("#E177ED"));
                //Drawable New = new ColorDrawable(Color.parseColor("#83ED77"));

                //TransitionDrawable td = new TransitionDrawable(new Drawable[] {Old, New});
                //btnJoin.setBackgroundDrawable(td);
                //td.startTransition(2000);

            }
            else{
                task_Login = new LOGIN(LogInActivity.this);
                task_Login.execute(URL_Login);
            }
        }
        else if(v.getId() == R.id.btnJoin){
            intent = new Intent(LogInActivity.this, JoinActivity.class);
            startActivity(intent);
        }

    }

    // 로그인할 EidtText에 빈칸인지 체크해주는 메소드
    // 둘중 하나라도 비였으면 true 반환
    public boolean EmptyCheck(){
        return (edtxLogInID.getText().toString().equals("") || edtxLogInPW.getText().toString().equals(""));
    }


    private class LOGIN extends AsyncTask<String, Integer, String> {

        private Context context;

        private ProgressDialog mProgressdlg;

        private final String strOK = "Login Success";
        private final String strIDerr = "ID error";
        private final String strPWerr = "PW error";

        private String pk;  //로그인한 아이디의 pk
        private String goal;    //로그인한 아이디의 목표금액
        private String Import;  //로그인한 아이디의 수입

        public LOGIN(Context context) {
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mProgressdlg = new ProgressDialog(context);
            mProgressdlg.setMessage("로그인 중...");
            mProgressdlg.setCancelable(false);
            mProgressdlg.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            // TODO Auto-generated method stub
            String strResponse = "";

            try {
                //URL 설정
                URL url = new URL(urls[0]);
                //접속
                HttpURLConnection http = (HttpURLConnection) url.openConnection();

                if(http != null){	//연결이 제대로 되었다면

                    //전송 모드 설정
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setDoOutput(true);
                    http.setRequestMethod("POST");	//전송방식을 POST

                    //서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
                    http.setRequestProperty("Accept", "application/xml");

                    //--------------------------
                    //  서버로 값 전송
                    //--------------------------

                    StringBuffer buffer = new StringBuffer();
                    String ID = edtxLogInID.getText().toString();
                    String PW = edtxLogInPW.getText().toString();

                    buffer.append("ID=").append(ID + "&");
                    buffer.append("PW=").append(PW);

                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();

                    //--------------------------
                    //  서버에서 값 전송 받기
                    //--------------------------

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader br = new BufferedReader(tmp);

                    strResponse = br.readLine();
                    pk = br.readLine();
                    goal = br.readLine();
                    Import = br.readLine();

                    br.close();
                }

                http.disconnect();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return strResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            //로그인 성공시
            if(result.equals(strOK)){
                Intent intent_LogInOK = new Intent(LogInActivity.this, TabActivity.class);
                intent_LogInOK.putExtra("pk", pk);
                intent_LogInOK.putExtra("Goal", goal);
                intent_LogInOK.putExtra("Import", Import);
                startActivity(intent_LogInOK);
                finish();
            }
            //ID가 없음
            else if(result.equals(strIDerr)){
                Toast.makeText(context, "ID가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
            }
            //PW 불일치
            else if(result.equals(strPWerr)){
                Toast.makeText(context, "비밀번호 불일치", Toast.LENGTH_SHORT).show();
            }

            mProgressdlg.dismiss();
        }
    }
}
