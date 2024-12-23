package com.sistema.pos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.sistema.pos.entity.Cliente;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.service.ClienteService;
import com.sistema.pos.util.HttpStatusMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
	
	@Autowired
    private ClienteService service;

	@GetMapping
	public ResponseEntity<ApiResponse<List<Cliente>>> listarClientes(@RequestParam(value = "search", required = false) String searchTerm) {
		List<Cliente> clientes; //= service.listCliente();
		if (searchTerm != null && !searchTerm.isEmpty()) {
            clientes = service.buscarClientes(searchTerm);
        } else {
            clientes = service.listCliente(); 
        }
		return new ResponseEntity<>(
				ApiResponse.<List<Cliente>>builder()
						.statusCode(HttpStatus.OK.value())
						.message(HttpStatusMessage.getMessage(HttpStatus.OK))
						.data(clientes)
						.build(),
				HttpStatus.OK
		);
	}
    
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Cliente>> obtenerCliente(@PathVariable Long id) {
		try {
			Cliente usuariosOpt = service.obtenerClientePorId(id);
			return new ResponseEntity<>(
					ApiResponse.<Cliente>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(usuariosOpt)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Cliente>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
    

	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('PERMISO_EDITAR_CLIENTES')")
	public ResponseEntity<ApiResponse<Cliente>>actualizarUsuario(@PathVariable Long id, @Valid @RequestBody Cliente userDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Cliente>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Cliente usuarioActualizado = service.modificarCliente(id, userDTO);
			return new ResponseEntity<>(
					ApiResponse.<Cliente>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(usuarioActualizado)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Cliente>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}

	@PatchMapping("/{id}/desactivar")
	@PreAuthorize("hasAuthority('PERMISO_ELIMINAR_CLIENTES')")
	public ResponseEntity<ApiResponse<Void>> desactivarCliente(@PathVariable Long id) {
		try {
			service.eliminarCliente(id);
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(HttpStatus.OK.value())
							.message("Cliente desactivado correctamente")
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
	@PreAuthorize("hasAuthority('PERMISO_CREAR_CLIENTES')")
	public ResponseEntity<ApiResponse<Cliente>> guardarCliente(@Valid @RequestBody Cliente cliente, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Cliente>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Cliente user = service.registrarCliente(cliente);
			return new ResponseEntity<>(
					ApiResponse.<Cliente>builder()
							.statusCode(HttpStatus.CREATED.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.CREATED))
							.data(user)
							.build(),
					HttpStatus.CREATED
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Cliente>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}

}
