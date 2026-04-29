package chatzidandis.service;

import chatzidandis.dto.MatchDTO;
import chatzidandis.dto.MatchOddsDTO;
import chatzidandis.entity.MatchEntity;
import chatzidandis.enums.Specifier;
import chatzidandis.enums.Sport;
import chatzidandis.mapper.MatchMapper;
import chatzidandis.mapper.MatchOddsMapper;
import chatzidandis.repository.MatchRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MatchService {

    private final MatchRepository repository;
    private final MatchMapper matchMapper;
    private final MatchOddsMapper matchOddsMapper;

    public MatchService(MatchRepository repository, MatchMapper matchMapper,
                    MatchOddsMapper matchOddsMapper) {
        this.repository = repository;
        this.matchMapper = matchMapper;
        this.matchOddsMapper = matchOddsMapper;
    }

    //create a new match - odds are optional
    @Transactional
    public MatchDTO create(MatchDTO dto) {
        validateMatchExists(dto);
        MatchEntity entity = matchMapper.toEntity(dto);
        return matchMapper.toDTO(repository.save(entity));
    }

    //create, update odds
    @Transactional
    public List<MatchOddsDTO> odds(Long matchId, List<MatchOddsDTO> oddsDtos) {
        if (oddsDtos == null || oddsDtos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                            String.format("Odds list can not be empty for match id %d", matchId));
        }
        MatchDTO matchDTO = getMatchDTOById(matchId);
        if (matchDTO.getOdds() == null ||  matchDTO.getOdds().isEmpty()) {
            matchDTO.setOdds(new ArrayList<>());
        }

        Map<Specifier, MatchOddsDTO> existingOddsMap = matchDTO.getOdds()
                        .stream()
                        .collect(Collectors.toMap(
                                        MatchOddsDTO::getSpecifier,
                                        o -> o
                        ));


        for (MatchOddsDTO incoming : oddsDtos) {
            incoming.setMatchId(matchId);

            //Basketball matches can not have odds for DRAW
            if (matchDTO.getSport().equals(Sport.BASKETBALL) && incoming.getSpecifier().equals(Specifier.DRAW)) {
                log.warn("Basketball match id {} can not have odd for DRAW", matchId);
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                                String.format("Basketball match id %d can not have odd for DRAW", matchId));
            }

            if (existingOddsMap.containsKey(incoming.getSpecifier())) {
                MatchOddsDTO existing = existingOddsMap.get(incoming.getSpecifier());
                existing.setOdd(incoming.getOdd());
            } else {
                matchDTO.getOdds().add(incoming);
            }
        }

        repository.save(matchMapper.toEntity(matchDTO));

        return matchDTO.getOdds();
    }

    //update match info by id
    @Transactional
    public MatchDTO update(Long id, MatchDTO dto) {

        MatchEntity existing = findMatchEntityById(id);

        existing.setDescription(dto.getDescription());
        existing.setMatchDate(dto.getMatchDate());
        existing.setMatchTime(dto.getMatchTime());
        existing.setTeamA(dto.getTeamA());
        existing.setTeamB(dto.getTeamB());
        existing.setSport(dto.getSport());

        return matchMapper.toDTO(repository.save(existing));
    }

    //delete match by id
    @Transactional
    public void delete(Long id) {
        MatchEntity entity = findMatchEntityById(id);
        repository.deleteById(id);
    }

    private void validateMatchExists(MatchDTO dto) {
        Optional<MatchEntity> exists = repository.findByTeamAAndTeamBAndMatchDateAndSport(
                        dto.getTeamA(),
                        dto.getTeamB(),
                        dto.getMatchDate(),
                        dto.getSport()
        );
        if (exists.isPresent()) {
            log.warn("Match already exists for {} vs {} on {} for {}",
                            dto.getTeamA(), dto.getTeamB(), dto.getMatchDate(), dto.getSport());
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                            String.format("Match already exists for %s vs %s on %s for %s",
                                            dto.getTeamA(), dto.getTeamB(), dto.getMatchDate(), dto.getSport()));

        }
    }

    //find match by id
    public MatchDTO getMatchDTOById(Long id) {
        MatchEntity match = findMatchEntityById(id);
        return matchMapper.toDTO(match);
    }

    //find odds by match id
    public List<MatchOddsDTO> findOddsByMatchId(Long matchId) {
        MatchEntity match = findMatchEntityById(matchId);

        if (match.getOdds() == null || match.getOdds().isEmpty()) {
            return List.of();
        }

        return match.getOdds()
                        .stream()
                        .map(matchOddsMapper::toDTO)
                        .toList();

    }

    private MatchEntity findMatchEntityById(Long matchId) {
        return repository.findById(matchId)
                        .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Match id %d not found", matchId)
                        ));
    }
}
