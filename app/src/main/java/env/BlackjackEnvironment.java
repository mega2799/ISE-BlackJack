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
import blackjack.EventListener;
import blackjack.GameCommand;
import blackjack.GamePanel;
import static env.BlackjackEnvironment.AgentClassifier.DEALER;
import static env.BlackjackEnvironment.AgentClassifier.HILO;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Structure;
import jason.environment.Environment;

public class BlackjackEnvironment extends Environment implements EventListener {

    public enum AgentClassifier {
        DEALER("dealer"),
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
        this.gamePanel.addListener(this);
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
                case "deal":
                    logger.log(Level.WARNING, "L'agente " + agName + " ha richiesto di iniziare una nuova partita.");
                    this.appWindow.actionPerformed(GameCommand.DEAL);

                    //HILO agent update cards
                    if (HILO.getName().equals(agName)) {
                        final ListTerm cardList = new ListTermImpl();
                        for (final Integer card : this.gamePanel.getPlayer().hand.stream().map(Card::getValue)
                                .collect(Collectors.toList())) {
                            cardList.add(new NumberTermImpl(card));
                        }
                        logger.log(Level.INFO, "Le carte della prima mano del player sono: " + cardList.toString());
                        this.addPercept(HILO.name, Literal.parseLiteral("update_counts(" + cardList.toString() + ")"));
                    }
                    //DEALER agent update cards
                    final ListTerm cardList = new ListTermImpl();
                    for (final Integer card : this.gamePanel.getDealer().hand.stream().map(Card::getValue)
                            .collect(Collectors.toList())) {
                        cardList.add(new NumberTermImpl(card));
                    }
                    //formalmente la prima non la vede il player ma l'environemnt si
                    logger.log(Level.INFO,
                            "Le carte della prima mano del dealer sono: "
                                    + cardList.subList(1, cardList.size()).toString());

                    this.addPercept(DEALER.name, Literal
                            .parseLiteral("tell_cards(" + cardList.subList(1, cardList.size()).toString() + ")"));
                    //Check Hand Value
                    logger.log(Level.INFO, "Valore della mano: " + this.gamePanel.getPlayer().hand.getTotal());
                    logger.log(Level.INFO,
                            "Il giocatore e\' stato sconfitto dopo il check_hand_value?: "
                                    + this.gamePanel.getPlayer().hand.isBust());
                    logger.log(Level.INFO,
                            "Il Dealer e\' stato sconfitto dopo il check_hand_value?: "
                                    + this.gamePanel.getDealer().hand.isBust());
                    this.checkIfBusted(agName, act);
                    return true;
                case "end_game":
                    this.getEndPlayerCardList();
                    return true;
                case "stand":
                    logger.log(Level.WARNING, "L'agente " + agName + " ha deciso di stare.");
                    this.appWindow.actionPerformed(GameCommand.STAND);
                    // final ListTerm dealerCardList = new ListTermImpl();
                    // for (final Integer card : this.gamePanel.getDealer().hand.stream().map(Card::getValue)
                    //         .collect(Collectors.toList())) {
                    //     dealerCardList.add(new NumberTermImpl(card));
                    // }
                    // logger.log(Level.INFO, "Le carte del dealer sono: " + dealerCardList.toString());
                    // this.addPercept(DEALER.name, Literal
                    //         .parseLiteral("tell_hand(" + this.gamePanel.getDealer().hand.getTotal() + ")"));
                    // dealerCardList.remove(1);
                    // logger.log(Level.INFO, "Le carte del dealer mai state viste sono: " + dealerCardList.toString());
                    // this.addPercept(DEALER.name, Literal
                    //         .parseLiteral("tell_cards(" + dealerCardList.toString() + ")"));
                    // this.executeAction(agName, new Structure(Literal.parseLiteral("end_game")));
                    //!duplicato???? ma almeno no si blocca ad un han_value random.......
                    // this.removePerceptsByUnif(agName, Literal.parseLiteral("hand_value(_)"));
                    this.getEndPlayerCardList();
                    return true;
                default:
                    if (DEALER.getName().equals(agName)) {
                        return this.manageDealerAction(agName, action, act);
                    } else {
                        return this.manageGamblerAction(agName, action, act);
                    }

            }
        }
    }

    @SuppressWarnings("LoggerStringConcat")
    private void getEndPlayerCardList() {
        //dealer has to show his cards
        final ListTerm endDealerCardList = new ListTermImpl();
        for (final Integer card : this.gamePanel.getDealer().hand.stream().map(Card::getValue)
                .collect(Collectors.toList())) {
            endDealerCardList .add(new NumberTermImpl(card));
        }
        logger.log(Level.INFO, "Le carte del dealer sono: " + endDealerCardList .toString());
        logger.log(Level.INFO, "Gonna tell dealer hand: " + this.gamePanel.getDealer().hand.getTotal());
        // final String percept = "tell_hand(" + this.gamePanel.getDealer().hand.getTotal() + ")";
        // System.out.println("Sending percept: " + percept);
        // System.out.println("Percetti del dealer: " + this.getPercepts(DEALER.name));

        // this.addPercept(DEALER.name, Literal
        //                     .parseLiteral("tell_hand(" + this.gamePanel.getDealer().hand.getTotal() + ")"));

        // System.out.println("Percetti del dealer DOPO: " + this.getPercepts(DEALER.name));

        //Fanculo messaggio da dealer si bloccava sempre
        this.addPercept(HILO.name, Literal
                            .parseLiteral("check_score(" + this.gamePanel.getDealer().hand.getTotal() + ")"));
        System.out.println("Percetti del gambler dopo check_score: " + this.getPercepts(HILO.name));
        this.addPercept(DEALER.name, Literal
                .parseLiteral("tell_cards(" + endDealerCardList .toString() + ")"));
        System.out.println("Percetti del dealer dopo tell_cards: " + this.getPercepts(DEALER.name));
        endDealerCardList.remove(1);
        logger.log(Level.INFO, "Le carte del dealer mai state viste sono: " + endDealerCardList .toString());
        //agents register his hadn at end of the game
        final ListTerm endPlayerCardList = new ListTermImpl();
        for (final Integer card : this.gamePanel.getPlayer().hand.stream().map(Card::getValue)
                .collect(Collectors.toList())) {
            endPlayerCardList.add(new NumberTermImpl(card));
        }
        logger.log(Level.INFO, "Le carte del player sono: " + endPlayerCardList.toString());
        this.addPercept(HILO.name, Literal
                .parseLiteral("update_counts(" + endPlayerCardList.toString() + ")"));

        // this.addPercept(HILO.name, Literal.parseLiteral("hand_value(0)"));
    }


    @SuppressWarnings("LoggerStringConcat")
    private Boolean manageDealerAction(final String agName, final Structure action, final String act) {
        switch (act) {
            // case "bet":
            //     if (action.getArity() == 1) {
            //         try {
            //             final NumberTerm betAmountTerm = (NumberTerm) action.getTerm(0);
            //             // currentBet = (int) betAmountTerm.solve();
            //             logger.log(Level.INFO,
            //                     "L'agente " + agName + " ha piazzato una puntata di " + betAmountTerm);
            //             final GameCommand betCommand = GameCommand
            //                     .parseBet(Integer.valueOf(betAmountTerm.toString()));
            //             this.appWindow.actionPerformed(betCommand);
            //             return true;
            //         } catch (final Exception e) {
            //             logger.log(Level.WARNING, "Errore nell'interpretare l'importo della puntata.", e);
            //             return false;
            //         }
            //     } else {
            //         logger.log(Level.WARNING, "L'azione bet richiede un parametro (importo della puntata).");
            //         return false;
            //     }

            // case "deal":
            //     logger.log(Level.WARNING, "L'agente " + agName + " ha richiesto di iniziare una nuova partita.");
            //     this.appWindow.actionPerformed(GameCommand.DEAL);

            //     // if (agName.indexOf("hilo") > 1) {
            //     if (HILO.getName().equals(agName)) {
            //         // aggiungo i valori delle mie carte al conteggio
            //         final ListTerm cardList = new ListTermImpl();
            //         for (final Integer card : this.gamePanel.getPlayer().hand.stream().map(Card::getValue)
            //                 .collect(Collectors.toList())) {
            //             cardList.add(new NumberTermImpl(card));
            //         }
            //         logger.log(Level.INFO, "Le carte della prima mano sono: " + cardList.toString());
            //         this.addPercept(agName, Literal.parseLiteral("update_counts(" + cardList.toString() + ")"));
            //     }
            //     return true;
            case "check_hand_value":
                logger.log(Level.WARNING, "L'agente " + agName + " ha richiesto il valore della mano.");
                logger.log(Level.INFO, "Valore della mano: " + this.gamePanel.getDealer().hand.getTotal());
                logger.log(Level.INFO,
                        "Il giocatore e\' stato sconfitto dopo il check_hand_value?: "
                        + this.gamePanel.getPlayer().hand.isBust());
                logger.log(Level.INFO,
                        "Il Dealer e\' stato sconfitto dopo il check_hand_value?: "
                        + this.gamePanel.getDealer().hand.isBust());
                this.checkIfBusted(agName, "check_hand_value");
                return true;
            case "askCard":
                logger.log(Level.WARNING, "L'agente " + agName + " richiede una carta.");
                logger.log(Level.INFO,
                        "Valore della mano prima: " + this.gamePanel.getPlayer().hand.getTotal());
                if (HILO.getName().equals(agName)) {
                    final int beforeScore = this.gamePanel.getPlayer().hand.getTotal();
                    logger.log(Level.INFO, "Mano agente: " + agName + " before: " + beforeScore);
                    this.appWindow.actionPerformed(GameCommand.HIT);
                    final int afterScore = this.gamePanel.getPlayer().hand.getTotal();
                    logger.log(Level.INFO, "Mano agente: " + agName + " after: " + afterScore);
                    final int diff = afterScore - beforeScore;
                    // //! caso speciale in cui l'asso cambia il valore da 11 ad 1
                    // if (diff < 0) {
                    //     diff += 10;
                    // }
                    logger.log(Level.INFO, "Il sistema inserisce val: " + "card_seen(" + diff + ")");
                    final ListTerm cardList = new ListTermImpl();
                    cardList.add(new NumberTermImpl(diff));
                    this.addPercept(agName, Literal.parseLiteral("card_seen(" + cardList.toString() + ")"));
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
                this.checkIfBusted(agName, act);
                return true;
            // case "stand":
            //     logger.log(Level.WARNING, "L'agente " + agName + " ha deciso di stare.");
            //     this.appWindow.actionPerformed(GameCommand.STAND);
            //     return true;
            default:
                logger.log(Level.WARNING, "Azione non riconosciuta: " + act);
                return false;
        }
    }



    @SuppressWarnings("LoggerStringConcat")
    private Boolean manageGamblerAction(final String agName, final Structure action, final String act) {
        switch (act) {
            case "bet":
                if (action.getArity() == 1) {
                    try {
                        final NumberTerm betAmountTerm = (NumberTerm) action.getTerm(0);
                        // currentBet = (int) betAmountTerm.solve();
                        logger.log(Level.INFO,
                                "L'agente " + agName + " ha piazzato una puntata di " + betAmountTerm);
                        final GameCommand betCommand = GameCommand
                                .parseBet(Integer.valueOf(betAmountTerm.toString()));
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

            // case "deal":
            //     logger.log(Level.WARNING, "L'agente " + agName + " ha richiesto di iniziare una nuova partita.");
            //     this.appWindow.actionPerformed(GameCommand.DEAL);

            //     // if (agName.indexOf("hilo") > 1) {
            //     if (HILO.getName().equals(agName)) {
            //         // aggiungo i valori delle mie carte al conteggio
            //         final ListTerm cardList = new ListTermImpl();
            //         for (final Integer card : this.gamePanel.getPlayer().hand.stream().map(Card::getValue)
            //                 .collect(Collectors.toList())) {
            //             cardList.add(new NumberTermImpl(card));
            //         }
            //         logger.log(Level.INFO, "Le carte della prima mano sono: " + cardList.toString());
            //         this.addPercept(agName, Literal.parseLiteral("update_counts(" + cardList.toString() + ")"));
            //     }
            //     return true;
            // case "check_hand_value":
            //     logger.log(Level.WARNING, "L'agente " + agName + " ha richiesto il valore della mano.");
            //     logger.log(Level.INFO, "Valore della mano: " + this.gamePanel.getPlayer().hand.getTotal());
            //     logger.log(Level.INFO,
            //             "Il giocatore e\' stato sconfitto dopo il check_hand_value?: "
            //             + this.gamePanel.getPlayer().hand.isBust());
            //     logger.log(Level.INFO,
            //             "Il Dealer e\' stato sconfitto dopo il check_hand_value?: "
            //             + this.gamePanel.getDealer().hand.isBust());
            //     this.checkIfBusted(agName, "check_hand_value");
            //     return true;
            case "askCard":
                logger.log(Level.WARNING, "L'agente " + agName + " richiede una carta e la sua mano vale: " + this.gamePanel.getPlayer().hand.getTotal() );
                logger.log(Level.INFO,
                        "Valore della mano prima: " + this.gamePanel.getPlayer().hand.getTotal());
                if (HILO.getName().equals(agName)) {
                    int beforeScore = this.gamePanel.getPlayer().hand.getTotal();
                    logger.log(Level.INFO, "Mano agente: " + agName + " before: " + beforeScore);
                    this.appWindow.actionPerformed(GameCommand.HIT);
                    final int afterScore = this.gamePanel.getPlayer().hand.getTotal();
                    logger.log(Level.INFO, "Mano agente: " + agName + " after: " + afterScore);
                    int diff = afterScore - beforeScore;
                    //! caso speciale in cui l'asso cambia il valore da 11 ad 1
                    if (diff < 0) {
                        logger.log(Level.INFO, "Il valore dell'asso e\' cambiato da 11 a 1 svegliaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        beforeScore -= 10;
                        diff = afterScore - beforeScore;
                    }    logger.log(Level.INFO, "Il sistema inserisce val: " + "card_seen(" + diff + ")");
                    final ListTerm cardList = new ListTermImpl();
                    cardList.add(new NumberTermImpl(diff));
                    this.addPercept(agName, Literal.parseLiteral("card_seen(" + cardList.toString() + ")"));
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
                // this.removePerceptsByUnif(agName, Literal.parseLiteral("hand_value(_)"));
                this.checkIfBusted(agName, act);
                return true;
            // case "stand":
            //     logger.log(Level.WARNING, "L'agente " + agName + " ha deciso di stare.");
            //     this.appWindow.actionPerformed(GameCommand.STAND);
            //     return true;
            default:
                logger.log(Level.WARNING, "Azione non riconosciuta: " + act);
                return false;
        }
    }

    @SuppressWarnings("LoggerStringConcat")
    private void checkIfBusted(final String agName, final String preAction) {
        // this.removePerceptsByUnif(agName, Literal.parseLiteral("hand_value(_)"));
        this.logger.log(Level.INFO, "Il controllo chi sballa dopo: " +  preAction);
        if (!this.gamePanel.getPlayer().hand.isBust()) {
            logger.log(Level.INFO, "Il giocatore " + agName + " non ha sballato, val: " + this.gamePanel.getPlayer().hand.getTotal());
            this.addPercept(agName,
                    Literal.parseLiteral("hand_value(" + this.gamePanel.getPlayer().hand.getTotal() + ")"));

        System.out.println("Percetti del gambler dopo aver mandato la mia mano: " + this.getPercepts(HILO.name));
        } else {
            logger.log(Level.INFO,
                    "Il giocatore  " + agName + " ha sballato, val: " + this.gamePanel.getPlayer().hand.getTotal());
                    //aggiorno le carte viste dal player nel caso in cui il dealer sbanca
                    final ListTerm cardList = new ListTermImpl();
                    for (final Integer card : this.gamePanel.getDealer().hand.stream().map(Card::getValue)
                            .collect(Collectors.toList())) {
                        cardList.add(new NumberTermImpl(card));
                    }
                    //formalmente la prima non la vede il player ma l'environemnt si
                    logger.log(Level.INFO,
                            "Le carte della prima mano sono: " + cardList.subList(1, cardList.size()).toString());

                    this.addPercept(DEALER.name, Literal
                            .parseLiteral("tell_cards(" + cardList.subList(1, cardList.size()).toString() + ")"));
                    // logger.log(Level.INFO, "Deal received by everyone");
            this.addPercept(agName,
                    Literal.parseLiteral("hand_value(" + this.gamePanel.getPlayer().hand.getTotal() + ")")); //Discutibile
        }
    }

    @Override
    public void onEvent(final String message) {
        if ("Deck".equals(message)) {
            this.addPercept(HILO.name,
                    Literal.parseLiteral("reset_card_count"));
        }
        if ("dealer_bust".equals(message)) {
            this.addPercept(HILO.name, Literal.parseLiteral("check_score(22)"));
            // this.addPercept(DEALER.name, Literal.parseLiteral("tell_hand(22)")); //!Sarebbe meglio creare una cosa a parte
        } else {
            logger.log(Level.WARNING, "Evento non riconosciuto: " + message);
        }

    }
}
