package com.springData.controller;

import com.springData.*;
import com.springData.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    private final FacturaRepository facturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;
    private final RemitoRepository remitoRepository;
    private final NotaCreditoRepository notaCreditoRepository;

    public FacturaController(FacturaRepository facturaRepository,
                             DetalleFacturaRepository detalleFacturaRepository,
                             RemitoRepository remitoRepository,
                             NotaCreditoRepository notaCreditoRepository) {
        this.facturaRepository = facturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.remitoRepository = remitoRepository;
        this.notaCreditoRepository = notaCreditoRepository;
    }

    @Value("${app.factura.establecimiento:001}")
    private String establecimiento;

    @Value("${app.factura.punto:001}")
    private String puntoExpedicion;

    @Value("${app.iva.rate:0.10}")
    private BigDecimal ivaRate;

    @GetMapping
    public String listar(@RequestParam(value = "q", required = false) String q,
                         @PageableDefault(size = 10) Pageable pageable,
                         Model model) {
        Page<Factura> page = (q != null && !q.isBlank())
                ? facturaRepository.findByClienteContainingIgnoreCase(q, pageable)
                : facturaRepository.findAll(pageable);
        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "facturas/index";
    }

    @PostMapping("/emitir/{remitoId}")
    public String emitirDesdeRemito(@PathVariable Long remitoId) {
        // Evitar facturar dos veces el mismo remito
        if (facturaRepository.findByRemito_Id(remitoId).isPresent()) {
            return "redirect:/remitos/detalle/" + remitoId;
        }
        Remito r = remitoRepository.findById(remitoId).orElse(null);
        if (r == null) return "redirect:/remitos";
        Pedido pedido = r.getPedido();

        Factura f = new Factura();
        f.setRemito(r);
        f.setFecha(LocalDate.now());
        f.setCliente(pedido != null ? pedido.getCliente() : "");
        f.setNeto(BigDecimal.ZERO);
        f.setIva(BigDecimal.ZERO);
        f.setTotal(BigDecimal.ZERO);
        f = facturaRepository.save(f);

        BigDecimal neto = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;
        if (r.getDetalles() != null) {
            for (DetalleRemito dr : r.getDetalles()) {
                if (dr.getProducto() == null) continue;
                BigDecimal precio = dr.getProducto().getPrecio() != null ? dr.getProducto().getPrecio() : BigDecimal.ZERO;
                BigDecimal lineaIvaRate = dr.getProducto().getIvaRate() != null ? dr.getProducto().getIvaRate() : ivaRate;
                // Si hay pedido, intenta buscar el precio del detalle del pedido para ese producto
                if (pedido != null && pedido.getDetalles() != null) {
                    for (DetallePedido dp : pedido.getDetalles()) {
                        if (dp.getProducto() != null && dp.getProducto().getId().equals(dr.getProducto().getId()) && dp.getPrecioUnitario() != null) {
                            precio = dp.getPrecioUnitario();
                            break;
                        }
                    }
                }
                DetalleFactura df = new DetalleFactura();
                df.setFactura(f);
                df.setProducto(dr.getProducto());
                df.setCantidad(dr.getCantidad());
                df.setPrecioUnitario(precio);
                df.setIvaRate(lineaIvaRate);
                detalleFacturaRepository.save(df);
                BigDecimal lineaNeto = precio.multiply(new BigDecimal(dr.getCantidad()));
                neto = neto.add(lineaNeto);
                iva = iva.add(lineaNeto.multiply(lineaIvaRate));
            }
        }
        BigDecimal total = neto.add(iva);
        f.setNeto(neto);
        f.setIva(iva);
        f.setTotal(total);
        // Numeración fiscal: EEE-PPP-000000N (configurable establecimiento/punto)
        String numero = String.format("%s-%s-%07d", establecimiento, puntoExpedicion, f.getId());
        f.setNumero(numero);
        facturaRepository.save(f);
        return "redirect:/facturas/detalle/" + f.getId();
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Factura f = facturaRepository.findById(id).orElse(null);
        if (f == null) return "redirect:/facturas";
        model.addAttribute("factura", f);
        return "facturas/detalle";
    }

    @GetMapping("/imprimir/{id}")
    public String imprimir(@PathVariable Long id, Model model) {
        Factura f = facturaRepository.findById(id).orElse(null);
        if (f == null) return "redirect:/facturas";
        model.addAttribute("factura", f);
        return "facturas/imprimir";
    }

    @PostMapping("/anular/{id}")
    public String anularConNotaCredito(@PathVariable Long id) {
        Factura f = facturaRepository.findById(id).orElse(null);
        if (f == null) return "redirect:/facturas";
        if (!"ANULADA".equalsIgnoreCase(f.getEstado())) {
            // Emitir nota de crédito espejo
            NotaCredito nc = new NotaCredito();
            nc.setFactura(f);
            nc.setFecha(LocalDate.now());
            nc.setNeto(f.getNeto());
            nc.setIva(f.getIva());
            nc.setTotal(f.getTotal().negate()); // negativo opcional según práctica
            nc = notaCreditoRepository.save(nc);
            String numero = String.format("NC-%1$tY%1$tm%1$td-%2$06d", java.time.LocalDate.now(), nc.getId());
            nc.setNumero(numero);
            notaCreditoRepository.save(nc);
            // Marcar factura como ANULADA
            f.setEstado("ANULADA");
            facturaRepository.save(f);
        }
        return "redirect:/facturas/detalle/" + id;
    }
}
