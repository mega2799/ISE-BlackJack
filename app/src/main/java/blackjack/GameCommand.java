package blackjack;

public enum GameCommand {
    BET_1("$1"),
    BET_5("$5"),
    BET_10("$10"),
    BET_25("$25"),
    BET_100("$100"),
    DEAL("Deal"),
    HIT("Hit"),
    DOUBLE("Double"),
    STAND("Stand"),
    UPDATE_PLAYER("Update Player Details"),
    SAVE_PLAYER("Save Current Player"),
    OPEN_PLAYER("Open Existing Player"),
    CHANGE_TABLE_COLOUR("Change Table Colour"),
    ABOUT_BLACKJACK("About Blackjack"),
    UNKNOWN("");

    private final String command;

    GameCommand(final String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    public static GameCommand fromString(final String command) {
        for (final GameCommand gc : values()) {
            if (gc.command.equalsIgnoreCase(command)) {
                return gc;
            }
        }
        return UNKNOWN;
    }
    
    public static GameCommand parseBet(final Integer betAmount) {
        switch (betAmount) {
            case 1:
                return BET_1;
            case 5:
                return BET_5;
            case 10:
                return BET_10;
            case 25:
                return BET_25;
            case 100:
                return BET_100;
            default:
                throw new IllegalArgumentException("Invalid bet amount: " + betAmount);
        }
    }
}

