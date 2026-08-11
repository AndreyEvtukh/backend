package com.portfolio.backend.graphql;

import com.portfolio.backend.model.User;
import com.portfolio.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserGraphQLController {

    private final UserRepository userRepository;

    /**
     * Обрабатывает GraphQL-запрос:
     *   query { test { id email name createdAt } }
     * Доступен только с валидным JWT (см. SecurityConfig).
     */
    @QueryMapping
    public List<User> test() {
        return userRepository.findAll();
    }
}
