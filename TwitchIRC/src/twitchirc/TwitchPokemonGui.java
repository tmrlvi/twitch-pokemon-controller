/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package twitchirc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

/**
 *
 * @author tmrlvi
 */
public class TwitchPokemonGui extends JFrame {
    
    public TwitchPokemonGui(){
        initUI();
    }
    
    public void initUI(){
        setTitle("Pokemon Twitch Auto Controller");
        
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        
        // Username and pass
        JPanel userInfo = new JPanel(); 
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.X_AXIS));
        JLabel userLabel = new JLabel("Username:");
        JLabel oauthLabel = new JLabel("Oauth:");
        JTextField user = new JTextField(255);
        JTextField oauth = new JTextField(255);
        
        user.setMaximumSize(user.getPreferredSize());
        oauth.setMaximumSize(oauth.getPreferredSize());
        
        userInfo.add(userLabel);
        userInfo.add(user);
        userInfo.add(oauthLabel);
        userInfo.add(oauth);
        main.add(userInfo);
        
        // Start, stop and progressbar
        JPanel runStatus = new JPanel();
        runStatus.setLayout(new BoxLayout(runStatus, BoxLayout.X_AXIS));
        JProgressBar progress = new JProgressBar();
        JButton start = new JButton("Start");
        JButton stop = new JButton("Stop");
        progress.setMaximumSize(new Dimension(Integer.MAX_VALUE, start.getPreferredSize().height));
        runStatus.add(progress);
        runStatus.add(start);
        runStatus.add(stop);
        main.add(runStatus);
        
        // Play modes
        JPanel playModes = new JPanel();
        playModes.setLayout(new BoxLayout(playModes, BoxLayout.X_AXIS));
        JRadioButton radioRandom = new JRadioButton("random");
        JRadioButton radioDirect = new JRadioButton("direct");
        ButtonGroup group = new ButtonGroup();
        group.add(radioRandom);
        group.add(radioDirect);
        playModes.add(radioRandom);
        playModes.add(radioDirect);
        main.add(playModes);
                
        // Text aread
        JScrollPane pane = new JScrollPane();
        JTextArea area = new JTextArea();
        pane.getViewport().add(area);
        main.add(pane);
        main.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Buttons
        JPanel panelButtons = createButtons();
        main.add(panelButtons);
        main.add(Box.createRigidArea(new Dimension(0, 10)));
        
        
        add(main);
        
        pack();
        setSize(new Dimension(400, 300));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        System.out.println(radioDirect.getSize());
    }
    
    private JPanel createButtons(){
        // Buttons
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        
        // Move Buttons
        JPanel panelButtonsLeft = new JPanel();
        panelButtonsLeft.setLayout(new BoxLayout(panelButtonsLeft, BoxLayout.Y_AXIS));
        JToggleButton leftButton = new JToggleButton("<");
        panelButtonsLeft.add(leftButton);
        
        JPanel panelButtonsUpDown = new JPanel();
        panelButtonsUpDown.setLayout(new BoxLayout(panelButtonsUpDown, BoxLayout.Y_AXIS));
        JToggleButton upButton = new JToggleButton("^");
        JToggleButton downButton = new JToggleButton("v");
        panelButtonsUpDown.add(upButton);
        panelButtonsUpDown.add(downButton);
        
        JPanel panelButtonsRight = new JPanel();
        panelButtonsRight.setLayout(new BoxLayout(panelButtonsRight, BoxLayout.Y_AXIS));
        JToggleButton rightButton = new JToggleButton(">");
        panelButtonsRight.add(rightButton);
        
        panelButtons.add(panelButtonsLeft);
        panelButtons.add(panelButtonsUpDown);
        panelButtons.add(panelButtonsRight);
        panelButtons.add(Box.createRigidArea(new Dimension(20, 0)));
        
        // Select & Start Buttons
        JToggleButton buttonSelect = new JToggleButton("select");
        JToggleButton buttonStart = new JToggleButton("start");
        panelButtons.add(buttonSelect);
        panelButtons.add(Box.createRigidArea(new Dimension(5, 0)));
        panelButtons.add(buttonStart);
        panelButtons.add(Box.createRigidArea(new Dimension(20, 0)));
        
        // A & B Buttons
        JPanel panelButtonsA = new JPanel();
        panelButtonsA.setLayout(new BoxLayout(panelButtonsA, BoxLayout.Y_AXIS));
        JToggleButton aButton = new JToggleButton("A");
        panelButtonsA.add(Box.createRigidArea(new Dimension(0, 25)));
        panelButtonsA.add(aButton);
        /*aButton.setMinimumSize(new Dimension(30, 30));
        aButton.setPreferredSize(new Dimension(30, 30));
        aButton.setMaximumSize(new Dimension(30, 30));
        aButton.setBorder(new AbstractBorder() {
        	@Override
        	public void paintBorder(Component c, Graphics g, int x, int y,
        			int width, int height) {
        		// TODO Auto-generated method stub
        		//super.paintBorder(c, g, x, y, width, height);
        		//g.drawRoundRect(1, 1, width-1, height-1, 1, 1);
        		g.drawOval(x, y, width - 1, height - 1);
        	}
		});*/
        JPanel panelButtonsB = new JPanel();
        panelButtonsB.setLayout(new BoxLayout(panelButtonsB, BoxLayout.Y_AXIS));
        JToggleButton bButton = new JToggleButton("B");
        panelButtonsB.add(bButton);
        panelButtonsB.add(Box.createRigidArea(new Dimension(0, 25)));
        panelButtons.add(panelButtonsA);
        panelButtons.add(panelButtonsB);
        return panelButtons;
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
