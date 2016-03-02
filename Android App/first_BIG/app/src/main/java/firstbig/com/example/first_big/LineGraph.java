package firstbig.com.example.first_big;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 짹짹 on 2014-08-21.
 */
public class LineGraph {

    // 변수
    private XYMultipleSeriesRenderer renderer;
    XYMultipleSeriesDataset dataset;

    private Context context;

    private String[] RendererTitles;

    private List<double[]> x;
    private List<double[]> values;

    private int[] RendererColors;

    private PointStyle[] styles;

    // 메소드

    public LineGraph(Context context, String RendererTitle, double[] x, double[] values){

        this.context = context;

        renderer = new XYMultipleSeriesRenderer();
        dataset = new XYMultipleSeriesDataset();

        this.RendererTitles = new String[] {RendererTitle};

        this.x = new ArrayList<double[]>();
        this.values = new ArrayList<double[]>();

        this.x.add(x);
        this.values.add(values);
    }

    public void setRenderer(int color, PointStyle style,
                            float AxisTitleTextSize, float ChartTitleTextSize, float LabelsTextSize, float LegendTextSize, float PointSize,
                            int[] Margins, int MarginColor, int BackgroundColor){

        int length;

        RendererColors = new int[] {color};
        styles = new PointStyle[] {style};

        renderer.setAxisTitleTextSize(AxisTitleTextSize);
        renderer.setChartTitleTextSize(ChartTitleTextSize);
        renderer.setLabelsTextSize(LabelsTextSize);
        renderer.setLegendTextSize(LegendTextSize);
        renderer.setPointSize(PointSize);
        renderer.setMargins(Margins);

        length = RendererColors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(RendererColors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }

        length = renderer.getSeriesRendererCount();
        for(int i=0;i<length;i++){
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }

        renderer.setMarginsColor(MarginColor);
        renderer.setBackgroundColor(BackgroundColor);

        renderer.setFitLegend(true);
    }

    public void setChartSettinds(String title, String xTitle, String yTitle,
                                 double xMin, double xMax, double yMin, double yMax,
                                 int axesColor, int labelsColor){
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);

        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);

        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    public void setLabel(int xLabel, int yLabel, int xLabelColor, int yLabelColor){

        renderer.setXLabels(xLabel);
        renderer.setYLabels(yLabel);

        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Paint.Align.CENTER);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        //renderer.setZoomButtonsVisible(true);
        //renderer.setPanLimits(new double[] { 0, 20, 0, 40 });
        //renderer.setZoomLimits(new double[] { 0, 20, 0, 40 });
        renderer.setPanEnabled(false, false);
        renderer.setZoomEnabled(false, false);

        renderer.setXLabelsColor(xLabelColor);
        renderer.setYLabelsColor(0, yLabelColor);

        renderer.setApplyBackgroundColor(true);
        //renderer.setShowLegend(false);
    }

    public void addXYSeries(){
        int length = RendererTitles.length;
        for(int i=0;i<length;i++){
            double[] xV = x.get(i);
            XYSeries series = new XYSeries(RendererTitles[i], 0);
            double[] yV = values.get(i);

            int seriesLength = xV.length;
            for(int j=0;j<seriesLength;j++){
                series.add(xV[j], yV[j]);
            }

            dataset.addSeries(series);
        }
    }

    public GraphicalView getChartView(){

        this.addXYSeries();

        return ChartFactory.getLineChartView(context, dataset, renderer);
    }
}