package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.enums.AdStatus;
import org.example.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный идентификатор

    @Column(nullable = false)
    private String title; // Заголовок объявления

    @Column(length = 1000, nullable = false)
    private String description; // Описание объявления

    @Column(nullable = false)
    private BigDecimal price; // Цена

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency; // Валюта

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdStatus status = AdStatus.UNDER_REVIEW; // Статус (по умолчанию "UNDER_REVIEW")

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // Дата создания

    @Column(nullable = false)
    private LocalDateTime updatedAt; // Дата обновления

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore // Исключаю сериализацию владельца для предотвращения ошибки
    private User owner; // Владелец объявления

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites;
}