package org.swisssystem;

import org.swisssystem.entity.Player;
import org.swisssystem.entity.Round;
import org.swisssystem.entity.Tournament;
import org.swisssystem.service.PlayerService;
import org.swisssystem.service.TournamentService;
import org.swisssystem.service.RoundService;
import org.swisssystem.service.PairingService;
import org.swisssystem.service.StandingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;

@Component
public class StartupRunner implements CommandLineRunner {

    private final PlayerService playerService;
    private final TournamentService tournamentService;
    private final RoundService roundService;
    private final PairingService pairingService;
    private final StandingService standingService;

    public StartupRunner(PlayerService playerService,
                         TournamentService tournamentService,
                         RoundService roundService,
                         PairingService pairingService,
                         StandingService standingService) {
        this.playerService = playerService;
        this.tournamentService = tournamentService;
        this.roundService = roundService;
        this.pairingService = pairingService;
        this.standingService = standingService;
    }

    @Override
    public void run(String... args) {
        System.out.println("===== SCHWEIZER SYSTEM – DEMO START =====");

        // 1. Turnier
        Tournament t = tournamentService.createTournament(
                "Testturnier",
                "Graz",
                5,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        // 2. Spieler erstellen
        Player p1 = playerService.createPlayer("Fabian", "Erdkönig", 1800);
        Player p2 = playerService.createPlayer("Luis", "Goalkeeping", 1750);
        Player p3 = playerService.createPlayer("Max", "Leoben", 1600);

        // 3. Spieler ins Turnier aufnehmen
        tournamentService.addPlayer(t, p1, 1, 1800, null, null);
        tournamentService.addPlayer(t, p2, 2, 1750, null, null);
        tournamentService.addPlayer(t, p3, 3, 1600, null, null);

        // 4. Runde erstellen
        Round r1 = roundService.createRound(t, 1, Instant.now());

        // 5. Paarungen erzeugen
        pairingService.generatePairingsForRound(t, r1);

        // 6. Standings berechnen
        standingService.recalculateForTournament(t);

        System.out.println("===== DEMO BEENDET =====");
    }
}
