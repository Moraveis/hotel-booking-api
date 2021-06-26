package com.alten.altentest.controller;

import com.alten.altentest.controller.mapper.RoomMapper;
import com.alten.altentest.datatransferobject.RoomDTO;
import com.alten.altentest.model.Room;
import com.alten.altentest.service.impl.DefaultRoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public RoomDTO createRoom(@Valid @RequestBody RoomDTO roomDTO) {
        Room room = RoomMapper.fromRoomDTO(roomDTO);
        return RoomMapper.toRoomDTO(defaultRoomService.createRoom(room));
    }

    @PutMapping("/{id}")
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
