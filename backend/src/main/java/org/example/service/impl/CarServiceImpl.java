package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.CarRequest;
import org.example.dto.CarResponse;
import org.example.dto.UserResponse;
import org.example.entity.Car;
import org.example.enums.RoleEnum;
import org.example.entity.User;
import org.example.repository.CarRepository;
import org.example.service.CarService;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final UserService userService;

    // Получаю список всех Машин
    @Override
    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Добавляю новую Машину
    @Override
    public CarResponse createCar(CarRequest carRequest, Long ownerId) {
        Car car = new Car();
        car.setModel(carRequest.getModel());
        car.setEnginePower(carRequest.getEnginePower());
        car.setTorque(carRequest.getTorque());
        car.setLastMaintenanceTimestamp(carRequest.getLastMaintenanceTimestamp());

        if (ownerId != null) {
            UserResponse userResponse = userService.getUserById(ownerId);
            car.setOwner(mapToUser(userResponse));
        } else {
            car.setOwner(null);
        }

        return mapToResponse(carRepository.save(car));
    }

    // Получаю Машину по ID
    @Override
    public CarResponse getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Машина с ID " + id + " не найдена."));
        return mapToResponse(car);
    }

    // Обновляю данные Машины
    @Override
    public CarResponse updateCar(Long id, CarRequest carRequest) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Машина с ID " + id + " не найдена."));

        // Проверяю права пользователя
        if (!userService.isCurrentUserOrAdmin(car.getOwner().getEmail())) {
            throw new SecurityException("Вы не можете редактировать данные этой машины.");
        }

        car.setModel(carRequest.getModel());
        car.setEnginePower(carRequest.getEnginePower());
        car.setTorque(carRequest.getTorque());
        car.setLastMaintenanceTimestamp(carRequest.getLastMaintenanceTimestamp());
        return mapToResponse(carRepository.save(car));
    }

    // Удаляю Машину по ID
    @Override
    public void deleteCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Машина с ID " + id + " не найдена."));

        // Проверяю права пользователя
        if (!userService.isCurrentUserOrAdmin(car.getOwner().getEmail())) {
            throw new SecurityException("Вы не можете удалить эту машину.");
        }

        carRepository.delete(car);
    }

    // Преобразую Car в CarResponse
    private CarResponse mapToResponse(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .model(car.getModel())
                .enginePower(car.getEnginePower())
                .torque(car.getTorque())
                .ownerEmail(car.getOwner() != null ? car.getOwner().getEmail() : null)
                .lastMaintenanceTimestamp(car.getLastMaintenanceTimestamp())
                .build();
    }

    // Преобразую UserResponse в User
    private User mapToUser(UserResponse userResponse) {
        User user = new User();
        user.setId(userResponse.getId());
        user.setUsername(userResponse.getUsername());
        user.setEmail(userResponse.getEmail());
        user.setRole(userResponse.getRole());
        return user;
    }

    // Выполняю запрос с фильтрацией
    @Override
    public List<CarResponse> filterCars(Integer minEnginePower, Integer maxEnginePower, Long ownerId, Long minTimestamp, Long maxTimestamp) {
        // Вызов фильтрации через репозиторий
        List<Car> cars = carRepository.findFilteredCars(minEnginePower, maxEnginePower, ownerId, minTimestamp, maxTimestamp);

        // Преобразование результатов в DTO
        return cars.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}