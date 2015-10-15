package com.fing.proingsoft.plainstock.otros;


import java.util.GregorianCalendar;

public class Configuracion {
    //
    public static final String WS_SALDO = "";
    public static final int TIEMPO_ACTUALIZACION_SALDO = 120000;
    public static final String BASE_URL_SERVIDOR = "http://52.88.80.212:8080/Servidor/rest/app";
    public static String dateFromat="yyyy-MM-dd HH:mm";

    private PlainStockDataSource.RANGO_GRAFICA rango = PlainStockDataSource.RANGO_GRAFICA.DIA;
    private GregorianCalendar fechaSistema = new GregorianCalendar();
    private static Configuracion instance;

    private Configuracion(){
        //CARGAR DATOS D CONFIGuRACION INICIALES
    }

    public static Configuracion getInstance(){
        if(instance==null){
            instance = new Configuracion();
        }
        return instance;
    }

    public GregorianCalendar getFechaSistema() {
        return fechaSistema;
    }

    public PlainStockDataSource.RANGO_GRAFICA getRango() {
        return rango;
    }
}
