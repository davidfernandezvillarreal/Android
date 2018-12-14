package com.example.david.proyectov2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.david.proyectov2.controlador.AnalizadorJSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ManejarTrabajador extends AppCompatActivity {

    EditText cajaId;
    EditText cajaN;
    EditText cajaPA;
    EditText cajaSA;
    EditText cajaS;
    EditText cajaFN;
    EditText cajaCal;
    EditText cajaNum;
    EditText cajaCol;
    EditText cajaCP;
    EditText cajaCiu;
    EditText cajaP;
    EditText cajaA;
    EditText cajaSubA;

    Button btnModificar;
    Button btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manejar_trabajador);

        cajaId = findViewById(R.id.cajaIdTrabajador);
        cajaN = findViewById(R.id.cajaNombre);
        cajaPA = findViewById(R.id.cajaPrimerAp);
        cajaSA = findViewById(R.id.cajaSegundoAp);
        cajaS = findViewById(R.id.cajaSexo);
        cajaFN = findViewById(R.id.cajaFechaNac);
        cajaCal = findViewById(R.id.cajaCalle);
        cajaNum = findViewById(R.id.cajaNumero);
        cajaCol = findViewById(R.id.cajaColonia);
        cajaCP = findViewById(R.id.cajaCodigoPostal);
        cajaCiu = findViewById(R.id.cajaCiudad);
        cajaP = findViewById(R.id.cajaPuesto);
        cajaA = findViewById(R.id.cajaArea);
        cajaSubA = findViewById(R.id.cajaSubarea);

        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);

        Intent i = getIntent();
        ArrayList<String> trabajador = i.getExtras().getStringArrayList("trabajador");
        Toast.makeText(getApplicationContext(), trabajador.get(1) + " " + trabajador.get(2) + " " + trabajador.get(3), Toast.LENGTH_SHORT).show();

        cajaId.setText(trabajador.get(0));
        cajaN.setText(trabajador.get(1));
        cajaPA.setText(trabajador.get(2));
        cajaSA.setText(trabajador.get(3));
        cajaS.setText(trabajador.get(4));
        cajaFN.setText(trabajador.get(5));
        cajaCal.setText(trabajador.get(6));
        cajaNum.setText(trabajador.get(7));
        cajaCol.setText(trabajador.get(8));
        cajaCP.setText(trabajador.get(9));
        cajaCiu.setText(trabajador.get(10));
        cajaP.setText(trabajador.get(11));
        cajaA.setText(trabajador.get(12));
        cajaSubA.setText(trabajador.get(13));

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = cajaId.getText().toString();
                String n = cajaN.getText().toString();
                String pa = cajaPA.getText().toString();
                String sa = cajaSA.getText().toString();
                String s = cajaS.getText().toString();
                String fn = cajaFN.getText().toString();
                String cal = cajaCal.getText().toString();
                String num = cajaNum.getText().toString();
                String col = cajaCol.getText().toString();
                String cp = cajaCP.getText().toString();
                String ciu = cajaCiu.getText().toString();
                String p = cajaP.getText().toString();
                String a = cajaA.getText().toString();
                String suba = cajaSubA.getText().toString();

                Log.i("modificar", id + " - " + n + " - " + pa + " - " + sa + " - " + s + " - " + fn + " - " + cal + " - " + num + " - " + col + " - " + cp + " - " + ciu + " - " + p + " - " + a + " - " + suba);

                if (!id.equals("") && !n.equals("") && !pa.equals("") && !sa.equals("") && !s.equals("") && !fn.equals("") &&
                        !cal.equals("") && !num.equals("") && !col.equals("") && !cp.equals("") && !ciu.equals("") && !p.equals("") &&
                        !a.equals("") && !suba.equals("")) {
                    new ModificarTrabajador().execute(id, n, pa, sa, s, fn, cal, num, col, cp, ciu, p, a, suba);
                } else {
                    Toast.makeText(getApplicationContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = cajaId.getText().toString();

                new EliminarTrabajador().execute(id);
            }
        });
    }

    class ModificarTrabajador extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            //String url = "http://itsjsistemaentradasalida.000webhostapp.com/scripts_android/android_modificar_trabajador.php";
            String url = "http://192.168.3.104/Practicas/ProyectoV2/scripts_android/android_modificar_trabajador.php";
            String metodoEnvio = "POST";

            String cadenaJSON = "";
            try {
                cadenaJSON = "{ \"id_trabajador\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\", " +
                        "\"nombre\" : \"" + URLEncoder.encode(datos[1], "UTF-8") + "\", " +
                        "\"primer_ap\" : \"" + URLEncoder.encode(datos[2], "UTF-8") + "\", " +
                        "\"segundo_ap\" : \"" + URLEncoder.encode(datos[3], "UTF-8") + "\", " +
                        "\"sexo\" : \"" + URLEncoder.encode(datos[4], "UTF-8") + "\", " +
                        "\"fecha_nac\" : \"" + URLEncoder.encode(datos[5], "UTF-8") + "\", " +
                        "\"calle\" : \"" + URLEncoder.encode(datos[6], "UTF-8") + "\", " +
                        "\"numero\" : \"" + URLEncoder.encode(datos[7], "UTF-8") + "\", " +
                        "\"colonia\" : \"" + URLEncoder.encode(datos[8], "UTF-8") + "\", " +
                        "\"codigo_postal\" : \"" + URLEncoder.encode(datos[9], "UTF-8") + "\", " +
                        "\"ciudad\" : \"" + URLEncoder.encode(datos[10], "UTF-8") + "\", " +
                        "\"puesto\" : \"" + URLEncoder.encode(datos[11], "UTF-8") + "\", " +
                        "\"area\" : \"" + URLEncoder.encode(datos[12], "UTF-8") + "\", " +
                        "\"subarea\" : \"" + URLEncoder.encode(datos[13], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.i("modificar", cadenaJSON);

            analizadorJSON.peticionHTTP(url, metodoEnvio, cadenaJSON);

            publishProgress("Registro modificado");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
        }
    }

    class EliminarTrabajador extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            //String url = "http://itsjsistemaentradasalida.000webhostapp.com/scripts_android/android_eliminar_trabajador.php";
            String url = "http://192.168.3.104/Practicas/ProyectoV2/scripts_android/android_eliminar_trabajador.php";
            String metodoEnvio = "POST";

            String cadenaJSON = "";
            try {
                cadenaJSON = "{ \"id_trabajador\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            analizadorJSON.peticionHTTP(url, metodoEnvio, cadenaJSON);

            publishProgress("Registro eliminado");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
        }
    }
}
