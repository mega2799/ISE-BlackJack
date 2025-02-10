package blackjack.Players;

import java.util.ArrayList;
import java.util.List;

import blackjack.Cards.DealerCardHand;
import blackjack.Cards.Deck;
import blackjack.Cards.PlayerCardHand;
import blackjack.EventListener;

/**
 * Class representing the Dealer of a Blackjack game.
 * Dealer must stand on 17 or over and can only accept an Ace as 11.
 *
 * @author David Winter
 */
public class Dealer extends BlackjackPlayer implements EventListener
{
    /**
     * The Deck of cards used for the game. The Dealer is in complete control
     * of the Deck.
     */
    public Deck deck;
    private final List<EventListener> listeners = new ArrayList<>();

    
    public DealerCardHand hand = new DealerCardHand();
    
    /**
     * Whether or not the Dealer has dealt the initial two cards.
     */
    private boolean firstDeal = true;
    
    /**
     * The value the dealer must stand on.
     */
    public static final int DEALER_STANDS_ON = 17;
    public static final int CARD_PACKS = 2;
    
    private boolean gameOver = true;
    private boolean cardsFaceUp = false;
    
    /**
     * Whether the player is allowed to double at this stage in game.
     */
    private boolean playerCanDouble = true;
    
    private String said = "Please place your wager.";
    
    /**
     * Default constructor that creates a new dealer with a deck of
     * 2 card packs.
     */
    public Dealer()
    {
        super("Le Chiffre", 45, "male");
        
        this.deck = new Deck(CARD_PACKS);
        this.deck.addListener(this);
    }
    
    public void say(final String announcement)
    {
        this.said = announcement;
        System.out.println(this.said);
    }
    
    public String says()
    {
        return this.said;
    }
    
    public boolean isGameOver()
    {
        return this.gameOver;
    }
    
    public boolean areCardsFaceUp()
    {
        return this.cardsFaceUp;
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
    /**
     * Acknowledge the bet from the player.
     *
     * @param   player  The player placing the bet.
     * @param   bet     The amount for the bet.
     */
    public boolean acceptBetFrom(final Player player, final double bet)
    {
        final boolean betSet = player.setBet(bet);
        
        if (player.betPlaced())
        {
            this.say("Thank you for your bet of $" + player.getBet() + ". Would you like me to deal?");
        }
        else
        {
            this.say("Please place your bet.");
        }
        
        return (betSet) ? true : false;
    }
    
    /**
     * Deals initial two cards to player and self.
     * 
     * @param   player  The player to deal cards to.
     *
     * @return True if cards were dealt, otherwise false.
     */
    public boolean deal(final Player player)
    {
        boolean cardsDealt = false;
        
        if (player.betPlaced() && !player.isBankrupt())
        {   
            this.gameOver = false;
            this.cardsFaceUp = false;

            this.playerCanDouble = true;
            
            player.hand = new PlayerCardHand();
            this.hand = new DealerCardHand();
            
            this.say("Initial deal made.");
            
            player.hand.add(this.deck.deal());
            this.hand.add(this.deck.deal());
            
            player.hand.add(this.deck.deal());
            this.hand.add(this.deck.deal());
            
            cardsDealt = true;
            this.firstDeal = false; 
            
            if (player.hand.hasBlackjack())
            {
                this.say("Blackjack!");
                this.go(player);
            }
            
        }
        else if (!player.betPlaced())
        {
            this.say("Please place your bets.");
            this.gameOver = true;
        }
        
        return cardsDealt;
    }
    
    /**
     * Player requests another card.
     *
     * @param player The player requesting another card.
     */
    public void hit(final Player player)
    {
        this.say(player.getName() + " hits.");
        player.hand.add(this.deck.deal());
        
        this.playerCanDouble = false;
        
        if (player.hand.isBust())
        {
            this.say(player.getName() + " busts. Loses $" + player.getBet());
            player.loses();
            this.gameOver = true;
        }
    }
    
    /**
     * Player would like to place a bet up to double of his original and
     * have the dealer give him one more card.
     *
     * @param player The player requesting to play double.
     */
    public void playDouble(final Player player)
    {
        if (player.doubleBet() && this.playerCanDouble)
        {
            player.hand.add(this.deck.deal());
            this.say(player.getName() + " plays double.");
            
            if (player.hand.isBust())
            {
                this.say(player.getName() + " busts. Loses $" + player.getBet());
                player.loses();
                this.gameOver = true;
            }
            else
            {
                this.go(player);
            }
        }
        else
        {
            this.say(player.getName() + ", you can't double. Not enough money.");
        }
    }
    
    /**
     * The player wishes to stand. The dealer then takes his go.
     *
     * @param player The player who wishes to stand.
     */
    public void stand(final Player player)
    {
        this.say(player.getName() + " stands. " + this.getName() + " turn.");
        this.go(player);
    }
    
    /**
     * The dealers turn.
     *
     * @param player The opposing player of the dealer.
     */
    private void go(final Player player)
    {
        this.cardsFaceUp = true;
        
        if (!this.hand.hasBlackjack())
        {
            while (this.hand.getTotal() < DEALER_STANDS_ON)
            {
                this.hand.add(this.deck.deal());
                this.say(this.getName() + " hits.");
            }
            
            if (this.hand.isBust())
            {
                this.say(this.getName() + " is BUST");
                this.notifyListeners("dealer_bust");
            }
            else
            {
                this.say(this.getName() + " stands on " + this.hand.getTotal());
            }            
        }
        else
        {
            this.say(this.getName() + " has BLACKJACK!");
        }
        
        if (this.hand.hasBlackjack() && player.hand.hasBlackjack())
        {
            this.say("Push");
            player.clearBet();
        }
        else if (player.hand.hasBlackjack())
        {
            final double winnings = (player.getBet() * 3) / 2;
            this.say(player.getName() + " wins with Blackjack $" + winnings);
            player.wins(player.getBet() + winnings);
        }
        else if (this.hand.hasBlackjack())
        {
            this.say("Dealer has Blackjack. " + player.getName() + " loses $" + player.getBet());
            player.loses();
        }
        else if (this.hand.isBust())
        {
            this.say("Dealer is bust. " + player.getName() + " wins $" + player.getBet());
            player.wins(player.getBet() * 2);
        }
        else if (player.hand.getTotal() == this.hand.getTotal())
        {
            this.say("Push");
            player.clearBet();
        }
        else if (player.hand.getTotal() < this.hand.getTotal())
        {
            this.say(player.getName() + " loses $" + player.getBet());
            player.loses();
        }
        else if (player.hand.getTotal() > this.hand.getTotal())
        {
            this.say(player.getName() + " wins $" + player.getBet());
            player.wins(player.getBet() * 2);
        }
        
        this.gameOver = true;
    }
    
    public int cardsLeftInPack()
    {
        return this.deck.size();
    }
    
    public void newDeck()
    {
        this.deck = new Deck(CARD_PACKS);
    }
    
    public boolean canPlayerDouble(final Player player)
    {
        return (this.playerCanDouble && player.canDouble()) ? true : false;
    }
    
    public DealerCardHand getHand()
    {
        return this.hand;
    }

    @Override
    public void onEvent(final String message) {
        this.notifyListeners(message);
    }
}