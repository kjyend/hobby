package com.hobbyproject.repository;

import com.hobbyproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long>,CommentRepositoryCustom {
}
