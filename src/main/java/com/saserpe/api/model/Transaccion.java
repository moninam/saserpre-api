package com.saserpe.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class Transaccion {
    @Column(name="id_trans")
    @JsonProperty("id_trans")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id_trans;

    @Column(name="RFC_usuario")
    @JsonProperty("RFC_usuario")
    private String RFC_usuario;

    @Column(name="RFC_empresa")
    @JsonProperty("RFC_empresa")
    private String RFC_empresa;

    @Column(name="precio_accion_tr")
    @JsonProperty("precio_accion_tr")
    private float precio_accion_tr;

    @Column(name="fecha_tr")
    @JsonProperty("fecha_tr")
    private String fecha_tr;

    @Column(name="operacion_accion_tr")
    @JsonProperty("operacion_accion_tr")
    private Integer operacion_accion_tr;

    @Column(name="tipo_accion_tr")
    @JsonProperty("tipo_accion_tr")
    private String tipo_accion_tr;

    public Transaccion(Integer id_trans, String RFC_usuario, String RFC_empresa, float precio_accion_tr, String fecha_tr, Integer operacion_accion_tr, String tipo_accion_tr) {
        this.id_trans = id_trans;
        this.RFC_usuario = RFC_usuario;
        this.RFC_empresa = RFC_empresa;
        this.precio_accion_tr = precio_accion_tr;
        this.fecha_tr = fecha_tr;
        this.operacion_accion_tr = operacion_accion_tr;
        this.tipo_accion_tr = tipo_accion_tr;
    }

    public Transaccion(){

    }

    public Integer getId_trans() {
        return id_trans;
    }

    public void setId_trans(Integer id_trans) {
        this.id_trans = id_trans;
    }

    public String getRFC_usuario() {
        return RFC_usuario;
    }

    public void setRFC_usuario(String RFC_usuario) {
        this.RFC_usuario = RFC_usuario;
    }

    public String getRFC_empresa() {
        return RFC_empresa;
    }

    public void setRFC_empresa(String RFC_empresa) {
        this.RFC_empresa = RFC_empresa;
    }

    public float getPrecio_accion_tr() {
        return precio_accion_tr;
    }

    public void setPrecio_accion_tr(float precio_accion_tr) {
        this.precio_accion_tr = precio_accion_tr;
    }

    public String getFecha_tr() {
        return fecha_tr;
    }

    public void setFecha_tr(String fecha_tr) {
        this.fecha_tr = fecha_tr;
    }

    public Integer getOperacion_accion_tr() {
        return operacion_accion_tr;
    }

    public void setOperacion_accion_tr(Integer operacion_accion_tr) {
        this.operacion_accion_tr = operacion_accion_tr;
    }

    public String getTipo_accion_tr() {
        return tipo_accion_tr;
    }

    public void setTipo_accion_tr(String tipo_accion_tr) {
        this.tipo_accion_tr = tipo_accion_tr;
    }
}
