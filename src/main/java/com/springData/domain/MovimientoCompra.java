package com.springData.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "movimientos_compra")
public class MovimientoCompra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    @NotNull
    private Proveedor proveedor;

    @Column(nullable = false)
    @NotNull
    private LocalDate fecha;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, length = 50)
    @NotNull
    @Size(min = 3, max = 50)
    private String metodoPago;

    @Column(length = 50)
    @Size(max = 50)
    private String nroFactura;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompra> detalles;

    @Column(length = 20)
    private String estado = "REGISTRADA";

    // Auditoría básica
    private String creadoPor;
    private LocalDateTime creadoEn;
    private String editadoPor;
    private LocalDateTime editadoEn;
    private String anuladoPor;
    private LocalDateTime anuladoEn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getNroFactura() { return nroFactura; }
    public void setNroFactura(String nroFactura) { this.nroFactura = nroFactura; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public List<DetalleCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompra> detalles) { this.detalles = detalles; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
    public String getEditadoPor() { return editadoPor; }
    public void setEditadoPor(String editadoPor) { this.editadoPor = editadoPor; }
    public LocalDateTime getEditadoEn() { return editadoEn; }
    public void setEditadoEn(LocalDateTime editadoEn) { this.editadoEn = editadoEn; }
    public String getAnuladoPor() { return anuladoPor; }
    public void setAnuladoPor(String anuladoPor) { this.anuladoPor = anuladoPor; }
    public LocalDateTime getAnuladoEn() { return anuladoEn; }
    public void setAnuladoEn(LocalDateTime anuladoEn) { this.anuladoEn = anuladoEn; }
}
