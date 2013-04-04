package de.br0tbox.coreutils.command;

import java.util.List;

import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.vanilla.component.entity.living.Living;

import de.br0tbox.coreutils.CoreUtils;

public class ButcherCommand {

	private CoreUtils plugin;

	public ButcherCommand(CoreUtils plugin) {
		this.plugin = plugin;
	}

	@Command(aliases = "butcher", desc = "Kills all NPCs in the given Radius.", min = 1, max = 1)
	public void butcher(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to butcher NPCs.");
		}
		Player player = (Player) source;
		if (args.length() < 1) {
			throw new CommandException("Insufficent arguments.");
		}
		int radius = args.getInteger(0);
		List<Entity> entitiesToKill = player.getWorld().getNearbyEntities(player, radius);
		int removedEntities = 0;
		for (Entity entity : entitiesToKill) {
			if (entity instanceof Player) {
				continue;
			}
			if (entity.get(Living.class) != null) {
				entity.remove();
				removedEntities++;
			}

		}
		player.sendMessage("Removed " + removedEntities + " Entities.");
	}

}
