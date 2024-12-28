package com.sistema.pos.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.dto.DetalleVentaDTO;
import com.sistema.pos.dto.MetodoPagoDTO;
import com.sistema.pos.dto.VentaDTO;
import com.sistema.pos.entity.Almacen;
import com.sistema.pos.entity.CajaSesion;
import com.sistema.pos.entity.Cliente;
import com.sistema.pos.entity.DetalleVenta;
import com.sistema.pos.entity.MetodoPago;
import com.sistema.pos.entity.Producto;
import com.sistema.pos.entity.ProductoAlmacen;
import com.sistema.pos.entity.TipoPago;
import com.sistema.pos.entity.Venta;
import com.sistema.pos.repository.AlmacenRepository;
import com.sistema.pos.repository.ProductoAlmacenRepository;
import com.sistema.pos.repository.VentaRepository;
import com.sistema.pos.util.EmailService;
import com.sistema.pos.util.VentaPDFService;

import jakarta.transaction.Transactional;

@Service
public class VentaService {

	@Autowired
	private VentaRepository ventaRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private ProductoAlmacenRepository productoAlmacenRepository;
	
	@Autowired
	private AlmacenRepository almacenRepository;
	
	@Autowired CajaSesionService cajaSesionService;
	
	@Autowired
	private ProductoAlmacenService productoAlmacenService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private VentaPDFService ventaPDFService;

	@Transactional
	public List<Venta> listarVentas() {
		return ventaRepository.findAll();
	}
	
	public Double obtenerTotalVentasPorFechas(LocalDateTime startDate, LocalDateTime endDate) {
	    return ventaRepository.sumTotalVentasByFechaVentaBetween(startDate, endDate);
	}
	
	public List<Venta> obtenerVentasPorFechas(LocalDateTime startDate, LocalDateTime endDate) {
        return ventaRepository.findVentasByFechaVentaBetween(startDate, endDate);
    }

	public Venta obtenerVenta(Long id) {
		Optional<Venta> venta = ventaRepository.findById(id);
		if (!venta.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la venta" + id);
		}
		return venta.get();
	}

	@Transactional
	@LoggableAction
	public Venta guardarVenta(VentaDTO ventaDTO) {
		
		CajaSesion cajaSesion = cajaSesionService.obtenerCajaSesion(ventaDTO.getId_caja_sesion());

		Cliente cliente = clienteService.obtenerClientePorId(ventaDTO.getId_cliente());
		
		Venta venta = new Venta();
		venta.setCliente(cliente);
		venta.setCajaSesion(cajaSesion);

		List<DetalleVenta> detalles = new ArrayList<>();
		Double total = 0.00;

		for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalleVentaDTOS()) {

			Producto producto = productoService.obtenerProducto(detalleDTO.getId_producto());
			
			List<Almacen> almacenes = almacenRepository.findBySucursalId(cajaSesion.getCaja().getSucursal().getId());
	        List<ProductoAlmacen> productosAlmacen = productoAlmacenRepository.findByProductoAndAlmacenIn(producto, almacenes);
	        
	        int stockTotal = productosAlmacen.stream().mapToInt(ProductoAlmacen::getStock).sum();
	        if (stockTotal < detalleDTO.getCantidad()) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	                    "No hay suficiente stock total del producto: " + producto.getNombre());
	        }

	        int cantidadRestante = detalleDTO.getCantidad();
	        for (ProductoAlmacen productoAlmacen : productosAlmacen) {
	            if (cantidadRestante <= 0) break;

	            int disponible = productoAlmacen.getStock();
	            int aDescontar = Math.min(disponible, cantidadRestante);
	            productoAlmacen.setStock(disponible - aDescontar);
	            cantidadRestante -= aDescontar;

	            productoAlmacenService.actualizarStock(productoAlmacen);
	        }

			DetalleVenta detalleVenta = new DetalleVenta();
			detalleVenta.setProducto(producto);
			detalleVenta.setCantidad(detalleDTO.getCantidad());
			detalleVenta.setPrecio(producto.getPrecioVenta());
			detalleVenta.setMonto(producto.getPrecioVenta() * detalleDTO.getCantidad());
			detalleVenta.setVenta(venta);
			detalles.add(detalleVenta);
			total += detalleVenta.getMonto();
		}
		venta.setDetalleVentaList(detalles);
		venta.setTotal(total);
		
		List<MetodoPago> metodosPago = new ArrayList<>();
	    Double sumaPagos = 0.00;
	    Double montoEfectivo = 0.00;
	    for (MetodoPagoDTO metodoPagoDTO : ventaDTO.getMetodosPago()) {
	        MetodoPago metodoPago = new MetodoPago();
	        metodoPago.setVenta(venta);
	        metodoPago.setTipoPago(metodoPagoDTO.getTipoPago());
	        metodoPago.setMonto(metodoPagoDTO.getMonto());
	        metodoPago.setDetalles(metodoPagoDTO.getDetalles());

	        if (metodoPagoDTO.getTipoPago() == TipoPago.EFECTIVO) {
	        	montoEfectivo += metodoPagoDTO.getMonto(); 
	            metodoPago.setCambio(0.0);
	        } else {
	            metodoPago.setCambio(0.0);
	        }

	        sumaPagos += metodoPagoDTO.getMonto();
	        metodosPago.add(metodoPago);
	    }
	    
	    if (montoEfectivo > 0) {
	        Double cambio = montoEfectivo - total;
	        if (cambio < 0) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	                    "El monto en efectivo no cubre el total de la venta.");
	        }

	        for (MetodoPago metodoPago : metodosPago) {
	            if (metodoPago.getTipoPago() == TipoPago.EFECTIVO) {
	                metodoPago.setCambio(cambio); 
	                break;
	            }
	        }
	        sumaPagos -= cambio; 
	    }

	    if (!sumaPagos.equals(total)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	                "La suma de los métodos de pago no coincide con el total de la venta.");
	    }
	    venta.setMetodosPago(metodosPago);
		
		Venta ventaGuardada = ventaRepository.save(venta);
		
		if (venta.getCliente() != null && venta.getCliente().getEmail() != null) {
	        try {
	            byte[] pdf = ventaPDFService.generarFacturaPDF(venta);
	            emailService.enviarFacturaPorCorreo(venta.getCliente().getEmail(), pdf, "Factura_" + venta.getId() + ".pdf");
	        } catch (Exception e) {
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo", e);
	        }
	    }
		
		return ventaGuardada;
	}

}
