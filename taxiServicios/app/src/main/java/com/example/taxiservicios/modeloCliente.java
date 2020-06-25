package com.example.taxiservicios;

public class modeloCliente {
    private String identificador;
    private String fechahora;
    private String direccion;
    private String status;
    private String notaxi;

    public String getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(String evaluacion) {
        this.evaluacion = evaluacion;
    }

    private String evaluacion;

    public String getNotaxi() {
        return notaxi;
    }

    public void setNotaxi(String notaxi) {
        this.notaxi = notaxi;
    }

    public String getDescripcionVehiculo() {
        return descripcionVehiculo;
    }

    public void setDescripcionVehiculo(String descripcionVehiculo) {
        this.descripcionVehiculo = descripcionVehiculo;
    }
    private  String descripcionVehiculo;

    public  String getCosto()
        {
            return costo;
        }
    private String costo;

    public modeloCliente(String identificador,String fechahora,String direccion, String status, String evaluacion,String costo)
    {
        this.identificador=identificador;
        this.fechahora=fechahora;
        this.direccion=direccion;
        this.status=status;
        this.evaluacion=evaluacion;
        this.costo=costo;
    }

    //Cuando el estatus ya esta confirmado
    public modeloCliente(String identificador,String fechahora,String direccion, String status,
                         String notaxi,  String descripcionVehiculo,String costo)
    {
        this.identificador=identificador;
        this.fechahora=fechahora;
        this.direccion=direccion;
        this.status=status;
        this.notaxi = notaxi;
        this.descripcionVehiculo = descripcionVehiculo;
        this.costo=costo;
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
