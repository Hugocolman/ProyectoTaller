
package com.springData.domain;

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
@Table(name="ciudades")
public class Ciudad implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_ciudad")
    private long idCiudad;
    
    @Column(name="ciu_des")
    private String ciudad;
    
    //@Column(name="id_pais")
    //private Integer idPais;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_pais")
    private Pais idPais;
    
    @Column(name="ciu_es")
    private String ciuEs;

    @Override
    public String toString(){
        return "ciudad [id_ciudad="+idCiudad+", ciu_des="+ciudad+", id_pais="+idPais+", ciu_es="+ciuEs+"]";       
    }

    public long getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(long idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Pais getIdPais() {
        return idPais;
    }

    public void setIdPais(Pais idPais) {
        this.idPais = idPais;
    }


    public String getCiuEs() {
        return ciuEs;
    }

    public void setCiuEs(String ciuEs) {
        this.ciuEs = ciuEs;
    }

    public void setPais(Pais pais) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    

    
    
}
