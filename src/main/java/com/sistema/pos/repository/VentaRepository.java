package com.sistema.pos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistema.pos.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>{
	
	@Query("SELECT SUM(v.total) FROM Venta v WHERE v.fechaVenta BETWEEN :startDate AND :endDate")
	Double sumTotalVentasByFechaVentaBetween(@Param("startDate") LocalDateTime startDate,
	                                         @Param("endDate") LocalDateTime endDate);
	
	@Query("SELECT v FROM Venta v WHERE v.fechaVenta BETWEEN :startDate AND :endDate")
    List<Venta> findVentasByFechaVentaBetween(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

}
