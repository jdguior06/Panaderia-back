package com.sistema.pos.repository;

import com.sistema.pos.entity.Proveedor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRespository extends JpaRepository<Proveedor,Long> {
	
	@Query("SELECT p FROM Proveedor p WHERE " +
	           "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
	           "LOWER(p.telefono) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
	           "LOWER(p.direccion) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
	           "LOWER(p.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	List<Proveedor> buscarProveedores(@Param("searchTerm") String searchTerm);


	Optional<Proveedor> findById(Long proveedorId);
	
	List<Proveedor> findByActivoTrue();
	
}
