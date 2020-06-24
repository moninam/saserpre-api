package com.saserpe.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Login {

    @JsonProperty("RFC_usuario")
    private String RFCUsuario;
    @JsonProperty("password")
    private String password;
    @JsonProperty("session_id")
    private String sessionID;

    public Login(){

    }
    public Login(String RFCUsuario, String password, String sessionID) {
        this.RFCUsuario = RFCUsuario;
        this.password = password;
        this.sessionID = sessionID;
    }

    public String getRFCUsuario() {
        return RFCUsuario;
    }

    public void setRFCUsuario(String RFCUsuario) {
        this.RFCUsuario = RFCUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
