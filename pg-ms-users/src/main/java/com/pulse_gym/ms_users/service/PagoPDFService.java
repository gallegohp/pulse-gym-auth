package com.pulse_gym.ms_users.service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.pulse_gym.lb_common.dto.PagoResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PagoPDFService {

    /** Formateador de fecha para el comprobante */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Genera un comprobante de pago en formato PDF
     * 
     * @param pago Datos del pago a incluir en el comprobante
     * @return Array de bytes del PDF generado
     * @throws RuntimeException Si ocurre un error al generar el PDF
     */
    public byte[] generarComprobantePDF(PagoResponseDTO pago) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            Paragraph title = new Paragraph("PULSE GYM")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.BLUE);
            document.add(title);

            Paragraph subtitle = new Paragraph("COMPROBANTE DE PAGO")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.DARK_GRAY);
            document.add(subtitle);

            document.add(new Paragraph("________________________________________")
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph(" "));

            Table table = new Table(UnitValue.createPercentArray(new float[] { 40, 60 }))
                    .setWidth(UnitValue.createPercentValue(100));

            addRow(table, "ID Pago:", pago.getIdPago().toString());
            addRow(table, "Socio:", pago.getNombreSocio());
            addRow(table, "Email:", pago.getEmailSocio());
            addRow(table, "Membresía:", pago.getNombreMembresia());
            addRow(table, "Monto:", "$ " + String.format("%,.0f", pago.getMonto()));
            addRow(table, "Método de Pago:", pago.getMetodoPago());
            addRow(table, "Fecha:", pago.getFechaPago().format(DATE_FORMATTER));
            addRow(table, "Comprobante:", pago.getNumeroComprobante());

            if (pago.getNombreAdminRegistro() != null) {
                addRow(table, "Registrado por:", pago.getNombreAdminRegistro());
            }

            if (pago.getAnulado() != null && pago.getAnulado()) {
                addRow(table, "Estado:", "ANULADO");
                if (pago.getMotivoAnulacion() != null) {
                    addRow(table, "Motivo Anulación:", pago.getMotivoAnulacion());
                }
            } else {
                addRow(table, "Estado:", "APROBADO");
            }

            document.add(table);
            document.add(new Paragraph(" "));

            Paragraph footer = new Paragraph("Este comprobante es válido como constancia de pago.")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY);
            document.add(footer);

            Paragraph footer2 = new Paragraph("Pulse Gym - Tu bienestar, nuestra pasión")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY);
            document.add(footer2);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("Error al generar el PDF del comprobante: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar el comprobante PDF: " + e.getMessage());
        }
    }

    /**
     * Agrega una fila a la tabla del PDF
     * 
     * @param table Tabla donde se agregará la fila
     * @param label Etiqueta de la fila
     * @param value Valor de la fila
     */
    private void addRow(Table table, String label, String value) {
        Cell labelCell = new Cell()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setPadding(8)
                .setFontSize(12)
                .setBold();
        labelCell.add(new Paragraph(label));

        Cell valueCell = new Cell()
                .setPadding(8)
                .setFontSize(12);
        valueCell.add(new Paragraph(value != null ? value : "-"));

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}