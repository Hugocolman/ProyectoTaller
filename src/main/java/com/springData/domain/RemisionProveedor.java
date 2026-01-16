package com.springData.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "remisiones_proveedor")
public class RemisionProveedor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    @NotNull(message = "El proveedor es obligatorio")
    private Proveedor proveedor;

    @Column(nullable = false)
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @Column(length = 32, unique = true)
    private String numero;

    @Column(length = 20)
    private String estado = "ENTREGADA"; // ENTREGADA / ANULADA

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @OneToMany(mappedBy = "remision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleRemisionProveedor> detalles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public List<DetalleRemisionProveedor> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleRemisionProveedor> detalles) { this.detalles = detalles; }
}
