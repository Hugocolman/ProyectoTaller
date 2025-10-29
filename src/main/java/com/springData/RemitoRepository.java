package com.springData;

import com.springData.domain.Remito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface RemitoRepository extends JpaRepository<Remito, Long> {
    @NonNull
    List<Remito> findByPedido_Id(@NonNull Long pedidoId);
}

