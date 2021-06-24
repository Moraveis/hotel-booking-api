package com.alten.altentest.service;

import com.alten.altentest.model.Reservation;
import com.alten.altentest.repository.ReservationRepository;
import com.alten.altentest.service.impl.DefaultReservationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class DefaultReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private DefaultReservationService defaultReservationService;

    @Test
    public void shouldReturnAllReservations() {
        Reservation reservation = Reservation.builder()
                .id(1L)
                .startDate(LocalDateTime.of(2021, 6, 25, 10, 0))
                .endDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .deleted(false)
                .reservedBy("Joao Moraveis")
                .build();

        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        List<Reservation> result = defaultReservationService.getAllReservation();

        assertEquals(1L, result.size());
    }

    @Test
    public void givenReservationIdThenReturnEntitySuccessfully() {
        Reservation reservation = Reservation.builder()
                .id(1L)
                .startDate(LocalDateTime.of(2021, 6, 25, 10, 0))
                .endDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .deleted(false)
                .reservedBy("Joao Moraveis")
                .build();

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
        Reservation reservation = Reservation.builder()
                .startDate(LocalDateTime.of(2021, 6, 25, 10, 0))
                .endDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .deleted(false)
                .reservedBy("Joao Moraveis")
                .build();

        when(reservationRepository.save(any())).thenReturn(reservation);

        defaultReservationService.createReservation(reservation);
    }

    @Test
    public void givenReservationIdThenDeleteSuccessfully() {
        Reservation reservation = Reservation.builder()
                .startDate(LocalDateTime.of(2021, 6, 25, 10, 0))
                .endDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .deleted(true)
                .reservedBy("Joao Moraveis")
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));
        when(reservationRepository.save(any())).thenReturn(reservation);

        defaultReservationService.deleteReservation(1L);
    }


}
