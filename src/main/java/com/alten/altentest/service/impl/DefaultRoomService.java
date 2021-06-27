package com.alten.altentest.service.impl;

import com.alten.altentest.exception.BadRequestException;
import com.alten.altentest.exception.ConstraintsViolationException;
import com.alten.altentest.exception.ElementNotFoundException;
import com.alten.altentest.model.Room;
import com.alten.altentest.repository.RoomRepository;
import com.alten.altentest.service.RoomService;
import com.alten.altentest.util.ReservationUtil;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Room createRoom(Room room) throws ConstraintsViolationException {
        Room saved;

        try {
            saved = roomRepository.save(room);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintsViolationException(e.getMessage());
        }

        return saved;
    }

    @Override
    public void updateRoom(Long id, Room room) {
        Room existingRoom = findRoomById(id);

        existingRoom.setNumber(Optional.ofNullable(room.getNumber()).orElse(existingRoom.getNumber()));
        existingRoom.setAvailable(Optional.ofNullable(room.getAvailable()).orElse(existingRoom.getAvailable()));
        existingRoom.setSuite(Optional.ofNullable(room.getSuite()).orElse(existingRoom.getSuite()));

        roomRepository.save(existingRoom);
    }

    @Override
    public void updateRoomAvailability(Long id, Boolean available) {
        Room room = findRoomById(id);
        room.setAvailable(available);

        roomRepository.save(room);
    }

    @Override
    public Room getRoomAvailabilityByReservationPeriod(Long id, LocalDateTime startDate, LocalDateTime endDate) {
        Room room = findRoomById(id);

        if (room.getReservations() == null || room.getReservations().isEmpty()) {
            return room;
        }

        if (room.getReservations().stream().filter(r -> !r.getDeleted()).anyMatch(r -> ReservationUtil.isWithinRange(startDate, endDate, r))) {
            throw new BadRequestException(String.format("No available room for the requested period: room=%s, start=%s, end=%s", id, startDate, endDate));
        }

        return room;
    }

    private Room findRoomById(Long id) throws ElementNotFoundException {
        return roomRepository
                .findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Room not found for the given identifier: RoomId=" + id));
    }
}
