package com.alten.altentest.util;

import com.alten.altentest.model.Reservation;

import java.time.LocalDateTime;

public class ReservationUtil {

    public static boolean isWithinRange(LocalDateTime startDate, LocalDateTime endDate, Reservation reservation) {
        return (startDate.compareTo(reservation.getStartDate()) >= 0 && startDate.compareTo(reservation.getEndDate()) <= 0)
                || (endDate.compareTo(reservation.getStartDate()) >= 0 && endDate.compareTo(reservation.getEndDate()) <= 0);
    }
}
