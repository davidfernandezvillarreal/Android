package com.example.david.proyectov2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.david.proyectov2.controlador.AnalizadorJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Login extends AppCompatActivity {

    EditText cajaUser;
    EditText cajaPassword;
    Button btnLogin;

    String usuario = "";
    String contraseña = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cajaUser = findViewById(R.id.cajaUser);
        cajaPassword = findViewById(R.id.cajaPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProcesarLogin().execute(cajaUser.getText().toString(), cajaPassword.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
         * Obtenemos los parametros de la activity que mandamos la información
         */
        if ((resultCode==RESULT_OK) && (requestCode==1)) {
            String cadena = data.getExtras().getCharSequence("sesionCerrada").toString();
        }
    }

    class ProcesarLogin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... datos) {
            AnalizadorJSON analizadorJSON = new AnalizadorJSON();

            //String url = "http://itsjsistemaentradasalida.000webhostapp.com/scripts_android/android_procesar_login.php";
            String url = "http://192.168.3.104/Practicas/ProyectoV2/scripts_android/android_procesar_login.php";
            String metodoEnvio = "POST";

            String cadenaJSON = "";
            try {
                cadenaJSON = "{ \"user\" : \"" + URLEncoder.encode(datos[0], "UTF-8") + "\", "
                        + "\"password\" : \"" + URLEncoder.encode(datos[1], "UTF-8") + "\" }";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JSONObject objetoJSON = analizadorJSON.peticionHTTP(url, metodoEnvio, cadenaJSON);
            try {
                JSONArray jsonArray = objetoJSON.getJSONArray("usuario");
                usuario = jsonArray.getJSONObject(0).getString("user");
                contraseña = jsonArray.getJSONObject(0).getString("password");

                publishProgress(usuario, contraseña, jsonArray.optJSONObject(0).getString("tipo_usuario"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            if (values[0].equals(cajaUser.getText().toString()) && values[1].equals(cajaPassword.getText().toString())) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("id", cajaUser.getText().toString());
                i.putExtra("tipo_usuario", values[2]);
                startActivityForResult(i, 1);
            } else {
                Toast.makeText(getApplicationContext(), "El usuario o contraseña son incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
