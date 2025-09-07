package com.springData.controller;

import com.springData.domain.Producto;
import com.springData.servicio.ProductoServicioImp;
import com.springData.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoServicioImp productoServicio;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public String listarProductos(
            @RequestParam(value = "q", required = false) String q,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {
        Page<Producto> page = (q != null && !q.isBlank())
                ? productoRepository.findByNombreContainingIgnoreCase(q, pageable)
                : productoRepository.findAll(pageable);
        model.addAttribute("page", page);
        model.addAttribute("productos", page.getContent());
        model.addAttribute("q", q);
        return "productos/index";
    }

    @GetMapping("/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoServicio.guardar(producto);
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoServicio.buscarPorId(id));
        return "productos/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoServicio.eliminar(id);
        return "redirect:/productos";
    }
}
