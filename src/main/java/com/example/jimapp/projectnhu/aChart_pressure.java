package com.example.jimapp.projectnhu;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2015/12/15.
 */
public class aChart_pressure extends Activity {
    private static String DATABASE_TABLE = "health";
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        //DB resource
        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        String[] titles = new String[] { "收縮壓","舒張壓" }; // 定義折線的名稱
        List<double[]> x = new ArrayList<double[]>(); // 點的x坐標
        List<double[]> y = new ArrayList<double[]>(); // 點的y坐標

        // 數值X坐標值輸入
        Cursor c1 = db.query(DATABASE_TABLE,new String[]{"編號","日期"},null,null,null,null,"日期",null);
        double[] x1 = new double[c1.getCount()];
        c1.moveToNext();
        for(int i = 0; i < c1.getCount(); i++) {
            x1[i] = c1.getDouble(0);
            c1.moveToNext();
        }
        c1.close();
        x.add(x1);
        Cursor c2 = db.query(DATABASE_TABLE,new String[]{"編號","日期"},null,null,null,null,"日期",null);
        double[] x2 = new double[c2.getCount()];
        c2.moveToNext();
        for(int i = 0; i < c2.getCount(); i++) {
            x2[i] = c2.getDouble(0);
            c2.moveToNext();
        }
        c2.close();
        x.add(x2);

        // 數值Y坐標值輸入
        Cursor c = db.query(DATABASE_TABLE,new String[]{"日期","收縮壓"},null,null,null,null,"日期",null);
        double[] y1 = new double[c.getCount()];
        c.moveToNext();
        for(int i = 0; i < c.getCount(); i++) {
            y1[i] = c.getDouble(1);
            c.moveToNext();
        }
        c.close();
        y.add(y1);
        Cursor c3 = db.query(DATABASE_TABLE,new String[]{"日期","舒張壓"},null,null,null,null,"日期",null);
        double[] y2 = new double[c3.getCount()];
        c3.moveToNext();
        for(int i = 0; i < c3.getCount(); i++) {
            y2[i] = c3.getDouble(1);
            c3.moveToNext();
        }
        c3.close();
        y.add(y2);

        XYMultipleSeriesDataset dataset = buildDatset(titles, x, y); // 儲存座標值

        int[] colors = new int[] { Color.BLUE,Color.GREEN};// 折線的顏色
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND}; // 折線點的形狀
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);

        setChartSettings(renderer, "血壓折線圖", "編號", "血壓", 0, 10, 40, 200, Color.BLACK);// 定義折線圖
        View chart = ChartFactory.getLineChartView(this, dataset, renderer);
        setContentView(chart);
    }

    // 定義折線圖名稱
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor) {
        renderer.setChartTitle(title); // 折線圖名稱
        renderer.setChartTitleTextSize(54); // 折線圖名稱字形大小
        renderer.setXTitle(xTitle); // X軸名稱
        renderer.setYTitle(yTitle); // Y軸名稱
        renderer.setXAxisMin(xMin); // X軸顯示最小值
        renderer.setXAxisMax(xMax); // X軸顯示最大值
        renderer.setXLabelsColor(Color.BLACK); // X軸線顏色
        renderer.setYAxisMin(yMin); // Y軸顯示最小值
        renderer.setYAxisMax(yMax); // Y軸顯示最大值
        renderer.setAxesColor(axesColor); // 設定坐標軸顏色
        renderer.setYLabelsColor(0, Color.BLACK); // Y軸線顏色
        renderer.setLabelsColor(Color.BLACK); // 設定標籤顏色
        renderer.setMarginsColor(Color.WHITE); // 設定背景顏色
        renderer.setShowGrid(true); // 設定格線
    }

    // 定義折線圖的格式
    private XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles, boolean fill) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            r.setFillPoints(fill);
            renderer.addSeriesRenderer(r); //將座標變成線加入圖中顯示
        }
        return renderer;
    }

    // 資料處理
    private XYMultipleSeriesDataset buildDatset(String[] titles, List<double[]> xValues,
                                                List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        int length = titles.length; // 折線數量
        for (int i = 0; i < length; i++) {
            // XYseries對象,用於提供繪製的點集合的資料
            XYSeries series = new XYSeries(titles[i]); // 依據每條線的名稱新增
            double[] xV = xValues.get(i); // 獲取第i條線的資料
            double[] yV = yValues.get(i);
            int seriesLength = xV.length; // 有幾個點

            for (int k = 0; k < seriesLength; k++) // 每條線裡有幾個點
            {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

    /**關閉資料庫*/
    protected void onStop() {
        super.onStop();
        db.close();
    }
}
