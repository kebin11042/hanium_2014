package firstbig.com.example.first_big;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by 짹짹 on 2014-08-04.
 */
public class MyInfoActivity extends Fragment implements View.OnClickListener{

    private ImageButton btnConnDevice;
    private ImageButton btnGoal;
    private ImageButton btnImport;

    private TextView txtvName;
    private TextView txtvDeviceInfo;
    private TextView txtvGoal;
    private TextView txtvImport;

    private String Logined_pk;

    private IMPORRTINFO task_ImprotInfo;
    private CONNECTDEVICE task_ConnectDevice;
    private UPDATEGOAL task_UpdateGoal;
    private UPDATEIMPORT task_UpdateImport;

    private String URL_importinfo;
    private String URL_ConnectDevice;
    private String URL_UpdateGoal;
    private String URL_UpdateImport;

    public static MyInfoActivity newInstance(String Logined_pk) {
        MyInfoActivity f = new MyInfoActivity();

        Bundle b = new Bundle();
        b.putString("Logined_pk", Logined_pk);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logined_pk = getArguments().getString("Logined_pk");

        URL_importinfo = "http://kebin1104.dothome.co.kr/BIG/ImportInfo.php";
        URL_ConnectDevice = "http://kebin1104.dothome.co.kr/BIG/ConnectDevice.php";
        URL_UpdateGoal = "http://kebin1104.dothome.co.kr/BIG/UpdateGoal.php";
        URL_UpdateImport = "http://kebin1104.dothome.co.kr/BIG/UpdateImport.php";
    }

    public void ExecuteImportInfo(){
        task_ImprotInfo = new IMPORRTINFO(getActivity());
        task_ImprotInfo.execute(URL_importinfo);
    }

    public void ExecuteConnectDevice(String DevNum, AlertDialog dialog){
        task_ConnectDevice = new CONNECTDEVICE(getActivity(), DevNum, dialog);
        task_ConnectDevice.execute(URL_ConnectDevice);
    }

    public void ExecuteUpdateGoal(String DevNum, AlertDialog dialog){
        task_UpdateGoal = new UPDATEGOAL(getActivity(), DevNum, dialog);
        task_UpdateGoal.execute(URL_UpdateGoal);
    }

    public void ExecuteUpdateImport(String Import, AlertDialog dialog){
        task_UpdateImport = new UPDATEIMPORT(getActivity(), Import, dialog);
        task_UpdateImport.execute(URL_UpdateImport);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_info, container, false);

        btnConnDevice = (ImageButton) v.findViewById(R.id.btnConnDevice);
        btnGoal = (ImageButton) v.findViewById(R.id.btnGoal);
        btnImport = (ImageButton) v.findViewById(R.id.btnImport);

        txtvName = (TextView) v.findViewById(R.id.txtvName);
        txtvDeviceInfo = (TextView) v.findViewById(R.id.txtvDeviceInfo);
        txtvGoal = (TextView) v.findViewById(R.id.txtvGoal);
        txtvImport = (TextView) v.findViewById(R.id.txtvImport);

        btnConnDevice.setOnClickListener(this);
        btnGoal.setOnClickListener(this);
        btnImport.setOnClickListener(this);

        ExecuteImportInfo();

        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnConnDevice){
            //Toast.makeText(getActivity(), "연결버튼 클릭", Toast.LENGTH_SHORT).show();

            final View dialogView = (View) View.inflate(getActivity(), R.layout.dialog_conndevice, null);

            final EditText edtxDeviceNum = (EditText) dialogView.findViewById(R.id.edtxDeviceNum);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);
            builder.setView(dialogView);
            builder.setPositiveButton("연결", new DialogInterface.OnClickListener() {
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
                    //Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                    String DevNum = edtxDeviceNum.getText().toString();
                    ExecuteConnectDevice(DevNum, dialog);

                }
            });
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_rectangle_whitetobrown));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_rectangle_whitetobrown));
        }
        else if(view.getId() == R.id.btnGoal){
            final View dialogView = (View) View.inflate(getActivity(), R.layout.dialog_goal, null);

            final EditText edtxGoal = (EditText) dialogView.findViewById(R.id.edtxGoal);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);
            builder.setView(dialogView);
            builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
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
                    //Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                    String Goal = edtxGoal.getText().toString();
                    ExecuteUpdateGoal(Goal, dialog);
                }
            });
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_rectangle_whitetobrown));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_rectangle_whitetobrown));
        }
        else if(view.getId() == R.id.btnImport){
            final View dialogView = (View) View.inflate(getActivity(), R.layout.dialog_import, null);

            final EditText edtxImport = (EditText) dialogView.findViewById(R.id.edtxImport);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);
            builder.setView(dialogView);
            builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
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
                    //Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                    String Import = edtxImport.getText().toString();
                    ExecuteUpdateImport(Import, dialog);
                }
            });
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_rectangle_whitetobrown));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_rectangle_whitetobrown));
        }
    }

    private class IMPORRTINFO extends AsyncTask<String, Integer, String> {

        private Context context;

        private String strName;
        private String strGoal;
        private String strImport;
        private String strDeviceNumber;

        public IMPORRTINFO (Context context) {
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

                    buffer.append("pk=").append(Logined_pk);

                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();

                    //--------------------------
                    //  서버에서 값 전송 받기
                    //--------------------------

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader br = new BufferedReader(tmp);

                    strName = br.readLine();
                    strGoal = br.readLine();
                    strImport = br.readLine();
                    strDeviceNumber = br.readLine();

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

            txtvName.setText(strName + " 님");
            if(strGoal.equals("")){
                txtvGoal.setText("설정 바람.");
            }
            else{
                txtvGoal.setText(String.format("%,d 원", Integer.parseInt(strGoal)));
            }
            if(strImport.equals("")){
                txtvImport.setText("설정 바람.");
            }
            else{
                txtvImport.setText(String.format("%,d 원", Integer.parseInt(strImport)));
            }

            //Toast.makeText(getActivity(), strDeviceNumber + "호미호미", Toast.LENGTH_SHORT).show();

            if(strDeviceNumber.equals("Not Connect")){
                txtvDeviceInfo.setText("연결된 기기가 없음");
            }
            else{
                txtvDeviceInfo.setText(strDeviceNumber);
            }
        }
    }

    private class CONNECTDEVICE extends AsyncTask<String, Integer, String> {

        private Context context;

        private String Device_Number;

        private AlertDialog dialog;

        private final String strDeviceNumberErr = "Device Number error";
        private final String strConnDevSuccess = "Connect Device Success";
        private final String strChangeDevSuccess = "Change Device Success";

        private ProgressDialog mProgressdlg;

        public CONNECTDEVICE (Context context, String Device_Number, AlertDialog dialog) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.Device_Number = Device_Number;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mProgressdlg = new ProgressDialog(context);
            mProgressdlg.setMessage("기기 연결중...");
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

                    buffer.append("member_pk=").append(Logined_pk + "&")
                    .append("device_Number=").append(Device_Number);

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
            mProgressdlg.dismiss();

            //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

            // 기기번호 에러
            if(result.equals(strDeviceNumberErr)){
                Toast.makeText(context, "찾으시는 기기번호가 없습니다", Toast.LENGTH_SHORT).show();
            }
            // 기기 신규 연결 성공
            else{
                dialog.dismiss();

                if(result.equals(strConnDevSuccess)){
                    Toast.makeText(context, "기기 연결 성공", Toast.LENGTH_SHORT).show();
                }
                // 기기 연결 변경 성공
                else if(result.equals(strChangeDevSuccess)){
                    Toast.makeText(context, "기기 변경 성공", Toast.LENGTH_SHORT).show();
                }
                txtvDeviceInfo.setText(Device_Number);
            }
        }
    }

    private class UPDATEGOAL extends AsyncTask<String, Integer, String> {

        private Context context;

        private String Goal;

        private AlertDialog dialog;

        private final String strSuccess = "Success";

        private ProgressDialog mProgressdlg;

        public UPDATEGOAL (Context context, String Goal, AlertDialog dialog) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.Goal = Goal;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mProgressdlg = new ProgressDialog(context);
            mProgressdlg.setMessage("목표 금액 설정중...");
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

                    buffer.append("member_pk=").append(Logined_pk + "&")
                            .append("Goal=").append(Goal);

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
            mProgressdlg.dismiss();

            //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

            // 기기번호 에러
            if(result.equals(strSuccess)){
                Toast.makeText(context, "목표 금액 설정 완료", Toast.LENGTH_SHORT).show();
                txtvGoal.setText(Goal + " 원");
                dialog.dismiss();
            }
        }
    }

    private class UPDATEIMPORT extends AsyncTask<String, Integer, String> {

        private Context context;

        private String Import;

        private AlertDialog dialog;

        private final String strSuccess = "Success";

        private ProgressDialog mProgressdlg;

        public UPDATEIMPORT (Context context, String Import, AlertDialog dialog) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.Import = Import;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mProgressdlg = new ProgressDialog(context);
            mProgressdlg.setMessage("한달 수입 설정중...");
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

                    buffer.append("member_pk=").append(Logined_pk + "&")
                            .append("Import=").append(Import);

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
            mProgressdlg.dismiss();

            //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

            // 기기번호 에러
            if(result.equals(strSuccess)){
                Toast.makeText(context, "한달 수입 설정 완료", Toast.LENGTH_SHORT).show();
                txtvImport.setText(Import + " 원");
                dialog.dismiss();
            }
        }
    }
}
