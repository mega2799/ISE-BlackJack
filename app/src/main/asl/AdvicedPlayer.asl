

//Belief iniziali
state(start).
game(1).
card_count(0).  // Iniziamo con un conteggio di 0
winned_games(0).
lost_games(0).
tied_games(0).


stopping_score(21).


!start.


//********************************************************************* UTILITIES ********************************************************************* */

+!tick <- 
    .wait(2000).

+!resume_stat <-
	?winned_games(W);
	?lost_games(L);
	?tied_games(T);
	.print("Statistiche: ");
	.print("Partite vinte: ", W);
	.print("Partite perse: ", L);
	.print("Partite pareggiate: ", T).

+!win <-
    ?winned_games(W);
    NewW = W + 1;
    -winned_games(W);
    +winned_games(NewW).

+!lose <-
    ?lost_games(L);
    NewL = L + 1;
    -lost_games(L);
    +lost_games(NewL).

+!tie <-
    ?tied_games(T);
    NewT = T + 1;
    -tied_games(T);
    +tied_games(NewT).

//********************************************************************* CONSENSUS ********************************************************************* */

+!decide_bet <-
	.print("Chiedo consiglio per la puntata");
	// ?card_count(C);
    .broadcast(tell, suggest_bet);  // Chiede suggerimenti
    // .wait(broadcast(tell, suggest_bet));  // Aspetta risposte
	// .send(strategist, tell, suggest_bet);
	// .send(aggressive, tell, suggest_bet);
	// .send(adaptivestrategist, tell, suggest_bet);
	.wait(3000);

    !aggregate_bets.
    // ?card_count(C);
    // .print("Decido la puntata.......", "C vale: ", C);
    // if ( C > 2 ) { .print("Punto: ", 100); bet(100) } 
    // if ( C < -1 ) { .print("Punto: ", 10); bet(10) } 
    // if ( C <= 2 & C >= -1 ) { .print("Punto: ", 25); bet(25)}.

+suggested_bet(Bet, Stop) <- 
    .print("Ricevuto suggerimento: Punto ", Bet, " - Stop a ", Stop);
    +bet_suggestion(Bet, Stop). 



+!aggregate_bets <- 
    .findall(B, bet_suggestion(B, _), Bets);
    .findall(S, bet_suggestion(_, S), Stops);
    
    // MostCommonBetList = mode(Bets);
    // MostCommonStopList = mode(Stops);

	// .java("env.GameEnvUtils", "findMode", Bets, MostCommonBet );
	// .java("env.GameEnvUtils", "findMode", Stops, MostCommonStop  );
    // MostCommonBet = head(MostCommonBetList);
    // MostCommonStop = head(MostCommonStopList);
	.print("Bets: ", Bets);
	.print("Stops: ", Stops);
	//TODO ora prende il primo elemento della lista, ma bisogna fare la moda
	.nth(0, Bets, MostCommonBet);
	.nth(0, Stops, MostCommonStop);
    .print("Decisione finale: Punto ", MostCommonBet, " e mi fermo a ", MostCommonStop);
    bet(MostCommonBet).


//********************************************************************* CORE ********************************************************************* */

+!bet_money : state(bet_check) <-
    .print("Betting money");
	!decide_bet;
	deal.

+!start <-
	!resume_stat;
	.wait(3000); // Aspetta che tutti abbiano contato le loro carte
	clear;
	// -bet_suggestion(_, _);
	-suggested_bet(_, _);
	?game(C);
	if(C > 0){
		!tick;
		-game(C);
		+game(C - 1);
		.print("Game Game Game Game Game Game Game Game Game Game Game ", C - 1);
		.print("start");
		-state(_);
		+state(bet_check);
		!bet_money;
		while(not hand_value(V)) {
			.print("Waiting for hand value");
			.wait(200);
		};
		?hand_value(V);
		// .wait(updating_card_count(false));
		// -updating_card_count(_);
		// while(not updating_card_count(_)) {
		// 	.print("updating: ", Val);
		// 	.print("Waiting for card count");
		// 	.wait(200);
		// };

		-stopping_score(Stop);
		// ?stopping_score(Stop);
		.print("My hand value: ", V, " my stopping score: ", Stop);
		!decide_action(V, Stop);
	} else {
		.print("Game Over");
	}.

//********************************************************************* Card count ********************************************************************* */




//********************************************************************* Gambler hand manage ********************************************************************* */

    
// chiedo carta soltanto se sicuro prenderla 
+!decide_action(V, Stop) : V < Stop & V < 21 <- 
    .print("Valore mano: ", V, " | Stopping point: ", Stop, " => Chiedo carta!");
	hit;
	// .wait(updating_card_count(false));
	// -updating_card_count(_);
	?hand_value(NewV);
	.print("My hand value: ", NewV);
	!decide_action(NewV, Stop).

// stare al gioco e rischioso quindi mi fermo
+!decide_action(V, Stop) : V >= Stop & V < 21 <- 
	.print("Valore mano: ", V, " | Stopping point: ", Stop, " => Mi fermo!");
	stand;
	// .wait(updating_card_count(false));
	// -updating_card_count(_);
	while(not dealer_busted(B)) {
		.print("Waiting for busting");
        .wait(200);
	};
	?dealer_busted(B);
	.print("Dealer busted: ", B);
	if(B){
		.print("Dealer busted");
		!win;
	} else {
		while(not dealer_score(S)) {
			.print("Waiting for dealer score");
			.wait(200);
		};
		?dealer_score(S);
		.print("Dealer hand value: ", S);
		if(S > V){
			.print("Dealer win");
			!lose;
		}
		if(S == V){
			.print("Draw");
			!tie;
		}
		if (S < V){ 
			.print("I win");
			!win;
		};
	};
	-state(_);
	+state(start);
	!start.

//Jackpot si incassa
+!decide_action(V, Stop) : V == 21 <- 
	.print("BlackJack!!! ", V);
	stand;
	-state(_);
	+state(start);
	!win;
	!start.

// ho perso
+!decide_action(V, Stop) : V > 21 <- 
	.print("Ho sballato");
	bust;
	// .wait(updating_card_count(false));
	// -updating_card_count(_);
	-state(_);
	+state(start);
	!lose;
	!start.

+!decide_action(V, Stop) <- 
	.print("Valore mano: ", V, " | Stopping point: ", Stop, " => Perche sono qui?").