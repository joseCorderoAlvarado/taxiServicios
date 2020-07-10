package app.proyecto.taxiservicios;

public class modeloBuzon {

    public String getCorreo() {
        return correo;
    }

    public String getMensaje() {
        return mensaje;
    }
 public String getNombre()
 {
     return nombre;
 }
    public String getFechaMensaje() {
        return fechaMensaje;
    }

    private String correo;
private String mensaje;
private String fechaMensaje;
private  String nombre;



    public modeloBuzon(String correo, String mensaje, String fechaMensaje,String nombre ) {
        this.correo = correo;
        this.mensaje = mensaje;
        this.fechaMensaje = fechaMensaje;
        this.nombre=nombre;
    }


}
