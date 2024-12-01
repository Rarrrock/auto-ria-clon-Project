package org.example.service;

import org.example.dto.CarRequest;
import org.example.dto.CarResponse;
import org.example.entity.Car;
import org.example.entity.User;

import java.util.List;

// Основные методы работы с Машинами
public interface CarService {
    List<CarResponse> getAllCars(); // Получаю список всех Машин
    CarResponse createCar(CarRequest carRequest, Long ownerId); // Добавляю новую Машину
    CarResponse getCarById(Long id); // Получаю Машину по ID
    CarResponse updateCar(Long id, CarRequest carRequest); // Обновляю данные Машины
    void deleteCar(Long id); // Удаляю Машину по ID

    // Фильтрую машины по заданным параметрам
    List<CarResponse> filterCars(Integer minEnginePower, Integer maxEnginePower, Long ownerId, Long minTimestamp, Long maxTimestamp);
}
