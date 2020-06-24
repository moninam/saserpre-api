package com.saserpe.api.controller;

import com.saserpe.api.model.Portafolio;
import com.saserpe.api.service.PortafolioService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PortafolioController {
    private Logger logger = LogManager.getLogger(this.getClass());
    //Mensajes a mostrar
    public static String failR = new JSONObject()
            .put("code",404)
            .put("mensaje","No se encontraron resultados")
            .toString();
    @Autowired
    private PortafolioService portafolioService;

    @RequestMapping(value = "/visualizar-portafolio/{id}",method= RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost")
    public ResponseEntity<?> obtenerPortafolio(@PathVariable String id){
        List<Portafolio> portafolios = portafolioService.getPortafolioByRFCUsuario(id);
        if (portafolios == null){
            return new ResponseEntity<>(failR, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(portafolios,HttpStatus.OK);
    }
}
