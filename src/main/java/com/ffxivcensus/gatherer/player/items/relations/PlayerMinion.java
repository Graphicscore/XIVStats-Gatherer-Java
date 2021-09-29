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

    @OneToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private PlayerBean playerId;

    @OneToOne
    @JoinColumn(name = "minion_id", referencedColumnName = "id")
    private Minion minionId;

    public void setPlayerId(PlayerBean player_id) {
        this.playerId = player_id;
    }

    public void setMinionId(Minion minion_id) {
        this.minionId = minion_id;
    }

    public static PlayerMinion Create(PlayerBean player, Minion minion){
        PlayerMinion result = new PlayerMinion();
        result.setPlayerId(player);
        result.setMinionId(minion);
        return result;
    }
}
