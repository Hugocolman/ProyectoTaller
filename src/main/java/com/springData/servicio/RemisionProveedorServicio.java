package com.springData.servicio;

import java.math.BigDecimal;

public interface RemisionProveedorServicio {
    Long registrar(com.springData.domain.RemisionProveedor remision,
                   Long[] itemProductoId,
                   Integer[] itemCantidad,
                   BigDecimal[] itemCosto);

    void anular(Long remisionId);
}

