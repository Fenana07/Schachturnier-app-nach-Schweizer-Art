package org.swisssystem.service;

import org.swisssystem.entity.Player;
import org.swisssystem.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPlayer(String firstName, String lastName, Integer rating) {
        Player p = new Player();
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setInitialRating(rating);
        p.setCreatedAt(Instant.now());
        return playerRepository.save(p);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }
}
