package net.shortninja.europa;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin
{
	private static Plugin plugin;
	public Europa europa;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		europa = new Europa(plugin, "ShitHCF Map 1.0", true);
	}
	
	@Override
	public void onDisable()
	{
		plugin = null;
	}
	
	public static Plugin get()
	{
		return plugin;
	}
}