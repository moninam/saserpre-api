package com.saserpe.api.controller;

import com.saserpe.api.model.*;
import com.saserpe.api.service.EmpresaService;
import com.saserpe.api.service.HilosService;
import com.saserpe.api.service.PortafolioService;
import com.saserpe.api.service.PropuestaService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@EnableScheduling
@RestController
public class PropuestaController {

    private  Logger logger = LogManager.getLogger(this.getClass());
    public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    //Mensajes a devolver
    public static String empresaNone = new JSONObject()
            .put("code",404)
            .put("mensaje","La empresa no se encuentra registrada en el sistema")
            .toString();
    public static String accionesInEm = new JSONObject()
            .put("code",404)
            .put("mensaje","La empresa no cuenta con las acciones suficientes para continuar la transaccion")
            .toString();
    public static String precioNPEmp = new JSONObject()
            .put("code",404)
            .put("mensaje","El precio de la accion no puede ser menor al precio actual")
            .toString();

    public static String portafolioNone = new JSONObject()
            .put("code",404)
            .put("mensaje","No cuenta con un portafolio de acciones para la empresa seleccionada")
            .toString();
    public static String accionesInUsr = new JSONObject()
            .put("code",404)
            .put("mensaje","No cuenta con las acciones suficientes para realizar la venta")
            .toString();
    public static String precioNPUsr = new JSONObject()
            .put("code",404)
            .put("mensaje","El precio de la accion no puede ser mayor al precio actual")
            .toString();

    public static String accionNL = new JSONObject()
            .put("code",404)
            .put("mensaje","Tipo de operacion no permitida")
            .toString();

    public static String exitoProp = new JSONObject()
            .put("code",200)
            .put("mensaje","La propuesta fue realizada con exito")
            .toString();
    //Termina Seccion de mensajes
    @Autowired
    private PropuestaService service;
    @Autowired
    private HilosService hilosService;
    @Autowired
    private PortafolioService portafolioService;
    @Autowired
    private EmpresaService empresaService;

    @GetMapping(value = "/suscribe-propuesta",consumes = MediaType.ALL_VALUE)
    @CrossOrigin(origins = "http://localhost")
    public ResponseEntity<SseEmitter> suscribePropuesta(){
        logger.debug("Se imprime clase");
        SseEmitter sseEmitter = new SseEmitter(-1L);

        try{
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException exc){
            logger.error("Error en el envio de evento {}",exc.getMessage());
        }
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));

        emitters.add(sseEmitter);
        return new ResponseEntity<SseEmitter>(sseEmitter, HttpStatus.OK);
    }

    @RequestMapping(value = "/upload-propuesta",method= RequestMethod.POST,consumes =MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost")
    public ResponseEntity<String> uploadPropuesta(@RequestBody Propuesta propuesta){
        String tAccion = propuesta.getTipo_accion();

        if (tAccion != null){
            switch (tAccion){
                case "C":
                    try{
                        Empresa emp = empresaService.findById(propuesta.getRFC_empresa());
                        Integer accionesD = emp.getAcciones_empr_disp();
                        Integer accionesP = propuesta.getOperacion_accion_prop();

                        if (accionesP > accionesD){
                            return new ResponseEntity<>(accionesInEm,HttpStatus.NOT_FOUND);
                        }

                        float precioEmp = emp.getPrecio_accion_empr();
                        float precioProp = propuesta.getPrecio_accion_prop();

                        if(precioProp < precioEmp){
                            return new ResponseEntity<>(precioNPEmp,HttpStatus.NOT_FOUND);
                        }
                    } catch (NoSuchElementException exc){
                        return new ResponseEntity<>(empresaNone,HttpStatus.NOT_FOUND);
                    }
                    break;
                case "V":
                    Portafolio temp = portafolioService.getPortafolio(propuesta.getRFC_usuario(),propuesta.getRFC_empresa());
                    if (temp == null){
                        return new ResponseEntity<>(portafolioNone,HttpStatus.NOT_FOUND);
                    }
                    Integer numeroAccionesUsr = temp.getAcciones_usr();
                    Integer numeroAccionesPr = propuesta.getOperacion_accion_prop();

                    if (numeroAccionesUsr < numeroAccionesPr){
                        return new ResponseEntity<>(accionesInUsr,HttpStatus.NOT_FOUND);
                    }
                    try{
                        Empresa empr = empresaService.findById(propuesta.getRFC_empresa());
                        float precioEmpresa = empr.getPrecio_accion_empr();
                        float precioUsuario = propuesta.getPrecio_accion_prop();

                        if (precioUsuario > precioEmpresa){
                            return new ResponseEntity<>(precioNPUsr,HttpStatus.NOT_FOUND);
                        }
                    } catch (NoSuchElementException exc){
                        return new ResponseEntity<>(empresaNone,HttpStatus.NOT_FOUND);
                    }
                    break;
                default:
                    return new ResponseEntity<>(accionNL,HttpStatus.NOT_FOUND);

            }

            //Se obtiene fecha y hora actual
            String fecha = setDate();
            String hora = getTime();
            String id_hilo = propuesta.getRFC_empresa()  + "-" + propuesta.getTipo_accion();
            //Se setea la fecha en la propuesta y se crea la propuesta
            propuesta.setFecha_prop(fecha);
            propuesta.setId_hilo(id_hilo);

            String tipoAccion = propuesta.getTipo_accion();


            Hilos hilo = hilosService.getHiloByIdHilo(id_hilo);

            if(hilo == null){
                Hilos nuevoHilo = new Hilos(id_hilo,hora,tipoAccion);
                nuevoHilo.setActivo(true);
                hilosService.save(nuevoHilo);
            } else{
                hilosService.updateHoraHilo(id_hilo,hora);
                if (!hilo.isActivo()){
                    hilosService.enableHilos(id_hilo);
                }
            }
            Hilos nh= hilosService.getHiloByIdHilo(id_hilo);

            String hiloN = new JSONObject()
                    .put("id_hilo",nh.getId_hilo())
                    .put("activo",nh.isActivo())
                    .put("actualizacion",nh.getActualizacion()).toString();

            logger.debug("Hilo {}",hiloN);

            service.save(propuesta);
            Propuesta temp = service.findByRFCEmpUsr(propuesta.getRFC_empresa(),propuesta.getRFC_usuario());

            String eventFormatted = new JSONObject()
                    .put("RFC_usuario",temp.getRFC_usuario())
                    .put("RFC_empresa",temp.getRFC_empresa())
                    .put("precio_accion_prop",temp.getPrecio_accion_prop())
                    .put("fecha_prop",temp.getFecha_prop())
                    .put("operacion_accion_prop",temp.getOperacion_accion_prop())
                    .put("tipo_accion",temp.getTipo_accion())
                    .put("id_hilo",temp.getId_hilo()).toString();

            logger.debug("Fecha {}",temp.getFecha_prop());
            logger.debug("String: {}",eventFormatted);

            for (SseEmitter emitter : emitters){
                try{
                    logger.debug("Entro a envio");
                    emitter.send(SseEmitter.event().name("propuesta-news").data(eventFormatted));
                } catch (IOException exc){
                    emitters.remove(emitter);
                }
            }
            return new ResponseEntity<>(exitoProp,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(accionNL,HttpStatus.NOT_FOUND);
        }

    }

    private String setDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    private String getTime () {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }


}
