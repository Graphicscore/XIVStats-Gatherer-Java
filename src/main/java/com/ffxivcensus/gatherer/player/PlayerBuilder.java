package com.ffxivcensus.gatherer.player;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import com.ffxivcensus.gatherer.player.items.data.GearItem;
import com.ffxivcensus.gatherer.player.items.data.Minion;
import com.ffxivcensus.gatherer.player.items.relations.GearSet;
import com.ffxivcensus.gatherer.player.items.relations.PlayerMinion;
import com.ffxivcensus.gatherer.player.items.relations.PlayerMount;
import com.ffxivcensus.gatherer.player.items.repositories.*;
import com.ffxivcensus.gatherer.player.items.data.Mount;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ffxivcensus.gatherer.edb.EorzeaDatabaseCache;
import com.ffxivcensus.gatherer.lodestone.CharacterDeletedException;
import com.ffxivcensus.gatherer.lodestone.LodestonePageLoader;
import com.ffxivcensus.gatherer.lodestone.ProductionLodestonePageLoader;
import com.ffxivcensus.gatherer.task.GathererTask;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * Builder class for creating PlayerBean objects from the Lodestone.
 *
 * @author Peter Reid
 * @author Matthew Hillier
 * @since v1.0
 * @see GathererTask
 */
@Service
public class PlayerBuilder {

    private static final String HEADER_LAST_MODIFIED = "Last-Modified";
    private static final String ATTR_SRC = "src";
    private static final String TAG_IMG = "img";
    private static final String TAG_A = "a";
    private static final String LAYOUT_CHARACTER_DETAIL_IMAGE = "character__detail__image";
    private static final String LAYOUT_CHARACTER_MOUNTS = "character__mounts";
    private static final String TAG_LI = "li";
    private static final String LAYOUT_CHARACTER_MINION = "character__minion";
    private static final String LAYOUT_CHARACTER_JOB_LEVEL = "character__job__level";
    private static final String LAYOUT_CHARACTER_FREECOMPANY_NAME = "character__freecompany__name";
    private static final String LAYOUT_CHARACTER_BLOCK_BOX = "character-block__box";
    private static final String LAYOUT_FRAME_CHARA_WORLD = "frame__chara__world";
    private static final String LAYOUT_CHARACTER_BLOCK_NAME = "character-block__name";

    private static final String LAYOUT_CHARACTER_JOB_LEVEL_OPTIONAL_BOZJA = "xiv-lds-resistance-level";
    private static final String LAYOUT_CHARACTER_JOB_LEVEL_OPTIONAL_EUREKA = "xiv-lds-elemental-level";

    private static final Logger LOG = LoggerFactory.getLogger(PlayerBuilder.class);
    /**
     * Number of days inactivity before character is considered inactive
     */
    private static final int ACTIVITY_RANGE_DAYS = 30;

    private static final long ONE_DAY_IN_MILLIS = 86400000;

    private LodestonePageLoader pageLoader = new ProductionLodestonePageLoader();
    private EorzeaDatabaseCache edbCache;

    private GearItemRepository gearItemRepository;

    private MountRepository mountRepository;
    private MinionRepository minionRepository;

    private PlayerMountRepository playerMountRepository;
    private PlayerMinionRepository playerMinionRepository;

    /**
     * Set player class levels.
     * As of 4.5, this is now parsed in the order:
     * - Gladiator
     * - Marauder
     * - Dark Knight
     * - Monk
     * - Dragoon
     * - Ninja
     * - Samurai
     * - White Mage
     * - Scholar
     * - Astrologian
     * - Bard
     * - Machinist
     * - Black Mage
     * - Summoner
     * - Red Mage
     * - Blue Mage
     *
     * @param arrLevels integer array of classes in order displayed on lodestone.
     */
    public void setLevels(final PlayerBean player, final int[] arrLevels) {
        player.setLevelGladiator(arrLevels[0]);
        player.setLevelMarauder(arrLevels[1]);
        player.setLevelDarkknight(arrLevels[2]);
        player.setLevelGunbreaker(arrLevels[3]);
        player.setLevelPugilist(arrLevels[4]);
        player.setLevelLancer(arrLevels[5]);
        player.setLevelRogue(arrLevels[6]);
        player.setLevelSamurai(arrLevels[7]);
        player.setLevelConjurer(arrLevels[8]);
        player.setLevelScholar(arrLevels[9]);
        player.setLevelAstrologian(arrLevels[10]);
        player.setLevelArcher(arrLevels[11]);
        player.setLevelMachinist(arrLevels[12]);
        player.setLevelDancer(arrLevels[13]);
        player.setLevelThaumaturge(arrLevels[14]);
        player.setLevelArcanist(arrLevels[15]);
        player.setLevelRedmage(arrLevels[16]);
        player.setLevelBluemage(arrLevels[17]);
        player.setLevelCarpenter(arrLevels[18]);
        player.setLevelBlacksmith(arrLevels[19]);
        player.setLevelArmorer(arrLevels[20]);
        player.setLevelGoldsmith(arrLevels[21]);
        player.setLevelLeatherworker(arrLevels[22]);
        player.setLevelWeaver(arrLevels[23]);
        player.setLevelAlchemist(arrLevels[24]);
        player.setLevelCulinarian(arrLevels[25]);
        player.setLevelMiner(arrLevels[26]);
        player.setLevelBotanist(arrLevels[27]);
        player.setLevelFisher(arrLevels[28]);
        //player.setLevelSage(arrLevels[29]); //TODO for Endwalker
        //player.setLevelReaper(arrLevels[30]); //TODO for Endwalker
        // Elemental Level is an optional component at the end of the section, so may not exist
        player.setLevelEureka(arrLevels[31]);
        player.setLevelBozja(arrLevels[32]);
    }

    /**
     * Determine if a player has a specified mount
     *
     * @param mountName the name of the mount to check for.
     * @return whether the player has the specified mount.
     */
    public boolean doesPlayerHaveMount(final PlayerBean player, final String mountName) {
        return player.getMounts().stream().anyMatch(o -> o.getName().equals(mountName));
    }

    /**
     * Determine if a player has a specified minion.
     *
     * @param minionName the name of the minion to check for
     * @return whether the player has the specified minion.
     */
    public boolean doesPlayerHaveMinion(final PlayerBean player, final String minionName) {
        return player.getMinions().stream().anyMatch(o -> o.getName().equals(minionName));
    }

    /**
     * Fetch a player from the lodestone specified by ID.
     *
     * @param playerID the ID of the player to fetch
     * @return the player object matching the specified ID.
     * @throws IOException thrown if more class levels returned than anticipated.
     * @throws InterruptedException on shutdown
     */
    public PlayerBean getPlayer(final int playerID, final PlayerBean existing) throws IOException, InterruptedException {
        // Initialize player object to return
        PlayerBean tmpExisting = null;
        PlayerBean player = existing;
        if(existing == null) {
            player = new PlayerBean();
            player.setId(playerID);
        } else {
            tmpExisting = existing.clone();
            tmpExisting.setGearSet(existing.getGearSet().clone());
            //this is an ugly hack but since we are only comparing the count of minions and mounts it should be fine, i hope?
            PlayerBean finalTmpExisting = tmpExisting;
            playerMinionRepository.findAllByPlayerId(tmpExisting.getId()).forEach(playerMinion -> finalTmpExisting.getMinions().add(new Minion()));
            playerMountRepository.findAllByPlayerId(tmpExisting.getId()).forEach(playerMount -> finalTmpExisting.getMounts().add(new Mount()));
        }
        // Declare HTML document
        try {
            Document doc = pageLoader.getCharacterPage(playerID);

            player.setPlayerName(getNameFromPage(doc));
            player.setRealm(getRealmFromPage(doc));
            player.setRace(getRaceFromPage(doc));
            player.setGender(getGenderFromPage(doc));
            player.setGrandCompany(getGrandCompanyFromPage(doc));
            player.setFreeCompany(getFreeCompanyFromPage(doc));
            player.setDateImgLastModified(getDateLastUpdatedFromPage(doc, playerID));
            
            Document classJobDoc = pageLoader.getClassJobPage(playerID);
            setLevels(player, getLevelsFromPage(classJobDoc));
            player.setGearSet(getGearSet(player, doc));


            // Mounts from the relevant sub-section
            try {
                Document mountDoc = pageLoader.getMountPage(playerID);
                player.setMounts(gatherMounts(player, mountDoc));
                //player.setMounts(getMountsFromPage(mountDoc));
            }catch (CharacterDeletedException ex){ //char might not really be deleted
                player.setMounts(Collections.emptyList());
            }

            // Minions from the relevant sub-section
            try{
            Document minionDoc = pageLoader.getMinionPage(playerID);
            player.setMinions(gatherMinions(player, minionDoc));
            //player.setMinions(getMinionsFromPage(minionDoc));
            }catch (CharacterDeletedException ex){ //char might not really be deleted
                player.setMinions(Collections.emptyList());
            }

            // Info based on the result of grabbing Mounts & Minions
            player.setHas30DaysSub(doesPlayerHaveMinion(player, "Wind-up Cursor"));
            player.setHas60DaysSub(doesPlayerHaveMinion(player, "Black Chocobo Chick"));
            player.setHas90DaysSub(doesPlayerHaveMinion(player, "Beady Eye"));
            player.setHas180DaysSub(doesPlayerHaveMinion(player, "Minion Of Light"));
            player.setHas270DaysSub(doesPlayerHaveMinion(player, "Wind-up Leader"));
            player.setHas360DaysSub(doesPlayerHaveMinion(player, "Wind-up Odin"));
            player.setHas450DaysSub(doesPlayerHaveMinion(player, "Wind-up Goblin"));
            player.setHas630DaysSub(doesPlayerHaveMinion(player, "Wind-up Nanamo"));
            player.setHas960DaysSub(doesPlayerHaveMinion(player, "Wind-up Firion"));
            player.setHasPreOrderArr(doesPlayerHaveMinion(player, "Cait Sith Doll"));
            player.setHasPreOrderHW(doesPlayerHaveMinion(player, "Chocobo Chick Courier"));
            player.setHasPreOrderSB(doesPlayerHaveMinion(player, "Wind-up Red Mage"));
            player.setHasPreOrderShB(doesPlayerHaveMinion(player, "Baby Gremlin"));
            player.setHasARRArtbook(doesPlayerHaveMinion(player, "Model Enterprise"));
            player.setHasHWArtbookOne(doesPlayerHaveMinion(player, "Wind-Up Relm"));
            player.setHasHWArtbookTwo(doesPlayerHaveMinion(player, "Wind-Up Hraesvelgr"));
            player.setHasSBArtbook(doesPlayerHaveMinion(player, "Wind-up Yotsuyu"));
            player.setHasSBArtbookTwo(doesPlayerHaveMinion(player, "Dress-up Tataru"));
            player.setHasEncyclopediaEorzea(doesPlayerHaveMinion(player, "Namingway"));
            player.setHasBeforeMeteor(doesPlayerHaveMinion(player, "Wind-up Dalamud"));
            player.setHasBeforeTheFall(doesPlayerHaveMinion(player, "Set Of Primogs"));
            player.setHasSoundtrack(doesPlayerHaveMinion(player, "Wind-up Bahamut"));
            player.setHasAttendedEternalBond(doesPlayerHaveMinion(player, "Demon Box"));
            player.setHasCompletedHWSightseeing(doesPlayerHaveMinion(player, "Fledgling Apkallu"));
            player.setHasCompleted2pt5(doesPlayerHaveMinion(player, "Midgardsormr"));
            player.setHasFiftyComms(doesPlayerHaveMinion(player, "Princely Hatchling"));
            player.setHasMooglePlush(doesPlayerHaveMinion(player, "Wind-up Delivery Moogle"));
            player.setHasTopazCarbunclePlush(doesPlayerHaveMinion(player, "Heliodor Carbuncle"));
            player.setHasEmeraldCarbunclePlush(doesPlayerHaveMinion(player, "Peridot Carbuncle"));
            player.setHasCompletedHildibrand(doesPlayerHaveMinion(player, "Wind-up Gentleman"));
            player.setHasPS4Collectors(doesPlayerHaveMinion(player, "Wind-up Moogle"));
            player.setHasCompleted3pt1(doesPlayerHaveMinion(player, "Wind-up Haurchefant"));
            player.setHasCompleted3pt3(doesPlayerHaveMinion(player, "Wind-up Aymeric"));
            player.setHasEternalBond(doesPlayerHaveMount(player, "Ceremony Chocobo"));
            player.setHasARRCollectors(doesPlayerHaveMount(player, "Coeurl"));
            player.setHasKobold(doesPlayerHaveMount(player, "Bomb Palanquin"));
            player.setHasSahagin(doesPlayerHaveMount(player, "Cavalry Elbst"));
            player.setHasAmaljaa(doesPlayerHaveMount(player, "Cavalry Drake"));
            player.setHasSylph(doesPlayerHaveMount(player, "Laurel Goobbue"));
            player.setHasMoogle(doesPlayerHaveMount(player, "Cloud Mallow"));
            player.setHasVanuVanu(doesPlayerHaveMount(player, "Sanuwa"));
            player.setHasVath(doesPlayerHaveMount(player, "Kongamato"));
            player.setHasCompletedHW(doesPlayerHaveMount(player, "Midgardsormr"));
            // Main Scenario quest doesn't drop a minion, so instead assume players will at least play one of the Level 70 dungeons and
            // eventually get the minion
            player.setHasCompletedSB(doesPlayerHaveMinion(player, "Ivon Coeurlfist Doll") || doesPlayerHaveMinion(player, "Dress-up Yugiri")
                                     || doesPlayerHaveMinion(player, "Wind-up Exdeath"));
            player.setLegacyPlayer(doesPlayerHaveMount(player, "Legacy Chocobo"));

            // Finalise character info
            player.setActive(isPlayerActiveInDateRange(player));
            player.setCharacterStatus(player.isActive() ? CharacterStatus.ACTIVE : CharacterStatus.INACTIVE);
        } catch(CharacterDeletedException cde) {
            player.setCharacterStatus(CharacterStatus.DELETED);
        }

        Date currentDate = new Date();

        Date lastChecked = player.getLastChecked();
        if(lastChecked == null){
            LOG.warn("Last Checked was null, setting Date 0 as tmp");
            lastChecked = new Date(0);
        }

        Date lastCheckedPlus30 = new Date(lastChecked.getTime() + (ACTIVITY_RANGE_DAYS * ONE_DAY_IN_MILLIS));

        player.setLastChecked(currentDate);
        if(player.hasChanged(tmpExisting) || currentDate.after(lastCheckedPlus30)){
            player.setActive(player.hasChanged(tmpExisting));
        }
        // Update last checked date to today

        return player;
    }

    /**
     * Determine whether a player is active based upon the last modified date of their full body image
     *
     * @return whether player has been active inside the activity window
     */
    private boolean isPlayerActiveInDateRange(final PlayerBean player) {

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();

        Date nowMinusIncludeRange = new Date(t - (ACTIVITY_RANGE_DAYS * ONE_DAY_IN_MILLIS));
        return player.getDateImgLastModified().after(nowMinusIncludeRange); // If the date occurs between the include range and now, then
                                                                            // return true. Else false
    }

    /**
     * Given a lodestone profile page, return the name of the character.
     *
     * @param doc the lodestone profile page.
     * @return the name of the character.
     */
    private String getNameFromPage(final Document doc) {
        String[] parts = doc.title().split(Pattern.quote("|"));
        return parts[0].trim();
    }

    /**
     * Given a lodestone profile page, return the realm of the character.
     *
     * @param doc the lodestone profile page.
     * @return the realm of the character.
     */
    private String getRealmFromPage(final Document doc) {
        // Get elements in the player name area, and return the Realm name (contained in the span)
        String realmAndDatacenter = doc.getElementsByClass(LAYOUT_FRAME_CHARA_WORLD).get(0).text();
        String realm = null;
        if(!StringUtils.isEmpty(realmAndDatacenter)) {
            realm = realmAndDatacenter.substring(0, realmAndDatacenter.indexOf("(") - 1);
        }
        return realm;
    }

    /**
     * Given a lodestone profile page, return the race of the character.
     *
     * @param doc the lodestone profile page.
     * @return the race of the character.
     */
    private String getRaceFromPage(final Document doc) {
        return doc.getElementsByClass(LAYOUT_CHARACTER_BLOCK_NAME).get(0).textNodes().get(0).text().trim();
    }

    /**
     * Given a lodestone profile page, return the gender of the character.
     *
     * @param doc the lodestone profile page.
     * @return the gender of the character.
     */
    private String getGenderFromPage(final Document doc) {
        String[] parts = doc.getElementsByClass(LAYOUT_CHARACTER_BLOCK_NAME).get(0).text().split(Pattern.quote("/"));
        String gender = parts[1].trim();
        if(gender.equals("♂")) {
            return "male";
        } else if(gender.equals("♀")) {
            return "female";
        } else {
            return null;
        }
    }

    /**
     * Given a lodestone profile page, return the grand company of the character.
     *
     * @param doc the lodestone profile page.
     * @return the grand company of the character.
     */
    private String getGrandCompanyFromPage(final Document doc) {
        String gc = null;
        // Get all elements with class chara_profile_box_info
        Elements elements = doc.getElementsByClass(LAYOUT_CHARACTER_BLOCK_BOX);
        if(elements.size() == 5) {
            gc = elements.get(3).getElementsByClass(LAYOUT_CHARACTER_BLOCK_NAME).get(0).text().split("/")[0].trim();
        } else if(elements.size() == 4) {
            if(!elements.get(3).getElementsByClass(LAYOUT_CHARACTER_FREECOMPANY_NAME).isEmpty()) { // If box is fc
                gc = "none";
            } else {
                gc = elements.get(3).getElementsByClass(LAYOUT_CHARACTER_BLOCK_NAME).get(0).text().split("/")[0].trim();
            }
        } else {
            gc = "none";
        }

        return gc;
    }

    /**
     * Given a lodestone profile page, return the free company of the character.
     *
     * @param doc the lodestone profile page.
     * @return the free company of the character.
     */
    private String getFreeCompanyFromPage(final Document doc) {
        String fc = null;
        // Get all elements with class chara_profile_box_info
        Elements elements = doc.getElementsByClass(LAYOUT_CHARACTER_BLOCK_BOX);

        // Checks to see if optional FC has been added
        if(elements.size() == 5) {
            fc = elements.get(4).getElementsByClass(LAYOUT_CHARACTER_FREECOMPANY_NAME).get(0).getElementsByTag(TAG_A).text();
        } else if(elements.size() == 4) { // If only 4 elements present

            if(!elements.get(3).getElementsByClass(LAYOUT_CHARACTER_FREECOMPANY_NAME).isEmpty()) { // If box is fc
                fc = elements.get(3).getElementsByClass(LAYOUT_CHARACTER_FREECOMPANY_NAME).get(0).getElementsByTag(TAG_A).text();
            } else { // Else must not be gc
                fc = "none";
            }
        } else {
            fc = "none";
        }
        return fc;
    }

    /**
     * Given a lodestone profile page, return the levelset of the character.
     *
     * @param doc the lodestone profile page
     * @return the set of levels of the player in the order displayed on the lodestone.
     * @throws Exception Exception thrown if more classes found than anticipated.
     */
    private int[] getLevelsFromPage(final Document doc) {
        // Initialize array list in which to store levels (in order displayed on lodestone)
        List<Integer> levels = new ArrayList<>();

        Element classJobTab = doc.getElementsByClass("character__content").get(0);
        Elements jobLevelElements = classJobTab.getElementsByClass(LAYOUT_CHARACTER_JOB_LEVEL);
        for(int i = 0; i < jobLevelElements.size(); i++) {
            if(i >= 29){ //TODO Endwalker jobs
                continue;
            }
            Element jobLevel = jobLevelElements.get(i);
            String strLvl = jobLevel.text();
            if(strLvl.equals("-")) {
                levels.add(0);
            } else {
                levels.add(Integer.parseInt(strLvl));
            }
        }

        // Initialize int array
        int[] arrLevels = new int[33]; //fix level size to 33
        // Convert array list to array of ints
        for(int index = 0; index < levels.size(); index++) {
            arrLevels[index] = Integer.parseInt(levels.get(index).toString());
        }

        //Eureka
        arrLevels[31] = getOptionalClassLevel(classJobTab,LAYOUT_CHARACTER_JOB_LEVEL_OPTIONAL_EUREKA);
        //Bozja
        arrLevels[32] = getOptionalClassLevel(classJobTab,LAYOUT_CHARACTER_JOB_LEVEL_OPTIONAL_BOZJA);

        // Check if levels array is larger than this system is programmed for
        // As of 5.0, this is now 30 - SCH and SMN are 2 jobs, + SAM, RDM, BLU, GNB, DNC & Eureka
        /*if(arrLevels.length > 32) {
            LOG.warn("Error: More class levels found (" + arrLevels.length
                    + ") than anticipated (31). The class definitions need to be updated.");
        }*/

        return arrLevels;
    }

    private int getOptionalClassLevel(Element classJobTab, String optionalClassName){
        Element bozjaClassElement = classJobTab.getElementsByClass(optionalClassName).first();
        if(bozjaClassElement != null){
            Element bParent = bozjaClassElement.parent();
            if(bParent != null){
                bParent = bParent.parent();
                if(bParent != null){
                    Element jobElement = bParent.getElementsByClass(LAYOUT_CHARACTER_JOB_LEVEL).first();
                    if(jobElement != null){
                        return Integer.parseInt(jobElement.text());
                    }
                }
            }
        }
        return 0;
    }

    private List<Minion> gatherMinions(final PlayerBean player, final Document doc) throws IOException, InterruptedException {
        List<Minion> result = new ArrayList<>();
        // Get minion box element
        Elements minionBoxes = doc.getElementsByClass(LAYOUT_CHARACTER_MINION);
        // Get mounts
        if(!minionBoxes.isEmpty()) {
            Elements minionSet = minionBoxes.get(0).getElementsByTag(TAG_LI);
            for(Element minionEl : minionSet) {
                Optional<Minion> minion = edbCache.getMinionFromTooltip(minionEl.attr("data-tooltip_href"), minionRepository);
                if(minion.isPresent()) {
                    PlayerMinion playerMinion = playerMinionRepository.findByPlayerIdAndMinionId(player.getId(), minion.get().getId());
                    if (playerMinion == null) {
                        playerMinion = PlayerMinion.Create(player.getId(), minion.get().getId());
                        playerMinionRepository.save(playerMinion);
                    }
                    result.add(minion.get());
                } else {
                    LOG.error("Optional Minion is null");
                }
            }
        }
        return result;
    }

    private List<Mount> gatherMounts(final PlayerBean player, final Document doc) throws IOException, InterruptedException {
        List<Mount> result = new ArrayList<>();
        // Get minion box element
        Elements mountBoxes = doc.getElementsByClass(LAYOUT_CHARACTER_MOUNTS);
        // Get mounts
        if(!mountBoxes.isEmpty()) {
            Elements mountSet = mountBoxes.get(0).getElementsByTag(TAG_LI);
            for(Element mountEl : mountSet) {
                Optional<Mount> mount = edbCache.getMountFromTooltip(mountEl.attr("data-tooltip_href"), mountRepository);
                if(mount.isPresent()) {
                    PlayerMount playerMount = playerMountRepository.findByPlayerIdAndMountId(player.getId(), mount.get().getId());
                    if (playerMount == null) {
                        playerMount = PlayerMount.Create(player.getId(), mount.get().getId());
                        playerMountRepository.save(playerMount);
                    }
                    result.add(mount.get());
                }
            }
        }
        return result;
    }


    /**
     * Gets the last-modified date of the Character full body image.
     *
     * @param doc the lodestone profile page to parse
     * @return the date on which the full body image was last modified.
     */
    private Date getDateLastUpdatedFromPage(final Document doc, final int id) {
        Date dateLastModified;
        // Get character image URL.
        String imgUrl = doc.getElementsByClass(LAYOUT_CHARACTER_DETAIL_IMAGE).get(0).getElementsByTag(TAG_A).get(0)
                           .getElementsByTag(TAG_IMG)
                           .get(0).attr(ATTR_SRC);
        String strLastModifiedDate = "";

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.head(imgUrl).asJson();

            strLastModifiedDate = jsonResponse.getHeaders().get(HEADER_LAST_MODIFIED).toString();
        } catch(Exception e) {
            LOG.warn("Setting last-active date to ARR launch date due to an an error loading character " + id
                     + "'s profile image: " + e.getMessage());
            strLastModifiedDate = "[Sat, 24 Aug 2013 00:00:01 GMT]";
        }

        strLastModifiedDate = strLastModifiedDate.replace("[", "");
        strLastModifiedDate = strLastModifiedDate.replace("]", "");
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

        try {
            dateLastModified = dateFormat.parse(strLastModifiedDate);
        } catch(ParseException e) {
            throw new IllegalArgumentException("Could not correctly parse date 'Last-Modified' header from full body image for character id"
                                               + id);
        }
        return dateLastModified;
    }

    private GearSet getGearSet(final PlayerBean player, final Document doc) {
        GearSet gearSet = player.getGearSet();
        if(gearSet == null) {
            gearSet = new GearSet();
        }
        gearSet.setPlayer(player);
        gearSet.setPlayerId(player.getId());

        Element characterView = doc.getElementsByClass("character__view").get(0);

        gearSet.setMainHand(getGearItem(characterView, 0));

        gearSet.setHead(getGearItem(characterView, 2));
        gearSet.setBody(getGearItem(characterView, 3));
        gearSet.setHands(getGearItem(characterView, 4));
        gearSet.setBelt(getGearItem(characterView, 5));
        gearSet.setLegs(getGearItem(characterView, 6));
        gearSet.setFeet(getGearItem(characterView, 7));

        gearSet.setOffHand(getGearItem(characterView, 1));
        gearSet.setEars(getGearItem(characterView, 8));
        gearSet.setNeck(getGearItem(characterView, 9));
        gearSet.setWrists(getGearItem(characterView, 10));
        gearSet.setLeftHand(getGearItem(characterView, 11));
        gearSet.setRightHand(getGearItem(characterView, 12));
        gearSet.setJobCrystal(getGearItem(characterView, 13));

        return gearSet;
    }

    private GearItem getGearItem(final Element characterView, final int itemPosition) {
        Element detail = characterView.getElementsByClass("icon-c--" + itemPosition).get(0);
        Elements toolTips = detail.getElementsByClass("db-tooltip");

        if(toolTips != null && !toolTips.isEmpty()) {
            Element toolTip = toolTips.get(0);

            // Get Eorzea Database ID
            String[] dbUrl = toolTip.getElementsByClass("db-tooltip__bt_item_detail").get(0).getElementsByTag("a").get(0).attr("href")
                                    .split("/");
            String dbId = dbUrl[5];

            Optional<GearItem> itemOpt = gearItemRepository.findById(dbId);
            GearItem item;
            if(!itemOpt.isPresent()) {
                item = new GearItem();
                item.setItemId(dbUrl[5]);
            } else {
                item = itemOpt.get();
            }

            // Get Item Details
            item.setName(toolTip.getElementsByClass("db-tooltip__item__name").get(0).text());
            item.setCategory(toolTip.getElementsByClass("db-tooltip__item__category").get(0).text());
            item.setiLevel(Integer.parseInt(toolTip.getElementsByClass("db-tooltip__item__level").get(0).text().split(" ")[2]));

            if(!gearItemRepository.findById(dbId).isPresent()) {
                gearItemRepository.save(item);
            }

            return item;
        }

        return null;
    }

    /**
     * Sets a Loadestone Page Loader to use.
     * By default, the PlayerBuilder will be initiatsed with a {@link ProductionLodestonePageLoader}.
     * 
     * @param pageLoader the pageLoader to set
     */
    public void setPageLoader(final LodestonePageLoader pageLoader) {
        this.pageLoader = pageLoader;
    }

    /**
     * Sets an instance of the EorzeaDatabaseCache used for read-through caching of lookups from Eorzea Database on the Lodestone.
     * 
     * @param edbCache
     */
    @Autowired
    public void setEorzeaDatabaseCache(final EorzeaDatabaseCache edbCache) {
        this.edbCache = edbCache;
    }

    /**
     * @param gearItemRepository the gearItemRepository to set
     */
    @Autowired
    public void setGearItemRepository(GearItemRepository gearItemRepository) {
        this.gearItemRepository = gearItemRepository;
    }

    /**
     * @param mountRepository the mountRepository to set
     */
    @Autowired
    public void setMountRepository(MountRepository mountRepository) {
        this.mountRepository = mountRepository;
    }

    /**
     * @param minionRepository the minionRepository to set
     */
    @Autowired
    public void setMinionRepository(MinionRepository minionRepository) {
        this.minionRepository = minionRepository;
    }

    @Autowired
    public void setPlayerMountRepository(PlayerMountRepository playerMountRepository) {
        this.playerMountRepository = playerMountRepository;
    }

    @Autowired
    public void setPlayerMinionRepository(PlayerMinionRepository playerMinionRepository) {
        this.playerMinionRepository = playerMinionRepository;
    }
}
