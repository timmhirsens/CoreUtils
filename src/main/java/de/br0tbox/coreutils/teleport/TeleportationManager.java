package de.br0tbox.coreutils.teleport;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;

/**
 * The {@link TeleportationManager} handles teleport requests.
 * 
 * @author fr1zle
 *
 */
public class TeleportationManager {

	private final Map<Player, Player> teleportationRequests = new HashMap<Player, Player>();

	public void addTeleportRequest(Player toTeleport, Player toPlayer) {
		getTeleportationRequests().put(toTeleport, toPlayer);
		toPlayer.sendMessage("Send a teleport request to player " + toPlayer.getName());
		toTeleport.sendMessage(toPlayer.getName() + " wants you to teleport to him. Type '/tpaccept' to accept his request.");
	}

	public void acceptTeleportRequest(Player acceptor) {
		Player teleportTarget = getTeleportationRequests().get(acceptor);
		if (teleportTarget != null && teleportTarget.getScene() != null) {
			Point position = teleportTarget.getScene().getTransform().getPosition();
			acceptor.teleport(position);
		} else {
			acceptor.sendMessage("There is now teleport request for you, or the player that wanted to teleport you logged off.");
		}
	}

	/**
	 * @return the teleportationRequests
	 */
	public Map<Player, Player> getTeleportationRequests() {
		return teleportationRequests;
	}

}
