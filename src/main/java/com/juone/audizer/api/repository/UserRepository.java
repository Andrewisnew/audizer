package com.juone.audizer.api.repository;

import com.juone.audizer.api.entities.Messenger;
import com.juone.audizer.api.repository.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByMessengerAndMessengerUserId(Messenger messenger, Long messengerUserId);

    @Modifying
    @Query("UPDATE UserModel u SET u.secretKeyword = :secretKeyword WHERE u.id = :id")
    int updateSecretKeyword(@Param("id") long id, @Param("secretKeyword") String secretKeyword);
}
