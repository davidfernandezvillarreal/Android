package com.example.david.proyectov2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
 * {@link Registros.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Registros#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Registros extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String id;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView listViewRegistros;
    ArrayList<String> listaDatos = new ArrayList<String>();
    ArrayAdapter<String> adaptador;
    TextView txtIdTrabajador;
    TextView txtNombre;
    Button btnRegistrarEntradaSalida;

    public Registros() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Registros.
     */
    // TODO: Rename and change types and number of parameters
    public static Registros newInstance(String param1, String param2) {
        Registros fragment = new Registros();
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

        new ConsultarRegistros().execute(id);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnRegistrarEntradaSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AgregarRegistros().execute(id);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registros, container, false);

        listViewRegistros = view.findViewById(R.id.listaRegistros);
        txtIdTrabajador = view.findViewById(R.id.txtIdTrabajador);
        txtNombre = view.findViewById(R.id.txtNombre);
        btnRegistrarEntradaSalida = view.findViewById(R.id.btnRegistrarEntradaSalida);

        return view;
    }

    class ConsultarRegistros extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            //String url = "http://itsjsistemaentradasalida.000webhostapp.com/scripts_android/android_consultar_registros.php";
            String url = "http://192.168.3.104/Practicas/ProyectoV2/scripts_android/android_consultar_registros.php";
            String metodoEnvio = "POST";

            String cadenaJSON = "";
            try {
                cadenaJSON = "{ \"id_trabajador\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(url, metodoEnvio, cadenaJSON);

            try {
                JSONArray jsonArrayEntrada = objetoJSON.getJSONArray("registrosEntrada");
                JSONArray jsonArraySalida = objetoJSON.getJSONArray("registrosSalida");

                String registro;

                for (int i = 0; i < jsonArraySalida.length(); i++) {
                    registro = jsonArrayEntrada.getJSONObject(i).getString("fecha") +
                            "\n" + jsonArrayEntrada.getJSONObject(i).getString("hora") +
                            " - " + jsonArraySalida.getJSONObject(i).getString("hora");
                    listaDatos.add(registro);
                }

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

            listViewRegistros.setAdapter(adaptador);
        }
    }

    class AgregarRegistros extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            //String url = "http://itsjsistemaentradasalida.000webhostapp.com/scripts_android/android_alta_registros.php";
            String url = "http://192.168.3.104/Practicas/ProyectoV2/scripts_android/android_alta_registros.php";
            String metodoEnvio = "POST";

            String cadenaJSON = "";
            try {
                cadenaJSON = "{ \"id_trabajador\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(url, metodoEnvio, cadenaJSON);

            try {
                JSONArray jsonArrayRegistrar = objetoJSON.getJSONArray("registrar");

                String registro = "";
                if (jsonArrayRegistrar.getJSONObject(0).getBoolean("entrada")==true &&
                        jsonArrayRegistrar.getJSONObject(0).getBoolean("salida")==false) {
                    registro = "Registro de entrada";
                } else if (jsonArrayRegistrar.getJSONObject(0).getBoolean("entrada")==false &&
                        jsonArrayRegistrar.getJSONObject(0).getBoolean("salida")==true){
                    registro = "Registro de salida";
                }

                publishProgress(registro);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getContext(), values[0], Toast.LENGTH_SHORT).show();
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
