package com.springData.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "presupuestos")
public class Presupuesto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String cliente; // nombre del cliente (simple)

    @Column(nullable = false)
    private LocalDate fecha;

    @Column
    private LocalDate validoHasta;

    @Column(columnDefinition = "TEXT")
    private String condiciones;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(length = 20)
    private String estado = "BORRADOR"; // BORRADOR/ENVIADO/ACEPTADO/RECHAZADO/VENCIDO

    @OneToMany(mappedBy = "presupuesto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePresupuesto> detalles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalDate getValidoHasta() { return validoHasta; }
    public void setValidoHasta(LocalDate validoHasta) { this.validoHasta = validoHasta; }
    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<DetallePresupuesto> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePresupuesto> detalles) { this.detalles = detalles; }
}

