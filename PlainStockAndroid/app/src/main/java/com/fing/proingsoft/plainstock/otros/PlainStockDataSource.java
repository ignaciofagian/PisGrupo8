package com.fing.proingsoft.plainstock.otros;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PlainStockDataSource {
    private SQLiteDatabase database;
    private PlainStockDbHelper helper;
    public static enum RANGO_GRAFICA {DIA,SEMANA,SEMANA2,MES,MES6,ANO,ANO5};
    private String dateFromat="yyyy-MM-dd HH:mm";

    public PlainStockDataSource(Context context){
        helper = new PlainStockDbHelper(context);
        database = helper.getWritableDatabase();
    }

    public int querySaldo(){
        String query =  "select saldo_saldo from saldo_table \n" +
                        "order by date_saldo desc \n" +
                        "limit 1";
        Cursor c = database.rawQuery(query, null);
        c.moveToNext();
        return c.getInt(0);
    }

    public void insertSaldo(GregorianCalendar fecha, int saldo){
        SimpleDateFormat dateF = new SimpleDateFormat(dateFromat);
        String fechaStr = dateF.format(fecha.getTime());
        String saldoStr = ((Integer) saldo).toString();
        String sql = "insert into saldo_table (date_saldo,saldo_saldo) values (\""+ fechaStr +"\",\"" +saldoStr+"\")";
        database.execSQL(sql);
    }

    public ArrayList<Pair> querySaldo(RANGO_GRAFICA rango, GregorianCalendar fechaFinal){
        String fechaInicialStr, fechaFinalStr, query;
        GregorianCalendar fechaInicial=null, fecha=null;
        SimpleDateFormat dateF = new SimpleDateFormat(dateFromat);
        ArrayList<Pair> res = new ArrayList<>();
        Cursor c;
        int salto=1, fieldSalto=Calendar.YEAR;

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
                //Definir salto
                break;
            case SEMANA2:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.DAY_OF_MONTH,-14);
                //Definir salto
                break;
            case MES:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.MONTH,-1);
                //Definir salto
                break;
            case MES6:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.MONTH,-6);
                //Definir salto
                break;
            case ANO:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.YEAR,-1);
                //Definir salto
                break;
            case ANO5:
                fechaInicial = (GregorianCalendar)fechaFinal.clone();
                fechaInicial.add(Calendar.YEAR,-5);
                //Definir salto
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
            c = database.rawQuery(query, null);
            if(c.moveToNext()){
                res.add(new Pair(c.getString(0),c.getInt(1)));
            }
            else{
                //VER CASOS
            }
            fechaInicial.add(fieldSalto,salto);
        }

        return res;
    }



}
