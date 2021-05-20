package com.juone.audizer.api.repository;

import com.juone.audizer.api.repository.model.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface MessageRepository extends JpaRepository<MessageModel, Long> {
    List<MessageModel> findAllByMessageIdInAndUserId(List<Long> messageIds, long userId);

    Long deleteByUserId(long userId);
}
