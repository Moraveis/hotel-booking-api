package com.alten.altentest.service;

import com.alten.altentest.exception.ConstraintsViolationException;
import com.alten.altentest.model.Room;

import java.util.List;

public interface RoomService {

    List<Room> getAllRooms();

    Room getRoomById(Long id);

    Room createRoom(Room room) throws ConstraintsViolationException;

    void updateRoom(Long id, Room room);

    void updateRoomAvailability(Long id, Boolean available);

}
