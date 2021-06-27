package com.alten.altentest.controller;

import com.alten.altentest.datatransferobject.RoomDTO;
import com.alten.altentest.model.Reservation;
import com.alten.altentest.model.Room;
import com.alten.altentest.repository.ReservationRepository;
import com.alten.altentest.repository.RoomRepository;
import com.alten.altentest.util.TestUtil;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
public class RoomControllerIntegTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("When all rooms are requested then they are all returned")
    void allRoomsRequested() throws Exception {
        mockMvc
                .perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("When a given ID is provided then we return the respective room")
    void givenIdFindRoom() throws Exception {
        mockMvc
                .perform(get("/rooms/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.number", equalTo("001")))
                .andExpect(jsonPath("$.suite", equalTo(false)))
                .andExpect(jsonPath("$.available", equalTo(true)));
    }

    @Test
    @DisplayName("When a given ID is provided then we return the respective room details")
    void givenIdFindRoomDetails() throws Exception {
        Room newRoom = Room.builder().number("002").suite(true).available(true).build();
        roomRepository.save(newRoom);

        Reservation reservation = TestUtil.buildReservation(newRoom);
        reservationRepository.save(reservation);

        mockMvc
                .perform(get("/rooms/2/reservations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(2)))
                .andExpect(jsonPath("$.number", equalTo("002")))
                .andExpect(jsonPath("$.suite", equalTo(true)))
                .andExpect(jsonPath("$.available", equalTo(true)))
                .andExpect(jsonPath("$.reservations", hasSize(1)));
    }

    @Test
    @DisplayName("When a room creation is requested then it is persisted")
    void roomCreatedCorrectly() throws Exception {
        RoomDTO newRoom = RoomDTO.builder().number("002").suite(true).available(true).build();

        mockMvc
                .perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newRoom)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(2)));
    }

    @Test
    @DisplayName("When Required Info Is Not Provided Return BadRequestException")
    void whenRequiredInfoIsNotProvidedThenReturnBadRequestException() throws Exception {
        RoomDTO newRoom = RoomDTO.builder().suite(true).available(true).build();

        mockMvc
                .perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newRoom)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Return a NotFoundException for given identifier")
    void whenIdentifierNotFoundThenReturnNotFoundException() throws Exception {
        mockMvc
                .perform(get("/rooms/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When a room update is requested then it is persisted")
    void roomUpdatedCorrectly() throws Exception {
        RoomDTO roomDTO = RoomDTO.builder().number("001").suite(true).available(false).build();

        mockMvc
                .perform(put("/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(roomDTO)))
                .andExpect(status().isNoContent());

        mockMvc
                .perform(get("/rooms/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.number", equalTo("001")))
                .andExpect(jsonPath("$.suite", equalTo(true)))
                .andExpect(jsonPath("$.available", equalTo(false)));
    }

    @Test
    @DisplayName("When a room availability is requested then it is persisted")
    void roomAvailabilityUpdatedCorrectly() throws Exception {
        mockMvc
                .perform(patch("/rooms/1?availability=false"))
                .andExpect(status().isNoContent());

        mockMvc
                .perform(get("/rooms/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.number", equalTo("001")))
                .andExpect(jsonPath("$.suite", equalTo(false)))
                .andExpect(jsonPath("$.available", equalTo(false)));
    }

}
