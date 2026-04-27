package chatzidandis.controller;

import chatzidandis.api.ApiResponse;
import chatzidandis.dto.MatchDTO;
import chatzidandis.dto.MatchOddsDTO;
import chatzidandis.service.MatchService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService service;

    public MatchController(MatchService service) {
        this.service = service;
    }

    //create a new match. Odds list is optional
    @PostMapping
    public ResponseEntity<ApiResponse<MatchDTO>> create(@Valid @RequestBody MatchDTO dto) {
        MatchDTO created = service.create(dto);
        log.info("Match created successfully: {} vs {} on {} {}",
                        created.getTeamA(),
                        created.getTeamB(),
                        created.getMatchDate(),
                        created.getMatchTime());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(created));
    }

    //create, update odds list for a match
    @PostMapping("/{matchId}/odds")
    public ResponseEntity<ApiResponse<List<MatchOddsDTO>>> addOdds(
                    @Valid
                    @PathVariable Long matchId,
                    @RequestBody List<MatchOddsDTO> oddsDtos) {

        List<MatchOddsDTO> odds = service.odds(matchId, oddsDtos);

        log.info("Processed {} odds for match id {}: {}", odds.size(), matchId, odds);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(odds));
    }

    //get odds list by match id
    @GetMapping("/{matchId}/odds")
    public ResponseEntity<ApiResponse<List<MatchOddsDTO>>> getOdds(@PathVariable Long matchId) {
        List<MatchOddsDTO> odds = service.findOddsByMatchId(matchId);
        log.info("Found {} odds for match id {}", odds.size(), matchId);
        return ResponseEntity.ok(ApiResponse.ok(odds));
    }

    //find match by id
    @GetMapping("/{matchId}")
    public ResponseEntity<ApiResponse<MatchDTO>> get(@PathVariable Long matchId) {
        MatchDTO match = service.findById(matchId);
        log.info("Found match with id {}", matchId);
        return ResponseEntity.ok(ApiResponse.ok(match));
    }

    //update match info by id
    @PutMapping("/{matchId}")
    public ResponseEntity<ApiResponse<MatchDTO>> update(@Valid @PathVariable Long matchId, @RequestBody MatchDTO dto) {
        MatchDTO match = service.update(matchId, dto);
        log.info("Updated match with id {}", matchId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.ok(match));
    }

    //delete match by id
    @DeleteMapping("/{matchId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long matchId) {
        service.delete(matchId);
        log.info("Deleted match with id {}", matchId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
