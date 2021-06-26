package com.alten.altentest.service.impl;

import com.alten.altentest.exception.ElementNotFoundException;
import com.alten.altentest.model.Room;
import com.alten.altentest.repository.RoomRepository;
import com.alten.altentest.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        existingRoom.setNumber(Optional.of(room.getNumber()).orElse(existingRoom.getNumber()));
        existingRoom.setAvailable(Optional.of(room.getAvailable()).orElse(existingRoom.getAvailable()));
        existingRoom.setSuite(Optional.of(room.getSuite()).orElse(existingRoom.getSuite()));

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
                .orElseThrow(() -> new ElementNotFoundException("Room not found for the given identifier: RoomId=" + id));
    }
}
