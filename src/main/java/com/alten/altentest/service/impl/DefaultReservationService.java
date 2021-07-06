package com.alten.altentest.service.impl;

import com.alten.altentest.exception.BadRequestException;
import com.alten.altentest.exception.ElementNotFoundException;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.model.Room;
import com.alten.altentest.repository.ReservationRepository;
import com.alten.altentest.repository.RoomRepository;
import com.alten.altentest.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alten.altentest.util.ReservationUtil.isOverlappingDates;
import static com.alten.altentest.util.ReservationUtil.isWithinRange;
import static com.alten.altentest.util.ReservationUtil.validateReservationRequest;

@Service
@AllArgsConstructor
public class DefaultReservationService implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<Reservation> getAllReservation() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Long id) {
        return findReservationById(id);
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        Room room = findRoomById(reservation.getRoom().getId());
        reservation.setRoom(room);

        validateReservationRequest(reservation);

        return reservationRepository.save(reservation);
    }

    @Override
    public void updateReservation(Long id, Reservation reservation) {
        Reservation existingReservation = findReservationById(id);

        existingReservation.setStartDate(reservation.getStartDate());
        existingReservation.setEndDate(reservation.getEndDate());
        existingReservation.setReservedBy(reservation.getReservedBy());
        existingReservation.setRoom(reservation.getRoom());

        validateReservationRequest(existingReservation);

        reservationRepository.save(existingReservation);
    }

    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = findReservationById(id);
        reservation.setDeleted(true);

        reservationRepository.save(reservation);
    }

    private Reservation findReservationById(Long id) throws ElementNotFoundException {
        return reservationRepository
                .findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Reservation not found for the given identifier: reservation=" + id));
    }

    private Room findRoomById(Long id) throws ElementNotFoundException {
        return roomRepository
                .findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Room not found for the given identifier: RoomId=" + id));
    }

}
