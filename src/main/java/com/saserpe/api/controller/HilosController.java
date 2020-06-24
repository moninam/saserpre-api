package com.saserpe.api.controller;

import com.google.gson.Gson;
import com.saserpe.api.model.*;
import com.saserpe.api.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@EnableScheduling
@RestController
public class HilosController {

    private Logger logger = LogManager.getLogger(this.getClass());
    public Map<String, SseEmitter> emitters = new HashMap<>();
    @Autowired
    private HilosService hilosService;

    @Autowired
    private AccesoService accesoService;
    @Autowired
    private PropuestaService propuestaService;
    @Autowired
    private TransaccionService transaccionService;
    @Autowired
    private PortafolioService portafolioService;
    @Autowired
    private EmpresaService empresaService;

    private List<String> accesos = new ArrayList<>();

    @GetMapping(value = "/suscribe-hilos",consumes = MediaType.ALL_VALUE)
    @CrossOrigin(origins = "http://localhost")
    public ResponseEntity<SseEmitter> suscribeHilo(@RequestParam String sessionID){
        SseEmitter sseEmitter = new SseEmitter(-1L);
        sendInitEvent(sseEmitter);
        emitters.put(sessionID,sseEmitter);

        sseEmitter.onTimeout(()->emitters.remove(sseEmitter));
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
        sseEmitter.onError((e) ->emitters.remove(sseEmitter));
        return new ResponseEntity<SseEmitter>(sseEmitter, HttpStatus.OK);
    }

    @Scheduled(fixedRate = 5000)
    @CrossOrigin(origins = "http://localhost")
    public void getHilos(){
        List<Hilos> ultimosHilos = hilosService.getHilosDesc();
        if (ultimosHilos != null){
            String tiempoAct = getTime();
            for (Hilos item: ultimosHilos) {
                String tiempoHilo = item.getActualizacion();
                long difference = getDifference(tiempoHilo,tiempoAct);
                logger.debug("Diferencia de tiempos {}",difference);

                if (difference > 120000L){
                    String idHilo = item.getId_hilo();
                    logger.debug("Hilo : {}",item.getId_hilo());
                    logger.debug("Envias mensaje de bloqueo");
                    notificarBloqueo(idHilo);

                    String accion = item.getTipo_accion();

                    switch (accion){
                        case "C":
                            logger.debug("Caso Compra: ");

                            Propuesta winner = getGanador("C",idHilo);
                            notificarDesbloqueo(idHilo);

                            setTransaccion(winner);
                            updatePortafolio(winner);
                            updatePrecioAccion(winner);
                            updateAccionesEmpresa(winner.getRFC_empresa(),winner);
                            break;
                        case "V":
                            logger.debug("Caso Venta: ");
                            Propuesta winnerV = getGanador("V",idHilo);

                            setTransaccion(winnerV);
                            updatePortafolio(winnerV);
                            updatePrecioAccion(winnerV);
                            updateAccionesEmpresa(winnerV.getRFC_empresa(),winnerV);
                            break;
                    }
                    logger.debug("Finalizo Hilos, borra hilo");
                    hilosService.disableHilos(idHilo);
                    logger.debug("Borrar propuestas de ese hilo");
                    propuestaService.deletePropuesta(idHilo);
                    logger.debug("Evento actualizar propuestas");
                    updatePropuestas();
                }

            }

        }
    }
    private void sendInitEvent(SseEmitter emitter){
        try{
            emitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException exc){
            logger.error("Error en el envio de evento {}",exc.getMessage());
        }
    }

    private long getDifference(String time1, String time2)  {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try{
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);

            long difference = date2.getTime() - date1.getTime();

            logger.debug("Diferencia de tiempo en milisegundos {}",difference);
            return difference;
        } catch (Exception exception){
            return 0L;
        }
    }

    private String getTime () {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    private void notificarBloqueo(String idHilo){
        logger.debug("Entra a funcion bloqueo");
        List<String> accesosN = new ArrayList();
        List<Propuesta> propuestas = propuestaService.getPropuestaByIdHilos(idHilo);

        if(propuestas != null){
            logger.debug("Propuesta no es null");
            for (Propuesta item: propuestas) {
                String rfcUsuario = item.getRFC_usuario();
                List<Acceso> accesos = accesoService.getAccesoByRFCUsuario(rfcUsuario);

                if (accesos != null){
                    logger.debug("Acceso no es null {}",item.getRFC_usuario());
                    List<String> temp = accesosUsr(accesos);
                    accesosN.addAll(temp);
                }
            }
        }
        accesos.addAll(accesosN);

        for (String item: accesosN) {
            String eventFormatted = new JSONObject()
                    .put("usuario",item)
                    .put("bloqueo",true).toString();
            notificarEvento(item,"bloqueo-hilos",eventFormatted);
        }

    }

    private List<String> accesosUsr(List<Acceso> accesos){
        List<String> accesosN = new ArrayList();
        logger.debug("Entro a acceso de usuario");
        for (Acceso item: accesos) {
            String sesion = item.getId_session();
            logger.debug("Sesion no es nula {}",item.getId_session());
            if(sesion != null){
                accesosN.add(sesion);
            }
        }
        return accesosN;
    }

    private void notificarEvento(String item, String tipoEvento, String response){
        logger.debug("Se envia evento a {} de {}",item,tipoEvento);
        SseEmitter emitter = emitters.get(item);
        if (emitter != null){
            try{
                emitter.send(SseEmitter.event().name(tipoEvento).data(response));
            } catch (IOException exc){
                emitters.remove(emitter);
            }
        }
    }

    private Propuesta getGanador(String tipo,String idHilo){
        Propuesta ganador = new Propuesta();
        switch (tipo){
            case "C":
                ganador = propuestaService.getWinnerCompra(idHilo);
                break;
            case "V":
                ganador = propuestaService.getWinnerVenta(idHilo);
                break;
            default:
                break;
        }
        if(ganador.getId_hilo() != null){
            String eventFormatted = new JSONObject()
                    .put("usuario",ganador.getRFC_usuario())
                    .put("ganador",true)
                    .put("hilo",ganador.getId_hilo()).toString();

            logger.debug("Ganador es {}",ganador.getRFC_usuario());
            //Se le notifica al ganador
            List<Acceso> accesos = accesoService.getAccesoByRFCUsuario(ganador.getRFC_usuario());
            if (accesos != null){
                logger.debug("Notificando a los accesos del usuario ganador");
                for (Acceso item:accesos) {
                    String idSesion = item.getId_session();
                    notificarEvento(idSesion,"finalizacion-propuesta",eventFormatted);
                }
            }
        }
        notificarPropuestaUsuario(idHilo,ganador.getRFC_usuario());
        return ganador;
    }

    private void notificarPropuestaUsuario(String idHilo, String idUsuario){
        logger.debug("Notificando a perdedores");
        List<Propuesta> propuestas = propuestaService.getPropuestaByIdHilos(idHilo);
        List<Acceso> accesosN = new ArrayList<>();

        if (propuestas != null){
            for (Propuesta item: propuestas) {
                if (!item.getRFC_usuario().equals(idUsuario)){
                    String rfcUsuario = item.getRFC_usuario();
                    List<Acceso> accesos = accesoService.getAccesoByRFCUsuario(rfcUsuario);

                    if (accesos != null){
                        logger.debug("Acceso a notificacion {}",item.getRFC_usuario());
                        List<Acceso> temp = accesosProp(accesos);
                        accesosN.addAll(temp);
                    }
                }
            }
            for (Acceso item: accesosN) {
                logger.debug("Notificacion de finalizacion de hilo a perdedores");
                String eventFormatted = new JSONObject()
                        .put("usuario",item.getRFC_usuario())
                        .put("ganador",false)
                        .put("hilo",idHilo).toString();
                notificarEvento(item.getId_session(),"finalizacion-propuesta",eventFormatted);
            }
        }
    }

    private List<Acceso> accesosProp(List<Acceso> accesos){
        List<Acceso> accesosN = new ArrayList();
        logger.debug("Entro a acceso de usuario");
        for (Acceso item: accesos) {
            logger.debug("El acceso no es nula {}",item.getId_session());
            if(item != null){
                accesosN.add(item);
            }
        }
        return accesosN;
    }

    private void setTransaccion(Propuesta ganador){
        logger.debug("Creando transaccion");
        Transaccion transaccion = new Transaccion();
        String RFC_usuario = ganador.getRFC_usuario();
        String RFC_empresa = ganador.getRFC_empresa();
        float precioAccion = ganador.getPrecio_accion_prop();
        Integer operacionAccion = ganador.getOperacion_accion_prop();
        String tipoAccion = ganador.getTipo_accion();
        String fecha = setDate();

        transaccion.setRFC_usuario(RFC_usuario);
        transaccion.setRFC_empresa(RFC_empresa);
        transaccion.setPrecio_accion_tr(precioAccion);
        transaccion.setOperacion_accion_tr(operacionAccion);
        transaccion.setTipo_accion_tr(tipoAccion);
        transaccion.setFecha_tr(fecha);

        transaccionService.save(transaccion);

    }

    private void updatePortafolio(Propuesta ganador){
        String RFCUsuario = ganador.getRFC_usuario();
        String RFCEmpresa = ganador.getRFC_empresa();
        float precioAccion = ganador.getPrecio_accion_prop();
        String tipoAccion = ganador.getTipo_accion();
        Integer numeroAcciones = ganador.getOperacion_accion_prop();

        Portafolio userPortafolio = portafolioService.getPortafolio(RFCUsuario,RFCEmpresa);

        if(userPortafolio != null){
            Integer temp = numeroAcciones;
            switch (tipoAccion){
                case "C":
                    Integer item = userPortafolio.getAcciones_usr() + numeroAcciones;
                    if (item < 0){
                        item = 0;
                    }
                    temp = item;
                    break;
                case "V":
                    Integer itemV = userPortafolio.getAcciones_usr() - numeroAcciones;
                    if (itemV < 0){
                        itemV = 0;
                    }
                    temp = itemV;
                    break;
            }
            portafolioService.updateUsrAcciones(temp,RFCUsuario,RFCEmpresa);
        } else{
            Portafolio temporal = new Portafolio();
            temporal.setRFC_usuario(RFCUsuario);
            temporal.setRFC_empresa(RFCEmpresa);
            temporal.setPrecio_accion_usr(precioAccion);
            temporal.setAcciones_usr(numeroAcciones);

            portafolioService.save(temporal);
        }
    }

    private void updatePrecioAccion(Propuesta ganador){
        String RFCEmpresa = ganador.getRFC_empresa();
        Empresa empresa = empresaService.findById(RFCEmpresa);
        float precioActual = empresa.getPrecio_accion_empr();
        float nuevoPrecio = ganador.getPrecio_accion_prop();

        if(precioActual > 0.0){
            empresaService.updatePrecioAccion(nuevoPrecio,RFCEmpresa);
            portafolioService.updatePrecioAccion(nuevoPrecio,RFCEmpresa);
        }
    }

    private void notificarDesbloqueo(String idHilo){

        for (String items: accesos) {
            String eventFormatted = new JSONObject()
                    .put("usuario",items)
                    .put("bloqueo",false)
                    .put("hilo",idHilo).toString();
            notificarEvento(items,"bloqueo-hilos",eventFormatted);
        }
        accesos.clear();
    }

    private void updatePropuestas(){
        List<Propuesta> temp = propuestaService.listAll();
        String json;
        if(temp != null){
            json = new Gson().toJson(temp);
        } else{
            json = new JSONObject()
                    .put("code",404)
                    .put("mensaje","No existen propuestas registradas")
                    .toString();
        }
        for (Map.Entry me : emitters.entrySet()) {
            notificarEvento(me.getKey().toString(),"actualizar-propuesta",json);
        }
    }

    private void updateAccionesEmpresa(String RFCEmpresa,Propuesta ganador){
        try{
            Empresa emp = empresaService.findById(RFCEmpresa);
            Integer acciones = emp.getAcciones_empr_disp();
            switch (ganador.getTipo_accion()){
                case "C":
                    acciones = emp.getAcciones_empr_disp() - ganador.getOperacion_accion_prop();
                    break;
                case "V":
                    acciones = emp.getAcciones_empr_disp() + ganador.getOperacion_accion_prop();
                    break;
            }
            empresaService.updateAccionesDisp(acciones,RFCEmpresa);
        } catch (NoSuchElementException exc){

        }

    }

    private String setDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }
}
