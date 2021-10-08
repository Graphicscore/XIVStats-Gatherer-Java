package com.ffxivcensus.gatherer.player.items.relations;

import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.items.data.Minion;

import javax.persistence.*;

/**
 * @author graphicscore
 */
@Entity
@Table(name = "character_minions")
public class PlayerMinion {

    @Id
    private int id;

    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private int playerId;

    @JoinColumn(name = "minion_id", referencedColumnName = "id")
    private String minionId;

    public void setPlayerId(int player_id) {
        this.playerId = player_id;
    }

    public void setMinionId(String minion_id) {
        this.minionId = minion_id;
    }

    public static PlayerMinion Create(int playerId, String minionId){
        PlayerMinion result = new PlayerMinion();
        result.setPlayerId(playerId);
        result.setMinionId(minionId);
        return result;
    }
}
