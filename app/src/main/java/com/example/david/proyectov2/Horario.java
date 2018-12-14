package com.example.david.proyectov2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.david.proyectov2.controlador.AnalizadorJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Horario.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Horario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Horario extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String id;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView listViewHorario;
    ArrayList<String> listaDatos = new ArrayList<>();
    ArrayAdapter<String> adaptador;

    TextView txtIdTrabajador;
    TextView txtNombre;

    public Horario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Horario.
     */
    // TODO: Rename and change types and number of parameters
    public static Horario newInstance(String param1, String param2) {
        Horario fragment = new Horario();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        new ConsultarHorario().execute(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_horario, container, false);
        listViewHorario = view.findViewById(R.id.listaHorario);
        txtIdTrabajador = view.findViewById(R.id.txtIdTrabajador);
        txtNombre = view.findViewById(R.id.txtNombre);
        return view;
    }

    class ConsultarHorario extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            //String url = "http://itsjsistemaentradasalida.000webhostapp.com/scripts_android/android_consultar_horario.php";
            String url = "http://192.168.3.104/Practicas/ProyectoV2/scripts_android/android_consultar_horario.php";
            String metodoEnvio = "POST";

            String cadenaJSON = "";
            try {
                cadenaJSON = "{ \"id_trabajador\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.i("cadenaHorario", cadenaJSON);

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(url, metodoEnvio, cadenaJSON);
            try {
                JSONArray jsonArrayEntrada = objetoJSON.getJSONArray("horarioEntrada");
                JSONArray jsonArraySalida = objetoJSON.getJSONArray("horarioSalida");

                String lunes = "Lunes: \n" + jsonArrayEntrada.getJSONObject(0).getString("lunes") +
                        " - " + jsonArraySalida.getJSONObject(0).getString("lunes");
                listaDatos.add(lunes);
                String martes = "Martes: \n" + jsonArrayEntrada.getJSONObject(0).getString("martes") +
                        " - " + jsonArraySalida.getJSONObject(0).getString("martes");
                listaDatos.add(martes);
                String miercoles = "Miercoles: \n" + jsonArrayEntrada.getJSONObject(0).getString("miercoles") +
                        " - " + jsonArraySalida.getJSONObject(0).getString("miercoles");
                listaDatos.add(miercoles);
                String jueves = "Jueves: \n" + jsonArrayEntrada.getJSONObject(0).getString("jueves") +
                        " - " + jsonArraySalida.getJSONObject(0).getString("jueves");
                listaDatos.add(jueves);
                String viernes = "Viernes: \n" + jsonArrayEntrada.getJSONObject(0).getString("viernes") +
                        " - " + jsonArraySalida.getJSONObject(0).getString("viernes");
                listaDatos.add(viernes);
                String sabado = "Sabado: \n" + jsonArrayEntrada.getJSONObject(0).getString("sabado") +
                        " - " + jsonArraySalida.getJSONObject(0).getString("sabado");
                listaDatos.add(sabado);

                adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listaDatos);

                publishProgress(jsonArrayEntrada.getJSONObject(0).getString("id"),
                        jsonArrayEntrada.getJSONObject(0).getString("nombre") + " " +
                        jsonArrayEntrada.getJSONObject(0).getString("primer_ap") + " " +
                        jsonArrayEntrada.getJSONObject(0).getString("segundo_ap"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            txtIdTrabajador.setText(values[0]);
            txtNombre.setText(values[1]);
            listViewHorario.setAdapter(adaptador);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
