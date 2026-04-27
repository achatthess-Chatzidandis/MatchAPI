package chatzidandis.entity;

import chatzidandis.converter.SportConverter;
import chatzidandis.enums.Sport;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "matches")
@Data
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "match_date")
    private LocalDate matchDate;

    @Column(name = "match_time")
    private LocalTime matchTime;

    @Column(name = "team_a", length = 50)
    private String teamA;

    @Column(name = "team_b" , length = 50)
    private String teamB;

    @Convert(converter = SportConverter.class)
    private Sport sport;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchOddsEntity> odds;
}
