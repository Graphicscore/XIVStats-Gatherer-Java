package com.ffxivcensus.gatherer.player.items;

import com.ffxivcensus.gatherer.player.PlayerBean;

import javax.persistence.*;

/**
 * @author graphicscore
 */
@Entity
@Table(name = "character_minions")
public class PlayerMinion {

    @Id
    private int id;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private PlayerBean player_id;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "mount_id", referencedColumnName = "id")
    private Mount mount_id;
}
