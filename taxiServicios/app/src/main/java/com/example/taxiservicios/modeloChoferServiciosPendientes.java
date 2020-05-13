package com.example.taxiservicios;

public class modeloChoferServiciosPendientes {





    public modeloChoferServiciosPendientes(String cliente, String fechahora, String direccionRecoger, String direccionLlevar, String telefono) {
        this.cliente = cliente;
        this.fechahora = fechahora;
        this.direccionRecoger = direccionRecoger;
        this.direccionLlevar = direccionLlevar;
        this.telefono = telefono;
    }

    private String fechahora;

    public String getFechahora() {
        return fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }

    public String getDireccionRecoger() {
        return direccionRecoger;
    }

    public void setDireccionRecoger(String direccionRecoger) {
        this.direccionRecoger = direccionRecoger;
    }

    public String getDireccionLlevar() {
        return direccionLlevar;
    }

    public void setDireccionLlevar(String direccionLlevar) {
        this.direccionLlevar = direccionLlevar;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    private String direccionRecoger;
    private String direccionLlevar;
    private String telefono;
    private String cliente;




}
