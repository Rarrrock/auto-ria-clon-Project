package org.example.repository;

import org.example.entity.Ad;
import org.example.entity.Car;
import org.example.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("SELECT COUNT(f) > 0 FROM Favorite f WHERE f.user.id = :userId AND f.car = :car")
    boolean existsByUserAndCar(Long userId, Car car);

    @Query("SELECT COUNT(f) > 0 FROM Favorite f WHERE f.user.id = :userId AND f.ad = :ad")
    boolean existsByUserAndAd(Long userId, Ad ad);

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.car = :car")
    Optional<Favorite> findByUserAndCar(Long userId, Car car);

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.ad = :ad")
    Optional<Favorite> findByUserAndAd(Long userId, Ad ad);

    @Query("SELECT f.car FROM Favorite f WHERE f.user.id = :userId")
    List<Car> findAllCarsByUser(Long userId);

    @Query("SELECT f.ad FROM Favorite f WHERE f.user.id = :userId")
    List<Ad> findAllAdsByUser(Long userId);

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.car.id = :carId")
    Optional<Favorite> findByUserAndCarId(Long userId, Long carId);

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.ad.id = :adId")
    Optional<Favorite> findByUserAndAdId(Long userId, Long adId);

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId")
    List<Favorite> findAllByUserId(@Param("userId") Long userId);
}