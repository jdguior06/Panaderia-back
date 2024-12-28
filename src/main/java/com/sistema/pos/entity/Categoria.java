package com.sistema.pos.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Table (name= "categoria")
@Entity
public class Categoria {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotEmpty(message = "El nombre no debe estar vacio")
    @Column(length = 45, nullable = false, unique = true)
    private String nombre;
    
    @Column(length = 1000)
    private String descripcion;
    
    private boolean activo;

}
