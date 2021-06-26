package com.alten.altentest.util;

import com.alten.altentest.datatransferobject.ReservationDTO;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.model.Room;

import java.time.LocalDateTime;

public class TestUtil {

    public static Reservation buildReservation(Room room) {
        return Reservation.builder()
                .startDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .endDate(LocalDateTime.of(2021, 6, 28, 8, 0))
                .reservedBy("user")
                .deleted(false)
                .room(room)
                .build();
    }

    public static ReservationDTO buildReservationDTO() {
        return ReservationDTO.builder()
                .startDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .endDate(LocalDateTime.of(2021, 6, 28, 8, 0))
                .reservedBy("user")
                .deleted(false)
                .roomId(1L)
                .build();
    }

    public static Room buildRoom() {
        return Room.builder().id(1L).available(true).number("001").suite(false).build();
    }
}
