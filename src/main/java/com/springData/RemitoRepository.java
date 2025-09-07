package com.springData;

import com.springData.domain.Remito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RemitoRepository extends JpaRepository<Remito, Long> {
    Page<Remito> findAll(Pageable pageable);
    List<Remito> findByPedido_Id(Long pedidoId);
}

