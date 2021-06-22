package com.alten.altentest.service.impl;

import com.alten.altentest.exception.ElementNotFoundException;
import com.alten.altentest.model.Room;
import com.alten.altentest.repository.RoomRepository;
import com.alten.altentest.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return findRoomBy(id);
    }

    @Override
    public Room createRoom(Room room) {
        // TODO: add validation for id? or use a DTO?

        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        Room room = findRoomBy(id);

        // TODO: make room unavailable
    }

    private Room findRoomBy(Long hotelId) throws ElementNotFoundException {
        return roomRepository
                .findById(hotelId)
                .orElseThrow(() -> new ElementNotFoundException("Hotel not found for the given identifier: hotelId=" + hotelId));
    }
}
