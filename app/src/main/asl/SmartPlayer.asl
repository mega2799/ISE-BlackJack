

//Belief iniziali
state(start).
game(50).

!start.


//********************************************************************* UTILITIES ********************************************************************* */


+!tick <- 
    .wait(2000).


//********************************************************************* CORE ********************************************************************* */

// +!hand_value(V)<- 
// 	.print("Deal done, current state: ", state(_));
// 	-state(_);
// 	+state(hand_manage);
// 	// ?hand_value(V);
// 	.print("My hand value: ", V).


+!bet_money : state(bet_check) <-
    .print("Betting money");
    bet(10);
	.print("Betted so i deal");
	deal.


+!start <-
	clear;
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
		.print("My hand value: ", V);
		!decide_action(V, 0);
	} else {
		.print("Game Over");
	}.

//********************************************************************* Gambler hand manage ********************************************************************* */

    
// chiedo carta soltanto se sicuro prenderla 
+!decide_action(V, C) : V < (18 - C) & V < 21 <- 
    .print("Valore mano: ", V, " | Card Count: ", C, " => Chiedo carta!");
	hit;
	?hand_value(NewV);
	.print("My hand value: ", NewV);
	!decide_action(NewV, C).

// stare al gioco e rischioso quindi mi fermo
+!decide_action(V, C) : V >= (18 - C) & V < 21 <- 
	.print("Valore mano: ", V, " | Card Count: ", C, " => Mi fermo!");
	stand;
	// !tick;
	while(not dealer_busted(B)) {
		.print("Waiting for busting");
        .wait(200);
	};
	?dealer_busted(B);
	.print("Dealer busted: ", B);
	if(B){
		.print("Dealer busted");
		// !win;
	} else {
		while(not dealer_score(S)) {
			.print("Waiting for dealer score");
			.wait(200);
		};
		?dealer_score(S);
		.print("Dealer hand value: ", S);
		// if(S > V){
		// 	.print("Dealer win");
		// 	// !lose;
		// }
		// if(S == V){
		// 	.print("Draw");
		// 	// !draw;
		// } else {
		// 	.print("I win");
		// 	// !win;
		// };
	};
	-state(_);
	+state(start);
	!start.

//Jackpot si incassa
+!decide_action(V, C) : V == 21 <- 
	.print("BlackJack!!! ", V);
	stand;
	-state(_);
	+state(start);
	!start.

// ho perso
+!decide_action(V, C) : V > 21 <- 
	.print("Ho sballato");
	-state(_);
	+state(start);
	!start.

+!decide_action(V, C) <- 
	.print("Valore mano: ", V, " | Card Count: ", C, " => Perche son ").