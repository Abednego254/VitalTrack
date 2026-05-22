package app.rest;

import java.io.Serializable;

public class ResponseStatus implements Serializable {

    private SuccessError status = SuccessError.SUCCESS;
    private String message = "OK";

    public ResponseStatus() {}

    public ResponseStatus(SuccessError status, String message) {
        this.status = status;
        this.message = message;
    }

    public SuccessError getStatus() {
        return status;
    }

    public void setStatus(SuccessError status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
