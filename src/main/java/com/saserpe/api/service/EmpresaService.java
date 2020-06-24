package com.saserpe.api.service;

import com.saserpe.api.model.Empresa;
import com.saserpe.api.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {
    @Autowired
    private EmpresaRepository repository;

    public List<Empresa> listAll(){
        return repository.findAll();
    }

    public void save(Empresa empresa){
        repository.save(empresa);
    }

    public Empresa findById(String RFC_empresa){
        return repository.findById(RFC_empresa).get();
    }

    public void delete(String RFC_empresa){
        repository.deleteById(RFC_empresa);
    }

    public void updatePrecioAccion(float precioAccion, String RFCEmpresa){
        repository.updatePrecioAccion(precioAccion,RFCEmpresa);
    }

    public void updateAccionesDisp(Integer disp, String RFC_empresa){
        repository.updateAccionesDisp(disp,RFC_empresa);
    }
}
