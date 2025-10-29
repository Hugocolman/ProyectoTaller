package com.springData.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsController {

    @GetMapping("/docs")
    public String docsIndex() {
        return "docs/index";
    }

    @GetMapping("/docs/flujo")
    public String flujo() {
        return "docs/flujo";
    }

    @GetMapping("/docs/proveedor")
    public String proveedor() {
        return "docs/modulo-proveedor";
    }

    @GetMapping("/docs/compras")
    public String compras() {
        return "docs/modulo-compras";
    }

    @GetMapping("/docs/inventario")
    public String inventario() {
        return "docs/modulo-inventario";
    }

    @GetMapping("/docs/contabilidad")
    public String contabilidad() {
        return "docs/modulo-contabilidad";
    }


    @GetMapping("/docs/cliente")
    public String cliente() {
        return "docs/modulo-cliente";
    }
}
