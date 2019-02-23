package com.example.masterrecipesandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.masterrecipesandroid.Adaptadores.RecetasAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.masterrecipesandroid.Principal.progressDialog;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecetasFragment extends Fragment {

    private RecetasAdapter recetasAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private Spinner spinner;
    private FloatingActionButton fab;
    private RequestQueue requestQueue;
    final String[] categorias = {"Entrantes","Pescados","Carnes","Verduras","Ensaladas","Postres"};


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecetasFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecetasFragment newInstance(int columnCount) {
        RecetasFragment fragment = new RecetasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recetas, container, false);
        recyclerView = view.findViewById(R.id.rvRecetasFragment);
        spinner = view.findViewById(R.id.spinner);
        fab = view.findViewById(R.id.floatingActionButton);

        requestQueue = Volley.newRequestQueue(getContext());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NuevaReceta.class);
                startActivity(intent);
            }
        });

        spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_item,categorias));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                Toast.makeText(adapterView.getContext(),
                        (String) adapterView.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                ArrayList<Receta> listaFiltrada = new ArrayList<Receta>();
                for (Receta a : Principal.recetas)
                {
                    if(RecetasAdapter.getCategoria(a.getCategoria()).equals(adapterView.getItemAtPosition(pos)))
                    {
                        listaFiltrada.add(a);
                    }
                }
                cargarAdaptador(listaFiltrada);

            }

            //cargarAdaptador(Principal.recetas);

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Receta receta);
    }

    public void cargarAdaptador(ArrayList<Receta> lista){
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recetasAdapter = new RecetasAdapter(getContext(),lista);
        recyclerView.setAdapter(recetasAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Principal.RecargarRecetas(getContext());
        spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_item, categorias));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Toast.makeText(adapterView.getContext(),
                        (String) adapterView.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                ArrayList<Receta> listaFiltrada = new ArrayList<Receta>();
                for (Receta a : Principal.recetas) {
                    if (RecetasAdapter.getCategoria(a.getCategoria()).equals(adapterView.getItemAtPosition(pos))) {
                        listaFiltrada.add(a);
                    }
                }
                cargarAdaptador(listaFiltrada);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cargarAdaptador(Principal.recetas);
    }
}

