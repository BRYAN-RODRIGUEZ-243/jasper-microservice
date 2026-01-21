package com.jasper.microservice.dto;

public class ErrorResponse {
    
    private String error;
    private String mensaje;
    private long timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(String error, String mensaje, long timestamp) {
        this.error = error;
        this.mensaje = mensaje;
        this.timestamp = timestamp;
    }
    
    public ErrorResponse(String error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
        this.timestamp = System.currentTimeMillis();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
