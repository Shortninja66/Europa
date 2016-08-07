package net.shortninja.europa.scoreboard.attribute;

import java.math.BigDecimal;

import net.shortninja.europa.scoreboard.Board;
import net.shortninja.europa.scoreboard.attribute.Modifier.CountType;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Entry
{
	private Board board;
	private String identifier;
	private String key;
	private CountType countType;
	private String originalText;
	private String currentText;
	private BigDecimal time;
	private int interval;
	private boolean isActive = false;
    private Team team;
	
	public Entry(Board board, String identifier)
	{
		this.board = board;
		this.identifier = identifier;
	}
	
	/**
	 * @return The Board that this Entry is set to display on.
	 */
	public Board getBoard()
	{
		return board;
	}
	
	/**
	 * @return The given identifier for this Entry.
	 */
	public String getIdentifier()
	{
		return identifier;
	}
	
	/**
	 * @return The key used to query for this Entry.
	 */
	public String getKey()
	{
		return key;
	}
	
	/**
	 * @return The current count type for this Entry.
	 */
	public CountType getCountType()
	{
		return countType;
	}
	
	/**
	 * @return The text that this Entry initially had set.
	 */
	public String getOriginalText()
	{
		return originalText;
	}
	
	/**
	 * @return The text that this Entry will display for the next timer iteration.
	 */
	public String getCurrentText()
	{
		return currentText;
	}
	
	/**
	 * @return The time that this Entry will display for the next timer iteration.
	 */
	public BigDecimal getTime()
	{
		return time;
	}
	
	/**
	 * @return This Entry's current interval for timing.
	 */
	public int getInterval()
	{
		return interval;
	}
	
	/**
	 * @return Whether or not this Entry is set to be updated in the next timer 
	 * iteration.
	 */
	public boolean isActive()
	{
		return isActive;
	}
	
	/**
	 * In order to return valid, the current text must not be null and it's length
	 * must not be greater than 32 characters.
	 * 
	 * @return Whether or not the current text is able to be displayed.
	 */
	public boolean isValid()
	{
		boolean isValid = true;
		
		if(currentText == null)
		{
			isValid = false;
		}else if(currentText.length() > 32)
		{
			isValid = false;
		}
		
		return isValid;
	}
	
	/**
	 * @param countType The CountType that this Entry will use for timers.
	 * @return Current Entry instance.
	 */
	public Entry setCountType(CountType countType)
	{
		this.countType = countType;
		
		return this;
	}
	
	/**
	 * No need to use coloring methods for your string, this method handles that.
	 * 
	 * @param text The string that will be set as the current text for this Entry.
	 * @return Current Entry instance.
	 */
	public Entry setText(String text)
	{
		text = board.getInstance().colorize(text);
		currentText = text;
		
		if(currentText.isEmpty())
		{
			originalText = text;
		}
		
		return this;
	}
	
	/**
	 * BigDecimal is used because its precision will work better for timers.
	 * 
	 * @param time The time to set as the current time.
	 * @return Current Entry instance.
	 */
    public Entry setTime(BigDecimal time)
    {
        this.time = time;
        
        return this;
    }
    
	/**
	 * Interval is used to determine when an Entry's time will change from minutes
	 * to seconds and etc.
	 * 
	 * @param interval The interval to set to.
	 * @return Current Entry instance.
	 */
    public Entry setInterval(int interval)
    {
    	this.interval = interval;
    	
    	return this;
    }
    
    /**
     * @param isActive Whether or not the Entry is updating time.
     * @return Current Entry instance.
     */
    public Entry setActive(boolean isActive)
    {
    	this.isActive = isActive;
    	
    	return this;
    }
    
	/**
	 * Attempts to setup this Entry for its appearance on the parent Board. This
	 * can fail if there are more than fifteen entries.
	 * 
	 * @return Current Entry instance.
	 */
	public Entry send()
	{
		if(board.getEntries().size() < 15)
		{
			setup();
			isActive = true;
		}
		
		return this;
	}
    
    /**
     * Sets up the entry by formatting the Bukkit Team text.
     */
	public void setup()
	{
		Scoreboard scoreboard = board.getScoreboard();
		String teamName = identifier;
		
		key = board.getUpdatedString(this);

		if(teamName.length() > 16)
		{
			teamName = teamName.substring(0, 16);
		}

		if(scoreboard.getTeam(teamName) != null)
		{
			team = scoreboard.getTeam(teamName);
		}else team = scoreboard.registerNewTeam(teamName);
		
		if(!team.getEntries().contains(key))
		{
			team.addEntry(key);
		}
	}
	
	/**
	 * Attempts to send an update to the Bukkit Scoreboard with the current text.
	 * This can fail if the Board player is null.
	 * 
	 * @param text The text to update the Entry with.
	 */
	public void sendUpdate(String text)
	{
		Objective objective = board.getObjective();
		Score score = objective.getScore(key);
		String suffix = "";
		Player player = board.getPlayer();
		
		if(text.length() > 16)
		{
			team.setPrefix(text.substring(0, 16));
			suffix = ChatColor.getLastColors(team.getPrefix()) + text.substring(16, text.length());

			if(suffix.length() > 16)
			{
				if((suffix.length() - 2) <= 16)
				{
					suffix = text.substring(16, text.length());
					team.setSuffix(suffix.substring(0, suffix.length()));
				}else team.setSuffix(suffix.substring(0, 16));
			}else team.setSuffix(suffix);
		}else team.setPrefix(text);
		
		score.setScore(board.getScore(this));

		if(player != null)
		{
			board.getPlayer().setScoreboard(board.getScoreboard());
			currentText = text;
		}
	}
}