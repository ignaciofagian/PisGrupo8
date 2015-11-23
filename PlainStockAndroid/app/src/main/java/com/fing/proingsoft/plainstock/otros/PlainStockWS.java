package com.fing.proingsoft.plainstock.otros;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.fing.proingsoft.plainstock.DeviceUuidFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.TimeZone;


public class PlainStockWS {
    private static String WS_OBJETO = "OBJETO";
    private static String WS_ARRAY = "ARRAY";

    public static Pair<GregorianCalendar,Integer> saldoActualWS(Context context){
        Pair result = null;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/saldo?id="+idStr;
            JSONObject jsonObject = (JSONObject)requestWebService(urlStr,WS_OBJETO);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configuracion.dateFromat);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = simpleDateFormat.parse(jsonObject.getString("tiempo"));
            GregorianCalendar fecha = new GregorianCalendar();
            fecha.setTime(date);
            Integer saldo = Integer.parseInt(jsonObject.getString("saldo"));

            String perdioStr = jsonObject.getString("l");
            if(perdioStr=="1"){
                Configuracion.getInstance().setPerdio(true);
            }


            result = new Pair<>(fecha,saldo);
        }
        catch (Exception e){
            e.getMessage();
        }
        return result;
    }

    public JSONObject resetCliente(Context context){
        JSONObject res = null;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
//            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/resetCliente?id="+idStr;
//            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr,WS_OBJETO);
//            res = (JSONObject) asyncTask.get();
            //TEMPORAL HASTA QUE SE IMPLEMENTE reset
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/borrarCliente?id="+idStr;
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr, WS_OBJETO);
            res = (JSONObject) asyncTask.get();
            String urlStr2 = Configuracion.BASE_URL_SERVIDOR+"/registrar?id="+idStr;
            AsyncTask asyncTask2 = new requestWebServiceAsync().execute(urlStr2, WS_OBJETO);
            res = (JSONObject) asyncTask2.get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public JSONObject maquinaTiempo(Context context, String fechaStr){
        JSONObject res = null;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr2 = Configuracion.BASE_URL_SERVIDOR+"/maquinatiempo?iden="+idStr+"&date="+fechaStr;
            AsyncTask asyncTask2 = new requestWebServiceAsync().execute(urlStr2, WS_OBJETO);
            res = (JSONObject) asyncTask2.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Pair<GregorianCalendar,Integer> saldoActualWS2(Context context){
        Pair<GregorianCalendar,Integer> res = null;
        JSONObject jsObj;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr2 = Configuracion.BASE_URL_SERVIDOR+"/saldo?id="+idStr;
            AsyncTask asyncTask2 = new requestWebServiceAsync().execute(urlStr2, WS_OBJETO);
            jsObj = (JSONObject) asyncTask2.get();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configuracion.dateFromat);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = simpleDateFormat.parse(jsObj.getString("tiempo"));
            GregorianCalendar fecha = new GregorianCalendar();
            fecha.setTime(date);
            int saldo = jsObj.getInt("saldo");
            String perdioStr = jsObj.getString("l");
            if(perdioStr=="1"){
                Configuracion.getInstance().setPerdio(true);
            }

            res = new Pair<>(fecha,saldo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public JSONArray goToNextDate(Context context, String fechaStr){
        JSONArray res = null;
        Date date;
        JSONObject item;
        int saldo;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr2 = Configuracion.BASE_URL_SERVIDOR+"/avanzar?id="+idStr+"&date="+fechaStr;
            AsyncTask asyncTask2 = new requestWebServiceAsync().execute(urlStr2, WS_ARRAY);
            res = (JSONArray) asyncTask2.get();
            int count = res.length();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configuracion.dateFromat);
            simpleDateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
            PlainStockDataSource dataSource = new PlainStockDataSource(context);
            GregorianCalendar fecha = new GregorianCalendar();
            for(int i = 0;i<count;i++){
                item = (JSONObject)res.get(i);
                date = simpleDateFormat.parse(item.getString("tiempo"));
                fecha.setTime(date);
                saldo = Integer.parseInt(item.getString("saldo"));
                dataSource.insertSaldo(fecha,saldo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public JSONArray saldoHistorico(Context context){

        JSONArray res = null;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/saldos?id="+"idStr";
            //no me importa si ya estoy registrado o no
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr, WS_ARRAY);
            res = (JSONArray) asyncTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void avanzar(Context context,GregorianCalendar fechaHasta){
        Date date;
        JSONArray jsonArray = null;
        JSONObject item;
        GregorianCalendar fecha;
        int saldo;
        PlainStockDataSource dataSource = new PlainStockDataSource(context);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configuracion.dateFromat);
        simpleDateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String hasta=sdf.format(fechaHasta.getTime());
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/avanzar?id="+idStr+"&date="+hasta;
            Configuracion.getInstance().setFechaSistema(fechaHasta);
            //no me importa si ya estoy registrado o no
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr, WS_ARRAY);
            jsonArray = (JSONArray) asyncTask.get();
            int count = jsonArray.length();
            for(int i = 0;i<count;i++){
                item = (JSONObject)jsonArray.get(i);
                date = simpleDateFormat.parse(item.getString("tiempo"));
                fechaHasta.setTime(date);
                saldo = Integer.parseInt(item.getString("saldo"));
                dataSource.insertSaldo(fechaHasta,saldo);
                String perdioStr = item.getString("l");
                if(perdioStr=="1"){
                    Configuracion.getInstance().setPerdio(true);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return res;
    }

    //Actualiza desde la ultima fecha en la base hasta 'fechaHasta'
    public void actualizarSaldoHistorico(Context context, GregorianCalendar fechaHasta){
        Date date;
        JSONObject item;
        GregorianCalendar fecha;
        int saldo;
        PlainStockDataSource dataSource = new PlainStockDataSource(context);
        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
        try {
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configuracion.dateFromat);
            simpleDateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
            GregorianCalendar fechaUltimoSaldo = dataSource.fechaUltimoSaldo();
            if(fechaUltimoSaldo == null){
                fechaUltimoSaldo = Configuracion.getInstance().getPrincipioDeLosTiempos();
            }
            String fechaInicioStr = simpleDateFormat.format(fechaUltimoSaldo.getTime()).substring(0, 10);
            String fechaFinStr = simpleDateFormat.format(fechaHasta.getTime()).substring(0, 10);
            String urlStr = Configuracion.BASE_URL_SERVIDOR + "/saldos2?id=" + idStr + "&desde=" + fechaInicioStr + "&hasta=" + fechaFinStr;
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr, WS_ARRAY);
            JSONArray jsonArray = (JSONArray) asyncTask.get();
            int count = jsonArray.length();
            for(int i = 0;i<count;i++){
                item = (JSONObject)jsonArray.get(i);
                date = simpleDateFormat.parse(item.getString("tiempo"));
                fechaHasta.setTime(date);
                saldo = Integer.parseInt(item.getString("saldo"));
                dataSource.insertSaldo(fechaHasta,saldo);

                String perdioStr = item.getString("l");
                if(perdioStr=="1"){
                    Configuracion.getInstance().setPerdio(true);
                }

            }
        }
        catch (Exception e){}
    }

    public JSONArray obtenerPreguntasGenerales(Context context){
        JSONArray res = null;
        try {
            //DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            //String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/preguntas/obtenerPreguntasGenerales";
            //no me importa si ya estoy registrado o no
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr, WS_ARRAY);
            res = (JSONArray) asyncTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public JSONObject registrarRespuestasGenerales(Context context,ArrayList<String> idResp){
        JSONObject res = null;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/preguntas/registrarRespuestasGenerales?id="+idStr+"&resp=";
            for(int i = 0; i<idResp.size(); i++){
                urlStr+=idResp.get(i)+"-";
            }
            urlStr=urlStr.substring(0,urlStr.length()-1);
            //no me importa si ya estoy registrado o no
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr,WS_OBJETO);
            res = (JSONObject) asyncTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public JSONArray obtenerPreguntasEspecificas(Context context){
        JSONArray res = null;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/preguntas/obtenerPreguntasEspecificas?id="+idStr;
            //no me importa si ya estoy registrado o no
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr,WS_ARRAY);
            res = (JSONArray) asyncTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public JSONObject registrarRespuestasEspecificas(Context context,ArrayList<String> idResp){
        JSONObject res = null;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/preguntas/registrarRespuestasEspecificas?id="+idStr+"&resp=";
            for(int i = 0; i<idResp.size(); i++){
                urlStr+=idResp.get(i)+"-";
            }
            urlStr=urlStr.substring(0,urlStr.length()-1);
            //no me importa si ya estoy registrado o no
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr,WS_OBJETO);
            res = (JSONObject) asyncTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public JSONArray obtenerPreguntasRespondidas(Context context){
        JSONArray res = null;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/preguntas/obtenerPreguntasRespondidas?id="+idStr;
            //no me importa si ya estoy registrado o no
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr,WS_ARRAY);
            res = (JSONArray) asyncTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public JSONObject resetearTodasLasPreguntas(Context context){
        JSONObject res = null;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/preguntas/resetearTodasLasPreguntas?id="+idStr;
            //no me importa si ya estoy registrado o no
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr,WS_ARRAY);
            res = (JSONObject) asyncTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean registrarDispositivo(Context context){
        boolean res=false;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/registrar?id="+idStr;
            //no me importa si ya estoy registrado o no
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr,WS_OBJETO);
            JSONObject jsonObject = (JSONObject) asyncTask.get();
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void inicioMaquinaDelTiempo(Context  context,String strNuevaFecha) {
        JSONObject res = null;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/maquinatiempo?iden="+idStr+"&date="+strNuevaFecha;
            //no me importa si ya estoy registrado o no
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr,WS_ARRAY);
            res = (JSONObject) asyncTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return res;

    }

    //Se crea porque no se permiten conexiones a red en main thread
    private class requestWebServiceAsync extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... params){
            Object object = null;
            try {
                object = requestWebService(params[0],params[1]);
            }
            catch (Exception e){}
            return object;
        }
    }

    public static Object requestWebService(String serviceUrl,String tipo_resultado) throws Exception{

        HttpURLConnection urlConnection = null;
        Object result = null;
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String respText = getResponseText(in);
            //result = new JSONObject(respText);
            if(tipo_resultado==WS_OBJETO){
                result = new JSONObject(respText);
            }else {
                result = new JSONArray(respText);
            }


        } catch (Exception e) {
            throw e;
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

}
