package com.hobbyproject.repository.notificationlog;

import com.hobbyproject.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    void deleteByCreatedAtBefore(LocalDateTime dateTime);
    List<NotificationLog> findTop6ByUserIdOrderByCreatedAtDesc(String userId);
}
