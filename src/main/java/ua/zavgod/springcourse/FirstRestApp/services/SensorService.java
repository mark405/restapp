package ua.zavgod.springcourse.FirstRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.zavgod.springcourse.FirstRestApp.models.Sensor;
import ua.zavgod.springcourse.FirstRestApp.repositories.SensorRepository;
import ua.zavgod.springcourse.FirstRestApp.util.SensorAlreadyExistsException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Optional<Sensor> findOneByName(String name) {
        return sensorRepository.findByName(name);
    }

    @Transactional
    public void save(Sensor sensor) {
        String sensorName = sensor.getName();
        if (sensorRepository.findByName(sensorName).isPresent()) {
            throw new SensorAlreadyExistsException("Sensor with such name already exists { " + sensorName + " }");
        }
        sensorRepository.save(sensor);
    }

    public List<Sensor> findAll() {

        return sensorRepository.findAll();
    }
}
