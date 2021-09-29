package com.ffxivcensus.gatherer.player.items.repositories;

import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.items.data.Mount;
import com.ffxivcensus.gatherer.player.items.relations.PlayerMount;
import org.springframework.data.repository.CrudRepository;

/**
 * @author graphicscore
 */
public interface PlayerMountRepository extends CrudRepository<PlayerMount, String> {
    PlayerMount findByPlayerIdAndMountId(PlayerBean playerId, Mount mountId);
}