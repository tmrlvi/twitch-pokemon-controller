package twitchirc.queues;

/**
 * Sends the mode as a command
 * @author tmrlvi
 */
public class ModeCommand implements CommandQueue {
	private final static String MODE = "anarchy";

	@Override
	public void add(String command) {
	}

	@Override
	public String get() {
		return MODE;
	}

	@Override
	public boolean remove(String command) {
		return false;
	}

}
