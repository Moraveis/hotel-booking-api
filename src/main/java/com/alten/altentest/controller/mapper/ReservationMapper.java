package com.alten.altentest.controller.mapper;

import com.alten.altentest.datatransferobject.ReservationDTO;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.model.Room;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationMapper {

    public static Reservation fromReservationDTO(ReservationDTO reservationDTO) {
        return Reservation.builder()
                .startDate(reservationDTO.getStartDate())
                .endDate(reservationDTO.getEndDate())
                .reservedBy(reservationDTO.getReservedBy())
                .room(Room.builder().id(reservationDTO.getRoomId()).build())
                .build();
    }

    public static ReservationDTO toReservationDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .reservedBy(reservation.getReservedBy())
                .deleted(reservation.getDeleted())
                .roomId(reservation.getRoom().getId())
                .build();
    }

    public static List<ReservationDTO> toReservationDTOList(List<Reservation> reservations) {
        return reservations.stream().map(ReservationMapper::toReservationDTO).collect(Collectors.toList());
    }
}
