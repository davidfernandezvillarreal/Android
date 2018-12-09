package com.example.david.proyectov2.controlador;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AnalizadorJSON {
    InputStream is = null;
    JSONObject jsonObject = null;
    String json = null;

    public JSONObject peticionHTTP(String url, String metodo, String cadenaJSON) {
        HttpURLConnection conexion = null;
        URL mUrl = null;

        try {
            mUrl = new URL(url);
            conexion = (HttpURLConnection) mUrl.openConnection();

            //activamos el envio de datos a traves de POST
            conexion.setDoOutput(true);

            //establecemos el método
            conexion.setRequestMethod(metodo);

            //tamaño previamente establecido
            conexion.setFixedLengthStreamingMode(cadenaJSON.getBytes().length);

            //Establecer formato de codificación estándar para los datos enviados
            conexion.setRequestProperty("Content-Type", "applicaiton/x-www-form-urlencoded");

            OutputStream os = new BufferedOutputStream(conexion.getOutputStream());

            os.write(cadenaJSON.getBytes());
            os.flush();
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Recibir respuesta HTTP desde PHP
        try {
            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder cad = new StringBuilder();
            String fila;
            while ( (fila = br.readLine()) != null) {
                cad.append(fila + "\n");
            }

            is.close();
            json = cad.toString();

            Log.i("MSJ", "cadena JSON: " + json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("MSJ", "Error en BufferedReader." + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MSJ", "Error en BufferedReader." + e.toString());
        }

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MSJ", "Error al convertir la cadena en cadena JSON");
        }

        return jsonObject;
    } //metodo peticionHTTP mandando cadena JSON

    public JSONObject peticionHTTP(String url, String metodo) {
        HttpURLConnection conexion = null;
        URL mUrl = null;

        String cadenaJSON = null;
        try {
            mUrl = new URL(url);
            conexion = (HttpURLConnection) mUrl.openConnection();

            //activamos el envio de datos a traves de POST
            conexion.setDoOutput(true);

            //establecemos el método
            conexion.setRequestMethod("POST");

            //Establecer formato de codificación estándar para los datos enviados
            conexion.setRequestProperty("Content-Type", "applicaiton/x-www-form-urlencoded");
            OutputStream os = new BufferedOutputStream(conexion.getOutputStream());

            os.flush();
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Recibir respuesta HTTP desde PHP

        try {
            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder cad = new StringBuilder();
            String fila;
            while ( (fila = br.readLine()) != null) {
                cad.append(fila + "\n");
            }

            is.close();
            json = cad.toString();

            Log.i("MSJ", "cadena JSON" + cad);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("MSJ", "Error en BufferedReader." + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MSJ", "Error en BufferedReader." + e.toString());
        }

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MSJ", "Error al convertir la cadena en cadena JSON");
        }

        return jsonObject;
    } //Metodo peticionHTTP sin utilizar cadena JSON
}
