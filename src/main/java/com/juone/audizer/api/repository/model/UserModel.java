package com.juone.audizer.api.repository.model;

import com.juone.audizer.api.entities.Messenger;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementDomain")
    @GenericGenerator(name = "incrementDomain", strategy = "increment")
    private Long id;

    @Column(name = "messenger")
    @Enumerated(EnumType.STRING)
    private Messenger messenger;

    @Column(name = "messenger_user_id")
    private Long messengerUserId;

    @Column(name = "secret_keyword")
    private String secretKeyword;

    public UserModel(Messenger messenger, Long messengerUserId, String secretKeyword) {
        this.messenger = messenger;
        this.messengerUserId = messengerUserId;
    }

    public UserModel() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public void setMessenger(Messenger name) {
        this.messenger = name;
    }

    public Long getMessengerUserId() {
        return messengerUserId;
    }

    public void setMessengerUserId(Long email) {
        this.messengerUserId = email;
    }

    public String getSecretKeyword() {
        return secretKeyword;
    }

    public void setSecretKeyword(String secretKeyword) {
        this.secretKeyword = secretKeyword;
    }
}
