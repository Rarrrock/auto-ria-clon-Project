package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CommentRequest;
import org.example.dto.CommentResponse;
import org.example.entity.Comment;
import org.example.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // Добавляю комментарий к объявлению
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest request, Authentication authentication) {
        CommentResponse commentResponse = commentService.addComment(request, authentication.getName());
        return ResponseEntity.ok(commentResponse);
    }

    // Получаю все комментарии к объявлению
    @GetMapping("/ad/{adId}")
    public ResponseEntity<List<CommentResponse>> getCommentsForAd(@PathVariable Long adId) {
        List<CommentResponse> comments = commentService.getCommentsForAd(adId);
        return ResponseEntity.ok(comments);
    }

    // Получаю все комментарии от пользователя для объявления
    @GetMapping("/ad/{adId}/user/{userId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByUserForAd(@PathVariable Long adId, @PathVariable Long userId) {
        List<CommentResponse> comments = commentService.getCommentsByUserForAd(adId, userId);
        return ResponseEntity.ok(comments);
    }

    // Получаю конкретный комментарий по ID и ID объявления
    @GetMapping("/ad/{adId}/comment/{commentId}")
    public ResponseEntity<CommentResponse> getCommentForAdById(@PathVariable Long adId, @PathVariable Long commentId) {
        CommentResponse comment = commentService.getCommentForAdById(adId, commentId);
        return ResponseEntity.ok(comment);
    }

    // Удаляю комментарий
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, Authentication authentication) {
        commentService.deleteComment(commentId, authentication.getName());
        return ResponseEntity.ok("Комментарий удален.");
    }
}