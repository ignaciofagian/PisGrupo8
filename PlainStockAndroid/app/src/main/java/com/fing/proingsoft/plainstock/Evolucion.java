package com.fing.proingsoft.plainstock;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.util.Pair;

import com.fing.proingsoft.plainstock.otros.Configuracion;
import com.fing.proingsoft.plainstock.otros.PlainStockDataSource;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.*;


import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Evolucion extends ActividadConMenuInf {

    private LineChart mChart;
    private String saldoText;
    Configuracion conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tituloevolucion);
        setContentView(R.layout.activity_evolucion);


        PlainStockDataSource dataSource = new PlainStockDataSource(this);


        //Cargar grafica
        mChart = (LineChart) findViewById(R.id.chart);
        LineData data = getData(36, 100);
        setupChart(mChart,data);


        //Instanciar saldo
        TextView saldoText= (TextView)findViewById(R.id.textView3);
        int saldoint = dataSource.querySaldo();
        String saldostr = (new Integer(saldoint)).toString();
        saldoText.setText(saldostr);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_evolucion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupChart(LineChart chart, LineData data) {

        chart.setDescription("");
        chart.setNoDataTextDescription("You need to provide data for the chart.");

        chart.setHighlightEnabled(false);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(true);

        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().setSpaceTop(0.15f);

        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setAvoidFirstLastClipping(true);

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);

        chart.setData(data);

        // mChart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);



        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10, 0, 10, 0);



        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.getAxisLeft().setEnabled(true);
        chart.getAxisRight().setEnabled(true);

        chart.getXAxis().setEnabled(true);

        // animate calls invalidate()...
        chart.animateX(2500);
    }

    private LineData getData(int count, float range) {
        PlainStockDataSource dataSource = new PlainStockDataSource(this);
        Configuracion conf = Configuracion.getInstance();
        ArrayList<Pair> queryResult = dataSource.querySaldo(conf.getRango(),conf.getFechaSistema());
        int cant = queryResult.size();

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < cant; i++) {
            xVals.add("i");//ACA IRIA LA FECHA
            //float val = (float) (Math.random() * range) + 3;
            int val = (Integer)queryResult.get(i).second;
            yVals.add(new Entry(val, i));
        }


        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
        set1.setDrawFilled(true);
        set1.setDrawCubic(true);
        set1.setCubicIntensity(0.2f);
        set1.setFillAlpha(110);
        set1.setDrawCircles(false);
        set1.setLineWidth(2f);
        set1.setCircleSize(5f);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(104, 241, 175));
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setLineWidth(1.75f);
        set1.setCircleSize(3f);
        //set1.setColor(Color.GREEN);
        //set1.setCircleColor(Color.WHITE);
        //set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(false);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        return data;
    }

    @Override
    public int getBtnMenuActivoId() {
        return R.id.btEvolucion;
    }
}
