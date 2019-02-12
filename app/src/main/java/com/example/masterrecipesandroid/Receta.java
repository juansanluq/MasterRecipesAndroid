package com.example.masterrecipesandroid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class Receta {
    @Expose
    @SerializedName("imagen")
    private String imagen;
    @Expose
    @SerializedName("pdf")
    private String pdf;
    @Expose
    @SerializedName("dificultad")
    private int dificultad;
    @Expose
    @SerializedName("categoria")
    private int categoria;
    @Expose
    @SerializedName("nombre")
    private String nombre;

    public Receta(String imagen, String pdf, int dificultad, int categoria, String nombre) {
        this.imagen = imagen;
        this.pdf = pdf;
        this.dificultad = dificultad;
        this.categoria = categoria;
        this.nombre = nombre;
    }

    public Receta()
    {

    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
