package app.proyecto.taxiservicios;

public class modeloChofer {





    public modeloChofer(String cliente, String fechahora, String direccionRecoger, String direccionLlevar, String telefono,String identificador,String costo,String status,String correochofer) {
        this.cliente = cliente;
        this.fechahora = fechahora;
        this.direccionRecoger = direccionRecoger;
        this.direccionLlevar = direccionLlevar;
        this.telefono = telefono;
        this.identificador=identificador;
        this.costo=costo;
        this.status=status;
        this.correochofer=correochofer;
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

    public String  getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCorreochofer() {
        return correochofer;
    }

    public void setCorreochofer(String correochofer) {
        this.correochofer = correochofer;
    }

    private String direccionRecoger;
    private String direccionLlevar;
    private String telefono;
    private String cliente;
    private String identificador;
    private  String costo;
    private String status;
    private String correochofer;
}
