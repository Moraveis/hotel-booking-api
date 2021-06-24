package com.alten.altentest.service;

import com.alten.altentest.model.Reservation;

import java.util.List;

public interface ReservationService {

    List<Reservation> getAllReservation();

    Reservation getReservationById(Long id);

    Reservation createReservation(Reservation reservation);

    void deleteReservation(Long id);

    void updateReservation(Reservation reservation);

}
