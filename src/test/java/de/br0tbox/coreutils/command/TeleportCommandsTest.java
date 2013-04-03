package de.br0tbox.coreutils.command;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Before;
import org.junit.Test;
import org.spout.api.Server;
import org.spout.api.command.CommandContext;
import org.spout.api.component.impl.SceneComponent;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;

import de.br0tbox.coreutils.CoreUtils;
import de.br0tbox.coreutils.teleport.TeleportationManager;

public class TeleportCommandsTest {

	@Mocked
	private CoreUtils mockPlugin;
	@Mocked
	private Server mockServer;
	@Mocked
	private Player mockPlayer;
	@Mocked
	private CommandContext commandContext;
	@Mocked
	private World mockWorld;
	@Mocked
	private SceneComponent mockScene;
	@Mocked
	private Transform mockTransform;
	private TeleportCommands teleportCommands;
	private Point point;
	private TeleportationManager teleManager;

	@Before
	public void setUp() {
		teleportCommands = new TeleportCommands(mockPlugin);
		point = new Point(mockWorld, 1, 1, 1);
		teleManager = new TeleportationManager();
		new NonStrictExpectations() {

			{
				mockPlugin.getTeleportationManager();
				result = teleManager;
				mockPlugin.getEngine();
				result = mockServer;
				mockPlayer.getScene();
				result = mockScene;
				mockScene.getTransform();
				result = mockTransform;
				mockTransform.getPosition();
				result = point;
				mockServer.getOnlinePlayers();
				result = new Player[] { mockPlayer };
			}
		};
	}

	@Test
	public void testTpAll() throws Exception {
		teleportCommands.tpAll(commandContext, mockPlayer);
		new Verifications() {
			{
				mockPlayer.teleport(point);
				times = 1;
			}
		};

	}

	@Test
	public void testTpAall() throws Exception {
		teleportCommands.tpAall(commandContext, mockPlayer);
		assertThat(mockPlugin.getTeleportationManager().getTeleportationRequests().size(), is(1));
	}

	@Test
	public void testTpSpawn() throws Exception {
		new NonStrictExpectations() {
			{
				mockPlayer.getWorld();
				result = mockWorld;
				mockWorld.getSpawnPoint();
				result = mockTransform;
			}
		};
		teleportCommands.tpSpawn(commandContext, mockPlayer);
		new Verifications() {
			{
				mockPlayer.teleport(point);
				times = 1;
			}
		};
	}

	@Test
	public void testTpAccept() throws Exception {
		teleManager.addTeleportRequest(mockPlayer, mockPlayer);
		teleportCommands.tpAccept(commandContext, mockPlayer);
		new Verifications() {
			{
				mockPlayer.teleport(point);
				times = 1;
			}
		};
	}

	@Test
	public void testTpa() throws Exception {
		new NonStrictExpectations() {
			{
				mockServer.getPlayer(anyString, true);
				result = mockPlayer;
			}
		};
		teleportCommands.tpa(commandContext, mockPlayer);
		assertThat(teleManager.getTeleportationRequests().size(), is(1));
	}

	@Test(expected = CommandException.class)
	public void testTpaNotOnline() throws Exception {
		new NonStrictExpectations() {
			{
				commandContext.getString(0);
				result = "IsNotOnline";
			}
		};
		teleportCommands.tpa(commandContext, mockPlayer);
		assertThat(teleManager.getTeleportationRequests().size(), is(1));
	}

}
