package com.alten.altentest.util;

import com.alten.altentest.datatransferobject.ReservationDTO;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.model.Room;

import java.time.LocalDateTime;

public class TestUtil {

    public static Reservation buildReservation(Room room) {
        LocalDateTime dateTime = buildStartDate(1L);

        return Reservation.builder()
                .startDate(dateTime)
                .endDate(dateTime.plusDays(3))
                .reservedBy("user")
                .deleted(false)
                .room(room)
                .build();
    }

    public static ReservationDTO buildReservationDTO() {
        LocalDateTime dateTime = buildStartDate(1L);

        return ReservationDTO.builder()
                .startDate(dateTime)
                .endDate(dateTime.plusDays(3))
                .reservedBy("user")
                .deleted(false)
                .roomId(1L)
                .build();
    }

    public static Room buildRoom() {
        return Room.builder()
                .id(1L)
                .available(true)
                .number("001")
                .suite(false)
                .build();
    }

    public static LocalDateTime buildStartDate(Long daysToAdd) {
        return LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).plusDays(daysToAdd);
    }
}
