package de.br0tbox.coreutils;

import org.spout.api.UnsafeMethod;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.RootCommand;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.Plugin;

import de.br0tbox.coreutils.command.TeleportCommands;
import de.br0tbox.coreutils.teleport.TeleportationManager;

public class CoreUtils extends CommonPlugin {

	private TeleportationManager teleportationManager;

	@Override
	@UnsafeMethod
	public void onEnable() {
		teleportationManager = new TeleportationManager();
		final CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(getEngine(), new SimpleInjector(this), new SimpleAnnotatedCommandExecutorFactory());
		final RootCommand root = getEngine().getRootCommand();
		root.addSubCommands(this, TeleportCommands.class, commandRegFactory);
		Plugin vanilla = getEngine().getPluginManager().getPlugin("Vanilla");
		if (vanilla != null) {
			getLogger().info("Found Vanilla, enabling features.");
		}
		getLogger().info("enabled");
	}

	@Override
	@UnsafeMethod
	public void onDisable() {
		teleportationManager = null;
		getLogger().info("disabled");
	}

	/**
	 * Returns the {@link TeleportationManager} of the Plugin.
	 * 
	 * @return the teleportationManager
	 */
	public TeleportationManager getTeleportationManager() {
		return teleportationManager;
	}

}
