package com.example.masterrecipesandroid.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.masterrecipesandroid.Login;
import com.example.masterrecipesandroid.R;
import com.example.masterrecipesandroid.Receta;
import com.example.masterrecipesandroid.Registro;
import com.example.masterrecipesandroid.VisorPDF;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecetasAdapter extends RecyclerView.Adapter<RecetasAdapter.RecetasViewHolder> {

    private ArrayList<Receta> listaRecetas;
    private Context context;
    public static Receta recetaVisor;

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

    class RecetasViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgvReceta;
        private TextView txvNombre;
        private TextView txvCategoria;
        private TextView txvDificultad;
        private View view;

        public RecetasViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imgvReceta = (ImageView)itemView.findViewById(R.id.imgvReceta);
            txvNombre= (TextView)itemView.findViewById(R.id.txvNombre);
            txvCategoria = itemView.findViewById(R.id.txvCategoria);
            txvDificultad = itemView.findViewById(R.id.txvDificultad);
        }
    }
}
