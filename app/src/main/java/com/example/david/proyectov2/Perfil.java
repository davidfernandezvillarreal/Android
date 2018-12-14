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
import android.widget.EditText;
import android.widget.ListView;
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
 * {@link Perfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Perfil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String id;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView listViewPerfil;
    ArrayList<String> listaDatos = new ArrayList<String>();
    ArrayAdapter<String> adaptador;

    public Perfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static Perfil newInstance(String param1, String param2) {
        Perfil fragment = new Perfil();
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

        new ConsultarPerfil().execute(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        listViewPerfil = view.findViewById(R.id.listaPerfil);
        return view;
    }

    class ConsultarPerfil extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            //String url = "http://itsjsistemaentradasalida.000webhostapp.com/scripts_android/android_consultar_perfil.php";
            String url = "http://192.168.3.104/Practicas/ProyectoV2/scripts_android/android_consultar_perfil.php";

            String metodoEnvio = "POST";

            String cadenaJSON = "";
            try {
                cadenaJSON = "{ \"id_trabajador\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.i("cadenaPerfil", cadenaJSON);

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(url, metodoEnvio, cadenaJSON);
            try {
                JSONArray jsonArray = objetoJSON.getJSONArray("perfil");

                listaDatos.add(jsonArray.getJSONObject(0).getString("id"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("nombre"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("primer_ap"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("segundo_ap"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("sexo"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("fecha_nac"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("calle"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("numero"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("colonia"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("codigo_postal"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("ciudad"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("puesto"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("area"));
                listaDatos.add(jsonArray.getJSONObject(0).getString("subarea"));

                adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listaDatos);

                publishProgress();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            listViewPerfil.setAdapter(adaptador);
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
