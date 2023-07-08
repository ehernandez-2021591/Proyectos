
package org.eduardohernandez.bean;

public class Vecino {
    private String NIT;
    private String DPI;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String municipalidad;
    private Integer codigoPostal;
    private String telefono;

    public Vecino() {
    }

    public Vecino(String NIT, String DPI, String nombres, String apellidos, String direccion, String municipalidad, Integer codigoPostal, String telefono) {
        this.NIT = NIT;
        this.DPI = DPI;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.municipalidad = municipalidad;
        this.codigoPostal = codigoPostal;
        this.telefono = telefono;
    }

    public String getNIT() {
        return NIT;
    }

    public void setNIT(String NIT) {
        this.NIT = NIT;
    }

    public String getDPI() {
        return DPI;
    }

    public void setDPI(String DPI) {
        this.DPI = DPI;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipalidad() {
        return municipalidad;
    }

    public void setMunicipalidad(String municipalidad) {
        this.municipalidad = municipalidad;
    }

    public Integer getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(Integer codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
      @Override
     public String toString() {
        return getNIT()+" | "+getNombres()+" "+getApellidos();}
}
