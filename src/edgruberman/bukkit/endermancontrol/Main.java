package edgruberman.bukkit.endermancontrol;

import java.util.logging.Handler;
import java.util.logging.Level;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.setLoggingLevel();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    private void setLoggingLevel() {
        this.reloadConfig();
        final String name = this.getConfig().getString("logLevel", "INFO");
        Level level;
        try { level = Level.parse(name); } catch (final Exception e) {
            level = Level.INFO;
            this.getLogger().warning("Unrecognized java.util.logging.Level in \"" + this.getDataFolder().getPath() + "\\config.yml\"; logLevel: " + name);
        }

        // Only set the parent handler lower if necessary, otherwise leave it alone for other configurations that have set it.
        for (final Handler h : this.getLogger().getParent().getHandlers())
            if (h.getLevel().intValue() > level.intValue()) h.setLevel(level);

        this.getLogger().setLevel(level);
        this.getLogger().log(Level.CONFIG, "Logging level set to: " + this.getLogger().getLevel());
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(final EntityChangeBlockEvent change) {
        if (change.getEntityType() != EntityType.ENDERMAN) return;

        if (this.getLogger().isLoggable(Level.FINER))
            this.getLogger().log(Level.FINER, "Denying Enderman changing block"
                    + " from " + change.getBlock().getType().name()
                    + " to " + change.getTo().name()
                    + " at"
                    + " x:" + change.getBlock().getX()
                    + " y:" + change.getBlock().getY()
                    + " z:" + change.getBlock().getZ()
            );

        change.setCancelled(true);
    }

}