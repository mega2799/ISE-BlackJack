package blackjack.Cards;

public class PlayerCardHand extends CardHand
{
    public PlayerCardHand()
    {
        super();
    }
    
    /**
     * Add a Card to the players hand and calculate the hands new total.
     *
     * @param   card    A card to add to the players hand.
     *
     * @return If the card was added or not successfully.
     */
    public boolean add(final Card card)
    {
        boolean cardAdded = false;
        
        if (!this.isBust() && !this.hasBlackjack())
        {
            cardAdded = super.add(card);
                        
            // if bust, check if all Aces are set to 1, if not, do it!
            if (this.isBust())
            {
                for (final Card eachCard : this)
                {
                    eachCard.getFace().switchAce(); // switch ace to low 1 switching...
                    
                    if (!this.isBust()) // if hand isn't bust anymore, then stop switching
                    {
                        break; // needed to break out of loop to stop switching aces
                    }
                }
            }
        }
        
        return (cardAdded) ? true : false;
    }
}