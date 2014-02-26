package twitchirc.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A twitch IRC client. Before running, must set a printer.
 * Checks for not sending too many messages, return the output to a printer.
 * @author tmrlvi
 */
public class TwitchIRC {
    Socket socket;
    PrintWriter writer;
    ThreadedPrinting printer;
    ArrayList<Long> commands_ts;
    
    public TwitchIRC() {
        commands_ts = new ArrayList<Long>();
        
    }
    
    /**
     * Sets the printer object
     * @param printer - a thread that handles the irc output (i.e. print to stdout)
     */
    public void setPrinter(ThreadedPrinting printer){
    	this.printer = printer;
    }
    
    /**
     * Connecting the the server. Must be called after setPrinter
     * @param addr - the host address
     * @param port - the host port
     * @throws IOException - Connection problem, or no printer to output to.
     */
    public void connect(String addr, int port) throws IOException {
    	if (printer == null)
    		throw new IOException("Output is not set. (Did you call setPrinter?)");
    	socket = new Socket(addr, port);
    	writer = new PrintWriter(socket.getOutputStream(), true);
    	BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	printer.setReader(reader);
    	printer.start();
    }
    
    /**
     * Safely closes the connection
     * @throws IOException
     */
    public void close() throws IOException{
        printer.interrupt();
        socket.close();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
    
    // Checks if passed the time limit for messages
    private boolean canSend(){
        long cur_time = System.currentTimeMillis();
        ArrayList<Long> new_commands = new ArrayList<Long>();
        for (long ts : commands_ts)
            if (cur_time - ts < 30*1000)
            	new_commands.add(ts);
        commands_ts = new_commands;
        if (commands_ts.size() > 15){
            System.err.println("Too many messages! wait...");
            return false;
        }
        commands_ts.add(cur_time);
        return true;
    }
    
    /**
     * IRC login command
     * @param user - the user name
     * @param oauth - the twitch oauth (taken from http://twitchapps.com/tmi/)
     */
    public void login(String user, String oauth){
        if (canSend()){
            writer.println("PASS " + oauth);
            writer.println("NICK " + user);
        }
    }
    
    /**
     * IRC join command (will send you every action on the chat)
     * @param channel
     */
    public void join(String channel){
        if (canSend())
            writer.println("JOIN #" + channel);
    }
    
    /**
     * IRC privmsg command (sends a message to the server)
     * @param channel - the channel to direct the message
     * @param msg - the message to send
     */
    public void privmsg(String channel, String msg){
        if (canSend())
            writer.println("PRIVMSG #" + channel + " " + msg);
    }
    
    
}
