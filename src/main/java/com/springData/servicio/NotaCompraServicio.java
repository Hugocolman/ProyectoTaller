package com.springData.servicio;

import com.springData.domain.NotaCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface NotaCompraServicio {
    Page<NotaCompra> listar(String q, LocalDate desde, LocalDate hasta, Pageable pageable);
    NotaCompra buscarPorId(Long id);
    void aplicar(NotaCompra nota);
}

