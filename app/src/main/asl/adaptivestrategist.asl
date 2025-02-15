// Agente con strategia adattiva
bet_strategy(adattiva). 

+suggest_bet(C) <-  
    .print("Card  count vale: ", C);
    if (C > 2) { Bet = 100; Stop = 19 }  // Conteggio alto → più aggressivo
    if (C < -1) { Bet = 10; Stop = 16 }  // Conteggio basso → più prudente
    if (C >= -1 & C <= 2) { Bet = 25; Stop = 18 }  // Conteggio medio → moderato
    .send(advicedPlayer, tell, suggested_bet(Bet, Stop)).
