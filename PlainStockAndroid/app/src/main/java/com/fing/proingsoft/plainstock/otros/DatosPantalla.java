package com.fing.proingsoft.plainstock.otros;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para las variables que estaban en Preguntas
 * Falta encapsular mucho pero no importa
 * Created by guillermo on 21/10/15.
 */
public class DatosPantalla implements Serializable {
    public static final long serialVersionUID = 1L;


    public static enum TIPO_PREGUNTAS{GENERALES,ESPECIFICAS};

    private String _idiomaActual = "ing";
    public boolean reiniciar = true; // encapsular
    public int _nroPantalla =0;
    public int _maxPregunta;
    private int _maxLeida = 0;
    private Map<String,String[]> _todasPregunta = new HashMap<>();
    private Map<String,String[][]> _todasRespuestas = new HashMap<>();
    private Map<String,String> mapAndroidJSON = new HashMap<>();
    public int cantidadPreguntas = 0;


    public void setIdioma(String code){
        if (mapAndroidJSON.containsKey(code)) {
            this._idiomaActual = mapAndroidJSON.get(code);
        }
        else {
            // si no tengo el idioma eligo English
            this._idiomaActual = mapAndroidJSON.get("en");
        }
    }

    private int[] _nroRespuesta;
    public int[] _contestadas;
    public String[][] _idRespuestas;
    public int _maxContestada = 0;

    public TIPO_PREGUNTAS tipo_preguntas;

    public boolean hayPreguntaProx(){
        return _nroPantalla < _maxLeida;
    }

    public boolean hayProximoPaso(){
        return hayPreguntaProx() || puedoConfirmar();
    }

    public boolean puedoConfirmar(){
        return _nroPantalla == _maxPregunta && _maxContestada == _maxPregunta;
    }

    public boolean hayPantallaAnt(){
        return _nroPantalla > 0;
    }

    public String pregActual(){
        return _todasPregunta.get(_idiomaActual)[_nroPantalla];
    }

    public String[] respuestasActuales(){
        return _todasRespuestas.get(_idiomaActual)[_nroPantalla];
    }

    public boolean back(){
        if (hayPantallaAnt()) {
            _nroPantalla--;
            return true;
        }
        return false;
    }

    public boolean next(){
        if (!puedoConfirmar()){
            _nroPantalla++;
            _maxLeida =Math.max(_nroPantalla,_maxLeida);
            return true;
        }
        return false;
    }

    public void contestar(int posicion){
        _contestadas[_nroPantalla] = posicion;
        _maxContestada = Math.max(_nroPantalla, _maxContestada);



    }

    public void cargarDesdeJSON(JSONArray respWS) throws JSONException {
        this.reiniciar = false;
        int count = respWS.length();
        this.cantidadPreguntas = count;
        this._nroPantalla  = 0;
        this._maxLeida = 0;


        _idRespuestas = new String[count][];
        _maxPregunta = count - 1;
        _nroRespuesta = new int[this._maxPregunta + 1];
        _contestadas = new int[this._maxPregunta + 1];

        _maxContestada = 0;
        Arrays.fill(_contestadas, -1);
        JSONObject item, item2;
        JSONArray resps;
        int count2;

        for(String idioma : mapAndroidJSON.values()){
            _todasPregunta.put(idioma,new String[count]);
            _todasRespuestas.put(idioma,new String[count][]);
        }
        for (int i = 0; i < count; i++) {
            item = respWS.getJSONObject(i);
            resps = item.getJSONArray("resp");
            count2 = resps.length();
            for(String idioma : mapAndroidJSON.values()){
                _todasPregunta.get(idioma)[i] = item.getString(idioma);
                _todasRespuestas.get(idioma)[i] = new String[count2];
                _idRespuestas[i] = new String[count2];
                for (int j = 0; j < count2; j++) {
                    item2 = resps.getJSONObject(j);
                    _todasRespuestas.get(idioma)[i][j] = item2.getString(idioma);
                    _idRespuestas[i][j] = item2.getString("id");
                }
            }



        }
    }



    public DatosPantalla() {
        // convierto codigos de lenguaje de android en codigos de lenguaje del json
        mapAndroidJSON.put("es", "esp");
        mapAndroidJSON.put("en", "ing");
    }
}



