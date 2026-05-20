package com.medicore.api.services.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.medicore.api.dtos.factura.FacturaCajaDTO;
import com.medicore.api.entities.Factura;
import com.medicore.api.entities.costos.TarifaEps;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import com.medicore.api.repositories.FacturaRepository;
import com.medicore.api.repositories.costos.TarifaEpsRepository;
import com.medicore.api.services.ICajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CajaServiceImpl implements ICajaService {

    private final FacturaRepository facturaRepository;
    private final TarifaEpsRepository tarifaEpsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FacturaCajaDTO> buscarFacturasPendientesPorPaciente(String documento) {
        List<Factura> facturas = facturaRepository.findFacturasPendientesCaja(documento);

        return facturas.stream().map(factura -> {
            BigDecimal porcentajeCobertura = obtenerPorcentajeCobertura(factura.getEps().getCodigo(), factura.getServicio().getCodigo());
            
            BigDecimal coberturaEps = factura.getCostoTotal()
                    .multiply(porcentajeCobertura)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                    
            BigDecimal copagoAPagar = factura.getCostoTotal().subtract(coberturaEps);

            return new FacturaCajaDTO(
                    factura.getCodigo(),
                    factura.getCita().getUsuario().getNombre() + " " + factura.getCita().getUsuario().getApellido(),
                    factura.getDescripcion(),
                    factura.getFecha(),
                    factura.getEps().getNombre(),
                    factura.getCostoTotal(),
                    coberturaEps,
                    copagoAPagar,
                    factura.getPacientePago()
            );
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public byte[] procesarPagoYGenerarRecibo(String codigoFactura) {
        Factura factura = facturaRepository.findById(codigoFactura)
                .orElseThrow(() -> new RecursoNoEncontradoException("Factura no encontrada"));

        if (factura.getPacientePago() != null && factura.getPacientePago()) {
            throw new IllegalArgumentException("Esta factura ya fue pagada por el paciente.");
        }

        // Calcular copago a pagar
        BigDecimal porcentajeCobertura = obtenerPorcentajeCobertura(factura.getEps().getCodigo(), factura.getServicio().getCodigo());
        BigDecimal coberturaEps = factura.getCostoTotal().multiply(porcentajeCobertura).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal copago = factura.getCostoTotal().subtract(coberturaEps);

        // Actualizar estado en BD
        factura.setPacientePago(true);
        factura.setValorCopagoPagado(copago);
        facturaRepository.save(factura);

        // Generar el PDF (Tirilla de Caja)
        return generarTirillaCaja(factura, copago);
    }

    private BigDecimal obtenerPorcentajeCobertura(String codigoEps, String codigoServicio) {
        return tarifaEpsRepository.findByEpsCodigoAndEstadoTrue(codigoEps)
                .stream()
                .filter(t -> t.getServicio().getCodigo().equals(codigoServicio))
                .map(TarifaEps::getPorcentajeCobertura)
                .findFirst()
                .orElse(BigDecimal.ZERO); // Si no hay convenio, el paciente paga el 100%
    }

    private byte[] generarTirillaCaja(Factura f, BigDecimal pagado) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A6); // Tamaño recibo pequeño
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font fontNormal = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font fontNegrita = new Font(Font.HELVETICA, 10, Font.BOLD);

            Paragraph titulo = new Paragraph("RECIBO DE CAJA - MEDICORE", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("================================", fontNormal));
            
            document.add(new Paragraph("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), fontNormal));
            document.add(new Paragraph("Factura N°: " + f.getCodigo(), fontNormal));
            document.add(new Paragraph("Paciente: " + f.getCita().getUsuario().getNombre().toUpperCase(), fontNormal));
            document.add(new Paragraph("EPS: " + f.getEps().getNombre().toUpperCase(), fontNormal));
            document.add(new Paragraph("Servicio: " + f.getDescripcion(), fontNormal));
            
            document.add(new Paragraph("------------------------------------------------", fontNormal));
            document.add(new Paragraph("Subtotal: $" + f.getCostoTotal(), fontNormal));
            document.add(new Paragraph("Aporte EPS: -$" + f.getCostoTotal().subtract(pagado), fontNormal));
            document.add(new Paragraph("TOTAL PAGADO: $" + pagado, fontNegrita));
            document.add(new Paragraph("------------------------------------------------", fontNormal));
            
            Paragraph footer = new Paragraph("¡Gracias por confiar en nosotros!", new Font(Font.HELVETICA, 8, Font.ITALIC));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph("\n"));
            document.add(footer);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar tirilla de pago: " + e.getMessage());
        }
    }
}