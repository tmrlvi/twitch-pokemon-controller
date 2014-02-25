/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package twitchirc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author tmrlvi
 */
public class TwitchIRC {
    Socket socket;
    PrintWriter writer;
    ThreadedPrinting printer;
    ArrayList<Long> commands_ts;
    
    public TwitchIRC(String addr, int port) throws IOException{
        commands_ts = new ArrayList<Long>();
        socket = new Socket(addr, port);
        writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printer = new ThreadedPrinting(reader);
        printer.start();
    }
    
    public void close() throws IOException{
        printer.interrupt();
        socket.close();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
    
    private boolean canSend(){
        long cur_time = System.currentTimeMillis();
        for (long ts : commands_ts)
            if (cur_time - ts > 30*1000)
                commands_ts.remove(ts);
        if (commands_ts.size() > 15){
            System.err.println("Too many messages! wait...");
            return false;
        }
        commands_ts.add(cur_time);
        return true;
    }
    
    public void login(String user, String oauth){
        if (canSend()){
            writer.println("PASS " + oauth);
            writer.println("NICK " + user);
        }
    }
    
    public void join(String channel){
        if (canSend())
            writer.println("JOIN #" + channel);
    }
    
    public void privmsg(String channel, String msg){
        if (canSend())
            writer.println("PRIVMSG #" + channel + " " + msg);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        TwitchIRC t = new TwitchIRC("199.9.252.26", 6667);
        t.login("tmrlvi", "oauth:n6wkn1xa290r7g2ge6f0smh5wj61e63");
        //t.join("twitchplayspokemon");
        t.privmsg("twitchplayspokemon", "up");
        t.privmsg("twitchplayspokemon", "down");
        t.privmsg("twitchplayspokemon", "up");
        Thread.sleep(5000);
        t.close();
        
    }
    
}
