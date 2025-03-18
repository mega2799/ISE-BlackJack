# ISE-BlackJack

![Stato Build](https://img.shields.io/github/actions/workflow/status/mega2799/ISE-BlackJack/build.yml)
![Licenza](https://img.shields.io/github/license/mega2799/ISE-BlackJack)
![Copertina](https://github.com/mega2799/ISE-BlackJack/blob/main/asset/album.jpg)
## Descrizione

**ISE-BlackJack** è un progetto che unisce il classico gioco del BlackJack con un'implementazione avanzata basata su **sistemi multi-agente (MAS)** e **agenti BDI (Belief-Desire-Intention)**. Il progetto utilizza **Jason AgentSpeak**, un linguaggio specifico per la programmazione di agenti intelligenti, per modellare il comportamento del banco e del giocatore in un ambiente simulato.

L'obiettivo principale è esplorare l'interazione tra agenti autonomi che prendono decisioni basate su credenze, desideri e intenzioni, migliorando così la strategia di gioco attraverso un processo decisionale dinamico.

## Caratteristiche Principali

- **Agenti BDI**: Il giocatore è implementato come un agente intelligente con credenze, desideri e intenzioni.
- **Sistema Multi-Agente (MAS)**: Utilizzo di Jason per gestire la comunicazione e la coordinazione tra gli agenti.
- **Strategia di gioco adattiva**: Gli agenti aggiornano le loro credenze e strategie in base all'evoluzione della partita.
- **Interfaccia testuale interattiva**: Il gioco si svolge in un ambiente a riga di comando con aggiornamenti in tempo reale.
- **Environment**: Il tavolo di gioco rappresenta a tutti gli effetti l'environment in cui gli attori sono immersi.

## Installazione e Avvio

1. **Clona la repository**:
   ```bash
   git clone https://github.com/mega2799/ISE-BlackJack.git
   ```
2. **Accedi alla directory del progetto**:
   ```bash
   cd ISE-BlackJack
   ```
3. **Avvia il gioco utilizzando uno degli agenti intelligenti**:
   ```bash
    ./gradlew runsmartPlayerMAS - Esegue il MAS smartPlayer.mas2j
    ./gradlew runStrategistMAS - Esegue il MAS Strategist.mas2j
    ./gradlew runwaySmarterPlayerMAS - Esegue il MAS waySmarterPlayer.mas2j
   ```

## Licenza

Questo progetto è distribuito sotto la licenza MIT. Vedi il file [LICENSE](https://github.com/mega2799/ISE-BlackJack/blob/main/LICENSE) per dettagli.

