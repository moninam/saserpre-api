package com.saserpe.api.service;

import com.saserpe.api.model.Propuesta;
import com.saserpe.api.repository.PropuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PropuestaService {
    @Autowired
    private PropuestaRepository repository;

    public List<Propuesta> listAll(){
        return repository.findAll();
    }

    public void save(Propuesta propuesta){
        repository.save(propuesta);
    }

    public Propuesta findById(Integer id){
        return repository.findById(id).get();
    }

    public void delete(Integer id){
        repository.deleteById(id);
    }

    public Propuesta findByRFCEmpUsr(String RFCEmpresa, String RFCUsuario){
        return repository.findByRFCEmpUsr(RFCEmpresa,RFCUsuario);
    }
    public Propuesta getWinnerVenta(String idHilo){
        return repository.getWinnerVenta(idHilo);
    }
    public Propuesta getWinnerCompra(String idHilo){
        return repository.getWinnerCompra(idHilo);
    }

    public List<Propuesta> getPropuestaByIdHilos(String idHilo){
        return repository.getPropuestasByHilo(idHilo);
    }

    public void deletePropuesta(String idHilo){
        repository.deletePropuesta(idHilo);
    }
}
