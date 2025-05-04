package server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameRoom extends Room {
    private Set<ServerThread> readyPlayers = new HashSet<>();
    private Map<ServerThread, String> playerChoices = new HashMap<>();
    private Map<ServerThread, Integer> playerPoints = new HashMap<>();
    private boolean roundActive = false;

    public GameRoom(String name) {
        super(name);
    }

    public synchronized void setPlayerReady(ServerThread client) {
        readyPlayers.add(client);
        broadcast("[SERVER] " + client.getClientName() + " is ready!");
        
        if (readyPlayers.size() >= 2 && !roundActive) {
            startRound();
        }
    }

    public synchronized void playerPick(ServerThread client, String choice) {
        if (roundActive && readyPlayers.contains(client)) {
            playerChoices.put(client, choice);
            client.send("[SERVER] You picked: " + choice);
        } else {
            client.send("[SERVER] You cannot pick yet or you are not ready.");
        }
    }

    public synchronized void startRound() {
        roundActive = true;
        broadcast("[SERVER] New round started! Please pick rock (r), paper (p), or scissors (s)!");

        for (ServerThread player : readyPlayers) {
            playerChoices.put(player, null); // Reset choices
        }

        new Thread(() -> {
            try {
                Thread.sleep(20000); // 20 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            endRound();
        }).start();
    }

    public synchronized void endRound() {
        broadcast("[SERVER] Round ended! Calculating results...");
        eliminatePlayersWithoutPick();
        awardPoints();

        playerChoices.clear();
        roundActive = false;

        if (readyPlayers.size() <= 1) {
            declareWinner();
            onSessionEnd();
        } else {
            startRound();
        }
    }

    private synchronized void eliminatePlayersWithoutPick() {
        Set<ServerThread> eliminated = new HashSet<>();
        for (ServerThread player : readyPlayers) {
            if (playerChoices.get(player) == null) {
                eliminated.add(player);
            }
        }
        for (ServerThread player : eliminated) {
            readyPlayers.remove(player);
            broadcast("[SERVER] " + player.getClientName() + " was eliminated for not picking!");
        }
    }

    private synchronized void awardPoints() {
        for (ServerThread player : readyPlayers) {
            String choice = playerChoices.get(player);
            for (ServerThread opponent : readyPlayers) {
                if (player == opponent) continue;
                String opponentChoice = playerChoices.get(opponent);
                if (beats(choice, opponentChoice)) {
                    playerPoints.put(player, playerPoints.getOrDefault(player, 0) + 1);
                }
            }
        }

        for (ServerThread player : readyPlayers) {
            int points = playerPoints.getOrDefault(player, 0);
            player.send("[SERVER] You have " + points + " point(s).");
        }
    }

    private boolean beats(String choice1, String choice2) {
        if (choice1 == null || choice2 == null) return false;
        switch (choice1) {
            case "r": return choice2.equals("s");
            case "p": return choice2.equals("r");
            case "s": return choice2.equals("p");
            default: return false;
        }
    }

    private synchronized void declareWinner() {
        if (readyPlayers.isEmpty()) {
            broadcast("[SERVER] No players left. Game over!");
        } else {
            ServerThread winner = readyPlayers.iterator().next();
            broadcast("[SERVER] " + winner.getClientName() + " wins the game!");
        }
    }

    public synchronized void onSessionEnd() {
        broadcast("[SERVER] The session has ended.");
        readyPlayers.clear();
        playerChoices.clear();
        playerPoints.clear();
        roundActive = false;
    }
}
