
package com.springData.dto;



//@Data
public class PersonaCiudadDTO {
    private String nombre;
    private String apellido;
    private String edad;
    private String telefono;
    private String correo;
    private String direccion;
    private String ciudad;
    private String idPais;
    
    public PersonaCiudadDTO(String nombre,String apellido, String edad,String telefono, 
                            String correo, String direccion, String ciudad,String idPais){
                            this.nombre=nombre;
                            this.apellido=apellido;
                            this.edad=edad;        
                            this.telefono=telefono;
                            this.correo=correo;  
                            this.direccion=direccion;
                            this.ciudad=ciudad;
                            this.idPais=idPais;        
                            }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }


    
    
}
