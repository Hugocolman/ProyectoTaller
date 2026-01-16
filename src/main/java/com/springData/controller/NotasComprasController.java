package com.springData.controller;

import com.springData.MovimientoCompraRepository;
import com.springData.ProductoRepository;
import com.springData.servicio.NotaCompraServicio;
import com.springData.domain.NotaCompra;
import com.springData.domain.MovimientoCompra;
import com.springData.domain.Producto;
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
@RequestMapping("/compras/notas")
public class NotasComprasController {

    @Autowired private NotaCompraServicio servicio;
    @Autowired private MovimientoCompraRepository compraRepo;
    @Autowired private ProductoRepository productoRepo;

    @GetMapping
    public String listar(@RequestParam(value = "q", required = false) String q,
                         @RequestParam(value = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                         @RequestParam(value = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
                         @PageableDefault(size = 10) Pageable pageable,
                         Model model) {
        Page<NotaCompra> page = servicio.listar(q, desde, hasta, pageable);
        model.addAttribute("page", page);
        model.addAttribute("notas", page.getContent());
        model.addAttribute("q", q);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        return "compras/notas/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(@RequestParam(value = "compraId", required = false) Long compraId, Model model) {
        NotaCompra n = new NotaCompra();
        if (compraId != null) {
            MovimientoCompra c = compraRepo.findById(compraId).orElse(null);
            if (c != null) n.setCompra(c);
        }
        model.addAttribute("nota", n);
        model.addAttribute("compras", compraRepo.findAll());
        model.addAttribute("productos", productoRepo.findAll());
        return "compras/notas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute NotaCompra nota) {
        // Resolver compra y producto por ID si solo vienen los IDs
        if (nota.getCompra() != null && nota.getCompra().getId() != null) {
            MovimientoCompra c = compraRepo.findById(nota.getCompra().getId()).orElse(null);
            nota.setCompra(c);
        }
        if (nota.getProducto() != null && nota.getProducto().getId() != null) {
            Producto p = productoRepo.findById(nota.getProducto().getId()).orElse(null);
            nota.setProducto(p);
        }
        servicio.aplicar(nota);
        return "redirect:/compras/notas";
    }
}

