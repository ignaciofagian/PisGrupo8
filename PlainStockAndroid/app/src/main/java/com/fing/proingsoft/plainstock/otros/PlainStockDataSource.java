package com.fing.proingsoft.plainstock.otros;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PlainStockDataSource {
    private SQLiteDatabase database;
    private PlainStockDbHelper helper;
    public static enum RANGO_GRAFICA {DIA,SEMANA,SEMANA2,MES,MES6,ANO,ANO5,TODO};

    public PlainStockDataSource(Context context){
        helper = new PlainStockDbHelper(context);
        database = helper.getWritableDatabase();
    }

    public void updateModo(int modo, GregorianCalendar fecha){
        SimpleDateFormat dateF = new SimpleDateFormat(Configuracion.dateFromat);
        String fechaStr = dateF.format(fecha.getTime());
        String modoStr = ((Integer) modo).toString();
        String sql = "delete from conf_table";
        database.execSQL(sql);
        sql = "insert into conf_table (modo_conf,date_conf) values (\"" + modoStr + "\",\"" + fechaStr + "\")";
        database.execSQL(sql);
    }

    public Pair<Integer,GregorianCalendar> quertModo(){
        Integer modo = -1;
        GregorianCalendar fecha = null;
        try{
            String query =  "modo_conf as modo, date_conf as date limit 1";
            Cursor c = database.rawQuery(query, null);
            c.moveToNext();
            modo = c.getInt(0);
            SimpleDateFormat dateF = new SimpleDateFormat(Configuracion.dateFromat);
            fecha = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            fecha.setTime(dateF.parse(c.getString(1)));
        }
        catch(Exception e){}
        return new Pair<>(modo,fecha);

    }

    public void borrarSaldos(Context context){
        String sql = "delete from saldo_table";
        database.execSQL(sql);
    }

    public int querySaldo(){
        String query =  "select saldo_saldo from saldo_table \n" +
                        "order by date_saldo desc \n" +
                        "limit 1";
        Cursor c = database.rawQuery(query, null);
        if(c.moveToNext()){
            return c.getInt(0);
        }
        else{
            return -1;
        }
    }

    public GregorianCalendar fechaUltimoSaldo(){
        GregorianCalendar res=null;
        String query =  "select max(date_saldo) from saldo_table";
        try{
            Cursor c = database.rawQuery(query, null);
            if(c.moveToNext()){
                String fechaStr = c.getString(0);
                SimpleDateFormat dateF = new SimpleDateFormat(Configuracion.dateFromat);
                Date date = dateF.parse(fechaStr);
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(date);
            }
            else{
                Configuracion conf = Configuracion.getInstance();
                res = conf.getPrincipioDeLosTiempos();
            }
        }
        catch(Exception e){

        }
        return res;
    }

    public void insertSaldo(GregorianCalendar fecha, int saldo){
        SimpleDateFormat dateF = new SimpleDateFormat(Configuracion.dateFromat);
        String fechaStr = dateF.format(fecha.getTime());
        String saldoStr = ((Integer) saldo).toString();
        String sql = "insert into saldo_table (date_saldo,saldo_saldo) values (\""+ fechaStr +"\",\"" +saldoStr+"\")";
        database.execSQL(sql);
    }

    public ArrayList<Pair> querySaldo(RANGO_GRAFICA rango, GregorianCalendar fechaFinal){
        String fechaInicialStr, fechaFinalStr, query;
        GregorianCalendar fechaInicial=null, fecha=null;
        SimpleDateFormat dateF = new SimpleDateFormat(Configuracion.dateFromat);
        dateF.setTimeZone(TimeZone.getTimeZone("UTC"));
        ArrayList<Pair> res = new ArrayList<>();
        Cursor c = null;
        int salto=1, fieldSalto=Calendar.YEAR;

        fechaFinal.set(Calendar.SECOND,0);
        fechaFinal.set(Calendar.MILLISECOND,0);

        switch (rango){
            case DIA:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.DAY_OF_MONTH,-1);
                fieldSalto=Calendar.MINUTE;
                salto=2;
                break;
            case SEMANA:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.DAY_OF_MONTH,-7);
                fieldSalto=Calendar.MINUTE;
                salto=20;
                break;
            case SEMANA2:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.DAY_OF_MONTH,-14);
                fieldSalto=Calendar.MINUTE;
                salto=30;
                break;
            case MES:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.MONTH,-1);
                fieldSalto=Calendar.HOUR;
                salto=1;
                break;
            case MES6:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.MONTH,-6);
                fieldSalto=Calendar.HOUR;
                salto=12;
                break;
            case ANO:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.YEAR,-1);
                fieldSalto=Calendar.DAY_OF_MONTH;
                salto=1;
                break;
            case ANO5:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.YEAR,-5);
                fieldSalto=Calendar.DAY_OF_MONTH;
                salto=5;
                break;
            case TODO:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.YEAR,-5);
                fieldSalto=Calendar.DAY_OF_MONTH;
                salto=5;
                break;
        }

        fecha=(GregorianCalendar)fechaInicial.clone();
        while (fecha.before(fechaFinal)){
            fecha.add(fieldSalto,salto);
            fechaInicialStr =dateF.format(fechaInicial.getTime());
            fechaFinalStr = dateF.format(fecha.getTime());
            query = "select date_saldo as fecha, saldo_saldo as saldo " +
                    "from saldo_table " +
                    "where date_saldo >= \"" + fechaInicialStr + "\" " +
                    " and date_saldo < \"" + fechaFinalStr + "\" "+
                    " order by date_saldo desc" +
                    " limit 1";
            try {
                c = database.rawQuery(query, null);
                if (c.moveToNext()) {
                    res.add(new Pair(c.getString(0), c.getInt(1)));
                } else {
                    //VER CASOS
                }
            }
            finally {
                // hay que cerrar el cursor sino eventualmente se cae
                if (c != null) c.close();
            }
            fechaInicial.add(fieldSalto,salto);
        }

        return res;
    }

    private Pair cargarPair(Cursor c){
            return  new Pair(c.getString(0), c.getInt(1));

    }

    /**
     * Metodo alternativo para saltear elementos en la consulta
     * Tomo el cursor en vez de una lista porque cargar primero la lista entera  podría usar demasiada memoria
     * El cursor es cerrado al final de la operación
     * @param cursor cursor retornado por la BD con primer elemento fecha como texto ordenado DESC
     * @param salto salto entero positivo
     * @param fieldSalto TimeUnit que representa la unidad en la que esta expresada el salto
     * @param dateF formato de fecha de la BD
     * @return lista de resultados filtrada en el mismo orden
     */
    private ArrayList<Pair> seleccionar(Cursor cursor,long salto,TimeUnit fieldSalto,SimpleDateFormat dateF){

        ArrayList<Pair> resultado = new ArrayList<>();

        try{
            if (!cursor.moveToNext()) return resultado; // si no tengo resultados devuelvo una lista vacia
            Pair pairAnterior = cargarPair(cursor);

            Date fechaAnterior =  dateF.parse((String) pairAnterior.first);
            Date ultimaAgregada = fechaAnterior;
            long saltoEnMilis = fieldSalto.toMillis(salto);

            while(cursor.moveToNext()){
                Pair p = cargarPair(cursor);
                Date actual =  dateF.parse((String)p.first);
                // si la diferencia excede el salto agrego el par anterior que haya procesado
                // es para ser robusto ante alguna diferencia de tiempo que exceda ligeramente el salto
                long  diff = actual.getTime() - ultimaAgregada.getTime();
                if (saltoEnMilis > diff){
                    if (fechaAnterior != ultimaAgregada) {
                        // agrego al resultado
                        resultado.add(pairAnterior);
                        ultimaAgregada = fechaAnterior;
                    }
                }
                fechaAnterior = actual;
                pairAnterior = p;
            }
            // agrego ultimo elemento si es distinto (incondicionalmente de la diferencia)
            if (fechaAnterior != ultimaAgregada){
                resultado.add(pairAnterior);
            }
        }
        catch (ParseException e){

        }
        finally {
            cursor.close();
        }
        return resultado;

    }



}
