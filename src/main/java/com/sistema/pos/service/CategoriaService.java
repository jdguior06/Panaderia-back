package com.sistema.pos.service;

import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.dto.CategoriaDTO;
import com.sistema.pos.entity.Categoria;
import com.sistema.pos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
	
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }
    
    public List<Categoria> findAllActive() {
        return categoriaRepository.findByActivoTrue();
    }
    
    @LoggableAction
    public Categoria save(CategoriaDTO categoriaDTO) {
    	Categoria categoria = new Categoria();
    	categoria.setNombre(categoriaDTO.getNombre());
    	categoria.setDescripcion(categoriaDTO.getDescripcion());
    	categoria.setActivo(true);
        return categoriaRepository.save(categoria);
    }

    public Categoria findById(Long id) {
    	Optional<Categoria> categoria = categoriaRepository.findById(id);
		if(!categoria.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la categoria con el id" + id
					);
		}
		return categoria.get();
    }
    
    @LoggableAction
	public Categoria modificarCategoria(Long id, CategoriaDTO categoriaDTO) {
		Categoria categoriaAct = findById(id);
		categoriaAct.setNombre(categoriaDTO.getNombre());
		categoriaAct.setDescripcion(categoriaDTO.getDescripcion());
		return categoriaRepository.save(categoriaAct);
	}
    
    @LoggableAction
	public Categoria desactivarCategoria(Long id) {
		Categoria categoria = findById(id);
		categoria.setActivo(false);
		return categoriaRepository.save(categoria);
	}
    
    @LoggableAction
	public Categoria activarCategoria(Long id) {
		Categoria categoria = findById(id);
		categoria.setActivo(true);
		return categoriaRepository.save(categoria);
	}

    public boolean existsById(Long id) {
        return categoriaRepository.existsById(id);
    }
    
}
