package chatzidandis.mapper;

import chatzidandis.dto.MatchDTO;
import chatzidandis.entity.MatchEntity;
import chatzidandis.entity.MatchOddsEntity;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    private final MatchOddsMapper oddsMapper;

    public MatchMapper(MatchOddsMapper oddsMapper) {
        this.oddsMapper = oddsMapper;
    }

    public MatchDTO toDTO(MatchEntity entity) {
        MatchDTO dto = new MatchDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setMatchDate(entity.getMatchDate());
        dto.setMatchTime(entity.getMatchTime());
        dto.setTeamA(entity.getTeamA());
        dto.setTeamB(entity.getTeamB());
        dto.setSport(entity.getSport());

        if (entity.getOdds() != null) {
            dto.setOdds(entity.getOdds()
                            .stream()
                            .map(oddsMapper::toDTO)
                            .toList());
        }

        return dto;
    }

    public MatchEntity toEntity(MatchDTO dto) {
        MatchEntity entity = new MatchEntity();

        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());
        entity.setMatchDate(dto.getMatchDate());
        entity.setMatchTime(dto.getMatchTime());
        entity.setTeamA(dto.getTeamA());
        entity.setTeamB(dto.getTeamB());
        entity.setSport(dto.getSport());

        if (dto.getOdds() != null) {

            entity.setOdds(
                dto.getOdds()
                    .stream()
                    .map(o -> {
                        MatchOddsEntity odds = new MatchOddsEntity();
                        odds.setId(o.getId());
                        odds.setSpecifier(o.getSpecifier());
                        odds.setOdd(o.getOdd());
                        odds.setMatch(entity);
                        return odds;
                    })
                    .toList()
            );
        }

        return entity;
    }
}
