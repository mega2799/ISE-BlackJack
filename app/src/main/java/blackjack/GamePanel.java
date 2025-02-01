package blackjack;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import blackjack.Cards.CardPack;
import blackjack.Cards.PlayerCardHand;
import blackjack.Players.Dealer;
import blackjack.Players.Player;

public class GamePanel extends JPanel implements ActionListener
{
    private final Dealer dealer;
    private Player player;
    
    private final GameTable table;
    
    private final JButton newGameButton = new JButton("Deal");
    private final JButton hitButton = new JButton("Hit");
    private final JButton doubleButton = new JButton("Double");
	private final JButton standButton = new JButton("Stand");
    private final JButton add1Chip = new JButton("1");
    private final JButton add5Chip = new JButton("5");
    private final JButton add10Chip = new JButton("10");
    private final JButton add25Chip = new JButton("25");
    private final JButton add100Chip = new JButton("100");
    private final JButton clearBet =  new JButton("Clear");
    
    private final JLabel currentBet = new JLabel("Please set your bet...");
    private final JLabel playerWallet = new JLabel("$999.99");
    private final JLabel cardsLeft = new JLabel("Cards left...");
    private final JLabel dealerSays = new JLabel("Dealer says...");
    
    public GamePanel()
    {
        this.setLayout(new BorderLayout());
        
        this.table = new GameTable();
        this.add(this.table, BorderLayout.CENTER);
        
        final JPanel betPanel = new JPanel();
        betPanel.add(this.currentBet);
        betPanel.add(this.clearBet);
        betPanel.add(this.add1Chip);
        betPanel.add(this.add5Chip);
        betPanel.add(this.add10Chip);
        betPanel.add(this.add25Chip);
        betPanel.add(this.add100Chip);
        betPanel.add(this.playerWallet);
        
        final JPanel dealerPanel = new JPanel();
        dealerPanel.add(this.dealerSays);
        
        final JPanel optionsPanel = new JPanel();
        optionsPanel.add(this.newGameButton);
        optionsPanel.add(this.hitButton);
        optionsPanel.add(this.doubleButton);
        optionsPanel.add(this.standButton);
        optionsPanel.add(this.cardsLeft);
        
        final JPanel bottomItems = new JPanel();
        bottomItems.setLayout(new GridLayout(0,1));
        bottomItems.add(dealerPanel);
        bottomItems.add(betPanel);
        bottomItems.add(optionsPanel);
        this.add(bottomItems, BorderLayout.SOUTH);
        
        // opaque stuff
        //this.setBackground(new Color(6, 120, 0)); // now done in AppWindow.java
        betPanel.setOpaque(false);
        dealerPanel.setOpaque(false);
        optionsPanel.setOpaque(false);
        bottomItems.setOpaque(false);
        
        // add listeners to buttons
        this.newGameButton.addActionListener(this);
        this.hitButton.addActionListener(this);
        this.doubleButton.addActionListener(this);
		this.standButton.addActionListener(this);
		this.clearBet.addActionListener(this);
		this.add1Chip.addActionListener(this);
		this.add5Chip.addActionListener(this);
		this.add10Chip.addActionListener(this);
		this.add25Chip.addActionListener(this);
		this.add100Chip.addActionListener(this);

		// tool tips
		this.newGameButton.setToolTipText("Deal a new game.");
		this.hitButton.setToolTipText("Request another card.");
		this.doubleButton.setToolTipText("Double your bet, and receive another card.");
		this.standButton.setToolTipText("Stand with your card-hand.");
        this.clearBet.setToolTipText("Clear your current bet.");
        this.add1Chip.setToolTipText("Add a $1 chip to your current bet.");
        this.add5Chip.setToolTipText("Add a $5 chip to your current bet.");
        this.add10Chip.setToolTipText("Add a $10 chip to your current bet.");
        this.add25Chip.setToolTipText("Add a $25 chip to your current bet.");
        this.add100Chip.setToolTipText("Add a $100 chip to your current bet.");
		
		this.dealer = new Dealer();
        this.player = new Player("James Bond", 32, "Male");
        this.player.setWallet(100.00);
		
        this.updateValues();
    }
    
    public void actionPerformed(final ActionEvent evt)
    {
        final String act = evt.getActionCommand();
        
        if ("Deal".equals(act))
        {
            this.newGame();
        }
        else if ("Hit".equals(act))
        {
            this.hit();
        }
        else if ("Double".equals(act))
        {
            this.playDouble();
        }
        else if ("Stand".equals(act))
        {
            this.stand();
        }
        else if ("1".equals(act) || "5".equals(act) || "10".equals(act) || "25".equals(act) || "100".equals(act))
        {
            this.increaseBet(Integer.parseInt(act));
        }
        else if ("Clear".equals(act))
        {
            System.out.println("clear bet");
            this.clearBet();
        }
        
        this.updateValues();
    }
    
    public void newGame()
    {
        this.dealer.deal(this.player);
    }
    
    public void hit()
    {
        this.dealer.hit(this.player);
    }
    
    public void playDouble()
    {
        this.dealer.playDouble(this.player);
    }
    
    public void stand()
    {
        this.dealer.stand(this.player);
    }
    
    public void increaseBet(final int amount)
    {
        this.dealer.acceptBetFrom(this.player, amount + this.player.getBet());
    }
    
    public void clearBet()
    {
        this.player.clearBet();
    }
    
    public void updateValues()
    {
        this.dealerSays.setText("<html><p align=\"center\"><font face=\"Serif\" color=\"white\" style=\"font-size: 20pt\">" + this.dealer.says() + "</font></p></html>");
        
        if (this.dealer.isGameOver())
        {
            if (this.player.betPlaced() && !this.player.isBankrupt())
            {
                this.newGameButton.setEnabled(true);
            }
            else
            {
                this.newGameButton.setEnabled(false);
            }
            this.hitButton.setEnabled(false);
            this.doubleButton.setEnabled(false);
            this.standButton.setEnabled(false);
            
            if (this.player.betPlaced())
            {
                this.clearBet.setEnabled(true);
            }
            else
            {
                this.clearBet.setEnabled(false);
            }
            
            if (this.player.getWallet() >= 1.0)
            {
                this.add1Chip.setEnabled(true);
            }
            else
            {
                this.add1Chip.setEnabled(false);
            }
            
            if (this.player.getWallet() >= 5)
            {
                this.add5Chip.setEnabled(true);
            }
            else
            {
                this.add5Chip.setEnabled(false);
            }
            
            if (this.player.getWallet() >= 10)
            {
                this.add10Chip.setEnabled(true);
            }
            else
            {
                this.add10Chip.setEnabled(false);
            }
            
            if (this.player.getWallet() >= 25)
            {
                this.add25Chip.setEnabled(true);
            }
            else
            {
                this.add25Chip.setEnabled(false);
            }
            
            if (this.player.getWallet() >= 100)
            {
                this.add100Chip.setEnabled(true);
            }
            else
            {
                this.add100Chip.setEnabled(false);
            }
        }
        else
        {
            this.newGameButton.setEnabled(false);
            this.hitButton.setEnabled(true);
            if (this.dealer.canPlayerDouble(this.player))
            {
                this.doubleButton.setEnabled(true);
            }
            else
            {
                this.doubleButton.setEnabled(false);
            }
            
            this.standButton.setEnabled(true);
            
            this.clearBet.setEnabled(false);
            this.add1Chip.setEnabled(false);
            this.add5Chip.setEnabled(false);
            this.add10Chip.setEnabled(false);
            this.add25Chip.setEnabled(false);
            this.add100Chip.setEnabled(false);
        }
        
        // redraw cards and totals
        this.table.update(this.dealer.getHand(), this.player.getHand(), (this.dealer.areCardsFaceUp()) ? true : false);
		this.table.setNames(this.dealer.getName(), this.player.getName());
        this.table.repaint();
        
        this.cardsLeft.setText("Deck: " + this.dealer.cardsLeftInPack() + "/" + (this.dealer.CARD_PACKS * CardPack.CARDS_IN_PACK));
        
        if (this.player.isBankrupt())
        {
            this.moreFunds();
        }
        
        // redraw bet
        this.currentBet.setText(Double.toString(this.player.getBet()));
        this.playerWallet.setText(Double.toString(this.player.getWallet()));
    }
    
    private void moreFunds()
    {
        final int response = JOptionPane.showConfirmDialog(null, "Marshall Aid. One Hundred dollars. With the compliments of the USA.", "Out of funds", JOptionPane.YES_NO_OPTION);
        
        if (response == JOptionPane.YES_OPTION)
        {
            this.player.setWallet(100.00);
            this.updateValues();
        }
    }
    
    public void savePlayer()
	{
	    if (this.dealer.isGameOver())
	    {
	        final JFileChooser playerSaveDialog = new JFileChooser("~");
	        final SimpleFileFilter fileFilter = new SimpleFileFilter(".ser", "(.ser) Serialised Files");
	        playerSaveDialog.addChoosableFileFilter(fileFilter);
            final int playerSaveResponse = playerSaveDialog.showSaveDialog(this);
        
            if (playerSaveResponse == playerSaveDialog.APPROVE_OPTION)
            {
                final String filePath = playerSaveDialog.getSelectedFile().getAbsolutePath();
            
                try
                {
                    final ObjectOutputStream playerOut = new ObjectOutputStream(new FileOutputStream(filePath));
                    playerOut.writeObject(this.player);
                    playerOut.close();
                }
                catch (final IOException e)
                {
                    System.out.println(e);
                }
            }
	    }
	    else
	    {
	        JOptionPane.showMessageDialog(this, "Can't save a player while a game is in progress.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	    
	}
	
	public void openPlayer()
	{
	    if (this.dealer.isGameOver())
	    {
	        final JFileChooser playerOpenDialog = new JFileChooser("~");
	        final SimpleFileFilter fileFilter = new SimpleFileFilter(".ser", "(.ser) Serialised Files");
	        playerOpenDialog.addChoosableFileFilter(fileFilter);
            final int playerOpenResponse = playerOpenDialog.showOpenDialog(this);
        
            if (playerOpenResponse == playerOpenDialog.APPROVE_OPTION)
            {
                final String filePath = playerOpenDialog.getSelectedFile().getAbsolutePath();
            
                try
                {
                    final ObjectInputStream playerIn = new ObjectInputStream(new FileInputStream(filePath));
                    final Player openedPlayer = (Player) playerIn.readObject();
                    openedPlayer.hand = new PlayerCardHand();
                    this.player = openedPlayer;
                    playerIn.close();
                    System.out.println(openedPlayer.getName());
                }
                catch (final ClassNotFoundException e)
                {
                    System.err.println(e);
                }
                catch (final IOException e)
                {
                    System.err.println(e);
                }
            }
	    }
	    else
	    {
	        JOptionPane.showMessageDialog(this, "Can't open an existing player while a game is in progress.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	public void updatePlayer()
	{
	    final PlayerDialog playerDetails = new PlayerDialog(null, "Player Details", true, this.player);
        playerDetails.setVisible(true);
        
        this.player = playerDetails.getPlayer();
	}
}