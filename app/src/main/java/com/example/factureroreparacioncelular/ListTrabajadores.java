package com.example.factureroreparacioncelular;

import java.io.Serializable;

public class ListTrabajadores implements Serializable {
    private String Nombre;
    private String Usuario;
    private String Rol;
    private String Contraseña;

    public ListTrabajadores(String nombre, String usuario, String rol) {
        Nombre = nombre;
        Usuario = usuario;
        Rol = rol;
    }

    public ListTrabajadores() {

    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getRol() {
        return Rol;
    }

    public void setRol(String rol) {
        Rol = rol;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }
}
