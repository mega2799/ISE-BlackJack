package env;

import java.util.Arrays;

import jason.asSyntax.Structure;
import jason.environment.Environment;

public class BlackjackEnvironment extends Environment {

    // Variabile interna per simulare il valore della mano
    private int handValue = 0;

    @Override
    public void init(final String[] args) {
        super.init(args);
		System.out.println("Environment: Inizializzazione in corso...");
		System.out.println(Arrays.toString(args));
        // Imposta la credenza iniziale della mano a 0
		//! Da controllare che sia fattibile....
        // try {
        //     // Nota: in un contesto reale potresti voler gestire diversamente l'aggiunta delle credenze iniziali.
        //     getAg().addInitialBelief(Literal.parseLiteral("handValue(0)"));
        // } catch (final Exception e) {
        //     e.printStackTrace();
        // }
        System.out.println("Environment: Inizializzazione completata.");
    }

    @Override
    public boolean executeAction(final String agName, final Structure action) {
        final String act = action.getFunctor();
        System.out.println("Environment: Agente " + agName + " esegue l'azione: " + act);
        
        if (null == act) {
            System.out.println("Environment: Azione non riconosciuta: " + act);
            return false;
        }
        else switch (act) {
            case "bet":
                // Simula il piazzamento della puntata
                System.out.println("Environment: Puntata piazzata dall'agente " + agName + ".");
                // Qui puoi integrare la logica di interazione con il tuo SW di blackjack.
                return true;
            case "askCard":
                // Simula la richiesta di una carta
                System.out.println("Environment: L'agente " + agName + " richiede una carta.");
                // In una versione reale, qui potresti interfacciarti con il tuo motore di gioco per pescare una carta.
                final int cardValue = this.drawCard();
                this.handValue += cardValue;
                System.out.println("Environment: Carta pescata con valore " + cardValue + ". Nuovo handValue = " + this.handValue);
                
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
                System.out.println("Environment: L'aggiornamento del valore della mano è già stato effettuato.");
                return true;
            default:
                System.out.println("Environment: Azione non riconosciuta: " + act);
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
