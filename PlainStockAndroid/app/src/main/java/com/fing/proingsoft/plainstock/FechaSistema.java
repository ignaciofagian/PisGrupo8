package com.fing.proingsoft.plainstock;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.fing.proingsoft.plainstock.otros.Configuracion;
import com.fing.proingsoft.plainstock.otros.PlainStockDataSource;
import com.fing.proingsoft.plainstock.otros.PlainStockWS;
import com.fing.proingsoft.plainstock.otros.SaldoUpdateTimer;

import java.sql.Time;
import java.util.GregorianCalendar;

public class FechaSistema extends AppCompatActivity {

    private Configuracion conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.titulo_fecha_sistema);
        setContentView(R.layout.activity_fecha_sistema);
        conf = Configuracion.getInstance();

        View boton_back = findViewById(R.id.fs_back_button);
        boton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentConfig = new Intent(v.getContext(), PantallaConfiguracion.class);
                startActivity(intentConfig);
            }
        });

        View boton_aceptar = findViewById(R.id.bt_config_cambiarFecha);
        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFechaSistema(v);
            }
        });

        DatePicker dp = (DatePicker)findViewById(R.id.datePicker);
        dp.setCalendarViewShown(false);
        dp.setSpinnersShown(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fecha_sistema, menu);
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

    public void cambiarFechaSistema(View v){
        int year, month, day;
        GregorianCalendar nuevaFecha;
        final Context c = v.getContext();

        //Obtengo datos del DatePicker
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        year = datePicker.getYear();
        month = datePicker.getMonth();
        day = datePicker.getDayOfMonth();
        nuevaFecha = new GregorianCalendar(year,month,day);

        //Reinicio simulacion en modo maquina del tiempo
        conf.reiniciarSimulacion(nuevaFecha);
        String msg = this.getString(R.string.str_informacion_maquina_del_tiempo) +" "+ nuevaFecha.getTime().toString();

        AlertDialog.Builder alert = new AlertDialog.Builder(FechaSistema.this);
        alert.setTitle("Info");
        alert.setMessage(msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intentConfig = new Intent(c, PantallaConfiguracion.class);
                startActivity(intentConfig);
            }
        });
        alert.show();
        Intent intent = new Intent(this,Preguntas.class);
        startActivity(intent);
    }

}
