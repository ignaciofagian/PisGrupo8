package com.fing.proingsoft.plainstock;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.provider.Settings.Secure;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fing.proingsoft.plainstock.otros.Configuracion;
import com.fing.proingsoft.plainstock.otros.PlainStockDataSource;
import com.fing.proingsoft.plainstock.otros.PlainStockWS;
import com.fing.proingsoft.plainstock.otros.SaldoUpdateTimer;

import org.json.JSONArray;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private Button _btComenzar;
    private UUID android_id; // declared on top of



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
       getSupportActionBar().setCustomView(R.layout.titulobarra);


       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF28AF00));
        //getSupportActionBar().setTitle();
        setContentView(R.layout.activity_main);

        PlainStockWS plainStockWS = new PlainStockWS();
        plainStockWS.registrarDispositivo(this);
        PlainStockDataSource dataSource = new PlainStockDataSource(this);
        Pair<Integer,GregorianCalendar> p = dataSource.quertModo();
        int modo = Configuracion.MODO_TIEMPO_REAL;
        GregorianCalendar fecha = null;
        if(p.first == Configuracion.MODO_MAQUINA_DEL_TIEMPO){
            modo = Configuracion.MODO_MAQUINA_DEL_TIEMPO;
        }
        else{
            SaldoUpdateTimer.getInstance(this);
            plainStockWS.actualizarSaldoHistorico(this,Configuracion.getInstance().getFechaSistema());
        }
        Configuracion.getInstance().inicializar(this,modo,fecha);
        Configuracion.getInstance().setPerdio(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                break;
            // This doesn't work?

        }
        return super.onOptionsItemSelected(item);
    }





    public void abrirPantalla(View v){
        Intent intent = new Intent(this,Preguntas.class);
        startActivity(intent);
    }
}
