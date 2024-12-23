package com.sistema.pos.entity;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Entity
public class Usuario implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;
    
    private String apellido;
    
    @Email(message = "El email debe ser válido")
    private String email;
    
    private String password; 
    
    private boolean activo;
    private boolean cuentaNoExpirada;
    private boolean cuentaNoBloqueada;
    private boolean credencialesNoExpiradas;
    
    private String themeColor;

    private boolean esAdministradorPrincipal;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
    		name = "user_rol",
    		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    		inverseJoinColumns =  @JoinColumn(name = "rol_id", referencedColumnName = "id")
    		)
    private Set<Rol> rol;

	public Usuario(String nombre, String apellido, String email, String password, Set<Rol> rol) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.password = password;
		this.rol = rol;
		this.activo = true;
        this.cuentaNoExpirada = true;
        this.cuentaNoBloqueada = true; 
        this.credencialesNoExpiradas = true;  
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		Set<GrantedAuthority> authorities = rol.stream()
//	            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNombre()))
//	            .collect(Collectors.toSet());
//		
//		 rol.forEach(role -> 
//	        role.getPermiso().forEach(permiso -> 
//	            authorities.add(new SimpleGrantedAuthority(permiso.getNombre()))
//	        )
//	    );
//		 
//		return authorities;
	    Set<GrantedAuthority> authorities = rol.stream()
	            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNombre()))
	            .collect(Collectors.toSet());

	    rol.forEach(role -> {
	        System.out.println("Rol: " + role.getNombre());
	        role.getPermiso().forEach(permiso -> {
	            System.out.println("Permiso: " + permiso.getNombre());
	            authorities.add(new SimpleGrantedAuthority(permiso.getNombre()));
	        });
	    });

	    return authorities;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return cuentaNoExpirada;
	}

	@Override
	public boolean isAccountNonLocked() {
		return cuentaNoBloqueada;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credencialesNoExpiradas;
	}

	@Override
	public boolean isEnabled() {
		return activo;
	}

	@Override
	public String getPassword() {
		return password;
	}

}
