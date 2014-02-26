package twitchirc.queues;

import java.util.ArrayList;

/**
 * Queues commands and sends them in order
 * @author tmrlvi
 */
public class DirectCommands implements CommandQueue {
	ArrayList<String> commands;
	
	public DirectCommands(){
		commands = new ArrayList<String>();
	}

	@Override
	public void add(String command) {
		commands.add(command);

	}

	@Override
	public String get() {
		if (commands.size() == 0)
			return null;
		String command  = commands.get(0);
		commands.remove(0);
		return command;
	}

	@Override
	public boolean remove(String command) {
		return false;
	}

}
