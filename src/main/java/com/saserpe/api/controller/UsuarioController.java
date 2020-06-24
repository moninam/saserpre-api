package com.saserpe.api.controller;

import com.saserpe.api.model.Acceso;
import com.saserpe.api.model.Empresa;
import com.saserpe.api.model.Login;
import com.saserpe.api.model.Usuario;
import com.saserpe.api.service.AccesoService;
import com.saserpe.api.service.UsuarioService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class UsuarioController {
    private Logger logger = LogManager.getLogger(this.getClass());

    //Mensajes a imprimir
    public static String failR = new JSONObject()
            .put("code",404)
            .put("mensaje","No se pudo registrar el usuario en el sistema")
            .toString();
    public static String errorFName = new JSONObject()
            .put("code",404)
            .put("mensaje","El RFC debe tener una longitud de 10 caracteres")
            .toString();
    public static String successR = new JSONObject()
            .put("code",200)
            .put("mensaje","El registro se realizo con exito")
            .toString();
    public static String noUserFound = new JSONObject()
            .put("code",404)
            .put("login",false)
            .put("mensaje","El usuario no se encuentra registrado en el sistema")
            .toString();
    public static String passwordWrong = new JSONObject()
            .put("code",404)
            .put("login",false)
            .put("mensaje","La contraseña es erronea")
            .toString();
    public static String alreadyR = new JSONObject()
            .put("code",404)
            .put("login",false)
            .put("mensaje","El usuario ya se encuentra registrado en el sistema")
            .toString();
    public static String loginSucces = new JSONObject()
            .put("code",200)
            .put("login",true)
            .put("mensaje","El usuario ingreso al sistema con exito")
            .toString();
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AccesoService accesoService;

    @RequestMapping(value = "/registro-usuario",method= RequestMethod.POST,produces= MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost")
    public ResponseEntity<String> registroUsuario(@RequestBody Usuario usuario){
        try{
            Usuario temp = usuarioService.findById(usuario.getRFC_usuario());
            return new ResponseEntity<>(alreadyR, HttpStatus.NOT_FOUND);
        } catch (NoSuchElementException exc){

        }
        try{
            if (usuario.getRFC_usuario().length() != 10){
                return new ResponseEntity<>(errorFName, HttpStatus.NOT_FOUND);
            }
            usuarioService.save(usuario);
        } catch (Exception ex){
            return new ResponseEntity<>(failR, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(successR, HttpStatus.OK);
    }
    @RequestMapping(value = "/login-usuario",method= RequestMethod.POST,produces= MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost")
    public ResponseEntity<String> loginUsuario(@RequestBody Login login){
        Acceso nuevoAcceso = new Acceso();
        try{
            Usuario temp = usuarioService.findById(login.getRFCUsuario());
            String contra = temp.getContra_usuario();
            if (contra.equals(login.getPassword())){
                nuevoAcceso.setId_session(login.getSessionID());
                nuevoAcceso.setRFC_usuario(login.getRFCUsuario());
                accesoService.save(nuevoAcceso);
                return new ResponseEntity<>(loginSucces, HttpStatus.OK);
            } else{
                return new ResponseEntity<>(passwordWrong, HttpStatus.NOT_FOUND);
            }
        } catch (NoSuchElementException exc){
            return new ResponseEntity<>(noUserFound, HttpStatus.NOT_FOUND);
        }

    }

}
