package com.sistema.pos.service;

import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.dto.ProductoDTO;
import com.sistema.pos.entity.Almacen;
import com.sistema.pos.entity.Categoria;
import com.sistema.pos.entity.Producto;
import com.sistema.pos.entity.ProductoAlmacen;
import com.sistema.pos.dto.ProductoVentaDTO;
import com.sistema.pos.repository.AlmacenRepository;
import com.sistema.pos.repository.ProductoAlmacenRepository;
import com.sistema.pos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private AlmacenRepository almacenRepository;
	
	@Autowired
	private ProductoAlmacenRepository productoAlmacenRepository;

	public List<Producto> findAll() {
		return productoRepository.findAll();
	}

	public List<Producto> listadeProductosActivos(){
		return productoRepository.findByActivoTrue();
	}

	public Producto obtenerProducto(Long id) {
		Optional<Producto> producto = productoRepository.findById(id);
		if (!producto.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el producto con el id" + id);
		}
		return producto.get();
	}

	@LoggableAction
	public Producto save(ProductoDTO productoDTO) {
		Categoria categoria = categoriaService.findById(productoDTO.getId_categoria());
		Producto producto = new Producto();
		producto.setNombre(productoDTO.getNombre());
		producto.setCodigo(productoDTO.getCodigo());
		producto.setDescripcion(productoDTO.getDescripcion());
		producto.setPrecioCompra(productoDTO.getPrecioCompra());
		producto.setPrecioVenta(productoDTO.getPrecioVenta());
		producto.setFoto(productoDTO.getFoto());
		producto.setCategoria(categoria);
		producto.setActivo(true);

		return productoRepository.save(producto);
	}

	@LoggableAction
	public Producto actualizarProducto(Long id, ProductoDTO productoDTO) {
		Producto producto = obtenerProducto(id);
		Categoria categoria = categoriaService.findById(productoDTO.getId_categoria());
		producto.setNombre(productoDTO.getNombre());
		producto.setCodigo(productoDTO.getCodigo());
		producto.setDescripcion(productoDTO.getDescripcion());
		producto.setPrecioCompra(productoDTO.getPrecioCompra());
		producto.setPrecioVenta(productoDTO.getPrecioVenta());
		producto.setFoto(productoDTO.getFoto());
		producto.setCategoria(categoria);

		return productoRepository.save(producto);
	}

	@LoggableAction
	public Producto desactivarProducto(Long id) {
		Producto producto = obtenerProducto(id);
		producto.setActivo(false);
		
		List<ProductoAlmacen> productosAlmacen = productoAlmacenRepository.findByProductoId(id);
	    for (ProductoAlmacen productoAlmacen : productosAlmacen) {
	        productoAlmacen.setActivo(false);
	        productoAlmacenRepository.save(productoAlmacen);
	    }
	    
		return productoRepository.save(producto);
	}
	
	@LoggableAction
	public Producto activarProducto(Long id) {
		Producto producto = obtenerProducto(id);
		producto.setActivo(true);
		
		List<ProductoAlmacen> productosAlmacen = productoAlmacenRepository.findByProductoId(id);
	    for (ProductoAlmacen productoAlmacen : productosAlmacen) {
	        productoAlmacen.setActivo(true);
	        productoAlmacenRepository.save(productoAlmacen);
	    }
		
		return productoRepository.save(producto);
	}

	public List<ProductoVentaDTO> obtenerProductosPorSucursal(Long idSucursal) {
		
		List<Almacen> almacenes = almacenRepository.findBySucursal_IdAndActivoTrue(idSucursal);

		if (almacenes.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"No se encontraron almacenes para la sucursal dada");
		}

		List<ProductoVentaDTO> productos = almacenes.stream().flatMap(almacen -> almacen.getProductosAlmacen().stream())
				.map(pa -> new ProductoVentaDTO(pa.getProducto().getNombre(), pa.getProducto().getDescripcion(),
						pa.getProducto().getId(), pa.getStock(), pa.getProducto().getPrecioVenta()))
				.collect(Collectors.toList());

		if (productos.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron productos en la sucursal dada");
		}
		return productos;
	}
	
}
