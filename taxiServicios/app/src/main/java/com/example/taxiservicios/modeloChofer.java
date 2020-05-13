package com.example.taxiservicios;

public class modeloChofer {
    public modeloChofer(String cliente, String evaluacion, String nota, String fecha_hora) {
        this.cliente = cliente;
        this.evaluacion = evaluacion;
        this.nota = nota;
        this.fecha_hora = fecha_hora;
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

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    private String fecha_hora;




}
