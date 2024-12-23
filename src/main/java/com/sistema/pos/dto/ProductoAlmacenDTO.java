package com.sistema.pos.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoAlmacenDTO {
	private String nombre;
	private String descripcion;
	private Long id_producto;
	private int stock;
	private Long almacen_id;
	private LocalDateTime ultimaModificacion;
}
