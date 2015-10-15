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
        //BORRAR
        String sql0;

        //Crear la base de datos
        String sql= "create table saldo_table("+
                "id_saldo integer primary key autoincrement,"+
                "date_saldo datetime not null,"+
                "saldo_saldo integer not null"+
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Actualizar la base de datos
    }
}
