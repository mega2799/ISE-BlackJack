
// !bet(10).

// +!bet(N) : true <-
//     .print("Blackjack Agent: Puntata piazzata. di ", N, "$").
//     spray_air(cold).
//     bet.
//     .send(environment, tell, bet(N)).


!start_play.


+fresca(X) <- .print("Received fresca: ", X).

+!start_play: true <-
    .print("Blackjack Agent: Inizio partita.");
    // .print(fresca.)
    bet(10).

// !bet.
