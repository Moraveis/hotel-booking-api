package com.alten.altentest.service.impl;

import com.alten.altentest.exception.ElementNotFoundException;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.repository.ReservationRepository;
import com.alten.altentest.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        existingReservation.setStartDate(Optional.ofNullable(reservation.getStartDate()).orElse(existingReservation.getStartDate()));
        existingReservation.setEndDate(Optional.ofNullable(reservation.getEndDate()).orElse(existingReservation.getEndDate()));
        existingReservation.setReservedBy(Optional.ofNullable(reservation.getReservedBy()).orElse(existingReservation.getReservedBy()));
        // TODO: show we allow change the room?
//        existingReservation.setRoom(reservation.getRoom());

        reservationRepository.save(existingReservation);
    }

    private Reservation findReservationById(Long id) throws ElementNotFoundException {
        return reservationRepository
                .findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Reservation not found for the given identifier: reservation=" + id));
    }
}
