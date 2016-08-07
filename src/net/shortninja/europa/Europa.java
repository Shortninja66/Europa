package net.shortninja.europa;

import java.util.UUID;

import net.shortninja.europa.scoreboard.BoardHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Europa implements Listener
{
	private static JavaPlugin plugin;
	private String title;
	private BoardHandler boardHandler;
	
	/**
	 * @param plugin Your plugin instance. This is used to register events and for
	 * bukkit runnables.
	 * @param title The default title for all scoreboards. This can be changed later.
	 * @param shouldListen Whether or not PlayerJoin and PlayerQuit events will be
	 * registered. If not, scoreboards will NOT automatically be created and
	 * destroyed for players.
	 */
	public Europa(JavaPlugin plugin, String title, boolean shouldListen)
	{
		Europa.plugin = plugin;
		this.title = colorize(title);
		this.boardHandler = new BoardHandler();
		
		if(shouldListen)
		{
			Bukkit.getPluginManager().registerEvents(this, plugin);
		}
	}
	
	/**
	 * This constructor will automatically handle PlayerJoin and PlayerQuit events.
	 * 
	 * @param plugin Your plugin instance. This is used to register events.
	 * @param title The default title for all scoreboards. This can be changed later.
	 */
	public Europa(JavaPlugin plugin, String title)
	{
		this(plugin, title, true);
	}
	
	/**
	 * @return The plugin that was given for this instance of Europa.
	 */
	public JavaPlugin getPlugin()
	{
		return plugin;
	}
	
	/**
	 * @return The current title for scoreboards.
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * @return The BoardHandler instance from Europa.
	 */
	public BoardHandler getBoardHandler()
	{
		return boardHandler;
	}
	
	/**
	 * @param title The new title for scoreboards.
	 * @param shouldUpdate Whether or not all scoreboards will be updated with this
	 * new title.
	 */
	public void setTitle(String title, boolean shouldUpdate)
	{
		if(shouldUpdate)
		{
			// TODO: Update all other boards with this title.
		}
		
		this.title = title;
	}
	
	/**
	 * @param string String to colorize.
	 * @return Turns regular '&' characters to '§' for color ingame.
	 */
	public String colorize(String string)
	{
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	public void registerBoards()
	{
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent event)
	{
		UUID uuid = event.getPlayer().getUniqueId();
		
		if(boardHandler.has(uuid))
		{
			boardHandler.unregister(boardHandler.get(uuid));
		}
		
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onQuit(PlayerQuitEvent event)
	{
		
	}
}