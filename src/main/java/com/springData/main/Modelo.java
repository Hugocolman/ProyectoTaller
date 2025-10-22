package com.springData.main;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate; // Para manejar la fecha de alta
import lombok.Data;

@Data
@Entity
@Table(name = "modelos")
public class Modelo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idModelo;

    private String modelDescp;
    private LocalDate modelFchAlta;
    private String modelEstado;
    private long idMarca;
    private long idUsuario;
}
