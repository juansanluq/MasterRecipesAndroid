package com.example.masterrecipesandroid;

public class Usuario {

    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("comentarios")
    private String comentarios;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("foto")
    private String foto;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("numero_telefono")
    private String numeroTelefono;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("coordenadas")
    private String coordenadas;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("email")
    private String email;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("fecha_nacimiento")
    private String fechaNacimiento;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("apellidos")
    private String apellidos;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("nombre")
    private String nombre;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("password")
    private String password;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("username")
    private String username;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("id")
    private int id;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    private String Token;

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
