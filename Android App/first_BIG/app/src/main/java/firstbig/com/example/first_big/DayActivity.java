package firstbig.com.example.first_big;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by 짹짹 on 2014-08-05.
 */
public class DayActivity extends Fragment implements View.OnClickListener{

    private ImageButton btnBack;
    private ImageButton btnNext;
    private ImageButton btnMonthPayInfo;
    private ImageButton btnToday;
    private ImageButton btnImgEnergyPool;

    private TextView txtvYearMonth;
    private TextView txtvDay;
    private TextView txtvDayMonthSumData;
    private TextView txtvDayMonthSumMoney;
    private TextView txtvDayElec;
    private TextView txtvDayMoney;
    private TextView txtvDayGoalMoney;
    private TextView txtvDayEnergyPool;

    private IMPORTDEVICEPK task_ImportDevicePk;
    private IMPORTMONTHDATA task_ImportMonthData;
    private IMPORTTODAYDATA task_ImportTodayData;
    private IMPORTDAYDATA task_ImportDayData;

    private String URL_ImportDevicePk;
    private String URL_ImportMonthData;
    private String URL_ImportTodayData;
    private String URL_ImportDayData;

    private String Logined_pk;
    private String device_pk;
    private String Goal;
    private String Import;
    private double Elec_Data;
    private double Today_Data;

    private CALENDAR mCalendar;
    private CALMONEY mCalMoney;
    private CALDAYMONEY mCalDayMoney;

    private int Year, Month, Date;

    public static DayActivity newInstance(String Logined_pk) {
        DayActivity f = new DayActivity();

        Bundle b = new Bundle();
        b.putString("Logined_pk", Logined_pk);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logined_pk = getArguments().getString("Logined_pk");

        Elec_Data = 0;

        mCalendar = new CALENDAR();
        mCalMoney = new CALMONEY();
        setNow();

        URL_ImportDevicePk = "http://kebin1104.dothome.co.kr/BIG/ImportDevice_pk.php";
        URL_ImportMonthData = "http://kebin1104.dothome.co.kr/BIG/ImportMonthData.php";
        URL_ImportDayData = "http://kebin1104.dothome.co.kr/BIG/ImportDayData.php";
        URL_ImportTodayData = "http://kebin1104.dothome.co.kr/BIG/ImportTodayData.php";
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_day, container, false);

        btnBack = (ImageButton) v.findViewById(R.id.btnDayPrev);
        btnBack.setEnabled(false);
        btnNext = (ImageButton) v.findViewById(R.id.btnDayNext);
        btnNext.setEnabled(false);
        btnMonthPayInfo = (ImageButton) v.findViewById(R.id.btnMonthPayInfo);
        btnMonthPayInfo.setEnabled(false);
        btnToday = (ImageButton) v.findViewById(R.id.btnToday);
        btnToday.setEnabled(false);
        btnImgEnergyPool = (ImageButton) v.findViewById(R.id.btnImgEnergyPool);

        txtvYearMonth = (TextView) v.findViewById(R.id.txtvDayYearMonth);
        txtvDay = (TextView) v.findViewById(R.id.txtvDay);
        txtvDayMonthSumData = (TextView) v.findViewById(R.id.txtvDayMontnSumData);
        txtvDayMonthSumMoney = (TextView) v.findViewById(R.id.txtvDayMontnSumMoney);
        txtvDayElec = (TextView) v.findViewById(R.id.txtvDayElec);
        txtvDayMoney = (TextView) v.findViewById(R.id.txtvDayMoney);
        txtvDayGoalMoney = (TextView) v.findViewById(R.id.txtvDayGoalMoney);
        txtvDayEnergyPool = (TextView) v.findViewById(R.id.txtvDayEnergyPool);

        btnNext.setOnClickListener(DayActivity.this);
        btnBack.setOnClickListener(DayActivity.this);
        btnMonthPayInfo.setOnClickListener(DayActivity.this);
        btnToday.setOnClickListener(DayActivity.this);

        DrawDateTxtv();

        ExecuteImportDevicePk();

        return v;
    }

    public void ExecuteImportDevicePk(){
        task_ImportDevicePk = new IMPORTDEVICEPK(getActivity());
        task_ImportDevicePk.execute(URL_ImportDevicePk);
    }

    public void ExecuteImportMonthData(){
        task_ImportMonthData = new IMPORTMONTHDATA(getActivity());
        task_ImportMonthData.execute(URL_ImportMonthData);
    }

    public void ExecuteImportTodayData(){
        task_ImportTodayData = new IMPORTTODAYDATA(getActivity());
        task_ImportTodayData.execute(URL_ImportTodayData);
    }

    public void ExecuteImportDayData(){
        task_ImportDayData = new IMPORTDAYDATA(getActivity());
        task_ImportDayData.execute(URL_ImportDayData);
    }

    public void setNow(){
        Year = mCalendar.getNowYear();
        Month = mCalendar.getNowMonth();
        Date = mCalendar.getNowDate();
    }

    public void DrawDateTxtv(){
        txtvYearMonth.setText(Year + " / " + (Month + 1));
        txtvDay.setText(Date + "");
    }

    public void DrawGoalTxtv(){
        double goal = Double.parseDouble(Goal) / mCalendar.getDayOfMonth(Year, Month);
        txtvDayGoalMoney.setText(String.format("%,d 원", (int)goal));
    }

    @Override
    public void onClick(View view) {

        int DayOfMonth = mCalendar.getDayOfMonth(Year, Month);

        if(view.getId() == R.id.btnDayPrev){
            //일 수를 한번 줄여준다
            //대신에 오늘이 1일이라면 달도 줄여줘야 댄다
            //또 대신에 달이 1월이라면 12월로 해주고 년도를 줄여줘야 한다
            //줄여준 일수는 그 달의 마지막 일이다

            txtvDayElec.setText("Loading...");
            txtvDayMoney.setText("Loading...");

            Date--;

            if(Date < 1){

                txtvDayMonthSumData.setText("Loading...");

                Month--;
                if(Month < 0){
                    Year--;
                    Month = 11;
                    Date = mCalendar.getDayOfMonth(Year, Month);
                }
                else{
                    Date = mCalendar.getDayOfMonth(Year, Month);
                }

                ExecuteImportMonthData();
            }

            DrawDateTxtv();
            ExecuteImportTodayData();
        }
        else if(view.getId() == R.id.btnDayNext){

            txtvDayElec.setText("Loading...");
            txtvDayMoney.setText("Loading...");

            Date++;
            if(Date > mCalendar.getDayOfMonth(Year, Month)){

                txtvDayMonthSumData.setText("Loading...");

                Month++;
                if(Month > 11){
                    Year++;
                    Month = 0;
                    Date = 1;
                }
                else{
                    Date = 1;
                }
                ExecuteImportMonthData();
            }

            DrawDateTxtv();
            ExecuteImportTodayData();
        }
        else if(view.getId() == R.id.txtvDay){
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    Year = year;
                    Month = monthOfYear;
                    Date = dayOfMonth;

                    DrawDateTxtv();

                    txtvDayMonthSumData.setText("Loading...");
                    txtvDayMonthSumMoney.setText("Loading...");
                    txtvDayElec.setText("Loading...");
                    txtvDayMoney.setText("Loading...");

                    ExecuteImportMonthData();
                    ExecuteImportTodayData();
                }
            }, Year, Month, Date);
            dialog.show();
        }
        else if(view.getId() == R.id.btnMonthPayInfo){
            Intent intent = new Intent(getActivity(), PayInfoActivity.class);
            intent.putExtra("Elec_Data", Elec_Data);
            startActivity(intent);
        }
        else if(view.getId() == R.id.btnToday){
            setNow();

            txtvDayMonthSumData.setText("Loading...");
            txtvDayMonthSumMoney.setText("Loading...");
            txtvDayElec.setText("Loading...");
            txtvDayMoney.setText("Loading...");

            DrawDateTxtv();

            ExecuteImportMonthData();
            ExecuteImportTodayData();
        }
        else if(view.getId() == R.id.txtvDayEnergyPool){
            int MonthMoney = (int) mCalMoney.getMoney(Elec_Data);

            Intent intent = new Intent(getActivity(), EnergyPoolActivity.class);
            intent.putExtra("MonthMoney", MonthMoney);
            intent.putExtra("Import", Import);
            startActivity(intent);
        }
    }

    private class IMPORTDEVICEPK extends AsyncTask<String, Integer, String> {

        private Context context;

        private final String strCheck = "Not Connect";

        public IMPORTDEVICEPK(Context context) {
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
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

                    buffer.append("Logined_pk=").append(Logined_pk);

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
                    Goal = br.readLine();
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

            if(result.equals(strCheck)){
                txtvDayMonthSumData.setText("기기와 연결 바람");
            }
            else{
                device_pk = result;

                btnNext.setEnabled(true);
                btnBack.setEnabled(true);
                btnMonthPayInfo.setEnabled(true);
                btnToday.setEnabled(true);

                txtvDay.setOnClickListener(DayActivity.this);
                txtvDayEnergyPool.setOnClickListener(DayActivity.this);

                DrawGoalTxtv();

                ExecuteImportMonthData();
                ExecuteImportTodayData();
            }
        }
    }

    private class IMPORTMONTHDATA extends AsyncTask<String, Integer, String> {

        private Context context;


        public IMPORTMONTHDATA(Context context) {
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            txtvDayEnergyPool.setText("Loading...");
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

                    buffer.append("device_pk=").append(device_pk + "&");
                    buffer.append("Year=").append(Year + "&");
                    //Month 주의 Month가 0 이면 1월을 뜻하기 때문이다
                    buffer.append("Month=").append((Month + 1) + "&");
                    buffer.append("Date=").append(Date);

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
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            Elec_Data = Double.parseDouble(result);
            double Money = mCalMoney.getMoney(Double.parseDouble(result));
            double sumData = Double.parseDouble(result);

            txtvDayMonthSumData.setText(String.format("%.3f kwh", sumData));
            //돈의 단위는 정수이다(int)
            txtvDayMonthSumMoney.setText(String.format("%,d", (int)Money) + " 원");



            int[] Icons = new int[] {R.drawable.happy, R.drawable.danger, R.drawable.sad};

            double base = Double.parseDouble(Import) * 10.0 / 100.0;

            if(Money >= base){
                btnImgEnergyPool.setImageResource(Icons[2]);
                txtvDayEnergyPool.setText("에너지 빈곤층 대상자 입니다.");
            }
            else if(Money >= base * 0.9){
                btnImgEnergyPool.setImageResource(Icons[1]);
                txtvDayEnergyPool.setText("에너지 빈곤층 위험 대상자 입니다.");
            }
            else{
                btnImgEnergyPool.setImageResource(Icons[0]);
                txtvDayEnergyPool.setText("에너지 빈곤층 대상자가 아닙니다.");
            }
        }
    }

    private class IMPORTTODAYDATA extends AsyncTask<String, Integer, String> {

        private Context context;


        public IMPORTTODAYDATA(Context context) {
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
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

                    buffer.append("device_pk=").append(device_pk + "&");
                    buffer.append("Year=").append(Year + "&");
                    //Month 주의 Month가 0 이면 1월을 뜻하기 때문이다
                    buffer.append("Month=").append((Month + 1) + "&");
                    buffer.append("Date=").append(Date);

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
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            Today_Data = Double.parseDouble(result);

            ExecuteImportDayData();
        }
    }

    private class IMPORTDAYDATA extends AsyncTask<String, Integer, String> {

        private Context context;


        public IMPORTDAYDATA(Context context) {
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
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

                    buffer.append("device_pk=").append(device_pk + "&");
                    buffer.append("Year=").append(Year + "&");
                    //Month 주의 Month가 0 이면 1월을 뜻하기 때문이다
                    buffer.append("Month=").append((Month + 1) + "&");
                    buffer.append("Date=").append(Date);

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
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            double dResult = Double.parseDouble(result);

            txtvDayElec.setText(String.format("%.3f kwh", dResult));
            mCalDayMoney = new CALDAYMONEY(Today_Data, Double.parseDouble(result));
            txtvDayMoney.setText(String.format("%,d 원", mCalDayMoney.getTodayMoney()));

            int p70 = (int)( ( ( Double.parseDouble(Goal) / (double)mCalendar.getDayOfMonth(Year,Month)) * 70.0 ) / 100.0);  //목표금액의 70프로

            int color;
            //사용량이 넘어가면 빨, 70%이상이면 노, 아래면 초록
            if(
            mCalDayMoney.getTodayMoney() >= (int) ( Double.parseDouble(Goal) / (double)mCalendar.getDayOfMonth(Year, Month) ) ){
                color = Color.parseColor("#e51c23");
            }
            else if(mCalDayMoney.getTodayMoney() >= p70){
                color = Color.parseColor("#ff9800");
            }
            else{
                color = Color.parseColor("#259b24");
            }

            txtvDayMoney.setTextColor(color);
        }
    }
}
