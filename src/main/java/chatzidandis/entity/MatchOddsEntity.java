package chatzidandis.entity;

import chatzidandis.converter.SpecifierConverter;
import chatzidandis.enums.Specifier;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "match_odds")
@Data
public class MatchOddsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = SpecifierConverter.class)
    private Specifier specifier;

    private BigDecimal odd;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private MatchEntity match;
}
