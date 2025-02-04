package env;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

import blackjack.AppWindow;
import blackjack.GameCommand;
import blackjack.GamePanel;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.Environment;

public class BlackjackEnvironment extends Environment {

    public static final Literal bet = Literal.parseLiteral("bet(_)");


    static final Logger logger = Logger.getLogger(BlackjackEnvironment.class.getName());
    private AppWindow appWindow;
    private GamePanel gamePanel;

    @Override
    public void init(final String[] args) {
        super.init(args);
		logger.info("Inizializzazione in corso...");
		logger.info(Arrays.toString(args));
        this.beatufiySwing();           
        this.appWindow = new AppWindow(args.length > 0 ? Double.parseDouble(args[0]) : 1000);
        this.gamePanel = this.appWindow.getGamePanel();
		//! Da controllare che sia fattibile....
        // try {
        //     // Nota: in un contesto reale potresti voler gestire diversamente l'aggiunta delle credenze iniziali.
        //     getAg().addInitialBelief(Literal.parseLiteral("handValue(0)"));
        // } catch (final Exception e) {
        //     e.printStackTrace();
        // }
        logger.info("Inizializzazione completata.");
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void beatufiySwing() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (final UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }


        UIManager.put("Button.arc", 999);                       // bottoni rotondi
        UIManager.put("Button.background", "#E0E0E0");            // colore di sfondo
        UIManager.put("Button.foreground", "#333333");            // colore del testo
        UIManager.put("Button.font", new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        UIManager.put("Button.focusWidth", 0);
    }

    @Override
    @SuppressWarnings("LoggerStringConcat")
    public boolean executeAction(final String agName, final Structure action) {

        if (action == null) {
            logger.log(Level.WARNING, "Azione NULL ricevuta");
            return false;
        }

        final String act = action.getFunctor();
        logger.log(Level.INFO, "Agente " + agName + " esegue l''azione " + act);
        logger.log(Level.INFO, "Azione riconosciuta: " + act);
        
        if (null == act) {
            logger.log(Level.INFO, "Azione non riconosciuta: {0}", act);
            return false;
        }
        else switch (act) {
            case "bet":
                 if (action.getArity() == 1) {
                    try {
                        final NumberTerm betAmountTerm = (NumberTerm) action.getTerm(0);
                        // currentBet = (int) betAmountTerm.solve();
                        logger.log(Level.INFO, "L'agente " + agName +" ha piazzato una puntata di " +  betAmountTerm);
                        final GameCommand betCommand = GameCommand.parseBet(Integer.parseInt(betAmountTerm.toString()));
                        this.appWindow.actionPerformed(betCommand);
                        return true;
                    } catch (final Exception e) {
                        logger.log(Level.WARNING, "Errore nell'interpretare l'importo della puntata.", e);
                        return false;
                    }
                } else {
                    logger.log(Level.WARNING, "L'azione bet richiede un parametro (importo della puntata).");
                    return false;
                }
            
            case "deal":
                logger.log(Level.INFO, "L'agente " + agName + " ha richiesto di iniziare una nuova partita.");
                this.appWindow.actionPerformed(GameCommand.DEAL);
                return true;
            case "check_hand_value":
                logger.log(Level.INFO, "L'agente " + agName + " ha richiesto il valore della mano.");
                // Invia all'agente il valore della mano
                this.addPercept(agName, Literal.parseLiteral("hand_value(" + this.gamePanel.getPlayer().hand.getTotal() + ")"));
                return true;
            case "askCard":
                // Simula la richiesta di una carta
                logger.log(Level.INFO, "L'agente " + agName + "richiede una carta.");
                this.appWindow.actionPerformed(GameCommand.HIT);
                this.addPercept(agName, Literal.parseLiteral("hand_value(" + this.gamePanel.getPlayer().hand.getTotal() + ")"));
                return true;
            case "stand":
                logger.log(Level.INFO, "L'agente " + agName + " ha deciso di stare.");
                this.appWindow.actionPerformed(GameCommand.STAND);
                return true;
            default:
                logger.log(Level.INFO, "Azione non riconosciuta: " + act);
                return false;
        }
    }
}
