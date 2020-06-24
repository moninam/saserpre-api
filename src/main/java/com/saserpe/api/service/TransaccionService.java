package com.saserpe.api.service;

import com.saserpe.api.model.Transaccion;
import com.saserpe.api.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransaccionService {
    @Autowired
    private TransaccionRepository repository;

    public List<Transaccion> listAll(){
        return repository.findAll();
    }

    public void save(Transaccion trans){
        repository.save(trans);
    }

    public Transaccion findById(Integer id){
        return repository.findById(id).get();
    }

    public void delete(Integer id){
        repository.deleteById(id);
    }
}
