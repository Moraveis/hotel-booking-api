package com.alten.altentest.datatransferobject;

import com.alten.altentest.model.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDTO {

    @JsonIgnore
    private Long id;

    @NotNull
    private String number;

    @Builder.Default
    private Boolean suite = Boolean.FALSE;

    @Builder.Default
    private Boolean available = Boolean.TRUE;

    private List<Reservation> reservations;

}
