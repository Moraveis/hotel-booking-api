package com.alten.altentest.controller;

import com.alten.altentest.datatransferobject.ReservationDTO;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.model.Room;
import com.alten.altentest.repository.ReservationRepository;
import com.alten.altentest.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
public class ReservationControllerIntegTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("When all Reservations are requested then they are all returned")
    void allReservationsRequested() throws Exception {
        Room room = roomRepository.findById(1L).get();
        Reservation reservation = Reservation.builder()
                .startDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .endDate(LocalDateTime.of(2021, 6, 28, 8, 0))
                .reservedBy("user")
                .room(room)
                .build();
        reservationRepository.save(reservation);

        mockMvc
                .perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("When a given ID is provided then we return the respective reservation")
    void givenIdFindReservation() throws Exception {
        Room room = roomRepository.findById(1L).get();
        Reservation reservation = Reservation.builder()
                .startDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .endDate(LocalDateTime.of(2021, 6, 28, 8, 0))
                .reservedBy("user")
                .room(room)
                .build();
        reservationRepository.save(reservation);

        mockMvc
                .perform(get("/reservations/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.startDate", equalTo("2021-06-27T08:00:00")))
                .andExpect(jsonPath("$.endDate", equalTo("2021-06-28T08:00:00")))
                .andExpect(jsonPath("$.reservedBy", equalTo("user")))
                .andExpect(jsonPath("$.deleted", equalTo(false)));
    }

    @Test
    @DisplayName("When a reservation creation is requested then it is persisted")
    void reservationCreatedCorrectly() throws Exception {
        Room room = roomRepository.findById(1L).get();

        ReservationDTO reservation = ReservationDTO.builder()
                .startDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .endDate(LocalDateTime.of(2021, 6, 28, 8, 0))
                .reservedBy("user")
                .deleted(false)
                .roomId(room.getId())
                .build();

        mockMvc
                .perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    @DisplayName("Return a NotFoundException for given identifier")
    void whenIdentifierNotFoundThenReturnNotFoundException() throws Exception {
        mockMvc
                .perform(get("/reservations/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When a reservation update is requested then it is persisted")
    void roomUpdatedCorrectly() throws Exception {
        Room room = roomRepository.findById(1L).get();
        Reservation reservation = Reservation.builder()
                .startDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .endDate(LocalDateTime.of(2021, 6, 28, 8, 0))
                .reservedBy("user")
                .room(room)
                .build();
        reservationRepository.save(reservation);

        ReservationDTO reservationDTO = ReservationDTO.builder()
                .startDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .endDate(LocalDateTime.of(2021, 6, 29, 8, 0))
                .reservedBy("user")
                .roomId(1L)
                .build();

        mockMvc
                .perform(put("/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservationDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("When a given ID is provided then we inactivate the respective reservation")
    void deleteReservationGivenID() throws Exception {
        Room room = roomRepository.findById(1L).get();
        Reservation reservation = Reservation.builder()
                .startDate(LocalDateTime.of(2021, 6, 27, 8, 0))
                .endDate(LocalDateTime.of(2021, 6, 28, 8, 0))
                .reservedBy("user")
                .room(room)
                .build();
        reservationRepository.save(reservation);

        mockMvc
                .perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());

        mockMvc
                .perform(get("/reservations/1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.deleted", equalTo(true)));
    }

}
