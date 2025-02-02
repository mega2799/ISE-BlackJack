package blackjack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import blackjack.Cards.Card;
import blackjack.Cards.CardPack;
import blackjack.Cards.DealerCardHand;
import blackjack.Cards.PlayerCardHand;

public class GameTable extends JPanel {
    private DealerCardHand dealer;
    private PlayerCardHand player;
    
    private boolean showAllDealerCards;
    
    // drawing position vars
    private final int CARD_INCREMENT = 20;
    private final int CARD_START = 100;
    private final int DEALER_POSITION = 50;
    private final int PLAYER_POSITION = 200;
    
    private final int CARD_IMAGE_WIDTH = 71;
    private final int CARD_IMAGE_HEIGHT = 96;
    
    private final int NAME_SPACE = 10;
    
    private final Font handTotalFont;
    private final Font playerNameFont;
    
    private String dealerName;
    private String playerName;
    
    // Utilizzo BufferedImage per immagini moderne
    private final BufferedImage[] cardImages = new BufferedImage[CardPack.CARDS_IN_PACK + 1];
    
    public GameTable() {
        super();
        this.setBackground(Color.BLUE);
        this.setOpaque(false);
        
        this.handTotalFont = new Font("Arial", Font.PLAIN, 96);
        this.playerNameFont = new Font("Arial", Font.ITALIC, 20);
        
        this.showAllDealerCards = true;
        
        // Caricamento delle immagini delle carte
        for (int i = 0; i < CardPack.CARDS_IN_PACK; i++) {
            final String imagePath = "card_images/" + (i + 1) + ".png";
            final URL imageUrl = ClassLoader.getSystemResource(imagePath);
            try {
                this.cardImages[i] = ImageIO.read(imageUrl);
            } catch (final IOException ex) {
                System.err.println("Errore nel caricamento dell'immagine: " + imagePath);
            }
        }
        
        // Caricamento dell'immagine del retro della carta
        final String backCardPath = "card_images/red_back.png";
        final URL backCardUrl = ClassLoader.getSystemResource(backCardPath);
        try {
            this.cardImages[CardPack.CARDS_IN_PACK] = ImageIO.read(backCardUrl);
        } catch (final IOException ex) {
            System.err.println("Errore nel caricamento dell'immagine: " + backCardPath);
        }
    }
    
    public void setNames(final String dealerName, final String playerName) {
        this.dealerName = dealerName;
        this.playerName = playerName;
    }
    
    public void update(final DealerCardHand dealer, final PlayerCardHand player, final boolean showDealer) {        
        this.dealer = dealer;
        this.player = player;
        this.showAllDealerCards = showDealer;
        // Richiedi la ridipintura del pannello dopo l'aggiornamento
        this.repaint();
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        
        // Uso di Graphics2D per una grafica migliore
        final Graphics2D g2d = (Graphics2D) g;
        // Abilita l'antialiasing per testi e grafica
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Disegna i nomi dei giocatori
        g2d.setColor(Color.WHITE);
        g2d.setFont(this.playerNameFont);
        if (this.dealerName != null) {
            g2d.drawString(this.dealerName, this.CARD_START, this.DEALER_POSITION - this.NAME_SPACE);
        }
        if (this.playerName != null) {
            g2d.drawString(this.playerName, this.CARD_START, this.PLAYER_POSITION - this.NAME_SPACE);
        }
        
        // Disegna le carte del dealer
        g2d.setFont(this.handTotalFont);
        int xPos = this.CARD_START;
        if (this.dealer != null) {
            if (this.showAllDealerCards) {
                for (final Card aCard : this.dealer) {
                    // Si assume che getCode restituisca un valore 1-based
                    g2d.drawImage(this.cardImages[aCard.getCode() - 1], xPos, this.DEALER_POSITION, this);
                    xPos += this.CARD_INCREMENT;
                }
                // Disegna il totale delle carte
                g2d.drawString(Integer.toString(this.dealer.getTotal()),
                               xPos + this.CARD_IMAGE_WIDTH + this.CARD_INCREMENT,
                               this.DEALER_POSITION + this.CARD_IMAGE_HEIGHT);
            } else {
                // Disegna il retro di tutte le carte
                for (final Card aCard : this.dealer) {
                    g2d.drawImage(this.cardImages[CardPack.CARDS_IN_PACK], xPos, this.DEALER_POSITION, this);
                    xPos += this.CARD_INCREMENT;
                }
                try {
                    // Recupera e ridisegna l'ultima carta (a faccia)
                    final Card topCard = this.dealer.lastElement();
                    xPos -= this.CARD_INCREMENT;
                    g2d.drawImage(this.cardImages[topCard.getCode() - 1], xPos, this.DEALER_POSITION, this);
                } catch (final Exception e) {
                    // Gestione dell'assenza di carte
                    // System.out.println("Nessuna carta ancora distribuita per il dealer.");
                }
                g2d.drawString("?",
                               xPos + this.CARD_IMAGE_WIDTH + this.CARD_INCREMENT,
                               this.DEALER_POSITION + this.CARD_IMAGE_HEIGHT);
            }
        }
        
        // Disegna le carte del giocatore
        xPos = this.CARD_START;
        if (this.player != null) {
            for (final Card aCard : this.player) {
                g2d.drawImage(this.cardImages[aCard.getCode() - 1], xPos, this.PLAYER_POSITION, this);
                xPos += this.CARD_INCREMENT;
            }
            g2d.drawString(Integer.toString(this.player.getTotal()),
                           xPos + this.CARD_IMAGE_WIDTH + this.CARD_INCREMENT,
                           this.PLAYER_POSITION + this.CARD_IMAGE_HEIGHT);
        }
    }
}
