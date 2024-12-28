package com.sistema.pos.controller;

import com.sistema.pos.dto.SucursalDTO;
import com.sistema.pos.entity.Sucursal;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.service.SucursalService;
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
@RequestMapping("/sucursal")
public class SucursalController {
	
	@Autowired
    private SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Sucursal>>> getAllSucursales() {
        List<Sucursal> sucursales = sucursalService.findAll();
        return new ResponseEntity<>(
                ApiResponse.<List<Sucursal>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                        .data(sucursales)
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_SUCURSALES')")
    public ResponseEntity<ApiResponse<Sucursal>> guardarSucursal(@Valid @RequestBody SucursalDTO sucursalDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(
                    ApiResponse.<Sucursal>builder()
                            .errors(errors)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
        try {
            Sucursal sucursal1 = sucursalService.save(sucursalDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Sucursal>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.CREATED))
                            .data(sucursal1)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Sucursal>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Sucursal>> getSucursal(@PathVariable Long id) {
        try {
            Sucursal sucursal = sucursalService.findById(id);
            return new ResponseEntity<>(
                    ApiResponse.<Sucursal>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                            .data(sucursal)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Sucursal>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_SUCURSALES')")
    public ResponseEntity<ApiResponse<Sucursal>> actualizarProducto(@PathVariable Long id, @Valid @RequestBody SucursalDTO sucursalDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(
                    ApiResponse.<Sucursal>builder()
                            .errors(errors)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
        try {
            Sucursal sucursalActualizado= sucursalService.modificar(id, sucursalDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Sucursal>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                            .data(sucursalActualizado)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Sucursal>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_SUCURSALES')")
    public ResponseEntity<ApiResponse<Void>> desactivarSucursal(@PathVariable Long id) {
        try {
            sucursalService.desactivarSucursal(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Sucursal desactivado correctamente")
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
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_SUCURSALES')")
    public ResponseEntity<ApiResponse<Void>> activarSucursal(@PathVariable Long id) {
        try {
            sucursalService.activarSucursal(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Sucursal desactivado correctamente")
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
