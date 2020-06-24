package com.saserpe.api.controller;

import com.saserpe.api.model.Hilos;
import com.saserpe.api.model.Propuesta;
import com.saserpe.api.service.HilosService;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

@EnableScheduling
@RestController
public class PropuestaController {

    private  Logger logger = LogManager.getLogger(this.getClass());
    public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @Autowired
    private PropuestaService service;
    @Autowired
    private HilosService hilosService;

    @GetMapping(value = "/suscribe-propuesta",consumes = MediaType.ALL_VALUE)
    @CrossOrigin(origins = "http://localhost")
    public ResponseEntity<SseEmitter> suscribePropuesta(){
        logger.debug("Se imprime clase");
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        try{
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException exc){
            logger.error("Error en el envio de evento {}",exc.getMessage());
        }
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
        emitters.add(sseEmitter);
        return new ResponseEntity<SseEmitter>(sseEmitter, HttpStatus.OK);
    }
    @PostMapping(value = "/upload-propuesta")
    @CrossOrigin(origins = "http://localhost")
    public void uploadPropuesta(@RequestBody Propuesta propuesta){
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
        }
        Hilos nh= hilosService.getHiloByIdHilo(id_hilo);

        String hiloN = new JSONObject()
                .put("id_hilo",nh.getId_hilo())
                .put("activo",nh.isActivo())
                .put("actualizacion",nh.getActualizacion()).toString();
        logger.debug("Hilo {}",hiloN);
        try{
            getDifference();
        } catch (Exception e){

        }


        service.save(propuesta);
        Propuesta temp = service.findByRFCEmpUsr(propuesta.getRFC_empresa(),propuesta.getRFC_usuario());

        String eventFormatted = new JSONObject()
                .put("RFC_usuario",temp.getRFC_usuario())
                .put("RFC_empresa",temp.getRFC_empresa())
                .put("precio_accion_prop",temp.getPrecio_accion_prop())
                .put("fecha_prop",temp.getFecha_prop())
                .put("operacion_accion_prop",temp.getOperacion_accion_prop()).toString();

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

    private void getDifference() throws ParseException {
        String time1 = "21:33:33";
        String time2 = "23:33:33";

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(time1);
        Date date2 = format.parse(time2);

        long difference = date2.getTime() - date1.getTime();

        logger.debug("Diferencia de tiempo en milisegundos {}",difference);

    }
}
