package com.saserpe.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Acceso {
    @Column(name="id_session")
    @JsonProperty("id_session")
    @Id
    private String id_session;

    @Column(name="RFC_usuario")
    @JsonProperty("RFC_usuario")
    private String RFC_usuario;

    public Acceso(String id_session, String RFC_usuario) {
        this.id_session = id_session;
        this.RFC_usuario = RFC_usuario;
    }
    public Acceso(){

    }

    public String getId_session() {
        return id_session;
    }

    public void setId_session(String id_session) {
        this.id_session = id_session;
    }

    public String getRFC_usuario() {
        return RFC_usuario;
    }

    public void setRFC_usuario(String RFC_usuario) {
        this.RFC_usuario = RFC_usuario;
    }
}
