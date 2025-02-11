package env;

import java.util.logging.Level;
import java.util.logging.Logger;

import blackjack.AppWindow;
import blackjack.GameCommand;
import blackjack.GamePanel;
import blackjack.Players.Dealer;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;

public class GameEnvUtils {

    static final Logger logger = Logger.getLogger(GameEnvUtils.class.getName());

    public static Boolean betManage(final String agName, final Structure action, final AppWindow appWindow) {
        if (action.getArity() == 1) {
            try {
                final NumberTerm betAmountTerm = (NumberTerm) action.getTerm(0);
                // currentBet = (int) betAmountTerm.solve();
                logger.log(Level.INFO,
                        "L'agente " + agName + " ha piazzato una puntata di " + betAmountTerm);
                final GameCommand betCommand = GameCommand
                        .parseBet(Integer.valueOf(betAmountTerm.toString()));
                appWindow.actionPerformed(betCommand);
                return true;
            } catch (final Exception e) {
                logger.log(Level.WARNING, "Errore nell'interpretare l'importo della puntata.", e);
                return false;
            }
        } else {
            logger.log(Level.WARNING, "L'azione bet richiede un parametro (importo della puntata).");
            return false;
        }
    }

	public static void sendToAgentCards(final TwentyOneEnvironment twentyOneEnvironment, final String agName, final GamePanel gamePanel) {
		final int value = gamePanel.getPlayer().hand.getTotal();
		twentyOneEnvironment.removePerceptsByUnif(agName, Literal.parseLiteral("hand_value(_)"));
		twentyOneEnvironment.addPercept(agName, Literal.parseLiteral("hand_value(" + value + ")"));
	}

	public static void checkBusted(final TwentyOneEnvironment twentyOneEnvironment, final String agName, final Dealer dealer) {
		if(dealer.hand.isBust()){
			twentyOneEnvironment.addPercept(agName, Literal.parseLiteral("dealer_busted(true)"));
		}else{
			twentyOneEnvironment.addPercept(agName, Literal.parseLiteral("dealer_busted(false)"));
			final int scoreDealer = dealer.hand.getTotal();
			twentyOneEnvironment.addPercept(agName, Literal.parseLiteral("dealer_score(" + scoreDealer + ")"));
		}
	}

}
