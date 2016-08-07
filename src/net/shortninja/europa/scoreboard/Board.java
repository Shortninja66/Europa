package net.shortninja.europa.scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import net.shortninja.europa.Europa;
import net.shortninja.europa.scoreboard.attribute.Entry;
import net.shortninja.europa.scoreboard.attribute.Modifier.CountType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Board
{
	private Europa europa;
	private UUID uuid;
    private Map<Entry, String> entryKeys = new HashMap<Entry, String>();
    private Map<Entry, Integer> entryScores =  new HashMap<Entry, Integer>();
    private Map<String, Entry> entries = new TreeMap<String, Entry>();
    private Scoreboard scoreboard;
    private Objective objective;
    private BukkitTask task;
	
	public Board(Europa europa, UUID uuid)
	{
		this.europa = europa;
		this.uuid = uuid;
		
		create();
		task = initialize();
	}
	
	/**
	 * @return The instance of Europa that this board was registered from.
	 */
	public Europa getInstance()
	{
		return europa;
	}
	
	/**
	 * @return The UUID object that this Board is coordinated with.
	 */
	public UUID getUuid()
	{
		return uuid;
	}
	
	/**
	 * Uses Bukkit#getPlayer(UUID) to get the player.
	 * 
	 * @return The player from the Board's uuid.
	 */
	public Player getPlayer()
	{
		return Bukkit.getPlayer(uuid);
	}
	
	/**
	 * Used to update entry text with a unique string in order to prevent 
	 * scoreboard flickering.
	 * 
	 * @param entry Entry to base the string off of.
	 * @return A new unique string.
	 */
	public String getUpdatedString(Entry entry)
	{
		String text = "";
		
		for(ChatColor color : ChatColor.values())
		{
			if(entry.getCurrentText().length() >= 16)
			{
				text = color + "" + ChatColor.WHITE + ChatColor.getLastColors(entry.getCurrentText().substring(0, 16));
			}else text = color + "" + ChatColor.WHITE + ChatColor.getLastColors(entry.getCurrentText());

			if(!(entryKeys.values().contains(text)))
			{
				entryKeys.put(entry, text);
				break;
			}
		}
		
		return text;
	}
	
	/**
	 * @param entry Entry to get the score for.
	 * @return The score that this entry should have.
	 */
	public int getScore(Entry entry)
	{
		int score = 0;
		
		for(int i = 0; i < entries.size() + 1; i++)
		{
			if(entryScores.containsKey(entry))
			{
				if(entryScores.get(entry) == i + 1)
				{
					if(!entryScores.values().contains(i) && i > 0)
					{
						entryScores.put(entry, i);
						score = i;
						break;
					}

					score = i + 1;
					break;
				}
			}else
			{
				entryScores.put(entry, i + 1);
				score = i + 1;
				break;
			}
		}

		return score;
	}
	
	/**
	 * @param identifier The Entry's identifier.
	 * @return The found entry. Can be null if the identifier does not match with
	 * an Entry.
	 */
	public Entry getEntry(String identifier)
	{
		return entries.get(identifier);
	}
	
	/**
	 * @return All entries attached to this instance.
	 */
	public Collection<Entry> getEntries()
	{
		return entries.values();
	}
	
	/**
	 * @return This Board's Bukkit Scoreboard instance.
	 */
	public Scoreboard getScoreboard()
	{
		return scoreboard;
	}
	
	/**
	 * @return This Board's Bukkit Objective instance.
	 */
	public Objective getObjective()
	{
		return objective;
	}
	
	/**
	 * Checks whether or not the Entry is cancelled and acts accordingly to assure
	 * that the Entry is removed from memory. This is called on every timer
	 * iteration, so you really don't need to use it that much.
	 * 
	 * @param entry The Entry to check for.
	 * @return Whether or not this Entry has been removed from the board.
	 */
	public boolean updateCancelled(Entry entry)
	{
		boolean shouldRemove = false;
		
		if(!entry.isActive())
		{
			scoreboard.resetScores(entry.getKey());
			entryKeys.remove(entry);
			entryScores.remove(entry);
			entry.getBoard().entries.remove(entry.getIdentifier());
		}
		
		return shouldRemove;
	}
	
	/**
	 * Checks entry's CountType to determine how it should be updated. If it has
	 * NONE as the CountType, it will update with a unique string.
	 * 
	 * @param entry Entry to update.
	 * @return Whether or not the text was updated (besides just unique string
	 * change).
	 */
	public boolean updateText(Entry entry)
	{
		boolean hasUpdated = true;
		CountType countType = entry.getCountType();
		String currentText = entry.getCurrentText();
		
		if(countType == CountType.NONE)
		{
			entry.sendUpdate(currentText);
			hasUpdated = false;
		}else
		{
			if(entry.getTime().doubleValue() >= 0)
			{
				int currentTime = entry.getTime().intValue();
				String nextText = currentText;
				
				if(currentTime < 3600)
				{
					
				}else if(currentTime < 60)
				{
					
				} 
			}else entry.setActive(false);
		}
		
		return hasUpdated;
	}
	
	/**
	 * Goes through the process of removing this scoreboard from the player and
	 * from memory.
	 */
	public void destroy()
	{
		Player player = getPlayer();
		
		task.cancel();
		
		for(Entry entry : getEntries())
		{
			entry.setActive(false);
			updateCancelled(entry);
		}
		
		if(player != null)
		{
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}
	
	private void create()
	{
		Player player = getPlayer();
		
		if(player == null)
		{
			return;
		}
		
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective(player.getName(), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(europa.getTitle());
	}
	
	private BukkitTask initialize()
	{
		BukkitTask task = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(Entry entry : getEntries())
				{
					String currentText = entry.getCurrentText();
					
					updateCancelled(entry);
					updateText(entry);
				}
			}
		}.runTaskTimer(europa.getPlugin(), 2L, 2L);
		
		return task;
	}
}