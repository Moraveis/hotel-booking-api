package com.alten.altentest.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationDTO {

    private final static String DATA_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";

    private Long id;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotEmpty
    private String reservedBy;

    @Builder.Default
    private Boolean deleted = Boolean.FALSE;

    @NotNull
    private Long roomId;
}
