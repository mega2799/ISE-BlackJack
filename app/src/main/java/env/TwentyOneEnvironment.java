package env;

import java.util.List;
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
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;

public class TwentyOneEnvironment extends Environment implements EventListener {
   static final Logger logger = Logger.getLogger(TwentyOneEnvironment.class.getName());
    private AppWindow appWindow;
    private GamePanel gamePanel;

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
    public void init(final String[] args) {
        super.init(args);
        this.beatufiySwing();
        this.appWindow = new AppWindow(args.length > 0 ? Double.parseDouble(args[0]) : 1000);
        this.gamePanel = this.appWindow.getGamePanel();
        this.gamePanel.addListener(this);
        
        logger.info("Inizializzazione completata.");
        // this.addPercept(Literal.parseLiteral("start"));
        // logger.setLevel(Level.WARNING);
        logger.setLevel(Level.INFO);
    }

    @Override
    public boolean executeAction(final String agName, final Structure action) {

        if (action == null) {
            logger.log(Level.WARNING, "Azione NULL ricevuta");
            return false;
        }
        this.logger.log(Level.INFO, "Percepts: " + this.getPercepts(agName));


        final String act = action.getFunctor();
        logger.log(Level.INFO, "Agente " + agName + " esegue l''azione " + act);
        if (null == act) {
            logger.log(Level.WARNING, "Azione non riconosciuta: {0}", act);
            return false;
        }
        if ("bet".equals(act)) {
            final var res = GameEnvUtils.betManage(agName, action, this.appWindow);
            return res;
        }
        if ("clear".equals(act)) {
            this.removePerceptsByUnif(agName, Literal.parseLiteral("dealer_busted(_)"));
            this.removePerceptsByUnif(agName, Literal.parseLiteral("dealer_score(_)"));
            this.removePerceptsByUnif(agName, Literal.parseLiteral("hand_value(_)"));
            this.removePerceptsByUnif(agName, Literal.parseLiteral("update_counts(_)"));
            return true;
        }
        if ("deal".equals(act)) {
            this.appWindow.actionPerformed(GameCommand.DEAL);
            //non dovbrebbe essere possibile bustare al deal...... quindi aggiorno le carte
            GameEnvUtils.sendToAgentCards(this, agName, this.gamePanel);
            GameEnvUtils.sendToAgentHandToCount(this, agName, this.gamePanel.getPlayer().hand.stream().map(Card::getValue)
                            .collect(Collectors.toList()));
            return true;
        }
        if ("hit".equals(act)) {
            this.appWindow.actionPerformed(GameCommand.HIT);
            GameEnvUtils.sendToAgentCards(this, agName, this.gamePanel);
            // Conto la carta che ho appena pescato
            final List<Integer> values = this.gamePanel.getPlayer().hand.stream()
                    .map(Card::getValue)
                    .collect(Collectors.toList());
            final Integer lastValue = values.isEmpty() ? null : values.get(values.size() - 1);
            final List<Integer> lastValueList = lastValue != null ? List.of(lastValue) : List.of();
            GameEnvUtils.sendToAgentHandToCount(this, agName, lastValueList);
            //         try {
            // // Attendi per 2 secondi (2000 millisecondi)
            // Thread.sleep(2000);
            // this.logger.log(Level.INFO, "Woke up after 2 seconds!");
            //         } catch (final InterruptedException e) {
            //             e.printStackTrace();
            //         }

            // GameEnvUtils.checkBusted(this, agName, this.gamePanel.getDealer());
            // GameEnvUtils.sendToAgentCards(this, agName, this.gamePanel);
            return true;
        }
        if ("bust".equals(act)) {
            // Conto le carte del dealer 
            GameEnvUtils.sendToAgentHandToCount(this, agName, this.gamePanel.getDealer().hand.stream()
                    .map(Card::getValue)
                    .collect(Collectors.toList()));
            return true;
        }
        if("stand".equals(act)) {
            this.appWindow.actionPerformed(GameCommand.STAND);
            // GameEnvUtils.sendToAgentCards(this, agName, this.gamePanel);
            GameEnvUtils.checkBusted(this, agName, this.gamePanel.getDealer());
            GameEnvUtils.sendToAgentHandToCount(this, agName, this.gamePanel.getDealer().hand.stream().map(Card::getValue)
                                .collect(Collectors.toList()));
            // this.logger.log(Level.INFO, "Percepts: " + this.getPercepts(agName));
            // GameEnvUtils.sendToAgentCards(this, agName, this.gamePanel);
            return true;
        }

        return false;
    }

	@Override
	public void onEvent(final String message) {
        // if("player_card".equals(message)){
        //     this.logger.log(Level.INFO, "Aggiorno le carte del giocatore");
        // }
	}

}
