// Agente con strategia conservativa
bet_strategy(conservativa). 

+suggest_bet(C) <-  
    .print("Card  count vale: ", C);
    Bet = 10;  // Puntata sempre bassa
    Stop = 16;  // Stop anticipato
    .send(advicedPlayer, tell, suggested_bet(Bet, Stop)).
