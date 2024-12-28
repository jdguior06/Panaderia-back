package com.sistema.pos.repository;

import com.sistema.pos.entity.Categoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long > {
	
	List<Categoria> findByActivoTrue();
	
}
