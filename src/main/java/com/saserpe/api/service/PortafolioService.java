package com.saserpe.api.service;


import com.saserpe.api.model.Portafolio;
import com.saserpe.api.repository.PortafolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortafolioService {
    @Autowired
    private PortafolioRepository repository;

    public List<Portafolio> listAll(){
        return repository.findAll();
    }

    public void save(Portafolio portafolio){
        repository.save(portafolio);
    }

    public Portafolio findById(Integer id){
        return repository.findById(id).get();
    }

    public void delete(Integer id){
        repository.deleteById(id);
    }

    public void updateUsrAcciones(Integer acciones,String RFCUsuario, String RFCEmpresa){
        repository.updateUsrAcciones(acciones,RFCUsuario,RFCEmpresa);
    }
    public void updatePrecioAccion(float precio, String RFCEmpresa){
        repository.updatePrecioAccion(precio,RFCEmpresa);
    }
    public void updatePrecioCompra(float precio, String RFCUsuario,String RFCEmpresa){
        repository.updatePrecioCompra(precio,RFCUsuario,RFCEmpresa);
    }

    public Portafolio getPortafolio(String RFCUsuario, String RFCEmpresa){
        return repository.getElemento(RFCUsuario,RFCEmpresa);
    }
    public List<Portafolio> getPortafolioByRFCUsuario(String RFCUsuario){
        return repository.getElementByRFCUsuario(RFCUsuario);
    }
}
