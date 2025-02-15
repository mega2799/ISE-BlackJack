package env;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import blackjack.AppWindow;
import blackjack.GameCommand;
import blackjack.GamePanel;
import blackjack.Players.Dealer;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
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

	public static void sendToAgentHandToCount(final TwentyOneEnvironment twentyOneEnvironment, final String agName,
            final List<Integer> cardValues) {
        final ListTerm cardList = new ListTermImpl();
        for (final Integer card : cardValues) {
            cardList.add(new NumberTermImpl(card));
        }
        twentyOneEnvironment.addPercept(Literal.parseLiteral("update_counts(" + cardList.toString() + ")"));
    }

     public static Object findMode(final List<Object> list) {
        logger.log(Level.INFO, "Calcolo della moda della lista: " + list);
        final Map<Object, Integer> freqMap = new HashMap<>();
        int maxCount = 0;
        Object mode = null;

        // Conta le occorrenze
        for (final Object elem : list) {
            freqMap.put(elem, freqMap.getOrDefault(elem, 0) + 1);
            if (freqMap.get(elem) > maxCount) {
                maxCount = freqMap.get(elem);
                mode = elem;
            }
        }
        return mode;
    }

}
