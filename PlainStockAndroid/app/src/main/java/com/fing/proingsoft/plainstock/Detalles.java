package com.fing.proingsoft.plainstock;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fing.proingsoft.plainstock.otros.Configuracion;
import com.fing.proingsoft.plainstock.otros.PlainStockDataSource;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collections;

public class Detalles extends ActividadConMenuInf {
    private Configuracion conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView lista_historia;
        int saldo, saldo2;
        float aumento;
        String tiempo, entrada;

        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.titulodetalles);
        setContentView(R.layout.activity_detalles2);

        lista_historia = (ListView)findViewById(R.id.lista_historia);

        PlainStockDataSource dataSource = new PlainStockDataSource(this);
        conf = Configuracion.getInstance();
        ArrayList<Pair> queryResult = dataSource.querySaldo(conf.getRango(), conf.getFechaSistema());
        Collections.reverse(queryResult);

        int cant = queryResult.size();
        aumento = 0;
        ArrayList<String> lista = new ArrayList<>();
        for (int i = 0; i < cant; i++) {
            saldo = (Integer)queryResult.get(i).second;
            if(i== cant-1){
                aumento=0;
            }
            else {
                saldo2 = (Integer)queryResult.get(i+1).second;
                aumento = ((((float)saldo)*100)/saldo2) -100;
            }
            tiempo = (String)queryResult.get(i).first;
            String[] aux;
            aux = tiempo.split(" ");


            entrada = tiempo + "           " + String.format("%.2f", aumento) + "%            $"  + ((Integer) saldo).toString() + "             ";
            lista.add(i,entrada);
        }
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, R.layout.texto_centrado, R.id.text1, lista);


        lista_historia.setAdapter(ad);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalles, menu);
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
        return R.id.btDetalles;
    }
}
