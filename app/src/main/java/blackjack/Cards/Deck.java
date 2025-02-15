package blackjack.Cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import blackjack.EventListener;

/**
 * Represents a shuffled Deck of playing Cards.
 *
 * @author David Winter
 */
public class Deck extends Stack<Card> implements  EventListener
{
        private final List<EventListener> listeners = new ArrayList<>();

    /**
     * The number of Card Packs used for this Deck.
     */
    private int numberOfPacks;
    
    /**
     * Default constructor that will create a Deck from 1 Card Pack.
     */
    public Deck()
    {
        super();
        
        this.setNumberOfPacks(1);
        
        this.addAll(new CardPack());
        
        this.shuffle();
    }
    
    /**
     * Conversion constructor that will create a Deck from the specified 
     * number of Card Packs.
     *
     * @param   numberOfPacks   The number of Card Packs to use.
     */
    public Deck(final int numberOfPacks)
    {
        super();
        
        this.setNumberOfPacks(numberOfPacks);
        
        for (int i = 0; i < numberOfPacks; i++)
        {
            this.addAll(new CardPack());
        }
        
        this.shuffle();
    }
    
    /**
     * Mutator method that sets the number of Card Packs used for this deck.
     *
     * @param   number  The number of Card Packs used for this deck.
     */
    private void setNumberOfPacks(final int number)
    {
        this.numberOfPacks = number;
    }
    
    /**
     * Accessor method that returns the number of Card Packs used
     * for this deck.
     *
     * @return The number of Card Packs used for this deck.
     */
    public int getNumberOfPacks()
    {
        return this.numberOfPacks;
    }
    
    /**
     * Shuffles the Deck of cards.
     */
    public void shuffle()
    {
        Collections.shuffle(this);
    }
    
    /**
     * Only the dealer should deal the card from the Deck.
     */
    public Card deal()
    {
        if (this.empty())
        {
            this.notifyListeners("Deck");
            System.out.println("Run out of cards. New Deck.");
            for (int i = 0; i < this.numberOfPacks; i++)
            {
                this.addAll(new CardPack());
            }

            this.shuffle();
        }
        
        return this.pop();
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
    @Override
    public void onEvent(final String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onEvent'");
    }
}