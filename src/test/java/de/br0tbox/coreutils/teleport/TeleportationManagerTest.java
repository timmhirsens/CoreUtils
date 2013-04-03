package de.br0tbox.coreutils.teleport;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Before;
import org.junit.Test;
import org.spout.api.component.impl.SceneComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;

public class TeleportationManagerTest {

	private Point point;
	@Mocked
	private World world;
	@Mocked
	private Player player;
	@Mocked
	private SceneComponent scene;
	@Mocked
	private Transform transform;

	@Before
	public void setUp() {
		point = new Point(world, 1, 1, 1);
		new NonStrictExpectations() {
			{
				player.getScene();
				result = scene;
				scene.getTransform();
				result = transform;
				transform.getPosition();
				result = point;
			}
		};
	}

	@Test
	public void testAddTeleportRequest() throws Exception {
		TeleportationManager teleportationManager = new TeleportationManager();
		teleportationManager.addTeleportRequest(player, player);
		assertThat(teleportationManager.getTeleportationRequests().size(), is(1));
	}

	@Test
	public void testAcceptTeleportRequest() throws Exception {
		TeleportationManager teleportationManager = new TeleportationManager();
		teleportationManager.addTeleportRequest(player, player);
		teleportationManager.acceptTeleportRequest(player);
		new Verifications() {
			{
				player.teleport(point);
				times = 1;
			}
		};
	}

	@Test
	public void testAcceptTeleportRequestNotOnline() throws Exception {
		TeleportationManager teleportationManager = new TeleportationManager();
		teleportationManager.acceptTeleportRequest(player);
		new Verifications() {
			{
				player.teleport(point);
				times = 0;
				player.sendMessage("There is now teleport request for you, or the player that wanted to teleport you logged off.");
				times = 1;
			}
		};
	}

}
