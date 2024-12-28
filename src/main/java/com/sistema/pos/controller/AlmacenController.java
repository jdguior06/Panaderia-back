package com.sistema.pos.controller;

import com.sistema.pos.dto.AlmacenDTO;
import com.sistema.pos.entity.Almacen;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.service.AlmacenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sucursales/{idSucursal}/almacen")
public class AlmacenController {
	
	@Autowired
    private AlmacenService almacenService;

	@GetMapping
    public ResponseEntity<ApiResponse<List<Almacen>>> getAllAlmacen(@PathVariable Long idSucursal) {
        List<Almacen> almacenes = almacenService.findAllBySucursalId(idSucursal);
        return ResponseEntity.ok(
                ApiResponse.<List<Almacen>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de almacenes de la sucursal")
                        .data(almacenes)
                        .build()
        );
    }

    @GetMapping("/todos")
    public ResponseEntity<ApiResponse<List<Almacen>>> getAllAlmacenes() {
        List<Almacen> almacenes = almacenService.findAll();
        return ResponseEntity.ok(
                ApiResponse.<List<Almacen>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de todos los almacenes")
                        .data(almacenes)
                        .build()
        );
    }

    @GetMapping("/{idAlmacen}")
    public ResponseEntity<ApiResponse<Almacen>> getAlmacen(
            @PathVariable Long idSucursal, @PathVariable Long idAlmacen) {
        Almacen almacen = almacenService.obtenerAlmacenDeSucursal(idSucursal, idAlmacen);
        return ResponseEntity.ok(
                ApiResponse.<Almacen>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Detalles del almacén")
                        .data(almacen)
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_ALMACENES')")
    public ResponseEntity<ApiResponse<Almacen>> guardarAlmacen(
            @PathVariable Long idSucursal, @Valid @RequestBody AlmacenDTO almacenDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Almacen>builder()
                            .errors(errors)
                            .build()
            );
        }

        Almacen almacen = almacenService.saveInSucursal(idSucursal, almacenDTO);
        return new ResponseEntity<>(
                ApiResponse.<Almacen>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Almacén creado con éxito")
                        .data(almacen)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{idAlmacen}")
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_ALMACENES')")
    public ResponseEntity<ApiResponse<Almacen>> actualizarAlmacen(
            @PathVariable Long idSucursal, @PathVariable Long idAlmacen,
            @Valid @RequestBody AlmacenDTO almacenDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Almacen>builder()
                            .errors(errors)
                            .build()
            );
        }

        Almacen almacenActual = almacenService.modificarAlmacenEnSucursal(idSucursal, idAlmacen, almacenDTO);
        return ResponseEntity.ok(
                ApiResponse.<Almacen>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Almacén actualizado con éxito")
                        .data(almacenActual)
                        .build()
        );
    }

    @PatchMapping("/{idAlmacen}/desactivar")
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_ALMACENES')")
    public ResponseEntity<ApiResponse<Void>> desactivarAlmacen(
            @PathVariable Long idSucursal, @PathVariable Long idAlmacen) {
        almacenService.desactivarAlmacenEnSucursal(idSucursal, idAlmacen);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Almacén desactivado correctamente")
                        .build()
        );
    }
    
    @PatchMapping("/{idAlmacen}/activar")
    @PreAuthorize("hasAuthority('PERMISO_ADMINISTRAR_ALMACENES')")
    public ResponseEntity<ApiResponse<Void>> activarAlmacen(
            @PathVariable Long idSucursal, @PathVariable Long idAlmacen) {
        almacenService.activarAlmacenEnSucursal(idSucursal, idAlmacen);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Almacén desactivado correctamente")
                        .build()
        );
    }
    
}
