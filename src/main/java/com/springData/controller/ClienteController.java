package com.springData.controller;

import com.springData.domain.Persona;
import com.springData.servicio.personaServicio;
import com.springData.servicio.ciudadServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private personaServicio perServicio;

    @Autowired
    private ciudadServicio ciuServicio;

    @GetMapping
    public String listar(Model model) {
        List<Persona> clientes = perServicio.listarClientes();
        model.addAttribute("clientes", clientes);
        return "clientes/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new Persona());
        model.addAttribute("ciudades", ciuServicio.listaCiudades());
        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Persona cliente) {
        cliente.setEsCliente(true);
        perServicio.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id_persona}")
    public String editar(@PathVariable("id_persona") Long idPersona, Model model) {
        Persona cliente = perServicio.buscarPorId(idPersona);
        model.addAttribute("cliente", cliente);
        model.addAttribute("ciudades", ciuServicio.listaCiudades());
        return "clientes/formulario";
    }

    @PostMapping("/eliminar/{id_persona}")
    public String eliminar(@PathVariable("id_persona") Long idPersona) {
        Persona cliente = perServicio.buscarPorId(idPersona);
        perServicio.eliminar(cliente);
        return "redirect:/clientes";
    }
}
