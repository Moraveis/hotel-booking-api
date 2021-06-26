package com.alten.altentest.datatransferobject;

import com.alten.altentest.model.Reservation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDTO {

    private Long id;

    @NotNull
    private String number;

    @Builder.Default
    private Boolean suite = Boolean.FALSE;

    @Builder.Default
    private Boolean available = Boolean.TRUE;

    private List<Reservation> reservations;

}
