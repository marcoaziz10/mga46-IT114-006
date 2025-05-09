package common;

public class Payload {
    private String clientId;
    private String message;
    private String type;

    public Payload(String clientId, String message, String type) {
        this.clientId = clientId;
        this.message = message;
        this.type = type;
    }

    public String getClientId() {
        return clientId;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[Payload] clientId=" + clientId + ", message=" + message + ", type=" + type;
    }
}
