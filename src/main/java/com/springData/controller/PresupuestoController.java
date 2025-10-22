package com.springData.controller;

import com.springData.PresupuestoRepository;
import com.springData.ProductoRepository;
import com.springData.domain.DetallePresupuesto;
import com.springData.domain.Presupuesto;
import com.springData.domain.Producto;
import com.springData.PedidoRepository;
import com.springData.domain.Pedido;
import com.springData.domain.DetallePedido;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/presupuestos")
public class PresupuestoController {

    private final PresupuestoRepository presupuestoRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;

    public PresupuestoController(PresupuestoRepository presupuestoRepository,
                                 ProductoRepository productoRepository,
                                 PedidoRepository pedidoRepository) {
        this.presupuestoRepository = presupuestoRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping
    public String listar(@RequestParam(value = "q", required = false) String q,
                         @PageableDefault(size = 10) Pageable pageable,
                         Model model) {
        Page<Presupuesto> page = (q != null && !q.isBlank())
                ? presupuestoRepository.findByClienteContainingIgnoreCase(q, pageable)
                : presupuestoRepository.findAll(pageable);
        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "presupuestos/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Presupuesto p = new Presupuesto();
        p.setFecha(LocalDate.now());
        p.setTotal(BigDecimal.ZERO);
        model.addAttribute("presupuesto", p);
        model.addAttribute("productos", productoRepository.findAll());
        return "presupuestos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Presupuesto p = presupuestoRepository.findById(id).orElse(null);
        if (p == null) return "redirect:/presupuestos";
        model.addAttribute("presupuesto", p);
        model.addAttribute("productos", productoRepository.findAll());
        return "presupuestos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Presupuesto presupuesto) {
        if (presupuesto.getDetalles() != null) {
            for (DetallePresupuesto det : presupuesto.getDetalles()) {
                if (det.getProducto() == null) continue;
                Producto prod = productoRepository.findById(det.getProducto().getId()).orElse(null);
                if (prod != null && det.getCantidad() > 0) {
                    if (det.getPrecioUnitario() == null) {
                        BigDecimal precio = prod.getPrecio() != null ? prod.getPrecio() : BigDecimal.ZERO;
                        det.setPrecioUnitario(precio);
                    }
                    det.setPresupuesto(presupuesto);
                }
            }
        }
        presupuestoRepository.save(presupuesto);
        return "redirect:/presupuestos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        presupuestoRepository.deleteById(id);
        return "redirect:/presupuestos";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Presupuesto p = presupuestoRepository.findById(id).orElse(null);
        if (p == null) return "redirect:/presupuestos";
        model.addAttribute("presupuesto", p);
        return "presupuestos/detalle";
    }

    @GetMapping("/imprimir/{id}")
    public String imprimir(@PathVariable Long id, Model model) {
        Presupuesto p = presupuestoRepository.findById(id).orElse(null);
        if (p == null) return "redirect:/presupuestos";
        model.addAttribute("presupuesto", p);
        return "presupuestos/imprimir";
    }

    @PostMapping("/convertir/{id}")
    public String convertirAPedido(@PathVariable Long id) {
        Presupuesto p = presupuestoRepository.findById(id).orElse(null);
        if (p == null) return "redirect:/presupuestos";
        // Marcar presupuesto como ACEPTADO
        p.setEstado("ACEPTADO");
        presupuestoRepository.save(p);
        Pedido pedido = new Pedido();
        pedido.setCliente(p.getCliente());
        pedido.setFecha(LocalDate.now());
        pedido.setTotal(p.getTotal());
        pedido.setEstado("PENDIENTE");
        pedido.setOrigenPresupuestoId(p.getId());
        if (p.getDetalles() != null) {
            java.util.ArrayList<DetallePedido> dets = new java.util.ArrayList<>();
            for (DetallePresupuesto dp : p.getDetalles()) {
                DetallePedido d = new DetallePedido();
                d.setPedido(pedido);
                d.setProducto(dp.getProducto());
                d.setCantidad(dp.getCantidad());
                d.setPrecioUnitario(dp.getPrecioUnitario());
                dets.add(d);
            }
            pedido.setDetalles(dets);
        }
        pedido = pedidoRepository.save(pedido);
        return "redirect:/pedidos/editar/" + pedido.getId();
    }
}
