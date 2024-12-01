package org.example.repository;

import org.example.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Получить все комментарии по ID объявления
    @Query("SELECT c FROM Comment c WHERE c.ad.id = :adId")
    List<Comment> findByAdId(Long adId);

    // Получить комментарии пользователя по ID объявления и ID пользователя
    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId AND c.ad.id = :adId")
    List<Comment> findByUserIdAndAdId(Long userId, Long adId);

    // Получить комментарий по ID объявления и ID комментария
    @Query("SELECT c FROM Comment c WHERE c.ad.id = :adId AND c.id = :commentId")
    Optional<Comment> findByAdIdAndId(Long adId, Long commentId);

}