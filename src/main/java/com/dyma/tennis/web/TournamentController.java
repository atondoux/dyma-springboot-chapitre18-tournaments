package com.dyma.tennis.web;


import com.dyma.tennis.model.Error;
import com.dyma.tennis.model.Tournament;
import com.dyma.tennis.model.TournamentToCreate;
import com.dyma.tennis.model.TournamentToUpdate;
import com.dyma.tennis.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tournaments API")
@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @Operation(summary = "Finds tournaments", description = "Finds tournaments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tournaments list",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Tournament.class)))}),
            @ApiResponse(responseCode = "403", description = "This user is not authorized to perform this action.")

    })
    @GetMapping
    public List<Tournament> list() {
        return tournamentService.getAllTournaments();
    }

    @Operation(summary = "Finds a tournament", description = "Finds a tournament")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tournament",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tournament.class))}),
            @ApiResponse(responseCode = "404", description = "Tournament with specified identifier was not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "403", description = "This user is not authorized to perform this action.")

    })
    @GetMapping("{identifier}")
    public Tournament getTournament(@PathVariable("identifier") UUID identifier) {
        return tournamentService.getByIdentifier(identifier);
    }

    @Operation(summary = "Creates a tournament", description = "Creates a tournament")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created tournament",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TournamentToCreate.class))}),
            @ApiResponse(responseCode = "400", description = "Tournament already exists.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "403", description = "This user is not authorized to perform this action.")

    })
    @PostMapping
    public Tournament createTournament(@RequestBody @Valid TournamentToCreate tournamentToCreate) {
        return tournamentService.create(tournamentToCreate);
    }

    @Operation(summary = "Updates a tournament", description = "Updates a tournament")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated tournament",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TournamentToUpdate.class))}),
            @ApiResponse(responseCode = "404", description = "Tournament with specified identifier was not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "403", description = "This user is not authorized to perform this action.")

    })
    @PutMapping
    public Tournament updateTournament(@RequestBody @Valid TournamentToUpdate tournamentToUpdate) {
        return tournamentService.update(tournamentToUpdate);
    }

    @Operation(summary = "Deletes a tournament", description = "Deletes a tournament")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tournament has been deleted"),
            @ApiResponse(responseCode = "404", description = "Tournament with specified identifier was not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "403", description = "This user is not authorized to perform this action.")

    })
    @DeleteMapping("{identifier}")
    public void deleteTournament(@PathVariable("identifier") UUID identifier) {
        tournamentService.delete(identifier);
    }
}
