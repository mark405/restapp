package ua.zavgod.springcourse.FirstRestApp.util;

public class MeasurementErrorResponse {
    private String message;
    private long localDateTime;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(long localDateTime) {
        this.localDateTime = localDateTime;
    }

    public MeasurementErrorResponse(String message, long localDateTime) {
        this.message = message;
        this.localDateTime = localDateTime;
    }
}
