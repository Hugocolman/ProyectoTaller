package com.springData.controller;

import com.springData.domain.Ciudad;
import com.springData.CiudadRepository;
import com.springData.PaisRepository;
import com.springData.servicio.ciudadServicio;
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
    private CiudadRepository ciudadRepository;

    @Autowired
    private PaisRepository paisRepository;

    @GetMapping
    public String listarCiudades(Model model) {
        List<Ciudad> ciudades =  ciuServicio.listaCiudades();
        model.addAttribute("ciudades", ciudades);
        return "ciudades/index";
    }

    @GetMapping("/nuevo")
    public String nuevaCiudad(Model model) {
        model.addAttribute("ciudad", new Ciudad());
        model.addAttribute("paises", paisRepository.findAll());
        return "ciudades/formulario";
    }

    @PostMapping("/guardar_ciudad")
    public String guardar_ciudad(@ModelAttribute("form") Ciudad ciuServ) {
        ciuServicio.guardar(ciuServ);
        return "redirect:/ciudades";
    }

    @GetMapping("/editar/{id}")
    public String editarCiudad(@PathVariable Long id, Model model) {
        Ciudad ciudad = ciudadRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("ciudad", ciudad);
        model.addAttribute("paises", paisRepository.findAll());
        return "ciudades/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCiudad(@PathVariable Long id) {
        ciudadRepository.deleteById(id);
        return "redirect:/ciudades";
    }
}
