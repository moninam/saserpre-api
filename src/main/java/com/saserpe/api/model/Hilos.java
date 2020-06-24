package com.saserpe.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class Hilos {

    @Column(name="iden")
    @JsonProperty("iden")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer iden;

    @Column(name="id_hilo")
    @JsonProperty("id_hilo")
    private String id_hilo;

    @Column(name="activo")
    @JsonProperty("activo")
    private boolean activo;

    @Column(name="actualizacion")
    @JsonProperty("actualizacion")
    private String actualizacion;

    @Column(name="tipo_accion")
    @JsonProperty("tipo_accion")
    private String tipo_accion;

    public Hilos(){

    }

    public Hilos(String id_hilo, String actualizacion, String tipoAccion) {
        this.id_hilo = id_hilo;
        this.actualizacion = actualizacion;
        this.tipo_accion = tipoAccion;
    }

    public String getTipo_accion() {
        return tipo_accion;
    }

    public void setTipo_accion(String tipo_accion) {
        this.tipo_accion = tipo_accion;
    }

    public Integer getIden() {
        return iden;
    }

    public void setIden(Integer iden) {
        this.iden = iden;
    }

    public String getId_hilo() {
        return id_hilo;
    }

    public void setId_hilo(String id_hilo) {
        this.id_hilo = id_hilo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getActualizacion() {
        return actualizacion;
    }

    public void setActualizacion(String actualizacion) {
        this.actualizacion = actualizacion;
    }
}
