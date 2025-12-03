package com.dn.parking.reservationservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReservationController {

    @GetMapping("reservation/{id}")
    public String reservation(@PathVariable int id){
        return "reservation23";
    }
}
