package com.example.masterrecipesandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity {

    public static Usuario loggedUser;
    static RequestQueue requestQueue;
    public static String base_url = "http://192.168.1.6";
    public static Context contexto;

    @BindView(R.id.login_title)
    TextView loginTitle;
    @BindView(R.id.edtUsername)
    EditText edtUsername;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.txvRegistro)
    TextView txvRegistro;
    @BindView(R.id.user_profile_photo)
    ImageButton userProfilePhoto;
    ProgressDialog progressDialog;

    static String UserToken;
    boolean logged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);

        makeAppFullscreen();

        contexto = getApplicationContext();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Autenticando...");
                progressDialog.show();
                new full_login().execute();
            }
        });

        txvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registro.class);
                startActivity(intent);
            }
        });
    }


     void makeAppFullscreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void login() {
        String url_login = base_url + "/api/1.0/login/";
        StringRequest sr_login = new StringRequest(Request.Method.POST, url_login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject UserTokenJSON = new JSONObject(response);
                            Login.UserToken = UserTokenJSON.getString("key");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (edtUsername.getText().toString().isEmpty())
                        {
                            edtUsername.setError("El nombre de usuario no puede estar vacío");
                        }
                        else if (edtPassword.getText().toString().isEmpty())
                        {
                            edtPassword.setError("La contraseña no puede estar vacía");
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "El nombre de usuario/contraseña es incorrecto", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", edtUsername.getText().toString());
                params.put("password", edtPassword.getText().toString());
                return params;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

        };
        requestQueue.add(sr_login);
    }

    private class full_login extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_login = base_url + "/api/1.0/login/";
            StringRequest sr_login = new StringRequest(Request.Method.POST, url_login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject UserTokenJSON = new JSONObject(response);
                                Login.UserToken = UserTokenJSON.getString("key");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (edtUsername.getText().toString().isEmpty())
                            {
                                edtUsername.setError("El nombre de usuario no puede estar vacío");
                            }
                            else if (edtPassword.getText().toString().isEmpty())
                            {
                                edtPassword.setError("La contraseña no puede estar vacía");
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "El nombre de usuario/contraseña es incorrecto", Toast.LENGTH_LONG).show();
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", edtUsername.getText().toString());
                    params.put("password", edtPassword.getText().toString());
                    return params;
                }

                @Override
                public Priority getPriority() {
                    return Priority.IMMEDIATE;
                }

            };
            requestQueue.add(sr_login);

            String url_getLoggedUser = base_url + "/api/1.0/usuarios/";
            StringRequest sr_getLoggedUser = new StringRequest(Request.Method.GET, url_getLoggedUser,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                loggedUser = new Usuario();
                                response = response.replace("[","");
                                response = response.replace("]","");
                                JSONObject Usuario = new JSONObject(response);
                                loggedUser.setId(Usuario.getInt("id"));
                                loggedUser.setUsername(Usuario.getString("username"));
                                loggedUser.setPassword(Usuario.getString("password"));
                                loggedUser.setNombre(Usuario.getString("nombre"));
                                loggedUser.setApellidos(Usuario.getString("apellidos"));
                                loggedUser.setFechaNacimiento(Usuario.getString("fecha_nacimiento"));
                                loggedUser.setEmail(Usuario.getString("email"));
                                loggedUser.setLatitud(Usuario.getString("latitud"));
                                loggedUser.setLongitud(Usuario.getString("longitud"));
                                loggedUser.setNumeroTelefono(Usuario.getString("numero_telefono"));
                                loggedUser.setFoto(Usuario.getString("foto"));
                                loggedUser.setComentarios(Usuario.getString("comentarios"));
                                loggedUser.setToken(UserToken);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Has iniciado sesión correctamente",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Principal.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Error interno del servidor, intentelo de nuevo más tarde",Toast.LENGTH_LONG).show();
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
                    params.put("Authorization","Token " + UserToken);

                    return params;
                }

                @Override
                public Priority getPriority() {
                    return Priority.LOW;
                }

            };
            requestQueue.add(sr_getLoggedUser);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loggedUser = null;
        edtUsername.setText("");
        edtPassword.setText("");
        edtUsername.clearFocus();
        edtPassword.clearFocus();
    }
}
