package ru.nonamejack.audioshop.service;

import org.springframework.stereotype.Service;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.model.User;
import ru.nonamejack.audioshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MessageService messageService;

    public UserService(UserRepository userRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageService.getMessage("error.user.not_found", id)
                ));
    }

    public User getCurrentUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new SecurityException(
                        messageService.getMessage("error.user.access_denied")
                ));
    }

}
