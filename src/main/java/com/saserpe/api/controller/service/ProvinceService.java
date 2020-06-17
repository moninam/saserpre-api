package com.saserpe.api.controller.service;

import com.saserpe.api.controller.repository.ProvinceRepository;
import com.saserpe.api.model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Service
public class ProvinceService {
    @Autowired
    private ProvinceRepository repository;

    public List<Province> listAll(){
        return repository.findAll();
    }
    public void save(Province province){
        repository.save(province);
    }
    public Province get(Integer id){
        return repository.findById(id).get();
    }
    public void delete(Integer id){
        repository.deleteById(id);
    }
}
