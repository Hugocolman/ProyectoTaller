package com.springData.controller;

import com.springData.domain.Rol;
import com.springData.servicio.RolServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private RolServicio rolServicio;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("roles", rolServicio.listar());
        return "roles/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("rol", new Rol());
        return "roles/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Rol rol) {
        rolServicio.guardar(rol);
        return "redirect:/roles";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Rol rol = rolServicio.buscarPorId(id);
        if (rol == null) {
            return "redirect:/roles";
        }
        model.addAttribute("rol", rol);
        return "roles/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        Rol rol = new Rol();
        rol.setId(id);
        rolServicio.eliminar(rol);
        return "redirect:/roles";
    }
}

