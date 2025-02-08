// Agente Blackjack con strategia di conteggio delle carte (Hi-Lo)

// Beliefs iniziali
card_count(0).  // Iniziamo con un conteggio di 0

// debug_mode(on).

//********************************************************************* UTILITIES ********************************************************************* */
+!debug_print([])  : debug_mode(on) <- .print("DEBUG: ").

+!debug_print([H])  : debug_mode(on) <- .print("DEBUG: ", H).

// **Piano di fallback** per quando debug_mode è off: NON STAMPA NULLA!
+!debug_print(_) : debug_mode(off) <- true.

// +?debug_print([M]) <- true.
// +?debug_print([]) <- true.

+!debug_print([H|T])  : debug_mode(on) <- 
    .concat("DEBUG: ", H, Msg);
    !debug_print_helper(Msg, T).

+!debug_print_helper(Msg, []) <- 
    .print(Msg).

+!debug_print_helper(Msg, [H|T]) <- 
    .concat(Msg, " ", Msg1);
    .concat(Msg1, H, Msg2);
    !debug_print_helper(Msg2, T).

+!tick <- 
    .wait(2000).

+!set_debug(on) <- 
    -debug_mode(_);
    +debug_mode(on);
    .print("DEBUG ATTIVATO").

+!set_debug(off) <- 
    -debug_mode(_);
    +debug_mode(off);
    .print("DEBUG DISATTIVATO").


//********************************************************************* Conteggio carte ********************************************************************* */

+cards_seen(List) <- update_counts(List).

+update_counts([]) <- !debug_print(["Nessuna carta da aggiornare."]).

+update_counts([H|T]) <-
    !debug_print(["Aggiorno il conteggio per la carta: ", H]);
    ?card_count(C);
    if (H >= 2 & H <= 6) { NewCount = C + 1 };
    if (H >= 7 & H <= 9) { NewCount = C + 0 };
    if (H >= 10 | H == 1) { NewCount = C - 1 };
    !debug_print(["Vecchio conteggio: ", C]);
    !debug_print(["Nuovo conteggio: ", NewCount]);
    -card_count(C);
    +card_count(NewCount);
    +update_counts(T).


// Strategia di puntata basata sul conteggio
+!decide_bet <-
    !debug_print(["Decido la puntata....... boh"]);
    ?card_count(C);
    if ( C > 2 ) { Bet = 100 } 
    if ( C < -1 ) { Bet = 10 } 
    else {Bet = 25 };    
    !debug_print(["Punto: ", Bet]);
    bet(Bet).

//********************************************************************* Gambler ********************************************************************* */

+hand_value(0).

!start_play.

+hand_value(V) : V < 17 <- 
    !debug_print(["La mia mano ha valore ", V]);
    !ask_card. 

+hand_value(V) : V >= 17 & V <21 <- 
    !debug_print(["Ho vinto io skyler ", V]);
    stand;
    !debug_print(["Resetto la partita..."]);
    +hand_value(0);
    !tick;
    !start_play.

+hand_value(V) : V == 21 <- 
    !debug_print(["BlackJack!!! ", V]);
    stand;
    !!debug_print(["Resetto la partita..."]);
    +hand_value(0);
    !tick;
    !start_play.

+hand_value(V) : V > 21 <- 
    !debug_print(["Ho perso la fresca"]);
    !debug_print(["Resetto la partita..."]);
    // -hand_value(V);
    +hand_value(0);
    !tick;
    !debug_print(["###################### Done ######################"]);
    !start_play.

+!start_play: true <-
    !debug_print(["Inizio partita."]);
    // bet(10);
    !decide_bet;
    !tick;
    deal;
    !tick;
    check_hand_value.

+!ask_card: true <-
    !tick;
    !debug_print(["Chiedo una carta."]);
    askCard.
