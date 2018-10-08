package firstbig.com.example.first_big;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 짹짹 on 2014-08-19.
 */
public class YearActivity extends Fragment {

    private LinearLayout layGraph;

    private GraphicalView mChartView;

    private CALENDAR mCalendar;
    private int Year, Month, Date;

    public static YearActivity newInstance(){
        YearActivity f = new YearActivity();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_year, container, false);

        layGraph = (LinearLayout) v.findViewById(R.id.layoutYearGraph);

        DrawGraph();

        return v;
    }

    public void DrawGraph(){

        double[] x = new double[12];
        double[] y = new double[12];

        Random random = new Random();
        double yMax = -1;

        for(int i=0;i<12;i++){
            x[i] = i+1;
            y[i] = random.nextInt(100);

            if(y[i] > yMax){
                yMax = y[i];
            }
        }

        LineGraph mLineGraph = new LineGraph(getActivity(), Year + " 년 전기 사용량", x, y);

        mLineGraph.setRenderer(Color.parseColor("#fead9e"), PointStyle.CIRCLE, 16, 20, 15, 20, 5f, new int[] { 25, 35, 10, 15 },
                Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));

        mLineGraph.setChartSettinds("title", "월", "사용량", 1, 12, 0, yMax + 10, Color.LTGRAY, Color.LTGRAY);

        mLineGraph.setLabel(12, 20, Color.BLACK, Color.BLACK);

        mChartView = mLineGraph.getChartView();

        layGraph.addView(mChartView);
    }
}
