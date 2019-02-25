package com.example.masterrecipesandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.masterrecipesandroid.Actividades.UsuarioDetalle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private View rootView;
    GoogleMap mMap;
    private SupportMapFragment mapFragment;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
    public static Usuario usuarioSeleccionado;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getContext());
        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);

        //Cargamos el mapa en el fragment map
        getChildFragmentManager().beginTransaction().replace(R.id.frameLayout, mapFragment).commit();

        //Inflamos la Vista rootView para Visualizar el Adaptador personalizado
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        //Devolvemos la Vista
        return rootView;
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sincronizando datos con el servidor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url_login = Login.base_url + "/api/1.0/usuarios/all";
        JsonArrayRequest sr_recetas = new JsonArrayRequest(
                Request.Method.GET,
                url_login,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            listaUsuarios.clear();
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject UsuarioJSON = response.getJSONObject(i);
                                listaUsuarios.add(new Usuario(UsuarioJSON.getInt("id"),UsuarioJSON.getString("username"),
                                        UsuarioJSON.getString("password"),UsuarioJSON.getString("nombre"),UsuarioJSON.getString("apellidos"),UsuarioJSON.getString("fecha_nacimiento"),
                                        UsuarioJSON.getString("email"),UsuarioJSON.getString("latitud"),UsuarioJSON.getString("longitud"),UsuarioJSON.getString("numero_telefono"),
                                        UsuarioJSON.getString("foto"),UsuarioJSON.getString("comentarios")));
                            }

                            for(Usuario u : listaUsuarios)
                            {
                                if(Login.loggedUser.getId() == u.getId())
                                {
                                    LatLng ubi = new LatLng(Double.parseDouble(u.getLatitud()), Double.parseDouble(u.getLongitud()));
                                    mMap.addMarker(new MarkerOptions().position(ubi).title("Tú"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubi, 15)); // Zoom del mapa
                                }
                                else
                                {
                                    LatLng ubi = new LatLng(Double.parseDouble(u.getLatitud()), Double.parseDouble(u.getLongitud()));
                                    mMap.addMarker(new MarkerOptions().position(ubi).title(u.getUsername()));
                                }
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        for(Usuario u : listaUsuarios)
        {
            if(u.getUsername().equals(marker.getTitle()))
            {
                usuarioSeleccionado = u;
                Intent intent = new Intent(getContext(), UsuarioDetalle.class);
                startActivity(intent);
            }
        }
        return false;
    }
}
