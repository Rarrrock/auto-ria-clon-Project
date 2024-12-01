package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.AdRequest;
import org.example.dto.AdResponse;
import org.example.dto.CarResponse;
import org.example.entity.Ad;
import org.example.entity.Car;
import org.example.entity.User;
import org.example.repository.AdRepository;
import org.example.repository.UserRepository;
import org.example.service.AdService;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    // Создаю Объявление
    @Override
    public AdResponse createAd(AdRequest adRequest, String email) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден."));

        // TODO: Добавить логику ограничения количества объявлений для базового аккаунта
        Ad ad = new Ad();
        ad.setTitle(adRequest.getTitle());
        ad.setDescription(adRequest.getDescription());
        ad.setPrice(adRequest.getPrice());
        ad.setCurrency(adRequest.getCurrency());
        ad.setOwner(owner);

        Ad savedAd = adRepository.save(ad);
        return mapToResponse(savedAd);
    }

    // Получаю все Объявления
    @Override
    public List<AdResponse> getAllAds() {
        return adRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Получаю Объявление
    @Override
    public AdResponse getAdById(Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + id + " не найдено."));
        return mapToResponse(ad);
    }

    // Переписываю Объявление
    @Override
    public AdResponse updateAd(Long id, AdRequest adRequest) {
        Ad existingAd = adRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + id + " не найдено."));

        User owner = existingAd.getOwner();

        // Проверка прав доступа
        if (!userService.isCurrentUserOrAdmin(owner.getEmail(), owner.getId())) {
            throw new SecurityException("Недостаточно прав для выполнения операции.");
        }

        // Обновляем данные объявления
        existingAd.setTitle(adRequest.getTitle());
        existingAd.setDescription(adRequest.getDescription());
        existingAd.setPrice(adRequest.getPrice());
        existingAd.setCurrency(adRequest.getCurrency());

        Ad updatedAd = adRepository.save(existingAd);
        return mapToResponse(updatedAd);
    }

    // Удаляю Объявление
    @Override
    public void deleteAd(Long id) {
        Ad existingAd = adRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + id + " не найдено."));

        User owner = existingAd.getOwner();

        // Проверка прав доступа
        if (!userService.isCurrentUserOrAdmin(owner.getEmail(), owner.getId())) {
            throw new SecurityException("Недостаточно прав для выполнения операции.");
        }

        adRepository.delete(existingAd);
    }

    // Преобразую Ad в AdResponse
    private AdResponse mapToResponse(Ad ad) {
        return AdResponse.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .description(ad.getDescription())
                .price(ad.getPrice())
                .currency(ad.getCurrency())
                .status(ad.getStatus())
                .createdAt(ad.getCreatedAt())
                .updatedAt(ad.getUpdatedAt())
                .ownerEmail(ad.getOwner() != null ? ad.getOwner().getEmail() : null)
                .build();
    }

    // Выполняю запрос с фильтрацией
    @Override
    public List<AdResponse> filterAds(BigDecimal minPrice, BigDecimal maxPrice, String currency, String status) {
        // Вызов фильтрации через репозиторий
        List<Ad> ads = adRepository.findFilteredAds(minPrice, maxPrice, currency, status);

        // Преобразование результатов в DTO
        return ads.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}