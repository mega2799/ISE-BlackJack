package env;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import blackjack.AppWindow;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.Environment;

public class BlackjackEnvironment extends Environment {

    // action literals
    public static final Literal bet = Literal.parseLiteral("bet(_)");



    // Variabile interna per simulare il valore della mano
    private int handValue = 0;

    static final Logger logger = Logger.getLogger(BlackjackEnvironment.class.getName());
    private AppWindow appWindow;

    @Override
    public void init(final String[] args) {
        super.init(args);
		logger.info("Inizializzazione in corso...");
		logger.info(Arrays.toString(args));
        this.appWindow = new AppWindow(args.length > 0 ? Double.parseDouble(args[0]) : 1000);
        // Imposta la credenza iniziale della mano a 0
		//! Da controllare che sia fattibile....
        this.addPercept(Literal.parseLiteral("fresca(69)"));
        // try {
        //     // Nota: in un contesto reale potresti voler gestire diversamente l'aggiunta delle credenze iniziali.
        //     getAg().addInitialBelief(Literal.parseLiteral("handValue(0)"));
        // } catch (final Exception e) {
        //     e.printStackTrace();
        // }
        logger.info("Inizializzazione completata.");
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
                // // Simula il piazzamento della puntata
                logger.log(Level.INFO, "Puntata piazzata dall''agente " + agName);
                // // Qui puoi integrare la logica di interazione con il tuo SW di blackjack.
                // return true;
                 if (action.getArity() == 1) {
                    try {
                        final NumberTerm betAmountTerm = (NumberTerm) action.getTerm(0);
                        // currentBet = (int) betAmountTerm.solve();
                        logger.log(Level.INFO, "L'agente " + agName +" ha piazzato una puntata di " +  betAmountTerm);
                        return true;
                    } catch (final Exception e) {
                        logger.log(Level.WARNING, "Errore nell'interpretare l'importo della puntata.", e);
                        return false;
                    }
                } else {
                    logger.log(Level.WARNING, "L'azione bet richiede un parametro (importo della puntata).");
                    return false;
                }

            case "askCard":
                // Simula la richiesta di una carta
                logger.log(Level.INFO, "L''agente " + agName + "richiede una carta.");
                // In una versione reale, qui potresti interfacciarti con il tuo motore di gioco per pescare una carta.
                final int cardValue = this.drawCard();
                this.handValue += cardValue;
                logger.log(Level.INFO, "Carta pescata con valore . Nuovo handValue = {1}", new Object[]{cardValue, this.handValue});
                
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
                logger.info("L'aggiornamento del valore della mano è già stato effettuato.");
                return true;
            default:
                logger.log(Level.INFO, "Azione non riconosciuta: " + act);
                return false;
        }
    }

    //TODO delete this
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
