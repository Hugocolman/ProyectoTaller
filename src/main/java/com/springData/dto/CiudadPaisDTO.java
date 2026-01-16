
package com.springData.dto;


public class CiudadPaisDTO {
    private String ciudad;
    private String idPais;
    private String ciuEs;
    
    public CiudadPaisDTO (String ciudad, String idPais, String ciuEs) {
        this.ciudad=ciudad;
        this.idPais=idPais; 
        this.ciuEs=ciuEs;
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

    public String getCiuEs() {
        return ciuEs;
    }

    public void setCiuEs(String ciuEs) {
        this.ciuEs = ciuEs;
    }

    
}
