package com.ffxivcensus.gatherer.player.items;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Mount from the Lodestone / Eorzea Database
 *
 * @author graphicscore
 */
@Entity
@Table(name = "mounts")
public class Mount {

    @Id
    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Mount{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
