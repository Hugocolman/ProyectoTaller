package com.springData.controller;

import com.springData.domain.OrdenCompra;
import com.springData.servicio.OrdenCompraServicio;
import com.springData.ProveedorRepository;
import com.springData.ProductoRepository;
import com.springData.domain.Proveedor;
import com.springData.domain.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/compras/ordenes")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraServicio ordenServicio;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping({"", "/lista"})
    public String listar(@RequestParam(value = "q", required = false) String q,
                         @PageableDefault(size = 10) Pageable pageable,
                         Model model) {
        Page<OrdenCompra> page = ordenServicio.listar(q, pageable);
        model.addAttribute("page", page);
        model.addAttribute("ordenes", page.getContent());
        model.addAttribute("q", q);
        return "compras/ordenes/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("orden", new OrdenCompra());
        model.addAttribute("proveedores", proveedorRepository.findAll());
        model.addAttribute("productos", productoRepository.findAll());
        return "compras/ordenes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@javax.validation.Valid @ModelAttribute OrdenCompra orden,
                          org.springframework.validation.BindingResult binding,
                          @RequestParam(value = "itemProductoId", required = false) Long[] itemProductoId,
                          @RequestParam(value = "itemCantidad", required = false) Integer[] itemCantidad,
                          @RequestParam(value = "itemCosto", required = false) java.math.BigDecimal[] itemCosto,
                          Model model) {
        if (orden.getProveedor() != null && orden.getProveedor().getId() != null) {
            Proveedor prov = proveedorRepository.findById(orden.getProveedor().getId()).orElse(null);
            orden.setProveedor(prov);
        }
        // construir detalles si vienen arrays
        if (itemProductoId != null && itemCantidad != null && itemCosto != null
                && itemProductoId.length == itemCantidad.length && itemCantidad.length == itemCosto.length) {
            java.util.List<com.springData.domain.DetalleOrdenCompra> detalles = new java.util.ArrayList<>();
            java.math.BigDecimal total = java.math.BigDecimal.ZERO;
            for (int i = 0; i < itemProductoId.length; i++) {
                Long pid = itemProductoId[i];
                Integer cant = itemCantidad[i];
                java.math.BigDecimal costo = itemCosto[i];
                if (pid == null || cant == null || cant <= 0 || costo == null) continue;
                Producto prod = productoRepository.findById(pid).orElse(null);
                if (prod == null) continue;
                com.springData.domain.DetalleOrdenCompra d = new com.springData.domain.DetalleOrdenCompra();
                d.setOrden(orden);
                d.setProducto(prod);
                d.setCantidad(cant);
                d.setCostoUnitario(costo);
                detalles.add(d);
                total = total.add(costo.multiply(new java.math.BigDecimal(cant)));
            }
            orden.setDetalles(detalles);
            orden.setTotal(total);
            if (detalles.isEmpty()) {
                model.addAttribute("orden", orden);
                model.addAttribute("proveedores", proveedorRepository.findAll());
                model.addAttribute("productos", productoRepository.findAll());
                model.addAttribute("error", "Debe agregar al menos una línea válida");
                return "compras/ordenes/formulario";
            }
        }
        if (binding.hasErrors()) {
            model.addAttribute("orden", orden);
            model.addAttribute("proveedores", proveedorRepository.findAll());
            model.addAttribute("productos", productoRepository.findAll());
            return "compras/ordenes/formulario";
        }
        ordenServicio.guardar(orden);
        return "redirect:/compras/ordenes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("orden", ordenServicio.buscarPorId(id));
        model.addAttribute("proveedores", proveedorRepository.findAll());
        model.addAttribute("productos", productoRepository.findAll());
        return "compras/ordenes/formulario";
    }

    @PostMapping("/recibir/{id}")
    public String recibir(@PathVariable Long id) {
        ordenServicio.recibir(id);
        return "redirect:/compras/ordenes";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        ordenServicio.eliminar(id);
        return "redirect:/compras/ordenes";
    }
}
