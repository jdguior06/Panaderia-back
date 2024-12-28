package com.sistema.pos.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "almacen")
@Entity
public class Almacen {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int numero;
    
    private String descripcion;
    
    private boolean activo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;
    
    @JsonIgnore
    @OneToMany(mappedBy = "almacen", cascade = CascadeType.ALL)
    private List<ProductoAlmacen> productosAlmacen;

}
