package com.dyma.tennis.service;

import com.dyma.tennis.model.Tournament;
import com.dyma.tennis.model.TournamentToCreate;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

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

}
