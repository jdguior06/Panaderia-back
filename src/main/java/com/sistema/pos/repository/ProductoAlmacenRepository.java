package com.sistema.pos.repository;


import com.sistema.pos.entity.Almacen;
import com.sistema.pos.entity.Producto;
import com.sistema.pos.entity.ProductoAlmacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoAlmacenRepository extends JpaRepository<ProductoAlmacen,Long> {
	
    @Query("SELECT COUNT(c) FROM ProductoAlmacen c WHERE c.almacen.id = :idAlmacen AND c.producto.id = :idProducto")
    Long verificarProducto(@Param("idAlmacen") Long idAlmacen, @Param("idProducto") Long idProducto);

    Optional<ProductoAlmacen> findByAlmacen_IdAndProducto_Id(Long almacenId, Long productoId);

    List<ProductoAlmacen> findByAlmacen_IdAndActivoTrue(Long almacenId);
    
    List<ProductoAlmacen> findByActivoTrueAndAlmacenIn(List<Almacen> almacenes);
    
    List<ProductoAlmacen> findByProductoAndAlmacenIn(Producto producto, List<Almacen> almacenes);

    @Query("SELECT pa FROM ProductoAlmacen pa WHERE " +
            "(:almacen IS NULL OR pa.almacen.id = :almacen) AND " +
            "(:producto IS NULL OR pa.producto.id = :producto) AND " +
            "(:stockMin IS NULL OR pa.stock >= :stockMin) AND " +
            "(:stockMax IS NULL OR pa.stock <= :stockMax)")
    
    List<ProductoAlmacen> findByAlmacenAndProductoAndStockRange(
            @Param("almacen") Long almacen,
            @Param("producto") Long producto,
            @Param("stockMin") Integer stockMin,
            @Param("stockMax") Integer stockMax
    );
    
    @Query("SELECT pa FROM ProductoAlmacen pa WHERE pa.producto.id = :idProducto AND pa.stock >= :cantidad ORDER BY pa.stock DESC")
    Optional<ProductoAlmacen> findByProductoIdAndStockGreaterThanEqualOrderByStockDesc(
            @Param("idProducto") Long idProducto,
            @Param("cantidad") Integer cantidad);
    
    
    @Query("SELECT pa FROM ProductoAlmacen pa WHERE pa.almacen.id = :almacenId ORDER BY pa.ultimaModificacion DESC")
    List<ProductoAlmacen> findByAlmacenIdOrderByUltimaModificacionDesc(@Param("almacenId") Long almacenId);
    
    List<ProductoAlmacen> findByProductoId(Long productoId);

}
