/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package twitchirc;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author tmrlvi
 */
public class ThreadedPrinting extends Thread {
    final static int READER_WAIT_TIME = 2000;
    BufferedReader reader;
    
    public ThreadedPrinting(BufferedReader reader){
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
    
    public void handleLine(String line){
        System.out.println(line);
    }
    
    public void handleError(Exception ex){
        System.err.println(ex.getMessage());
    }
    
}
