package com.springData.controller;

import com.springData.MovimientoCompraRepository;
import com.springData.DetalleCompraRepository;
import com.springData.OrdenCompraRepository;
import com.springData.domain.MovimientoCompra;
import com.springData.domain.DetalleCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/compras/informe")
public class ComprasInformeController {

    @Autowired private MovimientoCompraRepository compraRepo;
    @Autowired private DetalleCompraRepository detalleRepo;
    @Autowired private OrdenCompraRepository ocRepo;

    @GetMapping
    public String informe(@RequestParam(value = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                          @RequestParam(value = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
                          Model model) {
        LocalDate hoy = LocalDate.now();
        if (desde == null) desde = hoy.withDayOfMonth(1);
        if (hasta == null) hasta = hoy;

        List<MovimientoCompra> compras = compraRepo.findByFechaBetween(desde, hasta);

        long totalCompras = compras.size();
        BigDecimal totalFacturado = compras.stream()
                .map(MovimientoCompra::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long proveedoresActivos = compras.stream()
                .map(c -> c.getProveedor() != null ? c.getProveedor().getId() : null)
                .filter(Objects::nonNull)
                .distinct().count();

        long ocPendientes = ocRepo.countByEstadoIn(Arrays.asList("BORRADOR", "EMITIDA"));

        // Serie temporal (por día)
        Map<LocalDate, BigDecimal> serie = new TreeMap<>();
        for (MovimientoCompra c : compras) {
            if (c.getFecha() == null) continue;
            serie.merge(c.getFecha(), c.getTotal() != null ? c.getTotal() : BigDecimal.ZERO, BigDecimal::add);
        }
        List<String> labels = serie.keySet().stream().map(LocalDate::toString).collect(Collectors.toList());
        List<BigDecimal> data = labels.stream().map(d -> serie.get(LocalDate.parse(d))).collect(Collectors.toList());

        // Top productos por cantidad
        List<DetalleCompra> detalles = detalleRepo.findByCompra_FechaBetween(desde, hasta);
        Map<String, Integer> topMap = new HashMap<>();
        for (DetalleCompra d : detalles) {
            String nombre = d.getProducto() != null ? d.getProducto().getNombre() : "(Sin producto)";
            topMap.merge(nombre, d.getCantidad(), Integer::sum);
        }
        List<Map.Entry<String, Integer>> topProductos = new ArrayList<>(topMap.entrySet());
        topProductos.sort((a,b) -> Integer.compare(b.getValue(), a.getValue()));
        if (topProductos.size() > 5) topProductos = topProductos.subList(0,5);

        // Top proveedores por monto
        Map<String, BigDecimal> topProvMap = new HashMap<>();
        for (MovimientoCompra c : compras) {
            String nombre = c.getProveedor() != null ? c.getProveedor().getNombre() : "(Sin proveedor)";
            BigDecimal t = c.getTotal() != null ? c.getTotal() : BigDecimal.ZERO;
            topProvMap.merge(nombre, t, BigDecimal::add);
        }
        List<Map.Entry<String, BigDecimal>> topProveedores = new ArrayList<>(topProvMap.entrySet());
        topProveedores.sort((a,b) -> b.getValue().compareTo(a.getValue()));
        if (topProveedores.size() > 5) topProveedores = topProveedores.subList(0,5);

        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("totalCompras", totalCompras);
        model.addAttribute("totalFacturado", totalFacturado);
        model.addAttribute("proveedoresActivos", proveedoresActivos);
        model.addAttribute("ocPendientes", ocPendientes);
        model.addAttribute("labels", labels);
        model.addAttribute("data", data);
        model.addAttribute("topProductos", topProductos);
        model.addAttribute("topProveedores", topProveedores);
        return "compras/informe";
    }
}

