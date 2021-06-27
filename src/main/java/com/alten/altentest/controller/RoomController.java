package com.alten.altentest.controller;

import com.alten.altentest.controller.mapper.RoomMapper;
import com.alten.altentest.datatransferobject.RoomDTO;
import com.alten.altentest.exception.ConstraintsViolationException;
import com.alten.altentest.model.Room;
import com.alten.altentest.service.impl.DefaultRoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {

    private final DefaultRoomService defaultRoomService;

    @GetMapping
    public List<RoomDTO> getAllRooms() {
        return RoomMapper.toRoomDTOList(defaultRoomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public RoomDTO getRoomById(@PathVariable("id") Long id) {
        return RoomMapper.toRoomDTO(defaultRoomService.getRoomById(id));
    }

    @GetMapping("/{id}/reservations")
    public RoomDTO getReservationsForRoomId(@PathVariable("id") Long id) {
        return RoomMapper.toRoomDTOWithReservations(defaultRoomService.getRoomById(id));
    }

    @GetMapping("/{id}/availability")
    public RoomDTO getRoomAvailabilityByReservationPeriod(@PathVariable("id") Long id,
                                                          @RequestParam("startDate") LocalDateTime startDate,
                                                          @RequestParam("endDate") LocalDateTime endDate) {

        return RoomMapper.toRoomDTOWithReservations(defaultRoomService.getRoomAvailabilityByReservationPeriod(id, startDate, endDate));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public RoomDTO createRoom(@Valid @RequestBody RoomDTO roomDTO) throws ConstraintsViolationException {
        Room room = RoomMapper.fromRoomDTO(roomDTO);
        return RoomMapper.toRoomDTO(defaultRoomService.createRoom(room));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRoom(@PathVariable("id") Long id, @Valid @RequestBody RoomDTO roomDTO) {
        Room room = RoomMapper.fromRoomDTO(roomDTO);
        defaultRoomService.updateRoom(id, room);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRoomAvailability(@PathVariable("id") Long id, @RequestParam("availability") Boolean availability) {
        defaultRoomService.updateRoomAvailability(id, availability);
    }

}
