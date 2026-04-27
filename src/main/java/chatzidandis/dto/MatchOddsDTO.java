package chatzidandis.dto;

import chatzidandis.enums.Specifier;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatchOddsDTO {
    private Long id;
    private Long matchId;

    @NotNull
    private Specifier specifier;

    @DecimalMin("0.10")
    @DecimalMax("10000.00")
    @Digits(integer = 5, fraction = 2)
    private BigDecimal odd;
}
