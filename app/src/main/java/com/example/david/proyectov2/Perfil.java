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
import android.widget.EditText;
import android.widget.Toast;

import com.example.david.proyectov2.controlador.AnalizadorJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


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

    EditText cajaIdTrabajador;
    EditText cajaNombre;
    EditText cajaPrimerAp;
    EditText cajaSegundoAp;
    EditText cajaSexo;
    EditText cajaFechaNac;
    EditText cajaCalle;
    EditText cajaNumero;
    EditText cajaColonia;
    EditText cajaCodigoPostal;
    EditText cajaCiudad;
    EditText cajaPuesto;
    EditText cajaArea;
    EditText cajaSubarea;

    private OnFragmentInteractionListener mListener;

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

    class ConsultarPerfil extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            String url = "http://192.168.1.7/Practicas/Proyecto/scripts_android/android_consultar_perfil.php";
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

                publishProgress(jsonArray.getJSONObject(0).getString("id"),
                        jsonArray.getJSONObject(0).getString("nombre"),
                        jsonArray.getJSONObject(0).getString("primer_ap"),
                        jsonArray.getJSONObject(0).getString("segundo_ap"),
                        jsonArray.getJSONObject(0).getString("sexo"),
                        jsonArray.getJSONObject(0).getString("fecha_nac"),
                        jsonArray.getJSONObject(0).getString("calle"),
                        jsonArray.getJSONObject(0).getString("numero"),
                        jsonArray.getJSONObject(0).getString("colonia"),
                        jsonArray.getJSONObject(0).getString("codigo_postal"),
                        jsonArray.getJSONObject(0).getString("ciudad"),
                        jsonArray.getJSONObject(0).getString("puesto"),
                        jsonArray.getJSONObject(0).getString("area"),
                        jsonArray.getJSONObject(0).getString("subarea"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            cajaIdTrabajador.setText(values[0]);
            cajaNombre.setText(values[1]);
            cajaPrimerAp.setText(values[2]);
            cajaSegundoAp.setText(values[3]);
            cajaSexo.setText(values[4]);
            cajaFechaNac.setText(values[5]);
            cajaCalle.setText(values[6]);
            cajaNumero.setText(values[7]);
            cajaColonia.setText(values[8]);
            cajaCodigoPostal.setText(values[9]);
            cajaCiudad.setText(values[10]);
            cajaPuesto.setText(values[11]);
            cajaArea.setText(values[12]);
            cajaSubarea.setText(values[13]);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        cajaIdTrabajador = view.findViewById(R.id.cajaIdTrabajador);
        cajaNombre = view.findViewById(R.id.cajaNombre);
        cajaPrimerAp = view.findViewById(R.id.cajaPrimerAp);
        cajaSegundoAp = view.findViewById(R.id.cajaSegundoAp);
        cajaSexo = view.findViewById(R.id.cajaSexo);
        cajaFechaNac = view.findViewById(R.id.cajaFechaNac);
        cajaCalle = view.findViewById(R.id.cajaCalle);
        cajaNumero = view.findViewById(R.id.cajaNumero);
        cajaColonia = view.findViewById(R.id.cajaColonia);
        cajaCodigoPostal = view.findViewById(R.id.cajaCodigoPostal);
        cajaCiudad = view.findViewById(R.id.cajaCiudad);
        cajaPuesto = view.findViewById(R.id.cajaPuesto);
        cajaArea = view.findViewById(R.id.cajaArea);
        cajaSubarea = view.findViewById(R.id.cajaSubarea);
        return view;
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
