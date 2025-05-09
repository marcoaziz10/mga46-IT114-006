package common;

public class PointsPayload extends Payload {
    private int points;

    public PointsPayload(String clientId, String message, String type, int points) {
        super(clientId, message, type);
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "[PointsPayload] clientId=" + getClientId()
            + ", message=" + getMessage()
            + ", type=" + getType()
            + ", points=" + points;
    }
}
