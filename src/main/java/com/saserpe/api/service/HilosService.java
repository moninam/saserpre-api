package com.saserpe.api.service;

import com.saserpe.api.model.Hilos;
import com.saserpe.api.model.Propuesta;
import com.saserpe.api.repository.HilosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HilosService {
    @Autowired
    private HilosRepository repository;

    public List<Hilos> listAll(){
        return repository.findAll();
    }

    public void save(Hilos hilo){
        repository.save(hilo);
    }

    public Hilos findById(Integer id){
        return repository.findById(id).get();
    }

    public void delete(Integer id){
        repository.deleteById(id);
    }
    public List<Hilos> getHilosDesc(){
        return repository.getHilosDesc();
    }
    public Hilos getHiloByIdHilo(String idHilo){
        return repository.getHiloByIdHilo(idHilo);
    }
    public void updateHoraHilo(String idHilo,String hora){
        repository.updateHoraHilo(hora,idHilo);
    }
    public Hilos getActualHilo(){
        return repository.getActualHilo();
    }
    public void disableHilos(String idHilo){
        repository.disableHilo(idHilo);
    }
}
