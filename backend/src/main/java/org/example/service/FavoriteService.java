package org.example.service;

import org.example.dto.AdResponse;
import org.example.dto.CarResponse;

import java.util.List;

// Интерфейс для работы с избранным
public interface FavoriteService {
    void addCarToFavorites(Long carId, String email); // Добавляю машину в избранное
    void addAdToFavorites(Long adId, String email); // Добавляю объявление в избранное

    List<CarResponse> getFavoriteCars(String email); // Получаю список избранных машин
    List<AdResponse> getFavoriteAds(String email); // Получаю список избранных объявлений

    CarResponse getFavoriteCarById(Long carId, String email); // Получаю избранную машину по ID
    AdResponse getFavoriteAdById(Long adId, String email); // Получаю избранное объявление по ID

    void removeCarFromFavorites(Long carId, String email); // Удаляю машину из избранного
    void removeAdFromFavorites(Long adId, String email); // Удаляю объявление из избранного
}