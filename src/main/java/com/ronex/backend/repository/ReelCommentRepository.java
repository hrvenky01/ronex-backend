package com.ronex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ronex.backend.model.ReelComment;
import java.util.List;

@Repository
public interface ReelCommentRepository extends JpaRepository<ReelComment, Long> {

    List<ReelComment> findByReelId(Long reelId);
}