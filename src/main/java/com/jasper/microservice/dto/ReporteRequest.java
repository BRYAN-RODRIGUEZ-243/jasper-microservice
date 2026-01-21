package com.jasper.microservice.dto;

import java.util.Map;

public class ReporteRequest {
    
    private Map<String, Object> parametros;
    
    private String templateNombre; // Opcional, para reportes personalizados

    public ReporteRequest() {
    }

    public ReporteRequest(Map<String, Object> parametros, String templateNombre) {
        this.parametros = parametros;
        this.templateNombre = templateNombre;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public String getTemplateNombre() {
        return templateNombre;
    }

    public void setTemplateNombre(String templateNombre) {
        this.templateNombre = templateNombre;
    }
}
