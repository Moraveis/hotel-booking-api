package com.alten.altentest.controller;

import com.alten.altentest.model.Room;
import com.alten.altentest.service.impl.DefaultRoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {

    private final DefaultRoomService defaultRoomService;

    @GetMapping
    public List<Room> getAllRooms() {
        return defaultRoomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathParam("id") Long id) {
        return defaultRoomService.getRoomById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Room createRoom(@RequestBody Room room) {
        return defaultRoomService.createRoom(room);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRoom(@RequestBody Room room) {
        defaultRoomService.updateRoom(room);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRoomAvailability(Long id, Boolean availability) {
        defaultRoomService.updateRoomAvailability(id, availability);
    }

}
