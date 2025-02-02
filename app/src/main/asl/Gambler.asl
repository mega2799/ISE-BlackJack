
// !bet(10).

// +!bet(N) : true <-
//     .print("Blackjack Agent: Puntata piazzata. di ", N, "$").
//     spray_air(cold).
//     bet.
//     .send(environment, tell, bet(N)).


!start_play.

+!start_play: true <-
    .print("Blackjack Agent: Inizio partita.");
    bet(10).

// !bet.
