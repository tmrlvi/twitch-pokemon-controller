package twitchirc;

public class AnarchyCommand implements CommandQueue {

	@Override
	public void add(String command) {
	}

	@Override
	public String get() {
		return "anarchy";
	}

	@Override
	public boolean remove(String command) {
		return false;
	}

}
