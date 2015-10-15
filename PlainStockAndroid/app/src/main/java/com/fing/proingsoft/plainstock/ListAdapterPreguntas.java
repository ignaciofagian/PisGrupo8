package com.fing.proingsoft.plainstock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * http://www.hermosaprogramacion.com/2014/10/android-listas-adaptadores/
 * Created by guillermo on 12/09/15.
 */
public class ListAdapterPreguntas extends ArrayAdapter<String>{



    private View.OnClickListener onClickListener;
    private int _respuesta = -1;

    public int getRespuesta() {
        return _respuesta;
    }

    public void setRespuesta(int _respuesta) {
        this._respuesta = _respuesta;
    }

    public ListAdapterPreguntas(Context context, View.OnClickListener listener,String... objects) {
        super(context, 0, objects);
        this.onClickListener = listener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con two_line_list_item.xml
            listItemView = inflater.inflate(R.layout.row_respuesta, parent, false);
        }

        //Obteniendo instancias de los buttons
        Button respuesta = (Button)listItemView.findViewById(R.id.btRespuesta);


            //Obteniendo instancia de la Tarea en la posición actual
        String texto_resp = (String) getItem(position);

        respuesta.setText(texto_resp);
        respuesta.setTag(R.string.TAG_NroRespuesta,position);
        respuesta.setTag(R.string.TAG_ID_Respuesta, texto_resp);

        if (position == this._respuesta){

            respuesta.setBackgroundColor(listItemView.getResources().getColor(R.color._color_green_dark));
        }
        else{
            respuesta.setBackgroundColor(listItemView.getResources().getColor(R.color._color_green_plain));
        }

        respuesta.setOnClickListener(this.onClickListener);


        //Devolver al ListView la fila creada
        return listItemView;

    }



}



