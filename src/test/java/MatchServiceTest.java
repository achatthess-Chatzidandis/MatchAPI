import chatzidandis.dto.MatchDTO;
import chatzidandis.dto.MatchOddsDTO;
import chatzidandis.entity.MatchEntity;
import chatzidandis.enums.Sport;
import chatzidandis.mapper.MatchMapper;
import chatzidandis.repository.MatchRepository;
import chatzidandis.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MatchServiceTest {

    @Mock
    private MatchRepository repository;

    @Mock
    private MatchMapper mapper;

    @InjectMocks
    private MatchService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSaveMatch_whenNotExists() {

        MatchDTO dto = new MatchDTO();
        dto.setTeamA("OSFP");
        dto.setTeamB("PAO");
        dto.setMatchDate(LocalDate.now());
        dto.setSport(Sport.FOOTBALL);

        MatchEntity entity = new MatchEntity();

        when(repository.findByTeamAAndTeamBAndMatchDateAndSport(
                        any(), any(), any(), any()
        )).thenReturn(Optional.empty());

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        MatchDTO result = service.create(dto);

        assertNotNull(result);
        verify(repository).save(entity);
    }

    @Test
    void create_shouldThrowException_whenMatchExists() {

        MatchDTO dto = new MatchDTO();
        dto.setTeamA("OSFP");
        dto.setTeamB("PAO");
        dto.setMatchDate(LocalDate.now());
        dto.setSport(Sport.FOOTBALL);

        when(repository.findByTeamAAndTeamBAndMatchDateAndSport(
                        any(), any(), any(), any()
        )).thenReturn(Optional.of(new MatchEntity()));

        assertThrows(ResponseStatusException.class,
                        () -> service.create(dto));
    }

    @Test
    void odds_shouldThrowException_whenBasketballHasDraw() {

        Long matchId = 1L;

        MatchDTO match = new MatchDTO();
        match.setSport(Sport.BASKETBALL);

        MatchOddsDTO odd = new MatchOddsDTO();
        odd.setSpecifier(chatzidandis.enums.Specifier.DRAW);

        when(repository.existsById(matchId)).thenReturn(true);
        when(repository.findById(matchId)).thenReturn(Optional.of(new MatchEntity()));
        when(mapper.toDTO(any())).thenReturn(match);

        assertThrows(ResponseStatusException.class,
                        () -> service.odds(matchId, List.of(odd)));
    }

    @Test
    void findById_shouldReturnMatch() {

        MatchEntity entity = new MatchEntity();
        MatchDTO dto = new MatchDTO();

        when(repository.existsById(1L)).thenReturn(true);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        MatchDTO result = service.findById(1L);

        assertNotNull(result);
    }

    @Test
    void delete_shouldCallRepository() {

        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

}
