package de.br0tbox.coreutils.command;

import java.util.Collections;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Before;
import org.junit.Test;
import org.spout.api.command.CommandContext;
import org.spout.api.component.impl.SceneComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Transform;
import org.spout.vanilla.component.entity.living.Living;

import de.br0tbox.coreutils.CoreUtils;

public class ButcherCommandTest {

	@Mocked
	private CoreUtils mockPlugin;
	@Mocked
	private Player mockPlayer;
	@Mocked
	private SceneComponent mockScene;
	@Mocked
	private Transform mockTransform;
	@Mocked
	private CommandContext mockCommandContext;
	@Mocked
	private World mockWorld;
	@Mocked
	private Entity mockEntity;
	@Mocked
	private Living mockLiving;

	@Before
	public void setUp() {
		new NonStrictExpectations() {
			{
				mockPlayer.getScene();
				result = mockScene;
				mockScene.getTransform();
				result = mockTransform;
				mockPlayer.getWorld();
				result = mockWorld;
			}
		};
	}

	@Test
	public void testButcher() throws Exception {
		new NonStrictExpectations() {
			{
				mockCommandContext.length();
				result = 1;
				mockCommandContext.getInteger(0);
				result = 50;
				mockWorld.getNearbyEntities(mockPlayer, 50);
				result = Collections.singletonList(mockEntity);
				mockEntity.get(Living.class);
				result = mockLiving;
			}
		};
		ButcherCommand butcherCommand = new ButcherCommand(mockPlugin);
		butcherCommand.butcher(mockCommandContext, mockPlayer);
		new Verifications() {
			{
				mockEntity.remove();
				times = 1;
			}
		};
	}
}
