package com.sistema.pos.service;

import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.entity.Proveedor;
import com.sistema.pos.repository.ProveedorRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {
	
    @Autowired
    private ProveedorRespository proveedorRespository;

    public List<Proveedor> listProveedor() {
		return proveedorRespository.findAll();
	}
    
    public List<Proveedor> listProveedorActivos() {
		return proveedorRespository.findByActivoTrue();
	}
	
	public Proveedor obtenerProveedorPorId(Long id) {
		Optional<Proveedor> proveedor = proveedorRespository.findById(id);
		if (proveedor.isPresent()) {
			return proveedor.get();
		}else {
			throw new UsernameNotFoundException("El usuario no se encuentra");
		}
	}
	
	@LoggableAction
	public Proveedor registrarProveedor(Proveedor proveedor) {
		proveedor.setActivo(true);
		return proveedorRespository.save(proveedor);
	}
	
	@LoggableAction
	public Proveedor modificarProveedor(Long id, Proveedor proveedor) {
		Proveedor proveedorModificado = obtenerProveedorPorId(id);
		proveedorModificado.setNombre(proveedor.getNombre());
		proveedorModificado.setEmail(proveedor.getEmail());
		proveedorModificado.setTelefono(proveedor.getTelefono());
		proveedorModificado.setDireccion(proveedor.getDireccion());
		return proveedorRespository.save(proveedorModificado);
	}
	
	@LoggableAction
	public void desactivarProveedor(Long id) {
		Proveedor proveedor = obtenerProveedorPorId(id);
		proveedor.setActivo(false);
		proveedorRespository.save(proveedor);
	}
	
	@LoggableAction
	public void activarProveedor(Long id) {
		Proveedor proveedor = obtenerProveedorPorId(id);
		proveedor.setActivo(true);
		proveedorRespository.save(proveedor);
	}
	
	public List<Proveedor> buscarProveedores(String searchTerm) {
        return proveedorRespository.buscarProveedores(searchTerm);
    }

}
