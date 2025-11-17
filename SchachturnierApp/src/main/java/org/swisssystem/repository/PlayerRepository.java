package org.swisssystem.repository;

import org.swisssystem.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByFideId(String fideId);
}
