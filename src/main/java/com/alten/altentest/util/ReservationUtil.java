package com.alten.altentest.util;

import com.alten.altentest.exception.BadRequestException;
import com.alten.altentest.model.Reservation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReservationUtil {

    public static boolean isWithinRange(LocalDateTime startDate, LocalDateTime endDate, Reservation reservation) {
        return (startDate.compareTo(reservation.getStartDate()) >= 0 && startDate.compareTo(reservation.getEndDate()) <= 0)
                || (endDate.compareTo(reservation.getStartDate()) >= 0 && endDate.compareTo(reservation.getEndDate()) <= 0);
    }

    public static boolean isOverlappingDates(List<Reservation> activeReservations, Reservation reservation) {
        return isOverlappingDates(activeReservations, reservation.getStartDate(), reservation.getEndDate());
    }

    public static boolean isOverlappingDates(List<Reservation> activeReservations, LocalDateTime startDate, LocalDateTime endDate) {
        if (activeReservations == null) {
            return false;
        }

        return activeReservations
                .stream()
                .filter(r -> !r.getDeleted())
                .anyMatch(r -> isWithinRange(startDate, endDate, r));
    }

    public static void validateReservationRequest(Reservation reservation) {
        LocalDateTime createDate = LocalDateTime.now();

        if (reservation.getStartDate().toLocalDate().isBefore(createDate.toLocalDate())
                || reservation.getStartDate().toLocalDate().isEqual(createDate.toLocalDate())) {
            throw new BadRequestException("A Reservation must be done at least with one day in advance.");
        }

        if (ChronoUnit.DAYS.between(createDate, reservation.getStartDate()) > 30) {
            throw new BadRequestException("A Reservation cannot be done with more 30 days in advance.");
        }

        if (ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate()) > 3) {
            throw new BadRequestException("A Reservation can have a duration great than 3 days");
        }

        if (isOverlappingDates(reservation.getRoom().getReservations(), reservation)) {
            throw new BadRequestException("Another Reservation has already booked this period. Please try a different date.");
        }
    }
}
