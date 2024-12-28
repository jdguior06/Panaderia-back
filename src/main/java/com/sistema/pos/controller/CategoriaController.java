package com.sistema.pos.controller;

import com.sistema.pos.dto.CategoriaDTO;
import com.sistema.pos.entity.Categoria;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.service.CategoriaService;
import com.sistema.pos.util.HttpStatusMessage;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Categoria>>> getAllCategorias() {
		List<Categoria> categorias = categoriaService.findAll();
		return new ResponseEntity<>(
				ApiResponse.<List<Categoria>>builder()
						.statusCode(HttpStatus.OK.value())
						.message(HttpStatusMessage.getMessage(HttpStatus.OK))
						.data(categorias)
						.build(),
				HttpStatus.OK
		);
	}
    
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Categoria>>> getActiveCategories() {
        List<Categoria> categorias = categoriaService.findAllActive();
        return new ResponseEntity<>(
            ApiResponse.<List<Categoria>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                .data(categorias)
                .build(),
            HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> getCategoria(@PathVariable Long id) {
		try {
			Categoria categoria = categoriaService.findById(id);
			return new ResponseEntity<>(
					ApiResponse.<Categoria>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(categoria)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Categoria>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}

    @PostMapping
    public ResponseEntity<ApiResponse<Categoria>> guardarCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Categoria>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Categoria categoria = categoriaService.save(categoriaDTO);
			return new ResponseEntity<>(
					ApiResponse.<Categoria>builder()
							.statusCode(HttpStatus.CREATED.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.CREATED))
							.data(categoria)
							.build(),
					HttpStatus.CREATED
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Categoria>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}

    @PatchMapping("/{id}/desactivar")
	public ResponseEntity<ApiResponse<Void>> desactivarCategoria(@PathVariable Long id) {
		try {
			categoriaService.desactivarCategoria(id);
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(HttpStatus.OK.value())
							.message("Categoria desactivado correctamente")
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
    
    @PatchMapping("/{id}/activar")
	public ResponseEntity<ApiResponse<Void>> activarCategoria(@PathVariable Long id) {
		try {
			categoriaService.activarCategoria(id);
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(HttpStatus.OK.value())
							.message("Categoria desactivado correctamente")
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaDTO categoriaDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Categoria>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Categoria categoriaActualizado = categoriaService.modificarCategoria(id, categoriaDTO);
			return new ResponseEntity<>(
					ApiResponse.<Categoria>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(categoriaActualizado)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Categoria>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
    
}
