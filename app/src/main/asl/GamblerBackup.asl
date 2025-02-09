// File: blackjack.asl

// Inizialmente l'agente parte con il goal !playRound
// initially {
//     !playRound.
// }

/*
  Il piano principale:
  - Effettua la puntata (azione !bet)
  - Richiede carte finché il valore della mano è minore di 21
*/
!playRound : true <-
    .print("Blackjack Agent: Inizio partita. Puntata effettuata!"),
    !bet,
    // !drawCards.

/*
  Piano per effettuare la puntata.
  In un caso reale qui potresti invocare un'azione Java per registrare la puntata.
*/
!bet : true <-
    .print("Blackjack Agent: Puntata piazzata.").

/*
  Piano per richiedere carte.
  Si assume che la credenza 'handValue(V)' rappresenti il valore corrente della mano.
  Inizialmente impostiamo handValue a 0.
*/
+handValue(0).

// Se il valore della mano è inferiore a 21, richiede una carta e si aggiorna.
!drawCards : handValue(V) & V < 21 <-
    .print("Blackjack Agent: handValue = " + V + ". Richiedo una carta..."),
    !askCard,
    // Dopo aver chiesto la carta, si aggiorna il valore della mano
    !updateHandValue,
    !drawCards.

// Se il valore della mano è 21 o superiore, termina la richiesta di carte.
!drawCards : handValue(V) & V >= 21 <-
    .print("Blackjack Agent: handValue = " + V + ". Stop, ho raggiunto 21 o oltre!").

/*
  Piano per chiedere una carta.
  Qui invieremmo un messaggio o chiameremmo un'azione Java che simula il pescare una carta.
*/
!askCard : true <-
    .print("Blackjack Agent: Chiedo una carta..."),
    // Azione fittizia: in una reale integrazione potresti invocare .send(...) oppure un'azione esterna.
    true.

/*
  Piano per aggiornare il valore della mano.
  Per semplicità, questo piano fissa il valore della mano a 21 (simulando ad esempio il caso in cui la carta pescata porta a 21).
  In un'applicazione reale, qui dovresti interrogare il sistema Java per ottenere il nuovo valore della mano.
*/
!updateHandValue : true <-
    .print("Blackjack Agent: Aggiorno il valore della mano."),
    // Rimuovo ogni valore precedente e imposto 21.
    -handValue(_),
    +handValue(21).
