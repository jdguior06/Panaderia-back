package com.sistema.pos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "producto")
@Entity
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(length = 20)
	private String codigo;

	@Column(length = 50)
	@NotEmpty(message = "El nombre no debe estar vacio")
	private String nombre;

	@Column(length = 1000)
	private String descripcion;

	private float precioCompra;

	private float precioVenta;

	private String foto;

	@ManyToOne
	@JoinColumn(name = "id_categoria")
	private Categoria categoria;

	private boolean activo;

}
