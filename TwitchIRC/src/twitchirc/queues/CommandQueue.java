package twitchirc.queues;

/**
 * Handles the different command methods
 * @author tmrlvi
 */
public abstract interface CommandQueue {
	/**
	 * Add a command to the list
	 * @param command
	 */
	public void add(String command);
	/**
	 * Get the next command
	 * @return - the next command
	 */
	public String get();
	/**
	 * Remove a command from the list
	 * @param command - the command to remove
	 * @return - whether it was successful
	 */
	public boolean remove(String command);
}
