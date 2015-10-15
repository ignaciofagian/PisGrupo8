package com.fing.proingsoft.plainstock;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.provider.Settings.Secure;
import android.widget.ListView;
import android.widget.TextView;

import com.fing.proingsoft.plainstock.otros.PlainStockWS;
import com.fing.proingsoft.plainstock.otros.SaldoUpdateTimer;

import java.util.ArrayList;
import java.util.UUID;

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
        SaldoUpdateTimer sutimer= new SaldoUpdateTimer(this);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirPantalla(View v){
        Intent intent = new Intent(this,Preguntas.class);
        startActivity(intent);
    }
}
