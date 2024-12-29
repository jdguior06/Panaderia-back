package com.sistema.pos.controller;

import com.sistema.pos.dto.ProductoConsolidadoDTO;

import com.sistema.pos.dto.ProductoDTO;
import com.sistema.pos.dto.ProductoVentaDTO;
import com.sistema.pos.entity.Producto;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.service.ProductoAlmacenService;
import com.sistema.pos.service.ProductoService;
import com.sistema.pos.util.HttpStatusMessage;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private ProductoAlmacenService productoAlmacenService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Producto>>> getAllCategorias() {
		List<Producto> productos = productoService.findAll();
		return new ResponseEntity<>(
				ApiResponse.<List<Producto>>builder()
						.statusCode(HttpStatus.OK.value())
						.message(HttpStatusMessage.getMessage(HttpStatus.OK))
						.data(productos)
						.build(),
				HttpStatus.OK
		);
	}

	@GetMapping("/activos")
	public ResponseEntity<ApiResponse<List<Producto>>> productosActivos() {
		List<Producto> productos = productoService.listadeProductosActivos();
		return new ResponseEntity<>(
				ApiResponse.<List<Producto>>builder()
						.statusCode(HttpStatus.OK.value())
						.message(HttpStatusMessage.getMessage(HttpStatus.OK))
						.data(productos)
						.build(),
				HttpStatus.OK
		);
	}

    @PostMapping
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_PRODUCTOS')")
    public ResponseEntity<ApiResponse<Producto>> guardarProducto(@Valid @RequestBody ProductoDTO productoDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Producto>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Producto producto = productoService.save(productoDTO);
			return new ResponseEntity<>(
					ApiResponse.<Producto>builder()
							.statusCode(HttpStatus.CREATED.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.CREATED))
							.data(producto)
							.build(),
					HttpStatus.CREATED
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Producto>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Producto>> getProducto(@PathVariable Long id) {
		try {
			Producto producto = productoService.obtenerProducto(id);
			return new ResponseEntity<>(
					ApiResponse.<Producto>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(producto)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Producto>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
    
    @GetMapping("/consolidado/{idSucursal}")
    public ResponseEntity<ApiResponse<List<ProductoConsolidadoDTO>>> listarProductosConsolidadoPorSucursal(@PathVariable Long idSucursal) {
        List<ProductoConsolidadoDTO> productosConsolidados = productoAlmacenService.listarProductosConsolidadoPorSucursal(idSucursal);
        return ResponseEntity.ok(
            ApiResponse.<List<ProductoConsolidadoDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .data(productosConsolidados)
                .message("Productos consolidados obtenidos correctamente.")
                .build()
        );
    }

	@GetMapping("/por-sucursal")
	public ResponseEntity<List<ProductoVentaDTO>> obtenerProductosPorSucursal(@RequestParam Long idSucursal) {
		List<ProductoVentaDTO> productos = productoService.obtenerProductosPorSucursal(idSucursal);
		return ResponseEntity.ok(productos);
	}

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_PRODUCTOS')")
    public ResponseEntity<ApiResponse<Producto>> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Producto>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Producto productoActualizado = productoService.actualizarProducto(id, productoDTO);
			return new ResponseEntity<>(
					ApiResponse.<Producto>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(productoActualizado)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Producto>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_PRODUCTOS')")
    public ResponseEntity<ApiResponse<Void>> desactivarProducto(@PathVariable Long id) {
		try {
			productoService.desactivarProducto(id);
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(HttpStatus.OK.value())
							.message("Producto desactivado correctamente")
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
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_PRODUCTOS')")
    public ResponseEntity<ApiResponse<Void>> activarProducto(@PathVariable Long id) {
		try {
			productoService.activarProducto(id);
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(HttpStatus.OK.value())
							.message("Producto desactivado correctamente")
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
    
}
