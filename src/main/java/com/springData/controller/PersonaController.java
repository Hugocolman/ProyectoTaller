package com.springData.controller;

import com.springData.domain.Persona;
import com.springData.domain.Ciudad;
import com.springData.servicio.personaServicio;
import com.springData.servicio.ciudadServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/personas")
public class PersonaController {

    @Autowired
    private personaServicio perServicio;

    @Autowired
    private ciudadServicio ciuServicio;

    // Usar solo la capa de servicio; evitamos referenciar el repositorio directamente

    // Mostrar todas las personas
    @GetMapping
    public String listarPersonas(@RequestParam(value = "q", required = false) String q,
                                 @PageableDefault(size = 10) Pageable pageable,
                                 Model model) {
        Page<Persona> page = (q != null && !q.isBlank())
                ? perServicio.buscarPorNombreOApellido(q, pageable)
                : perServicio.listar(pageable);
        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "personas/index";
    }

    // Formulario para agregar una nueva persona
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("persona", new Persona());
        List<Ciudad> listCiudades = ciuServicio.listaCiudades();
        model.addAttribute("ciudades", listCiudades);
        return "personas/formulario"; // Vista: cambiar.html
    }

    // Guardar una nueva persona o actualizar una existente
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Persona persona) {
        perServicio.guardar(persona);
        return "redirect:/personas"; // Redirección a la lista de personas
    }

    // Formulario para editar una persona existente
    @GetMapping("/editar/{id_persona}")
    public String editar(@PathVariable("id_persona") Long idPersona, Model model) {
        Persona persona = perServicio.buscarPorId(idPersona); // Buscar por ID
        model.addAttribute("persona", persona);

        // Lista de ciudades
        List<Ciudad> listCiudades = ciuServicio.listaCiudades();
        model.addAttribute("ciudades", listCiudades);

        return "personas/formulario"; // Vista: cambiar.html
    }

    // Eliminar una persona
    @PostMapping("/eliminar/{id_persona}")
    public String eliminar(@PathVariable("id_persona") Long idPersona) {
        Persona persona = perServicio.buscarPorId(idPersona);
        perServicio.eliminar(persona);
        return "redirect:/personas"; // Redirección a la lista de personas
    }
}
