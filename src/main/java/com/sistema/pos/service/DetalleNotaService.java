package com.sistema.pos.service;

import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.entity.DetalleNotaE;
import com.sistema.pos.entity.Nota_Entrada;
import com.sistema.pos.entity.Producto;
import com.sistema.pos.repository.DetalleNotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleNotaService {

    @Autowired
    private DetalleNotaRepository detalleNotaRepository;

    @Autowired
    private ProductoService productoService;

    public List<DetalleNotaE> obtenerDetallesPorNota(Long idNota) {
        return detalleNotaRepository.findByNotaId_Id(idNota);
    }

    public DetalleNotaE obtenerDetallesPorId(Long idDetalle) {
        return detalleNotaRepository.findById(idDetalle)
                .orElseThrow(() -> new IllegalArgumentException("Detalle no encontrado"));
    }

    @LoggableAction
    public DetalleNotaE guardarDetalle(DetalleNotaE detalleNota, Nota_Entrada notaEntrada) {
        Producto producto = productoService.obtenerProducto(detalleNota.getProducto().getId());
        detalleNota.setProducto(producto);
        detalleNota.setNotaId(notaEntrada);
        detalleNota.setSubTotal(detalleNota.getCantidad() * detalleNota.getCostoUnitario());
        return detalleNotaRepository.save(detalleNota);
    }

    @LoggableAction
    public DetalleNotaE actualizarDetalle(Long id, DetalleNotaE detalleNota) {
        DetalleNotaE detalleNotaE = obtenerDetallesPorId(id);
        Producto producto = productoService.obtenerProducto(detalleNota.getProducto().getId());
        detalleNotaE.setProducto(producto);
        detalleNotaE.setCantidad(detalleNota.getCantidad());
        detalleNotaE.setCostoUnitario(detalleNota.getCostoUnitario());
        detalleNotaE.setSubTotal(detalleNotaE.getCantidad() * detalleNotaE.getCostoUnitario());
        return detalleNotaRepository.save(detalleNotaE);
    }

    @LoggableAction
    public void eliminarDetalle(Long idDetalle) {
        detalleNotaRepository.deleteById(idDetalle);
    }
}