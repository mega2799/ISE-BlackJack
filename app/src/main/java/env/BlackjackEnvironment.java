package env;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import blackjack.AppWindow;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;

public class BlackjackEnvironment extends Environment {

    // action literals
    public static final Literal bet = Literal.parseLiteral("bet");
    // public static final Literal coldAir = Literal.parseLiteral("spray_air(cold)");



    // Variabile interna per simulare il valore della mano
    private int handValue = 0;

    static final Logger logger = Logger.getLogger(BlackjackEnvironment.class.getName());
    private AppWindow appWindow;

    @Override
    public void init(final String[] args) {
        super.init(args);
		logger.info("Environment: Inizializzazione in corso...");
		logger.info(Arrays.toString(args));
        this.appWindow = new AppWindow();
        // Imposta la credenza iniziale della mano a 0
		//! Da controllare che sia fattibile....
        // try {
        //     // Nota: in un contesto reale potresti voler gestire diversamente l'aggiunta delle credenze iniziali.
        //     getAg().addInitialBelief(Literal.parseLiteral("handValue(0)"));
        // } catch (final Exception e) {
        //     e.printStackTrace();
        // }
        logger.info("Environment: Inizializzazione completata.");
    }

    @Override
    public boolean executeAction(final String agName, final Structure action) {
        final String act = action.getFunctor();
        logger.log(Level.INFO, "Environment: Agente {0} esegue l''azione: {1}", new Object[]{agName, act});
        
        if (null == act) {
            logger.log(Level.INFO, "Environment: Azione non riconosciuta: {0}", act);
            return false;
        }
        else switch (act) {
            case "bet":
                // Simula il piazzamento della puntata
                logger.log(Level.INFO, "Environment: Puntata piazzata dall''agente {0}.", agName);
                // Qui puoi integrare la logica di interazione con il tuo SW di blackjack.
                return true;
            case "askCard":
                // Simula la richiesta di una carta
                logger.log(Level.INFO, "Environment: L''agente {0} richiede una carta.", agName);
                // In una versione reale, qui potresti interfacciarti con il tuo motore di gioco per pescare una carta.
                final int cardValue = this.drawCard();
                this.handValue += cardValue;
                logger.log(Level.INFO, "{0}Environment: Carta pescata con valore . Nuovo handValue = {1}", new Object[]{cardValue, this.handValue});
                
                // Aggiorna la credenza handValue dell'agente:
				//! Da controllare che sia fattibile....
                // try {
                //     // Rimuovi ogni valore precedente della mano (semplice gestione: rimuoviamo tutte le occorrenze di "handValue(...)")
                //     removeBelief("handValue(_)");
                //     // Aggiungi il nuovo valore della mano
                //     addBelief(Literal.parseLiteral("handValue(" + this.handValue + ")"));
                // } catch (final Exception e) {
                //     e.printStackTrace();
                // }
                return true;
            case "updateHandValue":
                // In questo esempio, l'aggiornamento viene già gestito in askCard.
                logger.info("Environment: L'aggiornamento del valore della mano è già stato effettuato.");
                return true;
            default:
                logger.log(Level.INFO, "Environment: Azione non riconosciuta: {0}", act);
                return false;
        }
    }

    /**
     * Metodo fittizio per simulare il pescare una carta.
     * Qui restituisce un valore costante oppure potrebbe generare un valore casuale.
     */
    private int drawCard() {
        // Per semplicità, restituisce sempre un valore che porta la mano a 21 se possibile.
        // In una logica reale, potresti calcolare un valore randomico o seguire regole specifiche.
        final int cardValue = (this.handValue < 21) ? Math.min(10, 21 - this.handValue) : 0;
        return cardValue;
    }
}
