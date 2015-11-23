package com.fing.proingsoft.plainstock;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Pair;
import android.widget.Toast;

import com.fing.proingsoft.plainstock.otros.Configuracion;
import com.fing.proingsoft.plainstock.otros.PlainStockDataSource;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;


import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Evolucion extends ActividadConMenuInf implements OnChartGestureListener, View.OnClickListener{

    private LineChart mChart;
    private String saldoText;
    private ImageButton btnGoToNextDate;
    Configuracion conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.tituloevolucion);
            setContentView(R.layout.activity_evolucion);

            conf = Configuracion.getInstance();

            PlainStockDataSource dataSource = new PlainStockDataSource(this);


            //Cargar grafica
            mChart = (LineChart) findViewById(R.id.chart);
            LineData data = getData();
            setupChart(mChart,data);

            //Instanciar saldo
            TextView saldoText= (TextView)findViewById(R.id.textView3);
            int saldoint = dataSource.querySaldo();
            String saldostr = (new Integer(saldoint)).toString();
            saldoText.setText(saldostr);

            mChart.setOnChartGestureListener(this);

            btnGoToNextDate = (ImageButton) findViewById(R.id.bt_config_reiniciar);
            btnGoToNextDate.setOnClickListener(this);

            View goToNextDateLayout = findViewById(R.id.LinearLayoutGoToNextDate);

            if(conf.isTimeMachineEnabled()){
                goToNextDateLayout.setVisibility(View.VISIBLE);
            }
            else{
                goToNextDateLayout.setVisibility(View.INVISIBLE);
            }

            Spinner spinner = (Spinner) findViewById(R.id.spRango);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.sta_rangos_grafica, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            PlainStockDataSource.RANGO_GRAFICA rango = conf.getRango();
            int position = 0;
            switch (rango){
                case DIA:    position=0; break;
                case SEMANA: position=1; break;
                case SEMANA2:position=2; break;
                case MES:    position=3; break;
                case MES6:   position=4; break;
                case ANO:    position=5; break;
                case ANO5:   position=6; break;
                case TODO:   position=7; break;
            }

            spinner.setSelection(position);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    cambioDeRango(parent,view,position,id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        catch (Exception e){
            setContentView(R.layout.error);
            TextView desc = (TextView)findViewById(R.id.tvErrorDescripcion);
            desc.setText(e.getMessage());
        }

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
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this, PantallaConfiguracion.class);
                this.startActivity(intent);
                break;
            // This doesn't work?

        }
        return super.onOptionsItemSelected(item);
    }

    public void cambioDeRango(AdapterView<?> parent, View view, int position, long id){
        PlainStockDataSource.RANGO_GRAFICA rango= PlainStockDataSource.RANGO_GRAFICA.DIA;
        switch (position){
            case 0: rango = PlainStockDataSource.RANGO_GRAFICA.DIA; break;
            case 1: rango = PlainStockDataSource.RANGO_GRAFICA.SEMANA; break;
            case 2: rango = PlainStockDataSource.RANGO_GRAFICA.SEMANA2; break;
            case 3: rango = PlainStockDataSource.RANGO_GRAFICA.MES; break;
            case 4: rango = PlainStockDataSource.RANGO_GRAFICA.MES6; break;
            case 5: rango = PlainStockDataSource.RANGO_GRAFICA.ANO; break;
            case 6: rango = PlainStockDataSource.RANGO_GRAFICA.ANO5; break;
            case 7: rango = PlainStockDataSource.RANGO_GRAFICA.TODO; break;
        }
        if(rango != conf.getRango()){
            conf.setRango(rango);
            this.recreate();
        }
    }

    private void setupChart(LineChart chart, LineData data) {

        // no description text
        mChart.setDescription("");

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        mChart.setDrawGridBackground(false);

        XAxis x = mChart.getXAxis();
        x.setEnabled(true);

        YAxis y = mChart.getAxisLeft();
        y.setLabelCount(7, true);
        y.setEnabled(true);

        mChart.getAxisRight().setEnabled(false);

        // add data
        chart.setData(data);

        mChart.getLegend().setEnabled(false);

        chart.animateX(800);

        // dont forget to refresh the drawing
        mChart.invalidate();
    }

    private LineData getData() {
        PlainStockDataSource dataSource = new PlainStockDataSource(this);
        ArrayList<Pair> queryResult = dataSource.querySaldo(conf.getRango(), conf.getFechaSistema());
        //ArrayList<Pair<String,Integer>> queryResult = datosPrueba;
        int cant = queryResult.size();

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < cant; i++) {
            //xVals.add(((Integer) i).toString());//ACA IRIA LA FECHA
            xVals.add((String)(queryResult.get(i).first));
            int val = (Integer)queryResult.get(i).second;
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Saldo");
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
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public int getBtnMenuActivoId() {
        return R.id.btEvolucion;
    }


    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Toast.makeText(this,"Chorizo",Toast.LENGTH_SHORT);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_config_reiniciar:
                //Inicializar variables del DatePicker
                final Calendar c = Configuracion.getInstance().getFechaSistema();
                final int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH);
                final int mDay = c.get(Calendar.DAY_OF_MONTH);

                // Lanzar Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //Salto a la nueva fecha
                                goToNextDate(year,monthOfYear,dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setCalendarViewShown(false);
                dpd.getDatePicker().setSpinnersShown(true);
                dpd.show();
            break;
        }
    }

    public void goToNextDate(int year, int month, int day){
        conf.goToNextDate(year, month, day);
    }
}
