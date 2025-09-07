package com.springData.controller;

import com.springData.domain.MovimientoVenta;
import com.springData.domain.DetalleVenta;
import com.springData.domain.Producto;
import com.springData.MovimientoVentaRepository;
import com.springData.DetalleVentaRepository;
import com.springData.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ventas")
public class MovimientoVentaController {
    private final MovimientoVentaRepository repository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;

    public MovimientoVentaController(MovimientoVentaRepository repository, DetalleVentaRepository detalleVentaRepository, ProductoRepository productoRepository) {
        this.repository = repository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public String listarVentas(Model model) {
        List<MovimientoVenta> ventas = repository.findAll();
        model.addAttribute("ventas", ventas);
        return "ventas/index";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("venta", new MovimientoVenta());
        model.addAttribute("productos", productoRepository.findAll());
        return "ventas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute MovimientoVenta venta) {
        // Procesar detalles de venta y descontar stock
        if (venta.getDetalles() != null) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                Producto producto = productoRepository.findById(detalle.getProducto().getId()).orElse(null);
                if (producto != null && detalle.getCantidad() > 0 && producto.getCantidad() >= detalle.getCantidad()) {
                    // Descontar stock
                    producto.setCantidad(producto.getCantidad() - detalle.getCantidad());
                    productoRepository.save(producto);
                    // Set precio unitario por seguridad
                    detalle.setPrecioUnitario(producto.getPrecio());
                    detalle.setVenta(venta);
                }
            }
        }
        repository.save(venta);
        return "redirect:/ventas";
    }

    @GetMapping("/editar/{id}")
    public String editarVenta(@PathVariable Long id, Model model) {
        MovimientoVenta venta = repository.findById(id).orElse(null);
        if (venta != null) {
            model.addAttribute("venta", venta);
            model.addAttribute("productos", productoRepository.findAll());
            return "ventas/formulario";
        }
        return "redirect:/ventas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/ventas";
    }
}