package com.sistema.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaEntradaCompletoDTO {
    private Long almacen;
    private Long proveedor;
    private List<DetalleNotaDTO> detalles;
}