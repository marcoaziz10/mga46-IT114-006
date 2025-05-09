// start edits - mga46
package common;

public class PointsPayload extends Payload {
    private int points;
    private PlayerStatus status;

    public PointsPayload(String clientId, String message, int points, PlayerStatus status) {
        super(PayloadType.POINTS, clientId, message);
        this.points = points;
        this.status = status;
    }

    public int getPoints() {
        return points;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "PointsPayload{clientId='" + getClientId() + "', points=" + points + ", status=" + status + "}";
    }
}
// stop edits - mga46
