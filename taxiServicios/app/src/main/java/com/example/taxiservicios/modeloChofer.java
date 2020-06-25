package com.example.taxiservicios;

public class modeloChofer {





    public modeloChofer(String cliente, String fechahora, String direccionRecoger, String direccionLlevar, String telefono,String identificador,String costo) {
        this.cliente = cliente;
        this.fechahora = fechahora;
        this.direccionRecoger = direccionRecoger;
        this.direccionLlevar = direccionLlevar;
        this.telefono = telefono;
        this.identificador=identificador;
        this.costo=costo;
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
 public String getCosto()
 {
     return  costo;
 }
 public void  setCosto(String  costo){
        this.costo=costo;
 }
    public  String getIdentificador() {
        return identificador;
    }
    public  void  setIdentificador(String identificador)
    {
        this.identificador=identificador;
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
    private String identificador;
    private  String costo;
}
