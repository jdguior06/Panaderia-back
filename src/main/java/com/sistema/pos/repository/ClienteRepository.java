package com.sistema.pos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistema.pos.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	public Optional<Cliente> findByEmail(String email);
	
	@Query("SELECT c FROM Cliente c WHERE " +
	           "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
	           "LOWER(c.apellido) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
	           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
	           "LOWER(c.nit) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	List<Cliente> buscarClientes(@Param("searchTerm") String searchTerm);
	
	List<Cliente> findByActivoTrue();

}
