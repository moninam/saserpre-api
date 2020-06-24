package com.saserpe.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@Entity
public class Propuesta {

    @Column(name="id_prop")
    @JsonProperty("id_prop")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id_prop;

    @Column(name="RFC_usuario")
    @JsonProperty("RFC_usuario")
    private String RFC_usuario;

    @Column(name="RFC_empresa")
    @JsonProperty("RFC_empresa")
    private String RFC_empresa;

    @Column(name="precio_accion_prop")
    @JsonProperty("precio_accion_prop")
    private float precio_accion_prop;

    @Column(name="fecha_prop")
    @JsonProperty("fecha_prop")
    private String fecha_prop;

    @Column(name="operacion_accion_prop")
    @JsonProperty("operacion_accion_prop")
    private Integer operacion_accion_prop;

    @Column(name="tipo_accion")
    @JsonProperty("tipo_accion")
    private String tipo_accion;

    @Column(name="id_hilo")
    @JsonProperty("id_hilo")
    private String id_hilo;
    public Propuesta(){

    }
    public Propuesta(Integer id_prop, String RFC_usuario, String RFC_empresa, float precio_accion_prop, Integer operacion_accion_prop,String tipoAccion, String idHilo) {
        this.id_prop = id_prop;
        this.RFC_usuario = RFC_usuario;
        this.RFC_empresa = RFC_empresa;
        this.precio_accion_prop = precio_accion_prop;
        this.fecha_prop = setDate();
        //this.fecha_prop = fecha_prop;
        this.operacion_accion_prop = operacion_accion_prop;
        this.tipo_accion = tipoAccion;
        this.id_hilo = idHilo;
    }

    public Integer getId_prop() {
        return id_prop;
    }

    public void setId_prop(Integer id_prop) {
        this.id_prop = id_prop;
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

    public float getPrecio_accion_prop() {
        return precio_accion_prop;
    }

    public void setPrecio_accion_prop(float precio_accion_prop) {
        this.precio_accion_prop = precio_accion_prop;
    }

    public String getFecha_prop() {
        return fecha_prop;
    }

    public void setFecha_prop(String fecha_prop) {
        this.fecha_prop = fecha_prop;
    }

    public Integer getOperacion_accion_prop() {
        return operacion_accion_prop;
    }

    public void setOperacion_accion_prop(Integer operacion_accion_prop) {
        this.operacion_accion_prop = operacion_accion_prop;
    }

    public String getTipo_accion() {
        return tipo_accion;
    }

    public void setTipo_accion(String tipo_accion) {
        this.tipo_accion = tipo_accion;
    }

    public String getId_hilo() {
        return id_hilo;
    }

    public void setId_hilo(String id_hilo) {
        this.id_hilo = id_hilo;
    }

    private String setDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }
}

