package com.springData.controller;

import com.springData.domain.Ciudad;
import com.springData.servicio.ciudadServicio;
import com.springData.servicio.PaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ciudades")
public class CiudadController {

    @Autowired
    private ciudadServicio ciuServicio;

    @Autowired
    private PaisService paisService;

    @GetMapping
    public String listarCiudades(Model model) {
        List<Ciudad> ciudades = ciuServicio.listaCiudades();
        model.addAttribute("ciudades", ciudades);
        return "ciudades/index";
    }

    @GetMapping("/nuevo")
    public String nuevaCiudad(Model model) {
        model.addAttribute("ciudad", new Ciudad());
        model.addAttribute("paises", paisService.obtenerTodos());
        return "ciudades/formulario";
    }

    @PostMapping("/guardar_ciudad")
    public String guardar_ciudad(@ModelAttribute("form") Ciudad ciuServ) {
        ciuServicio.guardar(ciuServ);
        return "redirect:/ciudades";
    }

    @GetMapping("/editar/{id}")
    public String editarCiudad(@PathVariable Long id, Model model) {
        Ciudad ciudad = ciuServicio.buscarPorId(id);
        if (ciudad == null) {
            throw new IllegalArgumentException("ID inválido: " + id);
        }
        model.addAttribute("ciudad", ciudad);
        model.addAttribute("paises", paisService.obtenerTodos());
        return "ciudades/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCiudad(@PathVariable Long id) {
        ciuServicio.eliminar(id);
        return "redirect:/ciudades";
    }
}

