package com.springData;

import com.springData.domain.NotaCredito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaCreditoRepository extends JpaRepository<NotaCredito, Long> {
}

