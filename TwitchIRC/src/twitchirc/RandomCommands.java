package twitchirc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;

public class RandomCommands implements CommandQueue {
	List<String> commands;
	Random rand;
	
	public RandomCommands(){
		rand = new Random();
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
		int location = rand.nextInt(commands.size());
		return commands.get(location);
	}

	@Override
	public boolean remove(String command) {
		return commands.remove(command);
	}

}
