// Agente con strategia aggressiva
bet_strategy(aggressiva). 

+suggest_bet(C) <-  
    .print("Card  count vale: ", C);
    Bet = 100;  // Puntata alta
    Stop = 20;  // Si ferma tardi
    .send(advicedPlayer, tell, suggested_bet(Bet, Stop)).
