package me.ibcodin.plugins.securezone.commands;

import me.ibcodin.plugins.securezone.IntVector;
import me.ibcodin.plugins.securezone.SecureZone;
import me.ibcodin.plugins.securezone.SecureZoneZone;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * CommandExecutor for /securezonevisit
 * 
 * @author IBCodin
 */
public class CommandVisit implements CommandExecutor {

	private SecureZone plugin = null;

    final static private String zoneIgnorePermission;
    final static private String zonePrefix;

    static {
        zoneIgnorePermission = "securezone.ignorezones";
        zonePrefix = "securezone.";
    }

    /**
	 * @param plugin
	 *            Reference to SecureZone plugin
	 */
	public CommandVisit(SecureZone plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be an in-game player to run this command.");
            return false;
        }

        final Player player = (Player) sender;

        if (args.length != 1) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "This command requires a one word zone name.");
            return false;
        }

        final String zoneName = args[0];
        final SecureZoneZone zone = plugin.getZone(zoneName);

        if (zone == null) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Unknown zone: " + zoneName);
            return false;
        }

        if (!sender.hasPermission(zoneIgnorePermission) &&
                !sender.hasPermission(zonePrefix + zone.getName())) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "No permission to visit zone: " + zone.getName());
            return false;
        }

        final IntVector visitVector = zone.visitloc();

        final World world = plugin.getServer().getWorld(zone.getWorld());
        if (world == null) {
            sender.sendMessage(String.format(ChatColor.LIGHT_PURPLE + "Zone: %s World: %s: World not loaded", zone.getName(), zone.getWorld()));
            return false;
        }

        // this block is at the 'center' of the zone
        Block toBlock = world.getBlockAt(visitVector.getX(),
                visitVector.getY(), visitVector.getZ());

        // move down until the block below us is solid
        Block nextBlock = toBlock.getRelative(BlockFace.DOWN);
        int nextBlockTypeId = nextBlock.getTypeId();
        while ((nextBlockTypeId == 0)
                || ((nextBlockTypeId > 7) && (nextBlockTypeId < 12))) {
            toBlock = nextBlock;
            nextBlock = toBlock.getRelative(BlockFace.DOWN);
            nextBlockTypeId = nextBlock.getTypeId();
        }

        // move up until we have 2 clear blocks (to stand
        // in)
        nextBlock = toBlock.getRelative(BlockFace.UP);
        while ((toBlock.getTypeId() != 0)
                || (nextBlock.getTypeId() != 0)) {
            toBlock = nextBlock;
            nextBlock = toBlock.getRelative(BlockFace.UP);
        }

        // toBlock should be at the lowest point with 2
        // spaces over it (I hope)
        player.teleport(toBlock.getLocation());
        return true;
	}
}
