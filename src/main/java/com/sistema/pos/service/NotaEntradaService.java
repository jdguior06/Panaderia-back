package com.sistema.pos.service;

import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.dto.DetalleNotaDTO;
import com.sistema.pos.dto.NotaEntradaCompletoDTO;
import com.sistema.pos.entity.*;
import com.sistema.pos.repository.NotaEntradaRepository;
import com.sistema.pos.repository.ProveedorRespository;
import com.sistema.pos.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotaEntradaService {

	@Autowired
	private NotaEntradaRepository notaEntradaRepository;

	@Autowired
	private ProductoAlmacenService productoAlmacenService;

	@Autowired
	private DetalleNotaService detalleNotaEService;

	@Autowired
	private ProveedorService proveedorService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private AlmacenService almacenService;

	@Autowired
	private SucursalRepository sucursalRepository;
	
	@Autowired
	private ProveedorRespository proveedorRespository;

	public List<Nota_Entrada> obtenerTodasLasNotas() {
		return notaEntradaRepository.findAll();
	}

	public Nota_Entrada obtenerNotaPorId(Long idNota) {
		return notaEntradaRepository.findById(idNota)
				.orElseThrow(() -> new IllegalArgumentException("Nota de entrada no encontrada con el ID: " + idNota));
	}

	public List<Nota_Entrada> obtenerNotasPorProveedor(Long idProveedor) {
		Proveedor proveedor = proveedorRespository.findById(idProveedor)
				.orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado con el ID: " + idProveedor));
		return notaEntradaRepository.findByProveedorId(proveedor.getId());
	}

	public List<Nota_Entrada> obtenerNotasPorFecha(LocalDateTime fecha) {
		return notaEntradaRepository.findByFecha(fecha);
	}

	public List<Nota_Entrada> obtenerNotasPorSucursalYAlmacen(Long idAlmacen, Long idSucursal) {
		Sucursal sucursal = sucursalRepository.findById(idSucursal)
				.orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada con el ID: " + idSucursal));

		return notaEntradaRepository.findByAlmacen_SucursalIdAndAlmacenId(sucursal.getId(), idAlmacen);
	}

	@LoggableAction
	public Nota_Entrada guardarNota(NotaEntradaCompletoDTO notaEntradaCompletaDto) {
		Proveedor proveedor = proveedorService.obtenerProveedorPorId(notaEntradaCompletaDto.getProveedor());
		Almacen almacen = almacenService.obtenerAlmacenId(notaEntradaCompletaDto.getAlmacen());

		Nota_Entrada notaEntrada = new Nota_Entrada();
		notaEntrada.setProveedor(proveedor);
		notaEntrada.setAlmacen(almacen);

		notaEntrada = notaEntradaRepository.save(notaEntrada);

		float total = 0f;
		for (DetalleNotaDTO detalle : notaEntradaCompletaDto.getDetalles()) {
			Producto producto = productoService.obtenerProducto(detalle.getProductoId());

			DetalleNotaE detalleNota = new DetalleNotaE();
			detalleNota.setCantidad(detalle.getCantidad());
			detalleNota.setCostoUnitario(producto.getPrecioCompra());
			detalleNota.setProducto(producto);

			detalleNotaEService.guardarDetalle(detalleNota, notaEntrada);

			float subTotal = detalle.getCantidad() * producto.getPrecioCompra();
			total += subTotal;

			ProductoAlmacen productoAlmacen = new ProductoAlmacen();
			productoAlmacen.setProducto(detalleNota.getProducto());
			productoAlmacen.setAlmacen(almacen);
			productoAlmacenService.save(productoAlmacen, detalle);
		}

		notaEntrada.setTotal(total);

		return notaEntradaRepository.save(notaEntrada);
	}

	@LoggableAction
	public void eliminarNota(Long idNota) {
		notaEntradaRepository.deleteById(idNota);
	}
}
