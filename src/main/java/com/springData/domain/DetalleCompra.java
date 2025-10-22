package com.springData.domain;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_compra")
public class DetalleCompra implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id")
    private MovimientoCompra compra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    @NotNull
    private Producto producto;

    @Column(nullable = false)
    @Min(1)
    private int cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal costoUnitario;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public MovimientoCompra getCompra() { return compra; }
    public void setCompra(MovimientoCompra compra) { this.compra = compra; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public BigDecimal getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(BigDecimal costoUnitario) { this.costoUnitario = costoUnitario; }
}
