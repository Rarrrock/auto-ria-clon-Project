package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.AdRequest;
import org.example.dto.AdResponse;
import org.example.service.AdService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    // Создание объявления (только USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<AdResponse> createAd(@RequestBody AdRequest adRequest, Authentication authentication) {
        AdResponse createdAd = adService.createAd(adRequest, authentication.getName());
        return ResponseEntity.ok(createdAd);
    }

    // Получение всех объявлений (доступно всем)
    @GetMapping
    public ResponseEntity<List<AdResponse>> getAllAds() {
        List<AdResponse> ads = adService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    // Получение объявления по ID (доступно всем)
    @GetMapping("/{id}")
    public ResponseEntity<AdResponse> getAdById(@PathVariable Long id) {
        AdResponse ad = adService.getAdById(id);
        return ResponseEntity.ok(ad);
    }

    // Редактирование объявления (USER для своих объявлений, MANAGER и ADMIN для любых)
    @PreAuthorize("hasRole('USER') and @adSecurity.isAdOwner(#id, authentication.name) or hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<AdResponse> updateAd(@PathVariable Long id, @RequestBody AdRequest adRequest) {
        AdResponse updatedAd = adService.updateAd(id, adRequest);
        return ResponseEntity.ok(updatedAd);
    }

    // Удаление объявления (USER для своих объявлений, MANAGER и ADMIN для любых)
    @PreAuthorize("hasRole('USER') and @adSecurity.isAdOwner(#id, authentication.name) or hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    // Фильтрация объявлений (доступно всем)
    @GetMapping("/filter")
    public ResponseEntity<List<AdResponse>> filterAds(@RequestParam(required = false) BigDecimal minPrice,
                                                      @RequestParam(required = false) BigDecimal maxPrice,
                                                      @RequestParam(required = false) String currency,
                                                      @RequestParam(required = false) String status) {
        List<AdResponse> filteredAds = adService.filterAds(minPrice, maxPrice, currency, status);
        return ResponseEntity.ok(filteredAds);
    }
}