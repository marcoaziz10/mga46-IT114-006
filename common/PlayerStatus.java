// start edits - mga46
package common;

public enum PlayerStatus {
    ACTIVE,
    PENDING,
    ELIMINATED,
    AWAY,
    SPECTATOR;

    public static PlayerStatus fromString(String status) {
        try {
            return PlayerStatus.valueOf(status.trim().toUpperCase());
        } catch (Exception e) {
            return ACTIVE;
        }
    }
}
// stop edits - mga46
