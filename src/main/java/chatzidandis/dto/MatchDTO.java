package chatzidandis.dto;

import chatzidandis.enums.Sport;
import chatzidandis.validation.ValidMatch;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@ValidMatch
public class MatchDTO {
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate matchDate;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime matchTime;

    @NotBlank
    private String teamA;

    @NotBlank
    private String teamB;

    @NotNull
    private Sport sport;

    private List<MatchOddsDTO> odds;
}
