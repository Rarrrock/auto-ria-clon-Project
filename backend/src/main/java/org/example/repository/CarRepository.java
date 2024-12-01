package org.example.repository;

import org.example.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    // Запрос с фильтрацией машин
    @Query("SELECT c FROM Car c WHERE " +
            "(:minEnginePower IS NULL OR c.enginePower >= :minEnginePower) AND " +
            "(:maxEnginePower IS NULL OR c.enginePower <= :maxEnginePower) AND " +
            "(:ownerId IS NULL OR c.owner.id = :ownerId) AND " +
            "(:minTimestamp IS NULL OR c.lastMaintenanceTimestamp >= :minTimestamp) AND " +
            "(:maxTimestamp IS NULL OR c.lastMaintenanceTimestamp <= :maxTimestamp)")
    List<Car> findFilteredCars(
            @Param("minEnginePower") Integer minEnginePower,
            @Param("maxEnginePower") Integer maxEnginePower,
            @Param("ownerId") Long ownerId,
            @Param("minTimestamp") Long minTimestamp,
            @Param("maxTimestamp") Long maxTimestamp);
}