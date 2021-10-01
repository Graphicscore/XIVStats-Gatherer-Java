package com.ffxivcensus.gatherer.edb;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ffxivcensus.gatherer.player.items.data.Minion;
import com.ffxivcensus.gatherer.player.items.repositories.MinionRepository;
import com.ffxivcensus.gatherer.player.items.data.Mount;
import com.ffxivcensus.gatherer.player.items.repositories.MountRepository;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ffxivcensus.gatherer.lodestone.LodestonePageLoader;
import com.ffxivcensus.gatherer.lodestone.ProductionLodestonePageLoader;

/**
 * Provides a wrapper around accessing Mounts &uml; Minions from the web servers, caching responses so that we don't have to wait on HTTP
 * requests for anything we've already looked up.
 * 
 * @author matthew.hillier
 */
public class EorzeaDatabaseCache {

    /**
     * We should store the mounts and minions in the databse, however we should store a copy of the values in memory for faster access
     */

    private static final Logger LOG = LoggerFactory.getLogger(EorzeaDatabaseCache.class);
    private LodestonePageLoader loader = new ProductionLodestonePageLoader();

    private Map<String, String> minions = new ConcurrentHashMap<>();
    private Map<String, String> mounts = new ConcurrentHashMap<>();

    public String getMinionNameFromTooltip(String dataTooltipHref) throws IOException, InterruptedException {
        String id = dataTooltipHref.substring(dataTooltipHref.lastIndexOf("/") + 1);
        String name = minions.get(id);
        if(name == null) {
            Document doc = loader.getTooltipPage(dataTooltipHref);
            Elements headers = doc.getElementsByClass("minion__header__label");
            if(!headers.isEmpty()) {
                name = headers.get(0).text();
                minions.put(id, name);
                LOG.debug("Cached minion '" + name + "' under ID " + id);
            }
        }
        return name;
    }

    public Minion getMinionFromTooltip(String dataTooltipHref, MinionRepository repository) throws IOException, InterruptedException {
        String id = dataTooltipHref.substring(dataTooltipHref.lastIndexOf("/") + 1);
        Minion minion = null;
        //get mount name from memory cache
        String mountName = mounts.get(id);
        if(mountName == null){
            //get mount from database
            minion = repository.findOne(id);
            if(minion == null){
                //read mount name from website and save to db
                Document doc = loader.getTooltipPage(dataTooltipHref);
                Elements headers = doc.getElementsByClass("minion__header__label");
                if(!headers.isEmpty()) {
                    mountName = headers.get(0).text();

                    minion = new Minion();
                    minion.setId(id);
                    minion.setName(mountName);
                    try {
                        repository.save(minion);
                    } catch (Exception ex){
                        LOG.error("Error caching minion {}", ex.getMessage());
                    }
                }
            }
            if(minion != null) {
                mounts.put(minion.getId(), minion.getName());
                LOG.debug("Cached minion {}", minion);
            }
        } else {
            minion = new Minion();
            minion.setId(id);
            minion.setName(mountName);
        }
        return minion;
    }

    public Mount getMountFromTooltip(String dataTooltipHref, MountRepository repository) throws IOException, InterruptedException {
        String id = dataTooltipHref.substring(dataTooltipHref.lastIndexOf("/") + 1);
        Mount mount = null;
        //get mount name from memory cache
        String mountName = mounts.get(id);
        if(mountName == null){
            //get mount from database
            mount = repository.findOne(id);
            if(mount == null){
                //read mount name from website and save to db
                Document doc = loader.getTooltipPage(dataTooltipHref);
                Elements headers = doc.getElementsByClass("mount__header__label");
                if(!headers.isEmpty()) {
                    mountName = headers.get(0).text();

                    mount = new Mount();
                    mount.setId(id);
                    mount.setName(mountName);
                    try {
                        repository.save(mount);
                    } catch (Exception ex){
                        LOG.error("Error caching mount {}", ex.getMessage());
                    }
                }
            }
            if(mount != null) {
                mounts.put(mount.getId(), mount.getName());
                LOG.debug("Cached mount {}", mount);
            }
        } else {
            mount = new Mount();
            mount.setId(id);
            mount.setName(mountName);
        }
        return mount;
    }

    public String getMountNameFromTooltip(String dataTooltipHref) throws IOException, InterruptedException {
        String id = dataTooltipHref.substring(dataTooltipHref.lastIndexOf("/") + 1);
        String name = mounts.get(id);
        if(name == null) {
            Document doc = loader.getTooltipPage(dataTooltipHref);
            Elements headers = doc.getElementsByClass("mount__header__label");
            if(!headers.isEmpty()) {
                name = headers.get(0).text();
                mounts.put(id, name);
                LOG.debug("Cached mount '" + name + "' under ID " + id);
            }
        }
        return name;
    }
}
