package com.sistema.pos.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sistema.pos.entity.Permiso;
import com.sistema.pos.entity.Rol;
import com.sistema.pos.repository.PermisoRepository;
import com.sistema.pos.repository.RolRepository;
import com.sistema.pos.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Configuration
public class DataInitializer {

	@Bean
	public CommandLineRunner initData(UsuarioRepository usuarioRepository, RolRepository rolRepository, PermisoRepository permisoRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initRolesAndPermisos(usuarioRepository, rolRepository, permisoRepository, passwordEncoder);
		};
	}

	private Permiso obtenerOPersistirPermiso(PermisoRepository permisoRepository, String nombrePermiso) {
		return permisoRepository.findByNombre(nombrePermiso)
				.orElseGet(() -> permisoRepository.save(new Permiso(nombrePermiso)));
	}

	@Transactional
	public void initRolesAndPermisos(UsuarioRepository usuarioRepository, RolRepository rolRepository, PermisoRepository permisoRepository, PasswordEncoder passwordEncoder) {
	    Set<Permiso> permisos = new HashSet<>();
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_GESTIONAR_PERMISOS"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_GESTIONAR_ROLES"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_GESTIONAR_PERSONAL"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_VER_ALMACENES"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_ADMINISTRAR_ALMACENES"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_VER_SUCURSALES"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_ADMINISTRAR_SUCURSALES"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_VER_CLIENTES"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_CREAR_CLIENTES"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_EDITAR_CLIENTES"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_ELIMINAR_CLIENTES"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_GESTIONAR_PROVEEDORES"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_VER_CAJAS"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_ADMINISTRAR_CAJAS"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_VER_PRODUCTOS"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_ADMINISTRAR_PRODUCTOS"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_GENERAR_VENTAS"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_VER_REPORTE_VENTAS"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_VER_CATEGORIAS"));
	    permisos.add(obtenerOPersistirPermiso(permisoRepository, "PERMISO_ADMINISTRAR_CATEGORIAS"));
	    
	    Rol adminRole = rolRepository.findByNombre("ADMIN").orElseGet(() -> new Rol("ADMIN"));
	    adminRole.setPermiso(permisos);
	    rolRepository.save(adminRole); 
	       
	}

}
