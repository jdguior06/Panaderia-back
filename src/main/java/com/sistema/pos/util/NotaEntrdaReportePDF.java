package com.sistema.pos.util;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.sistema.pos.entity.Nota_Entrada;

import java.awt.*;
import java.util.List;

public class NotaEntrdaReportePDF {
    private List<Nota_Entrada> listaNotas;

    public NotaEntrdaReportePDF(List<Nota_Entrada> listaNotas) {
        super();
        this.listaNotas = listaNotas;
    }

    private void escribircabeceradelatabla(PdfPTable tabla){
        PdfPCell celda= new PdfPCell();
        celda.setBackgroundColor(Color.red);
        celda.setPadding(5);
        Font fuente= FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.WHITE);

        celda.setPhrase(new Phrase("ID",fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("ALMACEN",fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("FECHA",fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("DESCUENTO",fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("PROVEEDOR",fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("TOTAL",fuente));
        tabla.addCell(celda);
    }

    private void escribirDatos(PdfPTable table){
        for (Nota_Entrada notaEntrada : this.listaNotas) {
            table.addCell(String.valueOf((notaEntrada).getId()));
            table.addCell(String.valueOf(notaEntrada.getAlmacen()));
            table.addCell(String.valueOf(notaEntrada.getFecha()));
//            table.addCell(String.valueOf(notaEntrada.getDescuento()));
            table.addCell(String.valueOf(notaEntrada.getProveedor()));
            table.addCell(String.valueOf(notaEntrada.getTotal()));

        }
    }
}
