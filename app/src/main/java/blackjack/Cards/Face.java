package blackjack.Cards;

/**
 * Class to represent the face value of a Card.
 *
 * @author David Winter
 */
public class Face
{
    /**
     * String representation of the face value.
     */
    private String name;
    
    /**
     * Integer representation of the face value.
     */
    private int value;
    
    private final int HIGH_ACE = 11;
    private final int LOW_ACE = 1;
    
    /**
     * Conversion constructor that creates a Face object based on
     * an Integer code.
     *
     * @param   face    The Integer code that represents a face value.
     */
    public Face(final int face)
    {
        switch (face)
        {
            case 1:
                this.setName("Ace");
                this.setValue(11);
                break;
            case 2:
                this.setName("Two");
                this.setValue(2);
                break;
            case 3:
                this.setName("Three");
                this.setValue(3);
                break;
            case 4:
                this.setName("Four");
                this.setValue(4);
                break;
            case 5:
                this.setName("Five");
                this.setValue(5);
                break;
            case 6:
                this.setName("Six");
                this.setValue(6);
                break;
            case 7:
                this.setName("Seven");
                this.setValue(7);
                break;
            case 8:
                this.setName("Eight");
                this.setValue(8);
                break;
            case 9:
                this.setName("Nine");
                this.setValue(9);
                break;
            case 10:
                this.setName("Ten");
                this.setValue(10);
                break;
            case 11:
                this.setName("Jack");
                this.setValue(10);
                break;
            case 12:
                this.setName("Queen");
                this.setValue(10);
                break;
            case 13:
                this.setName("King");
                this.setValue(10);
                break;
            default:
                break;
        }
    }
    
    /**
     * Mutator method that sets the String representation of the face value.
     *
     * @param   name    The String representation of the face value.
     */
    private void setName(final String name)
    {
        this.name = name;
    }
    
    /**
     * Accessor method that returns the name of a face value.
     *
     * @return The name of the face value.
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Mutator method that sets the Integer value of a face value.
     *
     * @param   value   The Integer value of the face value.
     */
    private void setValue(final int value)
    {
        this.value = value;
    }
    
    /**
     * Accessor method that returns the Integer value of the face value.
     *
     * @return  The Integer value of the face value.
     */
    public int getValue()
    {
        return this.value;
    }
    
    public boolean isAce()
    {
        return ("Ace".equals(this.name)) ? true : false;
    }
    
    public boolean isLowAce()
    {
        return ("Ace".equals(this.name) && this.getValue() == this.LOW_ACE) ? true : false;
    }
    
    public void switchAce()
    {
        if (this.isAce())
        {
            if (this.getValue() == this.HIGH_ACE)
            {
                this.setValue(this.LOW_ACE);
            }
        }
    }
    
    /**
     * String representation of the face value.
     *
     * @return  The String representation of the face value.
     */
    public String toString()
    {
        return this.getName();
    }
}