package cat.nyaa.frostkiller;

import cat.nyaa.nyaacore.CommandReceiver;
import cat.nyaa.nyaacore.ILocalizer;
import cat.nyaa.nyaacore.Message;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FrostKillerCommand extends CommandReceiver {

    private Configuration configuration;

    public FrostKillerCommand(JavaPlugin plugin, ILocalizer _i18n) {
        super(plugin, _i18n);
        configuration = FrostKillerPlugin.plugin.configuration;
    }

    @Override
    public String getHelpPrefix() {
        return null;
    }

    @SubCommand(value = "reload", permission = "frostkiller.admin")
    public void onReload(CommandSender sender, Arguments arguments){
        FrostKillerPlugin.plugin.reload();
    }

    @SubCommand(value = "enable", permission = "frostkiller.admin")
    public void enable(CommandSender sender, Arguments arguments) {
        configuration.enabled = !configuration.enabled;
        if (configuration.enabled) {
            new Message(I18n.format("enabled"))
                    .send(sender);
        } else {
            new Message(I18n.format("disabled"))
                    .send(sender);
        }
    }

    @SubCommand(value = "smelt", permission = "frostkiller.admin")
    public void onSmelt(CommandSender sender, Arguments arguments) {
        if (arguments.top() != null) {
            String s = arguments.nextString();
            World world = sender.getServer().getWorld(s);
            if (world == null) {
                new Message(I18n.format("error.no_world", s))
                        .send(sender);
                return;
            }
            Chunk[] loadedChunks = world.getLoadedChunks();
            new SmeltTask(loadedChunks, sender)
                    .runTaskLaterAsynchronously(FrostKillerPlugin.plugin, 0);
        } else if (sender instanceof Player) {
            World world = ((Player) sender).getWorld();
            Chunk[] loadedChunks = world.getLoadedChunks();
            new SmeltTask(loadedChunks, sender)
                    .runTaskLaterAsynchronously(FrostKillerPlugin.plugin, 0);
        } else if (sender instanceof BlockCommandSender) {
            World world = ((BlockCommandSender) sender).getBlock().getWorld();
            Chunk[] loadedChunks = world.getLoadedChunks();
            new SmeltTask(loadedChunks, sender)
                    .runTaskLaterAsynchronously(FrostKillerPlugin.plugin, 0);
        } else {
            new Message("error.not_valid")
                    .send(sender);
        }
    }

    static class SmeltTask extends BukkitRunnable {
        Chunk[] chunks;
        private CommandSender sender;
        List<BukkitRunnable> foundFrostIce = new ArrayList<>();

        SmeltTask(Chunk[] chunks, CommandSender sender) {
            this.chunks = chunks;
            this.sender = sender;
        }


        @Override
        public void run() {
            for (Chunk chunk : chunks) {
                if (chunk != null) {
                    ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot(true, false, false);
                    List<Location> blockLocations = new ArrayList<>();
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = chunkSnapshot.getHighestBlockYAt(x, z); y > 0; y--) {
                                if (y >= 256) {
                                    continue;
                                }
                                Material blockType = chunkSnapshot.getBlockType(x, y, z);
                                if (blockType.equals(Material.FROSTED_ICE)) {
                                    blockLocations.add(new Location(chunk.getWorld(), x, y, z));
                                }
                            }
                        }
                    }
                    int index = foundFrostIce.size();
                    AtomicInteger blockCount = new AtomicInteger(0);
                    if (blockLocations.size() > 0) {
                        foundFrostIce.add(new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!blockLocations.isEmpty()) {
                                    blockCount.getAndAdd(blockLocations.size());
                                    blockLocations.forEach(location -> {
                                        Block block = chunk.getBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                                        if (block.getType().equals(Material.FROSTED_ICE)) {
                                            block.setType(Material.WATER);
                                        }
                                    });
                                    if (index == foundFrostIce.size() - 1) {
                                        new Message(I18n.format("clear_finished", index, blockCount.intValue())).send(sender);
                                    }
                                }
                            }
                        });
                    }
                }
            }
            for (int i = 0; i < foundFrostIce.size(); i++) {
                foundFrostIce.get(i).runTaskLater(FrostKillerPlugin.plugin, i);
            }
        }
    }
}
