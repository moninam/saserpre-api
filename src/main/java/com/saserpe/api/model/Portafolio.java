package com.saserpe.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class Portafolio {

    @Column(name="id_porta")
    @JsonProperty("id_porta")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id_porta;

    @Column(name="RFC_usuario")
    @JsonProperty("RFC_usuario")
    private String RFC_usuario;

    @Column(name="RFC_empresa")
    @JsonProperty("RFC_empresa")
    private String RFC_empresa;

    @Column(name="precio_accion_usr")
    @JsonProperty("precio_accion_usr")
    private float precio_accion_usr;

    @Column(name="acciones_usr")
    @JsonProperty("acciones_usr")
    private Integer acciones_usr;

    @Column(name="precio_compra")
    @JsonProperty("precio_compra")
    private float precio_compra;

    public Portafolio(Integer id_porta, String RFC_usuario, String RFC_empresa, float precio_accion_usr, Integer acciones_usr, float precioC) {
        this.id_porta = id_porta;
        this.RFC_usuario = RFC_usuario;
        this.RFC_empresa = RFC_empresa;
        this.precio_accion_usr = precio_accion_usr;
        this.acciones_usr = acciones_usr;
        this.precio_compra = precioC;
    }
    public Portafolio(){

    }

    public float getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(float precio_compra) {
        this.precio_compra = precio_compra;
    }

    public Integer getId_porta() {
        return id_porta;
    }

    public void setId_porta(Integer id_porta) {
        this.id_porta = id_porta;
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

    public float getPrecio_accion_usr() {
        return precio_accion_usr;
    }

    public void setPrecio_accion_usr(float precio_accion_usr) {
        this.precio_accion_usr = precio_accion_usr;
    }

    public Integer getAcciones_usr() {
        return acciones_usr;
    }

    public void setAcciones_usr(Integer acciones_usr) {
        this.acciones_usr = acciones_usr;
    }
}
