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
import org.springframework.web.bind.annotation.RestController;

import com.sistema.pos.dto.CajaDTO;
import com.sistema.pos.entity.Caja;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.service.CajaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sucursales/{idSucursal}/caja")
public class CajaController {

	@Autowired
	private CajaService cajaService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<Caja>>> getAllCaja(@PathVariable Long idSucursal) {
		List<Caja> cajas = cajaService.findAllBySucursalId(idSucursal);
		return ResponseEntity.ok(ApiResponse.<List<Caja>>builder().statusCode(HttpStatus.OK.value())
				.message("Lista de cajas de la sucursal").data(cajas).build());
	}

	@GetMapping("/{idCaja}")
	public ResponseEntity<ApiResponse<Caja>> getCaja(@PathVariable Long idSucursal, @PathVariable Long idCaja) {
		Caja caja = cajaService.obtenerCajaDeSucursal(idSucursal, idCaja);
		return ResponseEntity.ok(ApiResponse.<Caja>builder().statusCode(HttpStatus.OK.value()).message("Detalles caja")
				.data(caja).build());
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Caja>> guardarCaja(@PathVariable Long idSucursal,
			@Valid @RequestBody CajaDTO cajaDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
			return ResponseEntity.badRequest().body(ApiResponse.<Caja>builder().errors(errors).build());
		}

		Caja caja = cajaService.saveInSucursal(idSucursal, cajaDTO);
		return new ResponseEntity<>(ApiResponse.<Caja>builder().statusCode(HttpStatus.CREATED.value())
				.message("Caja creado con éxito").data(caja).build(), HttpStatus.CREATED);
	}

	@PatchMapping("/{idCaja}")
	public ResponseEntity<ApiResponse<Caja>> actualizarCaja(@PathVariable Long idSucursal, @PathVariable Long idCaja,
			@Valid @RequestBody CajaDTO caja, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
			return ResponseEntity.badRequest().body(ApiResponse.<Caja>builder().errors(errors).build());
		}

		Caja cajaActual = cajaService.modificarCajaEnSucursal(idSucursal, idCaja, caja);
		return ResponseEntity.ok(ApiResponse.<Caja>builder().statusCode(HttpStatus.OK.value())
				.message("Almacén actualizado con éxito").data(cajaActual).build());
	}

	@PatchMapping("/{idCaja}/desactivar")
	public ResponseEntity<ApiResponse<Void>> desactivarCaja(@PathVariable Long idSucursal, @PathVariable Long idCaja) {
		cajaService.desactivarCajaEnSucursal(idSucursal, idCaja);
		return ResponseEntity.ok(ApiResponse.<Void>builder().statusCode(HttpStatus.OK.value())
				.message("Caja desactivado correctamente").build());
	}
	
	@PatchMapping("/{idCaja}/activar")
	public ResponseEntity<ApiResponse<Void>> activarCaja(@PathVariable Long idSucursal, @PathVariable Long idCaja) {
		cajaService.activarCajaEnSucursal(idSucursal, idCaja);
		return ResponseEntity.ok(ApiResponse.<Void>builder().statusCode(HttpStatus.OK.value())
				.message("Caja desactivado correctamente").build());
	}

}
