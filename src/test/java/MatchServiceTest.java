

import chatzidandis.dto.MatchDTO;
import chatzidandis.dto.MatchOddsDTO;
import chatzidandis.entity.MatchEntity;
import chatzidandis.enums.Specifier;
import chatzidandis.enums.Sport;
import chatzidandis.mapper.MatchMapper;
import chatzidandis.mapper.MatchOddsMapper;
import chatzidandis.repository.MatchRepository;
import chatzidandis.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository repository;

    @Mock
    private MatchMapper matchMapper;

    @Mock
    private MatchOddsMapper matchOddsMapper;

    @InjectMocks
    private MatchService service;

    private MatchEntity entity;
    private MatchDTO dto;

    @BeforeEach
    void setup() {
        entity = buildEntity();
        dto = buildDTO();
    }

    // ---------------- CREATE ----------------
    @Test
    void shouldCreateMatch() {
        when(matchMapper.toEntity(any())).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(matchMapper.toDTO(any())).thenReturn(dto);

        MatchDTO result = service.create(dto);

        assertThat(result).isNotNull();
        verify(repository).save(any());
    }

    // ---------------- FIND BY ID ----------------
    @Test
    void shouldReturnMatchById() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(matchMapper.toDTO(entity)).thenReturn(dto);

        MatchDTO result = service.getMatchDTOById(1L);

        assertThat(result).isNotNull();
        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowWhenMatchNotFound() {
        assertThatThrownBy(() -> service.getMatchDTOById(1L))
                        .isInstanceOf(ResponseStatusException.class);
    }

    // ---------------- UPDATE ----------------
    @Test
    void shouldUpdateMatch() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);
        when(matchMapper.toDTO(any())).thenReturn(dto);

        MatchDTO result = service.update(1L, dto);

        assertThat(result).isNotNull();
        verify(repository).save(any());
    }

    // ---------------- DELETE ----------------
    @Test
    void shouldDeleteMatch() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    // ---------------- ODDS ----------------
    @Test
    void shouldAddOddsToMatch() {
        MatchOddsDTO oddsDTO = new MatchOddsDTO();
        oddsDTO.setSpecifier(Specifier.HOME);
        oddsDTO.setOdd(BigDecimal.valueOf(1.5));

        dto.setOdds(List.of(oddsDTO));
        dto.setSport(Sport.FOOTBALL);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(matchMapper.toDTO(entity)).thenReturn(dto);
        when(repository.save(any())).thenReturn(entity);

        List<MatchOddsDTO> result = service.odds(1L, List.of(oddsDTO));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    void shouldRejectOddsForBasketballDraw() {
        dto.setSport(Sport.BASKETBALL);

        MatchOddsDTO oddsDTO = new MatchOddsDTO();
        oddsDTO.setSpecifier(Specifier.DRAW);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(matchMapper.toDTO(entity)).thenReturn(dto);

        assertThatThrownBy(() -> service.odds(1L, List.of(oddsDTO)))
                        .isInstanceOf(ResponseStatusException.class);
    }

    // ---------------- HELPERS ----------------
    private MatchEntity buildEntity() {
        MatchEntity e = new MatchEntity();
        e.setId(1L);
        e.setTeamA("A");
        e.setTeamB("B");
        e.setSport(Sport.FOOTBALL);
        e.setMatchDate(LocalDate.now());
        e.setMatchTime(LocalTime.now());
        return e;
    }

    private MatchDTO buildDTO() {
        MatchDTO d = new MatchDTO();
        d.setId(1L);
        d.setTeamA("A");
        d.setTeamB("B");
        d.setSport(Sport.FOOTBALL);
        d.setMatchDate(LocalDate.now());
        d.setMatchTime(LocalTime.now());
        d.setOdds(List.of());
        return d;
    }
}