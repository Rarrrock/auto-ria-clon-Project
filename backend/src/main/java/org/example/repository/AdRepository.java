package org.example.repository;

import org.example.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    @Query("SELECT a FROM Ad a WHERE " +
            "(:minPrice IS NULL OR a.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR a.price <= :maxPrice) AND " +
            "(:currency IS NULL OR a.currency = :currency) AND " +
            "(:status IS NULL OR a.status = :status)")
    List<Ad> findFilteredAds(@Param("minPrice") BigDecimal minPrice,
                             @Param("maxPrice") BigDecimal maxPrice,
                             @Param("currency") String currency,
                             @Param("status") String status);

    // TODO: Использую позже
    // Возвращает список всех объявлений пользователя по его имейлу
    @Query("SELECT a FROM Ad a WHERE a.owner.email = :email")
    List<Ad> findByOwnerEmail(@Param("email") String email);

    @Query("SELECT COUNT(a) FROM Ad a WHERE a.owner.id = :ownerId")
    long countByOwnerId(@Param("ownerId") Long ownerId);
}