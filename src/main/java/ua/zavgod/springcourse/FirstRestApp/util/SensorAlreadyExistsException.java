package ua.zavgod.springcourse.FirstRestApp.util;

public class SensorAlreadyExistsException extends RuntimeException {

    public SensorAlreadyExistsException(String message) {
        super(message);
    }
}
