package com.fing.proingsoft.plainstock;

import android.app.Fragment;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.fing.proingsoft.plainstock.otros.Configuracion;
import com.fing.proingsoft.plainstock.otros.DatosPantalla;
import com.fing.proingsoft.plainstock.otros.DatosPantalla.TIPO_PREGUNTAS;
import com.fing.proingsoft.plainstock.otros.PlainStockWS;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Pregunta1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Pregunta1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pregunta1 extends Fragment {
    public static final String TAG_ERROR = "plainstock.P.OCV";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private TextView conf;
    //private String idioma = "ing"; //El idioma que aparece en el JSONArray


    private ListView _lista;
    private  ListAdapterPreguntas _adapter;
    private DatosPantalla d = new DatosPantalla();





    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Pregunta1.
     */
    // TODO: Rename and change types and number of parameters
    public static Pregunta1 newInstance(String param1, String[] param2) {
        Pregunta1 fragment = new Pregunta1();

        //fragment.setearPreguntasPantallaActual(param1, param2);
        return fragment;
    }

    private static void setArgs(Pregunta1 p, String param1, String[] param2){
        Bundle args = new Bundle();
        args.putString("_preguntaPantActual", param1);
        args.putStringArray("_respuestasPantActual", param2);
        p.setArguments(args);
    }

    /**
     * Constructor por defecto para que se pueda previsualizar en el diseñador
     */
    public Pregunta1() {

    }

    // metodo auxiliar
    private void setearPreguntasPantallaActual(View view, String pregunta, String[] respuestas){
        TextView txview = ((TextView) view.findViewById(R.id.tvPreguntas));
        txview.setText(pregunta);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contestar((Button) view);
            }
        };
        _adapter = new ListAdapterPreguntas(getActivity(), listener,respuestas);
        _lista.setAdapter(_adapter);
    }

    private boolean habilitoBack(){
        return d.hayPantallaAnt();
    }

    private boolean habilitoNext(){
        return d.hayProximoPaso();
    }


    private void  setEnableFlecha(ImageButton v,boolean enable){
        v.setEnabled(enable);
        v.setColorFilter(enable ? Color.TRANSPARENT : Color.GRAY);
    }
    /**
     * Se encarga de la logica de cambiar las preguntas, activar y desactivar botones
     * @param root
     * @param numero
     */
    private void cambiarPreguntas(View root,int numero){

        if(d.cantidadPreguntas != 0) {

            setearPreguntasPantallaActual(root, d.pregActual(), d.respuestasActuales());

            ImageButton back = (ImageButton) root.findViewById(R.id.btPrevQ);
            ImageButton next = (ImageButton) root.findViewById(R.id.btNextQ);

            // el boton reiniciar debe ser visible si hay alguna pregunta contestada
            final Button _btnreiniciar = (Button)root.findViewById(R.id.btnReiniciar);
            if (d._maxContestada == 0 && d.tipo_preguntas == TIPO_PREGUNTAS.GENERALES){
                _btnreiniciar.setVisibility(View.INVISIBLE);
            }
            else{
                _btnreiniciar.setVisibility(View.VISIBLE);
            }

            setEnableFlecha(back, habilitoBack());
            setEnableFlecha(next, habilitoNext());


            if ((d.puedoConfirmar())) {
                next.setImageDrawable(getResources().getDrawable(R.drawable.img_check_blue));
                String str_confirmar = getResources().getString(R.string.str_preg_confirmar);
                conf.setText(str_confirmar);
            }
            else{
                next.setImageDrawable(getResources().getDrawable(R.drawable.img_right_blue));
            }


            _adapter.setRespuesta(d._contestadas[numero]);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // se lee de vuelta en el onCreate y se usa en el onCreateView
        outState.putSerializable("d", this.d);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            DatosPantalla dp = (DatosPantalla)savedInstanceState.getSerializable("d");
            this.d = dp;
        }
        else{
            this.d = new DatosPantalla();
        }
    }



    public void back_listener(View boton){
        if (d.back()) {
            cambiarPreguntas(this.getView(), d._nroPantalla);
            conf.setText("");
        }
    }

    public void  next_listener(View boton){
        if(d.puedoConfirmar()){
            confirmarRespuestas();
        }
        else if (d.hayPreguntaProx()) {
            d.next();
            cambiarPreguntas(this.getView(), d._nroPantalla);
        }
    }

    public void _btnreiniciar_Listener(View boton){
        PlainStockWS webService = new PlainStockWS();
        webService.resetearTodasLasPreguntas(getActivity());

        refreshFragment();
    }

    public void refreshFragment(){
        this.d.reiniciar = true;
        getActivity().recreate();
    }

    /**
     * Listener que se usa cuando se hace click en una pregunta
     * @param b Objeto button que se presiono
     */
    private void contestar(Button b){
        // para decidir despues que hacer cuando no este hardcoreado

        int posicion = (int)b.getTag(R.string.TAG_NroRespuesta);
        String preguntaId = (String)b.getTag(R.string.TAG_ID_Respuesta);

        _adapter.setRespuesta(posicion);

        _adapter.notifyDataSetChanged();
        d.contestar(posicion);


        if (!d.puedoConfirmar()){

            //_maxContestada++;
            // cambio preguntas despues de un momento
            AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
            long milisDemoraEfecto = 700; // tiempo  antes de fade out
            long milisEfecto  =200;
            anim.setStartOffset(milisDemoraEfecto);
            anim.setDuration(milisEfecto);
            anim.setFillAfter(true);
            _lista.startAnimation(anim);
            Handler demora = new Handler();
            demora.postDelayed(new Runnable() {
                @Override
                public void run() {
                    d.next();
                    cambiarPreguntas(getView(), d._nroPantalla);
                    AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(300);
                    anim.setFillAfter(true);
                    _lista.startAnimation(anim);
                }
            },milisDemoraEfecto+milisEfecto);

        }
        else {
            cambiarPreguntas(this.getView(),d._nroPantalla);
        }

    }

    public void confirmarRespuestas(){
        PlainStockWS webService = new PlainStockWS();
        ArrayList<String> idResp = new ArrayList<>();
        for (int i = 0; i<= d._maxPregunta; i++){
            idResp.add(i,d._idRespuestas[i][d._contestadas[i]]);;
        }
        if(d.tipo_preguntas == TIPO_PREGUNTAS.GENERALES){
            webService.registrarRespuestasGenerales(getActivity(), idResp);
        }
        else{
            webService.registrarRespuestasEspecificas(getActivity(),idResp);
        }
        //aca hay que recargar fragmento
        refreshFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_pregunta1, container, false);


        if (d.reiniciar) {

            //Obtengo las preguntas del servidor
            try {
                d = new DatosPantalla();
                PlainStockWS webService = new PlainStockWS();
                JSONArray respWS = webService.obtenerPreguntasEspecificas(getActivity());
                d.tipo_preguntas = TIPO_PREGUNTAS.ESPECIFICAS;
                //Si aun no respondi preguntas generales
                JSONArray pregResp = webService.obtenerPreguntasRespondidas(getActivity());
                if ((respWS == null || respWS.length() == 0) && pregResp.length() == 0) {
                    respWS = webService.obtenerPreguntasGenerales(getActivity());
                    d.tipo_preguntas = TIPO_PREGUNTAS.GENERALES;

                }

            d.cargarDesdeJSON(respWS);
            } catch (Exception e) {
                Log.e(TAG_ERROR,e.getMessage(),e);
            }
        }
        d.setIdioma(Configuracion.getInstance().getLangCode());


        try {
            _lista = (ListView) rootView.findViewById(R.id.lvRespuestas);

            conf = (TextView) rootView.findViewById(R.id._confirmar);
            cambiarPreguntas(rootView, d._nroPantalla);

            ImageButton back = (ImageButton)rootView.findViewById(R.id.btPrevQ);
            setEnableFlecha(back,habilitoBack());
            ImageButton next = (ImageButton)rootView.findViewById(R.id.btNextQ);
            setEnableFlecha(next,habilitoNext());
            final Button _btnreiniciar = (Button)rootView.findViewById(R.id.btnReiniciar);

        /*
        Configuro los listeners
         */
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    back_listener(view);
                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    next_listener(view);

                }
            });
            _btnreiniciar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _btnreiniciar_Listener(view);
                }
            });
        } catch (Exception e) {
            Log.e(TAG_ERROR,e.getMessage(),e);
        }

        return rootView;
    }




    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

}