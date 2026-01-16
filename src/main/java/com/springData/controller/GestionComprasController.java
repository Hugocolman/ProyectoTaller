package com.springData.controller;

import com.springData.MovimientoCompraRepository;
import com.springData.domain.MovimientoCompra;
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
@RequestMapping("/compras/gestion")
public class GestionComprasController {

    @Autowired private MovimientoCompraRepository compraRepo;
    @Autowired private com.springData.servicio.GestionComprasServicio gestionServicio;

    @GetMapping
    public String listar(@RequestParam(value = "q", required = false) String q,
                         @RequestParam(value = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                         @RequestParam(value = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
                         @PageableDefault(size = 10) Pageable pageable,
                         Model model) {
        LocalDate hoy = LocalDate.now();
        if (desde == null) desde = hoy.withDayOfMonth(1);
        if (hasta == null) hasta = hoy;

        Page<MovimientoCompra> page;
        if (q != null && !q.isBlank()) {
            page = compraRepo.findByProveedor_NombreContainingIgnoreCaseAndFechaBetween(q, desde, hasta, pageable);
        } else {
            page = compraRepo.findByFechaBetween(desde, hasta, pageable);
        }
        model.addAttribute("page", page);
        model.addAttribute("compras", page.getContent());
        model.addAttribute("q", q);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        return "compras/gestion";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        MovimientoCompra c = compraRepo.findById(id).orElse(null);
        if (c == null) return "redirect:/compras/gestion";
        model.addAttribute("compra", c);
        return "compras/gestion/detalle";
    }

    @PostMapping("/anular/{id}")
    public String anular(@PathVariable Long id) {
        gestionServicio.anularCompra(id);
        return "redirect:/compras/gestion";
    }
}
