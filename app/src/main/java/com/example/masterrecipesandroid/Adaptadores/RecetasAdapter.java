package com.example.masterrecipesandroid.Adaptadores;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.masterrecipesandroid.Login;
import com.example.masterrecipesandroid.Principal;
import com.example.masterrecipesandroid.R;
import com.example.masterrecipesandroid.Receta;
import com.example.masterrecipesandroid.Registro;
import com.example.masterrecipesandroid.VisorPDF;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecetasAdapter extends RecyclerView.Adapter<RecetasAdapter.RecetasViewHolder> {

    private ArrayList<Receta> listaRecetas;
    private Context context;
    public static Receta recetaVisor;
    private Receta recetaEliminar;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    public RecetasAdapter(Context context,ArrayList<Receta> listaRecetas)
    {
        this.listaRecetas = listaRecetas;
        this.context = context;
    }

    @NonNull
    @Override
    public RecetasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecetasViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.receta, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecetasViewHolder recetasViewHolder, int i) {
        final Receta recetaSeleccionada = listaRecetas.get(i);
        recetaEliminar = recetaSeleccionada;
        Picasso.with(context).load(listaRecetas.get(i).getImagen()).into(recetasViewHolder.imgvReceta);
        recetasViewHolder.txvNombre.setText(listaRecetas.get(i).getNombre());
        recetasViewHolder.txvCategoria.setText(getCategoria(listaRecetas.get(i).getCategoria()));
        recetasViewHolder.txvDificultad.setText(getDificultad(listaRecetas.get(i).getDificultad()));

        recetasViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recetaVisor = recetaSeleccionada;
                Intent intent = new Intent(context.getApplicationContext(), VisorPDF.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });

        recetasViewHolder.setItemLongClickListener(new ItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int pos) {
                final int posicion = pos;
                Toast.makeText(context,listaRecetas.get(pos).getNombre(),Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        dialog.dismiss();
                        removeAt();

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaRecetas.size();
    }

    public String getCategoria(int numeroCategoria)
    {
        switch (numeroCategoria)
        {
            case 1:
                return "Entrantes";
            case 2:
                return "Pescados";
            case 3:
                return "Carnes";
            case 4:
                return "Verduras";
            case 5:
                return "Ensaladas";
            case 6:
                return "Postres";
        }
        return null;
    }

    public String getDificultad(int numeroDificultad)
    {
        switch (numeroDificultad)
        {
            case 0:
                return "Baja";
            case 1:
                return "Media";
            case 2:
                return "Alta";
        }
        return null;
    }

    class RecetasViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        private ImageView imgvReceta;
        private TextView txvNombre;
        private TextView txvCategoria;
        private TextView txvDificultad;
        private View view;
        ItemLongClickListener itemLongClickListener;


        public RecetasViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imgvReceta = (ImageView)itemView.findViewById(R.id.imgvReceta);
            txvNombre= (TextView)itemView.findViewById(R.id.txvNombre);
            txvCategoria = itemView.findViewById(R.id.txvCategoria);
            txvDificultad = itemView.findViewById(R.id.txvDificultad);
            itemView.setOnLongClickListener(this);
        }

        public void setItemLongClickListener(ItemLongClickListener ic)
        {
            this.itemLongClickListener=ic;
        }

        @Override
        public boolean onLongClick(View v) {
            this.itemLongClickListener.onItemLongClick(v,getLayoutPosition());
            return false;
        }
    }

    public void removeAt() {
        requestQueue = Volley.newRequestQueue(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Actualizando recetas en el servidor...");
        progressDialog.show();
        new eliminar_receta().execute();

    }

    private class eliminar_receta extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_login = Login.base_url + "/api/1.0/recetas/borrar/" + String.valueOf(recetaEliminar.getId());
            StringRequest sr_login = new StringRequest(Request.Method.DELETE, url_login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {

                @Override
                public Priority getPriority() {
                    return Priority.IMMEDIATE;
                }

            };
            requestQueue.add(sr_login);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            String url_login = Login.base_url + "/api/1.0/recetas/";
            JsonArrayRequest sr_recetas = new JsonArrayRequest(
                    Request.Method.GET,
                    url_login,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                listaRecetas.clear();
                                for(int i=0;i<response.length();i++){
                                    // Get current json object
                                    JSONObject RecetaJSON = response.getJSONObject(i);
                                    listaRecetas.add(new Receta(RecetaJSON.getString("imagen"),RecetaJSON.getString("pdf"),RecetaJSON.getInt("dificultad"),RecetaJSON.getInt("categoria"),RecetaJSON.getString("nombre"),RecetaJSON.getInt("id")));
                                }
                                notifyDataSetChanged();
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
}
