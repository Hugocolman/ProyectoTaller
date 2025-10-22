
package com.springData.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Huguito
 */

//@Data
@Entity
@Table(name="paises")
public class Pais implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_pais")
    private long idPais;
    
    @Column(name="pa_des")
    private String paDes;

   @Override
    public String toString(){
        return "pais [id_pais="+idPais+", pa_des="+paDes+"]";       
    }

    public long getIdPais() {
        return idPais;
    }

    public void setIdPais(long idPais) {
        this.idPais = idPais;
    }

    public String getPaDes() {
        return paDes;
    }

    public void setPaDes(String paDes) {
        this.paDes = paDes;
    }
    
    
}
