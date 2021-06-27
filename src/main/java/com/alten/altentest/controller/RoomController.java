package com.alten.altentest.controller;

import com.alten.altentest.controller.mapper.RoomMapper;
import com.alten.altentest.datatransferobject.RoomDTO;
import com.alten.altentest.exception.ConstraintsViolationException;
import com.alten.altentest.model.Room;
import com.alten.altentest.service.impl.DefaultRoomService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {

    private final DefaultRoomService defaultRoomService;

    @ApiOperation(value = "Get all Rooms")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return a list of Rooms."),
            @ApiResponse(code = 500, message = "Something went wrong.")
    })
    @GetMapping
    public List<RoomDTO> getAllRooms() {
        return RoomMapper.toRoomDTOList(defaultRoomService.getAllRooms());
    }

    @ApiOperation(value = "Get Room by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Room found for the specified id."),
            @ApiResponse(code = 404, message = "No Room found for the specified id."),
            @ApiResponse(code = 500, message = "Something went wrong.")
    })
    @GetMapping("/{id}")
    public RoomDTO getRoomById(@PathVariable("id") Long id) {
        return RoomMapper.toRoomDTO(defaultRoomService.getRoomById(id));
    }

    @ApiOperation(value = "Get Room's reservations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Room found and displayed with its reservations"),
            @ApiResponse(code = 404, message = "No Room found for the specified id."),
            @ApiResponse(code = 500, message = "Something went wrong.")
    })
    @GetMapping("/{id}/reservations")
    public RoomDTO getReservationsForRoomId(@PathVariable("id") Long id) {
        return RoomMapper.toRoomDTOWithReservations(defaultRoomService.getRoomById(id));
    }

    @ApiOperation(value = "Get Room's reservations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Room available to be booked for the period informed"),
            @ApiResponse(code = 400, message = "Missing information provided to complete the request"),
            @ApiResponse(code = 404, message = "No Room found for the specified id."),
            @ApiResponse(code = 500, message = "Something went wrong.")
    })
    @GetMapping("/{id}/availability")
    public RoomDTO getRoomAvailabilityByReservationPeriod(@PathVariable("id") Long id,
                                                          @RequestParam("startDate") LocalDateTime startDate,
                                                          @RequestParam("endDate") LocalDateTime endDate) {

        return RoomMapper.toRoomDTOWithReservations(defaultRoomService.getRoomAvailabilityByReservationPeriod(id, startDate, endDate));
    }

    @ApiOperation(value = "Create Room")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(value = "The content type of the body provided in the request.", name = "Content-Type", paramType = "header", defaultValue = "application/json", required = true),
//            @ApiImplicitParam(value = "The content type that the client will accept.", name = "Accept", paramType = "header", defaultValue = "application/json", required = true),
//    })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Room created Successfully."),
            @ApiResponse(code = 400, message = "Fail to save Room due to constraints violations."),
            @ApiResponse(code = 500, message = "Something went wrong while processing teh request.")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public RoomDTO createRoom(@Valid @RequestBody RoomDTO roomDTO) throws ConstraintsViolationException {
        Room room = RoomMapper.fromRoomDTO(roomDTO);
        return RoomMapper.toRoomDTO(defaultRoomService.createRoom(room));
    }

    @ApiOperation(value = "Update Room information")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(value = "The content type of the body provided in the request.", name = "Content-Type", paramType = "header", defaultValue = "application/json", required = true),
//            @ApiImplicitParam(value = "The content type that the client will accept.", name = "Accept", paramType = "header", defaultValue = "application/json", required = true),
//    })
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Room found and updated."),
            @ApiResponse(code = 400, message = "Invalid or missing information provided."),
            @ApiResponse(code = 404, message = "No Room found for the specified id."),
            @ApiResponse(code = 500, message = "Something went wrong.")
    })
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRoom(@PathVariable("id") Long id, @Valid @RequestBody RoomDTO roomDTO) {
        Room room = RoomMapper.fromRoomDTO(roomDTO);
        defaultRoomService.updateRoom(id, room);
    }

    @ApiOperation(value = "Update Room information")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Room found and updated."),
            @ApiResponse(code = 400, message = "Invalid or missing information provided."),
            @ApiResponse(code = 404, message = "No Room found for the specified id."),
            @ApiResponse(code = 500, message = "Something went wrong.")
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRoomAvailability(@PathVariable("id") Long id, @RequestParam("availability") Boolean availability) {
        defaultRoomService.updateRoomAvailability(id, availability);
    }

}
