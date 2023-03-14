package ua.zavgod.springcourse.FirstRestApp.util;

public class NoSensorWithSuchNameException extends RuntimeException{

    public NoSensorWithSuchNameException(String message) {
        super(message);
    }
}
