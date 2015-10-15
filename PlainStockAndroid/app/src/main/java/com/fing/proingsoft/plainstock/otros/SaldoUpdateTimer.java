package com.fing.proingsoft.plainstock.otros;

import android.content.Context;
import android.util.Pair;

import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class SaldoUpdateTimer extends Timer {
    private Context context;

    public SaldoUpdateTimer(Context context){
        super();
        this.context=context;
        TimerTask task = new saldoTimerTask(context);
        this.scheduleAtFixedRate(task,0,Configuracion.TIEMPO_ACTUALIZACION_SALDO);
    }

    public class saldoTimerTask extends TimerTask{
        private Context context;
        public saldoTimerTask(Context context){
            this.context=context;
        }

        @Override
        public void run() {
            Pair<GregorianCalendar,Integer> pair = PlainStockWS.saldoActualWS(this.context);
            PlainStockDataSource dataSource = new PlainStockDataSource(this.context);
            dataSource.insertSaldo(pair.first,pair.second);
        }
    }
}
