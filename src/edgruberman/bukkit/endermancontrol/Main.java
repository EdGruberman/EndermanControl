package edgruberman.bukkit.endermancontrol;

import java.util.logging.Handler;
import java.util.logging.Level;

import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.setLogLevel(this.getConfig().getString("logLevel"));
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public void setLogLevel(final String name) {
        Level level;
        try { level = Level.parse(name); } catch (final Exception e) {
            level = Level.INFO;
            this.getLogger().warning("Log level defaulted to " + level.getName() + "; Unrecognized java.util.logging.Level: " + name + "; " + e);
        }

        // only set the parent handler lower if necessary, otherwise leave it alone for other configurations that have set it
        for (final Handler h : this.getLogger().getParent().getHandlers())
            if (h.getLevel().intValue() > level.intValue()) h.setLevel(level);

        this.getLogger().setLevel(level);
        this.getLogger().log(Level.CONFIG, "Log level set to: {0} ({1,number,#})"
                , new Object[] { this.getLogger().getLevel(), this.getLogger().getLevel().intValue() });
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(final EntityChangeBlockEvent change) {
        // allow anything but an Enderman to change a block
        if (change.getEntityType() != EntityType.ENDERMAN) return;

        // allow Endermen to change blocks in The End
        if (change.getEntity().getWorld().getEnvironment() == Environment.THE_END) return;

        this.getLogger().log(Level.FINER, "Cancelling Enderman changing block from {0} to {1} at {2}"
                , new Object[] { change.getBlock().getType().name(), change.getTo().name(), change.getBlock() });

        change.setCancelled(true);
    }

}