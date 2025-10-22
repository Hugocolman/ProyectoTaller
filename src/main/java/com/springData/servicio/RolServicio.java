
package com.springData.servicio;

import com.springData.domain.Rol;

public interface RolServicio {
    Iterable<Rol> listar();
    Rol buscarPorId(Long id);
    void guardar(Rol rol);
    void eliminar(Rol rol);
}
