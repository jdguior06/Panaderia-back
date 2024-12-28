package com.sistema.pos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sucursal")
@Entity
public class Sucursal {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 20)
    private String codigo;
    
    @Column(length = 20)
    private String nit;
    
    @NotEmpty(message = "El nombre no debe estar vacio")
    private String nombre;
    
    private String razon_social;
    
    private String direccion;
    
    private boolean activo;


}