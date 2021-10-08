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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private int playerId;

    @JoinColumn(name = "mount_id", referencedColumnName = "id")
    private String mountId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int player_id) {
        this.playerId = player_id;
    }

    public String getMountId() {
        return mountId;
    }

    public void setMountId(String mount_id) {
        this.mountId = mount_id;
    }

    public static PlayerMount Create(int playerId, String mountId){
        PlayerMount result = new PlayerMount();
        result.setPlayerId(playerId);
        result.setMountId(mountId);
        return result;
    }
}
