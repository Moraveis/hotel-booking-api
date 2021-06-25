package com.alten.altentest.service.impl;

import com.alten.altentest.exception.ElementNotFoundException;
import com.alten.altentest.model.Room;
import com.alten.altentest.repository.RoomRepository;
import com.alten.altentest.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DefaultRoomService implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Long id) {
        return findRoomById(id);
    }

    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public void updateRoom(Long id, Room room) {
        Room existingRoom = findRoomById(id);

        existingRoom.setNumber(room.getNumber());
        existingRoom.setAvailable(room.getAvailable());
        existingRoom.setSuite(room.getSuite());

        roomRepository.save(existingRoom);
    }

    @Override
    public void updateRoomAvailability(Long id, Boolean available) {
        Room room = findRoomById(id);
        room.setAvailable(available);

        roomRepository.save(room);
    }

    private Room findRoomById(Long id) throws ElementNotFoundException {
        return roomRepository
                .findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Hotel not found for the given identifier: hotelId=" + id));
    }
}
