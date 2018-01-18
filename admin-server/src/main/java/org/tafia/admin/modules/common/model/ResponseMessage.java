package org.tafia.admin.modules.common.model;

public class ResponseMessage {

    private String message;

    private Long timestamp;

    private Object payload;

    public ResponseMessage() {
        this(null);
    }

    public ResponseMessage(String message) {
        this(message, null);
    }

    public ResponseMessage(String message, Object payload) {
        this(message, System.currentTimeMillis(), payload);
    }

    public ResponseMessage(String message, Long timestamp, Object payload) {
        this.message = message;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
