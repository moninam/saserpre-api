package com.saserpe.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class Empresa {

    @Column(name="RFC_empresa")
    @JsonProperty("RFC_empresa")
    @Id
    private String RFC_empresa;

    @Column(name="acciones_empr_disp")
    @JsonProperty("acciones_empr_disp")
    private Integer acciones_empr_disp;

    @Column(name="acciones_empr_total")
    @JsonProperty("acciones_empr_total")
    private Integer acciones_empr_total;

    @Column(name="precio_accion_empr")
    @JsonProperty("precio_accion_empr")
    private float precio_accion_empr;

    public Empresa(String RFC_empresa, Integer acciones_empr_disp, Integer acciones_empr_total, float precio_accion_empr) {
        this.RFC_empresa = RFC_empresa;
        this.acciones_empr_disp = acciones_empr_disp;
        this.acciones_empr_total = acciones_empr_total;
        this.precio_accion_empr = precio_accion_empr;
    }
    public Empresa(){

    }

    public String getRFC_empresa() {
        return RFC_empresa;
    }

    public void setRFC_empresa(String RFC_empresa) {
        this.RFC_empresa = RFC_empresa;
    }

    public Integer getAcciones_empr_disp() {
        return acciones_empr_disp;
    }

    public void setAcciones_empr_disp(Integer acciones_empr_disp) {
        this.acciones_empr_disp = acciones_empr_disp;
    }

    public Integer getAcciones_empr_total() {
        return acciones_empr_total;
    }

    public void setAcciones_empr_total(Integer acciones_empr_total) {
        this.acciones_empr_total = acciones_empr_total;
    }

    public float getPrecio_accion_empr() {
        return precio_accion_empr;
    }

    public void setPrecio_accion_empr(float precio_accion_empr) {
        this.precio_accion_empr = precio_accion_empr;
    }
}
