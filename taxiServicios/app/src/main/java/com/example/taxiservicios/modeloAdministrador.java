package com.example.taxiservicios;
public class modeloAdministrador {
    private String identificador;
    private String fechahora;
    private String direccion;
    private String referencia;
    private String status;
    public modeloAdministrador(String identificador,String fechahora,String direccion,String referencia,String status)
    {
        this.identificador=identificador;
        this.fechahora=fechahora;
        this.direccion=direccion;
        this.referencia=referencia;
        this.status=status;
    }
    public  String getIdentificador() {
        return identificador;
    }
    public  void  setIdentificador(String identificador)
    {
        this.identificador=identificador;
    }
    public  String getFechahora() {
        return fechahora;
    }
    public  void  setFechahora(String fechahora)
    {
        this.fechahora=fechahora;
    }
    public  String getDireccion() {
        return direccion;
    }
    public  void  setDireccion(String direccion)
    {
        this.direccion=direccion;
    }
    public  void setReferencia(String referencia)
    {
        this.referencia=referencia;
    }
    public String getReferencia()
    {
        return  referencia;
    }
    public  String getStatus() {
        return status;
    }
    public  void  setStatus(String status)
    {
        this.status=status;
    }
}
