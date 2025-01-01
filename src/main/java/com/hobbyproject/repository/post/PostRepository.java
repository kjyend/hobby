package com.hobbyproject.repository.post;

import com.hobbyproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>,PostRepositoryCustom {
}
