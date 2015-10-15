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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;


public class PlainStockWS {
    private static String dateFromat="yyyy-MM-dd hh:mm";
    private static String WS_OBJETO = "OBJETO";
    private static String WS_ARRAY = "ARRAY";

    public static Pair<GregorianCalendar,Integer> saldoActualWS(Context context){
        Pair result = null;
        try {
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/saldo?id="+idStr;
            JSONObject jsonObject = (JSONObject)requestWebService(urlStr,WS_OBJETO);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFromat);
            Date date = simpleDateFormat.parse(jsonObject.getString("tiempo"));
            GregorianCalendar fecha = new GregorianCalendar();
            fecha.setTime(date);
            Integer saldo = Integer.parseInt(jsonObject.getString("saldo"));
            result = new Pair<>(fecha,saldo);
        }
        catch (Exception e){
            e.getMessage();
        }
        return result;
    }

    public static ArrayList<Pair<GregorianCalendar,Integer>> saldoHistorico(){
        return null;
    }

    public JSONArray obtenerPreguntasGenerales(Context context){
        JSONArray res = null;
        try {
            //DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
            //String idStr = deviceUuidFactory.getDeviceUuid().toString();
            String urlStr = Configuracion.BASE_URL_SERVIDOR+"/preguntas/obtenerPreguntasGenerales";
            //no me importa si ya estoy registrado o no
            AsyncTask asyncTask = new requestWebServiceAsync().execute(urlStr,WS_ARRAY);
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
