package com.sistema.pos.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sistema.pos.entity.Permiso;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.service.PermisoService;
import com.sistema.pos.util.HttpStatusMessage;

@RestController
@RequestMapping("/permiso")
public class PermisoController {
	
	@Autowired
	private PermisoService permisoService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<Permiso>>> listarPermisos() {
		List<Permiso> rol = permisoService.listarPermiso();
		return new ResponseEntity<>(
				ApiResponse.<List<Permiso>>builder()
						.statusCode(HttpStatus.OK.value())
						.message(HttpStatusMessage.getMessage(HttpStatus.OK))
						.data(rol)
						.build(),
				HttpStatus.OK
		);
	}

}
