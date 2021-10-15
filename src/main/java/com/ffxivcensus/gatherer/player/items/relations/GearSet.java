package com.ffxivcensus.gatherer.player.items.relations;

import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.items.data.GearItem;

import javax.persistence.*;

/**
 * A Gear Set assigned to the player at the time of parsing.
 * 
 * @author matthew.hillier
 */
@Entity
@Table(name = "character_gear_sets")
public class GearSet implements Cloneable{

    @Id
    private int playerId;
    @OneToOne(mappedBy = "gearSet")
    private PlayerBean player;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "main_hand")
    private GearItem mainHand;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "head")
    private GearItem head;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "body")
    private GearItem body;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "hands")
    private GearItem hands;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "belt")
    private GearItem belt;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "legs")
    private GearItem legs;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "feet")
    private GearItem feet;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "off_hand")
    private GearItem offHand;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "ears")
    private GearItem ears;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "neck")
    private GearItem neck;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "wrists")
    private GearItem wrists;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "left_hand")
    private GearItem leftHand;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "right_hand")
    private GearItem rightHand;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "job_crystal")
    private GearItem jobCrystal;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public PlayerBean getPlayer() {
        return player;
    }

    public void setPlayer(PlayerBean player) {
        this.player = player;
    }

    public GearItem getMainHand() {
        return mainHand;
    }

    public void setMainHand(GearItem mainHand) {
        this.mainHand = mainHand;
    }

    public GearItem getHead() {
        return head;
    }

    public void setHead(GearItem head) {
        this.head = head;
    }

    public GearItem getBody() {
        return body;
    }

    public void setBody(GearItem body) {
        this.body = body;
    }

    public GearItem getHands() {
        return hands;
    }

    public void setHands(GearItem hands) {
        this.hands = hands;
    }

    public GearItem getBelt() {
        return belt;
    }

    public void setBelt(GearItem belt) {
        this.belt = belt;
    }

    public GearItem getLegs() {
        return legs;
    }

    public void setLegs(GearItem legs) {
        this.legs = legs;
    }

    public GearItem getFeet() {
        return feet;
    }

    public void setFeet(GearItem feet) {
        this.feet = feet;
    }

    public GearItem getOffHand() {
        return offHand;
    }

    public void setOffHand(GearItem offHand) {
        this.offHand = offHand;
    }

    public GearItem getEars() {
        return ears;
    }

    public void setEars(GearItem ears) {
        this.ears = ears;
    }

    public GearItem getNeck() {
        return neck;
    }

    public void setNeck(GearItem neck) {
        this.neck = neck;
    }

    public GearItem getWrists() {
        return wrists;
    }

    public void setWrists(GearItem wrists) {
        this.wrists = wrists;
    }

    public GearItem getLeftHand() {
        return leftHand;
    }

    public void setLeftHand(GearItem leftHand) {
        this.leftHand = leftHand;
    }

    public GearItem getRightHand() {
        return rightHand;
    }

    public void setRightHand(GearItem rightHand) {
        this.rightHand = rightHand;
    }

    public GearItem getJobCrystal() {
        return jobCrystal;
    }

    public void setJobCrystal(GearItem jobCrystal) {
        this.jobCrystal = jobCrystal;
    }

    public boolean hasChanged(GearSet os){
        if(compareItem(getMainHand(),os.getMainHand())) return true;
        if(compareItem(getHead(),os.getHead())) return true;
        if(compareItem(getBody(),os.getBody())) return true;
        if(compareItem(getHands(),os.getHands())) return true;
        if(compareItem(getBelt(),os.getBelt())) return true;
        if(compareItem(getLegs(),os.getLegs())) return true;
        if(compareItem(getFeet(),os.getFeet())) return true;
        if(compareItem(getOffHand(),os.getOffHand())) return true;
        if(compareItem(getEars(),os.getEars())) return true;
        if(compareItem(getNeck(),os.getNeck())) return true;
        if(compareItem(getWrists(),os.getWrists())) return true;
        if(compareItem(getLeftHand(),os.getLeftHand())) return true;
        if(compareItem(getRightHand(),os.getRightHand())) return true;
        if(compareItem(getJobCrystal(),os.getJobCrystal())) return true;
        return false;
    }

    //returns true if item has changed
    private boolean compareItem(GearItem item1, GearItem item2){
        if(item1 != null && item2 == null) return true;
        if(item2 != null && item1 == null) return true;
        if(item1 == null && item2 == null) return false;
        if(!item1.getItemId().contentEquals(item2.getItemId())) return true;
        return false;
    }

    @Override
    public GearSet clone() {
        try {
            GearSet clone = (GearSet) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
