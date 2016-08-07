package net.shortninja.europa.scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoardHandler
{
	private static Map<UUID, Board> boards = new HashMap<UUID, Board>();
	
	/**
	 * @param uuid The uuid to query for.
	 * @return The Board object associated with the given UUID. Can be null if the
	 * board is not registered.
	 */
	public Board get(UUID uuid)
	{
		return boards.get(uuid);
	}
	
	/**
	 * @param uuid The UUID to query for.
	 * @return Whether or not the given UUID has been registered with a Board.
	 */
	public boolean has(UUID uuid)
	{
		return boards.containsKey(uuid);
	}
	
	/**
	 * @param board The board to register. Handles creation of the board as well.
	 */
	public void register(Board board)
	{
		boards.put(board.getUuid(), board);
	}
	
	/**
	 * @param board The board to unregister. Handles destruction of the board as 
	 * well.
	 */
	public void unregister(Board board)
	{
		board.destroy();
		boards.remove(board.getUuid());
	}
}