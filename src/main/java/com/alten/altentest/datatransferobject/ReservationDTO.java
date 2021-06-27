package com.alten.altentest.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = DATA_FORMAT)
    private LocalDateTime startDate;

    @NotNull
    @Pattern(regexp = DATA_FORMAT)
    private LocalDateTime endDate;

    @NotNull
    private String reservedBy;

    @Builder.Default
    private Boolean deleted = Boolean.FALSE;

    @NotNull
    private Long roomId;
}
