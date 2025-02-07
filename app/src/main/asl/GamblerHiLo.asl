// Agente Blackjack con strategia di conteggio delle carte (Hi-Lo)

// Beliefs iniziali
card_count(0).  // Iniziamo con un conteggio di 0


// +total_hand_value(V) :- .my_hand(V).

// Regole per il conteggio delle carte
// +card_seen(Val) : (Val >= 2 & Val <= 6) <- +update_count(1).
// +card_seen(Val) : (Val >= 7 & Val <= 9) <- +update_count(0).
// +card_seen(Val) : (Val >= 10 | Val == 1) <- +update_count(-1).

// Aggiornamento del conteggio
// +update_count(Change) <-
//     .print("Blackjack Agent: Aggiorno il conteggio delle carte.");
//     // ( ?card_count(C) | (C = 0) );  // Se il belief non esiste, inizializzalo a 0
//     ?card_count(C);
//     NewCount = C + Change;
//     .print("Blackjack Agent: Vecchio conteggio: ", C);
//     .print("Blackjack Agent: Nuovo conteggio: ", NewCount);
//     -card_count(C);
//     +card_count(NewCount). // Aggiorna il belief concorrentemente


+cards_seen(List) <- update_counts(List).

// Aggiornamento del conteggio per una lista di valori
+update_counts([]) <- .print("Blackjack Agent: Nessuna carta da aggiornare.").

+update_counts([H|T]) : H >= 2 & H <= 6 <-
    .print("Blackjack Agent: Aggiorno il conteggio per la carta: ", H);
    ?card_count(C);
    NewCount = C + 1;
    .print("Blackjack Agent: Vecchio conteggio: ", C);
    .print("Blackjack Agent: Nuovo conteggio: ", NewCount);
    -card_count(C);
    +card_count(NewCount);
    +update_counts(T).

+update_counts([H|T]) : H >= 10 | H == 1 <-
    .print("Blackjack Agent: Aggiorno il conteggio per la carta: ", H);
    ?card_count(C);
    NewCount = C - 1;
    .print("Blackjack Agent: Vecchio conteggio: ", C);
    .print("Blackjack Agent: Nuovo conteggio: ", NewCount);
    -card_count(C);
    +card_count(NewCount);
    +update_counts(T).

+update_counts([H|T]) : (H >= 7 & H <= 9) <-
    .print("Blackjack Agent: Aggiorno il conteggio per la carta: ", H);
    ?card_count(C);
    NewCount = C + 0;
    .print("Blackjack Agent: Vecchio conteggio: ", C);
    .print("Blackjack Agent: Nuovo conteggio: ", NewCount);
    -card_count(C);
    +card_count(NewCount);
    +update_counts(T).




// Strategia di puntata basata sul conteggio
+!decide_bet <-
    .print("Blackjack Agent: Decido la puntata....... boh");
    ?card_count(C);
    if ( C > 2 ) { Bet = 100 } 
    if ( C < -1 ) { Bet = 10 } 
    else {Bet = 50 };    
    .print("Blackjack Agent: Punto: ", Bet).
    // !bet(Bet).


//stupid gambler here....
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
    decide_bet;
    .wait(1000);
    deal.
    // .wait(1000);
    // check_hand_value.

+!ask_card: true <-
    .wait(1000);
    .print("Blackjack Agent: Chiedo una carta.");
    askCard.
