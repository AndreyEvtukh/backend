package com.portfolio.backend.datafetchers;

import com.portfolio.backend.model.User;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserGraphQLQuery {
    private final UserRepository userRepository;

    public UserGraphQLQuery(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> users() {
        return userRepository.findAll();
    }
}
