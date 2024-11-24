package com.dyma.tennis.service;

import com.dyma.tennis.model.Tournament;
import com.dyma.tennis.model.TournamentToCreate;
import com.dyma.tennis.model.TournamentToUpdate;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TournamentServiceIntegrationTest {

    @Autowired
    private TournamentService tournamentService;

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void shouldCreateTournament() {
        // Given
        TournamentToCreate tournamentToCreate = new TournamentToCreate(
                "Madrid Master 1000",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(17),
                500000,
                64
        );

        // When
        Tournament tournament = tournamentService.create(tournamentToCreate);
        Tournament createdTournament = tournamentService.getByIdentifier(tournament.identifier());

        // Then
        Assertions.assertThat(createdTournament.name()).isEqualTo("Madrid Master 1000");
    }

    @Test
    public void shouldFailToCreateAnExistingTournament() {
        // Given
        TournamentToCreate tournamentToCreate = new TournamentToCreate(
                "Madrid Master 1000",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(17),
                500000,
                64
        );
        tournamentService.create(tournamentToCreate);
        TournamentToCreate sameTournamentToCreate = new TournamentToCreate(
                "Madrid Master 1000",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(17),
                500000,
                64
        );

        // When / Then
        Exception exception = assertThrows(TournamentDataRetrievalException.class, () -> {
            tournamentService.create(sameTournamentToCreate);
        });
        Assertions.assertThat(exception.getMessage()).contains("Could not retrieve tournament data");
    }

    @Test
    public void shouldUpdateTournament() {
        // Given
        TournamentToUpdate tournamentToUpdate = new TournamentToUpdate(
                "d4a9f8e2-9051-4739-90bc-1cb7e4c7ad42",
                "Roland Garros",
                LocalDate.of(2025, Month.MAY, 26),
                LocalDate.of(2025, Month.JUNE, 9),
                2500000,
                128
        );

        // When
        tournamentService.update(tournamentToUpdate);
        Tournament updatedTournament = tournamentService.getByIdentifier("d4a9f8e2-9051-4739-90bc-1cb7e4c7ad42");

        // Then
        Assertions.assertThat(updatedTournament.name()).isEqualTo("Roland Garros");
    }

    @Test
    public void shouldDeleteTournament() {
        // Given
        String tournamentToDelete = "124edf07-64fa-4ea4-a65e-3bfe96df5781";

        // When
        tournamentService.delete(tournamentToDelete);

        // Then
        List<Tournament> allTournaments = tournamentService.getAllTournaments();
        Assertions.assertThat(allTournaments)
                .extracting("name")
                .containsExactly("Australian Open", "French Open", "Wimbledon");
    }

    @Test
    public void shouldFailToDeleteTournament_WhenTournamentDoesNotExist() {
        // Given
        String tournamentToDelete = "5f8c9b43-8d74-49e8-b821-f43d57e4a9b7";

        // When / Then
        Exception exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentService.delete(tournamentToDelete);
        });
        Assertions.assertThat(exception.getMessage()).isEqualTo("Tournament with identifier 5f8c9b43-8d74-49e8-b821-f43d57e4a9b7 could not be found.");
    }
}
