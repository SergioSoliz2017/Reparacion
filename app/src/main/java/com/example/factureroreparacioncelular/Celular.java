package com.example.factureroreparacioncelular;

import java.io.Serializable;

public class Celular implements Serializable {
    private String cliente;
    private String modelo;
    private String ci;
    private String celular;
    private String reparacion;
    private String observaciones;
    private String fotoInicial;
    private String fotoFinal;
    private String precio;
    private String encargado;
    private String estado;

    private String fechaInicio;
    private String fechaFin;
    private String mes;
    private String codigo;

    public Celular(String cliente, String modelo, String ci, String celular, String reparacion, String observaciones, String fotoInicial, String fotoFinal, String precio, String encargado,String estado) {
        this.cliente = cliente;
        this.modelo = modelo;
        this.ci = ci;
        this.celular = celular;
        this.reparacion = reparacion;
        this.observaciones = observaciones;
        this.fotoInicial = fotoInicial;
        this.fotoFinal = fotoFinal;
        this.precio = precio;
        this.encargado = encargado;
        this.estado = estado;
    }

    public Celular() {

    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFotoFinal() {
        return fotoFinal;
    }

    public void setFotoFinal(String fotoFinal) {
        this.fotoFinal = fotoFinal;
    }

    public String getFotoInicial() {
        return fotoInicial;
    }

    public void setFotoInicial(String fotoInicial) {
        this.fotoInicial = fotoInicial;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getReparacion() {
        return reparacion;
    }

    public void setReparacion(String reparacion) {
        this.reparacion = reparacion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
