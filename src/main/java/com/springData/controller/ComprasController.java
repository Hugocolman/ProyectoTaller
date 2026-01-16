package com.springData.controller;

import com.springData.MovimientoCompraRepository;
import com.springData.domain.MovimientoCompra;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class ComprasController {

    private final MovimientoCompraRepository compraRepo;

    public ComprasController(MovimientoCompraRepository compraRepo) {
        this.compraRepo = compraRepo;
    }

    @Value("${app.iva.rate:0.10}")
    private java.math.BigDecimal ivaRate;

    @GetMapping
    public String index() { return "compras/index"; }

    @GetMapping("/libro")
    public String libro(
            @RequestParam(value = "desde", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(value = "hasta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            Model model) {
        LocalDate hoy = LocalDate.now();
        if (desde == null) desde = hoy.withDayOfMonth(1);
        if (hasta == null) hasta = hoy;
        List<MovimientoCompra> compras = compraRepo.findByFechaBetween(desde, hasta);
        BigDecimal totalNeto = compras.stream()
                .map(MovimientoCompra::getTotal)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalIva = totalNeto.multiply(ivaRate);
        BigDecimal totalConIva = totalNeto.add(totalIva);
        model.addAttribute("compras", compras);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("ivaRate", ivaRate);
        model.addAttribute("totalNeto", totalNeto);
        model.addAttribute("totalIva", totalIva);
        model.addAttribute("totalConIva", totalConIva);
        return "compras/libro";
    }

    @GetMapping(value = "/libro/export", produces = "text/csv")
    public @ResponseBody String exportLibro(
            @RequestParam(value = "desde", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(value = "hasta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        LocalDate hoy = LocalDate.now();
        if (desde == null) desde = hoy.withDayOfMonth(1);
        if (hasta == null) hasta = hoy;
        List<MovimientoCompra> compras = compraRepo.findByFechaBetween(desde, hasta);
        StringBuilder sb = new StringBuilder();
        sb.append("Fecha,Proveedor,Neto,IVA,Total\n");
        for (MovimientoCompra c : compras) {
            java.math.BigDecimal neto = c.getTotal() != null ? c.getTotal() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal iva = neto.multiply(ivaRate);
            java.math.BigDecimal total = neto.add(iva);
            String proveedor = c.getProveedor() != null ? c.getProveedor().getNombre() : "";
            sb.append(c.getFecha()).append(',')
              .append('"').append(proveedor.replace("\"","'"))
              .append('"').append(',')
              .append(neto).append(',')
              .append(iva).append(',')
              .append(total).append('\n');
        }
        return sb.toString();
    }

    @GetMapping("/ajustes")
    public String ajustes() { return "compras/ajustes"; }

    // Eliminado: manejado por NotasComprasController

    // Rutas de remisiones gestionadas por RemisionesProveedorController

    // Informe manejado por ComprasInformeController
}
