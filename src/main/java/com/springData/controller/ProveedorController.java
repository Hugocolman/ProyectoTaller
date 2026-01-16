package com.springData.controller;

import com.springData.domain.Proveedor;
import com.springData.servicio.ProveedorServicioImp;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorServicioImp proveedorServicio;

    @GetMapping
    public String listarProveedores(Model model) {
        model.addAttribute("proveedores", proveedorServicio.listar());
        return "proveedores/index";
    }

    @GetMapping("/nuevo")
public String nuevoProveedor(Model model) {
    model.addAttribute("proveedor", new Proveedor()); // Crea un nuevo objeto proveedor para el formulario
    return "proveedores/formulario"; // Asegúrate de que el nombre del archivo de la vista sea correcto
}

@GetMapping("/editar/{id}")
public String editarProveedor(@PathVariable Long id, Model model) {
    Optional<Proveedor> proveedorOptional = proveedorServicio.buscarPorId(id); // Suponiendo que esto devuelve un Optional
    if (proveedorOptional.isPresent()) {
        model.addAttribute("proveedor", proveedorOptional.get()); // Pasa el objeto Proveedor directamente
    } else {
        // En caso de que no se encuentre el proveedor
        model.addAttribute("proveedor", new Proveedor()); // Para un nuevo proveedor si no se encuentra el id
    }
    return "proveedores/formulario"; // Nombre de la vista
}

@PostMapping("/guardar")
public String guardarProveedor(@ModelAttribute Proveedor proveedor) {
    proveedorServicio.guardar(proveedor); // Guarda o actualiza el proveedor
    return "redirect:/proveedores"; // Redirige después de guardar
}


    @PostMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id) {
        proveedorServicio.eliminar(id);
        return "redirect:/proveedores";
    }
}
