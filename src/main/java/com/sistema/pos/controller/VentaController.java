package com.sistema.pos.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.pos.dto.VentaDTO;
import com.sistema.pos.entity.Venta;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.service.VentaService;
import com.sistema.pos.util.HttpStatusMessage;
import com.sistema.pos.util.ReporteVentaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/venta")
public class VentaController {
	
	@Autowired
    private VentaService ventaService;
	
	@Autowired
	private ReporteVentaService reporteVentaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Venta>>> listarVentas() {
        List<Venta> ventas = ventaService.listarVentas();
        return new ResponseEntity<>(
                ApiResponse.<List<Venta>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                        .data(ventas)
                        .build(),
                HttpStatus.OK
        );
    }
    
    @GetMapping("/reportes/por-fechas")
    public ResponseEntity<ApiResponse<Map<String, Object>>> reportePorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Venta> ventas = ventaService.obtenerVentasPorFechas(startDate, endDate);
        Double total = ventaService.obtenerTotalVentasPorFechas(startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("ventas", ventas);
        response.put("total", total);

        return ResponseEntity.ok(
                ApiResponse.<Map<String, Object>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Venta>> obtenerVenta(@PathVariable Long id) {
        try {
            Venta venta = ventaService.obtenerVenta(id);
            return new ResponseEntity<>(
                    ApiResponse.<Venta>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                            .data(venta)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Venta>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Venta>> realizarVenta(@Valid @RequestBody VentaDTO ventaDTO, BindingResult bindingResult) {
        // Validación de errores de campos
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(
                    ApiResponse.<Venta>builder()
                            .errors(errors)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Errores de validación en los campos")
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validaciones de negocio
        if (ventaDTO.getDetalleVentaDTOS() == null || ventaDTO.getDetalleVentaDTOS().isEmpty()) {
            return new ResponseEntity<>(
                    ApiResponse.<Venta>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("El carrito está vacío")
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (ventaDTO.getMetodosPago() == null || ventaDTO.getMetodosPago().isEmpty()) {
            return new ResponseEntity<>(
                    ApiResponse.<Venta>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("No se han especificado métodos de pago")
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            Venta venta = ventaService.guardarVenta(ventaDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Venta>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Venta realizada con éxito")
                            .data(venta)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Venta>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.<Venta>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error interno del servidor: " + e.getMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    
    @GetMapping("/reportes/excel")
    public ResponseEntity<byte[]> descargarReporteVentasExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) throws IOException {
        List<Venta> ventas = ventaService.obtenerVentasPorFechas(startDate, endDate);
        Double total = ventaService.obtenerTotalVentasPorFechas(startDate, endDate);

        byte[] excelFile = reporteVentaService.generarReporteVentasExcel(ventas, total);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_ventas.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }


}
