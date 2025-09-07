
package com.springData.domain;
//import javax.persistence.Entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//@Data
@Entity
@Table(name="personas")

public class Persona implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_persona;
    private String nombre;
    private String apellido;
    private String edad;
    private String telefono;
    private String correo;
    private String direccion;
    //private String id_ciudad;
    //private String id_pais;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_ciudad")
    private Ciudad ciudad;
    
    @Column(name = "id_pais")
    private String idPais;
    
    @Column(name = "es_cliente")
    private boolean esCliente;
    
    @Override
    public String toString(){
    return "cliente [id_persona="+id_persona+",nombre="+nombre+","
            + "apellido="+apellido+",edad="+edad+","
            + "telefono="+telefono+",correo="+correo+","
            + "direccion="+direccion+",id_ciudad="+ciudad+",id_pais="+idPais+"]";
    }

    public long getId_persona() {
        return id_persona;
    }

    public void setId_persona(long id_persona) {
        this.id_persona = id_persona;
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

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }
    
    public boolean isEsCliente() {
        return esCliente;
    }
    
    public void setEsCliente(boolean esCliente) {
        this.esCliente = esCliente;
    }

   

  
    
    
    
}
