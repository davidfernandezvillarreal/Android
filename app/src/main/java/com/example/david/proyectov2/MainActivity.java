package com.example.david.proyectov2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Perfil.OnFragmentInteractionListener,
        Horario.OnFragmentInteractionListener, Registros.OnFragmentInteractionListener, Trabajadores.OnFragmentInteractionListener {

    String id_trabajador = "";
    String tipo_usuario = "";
    Menu menu;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent i = getIntent();
        id_trabajador = i.getExtras().getCharSequence("id").toString();
        tipo_usuario = i.getExtras().getCharSequence("tipo_usuario").toString();

        if (tipo_usuario.equals("Administrador")) {
            menu = navigationView.getMenu();
            menuItem = menu.add(tipo_usuario).setTitle(tipo_usuario);
            menuItem.setIcon(R.drawable.icon_tipo_usuario);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cerrar_sesion) {
            i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        boolean fTransaccion = false;

        if (id == R.id.nav_perfil) {
            // Handle the camera action
            fragment = Perfil.newInstance(id_trabajador,"");
            fTransaccion = true;
            Toast.makeText(getApplicationContext(), "Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_horario) {
            fragment = Horario.newInstance(id_trabajador, "");
            fTransaccion = true;
            Toast.makeText(getApplicationContext(), "Horario", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_registros) {
            fragment = Registros.newInstance(id_trabajador, "");
            fTransaccion = true;
            Toast.makeText(getApplicationContext(), "Registros", Toast.LENGTH_SHORT).show();
        } else if (id == menuItem.getItemId()) {
            fragment = new Trabajadores();
            fTransaccion = true;
        }

        if(fTransaccion) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
