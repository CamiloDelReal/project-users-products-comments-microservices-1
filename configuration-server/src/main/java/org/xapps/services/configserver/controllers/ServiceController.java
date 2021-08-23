package org.xapps.services.configserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/status")
public class ServiceController {

    private Environment env;


    @Autowired
    public ServiceController(Environment env) {
        this.env = env;
    }

    @GetMapping(path = "/check")
    public String check() {
        return "Config service running with\n" +
                "\tPort: " + env.getProperty("server.port");
    }

}
