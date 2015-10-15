package com.fing.proingsoft.plainstock;

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

public class Detalles extends ActividadConMenuInf {

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
        Configuracion conf = Configuracion.getInstance();
        ArrayList<Pair> queryResult = dataSource.querySaldo(conf.getRango(), conf.getFechaSistema());

        int cant = queryResult.size();
        aumento = 0;
        saldo2 = (Integer)queryResult.get(0).second;
        ArrayList<String> lista = new ArrayList<>();
        for (int i = 0; i < cant; i++) {
            saldo = (Integer)queryResult.get(cant-i-1).second;
            aumento = (saldo*100/saldo2) -100;
            tiempo = (String)queryResult.get(i).first;
            entrada = tiempo + "  " + String.format("%.2f", aumento) + "%   $"  + ((Integer) saldo).toString();
            saldo2 = saldo;
            lista.add(i,entrada);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista);
        lista_historia.setAdapter(adapter);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getBtnMenuActivoId() {
        return R.id.btDetalles;
    }
}
