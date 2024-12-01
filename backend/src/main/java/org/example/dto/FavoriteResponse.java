package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteResponse {
    private Long id;
    private Long adId;
    private Long carId;
}
