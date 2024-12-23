package com.sistema.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.pos.entity.DetalleVenta;


@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

}
