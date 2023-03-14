package ua.zavgod.springcourse.FirstRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.zavgod.springcourse.FirstRestApp.DTO.MeasurementDTO;
import ua.zavgod.springcourse.FirstRestApp.DTO.MeasurementResponse;
import ua.zavgod.springcourse.FirstRestApp.models.Measurement;
import ua.zavgod.springcourse.FirstRestApp.models.Sensor;
import ua.zavgod.springcourse.FirstRestApp.services.MeasurementService;
import ua.zavgod.springcourse.FirstRestApp.services.SensorService;
import ua.zavgod.springcourse.FirstRestApp.util.MeasurementErrorResponse;
import ua.zavgod.springcourse.FirstRestApp.util.MeasurementNotCreatedException;
import ua.zavgod.springcourse.FirstRestApp.util.NoSensorWithSuchNameException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    private final SensorService sensorService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasurementService measurementService, SensorService sensorService, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorBuilder = new StringBuilder();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> {
                errorBuilder
                        .append(fieldError.getField())
                        .append(" - ")
                        .append(fieldError.getDefaultMessage())
                        .append(";");
            });

            throw new MeasurementNotCreatedException(errorBuilder.toString());
        }

        Optional<Sensor> sensor = sensorService.findOneByName(measurementDTO.getSensor().getName());

        if (sensor.isEmpty()) {
            throw new NoSensorWithSuchNameException("Sensor with name {" + measurementDTO.getSensor().getName() + "} doesn't exist");
        } else {
            Measurement measurement = convertToMeasurement(measurementDTO);
            measurement.getSensor().setId(sensor.get().getId());
            measurementService.save(measurement);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    @GetMapping()
    public MeasurementResponse show() {
        return new MeasurementResponse(measurementService.findAll().stream().map(this::convertToMeasurementDTO).collect(Collectors.toList()));
    }

    @GetMapping("/rainyDaysCount")
    public Integer countRainyDays() {
        return measurementService.countRainyDays();
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(NoSensorWithSuchNameException ex) {
        MeasurementErrorResponse measurementErrorResponse = new MeasurementErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(measurementErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementNotCreatedException ex) {
        MeasurementErrorResponse measurementErrorResponse = new MeasurementErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(measurementErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
