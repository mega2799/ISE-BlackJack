card_count(0).  // Iniziamo con un conteggio di 0

+!lemme_count(H) <-
    ?card_count(C);
    if (H >= 2 & H <= 6) { NewCount = C + 1 };
    if (H >= 7 & H <= 9) { NewCount = C + 0 };
    if (H >= 10 | H == 1) { NewCount = C - 1 };
    -card_count(C);
    +card_count(NewCount).

+update_counts(List) <-
	.print("counting cards: ", List);
	// +updating_card_count(true);
	for ( .member(I,List) ) {
		.print("Count card: ", I);
		!lemme_count(I);
    };
	.print("Fine conteggio").
	// +updating_card_count(false).
    // ?card_count(C);

+suggest_bet <-  
    ?card_count(C);
    .print("Card  count vale: ", C);
    if (C > 2) { Bet = 100; Stop = 19 }  // Conteggio alto → più aggressivo
    if (C < -1) { Bet = 10; Stop = 16 }  // Conteggio basso → più prudente
    if (C >= -1 & C <= 2) { Bet = 25; Stop = 18 }  // Conteggio medio → moderato
    .send(advicedPlayer, tell, suggested_bet(Bet, Stop)).

