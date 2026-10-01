package com.portfolio.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class GraphQlCookieInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(
            WebGraphQlRequest request,
            Chain chain
    ) {
        return chain.next(request)
                .doOnNext(response -> {

                    var context = response.getExecutionInput()
                            .getGraphQLContext();

                    // LOGIN
                    String token = context.get("access_token");

                    if (token != null) {
                        ResponseCookie cookie = ResponseCookie.from(
                                        "access_token",
                                        token
                                )
                                .httpOnly(true)
                                .secure(false)
                                .path("/")
                                .maxAge(Duration.ofDays(1))
                                .sameSite("Lax")
                                .build();

                        response.getResponseHeaders().add(
                                HttpHeaders.SET_COOKIE,
                                cookie.toString()
                        );
                    }

                    // LOGOUT
                    Boolean logout = context.get("clear_access_token");

                    if (Boolean.TRUE.equals(logout)) {
                        ResponseCookie cookie = ResponseCookie.from(
                                        "access_token",
                                        ""
                                )
                                .httpOnly(true)
                                .secure(false)
                                .path("/")
                                .maxAge(Duration.ZERO)
                                .sameSite("Lax")
                                .build();

                        response
                                .getResponseHeaders()
                                .add(HttpHeaders.SET_COOKIE, cookie.toString()
                                );

                        log.info("=> Clear access token [SUCCESS]");
                    }
                });
    }
}