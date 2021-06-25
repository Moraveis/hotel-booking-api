package com.alten.altentest.service.impl;

import com.alten.altentest.exception.BadRequestException;
import com.alten.altentest.exception.ElementNotFoundException;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.repository.ReservationRepository;
import com.alten.altentest.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DefaultReservationService implements ReservationService {

    private final ReservationRepository reservationRepository;

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
        if (reservation.getId() != null) {
            throw new BadRequestException("A record with this identifier already exists. To create a new record, please, don't provide a Id.");
        }

        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = findReservationById(id);
        reservation.setDeleted(true);

        reservationRepository.save(reservation);
    }

    @Override
    public void updateReservation(Long id, Reservation reservation) {
        Reservation existingReservation = findReservationById(id);

        existingReservation.setStartDate(reservation.getStartDate());
        existingReservation.setEndDate(reservation.getEndDate());
        // TODO: show we allow change the room?
        existingReservation.setRoom(reservation.getRoom());

        reservationRepository.save(reservation);
    }

    private Reservation findReservationById(Long id) throws ElementNotFoundException {
        return reservationRepository
                .findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Reservation not found for the given identifier: reservation=" + id));
    }
}
