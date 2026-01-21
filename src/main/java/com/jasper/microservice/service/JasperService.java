package com.jasper.microservice.service;

import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

@Service
public class JasperService {

    private static final Logger log = LoggerFactory.getLogger(JasperService.class);

    @Value("${jasper.template.url}")
    private String templateUrl;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    /**
     * Genera un contrato en formato PDF usando JasperReports
     * @param params Parámetros para el reporte
     * @return byte[] con el PDF generado
     * @throws Exception si ocurre algún error en la generación
     */
    public byte[] generarContrato(Map<String, Object> params) throws Exception {
        log.info("Generando contrato con parámetros: {}", params.keySet());
        
        Connection conn = null;
        InputStream jrxml = null;
        
        try {
            // Descargar el template JRXML
            jrxml = new URL(templateUrl).openStream();
            
            // Compilar el reporte
            JasperReport report = JasperCompileManager.compileReport(jrxml);
            
            // Conectar a la base de datos
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            
            // Llenar el reporte con datos
            JasperPrint print = JasperFillManager.fillReport(report, params, conn);
            
            // Exportar a PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(print);
            
            log.info("Contrato generado exitosamente. Tamaño: {} bytes", pdfBytes.length);
            return pdfBytes;
            
        } catch (Exception e) {
            log.error("Error generando contrato: {}", e.getMessage(), e);
            throw new Exception("Error al generar el contrato: " + e.getMessage(), e);
        } finally {
            // Cerrar recursos
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.warn("Error cerrando conexión: {}", e.getMessage());
                }
            }
            if (jrxml != null) {
                try {
                    jrxml.close();
                } catch (Exception e) {
                    log.warn("Error cerrando stream: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * Genera un reporte personalizado
     * @param templateName Nombre del template (sin extensión)
     * @param params Parámetros para el reporte
     * @return byte[] con el PDF generado
     * @throws Exception si ocurre algún error
     */
    public byte[] generarReportePersonalizado(String templateName, Map<String, Object> params) throws Exception {
        log.info("Generando reporte personalizado: {}", templateName);
        
        Connection conn = null;
        InputStream jrxml = null;
        
        try {
            // Construir URL del template
            String customTemplateUrl = templateUrl.replace("ContratoClientes.jrxml", templateName + ".jrxml");
            jrxml = new URL(customTemplateUrl).openStream();
            
            JasperReport report = JasperCompileManager.compileReport(jrxml);
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            JasperPrint print = JasperFillManager.fillReport(report, params, conn);
            
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(print);
            log.info("Reporte {} generado exitosamente", templateName);
            return pdfBytes;
            
        } catch (Exception e) {
            log.error("Error generando reporte {}: {}", templateName, e.getMessage(), e);
            throw new Exception("Error al generar el reporte: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.warn("Error cerrando conexión: {}", e.getMessage());
                }
            }
            if (jrxml != null) {
                try {
                    jrxml.close();
                } catch (Exception e) {
                    log.warn("Error cerrando stream: {}", e.getMessage());
                }
            }
        }
    }
}
