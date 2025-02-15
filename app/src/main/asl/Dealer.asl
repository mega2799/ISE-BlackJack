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
    .wait(200).

+!set_debug(on) <- 
    -debug_mode(_);
    +debug_mode(on);
    .print("DEBUG ATTIVATO").

+!set_debug(off) <- 
    -debug_mode(_);
    +debug_mode(off);
    .print("DEBUG DISATTIVATO").


//********************************************************************* Conteggio carte ********************************************************************* */

+tell_cards([]) <- !debug_print(["Nessuna carta da aggiornare."]).

+tell_cards([H|T]) <-
	!debug_print(["Informo il player della la carta: ", H]);
	// ?card_count(C);
    !send_message(gamblerhilo, cards_seen([H]));
	+tell_cards(T).


+!send_message(Receiver, Content) <- 
    .send(Receiver, tell, Content).


//********************************************************************* Gambler hand manage ********************************************************************* */

+hand_value(0).

!start_play.

//TODO inserire qui il punto di controllo nel caso in cui il dealer sia busted e io vinca automaticamente
+hand_value(V) <- 
    !debug_print(["La mia mano ha valore ", V]);
    ?card_count(C);
    !debug_print(["Il mio conteggio: ", C]);
    !decide_action(V, C).

// chiedo carta soltanto se sicuro prenderla 
+!decide_action(V, C) :  V < (18 - C) <- 
    !debug_print(["Valore mano: ", V, " | Card Count: ", C, " => Chiedo carta!"]);
    !ask_card.

// stare al gioco e rischioso quindi mi fermo
+!decide_action(V, C) : V >= (18 - C) & V < 21 <- 
    !debug_print(["Valore mano: ", V, " | Card Count: ", C, " => Mi fermo!"]);
    stand; 
    !debug_print(["###################### Done ######################"]);
    -hand_value(V);
    +hand_value(0);
    !tick;
    !start_play.

//Jackpot si incassa
+!decide_action(V, C) : V == 21 <- 
    !debug_print(["BlackJack!!! ", V]);
    stand;
    !debug_print(["###################### Done ######################"]);
    -hand_value(V);
    +hand_value(0);
    !tick;
    !start_play.

// ho perso
+!decide_action(V, C) : V > 21 <- 
    !debug_print(["Ho perso la fresca"]);
    !debug_print(["###################### Done ######################"]);
    -hand_value(V);
    +hand_value(0);
    !tick;
    !start_play.

+!ask_card: true <-
    !tick;
    !debug_print(["Chiedo una carta."]);
    askCard.

//********************************************************************* Game manage ********************************************************************* */

+!start_play: true <-
    !debug_print(["Inizio partita."]);
    // !decide_bet;
    !tick.
    // deal;
    // !tick;
    // check_hand_value.

