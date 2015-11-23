package com.fing.proingsoft.plainstock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fing.proingsoft.plainstock.otros.Configuracion;
import com.fing.proingsoft.plainstock.otros.PlainStockDataSource;
import com.fing.proingsoft.plainstock.otros.PlainStockWS;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PantallaConfiguracion
        extends ActividadConMenuInf
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Configuracion c;
    private TextView fechaActualText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = Configuracion.getInstance();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tituloconfiguracion);
        setContentView(R.layout.activity_pantalla_configuracion);
        Spinner sp = (Spinner)findViewById(R.id.spnIdiomas);
        sp.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.idiomas_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

        sp.setAdapter(adapter);
        if (c.getStoredLangCode().isEmpty()){
            sp.setSelection(0);
        }
        else{
            sp.setSelection(adapter.getPosition(c.getStoredLangName()));
        }


        this.fechaActualText = (TextView)findViewById(R.id.tvConfig_FechaActual);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        fechaActualText.setText(df.format(c.getFechaSistema().getTime()));

        findViewById(R.id.bt_config_reiniciar).setOnClickListener(this);
        findViewById(R.id.bt_config_cambiarFecha).setOnClickListener(this);
        findViewById(R.id.btInfoReiniciar).setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pantalla_configuracion, menu);
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
    @Override
    public int getBtnMenuActivoId() {
        return R.id.btConfig;
    }


    public void onItemSelected(AdapterView<?> arg0, View v, int position, long arg3) {
        Log.v("Value", "Language Value: " + position);

        if(position == 0){
            setLocale("","");
        }
        else if (position == 1){
            setLocale("en","English");
        }
        else if (position == 2){
            setLocale("es","Español");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //http://stackoverflow.com/questions/7305928/load-different-strings-xml-resource-on-spinner-change
    private void setLocale(String localeCode,String localeName){
        if (localeCode.equalsIgnoreCase(c.getStoredLangCode())){
            // si no cambie el idioma salgo
            return;
        }
        // guardo el idioma en las preferencias
        c.setLanguage(localeCode, localeName);

        if (localeCode.isEmpty()) localeCode = c.getLangCode(); // si es vacio tomo el por defecto
        // cambio el idioma en la aplicacion
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        this.recreate();
    }

    private void mostrarToast(int strRes,int duration) {
        Toast msg = Toast.makeText(PantallaConfiguracion.this, strRes, duration);
        msg.setGravity(Gravity.CENTER, 0, 0);
        msg.show();
    }

    @Override
    public void onClick(View view) {
        // provisional
        int id = view.getId();
        if (id == R.id.bt_config_cambiarFecha) {
            Intent intentDetalle = new Intent(this,FechaSistema.class);
            startActivity(intentDetalle);
        }
        else if (id == R.id.bt_config_reiniciar) {
            c.reiniciarSimulacion();
            mostrarToast(R.string.str_informacion_reinicio_completo,Toast.LENGTH_SHORT);
            this.recreate();
        }
        else if (id == R.id.btInfoReiniciar){
            mostrarToast(R.string.str_info_reiniciar,Toast.LENGTH_SHORT);
        }
    }

}
