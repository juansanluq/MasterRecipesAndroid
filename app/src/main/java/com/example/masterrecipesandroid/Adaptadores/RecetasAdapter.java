package com.example.masterrecipesandroid.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.masterrecipesandroid.Login;
import com.example.masterrecipesandroid.R;
import com.example.masterrecipesandroid.Receta;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecetasAdapter extends RecyclerView.Adapter<RecetasAdapter.RecetasViewHolder> {

    private ArrayList<Receta> listaRecetas;
    private Context context;

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
    public void onBindViewHolder(@NonNull RecetasViewHolder recetasViewHolder, int i) {
        final Receta recetaSeleccionada = listaRecetas.get(i);
        Picasso.with(context).load(listaRecetas.get(i).getImagen()).into(recetasViewHolder.imgvReceta);
        recetasViewHolder.txvNombre.setText(listaRecetas.get(i).getNombre());
    }

    @Override
    public int getItemCount() {
        return listaRecetas.size();
    }

    class RecetasViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgvReceta;
        private TextView txvNombre;
        private View view;

        public RecetasViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imgvReceta = (ImageView)itemView.findViewById(R.id.imgvReceta);
            txvNombre= (TextView)itemView.findViewById(R.id.txvNombre);
        }
    }
}
