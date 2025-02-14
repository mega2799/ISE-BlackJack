// Agente con strategia conservativa
bet_strategy(conservativa). 

+suggest_bet <-  
	.print("Consiglio una puntata bassa");
    // ?card_count(C);  // Legge il conteggio delle carte
    Bet = 10;  // Puntata sempre bassa
    Stop = 16;  // Stop anticipato
    .send(advicedPlayer, tell, suggested_bet(Bet, Stop)).
