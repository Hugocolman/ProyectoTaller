package com.springData.controller;

import com.springData.domain.Ciudad;
import com.springData.domain.Pais;
import com.springData.servicio.ciudadServicio;
import com.springData.servicio.PaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/paises")
public class PaisController {

    @Autowired
    private PaisService paisService;

    @Autowired
    private ciudadServicio ciudadService;

    // Listar todos los países
    @GetMapping
    public String listarPaises(Model model) {
        List<Pais> paises = (List<Pais>) paisService.obtenerTodos();
        model.addAttribute("paises", paises);
        return "paises/index"; // Vista correspondiente al listado de países
    }

    // Mostrar formulario para agregar un nuevo país
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoPais(Model model) {
        model.addAttribute("pais", new Pais()); // Objeto país vacío para el formulario
        return "paises/formulario"; // Vista del formulario para agregar/editar país
    }

    // Guardar o actualizar un país
    @PostMapping("/guardar")
    public String guardarPais(@ModelAttribute("pais") Pais pais) {
        paisService.guardar(pais); // Guarda o actualiza el país
        return "redirect:/paises"; // Redirige al listado de países
    }

    // Mostrar formulario para editar un país
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarPais(@PathVariable("id") Long id, Model model) {
        Optional<Pais> paisOptional = paisService.obtenerPorId(id);
        if (paisOptional.isPresent()) {
            model.addAttribute("pais", paisOptional.get()); // Carga el país existente en el formulario
            return "paises/formulario";
        } else {
            return "redirect:/paises"; // Si no encuentra el país, redirige al listado
        }
    }

    // Eliminar un país
    @PostMapping("/eliminar/{id}")
    public String eliminarPais(@PathVariable("id") Long id) {
        paisService.eliminar(id); // Elimina el país por ID
        return "redirect:/paises"; // Redirige al listado de países
    }

    // Listar ciudades de un país específico
    @GetMapping("/{id}/ciudades")
    public String listarCiudadesPorPais(@PathVariable("id") Long id, Model model) {
        Optional<Pais> paisOptional = paisService.obtenerPorId(id);
        if (paisOptional.isPresent()) {
            Pais pais = paisOptional.get();
            List<Ciudad> ciudades = ciudadService.buscarPorPais(pais);
            model.addAttribute("pais", pais);
            model.addAttribute("ciudades", ciudades);
            return "paises/ciudades"; // Vista específica para ciudades de un país
        } else {
            return "redirect:/paises"; // Si no encuentra el país, redirige al listado de países
        }
    }
}
