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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

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
import javax.swing.Timer;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author tmrlvi
 */
public class TwitchPokemonGui extends JFrame{
	private static final int LINE_COUNT_MAX = 100;

	TwitchIRC irc;
	CommandQueue commandQueue;
	
	JTextField user;
	JTextField oauth;
	
	Timer commandsTimer;
	JProgressBar progress;
	JTextArea area;
	
	boolean directToggle;
	JRadioButton radioRandom;
	JRadioButton radioDirect;
	JRadioButton radioAnarchy;
	JButton start;
	JButton stop;
	
	JToggleButton upButton;
	JToggleButton leftButton;
	JToggleButton rightButton;
	JToggleButton downButton;
	
	JToggleButton aButton;
	JToggleButton bButton;
	
	JToggleButton buttonStart;
	JToggleButton buttonSelect;
    
    public TwitchPokemonGui(){
        initUI();
        bindActions();
        radioRandom.doClick();
        //commandQueue =  new RandomCommands(); 
        user.setText("tmrlvi");
        oauth.setText("oauth:n6wkn1xa290r7g2ge6f0smh5wj61e63");
        
    }

	private void initUI(){
        setTitle("Pokemon Twitch Auto Controller");
        
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        
        // Username and pass
        JPanel userInfo = new JPanel(); 
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.X_AXIS));
        JLabel userLabel = new JLabel("Username:");
        JLabel oauthLabel = new JLabel("Oauth:");
        user = new JTextField(255);
        oauth = new JTextField(255);     
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
        progress = new JProgressBar();
        progress.setMaximum(30);
        progress.setValue(30);
        start = new JButton("Start");
        stop = new JButton("Stop");
        progress.setMaximumSize(new Dimension(Integer.MAX_VALUE, start.getPreferredSize().height));
        runStatus.add(progress);
        runStatus.add(start);
        runStatus.add(stop);
        stop.setEnabled(false);
        main.add(runStatus);
        
        // Play modes
        JPanel playModes = new JPanel();
        playModes.setLayout(new BoxLayout(playModes, BoxLayout.X_AXIS));
        radioRandom = new JRadioButton("random");
        radioDirect = new JRadioButton("direct");
        radioAnarchy = new JRadioButton("vote anarchy");
        ButtonGroup group = new ButtonGroup();
        group.add(radioRandom);
        group.add(radioDirect);
        group.add(radioAnarchy);
        playModes.add(radioRandom);
        playModes.add(radioDirect);
        playModes.add(radioAnarchy);
        main.add(playModes);
        
                
        // Text aread
        JScrollPane pane = new JScrollPane();
        area = new JTextArea();
        DefaultCaret caret = (DefaultCaret)area.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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
        leftButton = new JToggleButton("<");
        leftButton.setActionCommand("left");
        panelButtonsLeft.add(leftButton);
        
        JPanel panelButtonsUpDown = new JPanel();
        panelButtonsUpDown.setLayout(new BoxLayout(panelButtonsUpDown, BoxLayout.Y_AXIS));
        upButton = new JToggleButton("^");
        downButton = new JToggleButton("v");
        upButton.setActionCommand("up");
        downButton.setActionCommand("down");
        panelButtonsUpDown.add(upButton);
        panelButtonsUpDown.add(downButton);
        
        JPanel panelButtonsRight = new JPanel();
        panelButtonsRight.setLayout(new BoxLayout(panelButtonsRight, BoxLayout.Y_AXIS));
        rightButton = new JToggleButton(">");
        rightButton.setActionCommand("right");
        panelButtonsRight.add(rightButton);
        
        panelButtons.add(panelButtonsLeft);
        panelButtons.add(panelButtonsUpDown);
        panelButtons.add(panelButtonsRight);
        panelButtons.add(Box.createRigidArea(new Dimension(20, 0)));
        
        // Select & Start Buttons
        buttonSelect = new JToggleButton("select");
        buttonStart = new JToggleButton("start");
        buttonSelect.setActionCommand("select");
        buttonStart.setActionCommand("start");
        panelButtons.add(buttonSelect);
        panelButtons.add(Box.createRigidArea(new Dimension(5, 0)));
        panelButtons.add(buttonStart);
        panelButtons.add(Box.createRigidArea(new Dimension(20, 0)));
        
        // A & B Buttons
        JPanel panelButtonsA = new JPanel();
        panelButtonsA.setLayout(new BoxLayout(panelButtonsA, BoxLayout.Y_AXIS));
        aButton = new JToggleButton("A");
        aButton.setActionCommand("a");
        panelButtonsA.add(Box.createRigidArea(new Dimension(0, 25)));
        panelButtonsA.add(aButton);
        
        JPanel panelButtonsB = new JPanel();
        panelButtonsB.setLayout(new BoxLayout(panelButtonsB, BoxLayout.Y_AXIS));
        bButton = new JToggleButton("B");
        bButton.setActionCommand("b");
        panelButtonsB.add(bButton);
        panelButtonsB.add(Box.createRigidArea(new Dimension(0, 25)));
        panelButtons.add(panelButtonsA);
        panelButtons.add(panelButtonsB);
        return panelButtons;
    }
    
    private void resetButtons(){
    	upButton.setSelected(false);
    	downButton.setSelected(false);
    	leftButton.setSelected(false);
    	rightButton.setSelected(false);
    	buttonSelect.setSelected(false);
    	buttonStart.setSelected(false);
    	aButton.setSelected(false);
    	bButton.setSelected(false);
    }
    
    private void enableButtons(boolean enable){
    	upButton.setEnabled(enable);
    	downButton.setEnabled(enable);
    	leftButton.setEnabled(enable);
    	rightButton.setEnabled(enable);
    	buttonSelect.setEnabled(enable);
    	buttonStart.setEnabled(enable);
    	aButton.setEnabled(enable);
    	bButton.setEnabled(enable);
    }
    
    private void bindActions() {
		start.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				start.setEnabled(false);
				stop.setEnabled(true);
				try {
					irc = new TwitchIRC();
					irc.setPrinter(new ThreadedPrinting() {			
						@Override
						public void handleLine(String line) {
							if (line.contains(user.getText()))
								addLine(line);
						}
						
						@Override
						public void handleError(Exception ex) {
							addLine(ex.getMessage());
						}
					});
					irc.connect("199.9.252.26", 6667);
					irc.login(user.getText(), oauth.getText());
					irc.join("twitchplayspokemon");
					startCommands();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					addLine("" + e);
				}
			}
		});
		
		stop.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stop.setEnabled(false);
				start.setEnabled(true);
				commandsTimer.stop();
				progress.setValue(30);
				try {
					irc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					addLine("" + e);
				}
			}
		});
		
		radioRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				commandQueue = new RandomCommands();
				resetButtons();
				enableButtons(true);
				directToggle = false;
			}
		});
		
		radioDirect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				commandQueue = new DirectCommands();
				resetButtons();
				enableButtons(true);
				directToggle = true;
			}
		});
		
		radioAnarchy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				commandQueue = new AnarchyCommand();
				resetButtons();
				enableButtons(false);
				directToggle = true;
			}
		});
		
		ActionListener buttonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JToggleButton button = (JToggleButton) event.getSource();
				if (button.isSelected()){
					commandQueue.add(event.getActionCommand());
					if (directToggle)
						button.setSelected(false);
				}
			}
		};
		
		upButton.addActionListener(buttonListener);
		downButton.addActionListener(buttonListener);
		leftButton.addActionListener(buttonListener);
		rightButton.addActionListener(buttonListener);
		aButton.addActionListener(buttonListener);
		bButton.addActionListener(buttonListener);
		buttonSelect.addActionListener(buttonListener);
		buttonStart.addActionListener(buttonListener);
		
	}
    
    private void startCommands(){
    	commandsTimer = new Timer(1000, new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent event) {
				if (progress.getValue() == 30){
					String command = commandQueue.get();
					if (command != null){
						synchronized (irc){
							irc.privmsg("twitchplayspokemon", command);
							progress.setValue(0);
						}
					}
				} else
					progress.setValue(progress.getValue()+1);
			}
		});
    	commandsTimer.start();
    }
    
    
    private void addLine(String line){
    	if (area.getLineCount() > LINE_COUNT_MAX){
				try {
					int start = area.getLineStartOffset(0);
					int end = area.getLineEndOffset(0);
	    			area.replaceRange("", start, end);
				} catch (BadLocationException e) {
					area.setText("");
				}
    	}
    	area.append(line + "\r\n");
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
