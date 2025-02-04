
// !bet(10).

// +!bet(N) : true <-
//     .print("Blackjack Agent: Puntata piazzata. di ", N, "$").
//     spray_air(cold).
//     bet.
//     .send(environment, tell, bet(N)).

+hand_value(0).

!start_play.

+hand_value(V) : V < 17 <- 
    .print("Blackjack Agent: La mia mano ha valore ", V);
    !ask_card. 

+hand_value(V) : V >= 17 <- 
    .print("Blackjack Agent: Ho vinto io skyler", V);
    stand.
    // -hand_value(V);
    // !start_play.

+!start_play: true <-
    .print("Blackjack Agent: Inizio partita.");
    bet(10);
    deal;
    check_hand_value.

+!ask_card: true <-
    .print("Blackjack Agent: Chiedo una carta.");
    askCard.