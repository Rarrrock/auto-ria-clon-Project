package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.AdResponse;
import org.example.dto.CarResponse;
import org.example.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // Добавляю в Избранное
    @PostMapping("/cars/{carId}")
    public ResponseEntity<String> addCarToFavorites(@PathVariable Long carId, Authentication authentication) {
        favoriteService.addCarToFavorites(carId, authentication.getName());
        return ResponseEntity.ok("Машина добавлена в избранное");
    }

    @PostMapping("/ads/{adId}")
    public ResponseEntity<String> addAdToFavorites(@PathVariable Long adId, Authentication authentication) {
        favoriteService.addAdToFavorites(adId, authentication.getName());
        return ResponseEntity.ok("Объявление добавлено в избранное");
    }

    // Получаю избранные машины
    @GetMapping("/cars")
    public ResponseEntity<List<CarResponse>> getFavoriteCars(Authentication authentication) {
        List<CarResponse> favoriteCars = favoriteService.getFavoriteCars(authentication.getName());
        return ResponseEntity.ok(favoriteCars);
    }

    // Получаю избранные объявления
    @GetMapping("/ads")
    public ResponseEntity<List<AdResponse>> getFavoriteAds(Authentication authentication) {
        List<AdResponse> favoriteAds = favoriteService.getFavoriteAds(authentication.getName());
        return ResponseEntity.ok(favoriteAds);
    }

    // Получаю машину по ID
    @GetMapping("/cars/{carId}")
    public ResponseEntity<CarResponse> getFavoriteCarById(@PathVariable Long carId, Authentication authentication) {
        CarResponse car = favoriteService.getFavoriteCarById(carId, authentication.getName());
        return ResponseEntity.ok(car);
    }

    // Получаю объявление по ID
    @GetMapping("/ads/{adId}")
    public ResponseEntity<AdResponse> getFavoriteAdById(@PathVariable Long adId, Authentication authentication) {
        AdResponse ad = favoriteService.getFavoriteAdById(adId, authentication.getName());
        return ResponseEntity.ok(ad);
    }

    // Удаляю машину из избранного
    @DeleteMapping("/cars/{carId}")
    public ResponseEntity<String> removeCarFromFavorites(@PathVariable Long carId, Authentication authentication) {
        favoriteService.removeCarFromFavorites(carId, authentication.getName());
        return ResponseEntity.ok("Машина удалена из избранного");
    }

    // Удаляю объявление из избранного
    @DeleteMapping("/ads/{adId}")
    public ResponseEntity<String> removeAdFromFavorites(@PathVariable Long adId, Authentication authentication) {
        favoriteService.removeAdFromFavorites(adId, authentication.getName());
        return ResponseEntity.ok("Объявление удалено из избранного");
    }
}