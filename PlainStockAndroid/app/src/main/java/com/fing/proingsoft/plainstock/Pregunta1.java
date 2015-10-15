package com.fing.proingsoft.plainstock;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.fing.proingsoft.plainstock.otros.PlainStockWS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Pregunta1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Pregunta1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pregunta1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private String _pregunta;
    private String[] _respuestas;
    private TextView conf;
    private String idioma = "ing"; //El idioma que aparece en el JSONArray

    private int _nroPantalla =0;
    private int _maxPregunta;
    private String[] _todasPregunta;
    private String[][] _todasRespuestas;


    private int[] _nroRespuesta;
    private int[] _contestadas;
    private String[][] _idRespuestas;
    private int _maxContestada = 0;
    private ListView _lista;
    private  ListAdapterPreguntas _adapter;
    private static enum TIPO_PREGUNTAS{GENERALES,ESPECIFICAS};
    private TIPO_PREGUNTAS tipo_preguntas;

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

        //fragment.setearParametros(param1, param2);
        return fragment;
    }

    private static void setArgs(Pregunta1 p, String param1, String[] param2){
        Bundle args = new Bundle();
        args.putString("_pregunta", param1);
        args.putStringArray("_respuestas", param2);
        p.setArguments(args);
    }

    public Pregunta1() {
        _pregunta = "¿Qué piensa que pasa?";
        _respuestas =  new String[]{"Sube","Queda Igual","Baja"};
        //Arrays.fill(_contestadas, -1);
    }

    private void setearParametros(View view,String pregunta,String[] respuestas){
        TextView txview = ((TextView) view.findViewById(R.id.tvPreguntas));
        txview.setText(pregunta);
        //setArgs(this,_pregunta,_respuestas);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contestar((Button) view);
            }
        };
        _adapter = new ListAdapterPreguntas(getActivity(), listener,respuestas);
        _lista.setAdapter(_adapter);
    }

    private void cambiarPreguntas(View root,int numero){

        if(_todasPregunta.length != 0) {
            _pregunta = _todasPregunta[_nroPantalla];
            _respuestas = _todasRespuestas[_nroPantalla];
            setearParametros(root, _pregunta, _respuestas);
            ImageButton back = (ImageButton) root.findViewById(R.id.btPrevQ);
            ImageButton next = (ImageButton) root.findViewById(R.id.btNextQ);

            back.setEnabled(_nroPantalla > 0);
            next.setEnabled(_nroPantalla <= _maxContestada);
            if ((_nroPantalla == _maxPregunta)) {
                next.setEnabled(_nroPantalla > _maxContestada);
                next.setImageDrawable(getResources().getDrawable(R.drawable.img_check_blue));
                conf.setText("Confirmar");
                next.setEnabled(true);
            }
            _adapter.setRespuesta(_contestadas[numero]);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        //((ActionBarActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.titulopregunta);


        if (getArguments() != null) {
            _pregunta = getArguments().getString("_pregunta");
            _respuestas = getArguments().getStringArray("_respuestas");

            setArgs(this, _pregunta, _respuestas);
            //setearParametros(_pregunta,_respuestas);
        }


    }

    public void back_listener(View boton){
        if (_nroPantalla >= 0) {
            _nroPantalla--;
            cambiarPreguntas(this.getView(), _nroPantalla);
            conf.setText("");
        }
    }

    public void  next_listener(View boton){
        if(_nroPantalla==_maxPregunta){
            confirmarRespuestas();
        }
        else if (_nroPantalla >= 0) {
            _nroPantalla++;
            cambiarPreguntas(this.getView(), _nroPantalla);
        }else{
//
        }
    }

    public void _btnreiniciar_Listener(View boton){
        PlainStockWS webService = new PlainStockWS();
        webService.resetearTodasLasPreguntas(getActivity());
        refreshFragment();
    }

    public void refreshFragment(){
        getActivity().recreate();
    }

    private void contestar(Button b){
        // para decidir despues que hacer cuando no este hardcoreado
        int posicion = (int)b.getTag(R.string.TAG_NroRespuesta);
        String preguntaId = (String)b.getTag(R.string.TAG_ID_Respuesta);
        _contestadas[_nroPantalla] = posicion;
        _adapter.setRespuesta(posicion);
        _adapter.notifyDataSetChanged();
        if (_nroPantalla <= _maxPregunta){

            _maxContestada = Math.max(_nroPantalla, _maxContestada);
            _maxContestada++;
            cambiarPreguntas(this.getView(), _nroPantalla);
        }
        else{
        }
    }

    public void confirmarRespuestas(){
        PlainStockWS webService = new PlainStockWS();
        ArrayList<String> idResp = new ArrayList<>();
        for (int i = 0; i<= _maxPregunta; i++){
            idResp.add(i,_idRespuestas[i][_contestadas[i]]);;
        }
        if(tipo_preguntas == TIPO_PREGUNTAS.GENERALES){
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

        //Obtengo las preguntas del servidor
        try {
            PlainStockWS webService = new PlainStockWS();
            JSONArray respWS = webService.obtenerPreguntasEspecificas(getActivity());
            tipo_preguntas= TIPO_PREGUNTAS.ESPECIFICAS;
            //Si aun no respondi preguntas generales
            JSONArray pregResp = webService.obtenerPreguntasRespondidas(getActivity());
            if((respWS == null || respWS.length() == 0) && pregResp.length()==0){
                respWS = webService.obtenerPreguntasGenerales(getActivity());
                tipo_preguntas = TIPO_PREGUNTAS.GENERALES;
            }
//            //SUSTITUIR CON WS
//            JSONArray pregResp = webService.obtenerPreguntasRespondidas(getActivity());
//            if(pregResp.length()==(respWS.length()+2)){
//                respWS = new JSONArray();
//            }
//            //////////////////
            int count = respWS.length();
            _todasPregunta = new String[count];
            _todasRespuestas = new String[count][];
            _idRespuestas = new String[count][];
            _maxPregunta=count-1;
            _nroRespuesta = new int[_maxPregunta + 1];
            _contestadas = new int[_maxPregunta + 1];
            Arrays.fill(_contestadas, -1);
            JSONObject item,item2;
            JSONArray resps;
            int count2;
            for(int i=0;i<count;i++){
                item = respWS.getJSONObject(i);
                _todasPregunta[i] =item.getString(idioma);
                resps = item.getJSONArray("resp");
                count2 = resps.length();
                _todasRespuestas[i] = new String[count2];
                _idRespuestas[i] = new String[count2];
                for(int j=0;j<count2 ;j++){
                    item2 = resps.getJSONObject(j);
                    _todasRespuestas[i][j] = item2.getString(idioma);
                    _idRespuestas[i][j] = item2.getString("id");
                }
            }
        }
        catch (Exception e){}


        _lista = (ListView) rootView.findViewById(R.id.lvRespuestas);
        cambiarPreguntas(rootView, _nroPantalla);
        conf = (TextView) rootView.findViewById(R.id._confirmar);

        ImageButton back = (ImageButton)rootView.findViewById(R.id.btPrevQ);
        ImageButton next = (ImageButton)rootView.findViewById(R.id.btNextQ);
        final Button _btnreiniciar = (Button)rootView.findViewById(R.id.btnReiniciar);


        _btnreiniciar.setVisibility(rootView.INVISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_listener(view);
                _btnreiniciar.setVisibility(rootView.VISIBLE);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_listener(view);
                _btnreiniciar.setVisibility(rootView.VISIBLE);

            }
        });
        _btnreiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _btnreiniciar_Listener(view);
                _btnreiniciar.setVisibility(rootView.VISIBLE);
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}