// Agente con strategia aggressiva
bet_strategy(aggressiva). 

+suggest_bet <-  
    // ?card_count(C);  
    Bet = 200;  // Puntata alta
    Stop = 20;  // Si ferma tardi
    .send(advicedPlayer, tell, suggested_bet(Bet, Stop)).
