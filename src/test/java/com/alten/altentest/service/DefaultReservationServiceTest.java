package com.alten.altentest.service;

import com.alten.altentest.model.Reservation;
import com.alten.altentest.model.Room;
import com.alten.altentest.repository.ReservationRepository;
import com.alten.altentest.repository.RoomRepository;
import com.alten.altentest.service.impl.DefaultReservationService;
import com.alten.altentest.util.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.alten.altentest.util.TestUtil.buildRoom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class DefaultReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private DefaultReservationService defaultReservationService;

    @Test
    public void shouldReturnAllReservations() {
        Room room = buildRoom();
        Reservation reservation = TestUtil.buildReservation(room);

        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        List<Reservation> result = defaultReservationService.getAllReservation();

        assertEquals(1L, result.size());
    }

    @Test
    public void givenReservationIdThenReturnEntitySuccessfully() {
        Room room = buildRoom();
        Reservation reservation = TestUtil.buildReservation(room);

        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));

        Reservation result = defaultReservationService.getReservationById(1L);

        assertNotNull(result);
    }

    @Test(expected = EntityNotFoundException.class)
    public void givenReservationIdThenThrowExceptionWhenNotFound() {
        when(reservationRepository.findById(1L)).thenThrow(EntityNotFoundException.class);

        defaultReservationService.getReservationById(1L);
    }

    @Test
    public void givenValidReservationObjectThenCreateSuccessfully() {
        Room room = buildRoom();
        Reservation reservation = TestUtil.buildReservation(room);

        when(roomRepository.findById(1L)).thenReturn(Optional.ofNullable(room));
        when(reservationRepository.save(any())).thenReturn(reservation);

        defaultReservationService.createReservation(reservation);
    }

    @Test
    public void givenReservationIdThenDeleteSuccessfully() {
        Room room = buildRoom();
        Reservation reservation = TestUtil.buildReservation(room);

        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));
        when(reservationRepository.save(any())).thenReturn(reservation);

        defaultReservationService.deleteReservation(1L);
    }

}
