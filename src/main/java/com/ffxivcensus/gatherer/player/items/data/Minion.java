package com.ffxivcensus.gatherer.player.items.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author graphicscore
 */
@Entity
@Table(name = "minions")
public class Minion implements Cloneable{

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
        return "Minion{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public Minion clone() {
        try {
            Minion clone = (Minion) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}