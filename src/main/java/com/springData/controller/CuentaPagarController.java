package com.springData.controller;

import com.springData.domain.CuentaPagar;
import com.springData.servicio.CuentaPagarServicio;
import com.springData.ProveedorRepository;
import com.springData.domain.Proveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/compras/cuentas")
public class CuentaPagarController {

    @Autowired
    private CuentaPagarServicio servicio;
    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping({"", "/lista"})
    public String listar(@RequestParam(value = "q", required = false) String q,
                         @PageableDefault(size = 10) Pageable pageable,
                         Model model) {
        Page<CuentaPagar> page = servicio.listar(q, pageable);
        model.addAttribute("page", page);
        model.addAttribute("cuentas", page.getContent());
        model.addAttribute("q", q);
        return "compras/cuentas/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cuenta", new CuentaPagar());
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "compras/cuentas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@javax.validation.Valid @ModelAttribute CuentaPagar cp,
                          org.springframework.validation.BindingResult binding,
                          Model model) {
        if (cp.getProveedor() != null && cp.getProveedor().getId() != null) {
            Proveedor prov = proveedorRepository.findById(cp.getProveedor().getId()).orElse(null);
            cp.setProveedor(prov);
        }
        if (binding.hasErrors()) {
            model.addAttribute("cuenta", cp);
            model.addAttribute("proveedores", proveedorRepository.findAll());
            return "compras/cuentas/formulario";
        }
        servicio.guardar(cp);
        return "redirect:/compras/cuentas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("cuenta", servicio.buscarPorId(id));
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "compras/cuentas/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return "redirect:/compras/cuentas";
    }

    @PostMapping("/pagar/{id}")
    public String pagar(@PathVariable Long id, @RequestParam("importe") BigDecimal importe) {
        servicio.registrarPago(id, importe);
        return "redirect:/compras/cuentas";
    }
}
