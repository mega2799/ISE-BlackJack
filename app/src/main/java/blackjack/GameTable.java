package blackjack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JPanel;

import blackjack.Cards.Card;
import blackjack.Cards.CardPack;
import blackjack.Cards.DealerCardHand;
import blackjack.Cards.PlayerCardHand;

public class GameTable extends JPanel
{
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
    
    private final Image[] cardImages = new Image[CardPack.CARDS_IN_PACK + 1];
    
    // take game model as parameter so that it can get cards and draw them
    public GameTable()
    {
        super();
        
        this.setBackground(Color.BLUE);
        this.setOpaque(false);
        
        this.handTotalFont = new Font("Serif", Font.PLAIN, 96);
        this.playerNameFont = new Font("Serif", Font.ITALIC, 20);
        
        this.showAllDealerCards = true;
        
        for (int i = 0; i < CardPack.CARDS_IN_PACK; i++)
        {
            // final String cardName = "card_images/" + (i+1) + ".png";
            System.out.println("card_images/"+ (i+1) + ".png");
            final URL cardName = ClassLoader.getSystemResource("card_images/"+ (i+1) + ".png");
            
            // System.out.println(this.getClass().getResource(cardName));
            // final URL urlImg = this.getClass().getResource(cardName);
            System.out.println(cardName);
            final Image cardImage = Toolkit.getDefaultToolkit().getImage(cardName);
            this.cardImages[i] = cardImage;
        }
        
        final String backCard = "card_images/red_back.png";
        
        // final URL backCardURL = this.getClass().getResource(backCard);
        final URL backCardURL  = ClassLoader.getSystemResource(backCard);
        final Image backCardImage = Toolkit.getDefaultToolkit().getImage(backCardURL);
        
        this.cardImages[CardPack.CARDS_IN_PACK] = backCardImage;
        
        final MediaTracker imageTracker = new MediaTracker(this);
        
        for (int i = 0; i < CardPack.CARDS_IN_PACK + 1; i++)
        {
            imageTracker.addImage(this.cardImages[i], i + 1); 
        }
        
        try
        {
            imageTracker.waitForAll();
        }
        catch (final InterruptedException excep)
        {
            System.out.println("Interrupted while loading card images.");
        }
    }
    
    public void setNames(final String dealerName, final String playerName)
    {
        this.dealerName = dealerName;
        this.playerName = playerName;
    }
    
    public void update(final DealerCardHand dealer, final PlayerCardHand player, final boolean showDealer)
    {        
        this.dealer = dealer;
        this.player = player;
        this.showAllDealerCards = showDealer;
    }
    
    // draw images from jar archive or dir: http://www.particle.kth.se/~fmi/kurs/PhysicsSimulation/Lectures/10B/jar.html
    
    public void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        
        g.setColor(Color.WHITE);
        
        g.setFont(this.playerNameFont);
        
        g.drawString(this.dealerName, this.CARD_START, this.DEALER_POSITION - this.NAME_SPACE);
        g.drawString(this.playerName, this.CARD_START, this.PLAYER_POSITION - this.NAME_SPACE);
        
        g.setFont(this.handTotalFont);
        
        final String cardName;
    
        // draw dealer cards
    
        int i = this.CARD_START;
    
        if (this.showAllDealerCards)
        {
            for (final Card aCard : this.dealer)
            {
                g.drawImage(this.cardImages[aCard.getCode() - 1], i, this.DEALER_POSITION, this);

                i += this.CARD_INCREMENT;
            }
        
            g.drawString(Integer.toString(this.dealer.getTotal()), i 
                + this.CARD_IMAGE_WIDTH + this.CARD_INCREMENT, this.DEALER_POSITION 
                + this.CARD_IMAGE_HEIGHT);
        }
        else
        {
            for (final Card aCard : this.dealer)
            {
                g.drawImage(this.cardImages[CardPack.CARDS_IN_PACK], i, this.DEALER_POSITION, this);

                i += this.CARD_INCREMENT;
            }
        
            try
            {
                final Card topCard = this.dealer.lastElement();
            
                i -= this.CARD_INCREMENT;
            
                g.drawImage(this.cardImages[topCard.getCode() - 1], i, this.DEALER_POSITION, this);
            
                
                    
                //
                
            }
            catch (final Exception e)
            {
                // caused when trying to draw cards from empty vector
                // can't use NoSuchElementException above...?
                System.out.println("No cards have been dealt yet.");
            }
            
            g.drawString("?", i + this.CARD_IMAGE_WIDTH + this.CARD_INCREMENT, 
                this.DEALER_POSITION + this.CARD_IMAGE_HEIGHT);
            
        }
    
        // draw player cards
    
        i = this.CARD_START;

        for (final Card aCard : this.player)
        {
            g.drawImage(this.cardImages[aCard.getCode() - 1], i, this.PLAYER_POSITION, this);

            i += this.CARD_INCREMENT;
        }
    
        g.drawString(Integer.toString(this.player.getTotal()), i + this.CARD_IMAGE_WIDTH + this.CARD_INCREMENT, this.PLAYER_POSITION + this.CARD_IMAGE_HEIGHT); 
    }
}