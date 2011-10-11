package edgruberman.bukkit.endermancontrol;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EndermanPlaceEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.plugin.Plugin;

import edgruberman.bukkit.messagemanager.MessageLevel;
import edgruberman.bukkit.messagemanager.channels.Channel;

final class Enforcer extends EntityListener {

    Enforcer(final Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvent(Event.Type.ENDERMAN_PICKUP, this, Event.Priority.Normal, plugin);
        plugin.getServer().getPluginManager().registerEvent(Event.Type.ENDERMAN_PLACE, this, Event.Priority.Normal, plugin);
    }
    
    @Override
    public void onEndermanPickup(final EndermanPickupEvent pickup) {
        if (pickup.isCancelled()) return;
        
        Main.messageManager.log("Denying Enderman pickup of " + pickup.getBlock().getType().name() + " at"
                + " x:" + pickup.getBlock().getX()
                + " y:" + pickup.getBlock().getY()
                + " z:" + pickup.getBlock().getZ()
                , MessageLevel.FINER);
        
        pickup.setCancelled(true);
    }
    
    @Override
    public void onEndermanPlace(final EndermanPlaceEvent place) {
        if (place.isCancelled()) return;
        
        if (Main.messageManager.isLevel(Channel.Type.LOG, MessageLevel.FINER)) {
            Main.messageManager.log("Denying Enderman place of "
                    + Material.getMaterial(place.getLocation().getWorld().getBlockTypeIdAt(place.getLocation())).name() + " at"
                    + " x:" + place.getLocation().getBlockX()
                    + " y:" + place.getLocation().getBlockY()
                    + " z:" + place.getLocation().getBlockZ()
                    , MessageLevel.FINER);
        }
        
        place.setCancelled(true);
    }
}
