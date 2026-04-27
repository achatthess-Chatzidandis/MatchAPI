package chatzidandis.mapper;

import chatzidandis.dto.MatchOddsDTO;
import chatzidandis.entity.MatchEntity;
import chatzidandis.entity.MatchOddsEntity;
import org.springframework.stereotype.Component;

@Component
public class MatchOddsMapper {

    public MatchOddsDTO toDTO(MatchOddsEntity entity) {
        MatchOddsDTO dto = new MatchOddsDTO();
        dto.setId(entity.getId());
        dto.setMatchId(entity.getMatch().getId());
        dto.setSpecifier(entity.getSpecifier());
        dto.setOdd(entity.getOdd());
        return dto;
    }

    public MatchOddsEntity toEntity(MatchOddsDTO dto, MatchEntity match) {
        MatchOddsEntity entity = new MatchOddsEntity();
        entity.setId(dto.getId());
        entity.setMatch(match);
        entity.setSpecifier(dto.getSpecifier());
        entity.setOdd(dto.getOdd());
        return entity;
    }
}
