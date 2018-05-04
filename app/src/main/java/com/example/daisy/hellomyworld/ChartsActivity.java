package com.example.daisy.hellomyworld;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartsActivity extends Activity {

    private LineChartView mChart;
    private Map<String,Integer> table=new TreeMap<>();
    private LineChartData mData;
    private String ckey;
    private Integer cvalue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_view);
        mChart=(LineChartView) findViewById(R.id.chart);
        List<CostBean> allDate= (List<CostBean>) getIntent().getSerializableExtra("cost_list");
        generateValues(allDate);
        generateData();

        mChart.setLineChartData(mData);
        //设置行为属性，支持缩放、滑动以及平移
        mChart.setInteractive(true);
        mChart.setZoomType(ZoomType.HORIZONTAL);
        mChart.setMaxZoom((float) 2);//最大方法比例
        mChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mChart.setVisibility(View.VISIBLE);
        Viewport v = new Viewport(mChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        mChart.setCurrentViewport(v);

    }


    private void generateData() {
        List<Line> lines=new ArrayList<>();
        List<PointValue> values=new ArrayList<>();
        List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

        int index=0;
        Iterator<String> iter =  table.keySet().iterator();
        while (iter.hasNext()) {
            ckey = iter.next();
            cvalue=table.get(ckey);
            mAxisXValues.add(new AxisValue(index).setLabel(ckey));
            values.add(new PointValue(index,cvalue));
            index++;
        }

        Line line=new Line(values).setColor(Color.GRAY);
        line.setShape(ValueShape.CIRCLE);
        line.setPointColor(Color.BLUE);
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        lines.add(line);

        mData=new LineChartData();
        Axis axisX = new Axis();

        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
//        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
//        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
//        axisX.setHasLines(true); //x 轴分割线



        Axis axisY = new Axis();
        mData.setAxisXBottom(axisX);
        mData.setAxisYLeft(axisY);
        mData.setLines(lines);




    }

    private void generateValues(List<CostBean> allDate) {
        if(allDate!=null){
            for (int i = 0; i <allDate.size() ; i++) {
                CostBean costBean=allDate.get(i);
                String costDate=costBean.costDate;
                int costMoney= Integer.parseInt(costBean.costMoney);
                if (!table.containsKey(costDate)){
                    table.put(costDate,costMoney);
                }else {
                    int originMoney=table.get(costDate);
                    table.put(costDate,costMoney+originMoney);
                }

            }
        }
    }
}
