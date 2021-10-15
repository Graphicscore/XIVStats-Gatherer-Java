package com.ffxivcensus.gatherer.player.items.repositories;

import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.items.data.Minion;
import com.ffxivcensus.gatherer.player.items.relations.PlayerMinion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author graphicscore
 */
public interface PlayerMinionRepository extends CrudRepository<PlayerMinion, String> {

    PlayerMinion findByPlayerIdAndMinionId(int playerId, String minionId);

    List<PlayerMinion> findAllByPlayerId(int playerId);
}
