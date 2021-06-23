package com.alten.altentest.service;

import com.alten.altentest.model.Room;
import com.alten.altentest.repository.RoomRepository;
import com.alten.altentest.service.impl.DefaultRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class DefaultRoomServiceTest {

    @InjectMocks
    private DefaultRoomService service;

    @Mock
    private RoomRepository repository;

    @Test
    @DisplayName("Return all rooms persisted in the system.")
    public void shouldReturnAllRooms() {
        List<Room> rooms = Collections.singletonList(new Room());

        when(repository.findAll()).thenReturn(rooms);

        assertEquals(service.getAllRooms().size(), 1);
    }

    @Test
    @DisplayName("Given an Identifier then return respective entity.")
    public void givenRoomIdentifierThenReturnEntity() {
        Room room = Room.builder().id(1L).available(true).number("001").suite(false).build();

        when(repository.findById(anyLong())).thenReturn(Optional.of(room));

        Room returned = service.getRoomById(1L);

        assertNotNull(returned);
        assertEquals(room, returned);
    }

    @Test
    @DisplayName("Given an entity then successfully save it.")
    public void givenRoomEntityThenSuccessfullySaved() {
        Room room = Room.builder().id(1L).available(true).number("001").suite(false).build();

        when(repository.save(room)).thenReturn(room);

        Room savedRoom = service.createRoom(room);

        assertNotNull(savedRoom);
    }


}
