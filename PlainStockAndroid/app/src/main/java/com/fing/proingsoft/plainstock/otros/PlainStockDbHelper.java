package com.fing.proingsoft.plainstock.otros;

import android.content.Context;
import android.database.sqlite.*;

public class PlainStockDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="plainstock.db";
    private static final int DATABASE_VERSION = 1;


    public PlainStockDbHelper(Context context){
        super(context,
                DATABASE_NAME,//String name
                null,//factory
                DATABASE_VERSION//int version
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        //Crear la base de datos
        sql= "create table saldo_table("+
                "id_saldo integer primary key autoincrement,"+
                "date_saldo datetime not null,"+
                "saldo_saldo integer not null"+
                ")";
        db.execSQL(sql);
        db.execSQL("CREATE INDEX date_saldo_idx ON saldo_table(date_saldo); ");
        sql= "create table conf_table("+
                "modo_conf integer,"+
                "date_conf datetime"+
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Actualizar la base de datos
    }
}
