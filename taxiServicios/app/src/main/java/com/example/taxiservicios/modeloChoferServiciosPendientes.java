package com.example.taxiservicios;

public class modeloChoferServiciosPendientes {
    public modeloChoferServiciosPendientes(String cliente, String evaluacion, String nota, String fecha_hora,String status_idstatus) {
        this.cliente = cliente;
        this.evaluacion = evaluacion;
        this.nota = nota;
        this.fecha_hora = fecha_hora;
        this.status_idstatus=status_idstatus;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(String evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }


    private String cliente;
    private String evaluacion;
    private String nota;

    public String getStatus_idstatus() {
        return status_idstatus;
    }

    private String status_idstatus;

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    private String fecha_hora;




}
