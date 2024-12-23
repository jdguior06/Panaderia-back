package com.sistema.pos.controller;

import com.sistema.pos.dto.NotaEntradaCompletoDTO;
import com.sistema.pos.entity.Nota_Entrada;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.service.NotaEntradaService;
import com.sistema.pos.util.HttpStatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notaEntrada")
public class NotaEntradaController {

    @Autowired
    private NotaEntradaService notaEntradaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Nota_Entrada>>> obtenerTodasLasNotas() {
        try {
            List<Nota_Entrada> notas = notaEntradaService.obtenerTodasLasNotas();
            return ResponseEntity.ok(
                    ApiResponse.<List<Nota_Entrada>>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                            .data(notas)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<List<Nota_Entrada>>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Nota_Entrada>> obtenerNotaPorId(@PathVariable Long id) {
        try {
            Nota_Entrada nota = notaEntradaService.obtenerNotaPorId(id);
            return ResponseEntity.ok(
                    ApiResponse.<Nota_Entrada>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                            .data(nota)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Nota_Entrada>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Nota_Entrada>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }


    @GetMapping("/proveedor/{idProveedor}")
    public ResponseEntity<ApiResponse<List<Nota_Entrada>>> obtenerNotasPorProveedor(@PathVariable Long idProveedor) {
        try {
            List<Nota_Entrada> notas = notaEntradaService.obtenerNotasPorProveedor(idProveedor);
            return ResponseEntity.ok(
                    ApiResponse.<List<Nota_Entrada>>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                            .data(notas)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<List<Nota_Entrada>>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<List<Nota_Entrada>>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<ApiResponse<List<Nota_Entrada>>> obtenerNotasPorFecha(@PathVariable LocalDateTime fecha) {
        try {
            List<Nota_Entrada> notas = notaEntradaService.obtenerNotasPorFecha(fecha);
            return ResponseEntity.ok(
                    ApiResponse.<List<Nota_Entrada>>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                            .data(notas)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<List<Nota_Entrada>>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }
    @GetMapping("/Sucursal/{idSucursal}/almacen/{idAlmacen}")
    public ResponseEntity<ApiResponse<List<Nota_Entrada>>> obtenerNotasPorSucursalYAlmacen(
            @PathVariable Long idSucursal,
            @PathVariable Long idAlmacen) {
        try {
            List<Nota_Entrada> notas = notaEntradaService.obtenerNotasPorSucursalYAlmacen(idAlmacen, idSucursal);
            return ResponseEntity.ok(
                    ApiResponse.<List<Nota_Entrada>>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Notas de entrada de la sucursal y almacén")
                            .data(notas)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<List<Nota_Entrada>>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Nota_Entrada>> crearNotaEntrada(@RequestBody NotaEntradaCompletoDTO notaEntradaCompletaDto) {
        try {
            Nota_Entrada nuevaNota = notaEntradaService.guardarNota(notaEntradaCompletaDto);
            return new ResponseEntity<>(
                    ApiResponse.<Nota_Entrada>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.CREATED))
                            .data(nuevaNota)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Nota_Entrada>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Nota_Entrada>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

}
