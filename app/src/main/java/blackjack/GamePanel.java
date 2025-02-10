package blackjack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import blackjack.Cards.CardPack;
import blackjack.Cards.PlayerCardHand;
import blackjack.Players.Dealer;
import blackjack.Players.Player;

public class GamePanel extends JPanel implements EventListener{

    private final Dealer dealer;
    private Player player;

    public Dealer getDealer() {
        return this.dealer;
    }

    public Player getPlayer() {
        return this.player;
    }

    private final GameTable table;

    public GameTable getTable() {
        return this.table;
    }

    private final List<EventListener> listeners = new ArrayList<>();


    // Bottoni per il gioco
    private final JButton newGameButton = new JButton("Deal");
    private final JButton hitButton = new JButton("Hit");
    private final JButton doubleButton = new JButton("Double");
    private final JButton standButton = new JButton("Stand");
    private final JButton add1Chip = new JButton("1");
    private final JButton add5Chip = new JButton("5");
    private final JButton add10Chip = new JButton("10");
    private final JButton add25Chip = new JButton("25");
    private final JButton add100Chip = new JButton("100");
    private final JButton clearBet = new JButton("Clear");

    // Etichette di stato
    private final JLabel currentBet = new JLabel("Please set your bet...");
    private final JLabel playerWallet = new JLabel("$999.99");
    private final JLabel cardsLeft = new JLabel("Cards left...");
    private final JLabel dealerSays = new JLabel("Dealer says...");

    public GamePanel(final double walletAmount) {
        // Imposta layout principale
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(6, 120, 0)); // Se desideri cambiare lo sfondo, puoi farlo qui

        // Inizializza il tavolo di gioco
        this.table = new GameTable();
        this.add(this.table, BorderLayout.CENTER);

        // Crea il pannello per il bet
        final JPanel betPanel = new JPanel();
        betPanel.setOpaque(false);
        betPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        betPanel.add(this.currentBet);
        betPanel.add(this.clearBet);
        betPanel.add(this.add1Chip);
        betPanel.add(this.add5Chip);
        betPanel.add(this.add10Chip);
        betPanel.add(this.add25Chip);
        betPanel.add(this.add100Chip);
        betPanel.add(this.playerWallet);

        // Pannello per i messaggi del dealer
        final JPanel dealerPanel = new JPanel();
        dealerPanel.setOpaque(false);
        dealerPanel.add(this.dealerSays);

        // Pannello per le opzioni di gioco
        final JPanel optionsPanel = new JPanel();
        optionsPanel.setOpaque(false);
        optionsPanel.add(this.newGameButton);
        optionsPanel.add(this.hitButton);
        optionsPanel.add(this.doubleButton);
        optionsPanel.add(this.standButton);
        optionsPanel.add(this.cardsLeft);

        // Raggruppa i pannelli inferiori in un pannello con layout a righe
        final JPanel bottomItems = new JPanel(new GridLayout(0, 1));
        bottomItems.setOpaque(false);
        bottomItems.add(dealerPanel);
        bottomItems.add(betPanel);
        bottomItems.add(optionsPanel);
        this.add(bottomItems, BorderLayout.SOUTH);

        // Imposta uno stile di font per bottoni ed etichette
        final Font buttonFont = new Font("Arial", Font.BOLD, 14);
        final Font labelFont = new Font("Arial", Font.PLAIN, 16);
        for (final JButton btn : new JButton[]{this.newGameButton, this.hitButton, this.doubleButton, this.standButton, 
                                         this.add1Chip, this.add5Chip, this.add10Chip, this.add25Chip, this.add100Chip, this.clearBet}) {
            btn.setFont(buttonFont);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(220, 220, 220));
        }
        this.currentBet.setFont(labelFont);
        this.playerWallet.setFont(labelFont);
        this.cardsLeft.setFont(labelFont);
        this.dealerSays.setFont(new Font("Serif", Font.ITALIC, 20));

        // Imposta i tool tip per ciascun bottone
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

        // Inizializza dealer e player
        this.dealer = new Dealer();
        this.dealer.addListener(this);
        this.player = new Player("Kevin Spacey", 66, "Male");
        this.player.setWallet(walletAmount);

        // Aggiungi i listener tramite lambda expressions
        this.newGameButton.addActionListener(e -> {
            this.newGame();
            this.updateValues();
        });

        this.hitButton.addActionListener(e -> {
            this.hit();
            this.updateValues();
        });

        this.doubleButton.addActionListener(e -> {
            this.playDouble();
            this.updateValues();
        });

        this.standButton.addActionListener(e -> {
            this.stand();
            this.updateValues();
        });

        this.add1Chip.addActionListener(e -> {
            this.increaseBet(1);
            this.updateValues();
        });

        this.add5Chip.addActionListener(e -> {
            this.increaseBet(5);
            this.updateValues();
        });

        this.add10Chip.addActionListener(e -> {
            this.increaseBet(10);
            this.updateValues();
        });

        this.add25Chip.addActionListener(e -> {
            this.increaseBet(25);
            this.updateValues();
        });

        this.add100Chip.addActionListener(e -> {
            this.increaseBet(100);
            this.updateValues();
        });

        this.clearBet.addActionListener(e -> {
            this.clearBet();
            this.updateValues();
        });

        this.updateValues();
    }

    // Metodi di gioco

    public  void newGame() {
        this.dealer.deal(this.player);
    }

    public  void hit() {
        this.dealer.hit(this.player);
    }

    public  void playDouble() {
        this.dealer.playDouble(this.player);
    }

    public  void stand() {
        this.dealer.stand(this.player);
    }

    public  void increaseBet(final int amount) {
        this.dealer.acceptBetFrom(this.player, amount + this.player.getBet());
    }

    public void clearBet() {
        this.player.clearBet();
    }

    public void updateValues() {
        // Aggiorna il messaggio del dealer
        this.dealerSays
                .setText("<html><p align=\"center\"><font face=\"Serif\" color=\"white\" style=\"font-size: 20pt\">"
                        + this.dealer.says() + "</font></p></html>");

        // Aggiorna lo stato dei bottoni a seconda dello stato del gioco
        if (this.dealer.isGameOver()) {
            this.newGameButton.setEnabled(this.player.betPlaced() && !this.player.isBankrupt());
            this.hitButton.setEnabled(false);
            this.doubleButton.setEnabled(false);
            this.standButton.setEnabled(false);

            this.clearBet.setEnabled(this.player.betPlaced());

            this.add1Chip.setEnabled(this.player.getWallet() >= 1.0);
            this.add5Chip.setEnabled(this.player.getWallet() >= 5);
            this.add10Chip.setEnabled(this.player.getWallet() >= 10);
            this.add25Chip.setEnabled(this.player.getWallet() >= 25);
            this.add100Chip.setEnabled(this.player.getWallet() >= 100);
        } else {
            this.newGameButton.setEnabled(false);
            this.hitButton.setEnabled(true);
            this.doubleButton.setEnabled(this.dealer.canPlayerDouble(this.player));
            this.standButton.setEnabled(true);

            this.clearBet.setEnabled(false);
            this.add1Chip.setEnabled(false);
            this.add5Chip.setEnabled(false);
            this.add10Chip.setEnabled(false);
            this.add25Chip.setEnabled(false);
            this.add100Chip.setEnabled(false);
        }

        // Aggiorna la visualizzazione del tavolo
        this.table.update(this.dealer.getHand(), this.player.getHand(), this.dealer.areCardsFaceUp());
        this.table.setNames(this.dealer.getName(), this.player.getName());
        this.table.repaint();

        this.cardsLeft.setText(
                "Deck: " + this.dealer.cardsLeftInPack() + "/" + (this.dealer.CARD_PACKS * CardPack.CARDS_IN_PACK));

        // Se il giocatore è in bancarotta, offri ulteriori fondi
        if (this.player.isBankrupt()) {
            this.moreFunds();
        }

        // Aggiorna le etichette di bet e wallet
        this.currentBet.setText(String.format("$%.2f", this.player.getBet()));
        this.playerWallet.setText(String.format("$%.2f", this.player.getWallet()));
    }
    
    public void addListener(final EventListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(final EventListener listener) {
        this.listeners.remove(listener);
    }

    private void notifyListeners(final String message) {
        for (final EventListener listener : this.listeners) {
            listener.onEvent(message);
        }
    }

    private void moreFunds() {
        final int response = JOptionPane.showConfirmDialog(this,
                "Marshall Aid. One Hundred dollars. With the compliments of the USA.",
                "Out of funds", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            this.player.setWallet(100.00);
            this.updateValues();
        }
    }

    // Metodi per salvare/aprire il player

    public void savePlayer() {
        if (this.dealer.isGameOver()) {
            final JFileChooser playerSaveDialog = new JFileChooser("~");
            final SimpleFileFilter fileFilter = new SimpleFileFilter(".ser", "(.ser) Serialised Files");
            playerSaveDialog.addChoosableFileFilter(fileFilter);
            final int playerSaveResponse = playerSaveDialog.showSaveDialog(this);

            if (playerSaveResponse == JFileChooser.APPROVE_OPTION) {
                final String filePath = playerSaveDialog.getSelectedFile().getAbsolutePath();
                try (ObjectOutputStream playerOut = new ObjectOutputStream(new FileOutputStream(filePath))) {
                    playerOut.writeObject(this.player);
                } catch (final IOException e) {
                    System.err.println(e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Can't save a player while a game is in progress.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openPlayer() {
        if (this.dealer.isGameOver()) {
            final JFileChooser playerOpenDialog = new JFileChooser("~");
            final SimpleFileFilter fileFilter = new SimpleFileFilter(".ser", "(.ser) Serialised Files");
            playerOpenDialog.addChoosableFileFilter(fileFilter);
            final int playerOpenResponse = playerOpenDialog.showOpenDialog(this);

            if (playerOpenResponse == JFileChooser.APPROVE_OPTION) {
                final String filePath = playerOpenDialog.getSelectedFile().getAbsolutePath();
                try (ObjectInputStream playerIn = new ObjectInputStream(new FileInputStream(filePath))) {
                    final Player openedPlayer = (Player) playerIn.readObject();
                    // Resetta la mano del player
                    openedPlayer.hand = new PlayerCardHand();
                    this.player = openedPlayer;
                } catch (ClassNotFoundException | IOException e) {
                    System.err.println(e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Can't open an existing player while a game is in progress.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updatePlayer() {
        final PlayerDialog playerDetails = new PlayerDialog(null, "Player Details", true, this.player);
        playerDetails.setVisible(true);
        this.player = playerDetails.getPlayer();
    }

    @Override
    public void onEvent(final String message) {
            this.notifyListeners(message);
    }
}
