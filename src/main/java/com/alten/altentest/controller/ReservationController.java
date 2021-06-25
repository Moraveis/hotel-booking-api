package com.alten.altentest.controller;

import com.alten.altentest.model.Reservation;
import com.alten.altentest.service.impl.DefaultReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    private final DefaultReservationService defaultReservationService;

    @GetMapping
    public List<Reservation> getAllReservations() {
        return defaultReservationService.getAllReservation();
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathParam("id") Long id) {
        return defaultReservationService.getReservationById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return defaultReservationService.createReservation(reservation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathParam("id") Long id) {
        defaultReservationService.deleteReservation(id);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateReservation(@PathParam("id") Long id, @RequestBody Reservation reservation) {
        defaultReservationService.updateReservation(id, reservation);
    }

}
