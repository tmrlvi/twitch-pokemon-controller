package twitchirc;

import java.util.ArrayList;

public abstract interface CommandQueue {	
	public void add(String command);
	public String get();
	public boolean remove(String command);
}
