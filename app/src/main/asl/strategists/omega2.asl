card_count(0).

+!lemme_count(H) <-
    ?card_count(C);
    if (H >= 4 & H <= 6) { NewCount = C + 2 };
    if (H >= 2 & H <= 3) { NewCount = C + 1 };
    if (H == 7) { NewCount = C + 1 };
    if (H == 8) { NewCount = C + 0 };
    if (H == 9) { NewCount = C - 1 };
    if (H >= 10 | H == 1) { NewCount = C - 2 };
    -card_count(C);
    +card_count(NewCount).

+update_counts(List) <-
	.print("counting cards: ", List);
	for ( .member(I,List) ) {
		.print("Count card: ", I);
		!lemme_count(I);
    };
	.print("Fine conteggio").

+suggest_bet(G) <-  
    ?card_count(C);
    .wait(400);
    .print("Card  count vale: ", C);
    if (C > 2) { Bet = 100; Stop = 19 }  // Conteggio alto → più aggressivo
    if (C < -1) { Bet = 10; Stop = 16 }  // Conteggio basso → più prudente
    if (C >= -1 & C <= 2) { Bet = 25; Stop = 18 }  // Conteggio medio → moderato
    .print("Suggerisco di puntare: ", Bet, " e di fermarsi a: ", Stop);
    .send(advicedPlayer, tell, suggested_bet(Bet, Stop)).


