package ua.zavgod.springcourse.FirstRestApp.util;

public class SensorNotCreatedException extends RuntimeException{
    public SensorNotCreatedException(String message) {
        super(message);
    }
}
