package com.alten.altentest.controller.mapper;

import com.alten.altentest.datatransferobject.RoomDTO;
import com.alten.altentest.model.Room;

import java.util.List;
import java.util.stream.Collectors;

public class RoomMapper {

    public static Room fromRoomDTO(RoomDTO roomDTO) {
        return Room.builder()
                .available(roomDTO.getAvailable())
                .number(roomDTO.getNumber())
                .suite(roomDTO.getSuite())
                .build();
    }

    public static RoomDTO toRoomDTO(Room room) {
        // TODO: add list of reservations from now() to future
        return RoomDTO.builder()
                .id(room.getId())
                .available(room.getAvailable())
                .number(room.getNumber())
                .suite(room.getSuite())
                .build();
    }

    public static List<RoomDTO> toRoomDTOList(List<Room> rooms) {
        return rooms.stream().map(RoomMapper::toRoomDTO).collect(Collectors.toList());
    }

}
