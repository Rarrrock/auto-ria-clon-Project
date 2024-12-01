package org.example.service;

import org.example.dto.CommentRequest;
import org.example.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    // Добавляю комментарий
    CommentResponse addComment(CommentRequest request, String email);

    // Получаю все комментарии для объявления
    List<CommentResponse> getCommentsForAd(Long adId);

    // Получить комментарии пользователя для объявления
    List<CommentResponse> getCommentsByUserForAd(Long adId, Long userId);

    // Получить конкретный комментарий по ID и ID объявления
    CommentResponse getCommentForAdById(Long adId, Long commentId);

    // Удаляю комментарий
    void deleteComment(Long commentId, String email);
}