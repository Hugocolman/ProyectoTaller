package com.springData.servicio;

import com.springData.CuentaPagarRepository;
import com.springData.domain.CuentaPagar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CuentaPagarServicioImp implements CuentaPagarServicio {

    @Autowired
    private CuentaPagarRepository repository;

    @Override
    public Page<CuentaPagar> listar(String q, Pageable pageable) {
        if (q != null && !q.isBlank()) {
            return repository.findByProveedor_NombreContainingIgnoreCaseOrNumeroFacturaContainingIgnoreCase(q, q, pageable);
        }
        return repository.findAll(pageable);
    }

    @Override
    public CuentaPagar buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void guardar(CuentaPagar cp) {
        if (cp.getSaldo() == null || cp.getSaldo().compareTo(BigDecimal.ZERO) == 0) {
            cp.setSaldo(cp.getMonto());
        }
        if (cp.getFechaEmision() == null) {
            cp.setFechaEmision(java.time.LocalDate.now());
        }
        if (cp.getNumeroFactura() == null || cp.getNumeroFactura().isBlank()) {
            // Generar un identificador si no se proveyó número de factura (referencia interna)
            // Guardar primero si no tiene ID
            if (cp.getId() == null) {
                cp = repository.save(cp);
            }
            String numero = String.format("CXP-%1$tY%1$tm%1$td-%2$06d", java.time.LocalDate.now(), cp.getId());
            cp.setNumeroFactura(numero);
        }
        repository.save(cp);
    }

    @Override
    public void eliminar(Long id) {
        if (id != null) repository.deleteById(id);
    }

    @Override
    public void registrarPago(Long id, BigDecimal importe) {
        CuentaPagar cp = repository.findById(id).orElse(null);
        if (cp == null || importe == null) return;
        BigDecimal nuevoSaldo = cp.getSaldo().subtract(importe);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) <= 0) {
            cp.setSaldo(BigDecimal.ZERO);
            cp.setEstado("PAGADA");
        } else {
            cp.setSaldo(nuevoSaldo);
        }
        repository.save(cp);
    }
}
