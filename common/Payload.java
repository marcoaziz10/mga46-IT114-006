// start edits - mga46
package common;

import java.io.Serializable;

public class Payload implements Serializable {
    private PayloadType type;
    private String clientId;
    private String message;

    public Payload(PayloadType type, String clientId, String message) {
        this.type = type;
        this.clientId = clientId;
        this.message = message;
    }

    public PayloadType getPayloadType() {
        return type;
    }

    public void setPayloadType(PayloadType type) {
        this.type = type;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String id) {
        this.clientId = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }
}
// stop edits - mga46
