package com.ffxivcensus.gatherer.player.items.repositories;

import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.items.data.Minion;
import com.ffxivcensus.gatherer.player.items.relations.PlayerMinion;
import org.springframework.data.repository.CrudRepository;

/**
 * @author graphicscore
 */
public interface PlayerMinionRepository extends CrudRepository<PlayerMinion, String> {

    PlayerMinion findByPlayerIdAndMinionId(PlayerBean playerId, Minion minion);
}
