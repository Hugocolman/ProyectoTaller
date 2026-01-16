package com.springData.controller;

import com.springData.domain.Marca;
import com.springData.servicio.MarcaServicioImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/marcas")
public class MarcaController {

    @Autowired
    private MarcaServicioImp marcaServicio;

    @GetMapping
    public String listarMarcas(Model model) {
        model.addAttribute("marcas", marcaServicio.listar());
        return "marcas/index";  // Página de la lista de marcas
    }

    @GetMapping("/nuevo")
    public String nuevaMarca(Model model) {
        model.addAttribute("marca", new Marca());
        return "marcas/formulario";  // Página para crear nueva marca
    }

    @PostMapping("/guardar")
    public String guardarMarca(@ModelAttribute Marca marca) {
        marcaServicio.guardar(marca);
        return "redirect:/marcas";  // Redirige a la lista de marcas
    }

    @GetMapping("/editar/{id}")
    public String editarMarca(@PathVariable Long id, Model model) {
        model.addAttribute("marca", marcaServicio.buscarPorId(id));
        return "marcas/formulario";  // Página para editar una marca existente
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarMarca(@PathVariable Long id) {
        marcaServicio.eliminar(id); // Llama al método eliminar por ID
        return "redirect:/marcas";  // Redirige a la lista de marcas
    }
}

