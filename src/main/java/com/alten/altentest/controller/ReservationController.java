package com.alten.altentest.controller;

import com.alten.altentest.controller.mapper.ReservationMapper;
import com.alten.altentest.datatransferobject.ReservationDTO;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.service.impl.DefaultReservationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "Get all Reservations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return a list of Rooms."),
            @ApiResponse(code = 500, message = "Something went wrong.")
    })
    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return ReservationMapper.toReservationDTOList(defaultReservationService.getAllReservation());
    }

    @ApiOperation(value = "Get Reservation by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservation found for the specified id."),
            @ApiResponse(code = 404, message = "No Reservation found for the specified id."),
            @ApiResponse(code = 500, message = "Something went wrong.")
    })
    @GetMapping("/{id}")
    public ReservationDTO getReservationById(@PathVariable("id") Long id) {
        return ReservationMapper.toReservationDTO(defaultReservationService.getReservationById(id));
    }

    @ApiOperation(value = "Create Reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Reservation created Successfully."),
            @ApiResponse(code = 400, message = "Fail to save Reservation due to constraints violations or Invalid data provided."),
            @ApiResponse(code = 404, message = "No Room found for the requested."),
            @ApiResponse(code = 500, message = "Something went wrong while processing teh request.")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDTO createReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = ReservationMapper.fromReservationDTO(reservationDTO);
        return ReservationMapper.toReservationDTO(defaultReservationService.createReservation(reservation));
    }

    @ApiOperation(value = "Create Reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Reservation updated Successfully."),
            @ApiResponse(code = 400, message = "Fail to save Reservation due to constraints violations or Invalid data provided."),
            @ApiResponse(code = 404, message = "No Room found for the requested."),
            @ApiResponse(code = 500, message = "Something went wrong while processing teh request.")
    })
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReservation(@PathVariable("id") Long id, @RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = ReservationMapper.fromReservationDTO(reservationDTO);
        defaultReservationService.updateReservation(id, reservation);
    }

    @ApiOperation(value = "Cancel Reservation by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Reservation found for the specified id and cancelled."),
            @ApiResponse(code = 404, message = "No Reservation found for the specified id."),
            @ApiResponse(code = 500, message = "Something went wrong.")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable("id") Long id) {
        defaultReservationService.deleteReservation(id);
    }

}
