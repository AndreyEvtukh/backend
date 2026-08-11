package com.portfolio.backend.resolvers;

import com.portfolio.backend.model.User;
import com.portfolio.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserResolver {

    private final UserRepository userRepository;

    @QueryMapping
    public List<User> users() {
        return userRepository.findAll();
    }
}