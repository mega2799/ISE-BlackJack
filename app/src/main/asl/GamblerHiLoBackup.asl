
// Agente Blackjack con strategia di conteggio delle carte (Hi-Lo)

// Beliefs iniziali
card_count(0).  // Iniziamo con un conteggio di 0
winned_games(0).
lost_games(0).
tied_games(0).

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

+reset_card_count <- 
    !debug_print(["Resetto il conteggio delle carte."]);
    -card_count(_);
    +card_count(0).

+cards_seen(List)[source(Sender)] <- 
    .print("Messaggio ricevuto da ", Sender, ": aggiungo alle carte lette:", List);
    +update_counts(List).

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
    !debug_print(["Decido la puntata......."]);
    ?card_count(C);
    !debug_print(["C vale: ", C]);
    if ( C > 2 ) { !debug_print(["Punto: ", 100]); bet(100) } 
    if ( C < -1 ) { !debug_print(["Punto: ", 10]); bet(10) } 
    if ( C <= 2 & C >= -1 ) { !debug_print(["Punto: ", 25]); bet(25)}.
    // !debug_print(["Punto: ", Bet]);
    // bet(Bet).

//********************************************************************* Gambler hand manage ********************************************************************* */

+hand_value(0).

!start_play.

//TODO inserire qui il punto di controllo nel caso in cui il dealer sia busted e io vinca automaticamente
+hand_value(V) <- 
    !debug_print(["La mia mano ha valore ", V]);
    ?card_count(C);
    !debug_print(["Il mio conteggio: ", C]);
    !tick;
    !decide_action(V, C).

// chiedo carta soltanto se sicuro prenderla 
+!decide_action(V, C) : V < (18 - C) & V < 21 <- 
    !debug_print(["Valore mano: ", V, " | Card Count: ", C, " => Chiedo carta!"]);
    !tick;
    !ask_card.

// stare al gioco e rischioso quindi mi fermo
+!decide_action(V, C) : V >= (18 - C) & V < 21 <- 
    !debug_print(["Valore mano: ", V, " | Card Count: ", C, " => Mi fermo!"]);
    stand.
    // !end_game_routine.
    // !tick;
    // end_game;
    // !tick;
    // !start_play.

//Jackpot si incassa
+!decide_action(V, C) : V == 21 <- 
    !debug_print(["BlackJack!!! ", V]);
    // !debug_print(["###################### Done ######################"]);
    !win;
    !end_game_routine.
    // stand;
    // !tick;
    // // end_game;
    // !debug_print(["###################### Done ######################"]);
    // !tick;
    // !start_play.

// ho perso
+!decide_action(V, C) : V > 21 <- 
    !debug_print(["Ho perso la fresca"]);
    !lose;
    !end_game_routine.
    // !tick;
    // end_game;
    // !debug_print(["###################### Done ######################"]);
    // !tick;
    // !start_play.

+!ask_card: true <-
    !tick;
    !debug_print(["Chiedo una carta."]);
    askCard.

//********************************************************************* Game manage ********************************************************************* */

+check_score(D) <-
    !debug_print(["Il dealer ha fatto ", D, " punti."]);
    ?hand_value(V);
    !debug_print(["Il mio score ", V, " punti."]);
    //TODO manca il caso blackJack
    if (D > 21 | V > D) { !debug_print(["Ho vinto!"]); +win }
    if (V == D) { !debug_print(["Pareggio!"]); +tie}
    if (D == 21 | V < D) { !debug_print(["Ho perso!"]); +lose};
    !end_game_routine.

+!end_game_routine <-
    !tick;
    -hand_value(_);
    +hand_value(0);
    end_game;
    !debug_print(["###################### Done ######################"]).
    // !tick;
    // !start_play.

+!win <-
    ?winned_games(W);
    NewW = W + 1;
    -winned_games(W);
    +winned_games(NewW);
    !debug_print(["Partite vinte: ", NewW]).

+!lose <-
    ?lost_games(L);
    NewL = L + 1;
    -lost_games(L);
    +lost_games(NewL);
    !debug_print(["Partite perse: ", NewL]).

+!tie <-
    ?tied_games(T);
    NewT = T + 1;
    -tied_games(T);
    +tied_games(NewT);
    !debug_print(["Partite pareggiate: ", NewT]).

+!start_play : true <-
    !debug_print(["Inizio partita."]);
    !decide_bet;
    !tick;
    deal.
    // check_hand_value.

