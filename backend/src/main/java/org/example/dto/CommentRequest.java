package org.example.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private String text;
    private Long adId;
}