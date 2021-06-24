package com.alten.altentest.service;

import com.alten.altentest.exception.BadRequestException;
import com.alten.altentest.exception.ElementNotFoundException;
import com.alten.altentest.model.Room;
import com.alten.altentest.repository.RoomRepository;
import com.alten.altentest.service.impl.DefaultRoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultRoomServiceTest {

    @InjectMocks
    private DefaultRoomService service;

    @Mock
    private RoomRepository repository;

    @Test
    public void shouldReturnAllRooms() {
        List<Room> rooms = Collections.singletonList(new Room());

        when(repository.findAll()).thenReturn(rooms);

        assertEquals(service.getAllRooms().size(), 1);
    }

    @Test
    public void givenRoomIdentifierThenReturnEntity() {
        Room room = Room.builder().id(1L).available(true).number("001").suite(false).build();

        when(repository.findById(anyLong())).thenReturn(Optional.of(room));

        Room returned = service.getRoomById(1L);

        assertNotNull(returned);
        assertEquals(room, returned);
    }

    @Test
    public void givenRoomEntityThenSuccessfullySaved() {
        Room room = Room.builder().available(true).number("001").suite(false).build();

        when(repository.save(room)).thenReturn(room);

        Room savedRoom = service.createRoom(room);

        assertNotNull(savedRoom);
    }

    @Test(expected = ElementNotFoundException.class)
    public void shouldThrowExceptionNoEntityForId() {
        when(repository.findById(anyLong())).thenThrow(ElementNotFoundException.class);

        service.getRoomById(1L);
    }

    @Test
    public void givenAExistingRoomEntityThenUpdateRecord() {
        Room existingRoom = Room.builder().id(1L).available(true).number("002").suite(false).build();

        when(repository.save(any())).thenReturn(existingRoom);

        service.updateRoom(existingRoom);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowExceptionWhenUpdatingNoPersistedEntity() {
        Room room = Room.builder().available(true).build();

        service.updateRoom(room);
    }

    @Test
    public void givenIdAndRoomStatusThenUpdateRecord() {
        Room existingRoom = Room.builder().id(1L).available(true).number("001").suite(false).build();

        when(repository.findById(any())).thenReturn(Optional.ofNullable(existingRoom));
        when(repository.save(existingRoom)).thenReturn(existingRoom);

        service.updateRoomAvailability(1L, false);
    }


}
