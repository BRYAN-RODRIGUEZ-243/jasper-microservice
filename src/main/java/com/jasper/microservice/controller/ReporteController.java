package com.jasper.microservice.controller;

import com.jasper.microservice.dto.ErrorResponse;
import com.jasper.microservice.dto.ReporteRequest;
import com.jasper.microservice.service.JasperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private static final Logger log = LoggerFactory.getLogger(ReporteController.class);

    private final JasperService jasperService;

    public ReporteController(JasperService jasperService) {
        this.jasperService = jasperService;
    }

    /**
     * Endpoint para generar contrato
     * POST /api/reportes/contrato
     */
    @PostMapping("/contrato")
    public ResponseEntity<?> generarContrato(@RequestBody ReporteRequest request) {
        try {
            log.info("Solicitud de generación de contrato recibida");
            
            byte[] pdfBytes = jasperService.generarContrato(request.getParametros());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "contrato.pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error generando contrato: {}", e.getMessage(), e);
            ErrorResponse errorResponse = new ErrorResponse(
                "ERROR_GENERACION_CONTRATO",
                e.getMessage()
            );
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
        }
    }

    /**
     * Endpoint para generar reporte personalizado
     * POST /api/reportes/personalizado
     */
    @PostMapping("/personalizado")
    public ResponseEntity<?> generarReportePersonalizado(@RequestBody ReporteRequest request) {
        try {
            log.info("Solicitud de reporte personalizado: {}", request.getTemplateNombre());
            
            if (request.getTemplateNombre() == null || request.getTemplateNombre().isEmpty()) {
                return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(
                        "TEMPLATE_REQUERIDO",
                        "El nombre del template es requerido"
                    ));
            }
            
            byte[] pdfBytes = jasperService.generarReportePersonalizado(
                request.getTemplateNombre(),
                request.getParametros()
            );
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", request.getTemplateNombre() + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error generando reporte personalizado: {}", e.getMessage(), e);
            ErrorResponse errorResponse = new ErrorResponse(
                "ERROR_GENERACION_REPORTE",
                e.getMessage()
            );
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service is running");
    }
}
