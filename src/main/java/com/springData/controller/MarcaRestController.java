package com.springData.controller;

import com.springData.domain.Marca;
import com.springData.servicio.MarcaServicioImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/marcas") // Prefijo para REST API
public class MarcaRestController {

    @Autowired
    private MarcaServicioImp marcaServicio;

    // Obtener todas las marcas
    @GetMapping
    public ResponseEntity<Iterable<Marca>> listarMarcas() {
        Iterable<Marca> marcas = marcaServicio.listar();
        return ResponseEntity.ok(marcas); // Respuesta HTTP 200 con la lista de marcas
    }

    // Crear una nueva marca
    @PostMapping
    public ResponseEntity<Marca> guardarMarca(@RequestBody Marca marca) {
        
        marcaServicio.guardar(marca);
        Marca nuevaMarca = marca;
        return ResponseEntity.ok(nuevaMarca); // HTTP 200 con la marca guardada
    }

    // Obtener una marca por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Marca> obtenerMarcaPorId(@PathVariable Long id) {
        Marca marca = marcaServicio.buscarPorId(id);
        if (marca != null) {
            return ResponseEntity.ok(marca); // HTTP 200 si la marca existe
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 si no se encuentra
        }
    }

    // Actualizar una marca existente
    @PutMapping("/{id}")
    public ResponseEntity<Marca> actualizarMarca(@PathVariable Long id, @RequestBody Marca marcaActualizada) {
        Marca marca = marcaServicio.buscarPorId(id);
        if (marca != null) {
            marca.setNombre(marcaActualizada.getNombre());  
            // Actualiza otros campos necesarios
            marcaServicio.guardar(marca);
            return ResponseEntity.ok(marca); // HTTP 200 con la marca actualizada
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 si no se encuentra
        }
    }

    // Eliminar una marca
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMarca(@PathVariable Long id) {
        Marca marca = marcaServicio.buscarPorId(id);
        if (marca != null) {
            marcaServicio.eliminar(id);
            return ResponseEntity.noContent().build(); // HTTP 204 cuando se elimina con éxito
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 si no se encuentra
        }
    }
}
