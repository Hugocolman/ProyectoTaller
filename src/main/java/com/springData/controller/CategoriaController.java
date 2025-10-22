package com.springData.controller;

import com.springData.domain.Categoria;
import com.springData.servicio.CategoriaServicioImp;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaServicioImp categoriaServicio;

    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaServicio.listar());
        return "categorias/index";
    }

    @GetMapping("/nuevo")
    public String nuevaCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria) {
        categoriaServicio.guardar(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
public String editarCategoria(@PathVariable Long id, Model model) {
    Optional<Categoria> categoria = categoriaServicio.buscarPorId(id);
    
    // Pasamos el objeto directamente o null si no existe
    model.addAttribute("categoria", categoria.orElse(null));
    return "categorias/formulario";
}


    @PostMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        categoriaServicio.eliminar(id);
            return "redirect:/categorias";
    }
}
