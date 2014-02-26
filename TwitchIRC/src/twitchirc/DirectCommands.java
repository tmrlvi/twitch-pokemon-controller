package twitchirc;

import java.util.ArrayList;

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
