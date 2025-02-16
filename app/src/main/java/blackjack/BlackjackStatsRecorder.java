package blackjack;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BlackjackStatsRecorder {
    private final String filePath;
    private int gameWon;
    private int gameLost;
    private int gameTied;
    private double totalMoney;

    public BlackjackStatsRecorder(final String filePath) {
        this.filePath = filePath;
        this.loadLastStats();
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

    private void loadLastStats() {
        if (!Files.exists(Paths.get(this.filePath))) {
            this.gameWon = 0;
            this.gameLost = 0;
            this.gameTied = 0;
            this.totalMoney = 1000.0;
            return;
        }
        
        try {
            final List<String> lines = Files.readAllLines(Paths.get(this.filePath));
            if (lines.size() > 1) { // Check if there are records beyond headers
                final String lastLine = lines.get(lines.size() - 1);
                final String[] values = lastLine.split(",");
				if (values.length == 4) {
					this.gameWon = Integer.parseInt(values[0]);
					this.gameLost = Integer.parseInt(values[1]);
					this.gameTied = Integer.parseInt(values[2]);
					this.totalMoney = Double.parseDouble(values[3]);
				}

			System.out.println("Loaded stats from file: " + this.filePath);
			System.out.println("Wins: " + this.gameWon + " Losses: " + this.gameLost + " Ties: " + this.gameTied + " Money: " + this.totalMoney);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
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

