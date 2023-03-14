package ua.zavgod.springcourse.FirstRestApp.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SensorDTO {
    @NotNull(message = "Name should not be null")
    @Size(min = 3, max = 30, message = "Name should be between 3 and 30")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
