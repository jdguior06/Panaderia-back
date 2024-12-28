package com.sistema.pos.service;

import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.dto.CajaDTO;
import com.sistema.pos.entity.*;
import com.sistema.pos.repository.CajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;


@Service
public class CajaService {

    @Autowired
    private CajaRepository cajaRepository;

    @Autowired
    private SucursalService sucursalService;

    public List<Caja> findAll() {
        return cajaRepository.findAll();
    }
    
	public List<Caja> findAllBySucursalId(Long idSucursal) {
        Sucursal sucursal = sucursalService.findById(idSucursal);
        return cajaRepository.findAllBySucursal(sucursal);
    }

    public Caja findById(Long id) {
    	Caja caja = cajaRepository.findById(id)
    	        .orElseThrow(() -> new IllegalArgumentException("Caja no encontrada"));

        return caja;
    }
    
    public Caja obtenerCajaDeSucursal(Long idSucursal, Long idCaja) {
        Sucursal sucursal = sucursalService.findById(idSucursal);
        Optional<Caja> caja = cajaRepository.findByIdAndSucursal(idCaja, sucursal);
        if (!caja.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la caja con ID " + idCaja + " en la sucursal " + idSucursal);
        }
        return caja.get();
    }
    
    @LoggableAction
    public Caja saveInSucursal(Long idSucursal, CajaDTO caja) {
        Sucursal sucursal = sucursalService.findById(idSucursal);
        Caja nuevo = new Caja();
        nuevo.setNombre(caja.getNombre());
        nuevo.setSucursal(sucursal);
        nuevo.setActivo(true);
        return cajaRepository.save(nuevo);
    }
    
    @LoggableAction
    public Caja modificarCajaEnSucursal(Long idSucursal, Long idCaja, CajaDTO cajaDTO) {
        Sucursal sucursal = sucursalService.findById(idSucursal);
        Caja caja = obtenerCajaDeSucursal(idSucursal, idCaja);
        caja.setNombre(cajaDTO.getNombre());
        caja.setSucursal(sucursal);
        return cajaRepository.save(caja);
    }
    
    @LoggableAction
    public void eliminarCaja(Long id) {
       Optional<Caja> caja = cajaRepository.findById(id);
       Caja eliminadacaja=caja.get();
       eliminadacaja.setActivo(false);
       cajaRepository.save(eliminadacaja);
    }

    public List<Caja> buscarCajas(String searchTerm) {
        return cajaRepository.buscarCajas(searchTerm);
    }
    
    @LoggableAction
    public void desactivarCajaEnSucursal(Long idSucursal, Long idCaja) {
        Caja caja = obtenerCajaDeSucursal(idSucursal, idCaja);
        caja.setActivo(false);
        cajaRepository.save(caja);
    }
    
    @LoggableAction
    public void activarCajaEnSucursal(Long idSucursal, Long idCaja) {
        Caja caja = obtenerCajaDeSucursal(idSucursal, idCaja);
        caja.setActivo(true);
        cajaRepository.save(caja);
    }

    public boolean existsById(Long id) {
        return cajaRepository.existsById(id);
    }



}

