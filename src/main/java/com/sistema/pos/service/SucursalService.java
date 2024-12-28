package com.sistema.pos.service;

import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.dto.SucursalDTO;
import com.sistema.pos.entity.Sucursal;
import com.sistema.pos.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class SucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;

    public List<Sucursal> findAll() {
        return sucursalRepository.findAll();
    }
    
    public Sucursal findById(Long id) {
    	Optional<Sucursal> sucursal = sucursalRepository.findById(id);
    	if(!sucursal.isPresent()) {
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la sucursal con el id" + id);
    	}
    	return sucursal.get();
    }

    @LoggableAction
    public Sucursal save(SucursalDTO sucursalDTO) {
    	Sucursal sucursal = new Sucursal();
    	sucursal.setCodigo(sucursalDTO.getCodigo());
        sucursal.setNit(sucursalDTO.getNit());
        sucursal.setNombre(sucursalDTO.getNombre());
        sucursal.setRazon_social(sucursalDTO.getRazon_social());
        sucursal.setDireccion(sucursalDTO.getDireccion());
    	sucursal.setActivo(true);
        return sucursalRepository.save(sucursal);
    }
    
    @LoggableAction
    public Sucursal modificar(Long id, SucursalDTO sucursalDTO) {
    	Sucursal sucursal = findById(id);
    	sucursal.setCodigo(sucursalDTO.getCodigo());
    	sucursal.setNit(sucursalDTO.getNit());
    	sucursal.setNombre(sucursalDTO.getNombre());
    	sucursal.setDireccion(sucursalDTO.getDireccion());
    	sucursal.setRazon_social(sucursalDTO.getRazon_social());
    	return sucursalRepository.save(sucursal);
    }

    @LoggableAction
    public void desactivarSucursal(Long id) {
    	Sucursal sucursal = findById(id);
    	sucursal.setActivo(false);
        sucursalRepository.save(sucursal);
    }
    
    @LoggableAction
    public void activarSucursal(Long id) {
    	Sucursal sucursal = findById(id);
    	sucursal.setActivo(true);
        sucursalRepository.save(sucursal);
    }
    
    public boolean existsById(Long id) {
        return sucursalRepository.existsById(id);
    }

}