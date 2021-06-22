package com.alten.altentest.controller;

import com.alten.altentest.model.Room;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(path = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {

    @GetMapping
    public List<Room> getAllRooms() {
        return null;
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathParam("id") Long id) {
        return null;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Room createRoom(@Validated @RequestBody Room room) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathParam("id") Long id) {
    }

}
