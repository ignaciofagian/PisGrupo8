package com.fing.proingsoft.plainstock.otros;

import android.content.Context;
import android.util.Pair;
import android.widget.Toast;

import com.fing.proingsoft.plainstock.R;

import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class SaldoUpdateTimer extends Timer {
    private Context context;
    private TimerTask taskSaldo;
    private static SaldoUpdateTimer instance = null;

    private SaldoUpdateTimer(Context context){
        super();
        this.context=context;
        taskSaldo = new saldoTimerTask(context);
        this.scheduleAtFixedRate(taskSaldo,0,Configuracion.TIEMPO_ACTUALIZACION_SALDO);

    }

    public void restart(){
        instance = new SaldoUpdateTimer(context);
        taskSaldo = new saldoTimerTask(context);
        instance.scheduleAtFixedRate(taskSaldo,0,Configuracion.TIEMPO_ACTUALIZACION_SALDO);
    }

    public void stop(){
        try{
            this.cancel();
            this.purge();
        }
        catch(Exception e){
            e.getMessage();
        }
    }

    public static SaldoUpdateTimer getInstance(Context context) {
        if(instance == null){
            instance = new SaldoUpdateTimer(context);
        }
        return instance;
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
            if(pair !=null) {
                dataSource.insertSaldo(pair.first, pair.second);
            }
            if(Configuracion.getInstance().perdio()){
                Toast.makeText(context, R.string.str_simulation_over, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void stopSaldoUpdater(){
        try {
            taskSaldo.wait(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifiSaldoUpdater(){
        taskSaldo.notify();
    }
}
