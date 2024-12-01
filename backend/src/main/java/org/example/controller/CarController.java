package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CarRequest;
import org.example.dto.CarResponse;
import org.example.service.CarService;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final UserService userService;

    // Получаю список всех Машин
    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    // Добавляю новую Машину
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CarResponse> createCar(@RequestBody CarRequest carRequest, Authentication authentication) {
        Long ownerId = carRequest.getOwnerId(); // ID владельца
        CarResponse createdCar = carService.createCar(carRequest, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
    }

    // Получаю машину по ID
    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    // Обновляю данные Машины
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long id, @RequestBody CarRequest carRequest) {
        CarResponse updatedCar = carService.updateCar(id, carRequest);
        return ResponseEntity.ok(updatedCar);
    }

    // Удаляю Машину по ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    // Получаю машины с фильтрацией
    @GetMapping("/filter")
    public ResponseEntity<List<CarResponse>> filterCars(
            @RequestParam(required = false) Integer minEnginePower,
            @RequestParam(required = false) Integer maxEnginePower,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Long minTimestamp,
            @RequestParam(required = false) Long maxTimestamp) {

        // Вызываю метод фильтрации из CarService
        List<CarResponse> filteredCars = carService.filterCars(minEnginePower, maxEnginePower, ownerId, minTimestamp, maxTimestamp);
        return ResponseEntity.ok(filteredCars);
    }
}