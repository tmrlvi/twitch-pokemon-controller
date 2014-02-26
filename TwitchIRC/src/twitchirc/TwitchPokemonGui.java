package twitchirc;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

import twitchirc.queues.CommandQueue;
import twitchirc.queues.DirectCommands;
import twitchirc.queues.ModeCommand;
import twitchirc.queues.RandomCommands;

/**
 * A GUI class for controlling the TwitchPlaysPokemon
 * @author tmrlvi
 */
public class TwitchPokemonGui extends JFrame{
	public static final int LINE_COUNT_MAX = 100;
	public static final int TIMER_TICKS = 1000;
	public static final int TIMER_WAIT = 30;
	
	public static final String HOST = "199.9.252.26";
	public static final int PORT = 6667;
	public static final String CHANNEL = "twitchplayspokemon";

	// The classes that are responsible for the real work
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
        addLine("Welcome to the Auto Controller!");
        addLine("In order to use, you must have a twitch account.");
        addLine("Login is via the IRC chat, so you need oauth instead of password.");
        addLine("The oauth give access to the chat only.");
        addLine("");
        addLine("Get you oauth from : http://twitchapps.com/tmi/");
    }

    // Creates the controllers, and orders them in the main panel
	private void initUI(){
        setTitle("Pokemon Twitch Auto Controller");
        
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        
        main.add(createLoginPanel());
        main.add(createConnectionPanel());
        main.add(createModesPanel());
        main.add(createTextArea());
        main.add(Box.createRigidArea(new Dimension(0, 10)));
        main.add(createButtons());
        main.add(Box.createRigidArea(new Dimension(0, 10)));
        add(main);
        
        pack();
        setSize(new Dimension(400, 300));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
	
	// Creates the login panel
	private JPanel createLoginPanel(){
        // Username and pass
        JPanel userInfo = new JPanel(); 
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.X_AXIS));
        JLabel userLabel = new JLabel("Username:");
        JLabel oauthLabel = new JLabel("Oauth:");
        user = new JTextField(50);
        oauth = new JTextField(255);     
        user.setMaximumSize(user.getPreferredSize());
        oauth.setMaximumSize(oauth.getPreferredSize());
        userInfo.add(userLabel);
        userInfo.add(user);
        userInfo.add(oauthLabel);
        userInfo.add(oauth);
        return userInfo;
	}
	
	// Creates the connection controller panel
	private JPanel createConnectionPanel(){
		// Start, stop and progressbar
        JPanel runStatus = new JPanel();
        runStatus.setLayout(new BoxLayout(runStatus, BoxLayout.X_AXIS));
        progress = new JProgressBar();
        progress.setMaximum(TIMER_WAIT);
        progress.setValue(TIMER_WAIT);
        start = new JButton("Start");
        stop = new JButton("Stop");
        progress.setMaximumSize(new Dimension(Integer.MAX_VALUE, start.getPreferredSize().height));
        runStatus.add(progress);
        runStatus.add(start);
        runStatus.add(stop);
        stop.setEnabled(false);
        return runStatus;
	}
	
	// Creates the connection modes panel
	private JPanel createModesPanel(){
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
        return playModes;
	}
	
	// Creates the connection modes panel
	private JScrollPane createTextArea(){
        // Text aread
        JScrollPane pane = new JScrollPane();
        area = new JTextArea();
        DefaultCaret caret = (DefaultCaret)area.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        pane.getViewport().add(area);
        return pane;
	}
    
	// Creates the buttons, displays them like gameboy
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
        JPanel panelButtonsB = new JPanel();
        panelButtonsB.setLayout(new BoxLayout(panelButtonsB, BoxLayout.Y_AXIS));
        bButton = new JToggleButton("B");
        bButton.setActionCommand("b");
        // Add some space above
        panelButtonsB.add(Box.createRigidArea(new Dimension(0, 25)));
        panelButtonsB.add(bButton);
        
        JPanel panelButtonsA = new JPanel();
        panelButtonsA.setLayout(new BoxLayout(panelButtonsA, BoxLayout.Y_AXIS));
        aButton = new JToggleButton("A");
        aButton.setActionCommand("a");
        panelButtonsA.add(aButton);
     // Add some space below
        panelButtonsA.add(Box.createRigidArea(new Dimension(0, 25)));
        panelButtons.add(panelButtonsB);
        panelButtons.add(panelButtonsA);
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
    
    // Binds all the actions
    private void bindActions() {
    	// Start connection
		start.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent event) {
				area.setText("");
				start.setEnabled(false);
				stop.setEnabled(true);
				try {
					irc = new TwitchIRC();
					// Connect the printer to the area
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
					irc.connect(HOST, PORT);
					irc.login(user.getText(), oauth.getText());
					irc.join(CHANNEL);
					startCommands();
				} catch (IOException e) {
					addLine("Error: " + e);
				}
			}
		});
		
		// Stops the connection
		stop.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent event) {
				stop.setEnabled(false);
				start.setEnabled(true);
				commandsTimer.stop();
				progress.setValue(TIMER_WAIT);
				try {
					irc.close();
				} catch (IOException e) {
					addLine("Error: " + e);
				}
			}
		});
		
		// Queue methods
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
				commandQueue = new ModeCommand();
				resetButtons();
				enableButtons(false);
				directToggle = true;
			}
		});
		
		// binding buttons
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
    
    // Sends the command after every TIMER_WAIT number of TIMER_TICKS milliseconds
    private void startCommands(){
    	commandsTimer = new Timer(TIMER_TICKS, new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent event) {
				if (progress.getValue() >= TIMER_WAIT){
					String command = commandQueue.get();
					if (command != null){
						synchronized (irc){
							irc.privmsg(CHANNEL, command);
							progress.setValue(0);
						}
					}
				} else
					progress.setValue(progress.getValue()+1);
			}
		});
    	commandsTimer.start();
    }
    
    // Adds a line in the text area
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
     * Starts the app in a new thread (So it won't be blocking)
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                new TwitchPokemonGui();
            }
          
        });
    }

}
