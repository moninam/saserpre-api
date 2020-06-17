package com.saserpe.api.controller;

import com.saserpe.api.controller.service.ProvinceService;
import com.saserpe.api.model.Province;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class ProvinceController {

    private static final Logger logger = LogManager.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceService service;

    @GetMapping("/provinces")
    public List<Province> list(){
        return service.listAll();
    }
    @GetMapping("/provinces/{id}")
    public ResponseEntity<Province> get(@PathVariable Integer id){
        try{
            Province province = service.get(id);
            return new ResponseEntity<Province>(province, HttpStatus.OK);
        } catch (NoSuchElementException exc){
            return new ResponseEntity<Province>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/provinces")
    public void add(@RequestBody Province province){
        logger.debug("Add Province: " + province.getShortName());
        service.save(province);
    }

    @PutMapping("/provinces/{id}")
    public ResponseEntity<?> update(@RequestBody Province province,@PathVariable Integer id){
        try{
            Province temp = service.get(id);
            service.save(province);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException exc){
            logger.error("Error no foun",exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/provinces/{id}")
    public void delete(@PathVariable Integer id){
        service.delete(id);
    }
}
