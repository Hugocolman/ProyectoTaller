 package com.springData.controller;

import com.springData.domain.Barrio;
import com.springData.servicio.BarrioServicioImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/barrios")
public class BarrioController {

    @Autowired
    private BarrioServicioImp barrioServicio;

    @GetMapping
    public String listarBarrios(Model model) {
        model.addAttribute("barrios", barrioServicio.listar());
        return "barrios/index";  // Página de la lista de barrios
    }

    @GetMapping("/nuevo")
    public String nuevoBarrio(Model model) {
        model.addAttribute("barrio", new Barrio());
        return "barrios/formulario";  // Página para crear nuevo barrio
    }

    @PostMapping("/guardar")
    public String guardarBarrio(@ModelAttribute Barrio barrio) {
        barrioServicio.guardar(barrio);
        return "redirect:/barrios";  // Redirige a la lista de barrios
    }

    @GetMapping("/editar/{id}")
    public String editarBarrio(@PathVariable Long id, Model model) {
        model.addAttribute("barrio", barrioServicio.buscarPorId(id));
        return "barrios/formulario";  // Página para editar un barrio existente
    }

    @PostMapping("/actualizar")
    public String actualizarBarrio(@ModelAttribute Barrio barrio) {
        barrioServicio.guardar(barrio);
        return "redirect:/barrios";  // Redirige a la lista de barrios
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarBarrio(@PathVariable Long id) {
        barrioServicio.eliminar(id);
        return "redirect:/barrios";  // Redirige a la lista de barrios
    }
}
