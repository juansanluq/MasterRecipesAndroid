package com.example.masterrecipesandroid;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Registro extends AppCompatActivity {

    @BindView(R.id.register_title)
    TextView registerTitle;
    @BindView(R.id.imgPerfil)
    ImageView imgPerfil;
    @BindView(R.id.edtUsername)
    EditText edtUsername;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.edtNombre)
    EditText edtNombre;
    @BindView(R.id.edtApellidos)
    EditText edtApellidos;
    @BindView(R.id.edtFecha)
    EditText edtFecha;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtTelefono)
    EditText edtTelefono;
    @BindView(R.id.edtComentario)
    EditText edtComentario;
    @BindView(R.id.nestedScrollView2)
    NestedScrollView nestedScrollView2;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.6:80/api/1.0/usuarios/";
                StringRequest sr = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                                Log.e("Error: ",error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", edtUsername.getText().toString());
                        params.put("password", edtPassword.getText().toString());
                        params.put("nombre", edtNombre.getText().toString());
                        params.put("apellidos", edtApellidos.getText().toString());
                        params.put("fecha_nacimiento", edtFecha.getText().toString());
                        params.put("email", edtEmail.getText().toString());
                        params.put("numero_telefono", edtTelefono.getText().toString());
                        params.put("comentarios", edtComentario.getText().toString());
                        return params;
                    }

                };
                requestQueue.add(sr);
            }
        });



    }
}
