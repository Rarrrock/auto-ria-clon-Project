package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.CommentRequest;
import org.example.dto.CommentResponse;
import org.example.entity.Ad;
import org.example.entity.Comment;
import org.example.entity.User;
import org.example.repository.CommentRepository;
import org.example.repository.AdRepository;
import org.example.repository.UserRepository;
import org.example.service.CommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;

    // Добавить комментарий
    @Override
    public CommentResponse addComment(CommentRequest request, String email) {
        System.out.println("Запрос на добавление комментария: " + request + ", email: " + email);

        // Ищем пользователя по email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким email не найден"));

        System.out.println("Пользователь найден: " + user);

        // Ищем объявление по ID
        Ad ad = adRepository.findById(request.getAdId())
                .orElseThrow(() -> new IllegalArgumentException("Объявление с таким ID не найдено"));

        System.out.println("Объявление найдено: " + ad);

        // Создаем новый комментарий
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setAd(ad);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        // Сохраняем комментарий в базе
        Comment savedComment = commentRepository.save(comment);

        System.out.println("Комментарий успешно сохранен: " + savedComment);

        // Возвращаем ответ в формате CommentResponse
        return new CommentResponse(savedComment.getId(), savedComment.getText(), user.getEmail(), savedComment.getCreatedAt());
    }

    // Получить все комментарии для объявления
    @Override
    public List<CommentResponse> getCommentsForAd(Long adId) {
        // Получаем список комментариев для объявления
        List<Comment> comments = commentRepository.findByAdId(adId);

        // Преобразуем комментарии в CommentResponse
        return comments.stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getText(),
                        comment.getUser().getEmail(), comment.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // Получить комментарии пользователя для объявления
    @Override
    public List<CommentResponse> getCommentsByUserForAd(Long adId, Long userId) {
        List<Comment> comments = commentRepository.findByUserIdAndAdId(userId, adId);
        return comments.stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getText(),
                        comment.getUser().getEmail(), comment.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // Получить конкретный комментарий по ID и ID объявления
    @Override
    public CommentResponse getCommentForAdById(Long adId, Long commentId) {
        Comment comment = commentRepository.findByAdIdAndId(adId, commentId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий с таким ID не найден"));
        return new CommentResponse(comment.getId(), comment.getText(),
                comment.getUser().getEmail(), comment.getCreatedAt());
    }

    // Удалить комментарий
    @Override
    public void deleteComment(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий с таким ID не найден."));

        User owner = comment.getUser();
        if (!userRepository.isCurrentUserOrAdmin(owner.getEmail())) {
            throw new SecurityException("Вы не можете удалить этот комментарий.");
        }

        commentRepository.delete(comment);
    }
}