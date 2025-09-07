package com.springData;

import com.springData.domain.Barrio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarrioRepositorio extends JpaRepository<Barrio, Long> {
}
