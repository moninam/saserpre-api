package com.saserpe.api.service;

import com.saserpe.api.model.Acceso;
import com.saserpe.api.repository.AccesoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccesoService {
    @Autowired
    private AccesoRepository repository;

    public List<Acceso> listAll(){
        return repository.findAll();
    }

    public void save(Acceso acceso){
        repository.save(acceso);
    }

    public Acceso findById(String idSession){
        return repository.findById(idSession).get();
    }

    public void delete(String idSession){
        repository.deleteById(idSession);
    }

    public List<Acceso> getAccesoByRFCUsuario(String RFCUsuario){

        return repository.getAccesosByRFCUsuario(RFCUsuario);
    }
}
