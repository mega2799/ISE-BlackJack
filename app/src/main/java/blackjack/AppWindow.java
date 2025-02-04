package blackjack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;


/**
 * Application window.
 * Holds the menu-bar etc.
 *
 * @author David Winter
 */
public class AppWindow extends JFrame 
    implements ActionListener, ComponentListener
{
    private final GamePanel gamePanel;
    private final Color defaultTableColour = new Color(6, 120, 0);
    
    private final JMenuItem savePlayer = new JMenuItem("Save Current Player");
    private final JMenuItem openPlayer = new JMenuItem("Open Existing Player");
    
    final int WIDTH = 600;
    final int HEIGHT = 500;

	public AppWindow(final double walletAmount)
    {
        super("Blackjack");
        
        this.addComponentListener(this);
        
        final Dimension windowSize = new Dimension(this.WIDTH, this.HEIGHT);
        this.setSize(windowSize);
        this.setLocationRelativeTo(null); // put game in centre of screen
        
        this.setBackground(this.defaultTableColour);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // menu bar
        final JMenuBar menuBar = new JMenuBar();
        
        final JMenu playerMenu = new JMenu("Player");
        final JMenuItem updatePlayerDetails = new JMenuItem("Update Player Details");
        playerMenu.add(updatePlayerDetails);
        playerMenu.addSeparator();
        playerMenu.add(this.savePlayer);
        playerMenu.add(this.openPlayer);
        menuBar.add(playerMenu);
        
        final JMenu actionMenu = new JMenu("Actions");
        final JMenuItem dealAction = new JMenuItem("Deal");
        final JMenuItem hitAction = new JMenuItem("Hit");
        final JMenuItem doubleAction = new JMenuItem("Double");
        final JMenuItem standAction = new JMenuItem("Stand");
        actionMenu.add(dealAction);
        actionMenu.add(hitAction);
        actionMenu.add(doubleAction);
        actionMenu.add(standAction);
        menuBar.add(actionMenu);
        
        final JMenu betMenu = new JMenu("Bet");
        final JMenuItem oneChip = new JMenuItem("$1");
        final JMenuItem fiveChip = new JMenuItem("$5");
        final JMenuItem tenChip = new JMenuItem("$10");
        final JMenuItem twentyFiveChip = new JMenuItem("$25");
        final JMenuItem hundredChip = new JMenuItem("$100");
        betMenu.add(oneChip);
        betMenu.add(fiveChip);
        betMenu.add(tenChip);
        betMenu.add(twentyFiveChip);
        betMenu.add(hundredChip);
        menuBar.add(betMenu);
        
        final JMenu windowMenu = new JMenu("Window");
        final JMenuItem windowTableColourMenu = new JMenuItem("Change Table Colour");
        windowMenu.add(windowTableColourMenu);
        menuBar.add(windowMenu);
        
        final JMenu helpMenu = new JMenu("Help");
        final JMenuItem helpBlackjackRulesMenu = new JMenuItem("Blackjack Rules");
        final JMenuItem helpAboutMenu = new JMenuItem("About Blackjack");
        helpMenu.add(helpBlackjackRulesMenu);
        helpMenu.addSeparator();
        helpMenu.add(helpAboutMenu);
        menuBar.add(helpMenu);
        
        this.setJMenuBar(menuBar);
        
        // keyboard shortcuts
        
        updatePlayerDetails.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_U,                                            
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.savePlayer.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.openPlayer.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));   
        dealAction.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_N,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        hitAction.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_C,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        doubleAction.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_D,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        standAction.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        oneChip.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_1,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fiveChip.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_2,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        tenChip.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_3,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        twentyFiveChip.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_4,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        hundredChip.setAccelerator(
            KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_5,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        
		// action listeners
		dealAction.addActionListener(this);
        hitAction.addActionListener(this);
        doubleAction.addActionListener(this);
        standAction.addActionListener(this);
		updatePlayerDetails.addActionListener(this);
		this.savePlayer.addActionListener(this);
		this.openPlayer.addActionListener(this);
		windowTableColourMenu.addActionListener(this);
		helpAboutMenu.addActionListener(this);
		oneChip.addActionListener(this);
        fiveChip.addActionListener(this);
        tenChip.addActionListener(this);
        twentyFiveChip.addActionListener(this);
        hundredChip.addActionListener(this);
        		
        this.gamePanel = new GamePanel(walletAmount);
        this.gamePanel.setBackground(this.defaultTableColour);
		this.add(this.gamePanel);
        
        this.setVisible(true);
    }

    @Override
	public void actionPerformed(final ActionEvent evt)
    {   
        final String act = evt.getActionCommand();
        final GameCommand command = GameCommand.fromString(act);

        this.switchCommand(command);
	}

    private void switchCommand(final GameCommand command) {
        if (command != GameCommand.UNKNOWN) {
            switch (command) {
                case BET_1 ->
                    this.gamePanel.increaseBet(1);
                case BET_5 ->
                    this.gamePanel.increaseBet(5);
                case BET_10 ->
                    this.gamePanel.increaseBet(10);
                case BET_25 ->
                    this.gamePanel.increaseBet(25);
                case BET_100 ->
                    this.gamePanel.increaseBet(100);
                case DEAL ->
                    this.gamePanel.newGame();
                case HIT ->
                    this.gamePanel.hit();
                case DOUBLE ->
                    this.gamePanel.playDouble();
                case STAND ->
                    this.gamePanel.stand();
                case UPDATE_PLAYER ->
                    this.gamePanel.updatePlayer();
                case SAVE_PLAYER ->
                    this.gamePanel.savePlayer();
                case OPEN_PLAYER ->
                    this.gamePanel.openPlayer();
                case CHANGE_TABLE_COLOUR -> {
                    final Color tableColour = JColorChooser.showDialog(this, "Select Table Colour", this.defaultTableColour);
                    this.setBackground(tableColour);
                    this.gamePanel.setBackground(tableColour);
                    this.gamePanel.repaint();
                    this.repaint();
                }
                case ABOUT_BLACKJACK -> {
                    final String aboutText = "<html><p align=\"center\" style=\"padding-bottom: 10px;\">Written by David Winter &copy; 2006<br>Version 1.0</p><p align=\"center\" style=\"padding-bottom: 10px;\"><small>Become such an expert while developing this, <br>I won $1000 online in a game of Blackjack!</small></p><p align=\"center\">email: djw@davidwinter.me.uk<br>web: davidwinter.me.uk</p></html>";
                    JOptionPane.showMessageDialog(this, aboutText, "About Blackjack", JOptionPane.PLAIN_MESSAGE);
                }
                default -> {
                }
            }
            this.gamePanel.updateValues();
        }
    }

	public void actionPerformed(final GameCommand command)
    {   
        this.switchCommand(command);

	}

	
	public void componentResized(final ComponentEvent e)
	{
	    int currentWidth = this.getWidth();
	    int currentHeight = this.getHeight();
	    
	    boolean resize = false;
	    
	    if (currentWidth < this.WIDTH)
	    {
	        resize = true;
	        currentWidth = this.WIDTH;
	    }
	    
	    if (currentHeight < this.HEIGHT)
	    {
	        resize = true;
	        currentHeight = this.HEIGHT;
	    }
	    
	    if (resize)
	    {
	        this.setSize(currentWidth, currentHeight);
	    }
	}
	
	public void componentMoved(final ComponentEvent e) { }
	public void componentShown(final ComponentEvent e) { }
	public void componentHidden(final ComponentEvent e) { }
}