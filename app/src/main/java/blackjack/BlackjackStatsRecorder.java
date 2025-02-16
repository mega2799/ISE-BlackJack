package blackjack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BlackjackStatsRecorder {
    private final String filePath;
    private int gameWon;
    private int gameLost;
    private int gameTied;
    private double totalMoney;

    public BlackjackStatsRecorder(final String filePath, final double totalMoney) {
        this.filePath = filePath;
        this.gameWon = 0;
        this.gameLost = 0;
        this.gameTied = 0;
        this.totalMoney = totalMoney;
		 // Creazione della directory se non esiste
    final File directory = new File(filePath).getParentFile();
    if (directory != null && !directory.exists()) {
        directory.mkdirs();
    }
        this.initializeCSV();
    }

    private void initializeCSV() {
        if (!Files.exists(Paths.get(this.filePath))) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.filePath))) {
                writer.write("Wins,Losses,Ties,MoneyWonLost\n");
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void recordWin(final double money) {
        this.gameWon += 1;
        this.totalMoney += money;
        this.writeStatsToCSV();
    }

    public void recordLoss(final double money) {
        this.gameLost += 1;
        this.totalMoney -= money;
        this.writeStatsToCSV();
    }

    public void recordTie() {
        this.gameTied += 1;
        this.writeStatsToCSV();
    }

    private void writeStatsToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.filePath, true))) {
            writer.write(this.gameWon + "," + this.gameLost + "," + this.gameTied + "," + this.totalMoney + "\n");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}

