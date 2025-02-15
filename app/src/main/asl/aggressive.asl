// Agente con strategia aggressiva
bet_strategy(aggressiva). 
card_count(0).  // Iniziamo con un conteggio di 0

// @waitfor
// +!lemme_count(H) <-
//     ?card_count(C);
//     if (H >= 2 & H <= 6) { NewCount = C + 1 };
//     if (H >= 7 & H <= 9) { NewCount = C + 0 };
//     if (H >= 10 | H == 1) { NewCount = C - 1 };
//     -card_count(C);
//     +card_count(NewCount).

@waitfor
+update_counts(List) <-
	.print("counting cards: ", List);
	// +updating_card_count(true);
	for ( .member(I,List) ) {
		.print("Count card: ", I);
		// !lemme_count(I);
        ?card_count(C);
        if (I >= 2 & I <= 6) { NewCount = C + 1 };
        if (I >= 7 & I <= 9) { NewCount = C + 0 };
        if (I >= 10 | I == 1) { NewCount = C - 1 };
        -card_count(C);
        +card_count(NewCount);
    };
	.print("Fine conteggio").
	// +updating_card_count(false).
    // ?card_count(C);

+suggest_bet <-  
    ?card_count(C);
    .print("Card  count vale: ", C);
    Bet = 100;  // Puntata alta
    Stop = 20;  // Si ferma tardi
    .send(advicedPlayer, tell, suggested_bet(Bet, Stop)).
