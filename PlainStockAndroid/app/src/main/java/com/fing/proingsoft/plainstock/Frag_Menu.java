package com.fing.proingsoft.plainstock;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fing.proingsoft.plainstock.interfaces.MenuInferiorListener;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@linkMenuInferiorListener} interface
 * to handle interaction events.
 */
public class Frag_Menu extends Fragment implements View.OnClickListener{

    private int botonActivo = R.id.btDetalles;
    private MenuInferiorListener mListener;

    public Frag_Menu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ((RelativeLayout)view.findViewById(R.id.btPreguntas)).setOnClickListener(this);
        ((RelativeLayout)view.findViewById(R.id.btDetalles)).setOnClickListener(this);
        ((RelativeLayout)view.findViewById(R.id.btConfig)).setOnClickListener(this);
        ((RelativeLayout)view.findViewById(R.id.btEvolucion)).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tintButton((ImageView) view.findViewById(R.id.imgPreguntas));
        tintButton((ImageView)view.findViewById(R.id.imgConfig));
        tintButton((ImageView)view.findViewById(R.id.imgEvolucion));
        tintButton((ImageView)view.findViewById(R.id.imgPreguntas));
        activarBoton(botonActivo);
    }

    private void attach(Context activity){
        try {
            mListener = (MenuInferiorListener) activity;
            setBotonActivo(mListener.getBtnMenuActivoId());
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MenuInferiorListener");
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        attach(activity);
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        attach(activity);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public int getBotonActivo() {
        return botonActivo;
    }

    public void setBotonActivo(int botonActivo) {
        this.botonActivo = botonActivo;
        if (getView() != null){
            activarBoton(botonActivo);
        }
    }

    protected void activarBoton(int botonId){
        List<Integer> idBotones = Arrays.asList( R.id.btPreguntas,R.id.btConfig,R.id.btDetalles,R.id.btEvolucion);
        if (!idBotones.contains(botonId)) throw new IllegalArgumentException("No hay boton en menu con id " + botonId);
        for (int id : idBotones){
            View boton = getView().findViewById(id);
            boton.setSelected(id == botonId);

        }
    }

    public static void tintButton( ImageView button) {
        ColorStateList colours = button.getResources()
                .getColorStateList(R.color._selector_menu_tint);
        Drawable d = DrawableCompat.wrap(button.getDrawable());
        DrawableCompat.setTintList(d, colours);
        button.setImageDrawable(d);
    }

    @Override
    public void onClick(View view) {
        activarBoton(view.getId());
        mListener.MenuInferiorOnClick(view);
    }
}
