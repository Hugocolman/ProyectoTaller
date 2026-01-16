/*
  MÓDULO DE VENTAS DESHABILITADO TEMPORALMENTE
  Controlador de Remitos comentado para retirar el módulo sin eliminar código.

  (Inicio de comentario de bloque: todo el archivo queda comentado)

package com.springData.controller;

import com.springData.RemitoRepository;
import com.springData.DetalleRemitoRepository;
import com.springData.PedidoRepository;
import com.springData.ProductoRepository;
import com.springData.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequestMapping("/remitos")
public class RemitoController {
    private final RemitoRepository remitoRepository;
    private final DetalleRemitoRepository detalleRemitoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public RemitoController(RemitoRepository remitoRepository,
                            DetalleRemitoRepository detalleRemitoRepository,
                            PedidoRepository pedidoRepository,
                            ProductoRepository productoRepository) {
        this.remitoRepository = remitoRepository;
        this.detalleRemitoRepository = detalleRemitoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }
    @org.springframework.beans.factory.annotation.Value("${app.remito.establecimiento:001}")
    private String remitoEstablecimiento;
    @org.springframework.beans.factory.annotation.Value("${app.remito.punto:001}")
    private String remitoPunto;

    @GetMapping
    public String index(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<Remito> page = remitoRepository.findAll(pageable);
        model.addAttribute("page", page);
        return "remitos/index";
    }

    @GetMapping("/nuevo/{pedidoId}")
    public String nuevoDesdePedido(@PathVariable Long pedidoId, Model model) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null) return "redirect:/pedidos";

        // Calcular pendientes por producto: cantidad pedida - sum(cantidades remito)
        Map<Long, Integer> entregadoPorProducto = new HashMap<>();
        remitoRepository.findByPedido_Id(pedidoId).forEach(r -> {
            if (r.getDetalles() != null) {
                for (DetalleRemito dr : r.getDetalles()) {
                    Long prodId = dr.getProducto() != null ? dr.getProducto().getId() : null;
                    if (prodId != null) {
                        entregadoPorProducto.merge(prodId, dr.getCantidad(), Integer::sum);
                    }
                }
            }
        });

        // Construir líneas con pendiente
        List<Map<String, Object>> lineas = new ArrayList<>();
        if (pedido.getDetalles() != null) {
            for (DetallePedido dp : pedido.getDetalles()) {
                Long prodId = dp.getProducto() != null ? dp.getProducto().getId() : null;
                if (prodId == null) continue;
                int yaEntregado = entregadoPorProducto.getOrDefault(prodId, 0);
                int pendiente = Math.max(0, dp.getCantidad() - yaEntregado);
                if (pendiente > 0) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("producto", dp.getProducto());
                    m.put("pendiente", pendiente);
                    lineas.add(m);
                }
            }
        }

        Remito remito = new Remito();
        remito.setPedido(pedido);
        remito.setFecha(LocalDate.now());
        model.addAttribute("remito", remito);
        model.addAttribute("lineas", lineas);
        return "remitos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Long pedidoId,
                          @RequestParam(value = "productoId", required = false) List<Long> productoIds,
                          @RequestParam(value = "entregar", required = false) List<Integer> entregarCantidades) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null) return "redirect:/pedidos";
        if (productoIds == null || entregarCantidades == null || productoIds.isEmpty()) {
            return "redirect:/pedidos/editar/" + pedidoId;
        }

        Remito remito = new Remito();
        remito.setPedido(pedido);
        remito.setFecha(LocalDate.now());
        remito = remitoRepository.save(remito);
        // Asignar numeración propia al remito
        String remitoNumero = String.format("%s-%s-%07d", remitoEstablecimiento, remitoPunto, remito.getId());
        remito.setNumero(remitoNumero);
        remito = remitoRepository.save(remito);

        // Para cada línea, validar contra pendiente y stock, y descontar
        for (int i = 0; i < productoIds.size(); i++) {
            Long prodId = productoIds.get(i);
            int aEntregar = entregarCantidades.get(i) != null ? entregarCantidades.get(i) : 0;
            if (aEntregar <= 0) continue;
            Producto prod = productoRepository.findById(prodId).orElse(null);
            if (prod == null) continue;

            // Calcular pendiente de este producto
            int pedidoCant = pedido.getDetalles().stream()
                    .filter(d -> d.getProducto() != null && d.getProducto().getId().equals(prodId))
                    .mapToInt(DetallePedido::getCantidad).sum();
            int entregado = remitoRepository.findByPedido_Id(pedido.getId()).stream()
                    .flatMap(r -> r.getDetalles() != null ? r.getDetalles().stream() : Stream.<DetalleRemito>empty())
                    .filter(dr -> dr.getProducto() != null && dr.getProducto().getId().equals(prodId))
                    .mapToInt(DetalleRemito::getCantidad).sum();
            int pendiente = Math.max(0, pedidoCant - entregado);
            int entregarFinal = Math.min(aEntregar, pendiente);
            if (entregarFinal <= 0) continue;

            // Descontar stock
            prod.setCantidad(Math.max(0, prod.getCantidad() - entregarFinal));
            productoRepository.save(prod);

            DetalleRemito dr = new DetalleRemito();
            dr.setRemito(remito);
            dr.setProducto(prod);
            dr.setCantidad(entregarFinal);
            detalleRemitoRepository.save(dr);
        }

        // Actualizar estado del pedido
        boolean completo = true;
        if (pedido.getDetalles() != null) {
            for (DetallePedido dp : pedido.getDetalles()) {
                Long prodId = dp.getProducto() != null ? dp.getProducto().getId() : null;
                if (prodId == null) continue;
                int pedidoCant = dp.getCantidad();
                int entregado = remitoRepository.findByPedido_Id(pedido.getId()).stream()
                        .flatMap(r -> r.getDetalles() != null ? r.getDetalles().stream() : Stream.<DetalleRemito>empty())
                        .filter(dr -> dr.getProducto() != null && dr.getProducto().getId().equals(prodId))
                        .mapToInt(DetalleRemito::getCantidad).sum();
                if (entregado < pedidoCant) { completo = false; break; }
            }
        }
        pedido.setEstado(completo ? "COMPLETADO" : "PENDIENTE");
        pedidoRepository.save(pedido);

        return "redirect:/remitos/detalle/" + remito.getId();
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Remito r = remitoRepository.findById(id).orElse(null);
        if (r == null) return "redirect:/remitos";
        model.addAttribute("remito", r);
        return "remitos/detalle";
    }

    @GetMapping("/imprimir/{id}")
    public String imprimir(@PathVariable Long id, Model model) {
        Remito r = remitoRepository.findById(id).orElse(null);
        if (r == null) return "redirect:/remitos";
        model.addAttribute("remito", r);
        return "remitos/imprimir";
    }

    @PostMapping("/anular/{id}")
    @Transactional
    public String anular(@PathVariable Long id) {
        Remito r = remitoRepository.findById(id).orElse(null);
        if (r == null) return "redirect:/remitos";
        if (!"ANULADO".equalsIgnoreCase(r.getEstado())) {
            // Reponer stock de cada detalle
            if (r.getDetalles() != null) {
                for (DetalleRemito dr : r.getDetalles()) {
                    if (dr.getProducto() == null) continue;
                    Producto p = productoRepository.findById(dr.getProducto().getId()).orElse(null);
                    if (p != null) {
                        p.setCantidad(p.getCantidad() + dr.getCantidad());
                        productoRepository.save(p);
                    }
                }
            }
            r.setEstado("ANULADO");
            remitoRepository.save(r);
            // Recalcular estado del pedido asociado (volver a PENDIENTE si corresponde)
            Pedido pedido = r.getPedido();
            if (pedido != null) {
                boolean completo = true;
                if (pedido.getDetalles() != null) {
                    for (DetallePedido dp : pedido.getDetalles()) {
                        Long prodId = dp.getProducto() != null ? dp.getProducto().getId() : null;
                        if (prodId == null) continue;
                        int pedidoCant = dp.getCantidad();
                int entregado = remitoRepository.findByPedido_Id(pedido.getId()).stream()
                                .flatMap(rx -> {
                                    if ("ANULADO".equalsIgnoreCase(rx.getEstado()) || rx.getDetalles() == null) {
                                        return java.util.stream.Stream.<DetalleRemito>empty();
                                    }
                                    return rx.getDetalles().stream();
                                })
                                .filter(drx -> drx.getProducto() != null && drx.getProducto().getId().equals(prodId))
                                .mapToInt(DetalleRemito::getCantidad).sum();
                        if (entregado < pedidoCant) { completo = false; break; }
                    }
                }
                pedido.setEstado(completo ? "COMPLETADO" : "PENDIENTE");
                pedidoRepository.save(pedido);
            }
        }
        return "redirect:/remitos/detalle/" + id;
    }
}
*/
