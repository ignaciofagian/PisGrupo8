package com.fing.proingsoft.plainstock;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.fing.proingsoft.plainstock.interfaces.MenuInferiorListener;

public  class ActividadConMenuInf extends AppCompatActivity implements MenuInferiorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_con_menu_inf);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actividad_con_menu_inf, menu);
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

    protected  void cambiarActivityMenu(View boton){
        switch ( boton.getId()){
            case R.id.btPreguntas:
                Intent intentPreguntas = new Intent(this, Preguntas.class);
                startActivity(intentPreguntas);
                break;
            case R.id.btDetalles:
                Intent intentDetalle = new Intent(this,Detalles.class);
                startActivity(intentDetalle);
                break;
            case R.id.btEvolucion:
                Intent intentEvolucion = new Intent(this, Evolucion.class);
                startActivity(intentEvolucion);
                break;
            case R.id.btConfig:
                Intent intentConfig = new Intent(this, PantallaConfiguracion.class);
                startActivity(intentConfig);
        }
    }



    @Override
    public void MenuInferiorOnClick(View boton) {
        cambiarActivityMenu(boton);
    }

    @Override
    public int getBtnMenuActivoId() {
        return R.id.btPreguntas;
    }
}
