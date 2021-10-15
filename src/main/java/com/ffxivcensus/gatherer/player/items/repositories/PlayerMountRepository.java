package com.ffxivcensus.gatherer.player.items.repositories;

import com.ffxivcensus.gatherer.player.items.relations.PlayerMount;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author graphicscore
 */
public interface PlayerMountRepository extends CrudRepository<PlayerMount, String> {
    PlayerMount findByPlayerIdAndMountId(int playerId, String mountId);

    List<PlayerMount> findAllByPlayerId(int playerId);
}