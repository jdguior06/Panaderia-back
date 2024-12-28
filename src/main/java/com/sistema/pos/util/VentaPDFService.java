package com.sistema.pos.util;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.sistema.pos.entity.DetalleVenta;
import com.sistema.pos.entity.MetodoPago;
import com.sistema.pos.entity.Venta;

@Service
public class VentaPDFService {
	
	public byte[] generarFacturaPDF(Venta venta) throws DocumentException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        Font headerFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font clienteFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
        Font tableHeaderFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font tableBodyFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
        Font totalFont = new Font(Font.HELVETICA, 14, Font.BOLD);

        document.add(new Paragraph("Factura de Venta", headerFont));
        document.add(new Paragraph(" ")); 

        String fechaVentaFormatted = venta.getFechaVenta()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")); // Formato legible
        document.add(new Paragraph("Cliente: " + (venta.getCliente() != null ? venta.getCliente().getNombre() : "Anónimo"), clienteFont));
        document.add(new Paragraph("Fecha: " + fechaVentaFormatted, clienteFont));
        document.add(new Paragraph(" ")); 

        Table table = new Table(4); 
        table.setWidth(100);
        table.setPadding(5);

        table.addCell(new Cell(new Phrase("Producto", tableHeaderFont)));
        table.addCell(new Cell(new Phrase("Cantidad", tableHeaderFont)));
        table.addCell(new Cell(new Phrase("Precio Unitario", tableHeaderFont)));
        table.addCell(new Cell(new Phrase("Total", tableHeaderFont)));

        for (DetalleVenta detalle : venta.getDetalleVentaList()) {
            table.addCell(new Cell(new Phrase(detalle.getProducto().getNombre(), tableBodyFont)));
            table.addCell(new Cell(new Phrase(String.valueOf(detalle.getCantidad()), tableBodyFont)));
            table.addCell(new Cell(new Phrase("BS " + detalle.getPrecio(), tableBodyFont)));
            table.addCell(new Cell(new Phrase("BS " + detalle.getMonto(), tableBodyFont)));
        }

        document.add(table);
        document.add(new Paragraph(" ")); 

        document.add(new Paragraph("Métodos de Pago:", totalFont));
        Table paymentTable = new Table(3); 
        paymentTable.setWidth(100);
        paymentTable.setPadding(5);

        paymentTable.addCell(new Cell(new Phrase("Método", tableHeaderFont)));
        paymentTable.addCell(new Cell(new Phrase("Monto", tableHeaderFont)));
        paymentTable.addCell(new Cell(new Phrase("Cambio", tableHeaderFont)));

        for (MetodoPago metodoPago : venta.getMetodosPago()) {
            paymentTable.addCell(new Cell(new Phrase(metodoPago.getTipoPago().name(), tableBodyFont)));
            paymentTable.addCell(new Cell(new Phrase("Bs " + metodoPago.getMonto(), tableBodyFont)));
            paymentTable.addCell(new Cell(new Phrase("BS " + metodoPago.getCambio(), tableBodyFont)));
        }

        document.add(paymentTable);
        document.add(new Paragraph(" ")); 

        document.add(new Paragraph("Total de la Venta: BS " + venta.getTotal(), totalFont));

        document.close();

        return out.toByteArray();
    }
}
