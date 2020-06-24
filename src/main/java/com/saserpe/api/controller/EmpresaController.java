package com.saserpe.api.controller;

import com.saserpe.api.model.Empresa;
import com.saserpe.api.model.Usuario;
import com.saserpe.api.service.EmpresaService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class EmpresaController {
    private Logger logger = LogManager.getLogger(this.getClass());
    //Mensajes a mostrar
    public static String failR = new JSONObject()
            .put("code",404)
            .put("mensaje","No se pudo registrar la empresa en el sistema")
            .toString();
    public static String errorFName = new JSONObject()
            .put("code",404)
            .put("mensaje","El RFC debe tener una longitud de 10 caracteres")
            .toString();
    public static String successR = new JSONObject()
            .put("code",200)
            .put("mensaje","El registro se realizo con exito")
            .toString();
    public static String noADips = new JSONObject()
            .put("code",404)
            .put("mensaje","La empresa debe contar con acciones disponibles")
            .toString();
    public static String errorAc = new JSONObject()
            .put("code",404)
            .put("mensaje","El numero total de acciones no puede ser menor a las disponibles")
            .toString();
    public static String errorP = new JSONObject()
            .put("code",404)
            .put("mensaje","El precio de las acciones debe ser mayor a cero")
            .toString();
    public static String alreadyR = new JSONObject()
            .put("code",404)
            .put("mensaje","La empresa ya se encuentra registrada en el sistema")
            .toString();
    public static String noEmp = new JSONObject()
            .put("code",404)
            .put("mensaje","No se han encontrado resultados")
            .toString();
    //Terminan Mensajes

    @Autowired
    private EmpresaService empresaService;

    @RequestMapping(value = "/registro-empresa",method= RequestMethod.POST,produces= MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost")
    public ResponseEntity<String> registroEmpresa(@RequestBody Empresa empresa){
        try{
            Empresa temp = empresaService.findById(empresa.getRFC_empresa());
            return new ResponseEntity<>(alreadyR, HttpStatus.NOT_FOUND);
        } catch (NoSuchElementException exc){

        }
        try{
            if (empresa.getRFC_empresa().length() != 10){
                return new ResponseEntity<>(errorFName, HttpStatus.NOT_FOUND);
            }
            if (empresa.getAcciones_empr_disp() == 0){
                return new ResponseEntity<>(noADips, HttpStatus.NOT_FOUND);
            }
            if (empresa.getAcciones_empr_total() < empresa.getAcciones_empr_disp()){
                return new ResponseEntity<>(errorAc, HttpStatus.NOT_FOUND);
            }
            if (empresa.getPrecio_accion_empr() <= 0.0){
                return new ResponseEntity<>(errorP, HttpStatus.NOT_FOUND);
            }
            empresaService.save(empresa);
        } catch (Exception ex){
            return new ResponseEntity<>(failR, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(successR, HttpStatus.OK);
    }
    @RequestMapping(value = "/visualizar-acciones",method= RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost")
    public ResponseEntity<?> mostrarAccionesEmp(){
        try{
            List<Empresa> empresas = empresaService.listAll();
            return new ResponseEntity<>(empresas,HttpStatus.OK);
        } catch (NoSuchElementException exc){
            return new ResponseEntity<>(noEmp,HttpStatus.NOT_FOUND);
        }
    }
}
