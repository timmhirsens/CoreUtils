package de.br0tbox.coreutils.command;

import org.spout.api.Server;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Point;

import de.br0tbox.coreutils.CoreUtils;

/**
 * Basic teleportation commands for server admins and players.
 * 
 * @author fr1zle
 *
 */
public class TeleportCommands {

	private CoreUtils plugin;

	public TeleportCommands(CoreUtils plugin) {
		this.plugin = plugin;
	}

	@Command(aliases = "tpall", desc = "Teleports all players to your position.", min = 0, max = 0)
	@CommandPermissions("coreutils.tpall")
	public void tpAll(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to teleport all players to your position.");
		}
		Point position = ((Player) source).getScene().getTransform().getPosition();
		for (Player toTeleport : ((Server) plugin.getEngine()).getOnlinePlayers()) {
			toTeleport.teleport(position);
		}
	}

	@Command(aliases = "tpaall", desc = "Sends teleport requests to all players.", min = 0, max = 0)
	@CommandPermissions("coreutils.tpaall")
	public void tpAall(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to teleport all players to your position.");
		}
		Player teleportTo = (Player) source;
		for (Player toTeleport : ((Server) plugin.getEngine()).getOnlinePlayers()) {
			plugin.getTeleportationManager().addTeleportRequest(toTeleport, teleportTo);
		}
	}

	@Command(aliases = "tpspawn", desc = "Teleports you to the spawn of your current world.", min = 0, max = 0)
	@CommandPermissions("coreutils.tpspawn")
	public void tpSpawn(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to teleport to spawn.");
		}
		Player player = (Player) source;
		Point spawnPosition = player.getWorld().getSpawnPoint().getPosition();
		if (spawnPosition != null) {
			player.teleport(spawnPosition);
		} else {
			player.sendMessage("The world " + player.getWorld().getName() + " does not have a spawn point.");
		}
	}

	@Command(aliases = "tpa", desc = "Sends a teleportation request to the player.", min = 1, max = 1)
	@CommandPermissions("coreutils.tpa")
	public void tpa(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player send a teleport request.");
		}
		String playerName = args.getString(0);
		Player playerToTeleport = plugin.getEngine().getPlayer(playerName, true);
		if (playerToTeleport == null) {
			throw new CommandException("The player " + playerName + " does not exist or is not online.");
		}
		Player player = (Player) source;
		plugin.getTeleportationManager().addTeleportRequest(playerToTeleport, player);
	}

	@Command(aliases = "tpaccept", desc = "Accepts your latest telportation request.", min = 0, max = 0)
	@CommandPermissions("coreutils.tpaccept")
	public void tpAccept(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player accept a teleport request.");
		}
		plugin.getTeleportationManager().acceptTeleportRequest((Player) source);
	}
}