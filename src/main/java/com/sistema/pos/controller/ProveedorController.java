package com.sistema.pos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.pos.entity.Proveedor;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.service.ProveedorService;
import com.sistema.pos.util.HttpStatusMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/proveedor")
public class ProveedorController {
	
	@Autowired
    private ProveedorService service;

	@GetMapping
	public ResponseEntity<ApiResponse<List<Proveedor>>> listarProveedores(@RequestParam(value = "search", required = false) String searchTerm) {
		List<Proveedor> proveedores; 
		if (searchTerm != null && !searchTerm.isEmpty()) {
			proveedores = service.buscarProveedores(searchTerm);
        } else {
        	proveedores = service.listProveedor(); 
        }
		return new ResponseEntity<>(
				ApiResponse.<List<Proveedor>>builder()
						.statusCode(HttpStatus.OK.value())
						.message(HttpStatusMessage.getMessage(HttpStatus.OK))
						.data(proveedores)
						.build(),
				HttpStatus.OK
		);
	}
	
	@GetMapping("/activos")
	public ResponseEntity<ApiResponse<List<Proveedor>>> listarProveedoresActivos() {
		List<Proveedor> proveedores = service.listProveedorActivos(); 
		return new ResponseEntity<>(
				ApiResponse.<List<Proveedor>>builder()
						.statusCode(HttpStatus.OK.value())
						.message(HttpStatusMessage.getMessage(HttpStatus.OK))
						.data(proveedores)
						.build(),
				HttpStatus.OK
		);
	}
    
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Proveedor>> obtenerProveedor(@PathVariable Long id) {
		try {
			Proveedor proveedoresOpt = service.obtenerProveedorPorId(id);
			return new ResponseEntity<>(
					ApiResponse.<Proveedor>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(proveedoresOpt)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Proveedor>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
    

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<Proveedor>>actualizarProveedor(@PathVariable Long id, @Valid @RequestBody Proveedor proveedorDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Proveedor>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Proveedor proveedorActualizado = service.modificarProveedor(id, proveedorDTO);
			return new ResponseEntity<>(
					ApiResponse.<Proveedor>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(proveedorActualizado)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Proveedor>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}

	@PatchMapping("/{id}/desactivar")
	public ResponseEntity<ApiResponse<Void>> desactivarProveedor(@PathVariable Long id) {
		try {
			service.desactivarProveedor(id);
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(HttpStatus.OK.value())
							.message("Prveedor desactivado correctamente")
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
	public ResponseEntity<ApiResponse<Void>> activarProveedor(@PathVariable Long id) {
		try {
			service.activarProveedor(id);
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(HttpStatus.OK.value())
							.message("Prveedor desactivado correctamente")
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
	
	@PostMapping
	public ResponseEntity<ApiResponse<Proveedor>> guardarProveedor(@Valid @RequestBody Proveedor proveedorDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Proveedor>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Proveedor proveedor = service.registrarProveedor(proveedorDTO);
			return new ResponseEntity<>(
					ApiResponse.<Proveedor>builder()
							.statusCode(HttpStatus.CREATED.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.CREATED))
							.data(proveedor)
							.build(),
					HttpStatus.CREATED
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Proveedor>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}


}
