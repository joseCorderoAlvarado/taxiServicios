package app.proyecto.taxiservicios;
public class modeloAdministrador {
    private String identificador;
    private String fechahora;
    private String direccion;
    private String referencia;
    private String status;
    private String costo;
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getVehiculoCompleto() {
        return vehiculoCompleto;
    }

    public void setVehiculoCompleto(String vehiculoCompleto) {
        this.vehiculoCompleto = vehiculoCompleto;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    private String nombre;
    private String vehiculoCompleto;

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    private String telefono;

    public modeloAdministrador(String identificador,String fechahora,String direccion,String referencia,
                               String status,String nombre,String vehiculoCompleto,String costo, String telefono)
    {
        this.identificador=identificador;
        this.fechahora=fechahora;
        this.direccion=direccion;
        this.referencia=referencia;
        this.status=status;
        this.nombre=nombre;
        this.vehiculoCompleto=vehiculoCompleto;
        this.costo=costo;
        this.telefono=telefono;
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
