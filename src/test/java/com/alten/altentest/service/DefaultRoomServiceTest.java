package com.alten.altentest.service;

import com.alten.altentest.exception.BadRequestException;
import com.alten.altentest.exception.ConstraintsViolationException;
import com.alten.altentest.exception.ElementNotFoundException;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.model.Room;
import com.alten.altentest.repository.RoomRepository;
import com.alten.altentest.service.impl.DefaultRoomService;
import com.alten.altentest.util.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.alten.altentest.util.TestUtil.buildReservation;
import static com.alten.altentest.util.TestUtil.buildRoom;
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
        List<Room> rooms = Collections.singletonList(buildRoom());

        when(repository.findAll()).thenReturn(rooms);

        assertEquals(service.getAllRooms().size(), 1);
    }

    @Test
    public void givenRoomIdentifierThenReturnEntity() {
        Room room = buildRoom();

        when(repository.findById(anyLong())).thenReturn(Optional.of(room));

        Room returned = service.getRoomById(1L);

        assertNotNull(returned);
        assertEquals(room, returned);
    }

    @Test
    public void givenRoomEntityThenSuccessfullySaved() throws ConstraintsViolationException {
        Room room = buildRoom();

        when(repository.save(room)).thenReturn(room);

        Room savedRoom = service.createRoom(room);

        assertNotNull(savedRoom);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void shouldThrowExceptionWhenViolationConstraints() throws ConstraintsViolationException {
        Room room = buildRoom();

        when(repository.save(any())).thenThrow(DataIntegrityViolationException.class);

        service.createRoom(room);
    }

    @Test(expected = ElementNotFoundException.class)
    public void shouldThrowExceptionNoEntityForId() {
        when(repository.findById(anyLong())).thenThrow(ElementNotFoundException.class);

        service.getRoomById(1L);
    }

    @Test
    public void givenAExistingRoomEntityThenUpdateRecord() {
        Room roomToUpdate = buildRoom();
        roomToUpdate.setAvailable(false);

        Room existingRoom = buildRoom();
        existingRoom.setNumber("002");

        when(repository.findById(1L)).thenReturn(Optional.ofNullable(existingRoom));
        when(repository.save(any())).thenReturn(existingRoom);

        service.updateRoom(1L, roomToUpdate);
    }

    @Test(expected = ElementNotFoundException.class)
    public void shouldThrowExceptionWhenUpdatingNoPersistedEntity() {
        Room roomToUpdate = buildRoom();
        roomToUpdate.setAvailable(false);

        service.updateRoom(1L, roomToUpdate);
    }

    @Test
    public void givenIdAndRoomStatusThenUpdateRecord() {
        Room existingRoom = buildRoom();

        when(repository.findById(any())).thenReturn(Optional.ofNullable(existingRoom));
        assert existingRoom != null;

        when(repository.save(existingRoom)).thenReturn(existingRoom);

        service.updateRoomAvailability(1L, false);
    }

    @Test
    public void givenRoomIdAndReservationPeriodThenReturnAvailableRoom() {
        Room room = buildRoom();
        LocalDateTime startDate = TestUtil.buildStartDate(1L);
        LocalDateTime endDate = startDate.plusDays(3);

        when(repository.findById(1L)).thenReturn(Optional.ofNullable(room));

        Room savedRoom = service.getRoomAvailabilityByReservationPeriod(1L, startDate, endDate);
    }

    @Test(expected = BadRequestException.class)
    public void givenRoomIdAndReservationPeriodThenThrowExceptionWhenOverlapReservationExists() {
        Room room = buildRoom();
        Reservation reservation = buildReservation(null);
        room.setReservations(Collections.singletonList(reservation));

        LocalDateTime startDate = TestUtil.buildStartDate(2L);
        LocalDateTime endDate = startDate.plusDays(3);

        when(repository.findById(1L)).thenReturn(Optional.ofNullable(room));

        Room savedRoom = service.getRoomAvailabilityByReservationPeriod(1L, startDate, endDate);
    }


}
