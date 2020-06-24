package com.saserpe.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Usuario {
    @Column(name="RFC_usuario")
    @JsonProperty("RFC_usuario")
    @Id
    private String RFC_usuario;

    @Column(name="nombre_usuario")
    @JsonProperty("nombre_usuario")
    private String nombre_usuario;

    @Column(name="contra_usuario")
    @JsonProperty("contra_usuario")
    private String contra_usuario;

    public Usuario(String RFC_usuario, String nombre_usuario, String contra_usuario) {
        this.RFC_usuario = RFC_usuario;
        this.nombre_usuario = nombre_usuario;
        this.contra_usuario = contra_usuario;
    }

    public Usuario(){
    }

    public String getRFC_usuario() {
        return RFC_usuario;
    }

    public void setRFC_usuario(String RFC_usuario) {
        this.RFC_usuario = RFC_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getContra_usuario() {
        return contra_usuario;
    }

    public void setContra_usuario(String contra_usuario) {
        this.contra_usuario = contra_usuario;
    }
}
