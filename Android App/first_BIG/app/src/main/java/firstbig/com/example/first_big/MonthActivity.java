package firstbig.com.example.first_big;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Type;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

/**
 * Created by 짹짹 on 2014-08-06.
 */
public class MonthActivity extends Fragment implements  View.OnClickListener{

    private String Logined_pk;
    private String device_pk;

    private ImageButton btnPre;
    private ImageButton btnNext;
    private ImageButton btnReload;

    private TextView txtvMonth;
    private TextView txtvYear;

    private TextView txtvMonthAvgElecData;
    private TextView txtvMonthAvgMoney;
    private TextView txtvMonthMaxElecData;
    private TextView txtvMonthMinElecData;

    private LinearLayout layGraph;

    private GraphicalView mChartView;

    private CALENDAR mCalendar;
    private CALMONEY mCalMoney;

    private IMPORTDEVICEPK task_ImportDevicePk;
    private IMPORTMONTHLYDATA task_ImportMonthlyData;

    private String URL_ImportDevicePk;
    private String URL_ImportMonthlyData;

    private double[] ElecDatas;

    private int Year, Month, Date;
    private int DayOfMonth;

    public static MonthActivity newInstance(String Logined_pk){
        MonthActivity f = new MonthActivity();

        Bundle b = new Bundle();
        b.putString("Logined_pk", Logined_pk);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendar = new CALENDAR();
        mCalMoney = new CALMONEY();

        Logined_pk = getArguments().getString("Logined_pk");

        URL_ImportDevicePk = "http://kebin1104.dothome.co.kr/BIG/ImportDevice_pk.php";
        URL_ImportMonthlyData = "http://kebin1104.dothome.co.kr/BIG/ImportMonthlyData.php";

        SetNow();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_month, container, false);

        mChartView = null;

        btnPre = (ImageButton) v.findViewById(R.id.btnMonthPrev);
        btnPre.setEnabled(false);
        btnNext = (ImageButton) v.findViewById(R.id.btnMonthNext);
        btnNext.setEnabled(false);
        btnReload = (ImageButton) v.findViewById(R.id.btnThisMonth);
        btnReload.setEnabled(false);

        txtvYear = (TextView) v.findViewById(R.id.txtvMonthYear);
        txtvMonth = (TextView) v.findViewById(R.id.txtvMonth);
        txtvMonthAvgElecData = (TextView) v.findViewById(R.id.txtvMonthAvgElecData);
        txtvMonthAvgMoney = (TextView) v.findViewById(R.id.txtvMonthAvgMoney);
        txtvMonthMaxElecData = (TextView) v.findViewById(R.id.txtvMonthMaxElecData);
        txtvMonthMinElecData = (TextView) v.findViewById(R.id.txtvMonthMinElecData);

        layGraph = (LinearLayout) v.findViewById(R.id.layoutMonthGraph);

        btnNext.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnReload.setOnClickListener(this);

        double[] y = new double[mCalendar.getDayOfMonth(Year,Month)];
        for(int i=0;i<mCalendar.getDayOfMonth(Year, Month);i++){
            y[i] = 0;
        }
        DrawGraph(y);

        ExecuteImportDevicePk();

        DrawTxtv();

        return v;
    }

    public void ExecuteImportDevicePk(){
        task_ImportDevicePk = new IMPORTDEVICEPK(getActivity());
        task_ImportDevicePk.execute(URL_ImportDevicePk);
    }

    public void ExecuteImportMonthlyData(){
        task_ImportMonthlyData = new IMPORTMONTHLYDATA(getActivity());
        task_ImportMonthlyData.execute(URL_ImportMonthlyData);
    }

    public void SetNow(){
        Year = mCalendar.getNowYear();
        Month = mCalendar.getNowMonth();
        Date = mCalendar.getNowDate();
    }

    public void DrawTxtv(){
        txtvYear.setText("" + Year);
        txtvMonth.setText((Month + 1) + " 월");
    }

    public void DrawAvg(){

        double DataSum = 0;
        double DataAvg;
        double MoneySum = 0;
        double MoneyAvg;

        for(int i=0;i<ElecDatas.length;i++){
            DataSum += ElecDatas[i];
            MoneySum += mCalMoney.getElecMoney(ElecDatas[i]);
        }

        DataAvg = DataSum / (double)ElecDatas.length;
        MoneyAvg = MoneySum / (double)ElecDatas.length;

        txtvMonthAvgElecData.setText(String.format("%.2f kw/h", DataAvg));
        txtvMonthAvgMoney.setText(String.format("%,d 원", (int)MoneyAvg));
    }
    public void DrawMaxMin(){
        double[] sortData = new double[ElecDatas.length];
        for(int i=0;i<sortData.length;i++){
            sortData[i] = ElecDatas[i];
        }
        Arrays.sort(sortData);

        double Max = sortData[sortData.length -1];
        double Min = sortData[0];

        int MaxDay = 0;
        int MinDay = 0;

        //일 수를 찾기 위함
        for(int i=0;i<ElecDatas.length;i++){
            if(Max == ElecDatas[i]){
                MaxDay = i + 1;
            }

            if(Min == ElecDatas[i]){
                MinDay = i + 1;
            }
        }

        txtvMonthMaxElecData.setText(String.format("%.2f kwh (%d 일)", Max, MaxDay));
        txtvMonthMinElecData.setText(String.format("%.2f kwh (%d 일)", Min, MinDay));
    }

    public void DrawGraph(double[] y){

        DayOfMonth = mCalendar.getDayOfMonth(Year, Month);

        double[] x = new double[DayOfMonth];

        double yMax = -1;

        for(int i=0;i<DayOfMonth;i++){
            x[i] = i+1;

            if(y[i] > yMax){
                yMax = y[i];
            }
        }

        LineGraph mLineGraph = new LineGraph(getActivity(), (Month + 1) + " 월 전기 사용량", x, y);

        mLineGraph.setRenderer(Color.parseColor("#fead9e"), PointStyle.CIRCLE, 16, 20, 15, 20, 5f, new int[] { 25, 35, 10, 15 },
                Color.parseColor("#00ffffff"), Color.parseColor("#00ffffff"));

        mLineGraph.setChartSettinds("", "일", "사용량(kw/h)", 1, DayOfMonth, 0, yMax + 10, Color.LTGRAY, Color.LTGRAY);

        mLineGraph.setLabel(DayOfMonth, 20, Color.BLACK, Color.BLACK);

        mChartView = mLineGraph.getChartView();

        layGraph.addView(mChartView);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnMonthPrev){
            Month--;
            if(Month < 0){
                Year--;
                Month = 11;
            }

            DrawTxtv();
        }
        else if(view.getId() == R.id.btnMonthNext){
            Month++;
            if(Month > 11){
                Year++;
                Month = 0;
            }

            DrawTxtv();
        }
        else if(view.getId() == btnReload.getId()){
            SetNow();

            DrawTxtv();
        }

        ExecuteImportMonthlyData();
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

                TextView txtvNotConn = new TextView(context);
                txtvNotConn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                txtvNotConn.setText("기기가 연결되지 않았습니다");
                txtvNotConn.setTextSize(30);

                layGraph.addView(txtvNotConn);
            }
            else{
                device_pk = result;

                btnPre.setEnabled(true);
                btnNext.setEnabled(true);
                btnReload.setEnabled(true);

                ExecuteImportMonthlyData();
            }
        }
    }

    private class IMPORTMONTHLYDATA extends AsyncTask<String, Integer, String> {

        private Context context;
        private ProgressBar mProgress;

        private double[] y = new double[mCalendar.getDayOfMonth(Year, Month)];

        public IMPORTMONTHLYDATA(Context context) {
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mProgress = new ProgressBar(context);
            mProgress.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layGraph.removeAllViews();
            layGraph.addView(mProgress);
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
                    buffer.append("Month=").append((Month+1) + "&");
                    buffer.append("DayOfMonth=").append(mCalendar.getDayOfMonth(Year, Month));

                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();

                    //--------------------------
                    //  서버에서 값 전송 받기
                    //--------------------------

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader br = new BufferedReader(tmp);

                    for(int i=0;i<mCalendar.getDayOfMonth(Year, Month);i++){
                        y[i] = Double.parseDouble(br.readLine());
                    }

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

            ElecDatas = new double[y.length];
            ElecDatas = y;

            //그래프 그리기전에 기존에 있던거 싹 다 지워야함함
           if(layGraph.getChildCount() != 0){
                layGraph.removeAllViews();
            }

            DrawGraph(y);

            DrawAvg();
            DrawMaxMin();
        }
    }
}
