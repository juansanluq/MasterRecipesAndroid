package com.example.masterrecipesandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.ImageViewCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.downloader.Progress;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<Receta> recetas = new ArrayList<Receta>();
    public static ProgressDialog progressDialog;
    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        requestQueue = Volley.newRequestQueue(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        ImageView imgvPerfil = hView.findViewById(R.id.imageView);
        TextView txvNombre = hView.findViewById(R.id.txvNombre);
        TextView txvApellidos = hView.findViewById(R.id.txvApellidos);

        Picasso.with(getApplicationContext()).load(Login.loggedUser.getFoto()).into(imgvPerfil);

        txvNombre.setText(Login.loggedUser.getNombre());
        txvApellidos.setText(Login.loggedUser.getApellidos());

        progressDialog = new ProgressDialog(Principal.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sincronizando datos con el servidor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url_login = Login.base_url + "/api/1.0/recetas/";
        JsonArrayRequest sr_recetas = new JsonArrayRequest(
                Request.Method.GET,
                url_login,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            recetas.clear();
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject RecetaJSON = response.getJSONObject(i);
                                recetas.add(new Receta(RecetaJSON.getString("imagen"),RecetaJSON.getString("pdf"),RecetaJSON.getInt("dificultad"),RecetaJSON.getInt("categoria"),RecetaJSON.getString("nombre"),RecetaJSON.getInt("id")));
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                params.put("Authorization","Token " + Login.loggedUser.getToken());

                return params;
            }

        };
        requestQueue.add(sr_recetas);

        navigationView.setNavigationItemSelectedListener(this);
        Fragment fragment = new ProfileFragment();
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_perfil) {
            fragment = new ProfileFragment();
        } else if (id == R.id.nav_recetas) {
            fragment = new RecetasFragment();

        } else if (id == R.id.nav_mapa) {
            fragment = new MapFragment();

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_logout) {

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recetas.clear();
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        progressDialog = new ProgressDialog(Principal.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sincronizando datos con el servidor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url_login = Login.base_url + "/api/1.0/recetas/";
        JsonArrayRequest sr_recetas = new JsonArrayRequest(
                Request.Method.GET,
                url_login,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            recetas.clear();
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject RecetaJSON = response.getJSONObject(i);
                                recetas.add(new Receta(RecetaJSON.getString("imagen"),RecetaJSON.getString("pdf"),RecetaJSON.getInt("dificultad"),RecetaJSON.getInt("categoria"),RecetaJSON.getString("nombre"),RecetaJSON.getInt("id")));
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                params.put("Authorization","Token " + Login.loggedUser.getToken());

                return params;
            }

        };
        requestQueue.add(sr_recetas);
    }*/

    public static void RecargarRecetas(Context contexto)
    {
        progressDialog = new ProgressDialog(contexto);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sincronizando datos con el servidor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url_login = Login.base_url + "/api/1.0/recetas/";
        JsonArrayRequest sr_recetas = new JsonArrayRequest(
                Request.Method.GET,
                url_login,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            recetas.clear();
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject RecetaJSON = response.getJSONObject(i);
                                recetas.add(new Receta(RecetaJSON.getString("imagen"),RecetaJSON.getString("pdf"),RecetaJSON.getInt("dificultad"),RecetaJSON.getInt("categoria"),RecetaJSON.getString("nombre"),RecetaJSON.getInt("id")));
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                params.put("Authorization","Token " + Login.loggedUser.getToken());

                return params;
            }

        };
        requestQueue.add(sr_recetas);
    }
}
