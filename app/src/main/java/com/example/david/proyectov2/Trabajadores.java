package com.example.david.proyectov2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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
 * {@link Trabajadores.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Trabajadores#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Trabajadores extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView listViewTrabajadores;
    ArrayList<String> listaDatos = new ArrayList<String>();
    ArrayList<ArrayList> listaDatos2 = new ArrayList<ArrayList>();
    ArrayList<ArrayList> listaTrabajador = new ArrayList<ArrayList>();

    String datos;
    int textlength = 0;

    ArrayAdapter<String> adaptador;
    EditText cajaBusqueda;

    public Trabajadores() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Trabajadores.
     */
    // TODO: Rename and change types and number of parameters
    public static Trabajadores newInstance(String param1, String param2) {
        Trabajadores fragment = new Trabajadores();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        new ConsultarTrabajadores().execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cajaBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength = cajaBusqueda.getText().length();
                listaDatos.clear();

                for (int i = 0; i < listaDatos2.size(); i++) {
                    if (textlength <= listaDatos2.get(i).get(2).toString().length()) {
                        if (cajaBusqueda.getText().toString().equalsIgnoreCase(
                                listaDatos2.get(i).get(2).toString().subSequence(0, textlength).toString() )) {
                            datos =  listaDatos2.get(i).get(0).toString() + "\n" +
                                    listaDatos2.get(i).get(1).toString() + " " +
                                    listaDatos2.get(i).get(2).toString() + " " +
                                    listaDatos2.get(i).get(3).toString();
                            listaDatos.add(datos);
                        }
                    }
                }
                adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listaDatos);
                listViewTrabajadores.setAdapter(adaptador);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listViewTrabajadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                Intent i = new Intent(getContext(), ManejarTrabajador.class);
                i.putExtra("trabajador", listaTrabajador.get(position));
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trabajadores, container, false);
        listViewTrabajadores = view.findViewById(R.id.listaTrabajadores);
        cajaBusqueda = view.findViewById(R.id.cajaBusqueda);
        return view;
    }

    class ConsultarTrabajadores extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            //String url = "http://itsjsistemaentradasalida.000webhostapp.com/scripts_android/android_consultar_trabajadores.php";
            String url = "http://192.168.3.104/Practicas/ProyectoV2/scripts_android/android_consultar_trabajadores.php";
            String metodoEnvio = "POST";

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(url, metodoEnvio);

            try {
                JSONArray jsonArrayTrabajadores = objetoJSON.getJSONArray("trabajadores");

                for (int i=0; i<jsonArrayTrabajadores.length(); i++) {
                    ArrayList<String> listaDatosTrabajador = new ArrayList<>();

                    listaDatosTrabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("id"));
                    listaDatosTrabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("nombre"));
                    listaDatosTrabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("primer_ap"));
                    listaDatosTrabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("segundo_ap"));

                    listaDatos2.add(listaDatosTrabajador);

                    datos = jsonArrayTrabajadores.getJSONObject(i).getString("id") + "\n" +
                            jsonArrayTrabajadores.getJSONObject(i).getString("nombre") + " " +
                            jsonArrayTrabajadores.getJSONObject(i).getString("primer_ap") + " " +
                            jsonArrayTrabajadores.getJSONObject(i).getString("segundo_ap");

                    listaDatos.add(datos);

                    ArrayList<String> trabajador = new ArrayList<String>();

                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("id"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("nombre"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("primer_ap"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("segundo_ap"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("sexo"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("fecha_nac"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("calle"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("numero"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("colonia"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("codigo_postal"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("ciudad"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("puesto"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("area"));
                    trabajador.add(jsonArrayTrabajadores.getJSONObject(i).getString("subarea"));

                    listaTrabajador.add(trabajador);

                }

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
            listViewTrabajadores.setAdapter(adaptador);
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
