package com.springData.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "detalle_remito")
public class DetalleRemito implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remito_id")
    private Remito remito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(nullable = false)
    private int cantidad;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Remito getRemito() { return remito; }
    public void setRemito(Remito remito) { this.remito = remito; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}

