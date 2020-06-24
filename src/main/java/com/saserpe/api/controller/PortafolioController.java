package com.saserpe.api.controller;

import com.saserpe.api.model.Portafolio;
import com.saserpe.api.service.PortafolioService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortafolioController {
    private Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private PortafolioService portafolioService;
}
