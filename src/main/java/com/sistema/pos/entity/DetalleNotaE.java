package com.sistema.pos.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "detalle_nota_entrada")
public class DetalleNotaE {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    private int cantidad;
    
    private float costoUnitario;
    
    private float subTotal;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name= "id_nota_entrada")
    @JsonIgnore
    private Nota_Entrada notaId;
}
