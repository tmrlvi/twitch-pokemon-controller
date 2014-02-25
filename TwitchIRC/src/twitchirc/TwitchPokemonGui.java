/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package twitchirc;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author tmrlvi
 */
public class TwitchPokemonGui extends JFrame {
    
    public TwitchPokemonGui(){
        initUI();
    }
    
    public void initUI(){
        setLayout(null);
        setTitle("Test");
        setSize(300,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                TwitchPokemonGui t = new TwitchPokemonGui();
                t.setVisible(true);
            }
          
        });
    }
    
}
