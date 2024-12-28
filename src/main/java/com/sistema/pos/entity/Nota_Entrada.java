package com.sistema.pos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "nota_entrada")
public class Nota_Entrada  {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDateTime fecha;
    
    private float total;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    @JsonIgnore
    private  Almacen almacen;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @OneToMany(mappedBy = "notaId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleNotaE> detalles;
    
    @PrePersist
    public void prePersist() {
    	fecha = LocalDateTime.now(ZoneId.of("America/La_Paz"));
    }

}