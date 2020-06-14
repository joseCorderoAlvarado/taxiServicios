package com.example.taxiservicios;

public class modeloBuzon {

    public String getCorreo() {
        return correo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getFechaMensaje() {
        return fechaMensaje;
    }

    private String correo;
private String mensaje;
private String fechaMensaje;



    public modeloBuzon(String correo, String mensaje, String fechaMensaje) {
        this.correo = correo;
        this.mensaje = mensaje;
        this.fechaMensaje = fechaMensaje;
    }


}
