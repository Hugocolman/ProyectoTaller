package com.springData.servicio;

import com.springData.domain.CuentaPagar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CuentaPagarServicio {
    Page<CuentaPagar> listar(String q, Pageable pageable);
    CuentaPagar buscarPorId(Long id);
    void guardar(CuentaPagar cp);
    void eliminar(Long id);
    void registrarPago(Long id, BigDecimal importe);
}

