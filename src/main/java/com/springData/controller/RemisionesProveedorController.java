package com.springData.controller;

import com.springData.RemisionProveedorRepository;
import com.springData.ProveedorRepository;
import com.springData.ProductoRepository;
import com.springData.domain.RemisionProveedor;
import com.springData.domain.Proveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/compras/remisiones")
public class RemisionesProveedorController {

    @Autowired private RemisionProveedorRepository repo;
    @Autowired private ProveedorRepository proveedorRepo;
    @Autowired private ProductoRepository productoRepo;
    @Autowired private com.springData.servicio.RemisionProveedorServicio remisionServicio;

    @GetMapping
    public String listar(@RequestParam(value = "q", required = false) String q,
                         @RequestParam(value = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                         @RequestParam(value = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
                         @PageableDefault(size = 10) Pageable pageable,
                         Model model) {
        LocalDate hoy = LocalDate.now();
        if (desde == null) desde = hoy.withDayOfMonth(1);
        if (hasta == null) hasta = hoy;
        Page<RemisionProveedor> page = (q != null && !q.isBlank())
                ? repo.findByProveedor_NombreContainingIgnoreCaseAndFechaBetween(q, desde, hasta, pageable)
                : repo.findByFechaBetween(desde, hasta, pageable);
        model.addAttribute("page", page);
        model.addAttribute("remisiones", page.getContent());
        model.addAttribute("q", q);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        return "compras/remisiones/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        RemisionProveedor r = new RemisionProveedor();
        r.setFecha(LocalDate.now());
        model.addAttribute("remision", r);
        model.addAttribute("proveedores", proveedorRepo.findAll());
        model.addAttribute("productos", productoRepo.findAll());
        return "compras/remisiones/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@javax.validation.Valid @ModelAttribute RemisionProveedor remision,
                          org.springframework.validation.BindingResult binding,
                          @RequestParam(value = "itemProductoId", required = false) Long[] itemProductoId,
                          @RequestParam(value = "itemCantidad", required = false) Integer[] itemCantidad,
                          @RequestParam(value = "itemCosto", required = false) java.math.BigDecimal[] itemCosto,
                          Model model) {
        if (remision.getProveedor() != null && remision.getProveedor().getId() != null) {
            Proveedor prov = proveedorRepo.findById(remision.getProveedor().getId()).orElse(null);
            remision.setProveedor(prov);
        }
        if (binding.hasErrors()) {
            model.addAttribute("remision", remision);
            model.addAttribute("proveedores", proveedorRepo.findAll());
            model.addAttribute("productos", productoRepo.findAll());
            return "compras/remisiones/formulario";
        }
        try {
            remisionServicio.registrar(remision, itemProductoId, itemCantidad, itemCosto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("remision", remision);
            model.addAttribute("proveedores", proveedorRepo.findAll());
            model.addAttribute("productos", productoRepo.findAll());
            model.addAttribute("error", ex.getMessage());
            return "compras/remisiones/formulario";
        }
        return "redirect:/compras/remisiones";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        RemisionProveedor r = repo.findById(id).orElse(null);
        if (r == null) return "redirect:/compras/remisiones";
        model.addAttribute("remision", r);
        return "compras/remisiones/detalle";
    }

    @PostMapping("/anular/{id}")
    public String anular(@PathVariable Long id) {
        remisionServicio.anular(id);
        return "redirect:/compras/remisiones";
    }
}
