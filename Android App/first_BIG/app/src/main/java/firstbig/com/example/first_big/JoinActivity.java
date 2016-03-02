package firstbig.com.example.first_big;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
public class JoinActivity extends Activity {

    private Button btnJoinOK;
    private Button btnBack;

    private EditText edtxID;
    private EditText edtxName;
    private EditText edtxPW;
    private EditText edtxPwCon;

    private TextView txtvJoinTitle;
    private TextView txtvPwCon;

    private JOINMEMBER task_Join;

    private String URL_Join;

    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join);

        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

        Init();
    }

    public void Init(){

        btnJoinOK = (Button) findViewById(R.id.btnJoinOK);
        btnJoinOK.setTypeface(mTypeface);
        btnBack = (Button) findViewById(R.id.btnJoinBack);
        btnBack.setTypeface(mTypeface);

        edtxID = (EditText) findViewById(R.id.edtxJoinID);
        edtxName = (EditText) findViewById(R.id.edtxJoinName);
        edtxPW = (EditText) findViewById(R.id.edtxJoinPW);
        edtxPwCon = (EditText) findViewById(R.id.edtxJoinPwCon);

        txtvJoinTitle = (TextView) findViewById(R.id.txtvJoinTitle);
        txtvJoinTitle.setTypeface(mTypeface);
        txtvPwCon = (TextView) findViewById(R.id.txtvPwCon);

        SetEditText();

        URL_Join = "http://kebin1104.dothome.co.kr/BIG/Join.php";
    }

    public void mOnClick(View v){
        if(v.getId() == R.id.btnJoinOK){
            if(!EmptyCheck()){
                if(PwCheck()){
                    task_Join = new JOINMEMBER(JoinActivity.this);
                    task_Join.execute(URL_Join);
                }
                else{
                    Toast.makeText(JoinActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(JoinActivity.this, "빈칸이 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId() == R.id.btnJoinBack){
            finish();
        }
    }

    public void SetEditText(){
        edtxPwCon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(edtxPW.getText().toString().equals(edtxPwCon.getText().toString())){
                    txtvPwCon.setTextColor(Color.parseColor("#4EF25B"));
                }
                else{
                    txtvPwCon.setTextColor(Color.parseColor("#F24E4E"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public boolean EmptyCheck(){
        return edtxID.getText().toString().equals("") || edtxPW.getText().toString().equals("") || edtxPwCon.getText().toString().equals("") || edtxName.getText().toString().equals("");
    }

    public boolean PwCheck(){
        return edtxPW.getText().toString().equals(edtxPwCon.getText().toString());
    }

    private class JOINMEMBER extends AsyncTask<String, Integer, String>{

        private Context context;

        private ProgressDialog mProgressDlg;

        private final String JoinOK = "Join OK";
        private final String JoinOverLap = "ID overlap";

        public JOINMEMBER(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDlg = new ProgressDialog(context);
            mProgressDlg.setMessage("회원가입중...");
            mProgressDlg.setCancelable(false);
            mProgressDlg.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String strResponse = "";

            try {
                //URL 설정
                URL url = new URL(strings[0]);
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
                    String ID = edtxID.getText().toString();
                    String PW = edtxPW.getText().toString();
                    String Name = edtxName.getText().toString();

                    buffer.append("ID=").append(ID + "&");
                    buffer.append("Name=").append(Name + "&");
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
        protected void onPostExecute(String s) {

            mProgressDlg.dismiss();

            if(s.equals(JoinOK)){
                Toast.makeText(context, "회원가입 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(s.equals(JoinOverLap)){
                Toast.makeText(context, "ID 중복", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "네트워크 에러", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
