package com.alten.altentest.controller;

import com.alten.altentest.datatransferobject.ReservationDTO;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.model.Room;
import com.alten.altentest.repository.ReservationRepository;
import com.alten.altentest.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

import static com.alten.altentest.util.TestUtil.buildReservation;
import static com.alten.altentest.util.TestUtil.buildReservationDTO;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Reservation reservation = buildReservation(room);
        reservationRepository.save(reservation);

        mockMvc
                .perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // TODO: fix assertions
    @Test
    @DisplayName("When a given ID is provided then we return the respective reservation")
    void givenIdFindReservation() throws Exception {
        Room room = roomRepository.findById(1L).get();
        Reservation reservation = buildReservation(room);
        Reservation savedReservation = reservationRepository.save(reservation);

        ReservationDTO result =
                mapper.readValue(
                        mockMvc.perform(get("/reservations/1"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        ReservationDTO.class);

        assertEquals(savedReservation.getId(), result.getId());
        assertEquals(savedReservation.getStartDate(), result.getStartDate());
        assertEquals(savedReservation.getEndDate(), result.getEndDate());
        assertEquals(savedReservation.getReservedBy(), result.getReservedBy());
        assertEquals(savedReservation.getDeleted(), result.getDeleted());
        assertEquals(savedReservation.getRoom().getId(), result.getRoomId());


//        mockMvc
//                .perform(get("/reservations/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", equalTo(savedReservation.getId().intValue())))
//                .andExpect(jsonPath("$.startDate", equalTo(String.valueOf(savedReservation.getStartDate()))))
//                .andExpect(jsonPath("$.endDate", equalTo(String.valueOf(savedReservation.getEndDate()))))
//                .andExpect(jsonPath("$.reservedBy", equalTo(savedReservation.getReservedBy())))
//                .andExpect(jsonPath("$.deleted", equalTo(savedReservation.getDeleted())));
    }

    @Test
    @DisplayName("When a reservation creation is requested then it is persisted")
    void reservationCreatedCorrectly() throws Exception {
        ReservationDTO reservationDTO = buildReservationDTO();

        mockMvc
                .perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    @DisplayName("Given reservation StartDate is for the same Day then we return BadRequest When creating reservation")
    void whenRequestDataHasInvalidStartDateThenReturnBadRequestException() throws Exception {
        ReservationDTO reservationDTO = buildReservationDTO();
        reservationDTO.setStartDate(LocalDateTime.now().plusHours(1));

        mockMvc
                .perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When reservation is done for more than 30Days Then return BadRequestException")
    void whenReservationDoneForMoreThan30DaysThenReturnBadRequestException() throws Exception {
        ReservationDTO reservationDTO = buildReservationDTO();
        reservationDTO.setStartDate(reservationDTO.getStartDate().plusDays(31));

        mockMvc
                .perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When reservation duration is more than 3Days Then return BadRequestException")
    void whenReservationDurationIsMoreThan3DaysThenReturnBadRequestException() throws Exception {
        ReservationDTO reservationDTO = buildReservationDTO();
        reservationDTO.setEndDate(reservationDTO.getStartDate().plusDays(4));

        mockMvc
                .perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservationDTO)))
                .andExpect(status().isBadRequest());
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
    void reservationUpdatedCorrectly() throws Exception {
        Room room = roomRepository.findById(1L).get();

        Reservation reservation = buildReservation(room);
        reservationRepository.save(reservation);

        ReservationDTO reservationDTO = buildReservationDTO();
        reservationDTO.setEndDate(LocalDateTime.of(2021, 6, 29, 8, 0));

        mockMvc
                .perform(put("/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservationDTO)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("When a reservation update is requested with Overlap data then throw a BadRequestException")
    void reservationUpdatedBadRequest() throws Exception {
        Room room = roomRepository.findById(1L).get();

        Reservation reservation = buildReservation(room);
        reservationRepository.save(reservation);

        ReservationDTO reservationDTO = buildReservationDTO();
        reservationDTO.setStartDate(LocalDateTime.of(2021, 7, 5, 8, 0));
        reservationDTO.setEndDate(LocalDateTime.of(2021, 7, 9, 8, 0));

        mockMvc
                .perform(put("/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservationDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When a given ID is provided then we inactivate the respective reservation")
    void deleteReservationGivenID() throws Exception {
        Room room = roomRepository.findById(1L).get();
        Reservation reservation = buildReservation(room);
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
