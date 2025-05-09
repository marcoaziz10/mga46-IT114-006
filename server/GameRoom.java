// start edits - mga46
package server;

import java.util.*;
import common.*;

public class GameRoom extends Room {
    private Map<ServerThread, String> playerChoices = new HashMap<>();
    private Map<ServerThread, Integer> playerPoints = new HashMap<>();
    private Set<ServerThread> readyPlayers = new HashSet<>();
    private boolean roundActive = false;

    public GameRoom(String name) {
        super(name);
    }

    public synchronized void setPlayerReady(ServerThread client) {
        readyPlayers.add(client);
        broadcastPayload(new Payload(PayloadType.MESSAGE, "SERVER", client.getClientName() + " is ready!"));

        if (readyPlayers.size() >= 2 && !roundActive) {
            startRound();
        }
    }

    public synchronized void playerPick(ServerThread client, String choice) {
        if (roundActive && readyPlayers.contains(client)) {
            playerChoices.put(client, choice);
            client.sendPayload(new Payload(PayloadType.MESSAGE, "SERVER", "You picked: " + choice));
        } else {
            client.sendPayload(new Payload(PayloadType.MESSAGE, "SERVER", "You cannot pick yet or you are not ready."));
        }
    }

    private synchronized void startRound() {
        roundActive = true;
        broadcastPayload(new Payload(PayloadType.MESSAGE, "SERVER", "New round started!"));

        for (ServerThread player : readyPlayers) {
            playerChoices.put(player, null);
        }

        new Thread(() -> {
            int secondsLeft = 15;
            while (secondsLeft >= 0) {
                broadcastPayload(new Payload(PayloadType.TIMER, "SERVER", String.valueOf(secondsLeft)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
                secondsLeft--;
            }
            endRound();
        }).start();
    }

    private synchronized void endRound() {
        roundActive = false;
        broadcastPayload(new Payload(PayloadType.MESSAGE, "SERVER", "Round ended!"));

        eliminateIdlePlayers();
        evaluateRoundResults();

        playerChoices.clear();

        if (readyPlayers.size() <= 1) {
            declareWinner();
            onSessionEnd();
        } else {
            startRound();
        }
    }

    private void eliminateIdlePlayers() {
        Set<ServerThread> toRemove = new HashSet<>();
        for (ServerThread player : readyPlayers) {
            if (playerChoices.get(player) == null) {
                toRemove.add(player);
                broadcastPayload(new Payload(PayloadType.MESSAGE, "SERVER", player.getClientName() + " was eliminated for not picking!"));
            }
        }
        readyPlayers.removeAll(toRemove);
    }

    private void evaluateRoundResults() {
        List<ServerThread> players = new ArrayList<>(readyPlayers);
        boolean anyWin = false;

        for (int i = 0; i < players.size(); i++) {
            ServerThread p1 = players.get(i);
            String c1 = playerChoices.get(p1);
            for (int j = i + 1; j < players.size(); j++) {
                ServerThread p2 = players.get(j);
                String c2 = playerChoices.get(p2);

                if (c1 == null || c2 == null) continue;

                if (c1.equals(c2)) {
                    broadcastPayload(new Payload(PayloadType.MESSAGE, "SERVER", "Tie! Both picked " + c1));
                } else if (beats(c1, c2)) {
                    incrementScore(p1);
                    anyWin = true;
                    broadcastPayload(new Payload(PayloadType.MESSAGE, "SERVER", capitalize(c1) + " beats " + c2 + ", " + p1.getClientName() + " wins this round!"));
                } else if (beats(c2, c1)) {
                    incrementScore(p2);
                    anyWin = true;
                    broadcastPayload(new Payload(PayloadType.MESSAGE, "SERVER", capitalize(c2) + " beats " + c1 + ", " + p2.getClientName() + " wins this round!"));
                }
            }
        }

        for (ServerThread player : readyPlayers) {
            int points = playerPoints.getOrDefault(player, 0);
            PointsPayload pp = new PointsPayload(
                player.getClientName(),
                "You have " + points + " point(s).",
                points,
                PlayerStatus.ACTIVE
            );
            player.sendPayload(pp);
        }

        if (!anyWin) {
            broadcastPayload(new Payload(PayloadType.MESSAGE, "SERVER", "TIE game, no winner."));
        }
    }

    private boolean beats(String a, String b) {
        return switch (a) {
            case "rock" -> b.equals("scissors") || b.equals("lizard");
            case "paper" -> b.equals("rock") || b.equals("spock");
            case "scissors" -> b.equals("paper") || b.equals("lizard");
            case "lizard" -> b.equals("spock") || b.equals("paper");
            case "spock" -> b.equals("scissors") || b.equals("rock");
            default -> false;
        };
    }

    private void incrementScore(ServerThread player) {
        playerPoints.put(player, playerPoints.getOrDefault(player, 0) + 1);
    }

    private String capitalize(String word) {
        if (word == null || word.isEmpty()) return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    private void declareWinner() {
        if (readyPlayers.isEmpty()) {
            broadcastPayload(new Payload(PayloadType.MESSAGE, "SERVER", "No players left. Game over!"));
        } else {
            ServerThread winner = readyPlayers.iterator().next();
            broadcastPayload(new Payload(PayloadType.MESSAGE, "SERVER", winner.getClientName() + " wins the game!"));
        }
    }

    public synchronized void onSessionEnd() {
        broadcastPayload(new Payload(PayloadType.MESSAGE, "SERVER", "The session has ended."));
        readyPlayers.clear();
        playerChoices.clear();
        playerPoints.clear();
        roundActive = false;
    }
}
// stop edits - mga46
