package org.urzednicza.pylon.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PylonController {

    @GetMapping
    public String welcome(){
        return "Pylon is here";
    }
}
