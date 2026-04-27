package chatzidandis.repository;

import chatzidandis.entity.MatchEntity;
import chatzidandis.enums.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    boolean existsById(Long id);

    Optional<MatchEntity> findByTeamAAndTeamBAndMatchDateAndSport(
                    String teamA,
                    String teamB,
                    LocalDate matchDate,
                    Sport sport
    );
}
