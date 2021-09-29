package com.ffxivcensus.gatherer.player.items.relations;

import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.items.data.Mount;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;

/**
 * @author graphicscore
 */
@Entity
@Table(name = "character_mounts")
public class PlayerMount {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private PlayerBean playerId;

    @OneToOne
    @JoinColumn(name = "mount_id", referencedColumnName = "id")
    private Mount mountId;

    public PlayerBean getPlayerId() {
        return playerId;
    }

    public void setPlayerId(PlayerBean player_id) {
        this.playerId = player_id;
    }

    public Mount getMountId() {
        return mountId;
    }

    public void setMountId(Mount mount_id) {
        this.mountId = mount_id;
    }

    public static PlayerMount Create(PlayerBean player, Mount mount){
        PlayerMount result = new PlayerMount();
        result.setPlayerId(player);
        result.setMountId(mount);
        return result;
    }
}
