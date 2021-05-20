package com.juone.audizer.impl.services;

import com.juone.audizer.api.entities.Messenger;
import com.juone.audizer.api.entities.MessengerUser;
import com.juone.audizer.api.entities.User;
import com.juone.audizer.api.entities.ValidationData;
import com.juone.audizer.api.repository.UserRepository;
import com.juone.audizer.api.repository.model.UserModel;
import com.juone.audizer.api.services.EncrypterService;
import com.juone.audizer.api.services.UserService;
import com.juone.audizer.vkimpl.controllers.validators.VkUserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VkUserValidator vkUserValidator;

    @Autowired
    private EncrypterService encrypterService;

    @Override
    public boolean validate(@Nonnull MessengerUser messengerUser) {
        Messenger messenger = messengerUser.getMessenger();
        switch (messenger) {
            case VK:
                return vkUserValidator.validate(new ValidationData(messengerUser.getId(), messengerUser.getToken()));
            default:
                log.error("Unexpected messenger {}", messenger);
                return false;
        }
    }

    @Override
    public boolean sameSecretKeyword(@Nonnull User user) {
        Optional<UserModel> userModel = userRepository.findById(user.getId());
        if (!userModel.isPresent()) {
            throw new IllegalStateException("User (id: " + user.getId() + ") is not in system");
        }
        return encrypterService.encryptHash(user.getSecretKeyword()).equals(userModel.get().getSecretKeyword());
    }

    @Override
    public User resolve(@Nonnull MessengerUser messengerUser) {
        Optional<UserModel> userModel = userRepository.findByMessengerAndMessengerUserId(messengerUser.getMessenger(), messengerUser.getId());
        if (userModel.isPresent()) {
            return new User(userModel.get().getId(), messengerUser.getSecretKeyword());
        } else {
            String encryptedSecretKeyword = encrypterService.encryptHash(messengerUser.getSecretKeyword());
            UserModel model = userRepository.save(new UserModel(messengerUser.getMessenger(), messengerUser.getId(), encryptedSecretKeyword));
            return new User(model.getId(), messengerUser.getSecretKeyword());
        }
    }

    @Override
    public void updateSecretKeyword(@Nonnull User user) {
        String encryptedSecretKeyword = encrypterService.encryptHash(user.getSecretKeyword());
        userRepository.updateSecretKeyword(user.getId(), encryptedSecretKeyword);
    }
}
