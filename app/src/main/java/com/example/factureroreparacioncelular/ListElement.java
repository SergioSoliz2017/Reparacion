package com.example.factureroreparacioncelular;

import java.io.Serializable;

public class ListElement implements Serializable {
    private String modeloCelular;
    private String nombreCliente;
    private String estado;
    private String color;
    private String fecha;
    private String codigo;

    public ListElement (String modeloCelular, String nombreCliente, String estado, String color, String fecha, String codigo){
        this.modeloCelular=modeloCelular;
        this.nombreCliente=nombreCliente;
        this.estado=estado;
        this.color=color;
        this.fecha=fecha;
        this.codigo = codigo;
    }
    public String getModeloCelular() {
        return modeloCelular;
    }

    public void setModeloCelular(String modeloCelular) {
        this.modeloCelular = modeloCelular;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
