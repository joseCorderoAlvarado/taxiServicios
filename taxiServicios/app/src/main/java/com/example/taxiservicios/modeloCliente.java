package com.example.taxiservicios;

public class modeloCliente {
    private String identificador;
    private String fechahora;
    private String direccion;
    private String status;
    public modeloCliente(String identificador,String fechahora,String direccion, String status)
    {
        this.identificador=identificador;
        this.fechahora=fechahora;
        this.direccion=direccion;
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
    public  String getStatus() {
        return status;
    }
    public  void  setStatus(String status)
    {
        this.status=status;
    }
}
