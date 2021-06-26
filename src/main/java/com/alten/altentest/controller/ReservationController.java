package com.alten.altentest.controller;

import com.alten.altentest.controller.mapper.ReservationMapper;
import com.alten.altentest.datatransferobject.ReservationDTO;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.service.impl.DefaultReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    private final DefaultReservationService defaultReservationService;

    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return ReservationMapper.toReservationDTOList(defaultReservationService.getAllReservation());
    }

    @GetMapping("/{id}")
    public ReservationDTO getReservationById(@PathVariable("id") Long id) {
        return ReservationMapper.toReservationDTO(defaultReservationService.getReservationById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDTO createReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = ReservationMapper.fromReservationDTO(reservationDTO);
        return ReservationMapper.toReservationDTO(defaultReservationService.createReservation(reservation));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable("id") Long id) {
        defaultReservationService.deleteReservation(id);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateReservation(@PathVariable("id") Long id, @RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = ReservationMapper.fromReservationDTO(reservationDTO);
        defaultReservationService.updateReservation(id, reservation);
    }

}
