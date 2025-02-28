package com.ffxivcensus.gatherer.player;

import static org.junit.Assert.*;

import com.ffxivcensus.gatherer.player.items.repositories.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ffxivcensus.gatherer.edb.EorzeaDatabaseCache;
import com.ffxivcensus.gatherer.lodestone.TestDataLodestonePageLoader;

public class PlayerBuilderTest {

    private PlayerBuilder instance;
    private static EorzeaDatabaseCache EDB_CACHE = new EorzeaDatabaseCache();
    @Mock
    private GearItemRepository gearItemRepository;

    @Mock
    private MountRepository mountRepository;
    @Mock
    private MinionRepository minionRepository;
    @Mock
    private PlayerMountRepository playerMountRepository;
    @Mock
    private PlayerMinionRepository playerMinionRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new PlayerBuilder();
        // TODO: Figure out a mock of this
        instance.setEorzeaDatabaseCache(EDB_CACHE);
        instance.setGearItemRepository(gearItemRepository);
        instance.setMountRepository(mountRepository);
        instance.setMinionRepository(minionRepository);
        instance.setPlayerMountRepository(playerMountRepository);
        instance.setPlayerMinionRepository(playerMinionRepository);
    }

    @Test
    public void testLoadFrom2256025() throws Exception {
        instance.setPageLoader(new TestDataLodestonePageLoader());
        PlayerBean player = instance.getPlayer(2256025, null);

        // NOTE: All of the following tests assume various pieces of information
        // Testing information that is very unlikely to change
        assertEquals(2256025, player.getId());
        assertEquals("Russell Tyler", player.getPlayerName());
        assertEquals("Omega", player.getRealm());

        // Following can only be assumed to be true based on info at time of test creation
        assertEquals("Hyur", player.getRace());
        assertEquals("male", player.getGender());
        assertEquals("Order of the Twin Adder", player.getGrandCompany());
        // assertEquals("End of Days", playerOne.getFreeCompany());

        // Test classes - levels based on those at time of test creation
        // Tank
        assertEquals(0, player.getLevelGladiator());
        assertEquals(0, player.getLevelMarauder());
        assertEquals(80, player.getLevelDarkknight());
        assertEquals(80, player.getLevelGunbreaker());

        // Melee DPS
        assertEquals(80, player.getLevelPugilist());
        assertEquals(50, player.getLevelLancer());
        assertEquals(55, player.getLevelRogue());
        assertEquals(70, player.getLevelSamurai());

        // Ranged Physical DPS
        assertEquals(80, player.getLevelArcher());
        assertEquals(80, player.getLevelMachinist());
        assertEquals(80, player.getLevelDancer());

        // Ranged Magical DPS
        assertEquals(70, player.getLevelThaumaturge());
        assertEquals(80, player.getLevelArcanist());
        assertEquals(73, player.getLevelRedmage());
        assertEquals(60, player.getLevelBluemage());

        // Healer
        assertEquals(80, player.getLevelConjurer());
        assertEquals(80, player.getLevelScholar());
        assertEquals(80, player.getLevelAstrologian());

        // Disciples of the hand
        assertEquals(69, player.getLevelCarpenter());
        assertEquals(28, player.getLevelBlacksmith());
        assertEquals(25, player.getLevelArmorer());
        assertEquals(21, player.getLevelGoldsmith());
        assertEquals(0, player.getLevelLeatherworker());
        assertEquals(0, player.getLevelWeaver());
        assertEquals(80, player.getLevelAlchemist());
        assertEquals(70, player.getLevelCulinarian());

        // Disciples of the land
        assertEquals(0, player.getLevelMiner());
        assertEquals(58, player.getLevelBotanist());
        assertEquals(80, player.getLevelFisher());

        // The Forbidden Land, Eureka
        assertEquals(60, player.getLevelEureka());

        // Test boolean values
        // Subscription periods
        assertTrue(player.isHas30DaysSub());
        assertTrue(player.isHas60DaysSub());
        assertTrue(player.isHas90DaysSub());
        assertTrue(player.isHas180DaysSub());
        assertTrue(player.isHas270DaysSub());
        assertTrue(player.isHas360DaysSub());
        assertTrue(player.isHas450DaysSub());
        assertTrue(player.isHas630DaysSub());
        assertTrue(player.isHas960DaysSub());

        // Collectibles
        assertTrue(player.isHasPreOrderArr());
        assertTrue(player.isHasPreOrderHW());
        assertTrue(player.isHasPreOrderSB());
        assertTrue(player.isHasPS4Collectors());
        assertTrue(player.isHasARRCollectors());
        // Assuming the below don't change
        assertFalse(player.isHasARRArtbook());
        assertFalse(player.isHasBeforeMeteor());
        assertFalse(player.isHasBeforeTheFall());
        assertFalse(player.isHasSoundtrack());
        assertFalse(player.isHasMooglePlush());

        // Achievements
        assertFalse(player.isHasAttendedEternalBond());
        assertFalse(player.isHasCompletedHWSightseeing());
        assertTrue(player.isHasCompleted2pt5());
        assertTrue(player.isHasFiftyComms());
        assertTrue(player.isHasCompletedHildibrand());
        assertTrue(player.isHasEternalBond());
        assertFalse(player.isHasKobold());
        assertFalse(player.isHasSahagin());
        assertFalse(player.isHasAmaljaa());
        assertFalse(player.isHasSylph());
        assertTrue(player.isHasCompletedHW());
        // Currently no way to definitively tell player has completed the SB Main Scenario
        // Currently assumes minion drop from Kugane, Temple of the Fist or Delta V4
        assertTrue(player.isHasCompletedSB());
        assertTrue(player.isHasCompleted3pt1());
        assertFalse(player.isLegacyPlayer());

        // Test minions string
        // Test for data near start
        assertTrue(instance.doesPlayerHaveMinion(player,"Wayward Hatchling"));
        // Test for data in middle
        assertTrue(instance.doesPlayerHaveMinion(player,"Morbol Seedling"));
        // Test for data from end
        assertTrue(instance.doesPlayerHaveMinion(player,"Wind-up Sun"));

        // Test mounts string
        // Test for data from (near) start
        assertTrue(instance.doesPlayerHaveMount(player,"Company Chocobo"));
        // Test for data from middle
        assertFalse(instance.doesPlayerHaveMount(player,"Cavalry Drake"));
        // Test for data from very end
        assertTrue(instance.doesPlayerHaveMount(player,"Midgardsormr"));

        /*assertEquals("08372e63df2", player.getGearSet().getMainHand().getItemId());
        assertEquals("Augmented Deepshadow Codex", player.getGearSet().getMainHand().getName());
        assertEquals("Scholar's Arm", player.getGearSet().getMainHand().getCategory());
        assertEquals(470, player.getGearSet().getMainHand().getiLevel());

        assertEquals("4a2ae61210d", player.getGearSet().getHead().getItemId());
        assertEquals("Augmented Deepshadow Hood of Healing", player.getGearSet().getHead().getName());

        assertEquals("e7276bb2a56", player.getGearSet().getBody().getItemId());
        assertEquals("Augmented Deepshadow Scale Mail of Healing", player.getGearSet().getBody().getName());

        assertEquals("3804a54f06b", player.getGearSet().getHands().getItemId());
        assertEquals("Augmented Deepshadow Armguards of Healing", player.getGearSet().getHands().getName());

        assertEquals("e60f7637206", player.getGearSet().getBelt().getItemId());
        assertEquals("Augmented Deepshadow Tassets of Healing", player.getGearSet().getBelt().getName());

        assertEquals("9922348a266", player.getGearSet().getLegs().getItemId());
        assertEquals("Augmented Deepshadow Breeches of Healing", player.getGearSet().getLegs().getName());

        assertEquals("c4985948ea9", player.getGearSet().getFeet().getItemId());
        assertEquals("Augmented Deepshadow Greaves of Healing", player.getGearSet().getFeet().getName());

        assertNull(player.getGearSet().getOffHand());

        assertEquals("6433817eadd", player.getGearSet().getEars().getItemId());
        assertEquals("Augmented Deepshadow Earring of Healing", player.getGearSet().getEars().getName());

        assertEquals("f6ff748e3da", player.getGearSet().getNeck().getItemId());
        assertEquals("Augmented Deepshadow Necklace of Healing", player.getGearSet().getNeck().getName());

        assertEquals("3f76ec4df01", player.getGearSet().getWrists().getItemId());
        assertEquals("Augmented Deepshadow Bracelet of Healing", player.getGearSet().getWrists().getName());

        assertEquals("f90266bb91f", player.getGearSet().getLeftHand().getItemId());
        assertEquals("Edencall Ring of Healing", player.getGearSet().getLeftHand().getName());

        assertEquals("22c8c0bb824", player.getGearSet().getRightHand().getItemId());
        assertEquals("Augmented Deepshadow Ring of Healing", player.getGearSet().getRightHand().getName());

        assertEquals("eb511e3871f", player.getGearSet().getJobCrystal().getItemId());
        assertEquals("Soul of the Scholar", player.getGearSet().getJobCrystal().getName());
        */
    }

    @Test
    public void testLoadFrom22763008() throws Exception {
        instance.setPageLoader(new TestDataLodestonePageLoader());
        PlayerBean player = instance.getPlayer(22763008, null);

        // NOTE: All of the following tests assume various pieces of information
        // Testing information that is very unlikely to change
        assertEquals(22763008, player.getId());
        assertEquals("R'ythri Tia", player.getPlayerName());
        assertEquals("Tonberry", player.getRealm());

        // Following can only be assumed to be true based on info at time of test creation
        assertEquals("Miqo'te", player.getRace());
        assertEquals("male", player.getGender());
        assertEquals("Maelstrom", player.getGrandCompany());
        // assertEquals("End of Days", playerOne.getFreeCompany());

        // Test classes - levels based on those at time of test creation
        // Tank
        assertEquals(0, player.getLevelGladiator());
        assertEquals(33, player.getLevelMarauder());
        assertEquals(0, player.getLevelDarkknight());
        assertEquals(0, player.getLevelGunbreaker());

        // Melee DPS
        assertEquals(0, player.getLevelPugilist());
        assertEquals(0, player.getLevelLancer());
        assertEquals(0, player.getLevelRogue());
        assertEquals(0, player.getLevelSamurai());

        // Ranged Physical DPS
        assertEquals(0, player.getLevelArcher());
        assertEquals(0, player.getLevelMachinist());
        assertEquals(0, player.getLevelDancer());

        // Ranged Magical DPS
        assertEquals(0, player.getLevelThaumaturge());
        assertEquals(0, player.getLevelArcanist());
        assertEquals(0, player.getLevelRedmage());
        assertEquals(0, player.getLevelBluemage());

        // Healer
        assertEquals(0, player.getLevelConjurer());
        assertEquals(0, player.getLevelScholar());
        assertEquals(0, player.getLevelAstrologian());

        // Disciples of the hand
        assertEquals(0, player.getLevelCarpenter());
        assertEquals(0, player.getLevelBlacksmith());
        assertEquals(0, player.getLevelArmorer());
        assertEquals(0, player.getLevelGoldsmith());
        assertEquals(0, player.getLevelLeatherworker());
        assertEquals(0, player.getLevelWeaver());
        assertEquals(0, player.getLevelAlchemist());
        assertEquals(0, player.getLevelCulinarian());

        // Disciples of the land
        assertEquals(0, player.getLevelMiner());
        assertEquals(0, player.getLevelBotanist());
        assertEquals(0, player.getLevelFisher());

        // The Forbidden Land, Eureka
        assertEquals(0, player.getLevelEureka());

        // Test boolean values
        // Subscription periods
        assertTrue(player.isHas30DaysSub());
        assertTrue(player.isHas60DaysSub());
        assertTrue(player.isHas90DaysSub());
        assertTrue(player.isHas180DaysSub());
        assertTrue(player.isHas270DaysSub());
        assertTrue(player.isHas360DaysSub());
        assertTrue(player.isHas450DaysSub());
        assertTrue(player.isHas630DaysSub());
        assertTrue(player.isHas960DaysSub());

        // Collectibles
        assertTrue(player.isHasPreOrderArr());
        assertTrue(player.isHasPreOrderHW());
        assertTrue(player.isHasPreOrderSB());
        assertTrue(player.isHasPS4Collectors());
        assertTrue(player.isHasARRCollectors());
        // Assuming the below don't change
        assertFalse(player.isHasARRArtbook());
        assertFalse(player.isHasBeforeMeteor());
        assertFalse(player.isHasBeforeTheFall());
        assertFalse(player.isHasSoundtrack());
        assertFalse(player.isHasMooglePlush());

        // Achievements
        assertFalse(player.isHasAttendedEternalBond());
        assertFalse(player.isHasCompletedHWSightseeing());
        assertFalse(player.isHasCompleted2pt5());
        assertFalse(player.isHasFiftyComms());
        assertFalse(player.isHasCompletedHildibrand());
        assertFalse(player.isHasEternalBond());
        assertFalse(player.isHasKobold());
        assertFalse(player.isHasSahagin());
        assertFalse(player.isHasAmaljaa());
        assertFalse(player.isHasSylph());
        assertFalse(player.isHasCompletedHW());
        // Currently no way to definitively tell player has completed the SB Main Scenario
        // Currently assumes minion drop from Kugane, Temple of the Fist or Delta V4
        assertFalse(player.isHasCompletedSB());
        assertFalse(player.isHasCompleted3pt1());
        assertFalse(player.isLegacyPlayer());

        // Test minions string
        // Test for data near start
        assertFalse(instance.doesPlayerHaveMinion(player,"Wayward Hatchling"));
        // Test for data in middle
        assertFalse(instance.doesPlayerHaveMinion(player,"Morbol Seedling"));
        // Test for data from end
        assertFalse(instance.doesPlayerHaveMinion(player,"Wind-up Sun"));

        // Test mounts string
        // Test for data from (near) start
        assertTrue(instance.doesPlayerHaveMount(player,"Company Chocobo"));
        // Test for data from middle
        assertFalse(instance.doesPlayerHaveMount(player,"Cavalry Drake"));
        // Test for data from very end
        assertFalse(instance.doesPlayerHaveMount(player,"Midgardsormr"));
    }

}
