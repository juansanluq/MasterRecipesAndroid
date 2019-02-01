package com.example.masterrecipesandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity {

    public Usuario loggedUser = new Usuario();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        makeAppFullscreen();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
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


    private void makeAppFullscreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void login() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.6/api/1.0/login/";
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject UserTokenJSON = new JSONObject(response);
                            String UserToken = UserTokenJSON.getString("key");
                            loggedUser.setToken(UserToken);
                        } catch (JSONException e) {
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

        };
        requestQueue.add(sr);
    }
}
