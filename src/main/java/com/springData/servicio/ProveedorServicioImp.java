package com.springData.servicio;

import com.springData.domain.Proveedor;
import com.springData.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProveedorServicioImp implements ProveedorServicio {

    // Inyectamos el repositorio de Proveedor
    @Autowired
    private ProveedorRepository proveedorRepository;

    // Listar todos los proveedores
    @Override
    public Iterable<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    // Buscar proveedor por ID
    @Override
    public Optional<Proveedor> buscarPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    // Guardar o actualizar un proveedor
    @Override
    public void guardar(Proveedor proveedor) {
        proveedorRepository.save(proveedor);
    }

    // Eliminar un proveedor
    @Override
    public void eliminar(Long id) {
        proveedorRepository.deleteById(id);
    }
}
