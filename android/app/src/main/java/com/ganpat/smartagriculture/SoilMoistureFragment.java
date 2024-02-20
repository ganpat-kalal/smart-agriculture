package com.ganpat.smartagriculture;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class SoilMoistureFragment extends Fragment{

    public SoilMoistureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_soil, container, false);
        Context context = getActivity().getApplicationContext();

        String htmlstr = "<iframe width=\"450\" height=\"260\" style=\"border: 1px solid #cccccc;\" src=\"https://thingspeak.com/channels/552505/charts/3?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15\" ></iframe>";

        WebView webview;

        webview = (WebView) rootView.findViewById(R.id.webView);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadData(htmlstr, "text/html", null);

        if (Build.VERSION.SDK_INT >= 19) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        getActivity().setTitle("Soil Moisture");
//        PieChart pieChart = (PieChart) rootView.findViewById(R.id.piechart);
//        pieChart.setUsePercentValues(true);
//
//        // IMPORTANT: In a PieChart, no values (Entry) should have the same
//        // xIndex (even if from different DataSets), since no values can be
//        // drawn above each other.
//        ArrayList<Entry> yvalues = new ArrayList<Entry>();
//        yvalues.add(new Entry(25f, 0));
//        yvalues.add(new Entry(25f, 1));
//        yvalues.add(new Entry(25f, 2));
//        yvalues.add(new Entry(25f, 3));
//
//
//        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");
//
//        ArrayList<String> xVals = new ArrayList<String>();
//
//        xVals.add("Humidity Sensor");
//        xVals.add("Tempreature");
//        xVals.add("Soil moisture1");
//        xVals.add("Soil moisture2");
//
//
//        PieData data = new PieData(xVals, dataSet);
//        data.setValueFormatter(new PercentFormatter());
//        pieChart.setData(data);
//        pieChart.setDescription("This is Pie Chart");
//
//        pieChart.setDrawHoleEnabled(true);
//        pieChart.setTransparentCircleRadius(25f);
//        pieChart.setHoleRadius(25f);
//
//        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        data.setValueTextSize(13f);
//        data.setValueTextColor(Color.DKGRAY);
//        pieChart.setOnChartValueSelectedListener(this);
//
//        pieChart.animateXY(1400, 1400);

        return rootView;
    }

//    @Override
//    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//
//        if (e == null)
//            return;
//        Log.i("VAL SELECTED",
//                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
//                        + ", DataSet index: " + dataSetIndex);
//    }
//
//    @Override
//    public void onNothingSelected() {
//        Log.i("PieChart", "nothing selected");
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Charts");
//    }
}
