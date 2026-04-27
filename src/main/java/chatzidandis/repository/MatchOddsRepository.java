package chatzidandis.repository;

import chatzidandis.entity.MatchOddsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchOddsRepository extends JpaRepository<MatchOddsEntity, Long> {
    List<MatchOddsEntity> findByMatchId(Long matchId);
}
