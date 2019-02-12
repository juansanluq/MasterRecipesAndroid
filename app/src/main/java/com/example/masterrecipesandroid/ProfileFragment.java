package com.example.masterrecipesandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    TextView txvNombre, txvApellidos,txvEmail,txvNumeroTelefono, txvUsername, txvFechaNacimiento, txvComentarios;
    ImageView imgProfile;
    Button btnCerrarSesion;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        txvNombre = view.findViewById(R.id.txvNombre);
        txvApellidos = view.findViewById(R.id.txvApellidos);
        imgProfile = view.findViewById(R.id.imgProfile);
        txvEmail = view.findViewById(R.id.txvEmail);
        txvNumeroTelefono = view.findViewById(R.id.txvNumeroTelefono);
        txvUsername = view.findViewById(R.id.txvUsername);
        txvFechaNacimiento = view.findViewById(R.id.txvFechaNacimiento);
        txvComentarios = view.findViewById(R.id.txvComentarios);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        Picasso.with(getContext()).load(Login.loggedUser.getFoto()).into(imgProfile);
        txvNombre.setText(Login.loggedUser.getNombre());
        txvApellidos.setText(Login.loggedUser.getApellidos());
        txvEmail.setText(Login.loggedUser.getEmail());
        txvNumeroTelefono.setText(Login.loggedUser.getNumeroTelefono());
        txvUsername.setText(Login.loggedUser.getUsername());
        txvFechaNacimiento.setText(Login.loggedUser.getFechaNacimiento());
        txvComentarios.setText(Login.loggedUser.getComentarios());

        final RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getContext());

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Cerrando sesion...");
                progressDialog.show();
                String url_getLoggedUser = Login.base_url + "/api/1.0/logout/";
                StringRequest sr_getLoggedUser = new StringRequest(Request.Method.POST, url_getLoggedUser,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                getActivity().finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(),"Error interno del servidor, pruebe mas tarde",Toast.LENGTH_LONG).show();
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
                        params.put("Authorization","Token " + Login.UserToken);

                        return params;
                    }

                    @Override
                    public Priority getPriority() {
                        return Priority.LOW;
                    }

                };
                requestQueue.add(sr_getLoggedUser);
            }
        });

        return view;
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
}
