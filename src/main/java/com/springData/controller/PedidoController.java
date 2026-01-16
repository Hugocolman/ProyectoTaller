/*
  MÓDULO DE VENTAS DESHABILITADO TEMPORALMENTE
  Controlador de Pedidos (Ventas) comentado para retirar el módulo sin eliminar código.

  (Inicio de comentario de bloque: todo el archivo queda comentado)
 
package com.springData.controller;

import com.springData.PedidoRepository;
import com.springData.RemitoRepository;
import com.springData.ProductoRepository;
import com.springData.domain.DetallePedido;
import com.springData.domain.Pedido;
import com.springData.domain.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final RemitoRepository remitoRepository;

    public PedidoController(PedidoRepository pedidoRepository, ProductoRepository productoRepository, RemitoRepository remitoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.remitoRepository = remitoRepository;
    }

    @GetMapping
    public String listar(@RequestParam(value = "q", required = false) String q,
                         @PageableDefault(size = 10) Pageable pageable,
                         Model model) {
        Page<Pedido> page = (q != null && !q.isBlank())
                ? pedidoRepository.findByClienteContainingIgnoreCase(q, pageable)
                : pedidoRepository.findAll(pageable);
        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "pedidos/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Pedido p = new Pedido();
        p.setFecha(LocalDate.now());
        p.setTotal(BigDecimal.ZERO);
        model.addAttribute("pedido", p);
        model.addAttribute("productos", productoRepository.findAll());
        return "pedidos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Pedido p = pedidoRepository.findById(id).orElse(null);
        if (p == null) return "redirect:/pedidos";
        model.addAttribute("pedido", p);
        model.addAttribute("productos", productoRepository.findAll());
        return "pedidos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pedido pedido) {
        if (pedido.getDetalles() != null) {
            for (DetallePedido det : pedido.getDetalles()) {
                if (det.getProducto() == null) continue;
                Producto prod = productoRepository.findById(det.getProducto().getId()).orElse(null);
                if (prod != null && det.getCantidad() > 0) {
                    if (det.getPrecioUnitario() == null) {
                        BigDecimal precio = prod.getPrecio() != null ? prod.getPrecio() : BigDecimal.ZERO;
                        det.setPrecioUnitario(precio);
                    }
                    det.setPedido(pedido);
                }
            }
        }
        pedidoRepository.save(pedido);
        return "redirect:/pedidos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        pedidoRepository.deleteById(id);
        return "redirect:/pedidos";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Pedido p = pedidoRepository.findById(id).orElse(null);
        if (p == null) return "redirect:/pedidos";
        // mapa productoId -> entregado (solo remitos no anulados)
        java.util.Map<Long, Integer> entregadoMap = new java.util.HashMap<>();
        remitoRepository.findByPedido_Id(p.getId()).forEach(r -> {
            if ("ANULADO".equalsIgnoreCase(r.getEstado()) || r.getDetalles() == null) return;
            r.getDetalles().forEach(dr -> {
                if (dr.getProducto() != null) {
                    Long pid = dr.getProducto().getId();
                    entregadoMap.merge(pid, dr.getCantidad(), Integer::sum);
                }
            });
        });
        model.addAttribute("pedido", p);
        model.addAttribute("entregadoMap", entregadoMap);
        return "pedidos/detalle";
    }

    @GetMapping("/imprimir/{id}")
    public String imprimir(@PathVariable Long id, Model model) {
        Pedido p = pedidoRepository.findById(id).orElse(null);
        if (p == null) return "redirect:/pedidos";
        model.addAttribute("pedido", p);
        return "pedidos/imprimir";
    }
}
*/
