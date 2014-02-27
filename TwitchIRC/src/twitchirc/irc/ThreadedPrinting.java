package twitchirc.irc;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Handles the output of the socket
 * @author tmrlvi
 */
public abstract class ThreadedPrinting extends Thread {
    final static int READER_WAIT_TIME = 100;
    BufferedReader reader;
    
    public void setReader(BufferedReader reader){
    	this.reader = reader;
    }
    
    @Override
    public void run(){
        while (true){
            try {
                if (reader.ready())
                    handleLine(reader.readLine());
                else
                    sleep(READER_WAIT_TIME);
            } catch (IOException ex) {
                handleError(ex);
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
    
    /**
     * Will be called when a new line is processed
     * @param line
     */
    public abstract void handleLine(String line);
    
    /**
     * Will be called upon and error in reading the buffer
     * @param ex - the exception
     */
    public abstract void handleError(Exception ex);
    
}
