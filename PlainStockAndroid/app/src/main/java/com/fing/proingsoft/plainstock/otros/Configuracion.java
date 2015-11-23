package com.fing.proingsoft.plainstock.otros;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import android.widget.Toast;

import com.fing.proingsoft.plainstock.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Configuracion {

    public static final int TIEMPO_ACTUALIZACION_SALDO = 120000;
    public static final String BASE_URL_SERVIDOR = "http://52.88.80.212:8080/Servidor/rest/app";
    public static String dateFromat="yyyy-MM-dd HH:mm";
    public static final int MODO_TIEMPO_REAL =  0;
    public static final int MODO_MAQUINA_DEL_TIEMPO =  1;

    private PlainStockDataSource.RANGO_GRAFICA rango;
    private int deltaDay;
    private static Configuracion instance;
    private GregorianCalendar principioDeLosTiempos,fechaMaquinaDelTiempo;
    private Context context;
    private String horaAperturaMercado, horaCierreMercado;
    private int modoSimulacion;
    private boolean perdio;

    public boolean perdio(){
        return perdio;
    }

    public void setPerdio(boolean b){
        perdio = b;
    }

    private Configuracion(){
        //CARGAR DATOS DE CONFIGuRACION INICIALES
        deltaDay=0;
        principioDeLosTiempos = new GregorianCalendar(1970,00,01);
        rango = PlainStockDataSource.RANGO_GRAFICA.DIA;
        //Hora de apertura y cierre de mercado (UTC)
        horaAperturaMercado = "14:30";
        horaCierreMercado = "21:00";
    }


    public void inicializar(Activity activity,int modo, GregorianCalendar fecha){
        this.context = activity.getApplicationContext();
        if (this.getStoredLangCode() == null){
            // uso el codigo del sistema
            setLanguage("", "");
        }
        modoSimulacion = modo;
        fechaMaquinaDelTiempo = fecha;
    }



    public static Configuracion getInstance(){
        if(instance==null){
            instance = new Configuracion();
        }
        return instance;
    }

    public GregorianCalendar getFechaSistema() {
        GregorianCalendar fecha=null;

        if(modoSimulacion==MODO_MAQUINA_DEL_TIEMPO){
            fecha = fechaMaquinaDelTiempo;
        }else{
            fecha = new GregorianCalendar();
            fecha.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        return fecha;
    }

    public void setFechaSistema(GregorianCalendar nuevaFecha){
        if(modoSimulacion==MODO_MAQUINA_DEL_TIEMPO){
            fechaMaquinaDelTiempo = (GregorianCalendar)nuevaFecha.clone();
            PlainStockDataSource dataSource = new PlainStockDataSource(context);
            dataSource.updateModo(MODO_MAQUINA_DEL_TIEMPO,nuevaFecha);
        }
    }

    public PlainStockDataSource.RANGO_GRAFICA getRango() {
        return rango;
    }

    public GregorianCalendar getPrincipioDeLosTiempos() {
        return principioDeLosTiempos;
    }

    public String getStoredLangCode(){
        final SharedPreferences prefs = context
                .getSharedPreferences("pref.xml", 0);
        final String code = prefs.getString("langCode", null);
        return code;
    }

    public String getLangCode(){
        String stored = getStoredLangCode();
        if (stored == null | stored.isEmpty()){
            Locale def = getDefaultLang();
            return def.getLanguage();
        }
        else {
            return stored;
        }
    }


    private Locale getDefaultLang(){
        // Demeter no le puede ganar a Android
        return context.getResources().getSystem().getConfiguration().locale;
    }

    public String getStoredLangName(){
        final SharedPreferences prefs = context
                .getSharedPreferences("pref.xml", 0);
        final String nombre = prefs.getString("langName", null);

        return nombre;
    }

    public void setLanguage(String code, String nombre){
        SharedPreferences prefs = context
                .getSharedPreferences("pref.xml", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("langCode",code);
        edit.putString("langName",nombre);
        edit.apply();

    }

    public void setReiniciarDatos(boolean  reiniciar){
        SharedPreferences prefs = context
                .getSharedPreferences("pref.xml", 0);
         prefs.edit().putBoolean("reiniciarDatos",reiniciar).apply();
    }

    public boolean getReiniciarDatos(){
        SharedPreferences prefs = context
                .getSharedPreferences("pref.xml", 0);
        return  prefs.getBoolean("reiniciarDatos",false);
    }

    //Reinicia simulacion en modo tiempo real
    public void reiniciarSimulacion() {
        PlainStockWS webService = new PlainStockWS();
        webService.resetCliente(context);
        PlainStockDataSource dataSource = new PlainStockDataSource(context);
        dataSource.borrarSaldos(context);
        SaldoUpdateTimer.getInstance(context).restart();
        modoSimulacion  = MODO_TIEMPO_REAL;
    }

    //Reinicia simulacion en modo maquina del tiempo
    public void reiniciarSimulacion(GregorianCalendar fecha) {

        //Indico al server inicio de modo maquina del tiempo
        String strNuevaFecha = Integer.toString(fecha.get(Calendar.YEAR)) + "-"
                + Integer.toString(fecha.get(Calendar.MONTH)+1) + "-" + Integer.toString(fecha.get(Calendar.DAY_OF_MONTH));
        PlainStockWS webService = new PlainStockWS();
        webService.maquinaTiempo(context,strNuevaFecha);

        //Reinicio en el dispositivo
        PlainStockDataSource dataSource = new PlainStockDataSource(context);
        dataSource.borrarSaldos(context);
        SaldoUpdateTimer.getInstance(context).stop();
        modoSimulacion  = MODO_MAQUINA_DEL_TIEMPO;

        //Seteo nueva fecha en el sistema
        PlainStockWS ws = new PlainStockWS();
        Pair<GregorianCalendar,Integer> act = ws.saldoActualWS2(context);
        setFechaSistema(act.first);

        //Obtengo un saldo inicial para la base ya que el timer no va a estar funcionando
        ws.actualizarSaldoHistorico(context, getFechaSistema());
    }

    public void goToNextDate(int year, int month, int day){
        PlainStockWS webService = new PlainStockWS();
        GregorianCalendar fechaHasta = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        fechaHasta.set(Calendar.YEAR,year);
        fechaHasta.set(Calendar.MONTH,month);
        fechaHasta.set(Calendar.DAY_OF_MONTH,day);
        String strNuevaFecha = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
        webService.goToNextDate(context, strNuevaFecha);
        Pair<GregorianCalendar,Integer> act = webService.saldoActualWS2(context);
        setFechaSistema(act.first);
        if(perdio){
            Toast.makeText(context, R.string.str_simulation_over, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isTimeMachineEnabled() {
        return (modoSimulacion == MODO_MAQUINA_DEL_TIEMPO);
    }

    public void setRango(PlainStockDataSource.RANGO_GRAFICA rango) {
        this.rango = rango;
    }


}
