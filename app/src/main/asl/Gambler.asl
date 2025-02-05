
+hand_value(0).

!start_play.

+hand_value(V) : V < 17 <- 
    .print("Blackjack Agent: La mia mano ha valore ", V);
    !ask_card. 

+hand_value(V) : V >= 17 & V <21 <- 
    .print("Blackjack Agent: Ho vinto io skyler ", V);
    stand;
    .print("Blackjack Agent: Resetto la partita...");
    // -hand_value(V);
    +hand_value(0);
    .wait(1000);
    !start_play.

+hand_value(V) : V == 21 <- 
    .print("Blackjack Agent: BlackJack!!! ", V);
    stand;
    .print("Blackjack Agent: Resetto la partita...");
    // -hand_value(V);
    +hand_value(0);
    .wait(1000);
    !start_play.

+hand_value(V) : V > 21 <- 
    .print("Blackjack Agent: Ho perso la fresca");
    .print("Blackjack Agent: Resetto la partita...");
    // -hand_value(V);
    +hand_value(0);
    .wait(1000);
    .print("Blackjack Agent: ###################### Done ######################");
    !start_play.

+!start_play: true <-
    .print("Blackjack Agent: Inizio partita.");
    bet(10);
    .wait(1000);
    deal;
    .wait(1000);
    check_hand_value.

+!ask_card: true <-
    .wait(1000);
    .print("Blackjack Agent: Chiedo una carta.");
    askCard.
