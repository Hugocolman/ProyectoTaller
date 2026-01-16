package com.springData.controller;

import com.springData.domain.PedidoProveedor;
import com.springData.ProveedorRepository;
import com.springData.ProductoRepository;
import com.springData.domain.Proveedor;
import com.springData.domain.Producto;
import com.springData.servicio.PedidoProveedorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/compras/pedidos")
public class PedidoProveedorController {

    @Autowired
    private PedidoProveedorServicio servicio;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping({"", "/lista"})
    public String listar(@RequestParam(value = "q", required = false) String q,
                         @PageableDefault(size = 10) Pageable pageable,
                         Model model) {
        Page<PedidoProveedor> page = servicio.listar(q, pageable);
        model.addAttribute("page", page);
        model.addAttribute("pedidos", page.getContent());
        model.addAttribute("q", q);
        return "compras/pedidos/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("pedido", new PedidoProveedor());
        model.addAttribute("proveedores", proveedorRepository.findAll());
        model.addAttribute("productos", productoRepository.findAll());
        return "compras/pedidos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@javax.validation.Valid @ModelAttribute PedidoProveedor p,
                          org.springframework.validation.BindingResult binding,
                          @RequestParam(value = "itemProductoId", required = false) Long[] itemProductoId,
                          @RequestParam(value = "itemCantidad", required = false) Integer[] itemCantidad,
                          @RequestParam(value = "itemCosto", required = false) java.math.BigDecimal[] itemCosto,
                          Model model) {
        if (p.getProveedor() != null && p.getProveedor().getId() != null) {
            Proveedor prov = proveedorRepository.findById(p.getProveedor().getId()).orElse(null);
            p.setProveedor(prov);
        }
        if (p.getFecha() == null) p.setFecha(java.time.LocalDate.now());
        // fuerza estado PENDIENTE al guardar
        p.setEstado("PENDIENTE");
        if (itemProductoId != null && itemCantidad != null && itemCosto != null
                && itemProductoId.length == itemCantidad.length && itemCantidad.length == itemCosto.length) {
            java.util.List<com.springData.domain.DetallePedidoProveedor> detalles = new java.util.ArrayList<>();
            java.math.BigDecimal total = java.math.BigDecimal.ZERO;
            for (int i = 0; i < itemProductoId.length; i++) {
                Long pid = itemProductoId[i];
                Integer cant = itemCantidad[i];
                java.math.BigDecimal costo = itemCosto[i];
                if (pid == null || cant == null || cant <= 0 || costo == null) continue;
                Producto prod = productoRepository.findById(pid).orElse(null);
                if (prod == null) continue;
                com.springData.domain.DetallePedidoProveedor d = new com.springData.domain.DetallePedidoProveedor();
                d.setPedido(p);
                d.setProducto(prod);
                d.setCantidad(cant);
                d.setCostoUnitario(costo);
                detalles.add(d);
                total = total.add(costo.multiply(new java.math.BigDecimal(cant)));
            }
            p.setDetalles(detalles);
            p.setTotal(total);
            if (detalles.isEmpty()) {
                model.addAttribute("pedido", p);
                model.addAttribute("proveedores", proveedorRepository.findAll());
                model.addAttribute("productos", productoRepository.findAll());
                model.addAttribute("error", "Debe agregar al menos una línea válida");
                return "compras/pedidos/formulario";
            }
        }
        if (binding.hasErrors()) {
            model.addAttribute("pedido", p);
            model.addAttribute("proveedores", proveedorRepository.findAll());
            model.addAttribute("productos", productoRepository.findAll());
            return "compras/pedidos/formulario";
        }
        servicio.guardar(p);
        return "redirect:/compras/pedidos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        var p = servicio.buscarPorId(id);
        if (p == null) return "redirect:/compras/pedidos";
        if (!"PENDIENTE".equalsIgnoreCase(p.getEstado())) {
            ra.addFlashAttribute("error", "Solo se puede editar un pedido en estado PENDIENTE");
            return "redirect:/compras/pedidos";
        }
        model.addAttribute("pedido", p);
        model.addAttribute("proveedores", proveedorRepository.findAll());
        model.addAttribute("productos", productoRepository.findAll());
        return "compras/pedidos/formulario";
    }

    @PostMapping("/autorizar/{id}")
    public String autorizar(@PathVariable Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        boolean ok = servicio.autorizar(id);
        ra.addFlashAttribute(ok ? "success" : "error", ok ? "Pedido autorizado" : "No se puede autorizar: estado inválido");
        return "redirect:/compras/pedidos";
    }

    @PostMapping("/desautorizar/{id}")
    public String desautorizar(@PathVariable Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        // Bloquear si hay OC asociada
        boolean ok = servicio.desautorizar(id);
        ra.addFlashAttribute(ok ? "success" : "error", ok ? "Pedido desautorizado" : "No se puede desautorizar: estado inválido o asociado a una OC");
        return "redirect:/compras/pedidos";
    }

    @PostMapping("/anular/{id}")
    public String anular(@PathVariable Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        boolean ok = servicio.anular(id);
        ra.addFlashAttribute(ok ? "success" : "error", ok ? "Pedido anulado" : "No se puede anular: estado inválido o asociado a una OC");
        return "redirect:/compras/pedidos";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        var p = servicio.buscarPorId(id);
        if (p == null) return "redirect:/compras/pedidos";
        model.addAttribute("pedido", p);
        return "compras/pedidos/detalle";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return "redirect:/compras/pedidos";
    }

    @PostMapping("/convertir/{id}")
    public String convertir(@PathVariable Long id) {
        Long ocId = servicio.convertirAOrdenCompra(id);
        return ocId != null ? "redirect:/compras/ordenes/editar/" + ocId : "redirect:/compras/pedidos";
    }
}
