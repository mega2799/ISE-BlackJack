package env;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

import blackjack.AppWindow;
import blackjack.Cards.Card;
import blackjack.GameCommand;
import blackjack.GamePanel;
import static env.BlackjackEnvironment.AgentClassifier.HILO;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Structure;
import jason.environment.Environment;

public class BlackjackEnvironment extends Environment {

    public enum AgentClassifier {
        HILO("gamblerhilo"),
        BASIC("gambler"),
        ADVANCED("bho");

        public final String name;

        AgentClassifier(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }

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
        this.addPercept(Literal.parseLiteral("debug_mode(" + (Integer.parseInt(args[1]) == 1 ? "on" : "off") + ")"));
        logger.info("Inizializzazione completata.");
        // logger.setLevel(Level.WARNING);
        logger.setLevel(Level.INFO);
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
            logger.log(Level.WARNING, "Azione non riconosciuta: {0}", act);
            return false;
        } else {
            switch (act) {
                case "bet":
                    if (action.getArity() == 1) {
                        try {
                            final NumberTerm betAmountTerm = (NumberTerm) action.getTerm(0);
                            // currentBet = (int) betAmountTerm.solve();
                            logger.log(Level.INFO,
                                    "L'agente " + agName + " ha piazzato una puntata di " + betAmountTerm);
                            final GameCommand betCommand = GameCommand
                                    .parseBet(Integer.parseInt(betAmountTerm.toString()));
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
                    logger.log(Level.WARNING, "L'agente " + agName + " ha richiesto di iniziare una nuova partita.");
                    this.appWindow.actionPerformed(GameCommand.DEAL);

                    // if (agName.indexOf("hilo") > 1) {
                    if (HILO.getName().equals(agName)) {
                        // aggiungo i valori delle mie carte al conteggio
                        final ListTerm cardList = new ListTermImpl();
                        for (final Integer card : this.gamePanel.getPlayer().hand.stream().map(Card::getValue)
                                .collect(Collectors.toList())) {
                            cardList.add(new NumberTermImpl(card));
                        }
                        logger.log(Level.INFO, "Le carte della prima mano sono: " + cardList.toString());
                        this.addPercept(agName, Literal.parseLiteral("update_counts(" + cardList.toString() + ")"));
                    }
                    return true;
                case "check_hand_value":
                    logger.log(Level.WARNING, "L'agente " + agName + " ha richiesto il valore della mano.");
                    logger.log(Level.INFO, "Valore della mano: " + this.gamePanel.getPlayer().hand.getTotal());
                    logger.log(Level.INFO,
                            "Il giocatore e\' stato sconfitto dopo il check_hand_value?: "
                            + this.gamePanel.getPlayer().hand.isBust());
                    logger.log(Level.INFO,
                            "Il Dealer e\' stato sconfitto dopo il check_hand_value?: "
                            + this.gamePanel.getDealer().hand.isBust());
                    this.removePerceptsByUnif(agName, Literal.parseLiteral("hand_value(_)"));
                    if (!this.gamePanel.getPlayer().hand.isBust()) {
                        logger.log(Level.INFO, "Il sistema inserisce val: " + "hand_value("
                                + this.gamePanel.getPlayer().hand.getTotal() + ")");
                        this.addPercept(agName,
                                Literal.parseLiteral("hand_value(" + this.gamePanel.getPlayer().hand.getTotal() + ")"));
                    } else {
                        logger.log(Level.INFO,
                                "Il giocatore ha sballato, val: " + this.gamePanel.getPlayer().hand.getTotal());
                        this.addPercept(agName,
                                Literal.parseLiteral("hand_value(" + this.gamePanel.getPlayer().hand.getTotal() + ")"));
                    }
                    return true;
                case "askCard":
                    logger.log(Level.WARNING, "L'agente " + agName + " richiede una carta.");
                    logger.log(Level.INFO,
                            "Valore della mano prima: " + this.gamePanel.getPlayer().hand.getTotal());
                    System.out.println("agName.indexOf('hilo'): " + agName.indexOf("hilo"));
                    if (HILO.getName().equals(agName)) {
                        logger.log(Level.INFO, "L'agente " + agName + " e\' un hilo.");
                        final int beforeScore = this.gamePanel.getPlayer().hand.getTotal();
                        this.appWindow.actionPerformed(GameCommand.HIT);
                        final int afterScore = this.gamePanel.getPlayer().hand.getTotal();
                        final int diff = afterScore - beforeScore;
                        logger.log(Level.INFO, "Il sistema inserisce val: " + "card_seen(" + diff + ")");
                    } else {
                        this.appWindow.actionPerformed(GameCommand.HIT);
                    }
                    logger.log(Level.INFO,
                            "Aggiungo il nuovo valore della mano" + this.gamePanel.getPlayer().hand.getTotal());
                    logger.log(Level.INFO,
                            "Il giocatore e\' stato sconfitto dopo ask_card?: "
                            + this.gamePanel.getPlayer().hand.isBust());
                    logger.log(Level.INFO,
                            "Il Dealer e\' stato sconfitto dopo ask_card?: "
                            + this.gamePanel.getDealer().hand.isBust());
                    this.removePerceptsByUnif(agName, Literal.parseLiteral("hand_value(_)"));
                    if (!this.gamePanel.getPlayer().hand.isBust()) {
                        logger.log(Level.INFO, "Il sistema inserisce val: " + "hand_value("
                                + this.gamePanel.getPlayer().hand.getTotal() + ")");
                        this.addPercept(agName,
                                Literal.parseLiteral("hand_value(" + this.gamePanel.getPlayer().hand.getTotal() + ")"));
                    } else {
                        logger.log(Level.INFO,
                                "Il giocatore ha sballato, val: " + this.gamePanel.getPlayer().hand.getTotal());
                        this.addPercept(agName,
                                Literal.parseLiteral("hand_value(" + this.gamePanel.getPlayer().hand.getTotal() + ")"));
                    }
                    return true;
                case "stand":
                    logger.log(Level.WARNING, "L'agente " + agName + " ha deciso di stare.");
                    this.appWindow.actionPerformed(GameCommand.STAND);
                    return true;
                default:
                    logger.log(Level.WARNING, "Azione non riconosciuta: " + act);
                    return false;
            }
        }
    }
}
