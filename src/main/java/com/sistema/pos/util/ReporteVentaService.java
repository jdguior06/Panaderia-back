package com.sistema.pos.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.sistema.pos.entity.Venta;

@Service
public class ReporteVentaService {

	public byte[] generarReporteVentasExcel(List<Venta> ventas, Double totalVentas) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Ventas");

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("ID");
		header.createCell(1).setCellValue("Cliente");
		header.createCell(2).setCellValue("Total");
		header.createCell(3).setCellValue("Fecha Venta");

		int rowIndex = 1;
		for (Venta venta : ventas) {
			Row row = sheet.createRow(rowIndex++);
			row.createCell(0).setCellValue(venta.getId());
			row.createCell(1).setCellValue(venta.getCliente().getNombre());
			row.createCell(2).setCellValue(venta.getTotal());
			row.createCell(3).setCellValue(venta.getFechaVenta().toString());
		}

		Row totalRow = sheet.createRow(rowIndex);
		totalRow.createCell(1).setCellValue("Total:");
		totalRow.createCell(2).setCellValue(totalVentas);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();

		return out.toByteArray();
	}

}
