package com.springData.controller;

import com.springData.DetalleCompraRepository;
import com.springData.MovimientoCompraRepository;
import com.springData.ProductoRepository;
import com.springData.domain.DetalleCompra;
import com.springData.domain.MovimientoCompra;
import com.springData.domain.Producto;
import com.springData.domain.Proveedor;
import com.springData.ProveedorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/compras")
public class CompraController {

    private final MovimientoCompraRepository compraRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    public CompraController(MovimientoCompraRepository compraRepository,
                            DetalleCompraRepository detalleCompraRepository,
                            ProductoRepository productoRepository,
                            ProveedorRepository proveedorRepository) {
        this.compraRepository = compraRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @GetMapping
    public String listarCompras(@RequestParam(value = "factura", required = false) String factura,
                                @RequestParam(value = "prov", required = false) String prov,
                                @PageableDefault(size = 10) Pageable pageable,
                                Model model) {
        org.springframework.data.domain.Page<MovimientoCompra> page;
        if (factura != null && !factura.isBlank()) {
            page = compraRepository.findByNroFacturaContainingIgnoreCase(factura, pageable);
        } else if (prov != null && !prov.isBlank()) {
            page = compraRepository.findByProveedor_NombreContainingIgnoreCase(prov, pageable);
        } else {
            page = compraRepository.findAll(pageable);
        }
        model.addAttribute("page", page);
        model.addAttribute("factura", factura);
        model.addAttribute("prov", prov);
        return "compras/index";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("compra", new MovimientoCompra());
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "compras/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        MovimientoCompra compra = compraRepository.findById(id).orElse(null);
        if (compra == null) return "redirect:/compras";
        model.addAttribute("compra", compra);
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "compras/formulario";
    }

    @PostMapping("/guardar")
    @Transactional
    public String guardar(@ModelAttribute MovimientoCompra compra) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null ? auth.getName() : "system");
        // Normalizar proveedor por ID
        if (compra.getProveedor() != null && compra.getProveedor().getId() != null) {
            Proveedor prov = proveedorRepository.findById(compra.getProveedor().getId()).orElse(null);
            compra.setProveedor(prov);
        }
        // Si estÃ¡ ANULADA, no permitir editar
        if (compra.getId() != null) {
            MovimientoCompra existenteEstado = compraRepository.findById(compra.getId()).orElse(null);
            if (existenteEstado != null && "ANULADA".equalsIgnoreCase(existenteEstado.getEstado())) {
                return "redirect:/compras";
            }
        }
        // Si es ediciÃ³n: revertir el impacto de stock de los detalles anteriores
        if (compra.getId() != null) {
            MovimientoCompra existente = compraRepository.findById(compra.getId()).orElse(null);
            if (existente != null && existente.getDetalles() != null) {
                for (DetalleCompra detAnt : existente.getDetalles()) {
                    if (detAnt.getProducto() == null) continue;
                    Producto prodAnt = productoRepository.findById(detAnt.getProducto().getId()).orElse(null);
                    if (prodAnt != null) {
                        int nuevaCantidad = prodAnt.getCantidad() - detAnt.getCantidad();
                        prodAnt.setCantidad(Math.max(0, nuevaCantidad));
                        productoRepository.save(prodAnt);
                    }
                }
            }
            compra.setEditadoPor(user);
            compra.setEditadoEn(LocalDateTime.now());
        } else {
            compra.setCreadoPor(user);
            compra.setCreadoEn(LocalDateTime.now());
        }
        if (compra.getDetalles() != null) {
            for (DetalleCompra det : compra.getDetalles()) {
                if (det.getProducto() == null) continue;
                Producto prod = productoRepository.findById(det.getProducto().getId()).orElse(null);
                if (prod != null && det.getCantidad() > 0) {
                    // set costo unitario si viene vacÃ­o (fallback al precio actual)
                    if (det.getCostoUnitario() == null) {
                        BigDecimal costo = prod.getPrecio() != null ? prod.getPrecio() : BigDecimal.ZERO;
                        det.setCostoUnitario(costo);
                    }
                    // vincular detalle con compra
                    det.setCompra(compra);
                    // actualizar stock (sumar)
                    prod.setCantidad(prod.getCantidad() + det.getCantidad());
                    productoRepository.save(prod);
                }
            }
        }
        compraRepository.save(compra);
        return "redirect:/compras";
    }

    @PostMapping("/eliminar/{id}")
    @Transactional
    public String eliminar(@PathVariable Long id) {
        MovimientoCompra compra = compraRepository.findById(id).orElse(null);
        if (compra != null && compra.getDetalles() != null && !"ANULADA".equalsIgnoreCase(compra.getEstado())) {
            for (DetalleCompra det : compra.getDetalles()) {
                if (det.getProducto() == null) continue;
                Producto prod = productoRepository.findById(det.getProducto().getId()).orElse(null);
                if (prod != null) {
                    int nuevaCantidad = prod.getCantidad() - det.getCantidad();
                    prod.setCantidad(Math.max(0, nuevaCantidad));
                    productoRepository.save(prod);
                }
            }
        }
        compraRepository.deleteById(id);
        return "redirect:/compras";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        MovimientoCompra compra = compraRepository.findById(id).orElse(null);
        if (compra == null) return "redirect:/compras";
        model.addAttribute("compra", compra);
        return "compras/detalle";
    }

    @GetMapping("/imprimir/{id}")
    public String imprimir(@PathVariable Long id, Model model) {
        MovimientoCompra compra = compraRepository.findById(id).orElse(null);
        if (compra == null) return "redirect:/compras";
        model.addAttribute("compra", compra);
        return "compras/imprimir";
    }

    @PostMapping("/anular/{id}")
    @Transactional
    public String anular(@PathVariable Long id) {
        MovimientoCompra compra = compraRepository.findById(id).orElse(null);
        if (compra == null) return "redirect:/compras";
        if (!"ANULADA".equalsIgnoreCase(compra.getEstado())) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String user = (auth != null ? auth.getName() : "system");
            // revertir stock
            if (compra.getDetalles() != null) {
                for (DetalleCompra det : compra.getDetalles()) {
                    if (det.getProducto() == null) continue;
                    Producto prod = productoRepository.findById(det.getProducto().getId()).orElse(null);
                    if (prod != null) {
                        int nuevaCantidad = prod.getCantidad() - det.getCantidad();
                        prod.setCantidad(Math.max(0, nuevaCantidad));
                        productoRepository.save(prod);
                    }
                }
            }
            compra.setEstado("ANULADA");
            compra.setAnuladoPor(user);
            compra.setAnuladoEn(LocalDateTime.now());
            compraRepository.save(compra);
        }
        return "redirect:/compras";
    }
}


